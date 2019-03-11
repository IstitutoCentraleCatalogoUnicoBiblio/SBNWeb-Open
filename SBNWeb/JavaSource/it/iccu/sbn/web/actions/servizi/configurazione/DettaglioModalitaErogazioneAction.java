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
package it.iccu.sbn.web.actions.servizi.configurazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.GestioneServizi;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.servizi.configurazione.DettaglioModalitaErogazioneForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.sql.Timestamp;
import java.text.ParseException;
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

public class DettaglioModalitaErogazioneAction extends ConfigurazioneBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.annulla",   "chiudi");
		map.put("servizi.bottone.ok",        "ok");
		map.put("servizi.bottone.si",        "si");
		map.put("servizi.bottone.no",        "no");
		return map;
	}



	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioModalitaErogazioneForm currentForm = (DettaglioModalitaErogazioneForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setChiamante((String)request.getAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR));
		try {
			if (!currentForm.isSessione()) {

				List<TB_CODICI> svolgimento = CodiciProvider.getCodici(CodiciType.CODICE_TIPO_SVOLGIMENTO_DEL_SERVIZIO);
				svolgimento = CaricamentoCombo.cutFirst(svolgimento);
				currentForm.setLstSvolgimento(svolgimento);
				currentForm.setTipoSvolgimento("L");

				currentForm.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));

				ModalitaErogazioneVO modalitaErogazione = (ModalitaErogazioneVO)request.getAttribute(ServiziBaseAction.VO_TARIFFA_MODALITA_EROGAZIONE);

				if (modalitaErogazione != null) {
					//Dettaglio
					currentForm.setNuovo(false);
					currentForm.setUltimoSalvato((ModalitaErogazioneVO)modalitaErogazione.clone());
					currentForm.setTariffaModalitaErogazione(modalitaErogazione);
				} else {
					//Nuovo
					currentForm.setNuovo(true);
					currentForm.setUltimoSalvato(null);
					ModalitaErogazioneVO tariffaModalitaErogazione = new ModalitaErogazioneVO();
					currentForm.setTariffaModalitaErogazione(tariffaModalitaErogazione);
					currentForm.setLstCodiciErogazioneAssociati((List<String>)request.getAttribute(ServiziBaseAction.LISTA_CODICI_EROGAZIONE_ATTR));
					caricaModalita(currentForm, request);
					tariffaModalitaErogazione.setCodBib(currentForm.getBiblioteca());
					tariffaModalitaErogazione.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
					tariffaModalitaErogazione.setFlSvolg("L");

					if (currentForm.getLstModalitaErogazione().size()==1){
						ActionMessages errors = new ActionMessages();
						if (currentForm.getLstCodiciErogazioneAssociati().size()==0){
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.nonDefiniteModalitaErogazionePolo"));
						}
						else {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.definiteTutteModalitaErogazione"));
						}
						this.saveErrors(request, errors);
						return Navigation.getInstance(request).goBack(true);
					}

				}

				currentForm.setSessione(true);
			}

			if (ValidazioneDati.equals(currentForm.getUpdateCombo(), "svolgimento") ) {
				caricaModalita(currentForm, request);
				currentForm.setUpdateCombo(null);
			}

		} catch (ValidationException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.tipoServizioRichiesto"));
			this.saveErrors(request, errors);
			return backForward(request);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return backForward(request);
		}

		return mapping.getInputForward();
	}


	private void caricaModalita(DettaglioModalitaErogazioneForm currentForm, HttpServletRequest request) throws Exception {
		//currentForm.setLstModalitaErogazione(this.loadModalitaErogazioneDiverseDa(currentForm.getLstCodiciErogazioneAssociati(), request));
		if (currentForm.getTipoSvolgimento().equals("L"))
			currentForm.setLstModalitaErogazione(CodiciProvider.getCodici(CodiciType.CODICE_MODALITA_EROGAZIONE));
		else
			currentForm.setLstModalitaErogazione(CodiciProvider.getCodici(CodiciType.CODICE_MODALITA_EROGAZIONE_ILL));

	}



	private void checkOk(ActionForm form, HttpServletRequest request)
	throws ValidationException {
		DettaglioModalitaErogazioneForm dettaglio = (DettaglioModalitaErogazioneForm)form;
		ActionMessages errors = new ActionMessages();
		boolean checkOK=true;

		try {
			if (dettaglio.getTariffaModalitaErogazione().getTarBase().equals("")){
				dettaglio.getTariffaModalitaErogazione().setTarBase("0");
			}
			if (dettaglio.getTariffaModalitaErogazione().getCostoUnitario().equals("")){
				dettaglio.getTariffaModalitaErogazione().setCostoUnitario("0");
			}
			dettaglio.getTariffaModalitaErogazione().getTarBase(ServiziConstant.NUMBER_FORMAT_CONVERTER, this.getLocale(
					request, Constants.SBN_LOCALE)/* request.getLocale() */);
			dettaglio.getTariffaModalitaErogazione().getCostoUnitario(ServiziConstant.NUMBER_FORMAT_CONVERTER,
					this.getLocale(request,
							Constants.SBN_LOCALE)/* request.getLocale() */);
		} catch (ParseException e) {
			checkOK=false;
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.dettaglioModalita.tariffaErrata"));
			this.saveErrors(request, errors);
		}

		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}


	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		String pathChiamante = ((DettaglioModalitaErogazioneForm)form).getChiamante();
		return backForward(mapping, pathChiamante);
	}


	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioModalitaErogazioneForm dettaglio = (DettaglioModalitaErogazioneForm)form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!dettaglio.isSessione()) {
			dettaglio.setSessione(true);
		}


		dettaglio.setConferma(false);
		resetToken(request);
		return mapping.getInputForward();
	}



	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioModalitaErogazioneForm dettaglio = (DettaglioModalitaErogazioneForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			this.checkOk(form, request);
			ModalitaErogazioneVO ModalitaErogazione = dettaglio.getTariffaModalitaErogazione();
			if (!ModalitaErogazione.equals(dettaglio.getUltimoSalvato())) {
				//Ci sono modifiche da salvare
				Navigation navi = Navigation.getInstance(request);
				String utente = navi.getUtente().getFirmaUtente();
				Timestamp now = DaoManager.now();
				if (dettaglio.isNuovo()) {
					ModalitaErogazione.setUteIns(utente);
					ModalitaErogazione.setTsIns(now);
					ModalitaErogazione.setUteVar(utente);
					ModalitaErogazione.setTsVar(now);
					ModalitaErogazione.setFlCanc("N");
					ModalitaErogazione.setFlSvolg(dettaglio.getTipoSvolgimento());

					if (ModalitaErogazione.getCodErog().equals("")){
						ActionMessages errors = new ActionMessages();
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.scegliModalitaNuova"));
						this.saveErrors(request, errors);
						return mapping.getInputForward();
					}

				} else {
					//Aggiornamento
					ModalitaErogazione.setUteVar(utente);
					ModalitaErogazione.setTsVar(now);
				}

				//Nel caso di aggiornamento chiedo conferma all'utente
				dettaglio.setConferma(true);
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.confermaOperazione"));
				this.saveErrors(request, errors);
				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();

			}

			else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}

		} catch (ValidationException e) {
			resetToken(request);
			log.info(e.getMessage());
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioModalitaErogazioneForm dettaglio = (DettaglioModalitaErogazioneForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		ModalitaErogazioneVO ModalitaErogazione = dettaglio.getTariffaModalitaErogazione();
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			GestioneServizi servizi = factory.getGestioneServizi();
			boolean operazioneOK = servizi.aggiornaModalitaErogazione(Navigation.getInstance(request).getUserTicket(), ModalitaErogazione, dettaglio.isNuovo());

			if (operazioneOK) {
				dettaglio.setUltimoSalvato((ModalitaErogazioneVO)ModalitaErogazione.clone());
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
				this.saveErrors(request, errors);
				resetToken(request);
				request.setAttribute("prov", "moderogaz");
				return mapping.findForward("indietro");
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	@Override
	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, "SERVIZI_ILL")) {
			try {
				DettaglioModalitaErogazioneForm currentForm = (DettaglioModalitaErogazioneForm)form;
				ModalitaErogazioneVO modErog = currentForm.getTariffaModalitaErogazione();
				ParametriBibliotecaVO parametriBiblioteca = ServiziDelegate.getInstance(request).getParametriBiblioteca(modErog.getCodPolo(), modErog.getCodBib() );
				return parametriBiblioteca != null && parametriBiblioteca.isServiziILLAttivi();

			} catch (Exception e) {
				log.error("",  e);
				return false;
			}
		}
		return super.checkAttivita(request, form, idCheck);
	}

}
