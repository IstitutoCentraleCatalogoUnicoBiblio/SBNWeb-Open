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
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.profiliacquisto.ProfiliRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
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

public class ProfiliRicercaParzialeAction extends LookupDispatchAction implements SbnAttivitaChecker {
	//private ProfiliRicercaParzialeForm ricProfili;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.cerca","cerca");
		map.put("ricerca.button.crea","crea");
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.label.sezione","sezioneCerca");
		map.put("ricerca.label.bibliolist", "biblioCerca");
		return map;
	}

	public ActionForward biblioCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ProfiliRicercaParzialeForm ricProfili = (ProfiliRicercaParzialeForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_PROFILI_DI_ACQUISTO, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception 	{
		ProfiliRicercaParzialeForm ricProfili = (ProfiliRicercaParzialeForm) form;
		try {

			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if(!ricProfili.isSessione())
			{
				ricProfili.setSessione(true);
			}
			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.profili") && (ricProfili.getCodBibl()==null || (ricProfili.getCodBibl()!=null && ricProfili.getCodBibl().trim().length()==0)) && Navigation.getInstance(request).getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

/*				request.getSession().removeAttribute("attributeListaSuppProfiloVO");
				request.getSession().removeAttribute("profiliSelected");
				request.getSession().removeAttribute("criteriRicercaProfilo");
*/			}
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();

			if (biblio!=null &&   (ricProfili.getCodBibl()==null || (ricProfili.getCodBibl()!=null && ricProfili.getCodBibl().trim().length()==0)))
			{
				ricProfili.setCodBibl(biblio);
			}

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				ricProfili.setCodBibl(bibScelta.getCod_bib());
			}

			ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
			configurazioneCriteri.setCodBibl(ricProfili.getCodBibl());
			configurazioneCriteri.setCodPolo(polo);
			ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
			Boolean gestProf =true;
			if (configurazioneLetta!=null && !configurazioneLetta.isGestioneProfilo())
			{
				gestProf =configurazioneLetta.isGestioneProfilo();
			}

			if (gestProf!=null && !gestProf)
			{
				ricProfili.setGestProf(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.gestioneNonAmmessa"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}




			this.loadTipoOrdinamento( ricProfili);

			// condizioni di ricerca univoca
			if ( ricProfili.getCodProfilo()!=null &&  ricProfili.getCodProfilo().trim().length()!=0 )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				ricProfili.setCodSezione("");
				ricProfili.setDescrizioneProf("");
				ricProfili.setDisabilitaTutto(true);
			}
			else
			{
				ricProfili.setDisabilitaTutto(false);
			}

			//controllo se ho un risultato di una lista di supporto SEZIONI richiamata da questa pagina (risultato della simulazione)
			ListaSuppSezioneVO ricSez=(ListaSuppSezioneVO)request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
			if (ricSez!=null && ricSez.getChiamante()!=null && ricSez.getChiamante().equals(mapping.getPath()))
 			{
				if (ricSez!=null && ricSez.getSelezioniChiamato()!=null && ricSez.getSelezioniChiamato().size()!=0 )
				{
					if (ricSez.getSelezioniChiamato().get(0).getCodiceSezione()!=null && ricSez.getSelezioniChiamato().get(0).getCodiceSezione().length()!=0 )
					{
						ricProfili.setCodSezione(ricSez.getSelezioniChiamato().get(0).getCodiceSezione());
						ricProfili.setRientroDaSif(true);
					}
				}
				else
				{
					// pulizia della maschera di ricerca
					ricProfili.setCodSezione("");
				}

				// il reset dell'attributo di sessione deve avvenire solo dal chiamante
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");
				request.getSession().removeAttribute("criteriRicercaSezione");
 			}

			// controllo che non riceva l'attributo di sessione di una lista supporto
			ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");

			if (ricArr!=null && ricArr.getChiamante()!=null &&  (ricArr.getSelezioniChiamato()==null || ricArr.getChiamante().equals("/acquisizioni/fornitori/esaminaFornitore") ) )
			{
				// per il layout
				ricProfili.setVisibilitaIndietroLS(true);
				// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto

				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					ricProfili.setLSRicerca(true); // fai rox 2
				//}
				// per il layout fine

					if (ricArr.isModalitaSif())
					{
						List<StrutturaProfiloVO> listaProfili;
						listaProfili=this.getListaStrutturaProfiloVO(ricArr ); // va in errore se non ha risultati
						this.caricaAttributeListaSupp( ricProfili, ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
						return mapping.findForward("cerca");
					}
					else
					{
						if (!ricProfili.isRientroDaSif()) // per escludere il reset dal ritorno dei richiami di liste supporto effettuati da questa pagina
						{
							this.caricaAttributeListaSupp( ricProfili, ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
						}
						else
						{
							ricProfili.setRientroDaSif(false);
						}
						return mapping.getInputForward();
					}
			}
			else
			{
				return mapping.getInputForward();
			}

		}	catch (ValidationException ve) {
/*				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				this.saveErrors(request, errors);
*/				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaCambi=this.getListaCambiVO(ricArr )
				// assenzaRisultati = 4001;
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					ListaSuppProfiloVO ricArrRec=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
					this.caricaAttributeListaSupp( ricProfili, ricArrRec);
					//ricProfili.setVisibilitaIndietroLS(true);
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
						this.passaCriteri( ricProfili, request); // imposta il crea con i valori cercati
						return mapping.findForward("crea");
					}
*/
				}
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
		ProfiliRicercaParzialeForm ricProfili = (ProfiliRicercaParzialeForm) form;
		try {
			String chiama=null;
			//if (ricProfili.isLSRicerca())
			if (ricProfili.isVisibilitaIndietroLS())
			{
				ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
				chiama=ricArr.getChiamante();
			}

			// condizioni di ricerca univoca
			if ( ricProfili.getCodProfilo()!=null &&  ricProfili.getCodProfilo().trim().length()!=0 )
			{
				// ripulitura di tutti gli altri campi e disabilitazione
				ricProfili.setCodSezione("");
				ricProfili.setDescrizioneProf("");
			}


			String ticket=Navigation.getInstance(request).getUserTicket();
			request.getSession().removeAttribute("ultimoBloccoProfili");
			ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricProfili.getCodBibl();
			StrutturaCombo prof=new StrutturaCombo(ricProfili.getCodProfilo(),ricProfili.getDescrizioneProf());
			StrutturaCombo sez=new StrutturaCombo(ricProfili.getCodSezione(),"");
			StrutturaCombo lin=new StrutturaCombo("","");
			StrutturaCombo pae=new StrutturaCombo("","");
			//String chiama=null;
			String ordina=ricProfili.getTipoOrdinamSelez();
			eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae,  chiama, ordina );
			eleRicerca.setTicket(ticket);
			eleRicerca.setElemXBlocchi(ricProfili.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			if (ricProfili.getTipoOrdinamSelez()!=null && !ricProfili.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricProfili.getTipoOrdinamSelez());
			}
			request.getSession().setAttribute("attributeListaSuppProfiloVO", eleRicerca);

			List<StrutturaProfiloVO> listaProfili;
			if (ricProfili.getElemXBlocchi()>0)
			{
				request.setAttribute("numElexBlocchi",ricProfili.getElemXBlocchi());
			}
			listaProfili=this.getListaStrutturaProfiloVO(eleRicerca);

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
		ProfiliRicercaParzialeForm ricProfili = (ProfiliRicercaParzialeForm) form;
		try {
			this.passaCriteri( ricProfili, request);
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void passaCriteri(ProfiliRicercaParzialeForm ricProfili, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {
			String chiama=null;
			if (ricProfili.isLSRicerca())
			{
				ListaSuppProfiloVO ricArr=(ListaSuppProfiloVO) request.getSession().getAttribute("attributeListaSuppProfiloVO");
				chiama=ricArr.getChiamante();
			}

			String ticket=Navigation.getInstance(request).getUserTicket();
			ListaSuppProfiloVO eleRicerca=new ListaSuppProfiloVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricProfili.getCodBibl();
			StrutturaCombo prof=new StrutturaCombo("",ricProfili.getDescrizioneProf());
			StrutturaCombo sez=new StrutturaCombo(ricProfili.getCodSezione(),"");
			StrutturaCombo lin=new StrutturaCombo("","");
			StrutturaCombo pae=new StrutturaCombo("","");
			//String chiama=null;
			String ordina=ricProfili.getTipoOrdinamSelez();
			eleRicerca=new ListaSuppProfiloVO(codP, codB, prof, sez, lin, pae,  chiama, ordina );
			eleRicerca.setTicket(ticket);
			eleRicerca.setElemXBlocchi(ricProfili.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			request.getSession().setAttribute("ATTRIBUTEListaSuppProfiloVO", eleRicerca);

		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ProfiliRicercaParzialeForm ricProfili = (ProfiliRicercaParzialeForm) form;
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
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

		private List<StrutturaProfiloVO> getListaStrutturaProfiloVO(ListaSuppProfiloVO criRicerca) throws Exception
		{
		List<StrutturaProfiloVO> listaProfili;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		listaProfili = factory.getGestioneAcquisizioni().getRicercaListaProfili(criRicerca);
		return listaProfili;
		}

		private void caricaAttributeListaSupp(ProfiliRicercaParzialeForm ricProfili, ListaSuppProfiloVO  ricArr)
		{
		//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
		try {
			ricProfili.setCodBibl(ricArr.getCodBibl());
			ricProfili.setCodProfilo(ricArr.getProfilo().getCodice());
			ricProfili.setCodSezione(ricArr.getSezione().getCodice());
			ricProfili.setDescrizioneProf(ricArr.getProfilo().getDescrizione());

		}catch (Exception e) {	}
		}

		public ActionForward sezioneCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			ProfiliRicercaParzialeForm ricProfili = (ProfiliRicercaParzialeForm) form;
			try {
				// PULIZIA VARIABILI PREVENTIVA ALL'UTILIZZO LISTA SUPPORTO
				// && request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE)==null
				request.getSession().removeAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE);
				request.getSession().removeAttribute("sezioniSelected");
				request.getSession().removeAttribute("criteriRicercaSezione");

/*				if (request.getSession().getAttribute("criteriRicercaSezione")==null )
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
				this.impostaSezioneCerca( ricProfili,request,mapping);
				//return mapping.findForward("sezioneCerca");
				return mapping.findForward("sezioneLista");

			} catch (Exception e) {
				return mapping.getInputForward();
			}
		}
		private void impostaSezioneCerca( ProfiliRicercaParzialeForm ricProfili, HttpServletRequest request, ActionMapping mapping)
		{
		//impostazione di una lista di supporto
		try {
			ListaSuppSezioneVO eleRicerca=new ListaSuppSezioneVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricProfili.getCodBibl();
			String codSez=ricProfili.getCodSezione();
			String desSez="";
			String chiama=mapping.getPath();
			String ordina="";
			eleRicerca=new ListaSuppSezioneVO(codP,  codB,  codSez,  desSez , chiama, ordina);
			request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_SEZIONE, eleRicerca);
		}catch (Exception e) {	}
		}

		private void loadTipoOrdinamento(ProfiliRicercaParzialeForm ricProfili) throws Exception {
			List lista = new ArrayList();
/*			StrutturaCombo elem = new StrutturaCombo("","");
			lista.add(elem);
*/			StrutturaCombo elem = new StrutturaCombo("1","Cod. profilo");
			lista.add(elem);
			elem = new StrutturaCombo("2","Descrizione profilo");
			lista.add(elem);
			elem = new StrutturaCombo("3","Cod. sezione");
			lista.add(elem);

			ricProfili.setListaTipiOrdinam(lista);
		}
		public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
			if (idCheck.equals("CERCA") ){
				return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione

/*				Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				UserVO utente = Navigation.getInstance(request).getUtente();
				try {
					utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_PROFILI_DI_ACQUISTO, utente.getCodPolo(), utente.getCodBib(), null);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
					//return true; // temporaneamente per superare l'abilitazione negata a monte
				}
*/			}
			if (idCheck.equals("CREA") ){
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
