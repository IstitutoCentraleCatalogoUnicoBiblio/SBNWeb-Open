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
package it.iccu.sbn.web.actions.acquisizioni.cambi;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.acquisizioni.cambi.CambiRicercaParzialeForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.constant.NavigazioneAcquisizioni;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
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
import org.apache.struts.actions.LookupDispatchAction;


public class CambiRicercaParzialeAction extends LookupDispatchAction implements SbnAttivitaChecker {

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
	            new	SIFListaBibliotecheAffiliatePerAttivitaVO(utente.getCodPolo(),utente.getCodBib(), CodiciAttivita.getIstance().GA_INTERROGAZIONE_CAMBI, 10, "codBibDalista");
	        return	delegate.getSIFListaBibliotecheAffiliatePerAttivita(richiesta);

		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CambiRicercaParzialeForm currentForm = (CambiRicercaParzialeForm) form;

		try {

			//setto il token per le transazioni successive
			this.saveToken(request);
			if (Navigation.getInstance(request).isFromBar() )
	            return mapping.getInputForward();
			if(!currentForm.isSessione())
			{
				currentForm.setSessione(true);
			}
			if (request.getSession().getAttribute(Constants.CURRENT_MENU).equals("menu.acquisizioni.cambi") && (currentForm.getCodBibl()==null || (currentForm.getCodBibl()!=null && currentForm.getCodBibl().trim().length()==0)) &&  Navigation.getInstance(request).getActionCaller()==null)
			{
				// si proviene dal menu
				Pulisci.PulisciVar(request);
			}
			this.loadValuta( currentForm);
			this.loadTipoOrdinamento( currentForm);

			//currentForm.setValuta("");
			String ticket=Navigation.getInstance(request).getUserTicket();
			// cod bibl da caricare (Navigation.getInstance(request).getUtente().getCodBib());
			String biblio=Navigation.getInstance(request).getUtente().getCodBib();
			if (biblio!=null && (currentForm.getCodBibl()==null  || (currentForm.getCodBibl()!=null && currentForm.getCodBibl().trim().length()==0)))
			{
				currentForm.setCodBibl(biblio);
			}

			BibliotecaVO  bibScelta=(BibliotecaVO) request.getAttribute("codBibDalista");
			if (bibScelta!=null && bibScelta.getCod_bib()!=null)
			{
				currentForm.setCodBibl(bibScelta.getCod_bib());
			}

			//this.checkForm(request);

/*
			// test inizio per simulare l'accesso a lista supporto cambi richiamato dalla pagina cambiricercaparziale
			if (request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI)==null)
			{
				this.caricaParametroTest(request, mapping);
			}
			// test fine

*/
			ListaSuppCambioVO ricArr=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);

	/*	//controllo se ho un risultato di una lista di supporto richiamata da questa pagina (risultato della simulazione)
			if (ricArr!=null && ricArr.size()!=0 && ricArr.get(0).getSelezioniChiamato()!=null)
			{
				if (ricArr.size()>0)
				{
					currentForm.setValuta(ricArr.get(0).getCodValuta());
				}

			}
			else
			{
				currentForm.setValuta("");
			}
	*/
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


				String polo=Navigation.getInstance(request).getUtente().getCodPolo();
				ricArr.setCodPolo(polo);

