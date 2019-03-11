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
package it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamentoClasse;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO.LegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CatturaClassificazioneDaIndiceForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class CatturaClassificazioneDaIndiceAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(CatturaClassificazioneDaIndiceAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.selTutti", "tutti");
		map.put("button.deselTutti", "deseleziona");
		map.put("button.carica", "caricaBlocco");
		map.put("button.cattura", "cattura");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "indietro");

		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			CatturaClassificazioneDaIndiceForm currentForm = (CatturaClassificazioneDaIndiceForm) form;
			currentForm
					.setListaSistemiClassificazione(CaricamentoComboSemantica
							.loadComboSistemaClassificazione(ticket, true));
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

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatturaClassificazioneDaIndiceForm currentForm = (CatturaClassificazioneDaIndiceForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		CatSemClassificazioneVO ricSoggLista = null;
		String chiamante = null;
		FolderType folder = null;
		boolean isPolo = false;

		if (!currentForm.isSessione()) {
			log.info("CatturaClassificazioneDaIndiceAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			ricSoggLista = (CatSemClassificazioneVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			folder = (FolderType) request
					.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();

			if (ricSoggLista == null) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.Faild"));

				return mapping.getInputForward();
			}
			if (chiamante == null) {

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));

				return mapping.getInputForward();
			}
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));
			currentForm.setSessione(true);

			List<SinteticaClasseVO> lista = ricSoggLista.getListaClassi();
			if (ValidazioneDati.size(lista) == 1)
				currentForm.setCodSelezionato(lista.get(0).getIdentificativoClasse());

			currentForm.setOutputLista(ricSoggLista);

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
				currentForm.getCatalogazioneSemanticaComune().setLivAutBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getTreeElement()
								.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO()
								.getDetTitoloPFissaVO().getLivAut());
				currentForm.getCatalogazioneSemanticaComune().setNaturaBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getTreeElement()
								.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO()
								.getDetTitoloPFissaVO().getNatura());
				currentForm.getCatalogazioneSemanticaComune()
						.setTipoMaterialeBid(
								currentForm.getAreaDatiPassBiblioSemanticaVO()
										.getTreeElement()
										.getAreaDatiDettaglioOggettiVO()
										.getDettaglioTitoloCompletoVO()
										.getDetTitoloPFissaVO().getTipoMat());
				currentForm.getCatalogazioneSemanticaComune().setT005(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getTreeElement()
								.getAreaDatiDettaglioOggettiVO()
								.getDettaglioTitoloCompletoVO()
								.getDetTitoloPFissaVO().getVersione());
				currentForm.setFolder(folder);
			}
		} else {
			currentForm.setFolder((FolderType) request
					.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE));
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));
			currentForm.setOutputLista((CatSemClassificazioneVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
		}

		if (!this.initCombo(request, form, navi.getUserTicket()))
			return mapping.getInputForward();

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		currentForm.setAppoggio(appoggio);
		if (ricSoggLista != null) {
			currentForm.setIdLista(ricSoggLista.getIdLista());
			currentForm.setMaxRighe(ricSoggLista.getMaxRighe());
			currentForm.setNumBlocco(ricSoggLista.getNumBlocco());
			currentForm.setTotBlocchi(ricSoggLista.getTotBlocchi());
			currentForm.setTotRighe(ricSoggLista.getTotRighe());
		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		currentForm.setRicercaClasse((RicercaClassiVO) request
				.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
		currentForm.getRicercaComune().setPolo(isPolo);
		return mapping.getInputForward();

	}

	public ActionForward cattura(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// cattura mi porta a importa classificazione con il titolo sopra e
		// questa mi
		// manda al crea legame
		CatturaClassificazioneDaIndiceForm currentForm = (CatturaClassificazioneDaIndiceForm) form;


		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		if (!ValidazioneDati.strIsNull(currentForm.getCodSelezionato())
				&& !ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid())) {
			String xid = currentForm.getCodSelezionato();
			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	false);

			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			CreaVariaClasseVO classeIndice = null;

			try {
				classeIndice = factory.getGestioneSemantica().analiticaClasse(false, xid,	utenteCollegato.getTicket());

				if (classeIndice.isEsitoOk() ) {
					currentForm.setLivContr(classeIndice.getLivello());
					String dataInserimento = classeIndice.getDataInserimento();
					currentForm.setDataInserimento(dataInserimento);
					String dataVariazione = classeIndice.getDataVariazione();
					currentForm.setDataVariazione(dataVariazione);
					String descrizione = classeIndice.getDescrizione();
					currentForm.setDescrizione(descrizione);
					currentForm.getRicercaClasse().setCodSistemaClassificazione(classeIndice.getCodSistemaClassificazione());
					currentForm.getRicercaClasse().setCodEdizioneDewey(classeIndice.getCodEdizioneDewey());

				} else {
					// errori indice
					LinkableTagUtils.addError(request, new ActionMessage(
							"errors.gestioneSemantica.indiceTestoEsito",
							classeIndice.getTestoEsito()));

					return mapping.getInputForward();
				}
			} catch (EJBException e) {
				// errori indice
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.indiceTestoEsito", e.getMessage()));

				log.error("", e);
				// nessun codice selezionato
				return mapping.getInputForward();

			} catch (ValidationException ve) {
				// errori indice
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.erroreValidazione", ve
								.getMessage()));

				log.error("", ve);
				// nessun codice selezionato
				return mapping.getInputForward();
			}

			CreaVariaClasseVO classeImportata = null;
			CreaVariaClasseVO richiesta = new CreaVariaClasseVO();

			// Viene settato il token per le transazioni successive
			this.saveToken(request);

			CreaVariaClasseVO analitica;
			try {
				richiesta.setCodSistemaClassificazione(currentForm
						.getRicercaClasse().getCodSistemaClassificazione());
				richiesta.setCodEdizioneDewey(currentForm.getRicercaClasse()
						.getCodEdizioneDewey());
				if (currentForm.getDescrizione() == null) {
					richiesta.setDescrizione(" ");
				} else {
					richiesta.setDescrizione(currentForm.getDescrizione()
							.trim());
				}
				richiesta.setLivello(currentForm.getLivContr());
				richiesta.setLivelloPolo(true);
				richiesta.setT001(xid);

				classeImportata = this.eseguiImportazioneClasse(richiesta, request,
						mapping, form);
				if (classeImportata == null)
					return mapping.getInputForward();

				analitica = factory.getGestioneSemantica().analiticaClasse(
						true, classeImportata.getT001(), utenteCollegato.getTicket());

				if (analitica.isEsitoOk() ) {
					// VERIFICO LA PRESENZA O MENO DELla classificazione SULLA
					// B.I.
					// 1. Classificazione PRESENTE SULLA B.I. DI POLO
					// verifico se ha legami con titoli in polo
					// 1.1 se ha legami con titoli e uno dei titoli è quello di
					// partenza mando diag.
					// 1.2 se ha legami ma non con il bid di partenza inserisco
					// il legame con titolo
					// 1.3 se non ha legami con titoli inserisco il legame con
					// il bid di partenza
					// 2. classificazione NON PRESENTE SULLA B.I. DI POLO
					// 2.1 importo il cid e creo il legame con il titolo
					// currentForm.setT005(analitica.getT005());
					return creaLegameClassificazioneTitolo(mapping,
									request, LinkableTagUtils.getErrors(request), classeImportata, utenteCollegato,
									factory, currentForm);
					}
				// errore SBNMarc

				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", classeImportata
								.getTestoEsito()));

				// nessun codice selezionato
				return mapping.getInputForward();

			} catch (ValidationException e) {
				// errori indice
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));

				log.error("", e);
				// nessun codice selezionato
				return mapping.getInputForward();

			} catch (DataException e) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));

				log.error("", e);
				return mapping.getInputForward();// gestione errori java
			} catch (InfrastructureException e) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));

				log.error("", e);
				return mapping.getInputForward();// gestione errori java
			} catch (Exception e) {
				LinkableTagUtils.addError(request, new ActionMessage(
						"errors.gestioneSemantica.erroreSistema", e
								.getMessage()));

				log.error("", e);
				return mapping.getInputForward();// gestione errori java
			}
		} else {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));

			// nessun codice selezionato

			return mapping.getInputForward();
		}

	}

	private ActionForward creaLegameClassificazioneTitolo(
			ActionMapping mapping, HttpServletRequest request,
			ActionMessages errors, CreaVariaClasseVO classe1,
			UserVO utenteCollegato, FactoryEJBDelegate factory, ActionForm form)
			throws RemoteException, InfrastructureException, DataException,
			ValidationException {

		CatturaClassificazioneDaIndiceForm currentForm = (CatturaClassificazioneDaIndiceForm) form;
		AreaDatiPassBiblioSemanticaVO datiGB =
			currentForm.getAreaDatiPassBiblioSemanticaVO();
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = datiGB.getTreeElement()
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();

		// creo il legame con il bid in polo
		DatiLegameTitoloClasseVO legame = new DatiLegameTitoloClasseVO();

		legame.setBid(currentForm.getCatalogazioneSemanticaComune().getBid());
		legame.setBidNatura(currentForm.getCatalogazioneSemanticaComune()
				.getNaturaBid());
		legame.setT005(detTitoloPFissaVO.getVersione() );
		legame.setBidLivelloAut(currentForm.getCatalogazioneSemanticaComune()
				.getLivAutBid());
		legame.setBidTipoMateriale(currentForm
				.getCatalogazioneSemanticaComune().getTipoMaterialeBid());
		legame.setLivelloPolo(true);
		LegameTitoloClasseVO legameTitCla = legame.new LegameTitoloClasseVO();
		legame.getLegami().add(legameTitCla);
		legameTitCla.setIdentificativoClasse(classe1.getT001());
		legameTitCla.setNotaLegame("");
		legameTitCla.setCodSistemaClassificazione(currentForm.getRicercaClasse()
				.getCodSistemaClassificazione());
		DatiLegameTitoloClasseVO risposta = factory.getGestioneSemantica()
				.creaLegameTitoloClasse(legame, utenteCollegato.getTicket());

		if (!risposta.isEsitoOk() ) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", risposta
							.getTestoEsito()));

			return mapping.getInputForward();
		}

		//aggiorno timestamp del titolo soggettato
		datiGB.getTreeElement().setT005(risposta.getT005());
		detTitoloPFissaVO.setVersione(risposta.getT005() );

		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB);
		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.gestioneSemantica.operOk", risposta
						.getTestoEsito()));

		return mapping.findForward("catalogazioneSemantica");
	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatturaClassificazioneDaIndiceForm currentForm = (CatturaClassificazioneDaIndiceForm) form;

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaClasse().getCodSistemaClassificazione());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		if (currentForm.getNumBlocco() > currentForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.claNotFound"));

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

		CatSemClassificazioneVO areaDatiPass = new CatSemClassificazioneVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());
		areaDatiPass.setLivelloPolo(currentForm.getRicercaComune().isPolo());
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		CatSemClassificazioneVO areaDatiPassReturn = (CatSemClassificazioneVO) factory
				.getGestioneSemantica().getNextBloccoClassi(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.claNotFound"));

			resetToken(request);
			return mapping.getInputForward();
		}

		currentForm.setIdLista(areaDatiPassReturn.getIdLista());
		currentForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = currentForm.getNumBlocco();
		appoggio = currentForm.getAppoggio();
		appoggio.add(numBlocco);
		currentForm.setAppoggio(appoggio);

		currentForm.setNumBlocco(++numBlocco);

		currentForm.getOutputLista().getListaClassi().addAll(
				areaDatiPassReturn.getListaClassi());
		Collections.sort(currentForm.getOutputLista().getListaClassi(),
				SinteticaClasseVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();

	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	private CreaVariaClasseVO eseguiImportazioneClasse(
			CreaVariaClasseVO richiesta, HttpServletRequest request,
			ActionMapping mapping, ActionForm form) throws Exception {
		CatturaClassificazioneDaIndiceForm currentForm = (CatturaClassificazioneDaIndiceForm) form;
		if (richiesta.getDescrizione().equals(" ")) {
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// verifico che la notazione non esista già sulla b.i. di polo,
			// in Indice potrebbe essere stato variato il testo di una notazione
			// già precedentemente importata sulla b.i. di polo

			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			RicercaClassiVO ricerca1 = new RicercaClassiVO();
			ricerca1.setCodSistemaClassificazione(richiesta.getCodSistemaClassificazione());
			ricerca1.setCodEdizioneDewey(richiesta.getCodEdizioneDewey());
			ricerca1.setIdentificativoClasse(richiesta.getT001());
			ricerca1.setOrdinamentoClasse(TipoOrdinamentoClasse.PER_TESTO);
			ricerca1.setPolo(true);

			try {
				delegate.eseguiRicerca(ricerca1, mapping);
				RicercaClasseListaVO lista1 = (RicercaClasseListaVO) request.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);

				if (lista1.getRisultati().size() > 1) {
					// errore troppe classificazioni
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruenzaCla"));
					// nessun codice selezionato
					return null;
				}

				if (lista1.getRisultati().size() == 1) {
					// 1 simile trovato

					CreaVariaClasseVO analitica1 = factory
							.getGestioneSemantica().analiticaClasse(
									true,
									lista1.getRisultati()
											.get(0).getIdentificativoClasse(),
									utenteCollegato.getTicket());
					if (analitica1.isEsitoOk() ) {
						currentForm.setT005(analitica1.getT005());
					} else {
						LinkableTagUtils.addError(request, new ActionMessage(
								"errors.gestioneSemantica.incongruo",
								analitica1.getTestoEsito()));
						return null;
					}

					currentForm.setEsiste(true);

				} else
					currentForm.setEsiste(false);
				// la notazione è già presente con quell'identificativo sulla
				// b.i. di polo

				CreaVariaClasseVO classe = new CreaVariaClasseVO();
				SimboloDeweyVO sd = SimboloDeweyVO.parse(richiesta.getT001());
				richiesta.setSimbolo(sd.getSimbolo());

				if (currentForm.isEsiste()) {
					currentForm.setEsiste(false);
					richiesta.setT005(currentForm.getT005());
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.KoImportaCla"));
					return null;
				} else {
					richiesta.setCodSistemaClassificazione(currentForm
							.getRicercaClasse().getCodSistemaClassificazione());
					richiesta.setCodEdizioneDewey(currentForm
							.getRicercaClasse().getCodEdizioneDewey());
					if (currentForm.getDescrizione() == null) {
						richiesta.setDescrizione(" ");
					} else {
						richiesta.setDescrizione(currentForm.getDescrizione()
								.trim());
					}
					richiesta.setUlterioreTermine("");
					richiesta.setLivello(currentForm.getLivContr());
					richiesta.setLivelloPolo(true);
					// catturo notazione
					classe = factory.getGestioneSemantica().catturaClasse(
							richiesta, utenteCollegato.getTicket());
					currentForm.setEsiste(false);

				}
				if (!classe.isEsitoOk() ) {
					LinkableTagUtils.addError(request,
							new ActionMessage("errors.gestioneSemantica.incongruo", classe.getTestoEsito()));
					return null;
				}
				return classe;

			} catch (ValidationException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return null;

			} catch (DataException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return null;

			} catch (InfrastructureException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return null;

			} catch (Exception e) {
				LinkableTagUtils.addError(request,
						new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
				log.error("", e);
				return null;
			}
		} else {
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			RicercaClassiVO ricerca = new RicercaClassiVO();
			ricerca.setCodSistemaClassificazione(richiesta.getCodSistemaClassificazione());
			ricerca.setCodEdizioneDewey(richiesta.getCodEdizioneDewey());
			// ricerca.setParole(richiesta.getDescrizione());
			ricerca.setIdentificativoClasse(richiesta.getT001());
			ricerca.setOrdinamentoClasse(TipoOrdinamentoClasse.PER_TESTO);
			ricerca.setPolo(true);

			try {
				delegate.eseguiRicerca(ricerca, mapping);
				RicercaClasseListaVO lista = (RicercaClasseListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);

				if (lista.getRisultati().size() > 1) {
					// errore troppe classificazioni
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruenzaCla"));
					// nessun codice selezionato
					return null;
				}
				currentForm = (CatturaClassificazioneDaIndiceForm) form;
				if (lista.getRisultati().size() == 1) {
					// 1 simile trovato
					SinteticaClasseVO classeTrovata = lista
							.getRisultati().get(0);
					CreaVariaClasseVO classe1 = new CreaVariaClasseVO();
					classe1.setT001(classeTrovata.getIdentificativoClasse());
					classe1.setEsito(SbnMarcEsitoType.OK.getEsito());
					classe1.setTestoEsito("OK");
					classe1.setCondiviso(true);
					currentForm.setEsiste(true);

					return classe1;
				}
				UserVO utenteCollegato = Navigation.getInstance(request)
						.getUtente();
				// non ho trovato nulla, tento la creazione in polo
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				// verifico che la notazione non esista già sulla b.i. di polo,
				// in Indice potrebbe essere stato variato il testo di una
				// notazione
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
					// errore troppe classificazioni
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruenzaCla"));
					// nessun codice selezionato
					return null;
				}
				currentForm = (CatturaClassificazioneDaIndiceForm) form;
				if (lista1.getRisultati().size() == 1) {
					// 1 simile trovato

					CreaVariaClasseVO analitica1 = factory
							.getGestioneSemantica().analiticaClasse(
									true,
									lista1.getRisultati()
											.get(0).getIdentificativoClasse(),
									utenteCollegato.getTicket());
					if (analitica1.isEsitoOk() ) {
						currentForm.setT005(analitica1.getT005());
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo",
								analitica1.getTestoEsito()));
						return null;
					}

					currentForm.setEsiste(true);

				} else
					currentForm.setEsiste(false);
				// la notazione è già presente con quell'identificativo sulla
				// b.i.
				// di polo

				CreaVariaClasseVO classe = new CreaVariaClasseVO();

				SimboloDeweyVO sd = SimboloDeweyVO.parse(richiesta.getT001());
				richiesta.setSimbolo(sd.getSimbolo());

				if (currentForm.isEsiste()) {
					currentForm.setEsiste(false);
					richiesta.setT005(currentForm.getT005());
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.KoImportaCla"));
					return null;
				} else {
					richiesta.setCodSistemaClassificazione(sd.getSistema());
					richiesta.setCodEdizioneDewey(sd.getEdizione());
					richiesta.setDescrizione(currentForm.getDescrizione());
					richiesta.setUlterioreTermine("");
					richiesta.setLivello(currentForm.getLivContr());
					richiesta.setLivelloPolo(true);
					// importo notazione
					classe = factory.getGestioneSemantica().importaClasse(
							richiesta, utenteCollegato.getTicket());
					currentForm.setEsiste(false);

				}
				if (!classe.isEsitoOk() ) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", classe
									.getTestoEsito()));
					return null;
				}
				return classe;

			} catch (ValidationException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				// nessun codice selezionato
				return null;

			} catch (DataException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return null;

			} catch (InfrastructureException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return null;

			} catch (Exception e) {
				LinkableTagUtils.addError(request,
						new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
				log.error("", e);
				return null;
			}
		}

	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		try {
			if (idCheck.equals("CATTURA")) {
				ClassiDelegate delegate = ClassiDelegate.getInstance(request);
				delegate.getEJBUtente().isAbilitatoLegameTitoloAuthority("CL");
				//return delegate.isAbilitato(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);
				delegate.getEJBUtente().checkAttivita(CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003);
				return true;
			}
			return false;

		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

}
