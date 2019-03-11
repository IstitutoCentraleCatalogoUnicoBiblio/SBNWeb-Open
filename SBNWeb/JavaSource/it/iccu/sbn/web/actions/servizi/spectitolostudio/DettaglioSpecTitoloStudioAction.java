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
package it.iccu.sbn.web.actions.servizi.spectitolostudio;


import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.spectitolostudio.DettaglioSpecTitoloStudioForm;
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



public class DettaglioSpecTitoloStudioAction extends ServiziBaseAction {


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


	private void checkForm(HttpServletRequest request, DettaglioSpecTitoloStudioForm dettSpec)
	throws ValidationException {
		ActionMessages errors = new ActionMessages();
		String data = "";
		data = dettSpec.getAnaSpecialita().getTitoloStudio().trim();
		dettSpec.getAnaSpecialita().setTitoloStudio(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreTdSCampoObbligtorio"));
			this.saveErrors(request, errors);
			throw new ValidationException("Errore validazione dati");
		}
		data = dettSpec.getAnaSpecialita().getCodSpecialita().trim().toUpperCase();
		dettSpec.getAnaSpecialita().setCodSpecialita(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreCodSpecialitaCampoObbligtorio"));
			this.saveErrors(request, errors);
			throw new ValidationException("Errore validazione dati");
		}
		data = dettSpec.getAnaSpecialita().getDesSpecialita().trim();
		dettSpec.getAnaSpecialita().setDesSpecialita(data);
		if (ValidazioneDati.strIsNull(data)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.erroreDesSpecialitaCampoObbligtorio"));
			this.saveErrors(request, errors);
			// error = true;
			throw new ValidationException("Errore validazione dati");
		}
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		// setto il token per le transazioni successive
		DettaglioSpecTitoloStudioForm dettSpec = (DettaglioSpecTitoloStudioForm) form;
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		this.saveToken(request);

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			if (!dettSpec.isSessione()) {
				dettSpec.setSessione(true);


				if (request.getAttribute("SpecSel") != null) {
					dettSpec.setSelectedSpecTit((List) request.getAttribute("SpecSel"));
					dettSpec.setNumSpecTit(dettSpec.getSelectedSpecTit().size());
					dettSpec.setAnaSpecialita((SpecTitoloStudioVO) dettSpec.getSelectedSpecTit().get(0));
					if (dettSpec.getPosizioneScorrimento()>0)
					{
						dettSpec.setAnaSpecialita((SpecTitoloStudioVO)  dettSpec.getSelectedSpecTit().get(dettSpec.getPosizioneScorrimento()));
					}
					dettSpec.getAnaSpecialita().setNewSpecialita(false);
					dettSpec.setDatiSalvati((SpecTitoloStudioVO)dettSpec.getAnaSpecialita().clone());
				}
				else if (request.getAttribute(ServiziDelegate.DETTAGLIO_SPEC_TITOLO_STUDIO) != null) {
					dettSpec.setAnaSpecialita((SpecTitoloStudioVO) request.getAttribute(ServiziDelegate.DETTAGLIO_SPEC_TITOLO_STUDIO));
					dettSpec.getAnaSpecialita().setNewSpecialita(false);
					dettSpec.setDatiSalvati((SpecTitoloStudioVO)dettSpec.getAnaSpecialita().clone());
				} else {
					dettSpec.setAnaSpecialita(new SpecTitoloStudioVO());
					dettSpec.getAnaSpecialita().setNewSpecialita(true);
					dettSpec.getAnaSpecialita().setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
					dettSpec.getAnaSpecialita().setCodBiblioteca(Navigation.getInstance(request).getUtente().getCodBib());
				}

/*				if (dettSpec.getAnaSpecialita()!=null) {
					dettSpec.getAnaSpecialita().setNewSpecialita(false);
					dettSpec.setDatiSalvati((SpecTitoloStudioVO)dettSpec.getAnaSpecialita().clone());
				} else {
					dettSpec.setAnaSpecialita(new SpecTitoloStudioVO());
					dettSpec.getAnaSpecialita().setNewSpecialita(true);
					dettSpec.getAnaSpecialita().setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
					dettSpec.getAnaSpecialita().setCodBiblioteca(Navigation.getInstance(request).getUtente().getCodBib());
				}
*/
				dettSpec.setConferma(false);

				dettSpec.setListaTdS(this.loadTitoliStudio(request));
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
		DettaglioSpecTitoloStudioForm dettSpec = (DettaglioSpecTitoloStudioForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!dettSpec.isSessione()) {
				dettSpec.setSessione(true);
			}
			this.checkForm(request, dettSpec);

			if (!dettSpec.getAnaSpecialita().equals(dettSpec.getDatiSalvati())) {
				dettSpec.setConferma(true);
				dettSpec.setRichiesta("Aggiorna");
				ActionMessages errors = new ActionMessages();
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

	public ActionForward scorriAvanti(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioSpecTitoloStudioForm currentForm = (DettaglioSpecTitoloStudioForm) form;

		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()+1;
			int dimensione=currentForm.getSelectedSpecTit().size();
			if (attualePosizione > dimensione-1)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriAvanti"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setAnaSpecialita((SpecTitoloStudioVO) currentForm.getSelectedSpecTit().get(attualePosizione));
					currentForm.setDatiSalvati((SpecTitoloStudioVO)currentForm.getAnaSpecialita().clone());
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward scorriIndietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioSpecTitoloStudioForm currentForm = (DettaglioSpecTitoloStudioForm) form;

		try {
			int attualePosizione=currentForm.getPosizioneScorrimento()-1;
			int dimensione=currentForm.getSelectedSpecTit().size();
			if (attualePosizione < 0)
				{

				ActionMessages errors = new ActionMessages();
				errors.add("generico", new ActionMessage(
						"errors.acquisizioni.erroreScorriIndietro"));
				this.saveErrors(request, errors);

				}
			else
				{
					currentForm.setAnaSpecialita((SpecTitoloStudioVO) currentForm.getSelectedSpecTit().get(attualePosizione));
					currentForm.setDatiSalvati((SpecTitoloStudioVO)currentForm.getAnaSpecialita().clone());
					currentForm.setPosizioneScorrimento(attualePosizione);
				}
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward Cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSpecTitoloStudioForm dettSpec = (DettaglioSpecTitoloStudioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettSpec.isSessione()) {
			dettSpec.setSessione(true);
		}
		this.resetToken(request);

		dettSpec.setConferma(true);
		dettSpec.setRichiesta("Cancella");

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
		this.saveErrors(request, errors);
		this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

		return mapping.getInputForward();
	}


	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSpecTitoloStudioForm dettSpec = (DettaglioSpecTitoloStudioForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!dettSpec.isSessione()) {
				dettSpec.setSessione(true);
			}

			this.resetToken(request);

			dettSpec.setConferma(false);

			SpecTitoloStudioVO dettaglioSpec = dettSpec.getAnaSpecialita();
			String utente = Navigation.getInstance(request).getUtente().getFirmaUtente();
			ActionMessages errors = new ActionMessages();
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			if (dettSpec.getRichiesta().equalsIgnoreCase("Aggiorna")) {
				dettSpec.setRichiesta("");

				if (dettaglioSpec.isNewSpecialita()) {
					dettaglioSpec.setUteIns(utente);
					dettaglioSpec.setUteVar(utente);
					dettaglioSpec.setTsIns(DaoManager.now());
				} else {
					dettaglioSpec.setUteVar(utente);
				}

				try {
					delegate.aggiornaSpecTitoloStudio(dettaglioSpec, dettaglioSpec.isNewSpecialita());
				} catch (AlreadyExistsException e) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.dettaglioSpeTitoloStudio.codiceTitoloGiaEsiste"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
				}

				dettSpec.setDatiSalvati((SpecTitoloStudioVO)dettaglioSpec.clone());
				dettaglioSpec.setNewSpecialita(false);

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
				this.saveErrors(request, errors);

				return mapping.getInputForward();

			}


			if (dettSpec.getRichiesta().equalsIgnoreCase("Cancella")) {
				dettSpec.setRichiesta("");

				// inibizione cancellazione
				//Verifica esistenza utenti cui è assegnata l'autorizzazione che si vuole cancellare
				boolean ret = true;
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				ret = factory.getGestioneServizi().esistonoUtentiConSpecTit(Navigation.getInstance(request).getUserTicket(), dettaglioSpec.getCodPolo(), dettaglioSpec.getCodBiblioteca(), dettaglioSpec.getIdTitoloStudio());
				if (ret) {
					//Impossibile cancellare autorizzazione perchè assegnata almeno a un utente
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.specTitolo.utilizzata"));
					this.saveErrors(request, errors);
					//this.resetToken(request);
					return mapping.getInputForward();
				}
				// fine inibizione


				delegate.cancellaSpecTitoloStudio(new Integer[]{dettaglioSpec.getIdTitoloStudio()}, utente);

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceCancellazioneEffettuata"));
				this.saveErrors(request, errors);

				//rimando indietro alla lista facendola ricaricare
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


	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSpecTitoloStudioForm dettSpec = (DettaglioSpecTitoloStudioForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettSpec.isSessione()) {
			dettSpec.setSessione(true);
		}

		this.saveToken(request);
		dettSpec.setConferma(false);
		dettSpec.setRichiesta("");

		return mapping.getInputForward();
	}


	public ActionForward Nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSpecTitoloStudioForm dettSpec = (DettaglioSpecTitoloStudioForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettSpec.isSessione()) {
			dettSpec.setSessione(true);
		}
		resetToken(request);

		dettSpec.setAnaSpecialita(new SpecTitoloStudioVO());
		dettSpec.getAnaSpecialita().setNewSpecialita(true);
		dettSpec.getAnaSpecialita().setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
		dettSpec.getAnaSpecialita().setCodBiblioteca(Navigation.getInstance(request).getUtente().getCodBib());

		return mapping.getInputForward();
	}

	public ActionForward Annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioSpecTitoloStudioForm dettSpec = (DettaglioSpecTitoloStudioForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettSpec.isSessione()) {
			dettSpec.setSessione(true);
		}
		resetToken(request);

		return this.backForward(request, false);
	}
}
