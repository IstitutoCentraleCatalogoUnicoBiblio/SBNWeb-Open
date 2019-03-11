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
package it.iccu.sbn.web.actions.acquisizioni.sezioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.sezioni.SezioniRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
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

public class SezioniRicercaParzialeAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {
	//private SezioniRicercaParzialeForm ricSezioni;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.button.indietro","indietro");
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
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_SEZIONE_ACQUISIZIONI, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception 	{
		SezioniRicercaParzialeForm currentForm = (SezioniRicercaParzialeForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar() )
				return mapping.getInputForward();
			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}
			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.sezioni") && (currentForm.getCodBiblio()==null || (currentForm.getCodBiblio()!=null && currentForm.getCodBiblio().trim().length()==0)) && Navigation.getInstance(request).getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

/*				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");
				request.getSession().removeAttribute("criteriRicercaSezione");
*/			}

			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();

			if (biblio!=null &&   (currentForm.getCodBiblio()==null || (currentForm.getCodBiblio()!=null && currentForm.getCodBiblio().trim().length()==0)))
			{
				currentForm.setCodBiblio(biblio);
			}

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				currentForm.setCodBiblio(bibScelta.getCod_bib());
			}

			ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
			configurazioneCriteri.setCodBibl(currentForm.getCodBiblio());
			configurazioneCriteri.setCodPolo(polo);
			ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
			Boolean gestSez =true;
			if (configurazioneLetta!=null && !configurazioneLetta.isGestioneSezione())
			{
				gestSez =configurazioneLetta.isGestioneSezione();
			}

			if (gestSez!=null && !gestSez)
			{
				currentForm.setGestSez(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.gestioneNonAmmessa"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

			}




			this.loadTipoOrdinamento( currentForm);
			//this.loadBilio();

			ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// per il layout
				currentForm.setVisibilitaIndietroLS(true);
				// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					currentForm.setLSRicerca(true); // fai rox 2
				//}
				// per il layout fine
				if (ricArr.isModalitaSif())
				{
					this.getListaSezioniVO(ricArr, request );
					 // va in errore se non ha risultati
					this.caricaAttributeListaSupp( currentForm, ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					return mapping.findForward("cerca");
				}
				else
				{
					this.caricaAttributeListaSupp( currentForm, ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					return mapping.getInputForward();
				}

			}
			else
			{
				return mapping.getInputForward();
			}

		}	catch (ValidationException ve) {
/*
 				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
*/				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaCambi=this.getListaCambiVO(ricArr )
				// assenzaRisultati = 4001;
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					ListaSuppSezioneVO ricArrRec=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
					this.caricaAttributeListaSupp( currentForm, ricArrRec);
					//ricSezioni.setVisibilitaIndietroLS(true);
					//return mapping.getInputForward();
					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && ricArrRec.getChiamante().endsWith("RicercaParziale"))
					if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null )
					{
		 				// gestione della provenienza della lista di supporto da una schermata di RICERCA:
						// in tal caso la ricerca senza esito
						// non deve automaticamente presentare la maschera di crea ma emettere il messaggio
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
						this.saveErrors(request, errors);
						//return mapping.getInputForward();
					}
					return mapping.getInputForward();

