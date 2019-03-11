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
package it.iccu.sbn.web.actions.servizi.relazioni;

import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.relazioni.RelazioniVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.relazioni.GestioneRelazioniForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class GestioneRelazioniAction extends ServiziBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.annulla", "chiudi");
		map.put("servizi.bottone.nuovo", "nuovo");
		map.put("servizi.bottone.cancella", "cancella");
		map.put("servizi.bottone.riattiva", "riattiva");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
			currentForm.setConferma(false);

			String codBibOperante = (String) request.getAttribute("BIBLIOTECA");
			if (ValidazioneDati.isFilled(codBibOperante))
				currentForm.setBiblioteca(codBibOperante);
			else
				currentForm.setBiblioteca(navi.getUtente().getCodBib());

			try {
				this.caricaRelazioni(currentForm, request);
				this.caricaListe(request, currentForm);
			} catch (Exception e) {
				log.error("", e);
				this.setErroreGenerico(request, e);
				return mapping.getInputForward();
			}

			Short chiamante = (Short) request.getAttribute("CHIAMANTE");
			switch (chiamante == null ? GestioneRelazioniForm.NESSUN_CHIAMANTE : chiamante) {
			case GestioneRelazioniForm.TIPO_SERVIZIO_CATEGORIE_FRUIZIONE:
				currentForm.setStep(GestioneRelazioniForm.GESTIONE_RELAZIONI);
				currentForm
						.setCodiceRelazione(CodiciType.CODICE_TIPO_SERVIZIO_CATEGORIA_FRUIZIONE
								.getTp_Tabella());
				break;

			case GestioneRelazioniForm.TIPO_SERVIZIO_MODALITA_EROGAZIONE:
				currentForm.setStep(GestioneRelazioniForm.GESTIONE_RELAZIONI);
				currentForm
						.setCodiceRelazione(CodiciType.CODICE_TIPO_SERVIZIO_MOD_EROGAZIONE
								.getTp_Tabella());
				break;

			case GestioneRelazioniForm.TIPO_SUPPORTO_MODALITA_EROGAZIONE:
				currentForm.setStep(GestioneRelazioniForm.GESTIONE_RELAZIONI);
				currentForm
						.setCodiceRelazione(CodiciType.CODICE_TIPO_SUPPORTO_MODALITA_EROGAZIONE
								.getTp_Tabella());
				break;

			default:
				break;
			}

			if (currentForm.getStep() == GestioneRelazioniForm.GESTIONE_RELAZIONI) {
				return this.ok(mapping, form, request, response);
			}
		}

		return mapping.getInputForward();
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;
		switch (currentForm.getStep()) {
		case GestioneRelazioniForm.AGGIUNGI_MODIFICA_RELAZIONE:
			currentForm.setStep(GestioneRelazioniForm.GESTIONE_RELAZIONI);
			break;
		case GestioneRelazioniForm.GESTIONE_RELAZIONI:
			currentForm.setStep(GestioneRelazioniForm.SCELTA_RELAZIONE);
			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;

		try {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			switch (currentForm.getStep()) {
			case GestioneRelazioniForm.SCELTA_RELAZIONE:
				currentForm.setStep(GestioneRelazioniForm.GESTIONE_RELAZIONI);

			case GestioneRelazioniForm.GESTIONE_RELAZIONI:
				this.checkSceltaRelazione(request, currentForm);
				currentForm.setLstRelazioniCaricate(delegate.caricaRelazioni(
						navi.getUtente().getCodPolo(), currentForm
								.getBiblioteca(), currentForm
								.getCodiceRelazione()));

				break;
			case GestioneRelazioniForm.AGGIUNGI_MODIFICA_RELAZIONE:
				this.checkAggiungiModificaRelazione(request, currentForm);
				delegate.aggiornaRelazione(fillRelazione(request, currentForm));

				currentForm.setLstRelazioniCaricate(delegate.caricaRelazioni(
						navi.getUtente().getCodPolo(), currentForm
								.getBiblioteca(), currentForm
								.getCodiceRelazione()));

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"message.servizi.relazioni.insOK"));
				this.saveErrors(request, errors);

				currentForm.setStep(GestioneRelazioniForm.GESTIONE_RELAZIONI);
				break;

			}

		} catch (ValidationException e) {
			resetToken(request);
		} catch (AlreadyExistsException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.relazioni.relazioneDuplicata"));
			this.saveErrors(request, errors);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}

		return mapping.getInputForward();
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;

		if (currentForm.getIdSelMultipla() != null
				&& currentForm.getIdSelMultipla().length > 0) {
			currentForm.setConferma(true);
			currentForm.setRichiesta("cancella");
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this,
					mapping, request));
			saveToken(request);

			currentForm.setIdSelMultiplaConferma(new Integer[currentForm
					.getIdSelMultipla().length]);
			for (int i = 0; i < currentForm.getIdSelMultipla().length; i++) {
				currentForm.getIdSelMultiplaConferma()[i] = currentForm
						.getIdSelMultipla()[i];
			}
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}

	public ActionForward riattiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;

		if (currentForm.getIdSelMultipla() != null
				&& currentForm.getIdSelMultipla().length > 0) {
			currentForm.setConferma(true);
			currentForm.setRichiesta("riattiva");
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this,
					mapping, request));
			saveToken(request);

			currentForm.setIdSelMultiplaConferma(new Integer[currentForm
					.getIdSelMultipla().length]);
			for (int i = 0; i < currentForm.getIdSelMultipla().length; i++) {
				currentForm.getIdSelMultiplaConferma()[i] = currentForm
						.getIdSelMultipla()[i];
			}
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
		}

		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;
		currentForm.setConferma(false);
		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (!isTokenValid(request))
			saveToken(request);

		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;

		ActionMessages errors = new ActionMessages();
		try {
			if (currentForm.getRichiesta().equals("cancella")) {
				currentForm.setRichiesta("");
				ServiziDelegate.getInstance(request).cancellaRelazioni(currentForm
						.getIdSelMultiplaConferma(), Navigation.getInstance(
						request).getUtente().getFirmaUtente());
			} else if (currentForm.getRichiesta().equals("riattiva")) {
				currentForm.setRichiesta("");
				ServiziDelegate.getInstance(request).riattivaRelazioni(currentForm
						.getIdSelMultiplaConferma(), Navigation.getInstance(
						request).getUtente().getFirmaUtente());
			}
		} catch (RemoteException e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.codiceAggiornamentoEffettuato"));
		this.saveErrors(request, errors);
		this.resetToken(request);
		currentForm.setConferma(false);

		currentForm.setStep(GestioneRelazioniForm.SCELTA_RELAZIONE);
		return this.ok(mapping, form, request, response);
		//return this.chiudi(mapping, form, request, response);
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;
		currentForm.setIdTipoServizio(0);
		currentForm.setIdTipoSupporto(0);
		currentForm.setCodFruizione("");
		currentForm.setCodModalitaErogazione("");
		currentForm.setStep(GestioneRelazioniForm.AGGIUNGI_MODIFICA_RELAZIONE);
		return mapping.getInputForward();
	}

	private RelazioniVO fillRelazione(HttpServletRequest request,
			ActionForm form) {

		GestioneRelazioniForm currentForm = (GestioneRelazioniForm) form;
		RelazioniVO relazione = new RelazioniVO();
		UserVO utente = Navigation.getInstance(request).getUtente();
		relazione.setCodPolo(utente.getCodPolo());
		relazione.setCodBib(currentForm.getBiblioteca());
		relazione.setCodRelazione(currentForm.getCodiceRelazione());

		CodiciType type = CodiciType.fromString(currentForm
				.getCodiceRelazione());
		switch (type) {
		case CODICE_TIPO_SERVIZIO_CATEGORIA_FRUIZIONE:
			relazione.setIdTabellaCodici(currentForm.getCodFruizione());
			relazione.setTabellaCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE
					.getTp_Tabella());
			relazione.setIdTabellaRelazione(currentForm.getIdTipoServizio());
			relazione.setTabellaRelazione("tbl_tipo_servizio");
			break;
		case CODICE_TIPO_SERVIZIO_MOD_EROGAZIONE:
			relazione
					.setIdTabellaCodici(currentForm.getCodModalitaErogazione());
			relazione.setTabellaCodici(CodiciType.CODICE_MODALITA_EROGAZIONE
					.getTp_Tabella());
			relazione.setIdTabellaRelazione(currentForm.getIdTipoServizio());
			relazione.setTabellaRelazione("tbl_tipo_servizio");
			break;
		case CODICE_TIPO_SUPPORTO_MODALITA_EROGAZIONE:
			relazione
					.setIdTabellaCodici(currentForm.getCodModalitaErogazione());
			relazione.setTabellaCodici(CodiciType.CODICE_MODALITA_EROGAZIONE
					.getTp_Tabella());
			relazione.setIdTabellaRelazione(currentForm.getIdTipoSupporto());
			relazione.setTabellaRelazione("tbl_supporti_biblioteca");
			break;
		}

		relazione.setUteIns(utente.getFirmaUtente());
		relazione.setUteVar(utente.getFirmaUtente());
		Timestamp ts = DaoManager.now();
		relazione.setTsIns(ts);
		relazione.setTsVar(ts);
		relazione.setFlCanc("N");

		return relazione;

	}

	private void checkSceltaRelazione(HttpServletRequest request,
			GestioneRelazioniForm form) throws ValidationException {
		boolean checkOK = true;
		ActionMessages errors = new ActionMessages();

		if (form.getCodiceRelazione() == null
				|| form.getCodiceRelazione().trim().equals("")) {
			checkOK = false;
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.relazioni.tipoRelazioneAssente"));
			this.addErrors(request, errors);
		}

		if (!checkOK)
			throw new ValidationException("");
	}

	private void checkAggiungiModificaRelazione(HttpServletRequest request,
			GestioneRelazioniForm form) throws ValidationException {
		boolean checkOK = true;
		ActionMessages errors = new ActionMessages();

		if (form.getCodiceRelazione().equals(GestioneRelazioniForm.TSCF)) {
			if (form.getIdTipoServizio() == 0) {
				checkOK = false;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.relazioni.tipoServizioAssente"));
			}
			if (form.getCodFruizione() == null
					|| form.getCodFruizione().equals("")) {
				checkOK = false;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.relazioni.codiceFruizioneAssente"));
			}
		} else if (form.getCodiceRelazione().equals(GestioneRelazioniForm.TSME)) {
			if (form.getIdTipoServizio() == 0) {
				checkOK = false;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.relazioni.tipoServizioAssente"));
			}
			if (form.getCodModalitaErogazione() == null
					|| form.getCodModalitaErogazione().equals("")) {
				checkOK = false;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.relazioni.modalitaErogazioneAssente"));
			}
		} else if (form.getCodiceRelazione().equals(GestioneRelazioniForm.SUME)) {
			if (form.getIdTipoSupporto() == 0) {
				checkOK = false;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.relazioni.tipoSuppotoAssente"));
			}
			if (form.getCodModalitaErogazione() == null
					|| form.getCodModalitaErogazione().equals("")) {
				checkOK = false;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.relazioni.modalitaErogazioneAssente"));
			}
		}

		this.addErrors(request, errors);
		if (!checkOK)
			throw new ValidationException("");
	}

	private void caricaRelazioni(GestioneRelazioniForm form,
			HttpServletRequest request) throws RemoteException, CreateException {
		form.setLstRelazioni(ServiziDelegate.getInstance(request).loadCodici(CodiciType.CODICE_TIPO_RELAZIONE));
	}

	private void caricaListe(HttpServletRequest request,
			GestioneRelazioniForm form) throws RemoteException, CreateException {
		// Carico lista tipi servizio associati alla biblioteca operante

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		String codPolo = Navigation.getInstance(request).getUtente()
				.getCodPolo();
		String codBib = form.getBiblioteca();

		form.setLstTipiServizio(delegate.getListaTipiServizio(codPolo, codBib));
		form.getLstTipiServizio().add(0, new TipoServizioVO());

		form.setLstTipiSupporto(this.getSupportiBiblioteca(codPolo, codBib, request));
		form.getLstTipiSupporto().add(0, new SupportoBibliotecaVO() );
		form.setLstCategorieFruizione(delegate.loadCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE));
		form.setLstModalitaErogazione(delegate.loadCodici(CodiciType.CODICE_MODALITA_EROGAZIONE));
	}

}
