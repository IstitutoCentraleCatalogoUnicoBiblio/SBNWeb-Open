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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.common.TipoAggiornamentoIter;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.servizi.configurazione.DettaglioAttivitaForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationForward;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class DettaglioAttivitaAction extends ConfigurazioneBaseAction {

	public static final String CONFERMA_CONTINUA_CONFIGURAZIONE           = "CONFERMA_CONTINUA_CONFIGURAZIONE";
	public static final String CONFERMA_CONTINUA_CONFIGURAZIONE_ANNULLATA = "CONFERMA_CONTINUA_CONFIGURAZIONE_ANNULLATA";


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.annulla",       "chiudi");
		map.put("servizi.bottone.indietro",      "chiudi");
		map.put("servizi.bottone.bibliotecari",  "bibliotecari");
		map.put("servizi.bottone.controlloIter", "controlloIter");
		map.put("servizi.bottone.ok",            "ok");
		map.put("servizi.bottone.si",            "si");
		map.put("servizi.bottone.no",            "no");
		return map;
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		DettaglioAttivitaForm currentForm = (DettaglioAttivitaForm)form;

		HttpSession session = request.getSession();
		if (session.getAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE_ANNULLATA)!=null) {
			//vengo da Chiudi di  Abilitazione Bibliotecari in seguito all'aggiunta di una nuova attività
			session.removeAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE);
			session.removeAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE_ANNULLATA);
			//return mapping.findForward("indietro");
			NavigationForward goBack = navi.goBack();
			goBack.addParameter("ricaricaListaIter", "true");
			return goBack;
		}

		if (request.getAttribute("abilitata")!=null) {
			//vengo da OK di abilitazione bibliotecari
			session.removeAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE);
			if (((String) request.getAttribute("abilitata")).equalsIgnoreCase("true"))
				currentForm.getIterServizio().setFlgAbil("S");
			else
				currentForm.getIterServizio().setFlgAbil("N");

			return this.ok(mapping, form, request, response);
		}

		currentForm.setChiamante((String)request.getAttribute(ServiziBaseAction.PATH_CHIAMANTE_ATTR));

		try {
			if (!currentForm.isSessione()) {
				TipoServizioVO tipoServizio = (TipoServizioVO)request.getAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR);
				if (tipoServizio == null) {
					throw new ValidationException("Non è stato impostato il tipo servizio");
				}
				currentForm.setTipoServizioVO(tipoServizio);
				currentForm.setBiblioteca((String)request.getAttribute(ServiziBaseAction.BIBLIOTECA_ATTR));

				IterServizioVO iterServizio = (IterServizioVO)request.getAttribute(ServiziBaseAction.VO_ITER_SERVIZIO);
				if (iterServizio == null) {
					currentForm.setNuovo(true);
					currentForm.setUltimoSalvato(null);
					currentForm.setObbligatoria(true);
				} else {
					currentForm.setNuovo(false);
					currentForm.setIterServizio(iterServizio);
					currentForm.setObbligatoria(iterServizio.getObbl().equalsIgnoreCase("S") ? true : false);
					currentForm.setProrogabile(iterServizio.getFlgRinn().equalsIgnoreCase("S") ? true : false);
					currentForm.setUltimoSalvato((IterServizioVO)iterServizio.clone());
				}

				List<String> lstCodiciAttivita = (List<String>)request.getAttribute(ServiziBaseAction.LISTA_CODICI_ATTIVITA);
				if (lstCodiciAttivita != null)
					currentForm.setLstCodiciAttivitaGiaAssegnati(lstCodiciAttivita);
				else
					currentForm.setLstCodiciAttivitaGiaAssegnati(new ArrayList<String>());

				String tipo_operazione = request.getParameter("tipo_operazione");
				if (tipo_operazione != null)
					currentForm.setTipoOperazione(tipo_operazione);

				String progr_iter_scelto_str = (String)request.getAttribute(ServiziBaseAction.PROGRESSIVO_ITER_SCELTO);
				if (progr_iter_scelto_str != null) {
					currentForm.setProgressivoIterScelto(new Integer(progr_iter_scelto_str).intValue());
				}

				this.caricaSelectOption(currentForm, request);

				currentForm.setConferma(false);
				currentForm.setSessione(true);
			}
		} catch (ValidationException e) {
			log.error("", e);

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



	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			  HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (((DettaglioAttivitaForm)form).isConfermaContinuaConfigurazione()) {
			NavigationForward goBack = Navigation.getInstance(request).goBack();
			goBack.addParameter("ricaricaListaIter", "true");
			return goBack;
		}

		return this.backForward(request);
	}


	public ActionForward controlloIter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}
		if (!isTokenValid(request)) {
			saveToken(request);
		}

		DettaglioAttivitaForm dettaglio = (DettaglioAttivitaForm)form;
		//dettaglio.setGestioneControllo(true);

		request.setAttribute(ServiziBaseAction.VO_ITER_SERVIZIO,         dettaglio.getIterServizio());
		request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, dettaglio.getTipoServizioVO());
		request.setAttribute(ServiziBaseAction.BIBLIOTECA_ATTR,       dettaglio.getBiblioteca());

		return mapping.findForward("configuraControlloIter");
	}


	public ActionForward bibliotecari(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (request.getSession().getAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE_ANNULLATA)!=null) {
			//vengo da Chiudi di  Abilitazione Bibliotecari in seguito all'aggiunta di una nuova attività
			request.getSession().removeAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE);
			request.getSession().removeAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE_ANNULLATA);
			return mapping.findForward("indietro");
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		DettaglioAttivitaForm dettaglio = (DettaglioAttivitaForm)form;
		if (dettaglio.getIterServizio()!=null) {
			impostaAttivita(dettaglio);
			request.setAttribute(ServiziBaseAction.DESC_ATTIVITA_ATTR, dettaglio.getIterServizio().getDescrizione());
			request.setAttribute(ServiziBaseAction.COD_ATTIVITA_ATTR, dettaglio.getIterServizio().getCodAttivita());
		}
		request.setAttribute(ServiziBaseAction.VO_TIPO_SERVIZIO_ATTR, dettaglio.getTipoServizioVO());

		return mapping.findForward("bibliotecari");
	}


	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}
		DettaglioAttivitaForm dettaglio = (DettaglioAttivitaForm)form;

		try {
			//controllo se OK() si riferisce all'inserimento dei controlli
//			if (dettaglio.isGestioneControllo()) {
//				return this.gestioneControlloIter(mapping, form, request, response);
//			}

			//gestione OK per dettaglio attività
			this.checkOk(form, request);

			IterServizioVO iterVO = dettaglio.getIterServizio();
			iterVO.setObbl(dettaglio.isObbligatoria() ? "S" : "N");
			iterVO.setFlgRinn(dettaglio.isProrogabile() ? "S" : "N");

			if (!iterVO.equals(dettaglio.getUltimoSalvato())) {
				//Ci sono modifiche da salvare
				Navigation navi = Navigation.getInstance(request);
				String utente = navi.getUtente().getFirmaUtente();
				Timestamp now = DaoManager.now();
				if (dettaglio.isNuovo()) {
					iterVO.setFlCanc("N");
					iterVO.setUteIns(utente);
					iterVO.setUteVar(utente);
					iterVO.setTsIns(now);
					iterVO.setTsVar(now);

					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					boolean operazioneOK = factory.getGestioneServizi()
							.aggiornaIter(Navigation.getInstance(request).getUserTicket(), dettaglio.getTipoServizioVO().getIdTipoServizio(),
										  (dettaglio.getTipoOperazione().equalsIgnoreCase("I") ? dettaglio.getProgressivoIterScelto() : 0),
										  iterVO,
										  ((dettaglio.getTipoOperazione().equalsIgnoreCase("A")) ? TipoAggiornamentoIter.AGGIUNTA : ((dettaglio.getTipoOperazione().equalsIgnoreCase("I")) ? TipoAggiornamentoIter.INSERIMENTO : TipoAggiornamentoIter.MODIFICA) ),
										  true);

					if (operazioneOK) {
						dettaglio.setUltimoSalvato((IterServizioVO)iterVO.clone());

						dettaglio.setNuovo(false);
						dettaglio.setConfermaContinuaConfigurazione(true);

						LinkableTagUtils.addError(request, new ActionMessage("servizi.dettaglioAttivita.confermaAssociazioneBibliotecari"));

						this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));

						request.getSession().setAttribute(DettaglioAttivitaAction.CONFERMA_CONTINUA_CONFIGURAZIONE, "true");
						return mapping.getInputForward();
					} else {

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.AggiornamentoNonEffettuato"));

					}

				} else {
					//Aggiornamento
					iterVO.setUteVar(utente);
					iterVO.setTsVar(DaoManager.now());

					if (request.getAttribute("abilitata")!=null) {
						//se l'aggiornamento consiste solo nella modifica dei bibliotecari associati all'attività
						//e quindi devo modificare solo il flag abilitazione non chiedo conferma
						return this.si(mapping, form, request, response);
					} else {
						//Nel caso di aggiornamento chiedo conferma all'utente
						dettaglio.setConferma(true);

						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.confermaOperazione"));

						this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
						return mapping.getInputForward();
					}
				}
			}
			else {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.noSalvaNoVariazioni"));

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

		resetToken(request);

		return mapping.getInputForward();
	}



	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioAttivitaForm dettaglio = (DettaglioAttivitaForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		//il no si riferisce all'aggiornamento dei dettagli dell'attività
		dettaglio.setConferma(false);
		return mapping.getInputForward();
	}


	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		DettaglioAttivitaForm dettaglio = (DettaglioAttivitaForm)form;

		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
		}


		IterServizioVO iterVO = dettaglio.getIterServizio();
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			boolean operazioneOK = factory.getGestioneServizi()
					.aggiornaIter(Navigation.getInstance(request).getUserTicket(), dettaglio.getTipoServizioVO().getIdTipoServizio(),
								  0, iterVO,
								  TipoAggiornamentoIter.MODIFICA, false);

			if (operazioneOK) {
				dettaglio.setUltimoSalvato((IterServizioVO)iterVO.clone());

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));

				resetToken(request);
				//return mapping.findForward("indietro");
				NavigationForward goBack = Navigation.getInstance(request).goBack();
				goBack.addParameter("ricaricaListaIter", "true");
				return goBack;

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


	private void caricaSelectOption(ActionForm form, HttpServletRequest request) throws Exception {
		//Stati richiesta
		//almaviva5_20110523 segnalazione TO0
		//form.setLstCodiciRichiesta(this.loadCodiciStatoRichiesta(request));
		DettaglioAttivitaForm currentForm = (DettaglioAttivitaForm)form;
		try {
			List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_STATO_RICHIESTA, true);
			Iterator<TB_CODICI> i = codici.iterator();
			while (i.hasNext()) {
				if (ValidazioneDati.equals(i.next().getCd_flg4(), "N")) //non visibile in conf.
					i.remove();
			}
			CaricamentoCombo combo = new CaricamentoCombo();
			currentForm.setLstCodiciRichiesta(combo.loadComboCodiciDesc(codici));
		} catch (Exception e) {
			log.error("", e);
		}

		//Stati movimento
		currentForm.setLstCodiciMovimento(this.loadCodiciStatoMovimento(request));

		//Codici attività
		currentForm.setLstCodiciAttivita(this.loadCodiciAttivitaDiversiDa(currentForm.getLstCodiciAttivitaGiaAssegnati(), request));
	}


	private void checkOk(ActionForm form, HttpServletRequest request)
	throws ValidationException {
		DettaglioAttivitaForm currentForm = (DettaglioAttivitaForm)form;

		boolean checkOK=true;

		IterServizioVO iterVO = currentForm.getIterServizio();

		if (iterVO.getNumPag()>0) iterVO.setFlagStampa("S");

		if (currentForm.isNuovo()) {
			impostaAttivita(currentForm);

			if (iterVO.getCodAttivita()==null || iterVO.getCodAttivita().equals("")) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.attivitaAssente"));

				checkOK=false;
			}
		}
		if (iterVO.getCodStatoMov()==null || iterVO.getCodStatoMov().equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.dettaglioIter.movimentoAssente"));

			checkOK=false;
		}
		if (iterVO.getCodStatoRich()==null || iterVO.getCodStatoRich().equals("")) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazione.dettaglioIter.richiestaAssente"));

			checkOK=false;
		}

		if(!checkOK) throw new ValidationException("Validazione dati fallita");
	}


	private void impostaAttivita(DettaglioAttivitaForm dettaglio) {
		IterServizioVO iterVO = dettaglio.getIterServizio();

		if (dettaglio.isNuovo()) {
			String comboValue = iterVO.getCodAttivita();
			if (comboValue!=null && !comboValue.equals("")){
				iterVO.setDescrizione(comboValue.substring(comboValue.indexOf("_")+1));
				iterVO.setCodAttivita(comboValue.substring(0, comboValue.indexOf("_")));
			}
		}
	}
}