/*					else
					{
						// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
						// in tal caso la ricerca senza esito
						// deve automaticamente presentare la maschera di crea
						this.passaCriteri( ricSezioni, request); // imposta il crea con i valori cercati
						return mapping.findForward("crea");
					}
*/				}
				else
				{
					return mapping.getInputForward();
				}
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

		public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SezioniRicercaParzialeForm currentForm = (SezioniRicercaParzialeForm) form;
			try {

				String chiama=null;
				//if (ricSezioni.isLSRicerca())
				if (currentForm.isVisibilitaIndietroLS())
				{
					ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
					chiama=ricArr.getChiamante();
				}

				ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
				// carica i criteri di ricerca da passare alla esamina
				request.getSession().removeAttribute("ultimoBloccoSezioni");
				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String codP=polo;
				String codB=currentForm.getCodBiblio();
				String codSez=currentForm.getCodSezione();
				String desSez=currentForm.getNomeSezione();
				//String chiama=null;
				String ordina=currentForm.getTipoOrdinamSelez();
				eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
				eleRicerca.setElemXBlocchi(currentForm.getElemXBlocchi());
				eleRicerca.setOrdinamento("");
				if (currentForm.getTipoOrdinamSelez()!=null && !currentForm.getTipoOrdinamSelez().equals(""))
				{
					eleRicerca.setOrdinamento(currentForm.getTipoOrdinamSelez());
				}
				if (currentForm.isSoloChiuse())
				{
					eleRicerca.setChiusura(true);
				}

				request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
				//ListaSuppCambioVO eleRicArr=ricerca.get(0);

				if (currentForm.getElemXBlocchi()>0)
				{
					request.setAttribute("numElexBlocchi",currentForm.getElemXBlocchi());
				}

				this.getListaSezioniVO(eleRicerca, request);
			return mapping.findForward("cerca");
			}	catch (ValidationException ve) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
			}
			// altri tipi di errore
			catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + e.getMessage()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			SezioniRicercaParzialeForm ricSezioni = (SezioniRicercaParzialeForm) form;
			try {
				this.passaCriteri( ricSezioni, request);
				return mapping.findForward("crea");
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			try {
				// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
				// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
				ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				if (ricArr!=null )
				{
					// gestione del chiamante
					if (ricArr!=null && ricArr.getChiamante()!=null)
					{
						ActionForward action = new ActionForward();
						action.setName("RITORNA");
						action.setPath(ricArr.getChiamante()+".do");
						return action;
					}
					else
					{
						return mapping.getInputForward();
					}
				}
				else
				{
					return mapping.getInputForward();
				}
			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}

		private List<SezioneVO> getListaSezioniVO(ListaSuppSezioneVO criRicerca, HttpServletRequest request) throws Exception
		{
		List<SezioneVO> listaSezioni;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		criRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunta per Docuemnto Fisico 09/05/08
		listaSezioni = factory.getGestioneAcquisizioni().getRicercaListaSezioni(criRicerca);
		// prova hibernate
		//listaSezioni = (List<SezioneVO>) factory.getGestioneAcquisizioni().getRicercaListaSezioniHib(criRicerca);
		//this.sinCambio.setListaCambi(listaBilanci);
		return listaSezioni;
		}


		private void caricaAttributeListaSupp(SezioniRicercaParzialeForm ricSezioni, ListaSuppSezioneVO  ricArr)
		{
		//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
		try {
			ricSezioni.setCodBiblio(ricArr.getCodBibl());
			ricSezioni.setCodSezione(ricArr.getCodiceSezione());
			ricSezioni.setNomeSezione(ricArr.getDescrizioneSezione());

		}catch (Exception e) {	}
		}



			private void passaCriteri(SezioniRicercaParzialeForm ricSezioni, HttpServletRequest request)
			{
				// caricamento dei criteri di ricerca per il crea
				try {
					String chiama=null;
					if (ricSezioni.isLSRicerca())
					{
						ListaSuppSezioneVO ricArr=(ListaSuppSezioneVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
						chiama=ricArr.getChiamante();
					}

				ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
				// carica i criteri di ricerca da passare alla esamina

				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				String codP=polo;
				String codB=ricSezioni.getCodBiblio();
				String codSez=ricSezioni.getCodSezione();
				String desSez=ricSezioni.getNomeSezione();
				//String chiama=null;
				String ordina="";
				eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
				eleRicerca.setElemXBlocchi(ricSezioni.getElemXBlocchi());
				eleRicerca.setOrdinamento("");
			    request.getSession().setAttribute("ATTRIBUTEListaSuppSezioneVO", eleRicerca);

				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}

/*	private void loadBilio() throws Exception {

		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("01","bilio01");
		lista.add(elem);
		elem = new StrutturaCombo("02","bilio01");
		lista.add(elem);
		elem = new StrutturaCombo("03","bilio01");
		lista.add(elem);
		elem = new StrutturaCombo("04","bilio01");
		lista.add(elem);

		ricSezioni.setListaCodBiblio(lista);
	}*/

	   private void loadTipoOrdinamento(SezioniRicercaParzialeForm ricSezioni) throws Exception {
			List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
/*			StrutturaCombo elem = new StrutturaCombo("","");
			StrutturaCombolista.add(elem);
*/			StrutturaCombo elem = new StrutturaCombo("1","Cod.");
			lista.add(elem);
			elem = new StrutturaCombo("2","Nome");
			lista.add(elem);
			ricSezioni.setListaTipiOrdinam(lista);
		}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione

/*			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_SEZIONE_ACQUISIZIONI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
*/		}
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_SEZIONE_ACQUISIZIONI, utente.getCodPolo(), utente.getCodBib(), null);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
				//return true; // temporaneamente per superare l'abilitazione negata a monte
			}
		}


		return false;
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


}
