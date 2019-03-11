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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO.TipoOperazione;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.buoniordine.InserisciBuonoOrdineForm;
import it.iccu.sbn.web.actions.acquisizioni.AcquisizioniBaseAction;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.DynaValidatorForm;

public class InserisciBuonoOrdineAction extends AcquisizioniBaseAction implements SbnAttivitaChecker {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
        map.put("servizi.bottone.si", "si");
        map.put("servizi.bottone.no", "no");
		map.put("ricerca.button.insRiga","inserisciRiga");
		map.put("ricerca.button.cancRiga","cancellaRiga");
		map.put("ricerca.button.stampa","stampa");
		map.put("ricerca.button.ordine","ordine");
		map.put("ordine.label.fornitore","fornitoreCerca");
		map.put("ricerca.button.scegli","scegli");
		map.put("ricerca.button.completa","completa");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_GESTIONE_BUONI_ORDINE, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		InserisciBuonoOrdineForm currentForm = (InserisciBuonoOrdineForm) form;
		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
	            return mapping.getInputForward();

			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}


			List arrListaStatoBuono=this.loadStatoBuono();
			currentForm.set("listaStatoBuono",arrListaStatoBuono);

			List arrListaStatoOrdine=this.loadStato();
			currentForm.set("listaStato",arrListaStatoOrdine);

			List arrListaTipoOrdine=this.loadTipoOrdine();
			currentForm.set("listaTipoOrdine",arrListaTipoOrdine);
			// reimposta la pagina con i valori immessi oppure carica la pagina iniziale
			BuoniOrdineVO buono = loadBuono();
			String ticket = navi.getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=navi.getUtente().getCodBib();

			// preimpostazione della schermata di inserimento con i valori ricercati
			HttpSession session = request.getSession();
			ListaSuppBuoniOrdineVO lsbo = (ListaSuppBuoniOrdineVO) session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			if (lsbo != null) {
				//almaviva5_20140624 #4631
				currentForm.setParametri(lsbo);
				session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
				if (lsbo.getBilancio()!=null )
				{
					buono.setBilancio(lsbo.getBilancio());
				}
				if (lsbo.getFornitore()!=null )
				{
					buono.setFornitore(lsbo.getFornitore());;
				}
				//				 MANCA GESTIONE DI RIGA ORDINE
				if (lsbo.getOrdine()!=null && lsbo.getOrdine().getCodice1()!=null && lsbo.getOrdine().getCodice1().trim().length()>0 && lsbo.getOrdine().getCodice2()!=null && lsbo.getOrdine().getCodice2().trim().length()>0 && lsbo.getOrdine().getCodice3()!=null && lsbo.getOrdine().getCodice3().trim().length()>0)
				{
					// controllo esistenza bilancio incrementato
					ListaSuppOrdiniVO ricercaOrdini=new ListaSuppOrdiniVO();
					ricercaOrdini.setCodPolo(lsbo.getCodPolo());
					ricercaOrdini.setCodBibl(lsbo.getCodBibl());
					ricercaOrdini.setTipoOrdine(lsbo.getOrdine().getCodice1());
					ricercaOrdini.setAnnoOrdine(lsbo.getOrdine().getCodice2());
					ricercaOrdini.setCodOrdine(lsbo.getOrdine().getCodice3());

		        	List<OrdiniVO> elenco=null;
					try {
						elenco= this.getListaOrdiniVO(ricercaOrdini);

					} catch (Exception e) {
						e.printStackTrace();
					}


					OrdiniVO[] elencaBuoni=new OrdiniVO[1];
					OrdiniVO dettBuo = new OrdiniVO("", "", "", "", "", "", "", 0, true, "", "", "","", "", "",new StrutturaCombo("","") , "", "", new StrutturaTerna ("","",""), "","","", 0,0, "", "", "", "", new StrutturaCombo ("",""),"", "", "", "", "", "", 0, "", "", "",false,  false, "");
					dettBuo.setPrezzoEuroOrdineStr("0,00");
					dettBuo.setAnnoOrdine(lsbo.getOrdine().getCodice2());
					dettBuo.setTipoOrdine(lsbo.getOrdine().getCodice1());
					dettBuo.setCodOrdine(lsbo.getOrdine().getCodice3());

		        	if (elenco!=null && elenco.size()==1)
		        	{
						dettBuo.setPrezzoEuroOrdineStr(String.valueOf(elenco.get(0).getPrezzoEuroOrdine()));
						dettBuo.getTitolo().setCodice(elenco.get(0).getTitolo().getCodice());
						dettBuo.getTitolo().setDescrizione(elenco.get(0).getTitolo().getDescrizione());
						dettBuo.setStatoOrdine(elenco.get(0).getStatoOrdine());
		        	}


					elencaBuoni[0]=dettBuo;
					currentForm.set("elencaBuoni",elencaBuoni);
					// trasformo in arraylist
					List lista2 =new ArrayList();
					for (int w=0;  w < elencaBuoni.length; w++)
					{
						OrdiniVO elem2 = elencaBuoni[w];
					    lista2.add(elem2);
					}
					buono.setListaOrdiniBuono(lista2);
					//creaBuono.set("buono",oggettoBuo);
					//creaBuono.set("numOrdini",elencaBuoni.length);

				}

				//almaviva5_20140624 #4631
				OrdiniVO ordine = lsbo.getDatiOrdine();
				if (ordine != null) {
					List<OrdiniVO> ordiniBuono = ValidazioneDati.asSingletonList(ordine);
					buono.setListaOrdiniBuono(ordiniBuono);
					currentForm.set("elencaBuoni", ordiniBuono.toArray(new OrdiniVO[1]) );
				}


			}



			// reimposta il buono con i valori immessi
			if (currentForm.get("buono")!=null )
			{
				BuoniOrdineVO oggettoBuoTemp=(BuoniOrdineVO) currentForm.get("buono");

				if (oggettoBuoTemp.getBilancio()!=null && oggettoBuoTemp.getFornitore()!=null && oggettoBuoTemp.getCodBibl()!=null && oggettoBuoTemp.getNumBuonoOrdine()!=null )
				{
					buono=(BuoniOrdineVO) currentForm.get("buono");
				}
			}

			if (biblio!=null && buono !=null && (buono.getCodBibl()!=null || (buono.getCodBibl()!=null && buono.getCodBibl().trim().length()==0)))
			{
				buono.setCodBibl(biblio);
			}

			buono.setTicket(ticket);
			String polo=navi.getUtente().getCodPolo();
			buono.setCodPolo(polo);


			ConfigurazioneBOVO config=new ConfigurazioneBOVO();
			config.setCodPolo(buono.getCodPolo());
			config.setCodBibl(buono.getCodBibl());
			ConfigurazioneBOVO configLetta=loadConfigurazione(config);
			if (configLetta!=null && !configLetta.isNumAutomatica() )
			{
				currentForm.setNumAuto(false);
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


			// ***** caricamenti liste supporto*********
			//controllo se ho un risultato di una lista di supporto FORNITORE richiamata da questa pagina (risultato della simulazione)
			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)session.getAttribute("attributeListaSuppFornitoreVO");
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
				session.removeAttribute("attributeListaSuppFornitoreVO");
				session.removeAttribute("fornitoriSelected");
				session.removeAttribute("criteriRicercaFornitore");

 			}
			//controllo se ho un risultato di una lista di supporto ORDINI richiamata da questa pagina (risultato della simulazione)
			// o di ritorno da lista ordini
			ListaSuppOrdiniVO ricOrd=(ListaSuppOrdiniVO)request.getAttribute("passaggioListaSuppOrdiniVO");
			if (ricOrd==null) // test di ritorno da lista ordini
			{
				ricOrd=(ListaSuppOrdiniVO)session.getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
			}

			if (ricOrd!=null && ricOrd.getChiamante()!=null && ricOrd.getChiamante().equals(mapping.getPath()))
 			{
				if (ricOrd!=null && ricOrd.getSelezioniChiamato()!=null && ricOrd.getSelezioniChiamato().size()!=0 )
				{
					if (ricOrd.getSelezioniChiamato().get(0).getChiave()!=null && ricOrd.getSelezioniChiamato().get(0).getChiave().length()!=0 )
					{
						//dal size aggiungo le righe selezionate e importateù
						if (buono.getListaOrdiniBuono()!=null && buono.getListaOrdiniBuono().size()>0)
						{
							for (int i=0; i<ricOrd.getSelezioniChiamato().size(); i++)
							{
								buono.getListaOrdiniBuono().add(ricOrd.getSelezioniChiamato().get(i));
								//oggettoBuo.getListaOrdiniBuono().get(i).setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto(ricOrd.getSelezioniChiamato().get(i).getPrezzoEuroOrdine()));
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
				session.removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_ORDINE);
				session.removeAttribute("ordiniSelected");
				session.removeAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE);

 			}
			// ***** FINE caricamenti liste supporto*********

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				buono.setCodBibl(bibScelta.getCod_bib());
			}

			currentForm.set("buono",buono);

			int totOrdini=0;
			if (buono.getListaOrdiniBuono()!=null && buono.getListaOrdiniBuono().size()>0)
			{
				totOrdini=buono.getListaOrdiniBuono().size();
			}
			currentForm.set("numOrdini",totOrdini);

			OrdiniVO[] oggettoDettBuo=new OrdiniVO[totOrdini];
			for (int i=0; i<totOrdini; i++)
			{
				oggettoDettBuo[i]=buono.getListaOrdiniBuono().get(i);
			}
			currentForm.set("elencaBuoni",oggettoDettBuo);

			/*
			// gestione elemento riga in inserimento
			int indiceRigaIns=999;
			if (creaBuono.get("eleIns")!=null)
			{
				indiceRigaIns=(Integer) creaBuono.get("eleIns");
				if (indiceRigaIns >=0 && indiceRigaIns!=999)
				{

					OrdiniVO[] appo2= (OrdiniVO[]) creaBuono.get("elencaBuoni");
					String tipoappo=appo2[indiceRigaIns].getTipoOrdine();

					String tipo=oggettoDettBuo[indiceRigaIns].getTipoOrdine();

					String anno=oggettoDettBuo[indiceRigaIns].getAnnoOrdine();
					String codice=oggettoDettBuo[indiceRigaIns].getCodOrdine();
					if (!tipo.equal("") && !anno.equal("") && !codice.equal(""))
					{
						String appo="cerca l'ordine e imposta gli altri campi";
					}
				}
			}
*/			// se viene richiamata come lista di supporto BUONI ORDINE  e non viene trovato nulla e si passa a crea
			ListaSuppBuoniOrdineVO ricArr=lsbo;
			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// imposta visibilità bottone scegli
				// perimpostazione di campi con elemento ricercato e non trovato
				BuoniOrdineVO oggBuo= (BuoniOrdineVO) currentForm.get("buono");
				oggBuo.setBilancio(ricArr.getBilancio());
				oggBuo.setFornitore(ricArr.getFornitore());
				currentForm.set("buono", oggBuo);
				currentForm.set("visibilitaIndietroLS",true);

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
			Navigation navi = Navigation.getInstance(request);
			if (navi.bookmarkExists(NavigazioneAcquisizioni.ORDINE_ATTIVO))
				return navi.goToBookmark(NavigazioneAcquisizioni.ORDINE_ATTIVO, false);

			ListaSuppBuoniOrdineVO ricArr=(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				ActionForward action = new ActionForward();
				action.setName("RITORNA");
				action.setPath(ricArr.getChiamante()+".do");
				return action;
			}
			else
			{
				return mapping.findForward("indietro");
			}

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward scegli(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBuonoOrdineForm currentForm = (InserisciBuonoOrdineForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppBuoniOrdineVO ricArr=(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
			if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						// carico i risultati della selezione nella variabile da restituire
							request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE, this.AggiornaRisultatiListaSupportoDaIns( currentForm,ricArr));
					}
					ActionForward action = new ActionForward();
					action.setName("RITORNA");
					action.setPath(ricArr.getChiamante()+".do");
					return action;
			}
			else
			{
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm creaBuono = (DynaValidatorForm) form;

		try {
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) creaBuono.get("elencaBuoni");
			BuoniOrdineVO appobuo=(BuoniOrdineVO)creaBuono.get("buono");
			OrdiniVO dettBuo = new OrdiniVO("", "", "", "", "", "", "", 0, true, "", "", "","", "", "",new StrutturaCombo("","") , "", "", new StrutturaTerna ("","",""), "","","", 0,0, "", "", "", "", new StrutturaCombo ("",""),"", "", "", "", "", "", 0, "", "", "",false,  false, "");
			dettBuo.setPrezzoEuroOrdineStr("0,00");
			if (elencaBuoni!=null &&  elencaBuoni.length!=0)
			{
				OrdiniVO[] listaDettBuo=new OrdiniVO[elencaBuoni.length +1];
				for (int i=0; i<elencaBuoni.length; i++)
				{
					listaDettBuo[i]=elencaBuoni[i];
				}
				listaDettBuo[elencaBuoni.length]=dettBuo;
				creaBuono.set("elencaBuoni",listaDettBuo);

				// trasformo in arraylist
				List lista2 =new ArrayList();
				for (int w=0;  w < listaDettBuo.length; w++)
				{
					OrdiniVO elem2 = listaDettBuo[w];
				    lista2.add(elem2);
				}
				appobuo.setListaOrdiniBuono(lista2);
				creaBuono.set("buono",appobuo);
				creaBuono.set("numOrdini",listaDettBuo.length);

				// gestione selezione della riga in inserimento
				Integer[]  radioOrd=new Integer[1];
				if (appobuo!=null && listaDettBuo.length>0)
				{
					radioOrd[0]=listaDettBuo.length-1;
					creaBuono.set("radioOrd",radioOrd);
				}

			}
			else
			{
				elencaBuoni=new OrdiniVO[1];
				elencaBuoni[0]=dettBuo;
				creaBuono.set("elencaBuoni",elencaBuoni);
				// trasformo in arraylist
				List lista2 =new ArrayList();
				for (int w=0;  w < elencaBuoni.length; w++)
				{
					OrdiniVO elem2 = elencaBuoni[w];
				    lista2.add(elem2);
				}
				appobuo.setListaOrdiniBuono(lista2);
				creaBuono.set("buono",appobuo);
				creaBuono.set("numOrdini",elencaBuoni.length);
				// gestione selezione della riga in inserimento
				Integer[]  radioOrd=new Integer[1];
				if (elencaBuoni!=null && elencaBuoni.length>0)
				{
					radioOrd[0]=elencaBuoni.length -1;
					creaBuono.set("radioOrd",radioOrd);
				}

			}

			//esaBilanci.set("radioImpegno",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm creaBuono = (DynaValidatorForm) form;
		try {
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) creaBuono.get("elencaBuoni");
			BuoniOrdineVO eleBuo=(BuoniOrdineVO)creaBuono.get("buono");
			Integer[] radioOrd=(Integer[])creaBuono.get("radioOrd");
			if (radioOrd.length!=0)
			{
				if (elencaBuoni.length!=0)
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

					creaBuono.set("elencaBuoni",lista_fin);
					creaBuono.set("radioOrd", null) ;
					creaBuono.set("numOrdini",lista_fin.length);
					 // mettere il controllo se si sono cancellate tutte le righe e il size è 1

					// trasformo in arraylist
					List lista2 =new ArrayList();
					for (int w=0;  w < lista_fin.length; w++)
					{
						OrdiniVO elem2 = lista_fin[w];
					    lista2.add(elem2);
					}
					eleBuo.setListaOrdiniBuono(lista2);
					creaBuono.set("buono",eleBuo);

					// se non sono presenti righe il bilancio si azzera
					if (lista_fin.length==0)
					{
						// fornitore editabile
						eleBuo.getFornitore().setCodice("");
						eleBuo.getFornitore().setDescrizione("");
						// bilancio editabile
						eleBuo.getBilancio().setCodice1("");
						eleBuo.getBilancio().setCodice2("");
						eleBuo.getBilancio().setCodice3("");
					}

					creaBuono.set("buono",eleBuo);

				}
			}
			creaBuono.set("radioOrd",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//InserisciBuonoOrdineForm creaBuono = (InserisciBuonoOrdineForm) form;
		DynaValidatorForm creaBuono = (DynaValidatorForm) form;

		try {
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) creaBuono.get("elencaBuoni");
			BuoniOrdineVO eleBuono=(BuoniOrdineVO)creaBuono.get("buono");

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
			creaBuono.set("buono",eleBuono);

			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaBuoniOrdineVO(eleBuono);
			// fine validazione

			((InserisciBuonoOrdineForm) creaBuono).setConferma(true);
			((InserisciBuonoOrdineForm) creaBuono).setPressioneBottone("salva");
			((InserisciBuonoOrdineForm) creaBuono).setDisabilitaTutto(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		}	catch (ValidationException ve) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

			((InserisciBuonoOrdineForm) creaBuono).setConferma(false);
			((InserisciBuonoOrdineForm) creaBuono).setPressioneBottone("");
			((InserisciBuonoOrdineForm) creaBuono).setDisabilitaTutto(false);
			return mapping.getInputForward();

		} catch (Exception e) {
			((InserisciBuonoOrdineForm) creaBuono).setConferma(false);
			((InserisciBuonoOrdineForm) creaBuono).setPressioneBottone("");
			((InserisciBuonoOrdineForm) creaBuono).setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBuonoOrdineForm creaBuono = (InserisciBuonoOrdineForm) form;
		try {

			creaBuono.setConferma(true);
			creaBuono.setPressioneBottone("cancella");
    		creaBuono.setDisabilitaTutto(true);
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.confermaOperazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			creaBuono.setConferma(false);
			creaBuono.setPressioneBottone("");
    		creaBuono.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}


	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBuonoOrdineForm creaBuono = (InserisciBuonoOrdineForm) form;
		try {

			// n.b. il ripristina in inserimento consiste nell'azzeramento se il record è inesistente,
			// mentre se si vuole effettuare modifica su un record appena inserito deve leggere
			BuoniOrdineVO eleBuo=(BuoniOrdineVO)creaBuono.get("buono");

			if ( eleBuo!=null && eleBuo.getNumBuonoOrdine()!=null && eleBuo.getNumBuonoOrdine().trim().length()>0)
			{
				// il record è già esistente
				// lettura
				BuoniOrdineVO eleBuonoLetto=this.loadDatiINS(eleBuo);


				creaBuono.set("buono",eleBuonoLetto);

				int totOrdini=0;
				if (eleBuonoLetto.getListaOrdiniBuono()!=null && eleBuonoLetto.getListaOrdiniBuono().size()>0)
				{
					totOrdini=eleBuonoLetto.getListaOrdiniBuono().size();
				}
				creaBuono.set("numOrdini",totOrdini);

				OrdiniVO[] oggettoDettBuo=new OrdiniVO[totOrdini];
				for (int i=0; i<totOrdini; i++)
				{
					oggettoDettBuo[i]=eleBuonoLetto.getListaOrdiniBuono().get(i);
				}
				creaBuono.set("elencaBuoni",oggettoDettBuo);

			}
			else
			{
				BuoniOrdineVO oggettoBuo=loadBuono();
				if (eleBuo!=null )
				{
					if (eleBuo.getCodBibl()!=null &&  eleBuo.getCodBibl().trim().length()>0)
					{
						oggettoBuo.setCodBibl(eleBuo.getCodBibl());
					}
					if (eleBuo.getCodPolo()!=null &&  eleBuo.getCodPolo().trim().length()>0)
					{
						oggettoBuo.setCodPolo(eleBuo.getCodPolo());
					}
				}
				creaBuono.set("buono",oggettoBuo);
				creaBuono.set("numOrdini",0);
				OrdiniVO[] oggettoDettBuo=new OrdiniVO[0];
				creaBuono.set("elencaBuoni",oggettoDettBuo);
			}




			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;
		try {
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	public ActionForward ordine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm creaBuono = (DynaValidatorForm) form;
		//dettBuonoOrd = (EsaminaBuonoOrdineForm) form;

		try {

			// CONTROLLO CHECK DI RIGA
/*			Integer[] radioOrd=(Integer[])creaBuono.get("radioOrd");

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
			BuoniOrdineVO oggettoBuo=(BuoniOrdineVO) creaBuono.get("buono");
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
				String tipoOrd=null; // A
				String annoOrd=null;
				String codOrd=null;

				if (creaBuono.get("radioOrd")!=null )
				{
					Integer[] radioOrd=(Integer[])creaBuono.get("radioOrd");
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
					//return mapping.findForward("ordine");
					if (request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE)!=null)
					{
						ListaSuppBuoniOrdineVO ele=(ListaSuppBuoniOrdineVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_BUONI_ORDINE);
						if (ele.getChiamante()!=null && ele.getChiamante().equals("/acquisizioni/ordini/esaminaOrdineMod"))
						{
							return mapping.findForward("ordineBo");
						}
					}
					return mapping.findForward("ordine");



		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward fornitoreCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBuonoOrdineForm creaBuono = (InserisciBuonoOrdineForm) form;
		try {
			ActionForward forward = fornitoreCercaVeloce(mapping, request, creaBuono);
			if (forward != null){
				return forward;
			}
			BuoniOrdineVO oggBuo= (BuoniOrdineVO) creaBuono.get("buono");
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
*/			this.impostaFornitoreCerca(request,mapping,oggBuo );
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
		//String biblio=Navigation.getInstance(request).getUtente().getCodBib();
		String biblio=oggBuo.getCodBibl();
		String codB=biblio;
		String codForn=oggBuo.getFornitore().getCodice();
		String nomeForn=oggBuo.getFornitore().getDescrizione();
		String codProfAcq="";
		String paeseForn="";
		String tipoPForn="";
		String provForn="";
		String loc="0";
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
			DynaValidatorForm creaBuono = (DynaValidatorForm) form;
/*			if (creaBuono.get("eleIns")!=null) // test sull'elemento appena inserito
			{
				int indiceRigaIns=999;
				indiceRigaIns=(Integer) creaBuono.get("eleIns");
				if (indiceRigaIns >=0 && indiceRigaIns!=999)
				{

*/
			Integer[] radioOrd=(Integer[])creaBuono.get("radioOrd");
			if (radioOrd.length!=0)
			{
				OrdiniVO[] elencaBuoni= (OrdiniVO[]) creaBuono.get("elencaBuoni");

				if (elencaBuoni.length!=0 )
				{
					//String appo="cerca l'ordine e imposta gli altri campi";
					BuoniOrdineVO eleBuo=(BuoniOrdineVO)creaBuono.get("buono");
					int indiceRigaIns= radioOrd[0]; // unico check
					String tipo=elencaBuoni[indiceRigaIns].getTipoOrdine();
					String anno=elencaBuoni[indiceRigaIns].getAnnoOrdine();
					String codice=elencaBuoni[indiceRigaIns].getCodOrdine();
					boolean trovato=false;
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

						eleRicerca=new ListaSuppOrdiniVO(codP,  codB, codBAff, codOrd,  annoOrd,  tipoOrd, dataOrdDa,dataOrdA,   cont, statoOrd,  forn,  tipoInvioOrd, bil,   sezioneAcqOrd,  tit,   dataFineAbbOrdDa, dataFineAbbOrdA,   naturaOrd,  chiama, statoOrdArr, rinn,stamp);
						String ticket=Navigation.getInstance(request).getUserTicket();
						eleRicerca.setTicket(ticket);

						//request.getSession().setAttribute(NavigazioneAcquisizioni.PARAMETRI_RICERCA_ORDINE, (ListaSuppOrdiniVO) eleRicerca);
						List<OrdiniVO> listaOrdini;

						try {
							listaOrdini=this.getListaOrdiniVO(eleRicerca); // va in errore se non ha risultati
							trovato=true;
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
					if (trovato)
					{
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
						creaBuono.set("buono",eleBuo);
					}
					else
					{
						// cancella riga
					}

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
		DynaValidatorForm creaBuono = (DynaValidatorForm) form;

		try {
			OrdiniVO[] elencaBuoni= (OrdiniVO[]) creaBuono.get("elencaBuoni");
			BuoniOrdineVO eleBuo=(BuoniOrdineVO)creaBuono.get("buono");
			eleBuo.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			String amount=eleBuo.getImportoStr().trim();
		    Double risult=Pulisci.ControllaImporto(amount);
		    eleBuo.setImporto(risult);
		    eleBuo.setImportoStr(Pulisci.VisualizzaImporto(eleBuo.getImporto()));


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
			eleBuo.setListaOrdiniBuono(lista);
			creaBuono.set("buono",eleBuo);
			((InserisciBuonoOrdineForm) creaBuono).setConferma(false);
			((InserisciBuonoOrdineForm) creaBuono).setDisabilitaTutto(false);


			if (((InserisciBuonoOrdineForm) creaBuono).getPressioneBottone().equals("salva")) {
				((InserisciBuonoOrdineForm) creaBuono).setPressioneBottone("");

				// se il codice ordine è già valorizzato si deve procedere alla modifica

				if (eleBuo.getIDBuonoOrd()!=0)
				{
					if (!this.modificaBuono(eleBuo)) {

						LinkableTagUtils.addError(request, new ActionMessage(
								"errors.acquisizioni.erroreModifica"));

						//return mapping.getInputForward();
					}
					else
					{

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.modificaOK"));

						return ripristina( mapping,  form,  request,  response);

					}

				}
				else
				{
					if (!this.inserisciBuono(eleBuo)) {

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.erroreinserimento"));

						//return mapping.getInputForward();
					}
					else
					{

						LinkableTagUtils.addError(request, new ActionMessage(
						"errors.acquisizioni.inserimentoOK"));

						return ripristina( mapping,  form,  request,  response);

					}

				}
			}
			if (((InserisciBuonoOrdineForm) creaBuono).getPressioneBottone().equals("cancella")) {
				((InserisciBuonoOrdineForm) creaBuono).setPressioneBottone("");
				if (!this.cancellaBuono(eleBuo)) {

					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.acquisizioni.erroreCancella"));

				}
				else
				{

					LinkableTagUtils.addError(request, new ActionMessage(
					"errors.acquisizioni.cancellaOK"));

				}

			}


			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			((InserisciBuonoOrdineForm) creaBuono).setConferma(false);
			((InserisciBuonoOrdineForm) creaBuono).setPressioneBottone("");
			((InserisciBuonoOrdineForm) creaBuono).setDisabilitaTutto(false);

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()),ve.getBid(), ve.getIsbd());
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
			((InserisciBuonoOrdineForm) creaBuono).setConferma(false);
			((InserisciBuonoOrdineForm) creaBuono).setPressioneBottone("");
			((InserisciBuonoOrdineForm) creaBuono).setDisabilitaTutto(false);

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InserisciBuonoOrdineForm creaBuono = (InserisciBuonoOrdineForm) form;
		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			creaBuono.setConferma(false);
			creaBuono.setPressioneBottone("");
    		creaBuono.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	/**
	 * InserisciBuonoOrdineAction.java
	 * @param eleRicArr
	 * @return
	 * Questo metodo riceve il VO della lista di supporto su cui sono presenti i criteri con cui si effettua la ricerca
	 * ed aggiunge nel campo SelezioniChiamato del VO solo l'array delle selezioni effettuata dall'utente fra i fornitori prodotti dalla ricerca
	 */
	private ListaSuppBuoniOrdineVO AggiornaRisultatiListaSupportoDaIns (InserisciBuonoOrdineForm creaBuono, ListaSuppBuoniOrdineVO eleRicArr)
	{
		try {
			List<BuoniOrdineVO> risultati=new ArrayList();
			BuoniOrdineVO eleBuono=(BuoniOrdineVO)creaBuono.get("buono");
			risultati.add(eleBuono);
			eleRicArr.setSelezioniChiamato(risultati);
		} catch (Exception e) {

		}
		return eleRicArr;
	}

	private BuoniOrdineVO loadBuono() throws Exception {
		Date dataodierna=new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataOdiernaStr =formato.format(dataodierna);
		BuoniOrdineVO buo =new BuoniOrdineVO ("","","", dataOdiernaStr,"A", new StrutturaCombo("", ""), new StrutturaTerna("", "", ""), 0.00, "" );
		buo.setImportoStr("0,00");
		buo.setListaOrdiniBuono(null);
		return buo;
	}


	private BuoniOrdineVO loadDatiINS(BuoniOrdineVO ele) throws Exception
	{

		BuoniOrdineVO eleLetto =null;

		ListaSuppBuoniOrdineVO eleRicerca=new ListaSuppBuoniOrdineVO();
		// carica i criteri di ricerca da passare alla esamina
		String codP=ele.getCodPolo();
		String codB=ele.getCodBibl();
		String numDa=ele.getNumBuonoOrdine();
		String numA=ele.getNumBuonoOrdine();
		String dataDa="";
		String dataA="";
		String stato="";
		StrutturaTerna ord=new StrutturaTerna("","","");
		StrutturaCombo forn=new StrutturaCombo("","");
		StrutturaTerna bil=new StrutturaTerna("","","");
		String chiama=null;
		String ordina="";

		eleRicerca=new ListaSuppBuoniOrdineVO(codP, codB, numDa, numA, dataDa, dataA, stato, ord, forn, bil, chiama, ordina );
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<BuoniOrdineVO> recordTrovati = new ArrayList();
		try {
			recordTrovati = factory.getGestioneAcquisizioni().getRicercaListaBuoniOrd(eleRicerca);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		if (recordTrovati.size()>0)
		{
			eleLetto=recordTrovati.get(0);

			try {
				eleLetto.setImportoStr(Pulisci.VisualizzaImporto(eleLetto.getImporto()));
				for (int w=0;  w < eleLetto.getListaOrdiniBuono().size(); w++)
				{
					//BilancioDettVO elem = bil.getDettagliBilancio().get(w);
					eleLetto.getListaOrdiniBuono().get(w).setPrezzoEuroOrdineStr(Pulisci.VisualizzaImporto( eleLetto.getListaOrdiniBuono().get(w).getPrezzoEuroOrdine()));
					//lista.add(elem);
				}

			} catch (Exception e) {
			    	//e.printStackTrace();
			    	//throw new ValidationException("importoErrato",
			    	//		ValidationExceptionCodici.importoErrato);
				eleLetto.setImportoStr("0,00");
			}
		}

		return eleLetto;
	}



	private boolean inserisciBuono(BuoniOrdineVO buono) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().inserisciBuonoOrd(buono);
		return valRitorno;
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

	private List<OrdiniVO> getListaOrdiniVO(ListaSuppOrdiniVO criRicerca) throws Exception
	{
	List<OrdiniVO> listaOrdini;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	listaOrdini = factory.getGestioneAcquisizioni().getRicercaListaOrdini(criRicerca);
	//this.sinCambio.setListaCambi(listaCambi);
	return listaOrdini;
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
		// impostazione delle variabili dinamiche

		return configurazioneTrovata;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, "SCEGLI")) {
			//almaviva5_20140624 #4631
			InserisciBuonoOrdineForm currentForm = (InserisciBuonoOrdineForm) form;
			ListaSuppBuoniOrdineVO params = currentForm.getParametri();
			return ValidazioneDati.in(params.getTipoOperazione(), TipoOperazione.CREA_DA_ORDINE);
		}
		return false;
	}

}
