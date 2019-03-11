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
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.web.actionforms.servizi.configurazione.DettaglioModalitaSupportoForm;
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

public class DettaglioModalitaSupportoAction extends ConfigurazioneBaseAction {

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
		DettaglioModalitaSupportoForm currentForm = (DettaglioModalitaSupportoForm)form;

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

				SupportoBibliotecaVO supporto = (SupportoBibliotecaVO)request.getAttribute(ServiziBaseAction.VO_SUPPORTO_ATTR);

				//I seguenti 2 attributi devono sempre essere impostati
				String codSupporto = supporto.getCodSupporto();
				String desSupporto = supporto.getDescrizione();

				if (!ValidazioneDati.isFilled(codSupporto) || !ValidazioneDati.isFilled(desSupporto)) {
					throw new ValidationException("Non è stato impostato il supporto");
				}
				Integer idSupporto = supporto.getId();
				if (idSupporto == 0)
					throw new ValidationException("Non è stato impostato l'ID del supporto");

				currentForm.setCodiceSupporto(codSupporto);
				currentForm.setDesSupporto(desSupporto);
				currentForm.setIdSupporto(idSupporto.intValue());


				currentForm.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));

				SupportiModalitaErogazioneVO supportiModalitaErogazione = (SupportiModalitaErogazioneVO)request.getAttribute(ServiziBaseAction.VO_TARIFFA_MODALITA_EROGAZIONE);

				if (supportiModalitaErogazione != null) {
					//Dettaglio
					currentForm.setNuovo(false);
					currentForm.setUltimoSalvato((SupportiModalitaErogazioneVO) supportiModalitaErogazione.clone());
					currentForm.setSupportoModalitaErogazione(supportiModalitaErogazione);
				} else {
					//Nuovo
					currentForm.setNuovo(true);
					currentForm.setUltimoSalvato(null);
					currentForm.setSupportoModalitaErogazione(new SupportiModalitaErogazioneVO());
					currentForm.setLstCodiciErogazioneAssociati((List<String>)request.getAttribute(ServiziBaseAction.LISTA_CODICI_EROGAZIONE_ATTR));
					currentForm.setLstModalitaErogazione(this.loadModalitaErogazioneDiverseDaQuelleDefinite(
							navi.getUtente().getCodPolo(), currentForm.getBiblioteca(),
							currentForm.getLstCodiciErogazioneAssociati(), request, supporto.getFlSvolg()));

					if (currentForm.getLstModalitaErogazione().size()==1){

						if (currentForm.getLstCodiciErogazioneAssociati().size()==0){
								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.nonDefiniteModalitaErogazioneBiblioteca"));
						}
						else {
							LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.associateTutteModalitaErogazioneSupporto"));
						}

						return navi.goBack(true);
					}
				}


				currentForm.setSessione(true);
			}
		} catch (ValidationException e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.supportoRichiesto"));

			//return backForward(request);
			return navi.goBack(true);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			//return backForward(request);
			return navi.goBack(true);
		}

		return mapping.getInputForward();
	}


	void checkOk(ActionForm form, HttpServletRequest request)
			throws ValidationException {
		DettaglioModalitaSupportoForm currentForm = (DettaglioModalitaSupportoForm) form;

		boolean checkOK = true;

		try {
			currentForm.getSupportoModalitaErogazione().getTarBase(ServiziConstant.NUMBER_FORMAT_CONVERTER,
					this.getLocale(request,	Constants.SBN_LOCALE)/* request.getLocale() */);
			currentForm.getSupportoModalitaErogazione().getCostoUnitario(ServiziConstant.NUMBER_FORMAT_CONVERTER,
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

		String pathChiamante = ((DettaglioModalitaSupportoForm)form).getChiamante();
		return backForward(mapping, pathChiamante);
	}


	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioModalitaSupportoForm dettaglio = (DettaglioModalitaSupportoForm)form;
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
		DettaglioModalitaSupportoForm dettaglio = (DettaglioModalitaSupportoForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			SupportiModalitaErogazioneVO supportiModalitaErogazione = dettaglio.getSupportoModalitaErogazione();
			if (!supportiModalitaErogazione.equals(dettaglio.getUltimoSalvato())) {
				//Ci sono modifiche da salvare
				Navigation navi = Navigation.getInstance(request);
				String utente = navi.getUtente().getFirmaUtente();
				long time = System.currentTimeMillis();
				if (dettaglio.isNuovo()) {
					supportiModalitaErogazione.setUteIns(utente);
					supportiModalitaErogazione.setTsIns(new Date(time));
					supportiModalitaErogazione.setUteVar(utente);
					supportiModalitaErogazione.setTsVar(new Date(time));
					supportiModalitaErogazione.setFlCanc("N");

					if (supportiModalitaErogazione.getCodErog().equals("")){

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliModalitaNuova"));

						return mapping.getInputForward();
					}

				} else {
					//Aggiornamento
					supportiModalitaErogazione.setUteVar(utente);
					supportiModalitaErogazione.setTsVar(new Date(time));
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
		DettaglioModalitaSupportoForm dettaglio = (DettaglioModalitaSupportoForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		SupportiModalitaErogazioneVO supportiModalitaErogazione = dettaglio.getSupportoModalitaErogazione();
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			GestioneServizi servizi = factory.getGestioneServizi();
			boolean operazioneOK = servizi.aggiornaSupportiModalitaErogazione(Navigation.getInstance(request).getUserTicket(), supportiModalitaErogazione, dettaglio.isNuovo(), dettaglio.getIdSupporto());

			if (operazioneOK) {
				dettaglio.setUltimoSalvato((SupportiModalitaErogazioneVO)supportiModalitaErogazione.clone());

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

				resetToken(request);
				request.setAttribute("prov_dettModSupp", "moderogaz");
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
