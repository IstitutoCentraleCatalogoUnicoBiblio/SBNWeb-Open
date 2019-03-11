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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoParoleVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaFormTypes;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.ListaSoggettiForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate.RicercaSoggettoResult;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ListaSoggettiAction extends SinteticaLookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(ListaSoggettiAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.selTutti", "tutti");
		map.put("button.deselTutti", "deseleziona");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.crea", "crea");
		map.put("button.analitica", "analitica");
		map.put("button.polo", "polo");
		map.put("button.biblio", "biblio");
		map.put("button.importa", "importa");
		map.put("button.gestione", "gestione");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "indietro");
		map.put("button.scegli", "scegli");

		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");
		map.put("button.titoli.indice", "indice");

		return map;
	}

	private void loadSoggettario(String ticket, ActionForm form) throws Exception {
		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, false));
	}

	private SoggettiDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new SoggettiDelegate(
				FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		String chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
		if (chiamante == null)
			chiamante = navi.getActionCaller();

		RicercaSoggettoListaVO output = null;

		if (!currentForm.isSessione()) {
			output = initSintetica(mapping, currentForm, request, chiamante);
			if (output == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			}

			ParametriSoggetti parametri = ParametriSoggetti.retrieve(request);
			if (parametri != null)
				currentForm.setParametriSogg(parametri);

		}
		 else {
			// Posso cercare in indice solo se è la prima volta che carico
			// la lista
			currentForm.setFolder((FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE));
			if (currentForm.getFolder() != null
					&& currentForm.getFolder() == FolderType.FOLDER_SOGGETTI) {
				currentForm.setEnableOk(true);
			} else {
				currentForm.setEnableOk(false);
			}

			currentForm.setAction(chiamante);
			RicercaComuneVO paramRicerca = (RicercaComuneVO) request.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
			if (paramRicerca != null) {
				currentForm.setRicercaComune(paramRicerca);
				currentForm.setEnableIndice(paramRicerca.isIndice() );
			}
			output = (RicercaSoggettoListaVO) request.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			if (output != null)
				currentForm.setOutput(output);

			String livAut = (String) request.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA);
			if (livAut != null)
				currentForm.setLivContr(livAut);
			if (currentForm.getAction().equals("/gestionesemantica/soggetto/AnaliticaSoggetto"))
				currentForm.setTipoSoggetto((String) request.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO));

			currentForm.setDataInserimento((String) request
					.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO));
			currentForm.setDataVariazione((String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA));
			currentForm
					.setRootAnalitica((TreeElementViewSoggetti) request
							.getAttribute(NavigazioneSemantica.ANALITICA));


			// ??????QUANDO RITORNO DA TRASCINATITOLIACTION DOVREI REIMPOSTARE
			// LA LISTA E IL SOGGETTARIO
			// CON I VALORI CHE MI SONO STATI PASSATI DA TRASCINATITOLI
			currentForm.setEnableCercaIndice(false);

		}

		ActionMessages errors = currentForm.validate(mapping, request);
		try {
			this.loadSoggettario(navi.getUserTicket(), currentForm);
		} catch (Exception ex) {
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
		}

		if (currentForm.getAction().equals(
				"/gestionesemantica/soggetto/AnaliticaDescrittore")) {
			currentForm.setEnableCercaIndice(false);
		}
		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		currentForm.setBlocchiCaricati(appoggio);
		if (output != null) {
			String idLista = output.getIdLista();
			currentForm.setIdLista(idLista);
			currentForm.setMaxRighe(output.getMaxRighe());
			currentForm.setNumBlocco(output.getNumBlocco());
			currentForm.setTotBlocchi(output.getTotBlocchi());
			currentForm.setTotRighe(output.getTotRighe());
			super.addSbnMarcIdLista(request, idLista);
		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		// currentForm.setOffset(0);

		if (currentForm.getAction().equals(
				"/gestionesemantica/soggetto/AnaliticaSoggetto")) {
			currentForm.setTipoSoggetto((String) request
					.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO));
		}

		currentForm.setRootAnalitica((TreeElementViewSoggetti) request
				.getAttribute(NavigazioneSemantica.ANALITICA));
		currentForm.setDataInserimento((String) request
				.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO));
		currentForm.setDataVariazione((String) request
				.getAttribute(NavigazioneSemantica.DATA_MODIFICA));
		currentForm.setProgr((String) request.getAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO));
		currentForm.setTitoliBiblio((List<?>) request
				.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));
		return mapping.getInputForward();

	}

	private RicercaSoggettoListaVO initSintetica(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, String chiamante) {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		RicercaSoggettoListaVO currentFormLista;
		RicercaSoggettoListaVO currentFormListaDescr;
		RicercaSoggettoListaVO currentFormListaSog;
		RicercaComuneVO currentFormRicerca;
		String currentFormTesto;
		FolderType folder;
		boolean isPolo;
		log.info("ListaSoggettiAction::unspecified");

		// devo inizializzare tramite le request.getAttribute(......)
		currentFormLista = (RicercaSoggettoListaVO) request
				.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
		currentFormListaDescr = (RicercaSoggettoListaVO) request
				.getAttribute("outputlistadescr");
		currentFormListaSog = (RicercaSoggettoListaVO) request
				.getAttribute("outputlistadescrsog");
		currentFormRicerca = (RicercaComuneVO) request
				.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
		currentForm.setCidTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA));
		currentForm.setTestoTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));
		currentForm
				.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
						.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));
		currentFormTesto = (String) request.getAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE);
		folder = (FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
		isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();

		if (currentFormLista == null || currentFormRicerca == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.setErrors(request, errors, null);
			return null;
		}
		currentForm.setRicercaComune(currentFormRicerca);
		if (chiamante == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.FunzChiamNonImp"));
			this.setErrors(request, errors, null);
			return null;
		}
		currentForm.setAction(chiamante);
		if (ValidazioneDati.isFilled(currentFormTesto) )
			currentForm.setTesto(currentFormTesto.trim());

		currentForm.setSessione(true);
		currentForm.setOutput(currentFormLista);
		currentForm.getRicercaComune().setPolo(isPolo);

		List<ElementoSinteticaSoggettoVO> risultati = currentFormLista.getRisultati();
		if (risultati.size() == 1)
			currentForm.setCodSelezionato(risultati.get(0).getCid() );

		Boolean abilitaRicercaIndice = (Boolean) request
				.getAttribute(NavigazioneSemantica.ABILITA_RICERCA_INDICE);
		if (abilitaRicercaIndice != null) {
			if (currentForm.getAction().equals(
					"/gestionesemantica/soggetto/SinteticaDescrittori")) {
				currentForm.setEnableCercaIndice(abilitaRicercaIndice);
			}
			if (currentForm.getAction().equals(
					"/gestionesemantica/soggetto/SinteticaDescrittoriSoggetto")) {
				currentForm.setEnableCercaIndice(abilitaRicercaIndice);
			}
			if (currentForm.getAction().equals(
					"/gestionesemantica/soggetto/AnaliticaSoggetto")) {
				currentForm.setEnableCercaIndice(abilitaRicercaIndice);
			}
		}
		currentForm.setEnableIndice(currentForm.getRicercaComune().isIndice());
		currentForm.setOutputDescrittori(currentFormListaDescr);
		currentForm.setOutputDescrittoriSog(currentFormListaSog);

		AreaDatiPassBiblioSemanticaVO test = (AreaDatiPassBiblioSemanticaVO) request
				.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
		if (test != null) {
			currentForm.setAreaDatiPassBiblioSemanticaVO(test);
			currentForm.getCatalogazioneSemanticaComune().setBid(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getBidPartenza());
			currentForm.getCatalogazioneSemanticaComune().setTestoBid(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getDescPartenza());
			currentForm.setFolder(folder);

			if (currentForm.getFolder() != null
					&& currentForm.getFolder() == FolderType.FOLDER_SOGGETTI) {
				currentForm.setEnableTit(true);
				currentForm.setEnableCercaIndice(false);
				currentForm.setEnableDeselTutti(false);
				currentForm.setEnableSelTutti(false);
				currentForm.setEnableStampa(false);
				// currentForm.setEnableCreaListaPolo(false);
				currentForm.setEnableOk(true);
			}
		}
		if (currentForm.getCidTrascinaDa() != null) {
			currentForm.setEnableCercaIndice(false);
			currentForm.setEnableOk(true);
		}

		//combo esamina
		currentForm.setComboGestioneEsamina(LabelGestioneSemantica
				.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
						request, form, new String[]{"SO"}, this));
		currentForm.setIdFunzioneEsamina("");

		OggettoRiferimentoVO oggettoRiferimento = (OggettoRiferimentoVO) request
				.getAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO);
		if (oggettoRiferimento != null) {
			currentForm.setOggettoRiferimento(oggettoRiferimento);
			ActionForm callerForm = Navigation.getInstance(request).getCallerForm();
//			if (callerForm instanceof EsaminaDescrittoreForm || callerForm instanceof GestioneDescrittoreForm)
//				currentForm.setEnableCercaIndice(false);
			switch (SemanticaFormTypes.getFormType(callerForm)) {
			case ESAMINA_DESCRITTORE:
			case GESTIONE_DESCRITTORE:
			case SINTETICA_DESCRITTORI:
				currentForm.setEnableCercaIndice(false);
			}
		}
		return currentFormLista;
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		//almaviva5_20130517 #5314
		List<String> items = getMultiBoxSelectedItems(currentForm.getCodSoggetto());

		if (ValidazioneDati.isFilled(items) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noBox"));
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		if (!ValidazioneDati.strIsNull(currentForm.getCodSelezionato())
				&& !ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid()) ) {
			String xid = currentForm.getCodSelezionato();

			ElementoSinteticaSoggettoVO elemento = getElemento(form, xid);
			if (!getDelegate(request).isSoggettarioGestito(elemento.getCodiceSoggettario()) ) {
				// messaggio di errore.
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.soggettarioNonGestito") );
				// nessun codice selezionato
				return mapping.getInputForward();
			}

			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, new Boolean(currentForm.getRicercaComune().isPolo()));
			String tipo = NavigazioneSemantica.TIPO_OGGETTO_CID;
			AnaliticaSoggettoVO reticolo = this.ricaricaReticolo(xid, tipo, currentForm, request);
			if (reticolo == null)
				return mapping.getInputForward();

			request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO, reticolo.getReticolo().getDettaglio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
			request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, xid);
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
			request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, elemento.getCodiceSoggettario() );
			request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, currentForm.getRicercaComune().getDescSoggettario());

			return mapping.findForward("creaLegameSoggettoTitolo");
		}

		// sto tentando di trascinare sullo stesso CID
		String xid = currentForm.getCodSelezionato();
		if (!ValidazioneDati.strIsNull(currentForm.getCidTrascinaDa() )
				&& currentForm.getCidTrascinaDa().equals(xid)) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", "Trascinamento non consentito"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		if (currentForm.getTitoliBiblio() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());
			for (Object elemento : currentForm.getOutput().getRisultati()) {
				if (((ElementoSinteticaSoggettoVO) elemento).getCid().equals(xid)) {
					request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, ((ElementoSinteticaSoggettoVO) elemento).getTesto());
					break;
				}
			}
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_ARRIVO, xid);
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm.getTestoTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm.getCidTrascinaDa());
			request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
			request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutput());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm.getDatiBibliografici());
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );

			AnaliticaSoggettoVO reticolo = this.ricaricaReticolo(xid, NavigazioneSemantica.TIPO_OGGETTO_CID, currentForm, request);
			if (reticolo == null)
				return mapping.getInputForward();

			ParametriSoggetti parametri = currentForm.getParametriSogg().copy();
			parametri.put(SoggettiParamType.DETTAGLIO_ID_ARRIVO, reticolo.getReticolo().getDettaglio() );
			ParametriSoggetti.send(request, parametri);

			return Navigation.getInstance(request).goForward(mapping.findForward("ok"));
		}

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (!ValidazioneDati.strIsNull(currentForm.getCodSelezionato()) ) {
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			currentForm.setBiblioteca(utenteCollegato.getCodBib());

			// // ho selezionato un CID
			request.setAttribute(TitoliCollegatiInvoke.codBiblio, currentForm.getBiblioteca());
			request.setAttribute(TitoliCollegatiInvoke.livDiRicerca, TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
			// per quanto riguarda il cid è quello della mappa
			request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm.getCidTrascinaDa());
			request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm.getTestoTrascinaDa());
			request.setAttribute(TitoliCollegatiInvoke.visualCall, "NO");
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping.getPath());
			request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));

			return Navigation.getInstance(request).goForward(mapping.findForward("titoliCollegatiBiblio"));

		} else {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}
	}

	private ElementoSinteticaSoggettoVO getElemento(ActionForm form, String cid) {
		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
		List<ElementoSinteticaSoggettoVO> risultati = currentForm.getOutput().getRisultati();
		for (ElementoSinteticaSoggettoVO elemento : risultati)
			if (elemento.getCid().equals(cid) )
				return elemento;

		return null;
	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
		ActionMessages errors = new ActionMessages();
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune()
				.getCodSoggettario());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		if (currentForm.getNumBlocco() > currentForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
			this.setErrors(request, errors, null);
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

		RicercaSoggettoListaVO areaDatiPass = new RicercaSoggettoListaVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());
		areaDatiPass.setLivelloPolo(currentForm.getRicercaComune().isPolo());
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		RicercaSoggettoListaVO areaDatiPassReturn = (RicercaSoggettoListaVO) factory
				.getGestioneSemantica().getNextBloccoSoggetti(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
			this.setErrors(request, errors, null);
			resetToken(request);
			return mapping.getInputForward();
		}

		String idLista = areaDatiPassReturn.getIdLista();
		currentForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		currentForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = currentForm.getNumBlocco();
		appoggio = currentForm.getBlocchiCaricati();
		appoggio.add(numBlocco);
		currentForm.setBlocchiCaricati(appoggio);

		// currentForm.setNumBlocco(++numBlocco);
		currentForm.setNumBlocco(numBlocco);
		currentForm.getOutput().getRisultati().addAll(
				areaDatiPassReturn.getRisultati());
		Collections.sort(currentForm.getOutput().getRisultati(),
				ElementoSinteticaSoggettoVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();
	}

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		RicercaComuneVO parametriRicercaPolo = currentForm.getRicercaComune().copy();
		parametriRicercaPolo.setPolo(false);

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, parametriRicercaPolo.getCodSoggettario());

		ActionMessages errors = new ActionMessages();

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, false);
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, parametriRicercaPolo );
		// Verifico se la ricerca è stata impostata in input per descrittore e
		// in tal caso invio la mappa appropriata
		if (parametriRicercaPolo.getRicercaSoggetto().count() > 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.indice"));
			this.setErrors(request, errors, null);
			RicercaSoggettoDescrittoriVO tmp = (RicercaSoggettoDescrittoriVO) parametriRicercaPolo.getRicercaSoggetto();
			RicercaSoggettoParoleVO parole = new RicercaSoggettoParoleVO(tmp
					.getTestoSogg(), tmp.getCid(), tmp.getDescrittoriSogg(),
					tmp.getDescrittoriSogg1(), tmp.getDescrittoriSogg2(), tmp
							.getDescrittoriSogg3(), "", "");
			request.setAttribute(NavigazioneSemantica.RICERCA_SOGGETTI_PER_PAROLE, parole);

			return mapping.findForward("cercaIndicePerParole");

		} else try {
			SoggettiDelegate delegate = getDelegate(request);
			delegate.eseguiRicerca(parametriRicercaPolo, mapping);
			//currentForm.setEnableIndice(true);
			HashSet<Integer> appoggio = new HashSet<Integer>();
			appoggio.add(1);
			currentForm.setBlocchiCaricati(appoggio);
			RicercaSoggettoResult op = delegate.getOperazione();
			RicercaSoggettoListaVO output = delegate.getOutput();

			switch (op) {
			case analitica_1:// SoggettiDelegate.analitica:
				currentForm.setOutput(output);
				currentForm.setRicercaComune(parametriRicercaPolo);
				if (output.getTotRighe() > 0) {
					String idLista = output.getIdLista();
					currentForm.setIdLista(idLista);
					super.addSbnMarcIdLista(request, idLista);
					currentForm.setMaxRighe(output.getMaxRighe());
					currentForm.setTotRighe(output.getTotRighe());
					currentForm.setNumBlocco(1);
					currentForm.setNumNotizie(output.getTotRighe());
					currentForm.setTotBlocchi(output.getTotBlocchi());
				}

				//combo esamina
				currentForm.setComboGestioneEsamina(LabelGestioneSemantica
						.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
								request, form, new String[]{"SO"}, this));
				currentForm.setIdFunzioneEsamina("");
				currentForm.setEnableIndice(true);
				return mapping.getInputForward();

			case crea_4:// SoggettiDelegate.crea:
				currentForm.setEnableIndice(false);
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();

			case sintetica_3:// SoggettiDelegate.sintetica:
				if (parametriRicercaPolo.getOperazione() instanceof RicercaDescrittoreVO) {
					if (parametriRicercaPolo.getRicercaDescrittore()
							.getTestoDescr() != null
							&& currentForm.getRicercaComune()
									.getRicercaDescrittore().getTestoDescr()
									.length() > 0)
						return mapping.findForward("sinteticadescrittori");
					else
						request.setAttribute(NavigazioneSemantica.PAROLE, parametriRicercaPolo.getRicercaDescrittore());

					return mapping.findForward("sinteticadescrittoriparole");
				} else
					request.setAttribute(NavigazioneSemantica.DESCRITTORI_DEL_SOGGETTO, parametriRicercaPolo.getRicercaSoggetto());
				return mapping.findForward("sinteticadescrittorisoggetto");

			case lista_2:// SoggettiDelegate.lista:
				currentForm.setOutput(output);
				currentForm.setRicercaComune(parametriRicercaPolo);
				if (output.getTotRighe() > 0) {
					String idLista = output.getIdLista();
					currentForm.setIdLista(idLista);
					super.addSbnMarcIdLista(request, idLista);
					currentForm.setMaxRighe(output.getMaxRighe());
					currentForm.setTotRighe(output.getTotRighe());
					currentForm.setNumBlocco(1);
					currentForm.setNumNotizie(output.getTotRighe());
					currentForm.setTotBlocchi(output.getTotBlocchi());
				}

				currentForm.setEnableIndice(true);

				//combo esamina
				currentForm.setComboGestioneEsamina(LabelGestioneSemantica
						.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
								request, form, new String[]{"SO"}, this));
				currentForm.setIdFunzioneEsamina("");

				return mapping.getInputForward();

			case diagnostico_0:// SoggettiDelegate.diagnostico:
				if (!output.isEsitoNonTrovato()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", output
									.getTestoEsito()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				currentForm.setEnableIndice(false);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.setErrors(request, errors, null);
				currentForm.setNumBlocco(0);
				currentForm.setTotBlocchi(0);
				currentForm.setTotRighe(0);
				currentForm.setEnableCarica(false);
				currentForm.setEnableDeselTutti(false);
				currentForm.setEnableSelTutti(false);
				currentForm.setEnableEsamina(false);
				currentForm.setEnableStampa(false);
				currentForm.getOutput().getRisultati().clear();
				return mapping.getInputForward();

			default:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noselezione"));
				//currentForm.setRicercaComune(parametriRicercaPolo);
				return mapping.getInputForward();
			}

		} catch (ValidationException e) {
			// errori indice

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			//currentForm.setRicercaComune(parametriRicercaPolo);
			return mapping.getInputForward();

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			//currentForm.setRicercaComune(parametriRicercaPolo);
			return mapping.getInputForward();
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			//currentForm.setRicercaComune(parametriRicercaPolo);
			return mapping.getInputForward();
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e
							.getMessage()));
			this.setErrors(request, errors, e);
			log.error("", e);
			//currentForm.setRicercaComune(parametriRicercaPolo);
			return mapping.getInputForward();
		}

	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
		RicercaComuneVO ricerca = currentForm.getRicercaComune();
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, ricerca.getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.EDIZIONE_SOGGETTARIO, ricerca.getEdizioneSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, ricerca.getDescSoggettario());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutput());
		request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, currentForm.getTesto());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, new Boolean(ricerca
				.isPolo()));
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
		if (currentForm.getCidTrascinaDa() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm.getCidTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm
					.getTestoTrascinaDa().trim());
			request
					.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm
					.getDatiBibliografici());
		}
		return Navigation.getInstance(request).goForward(
				mapping.findForward("creaSoggetto"));
	}

	public ActionForward analitica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
		ActionMessages errors = new ActionMessages();

		if (currentForm.getCodSoggetto() == null
				&& currentForm.getCodSelezionato() == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.selObblOggSint"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, currentForm.getRicercaComune()
				.getDescSoggettario());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, new Boolean(currentForm.getRicercaComune()
				.isPolo()));
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutput());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
		// request.setAttribute(NavigazioneSemantica.ELEMENTI_PER_BLOCCO,
		// currentForm.getRicercaComune().getElemBlocco());
		if (currentForm.getCidTrascinaDa() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm.getCidTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm
					.getTestoTrascinaDa().trim());
			request
					.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm
					.getDatiBibliografici());
		}

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		// Selezione multipla
		List<String> items = getMultiBoxSelectedItems(currentForm.getCodSoggetto());
		//almaviva5_20130523 #5314
		if (ValidazioneDati.isFilled(items)) {
			String[] listaCidSelez = items.toArray(new String[0]);
			String xid = listaCidSelez[0];
			request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, xid);
			request.setAttribute(NavigazioneSemantica.LISTA_OGGETTI_SELEZIONATI, listaCidSelez);
			String tipo = NavigazioneSemantica.TIPO_OGGETTO_CID;
			AnaliticaSoggettoVO reticolo = this.ricaricaReticolo(xid, tipo, currentForm, request);
			if (reticolo == null)
				return mapping.getInputForward();

			resetToken(request);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("analiticaSoggetto"));
		}

		// Selezione singola
		if (currentForm.getCodSelezionato() != null) {
			// per rendere un campo int confrontabile con un object
			// request.setAttribute("numTitoliIndice", new
			// Integer(currentForm.getNumTitoliIndice()));

			String xid = currentForm.getCodSelezionato();
			String tipo = NavigazioneSemantica.TIPO_OGGETTO_CID;
			AnaliticaSoggettoVO reticolo = this.ricaricaReticolo(xid, tipo, currentForm, request);
			if (reticolo == null)
				return mapping.getInputForward();

			request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, xid);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("analiticaSoggetto"));
		}
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.codiceNessunaSelezione"));
		this.setErrors(request, errors, null);
		// nessun codice selezionato
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
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
		this.setErrors(request, errors, null);
		// nessun codice selezionato
		return mapping.getInputForward();
	}

	public ActionForward deseleziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		this.deselezionaTutti(form);

		return mapping.getInputForward();
	}

	private void deselezionaTutti(ActionForm form) {
		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
		currentForm.setCodSoggetto(null);
		currentForm.setCodSelezionato(null);
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		List<ElementoSinteticaSoggettoVO> risultati = currentForm.getOutput().getRisultati();
		String[] selezione = new String[risultati.size()];
		int i = 0;
		for (ElementoSinteticaSoggettoVO ric : risultati )
			selezione[i++] = String.valueOf(ric.getCid());

		currentForm.setCodSoggetto(selezione);
		currentForm.setCodSelezionato(null);
		return mapping.getInputForward();
	}

	public ActionForward importa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		ActionMessages errors = new ActionMessages();

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutput());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		ElementoSinteticaSoggettoVO elemento = getElemento(form, codSelezionato);
		if (elemento == null)
			return mapping.getInputForward();

		SoggettiDelegate delegate = getDelegate(request);

		if (!delegate.isSoggettarioGestito(elemento.getCodiceSoggettario())) {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.soggettarioNonGestito"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}
/*
		List<DatiCondivisioneSoggettoVO> datiCondivisione = elemento.getDatiCondivisione();
		if (datiCondivisione.size() > 0) {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.duplicato", datiCondivisione.get(
							0).getCidPolo()));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}
*/
		AnaliticaSoggettoVO reticolo = this.ricaricaReticolo(elemento.getCid(),
				NavigazioneSemantica.TIPO_OGGETTO_CID, form, request);
		if (reticolo == null)
			return mapping.getInputForward();

		TreeElementViewSoggetti root = currentForm.getRootAnalitica();
		DettaglioSoggettoVO dettSogGenVO = (DettaglioSoggettoVO) root.getDettaglio();
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO, dettSogGenVO);
		resetToken(request);

		if (currentForm.getRicercaComune().isIndice())
			currentForm.setEnableEsamina(true);

		if (currentForm.getCidTrascinaDa() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA,
					currentForm.getCidTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
					currentForm.getTestoTrascinaDa().trim());
			request.setAttribute(
					NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm
							.getTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
					currentForm.getDatiBibliografici());
		}

		return mapping.findForward("importaSoggetto");

	}

	private AnaliticaSoggettoVO ricaricaReticolo(String xid, String tipo, ActionForm form,
			HttpServletRequest request) throws Exception {

		if (tipo.equals(NavigazioneSemantica.TIPO_OGGETTO_CID)) {
			String cid = xid;
			// chiamata a EJB con tipo oggetto padre a CID
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			AnaliticaSoggettoVO analitica = null;
			ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
			try {
				analitica = factory.getGestioneSemantica()
						.creaAnaliticaSoggettoPerCid(
								currentForm.getRicercaComune()
										.isPolo(), cid,
								utenteCollegato.getTicket());

			} catch (ValidationException e) {
				// errori indice
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return null;

			} catch (DataException e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return null;
			} catch (InfrastructureException e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return null;
			} catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.erroreSistema", e
								.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return  null;
			}

			if (!analitica.isEsitoOk() ) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", analitica
								.getTestoEsito()));
				this.setErrors(request, errors, null);
				return null;
			}

			currentForm.setRootAnalitica(analitica
					.getReticolo());
			request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm
					.getRootAnalitica());
			String livelloAut = analitica.getReticolo().getLivelloAutorita();
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, livelloAut);
			request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_POLO, analitica.getReticolo()
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioSoggettoGeneraleVO().getNumTitoliPolo());
			request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_BIBLIO, analitica.getReticolo()
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioSoggettoGeneraleVO().getNumTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.HAS_LEGAMI_POLO, analitica.getReticolo()
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioSoggettoGeneraleVO().isHas_num_tit_coll());
			request.setAttribute(NavigazioneSemantica.HAS_LEGAMI_BIBLIO, analitica.getReticolo()
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioSoggettoGeneraleVO().isHas_num_tit_coll_bib());
			request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE, analitica.getReticolo()
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioSoggettoGeneraleVO().isCondiviso() );
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, livelloAut);
			String dataInserimento = analitica.getDataInserimento();
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, dataInserimento);
			String dataVariazione = analitica.getDataVariazione();
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dataVariazione);
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dataVariazione);
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, dataInserimento);
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dataVariazione);
			String tipoSoggetto = analitica.getReticolo()
					.getCategoriaSoggetto();
			request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, tipoSoggetto);
			String testo = analitica.getReticolo().getTesto();
			request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, testo);

			return analitica;

		}
		return null;

	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		for (Object o : currentForm.getOutput().getRisultati()) {
			ElementoSinteticaSoggettoVO elem = (ElementoSinteticaSoggettoVO) o;
			if (elem.getCid().equals(currentForm.getCodSelezionato())) {
				currentForm.setCodSelezionato(elem.getCid());
				currentForm.setTesto(elem.getTesto());
				break;
			}
		}
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, utenteCollegato.getCodBib() );
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCodSelezionato());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("delegate_titoliCollegati"));

	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		for (Object o : currentForm.getOutput().getRisultati()) {
			ElementoSinteticaSoggettoVO elem = (ElementoSinteticaSoggettoVO) o;
			if (elem.getCid().equals(currentForm.getCodSelezionato())) {
				currentForm.setCodSelezionato(elem.getCid());
				currentForm.setTesto(elem.getTesto());
				break;
			}
		}

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCodSelezionato());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		// + ".do?FROM_SINT_TITOLI=Y");
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("delegate_titoliCollegati"));
	}


	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		for (Object o : currentForm.getOutput().getRisultati()) {
			ElementoSinteticaSoggettoVO elem = (ElementoSinteticaSoggettoVO) o;
			if (elem.getCid().equals(currentForm.getCodSelezionato())) {
				currentForm.setCodSelezionato(elem.getCid());
				currentForm.setTesto(elem.getTesto());
				break;
			}
		}

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCodSelezionato());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		// + ".do?FROM_SINT_TITOLI=Y");
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("delegate_titoliCollegatiFiltro"));
	}



	@Override
	public ActionForward navigationBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("navigationBack");
		return mapping.getInputForward();
	}

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		for (Object o : currentForm.getOutput().getRisultati()) {
			ElementoSinteticaSoggettoVO elem = (ElementoSinteticaSoggettoVO) o;
			if (elem.getCid().equals(codSelezionato)) {
				currentForm.setCodSelezionato(elem.getCid());
				currentForm.setTesto(elem.getTesto());
				break;
			}
		}

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, codSelezionato);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath() );
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		Navigation navi = Navigation.getInstance(request);

		return navi.goForward(mapping.findForward("delegate_titoliCollegati"));
	}


	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSoggettiForm currentForm = (ListaSoggettiForm) form;

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
		IMPORTA,

		TITOLI_COLL_POLO,
		TITOLI_COLL_POLO_FILTRO,
		TITOLI_COLL_INDICE,
		SIMILI_INDICE;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			ListaSoggettiForm currentForm = (ListaSoggettiForm) form;
			SoggettiDelegate delegate = getDelegate(request);
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);
			boolean livelloPolo = currentForm.getRicercaComune().isPolo();

			switch (attivita) {
			case CREA:
				if (delegate.countSoggettariGestiti() < 1)
					return false;
				return delegate
						.isAbilitatoSO(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);
			case IMPORTA:
				if (delegate.countSoggettariGestiti() < 1)
					return false;
				return delegate
						.isAbilitatoSO(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);

			case TITOLI_COLL_POLO:
				return livelloPolo;
			case TITOLI_COLL_POLO_FILTRO:
				return true;
			case TITOLI_COLL_INDICE:
				return !livelloPolo;

			}
			return false;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}

	}
}
