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
package it.iccu.sbn.web.actions.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOperazioneLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CreaVariaTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaAnaliticaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTerminiType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaDescrittoreVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.AnaliticaThesauroForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class AnaliticaThesauroAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static String[] BOTTONIERA_CONFERMA = new String[] { "button.si",
		"button.no" };

	private void setErrors(HttpServletRequest request, ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.inserisci", "inserisci");
		map.put("button.esamina", "dettaglio");
		map.put("button.gestione", "gestione");
		map.put("button.elimina", "elimina");
		map.put("button.modifica", "modifica");
		map.put("button.stampa", "stampa");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.biblio", "biblio");
		map.put("button.polo", "polo");
		map.put("button.annulla", "annulla");
		map.put("button.scambia", "scambia");

		// tasti per l'avanzamento su elemento precedente/successivo
		map.put("button.elemPrec", "elementoPrecedente");
		map.put("button.elemSucc", "elementoSuccessivo");

		map.put("button.scambia", "scambia");
		map.put("button.crea", "crea");
		map.put("button.conferma", "conferma");

		map.put("button.conferma", "conferma");
		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");

		return map;
	}

	private TreeElementViewSoggetti getNodoSelezionato(
			HttpServletRequest request, ActionForm form) {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		ActionMessages errors = new ActionMessages();
		String codSelezionato = currentForm.getNodoSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			return null;
		}

		try {
			return (TreeElementViewSoggetti) currentForm.getReticolo()
					.findElementUnique(Integer.valueOf(codSelezionato));
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, e);
			return null;
		}
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form) {

		try {
			AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
			currentForm.setListaThesauri(CaricamentoComboSemantica.loadComboThesauro(Navigation.getInstance(request).getUserTicket(), false));
			currentForm.setListaLivelloAutorita(CaricamentoComboSemantica.loadComboStato(null));

			return true;

		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.setErrors(request, errors, ex);
			return false;
		}
	}

	private ActionForward init(ActionMapping mapping, HttpServletRequest request, ActionForm form) {

		initCombo(request, form);
		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		currentForm.setParametri(parametri);
		ThRicercaComuneVO parametriRicerca = (ThRicercaComuneVO) parametri.get(ParamType.PARAMETRI_RICERCA);
		currentForm.setRicercaComune(parametriRicerca);
		currentForm.setListaPulsanti(BOTTONIERA_CONFERMA);

		currentForm.setInitialized(true);
		return mapping.getInputForward();
	}

	private ActionForward reticolo_URL_PARAM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String urlParam = request.getParameter(TreeElementView.TREEVIEW_URL_PARAM);
		if (urlParam != null) {
			AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
			currentForm.setNodoSelezionato(urlParam);
			return dettaglio(mapping, form,  request, response);
		}
		return null;
	}

	private boolean reticolo_KEY_PARAM(ActionForm form,
			HttpServletRequest request) throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;

		String parameter = request.getParameter(TreeElementView.TREEVIEW_KEY_PARAM);
		if (parameter == null)
			return false;

		TreeElementViewSoggetti root = currentForm.getReticolo();
		TreeElementViewSoggetti termine = (TreeElementViewSoggetti) root
				.findElementUnique(Integer.valueOf(parameter));

		Navigation.getInstance(request).getCache().getCurrentElement()
				.setAnchorId(LinkableTagUtils.ANCHOR_PREFIX + parameter);

		// nodo aperto
		if (termine.isOpened()) {
			termine.close();
			return true;
		}

		if (!termine.isExplored()) {

			// nodo chiuso
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			AnaliticaThesauroVO analitica = delegate.caricaSottoReticolo(true,
					termine.getKey(), root.findMaxElement());

			if (!analitica.isEsitoOk()) {
				// errore
			}

			termine.adoptChildren(analitica.getReticolo());
			termine.setRadioVisible(true);
			termine.open();

		} else {
			termine.setRadioVisible(true);
			termine.open();
		}
		currentForm.setNodoSelezionato(parameter);
		return true;
	}


	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		if (!currentForm.isInitialized())
			init(mapping, request, form);

		// check navigazione
		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		// check open/close nodo reticolo
		if (reticolo_KEY_PARAM(form, request)) {
			updateCombo(request, form);
			return mapping.getInputForward();
		}

		// check key url su reticolo
		ActionForward forward = reticolo_URL_PARAM(mapping, form, request, response);
		if (forward != null)
			return forward;

		return super.execute(mapping, form, request, response);
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		ParametriThesauro parametri = ParametriThesauro.retrieve(request);
		if (parametri != null)
			currentForm.setParametri(parametri);
		else
			parametri = currentForm.getParametri();

		TreeElementViewSoggetti root = (TreeElementViewSoggetti) parametri.get(ParamType.ANALITICA);
		currentForm.setReticolo(root);

		Integer nodoSelezionato = (Integer) parametri.get(ParamType.ID_NODO_SELEZIONATO);
		if (nodoSelezionato == null) {
			if (ValidazioneDati.strIsNull(currentForm.getNodoSelezionato() ))
				nodoSelezionato = root.getRepeatableId();
			else
				nodoSelezionato = Integer.valueOf(currentForm.getNodoSelezionato() );
		} else
			parametri.remove(ParamType.ID_NODO_SELEZIONATO);

		currentForm.setNodoSelezionato(String.valueOf(nodoSelezionato));
		updateCombo(request, form);

		DettaglioTermineThesauroVO dettaglio =
			(DettaglioTermineThesauroVO) root.getDettaglio();
		currentForm.setCodThesauro(dettaglio.getCodThesauro());
		currentForm.setLivelloAutorita(dettaglio.getLivAut());
		currentForm.setDataInserimento(dettaglio.getDataIns());
		currentForm.setDataVariazione(dettaglio.getDataAgg());

		currentForm.setListaDidSelez((String[]) parametri
				.get(ParamType.LISTA_DID_SELEZIONATI));

		return mapping.getInputForward();
	}

	private void updateCombo(HttpServletRequest request, ActionForm form) {
		// impostazione combo gestione
		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		currentForm.setComboGestione(LabelGestioneSemantica
				.getComboGestioneSematica(servlet.getServletContext(),
						request, form, getNodoSelezionato(request, form), this));
		currentForm.setIdFunzione("");

		currentForm.setComboGestioneEsamina(LabelGestioneSemantica
				.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
						request, form, getNodoSelezionato(request, form), this));
		currentForm.setIdFunzioneEsamina("");
	}


	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		if (!delegate.isLivAutOk(nodoSelezionato.getDettaglio().getLivAut(), true))
			return mapping.getInputForward();

		ParametriThesauro parametri = (ParametriThesauro) currentForm.getParametri().copy();

		//imposto i dati di partenza del legame
		DatiLegameTerminiVO datiLegame = new DatiLegameTerminiVO();
		datiLegame.setDid1((DettaglioTermineThesauroVO) nodoSelezionato.getDettaglio() );
		parametri.put(ParamType.DATI_LEGAME_TERMINI, datiLegame);
		parametri.put(ParamType.NODO_PARTENZA_LEGAME, nodoSelezionato );//currentForm.getReticolo().getKey());

		//modalità di ricerca per creazione legame
		parametri.put(ParamType.MODALITA_CERCA_TERMINE, ModalitaCercaType.CREA_LEGAME_TERMINI);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("inserisciLegameTermini"));
	}

	public ActionForward dettaglio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO dettaglio = (DettaglioTermineThesauroVO) nodoSelezionato.getDettaglio();
		ParametriThesauro parametri = (ParametriThesauro) currentForm.getParametri().copy();
		parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.ESAMINA);
		parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglio);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("esaminaThesauro"));
	}

	public ActionForward trascina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO dettaglio = nodoSelezionato.getDettaglio().copy();

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		if (!delegate.isLivAutOk(dettaglio.getLivAut(), true) )
			return mapping.getInputForward();

		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.MODALITA_GESTIONE_TERMINE,	ModalitaGestioneType.GESTIONE);
		parametri.put(ParamType.FORZA_TRASCINAMENTO, true);
		parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglio);
		parametri.put(ParamType.NODO_PARTENZA_LEGAME, nodoSelezionato);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneThesauro"));
	}


	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO dettaglio = nodoSelezionato.getDettaglio().copy();

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		if (!delegate.isLivAutOk(dettaglio.getLivAut(), true) )
			return mapping.getInputForward();

		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.MODALITA_GESTIONE_TERMINE,	ModalitaGestioneType.GESTIONE);
		parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglio);
		parametri.put(ParamType.NODO_PARTENZA_LEGAME, nodoSelezionato);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneThesauro"));
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;

		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.CODICE_THESAURO, currentForm.getRicercaComune().getCodThesauro());
		parametri.put(ParamType.LIVELLO_POLO, currentForm.getRicercaComune().isPolo());
		ModalitaCercaType modalita = (ModalitaCercaType) parametri.get(ParamType.MODALITA_CERCA_TERMINE);

		switch (modalita) {
		case CERCA:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA);
			break;
		case TRASCINA_TITOLI:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_TRASCINA_TITOLI);
			break;
		case CREA_LEGAME_TERMINI:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_LEGAME_TERMINI);
			break;
		case CREA_LEGAME_TITOLO:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_LEGAME_TITOLO);
			break;
		}

		ThRicercaDescrittoreVO ricercaThesauroDescrittore = currentForm.getRicercaComune().getRicercaThesauroDescrittore();
		if (ricercaThesauroDescrittore != null && ValidazioneDati.isFilled(ricercaThesauroDescrittore.getTestoDescr()))
			parametri.put(ParamType.TESTO_THESAURO, ricercaThesauroDescrittore.getTestoDescr().trim());

		ParametriThesauro.send(request, parametri);
		return Navigation.getInstance(request).goForward(mapping.findForward("gestioneThesauro"));
	}



	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		if (!delegate.isLivAutOk(nodoSelezionato.getDettaglio().getLivAut(), true))
			return mapping.getInputForward();

		// funzione non disponibile sulla root
		if (nodoSelezionato.isRoot() ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.setErrors(request, errors, null);

			return mapping.getInputForward();
		}

		ParametriThesauro parametri = currentForm.getParametri().copy();
		TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato.getParent();

		//imposto i dati del legame
		DatiLegameTerminiVO datiLegame = new DatiLegameTerminiVO();
		datiLegame.setDid1((DettaglioTermineThesauroVO) padre.getDettaglio() );
		datiLegame.setDid2((DettaglioTermineThesauroVO) nodoSelezionato.getDettaglio() );
		datiLegame.setTipoLegame(nodoSelezionato.getDatiLegame().toString());
		datiLegame.setNoteLegame(nodoSelezionato.getNotaLegame());
		parametri.put(ParamType.DATI_LEGAME_TERMINI, datiLegame);
		parametri.put(ParamType.NODO_PARTENZA_LEGAME, nodoSelezionato.getParent() );

		//modalità di ricerca per creazione legame
		parametri.put(ParamType.MODALITA_LEGAME_TERMINI, ModalitaLegameTerminiType.CANCELLA);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("modificaLegameTermini"));
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		if (!delegate.isLivAutOk(nodoSelezionato.getDettaglio().getLivAut(), true))
			return mapping.getInputForward();

		// funzione non disponibile sulla root
		if (nodoSelezionato.isRoot() ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.setErrors(request, errors, null);

			return mapping.getInputForward();
		}

		ParametriThesauro parametri = currentForm.getParametri().copy();
		TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato.getParent();

		//imposto i dati del legame
		DatiLegameTerminiVO datiLegame = new DatiLegameTerminiVO();
		datiLegame.setDid1((DettaglioTermineThesauroVO) padre.getDettaglio() );
		datiLegame.setDid2((DettaglioTermineThesauroVO) nodoSelezionato.getDettaglio() );
		datiLegame.setTipoLegame(nodoSelezionato.getDatiLegame().toString());
		datiLegame.setNoteLegame(nodoSelezionato.getNotaLegame());
		parametri.put(ParamType.DATI_LEGAME_TERMINI, datiLegame);

		parametri.put(ParamType.NODO_PARTENZA_LEGAME, nodoSelezionato.getParent() ); //currentForm.getReticolo().getKey());

		//modalità di ricerca per creazione legame
		parametri.put(ParamType.MODALITA_LEGAME_TERMINI, ModalitaLegameTerminiType.MODIFICA);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("modificaLegameTermini"));
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

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		currentForm.setEnableConferma(false);
		ParametriThesauro parametri = currentForm.getParametri();
		ModalitaAnaliticaType modalita = (ModalitaAnaliticaType) parametri.get(ParamType.MODALITA_ANALITICA);
		if (modalita == null)
			return mapping.getInputForward();

		parametri.remove(ParamType.MODALITA_ANALITICA);
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

		switch (modalita) {
		case CONFERMA_SCAMBIA_FORMA:

			TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato.getParent();

			DatiLegameTerminiVO datiLegame = new DatiLegameTerminiVO();
			datiLegame.setDid1((DettaglioTermineThesauroVO) padre.getDettaglio() );
			datiLegame.setDid2((DettaglioTermineThesauroVO) nodoSelezionato.getDettaglio() );
			datiLegame.setTipoLegame(nodoSelezionato.getDatiLegame().toString());
			datiLegame.setNoteLegame(nodoSelezionato.getNotaLegame());
			datiLegame.setOperazione(TipoOperazioneLegame.SCAMBIA);

			CreaVariaTermineVO risposta = delegate.gestioneLegameTermini(datiLegame);
			if (risposta == null)
				return mapping.getInputForward();

			//ricarico l'analitica del padre
			AnaliticaThesauroVO reticolo = delegate.ricaricaReticolo(true, padre.getKey() );
			if (reticolo == null)
				return mapping.getInputForward();
			padre.adoptChildren(reticolo.getReticolo());

			break;
		}

		return mapping.getInputForward();
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		currentForm.setEnableConferma(false);
		currentForm.getParametri().remove(ParamType.MODALITA_ANALITICA);

		return mapping.getInputForward();
	}

	public ActionForward scambia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		ParametriThesauro parametri = currentForm.getParametri();
		parametri.put(ParamType.MODALITA_ANALITICA, ModalitaAnaliticaType.CONFERMA_SCAMBIA_FORMA);
		currentForm.setEnableConferma(true);

		//conferma operazione
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.thesauro.scambia"));
		this.setErrors(request, errors, null);

		return mapping.getInputForward();
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;

		TreeElementViewSoggetti root = currentForm.getReticolo();
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		DettaglioTermineThesauroVO dettaglio = (DettaglioTermineThesauroVO) root.getDettaglio();
		UserVO utente = Navigation.getInstance(request).getUtente();
		ActionForward titoliCollegati =
			delegate.titoliCollegatiBiblio(utente.getCodBib(), dettaglio.getDid(), dettaglio.getTesto(), mapping, false);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;

		TreeElementViewSoggetti root = currentForm.getReticolo();
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		DettaglioTermineThesauroVO dettaglio = (DettaglioTermineThesauroVO) root.getDettaglio();
		ActionForward titoliCollegati = delegate.titoliCollegatiPolo(dettaglio.getDid(), dettaglio.getTesto(), mapping, false);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}

	public ActionForward biblioFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;

		TreeElementViewSoggetti root = currentForm.getReticolo();
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		DettaglioTermineThesauroVO dettaglio = (DettaglioTermineThesauroVO) root.getDettaglio();
		UserVO utente = Navigation.getInstance(request).getUtente();
		ActionForward titoliCollegati =
			delegate.titoliCollegatiBiblio(utente.getCodBib(), dettaglio.getDid(), dettaglio.getTesto(), mapping, true);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}

	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;

		TreeElementViewSoggetti root = currentForm.getReticolo();
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		DettaglioTermineThesauroVO dettaglio = (DettaglioTermineThesauroVO) root.getDettaglio();
		ActionForward titoliCollegati = delegate.titoliCollegatiPolo(dettaglio.getDid(), dettaglio.getTesto(), mapping, true);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward elementoPrecedente(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		String[] listaCidSelez = currentForm.getListaDidSelez();
		int pos = currentForm.getPosizioneCorrente();
		if (pos == 0) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.fineScorrimento"));
			this.setErrors(request, errors, null);
			resetToken(request);
			return mapping.getInputForward();
		}

		String did = listaCidSelez[pos - 1];
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true, did);
		if (analitica == null)
			return mapping.getInputForward();

		currentForm.setReticolo(analitica.getReticolo());
		currentForm.setPosizioneCorrente(--pos);
		return mapping.getInputForward();
	}

	public ActionForward elementoSuccessivo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		String[] listaCidSelez = currentForm.getListaDidSelez();
		int pos = currentForm.getPosizioneCorrente();
		if (pos == listaCidSelez.length - 1) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.fineScorrimento"));
			this.setErrors(request, errors, null);
			resetToken(request);
			return mapping.getInputForward();
		}

		String did = listaCidSelez[pos + 1];
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true, did);
		if (analitica == null)
			return mapping.getInputForward();

		currentForm.setReticolo(analitica.getReticolo());
		currentForm.setPosizioneCorrente(++pos);
		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzione();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
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

	public ActionForward cancellaTermine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request,
				form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO dettaglio = nodoSelezionato.getDettaglio()
				.copy();

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		if (!delegate.isLivAutOk(dettaglio.getLivAut(), true))
			return mapping.getInputForward();

		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.MODALITA_GESTIONE_TERMINE,
				ModalitaGestioneType.CANCELLA);
		//parametri.put(ThesauroParamType.FORZA_TRASCINAMENTO, true);
		parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglio);
		parametri.put(ParamType.NODO_PARTENZA_LEGAME, nodoSelezionato);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(
				mapping.findForward("gestioneThesauro"));
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			if (currentForm.getReticolo() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
						.getReticolo() );
			return mapping.getInputForward();

		}

		if (!checkAttivita(request, form, idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	public ActionForward classi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaThesauroForm currentForm = (AnaliticaThesauroForm) form;
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		if (nodoSelezionato == null)
			return mapping.getInputForward();

		DettaglioTermineThesauroVO thes = (DettaglioTermineThesauroVO) nodoSelezionato.getDettaglio();
		ClassiDelegate delegate = ClassiDelegate.getInstance(request);
		RicercaClassiTermineVO richiesta = new RicercaClassiTermineVO();
		richiesta.setCodThesauro(thes.getCodThesauro());
		richiesta.setDid(thes.getDid());

		Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);
		richiesta.setElemBlocco((String) utente.getDefault(ConstantDefault.RIC_CLA_ELEMENTI_BLOCCHI));

		ClassiCollegateTermineVO cct = delegate.ricercaClassiPerTermineCollegato(richiesta);
		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.PARAMETRI_RICERCA_CLASSI, richiesta);
		parametri.put(ParamType.ANALITICA, currentForm.getReticolo());
		parametri.put(ParamType.DETTAGLIO_OGGETTO, thes);
		parametri.put(ParamType.CLASSI_COLLEGATE, cct);

		OggettoRiferimentoVO rif = new OggettoRiferimentoVO(true, thes.getDid(), thes.getTesto());
		parametri.put(ParamType.OGGETTO_RIFERIMENTO, rif);
		ParametriThesauro.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForwardConfig("listaClassi"));
	}


	private static enum TipoAttivita {
		MODIFICA,
		SCAMBIA,
		CREA,

		MODIFICA_TERMINE,
		CANCELLA_TERMINE,
		INS_LEGAME,
		TRASCINA_TERMINE,
		MOD_LEGAME,
		CANCELLA_LEGAME,
		SCAMBIA_FORMA,

		TITOLI_COLL_BIBLIO,
		TITOLI_COLL_BIBLIO_FILTRO,
		TITOLI_COLL_POLO,
		TITOLI_COLL_POLO_FILTRO,

		//almaviva5_20111019 evolutive CFI
		CLASSI_COLLEGATE;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		Navigation navi = Navigation.getInstance(request);
		try {
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
			DettaglioTermineThesauroVO dettaglio = (DettaglioTermineThesauroVO) nodoSelezionato.getDettaglio();
			String codThesauro = dettaglio.getCodThesauro();

			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);
			switch (attivita) {
			case CREA:
				return
					delegate.isAbilitato(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);

			case MODIFICA:
				if (!delegate.isThesauroGestito(codThesauro) )
					return false;
				return
					delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301);
			case SCAMBIA:
				if (!delegate.isThesauroGestito(codThesauro) )
					return false;
				return delegate.isAbilitato(CodiciAttivita.getIstance().SCAMBIO_FORMA_1029);


			case MODIFICA_TERMINE:
				if (!delegate.isThesauroGestito(codThesauro) )
					return false;
				if (!delegate.isLivAutOk(dettaglio.getLivAut(), false))
					return false;
//				if (navi.bookmarkExists(NavigazioneSemantica.SOGGETTAZIONE_ATTIVA) )
//					return false;
				return
					delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301);

			case CANCELLA_TERMINE:
				if (!delegate.isThesauroGestito(codThesauro) )
					return false;
				if (!delegate.isLivAutOk(dettaglio.getLivAut(), false))
					return false;
				if (nodoSelezionato.hasChildren())
					return false;
				if (!delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301) )
					return false;
				/*
				if (!checkAttivita(request, form, TipoAttivita.CANCELLA_LEGAME.name() ))
					return false;
				if (navi.bookmarkExists(NavigazioneSemantica.SOGGETTAZIONE_ATTIVA) )
					return false;
				*/
				// solo se non legato a titoli
				return (dettaglio.getNumTitoliBiblio() == 0 && dettaglio.getNumTitoliPolo() == 0);

			case TRASCINA_TERMINE:
				if (!delegate.isThesauroGestito(codThesauro) )
					return false;
				if (!delegate.isLivAutOk(dettaglio.getLivAut(), false))
					return false;
				if (!delegate.isAbilitato(CodiciAttivita.getIstance().FONDE_THESAURO_1300) )
					return false;
