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
package it.iccu.sbn.web.actions.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ListaTitoliTrascinaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.TrascinaTitoliClassiForm;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class TrascinaTitoliClassiAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory
			.getLog(TrascinaTitoliClassiAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.selTutti", "tutti");
		map.put("button.deselTutti", "deseleziona");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		map.put("button.ok", "ok");
		map.put("button.si", "si");
		map.put("button.no", "no");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TrascinaTitoliClassiForm currentForm = (TrascinaTitoliClassiForm) form;
		if (Navigation.getInstance(request).isFromBar()) {
			return mapping.getInputForward();
		}

		String notazioneTrascinaDa = null;
		String testoTrascinaDa = null;
		String notazioneTrascinaA = null;
		String testoTrascinaA = null;
		AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici = null;
		RicercaClasseListaVO currentFormLista = null;
		RicercaClassiVO currentFormRicerca = null;

		if (!currentForm.isSessione()) {

			log.info("TrascinaTitoliClassiAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			notazioneTrascinaDa = (String) request
					.getAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA);
			testoTrascinaDa = (String) request
					.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA);
			notazioneTrascinaA = (String) request
					.getAttribute(NavigazioneSemantica.TRASCINA_CLASSE_ARRIVO);
			testoTrascinaA = (String) request
					.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO);
			List<?> sinteticaTrascina = (List<?>) request
					.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA);
			datiBibliografici = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI);
			currentFormLista = (RicercaClasseListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			currentFormRicerca = (RicercaClassiVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);

			List<ListaTitoliTrascinaVO> appoArray = new ArrayList<ListaTitoliTrascinaVO>();
			for (int i = 0; i < sinteticaTrascina.size(); i++) {
				ListaTitoliTrascinaVO lisTrasVO = new ListaTitoliTrascinaVO((SinteticaTitoliView) sinteticaTrascina.get(i));
				lisTrasVO.setUtilizzato("SI");
				appoArray.add(lisTrasVO);
			}

			currentForm.setListaSintetica(appoArray);

			currentForm.setSessione(true);
			currentForm.setNotazioneTrascinaDa(notazioneTrascinaDa);
			currentForm.setTestoTrascinaDa(testoTrascinaDa.trim());
			currentForm.setNotazioneTrascinaA(notazioneTrascinaA);
			currentForm.setTestoTrascinaA(testoTrascinaA.trim());
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));
		}
		currentForm.setRicercaClasse(currentFormRicerca);
		currentForm.setOutputTrascina(currentFormLista);
		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		currentForm.setBlocchiCaricati(appoggio);
		if (datiBibliografici != null) {
			String idLista = datiBibliografici.getIdLista();
			currentForm.setIdLista(idLista);
			super.addSbnMarcIdLista(request, idLista);
			currentForm.setMaxRighe(datiBibliografici.getMaxRighe());
			currentForm.setNumBlocco(datiBibliografici.getNumBlocco());
			currentForm.setTotBlocchi(datiBibliografici.getTotBlocchi());
			currentForm.setTotRighe(datiBibliografici.getTotRighe());
		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();

	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TrascinaTitoliClassiForm currentForm = (TrascinaTitoliClassiForm) form;
		ActionMessages errors = new ActionMessages();
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		if (currentForm.getNumBlocco() > currentForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.sogNotFound"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		Set<Integer> appoggio = currentForm.getBlocchiCaricati();
		int i = currentForm.getNumBlocco();

		if (appoggio != null) {
			if (appoggio.contains(i)) {
				return mapping.getInputForward();
			}
		}
		AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());

		areaDatiPass.setTipoOrdinam("1");
		areaDatiPass.setTipoOutput("001");
		areaDatiPass.setRicercaPolo(true);
		areaDatiPass.setRicercaIndice(false);

		areaDatiPass.setRicercaPolo(currentForm.getRicercaClasse().isPolo());
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().getNextBloccoTitoli(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		List<?> listaSintetica = areaDatiPassReturn.getListaSintetica();
		for (int i1 = 0; i1 < listaSintetica.size(); i1++) {
			ListaTitoliTrascinaVO lisTrasVO = new ListaTitoliTrascinaVO(
					(SinteticaTitoliView) listaSintetica.get(i1));
			lisTrasVO.setUtilizzato("SI");
			currentForm.getListaSintetica().add(lisTrasVO);

		}

		String idLista = areaDatiPassReturn.getIdLista();
		currentForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		currentForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = currentForm.getNumBlocco();
		appoggio = currentForm.getBlocchiCaricati();
		appoggio.add(numBlocco);
		currentForm.setBlocchiCaricati(appoggio);

		currentForm.setNumBlocco(numBlocco);

		Collections.sort(currentForm.getListaSintetica(),
				ListaTitoliTrascinaVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TrascinaTitoliClassiForm currentForm = (TrascinaTitoliClassiForm) form;
		List<String> appoLista = new ArrayList<String>();
		ActionMessages errors = new ActionMessages();

		List<ListaTitoliTrascinaVO> listaSintetica = currentForm.getListaSintetica();
		for (ListaTitoliTrascinaVO richOff : listaSintetica) {
			if (ValidazioneDati.isFilled(richOff.getSelezBox()) )
				appoLista.add(new String(richOff.getBid()));
		}

		if (appoLista.size() > 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.okTraCla"));
			this.saveErrors(request, errors);
			currentForm.setListaTitoliSelezionati(appoLista);
			currentForm.setEnableConferma(true);
			this.preparaConferma(mapping, request);
			return mapping.getInputForward();

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessuna ceckbox selezionata
			return mapping.getInputForward();
		}
	}

	private void preparaConferma(ActionMapping mapping,
			HttpServletRequest request) {
		ActionMessages messages = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("button.parameter", mapping
				.getParameter());
		messages.add("gestionesemantica.parameter.conferma", msg1);
		this.saveMessages(request, messages);
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		TrascinaTitoliClassiForm currentForm = (TrascinaTitoliClassiForm) form;
		AreaDatiAccorpamentoReturnVO ritorno = null;
		List<?> appoLista = currentForm.getListaTitoliSelezionati();
		AreaDatiAccorpamentoVO richiesta = new AreaDatiAccorpamentoVO();
		richiesta.setIdElementoEliminato(currentForm.getNotazioneTrascinaDa());
		richiesta.setIdElementoAccorpante(currentForm.getNotazioneTrascinaA());
		String[] listaID = new String[appoLista.size()];
		listaID = appoLista.toArray(listaID);
		richiesta.setIdTitoliLegati(listaID);

		CreaVariaClasseVO classe;
		Navigation navi = Navigation.getInstance(request);

		try {
			// CHIAMO EJB PER EFFETTUARE TRASCINAMENTO usando il parametro
			// List
			// appoLista
			UserVO utenteCollegato = navi.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ritorno = factory.getGestioneSemantica().trascinaTitoliTraClassi(
					currentForm.getRicercaClasse().isPolo(), richiesta,
					utenteCollegato.getTicket());

			if (!ritorno.getCodiceRitorno().equals("0000")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", ritorno.getIdErrore()));
				this.saveErrors(request, errors);
				currentForm.setEnableConferma(false);
				resetToken(request);
				return mapping.getInputForward();
			}

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.operOk"));
			this.saveErrors(request, errors);

			request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA,
					currentForm.getOutputTrascina());
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
					currentForm.getRicercaClasse().clone());

			// return mapping.findForward("listaSintetica");

			classe = factory.getGestioneSemantica().analiticaClasse(true,
					currentForm.getNotazioneTrascinaA(), utenteCollegato.getTicket());

		} catch (EJBException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.gestioneSemantica.indiceTestoEsito", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (ValidationException ve) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreValidazione", ve
							.getMessage()));
			this.saveErrors(request, errors);
			log.error("", ve);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		DettaglioClasseVO dettaglio = new DettaglioClasseVO(classe);
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, dettaglio);

		navi.purgeThis();
		return mapping.findForward("esamina");

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TrascinaTitoliClassiForm currentForm = (TrascinaTitoliClassiForm) form;
		currentForm.setEnableConferma(false);
		this.deselezionaTutti(form);
		return mapping.getInputForward();
	}

	public ActionForward deseleziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		this.deselezionaTutti(form);

		return mapping.getInputForward();
	}

	private void deselezionaTutti(ActionForm form) {
		TrascinaTitoliClassiForm currentForm = (TrascinaTitoliClassiForm) form;
		int i;
		for (i = 0; i < currentForm.getListaSintetica().size(); i++) {
			ListaTitoliTrascinaVO richOff = (ListaTitoliTrascinaVO) currentForm
					.getListaSintetica().get(i);
			richOff.setSelezBox(null);
		}
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TrascinaTitoliClassiForm currentForm = (TrascinaTitoliClassiForm) form;
		int i;
		for (i = 0; i < currentForm.getListaSintetica().size(); i++) {
			ListaTitoliTrascinaVO richOff = (ListaTitoliTrascinaVO) currentForm
					.getListaSintetica().get(i);
			richOff.setSelezBox(String.valueOf(i));
		}
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.saveErrors(request, errors);
		// nessun codice selezionato
		return mapping.getInputForward();
	}

}
