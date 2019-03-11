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
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.web.actionforms.servizi.configurazione.DettaglioModalitaForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class DettaglioModalitaAction extends ConfigurazioneBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.annulla",   "Chiudi");
		map.put("servizi.bottone.ok",        "ok");
		map.put("servizi.bottone.si",        "Si");
		map.put("servizi.bottone.no",        "No");
		return map;
	}



	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioModalitaForm currentForm = (DettaglioModalitaForm)form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		currentForm.setChiamante((String)request.getAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR));
		try {
			if (!currentForm.isSessione()) {
				//I seguenti 2 attributi devono sempre essere impostati
				String codTipoServizio = (String)request.getAttribute(ServiziBaseAction.COD_TIPO_SERVIZIO_ATTR);
				String desTipoServizio = (String)request.getAttribute(ServiziBaseAction.DESC_TIPO_SERVIZIO_ATTR);
				if (!ValidazioneDati.isFilled(codTipoServizio) || !ValidazioneDati.isFilled(desTipoServizio) ) {
					throw new ValidationException("Non è stato impostato il tipo servizio");
				}
				Integer idTipoServizio = (Integer)request.getAttribute(ServiziBaseAction.ID_TIPO_SERVIZIO_ATTR);
				if (idTipoServizio == null)
					throw new ValidationException("Non è stato impostato l'ID del tipo servizio");

				currentForm.setCodiceTipoServizio(codTipoServizio);
				currentForm.setDesTipoServizio(desTipoServizio);
				currentForm.setIdTipoServizio(idTipoServizio.intValue());
				currentForm.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));

				TariffeModalitaErogazioneVO tariffeModalitaErogazione = (TariffeModalitaErogazioneVO)request.getAttribute(ServiziBaseAction.VO_TARIFFA_MODALITA_EROGAZIONE);

				if (tariffeModalitaErogazione!=null) {
					//Dettaglio
					currentForm.setNuovo(false);
					currentForm.setUltimoSalvato((TariffeModalitaErogazioneVO)tariffeModalitaErogazione.clone());
					currentForm.setTariffaModalitaErogazione(tariffeModalitaErogazione);
				} else {
					//Nuovo
					currentForm.setNuovo(true);
					currentForm.setUltimoSalvato(null);
					currentForm.setTariffaModalitaErogazione(new TariffeModalitaErogazioneVO());
					currentForm.setLstCodiciErogazioneAssociati((List<String>)request.getAttribute(ServiziBaseAction.LISTA_CODICI_EROGAZIONE_ATTR));
					currentForm.setLstModalitaErogazione(this.loadModalitaErogazioneDiverseDaQuelleDefinite(
							navi.getUtente().getCodPolo(), currentForm.getBiblioteca(),
							currentForm.getLstCodiciErogazioneAssociati(), request));

					if (currentForm.getLstModalitaErogazione().size()==1){

						if (currentForm.getLstCodiciErogazioneAssociati().size()==0){
								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.nonDefiniteModalitaErogazioneBiblioteca"));
						}
						else {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.associateTutteModalitaErogazioneServizio"));
						}

						return navi.goBack(true);
					}
				}


				currentForm.setSessione(true);
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.tipoServizioRichiesto"));

			return backForward(request);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return backForward(request);
		}

		return mapping.getInputForward();
	}


	void checkOk(ActionForm form, HttpServletRequest request)
			throws ValidationException {
		DettaglioModalitaForm currentForm = (DettaglioModalitaForm) form;

		boolean checkOK = true;

		try {
			currentForm.getTariffaModalitaErogazione().getTarBase(ServiziConstant.NUMBER_FORMAT_CONVERTER,
					this.getLocale(request, Constants.SBN_LOCALE)/* request.getLocale() */);
			currentForm.getTariffaModalitaErogazione().getCostoUnitario(ServiziConstant.NUMBER_FORMAT_CONVERTER,
					this.getLocale(request,	Constants.SBN_LOCALE)/* request.getLocale() */);
		} catch (ParseException e) {
			checkOK = false;
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.dettaglioModalita.tariffaErrata"));
		}

		if (!checkOK)
			throw new ValidationException("Validazione dati fallita");
	}


	public ActionForward Chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		String pathChiamante = ((DettaglioModalitaForm)form).getChiamante();
		return backForward(mapping, pathChiamante);
	}


	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioModalitaForm dettaglio = (DettaglioModalitaForm)form;
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
		DettaglioModalitaForm dettaglio = (DettaglioModalitaForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			TariffeModalitaErogazioneVO tariffeModalitaErogazione = dettaglio.getTariffaModalitaErogazione();
			if (!tariffeModalitaErogazione.equals(dettaglio.getUltimoSalvato())) {
				//Ci sono modifiche da salvare
				Navigation navi = Navigation.getInstance(request);
				String utente = navi.getUtente().getFirmaUtente();
				long time = System.currentTimeMillis();
				if (dettaglio.isNuovo()) {
					tariffeModalitaErogazione.setUteIns(utente);
					tariffeModalitaErogazione.setTsIns(new Date(time));
					tariffeModalitaErogazione.setUteVar(utente);
					tariffeModalitaErogazione.setTsVar(new Date(time));
					tariffeModalitaErogazione.setFlCanc("N");

					if (tariffeModalitaErogazione.getCodErog().equals("")){

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliModalitaNuova"));

						return mapping.getInputForward();
					}

				} else {
					//Aggiornamento
					tariffeModalitaErogazione.setUteVar(utente);
					tariffeModalitaErogazione.setTsVar(new Date(time));
				}

				//Nel caso di aggiornamento chiedo conferma all'utente
				dettaglio.setConferma(true);

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

				this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
				return mapping.getInputForward();

			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}


	public ActionForward Si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioModalitaForm dettaglio = (DettaglioModalitaForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		TariffeModalitaErogazioneVO tariffeModalitaErogazione = dettaglio.getTariffaModalitaErogazione();
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			GestioneServizi servizi = factory.getGestioneServizi();
			boolean operazioneOK = servizi.aggiornaTariffeModalitaErogazione(Navigation.getInstance(request).getUserTicket(), tariffeModalitaErogazione, dettaglio.isNuovo(), dettaglio.getIdTipoServizio());

			if (operazioneOK) {
				dettaglio.setUltimoSalvato((TariffeModalitaErogazioneVO)tariffeModalitaErogazione.clone());

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

				resetToken(request);
				request.setAttribute("prov", "moderogaz");
				return mapping.findForward("indietro");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));

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


}
