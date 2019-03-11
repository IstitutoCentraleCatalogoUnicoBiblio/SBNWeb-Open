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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ElementoStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.LegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaTerminiThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.AnaliticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiFusioneTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ElementoSinteticaThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTerminiType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTitoloTermineType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.thesauro.ListaThesauriForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.ThesauroDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ListaThesauriAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(ListaThesauriAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.selTutti", "tutti");
		map.put("button.deselTutti", "deseleziona");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.crea", "crea");
		map.put("button.analitica", "analitica");
		map.put("button.gestione", "gestione");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "indietro");
		map.put("button.scegli", "scegli");

		map.put("button.polo", "polo");
		map.put("button.biblio", "biblio");
		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");

		return map;
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private String getTestoTermine(String did, ActionForm form) {
		ListaThesauriForm currentForm = (ListaThesauriForm) form;

		String testo = null;
		for (Object o : currentForm.getOutput().getRisultati()) {
			ElementoSinteticaThesauroVO termine = (ElementoSinteticaThesauroVO) o;
			if (!termine.getDid().equals(did)) continue;

			testo = termine.getTermine();
		}

		return testo;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form)
			throws Exception {

		try {
			ListaThesauriForm currentForm = (ListaThesauriForm) form;
			currentForm.setListaThesauri(CaricamentoComboSemantica
					.loadComboThesauro(Navigation.getInstance(request).getUserTicket(),
							currentForm.getModalita() != ModalitaCercaType.CERCA) );

			return true;

		} catch (Exception ex) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.setErrors(request, errors, ex);
			return false;
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		RicercaThesauroListaVO ricThesLista = null;
		ThRicercaComuneVO ricThesRicerca = null;
		String chiamante = null;
		FolderType folder = null;
		ParametriThesauro parametri = ParametriThesauro.retrieve(request);

		if (!currentForm.isSessione()) {
			log.info("ListaThesauriAction::unspecified");
			currentForm.setParametri(parametri);
			ricThesLista = (RicercaThesauroListaVO) parametri
					.get(ParamType.OUTPUT_SINTETICA);
			ricThesRicerca = (ThRicercaComuneVO) parametri
					.get(ParamType.PARAMETRI_RICERCA);
			OggettoRiferimentoVO oggettoRiferimento =
				(OggettoRiferimentoVO) parametri.get(ParamType.OGGETTO_RIFERIMENTO);
			if (oggettoRiferimento != null)
				currentForm.setOggettoRiferimento(oggettoRiferimento);

			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			if (chiamante == null)
				chiamante = navi.getActionCaller();

			folder = (FolderType) request
					.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
			ModalitaCercaType modalita =
				(ModalitaCercaType) parametri.get(ParamType.MODALITA_CERCA_TERMINE);

			switch (modalita) {
			case CREA_LEGAME_TITOLO:
				AreaDatiPassBiblioSemanticaVO datiBibliografici =
					(AreaDatiPassBiblioSemanticaVO) parametri.get(ParamType.DATI_BIBLIOGRAFICI);
				currentForm.getCatalogazioneSemanticaComune().setBid(datiBibliografici.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(datiBibliografici.getDescPartenza());
				break;

			case CERCA:
				break;

			case TRASCINA_TITOLI:
				DatiFusioneTerminiVO datiFusione = (DatiFusioneTerminiVO) parametri.get(ParamType.DATI_FUSIONE_TERMINI);
				currentForm.setDatiLegame(datiFusione);
				break;

			case CREA_LEGAME_TERMINI:
				DatiLegameTerminiVO datiLegame = (DatiLegameTerminiVO) parametri.get(ParamType.DATI_LEGAME_TERMINI);
				currentForm.setDatiLegame(datiLegame);
				break;
			}

			if (ricThesLista == null || ricThesRicerca == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			}
			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			}
			currentForm.setAction(chiamante);
			currentForm.setSessione(true);
			currentForm.setOutput(ricThesLista);
			currentForm.setRicercaComune(ricThesRicerca);
			currentForm.setModalita(modalita);

			//combo esamina
			currentForm.setComboGestioneEsamina(LabelGestioneSemantica
					.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
							request, form, new String[]{"TH"}, this));
			currentForm.setIdFunzioneEsamina("");

			AreaDatiPassBiblioSemanticaVO areaBib = (AreaDatiPassBiblioSemanticaVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
			if (areaBib != null) {
				currentForm.setAreaDatiPassBiblioSemanticaVO(areaBib);
				currentForm.getCatalogazioneSemanticaComune().setBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getDescPartenza());
				currentForm.setFolder(folder);
			}
			if (currentForm.getFolder() != null
					&& currentForm.getFolder() == FolderType.FOLDER_THESAURO) {
				currentForm.setEnableTit(true);
				currentForm.setEnableDeselTutti(false);
				currentForm.setEnableSelTutti(false);
				currentForm.setEnableStampa(false);
				currentForm.setEnableOk(true);
			}

		} else {
			// Posso cercare in indice solo se è la prima volta che carico
			// la lista
			if (currentForm.getFolder() != null
					&& currentForm.getFolder() == FolderType.FOLDER_THESAURO) {
				currentForm.setEnableOk(true);
			} else {
				currentForm.setEnableOk(false);
			}
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));
			// ricThes.setRicercaComune((ThRicercaComuneVO) request
			// .getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
			// ricThes.setOutput((RicercaThesauroListaVO) request
			// .getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
			currentForm.setDataInserimento((String) request
					.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO));
			currentForm.setDataVariazione((String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA));
			currentForm
					.setTreeElementViewSoggetti((TreeElementViewSoggetti) request
							.getAttribute(NavigazioneSemantica.ANALITICA));

		}

		if (!initCombo(request, form))
			return mapping.getInputForward();

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		currentForm.setAppoggio(appoggio);
		if (ricThesLista != null) {
			String idLista = ricThesLista.getIdLista();
			currentForm.setIdLista(idLista);
			super.addSbnMarcIdLista(request, idLista);
			currentForm.setMaxRighe(ricThesLista.getMaxRighe());
			currentForm.setNumBlocco(ricThesLista.getNumBlocco());
			currentForm.setTotBlocchi(ricThesLista.getTotBlocchi());
			currentForm.setTotRighe(ricThesLista.getTotRighe());

			// se ho un solo elemento lo seleziono
			List<ElementoSinteticaThesauroVO> risultati = ricThesLista.getRisultati();
			if (risultati.size() == 1)
				currentForm.setCodSelezionato(risultati.get(0).getDid());

		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();

	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		ActionMessages errors = new ActionMessages();

		if (currentForm.getDidMultiSelez() != null
				&& currentForm.getDidMultiSelez().length > 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noBox"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato) ) {

			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		ElementoSinteticaThesauroVO elementoSelezionato = getElemento(form, codSelezionato);
		if (!delegate.isThesauroGestito(elementoSelezionato.getCodThesauro() ) ) {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.thesauroNonGestito") );
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		ParametriThesauro parametri = currentForm.getParametri().copy();
		AnaliticaThesauroVO analitica = null;

		switch (currentForm.getModalita()) {
		case CERCA:
			return mapping.getInputForward();

		case TRASCINA_TITOLI:
			analitica = delegate.ricaricaReticolo(true,
					codSelezionato);

			if (analitica == null)
				return mapping.getInputForward();

			DatiFusioneTerminiVO datiFusione = (DatiFusioneTerminiVO) parametri.get(ParamType.DATI_FUSIONE_TERMINI);
			DettaglioTermineThesauroVO dettaglioTermineScelto = analitica.getReticolo().getAreaDatiDettaglioOggettiVO().getDettaglioTermineThesauroVO();
			datiFusione.setDid2(dettaglioTermineScelto);

			AreaDatiPassaggioInterrogazioneTitoloReturnVO listaTitoliCollegatiBiblio =
				delegate.listaTitoliCollegatiBiblio(datiFusione.getDid1().getDid(), currentForm.getMaxRighe());

			if (listaTitoliCollegatiBiblio == null)
				return mapping.getInputForward();

			datiFusione.setTitoliCollegati(listaTitoliCollegatiBiblio);
			ParametriThesauro.send(request, parametri);

			return navi.goForward(mapping.findForward("sinteticaTitoliFusione"));

		case CREA_LEGAME_TITOLO:
			analitica = delegate.ricaricaReticolo(true,	codSelezionato);

			if (analitica == null)
				return mapping.getInputForward();

			// imposto i dati del did arrivo
			DettaglioTermineThesauroVO dettaglioTermine = analitica.getReticolo()
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTermineThesauroVO();
			parametri.put(ParamType.DETTAGLIO_OGGETTO, dettaglioTermine);
			parametri.put(ParamType.MODALITA_LEGAME_TITOLO_TERMINE, ModalitaLegameTitoloTermineType.CREA);
			ParametriThesauro.send(request, parametri);

			return navi.goForward(mapping.findForward("inserisciLegameTitoloTermine"));

		case CREA_LEGAME_TERMINI:
			analitica = delegate.ricaricaReticolo(true,
					codSelezionato);

			if (analitica == null)
				return mapping.getInputForward();

			DatiLegameTerminiVO datiLegame = (DatiLegameTerminiVO) parametri.get(ParamType.DATI_LEGAME_TERMINI);
			dettaglioTermineScelto = analitica.getReticolo().getAreaDatiDettaglioOggettiVO().getDettaglioTermineThesauroVO();
			datiLegame.setDid2(dettaglioTermineScelto);
			parametri.put(ParamType.MODALITA_LEGAME_TERMINI, ModalitaLegameTerminiType.CREA);
			ParametriThesauro.send(request, parametri);

			return navi.goForward(mapping.findForward("creaLegameTermini"));
		}

		return mapping.getInputForward();

	}

	private ElementoSinteticaThesauroVO getElemento(ActionForm form,
			String codSelezionato) {
		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		List<ElementoSinteticaThesauroVO> risultati = currentForm.getOutput().getRisultati();
		for (ElementoSinteticaThesauroVO elemento : risultati)
			if (elemento.getDid().equals(codSelezionato) )
				return elemento;

		return null;
	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		ActionMessages errors = new ActionMessages();
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (currentForm.getNumBlocco() > currentForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
			this.setErrors(request, errors, null);
			resetToken(request);
			return mapping.getInputForward();
		}

		HashSet<Integer> appoggio = currentForm.getAppoggio();
		int i = currentForm.getNumBlocco();

		if (appoggio != null) {
			if (appoggio.contains(i)) {
				return mapping.getInputForward();
			}
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

		RicercaThesauroListaVO areaDatiPassReturn =
			delegate.nextBlocco(currentForm.getIdLista(),
					currentForm.getNumBlocco(),
					currentForm.getMaxRighe());

		if (areaDatiPassReturn == null)
			return mapping.getInputForward();

		int numBlocco = currentForm.getNumBlocco();
		currentForm.getAppoggio().add(numBlocco);
		currentForm.setNumBlocco(numBlocco);
		List<ElementoSinteticaThesauroVO> sintetica = currentForm.getOutput()
				.getRisultati();
		sintetica.addAll(areaDatiPassReturn.getRisultati());
		Collections.sort(sintetica,
				ElementoSinteticaThesauroVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		ParametriThesauro parametri = currentForm.getParametri().copy();
		parametri.put(ParamType.CODICE_THESAURO, currentForm.getRicercaComune().getCodThesauro());
		parametri.put(ParamType.LIVELLO_POLO, currentForm.getRicercaComune().isPolo());
		//parametri.setAttribute(ThesauroParamType.DATI_BIBLIOGRAFICI, currentForm.getAreaDatiPassBiblioSemanticaVO());

		switch (currentForm.getModalita()) {
		case CERCA:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA);
			break;
		case TRASCINA_TITOLI:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_TRASCINA_TITOLI);
			break;
		case CREA_LEGAME_TITOLO:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_LEGAME_TITOLO);
			break;
		case CREA_LEGAME_TERMINI:
			parametri.put(ParamType.MODALITA_GESTIONE_TERMINE, ModalitaGestioneType.CREA_PER_LEGAME_TERMINI);
			break;
		}

		if (!ValidazioneDati.strIsNull(currentForm.getRicercaComune().getRicercaThesauroDescrittore()
				.getTestoDescr())) {
			parametri.put(ParamType.TESTO_THESAURO, currentForm.getRicercaComune()
					.getRicercaThesauroDescrittore().getTestoDescr().trim());
		}

		ParametriThesauro.send(request, parametri);
		return Navigation.getInstance(request).goForward(mapping.findForward("creaThesauro"));
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		ActionMessages errors = new ActionMessages();

		String did = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(did)) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		UserVO utente = Navigation.getInstance(request).getUtente();
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		ActionForward titoliCollegati =
			delegate.titoliCollegatiBiblio(utente.getCodBib(), did, getTestoTermine(did, form), mapping, false);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		ActionMessages errors = new ActionMessages();

		String did = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(did)) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		ActionForward titoliCollegati = delegate.titoliCollegatiPolo(did, getTestoTermine(did, form), mapping, false);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}


	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		ActionMessages errors = new ActionMessages();

		String did = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(did)) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		ActionForward titoliCollegati = delegate.titoliCollegatiPolo(did, getTestoTermine(did, form), mapping, true);
		if (titoliCollegati != null)
			return titoliCollegati;

		return mapping.getInputForward();
	}



	public ActionForward analitica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;

		ActionMessages errors = new ActionMessages();
		if (currentForm.getDidMultiSelez() == null
				&& currentForm.getCodSelezionato() == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.selObblOggSint"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);

		if (currentForm.getDidMultiSelez() != null
				&& currentForm.getDidMultiSelez().length > 0) {
			String[] listaDidSelez = currentForm.getDidMultiSelez();
			String xid = listaDidSelez[0];

			AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true,
					xid);
			if (analitica == null)
				return mapping.getInputForward();

			resetToken(request);
			ParametriThesauro parametri = (ParametriThesauro) currentForm
					.getParametri().clone();
			parametri.put(ParamType.ANALITICA, analitica
					.getReticolo());
			parametri.put(ParamType.LISTA_DID_SELEZIONATI, listaDidSelez);
			ParametriThesauro.send(request, parametri);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("analiticaThesauro"));
		}

		if (!ValidazioneDati.strIsNull(currentForm.getCodSelezionato())) {
			String xid = currentForm.getCodSelezionato();
			AnaliticaThesauroVO analitica = delegate.ricaricaReticolo(true,
					xid);
			if (analitica == null)
				return mapping.getInputForward();

			resetToken(request);
			ParametriThesauro parametri = (ParametriThesauro) currentForm
					.getParametri().clone();
			parametri.put(ParamType.ANALITICA, analitica
					.getReticolo());
			ParametriThesauro.send(request, parametri);
			request.setAttribute(NavigazioneSemantica.DID_RIFERIMENTO, xid);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("analiticaThesauro"));

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato

			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;

		ActionMessages errors = new ActionMessages();
		String[] multiSelez = currentForm.getDidMultiSelez();
		String codSelezionato = currentForm.getCodSelezionato();
		if (multiSelez == null || multiSelez.length == 0) {
			if (ValidazioneDati.strIsNull(codSelezionato)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.selObblOggSint"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			}
			multiSelez = new String[] { codSelezionato };
		}

		Navigation navi = Navigation.getInstance(request);
		UserVO utente = navi.getUtente();
		ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
		ParametriStampaTerminiThesauroVO parametriStampa = new ParametriStampaTerminiThesauroVO();
		parametriStampa.setOnline(true);
		parametriStampa.setCodPolo(utente.getCodPolo());
		parametriStampa.setCodBib(utente.getCodBib());
		parametriStampa.setDescrizioneBiblioteca(utente.getBiblioteca());
		parametriStampa.setStampaTitoli(true);
		parametriStampa.setStampaTerminiCollegati(true);
		String codThesauro = currentForm.getRicercaComune().getCodThesauro();
		if (ValidazioneDati.isFilled(codThesauro)) {
			parametriStampa.setCodThesauro(codThesauro);
			String descrizioneThesauro = delegate.getDescrizioneCodice(CodiciType.CODICE_THESAURO, codThesauro);
			parametriStampa.setDescrizioneThesauro(descrizioneThesauro);
		}

		List<ElementoStampaSemanticaVO> outputStampa = new ArrayList<ElementoStampaSemanticaVO>();
		HashSet<String> tmp = new HashSet<String>(Arrays.asList(multiSelez));
		for (Object o : currentForm.getOutput().getRisultati() ) {
			ElementoSinteticaThesauroVO elemento = (ElementoSinteticaThesauroVO) o;
			if (tmp.contains(elemento.getDid())) {
				ElementoStampaTerminiThesauroVO elementoStampa = new ElementoStampaTerminiThesauroVO(elemento);
				//fake legame titolo
				LegameTitoloVO legameTitolo = new LegameTitoloVO();
				legameTitolo.setBid("AQ10030614");
				legameTitolo.setTitolo("Tomaso Babington Macaulay ; versione dall'inglese con note e prefazione intorno alla vita ed agli scritti dell'autore di P. E. Nicoli.");
				legameTitolo.setNotaLegame("test nota legame titolo");
				List<LegameTitoloVO> legami = new ArrayList<LegameTitoloVO>();
				legami.add(legameTitolo);
				elementoStampa.setLegamiTitoli(legami);

				//fake legame termine
				LegameTermineVO legameTermine = new LegameTermineVO();
				legameTermine.setDid("X11D008911");
				legameTermine.setTesto("storia del fascismo");
				legameTermine.setTipoLegame("BT");
				legameTermine.setNotaLegame("test nota legame titolo");
				List<LegameTermineVO> legamiTermini = new ArrayList<LegameTermineVO>();
				legamiTermini.add(legameTermine);

				legameTermine = new LegameTermineVO();
				legameTermine.setDid("CSWD000099");
				legameTermine.setTesto("storia del nazismo");
				legameTermine.setTipoLegame("UF");
				legameTermine.setNotaLegame("test2");
				legamiTermini.add(legameTermine);

				elementoStampa.setLegamiTermini(legamiTermini );

				outputStampa.add(elementoStampa);
			}
		}

		parametriStampa.setOutput(outputStampa);

		request.setAttribute("FUNZIONE_STAMPA",  StampaType.STAMPA_TERMINI_THESAURO);
		request.setAttribute("DATI_STAMPE_ON_LINE", parametriStampa);
		return navi.goForward(mapping.findForward("stampaSintetica") );
	}

	public ActionForward deseleziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		currentForm.setDidMultiSelez(null);
		return mapping.getInputForward();
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;
		List<ElementoSinteticaThesauroVO> risultati = currentForm.getOutput()
				.getRisultati();
		String[] selezione = new String[risultati.size()];
		for (int i = 0; i < risultati.size(); i++) {
			ElementoSinteticaThesauroVO elem = risultati.get(i);
			selezione[i] = elem.getDid();
		}

		currentForm.setDidMultiSelez(selezione);
		currentForm.setCodSelezionato(null);
		return mapping.getInputForward();
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaThesauriForm currentForm = (ListaThesauriForm) form;

		// Correzione Bug esercizio 6304: corretto il caso in cui, al ritorno dalla ricerca dei titoli collegati senza aver trovato nulla,
		// si va in errore invece di riprospettare la vecchia mappa con l'opportuno diagnostico
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	private enum TipoAttivita {
		CREA,
		TITOLI_COLL_POLO,
		TITOLI_COLL_POLO_FILTRO;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		try {
			ThesauroDelegate delegate = ThesauroDelegate.getInstance(request);
			switch (TipoAttivita.valueOf(idCheck) ) {
			case CREA:
				return delegate.isAbilitato(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);
			case TITOLI_COLL_POLO:
			case TITOLI_COLL_POLO_FILTRO:
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

}
