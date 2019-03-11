/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.iccu.sbn.web.actions.acquisizioni.buoniordine;


import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO.TipoOperazione;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuonoOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.buoniordine.EsaminaBuonoOrdineForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.DynaValidatorForm;

public class EsaminaBuonoOrdineAction extends AcquisizioniBaseAction implements SbnAttivitaChecker {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
        map.put("servizi.bottone.si", "si");
        map.put("servizi.bottone.no", "no");
		map.put("ricerca.button.scorriAvanti","scorriAvanti");
		map.put("ricerca.button.scorriIndietro","scorriIndietro");
		map.put("ricerca.button.insRiga","inserisciRiga");
		map.put("ricerca.button.cancRiga","cancellaRiga");
		map.put("ricerca.button.stampa","stampa");
		map.put("ricerca.button.ordine","ordine");
		map.put("ricerca.button.chiudiBuono","chiudiBuono");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.button.completa","completa");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		EsaminaBuonoOrdineForm currentForm = (EsaminaBuonoOrdineForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar())
	            return mapping.getInputForward();

			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}
			//String ticket=Navigation.getInstance(request).getUserTicket();

			// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
			// DISABILITAZIONE DELL'INPUT
			ListaSuppBuoniOrdineVO ricerca = (ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricerca!=null  && ricerca.getChiamante()!=null)
			{
				currentForm.setEsaminaInibito(true);
				currentForm.setDisabilitaTutto(true);
			}


			currentForm.setListaDaScorrere((List<ListaSuppBuoniOrdineVO>) request.getSession().getAttribute(NavigazioneAcquisizioni.DETTAGLIO_BUONO_ORDINE));
			List<ListaSuppBuoniOrdineVO> buoni = currentForm.getListaDaScorrere();
			if(ValidazioneDati.isFilled(buoni) ) {
				//almaviva5_20140624 #4631
				currentForm.setParametri(buoni.get(0));
				if (buoni.size() > 1)
				{
					currentForm.setEnableScorrimento(true);
					//esaCambio.setPosizioneScorrimento(0);
					// carica ticket

				}
				else
				{
					currentForm.setEnableScorrimento(false);
				}

				//this.loadAppo(resultckeck);
			}
			if (String.valueOf(currentForm.getPosizioneScorrimento()).length()==0 )
			{
				currentForm.setPosizioneScorrimento(0);
			}
			// richiamo ricerca su db con elemento 1 di ricerca
			//this.loadDatiOrdinePassato(esaordini.getListaDaScorrere().get(this.esaordini.getPosizioneScorrimento()));
			// reimposta la pagina con i valori immessi oppure carica la pagina iniziale


			BuoniOrdineVO buono = new BuoniOrdineVO() ;

			if (!currentForm.isCaricamentoIniziale())
			{
				buono=this.loadDatiBuonoPassato(buoni.get(currentForm.getPosizioneScorrimento()));
				currentForm.setCaricamentoIniziale(true);
			}
			else			// reimposta la fattura con i valori immessi
    		{
				// reimposta il buono con i valori immessi
				if (currentForm.get("buono")!=null )
				{
					BuoniOrdineVO oggettoBuoTemp=(BuoniOrdineVO) currentForm.get("buono");

					if (oggettoBuoTemp.getBilancio()!=null && oggettoBuoTemp.getFornitore()!=null && oggettoBuoTemp.getCodBibl()!=null && oggettoBuoTemp.getNumBuonoOrdine()!=null )
					{
						buono=(BuoniOrdineVO) currentForm.get("buono");
					}
				}
				// carico eventuali righe inserite
				if (currentForm.get("elencaBuoni")!=null )
				{
					OrdiniVO[] elencaRigheOrd= (OrdiniVO[]) currentForm.get("elencaBuoni");
					// trasformo in arraylist
					List listaRig =new ArrayList();
					for (int w=0;  w < elencaRigheOrd.length; w++)
					{
						OrdiniVO elem = elencaRigheOrd[w];
						listaRig.add(elem);
					}
					buono.setListaOrdiniBuono(listaRig);
					currentForm.set("buono",buono);
				}

    		}


			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						buono.getFornitore().setCodice(ricForn.getSelezioniChiamato().get(0).getCodFornitore());
						buono.getFornitore().setDescrizione(ricForn.getSelezioniChiamato().get(0).getNomeFornitore());
					}
				}
