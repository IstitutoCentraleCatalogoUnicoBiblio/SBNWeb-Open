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
package it.iccu.sbn.web.actions.documentofisico.modelliEtichette;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.GestioneCodici;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.sif.SIFListaBibliotecheAffiliatePerAttivitaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.documentofisico.modelliEtichette.ModelliEtichetteListaForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * ModelliEtichetteListaAction.java dic-2006
 *
 */
public class ModelliEtichetteListaAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker{

	private static Log log = LogFactory.getLog(ModelliEtichetteListaAction.class);

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
		return map;
	}

	private ActionForward loadDefault(HttpServletRequest request,
			ActionMapping mapping, ActionForm form) {
		ActionMessages errors = new ActionMessages();
		ModelliEtichetteListaForm currentForm = (ModelliEtichetteListaForm) form;
		if (Navigation.getInstance(request).getUtente().getCodBib().equals(currentForm.getCodBib())){
			if (!currentForm.isSessione()) {
				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				try {
					currentForm.setNRec(Integer.valueOf((String) utenteEjb.getDefault(ConstantDefault.ELEMENTI_BLOCCHI)));

				} catch (Exception e) {
					errors.clear();
					errors.add("noSelection", new ActionMessage("error.documentofisico.erroreDefault"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}
		}
		return mapping.getInputForward();
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		HttpSession httpSession = request.getSession();
		this.saveToken(request);
		if (Navigation.getInstance(request).isFromBar() ){
			return mapping.getInputForward();
		}

		// controllo se ho già i dati in sessione;
		if (!myForm.isSessione()) {
			myForm.setTicket(Navigation.getInstance(request).getUserTicket());
			myForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
			myForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
			myForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
			log.info("ModelliEtichetteListaAction::unspecified");
			loadDefault(request, mapping, form);
			myForm.setSessione(true);
		}
		try {
			// gestione richiamo lista modelli da lente
			if (request.getAttribute("prov") != null && request.getAttribute("prov").equals("listaSupportoModelli")) {
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
				// provengo dalla lista biblioteche
				// carico la lista relativa al codice selezionato
				myForm.setCodBib((String) request.getAttribute("codBib"));
				myForm.setDescrBib((String)request.getAttribute("descrBib"));
			}
			List<ModelloEtichetteVO> listaModello = this.getListaModelli(myForm.getCodPolo(),
					myForm.getCodBib(),	myForm.getTicket(), myForm.getNRec(), form);

			if (listaModello == null || listaModello.size() < 1) {

				Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
				//almaviva5_20090907
				//utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE, myForm.getCodPolo(), myForm.getCodBib(), null);
				utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_GESTIONE_MODELLI_ETICHETTE, myForm.getCodPolo(), myForm.getCodBib(), null);

				if (myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")) {
					ActionMessages errors = new ActionMessages();
					errors.add("generico", new ActionMessage("error.documentofisico.listaModelliVuota"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}else{
					myForm.setNoModello(true);
					request.setAttribute("codBib", myForm.getCodBib());
//					request.setAttribute("descrBib", Navigation.getInstance(request).getUtente().getBiblioteca());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("prov", "nuova");
					return mapping.findForward("nuova");
				}
			} else {
				if (listaModello.size() == 1){
					myForm.setSelectedModello("0");
				}else{
					myForm.setSelectedModello("");
				}
				myForm.setNoModello(false);
				List listaBlocco = this.popolaListaPerBlocco((listaModello));
				myForm.setListaModelli(listaBlocco);
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			if (myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")){
				return Navigation.getInstance(request).goBack(true);
			}else{
				return mapping.getInputForward();
			}
		} catch (it.iccu.sbn.ejb.exception.DataException de) {
			if (de.getMessage().equals("listaModelliVuota")){
				myForm.setConferma(false);
//				if (myForm.getListaModelli() == null || myForm.getListaModelli() .size() <= 0) {
				myForm.setListaModelli(null);
				myForm.setTotRighe(0);
					request.setAttribute("codBib", myForm.getCodBib());
//					request.setAttribute("descrBib", Navigation.getInstance(request).getUtente().getBiblioteca());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("prov", "nuova");
					return mapping.findForward("nuova");
//				}
			}else{
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico."+ de.getMessage()));
				this.saveErrors(request, errors);
				myForm.setConferma(false);
				return mapping.getInputForward();
			}
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java nuova ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * Crea una nuova modello di etichette
	 */
	public ActionForward nuova(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		try {
			request.setAttribute("codBib", myForm.getCodBib());
//			request.setAttribute("descrBib", Navigation.getInstance(request).getUtente().getBiblioteca());
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java modifica ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * modifica una modello di etichette a fronte di una selezione
	 */
	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		try {
			request.setAttribute("check", myForm.getSelectedModello());
			String check = myForm.getSelectedModello();
			int modsel;
			if (check != null && check.length() != 0) {
				modsel = Integer.parseInt(myForm.getSelectedModello());
				ModelloEtichetteVO scelMod = (ModelloEtichetteVO) myForm.getListaModelli().get(modsel);
				request.setAttribute("codBib", myForm.getCodBib());
//				request.setAttribute("descrBib", Navigation.getInstance(request).getUtente().getBiblioteca());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codModello", scelMod.getCodModello());
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java esamina ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * esamina un modello a fronte di una selezione
	 */
	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		try {
			int modSel;
			request.setAttribute("checkS", myForm.getSelectedModello());
			String check = myForm.getSelectedModello();
			if (check != null && check.length() != 0) {
				modSel = Integer.parseInt(myForm.getSelectedModello());
				ModelloEtichetteVO scelMod = (ModelloEtichetteVO) myForm.getListaModelli().get(modSel);
				scelMod.setCodBib(myForm.getCodBib());
				request.setAttribute("codBib", myForm.getCodBib());
//				request.setAttribute("descrBib", Navigation.getInstance(request).getUtente().getBiblioteca());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codModello", scelMod.getCodModello());
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java cancella ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * cancella un modello a fronte di una selezione
	 */
	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		try {
			request.setAttribute("check", myForm.getSelectedModello());
			String check = myForm.getSelectedModello();
			int modsel;
			if (check != null && check.length() != 0) {
				modsel = Integer.parseInt(myForm.getSelectedModello());
				myForm.setRecModello((ModelloEtichetteVO) myForm.getListaModelli().get(modsel));
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.confermaCancellazione"));
				this.saveErrors(request, errors);
				this.saveToken(request);
				myForm.setConferma(true);
				return mapping.getInputForward();
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java ok ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * Conferma di scelta del modello selezionato
	 */
	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		try {
			int modSel;
			request.setAttribute("checkS", myForm.getSelectedModello());
			String checkS = myForm.getSelectedModello();
			if (checkS != null && checkS.length() != 0) {
				modSel = Integer.parseInt(myForm.getSelectedModello());
				ModelloEtichetteVO scelModello = (ModelloEtichetteVO) myForm.getListaModelli().get(modSel);
				request.setAttribute("codBib", myForm.getCodBib());
//				request.setAttribute("descrBib", Navigation.getInstance(request).getUtente().getBiblioteca());
				request.setAttribute("descrBib", myForm.getDescrBib());
				request.setAttribute("codModello", scelModello.getCodModello().trim());
				request.setAttribute("prov", "listaSupportoModelli");
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java chiudi ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * richiesta di abbandono della pagina di lista modelli senza scelte
	 */
	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		try {
			if (myForm.getRichiamo() != null && myForm.getRichiamo().equals("lista")) {
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java listaSupportoBib ActionForward
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * corrisponde al tasto che attiva la lista di supporto modelli
	 */
	public ActionForward listaSupportoBib(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			BibliotecaDelegate biblio = new BibliotecaDelegate(factory, request);
			SIFListaBibliotecheAffiliatePerAttivitaVO richiesta = new SIFListaBibliotecheAffiliatePerAttivitaVO(Navigation.getInstance(request).getUtente().getCodPolo(),
					Navigation.getInstance(request).getUtente().getCodBib(),
					CodiciAttivita.getIstance().GDF_ETICHETTE, myForm.getNRec(), "codBibDaLista");
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java si ActionForward
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
		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		try {
			myForm.getRecModello().setCodPolo(myForm.getCodPolo());
			myForm.getRecModello().setCodBib(myForm.getCodBib());
			myForm.getRecModello().setUteAgg(Navigation.getInstance(request).getUtente().getUserId());
			if (!this.deleteModello(myForm.getRecModello(), myForm.getTicket())) {
			} else {
				// ricarico la pagina con la lista aggiornata
				List<ModelloEtichetteVO> listaModelli = this.getListaModelli(myForm.getCodPolo(),
						myForm.getCodBib(), myForm.getTicket(), myForm.getNRec(), form);
				myForm.setListaModelli(listaModelli);
				myForm.setConferma(false);
				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage("error.documentofisico.codiceCancellazioneEffettuata"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);
			this.saveErrors(request, errors);
			myForm.setConferma(false);
			return mapping.getInputForward();
		} catch (DataException de) {
			if (de.getMessage().equals("listaModelliVuota")){
				myForm.setConferma(false);
				myForm.setListaModelli(null);
				myForm.setTotRighe(0);
					request.setAttribute("codBib", myForm.getCodBib());
//					request.setAttribute("descrBib", Navigation.getInstance(request).getUtente().getBiblioteca());
					request.setAttribute("descrBib", myForm.getDescrBib());
					request.setAttribute("prov", "nuova");
					return mapping.findForward("nuova");
			}
			ActionMessages errors = new ActionMessages();
			errors.add("generico", new ActionMessage("error.documentofisico." + de.getMessage()));
			this.saveErrors(request, errors);
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
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java no ActionForward
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
		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		// Viene settato il token per le transazioni successive
		try {
			request.setAttribute("codBib", myForm.getCodBib());
			request.setAttribute("codPolo", myForm.getCodPolo());
			myForm.setConferma(false);
//			request.setAttribute("descrBib", Navigation.getInstance(request).getUtente().getBiblioteca());
			request.setAttribute("descrBib", myForm.getDescrBib());
			this.unspecified(mapping, myForm, request, response);
		} catch (Exception e) {
			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
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
				myForm.getListaModelli().addAll(listaBlocco);
			}
		}
		return mapping.getInputForward();
	}

	private List popolaListaPerBlocco(List listaModelli) throws Exception  {

		List<ModelloEtichetteVO> lista = new ArrayList<ModelloEtichetteVO>();
		for (int index = 0; index < listaModelli.size(); index++) {
			ModelloEtichetteVO modello = (ModelloEtichetteVO) listaModelli.get(index);
			GestioneCodici codici = null;
			try {
				codici = this.getCodici();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}
			lista.add(modello);
		}
		return lista;
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java getListaModelli List
	 *
	 * @param codBib
	 * @param nRec
	 * @param form
	 * @return
	 * @throws Exception
	 *
	 * servizio richiesta lista modelli di etichette
	 */
	private List<ModelloEtichetteVO> getListaModelli(String codPolo,
			String codBib, String ticket, int nRec, ActionForm form)
			throws Exception {

		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		DescrittoreBloccoVO blocco1;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		blocco1 = factory.getGestioneDocumentoFisico().getListaModelli(codPolo,	codBib, ticket, nRec);
//		if (blocco1 == null || blocco1.getTotRighe() < 1) {
		if (blocco1 == null || blocco1.getTotRighe() <= 0) {
			myForm.setNoModello(true);
			return null;
		} else {
			myForm.setNoModello(false);
			myForm.setIdLista(blocco1.getIdLista());
			myForm.setTotBlocchi(blocco1.getTotBlocchi());
			myForm.setTotRighe(blocco1.getTotRighe());
			myForm.setBloccoSelezionato(blocco1.getNumBlocco());
			myForm.setElemPerBlocchi(blocco1.getMaxRighe());
			if ((blocco1.getTotBlocchi() > 1)) {
				myForm.setAbilitaBottoneCarBlocchi(false);
			} else {
				myForm.setAbilitaBottoneCarBlocchi(true);
			}
			return blocco1.getLista();
		}
	}

	/**
	 * it.iccu.sbn.web.actions.documentofisico.ModelliEtichette
	 * ModelliEtichetteListaAction.java deleteModello boolean
	 *
	 * @param modello
	 * @return
	 * @throws Exception
	 *
	 * servizio richiesta di cancellazione logica di un modello di etichette
	 */
	private boolean deleteModello(ModelloEtichetteVO modello, String ticket)
	throws Exception {
		boolean ret = false;
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ret = factory.getGestioneDocumentoFisico().deleteModello(modello, ticket);
		return ret;
	}

	private GestioneCodici getCodici() throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		return factory.getCodici();
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		ModelliEtichetteListaForm myForm = (ModelliEtichetteListaForm) form;
		// gestione bottoni
		try{
			Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
			//almaviva5_20090907
			//utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_ETICHETTE, myForm.getCodPolo(), myForm.getCodBib(), null);
			utenteEjb.checkAttivita(CodiciAttivita.getIstance().GDF_GESTIONE_MODELLI_ETICHETTE, myForm.getCodPolo(), myForm.getCodBib(), null);
			return true;
		}catch (Exception e) {
			return false;
		}

	}
}
