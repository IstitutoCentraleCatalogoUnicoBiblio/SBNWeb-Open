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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.actionforms.servizi.configurazione.DettaglioTipoServizioForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class DettaglioTipoServizioAction extends ConfigurazioneBaseAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.annulla", "chiudi");
		map.put("servizi.bottone.ok", "ok");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		return map;
	}


	private void check(ActionForm form, HttpServletRequest request)
	throws Exception {
		DettaglioTipoServizioForm dettaglio = (DettaglioTipoServizioForm)form;

		TipoServizioVO tipoServizio = dettaglio.getTipoServizio();
		boolean checkOK=true;

		if (tipoServizio.getOreRidis()<0 || tipoServizio.getGgRidis()<0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.ore_gg_positivi"));

			checkOK=false;
		}
		if (tipoServizio.getOreRidis()!=0 && tipoServizio.getGgRidis()!=0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.ore_gg_solo_uno"));

			checkOK=false;
		}
		if (tipoServizio.getOreRidis()>23) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.ore_minori23"));

			checkOK=false;
		}

		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioTipoServizioForm dettaglio = (DettaglioTipoServizioForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		dettaglio.setChiamante((String)request.getAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR));
		try {

			String verInserimentoUtente = null;

			if (dettaglio.getInserimentoUtente() != null) {
				verInserimentoUtente = dettaglio.getInserimentoUtente();
			}

			if (verInserimentoUtente != null && verInserimentoUtente.equals("SI")) {
				dettaglio.setInserimentoUtente(null);
			}

			// viene verificato se si proviene dalla modifica del campo sull'inserimento della richiesta da parte dell'utente
			if (verInserimentoUtente != null && verInserimentoUtente.equals("SI")) {
			}
			else {

				// in caso negativo proseguo l'elaborazione

				if (!dettaglio.isSessione()) {
					Navigation navi = Navigation.getInstance(request);
					TipoServizioVO tipoServizio = (TipoServizioVO)request.getAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR);
					if (tipoServizio==null || !tipoServizio.isValid()) {
						//Nuovo tipo servizio
						dettaglio.setNuovo(true);
						dettaglio.getTipoServizio().setCodBib((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));
						dettaglio.getTipoServizio().setCodPolo(navi.getUtente().getCodPolo());

						//Carico lista codici tipi servizio associati alla biblioteca
						List<String> lstCodiciTipiServAssociati = this.getCodiciTipiServizio(
																	dettaglio.getTipoServizio().getCodPolo(),
																	dettaglio.getTipoServizio().getCodBib(), request);
						//Carico la combo dei tipi servizi disponibili esclusi quelli già configurati
						List<ComboCodDescVO> lstTipiServizi = this.loadTipiServizioDiversiDa(lstCodiciTipiServAssociati, request);
						dettaglio.setLstTipiServizio(lstTipiServizi);

						if (dettaglio.getLstTipiServizio().size()==1){

							if (lstCodiciTipiServAssociati.size()==0){
								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.nonDefinitiServizi"));
							}
							else {
								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.associatiTuttiServizi"));
							}

							return Navigation.getInstance(request).goBack(true);
						}

					}
					else {
						dettaglio.setNuovo(false);
						//Dettaglio tipo servizio
						dettaglio.setTipoServizio(tipoServizio);
						dettaglio.setUltimoSalvato((TipoServizioVO)tipoServizio.clone());
						check(form, request);
					}

					dettaglio.setSessione(true);

					// verifica se nei parametri di biblioteca è impostato il campo "E' ammesso l'inserimento della richiesta da parte dell'utente"
					// per impostarlo nel form e riutilizzarlo quando si deve o meno prospettare l'equivalente campo  tra le proprietà del Servizio
					// se impostato nei parametri di biblioteca non verranno prospettati tra le proprietà del servizio i campi sull'inserimento
					// della richiesta da parte dell'utente e quello relativo anche da WEB
					ParametriBibliotecaVO parametri = this.getParametriBiblioteca(navi.getUtente().getCodPolo(), dettaglio.getTipoServizio().getCodBib(), request);
					if (parametri == null) {

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreConfiguraServizio"));

						this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
						NavigationForward goBack = Navigation.getInstance(request).goBack(true);
						return goBack;
					}
					dettaglio.setAmmInsUtenteParamBiblioteca(parametri.isAmmessoInserimentoUtente());

				}
			}
		} catch (ValidationException e) {
			resetToken(request);
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		return mapping.getInputForward();
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
		String pathChiamante = ((DettaglioTipoServizioForm)form).getChiamante();
		return backForward(mapping, pathChiamante);
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioTipoServizioForm currentForm = (DettaglioTipoServizioForm)form;

		if (Navigation.getInstance(request).isFromBar() ) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		try {
			check(form, request);
			TipoServizioVO tipoServizioVO = currentForm.getTipoServizio();
			if (!tipoServizioVO.equals(currentForm.getUltimoSalvato())) {
				Navigation navi = Navigation.getInstance(request);
				String utente = navi.getUtente().getFirmaUtente();
				Timestamp now = DaoManager.now();

				if (currentForm.isNuovo()) {
					//Nuovo tipo servizio
					tipoServizioVO.setUteIns(utente);
					tipoServizioVO.setUteVar(utente);
					tipoServizioVO.setTsIns(now);
					tipoServizioVO.setTsVar(now);
					tipoServizioVO.setFlCanc("N");

					String codTipoServizio = tipoServizioVO.getCodiceTipoServizio();
					if (!ValidazioneDati.isFilled(codTipoServizio)) {

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.scegliTipoServizio"));

						resetToken(request);
						return mapping.getInputForward();
					}

					//Conferma all'utente dell'aggiornamento

					currentForm.setConferma(true);

					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));
					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					return mapping.getInputForward();

				} else {
					//Aggiornamento tipo servizio
					tipoServizioVO.setUteVar(utente);
					tipoServizioVO.setTsVar(now);
					//Conferma all'utente dell'aggiornamento

					currentForm.setConferma(true);

					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

					this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
					return mapping.getInputForward();
				}
			}
		} catch (ValidationException e) {
			resetToken(request);
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}

		resetToken(request);
		return mapping.getInputForward();
	}


	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioTipoServizioForm dettaglio = (DettaglioTipoServizioForm)form;
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


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioTipoServizioForm currentForm = (DettaglioTipoServizioForm)form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() ) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		TipoServizioVO tipoServizioVO = currentForm.getTipoServizio();
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			GestioneServizi servizi = factory.getGestioneServizi();
			boolean operazioneOK=servizi.aggiornaTipoServizio(Navigation.getInstance(request).getUserTicket(), tipoServizioVO);
			if (operazioneOK) {
				currentForm.setUltimoSalvato((TipoServizioVO)tipoServizioVO.clone());

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

				resetToken(request);
				return mapping.findForward("indietro");
			} else {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));

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