//				if (navi.bookmarkExists(NavigazioneSemantica.SOGGETTAZIONE_ATTIVA) )
//					return false;

				// solo se legato a titoli
				return (dettaglio.getNumTitoliBiblio() > 0 || dettaglio.getNumTitoliPolo() > 0);

			case INS_LEGAME:
				if (!delegate.isThesauroGestito(codThesauro) )
					return false;
				if (!delegate.isLivAutOk(dettaglio.getLivAut(), false))
					return false;
				if (nodoSelezionato.hasChildren() && dettaglio.isRinvio())
					return false;
				//if (!nodoSelezionato.isRoot() && nodoSelezionato.isRinvio() )
				if (nodoSelezionato.isRinvio() )
					return false;
				return
					delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301);

			case MOD_LEGAME:
			case CANCELLA_LEGAME:
				if (nodoSelezionato.isRoot() )
					return false;
				if (!delegate.isThesauroGestito(codThesauro) )
					return false;

				DettaglioTermineThesauroVO dettaglioPadre =
					(DettaglioTermineThesauroVO) nodoSelezionato.getDettaglioPadre();
				if (!delegate.isLivAutOk(dettaglioPadre.getLivAut(), false))
					return false;
				if (nodoSelezionato.hasChildren() && dettaglio.isRinvio())
					return false;
				return
					delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_THESAURO_1301);

			case SCAMBIA_FORMA:
				if (!delegate.isThesauroGestito(codThesauro) )
					return false;
				if (!delegate.isLivAutOk(dettaglio.getLivAut(), false))
					return false;
				if (!dettaglio.isRinvio())
					return false;
				if (nodoSelezionato.isRoot() && !nodoSelezionato.hasChildren() )
					return false;
				return
					delegate.isAbilitato(CodiciAttivita.getIstance().SCAMBIO_FORMA_1029);

			case TITOLI_COLL_BIBLIO:
			case TITOLI_COLL_BIBLIO_FILTRO:
				return (dettaglio.getNumTitoliBiblio() > 0);
			case TITOLI_COLL_POLO:
			case TITOLI_COLL_POLO_FILTRO:
				return (dettaglio.getNumTitoliPolo() > 0);

			case CLASSI_COLLEGATE:
				return true;
			}

			return false;

		} catch (IllegalArgumentException e) {	//valori fuori enum
			return true;

		} catch (Exception e) {
			log.error("", e);
			navi.setExceptionLog(e);
			return false;
		}
	}

}
