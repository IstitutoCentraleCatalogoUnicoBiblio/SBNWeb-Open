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
package it.iccu.sbn.web.actions.servizi.segnature;

import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.IntervalloSegnaturaNonValidoException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.segnature.DettaglioSegnatureForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class DettaglioSegnatureAction extends SegnatureAction {

	private static Logger log = Logger.getLogger(DettaglioSegnatureAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok", "Ok");
		map.put("servizi.bottone.si", "Si");
		map.put("servizi.bottone.no", "No");
		map.put("servizi.bottone.cancella", "Cancella");
		map.put("servizi.bottone.annulla", "Annulla");
		map.put("servizi.bottone.scorriAvanti", "scorriAvanti");
		map.put("servizi.bottone.scorriIndietro", "scorriIndietro");

		return map;
	}

	private void checkForm(HttpServletRequest request,
			DettaglioSegnatureForm currentForm) throws Exception {
		ActionMessages errors = new ActionMessages();
		boolean error = false;

		RangeSegnatureVO segnatura = currentForm.getDettSegn();

		if (segnatura.isNewSegnatura()) {
			// tck 2681
			/*
			 * if (segnatura.getCodSegnatura()==0) { error = true;
			 * errors.add(ActionMessages.GLOBAL_MESSAGE, new
			 * ActionMessage("errors.servizi.segnature.richiestoCodiceSegnatura"
			 * )); }
			 */
			boolean isFruizione = ValidazioneDati.isFilled(segnatura.getCodFruizione());
			boolean isIndisponibile = ValidazioneDati.isFilled(segnatura.getCodIndisp());

			if (!isFruizione) {
				error = true;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.segnature.richiestoCodiceFruizione"));
			}
			if (!isIndisponibile && !isFruizione) {
				error = true;
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.servizi.segnature.richiestoCodiceIndisponibilita"));
			}
		}

		String segnInizio = segnatura.getSegnInizio();
		String segnFine = segnatura.getSegnFine();

		if (ValidazioneDati.strIsNull(segnInizio))
			segnatura.setSegnInizio(" ");

		if (ValidazioneDati.strIsNull(segnFine))
			segnatura.setSegnFine("  ");

		//almaviva5_20110128 #4077
		String segn_da = ValidazioneDati.trimOrEmpty(OrdinamentoCollocazione2.normalizza(segnInizio));
		String segn_aa = ValidazioneDati.trimOrEmpty(OrdinamentoCollocazione2.normalizza(segnFine));
		if (segn_da.compareTo(segn_aa) > 0) {
			if (!ValidazioneDati.isFilled(segnFine)) {
				segnatura.setSegnFine(segnInizio);
			} else {
				error = true;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.segnature.segnInizioMaggioreSegnFine"));
			}
		}

		if (error) {
			this.saveErrors(request, errors);
			throw new ValidationException("");
		}
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		DettaglioSegnatureForm currentForm = (DettaglioSegnatureForm) form;

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				log.debug("DettaglioSegnatureAction::unspecified");
				currentForm.setSessione(true);
				this.loadDefault(currentForm, request);

				currentForm.setDettSegn((RangeSegnatureVO) request.getAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA));
				Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());

				if (request.getAttribute("SegnSel") != null) {
					currentForm.setSelectedSegnature((List) request
							.getAttribute("SegnSel"));
					currentForm.setNumSegn(currentForm.getSelectedSegnature()
							.size());
					currentForm.setDettSegn((RangeSegnatureVO) currentForm
							.getSelectedSegnature().get(0));
					if (currentForm.getPosizioneScorrimento() > 0) {
						currentForm.setDettSegn((RangeSegnatureVO) currentForm
								.getSelectedSegnature().get(
										currentForm.getPosizioneScorrimento()));
					}
				}

				if (currentForm.getDettSegn().isNewSegnatura()) {
					currentForm.getDettSegn().setTsIns(ts);
					currentForm.getDettSegn().setTsVar(ts);
				} else
					currentForm
							.setDettSegnSalvato((RangeSegnatureVO) currentForm
									.getDettSegn().clone());

				currentForm.setConferma(false);
			}
			return mapping.getInputForward();
		} catch (Exception ex) {
			return mapping.findForward("Annulla");
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSegnatureForm currentForm = (DettaglioSegnatureForm) form;

		try {
			int attualePosizione = currentForm.getPosizioneScorrimento() + 1;
			int dimensione = currentForm.getSelectedSegnature().size();
			if (attualePosizione > dimensione - 1) {

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

			} else {
				currentForm.setDettSegn((RangeSegnatureVO) currentForm
						.getSelectedSegnature().get(attualePosizione));
				currentForm.setPosizioneScorrimento(attualePosizione);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSegnatureForm currentForm = (DettaglioSegnatureForm) form;

		try {
			int attualePosizione = currentForm.getPosizioneScorrimento() - 1;
			int dimensione = currentForm.getSelectedSegnature().size();
			if (attualePosizione < 0) {

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

			} else {
				currentForm.setDettSegn((RangeSegnatureVO) currentForm
						.getSelectedSegnature().get(attualePosizione));
				currentForm.setPosizioneScorrimento(attualePosizione);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSegnatureForm currentForm = (DettaglioSegnatureForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			this.checkForm(request, currentForm);

			RangeSegnatureVO dettaglioSegn = currentForm.getDettSegn();
			if (dettaglioSegn.isNewSegnatura()) {
				dettaglioSegn.setUteIns(Navigation.getInstance(request)
						.getUtente().getFirmaUtente());
				dettaglioSegn
						.setTsIns(DaoManager.now());
			}

			if (!dettaglioSegn.equals(currentForm.getDettSegnSalvato())) {
				this.normalizzaSegnatura(dettaglioSegn);

				dettaglioSegn.setUteVar(Navigation.getInstance(request)
						.getUtente().getFirmaUtente());
				dettaglioSegn
						.setTsVar(DaoManager.now());

				currentForm.setConferma(true);
				currentForm.setRichiesta("Aggiorna");
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request,
						ConfermaDati.preparaConferma(this, mapping, request));
			}

			return mapping.getInputForward();
		} catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		}
	}

	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSegnatureForm currentForm = (DettaglioSegnatureForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}

		currentForm.setConferma(true);
		currentForm.setRichiesta("Cancella");
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.servizi.confermaOperazione"));
		this.saveErrors(request, errors);
		this.saveMessages(request,
				ConfermaDati.preparaConferma(this, mapping, request));

		return mapping.getInputForward();
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSegnatureForm currentForm = (DettaglioSegnatureForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			this.resetToken(request);
			currentForm.setConferma(false);
			RangeSegnatureVO eleSegn = currentForm.getDettSegn();

			if (currentForm.getRichiesta().equalsIgnoreCase("Aggiorna")) {
				currentForm.setRichiesta("");

				ServiziDelegate.getInstance(request).aggiornaSegnatura(eleSegn,
						eleSegn.isNewSegnatura());
				eleSegn.setNewSegnatura(false);
				currentForm.setDettSegnSalvato((RangeSegnatureVO) eleSegn
						.clone());

				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.codiceAggiornamentoEffettuato"));
				this.saveErrors(request, errors);

				return mapping.findForward("ok");
			}

			if (currentForm.getRichiesta().equalsIgnoreCase("Cancella")) {
				currentForm.setRichiesta("");

				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				// FASE DI CONTROLLO di esistenza doc legati almaviva4 26.02.2010
				DocumentoNonSbnVO docNSBN = factory
						.getGestioneServizi()
						.getCategoriaFruizioneSegnatura(
								Navigation.getInstance(request).getUserTicket(),
								eleSegn.getCodPolo(),
								eleSegn.getCodBiblioteca(), eleSegn.getSegnDa());

				/*
				 * RangeSegnatureVO segnaturaRangeDaCercare = (RangeSegnatureVO)
				 * eleSegn.clone(); segnaturaRangeDaCercare.setId(0);
				 * segnaturaRangeDaCercare.setCodSegnatura(0);
				 * segnaturaRangeDaCercare.setElemPerBlocchi(10);
				 * segnaturaRangeDaCercare.setCodFruizione("");
				 * segnaturaRangeDaCercare.setCodIndisp("");
				 * segnaturaRangeDaCercare.setIndisponibile(null);
				 * segnaturaRangeDaCercare
				 * .setSegnInizio(eleSegn.getSegnInizioTrim());
				 * segnaturaRangeDaCercare.setSegnFine("");
				 * segnaturaRangeDaCercare.setSegnDa("");
				 * segnaturaRangeDaCercare.setSegnA("");
				 * segnaturaRangeDaCercare.setNewSegnatura(false);
				 *
				 * if (segnaturaRangeDaCercare.getSegnInizio() != null &&
				 * !segnaturaRangeDaCercare.getSegnInizio().trim().equals("")) {
				 * segnaturaRangeDaCercare
				 * .setSegnFine(segnaturaRangeDaCercare.getSegnInizio());
				 * this.normalizzaSegnatura(segnaturaRangeDaCercare); }
				 *
				 * DescrittoreBloccoVO blocco1 = new
				 * ServiziDelegate(request).caricaSegnature
				 * (eleSegn.getCodPolo(),
				 * eleSegn.getCodBiblioteca(),segnaturaRangeDaCercare
				 * ,Navigation.getInstance(request).getUserTicket());
				 */

				if (docNSBN.getIdDocumento() > 0) {
					ActionMessages errors = new ActionMessages();
					errors.add(
							ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.servizi.cancellazioneImpossibileDocNonSbnPresenti"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				ServiziDelegate.getInstance(request).cancellaSegnature(
						new Integer[] { eleSegn.getId() }, Navigation
								.getInstance(request).getUtente()
								.getFirmaUtente());

				// mando msg di operazione ok
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.codiceCancellazioneEffettuata"));
				this.saveErrors(request, errors);
				currentForm.getDettSegn().clear();
				return mapping.findForward("cancella");
			}
			return mapping.getInputForward();
		} catch (IntervalloSegnaturaNonValidoException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.segnature.intervalloNonValido"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (AlreadyExistsException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.segnature.codiceSegnaturaDuplicato"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSegnatureForm currentForm = (DettaglioSegnatureForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setConferma(false);
		return mapping.getInputForward();
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSegnatureForm currentForm = (DettaglioSegnatureForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		resetToken(request);
		return this.backForward(request, false);
	}

	private void loadDefault(DettaglioSegnatureForm currentForm,
			HttpServletRequest request) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		currentForm.setLstFruizioni(delegate.loadCodiciCategoriaDiFruizione());
		currentForm.setLstIndisp(delegate.loadCodiciNonDisponibilita());
	}

}
