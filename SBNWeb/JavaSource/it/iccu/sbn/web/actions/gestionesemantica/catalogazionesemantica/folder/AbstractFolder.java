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
package it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.CreaVariaAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.RicercaAbstractListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.RicercaAbstractVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.abstracto.SinteticaAbstractPerLegameTitVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AbstractFolder extends Action implements SemanticaFolder {

	private static Logger log = Logger.getLogger(AbstractFolder.class);

	private void loadStatoControllo(ActionForm form) throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		currentForm.setListaStatoControllo(CaricamentoComboSemantica
				.loadComboStato(null));
	}

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// setto il token per le transazioni successive
		this.saveToken(request);

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		try {
			//almaviva5_20100127 #2952
			Navigation navi = Navigation.getInstance(request);
			navi.setSuffissoTesto("/" + LinkableTagUtils.findMessage(request, request.getLocale(), "button.tag.abstracto"));

			this.loadStatoControllo(currentForm);
			// ATTENZIONE: IMPOSTARE DEFAULT DA PROFILO UTENTE
			String codStatoControllo = ((ComboCodDescVO) currentForm
					.getListaStatoControllo().get(0)).getCodice();
			currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneAbstract().setLivelloAutorita(
							codStatoControllo);
			currentForm.setCodice(currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneAbstract().getLivelloAutorita());

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();
		}
		currentForm.getCatalogazioneSemanticaComune().setPolo(currentForm.getAreaDatiPassBiblioSemanticaVO()
								.isInserimentoPolo());
		currentForm.setEnableIndice(currentForm.getAreaDatiPassBiblioSemanticaVO()
				.isInserimentoPolo());
		// indico che ho caricato il folder Abstract
		currentForm.getCatalogazioneSemanticaComune().setFolder(
				FolderType.FOLDER_ABSTRACT);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		RicercaAbstractVO datiRicerca = new RicercaAbstractVO();
		datiRicerca.setBid(currentForm.getAreaDatiPassBiblioSemanticaVO()
				.getBidPartenza());
		try {
			RicercaAbstractListaVO listaAbstract = factory
					.getGestioneSemantica().ricercaAbstractPerBidCollegato(
							datiRicerca, utenteCollegato.getTicket());

			currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneAbstract().setListaAbstract(
							listaAbstract.getRisultati());

			if (listaAbstract.getTotRighe() == 0) {
				currentForm.setEnableOk(true);
				currentForm.setEnableModifica(false);
				currentForm.setEnableElimina(false);
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneAbstract().setDataInserimento("");
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneAbstract().setDataVariazione("");
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneAbstract().setDescrizione("");
				return mapping.getInputForward();
			} else {
				HashSet<Integer> appoggio = new HashSet<Integer>();
				appoggio.add(1);
				currentForm.setBlocchiCaricati(appoggio);
				currentForm.setIdLista(listaAbstract.getIdLista());
				currentForm.setMaxRighe(listaAbstract.getMaxRighe());
				currentForm.setNumBlocco(listaAbstract.getNumBlocco());
				currentForm.setTotBlocchi(listaAbstract.getTotBlocchi());
				currentForm.setTotRighe(listaAbstract.getTotRighe());
				currentForm.setEnableElimina(true);
				currentForm.setEnableModifica(true);
				currentForm.setEnableOk(false);
				SinteticaAbstractPerLegameTitVO abstracto = listaAbstract
						.getRisultati().get(0);
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneAbstract().setDescrizione(
								abstracto.getDescrizione());
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneAbstract().setDataInserimento(
								abstracto.getDataInserimento());
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneAbstract().setDataVariazione(
								abstracto.getDataVariazione());
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneAbstract().setLivelloAutorita(
								abstracto.getLivelloAutorita());
			}

		} catch (ValidationException e) {
			// errori indice
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreValidazione", e
							.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		} catch (InfrastructureException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		}
		boolean abilitaModElimina = true;
		currentForm.setEnableElimina(abilitaModElimina);
		return mapping.getInputForward();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#caricaBlocco(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#gestione(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#Indice(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#crea(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		// poichè per l'abstract il tasto é ok sia per crea che per modifica
		// devo
		// capire cosa innescare "crea" o "modifica"
		if (!currentForm.isEnableOk())
			return this.modifica(mapping, form, request, response);

		CreaVariaAbstractVO abstracto = null;
		CreaVariaAbstractVO richiesta = new CreaVariaAbstractVO();

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		ActionMessages errors = new ActionMessages();
		try {

			richiesta.setT001(currentForm.getCatalogazioneSemanticaComune()
					.getBid());
			richiesta.setLivello(currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneAbstract().getLivelloAutorita());
			richiesta.setDescrizione(currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneAbstract().getDescrizione());

			abstracto = factory.getGestioneSemantica()
					.creaLegameTitoloAbstract(richiesta,
							utenteCollegato.getTicket());

			if (!abstracto.isEsitoOk() ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", abstracto
								.getTestoEsito()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}

		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.operOk"));
		this.saveErrors(request, errors);
		currentForm.setEnableOk(false);
		return mapping.getInputForward();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#modifica(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		CreaVariaAbstractVO abstracto = null;
		CreaVariaAbstractVO richiesta = new CreaVariaAbstractVO();

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		ActionMessages errors = new ActionMessages();
		try {

			richiesta.setT001(currentForm.getCatalogazioneSemanticaComune()
					.getBid());
			richiesta.setLivello(currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneAbstract().getLivelloAutorita());
			richiesta.setDescrizione(currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneAbstract().getDescrizione());
			richiesta.setT005(currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneAbstract().getListaAbstract().get(0)
					.getT005());

			abstracto = factory.getGestioneSemantica()
					.variaLegameTitoloAbstract(richiesta,
							utenteCollegato.getTicket());

			if (!abstracto.isEsitoOk() ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", abstracto
								.getTestoEsito()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.operOk"));
		this.saveErrors(request, errors);
		currentForm.setEnableOk(false);
		return mapping.getInputForward();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#recupera(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward recupera(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#elimina(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		ActionMessages errors = new ActionMessages();
		try {
			factory.getGestioneSemantica().cancellaLegameTitoloAbstract(
					currentForm.getCatalogazioneSemanticaComune().getBid(),
					utenteCollegato.getTicket());

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}
		currentForm.getCatalogazioneSemanticaComune().getCatalogazioneAbstract()
				.setDescrizione("");
		currentForm.setEnableElimina(false);
		return mapping.getInputForward();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#chiudi(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA, "P");
		request.setAttribute(NavigazioneSemantica.BID_RIFERIMENTO, currentForm.getCatalogazioneSemanticaComune()
				.getBid());
		return mapping.findForward("analiticaTitolo");
	}

	public ActionForward invioInIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return null;
	}

	public ActionForward aggiornaDati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return null;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		return false;
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward moveUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	public ActionForward moveDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

}
