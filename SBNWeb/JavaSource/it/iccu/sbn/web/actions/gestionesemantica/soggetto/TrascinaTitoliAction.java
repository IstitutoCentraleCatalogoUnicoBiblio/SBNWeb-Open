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
package it.iccu.sbn.web.actions.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ListaTitoliTrascinaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.TrascinaTitoliForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class TrascinaTitoliAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory.getLog(TrascinaTitoliAction.class);

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

	private void loadSoggettario(String ticket, ActionForm form) throws Exception {
		TrascinaTitoliForm currentForm = (TrascinaTitoliForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TrascinaTitoliForm currentForm = (TrascinaTitoliForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		RicercaSoggettoListaVO currentFormLista = null;
		RicercaComuneVO currentFormRicerca = null;
		String chiamante = null;
		String cidTrascinaDa = null;
		String testoTrascinaDa = null;
		String cidTrascinaA = null;
		String testoTrascinaA = null;
		AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici = null;

		if (!currentForm.isSessione()) {
			log.info("TrascinaTitoliAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			cidTrascinaDa = (String) request.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA);
			testoTrascinaDa = (String) request.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA);
			cidTrascinaA = (String) request.getAttribute(NavigazioneSemantica.TRASCINA_CID_ARRIVO);
			testoTrascinaA = (String) request.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO);
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			currentFormLista = (RicercaSoggettoListaVO) request
			.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			currentFormRicerca = (RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
			List<?> sinteticaTrascina = (List<?>) request
					.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA);
			datiBibliografici = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI);

			List<ListaTitoliTrascinaVO> appoArray = new ArrayList<ListaTitoliTrascinaVO>();
			for (int i = 0; i < sinteticaTrascina.size(); i++) {
				ListaTitoliTrascinaVO lisTrasVO = new ListaTitoliTrascinaVO(
						(SinteticaTitoliView) sinteticaTrascina.get(i));

				//lisTrasVO.setProgr(i + 1);
				appoArray.add(lisTrasVO);

			}

			currentForm.setListaSintetica(appoArray);

			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setRicercaComune(currentFormRicerca);
			currentForm.setAction(chiamante);
			currentForm.setOutputTrascina(currentFormLista);
			currentForm.setCidTrascinaDa(cidTrascinaDa);
			currentForm.setTestoTrascinaDa(testoTrascinaDa.trim());
			currentForm.setCidTrascinaA(cidTrascinaA);
			currentForm.setTestoTrascinaA(testoTrascinaA.trim());

			ActionMessages errors = currentForm.validate(mapping, request);
			try {
				this.loadSoggettario(Navigation.getInstance(request).getUserTicket(), currentForm);
			} catch (Exception ex) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
			}

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

			currentForm.setAction((String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER));

			ParametriSoggetti parametri = ParametriSoggetti.retrieve(request);
			if (parametri != null)
				currentForm.setParametriSogg(parametri);
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();

	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TrascinaTitoliForm currentForm = (TrascinaTitoliForm) form;
		List<String> appoLista = new ArrayList<String>();
		ActionMessages errors = new ActionMessages();
		int i;
		for (i = 0; i < currentForm.getListaSintetica().size(); i++) {
			ListaTitoliTrascinaVO richOff = (ListaTitoliTrascinaVO) currentForm
					.getListaSintetica().get(i);
			if ((richOff.getSelezBox() != null)
					&& richOff.getSelezBox().length() > 0) {
				// appoLista.add(richOff);
				appoLista.add(new String(richOff.getBid()));
			}

		}
		if (appoLista.size() > 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.okTra"));
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

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TrascinaTitoliForm currentForm = (TrascinaTitoliForm) form;
		ActionMessages errors = new ActionMessages();
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune()
				.getCodSoggettario());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
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

		int i1;
		for (i1 = 0; i1 < areaDatiPassReturn.getListaSintetica().size(); i1++) {
			ListaTitoliTrascinaVO lisTrasVO = new ListaTitoliTrascinaVO();
			lisTrasVO.setProgr(((SinteticaTitoliView) areaDatiPassReturn
					.getListaSintetica().get(i1)).getProgressivo());
			lisTrasVO.setBid(((SinteticaTitoliView) areaDatiPassReturn
					.getListaSintetica().get(i1)).getBid());
			lisTrasVO.setIsbdELegami(((SinteticaTitoliView) areaDatiPassReturn
					.getListaSintetica().get(i1)).getDescrizioneLegami());
			lisTrasVO.setStato(((SinteticaTitoliView) areaDatiPassReturn
					.getListaSintetica().get(i1)).getLivelloAutorita());
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

		TrascinaTitoliForm currentForm = (TrascinaTitoliForm) form;
		AreaDatiAccorpamentoReturnVO ritorno = null;
		List<?> appoLista = currentForm.getListaTitoliSelezionati();
		AreaDatiAccorpamentoVO area = new AreaDatiAccorpamentoVO();
		area.setIdElementoEliminato(currentForm.getCidTrascinaDa());
		area.setIdElementoAccorpante(currentForm.getCidTrascinaA());
		// area.setTipoAuthority(SbnAutority.)
		String[] listaID = new String[appoLista.size()];
		listaID = appoLista.toArray(listaID);
		area.setIdTitoliLegati(listaID);

		// CHIAMO EJB PER EFFETTUARE TRASCINAMENTO usando il parametro List
		// appoLista
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		ritorno = factory.getGestioneSemantica().trascinaTitoliTraSoggetti(
				currentForm.getRicercaComune().isPolo(), area,
				utenteCollegato.getTicket());
		if (!ritorno.getCodiceRitorno().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", ritorno.getIdErrore()));
			this.saveErrors(request, errors);
			currentForm.setEnableConferma(false);
			resetToken(request);
			return mapping.getInputForward();
		}

		String xid = currentForm.getCidTrascinaA();
		String tipo = NavigazioneSemantica.TIPO_OGGETTO_CID;
		this.ricaricaReticolo(xid, tipo, currentForm, request);
		request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, xid);
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune()
				.getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, currentForm.getRicercaComune()
				.getDescSoggettario());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, new Boolean(currentForm.getRicercaComune()
				.isPolo()));
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
		request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, null);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		return mapping.findForward("analiticaSoggetto");
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TrascinaTitoliForm currentForm = (TrascinaTitoliForm) form;
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
		TrascinaTitoliForm currentForm = (TrascinaTitoliForm) form;
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

		TrascinaTitoliForm currentForm = (TrascinaTitoliForm) form;
		int i;
		for (i = 0; i < currentForm.getListaSintetica().size(); i++) {
			ListaTitoliTrascinaVO titolo = (ListaTitoliTrascinaVO) currentForm.getListaSintetica().get(i);
			//if (titolo.isLocalizzato())
			titolo.setSelezBox(String.valueOf(i));
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

	private void ricaricaReticolo(String xid, String tipo, ActionForm form,
			HttpServletRequest request) throws Exception {

		if (tipo.equals(NavigazioneSemantica.TIPO_OGGETTO_CID)) {
			String cid = xid;
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			AnaliticaSoggettoVO analitica = null;
			try {
				analitica = factory.getGestioneSemantica()
						.creaAnaliticaSoggettoPerCid(
								((TrascinaTitoliForm) form).getRicercaComune()
										.isPolo(), cid, utenteCollegato.getTicket());

			} catch (ValidationException e) {
				// errori indice
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				return;

			} catch (DataException e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				return;
			} catch (InfrastructureException e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				return;
			} catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.erroreSistema", e.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				return;
			}
			if (!analitica.isEsitoOk() ) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", analitica
								.getTestoEsito()));
				this.saveErrors(request, errors);
			}

			((TrascinaTitoliForm) form).setTreeElementViewSoggetti(analitica
					.getReticolo());
			request.setAttribute(NavigazioneSemantica.ANALITICA, ((TrascinaTitoliForm) form)
					.getTreeElementViewSoggetti());
			String livelloAut = analitica.getReticolo().getLivelloAutorita();
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, livelloAut);
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, livelloAut);
			String dataInserimento = analitica.getDataInserimento();
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, dataInserimento);
			String dataVariazione = analitica.getDataVariazione();
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dataVariazione);
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dataVariazione);
			String tipoSoggetto = analitica.getReticolo()
					.getCategoriaSoggetto();
			request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_POLO, analitica.getReticolo().getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO().getNumTitoliPolo());
			request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_BIBLIO, analitica.getReticolo().getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO().getNumTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, tipoSoggetto);
			String testo = analitica.getReticolo().getTesto();
			request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, testo);

		}

	}

}
