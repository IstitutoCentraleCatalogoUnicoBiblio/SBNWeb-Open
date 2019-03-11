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
package it.iccu.sbn.web.actions.acquisizioni.profiliacquisto;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.profiliacquisto.EsaminaProfiloForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
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
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public class EsaminaProfiloAction extends LookupDispatchAction implements SbnAttivitaChecker {
	//private EsaminaProfiloForm esaProfilo;
	//private EsaminaProfiloForm esaProfiloNorm;



	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.salva","salva");
		map.put("ricerca.button.ripristina","ripristina");
		map.put("ricerca.button.cancella","cancella");
		map.put("ricerca.button.indietro","indietro");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		map.put("ricerca.button.scorriAvanti","scorriAvanti");
		map.put("ricerca.button.scorriIndietro","scorriIndietro");
//		map.put("ricerca.button.insRiga","inserisciRiga");
//		map.put("ricerca.button.cancRiga","cancellaRiga");
//		map.put("ricerca.button.stampa","stampa");
		map.put("ricerca.label.fornitori","fornitore");
		map.put("ricerca.label.sezione","sezioneCerca");
		map.put("ricerca.button.insForn","inserisciForn");
		map.put("ricerca.button.cancForn","cancellaForn");

		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		EsaminaProfiloForm esaProfiloNorm = (EsaminaProfiloForm) form;

		try {
			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if(!esaProfiloNorm.isSessione())
			{
				esaProfiloNorm.setSessione(true);
			}
			//String ticket=Navigation.getInstance(request).getUserTicket();

			// CONTROLLO SE E' RICHIAMATA COME LISTA DI SUPPORTO
			// DISABILITAZIONE DELL'INPUT
			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null  && ricArr.getChiamante()!=null)
			{
				esaProfiloNorm.setEsaminaInibito(true);
				esaProfiloNorm.setDisabilitaTutto(true);
			}

			esaProfiloNorm.setListaDaScorrere((List<ListaSuppProfiloVO>) request.getSession().getAttribute("criteriRicercaProfilo"));
			if(esaProfiloNorm.getListaDaScorrere() != null && esaProfiloNorm.getListaDaScorrere().size()!=0)
			{
				if(esaProfiloNorm.getListaDaScorrere().size()>1 )
				{
					esaProfiloNorm.setEnableScorrimento(true);
					//esaCambio.setPosizioneScorrimento(0);
					// carica ticket

				}
				else
				{
					esaProfiloNorm.setEnableScorrimento(false);
				}

				//this.loadAppo(resultckeck);
			}
			if (String.valueOf(esaProfiloNorm.getPosizioneScorrimento()).length()==0 )
			{
				esaProfiloNorm.setPosizioneScorrimento(0);
			}
			// richiamo ricerca su db con elemento 1 di ricerca
			//this.loadDatiOrdinePassato(esaordini.getListaDaScorrere().get(this.esaordini.getPosizioneScorrimento()));
			// reimposta la pagina con i valori immessi oppure carica la pagina iniziale


			DynaValidatorForm esaProfilo = (DynaValidatorForm) form;

			StrutturaProfiloVO oggettoProf;
			if (!esaProfiloNorm.isCaricamentoIniziale())
			{
				oggettoProf=this.loadDatiProfiloPassato(esaProfiloNorm.getListaDaScorrere().get(esaProfiloNorm.getPosizioneScorrimento()));
				esaProfiloNorm.setCaricamentoIniziale(true);
			}
			else
			{
				oggettoProf=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");
				// carico eventuali righe inserite
				StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");
				// trasformo in arraylist
				List lista =new ArrayList();

				for (int w=0;  w < elencaRigheProf.length; w++)
				{
					StrutturaTerna elem = elencaRigheProf[w];
					lista.add(elem);
				}
				oggettoProf.setListaFornitori(lista);
			}
			esaProfilo.set("datiProfilo",oggettoProf);

			/*
			StrutturaProfiloVO oggettoProf=this.loadDatiProfiloPassato(esaProfiloNorm.getListaDaScorrere().get(esaProfiloNorm.getPosizioneScorrimento()));


		// reimposta la fattura con i valori immessi precedentemente
			if (esaProfilo.get("datiProfilo")!=null )
			{
				StrutturaProfiloVO oggettoTemp=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");

				if (oggettoTemp.getCodBibl()!=null || (oggettoTemp.getProfilo()!=null && oggettoTemp.getProfilo().getCodice()!=null) || (oggettoTemp.getSezione()!=null && oggettoTemp.getSezione().getCodice()!=null) || (oggettoTemp.getPaese()!=null && oggettoTemp.getPaese().getCodice()!=null) || (oggettoTemp.getLingua()!=null && oggettoTemp.getLingua().getCodice()!=null)    )
				{
					oggettoProf=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");
					// carico eventuali righe inserite
					StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");
					// trasformo in arraylist
					List lista =new ArrayList();

					for (int w=0;  w < elencaRigheProf.length; w++)
					{
						StrutturaTerna elem = elencaRigheProf[w];
						lista.add(elem);
					}
					oggettoProf.setListaFornitori(lista);

				}
			}
			esaProfilo.set("datiProfilo",oggettoProf);
*/

			//controllo se ho un risultato di una lista di supporto SEZIONI richiamata da questa pagina (risultato della simulazione)
			ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))
 			{
				if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 )
				{
					if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )
					{
						oggettoProf.getSezione().setCodice(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
						oggettoProf.setIDSez(ricSez.getSelezioniChiamato().get(0).getIdSezione());
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					oggettoProf.getSezione().setCodice("");
				}
				// vedere se è necessaria l'impostazione
				esaProfilo.set("datiProfilo",oggettoProf);
				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");
				request.getSession().removeAttribute("criteriRicercaSezione");

 			}


			//controllo se ho un risultato di una lista di supporto FORNITORI richiamata da questa pagina (risultato della simulazione)

			ListaSuppFornitoreVO ricForn=(ListaSuppFornitoreVO)request.getSession().getAttribute("attributeListaSuppFornitoreVO");
			if (ricForn!=null && ricForn.getChiamante()!=null && ricForn.getChiamante().equals(mapping.getPath()))
 			{
				if (ricForn!=null && ricForn.getSelezioniChiamato()!=null && ricForn.getSelezioniChiamato().size()!=0 )
				{
					if (ricForn.getSelezioniChiamato().get(0).getCodFornitore()!=null && ricForn.getSelezioniChiamato().get(0).getCodFornitore().length()!=0 )
					{
						//dal size aggiungo le righe selezionate e importateù
						if (oggettoProf.getListaFornitori()!=null && oggettoProf.getListaFornitori().size()>0)
						{
							int indiceStart=oggettoProf.getListaFornitori().size();
							for (int i=0; i<ricForn.getSelezioniChiamato().size(); i++)
							{
								indiceStart=indiceStart+1;
								FornitoreVO eleRicForn = ricForn.getSelezioniChiamato().get(i);
								StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(indiceStart),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
								oggettoProf.getListaFornitori().add(eleListaForn);
							}

						}
						else
						{
							oggettoProf.setListaFornitori(new ArrayList());
							for (int i=0; i<ricForn.getSelezioniChiamato().size(); i++)
							{
								FornitoreVO eleRicForn = ricForn.getSelezioniChiamato().get(i);
								StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(i),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
								oggettoProf.getListaFornitori().add(eleListaForn);
							}
						}
						esaProfilo.set("datiProfilo",oggettoProf);
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
				request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
				request.getSession().removeAttribute("fornitoriSelected");
				request.getSession().removeAttribute("criteriRicercaFornitore");

 			}

			// reimposta la fattura con i valori immessi
/*			if (esaProfilo.get("datiProfilo")!=null )
			{
				StrutturaProfiloVO oggettoTemp=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");

				if (oggettoTemp.getProfilo()!=null && oggettoTemp.getCodBibl()!=null && oggettoTemp.getProfilo().getCodice()!=null   )
				{
					oggettoProf=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");
				}
			}*/

			esaProfilo.set("datiProfilo",oggettoProf);
			int totProfili=0;
			if (oggettoProf.getListaFornitori()!=null && oggettoProf.getListaFornitori().size()>0)
			{
				totProfili=oggettoProf.getListaFornitori().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			esaProfilo.set("numForn",totProfili);

			StrutturaTerna[] oggettoDettProf=new StrutturaTerna[totProfili];
			for (int i=0; i<totProfili; i++)
			{
				//FornitoreVO eleRicForn = (FornitoreVO) oggettoProf.getListaFornitori().get(i);
				//StrutturaTerna eleListaForn=new StrutturaTerna (String.valueOf(i+1),eleRicForn.getCodFornitore() ,eleRicForn.getNomeFornitore() );
				oggettoDettProf[i]=(StrutturaTerna) oggettoProf.getListaFornitori().get(i);

				//oggettoDettProf[i]=(StrutturaTerna) oggettoProf.getListaFornitori().get(i);
			}
			esaProfilo.set("elencaFornitori",oggettoDettProf);

			List arrListaPaesi=this.loadPaesi();
			esaProfilo.set("listaPaesi",arrListaPaesi);

			List arrListaLingue=this.loadLingue();
			esaProfilo.set("listaLingue",arrListaLingue);


			esaProfilo.set("enableScorrimento",esaProfiloNorm.isEnableScorrimento());



			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			//errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}

	public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm esaProfilo = (DynaValidatorForm) form;
		try {
			// aggiorno le righe eventuali
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			esaProfilo.set("datiProfilo",eleProfilo);
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE)==null
			request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			request.getSession().removeAttribute("sezioniSelected");
			request.getSession().removeAttribute("criteriRicercaSezione");

/*			if (request.getSession().getAttribute("criteriRicercaSezione")==null )
			{
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");
				request.getSession().removeAttribute("criteriRicercaSezione");
			}
			else
			{
				//throw new Exception("limite di ricorsione");
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			this.impostaSezioneCerca(request,mapping, eleProfilo);
			//return mapping.findForward("sezioneCerca");
			return mapping.findForward("sezioneLista");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	private void impostaSezioneCerca( HttpServletRequest request, ActionMapping mapping, StrutturaProfiloVO eleProfilo)
	{
	//impostazione di una lista di supporto
	try {
		ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
		// carica i criteri di ricerca da passare alla esamina
		String polo=Navigation.getInstance(request).getUtente().getCodPolo();
		String codP=polo;
		String codB=eleProfilo.getCodBibl();
		String codSez=eleProfilo.getSezione().getCodice();
		String desSez="";
		String chiama=mapping.getPath();
		String ordina="";
		eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
	}catch (Exception e) {	}
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaProfiloForm esaProfiloNorm = (EsaminaProfiloForm) form;

// 			DynaValidatorForm dettBuonoOrd = (DynaValidatorForm) form;

		try {
		return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward fornitore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		DynaValidatorForm esaProfilo = (DynaValidatorForm) form;

		try {

			// CONTROLLO CHECK DI RIGA
/*			Integer[] radioForn=(Integer[])esaProfilo.get("radioForn");

			if (radioForn.length!=0)
			{
				int indiceRigaIns= radioForn[0]; // unico check
			}
			else
			{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.buoniOrdine.crea"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
			}*/


			// aggiorno le righe eventuali
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			esaProfilo.set("datiProfilo",eleProfilo);

			// impongo il profilo,  il paese
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
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.ordineLimiteRicorsione" ));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
*/
			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=eleProfilo.getCodBibl();
			String codForn="";
			String nomeForn="";
			String codProfAcq="";
			String paeseForn="";
			String tipoPForn="";
			String provForn="";
			String loc="0"; // DEVO PESCARE SOLO I FORNITORI DI BIBLIOTECA
			String chiama=mapping.getPath();
			//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
			eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
			//ricerca.add(eleRicerca);
			request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);

			return mapping.findForward("fornitori");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


/*	public ActionForward inserisciRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm esaProfilo = (DynaValidatorForm) form;

		try {

			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");

			StrutturaTerna dettProf = new StrutturaTerna("1", "","");

			if (elencaRigheProf!=null &&  elencaRigheProf.length!=0)
			{
				StrutturaTerna[] listaDettProf=new StrutturaTerna[elencaRigheProf.length +1];

				for (int i=0; i<elencaRigheProf.length; i++)
				{
					listaDettProf[i]=elencaRigheProf[i];
				}
				// imposta numero di riga
				dettProf.setCodice1(String.valueOf(elencaRigheProf.length+1));
				listaDettProf[elencaRigheProf.length]=dettProf;
				esaProfilo.set("elencaFornitori",listaDettProf);
				esaProfilo.set("numForn",listaDettProf.length);
				// ricarica la fattura completamente
				List<StrutturaTerna> elencaRigheProfArr=new ArrayList();
				for (int i=0; i<listaDettProf.length; i++)
				{
					elencaRigheProfArr.add(listaDettProf[i]);
				}

			}
			else
			{
				elencaRigheProf=new StrutturaTerna[1];
				elencaRigheProf[0]=dettProf;
				esaProfilo.set("elencaFornitori",elencaRigheProf);
			}
			esaProfilo.set("numForn",elencaRigheProf.length);
			// trasformazione in arraylist



			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}*/

	public ActionForward inserisciForn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm esaProfilo = (DynaValidatorForm) form;

		try {

			// aggiorno le righe eventuali
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			esaProfilo.set("datiProfilo",eleProfilo);

			// impongo il profilo,  il paese
			// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
			// && request.getSession().getAttribute("attributeListaSuppFornitoreVO")==null
			request.getSession().removeAttribute("attributeListaSuppFornitoreVO");
			request.getSession().removeAttribute("fornitoriSelected");
			request.getSession().removeAttribute("criteriRicercaFornitore");

			ListaSuppFornitoreVO eleRicerca=new ListaSuppFornitoreVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=eleProfilo.getCodBibl();
			String codForn="";
			String nomeForn="";
			String codProfAcq="";
			String paeseForn="";
			String tipoPForn="";
			String provForn="";
			String loc="0"; // DEVO PESCARE SOLO I FORNITORI DI BIBLIOTECA
			String chiama=mapping.getPath();
			//String chiama="/acquisizioni/ordini/ordineRicercaParziale";
			eleRicerca=new ListaSuppFornitoreVO(codP,  codB, codForn, nomeForn, codProfAcq, paeseForn, tipoPForn, provForn, chiama, loc);
			//ricerca.add(eleRicerca);
			eleRicerca.setModalitaSif(false);
			request.getSession().setAttribute("attributeListaSuppFornitoreVO", eleRicerca);

			return mapping.findForward("fornitori");

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

/*	public ActionForward cancellaRiga(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm 	esaProfilo  = (DynaValidatorForm) form;
		try {
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");

			Integer[] radioForn=(Integer[])esaProfilo.get("radioForn");
			if (radioForn.length!=0)
			{
				if (elencaRigheProf.length!=0 )
				{
					List lista =new ArrayList();
					for (int t=0;  t < elencaRigheProf.length; t++)
					{
						StrutturaTerna elem = elencaRigheProf[t];
						lista.add(elem);
					}

					//String[] appo=  selectedDatiIntest;
					int i= (radioForn.length) -1;
					// ciclo dall'ultimo codice selezionato
					while (i>=0)
					{
						int elem = radioForn[i];
						// il valore del num riga è superiore di una unità rispetto all'indice dell'array
						lista.remove(elem);
						i=i-1;
					}

					//this.renumera(lista);


					StrutturaTerna[] lista_fin =new StrutturaTerna [lista.size()];

					for (int r=0;  r < lista.size(); r++)
					{
						lista_fin [r] = (StrutturaTerna)lista.get(r);
						lista_fin [r].setCodice1(String.valueOf(r+1)); //renumera
					}

					esaProfilo.set("elencaFornitori",lista_fin);
					esaProfilo.set("radioForn", null) ;
					esaProfilo.set("numForn",lista_fin.length);
					// ricarica la fattura completamente
					StrutturaProfiloVO appoProf=(StrutturaProfiloVO)esaProfilo.get("datiProfilo");
					List<StrutturaTerna> elencaRigheProfArr=new ArrayList();
					for (int j=0; j<lista_fin.length; j++)
					{
						elencaRigheProfArr.add(lista_fin[j]);
					}
					appoProf.setListaFornitori(elencaRigheProfArr);
					esaProfilo.set("datiProfilo",appoProf);

				}
			}
			esaProfilo.set("radioForn",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}*/

	public ActionForward cancellaForn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm 	esaProfilo  = (DynaValidatorForm) form;
		try {
			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");

			Integer[] radioForn=(Integer[])esaProfilo.get("radioForn");
			if (radioForn.length!=0)
			{
				if (elencaRigheProf.length!=0 )
				{
					List lista =new ArrayList();
					for (int t=0;  t < elencaRigheProf.length; t++)
					{
						StrutturaTerna elem = elencaRigheProf[t];
						lista.add(elem);
					}

					//String[] appo=  selectedDatiIntest;
					int i= (radioForn.length) -1;
					// ciclo dall'ultimo codice selezionato
					while (i>=0)
					{
						int elem = radioForn[i];
						// il valore del num riga è superiore di una unità rispetto all'indice dell'array
						lista.remove(elem);
						i=i-1;
					}

					//this.renumera(lista);


					StrutturaTerna[] lista_fin =new StrutturaTerna [lista.size()];

					for (int r=0;  r < lista.size(); r++)
					{
						lista_fin [r] = (StrutturaTerna)lista.get(r);
						lista_fin [r].setCodice1(String.valueOf(r+1)); //renumera
					}

					esaProfilo.set("elencaFornitori",lista_fin);
					esaProfilo.set("radioForn", null) ;
					esaProfilo.set("numForn",lista_fin.length);
					// ricarica la fattura completamente
					StrutturaProfiloVO appoProf=(StrutturaProfiloVO)esaProfilo.get("datiProfilo");
					List<StrutturaTerna> elencaRigheProfArr=new ArrayList();
					for (int j=0; j<lista_fin.length; j++)
					{
						elencaRigheProfArr.add(lista_fin[j]);
					}
					appoProf.setListaFornitori(elencaRigheProfArr);
					esaProfilo.set("datiProfilo",appoProf);

				}
			}
			esaProfilo.set("radioForn",0);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		//EsaminaProfiloForm esaProfilo = (EsaminaProfiloForm) form;
		DynaValidatorForm esaProfilo  = (DynaValidatorForm) form;
		try {

			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			esaProfilo.set("datiProfilo",eleProfilo);

			// validazione
			boolean valRitorno = false;
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			factory.getGestioneAcquisizioni().ValidaStrutturaProfiloVO(eleProfilo);
			// fine validazione

			ActionMessages errors = new ActionMessages();
			((EsaminaProfiloForm) esaProfilo).setConferma(true);
			((EsaminaProfiloForm) esaProfilo).setPressioneBottone("salva");
			((EsaminaProfiloForm) esaProfilo).setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();

		}	catch (ValidationException ve) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
			this.saveErrors(request, errors);
			((EsaminaProfiloForm) esaProfilo).setConferma(false);
			((EsaminaProfiloForm) esaProfilo).setPressioneBottone("");
			((EsaminaProfiloForm) esaProfilo).setDisabilitaTutto(false);
			return mapping.getInputForward();

		} catch (Exception e) {
			((EsaminaProfiloForm) esaProfilo).setConferma(false);
			((EsaminaProfiloForm) esaProfilo).setPressioneBottone("");
			((EsaminaProfiloForm) esaProfilo).setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}

	public ActionForward ripristina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaProfiloForm esaProfilo = (EsaminaProfiloForm) form;

		try {
			StrutturaProfiloVO oggettoProf=loadDatiProfiloPassato(esaProfilo.getListaDaScorrere().get(esaProfilo.getPosizioneScorrimento()));
			esaProfilo.set("datiProfilo",oggettoProf);
			esaProfilo.set("numForn",0);
			int totRigheProfilo=0;
			if (oggettoProf.getListaFornitori()!=null && oggettoProf.getListaFornitori().size()>0)
			{
				totRigheProfilo=oggettoProf.getListaFornitori().size();
			}
			//this.insBilanci.setNumImpegni(totImpegni);
			esaProfilo.set("numForn",totRigheProfilo);

			StrutturaTerna[] oggettoDettProf=new StrutturaTerna[totRigheProfilo];
			for (int i=0; i<totRigheProfilo; i++)
			{
				oggettoDettProf[i]=(StrutturaTerna) oggettoProf.getListaFornitori().get(i);
			}
			esaProfilo.set("elencaFornitori",oggettoDettProf);

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DynaValidatorForm esaProfilo  = (DynaValidatorForm) form;
		try {

			StrutturaTerna[] elencaRigheProf= (StrutturaTerna[]) esaProfilo.get("elencaFornitori");
			StrutturaProfiloVO eleProfilo=(StrutturaProfiloVO) esaProfilo.get("datiProfilo");
			eleProfilo.setUtente(Navigation.getInstance(request).getUtente().getFirmaUtente());

			// trasformo in arraylist
			List lista =new ArrayList();
			for (int w=0;  w < elencaRigheProf.length; w++)
			{
				StrutturaTerna elem = elencaRigheProf[w];
				lista.add(elem);
			}
			eleProfilo.setListaFornitori(lista);
			esaProfilo.set("datiProfilo",eleProfilo);

			((EsaminaProfiloForm) esaProfilo).setConferma(false);
			((EsaminaProfiloForm) esaProfilo).setDisabilitaTutto(false);

			if (((EsaminaProfiloForm) esaProfilo).getPressioneBottone().equals("salva")) {
				((EsaminaProfiloForm) esaProfilo).setPressioneBottone("");


				if (!this.modificaProfilo(eleProfilo)) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
							"errors.acquisizioni.erroreModifica"));
					this.saveErrors(request, errors);
					//return mapping.getInputForward();
				}
				else
				{
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage(
					"errors.acquisizioni.modificaOK"));
					this.saveErrors(request, errors);
					return ripristina( mapping,  form,  request,  response);

				}

			}
			if (((EsaminaProfiloForm) esaProfilo).getPressioneBottone().equals("cancella")) {
				((EsaminaProfiloForm) esaProfilo).setPressioneBottone("");
					if (!this.cancellaProfilo(eleProfilo)) {
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
								"errors.acquisizioni.erroreCancella"));
						this.saveErrors(request, errors);
					}
					else
					{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage(
						"errors.acquisizioni.cancellaOK"));
						this.saveErrors(request, errors);
						((EsaminaProfiloForm) esaProfilo).setDisabilitaTutto(true);
					}
			}
			return mapping.getInputForward();
		}	catch (ValidationException ve) {
			((EsaminaProfiloForm) esaProfilo).setConferma(false);
			((EsaminaProfiloForm) esaProfilo).setPressioneBottone("");
			((EsaminaProfiloForm) esaProfilo).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

		// altri tipi di errore
		} catch (Exception e) {
			((EsaminaProfiloForm) esaProfilo).setConferma(false);
			((EsaminaProfiloForm) esaProfilo).setPressioneBottone("");
    		((EsaminaProfiloForm) esaProfilo).setDisabilitaTutto(false);
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaProfiloForm esaProfilo = (EsaminaProfiloForm) form;

		try {
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			esaProfilo.setConferma(false);
			esaProfilo.setPressioneBottone("");
    		esaProfilo.setDisabilitaTutto(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private boolean modificaProfilo(StrutturaProfiloVO profilo) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().modificaProfilo(profilo);
		return valRitorno;
	}

	private boolean cancellaProfilo(StrutturaProfiloVO profilo) throws Exception {
		boolean valRitorno = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		valRitorno = factory.getGestioneAcquisizioni().cancellaProfilo(profilo);
		return valRitorno;
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaProfiloForm esaProfilo = (EsaminaProfiloForm) form;
		EsaminaProfiloForm esaProfiloNorm = (EsaminaProfiloForm) form;

		try {
			int attualePosizione=esaProfiloNorm.getPosizioneScorrimento()+1;
			int dimensione=esaProfiloNorm.getListaDaScorrere().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					try
					{
						StrutturaProfiloVO oggettoProf=loadDatiProfiloPassato(esaProfilo.getListaDaScorrere().get(attualePosizione));
						esaProfilo.set("datiProfilo",oggettoProf);
						esaProfilo.set("numForn",0);
						int totProfili=0;
						if (oggettoProf.getListaFornitori()!=null && oggettoProf.getListaFornitori().size()>0)
						{
							totProfili=oggettoProf.getListaFornitori().size();
						}
						//this.insBilanci.setNumImpegni(totImpegni);
						esaProfilo.set("numForn",totProfili);

						StrutturaTerna[] oggettoDettProf=new StrutturaTerna[totProfili];
						for (int i=0; i<totProfili; i++)
						{
							oggettoDettProf[i]=(StrutturaTerna) oggettoProf.getListaFornitori().get(i);
						}
						esaProfilo.set("elencaFornitori",oggettoDettProf);

					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							esaProfiloNorm.setPosizioneScorrimento(attualePosizione);
							return scorriAvanti( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
					esaProfiloNorm.setPosizioneScorrimento(attualePosizione);
					// aggiornamento del tab di visualizzazione dei dati per tipo ordine
					esaProfilo.setDisabilitaTutto(false);
					if (esaProfilo.isEsaminaInibito())
					{
						esaProfilo.setDisabilitaTutto(true);
					}

				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaProfiloForm esaProfilo = (EsaminaProfiloForm) form;
		EsaminaProfiloForm esaProfiloNorm = (EsaminaProfiloForm) form;

		try {
			int attualePosizione=esaProfiloNorm.getPosizioneScorrimento()-1;
			int dimensione=esaProfiloNorm.getListaDaScorrere().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					try
					{
						//this.loadDatiBilancioPassato(esaBilanci.getListaDaScorrere().get(attualePosizione));
						StrutturaProfiloVO oggettoProf=loadDatiProfiloPassato(esaProfilo.getListaDaScorrere().get(attualePosizione));
						esaProfilo.set("datiProfilo",oggettoProf);
						esaProfilo.set("numForn",0);
						int totProfili=0;
						if (oggettoProf.getListaFornitori()!=null && oggettoProf.getListaFornitori().size()>0)
						{
							totProfili=oggettoProf.getListaFornitori().size();
						}
						//this.insBilanci.setNumImpegni(totImpegni);
						esaProfilo.set("numForn",totProfili);

						StrutturaTerna[] oggettoDettProf=new StrutturaTerna[totProfili];
						for (int i=0; i<totProfili; i++)
						{
							oggettoDettProf[i]=(StrutturaTerna) oggettoProf.getListaFornitori().get(i);
						}
						esaProfilo.set("elencaFornitori",oggettoDettProf);

					} catch (ValidationException ve) {
						// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaOrdini=this.getListaOrdiniVO(ricArr ))
						// assenzaRisultati = 4001;
						if (ve.getError()==4001)
						{
							// passa indietro perchè l'elemento è stato cancellato
							esaProfiloNorm.setPosizioneScorrimento(attualePosizione);
							return scorriIndietro( mapping,  form,  request,  response);
						}
						return mapping.getInputForward();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}


					esaProfiloNorm.setPosizioneScorrimento(attualePosizione);
					esaProfilo.setDisabilitaTutto(false);
					if (esaProfilo.isEsaminaInibito())
					{
						esaProfilo.setDisabilitaTutto(true);
					}

				}

			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}




	public ActionForward cancella(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EsaminaProfiloForm esaProfilo = (EsaminaProfiloForm) form;

		try {
			ActionMessages errors = new ActionMessages();
			esaProfilo.setConferma(true);
			esaProfilo.setPressioneBottone("cancella");
    		esaProfilo.setDisabilitaTutto(true);
			errors.add("generico", new ActionMessage("errors.acquisizioni.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return mapping.getInputForward();
		} catch (Exception e) {
			esaProfilo.setConferma(false);
			esaProfilo.setPressioneBottone("");
    		esaProfilo.setDisabilitaTutto(false);
			return mapping.getInputForward();
		}
	}


	private StrutturaProfiloVO loadDatiProfiloPassato(ListaSuppProfiloVO criteriRicercaProfilo) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		List<StrutturaProfiloVO> profiliTrovati = new ArrayList();
		profiliTrovati = factory.getGestioneAcquisizioni().getRicercaListaProfili(criteriRicercaProfilo);
		//gestire l'esistenza del risultato e che sia univoco
		//this.esaBilanci.setBilancio(bilanciTrovato.get(0));
		//for (int i=0; i<bilanciTrovato.size(); i++)
		//this.esaBilanci.setListaImpegni(bilanciTrovato.get(0).getDettagliBilancio());
		StrutturaProfiloVO prof=profiliTrovati.get(0);
		return prof;
	}

/*	private void loadProfili() throws Exception {
		List lista = new ArrayList();

		StrutturaCombo forn = new StrutturaCombo("19", "Libreria Casalini");
		lista.add(forn);

		forn = new StrutturaCombo("22", "Libreria KAPPA");
		lista.add(forn);

		forn = new StrutturaCombo("23", "Libreria Laterza");
		lista.add(forn);

		forn = new StrutturaCombo("33", "Libreria Grande");
		lista.add(forn);

		forn = new StrutturaCombo("13", "Libreria Feltrinelli");
		lista.add(forn);

		esaProfilo.setElencaFornitori(lista);
	}*/

	private List loadLingue() throws Exception {
    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceLingua());
		return lista;


/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("ITA","ITA - Italiano");
		lista.add(elem);
		elem = new StrutturaCombo("ENG","ENG - Inglese");
		lista.add(elem);
		elem = new StrutturaCombo("FRE","FRE - Francese");
		lista.add(elem);
		elem = new StrutturaCombo("JPN","JPN - Giapponese");
		lista.add(elem);

		//esaProfilo.setListaLingue(lista);
		return lista;
*/	}


	private List loadPaesi() throws Exception {

    	List lista = new ArrayList();
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
		lista=carCombo.loadComboCodiciDesc (factory.getCodici().getCodicePaese());
		return lista;

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("GESTIONE") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_PROFILI_DI_ACQUISTO, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}

		return false;
	}

}
