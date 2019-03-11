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

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamentoClasse;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.TipoOperazione;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.ImportaClasseForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ImportaClasseAction extends NavigationBaseAction {

	private static Logger log = Logger.getLogger(ImportaClasseAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.importa", "importa");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			ImportaClasseForm currentForm = (ImportaClasseForm) form;
			currentForm.setListaSistemiClassificazione(CaricamentoComboSemantica
							.loadComboSistemaClassificazione(ticket, false));
			currentForm.setListaEdizioni(CaricamentoComboSemantica.loadComboEdizioneDewey());
			currentForm.setListaStatoControllo(CaricamentoComboSemantica
					.loadComboStato(null));

			return true;
		} catch (Exception e) {

			LinkableTagUtils.resetErrors(request);
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.Faild"));

			log.error("", e);
			// nessun codice selezionato
			return false;
		}
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		log.info("ImportaClasseAction::init");
		super.init(request, form);
		ImportaClasseForm currentForm = (ImportaClasseForm) form;
		ParametriThesauro paramThes = ParametriThesauro.retrieve(request);
		if (paramThes != null) {
			currentForm.setParametriThes(paramThes);
			OggettoRiferimentoVO rif = (OggettoRiferimentoVO) paramThes.get(ParamType.OGGETTO_RIFERIMENTO);
			if (rif == null)
				return;

			currentForm.setOggettoRiferimento(rif);
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ImportaClasseForm currentForm = (ImportaClasseForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		String chiamante = null;
		// String inserito = null;
		// String modificato = null;
		String dataInserimento = null;
		String dataModifica = null;
		DettaglioClasseVO dettaglio = null;
		RicercaClasseListaVO ricSoggLista = null;
		boolean isPolo = false;
		String simbolo = null;
		// String edizione = null;
		// FolderType folder = null;

		if (!currentForm.isSessione()) {

			this.init(request, currentForm);

			// devo inizializzare tramite le request.getAttribute(......)

			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			dataInserimento = (String) request
					.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			ricSoggLista = (RicercaClasseListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			dettaglio = (DettaglioClasseVO) request
					.getAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE);
			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();
			simbolo = (String) request
					.getAttribute(NavigazioneSemantica.SIMBOLO_CLASSE);
			currentForm
					.getRicercaClasse()
					.setCodEdizioneDewey(
							(String) request
									.getAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY));

			if (chiamante == null) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));

				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setAction(chiamante);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setDettClaGen(dettaglio);
			currentForm.setOutput(ricSoggLista);

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
				currentForm.setFolder((FolderType) request
						.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE));
			}
		}

		if (!this.initCombo(request, form, navi.getUserTicket()))
			return mapping.getInputForward();


		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setDettClaGen(dettaglio);
		currentForm.getRicercaClasse().setPolo(isPolo);
		currentForm.setCodStatoControllo(currentForm.getDettClaGen()
				.getLivAut());
		currentForm.setIdentificativoClasse(currentForm.getDettClaGen()
				.getIdentificativo());
		currentForm.setSimbolo(simbolo);
		currentForm
				.setDescrizione(currentForm.getDettClaGen().getDescrizione());
		currentForm.getRicercaClasse().setCodSistemaClassificazione(
				currentForm.getDettClaGen().getCampoSistema());
		currentForm.getRicercaClasse().setCodEdizioneDewey(
				currentForm.getDettClaGen().getCampoEdizione());
		currentForm
				.setDescrizione(currentForm.getDettClaGen().getDescrizione());
		currentForm.setUlterioreTermine(currentForm.getDettClaGen()
				.getUlterioreTermine());
		currentForm.setDataInserimento((String) request
				.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO));
		currentForm.setDataModifica((String) request
				.getAttribute(NavigazioneSemantica.DATA_MODIFICA));

		if (currentForm.getFolder() != null
				&& currentForm.getFolder() == FolderType.FOLDER_CLASSI) {
			currentForm.setEnableTit(true);
		}
		currentForm.setModificato(false);
		currentForm.setAbilita(true);
		currentForm.setTitoliBiblio((List<?>) request
				.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));
		currentForm.setCidTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA));
		currentForm.setTestoTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));
		currentForm
				.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
						.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));
		return mapping.getInputForward();
	}

	public ActionForward importa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ImportaClasseForm currentForm = (ImportaClasseForm) form;
		CreaVariaClasseVO classe = null;
		CreaVariaClasseVO richiesta = new CreaVariaClasseVO();

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		CreaVariaClasseVO classeImportata = null;

		Navigation navi = Navigation.getInstance(request);
		try {
			RicercaClassiVO ricercaClasse = currentForm.getRicercaClasse();
			richiesta.setCodSistemaClassificazione(ricercaClasse.getCodSistemaClassificazione());
			richiesta.setCodEdizioneDewey(ricercaClasse.getCodEdizioneDewey());
			richiesta.setDescrizione(currentForm.getDescrizione());
			richiesta.setUlterioreTermine(currentForm.getUlterioreTermine());
			richiesta.setLivello(currentForm.getCodStatoControllo());
			richiesta.setLivelloPolo(true);
			richiesta.setSimbolo(currentForm.getSimbolo());
			richiesta.setT001(currentForm.getIdentificativoClasse());
			DettaglioClasseVO dettaglio = currentForm.getDettClaGen();
			richiesta.setCostruito(dettaglio.isCostruito());

			classe = this.eseguiImportazioneClasse(richiesta, request, mapping,	form);

			if (classe == null)
				return mapping.getInputForward();

			if (classe.isEsitoOk() ) {
				// OK

			} else {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", classe
								.getTestoEsito()));

				return mapping.getInputForward();
			}

			UserVO utenteCollegato = navi.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			ricercaClasse.setPolo(true);
			classeImportata = factory.getGestioneSemantica().analiticaClasse(
					ricercaClasse.isPolo(),
					currentForm.getIdentificativoClasse(),
					utenteCollegato.getTicket());

			if (currentForm.isEsiste()) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.duplicataCla", currentForm
								.getIdentificativoClasse()));

			} else {
				if (currentForm.getFolder() == null && !currentForm.isVaria()) {
					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.gestioneSemantica.operOkInsCla",
							classeImportata.getSimbolo()));

				} else {
					if (!currentForm.isEnableTit())
						LinkableTagUtils.addError(request, new ActionMessage(
								"errors.gestioneSemantica.operOk"));

				}
			}
			if (classeImportata.isEsitoOk() ) {

				//almaviva5_20111021 evolutive CFI
				ParametriThesauro parametriThes = currentForm.getParametriThes().copy();
				TipoOperazione op = (TipoOperazione) parametriThes.get(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
				if (op != null)	{ //sono in gestione legame termine-classe?
					DatiLegameTermineClasseVO datiLegame = new DatiLegameTermineClasseVO();
					parametriThes.put(ParamType.DETTAGLIO_LEGAME_CLASSE, datiLegame.new LegameTermineClasseVO(new DettaglioClasseVO(classeImportata)) );
					ParametriThesauro.send(request, parametriThes);
					return mapping.findForward("gestioneLegame");
				}

				request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
						ricercaClasse.isPolo());

				request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA,
						currentForm.getOutput());
				dettaglio.setLivAut(classeImportata.getLivello());
				dettaglio.setDescrizione(classeImportata.getDescrizione());
				dettaglio.setCampoSistema(classeImportata.getCodSistemaClassificazione());
				dettaglio.setCampoEdizione(classeImportata.getCodEdizioneDewey());
				dettaglio.setDataAgg(classeImportata.getDataVariazione());
				dettaglio.setDataIns(classeImportata.getDataInserimento());
				dettaglio.setIndicatore("NO");
				dettaglio.setT005(classeImportata.getT005());
				dettaglio.setUlterioreTermine(classeImportata.getUlterioreTermine());
				//currentForm.getDettClaGen().setIdentificativo(classeImportata.getSimbolo());
				dettaglio.setIdentificativo(classeImportata.getT001());
				dettaglio.setCondiviso(classeImportata.isCondiviso());
				dettaglio.setCostruito(classeImportata.isCostruito());
				request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE,
						dettaglio);
				request.setAttribute(
						NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
						currentForm.getAreaDatiPassBiblioSemanticaVO());
				request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE,
						currentForm.getFolder());
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
						mapping.getPath());
				if (currentForm.isEnableTit()) {
					navi.purgeThis();
					return mapping.findForward("catalogazioneSemantica");
				} else {
					if (currentForm.getTitoliBiblio() != null) {
						request.setAttribute(
								NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA,
								currentForm.getTitoliBiblio());
						request.setAttribute(
								NavigazioneSemantica.TRASCINA_TESTO_ARRIVO,
								classeImportata.getDescrizione());
						request.setAttribute(
								NavigazioneSemantica.TRASCINA_CLASSE_ARRIVO,
								classeImportata.getT001());
								//classeImportata.getSimbolo());
						request.setAttribute(
								NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
								currentForm.getTestoTrascinaDa());
						request.setAttribute(
								NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
								currentForm.getCidTrascinaDa());
						request.setAttribute(
								NavigazioneSemantica.ACTION_CALLER, mapping
										.getPath());
						request.setAttribute(
								NavigazioneSemantica.OUTPUT_SINTETICA,
								currentForm.getOutput());
						request.setAttribute(
								NavigazioneSemantica.DATI_BIBLIOGRAFICI,
								currentForm.getDatiBibliografici());
						ricercaClasse.setPolo(true);
						request.setAttribute(
								NavigazioneSemantica.PARAMETRI_RICERCA,
								ricercaClasse.clone() );
						navi.purgeThis();
						return navi.goForward(mapping.findForward("trascina"));
					} else {
						navi.purgeThis();
						return mapping.findForward("esaminaClasse");
					}

				}
			}
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.SBNMarc", classeImportata
							.getTestoEsito()));

			return mapping.getInputForward();

		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			navi.setExceptionLog(e);
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			navi.setExceptionLog(e);
			return mapping.getInputForward();
		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			navi.setExceptionLog(e);
			return mapping.getInputForward();
		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			navi.setExceptionLog(e);
			return mapping.getInputForward();
		}
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));

		// nessun codice selezionato
		return mapping.getInputForward();
	}

	private CreaVariaClasseVO eseguiImportazioneClasse(
			CreaVariaClasseVO richiesta, HttpServletRequest request,
			ActionMapping mapping, ActionForm form) throws Exception {
		ClassiDelegate delegate = ClassiDelegate.getInstance(request);
		RicercaClassiVO ricerca = new RicercaClassiVO();
		ricerca.setCodSistemaClassificazione(richiesta.getCodSistemaClassificazione());
		ricerca.setCodEdizioneDewey(richiesta.getCodEdizioneDewey());
		// ricerca.setParole(richiesta.getDescrizione().trim());
		ricerca.setSimbolo(richiesta.getSimbolo().trim());
		ricerca.setOrdinamentoClasse(TipoOrdinamentoClasse.PER_TESTO);
		ricerca.setIdentificativoClasse(richiesta.getT001());
		ricerca.setPolo(true);
		ricerca.setPuntuale(true);

		try {
			delegate.eseguiRicerca(ricerca, mapping);
			RicercaClasseListaVO lista = (RicercaClasseListaVO) request.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			List<SinteticaClasseVO> classi = lista.getRisultati();

			int size = ValidazioneDati.size(classi);
			if (size > 1) {
				// errore troppi soggetti
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruenzaCla"));

				// nessun codice selezionato
				return null;
			}
			ImportaClasseForm currentForm = (ImportaClasseForm) form;
			if (size == 1) {
				// 1 simile trovato
				SinteticaClasseVO classeTrovata = ValidazioneDati.first(classi);
				CreaVariaClasseVO classe = this.richiediAnaliticaClasse(classeTrovata.getIdentificativoClasse(), request, form);
				currentForm.setEsiste(true);
				return classe;
			}

			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			// // non ho trovato nulla, tento la creazione in polo
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// verifico che la notazione non esista già sulla b.i. di polo,
			// in Indice potrebbe essere stato variato il testo di una notazione
			// già precedentemente importata sulla b.i. di polo

			ClassiDelegate delegate1 = ClassiDelegate.getInstance(request);
			RicercaClassiVO ricerca1 = new RicercaClassiVO();
			ricerca1.setCodSistemaClassificazione(richiesta.getCodSistemaClassificazione());
			ricerca1.setCodEdizioneDewey(richiesta.getCodEdizioneDewey());
			ricerca1.setIdentificativoClasse(richiesta.getT001());
			ricerca1.setOrdinamentoClasse(TipoOrdinamentoClasse.PER_TESTO);
			ricerca1.setPolo(true);

			delegate1.eseguiRicerca(ricerca1, mapping);
			RicercaClasseListaVO lista1 = (RicercaClasseListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);

			if (lista1.getRisultati().size() > 1) {
				// errore troppi soggetti
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruenzaCla"));

				// nessun codice selezionato
				return null;
			}
			currentForm = (ImportaClasseForm) form;
			if (lista1.getRisultati().size() == 1) {
				// 1 simile trovato

				// String idClasse = ((SinteticaClasseVO)
				// lista1.getRisultati().get(0)).getIdentificativoClasse();
				// CreaVariaClasseVO analitica1 =
				// this.richiediAnaliticaClasse(idClasse, request, form,
				// errors);
				currentForm.setEsiste(true);

			} else
				currentForm.setEsiste(false);
			// la notazione è già presente con quell'identificativo sulla
			// b.i. di polo

			CreaVariaClasseVO classe = new CreaVariaClasseVO();
			if (currentForm.isEsiste()) {
				currentForm.setEsiste(false);
				currentForm.setVaria(true);
				richiesta.setT005(currentForm.getT005());
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.KoImportaCla"));

				return null;
				// classe =
				// factory.getGestioneSemantica().variaClasse(richiesta,
				// utenteCollegato.getTicket());
			} else {
				richiesta.setCodSistemaClassificazione(currentForm
						.getRicercaClasse().getCodSistemaClassificazione());
				richiesta.setCodEdizioneDewey(currentForm.getRicercaClasse()
						.getCodEdizioneDewey());
				richiesta.setDescrizione(currentForm.getDescrizione());
				richiesta
						.setUlterioreTermine(currentForm.getUlterioreTermine());
				richiesta.setSimbolo(currentForm.getSimbolo());
				richiesta.setLivello(currentForm.getCodStatoControllo());
				richiesta.setLivelloPolo(true);
				richiesta.setCondiviso(true);
				if (currentForm.isModificato()) {
					// creo notazione

					classe = factory.getGestioneSemantica().creaClasse(
							richiesta, utenteCollegato.getTicket());
					currentForm.setAbilita(false);
					currentForm.setEsiste(false);
				} else {
					// importo notazione
					classe = factory.getGestioneSemantica().importaClasse(
							richiesta, utenteCollegato.getTicket());
					currentForm.setEsiste(false);
				}

			}
			if (!classe.isEsitoOk() ) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", classe
								.getTestoEsito()));

				return classe;
			}
			return classe;

		} catch (ValidationException e) {
			Navigation.getInstance(request).setExceptionLog(e);
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			// nessun codice selezionato
			CreaVariaClasseVO classe = new CreaVariaClasseVO();
			classe.setEsito("9999");
			classe.setTestoEsito(e.getMessage());
			return classe;

		} catch (DataException e) {
			Navigation.getInstance(request).setExceptionLog(e);
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			CreaVariaClasseVO classe = new CreaVariaClasseVO();
			classe.setEsito("9999");
			classe.setTestoEsito(e.getMessage());
			return classe;
		} catch (InfrastructureException e) {
			Navigation.getInstance(request).setExceptionLog(e);
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			log.error("", e);
			CreaVariaClasseVO classe = new CreaVariaClasseVO();
			classe.setEsito("9999");
			classe.setTestoEsito(e.getMessage());
			return classe;
		} catch (Exception e) {
			Navigation.getInstance(request).setExceptionLog(e);
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);
			CreaVariaClasseVO classe = new CreaVariaClasseVO();
			classe.setEsito("9999");
			classe.setTestoEsito(e.getMessage());
			return classe;
		}

	}

	private CreaVariaClasseVO richiediAnaliticaClasse(String idClasse,
			HttpServletRequest request, ActionForm form) throws Exception {

		ImportaClasseForm currentForm = (ImportaClasseForm) form;
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		CreaVariaClasseVO analiticaClasse;
		try {
			analiticaClasse = factory.getGestioneSemantica().analiticaClasse(
					true, idClasse, utenteCollegato.getTicket());

			if (analiticaClasse.isEsitoOk() ) {
				currentForm.setT005(analiticaClasse.getT005());
			} else {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", analiticaClasse
								.getTestoEsito()));

				return analiticaClasse;
			}

			return analiticaClasse;

		} catch (Exception e) {
			log.error("", e);
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));

			return null;
		}
	}
}