				if (ricArr.isModalitaSif())
				{
					List<CambioVO> listaCambi;
					listaCambi=this.getListaCambiVO(ricArr ); // va in errore se non ha risultati
					this.caricaAttributeListaSupp( currentForm,ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					return mapping.findForward("cerca");
				}
				else
				{
					this.caricaAttributeListaSupp( currentForm,ricArr); // IMPOSTA CRITERI DI RICERCA SULLA PAGINA
					return mapping.getInputForward();
				}


			}
			else
			{
				return mapping.getInputForward();
			}
		}	catch (ValidationException ve) {
				//
				//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));
				//
				// impostazione nel caso ci sia assenza  di  risultati (va in errore l'istr. listaCambi=this.getListaCambiVO(ricArr )
				// assenzaRisultati = 4001;
				if (ve.getError()==4001)
				{
					// impostazione visibilità bottone indietro e della pagina di ricerca con i criteri
					ListaSuppCambioVO ricArrRec=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
					this.caricaAttributeListaSupp( currentForm,ricArrRec);
					currentForm.setVisibilitaIndietroLS(true);
					//return mapping.getInputForward();
					//si richiede che si presenti la maschera di crea  ed eliminazione messaggio non trovato
					//return mapping.findForward("crea");
					//if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null && ricArrRec.getChiamante().endsWith("RicercaParziale"))
					if (ricArrRec!=null &&  ricArrRec.getSelezioniChiamato()==null && ricArrRec.getChiamante()!=null )
					{
		 				// gestione della provenienza della lista di supporto da una schermata di RICERCA:
						// in tal caso la ricerca senza esito
						// non deve automaticamente presentare la maschera di crea ma emettere il messaggio

						LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

						//return mapping.getInputForward();
					}
					return mapping.getInputForward();

/*					else
					{
						// gestione della provenienza della lista di supporto da una schermata di esamina o inserisci
						// in tal caso la ricerca senza esito
						// deve automaticamente presentare la maschera di crea
						this.passaCriteri( ricCambi, request); // imposta il crea con i valori cercati
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

			//LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));
			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni.erroreGenericoAcquisizioni"));

			return mapping.getInputForward();
		}

	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		CambiRicercaParzialeForm currentForm = (CambiRicercaParzialeForm) form;
		try {
			String chiama=null;
			if (currentForm.isVisibilitaIndietroLS())

			{
				ListaSuppCambioVO ricArr=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
				chiama=ricArr.getChiamante();
			}


			ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
			request.getSession().removeAttribute("ultimoBloccoCambi");
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=currentForm.getCodBibl();
			String codVal=currentForm.getValuta();
			String desVal=currentForm.getDesValuta();
			//String chiama=null;
			eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
			eleRicerca.setElemXBlocchi(currentForm.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			if (currentForm.getTipoOrdinamSelez()!=null && !currentForm.getTipoOrdinamSelez().equals(""))
			{
				eleRicerca.setOrdinamento(currentForm.getTipoOrdinamSelez());
			}

			//ricerca.add(eleRicerca);
		request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI, eleRicerca);
		//ListaSuppCambioVO eleRicArr=ricerca.get(0);
		List<CambioVO> listaCambi = this.getListaCambiVO(eleRicerca);
		return mapping.findForward("cerca");
		}	catch (ValidationException ve) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + ve.getMessage()));

				return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.acquisizioni." + e.getMessage()));

			return mapping.getInputForward();
		}
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		CambiRicercaParzialeForm currentForm = (CambiRicercaParzialeForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
				if(!currentForm.isSessione())
				{
					currentForm.setSessione(true);
				}
				return mapping.getInputForward();
			}
			//	this.checkForm(request);
		resetToken(request);
		this.passaCriteri( currentForm, request);
		return mapping.findForward("crea");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private void passaCriteri(CambiRicercaParzialeForm currentForm, HttpServletRequest request)
	{
		// caricamento dei criteri di ricerca per il crea
		try {
			String chiama=null;
			if (currentForm.isLSRicerca())
			{
				ListaSuppCambioVO ricArr=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
				chiama=ricArr.getChiamante();
			}

			ListaSuppCambioVO eleRicerca=new ListaSuppCambioVO();
			// carica i criteri di ricerca da passare alla esamina
			String polo=Navigation.getInstance(request).getUtente().getCodPolo();
			String codP=polo;
			String codB=currentForm.getCodBibl();
			String codVal=currentForm.getValuta();
			String desVal=currentForm.getDesValuta();
			//String chiama=null;
			eleRicerca=new ListaSuppCambioVO(codP,  codB,  codVal,  desVal ,  chiama);
			eleRicerca.setElemXBlocchi(currentForm.getElemXBlocchi());
			eleRicerca.setOrdinamento("");
			//ricerca.add(eleRicerca);
			request.getSession().setAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI, eleRicerca);


		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		try {
			// l'azione di indietro della ricerca torna al chiamante se è stata invocata la lista di supporto, altrimenti non è visibile il bottone
			// aggiornamento dell'attributo lista di supporto ricerca con i risultati della sintetica ottenuta
			ListaSuppCambioVO ricArr=(ListaSuppCambioVO) request.getSession().getAttribute(NavigazioneAcquisizioni.LISTA_SUPPORTO_CAMBI);
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


	private void loadValuta(CambiRicercaParzialeForm currentForm) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		CaricamentoCombo carCombo = new CaricamentoCombo();
		//carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta());
		currentForm.setListaValuta(carCombo.loadComboCodiciDesc (factory.getCodici().getCodiceValuta()));
/*		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
		elem = new StrutturaCombo("AFA","AFA - afghani");
		lista.add(elem);
		elem = new StrutturaCombo("EUR","EUR - euro");
		lista.add(elem);
		elem = new StrutturaCombo("NZD","NZD - dollaro neozelandese");
		lista.add(elem);
		currentForm.setListaValuta(lista);
*/	}

	private List<CambioVO> getListaCambiVO(ListaSuppCambioVO criRicerca) throws Exception
	{
	List<CambioVO> listaCambi;
	FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
	//listaCambi = (List<CambioVO>) factory.getGestioneAcquisizioni().getRicercaListaCambi(criRicerca);
	// prova hibernate
	listaCambi = factory.getGestioneAcquisizioni().getRicercaListaCambiHib(criRicerca);
	//currentForm.setListaCambi(listaCambi);

	// per aggiungere un elemento vuoto

	//CambioVO elem = new CambioVO("","", null, null, 0, null, null);
	//listaCambi.add(elem);


	return listaCambi;
	}

	private void caricaAttributeListaSupp(CambiRicercaParzialeForm currentForm,ListaSuppCambioVO  ricArr)
	{
	//caricamento della pagina di ricerca con i criteri forniti dalla lista di supporto
	try {
		currentForm.setCodBibl(ricArr.getCodBibl());
		currentForm.setValuta(ricArr.getCodValuta());
		currentForm.setDesValuta(ricArr.getDesValuta());
	}catch (Exception e) {	}
	}


	private void loadTipoOrdinamento(CambiRicercaParzialeForm currentForm) throws Exception {
		List lista = new ArrayList();
/*		StrutturaCombo elem = new StrutturaCombo("","");
		lista.add(elem);
*/		StrutturaCombo elem = new StrutturaCombo("1","Codice valuta");
		lista.add(elem);
		elem = new StrutturaCombo("2","Descrizione");
		lista.add(elem);

		currentForm.setListaTipiOrdinam(lista);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (idCheck.equals("CERCA") ){
			return true; // temporaneamente per non considerare l'abilitazione sull'interrogazione
		}
		if (idCheck.equals("CREA") ){
			Utente utenteEJB = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			UserVO utente = Navigation.getInstance(request).getUtente();
			try {
				utenteEJB.checkAttivita(CodiciAttivita.getIstance().GA_GESTIONE_CAMBI, utente.getCodPolo(), utente.getCodBib(), null);
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
