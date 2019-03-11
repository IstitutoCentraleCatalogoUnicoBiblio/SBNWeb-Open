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
package it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.GestioneCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.sezioniCollocazioni.SezioniCollocazioniListaForm;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.NavigazioneDocFisico;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * SezioniCollocazioniListaAction.java dic-2006
 *
 */
public class SezioniCollocazioniListaAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{

	private static Log log = LogFactory.getLog(SezioniCollocazioniListaAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("documentofisico.lsBib", "listaSupportoBib");
		map.put("button.blocco", "blocco");
		map.put("documentofisico.bottone.nuova", "nuova");
		map.put("documentofisico.bottone.modifica", "modifica");
		map.put("documentofisico.bottone.esamina", "esamina");
		map.put("documentofisico.bottone.cancella", "cancella");
		map.put("documentofisico.bottone.scegli", "ok");
		map.put("documentofisico.bottone.indietro", "chiudi");
		map.put("documentofisico.bottone.si", "si");
		map.put("documentofisico.bottone.no", "no");
		map.put("documentofisico.bottone.scegliSezione", "ok");
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		SezioniCollocazioniListaForm currentForm = (SezioniCollocazioniListaForm) form;
		if (!currentForm.isSessione()) {
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			try {
//				currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

			} catch (Exception e) {
				errors.clear();
				errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		this.saveToken(request);
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		// controllo se ho già i dati in sessione;
		UserVO utente = navi.getUtente();
		if (!myForm.isSessione()) {
			myForm.setTicket(navi.getUserTicket());
			myForm.setCodPolo(utente.getCodPolo());
			myForm.setCodBib(utente.getCodBib());
			myForm.setDescrBib(utente.getBiblioteca());
			log.info("SezioniCollocazioniListaAction::unspecified");
			loadDefault(request, mapping, form);
			myForm.setSessione(true);
		}
			try {
			// gestione richiamo lista sezioni da lente
			if (request.getAttribute("chiamante") != null) {
				myForm.setRichiamo("lista");
				myForm.setAction((String) request.getAttribute("chiamante"));
				myForm.setCodBib((String) request.getAttribute("codBib"));
				myForm.setDescrBib((String) request.getAttribute("descrBib"));
			}

			if (request.getAttribute("codBibDaLista") != null) {
				BibliotecaVO bib = (BibliotecaVO)request.getAttribute("codBibDaLista");
				request.setAttribute("codBib", bib.getCod_bib());
				request.setAttribute("descrBib", bib.getNom_biblioteca());
			}
			if (request.getAttribute("codBib") != null) {
				myForm.setCodBib((String) request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
			}
			List<SezioneCollocazioneVO> listaSez = this.getListaSezioni(myForm.getCodPolo(),
					myForm.getCodBib(),	myForm.getTicket(), myForm.getNRec(), form);

			if (listaSez == null || listaSez.size() < 1) {


				if (myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")) {
					if (!myForm.getCodBib().equals(utente.getCodBib())){
						myForm.setConferma(false);
						myForm.setListaSezioni(null);
						myForm.setTotRighe(0);
					}
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.listaSezVuota"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}else{
					Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
					utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_SEZIONI_DI_COLLOCAZIONE, myForm.getCodPolo(), myForm.getCodBib(), null);
					myForm.setNoSezione(true);
					request.setAttribute("codBib", myForm.getCodBib());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("prov", "nuova");
					if (!this.checkAttivita(request, myForm, "df")){
						return mapping.getInputForward();
					}else{
						return mapping.findForward("nuova");
					}
				}
			} else {
				if (listaSez.size() == 1){
					myForm.setSelectedSez("0");
				}
				myForm.setNoSezione(false);
				List listaBlocco = this.popolaListaPerBlocco((listaSez));
				myForm.setListaSezioni(listaBlocco);
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		} catch (DataException de) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ de.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		} catch (UtenteNotAuthorizedException e) {
			//??
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java nuova ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * Crea una nuova sezione di collocazione
	 */
	public ActionForward nuova(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		try {
			request.setAttribute("codBib", myForm.getCodBib());
			request.setAttribute("descrBib", myForm.getDescrBib());
			request.setAttribute("prov", "nuova");
			return mapping.findForward("nuova");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java modifica ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * modifica una sezione di collocazione a fronte di una selezione
	 */
	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		try {
			request.setAttribute("check", myForm.getSelectedSez());
			String check = myForm.getSelectedSez();
			int sezsel;
			if (check != null && check.length() != 0) {
				sezsel = Integer.parseInt(myForm.getSelectedSez());
				SezioneCollocazioneVO scelSez = (SezioneCollocazioneVO) myForm.getListaSezioni().get(sezsel);
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codSez", scelSez.getCodSezione().trim());
				request.setAttribute("prov", "modifica");
				return mapping.findForward("modifica");
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java esamina ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * esamina una sezione di collocazione a fronte di una selezione
	 */
	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		try {
			int sezSel;
			request.setAttribute("checkS", myForm.getSelectedSez());
			String check = myForm.getSelectedSez();
			if (check != null && check.length() != 0) {
				sezSel = Integer.parseInt(myForm.getSelectedSez());
				SezioneCollocazioneVO scelSez = (SezioneCollocazioneVO) myForm.getListaSezioni().get(sezSel);
				scelSez.setCodBib(myForm.getCodBib());
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codSez", scelSez.getCodSezione());
				request.setAttribute("prov", "esamina");
				return mapping.findForward("esamina");
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java cancella ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * cancella una sezione di collocazione a fronte di una selezione
	 */
	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		try {
			request.setAttribute("check", myForm.getSelectedSez());
			String check = myForm.getSelectedSez();
			int sezsel;
			if (check != null && check.length() != 0) {
				sezsel = Integer.parseInt(myForm.getSelectedSez());
				myForm.setRecSez((SezioneCollocazioneVO) myForm.getListaSezioni().get(sezsel));
//				if (cancSez.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)){
//					ActionMessages errors = new ActionMessages();
//					errors.add("generico", new ActionMessage("error.documentofisico.evolutivaFormatiSezioniInCorsoScelta"));
//					this.saveErrors(request, errors);
//					return mapping.getInputForward();
//				}else{
					if(myForm.getRecSez().getInventariCollocati() > 0){
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.erroreSezioneNonCancellabile"));
						this.saveErrors(request, errors);
						myForm.setConferma(false);
						return mapping.getInputForward();
					}else{
						ActionMessages errors = new ActionMessages();
						errors.add("generico", new ActionMessage("error.documentofisico.confermaCancellazione"));
						this.saveErrors(request, errors);
						this.saveToken(request);
						myForm.setDisable(true);
						myForm.setConferma(true);
						return mapping.getInputForward();
				}
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}
	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java ok ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * Conferma di scelta della sezione di collocazione selezionata
	 */
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		try {
				int sezSel;
				request.setAttribute("checkS", myForm.getSelectedSez());
				String checkS = myForm.getSelectedSez();
				if (checkS != null && checkS.length() != 0) {
					sezSel = Integer.parseInt(myForm.getSelectedSez());
					SezioneCollocazioneVO scelSez = (SezioneCollocazioneVO) myForm.getListaSezioni().get(sezSel);
					if (scelSez.getTipoColloc().equals(DocumentoFisicoCostant.COD_MAGAZZINO_A_FORMATO)
							|| scelSez.getTipoColloc().equals(DocumentoFisicoCostant.COD_CONTINUAZIONE)){
						List listaForSez = this.getListaFormatiSezioni(myForm.getCodPolo(), myForm.getCodBib(), scelSez.getCodSezione(), myForm.getTicket());
						if (listaForSez == null){
							ActionMessages errors = new ActionMessages();
							errors.add("noSelection", new ActionMessage("error.documentofisico.listaFormatiVuota"));
							this.saveErrors(request, errors);
							return mapping.getInputForward();
						}
					}
					request.setAttribute(NavigazioneDocFisico.SEZIONE_COLLOC_SELEZIONATA, scelSez);
					request.setAttribute("codBib", scelSez.getCodBib());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("codSez", scelSez.getCodSezione());
					request.setAttribute("codTipoColloc", scelSez.getTipoColloc());
					return Navigation.getInstance(request).goBack();
				} else {
					ActionMessages errors = new ActionMessages();
					errors.add("noSelection", new ActionMessage("error.documentofisico.noSelection"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java chiudi ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * richiesta di abbandono della pagina di lista sezioni senza scelte
	 */
	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		try {
			if (myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")) {
				request.setAttribute("listaSezioni", "listaSezioni");
				return Navigation.getInstance(request).goBack(true);
			}
			return mapping.findForward("chiudi");
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java listaSupportoBib ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * corrisponde al tasto che attiva la lista di supporto sezioni
	 */
	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_SEZIONI_DI_COLLOCAZIONE, myForm.getNRec(), "codBibDaLista");
			return biblio.getSIFListaBibliotecheAffiliatePerAttivita(richiesta );

		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java si ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * conferma di modifica a fronte di tasto ok per variazione dei dati di
	 * input
	 */
	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		try {
			myForm.getRecSez().setUteAgg(Navigation.getInstance(request).getUtente().getUserId());
			if (!this.deleteSezione(myForm.getRecSez(), myForm.getTicket())) {
//				ActionMessages errors = new ActionMessages();
//				errors.add("generico", new ActionMessage("error.documentofisico.erroreCancella"));
//				this.saveErrors(request, errors);
			} else {
				// ricarico la pagina con la lista aggiornata
				List<SezioneCollocazioneVO> listaSez = this.getListaSezioni(myForm.getCodPolo(),
						myForm.getCodBib(), myForm.getTicket(), myForm.getNRec(), form);
				if (listaSez == null || listaSez.size() <= 0) {
					request.setAttribute("codBib", myForm.getCodBib());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("prov", "nuova");
					return mapping.findForward("nuova");
				} else {
					myForm.setNoSezione(false);
					List listaBlocco = this.popolaListaPerBlocco((listaSez));
					myForm.setListaSezioni(listaBlocco);
					myForm.setDisable(false);
					myForm.setConferma(false);
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.codiceCancellazioneEffettuata"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		} catch (ValidationException e) {
			if (e.getMsg() != null) {
				if (e.getMsg().equals("msgSez")) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
					this.saveErrors(request, errors);
					this.saveToken(request);
					myForm.setDisable(true);
					myForm.setConferma(true);
					return mapping.getInputForward();
				} else if ((e.getMsg().equals("msgC"))) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
					this.saveErrors(request, errors);
					this.saveToken(request);
					myForm.setDisable(true);
					myForm.setConferma(true);
					return mapping.getInputForward();
				} else if ((e.getMsg().equals("msgGiaPresente"))) {
					this.saveToken(request);
					return mapping.findForward("chiudi");
				}
			}
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		} catch (DataException de) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ de.getMessage()));
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java no ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * richiesta di abbandono della pagina di conferma
	 */
	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		// Viene settato il token per le transazioni successive
		try {
				request.setAttribute("codBib", myForm.getCodBib());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codPolo", myForm.getCodPolo());
				myForm.setDisable(false);
				myForm.setConferma(false);
				return mapping.getInputForward();
//				return Navigation.getInstance(request).goBack(true);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		if (!myForm.isSessione()) {
			myForm.setSessione(true);
		}
		int numBlocco = myForm.getBloccoSelezionato();
		String idLista = myForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getGestioneDocumentoFisico().nextBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				List listaBlocco = this.popolaListaPerBlocco((bloccoVO.getLista()));
				myForm.getListaSezioni().addAll(listaBlocco);
				Collections.sort(myForm.getListaSezioni());
			}
		}
		return mapping.getInputForward();
	}

	private List popolaListaPerBlocco(List listaSez) throws Exception  {

		List<SezioneCollocazioneVO> lista = new ArrayList<SezioneCollocazioneVO>();
		for (int index = 0; index < listaSez.size(); index++) {
			SezioneCollocazioneVO sezione = (SezioneCollocazioneVO) listaSez.get(index);
			GestioneCodici codici = null;
			try {
				codici = this.getCodici();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}
			sezione.setDescrTipoSez(codici.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SEZIONE, sezione.getTipoSezione()));
			sezione.setDescrTipoColl(codici.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_COLLOCAZIONE, sezione.getTipoColloc()));
			lista.add(sezione);
		}
		return lista;
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java getListaSezioni List
	 *
	 * @param codBib
	 * @param nRec
	 * @param form
	 * @return
	 * @throws Exception
	 *
	 * servizio richiesta lista sezioni di collocazioni
	 */
	private List<SezioneCollocazioneVO> getListaSezioni(String codPolo,
			String codBib, String ticket, int nRec, ActionForm form)
			throws Exception {

		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaSezioni(codPolo,	codBib, ticket, nRec);
//		if (blocco1 == null || blocco1.getTotRighe() < 1) {
			if (blocco1 == null || blocco1.getTotRighe() <= 0) {
			myForm.setNoSezione(true);
			return null;
		} else {
			myForm.setNoSezione(false);
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setBloccoSelezionato(blocco1.getNumBlocco());
			myForm.setElemPerBlocchiSez(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)) {
				myForm.setAbilitaBottoneCarBlocchi(false);
			} else {
				myForm.setAbilitaBottoneCarBlocchi(true);
			}
			return blocco1.getLista();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.sezioniCollocazioni
	 * SezioniCollocazioniListaAction.java deleteSezione boolean
	 *
	 * @param sezione
	 * @return
	 * @throws Exception
	 *
	 * servizio richiesta di cancellazione logica di una sezione di collocazione
	 */
	private boolean deleteSezione(SezioneCollocazioneVO sezione, String ticket)
			throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().deleteSezione(sezione, ticket);
		return ret;
	}

	private GestioneCodici getCodici() throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		return factory.getCodici();
	}
	private List getListaFormatiSezioni(String codPolo, String codBib, String codSez, String ticket) throws Exception {
		List formati;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		formati = factory.getGestioneDocumentoFisico().getListaFormatiSezioni(codPolo, codBib, codSez, ticket);
		return formati;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		SezioniCollocazioniListaForm myForm = (SezioniCollocazioniListaForm) form;
		// gestione bottoni
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_SEZIONI_DI_COLLOCAZIONE, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}

	}
}
