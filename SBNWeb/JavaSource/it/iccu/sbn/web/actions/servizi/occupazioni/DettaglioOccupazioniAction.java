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
package it.iccu.sbn.web.actions.servizi.occupazioni;

import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.occupazioni.DettaglioOccupazioniForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

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



public class DettaglioOccupazioniAction extends ServiziBaseAction {

	private static Logger log = Logger.getLogger(DettaglioOccupazioniAction.class);

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


	private void checkForm(HttpServletRequest request, DettaglioOccupazioniForm dettOccup)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		String data = "";

		data = dettOccup.getDettaglio().getDesOccupazione().trim();
		dettOccup.getDettaglio().setDesOccupazione(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreDesOccupCampoObbligtorio"));
			this.saveErrors(request, errors);
			throw new ValidationException("Errore validazione dati");
		}
		data = dettOccup.getDettaglio().getCodOccupazione().trim().toUpperCase();
		dettOccup.getDettaglio().setCodOccupazione(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreCodOccupazioneCampoObbligtorio"));
			this.saveErrors(request, errors);
			throw new ValidationException("Errore validazione dati");
		}
		data = dettOccup.getDettaglio().getProfessione().trim();
		dettOccup.getDettaglio().setProfessione(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreProfessioneCampoObbligtorio"));
			this.saveErrors(request, errors);
			throw new ValidationException("Errore validazione dati");
		}
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioOccupazioniForm dettOccup = (DettaglioOccupazioniForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!dettOccup.isSessione()) {
				dettOccup.setDettaglio((OccupazioneVO) request.getAttribute(ServiziDelegate.DETTAGLIO_OCCUPAZIONE));
				dettOccup.setSessione(true);
				if (request.getAttribute("OccSel") != null) {
					dettOccup.setSelectedOccup((List) request.getAttribute("OccSel"));
					dettOccup.setNumOcc(dettOccup.getSelectedOccup().size());
					dettOccup.setDettaglio((OccupazioneVO) dettOccup.getSelectedOccup().get(0));
					if (dettOccup.getPosizioneScorrimento()>0)
					{
						dettOccup.setDettaglio((OccupazioneVO)  dettOccup.getSelectedOccup().get(dettOccup.getPosizioneScorrimento()));
					}
				}
				if (dettOccup.getDettaglio()!=null) {
					dettOccup.getDettaglio().setNewOccupazione(false);
					dettOccup.setDatiSalvati((OccupazioneVO)dettOccup.getDettaglio().clone());
				} else {
					dettOccup.setDettaglio(new OccupazioneVO());
					dettOccup.getDettaglio().setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
					dettOccup.getDettaglio().setCodBiblioteca(Navigation.getInstance(request).getUtente().getCodBib());
					dettOccup.getDettaglio().setNewOccupazione(true);
				}

				dettOccup.setLstProfessioni(this.loadProfessioni(request));
			}
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioOccupazioniForm dettOccup = (DettaglioOccupazioniForm) form;
		ActionMessages errors = new ActionMessages();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!dettOccup.isSessione()) {
				dettOccup.setSessione(true);
			}

			OccupazioneVO dettaglio = dettOccup.getDettaglio();
			if (!dettaglio.equals(dettOccup.getDatiSalvati())) {
				this.checkForm(request, dettOccup);
				dettOccup.setConferma(true);
				dettOccup.setRichiesta("Aggiorna");
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			}
			return mapping.getInputForward();
		} catch (ValidationException e) {
			resetToken(request);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioOccupazioniForm dettOccup = (DettaglioOccupazioniForm) form;
		ActionMessages errors = new ActionMessages();
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettOccup.isSessione()) {
			dettOccup.setSessione(true);
		}
		dettOccup.setConferma(true);
		dettOccup.setRichiesta("Cancella");
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
		this.saveErrors(request, errors);
		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
		return mapping.getInputForward();
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioOccupazioniForm dettOccup = (DettaglioOccupazioniForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!dettOccup.isSessione()) {
				dettOccup.setSessione(true);
			}
			dettOccup.setConferma(false);
			String utente = Navigation.getInstance(request).getUtente().getFirmaUtente();
			ActionMessages errors = new ActionMessages();
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			OccupazioneVO dettaglio = dettOccup.getDettaglio();


			if (dettOccup.getRichiesta().equalsIgnoreCase("Aggiorna")) {
				dettOccup.setRichiesta("");
				if (dettaglio.isNewOccupazione()) {
					dettaglio.setUteIns(utente);
					dettaglio.setUteVar(utente);
					dettaglio.setTsIns(DaoManager.now());
				} else {
					dettaglio.setUteVar(utente);
				}

				try {
					delegate.aggiornaOccupazione(dettaglio, dettaglio.isNewOccupazione());
				} catch (AlreadyExistsException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dettaglioOccupazione.codiceOccupazioneGiaEsiste"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				dettOccup.setDatiSalvati((OccupazioneVO)dettaglio.clone());
				dettaglio.setNewOccupazione(false);

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
				this.saveErrors(request, errors);

				return mapping.getInputForward();
			}

			if (dettOccup.getRichiesta().equalsIgnoreCase("Cancella")) {
				dettOccup.setRichiesta("");

				// inibizione cancellazione
				//Verifica esistenza utenti cui è assegnata l'autorizzazione che si vuole cancellare
				boolean ret = true;
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				ret = factory.getGestioneServizi().esistonoUtentiConOcc(Navigation.getInstance(request).getUserTicket(), dettaglio.getCodPolo(), dettaglio.getCodBiblioteca(), dettaglio.getIdOccupazioni());
				if (ret) {
					//Impossibile cancellare autorizzazione perchè assegnata almeno a un utente
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.occupazione.utilizzata"));
					this.saveErrors(request, errors);
					//this.resetToken(request);
					return mapping.getInputForward();
				}
				// fine inibizione

				delegate.cancellaOccupazioni(new Integer[]{dettaglio.getIdOccupazioni()}, utente);

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
				this.saveErrors(request, errors);

				return this.backForward(request, false);
			}

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.richiestaOperazioneInvalida"));
			this.saveErrors(request, errors);

			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioOccupazioniForm currentForm = (DettaglioOccupazioniForm) form;

		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getSelectedOccup().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setDettaglio((OccupazioneVO) currentForm.getSelectedOccup().get(attualePosizione));
					currentForm.setDatiSalvati((OccupazioneVO)currentForm.getDettaglio().clone());
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioOccupazioniForm currentForm = (DettaglioOccupazioniForm) form;

		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;
			int dimensione=currentForm.getSelectedOccup().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setDettaglio((OccupazioneVO) currentForm.getSelectedOccup().get(attualePosizione));
					currentForm.setDatiSalvati((OccupazioneVO)currentForm.getDettaglio().clone());
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioOccupazioniForm dettOccup = (DettaglioOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettOccup.isSessione()) {
			dettOccup.setSessione(true);
		}

		this.saveToken(request);
		dettOccup.setConferma(false);
		dettOccup.setRichiesta("");

		return mapping.getInputForward();
	}

	public ActionForward Nuovo(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioOccupazioniForm dettOccup = (DettaglioOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettOccup.isSessione()) {
			dettOccup.setSessione(true);
		}

		dettOccup.setDettaglio(new OccupazioneVO());
		dettOccup.getDettaglio().setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
		dettOccup.getDettaglio().setCodBiblioteca(Navigation.getInstance(request).getUtente().getCodBib());
		dettOccup.getDettaglio().setNewOccupazione(true);
		dettOccup.setConferma(false);

		return mapping.getInputForward();
	}


	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioOccupazioniForm dettOccup = (DettaglioOccupazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettOccup.isSessione()) {
			dettOccup.setSessione(true);
		}

		return this.backForward(request, false);
	}
}
