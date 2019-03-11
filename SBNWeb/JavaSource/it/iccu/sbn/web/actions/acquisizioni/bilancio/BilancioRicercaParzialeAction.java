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
package it.iccu.sbn.web.actions.acquisizioni.bilancio;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.bilancio.BilancioRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
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

public class BilancioRicercaParzialeAction extends SinteticaLookupDispatchAction   implements SbnAttivitaChecker{
	//private BilancioRicercaParzialeForm ricBilanci;


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
		BilancioRicercaParzialeForm ricBilanci = (BilancioRicercaParzialeForm) form;
		try {
	        FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	        UserVO utente = Navigation.getInstance(request).getUtente();
	        BibliotecaDelegate delegate = new BibliotecaDelegate(factory, request);
	        SIFListaBibliotecheAffiliatePerAttivitaVO richiesta =
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_BILANCIO, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception 	{
		BilancioRicercaParzialeForm ricBilanci = (BilancioRicercaParzialeForm) form;
		try {

			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if(!ricBilanci.isSessione())
			{
				ricBilanci.setSessione(true);
			}
			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.bilancio") && (ricBilanci.getCodBibl()==null || (ricBilanci.getCodBibl()!=null && ricBilanci.getCodBibl().trim().length()==0)) && Navigation.getInstance(request).getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);

/*				request.getSession().removeAttribute("attributeListaSuppBilancioVO");
				request.getSession().removeAttribute("bilanciSelected");
				request.getSession().removeAttribute("criteriRicercaBilancio");
*/			}

			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();

			if (biblio!=null && (ricBilanci.getCodBibl()==null || (ricBilanci.getCodBibl()!=null && ricBilanci.getCodBibl().trim().length()==0)))
			{
				ricBilanci.setCodBibl(biblio);
			}

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				ricBilanci.setCodBibl(bibScelta.getCod_bib());
			}


			ConfigurazioneORDVO configurazioneCriteri = new ConfigurazioneORDVO();
			configurazioneCriteri.setCodBibl(ricBilanci.getCodBibl());
			configurazioneCriteri.setCodPolo(polo);
			ConfigurazioneORDVO configurazioneLetta=this.loadConfigurazioneORD(configurazioneCriteri);
			Boolean gestBil =true;
			if (configurazioneLetta!=null && !configurazioneLetta.isGestioneBilancio())
			{
				gestBil =configurazioneLetta.isGestioneBilancio();
			}

			if (gestBil!=null && !gestBil)
			{
				ricBilanci.setGestBil(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("errors.acquisizioni.gestioneNonAmmessa"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}


			this.loadTipoImpegno( ricBilanci);
			this.loadTipoOrdinamento(ricBilanci);

			/*
			// test inizio per simulare l'accesso a lista supporto cambi richiamato dalla pagina cambiricercaparziale
			if (request.getSession().getAttribute("attributeListaSuppBilancioVO")==null)
			{
				this.caricaParametroTest(request, mapping);
			}
			// test fine

*/
			ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
	/*	//controllo se ho un risultato di una lista di supporto richiamata da questa pagina (risultato della simulazione)
			if (ricArr!=null && ricArr.size()!=0 && ricArr.get(0).getSelezioniChiamato()!=null)
			{
				if (ricArr.size()>0)
				{
					this.ricCambi.setValuta(ricArr.get(0).getCodValuta());
				}

			}
			else
			{
				this.ricCambi.setValuta("");
			}
	*/
			// controllo che non riceva l'attributo di sessione di una lista supporto
			if (ricArr!=null &&  ricArr.getSelezioniChiamato()==null && ricArr.getChiamante()!=null)
			{
				// per il layout
				ricBilanci.setVisibilitaIndietroLS(true);
				// il bottone crea su ricerca non deve essere visibile in caso di lista di supporto
				//if (ricArr.getChiamante().endsWith("RicercaParziale"))
				//{
					ricBilanci.setLSRicerca(true); // fai rox 2
				//}
				// per il layout fine
					if (ricArr.isModalitaSif())
					{
						List<BilancioVO> listaBilanci;
						listaBilanci=this.getListaBilanciVO(ricArr,request ); // va in errore se non ha risultati
						this.caricaAttributeListaSupp(ricBilanci,ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
						return mapping.findForward("cerca");
					}
					else
					{
						this.caricaAttributeListaSupp(ricBilanci,ricArr); // fai rox 1
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
					ListaSuppBilancioVO ricArrRec=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
					this.caricaAttributeListaSupp(ricBilanci,ricArrRec);
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
						this.passaCriteri( ricBilanci, request); // imposta il crea con i valori cercati
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
		BilancioRicercaParzialeForm ricBilanci = (BilancioRicercaParzialeForm) form;
		try {
			// fai rox 3
			String chiama=null;
			//if (ricBilanci.isLSRicerca())
			if (ricBilanci.isVisibilitaIndietroLS())
			{
				ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
				chiama=ricArr.getChiamante();
			}

			ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
			request.getSession().removeAttribute("ultimoBloccoBilanci");
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricBilanci.getCodBibl();
			String ese=ricBilanci.getEsercizio();
			String cap=ricBilanci.getCapitolo();
			String imp=ricBilanci.getTipoImpegno();
			String ordina=ricBilanci.getTipoOrdinamSelez();
			eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
			eleRicerca.setElemXBlocchi(ricBilanci.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			//String chiama=null;

			if (ricBilanci.getTipoOrdinamSelez()!=null && !ricBilanci.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(ricBilanci.getTipoOrdinamSelez());
			}
			request.getSession().setAttribute("attributeListaSuppBilancioVO", eleRicerca);
			//ListaSuppCambioVO eleRicArr=ricerca.get(0);
			List<BilancioVO> listaBilanci;
			if (ricBilanci.getElemXBlocchi()>0)
			{
				request.setAttribute("numElexBlocchi",ricBilanci.getElemXBlocchi());
			}
			listaBilanci=this.getListaBilanciVO(eleRicerca, request);
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
		BilancioRicercaParzialeForm ricBilanci = (BilancioRicercaParzialeForm) form;
		try {
			this.passaCriteri( ricBilanci, request);
			return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void passaCriteri(BilancioRicercaParzialeForm ricBilanci, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {

			String chiama=null;
			if (ricBilanci.isLSRicerca())
			{
				ListaSuppBilancioVO ricArr=(ListaSuppBilancioVO) request.getSession().getAttribute("attributeListaSuppBilancioVO");
				chiama=ricArr.getChiamante();
			}

			ListaSuppBilancioVO eleRicerca=new ListaSuppBilancioVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=ricBilanci.getCodBibl();
			String ese=ricBilanci.getEsercizio();
			String cap=ricBilanci.getCapitolo();
			String imp="";
			//String chiama=null;
			String ordina="";
			eleRicerca=new ListaSuppBilancioVO(codP,  codB,  ese,  cap , imp,  chiama, ordina);
			eleRicerca.setElemXBlocchi(ricBilanci.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			request.getSession().setAttribute("ATTRIBUTEListaSuppBilancioVO", eleRicerca);


		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		BilancioRicercaParzialeForm ricBilanci = (BilancioRicercaParzialeForm) form;
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

	private List<BilancioVO> getListaBilanciVO(ListaSuppBilancioVO criRicerca, HttpServletRequest request) throws Exception
	{
	List<BilancioVO> listaBilanci;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	//criRicerca.setLoc(request.getLocale()); // aggiunto per Documento Fisico 09/05/08
	criRicerca.setLoc(this.getLocale(request, Constants.SBN_LOCALE)); // aggiunto per Documento Fisico 27/05/08 insieme al SinteticaLookupDispatchAction invece di LookupDispatchAction

	listaBilanci = factory.getGestioneAcquisizioni().getRicercaListaBilanci(criRicerca);
	//this.sinCambio.setListaCambi(listaBilanci);
	return listaBilanci;
	}

    private void loadTipoImpegno(BilancioRicercaParzialeForm ricBilanci) throws Exception {
    	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
    	CaricamentoCombo carCombo = new CaricamentoCombo();
    	ricBilanci.setListaTipoImpegno(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceTipoMateriale()));
	}

	private void caricaAttributeListaSupp(BilancioRicercaParzialeForm ricBilanci,ListaSuppBilancioVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto

	try {
		ricBilanci.setCodBibl(ricArr.getCodBibl());
		ricBilanci.setEsercizio(ricArr.getEsercizio());
		ricBilanci.setCapitolo(ricArr.getCapitolo());
		ricBilanci.setTipoImpegno(ricArr.getImpegno());
	}catch (Exception e) {	}
	}

	private void loadTipoOrdinamento(BilancioRicercaParzialeForm ricBilanci) throws Exception {

		List lista = new ArrayList();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		StrutturaCombo elem = new StrutturaCombo("1","Esercizio (disc.) - Capitolo");
		lista.add(elem);
		elem = new StrutturaCombo("2","Capitolo - Esercizio (disc.)");
		lista.add(elem);
		ricBilanci.setListaTipiOrdinam(lista);
	}
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione
/*
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_INTERROGAZIONE_BILANCIO, utente.getCodPolo(), utente.getCodBib(), null);
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
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_BILANCIO, utente.getCodPolo(), utente.getCodBib(), null);
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