/*				else
				{
					// pulizia della maschera di ricerca
					this.ricBuoni.setCodFornitore("");
					this.ricBuoni.setFornitore("");
				}
*/
				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

 			}

			//controllo se ho un risultato di una lista di supporto ORDINI richiamata da questa pagina (risultato della simulazione)
			ListaSuppOrdiniVO ricOrd=(ListaSuppOrdiniVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			if (ricOrd!=null && ricOrd.getChiamante()!=null && ricOrd.getChiamante().equals(mapping.getPath()))
 			{
				if (ricOrd!=null && ricOrd.getSelezioniChiamato()!=null && ricOrd.getSelezioniChiamato().size()!=0 )
				{
					if (ricOrd.getSelezioniChiamato().get(0).getChiave()!=null && ricOrd.getSelezioniChiamato().get(0).getChiave().length()!=0 )
					{
						//dal size aggiungo le righe selezionate e importateù
						if (buono.getListaOrdiniBuono()!=null && buono.getListaOrdiniBuono().size()>0 )
						{
							for (int i=0; i<ricOrd.getSelezioniChiamato().size(); i++)
							{
								buono.getListaOrdiniBuono().add(ricOrd.getSelezioniChiamato().get(i));
							}
						}
						else
						{
							buono.setListaOrdiniBuono(new ArrayList());
							for (int i=0; i<ricOrd.getSelezioniChiamato().size(); i++)
							{
								buono.getListaOrdiniBuono().add(ricOrd.getSelezioniChiamato().get(i));
							}
						}
						// importazione del fornitore del I Ordine
						StrutturaCombo fornit=new StrutturaCombo("","");
						if (ricOrd.getSelezioniChiamato().get(0).getFornitore()!=null)
						{
							fornit=ricOrd.getSelezioniChiamato().get(0).getFornitore();	// il primo fornitore
						}
						// l'istruzione sottostante carica il valore solo se non è stato impostato
						if (buono.getFornitore()==null || (buono.getFornitore()!=null && buono.getFornitore().getCodice()!=null && buono.getFornitore().getCodice().trim().length()==0 ))
						{
							buono.setFornitore(fornit);
						}
						// importazione del bilancio del I Ordine
						StrutturaTerna bilanc=new StrutturaTerna("","","");
						if (ricOrd.getSelezioniChiamato().get(0).getBilancio()!=null)
						{
							bilanc=ricOrd.getSelezioniChiamato().get(0).getBilancio();	// il primo bilancio
						}
						// l'istruzione sottostante carica il valore solo se non è stato impostato
						if (buono.getBilancio()==null || (buono.getBilancio()!=null && buono.getBilancio().getCodice1()!=null && buono.getBilancio().getCodice1().trim().length()==0 ))
						{
							buono.setBilancio(bilanc);
							if (ricOrd.getSelezioniChiamato().get(0).getIDBil()!=0)
							{
								buono.setIDBil(ricOrd.getSelezioniChiamato().get(0).getIDBil());
							}
						}

					}
					OrdiniVO[] oggettoDettBuo=new OrdiniVO[buono.getListaOrdiniBuono().size()];
					for (int i=0; i<buono.getListaOrdiniBuono().size(); i++)
					{
						oggettoDettBuo[i]=buono.getListaOrdiniBuono().get(i);
					}
					//oggettoBuo.setListaOrdiniBuono(listaRig);

					currentForm.set("elencaBuoni",oggettoDettBuo);
					currentForm.set("buono",buono);
					currentForm.set("numOrdini",buono.getListaOrdiniBuono().size());

				}
/*				else
				{
					// pulizia della maschera di ricerca
					this.ricBuoni.setTipoOrdine("");
					this.ricBuoni.setAnno("");
					this.ricBuoni.setNumero("");

				}
*/
				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

 			}

			ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
			configurazioneCriteri.setCodBibl(buono.getCodBibl());
			configurazioneCriteri.setCodPolo(buono.getCodPolo());
			ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
			Boolean gestBil =true;
			if (configurazioneLetta!=null && !configurazioneLetta.isGestioneBilancio())
			{
				gestBil =configurazioneLetta.isGestioneBilancio();
			}


			if (gestBil!=null && !gestBil)
			{
				currentForm.setGestBil(false);
			}


			currentForm.set("buono",buono);
			int totOrdini=0;
			if (buono.getListaOrdiniBuono()!=null && buono.getListaOrdiniBuono().size()>0)
			{
				totOrdini=buono.getListaOrdiniBuono().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			currentForm.set("numOrdini",totOrdini);

			OrdiniVO[] oggettoDettBuo=new OrdiniVO[totOrdini];
			for (int i=0; i<totOrdini; i++)
			{
				oggettoDettBuo[i]=buono.getListaOrdiniBuono().get(i);
			}
			currentForm.set("elencaBuoni",oggettoDettBuo);
			//this.esaBilanci.setNumImpegni(this.esaBilanci.getListaImpegni().size());

			//esaBilanci.set("elencaImpegni",oggettoBil.getDettagliBilancio());
			//this.loadImpegni();

			// da fare
			//this.loadTipoImpegno();
			List arrListaStatoBuono=this.loadStatoBuono();
			currentForm.set("listaStatoBuono",arrListaStatoBuono);

			List arrListaStatoOrdine=this.loadStato();
			currentForm.set("listaStato",arrListaStatoOrdine);

			List arrListaTipoOrdine=this.loadTipoOrdine();
			currentForm.set("listaTipoOrdine",arrListaTipoOrdine);

			currentForm.set("enableScorrimento",currentForm.isEnableScorrimento());
			// stato buono ordine CHIUSO
			if (buono!=null && buono.getStatoBuonoOrdine()!=null  && buono.getStatoBuonoOrdine().equals("C"))
			{
				currentForm.setDisabilitaTutto(true);
			}

			return mapping.getInputForward();
		}	catch (ValidationException ve) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}

	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			return Navigation.getInstance(request).goBack(false);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;
		try {
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) dettBuonoOrd.get("elencaBuoni");
			BuoniOrdineVO appobuo=(BuoniOrdineVO)dettBuonoOrd.get("buono");
			OrdiniVO dettBuo = new OrdiniVO("", "", "", "", "", "", "", 0, true, "", "", "","", "", "",new StrutturaCombo("","") , "", "", new StrutturaTerna ("","",""), "","","", 0,0, "", "", "", "", new StrutturaCombo ("",""),"", "", "", "", "", "", 0, "", "", "", false,false, "");
			if (elencaBuoni!=null &&  elencaBuoni.length!=0)
			{
				OrdiniVO[] listaDettBuo=new OrdiniVO[elencaBuoni.length +1];
				for (int i=0; i<elencaBuoni.length; i++)
				{
					listaDettBuo[i]=elencaBuoni[i];
				}
				listaDettBuo[elencaBuoni.length]=dettBuo;
				dettBuonoOrd.set("elencaBuoni",listaDettBuo);
				// trasformo in arraylist
				List lista2 =new ArrayList();
				for (int w=0;  w < listaDettBuo.length; w++)
				{
					OrdiniVO elem2 = listaDettBuo[w];
				    lista2.add(elem2);
				}
				appobuo.setListaOrdiniBuono(lista2);
				dettBuonoOrd.set("buono",appobuo);
				dettBuonoOrd.set("numOrdini",listaDettBuo.length);

				// gestione selezione della riga in inserimento
				Integer[]  radioOrd=new Integer[1];
				if (appobuo!=null && listaDettBuo.length>0)
				{
					radioOrd[0]=listaDettBuo.length-1;
					dettBuonoOrd.set("radioOrd",radioOrd);
				}

			}
			else
			{
				elencaBuoni=new OrdiniVO[1];
				elencaBuoni[0]=dettBuo;
				dettBuonoOrd.set("elencaBuoni",elencaBuoni);
				// trasformo in arraylist
				List lista2 =new ArrayList();
				for (int w=0;  w < elencaBuoni.length; w++)
				{
					OrdiniVO elem2 = elencaBuoni[w];
				    lista2.add(elem2);
				}
				appobuo.setListaOrdiniBuono(lista2);
				dettBuonoOrd.set("buono",appobuo);
				dettBuonoOrd.set("numOrdini",elencaBuoni.length);

				// gestione selezione della riga in inserimento
				Integer[]  radioOrd=new Integer[1];
				if (elencaBuoni!=null && elencaBuoni.length>0)
				{
					radioOrd[0]=elencaBuoni.length -1;
					dettBuonoOrd.set("radioOrd",radioOrd);
				}


			}
			//esaBilanci.set("radioImpegno",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			//e.printStackTrace();
			return mapping.getInputForward();
		}
	}

	public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;
		try {
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) dettBuonoOrd.get("elencaBuoni");
			BuoniOrdineVO appobuo=(BuoniOrdineVO)dettBuonoOrd.get("buono");
			Integer[] radioOrd=(Integer[])dettBuonoOrd.get("radioOrd");
			if (radioOrd.length!=0)
			{
				if (elencaBuoni.length!=0 )
				{
					List lista =new ArrayList();
					for (int t=0;  t < elencaBuoni.length; t++)
					{
						OrdiniVO elem = elencaBuoni[t];
						lista.add(elem);
					}

					//String[] appo=  selectedDatiIntest;
					int i= (radioOrd.length) -1;
					// ciclo dall'ultimo codice selezionato
					while (i>=0)
					{
						int elem = radioOrd[i];
						// il valore del num riga è superiore di una unità rispetto all'indice dell'array
						lista.remove(elem);
						i=i-1;
					}

					//this.renumera(lista);
					OrdiniVO[] lista_fin =new OrdiniVO [lista.size()];

					for (int r=0;  r < lista.size(); r++)
					{
						lista_fin [r] = (OrdiniVO)lista.get(r);
					}

				dettBuonoOrd.set("elencaBuoni",lista_fin);
				dettBuonoOrd.set("radioOrd", null) ;
				dettBuonoOrd.set("numOrdini",lista_fin.length);

				// trasformo in arraylist
				List lista2 =new ArrayList();
				for (int w=0;  w < lista_fin.length; w++)
				{
					OrdiniVO elem2 = lista_fin[w];
				    lista2.add(elem2);
				}
				appobuo.setListaOrdiniBuono(lista2);
				dettBuonoOrd.set("buono",appobuo);

				// se non sono presenti righe il bilancio si azzera
				if (lista_fin.length==0)
				{
					// fornitore editabile
					appobuo.getFornitore().setCodice("");
					appobuo.getFornitore().setDescrizione("");
					// bilancio editabile
					appobuo.getBilancio().setCodice1("");
					appobuo.getBilancio().setCodice2("");
					appobuo.getBilancio().setCodice3("");
				}

				dettBuonoOrd.set("buono",appobuo);



				}
			}
			dettBuonoOrd.set("radioOrd",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//EsaminaBuonoOrdineForm dettBuonoOrd = (EsaminaBuonoOrdineForm) form;
		DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

		try {
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) dettBuonoOrd.get("elencaBuoni");
			BuoniOrdineVO eleBuono=(BuoniOrdineVO)dettBuonoOrd.get("buono");

			String amount=eleBuono.getImportoStr().trim();
		    Double risult=Pulisci.ControllaImporto(amount);
		    eleBuono.setImporto(risult);
		    eleBuono.setImportoStr(Pulisci.VisualizzaImporto(eleBuono.getImporto()));

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaBuoni.length; w++)
			{
				OrdiniVO elem = elencaBuoni[w];
				if (elem.getPrezzoEuroOrdineStr()!=null)
				{
					String amountDett=elem.getPrezzoEuroOrdineStr().trim();
				    Double risultDett=Pulisci.ControllaImporto(amountDett);
				    elem.setPrezzoEuroOrdine(risultDett);
				    elem.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto(elem.getPrezzoEuroOrdine()));
				}

				lista.add(elem);
			}
			eleBuono.setListaOrdiniBuono(lista);
			dettBuonoOrd.set("buono",eleBuono);

			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaBuoniOrdineVO(eleBuono);
			// fine validazione


			((EsaminaBuonoOrdineForm) dettBuonoOrd).setConferma(true);
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setPressioneBottone("salva");
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		}	catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			if (ve.getError()==4118) // buono d'ordine stampato
			{
				((EsaminaBuonoOrdineForm) dettBuonoOrd).setConferma(true);
				((EsaminaBuonoOrdineForm) dettBuonoOrd).setPressioneBottone("salva");
				((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(true);

			}
			else
			{
				((EsaminaBuonoOrdineForm) dettBuonoOrd).setConferma(false);
				((EsaminaBuonoOrdineForm) dettBuonoOrd).setPressioneBottone("");
				((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(false);
			}
			return mapping.getInputForward();

		} catch (Exception e) {
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setConferma(false);
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setPressioneBottone("");
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaBuonoOrdineForm dettBuonoOrd = (EsaminaBuonoOrdineForm) form;
		try {
			BuoniOrdineVO oggettoBuo=loadDatiBuonoPassato(dettBuonoOrd.getListaDaScorrere().get(dettBuonoOrd.getPosizioneScorrimento()));
			dettBuonoOrd.set("buono",oggettoBuo);
			dettBuonoOrd.set("numOrdini",0);
			int totOrdini=0;
			if (oggettoBuo.getListaOrdiniBuono()!=null && oggettoBuo.getListaOrdiniBuono().size()>0)
			{
				totOrdini=oggettoBuo.getListaOrdiniBuono().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			dettBuonoOrd.set("numOrdini",totOrdini);

			OrdiniVO[] oggettoDettBuo=new OrdiniVO[totOrdini];
			for (int i=0; i<totOrdini; i++)
			{
				oggettoDettBuo[i]=oggettoBuo.getListaOrdiniBuono().get(i);
			}
			dettBuonoOrd.set("elencaBuoni",oggettoDettBuo);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;
		try {
			List<BuoniOrdineVO> provaElencoOrdinato=new ArrayList<BuoniOrdineVO>();

			BuoniOrdineVO oggettoBuo=(BuoniOrdineVO) dettBuonoOrd.get("buono");
			provaElencoOrdinato.add(oggettoBuo);

			ConfigurazioneBOVO confBOnew=new ConfigurazioneBOVO();
			confBOnew.setCodBibl(oggettoBuo.getCodBibl());
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			confBOnew.setCodPolo(polo);
			ConfigurazioneBOVO confLettonew=this.loadConfigurazione(confBOnew);
			if (confLettonew!=null)
			{
				confBOnew=confLettonew;
			}
			confBOnew.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

            StampaBuonoOrdineVO stampaOL=new StampaBuonoOrdineVO ();
			stampaOL.setCodBibl(oggettoBuo.getCodBibl());
			stampaOL.setCodPolo(polo);

            stampaOL.setConfigurazione(confBOnew);
			//stampaOL.setConfigurazione(confBO);
			//stampaOL.setListaOrdiniDaStampare(listaOrdiniSel);
			stampaOL.setListaOrdiniDaStampare(null);
			stampaOL.setListaBuoniOrdineDaStampare(provaElencoOrdinato);
			stampaOL.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());
			stampaOL.setTicket(Navigation.getInstance(request).getUserTicket());

			List listaFiltrata=this.Stampato(provaElencoOrdinato,"BUO","");

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			List risultatoPerStampa=factory.getGestioneAcquisizioni().impostaBuoniOrdineDaStampare( confBOnew, listaFiltrata, "BUO", false,  Navigation.getInstance(request).getUserTicket(), Navigation.getInstance(request).getUtente().getFirmaUtente(),  confBOnew.getDenoBibl());

			listaFiltrata=risultatoPerStampa;

			if (listaFiltrata!=null && listaFiltrata.size()>0)
			{
				stampaOL.setListaBuoniOrdineDaStampare(listaFiltrata);
					// test su memorizzazione flag stampato	SPOSTATO IN STAMPAONLINEACTION
					// boolean risultato=this.StampaOrdini(provaElencoOrdinato,null,"BUO",confBOnew.getUtente(),"");
					boolean risultato=true;

				if (!risultato)
				{

					// cambiare messaggio
					LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoBuoniOrdineNonStampati"));

					return mapping.getInputForward();
				}
				else
				{
					// DA RIPRISTINARE PER LA STAMPA (almaviva)
						//TODO GVCANCE
						request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_BUONI_ORDINE);
						//request.setAttribute("DATI_STAMPE_ON_LINE", stampaOL);
						request.setAttribute("DATI_STAMPE_ON_LINE", listaFiltrata);
						return mapping.findForward("stampaOL");
				}
			}
			else
			{

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.scartoOrdiniNonStampati"));

				return mapping.getInputForward();
			}

			//return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward ordine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;
		//dettBuonoOrd = (EsaminaBuonoOrdineForm) form;

		try {

			// CONTROLLO CHECK DI RIGA
/*			Integer[] radioOrd=(Integer[])dettBuonoOrd.get("radioOrd");
			if (radioOrd.length!=0)
			{
				int indiceRigaIns= radioOrd[0]; // unico check
			}
			else
			{

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.buoniOrdine.crea"));

					return mapping.getInputForward();
			}
			*/

			// stato buono da cambiare
			// impongo il fornitore e il bilancio prescelti
			BuoniOrdineVO oggettoBuo=(BuoniOrdineVO) dettBuonoOrd.get("buono");
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null

			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			request.getSession().removeAttribute("ordiniSelected");
			request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

/*			if (request.getSession().getAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE)==null )
			{
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
				request.getSession().removeAttribute("ordiniSelected");
				request.getSession().removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);
			}
			else
			{
				//throw new Exception("limite di ricorsione");

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));

				return mapping.getInputForward();
			}
*/

			ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
				// carica i criteri di ricerca da passare alla esamina
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String codP=polo;
				String codB=oggettoBuo.getCodBibl();
				String codBAff = null;
				String codOrd=null;
				String annoOrd=null;
				String tipoOrd=null; // A
				if (dettBuonoOrd.get("radioOrd")!=null )
				{
					Integer[] radioOrd=(Integer[])dettBuonoOrd.get("radioOrd");
					if (radioOrd.length!=0)
					{
						int indiceRigaIns= radioOrd[0]; // unico check
						if (oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getTipoOrdine()!=null && oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getTipoOrdine().length()>0 )
						{
							tipoOrd=oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getTipoOrdine();
						}
						if (oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getAnnoOrdine()!=null && oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getAnnoOrdine().length()>0 )
						{
							annoOrd=oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getAnnoOrdine();
						}
						if (oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getCodOrdine()!=null && oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getCodOrdine().length()>0 )
						{
							codOrd=oggettoBuo.getListaOrdiniBuono().get(indiceRigaIns).getCodOrdine();

						}
					}

				}
				String dataOrdDa=null;
				String dataOrdA=null;
				String cont=null;
				String statoOrd=null;
				StrutturaCombo forn=new StrutturaCombo (oggettoBuo.getFornitore().getCodice(),oggettoBuo.getFornitore().getDescrizione().trim());
				String tipoInvioOrd=null;
				StrutturaTerna bil=new StrutturaTerna(oggettoBuo.getBilancio().getCodice1(),oggettoBuo.getBilancio().getCodice2(),"" );
				String sezioneAcqOrd=null;
				StrutturaCombo tit=new StrutturaCombo ("","");
				String dataFineAbbOrdDa=null;
				String dataFineAbbOrdA=null;
				String naturaOrd=null;
				String chiama=mapping.getPath();
				String[] statoOrdArr=new String[1];
				statoOrdArr[0]="A";
				Boolean rinn=false;
				Boolean stamp=false;

				eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp);
				String ticket=Navigation.getInstance(request).getUserTicket();
				eleRicerca.setTicket(ticket);
				eleRicerca.setModalitaSif(false);

				request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE, eleRicerca);
				return mapping.findForward("ordine");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward chiudiBuono(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;
		try {
			// stato buono da cambiare
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) dettBuonoOrd.get("elencaBuoni");
			BuoniOrdineVO eleBuono=(BuoniOrdineVO)dettBuonoOrd.get("buono");

			boolean controlloStatoOrdine=true;
			for (int w=0;  w < elencaBuoni.length; w++)
			{
				OrdiniVO elem = elencaBuoni[w];
				if (elem!=null && elem.getStatoOrdine()!=null && elem.getStatoOrdine().trim().length()>0)
				{
					if (!elem.getStatoOrdine().equals("C") &&  !elem.getStatoOrdine().equals("N") && !elem.isRinnovato()) // &&  elem.getAnnoPrimoOrdine()==null
					{
						controlloStatoOrdine=false;
						break;
					}
				}
			}
			if (!controlloStatoOrdine)
			{

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.chiudiBuonoNO"));

				//return mapping.getInputForward();
			}
			else
			{
				eleBuono.setStatoBuonoOrdine("C");
				dettBuonoOrd.set("buono",eleBuono);

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.chiudiBuonoSI"));

				//return mapping.getInputForward();
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaBuonoOrdineForm dettBuonoOrd = (EsaminaBuonoOrdineForm) form;
		try {
			ActionForward forward = fornitoreCercaVeloce(mapping, request, dettBuonoOrd);
			if (forward != null){
				return forward;
			}
			BuoniOrdineVO oggBuo= (BuoniOrdineVO) dettBuonoOrd.get("buono");
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

/*			if (request.getSession().getAttribute("criteriRicercaFornitore")==null )
			{
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

			}
			else
			{
				//throw new Exception("limite di ricorsione");

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));

				return mapping.getInputForward();
			}
*/
			this.impostaFornitoreCerca(request,mapping, oggBuo);
			return mapping.findForward("fornitoreCerca");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void impostaFornitoreCerca( HttpServletRequest request, ActionMapping mapping, BuoniOrdineVO oggBuo)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String biblio=Navigation.getInstance(request).getUtente().getCodBib();
		//String codB=biblio;
		String codB=oggBuo.getCodBibl();
		String codForn="";
		String nomeForn="";
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="";
		String provForn="";
		String loc="0"; // ricerca sempre locale
		String chiama=mapping.getPath();
		//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
		eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
		//ricerca.add(eleRicerca);
		eleRicerca.setModalitaSif(false);
		request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);
	}catch (Exception e) {	}
	}

	public ActionForward completa (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;
/*			if (creaBuono.get("eleIns")!=null) // test sull'elemento appena inserito
			{
				int indiceRigaIns=999;
				indiceRigaIns=(Integer) creaBuono.get("eleIns");
				if (indiceRigaIns >=0 && indiceRigaIns!=999)
				{

*/
			Integer[] radioOrd=(Integer[])dettBuonoOrd.get("radioOrd");
			if (radioOrd.length!=0)
			{
				OrdiniVO[] elencaBuoni= (OrdiniVO[]) dettBuonoOrd.get("elencaBuoni");

				if (elencaBuoni.length!=0 )
				{
					//String appo="cerca l'ordine e imposta gli altri campi";
					BuoniOrdineVO eleBuo=(BuoniOrdineVO)dettBuonoOrd.get("buono");
					int indiceRigaIns= radioOrd[0]; // unico check
					String tipo=elencaBuoni[indiceRigaIns].getTipoOrdine();
					String anno=elencaBuoni[indiceRigaIns].getAnnoOrdine();
					String codice=elencaBuoni[indiceRigaIns].getCodOrdine();

					if (!tipo.equals("") && !anno.equals("") && !codice.equals(""))
					{
						ListaSuppOrdiniVO eleRicerca=new ListaSuppOrdiniVO();
						// carica i criteri di ricerca da passare alla esamina
						String polo=Navigation.getInstance(request).getUtente().getCodPolo();
						String codP=polo;
						String codB=eleBuo.getCodBibl();
						String codBAff = null;
						String codOrd=codice;
						String annoOrd=anno;
						String tipoOrd=tipo; // A
						String dataOrdDa=null;
						String dataOrdA=null;
						String cont=null;
						String statoOrd=null;
						StrutturaCombo forn=new StrutturaCombo ("","");
						String tipoInvioOrd=null;
						StrutturaTerna bil=new StrutturaTerna("","","" );
						String sezioneAcqOrd=null;
						StrutturaCombo tit=new StrutturaCombo ("","");
						String dataFineAbbOrdDa=null;
						String dataFineAbbOrdA=null;
						String naturaOrd=null;
						String chiama=mapping.getPath();
						String[] statoOrdArr=new String[0];
						Boolean rinn=false;
						Boolean stamp=false;

						eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn, stamp);
						String ticket=Navigation.getInstance(request).getUserTicket();
						eleRicerca.setTicket(ticket);

						//request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE, (ListaSuppOrdiniVO) eleRicerca);
						List<OrdiniVO> listaOrdini;
						try {
							listaOrdini=this.getListaOrdiniVO(eleRicerca); // va in errore se non ha risultati
						// AGGIUNGERE GESTIONE ERRRI VE test di 				if (ve.getError()==4001)
						}	catch (ValidationException ve) {
							if (ve.getError()==4001)
							{

								LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.rigaOrdineInesistente"));

							}
							else
							{

								LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.rigaOrdineErrGenerico"));

							}
							return mapping.getInputForward();
						}
						// altri tipi di errore
						catch (Exception e) {

							//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
							LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

							return mapping.getInputForward();
						}

						if (listaOrdini!=null && listaOrdini.size()==1)
						{
							String bid=listaOrdini.get(0).getTitolo().getCodice();
							String titolo=listaOrdini.get(0).getTitolo().getDescrizione();
							String stato=listaOrdini.get(0).getStatoOrdine();
							double prezzo=listaOrdini.get(0).getPrezzoEuroOrdine();
						    String prezzoStr=Pulisci.VisualizzaImporto(prezzo);
							//String prezzo=listaOrdini.get(0).getPrezzoEuroOrdineStr();

						    Boolean continuativo=listaOrdini.get(0).isContinuativo();

							elencaBuoni[indiceRigaIns].getTitolo().setCodice(bid);
							elencaBuoni[indiceRigaIns].getTitolo().setDescrizione(titolo);
							elencaBuoni[indiceRigaIns].setStatoOrdine(stato);
							elencaBuoni[indiceRigaIns].setPrezzoEuroOrdine(prezzo);
							elencaBuoni[indiceRigaIns].setPrezzoEuroOrdineStr(prezzoStr);
							elencaBuoni[indiceRigaIns].setContinuativo(continuativo);

						}
					}
					else
					{

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.chiaveOrdine"));


					}
					List lista =new ArrayList();
					// calcolo totale
					double totale=0;
					for (int w=0;  w < elencaBuoni.length; w++)
					{
						OrdiniVO elem = elencaBuoni[w];
						lista.add(elem);
						if (elem.getPrezzoEuroOrdine()>0)
						{
							totale=totale+elem.getPrezzoEuroOrdine();
						}
					}
					eleBuo.setListaOrdiniBuono(lista);
					eleBuo.setImportoStr(Pulisci.VisualizzaImporto(totale));
					dettBuonoOrd.set("buono",eleBuo);
				}
			}

			//OrdiniVO[] elencaBuoni= (OrdiniVO[]) creaBuono.get("elencaBuoni");
			//BuoniOrdineVO eleBuo=(BuoniOrdineVO)creaBuono.get("buono");
			// trasformo in arraylist
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;
		try {
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) dettBuonoOrd.get("elencaBuoni");
			BuoniOrdineVO eleBuono=(BuoniOrdineVO)dettBuonoOrd.get("buono");
			eleBuono.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			String amount=eleBuono.getImportoStr().trim();
		    Double risult=Pulisci.ControllaImporto(amount);
		    eleBuono.setImporto(risult);
		    eleBuono.setImportoStr(Pulisci.VisualizzaImporto(eleBuono.getImporto()));


			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaBuoni.length; w++)
			{
				OrdiniVO elem = elencaBuoni[w];
				if (elem.getPrezzoEuroOrdineStr()!=null)
				{
					String amountDett=elem.getPrezzoEuroOrdineStr().trim();
				    Double risultDett=Pulisci.ControllaImporto(amountDett);
				    elem.setPrezzoEuroOrdine(risultDett);
				    elem.setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto(elem.getPrezzoEuroOrdine()));
				}
				lista.add(elem);
			}
			eleBuono.setListaOrdiniBuono(lista);
			String ticket=Navigation.getInstance(request).getUserTicket();
			eleBuono.setTicket(ticket);

			dettBuonoOrd.set("buono",eleBuono);

			((EsaminaBuonoOrdineForm) dettBuonoOrd).setConferma(false);
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(false);

			if (((EsaminaBuonoOrdineForm) dettBuonoOrd).getPressioneBottone().equals("salva")) {
				((EsaminaBuonoOrdineForm) dettBuonoOrd).setPressioneBottone("");

				if (eleBuono.getStatoBuonoOrdine().equals("S"))
				{
					eleBuono.setSalvaEffettuato(true);
				}

				if (!this.modificaBuono(eleBuono)) {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.erroreModifica"));

					//return mapping.getInputForward();
				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.modificaOK"));

					// stato buono ordine CHIUSO
					if (eleBuono!=null && eleBuono.getStatoBuonoOrdine()!=null  && eleBuono.getStatoBuonoOrdine().equals("C"))
					{
						((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(true);
					}
					return ripristina( mapping,  form,  request,  response);

				}
			}
			if (((EsaminaBuonoOrdineForm) dettBuonoOrd).getPressioneBottone().equals("cancella")) {
				((EsaminaBuonoOrdineForm) dettBuonoOrd).setPressioneBottone("");
					if (!this.cancellaBuono(eleBuono)) {

						LinkableTagUtils.addError(request, new ActionMessage(
								"errors.acquisizioni.erroreCancella"));

					}
					else
					{

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.cancellaOK"));

						((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(true);
					}
			}



			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setConferma(false);
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setPressioneBottone("");
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(false);

			if (ve.getMsg()!=null && ve.getMsg().equals("msgSez")){
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni."+ ve.getMessage(), ve.getBid(), ve.getIsbd()));
			}
			else
			{
				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni."+ ve.getMessage()));
			}

				return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setConferma(false);
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setPressioneBottone("");
			((EsaminaBuonoOrdineForm) dettBuonoOrd).setDisabilitaTutto(false);

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaBuonoOrdineForm dettBuonoOrd = (EsaminaBuonoOrdineForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			 dettBuonoOrd.setConferma(false);
			 dettBuonoOrd.setPressioneBottone("");
			 dettBuonoOrd.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaBuonoOrdineForm dettBuonoOrd = (EsaminaBuonoOrdineForm) form;
		EsaminaBuonoOrdineForm dettBuonoOrdNorm = (EsaminaBuonoOrdineForm) form;

		try {
			int attualePosizione=dettBuonoOrdNorm.getPosizioneScorrimento()+1;
			int dimensione=dettBuonoOrdNorm.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1)
				{


				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));


				}
			else
				{
					try
					{
						//this.loadDatiBilancioPassato(esaBilanci.getListaDaScorrere().get(attualePosizione));
						BuoniOrdineVO oggettoBuo=loadDatiBuonoPassato(dettBuonoOrd.getListaDaScorrere().get(attualePosizione));
						dettBuonoOrd.set("buono",oggettoBuo);
						dettBuonoOrd.set("numOrdini",0);
						int totOrdini=0;
						if (oggettoBuo.getListaOrdiniBuono()!=null && oggettoBuo.getListaOrdiniBuono().size()>0)
						{
							totOrdini=oggettoBuo.getListaOrdiniBuono().size();
						}
						//this.insBilanci.setNumImpegni(totImpegni);
						dettBuonoOrd.set("numOrdini",totOrdini);

						OrdiniVO[] oggettoDettBuo=new OrdiniVO[totOrdini];
						for (int i=0; i<totOrdini; i++)
						{
							oggettoDettBuo[i]=oggettoBuo.getListaOrdiniBuono().get(i);
						}
						dettBuonoOrd.set("elencaBuoni",oggettoDettBuo);

						// aggiornamento del tab di visualizzazione dei dati per tipo ordine

						if (oggettoBuo!=null && oggettoBuo.getStatoBuonoOrdine()!=null  && oggettoBuo.getStatoBuonoOrdine().equals("C"))
						{
							dettBuonoOrdNorm.setDisabilitaTutto(true);
						}
						else
						{
							dettBuonoOrdNorm.setDisabilitaTutto(false);
						}
						if (dettBuonoOrdNorm.isEsaminaInibito())
						{
							dettBuonoOrdNorm.setDisabilitaTutto(true);
						}


					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							dettBuonoOrd.setPosizioneScorrimento(attualePosizione);
							return scorriAvanti( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
					dettBuonoOrdNorm.setPosizioneScorrimento(attualePosizione);
				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaBuonoOrdineForm dettBuonoOrd = (EsaminaBuonoOrdineForm) form;
		EsaminaBuonoOrdineForm dettBuonoOrdNorm = (EsaminaBuonoOrdineForm) form;

		try {
			int attualePosizione=dettBuonoOrdNorm.getPosizioneScorrimento()-1;
			int dimensione=dettBuonoOrdNorm.getListaDaScorrere().size();
			if (attualePosizione < 0)
				{


				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));


				}
			else
				{
					try
					{
						//this.loadDatiBilancioPassato(esaBilanci.getListaDaScorrere().get(attualePosizione));
						BuoniOrdineVO oggettoBuo=loadDatiBuonoPassato(dettBuonoOrd.getListaDaScorrere().get(attualePosizione));
						dettBuonoOrd.set("buono",oggettoBuo);
						dettBuonoOrd.set("numOrdini",0);
						int totOrdini=0;
						if (oggettoBuo.getListaOrdiniBuono()!=null && oggettoBuo.getListaOrdiniBuono().size()>0)
						{
							totOrdini=oggettoBuo.getListaOrdiniBuono().size();
						}
						//this.insBilanci.setNumImpegni(totImpegni);
						dettBuonoOrd.set("numOrdini",totOrdini);

						OrdiniVO[] oggettoDettBuo=new OrdiniVO[totOrdini];
						for (int i=0; i<totOrdini; i++)
						{
							oggettoDettBuo[i]=oggettoBuo.getListaOrdiniBuono().get(i);
						}
						dettBuonoOrd.set("elencaBuoni",oggettoDettBuo);

						if (oggettoBuo!=null && oggettoBuo.getStatoBuonoOrdine()!=null  && oggettoBuo.getStatoBuonoOrdine().equals("C"))
						{
							dettBuonoOrdNorm.setDisabilitaTutto(true);
						}
						else
						{
							dettBuonoOrdNorm.setDisabilitaTutto(false);
						}
						if (dettBuonoOrdNorm.isEsaminaInibito())
						{
							dettBuonoOrdNorm.setDisabilitaTutto(true);
						}


					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							dettBuonoOrd.setPosizioneScorrimento(attualePosizione);
							return scorriIndietro( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
					dettBuonoOrdNorm.setPosizioneScorrimento(attualePosizione);
				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaBuonoOrdineForm currentForm = (EsaminaBuonoOrdineForm) form;
		try {

			currentForm.setConferma(true);
			currentForm.setPressioneBottone("cancella");
    		currentForm.setDisabilitaTutto(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			currentForm.setConferma(false);
			currentForm.setPressioneBottone("");
    		currentForm.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	private List<OrdiniVO> getListaOrdiniVO(ListaSuppOrdiniVO criRicerca) throws Exception
	{
	List<OrdiniVO> listaOrdini;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaOrdini = factory.getGestioneAcquisizioni().getRicercaListaOrdini(criRicerca);
	//this.sinCambio.setListaCambi(listaCambi);
	return listaOrdini;
	}

	private BuoniOrdineVO loadDatiBuonoPassato(ListaSuppBuoniOrdineVO criteriRicercaBuono) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<BuoniOrdineVO> buoniTrovato = new ArrayList();
		buoniTrovato = factory.getGestioneAcquisizioni().getRicercaListaBuoniOrd(criteriRicercaBuono);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaBilanci.setBilancio(bilanciTrovato.get(0));
		//for (int i=0; i<bilanciTrovato.size(); i++)
		//this.esaBilanci.setListaImpegni(bilanciTrovato.get(0).getDettagliBilancio());
		BuoniOrdineVO buo=buoniTrovato.get(0);
		try {
			buo.setImportoStr(Pulisci.VisualizzaImporto( buo.getImporto()));
			for (int w=0;  w < buo.getListaOrdiniBuono().size(); w++)
			{
				//BilancioDettVO elem = bil.getDettagliBilancio().get(w);
				buo.getListaOrdiniBuono().get(w).setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto( buo.getListaOrdiniBuono().get(w).getPrezzoEuroOrdine()));
				//lista.add(elem);
			}
		} catch (Exception e) {
		    	//e.printStackTrace();
		    	//throw new ValidationException("importoErrato",
		    	//		ValidationExceptionCodici.importoErrato);
			buo.setImportoStr("0,00");

		}

		return buo;
	}

	private List loadStatoBuono() throws Exception {
		List lista = new ArrayList();

		StrutturaCombo elem = new StrutturaCombo("A","A - Aperto");
		lista.add(elem);
		elem = new StrutturaCombo("C","C - Chiuso");
		lista.add(elem);
		elem = new StrutturaCombo("S","S - Stampato");
		lista.add(elem);
		return lista;

	}
	private List loadTipoOrdine() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("A","A - Acquisto");
		lista.add(elem);
		elem = new StrutturaCombo("L","L - Deposito Legale");
		lista.add(elem);
		elem = new StrutturaCombo("D","D - Dono");
		lista.add(elem);
		elem = new StrutturaCombo("C","C - Scambio");
		lista.add(elem);
		elem = new StrutturaCombo("V","V - Visione Trattenuta");
		lista.add(elem);
		elem = new StrutturaCombo("R","R - Rilegatura");
		lista.add(elem);

		return lista;
	}

	private List loadStato() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("A","A - Aperto");
		lista.add(elem);
		elem = new StrutturaCombo("C","C - Chiuso");
		lista.add(elem);
		elem = new StrutturaCombo("I","I - Spedito");
		lista.add(elem);
		elem = new StrutturaCombo("N","N - Annullato");
		lista.add(elem);
		elem = new StrutturaCombo("R","R - Rinnovato");
		lista.add(elem);

		return lista;
	}

	private boolean modificaBuono(BuoniOrdineVO buono) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaBuonoOrd(buono);
		return valRitorno;
	}

	private boolean cancellaBuono(BuoniOrdineVO buono) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaBuonoOrd(buono);
		return valRitorno;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {
			if (idCheck.equals("GESTIONE")) {
				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				UserVO utente = Navigation.getInstance(request).getUtente();

				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_BUONI_ORDINE, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			}

			if (ValidazioneDati.equals(idCheck, "ESAMINA")) {
				//almaviva5_20140624 #4631
				EsaminaBuonoOrdineForm currentForm = (EsaminaBuonoOrdineForm) form;
				ListaSuppBuoniOrdineVO params = currentForm.getParametri();
				return ValidazioneDati.in(params.getTipoOperazione(), TipoOperazione.ESAMINA_DA_ORDINE);
			}

		} catch (Exception e) {

			return false;
		}

		return false;
	}

	private List  Stampato(List listaOggetti, String tipoOggetti,  String bo) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List ris = factory.getGestioneAcquisizioni().gestioneStampato( listaOggetti,  tipoOggetti, bo);
		return ris;
	}
	private ConfigurazioneORDVO loadConfigurazioneORD(ConfigurazioneORDVO configurazioneORD) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneORDVO configurazioneTrovata = new ConfigurazioneORDVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazioneOrdini(configurazioneORD);
		//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
		// impostazione delle variabili dinamiche

		return configurazioneTrovata;
	}



	private ConfigurazioneBOVO loadConfigurazione(ConfigurazioneBOVO configurazione) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ConfigurazioneBOVO configurazioneTrovata = new ConfigurazioneBOVO();
		configurazioneTrovata = factory.getGestioneAcquisizioni().loadConfigurazione(configurazione);
		//ConfigurazioneBOVO config=configurazioneTrovata.get(0);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaCom.setDatiComunicazione(configurazioneTrovata.get(0));
		return configurazioneTrovata;
	}

}
