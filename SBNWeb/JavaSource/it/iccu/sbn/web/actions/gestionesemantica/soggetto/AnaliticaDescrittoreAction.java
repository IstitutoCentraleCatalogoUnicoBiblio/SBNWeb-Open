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
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaLegameSoggettoDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiLegameDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.AnaliticaDescrittoreForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate.RicercaSoggettoResult;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class AnaliticaDescrittoreAction extends LookupDispatchAction implements
		SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(AnaliticaDescrittoreAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.inserisci", "inserisci");
		map.put("button.esamina", "dettaglio");
		map.put("button.gestione", "gestione");
		map.put("button.elimina", "elimina");
		map.put("button.modifica", "modifica");
		map.put("button.stampa", "stampa");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.soggetti", "soggetti");
		map.put("button.utilizzati", "utilizzati");
		map.put("button.annulla", "annulla");
		// tasti per l'avanzamento su elemento precedente/successivo
		map.put("button.elemPrec", "elementoPrecedente");
		map.put("button.elemSucc", "elementoSuccessivo");

		map.put("button.scambia", "scambia");
		map.put("button.conferma", "conferma");
		return map;
	}

	private TreeElementViewSoggetti getNodoSelezionato(
			HttpServletRequest request, ActionForm form) {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		ActionMessages errors = new ActionMessages();
		String codSelezionato = currentForm.getNodoSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			return null;
		}

		try {

			return (TreeElementViewSoggetti) currentForm
					.getTreeElementViewSoggetti().findElementUnique(
							Integer.valueOf(codSelezionato));

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			return null;
		}

	}

	private void setErrors(HttpServletRequest request, ActionMessages errors,
			Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private boolean initCombo(HttpServletRequest request, String ticket,
			ActionForm form) {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		try {
			currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, false));
			currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));
			//almaviva5_20111130 evolutive CFI
			currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
			currentForm.setListaCategoriaTermine(CaricamentoComboSemantica.loadComboCategoriaSogDes(SbnAuthority.DE));
			return true;
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.setErrors(request, errors, e);
			return false;
		}
	}

	private boolean mostraReticolo(ActionForm form, HttpServletRequest request)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;

		String parameter = request
				.getParameter(TreeElementView.TREEVIEW_KEY_PARAM);
		if (parameter == null)
			return false;

		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		TreeElementViewSoggetti descrittore = (TreeElementViewSoggetti) root
				.findElementUnique(Integer.valueOf(parameter));
		Navigation.getInstance(request).getCache().getCurrentElement()
				.setAnchorId(LinkableTagUtils.ANCHOR_PREFIX + parameter);

		// nodo aperto
		if (descrittore.isOpened()) {
			descrittore.close();
			return true;
		}

		if (!descrittore.isExplored()) {

			AnaliticaSoggettoVO analitica = SoggettiDelegate.getInstance(request)
					.caricaSottoReticoloDescrittore(
							descrittore.isLivelloPolo(), descrittore.getKey(),
							root.findMaxElement());

			if (analitica == null || !analitica.isEsitoOk()) {
				// errore
				currentForm.setNodoSelezionato(null);
				return true;
			}

			descrittore.adoptChildren(analitica.getReticolo());
			descrittore.setRadioVisible(true);
			descrittore.open();
		} else {
			descrittore.setRadioVisible(true);
			descrittore.open();
		}

		request.setAttribute(NavigazioneSemantica.ANALITICA, root);
		currentForm.setTreeElementViewSoggetti(root);
		currentForm.setNodoSelezionato(parameter);

		return true;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		TreeElementViewSoggetti newReticolo = (TreeElementViewSoggetti) request
				.getAttribute(NavigazioneSemantica.ANALITICA);

		if (newReticolo != null)
			root = newReticolo;

		if (root != null
				&& (request.getParameter(mapping.getParameter()) == null)) {

			currentForm.setTreeElementViewSoggetti(root);
			updateCombo(request, form);
		}

		return super.execute(mapping, form, request, response);
	}

	private void updateCombo(HttpServletRequest request, ActionForm form) {
		// submit dal tree
		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		String codSelezionato = currentForm.getNodoSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato))
			codSelezionato = String.valueOf(root.getRepeatableId());
		currentForm.setNodoSelezionato(codSelezionato);

		currentForm.setComboGestione(LabelGestioneSemantica
				.getComboGestioneSematica(servlet.getServletContext(), request,
						form, (TreeElementViewSoggetti) root
								.findElementUnique(Integer
										.valueOf(codSelezionato)), this));
		currentForm.setIdFunzione("");
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		ActionMessages errors = new ActionMessages();

		log.debug("AnaliticaDescrittoreAction::unspecified");
		currentForm.setVisualCheckCattura(false);
		ElementoSinteticaDescrittoreVO didCorrente = null;
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar()) {
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();
		}

		if (root != null)
			request.setAttribute(NavigazioneSemantica.ANALITICA, root);

		if (this.mostraReticolo(form, request)) {
			updateCombo(request, form); // aggiorno combo gestione
			return mapping.getInputForward();
		}

		// NELL'IPOTESI IN CUI PROVENGO DA SINTETICA DESCRITTORI E HO CHIESTO
		// L'ESAMINA DI UN DETERMINATO CID
		// HO IL TREEELEMENT GIA' CARICATO
		currentForm.setTreeElementViewSoggetti((TreeElementViewSoggetti) request.getAttribute(NavigazioneSemantica.ANALITICA));
		root = currentForm.getTreeElementViewSoggetti();

		// link su key albero
		String linkTree = request.getParameter(TreeElementView.TREEVIEW_URL_PARAM);
		if (linkTree != null) {
			currentForm.setNodoSelezionato(linkTree);
			return dettaglio(mapping, form, request, response);
		}

		currentForm.setDid((String) request
				.getAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO));
		currentForm.setDidPadre((String) request
				.getAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI));
		currentForm.setT005((String) request
				.getAttribute(NavigazioneSemantica.DATA_MODIFICA_DID_RADICE));

		Boolean isPoloReq = ((Boolean) request
				.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO));

		RicercaComuneVO paramRicerca = (RicercaComuneVO) request
				.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);

		if (root != null) {

			String[] listaDidSelez = (String[]) request
					.getAttribute(NavigazioneSemantica.LISTA_OGGETTI_SELEZIONATI);
			if (listaDidSelez != null) {
				currentForm.setListaDidSelez(listaDidSelez);
				currentForm.setPosizioneCorrente(0);
			}

			if (paramRicerca != null)
				currentForm.setRicercaComune(paramRicerca);

			DettaglioDescrittoreVO dettaglioDes = (DettaglioDescrittoreVO) root.getDettaglio();

			if (isPoloReq != null)
				currentForm.getRicercaComune()
						.setPolo(isPoloReq.booleanValue());
			currentForm
					.getRicercaComune()
					.setDescSoggettario(
							(String) request
									.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO));

			currentForm.setEnableIndice(currentForm.getRicercaComune()
					.isIndice());

			String dataInserimento = (String) request
					.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			currentForm.setDataInserimento(dataInserimento);
			String dataVariazione = (String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			currentForm.setDataVariazione(dataVariazione);
			currentForm.setEnableModifica(true);

			if (!initCombo(request, navi.getUserTicket(), form))
				return mapping.getInputForward();

			currentForm.setOutputDescrittori((RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));
			// INSERISCO RICHIESTA NUMERO TOTALE SOGGETTI COLLEGATI AL DID
			currentForm.setNumSoggetti(dettaglioDes.getSoggettiCollegati());
			currentForm.setNumUtilizzati(""
					+ dettaglioDes.getSoggettiUtilizzati());
			currentForm.setEnableNumSogg((currentForm.getNumSoggetti() > 0));

			OggettoRiferimentoVO oggettoRiferimento = ((SemanticaBaseForm) navi
					.getCallerForm()).getOggettoRiferimento();
			currentForm.setEnableCercaIndice(currentForm.getRicercaComune().isPolo()
					&& !oggettoRiferimento.isEnabled());

			String xid = (String) request
					.getAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID);
			if (xid != null && xid.length() > 0) {
				String tipo = (String) request
						.getAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE);
				this.ricaricaReticolo(xid, tipo, currentForm, request);
			}

			return mapping.getInputForward();
		}

		RicercaSoggettoListaVO ricSoggLista = null;
		RicercaComuneVO ricSoggRicerca = null;
		RicercaSoggettoListaVO ricSoggListaDescr = null;
		boolean isPolo = false;

		// String tipo = null;

		// NON HO BISOGNO DI STABILIRE L'OGGETTO PADRE PER RICARICARE IL
		// RETICOLO
		// TRATTO SEMPRE UN DID

		String xid = (String) request.getAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID);
		if (ValidazioneDati.isFilled(xid) ) {
			String tipo = (String) request.getAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE);
			this.ricaricaReticolo(xid, tipo, currentForm, request);
		}

		if (!currentForm.isSessione()) {
			// devo inizializzare tramite le request.getAttribute(......)
			String chiamante = null;
			ricSoggLista = (RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);

			isPolo = isPoloReq.booleanValue();
			ricSoggListaDescr = (RicercaSoggettoListaVO) request
					.getAttribute("outputlistadescr");
			String[] listaDidSelez = (String[]) request
					.getAttribute(NavigazioneSemantica.LISTA_OGGETTI_SELEZIONATI);
			if (listaDidSelez != null) {
				currentForm.setListaDidSelez(listaDidSelez);
				currentForm.setPosizioneCorrente(0);
			}

			if (ricSoggLista == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			}
			if (chiamante == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setAction(chiamante);
			currentForm.setCodice(ricSoggLista.getSoggettario().getCodice());
			currentForm.setDescrizione(ricSoggLista.getSoggettario()
					.getDescrizione());
			didCorrente = (ElementoSinteticaDescrittoreVO) ricSoggLista
					.getRisultati().get(0);
			currentForm.setTermine(didCorrente.getTermine());
			currentForm.setOutputDescrittori(ricSoggListaDescr);
			currentForm.setEnableModifica(true);

			int progr;

			try {
				progr = Integer
						.parseInt((String) request
								.getAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO)) - 1;
				didCorrente = (ElementoSinteticaDescrittoreVO) ((RicercaSoggettoListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA))
						.getRisultati().get(progr);
				currentForm.setTermine(didCorrente.getTermine());

			} catch (Exception e) {
				progr = 0;
			}

			this.ricaricaReticolo(didCorrente.getDid(),
				NavigazioneSemantica.TIPO_OGGETTO_DID, currentForm,	request);

			root = currentForm.getTreeElementViewSoggetti();
			currentForm.setNodoSelezionato(String.valueOf(root.getRepeatableId()));

		} else {
			// Posso cercare in indice solo se è la prima volta che carico
			// il reticolo
			OggettoRiferimentoVO oggettoRiferimento = ((SemanticaBaseForm) navi
					.getCallerForm()).getOggettoRiferimento();
			currentForm.setEnableCercaIndice(currentForm.getRicercaComune().isPolo()
					&& !oggettoRiferimento.isEnabled());
		}

		currentForm.setDid(didCorrente.getDid());
		currentForm.setNumSoggettiIndice("");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (!initCombo(request,
				Navigation.getInstance(request).getUserTicket(), form))
			return mapping.getInputForward();

		this.setErrors(request, errors, null);
		currentForm.getRicercaComune().setPolo(isPolo);
		currentForm.setEnableIndice(currentForm.getRicercaComune().isIndice());
		currentForm.getRicercaComune().setCodSoggettario(
				currentForm.getCodice());
		currentForm.getRicercaComune().setDescSoggettario(
				currentForm.getDescrizione());

		currentForm.setEnableGestione(isPolo);
		currentForm.setEnableCercaIndice(false);

		currentForm.setNumUtilizzati("");
		currentForm.setRicercaComune(ricSoggRicerca);
		currentForm.setDidPadre((String) request
				.getAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI));
		currentForm.setT005((String) request
				.getAttribute(NavigazioneSemantica.DATA_MODIFICA_DID_RADICE));
		currentForm.setNodoSelezionato(String.valueOf(root.getRepeatableId()));
		currentForm.setAction((String) request
				.getAttribute(NavigazioneSemantica.ACTION_CALLER));
		return mapping.getInputForward();
	}

	private void ricaricaReticolo(String xid, String tipo,
			AnaliticaDescrittoreForm currentForm, HttpServletRequest request) {

		ActionMessages errors = new ActionMessages();
		String did = xid;
		AnaliticaSoggettoVO analitica = null;

		try {
			analitica = SoggettiDelegate.getInstance(request).caricaReticoloDescrittore(
					currentForm.getRicercaComune().isPolo(), did);

			if (analitica == null)
				return;

			if (analitica.isEsitoOk()) {

				currentForm.setTreeElementViewSoggetti(analitica.getReticolo());
				request.setAttribute(NavigazioneSemantica.ANALITICA,
						currentForm.getTreeElementViewSoggetti());

			} else {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", analitica
								.getTestoEsito()));
				this.setErrors(request, errors, null);
				return;
			}

		} catch (ValidationException e) {
			// errori indice

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			// nessun codice selezionato
			return;

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return;
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return;
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return;
		}

	}

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;

		ElementoSinteticaDescrittoreVO didCorrente = null;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		ActionMessages errors = new ActionMessages();
		RicercaComuneVO ricercaComune = currentForm.getRicercaComune().copy();
		ricercaComune.setPolo(false);

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(ricercaComune.isPolo()));
		try {
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			delegate.eseguiRicerca(ricercaComune, mapping);
			RicercaSoggettoResult op = delegate.getOperazione();
			RicercaSoggettoListaVO output = delegate.getOutput();

			switch (op) {
			case analitica_1:// SoggettiDelegate.analitica:

				didCorrente = (ElementoSinteticaDescrittoreVO) output
						.getRisultati().get(0);
				String did = didCorrente.getDid();
				this.ricaricaReticolo(did,
						NavigazioneSemantica.TIPO_OGGETTO_DID, currentForm,
						request);
				currentForm.setEnableIndice(true);
				return mapping.getInputForward();
			case crea_4:// SoggettiDelegate.crea:
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
						mapping.getPath());
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.setErrors(request, errors, null);
				request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
						ricercaComune.clone() );
				request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
						ricercaComune.getCodSoggettario());
				request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
						ricercaComune.getDescSoggettario());

				request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
						ricercaComune.isPolo());
				request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA,
						currentForm.getLivContr());
				request.setAttribute(NavigazioneSemantica.ANALITICA,
						currentForm.getTreeElementViewSoggetti());
				request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
						currentForm.getDataInserimento());
				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
						currentForm.getDataVariazione());
				request.setAttribute(
						NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
						currentForm.isEnableAnaSog());
				request.setAttribute(
						NavigazioneSemantica.DETTAGLIO_DESCRITTORE, currentForm
								.getDettDesGenVO());

				if (currentForm.getOutputDescrittori() != null) {
					request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA,
							currentForm.getOutputDescrittori());
				}

				ActionForward action = new ActionForward();
				action.setName("back");
				action.setPath(currentForm.getAction() + ".do");
				return action;

				// return mapping.getInputForward();
			case sintetica_3:// SoggettiDelegate.sintetica:
				currentForm.setEnableIndice(true);
				if (ricercaComune.getOperazione() instanceof RicercaDescrittoreVO) {
					if (ricercaComune.getRicercaDescrittore().getTestoDescr() != null
							&& ricercaComune.getRicercaDescrittore()
									.getTestoDescr().length() > 0)
						return mapping.findForward("sinteticadescrittori");
					else
						request.setAttribute(NavigazioneSemantica.PAROLE,
								ricercaComune.getRicercaDescrittore());
					return mapping.findForward("sinteticadescrittoriparole");
				} else
					request.setAttribute(
							NavigazioneSemantica.DESCRITTORI_DEL_SOGGETTO,
							ricercaComune.getRicercaSoggetto());
				return mapping.findForward("sinteticadescrittorisoggetto");

			case lista_2:// SoggettiDelegate.lista:
				return mapping.findForward("listasintetica");

			case diagnostico_0:// SoggettiDelegate.diagnostico:
				if (!output.isEsitoNonTrovato()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo",
									output.getTestoEsito()));
					this.setErrors(request, errors, null);
					return mapping.getInputForward();
				}

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			default:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noselezione"));
				return mapping.getInputForward();
				// return mapping.getInputForward();
			}
		} catch (ValidationException e) {
			// errori indice

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}

	}

	public ActionForward dettaglio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());
		currentForm.setEnableConferma(false);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataVariazione());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(currentForm.getRicercaComune().isPolo()));

		request.setAttribute("outputlistadescr", currentForm
				.getOutputDescrittori());
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
				currentForm.getDid());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			if (nodoSelezionato.isSoggetto() ) {
				// ho selezionato un CID
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.codiceNessunaSelezione"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
			} else {
				if (nodoSelezionato.isDescrittore() ) {
					// ho selezionato un DID
					request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO,
							nodoSelezionato.getKey());

					// devo verificare se é automatico per altri soggetti
					SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
					currentForm.setEnableManuale(!delegate.isDescrittoreAutomaticoPerAltriSoggetti(nodoSelezionato.getKey()));

					DettaglioDescrittoreVO dettaglio = nodoSelezionato.getAreaDatiDettaglioOggettiVO()
							.getDettaglioDescrittoreGeneraleVO();

					request.setAttribute(
							NavigazioneSemantica.DATA_MODIFICA_T005,
							nodoSelezionato.getT005());
					request.setAttribute(
							NavigazioneSemantica.DID_RADICE_LEGAMI, currentForm
									.getDidPadre());
					dettaglio.setFormaNome(
									nodoSelezionato.getFormaNome());

					request.setAttribute(NavigazioneSemantica.NOTE_OGGETTO,
							nodoSelezionato.getNote());
					currentForm.setEnableAnaSog(true);
					request.setAttribute(
							NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
							currentForm.isEnableAnaSog());
					DettaglioDescrittoreVO dettDesGenVO = dettaglio;

					resetToken(request);
					String dataVar = SBNMarcUtil
							.converteDataVariazione(nodoSelezionato.getT005());
					request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
							dataVar);
					dettaglio.setDataAgg(
									dataVar);
					String dataIns = dettaglio.getDataIns();
					request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
							dataIns);
					request.setAttribute(
							NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
							dettDesGenVO);
					currentForm.setDettDesGenVO(dettDesGenVO);
					request.setAttribute(
							NavigazioneSemantica.DESCRITTORE_MANUALE,
							currentForm.isEnableManuale());

					if (nodoSelezionato.getDatiLegame() != null) {
						if (!nodoSelezionato.getDatiLegame().equals(
								SbnLegameAut.valueOf("931"))) {
							UserVO utenteCollegato = Navigation.getInstance(
									request).getUtente();
							FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

							AnaliticaSoggettoVO analitica = null;
							analitica = factory.getGestioneSemantica()
									.creaAnaliticaSoggettoPerDid(
											currentForm.getRicercaComune()
													.isPolo(),
											nodoSelezionato.getKey(), 0,
											utenteCollegato.getTicket());
							if (analitica.isEsitoOk()) {

								String dataVar1 = SBNMarcUtil
										.converteDataVariazione(analitica
												.getReticolo().getT005());
								request.setAttribute(
										NavigazioneSemantica.DATA_MODIFICA,
										dataVar1);
								dettaglio
										.setDataAgg(dataVar1);
								request.setAttribute(
										NavigazioneSemantica.DATA_INSERIMENTO,
										analitica.getDataInserimento());
								request
										.setAttribute(
												NavigazioneSemantica.DATA_MODIFICA_T005,
												analitica.getReticolo()
														.getT005());
								dettDesGenVO = analitica.getReticolo()
										.getAreaDatiDettaglioOggettiVO()
										.getDettaglioDescrittoreGeneraleVO();
								currentForm.setDettDesGenVO(dettDesGenVO);
							}
						}
					}
					request.setAttribute(
							NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
							dettDesGenVO);
					return mapping.findForward("esaminaDescrittore");

				} else {
					// Errore da gestire
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					return mapping.getInputForward();
				}
			}
		}

		else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA,
						currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}
	}

	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());
		currentForm.setEnableConferma(false);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataVariazione());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(currentForm.getRicercaComune().isPolo()));
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
				currentForm.getDid());
		request.setAttribute("outputlistadescr", currentForm
				.getOutputDescrittori());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request,
				form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		if (nodoSelezionato.isSoggetto()) {
			// ho selezionato un CID
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA,
						currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}

		// ho selezionato un DID
		request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO, nodoSelezionato.getKey());

		// devo verificare se é automatico per altri soggetti
		SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
		currentForm.setEnableManuale(!delegate.isDescrittoreAutomaticoPerAltriSoggetti(nodoSelezionato.getKey()));

		request.setAttribute(NavigazioneSemantica.DESCRITTORE_MANUALE,
				currentForm.isEnableManuale());
		DettaglioDescrittoreVO dettDesGenVO =
			(DettaglioDescrittoreVO) nodoSelezionato.getDettaglio();

		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_T005,
				nodoSelezionato.getT005());
		request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI,
				nodoSelezionato.getRoot().getKey() );

		currentForm.setEnableAnaSog(true);
		request.setAttribute(NavigazioneSemantica.NOTE_OGGETTO, nodoSelezionato
				.getNote());
		request.setAttribute(NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
				currentForm.isEnableAnaSog());

		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dettDesGenVO.getDataAgg() );
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, dettDesGenVO.getDataIns() );
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_DESCRITTORE, dettDesGenVO);
		currentForm.setDettDesGenVO(dettDesGenVO);
		if (nodoSelezionato.getDatiLegame() != null) {
			if (!nodoSelezionato.getDatiLegame().equals(
					SbnLegameAut.valueOf("931"))) {
				UserVO utenteCollegato = Navigation.getInstance(request)
						.getUtente();
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				AnaliticaSoggettoVO analitica = null;
				analitica = factory.getGestioneSemantica()
						.creaAnaliticaSoggettoPerDid(
								currentForm.getRicercaComune().isPolo(),
								nodoSelezionato.getKey(), 0,
								utenteCollegato.getTicket());
				if (analitica.isEsitoOk()) {

					String dataVar1 = SBNMarcUtil
							.converteDataVariazione(analitica.getReticolo()
									.getT005());
					request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
							dataVar1);
					dettDesGenVO.setDataAgg(dataVar1);
					request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
							analitica.getDataInserimento());
					request.setAttribute(
							NavigazioneSemantica.DATA_MODIFICA_T005, analitica
									.getReticolo().getT005());
					dettDesGenVO = analitica.getReticolo()
							.getAreaDatiDettaglioOggettiVO()
							.getDettaglioDescrittoreGeneraleVO();
					currentForm.setDettDesGenVO(dettDesGenVO);
				}
			}
		}
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
				dettDesGenVO);
		request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE,
				dettDesGenVO.isCondiviso());
		return mapping.findForward("gestioneDescrittore");

	}

	public ActionForward cancellaDescrittore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.setAttribute(GestioneDescrittoreAction.CANCELLA_IMMEDIATE, "xxx");
		return gestione(mapping, form, request, response);
	}



	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataVariazione());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
				.getTreeElementViewSoggetti());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getRicercaComune().getDescSoggettario());
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
				currentForm.getDid());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaComune().clone() );

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		// consento l'inserisci solo per descrittore primo elemento del reticolo
		if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(
					request, form);

			if (nodoSelezionato.isSoggetto()) {
				// ho selezionato un CID
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.codiceNessunaSelezione"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();

			} else if (nodoSelezionato.isDescrittore()) {
				if ((nodoSelezionato.getDatiLegame() != null)
						&& (nodoSelezionato.getDatiLegame().equals(
								SbnLegameAut.valueOf("UF")) || nodoSelezionato
								.getDatiLegame().equals(
										SbnLegameAut.valueOf("USE")))) {
					ActionMessages errors = new ActionMessages();
					errors
							.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"errors.gestioneSemantica.noFunz"));
					this.setErrors(request, errors, null);
					// nessun codice selezionato
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					return mapping.getInputForward();
				}
				// ho selezionato un DID
				request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO,
						nodoSelezionato.getKey());

				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_T005,
						nodoSelezionato.getT005());
				request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI,
						nodoSelezionato.getRoot().getKey());

				DettaglioDescrittoreVO dettDesGenVO =
					(DettaglioDescrittoreVO) nodoSelezionato.getDettaglio();

				if (!SoggettiDelegate.getInstance(request).isLivAutOkDE(
						dettDesGenVO.getLivAut(), true))
					return mapping.getInputForward();

				request.setAttribute(
						NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
						dettDesGenVO);

				request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
						new OggettoRiferimentoVO(true, dettDesGenVO.getDid(),
								dettDesGenVO.getTesto()));

			} else {
				// Errore da gestire
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
			}

		} else if (currentForm.getNodoSelezionato() == null) {
			// nessun codice selezionato
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA,
						currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}

		return mapping.findForward("inserisciDescrLegameDescr");
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

			TreeElementViewSoggetti nodoSelezionato = (TreeElementViewSoggetti) root
					.findElementUnique(Integer.valueOf(currentForm
							.getNodoSelezionato()));

			if (!root.isLivelloPolo() || nodoSelezionato.isSoggetto() ) {
				// ho selezionato un CID
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (root != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA, root);
				return mapping.getInputForward();
			} else {
				if (nodoSelezionato.isDescrittore()) {
					// ho selezionato un DID

					if (nodoSelezionato.getDatiLegame() == null) {
						ActionMessages errors = new ActionMessages();
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(
										"errors.gestioneSemantica.noFunz"));
						this.setErrors(request, errors, null);
						if (root != null)
							request.setAttribute(
									NavigazioneSemantica.ANALITICA, root);
						return mapping.getInputForward();
					} else {
						currentForm.setTipoLegame(nodoSelezionato
								.getDatiLegame().toString());
						ActionMessages errors = new ActionMessages();
						errors
								.add(
										ActionMessages.GLOBAL_MESSAGE,
										new ActionMessage(
												"errors.gestioneSemantica.cancDesLegame"));
						this.setErrors(request, errors, null);

					}

					TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato
							.getParent();

					if (!SoggettiDelegate.getInstance(request).isLivAutOkDE(
							padre.getAreaDatiDettaglioOggettiVO()
									.getDettaglioDescrittoreGeneraleVO()
									.getLivAut(), true))
						return mapping.getInputForward();

					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								root);
					currentForm.setXidConferma(nodoSelezionato);
					currentForm.setEnableCercaIndice(false);
					currentForm.setEnableConferma(true);
					this.preparaConferma(mapping, request);
					return mapping.getInputForward();

				} else {
					// Errore da gestire
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								root);
					return mapping.getInputForward();
				}
			}
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();
		}

	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		// RicercaPerDescrittoreVO didCorrente = null;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());

		TreeElementViewSoggetti xid = currentForm.getXidConferma();
		String tipoLegame = currentForm.getTipoLegame();
		if (xid == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		if (xid.isSoggetto() ) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.setErrors(request, errors, null);
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA,
						currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		} else if (xid.isDescrittore() ) {
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			if (tipoLegame.equals("931")) {
				try {
					RicercaComuneVO ricercaComune = currentForm.getRicercaComune();
					RicercaSoggettoListaVO lista = factory
							.getGestioneSemantica()
							.ricercaSoggettiPerDidCollegato(
									ricercaComune.isPolo(),
									xid.getKey(),
									10, utenteCollegato.getTicket(),
									ricercaComune.getOrdinamentoSoggetto(), null );

					if (!lista.isEsitoOk()) {
						// errori indice
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(
										"errors.gestioneSemantica.incongruo",
										lista.getTestoEsito()));
						if (currentForm.getTreeElementViewSoggetti() != null)
							request.setAttribute(
									NavigazioneSemantica.ANALITICA, currentForm
											.getTreeElementViewSoggetti());
						this.setErrors(request, errors, null);
						// nessun codice selezionato
						return mapping.getInputForward();
					}
					// boolean cancella = (lista.getTotRighe() == 1);
					CreaLegameSoggettoDescrittoreVO legame = new CreaLegameSoggettoDescrittoreVO();
					legame.setCid(currentForm.getTreeElementViewSoggetti()
							.getKey());
					legame.setCodSoggettario(ricercaComune
							.getCodSoggettario());
					legame.setLivelloAutorita(currentForm
							.getTreeElementViewSoggetti().getLivelloAutorita());
					legame.setT005(currentForm.getTreeElementViewSoggetti()
							.getT005());
					legame.setDid(xid.getKey());
					legame.setPolo(ricercaComune.isPolo());
					legame.setCondiviso(currentForm
							.getTreeElementViewSoggetti()
							.getAreaDatiDettaglioOggettiVO()
							.getDettaglioDescrittoreGeneraleVO().isCondiviso());
					CreaVariaDescrittoreVO descrittore = factory
							.getGestioneSemantica()
							.cancellaLegameSoggettoDescrittore(legame,
									utenteCollegato.getTicket());
					if (!descrittore.isEsitoOk()) {
						// errori indice

						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(
										"errors.gestioneSemantica.incongruo",
										descrittore.getTestoEsito()));
						this.setErrors(request, errors, null);
						if (currentForm.getTreeElementViewSoggetti() != null)
							request.setAttribute(
									NavigazioneSemantica.ANALITICA, currentForm
											.getTreeElementViewSoggetti());
						// nessun codice selezionato
						return mapping.getInputForward();
					}
					// try {
					// if (cancella) {
					//
					// AnaliticaSoggettoVO analitica = null;
					// analitica = factory.getGestioneSemantica()
					// .creaAnaliticaSoggettoPerDid(
					// ((AnaliticaDescrittoreForm) form)
					// .getRicercaComune()
					// .isPolo(), xid, 0,
					// utenteCollegato.getTicket());
					//
					// if (analitica.isEsitoOk() ) {
					// ((AnaliticaDescrittoreForm) form)
					// .setTreeElementViewSoggetti(analitica
					// .getReticolo());
					// }
					//
					// if (!analitica.getReticolo().hasChildren()) {
					// factory.getGestioneSemantica()
					// .cancellaSoggettoDescrittore(
					// currentForm.getRicercaComune()
					// .isPolo(), xid,
					// utenteCollegato.getTicket());
					// }
					// }
					// } catch (DataException e) {
					// ActionMessages errors = new ActionMessages();
					// errors.add(ActionMessages.GLOBAL_MESSAGE, new
					// ActionMessage(
					// "errors.gestioneSemantica.incongruo", e
					// .getMessage()));
					// this.setErrors(request, errors);
					// if (currentForm.getTreeElementViewSoggetti() != null)
					// request.setAttribute(NavigazioneSemantica.ANALITICA,
					// ricSogg
					// .getTreeElementViewSoggetti());
					// log.error("", e);
					// return mapping.getInputForward();
					// } catch (InfrastructureException e) {
					// ActionMessages errors = new ActionMessages();
					// errors.add(ActionMessages.GLOBAL_MESSAGE, new
					// ActionMessage(
					// "errors.gestioneSemantica.incongruo", e
					// .getMessage()));
					// this.setErrors(request, errors);
					// if (currentForm.getTreeElementViewSoggetti() != null)
					// request.setAttribute(NavigazioneSemantica.ANALITICA,
					// ricSogg
					// .getTreeElementViewSoggetti());
					// log.error("", e);
					// return mapping.getInputForward();
					// }

					errors
							.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"errors.gestioneSemantica.operOk"));
					this.setErrors(request, errors, null);
					currentForm.setEnableConferma(false);
					currentForm.setEnableCercaIndice(false);
					this.ricaricaReticolo(currentForm.getDid(),
							NavigazioneSemantica.TIPO_OGGETTO_DID, currentForm,
							request);
					return mapping.getInputForward();

				} catch (ValidationException e) {
					// errori indice

					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo", e
											.getMessage()));
					this.setErrors(request, errors, e);
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					log.error("", e);
					// nessun codice selezionato
					return mapping.getInputForward();

				} catch (DataException e) {

					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo", e
											.getMessage()));
					this.setErrors(request, errors, e);
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					log.error("", e);
					return mapping.getInputForward();
				} catch (InfrastructureException e) {

					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo", e
											.getMessage()));
					this.setErrors(request, errors, e);
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					log.error("", e);
					return mapping.getInputForward();
				} catch (Exception e) {

					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.erroreSistema", e
											.getMessage()));
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					this.setErrors(request, errors, e);
					log.error("", e);
					return mapping.getInputForward();
				}

			} else { // non è 931 più esterno
				TreeElementViewSoggetti nodoSelezionato = (TreeElementViewSoggetti) currentForm
						.getTreeElementViewSoggetti().findElementUnique(
								Integer.valueOf(currentForm
										.getNodoSelezionato()));

				if (xid.isSoggetto()) {

					errors
							.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"errors.gestioneSemantica.noFunz"));
					this.setErrors(request, errors, null);
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					return mapping.getInputForward();
				} else if (xid.isDescrittore()) {

					if (nodoSelezionato.getDatiLegame().equals(
							SbnLegameAut.valueOf("931"))) {

						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(
										"errors.gestioneSemantica.noFunz"));
						this.setErrors(request, errors, null);
						if (currentForm.getTreeElementViewSoggetti() != null)
							request.setAttribute(
									NavigazioneSemantica.ANALITICA, currentForm
											.getTreeElementViewSoggetti());
						return mapping.getInputForward();
					} else {
						TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato
								.getParent();
						if (padre == null) {

							errors
									.add(
											ActionMessages.GLOBAL_MESSAGE,
											new ActionMessage(
													"errors.gestioneSemantica.noTasto"));
							this.setErrors(request, errors, null);
							return mapping.getInputForward();
						}
						try {
							DatiLegameDescrittoreVO legame = new DatiLegameDescrittoreVO();
							legame.setDidPartenza(padre.getKey());
							legame
									.setDidPartenzaFormaNome(padre
											.getFormaNome());
							request.setAttribute(
									NavigazioneSemantica.DATA_MODIFICA_T005,
									padre.getT005());
							request.setAttribute(
									NavigazioneSemantica.DID_RADICE_LEGAMI,
									currentForm.getDidPadre());
							legame.setT005(padre.getT005());
							legame.setDidPartenzaLivelloAut(padre
									.getLivelloAutorita());
							legame.setDidArrivo(xid.getKey());
							legame.setDidArrivoFormaNome(nodoSelezionato
									.getFormaNome());
							legame.setTipoLegame(nodoSelezionato
									.getDatiLegame().toString());
							legame.setNotaLegame("");
							legame.setLivelloPolo(true);
							legame.setCondiviso(padre
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioDescrittoreGeneraleVO()
									.isCondiviso());

							CreaVariaDescrittoreVO descrittore = factory
									.getGestioneSemantica()
									.cancellaLegameDescrittori(legame,
											utenteCollegato.getTicket());
							if (!descrittore.isEsitoOk()) {
								// errori indice

								errors
										.add(
												ActionMessages.GLOBAL_MESSAGE,
												new ActionMessage(
														"errors.gestioneSemantica.incongruo",
														descrittore
																.getTestoEsito()));
								this.setErrors(request, errors, null);
								if (currentForm.getTreeElementViewSoggetti() != null)
									request
											.setAttribute(
													NavigazioneSemantica.ANALITICA,
													currentForm
															.getTreeElementViewSoggetti());
								// nessun codice selezionato
								return mapping.getInputForward();
							}
							// per cancellare il did selezionato leggo la sua
							// analitica
							// per verificare se ha legami con altri descrittori
							// oltre a
							// quello selezionato
							// RicercaSoggettoListaVO lista = null;
							// try {
							// lista = factory
							// .getGestioneSemantica()
							// .ricercaSoggettiPerDidCollegato(
							// currentForm.getRicercaComune()
							// .isPolo(),
							// padre.getKey(), 10,
							// utenteCollegato.getTicket());
							//
							// if (lista.isEsitoOk() ) {
							// boolean cancella = (lista.getTotRighe() == 0);
							// if (cancella) {
							// AnaliticaSoggettoVO analitica = null;
							// analitica = factory
							// .getGestioneSemantica()
							// .creaAnaliticaSoggettoPerDid(
							// ((AnaliticaDescrittoreForm) form)
							// .getRicercaComune()
							// .isPolo(),
							// xid,
							// 0,
							// utenteCollegato
							// .getTicket());
							//
							// if (analitica.isEsitoOk() ) {
							//
							// if (!analitica.getReticolo()
							// .hasChildren()) {
							// factory
							// .getGestioneSemantica()
							// .cancellaSoggettoDescrittore(
							// ricSogg
							// .getRicercaComune()
							// .isPolo(),
							// xid,
							// utenteCollegato
							// .getTicket());
							// }
							// }
							// }
							// }
							// } catch (DataException e) {
							// ActionMessages errors = new ActionMessages();
							// errors.add(ActionMessages.GLOBAL_MESSAGE, new
							// ActionMessage(
							// "errors.gestioneSemantica.incongruo", e
							// .getMessage()));
							// this.setErrors(request, errors);
							// if (currentForm.getTreeElementViewSoggetti() !=
							// null)
							// request.setAttribute(NavigazioneSemantica.ANALITICA,
							// ricSogg
							// .getTreeElementViewSoggetti());
							// log.error("", e);
							// return mapping.getInputForward();
							// } catch (InfrastructureException e) {
							// ActionMessages errors = new ActionMessages();
							// errors.add(ActionMessages.GLOBAL_MESSAGE, new
							// ActionMessage(
							// "errors.gestioneSemantica.incongruo", e
							// .getMessage()));
							// this.setErrors(request, errors);
							// if (currentForm.getTreeElementViewSoggetti() !=
							// null)
							// request.setAttribute(NavigazioneSemantica.ANALITICA,
							// ricSogg
							// .getTreeElementViewSoggetti());
							// log.error("", e);
							// return mapping.getInputForward();
							// }
							// // per cancellare il didpartenza leggo la sua
							// // analitica
							// // per verificare se ha legami con altri
							// descrittori
							// // oltre a
							// // quello selezionato
							// try {
							// if (lista.isEsitoOk() ) {
							// boolean cancella = (lista.getTotRighe() == 0);
							// if (cancella) {
							//
							// AnaliticaSoggettoVO analitica1 = null;
							//
							// analitica1 = factory
							// .getGestioneSemantica()
							// .creaAnaliticaSoggettoPerDid(
							// ((AnaliticaDescrittoreForm) form)
							// .getRicercaComune()
							// .isPolo(),
							// padre.getKey(),
							// 0,
							// utenteCollegato
							// .getTicket());
							//
							// if (analitica1.getEsito()
							// .equals("0000")) {
							// ((AnaliticaDescrittoreForm) form)
							// .setTreeElementViewSoggetti(analitica1
							// .getReticolo());
							// }
							//
							// if (!analitica1.getReticolo()
							// .hasChildren()) {
							// factory
							// .getGestioneSemantica()
							// .cancellaSoggettoDescrittore(
							// ricSogg
							// .getRicercaComune()
							// .isPolo(),
							// padre.getKey(),
							// utenteCollegato
							// .getTicket());
							// }
							// }
							// }
							// } catch (DataException e) {
							// ActionMessages errors = new ActionMessages();
							// errors.add(ActionMessages.GLOBAL_MESSAGE, new
							// ActionMessage(
							// "errors.gestioneSemantica.incongruo", e
							// .getMessage()));
							// this.setErrors(request, errors);
							// if (currentForm.getTreeElementViewSoggetti() !=
							// null)
							// request.setAttribute(NavigazioneSemantica.ANALITICA,
							// ricSogg
							// .getTreeElementViewSoggetti());
							// log.error("", e);
							// return mapping.getInputForward();
							// } catch (InfrastructureException e) {
							// ActionMessages errors = new ActionMessages();
							// errors.add(ActionMessages.GLOBAL_MESSAGE, new
							// ActionMessage(
							// "errors.gestioneSemantica.incongruo", e
							// .getMessage()));
							// this.setErrors(request, errors);
							// if (currentForm.getTreeElementViewSoggetti() !=
							// null)
							// request.setAttribute(NavigazioneSemantica.ANALITICA,
							// ricSogg
							// .getTreeElementViewSoggetti());
							// log.error("", e);
							// return mapping.getInputForward();
							// }
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"errors.gestioneSemantica.operOk"));
							this.setErrors(request, errors, null);
							currentForm.setEnableConferma(false);
							currentForm.setEnableCercaIndice(false);
							this.ricaricaReticolo(
									currentForm.getTreeElementViewSoggetti()
											.getKey(),// currentForm.getDidPadre(),
									NavigazioneSemantica.TIPO_OGGETTO_DID,
									currentForm, request);
							return mapping.getInputForward();

						} catch (ValidationException e) {
							// errori indice

							errors
									.add(
											ActionMessages.GLOBAL_MESSAGE,
											new ActionMessage(
													"errors.gestioneSemantica.incongruo",
													e.getMessage()));
							this.setErrors(request, errors, e);
							if (currentForm.getTreeElementViewSoggetti() != null)
								request.setAttribute(
										NavigazioneSemantica.ANALITICA,
										currentForm
												.getTreeElementViewSoggetti());
							log.error("", e);
							// nessun codice selezionato
							return mapping.getInputForward();

						} catch (DataException e) {

							errors
									.add(
											ActionMessages.GLOBAL_MESSAGE,
											new ActionMessage(
													"errors.gestioneSemantica.incongruo",
													e.getMessage()));
							this.setErrors(request, errors, e);
							if (currentForm.getTreeElementViewSoggetti() != null)
								request.setAttribute(
										NavigazioneSemantica.ANALITICA,
										currentForm
												.getTreeElementViewSoggetti());
							log.error("", e);
							return mapping.getInputForward();
						} catch (InfrastructureException e) {

							errors
									.add(
											ActionMessages.GLOBAL_MESSAGE,
											new ActionMessage(
													"errors.gestioneSemantica.incongruo",
													e.getMessage()));
							this.setErrors(request, errors, e);
							if (currentForm.getTreeElementViewSoggetti() != null)
								request.setAttribute(
										NavigazioneSemantica.ANALITICA,
										currentForm
												.getTreeElementViewSoggetti());
							log.error("", e);
							return mapping.getInputForward();
						} catch (Exception e) {

							errors
									.add(
											ActionMessages.GLOBAL_MESSAGE,
											new ActionMessage(
													"errors.gestioneSemantica.erroreSistema",
													e.getMessage()));
							if (currentForm.getTreeElementViewSoggetti() != null)
								request.setAttribute(
										NavigazioneSemantica.ANALITICA,
										currentForm
												.getTreeElementViewSoggetti());
							this.setErrors(request, errors, e);
							log.error("", e);
							return mapping.getInputForward();
						}

					}

				} else { // else più interno non è C e non è D
					// messaggio di errore.

					errors
							.add(
									ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"errors.gestioneSemantica.codiceNessunaSelezione"));
					this.setErrors(request, errors, null);
					// nessun codice selezionato
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					return mapping.getInputForward();
				}
			}

		} else { // else più esterno non è C e non è D
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA,
						currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());

		currentForm.setEnableConferma(false);
		currentForm.setEnableIndice(false);
		currentForm.setEnableCercaIndice(false);
		currentForm.setEnableModifica(true);
		currentForm.setEnableInserisci(true);
		currentForm.setEnableElimina(true);
		currentForm.setEnableEsamina(true);
		currentForm.setEnableGestione(true);
		currentForm.setEnableStampa(true);
		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);

	}

	public ActionForward soggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		try {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(
					request, form);
			DettaglioDescrittoreVO dettaglio = nodoSelezionato
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioDescrittoreGeneraleVO();

			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			RicercaComuneVO ricercaComune = currentForm.getRicercaComune()
					.copy();
			// DEVO GESTIRE L'ACCESSO DIVERSIFICATO PER LIVELLO DI RICERCA
			// IMPOSTATO IN INPUT
			RicercaSoggettoDescrittoriVO ricDid = new RicercaSoggettoDescrittoriVO();
			ricDid.setDid(dettaglio.getDid());
			ricercaComune.setRicercaSoggetto(ricDid);
			ricercaComune.setRicercaDescrittore(null);

			delegate.eseguiRicerca(ricercaComune, mapping);
			if (!delegate.getOutput().isEsitoOk()) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", delegate
								.getOutput().getTestoEsito()));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();

			}

			request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
					new OggettoRiferimentoVO(true, dettaglio.getDid(),
							dettaglio.getTesto()));

		} catch (Exception ex) {
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.NoServer"));
			return mapping.getInputForward();

		}
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm
				.getTreeElementViewSoggetti().getLivelloAutorita());
		request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
				.getTreeElementViewSoggetti());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataVariazione());
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
				currentForm.getDidPadre());
		request.setAttribute("ricercalista", currentForm.getRicercaComune());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getRicercaComune().getDescSoggettario());
		currentForm.setEnableCercaIndice(false);
		request.setAttribute(NavigazioneSemantica.ABILITA_RICERCA_INDICE,
				currentForm.isEnableCercaIndice());

		return mapping.findForward("listasintetica");
	}

	public ActionForward utilizzati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		request.setAttribute("bibDiRicerca", currentForm.getBiblioteca());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		try {
			SoggettiDelegate ricerca = SoggettiDelegate.getInstance(request);
			RicercaComuneVO ricercaComune = new RicercaComuneVO();
			ricercaComune.setCodSoggettario(currentForm.getRicercaComune()
					.getCodSoggettario());
			ricercaComune.setDescSoggettario(currentForm.getRicercaComune()
					.getDescSoggettario());
			ricercaComune.setPolo(currentForm.getRicercaComune().isPolo());
			// DEVO GESTIRE L'ACCESSO DIVERSIFICATO PER LIVELLO DI RICERCA
			// IMPOSTATO IN INPUT

			RicercaSoggettoDescrittoriVO ricDid = new RicercaSoggettoDescrittoriVO();
			ricDid.setDid(currentForm.getDid());
			ricercaComune.setRicercaSoggetto(ricDid);
			ricercaComune.setRicercaDescrittore(null);
			ricercaComune.setElemBlocco("10");
			ricerca.eseguiRicerca(ricercaComune, mapping);
		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.NoServer"));
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm
				.getLivContr());
		request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
				.getTreeElementViewSoggetti());
		request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
				currentForm.getDidPadre());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataVariazione());
		return mapping.findForward("listasintetica");

	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, null);
			request.setAttribute(NavigazioneSemantica.TESTO_SOGGETTO, root.getText());
			request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, null);
			if (!root.isLivelloPolo() || nodoSelezionato.isSoggetto() ) {
				// ho selezionato un CID anziche' un DID
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				if (root != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA, root);
				return mapping.getInputForward();
			} else {
				if (nodoSelezionato.isDescrittore() ) {
					if (nodoSelezionato.getDatiLegame() == null) {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(
										"errors.gestioneSemantica.noFunz"));
						this.setErrors(request, errors, null);
						if (root != null)
							request.setAttribute(
									NavigazioneSemantica.ANALITICA, root);
						return mapping.getInputForward();
					}

					TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato.getParent();
					if (padre == null) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noTasto"));
						return mapping.getInputForward();
					}

					DettaglioDescrittoreVO dettaglio =
						(DettaglioDescrittoreVO) nodoSelezionato.getDettaglio();
					DettaglioDescrittoreVO dettaglioPadre =
						(DettaglioDescrittoreVO) nodoSelezionato.getDettaglioPadre();

					if (!SoggettiDelegate.getInstance(request).isLivAutOkDE(dettaglioPadre.getLivAut(), true))
						return mapping.getInputForward();

					RicercaComuneVO ricercaComune = currentForm.getRicercaComune().copy();
					ricercaComune.setCodSoggettario(dettaglio.getCampoSoggettario() );
					request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
							ricercaComune.getCodSoggettario());
					request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
							.getPath());

					request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
							new Boolean(ricercaComune.isPolo()));
					request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
							.getDataInserimento());
					request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
							.getDataVariazione());
					request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
							ricercaComune.getDescSoggettario());

					request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME, dettaglioPadre.getDid());
					request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_TESTO, dettaglioPadre.getTesto());
					request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_FORMA_NOME, dettaglioPadre.getFormaNome());
					request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_LIVELLO_AUTORITA, dettaglioPadre.getLivAut());
					request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_T005, dettaglioPadre.getT005());
					request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE, dettaglioPadre.isCondiviso());

					request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI, currentForm.getDidPadre());

					request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME, dettaglio.getDid() );
					request.setAttribute(NavigazioneSemantica.TIPO_LEGAME, nodoSelezionato.getDatiLegame().toString());
					request.setAttribute(NavigazioneSemantica.NOTE_AL_LEGAME, nodoSelezionato.getNotaLegame());
					request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_TESTO, dettaglio.getTesto());
					request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_FORMA_NOME, dettaglio.getFormaNome());
					request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_LIVELLO_AUTORITA, dettaglio.getLivAut());
					request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, ricercaComune);

					//almaviva5_20120507 evolutive CFI
					ParametriSoggetti parametri = currentForm.getParametriSogg().copy();
					parametri.put(SoggettiParamType.DETTAGLIO_ID_PARTENZA, dettaglioPadre);
					parametri.put(SoggettiParamType.DETTAGLIO_ID_ARRIVO, dettaglio);
					ParametriSoggetti.send(request, parametri);

					return mapping.findForward("modificaLegameTraDescr");

				} else {
					// Errore da gestire
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								root);
					return mapping.getInputForward();
				}
			}
		} else {
			// messaggio di errore.

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();
		}
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.setErrors(request, errors, null);
		// nessun codice selezionato
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

	public ActionForward elementoPrecedente(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		String[] listaDidSelez = currentForm.getListaDidSelez();
		int pos = currentForm.getPosizioneCorrente();
		if (pos == 0) {
			request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
					.getTreeElementViewSoggetti());
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.fineScorrimento"));
			this.setErrors(request, errors, null);
			resetToken(request);
			return mapping.getInputForward();
		}
		currentForm.setPosizioneCorrente(--pos);
		String xid = listaDidSelez[pos];

		this.ricaricaReticolo(xid, NavigazioneSemantica.TIPO_OGGETTO_DID, currentForm, request);
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();

		DettaglioDescrittoreVO dettaglioDes = (DettaglioDescrittoreVO) root.getDettaglio();
		String codice = dettaglioDes.getCampoSoggettario();
		currentForm.getRicercaComune().setCodSoggettario(codice);
		// INSERISCO RICHIESTA NUMERO TOTALE SOGGETTI COLLEGATI AL DID

		currentForm.setNumSoggetti(dettaglioDes.getSoggettiCollegati());
		currentForm.setNumUtilizzati(""	+ dettaglioDes.getSoggettiUtilizzati());
		return mapping.getInputForward();
	}

	public ActionForward elementoSuccessivo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;

		String[] listaDidSelez = currentForm.getListaDidSelez();
		int pos = currentForm.getPosizioneCorrente();

		if (pos == listaDidSelez.length - 1) {
			request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
					.getTreeElementViewSoggetti());
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.fineScorrimento"));
			this.setErrors(request, errors, null);
			resetToken(request);
			return mapping.getInputForward();
		}
		currentForm.setPosizioneCorrente(++pos);
		String xid = listaDidSelez[pos];

		this.ricaricaReticolo(xid, NavigazioneSemantica.TIPO_OGGETTO_DID,
				currentForm, request);
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		DettaglioDescrittoreVO dettaglioDes = (DettaglioDescrittoreVO) root.getDettaglio();
		String codice = dettaglioDes.getCampoSoggettario();
		currentForm.getRicercaComune().setCodSoggettario(codice);
		// INSERISCO RICHIESTA NUMERO TOTALE SOGGETTI COLLEGATI AL DID

		currentForm.setNumSoggetti(dettaglioDes.getSoggettiCollegati());
		currentForm.setNumUtilizzati(""	+ dettaglioDes.getSoggettiUtilizzati());

		return mapping.getInputForward();
	}

	public ActionForward scambia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		ActionMessages errors = new ActionMessages();
		this.saveToken(request);
		try {
			if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

				TreeElementViewSoggetti nodoSelezionato = (TreeElementViewSoggetti) currentForm
						.getTreeElementViewSoggetti().findElementUnique(
								Integer.valueOf(currentForm
										.getNodoSelezionato()));

				// vale solo per i descrittore in forma di rinvio
				if (!nodoSelezionato.isDescrittore() || !nodoSelezionato.isRinvio()) {
					errors
							.add(
									ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"errors.gestioneSemantica.selezionaRinvio"));
					this.setErrors(request, errors, null);
					return mapping.getInputForward();
				}

				UserVO utenteCollegato = Navigation.getInstance(request)
						.getUtente();
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				DettaglioDescrittoreVO dettaglioRinvio = nodoSelezionato
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioDescrittoreGeneraleVO();

				TreeElementViewSoggetti padre = null;
				// se ho richiesto l'analitica dal did di rinvio devo invertire
				// il tutto:
				// dovrei avere come figlio un solo descrittore in forma
				// accettata
				if (nodoSelezionato.isRoot())
					padre = (TreeElementViewSoggetti) nodoSelezionato
							.getChildren().get(0);
				else
					padre = (TreeElementViewSoggetti) nodoSelezionato
							.getParent();

				DettaglioDescrittoreVO dettaglioPadre = padre
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioDescrittoreGeneraleVO();

				if (!SoggettiDelegate.getInstance(request).isLivAutOkDE(
						dettaglioPadre.getLivAut(), true))
					return mapping.getInputForward();

				DatiLegameDescrittoreVO scambia = new DatiLegameDescrittoreVO();
				scambia.setCodiceSoggettario(dettaglioPadre
						.getCampoSoggettario());
				scambia.setDidPartenza(dettaglioPadre.getDid());
				scambia.setDidPartenzaFormaNome(dettaglioPadre.getFormaNome());
				scambia.setDidPartenzaLivelloAut(dettaglioPadre.getLivAut());
				scambia.setCondiviso(dettaglioPadre.isCondiviso());
				scambia.setT005(dettaglioPadre.getT005());
				scambia.setDidArrivo(dettaglioRinvio.getDid());
				scambia.setDidArrivoFormaNome(dettaglioRinvio.getFormaNome());
				scambia.setTipoLegame("UF");
				scambia.setLivelloPolo(padre.isLivelloPolo());

				CreaVariaDescrittoreVO scambioFormaDescrittori = factory
						.getGestioneSemantica().scambioFormaDescrittori(
								scambia, utenteCollegato.getTicket());

				if (scambioFormaDescrittori.isEsitoOk()) {

					this.ricaricaReticolo(currentForm
							.getTreeElementViewSoggetti().getKey(),
							NavigazioneSemantica.TIPO_OGGETTO_DID, currentForm,
							request);
					errors
							.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"errors.gestioneSemantica.operOk"));
					this.setErrors(request, errors, null);
				} else {

					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo",
									scambioFormaDescrittori.getTestoEsito()));
					this.setErrors(request, errors, null);
				}
			} else {
				// messaggio di errore.
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.codiceNessunaSelezione"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
			}

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);

		} catch (DataException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
		} catch (InfrastructureException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
		}
		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzione();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA,
						currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();

		}

		if (!checkAttivita(request, form, idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"error.authentication.non_abilitato"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	private enum TipoAttivita {
		SCAMBIA, MODIFICA, LEGAME,

		MODIFICA_DESCRITTORE, CANCELLA_DESCRITTORE, INS_LEGAME_DES_DES, MOD_LEGAME_DES, CANCELLA_LEGAME_DES, SCAMBIA_FORMA;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			AnaliticaDescrittoreForm currentForm = (AnaliticaDescrittoreForm) form;
			TreeElementViewSoggetti root = currentForm
					.getTreeElementViewSoggetti();
			DettaglioDescrittoreVO dettaglioRoot = (DettaglioDescrittoreVO) root.getDettaglio();
			String codSoggettario = dettaglioRoot.getCampoSoggettario();
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);
			CodiciAttivita codiciAttivita = CodiciAttivita.getIstance();

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
			DettaglioDescrittoreVO dettaglioDES =
				(DettaglioDescrittoreVO) nodoSelezionato.getDettaglio();

			if (!delegate.isSoggettarioGestito(codSoggettario))
				return false;

			switch (attivita) { // PULSANTI

			case SCAMBIA:
				return delegate
						.isAbilitatoDE(codiciAttivita.SCAMBIO_FORMA_1029);
			case LEGAME:
			case MODIFICA:
				return delegate
						.isAbilitatoDE(codiciAttivita.MODIFICA_SOGGETTO_1265);
			}

			switch (attivita) { // DESCRITTORI

			case MODIFICA_DESCRITTORE:
				if (!delegate.isLivAutOkDE(dettaglioDES.getLivAut(), false))
					return false;
				//almaviva5_20100614 #3799
				//if (nodoSelezionato.hasChildren()) return false;
				return delegate
						.isAbilitatoDE(codiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);

			case CANCELLA_DESCRITTORE:
				if (!delegate.isLivAutOkDE(dettaglioDES.getLivAut(), false))
					return false;
				if (nodoSelezionato.hasChildren())
					return false;
				if (dettaglioDES.getSoggettiCollegati() > 0)
					return false;
				// controllo per cancellazione legame des-des
				if (!nodoSelezionato.isRoot() )
					if (!checkAttivita(request, form, "CANCELLA_LEGAME_DES"))
						return false;
				return delegate
						.isAbilitatoDE(codiciAttivita.CANCELLA_ELEMENTO_AUTHORITY_1028);

			case INS_LEGAME_DES_DES:
				if (!delegate.isLivAutOkDE(dettaglioDES.getLivAut(), false))
					return false;
				if (nodoSelezionato.isRinvio() && nodoSelezionato.hasChildren())
					return false;
				if (nodoSelezionato.isRinvio() && !nodoSelezionato.isRoot())
					return false;
				return delegate
						.isAbilitatoDE(codiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);

			case MOD_LEGAME_DES:
			case CANCELLA_LEGAME_DES:
				if (nodoSelezionato.isRoot())
					return false;
				DettaglioDescrittoreVO dettaglioPadre =
					(DettaglioDescrittoreVO) nodoSelezionato.getDettaglioPadre();
				if (!delegate.isLivAutOkDE(dettaglioPadre.getLivAut(), false))
					return false;
				return delegate
						.isAbilitatoDE(codiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);

			case SCAMBIA_FORMA:
				if (!nodoSelezionato.isRinvio())
					return false;
				return delegate
						.isAbilitatoDE(codiciAttivita.SCAMBIO_FORMA_1029);
			}

			return false;
		} catch (Exception e) {
			log.error("", e);
			Navigation.getInstance(request).setExceptionLog(e);
			return false;
		}

	}

}
