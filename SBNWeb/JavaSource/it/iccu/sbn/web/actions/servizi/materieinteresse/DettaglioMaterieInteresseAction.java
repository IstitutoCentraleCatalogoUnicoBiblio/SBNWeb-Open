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
package it.iccu.sbn.web.actions.servizi.materieinteresse;

import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.materieinteresse.DettaglioMaterieInteresseForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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


public class DettaglioMaterieInteresseAction extends ServiziBaseAction {
	private static Logger log = Logger.getLogger(DettaglioMaterieInteresseAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.ok",       "Ok");
		map.put("servizi.bottone.si",       "Si");
		map.put("servizi.bottone.no",       "No");
		map.put("servizi.bottone.nuovo",    "Nuovo");
		map.put("servizi.bottone.cancella", "Cancella");
		map.put("servizi.bottone.annulla",  "Annulla");
		map.put("servizi.bottone.scorriAvanti","scorriAvanti");
		map.put("servizi.bottone.scorriIndietro","scorriIndietro");
		return map;
	}

	private void checkForm(HttpServletRequest request, DettaglioMaterieInteresseForm dettMateria)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		boolean error = false;
		String data = "";

		data = dettMateria.getAnaMateria().getCodice().trim().toUpperCase();
		dettMateria.getAnaMateria().setCodice(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dettaglioMateria.codiceMateriaObbligatorio"));
			this.saveErrors(request, errors);
			error = true;
		}

		data = dettMateria.getAnaMateria().getDescrizione().trim();
		dettMateria.getAnaMateria().setDescrizione(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dettaglioMateria.descMateriaObbligatorio"));
			this.saveErrors(request, errors);
			error = true;
		}

		if (error) {
			throw new ValidationException("Errori validazione dati");
		}
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// setto il token per le transazioni successive
		DettaglioMaterieInteresseForm dettMateria = (DettaglioMaterieInteresseForm) form;

		if (Navigation.getInstance(request).isFromBar())		return mapping.getInputForward();

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			if (!dettMateria.isSessione()) {
				dettMateria.setSessione(true);
				dettMateria.setAnaMateria((MateriaVO) request.getAttribute(ServiziDelegate.DETTAGLIO_MATERIA));

				if (request.getAttribute("MatSel") != null) {
					dettMateria.setSelectedMatInt((List) request.getAttribute("MatSel"));
					dettMateria.setNumMat(dettMateria.getSelectedMatInt().size());
					dettMateria.setAnaMateria((MateriaVO) dettMateria.getSelectedMatInt().get(0));
					if (dettMateria.getPosizioneScorrimento()>0)
					{
						dettMateria.setAnaMateria((MateriaVO)  dettMateria.getSelectedMatInt().get(dettMateria.getPosizioneScorrimento()));
					}
				}
				if (dettMateria.getAnaMateria()!=null) {
					dettMateria.setNuovo(false);
					dettMateria.setMateriaSalvata((MateriaVO)dettMateria.getAnaMateria().clone());
				} else {
					dettMateria.setAnaMateria(new MateriaVO());
					dettMateria.getAnaMateria().setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
					dettMateria.getAnaMateria().setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
					dettMateria.setNuovo(true);
				}
				dettMateria.setConferma(false);
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioMaterieInteresseForm currentForm = (DettaglioMaterieInteresseForm) form;

		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getSelectedMatInt().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setAnaMateria((MateriaVO) currentForm.getSelectedMatInt().get(attualePosizione));
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioMaterieInteresseForm currentForm = (DettaglioMaterieInteresseForm) form;

		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;
			int dimensione=currentForm.getSelectedMatInt().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setAnaMateria((MateriaVO) currentForm.getSelectedMatInt().get(attualePosizione));
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
		DettaglioMaterieInteresseForm dettMateria = (DettaglioMaterieInteresseForm) form;
		ActionMessages errors = new ActionMessages();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!dettMateria.isSessione()) {
				dettMateria.setSessione(true);
			}

			MateriaVO materia = dettMateria.getAnaMateria();
			if (!materia.equals(dettMateria.getMateriaSalvata())) {
				this.checkForm(request, dettMateria);

				dettMateria.setConferma(true);
				dettMateria.setRichiesta("Aggiorna");
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));

				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			}

			return mapping.getInputForward();
		} catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		}  catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioMaterieInteresseForm currentForm = (DettaglioMaterieInteresseForm) form;

		ActionMessages errors = new ActionMessages();

		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);


		currentForm.setConferma(true);
		currentForm.setRichiesta("Cancella");
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
		this.saveErrors(request, errors);
		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

		return mapping.getInputForward();
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioMaterieInteresseForm currentForm = (DettaglioMaterieInteresseForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			this.resetToken(request);

			currentForm.setConferma(false);

			String utente = Navigation.getInstance(request).getUtente().getFirmaUtente();
			MateriaVO materia = currentForm.getAnaMateria();
			ActionMessages errors = new ActionMessages();
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			String richiesta = currentForm.getRichiesta();
			if (richiesta != null && richiesta.equals("Aggiorna")) {
				currentForm.setRichiesta("");

				if (currentForm.isNuovo()) {
					materia.setUteIns(utente);
					materia.setUteVar(utente);
					materia.setTsIns(DaoManager.now());
				} else
					materia.setUteVar(utente);


				try {
					delegate.aggiornaMateria(materia, currentForm.isNuovo());
				} catch (AlreadyExistsException e) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dettaglioMateria.codiceMateriaGiaEsiste"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
				}

				currentForm.setMateriaSalvata((MateriaVO)materia.clone());
				currentForm.setNuovo(false);

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
				this.saveErrors(request, errors);

				return mapping.getInputForward();
			}


			if (richiesta != null && richiesta.equals("Cancella")) {
				currentForm.setRichiesta("");

				delegate.cancellaMaterie(new Integer[]{materia.getIdMateria()}, utente);

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
				this.saveErrors(request, errors);

				//rimando indietro alla lista facendola ricaricare
				return this.backForward(request, false);
			}

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.richiestaOperazioneInvalida"));
			this.saveErrors(request, errors);

			return mapping.getInputForward();

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			return mapping.getInputForward();

		}  catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		DettaglioMaterieInteresseForm dettMateria = (DettaglioMaterieInteresseForm) form;
		if (!dettMateria.isSessione()) {
			dettMateria.setSessione(true);
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		dettMateria.setConferma(false);
		dettMateria.setRichiesta("");

		return mapping.getInputForward();
	}

	public ActionForward Nuovo(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioMaterieInteresseForm dettMateria = (DettaglioMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettMateria.isSessione()) {
			dettMateria.setSessione(true);
		}
		resetToken(request);

		dettMateria.setAnaMateria(new MateriaVO());
		dettMateria.getAnaMateria().setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
		dettMateria.getAnaMateria().setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
		dettMateria.setNuovo(true);
		dettMateria.setConferma(false);

		return mapping.getInputForward();
	}


	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioMaterieInteresseForm dettMateria = (DettaglioMaterieInteresseForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettMateria.isSessione()) {
			dettMateria.setSessione(true);
		}

		return this.backForward(request, false);
	}

}
