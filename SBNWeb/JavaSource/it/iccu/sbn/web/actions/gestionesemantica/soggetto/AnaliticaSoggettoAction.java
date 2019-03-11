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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.DettaglioOggettoSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaLegameSoggettoDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiLegameDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.ModalitaConfermaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoParoleVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaFormTypes;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.AnaliticaSoggettoForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate.RicercaSoggettoResult;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

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
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class AnaliticaSoggettoAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(AnaliticaSoggettoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.crea", "crea");
		map.put("button.inserisci", "inserisci");
		map.put("button.modifica", "modifica");

		map.put("button.gestione", "gestione");
		map.put("button.elimina", "cancellaSoggetto");
		map.put("button.importa", "importa");
		map.put("button.stampa", "stampa");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.polo", "polo");
		map.put("button.biblio", "biblio");
		map.put("button.indice", "indice");
		map.put("button.indice.filtro", "indiceFiltro");

		map.put("button.elemPrec", "elementoPrecedente");
		map.put("button.elemSucc", "elementoSuccessivo");
		map.put("button.soggetti", "soggetti");
		map.put("button.annulla", "indietro");
		map.put("button.ok", "ok");

		map.put("button.invioIndice", "inviaIndice");
		map.put("button.scambia", "scambia");

		map.put("button.conferma", "conferma");
		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");

		return map;
	}

	private TreeElementViewSoggetti getNodoSelezionato(
			HttpServletRequest request, ActionForm form) {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		String codSelezionato = currentForm.getNodoSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return null;
		}

		try {
			return (TreeElementViewSoggetti) currentForm
					.getTreeElementViewSoggetti().findElementUnique(
							Integer.valueOf(codSelezionato));

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return null;
		}
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private boolean initCombo(String ticket, ActionForm form,
			HttpServletRequest request) {
		try {
			AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
			if (currentForm.isSessione())
				return true;

			currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, false));
			currentForm.setListaTipoSoggetto(CaricamentoComboSemantica.loadComboCategoriaSoggetto(SbnAuthority.SO));
			currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));
			//almaviva5_20111123 evolutive CFI
			currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
			return true;

		} catch (Exception ex) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
			return false;
		}
	}

	private void ricaricaReticolo(String xid, String tipo, ActionForm form,
			HttpServletRequest request) throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AnaliticaSoggettoVO analitica = null;
		try {
			analitica = factory.getGestioneSemantica()
					.creaAnaliticaSoggettoPerCid(
							currentForm.getRicercaComune().isPolo(), xid,
							utenteCollegato.getTicket());

			if (analitica.isEsitoOk()) {
				TreeElementViewSoggetti reticolo = analitica.getReticolo();
				((AnaliticaSoggettoForm) form)
						.setTreeElementViewSoggetti(reticolo);
				request.setAttribute(NavigazioneSemantica.ANALITICA, reticolo);

				String livelloAut = reticolo.getLivelloAutorita();
				String categoriaSoggetto = reticolo.getCategoriaSoggetto();
				currentForm.setLivelloContr(livelloAut);
				DettaglioSoggettoVO dettaglio = (DettaglioSoggettoVO) reticolo.getDettaglio();
				request.setAttribute(
						NavigazioneSemantica.TITOLI_COLLEGATI_POLO, dettaglio
								.getNumTitoliPolo());
				request.setAttribute(
						NavigazioneSemantica.TITOLI_COLLEGATI_BIBLIO, dettaglio
								.getNumTitoliBiblio());
				currentForm.setCodCategoriaSoggetto(categoriaSoggetto);

				String dataInserimento = analitica.getDataInserimento();
				String dataVariazione = analitica.getDataVariazione();
				currentForm.setDataInserimento(dataInserimento);
				currentForm.setDataVariazione(dataVariazione);
				String descr = CodiciProvider.getDescrizioneCodiceUNIMARC(
						CodiciType.CODICE_LIVELLO_AUTORITA, livelloAut);
				currentForm.setLivContr(descr);
				currentForm.setDataInserimento(dataInserimento);
				currentForm.setDataVariazione(dataVariazione);
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

	private boolean mostraReticolo(ActionForm form, HttpServletRequest request)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;

		String parameter = request.getParameter(TreeElementView.TREEVIEW_KEY_PARAM);
		if (parameter == null)
			return false;

		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		TreeElementViewSoggetti nodo = (TreeElementViewSoggetti) root
				.findElementUnique(Integer.valueOf(parameter));

		Navigation.getInstance(request).getCache().getCurrentElement()
				.setAnchorId(LinkableTagUtils.ANCHOR_PREFIX + parameter);

		if (nodo == null)
			return false;

		// nodo aperto
		if (nodo.isOpened()) {
			nodo.close();
			return true;
		}

		if (nodo.isDescrittore() && !nodo.isExplored()) {

			// nodo chiuso
			AnaliticaSoggettoVO analitica = this.caricaSottoReticolo(nodo
					.getKey(), NavigazioneSemantica.TIPO_OGGETTO_DID, form,
					request, root.findMaxElement());

			if (analitica == null)	// errore nell'invocazione ejb
				return false;

			if (!analitica.isEsitoOk())
				return false;

			nodo.adoptChildren(analitica.getReticolo());
			nodo.setRadioVisible(true);
			nodo.open();

		} else {
			nodo.setRadioVisible(true);
			nodo.open();

		}

		request.setAttribute(NavigazioneSemantica.ANALITICA, root);
		currentForm.setTreeElementViewSoggetti(root);
		currentForm.setNodoSelezionato(parameter);

		currentForm.setComboGestione(LabelGestioneSemantica
				.getComboGestioneSematica(servlet.getServletContext(),
						request, form, (TreeElementViewSoggetti) root
								.findElementUnique(Integer.valueOf(currentForm.getNodoSelezionato())), this));

		return true;
	}

	private AnaliticaSoggettoVO caricaSottoReticolo(String xid, String tipo,
			ActionForm form, HttpServletRequest request, int startIndex)
			throws Exception {

		ActionMessages errors = new ActionMessages();

		if (tipo.equals(NavigazioneSemantica.TIPO_OGGETTO_DID)) {
			String did = xid;
			// chiamata a EJB con tipo oggetto padre a CID
			// currentForm.setElementiReticolo(CaricamentoComboSemantica.emulazioneReticolo());
			UserVO utenteCollegato = Navigation.getInstance(request)
					.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			AnaliticaSoggettoVO analitica = null;
			try {
				analitica = factory.getGestioneSemantica()
						.creaAnaliticaSoggettoPerDid(
								((AnaliticaSoggettoForm) form)
										.getRicercaComune().isPolo(), did,
								startIndex, utenteCollegato.getTicket());

				return analitica;

			} catch (ValidationException e) {
				// errori indice
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return null;

			} catch (DataException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return null;
			} catch (InfrastructureException e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return null;
			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.erroreSistema", e
								.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return null;
			}

		}

		return null;
	}


	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		TreeElementViewSoggetti newReticolo = (TreeElementViewSoggetti) request.getAttribute(NavigazioneSemantica.ANALITICA);

		if (newReticolo != null)
			root = newReticolo;

		if (root != null &&	(request.getParameter(mapping.getParameter()) == null) ) {

			currentForm.setTreeElementViewSoggetti(root);
			updateCombo(request, form);
		}

		return super.execute(mapping, form, request, response);
	}

	private void updateCombo(HttpServletRequest request,
			ActionForm form) {
		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		String codSelezionato = currentForm.getNodoSelezionato();

		if (ValidazioneDati.strIsNull(codSelezionato) )
			codSelezionato = String.valueOf(root.getRepeatableId());

		currentForm.setNodoSelezionato(codSelezionato);
		currentForm.setComboGestione(LabelGestioneSemantica
				.getComboGestioneSematica(servlet.getServletContext(),
						request, form, (TreeElementViewSoggetti) root
								.findElementUnique(Integer.valueOf(codSelezionato)), this));

		currentForm.setComboGestioneNonFiltrata(LabelGestioneSemantica
				.getComboGestioneSematica(servlet.getServletContext(),
						request, form, new String[]{"SO", "DE"}, null));

		currentForm.setIdFunzione("");

		currentForm.setComboGestioneEsamina(LabelGestioneSemantica
				.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
						request, form, (TreeElementViewSoggetti) root
								.findElementUnique(Integer.valueOf(codSelezionato)), this));

		currentForm.setComboGestioneEsaminaNonFiltrata(LabelGestioneSemantica
				.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
						request, form, new String[]{"SO", "DE"}, null));

		currentForm.setIdFunzioneEsamina("");
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ElementoSinteticaSoggettoVO cidCorrente = null;

		try {

			log.debug("AnaliticaSoggettoAction - unspecified");
			AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
			Navigation navi = Navigation.getInstance(request);

			currentForm.setVisualCheckCattura(false);
			if (navi.isFromBar() || request.getParameter("FROM_SINT_TITOLI") != null)
				return mapping.getInputForward();

			// caricamento combo
			if (!initCombo(navi.getUserTicket(), form, request))
				return mapping.getInputForward();

			TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
			if (root != null && request.getAttribute(NavigazioneSemantica.ANALITICA) == null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);

			// NELL'IPOTESI IN CUI PROVENGO DA LISTA SINTETICA E HO CHIESTO
			// L'ESAMINA DI UN DETERMINATO CID
			// HO IL TREEELEMENT GIA' CARICATO
			currentForm.setTreeElementViewSoggetti((TreeElementViewSoggetti) request.getAttribute(NavigazioneSemantica.ANALITICA));
			root = currentForm.getTreeElementViewSoggetti();

			if (this.mostraReticolo(form, request)) {
				if (currentForm.getOutputlistaprima() != null)
					request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA_PRIMA, currentForm.getOutputlistaprima());

				updateCombo(request, form);
				return mapping.getInputForward();
			}

			// link su key albero
			String linkTree = request.getParameter(TreeElementView.TREEVIEW_URL_PARAM);
			if (linkTree != null) {
				currentForm.setNodoSelezionato(linkTree);
				return dettaglio(mapping, form, request, response);
			}

			currentForm.setAction((String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER));

			if (root != null) {

				String[] listaCidSelez = (String[]) request
						.getAttribute(NavigazioneSemantica.LISTA_OGGETTI_SELEZIONATI);
				if (listaCidSelez != null) {
					currentForm.setListaCidSelez(listaCidSelez);
					currentForm.setPosizioneCorrente(0);
				}
				if ((RicercaComuneVO) request
						.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA) != null) {
					currentForm.setRicercaComune((RicercaComuneVO) request
							.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
				}

				DettaglioSoggettoVO dettaglioRoot = root.getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO();

				String codice = dettaglioRoot.getCampoSoggettario();

				if (ValidazioneDati.isFilled(codice) )
					currentForm.getRicercaComune().setCodSoggettario(codice);


				String descrizione = (String) request
						.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO);
				if (descrizione != null && descrizione.length() > 0) {
					currentForm.getRicercaComune()
							.setDescSoggettario(
									(String) request
											.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO));
				}

				Object livRicercaPolo = request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO);
				if (livRicercaPolo != null)
					currentForm.getRicercaComune()
							.setPolo(((Boolean) livRicercaPolo).booleanValue());


				currentForm.setEnableIndice(currentForm.getRicercaComune().isIndice());

				String dataInserimento = (String) request
						.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
				currentForm.setDataInserimento(dataInserimento);
				String dataVariazione = (String) request
						.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
				currentForm.setDataVariazione(dataVariazione);

				currentForm.setOutput((RicercaSoggettoListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
				currentForm.setOutputlistaprima((RicercaSoggettoListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA_PRIMA));

				String tipoSoggetto = (String) request
						.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);
				if (ValidazioneDati.isFilled(tipoSoggetto) ) {
					currentForm.setCodCategoriaSoggetto(tipoSoggetto);
					root.setCategoriaSoggetto(tipoSoggetto);
				}

				OggettoRiferimentoVO rifSintetica = ((SemanticaBaseForm)navi.getCallerForm()).getOggettoRiferimento();
				currentForm.setEnableCercaIndice(currentForm.getRicercaComune().isPolo() && !rifSintetica.isEnabled() &&
						ValidazioneDati.strIsNull(currentForm.getAreaDatiPassBiblioSemanticaVO().getBidPartenza()));

				String xid = (String) request.getAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID);
				if (ValidazioneDati.isFilled(xid) )
					this.ricaricaReticolo(xid,
							NavigazioneSemantica.TIPO_OGGETTO_CID,
							currentForm,
							request);


				currentForm.setEnableModifica(true);
				AreaDatiPassBiblioSemanticaVO datiGB = (AreaDatiPassBiblioSemanticaVO) request
						.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
				if (datiGB != null) {
					currentForm.setAreaDatiPassBiblioSemanticaVO(datiGB);
					currentForm.getCatalogazioneSemanticaComune().setBid(datiGB.getBidPartenza());
					currentForm.getCatalogazioneSemanticaComune().setTestoBid(datiGB.getDescPartenza());
					currentForm.setFolder((FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE));

					if (currentForm.getFolder() != null	&& currentForm.getFolder() == FolderType.FOLDER_SOGGETTI) {
						currentForm.setEnableTit(true);
						currentForm.setEnableOk(true);
						currentForm.setEnableCercaIndice(false);
						currentForm.setEnableCrea(false);
						currentForm.setEnableElimina(false);
						currentForm.setEnableEsamina(false);
						currentForm.setEnableGestione(false);
						currentForm.setEnableInserisci(false);
						currentForm.setEnableModifica(false);
						currentForm.setEnableSogColl(false);
						currentForm.setEnableStampa(false);
					}
				}

				if (currentForm.getRicercaComune().isPolo()) {

					currentForm.setNumTitoliPolo(dettaglioRoot.getNumTitoliPolo());
					currentForm.setEnableNumPolo(currentForm.getNumTitoliPolo() > 0);
					currentForm.setNumTitoliBiblio(dettaglioRoot.getNumTitoliBiblio());
					currentForm.setEnableNumBiblio(currentForm.getNumTitoliBiblio() > 0);
				}

				currentForm.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
								.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));
				currentForm.setTitoliBiblio((List<?>) request
								.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));
				currentForm.setCidTrascinaDa((String) request
						.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA));
				currentForm.setTestoTrascinaDa((String) request
								.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));
				if (currentForm.getCidTrascinaDa() != null) {
					currentForm.setEnableCercaIndice(false);
					currentForm.setEnableLegame(false);
					currentForm.setEnableElimina(false);
					currentForm.setEnableEsamina(false);
					currentForm.setEnableSogColl(false);
					currentForm.setEnableGestione(false);
					currentForm.setEnableInserisci(false);
					currentForm.setEnableModifica(false);
					currentForm.setEnableOk(true);
				} else {
					currentForm.setEnableLegame(true);
					currentForm.setEnableElimina(true);
					currentForm.setEnableEsamina(true);
					currentForm.setEnableSogColl(true);
					currentForm.setEnableGestione(true);
					currentForm.setEnableInserisci(true);
					currentForm.setEnableOk(false);
				}
				if (ValidazioneDati.strIsNull(currentForm.getNodoSelezionato()) )
					currentForm.setNodoSelezionato(String.valueOf(root.getRepeatableId()));

				request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
				return mapping.getInputForward();
			}

			if (this.mostraReticolo(form, request))
				return mapping.getInputForward();

			RicercaComuneVO ricSoggRicerca = null;
			boolean isIndice = false;
			FolderType folder = null;
			// String tipo = null;

			String prev = navi.getActionCaller();
			String xid = (String) request.getAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID);
			if (ValidazioneDati.isFilled(xid) ) {
				currentForm.setAction((String) request
						.getAttribute(NavigazioneSemantica.ACTION_CALLER));
				if (prev.toUpperCase().indexOf("CATALOGAZIONESEMANTICA") > -1) {
					currentForm.setRicercaComune((RicercaComuneVO) request
							.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
				}
				String tipo = (String) request
						.getAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE);
				this.ricaricaReticolo(xid, tipo, currentForm, request);
				root = currentForm.getTreeElementViewSoggetti();
				DettaglioSoggettoVO dettaglioRoot = root.getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO();
				currentForm.getRicercaComune().setCodSoggettario(dettaglioRoot.getCampoSoggettario());

				currentForm.setAction((String) request
						.getAttribute(NavigazioneSemantica.ACTION_CALLER));

				if (prev.toUpperCase().indexOf("CATALOGAZIONESEMANTICA") > -1
						&& request.getParameter("FROM_CATTURA") == null) {
					AreaDatiPassBiblioSemanticaVO datiGB = (AreaDatiPassBiblioSemanticaVO) request
							.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
					if (datiGB != null) {
						currentForm.setAreaDatiPassBiblioSemanticaVO(datiGB);
						currentForm.getCatalogazioneSemanticaComune().setBid(
								datiGB
										.getBidPartenza());
						currentForm.getCatalogazioneSemanticaComune().setTestoBid(
								datiGB
										.getDescPartenza());
						currentForm.setFolder((FolderType) request
										.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE));
					}
					currentForm.setEnableOk(false);
					currentForm.setEnableCercaIndice(false);
					currentForm.setEnableModifica(true);
					currentForm.setNumTitoliPolo((Integer) request
									.getAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_POLO));
					if (currentForm.getNumTitoliPolo() == 0) {
						currentForm.setEnableNumPolo(false);
					} else {
						currentForm.setEnableNumPolo(true);
					}
					currentForm.setNumTitoliBiblio((Integer) request
									.getAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_BIBLIO));
					if (currentForm.getNumTitoliBiblio() == 0) {
						currentForm.setEnableNumBiblio(false);
					} else {
						currentForm.setEnableNumBiblio(true);
					}

					ActionMessages errors = currentForm.validate(mapping, request);
					this.setErrors(request, errors, null);
					return mapping.getInputForward();
				}

			}

			if (!currentForm.isSessione()) {
				String chiamante = null;

				ricSoggRicerca = (RicercaComuneVO) request
						.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
				chiamante = (String) request
						.getAttribute(NavigazioneSemantica.ACTION_CALLER);
				folder = (FolderType) request
						.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
				String[] listaCidSelez = (String[]) request
						.getAttribute(NavigazioneSemantica.LISTA_OGGETTI_SELEZIONATI);
				if (listaCidSelez != null) {
					currentForm.setListaCidSelez(listaCidSelez);
					currentForm.setPosizioneCorrente(0);
				}

				currentForm.setNodoSelezionato("0");
				currentForm.setCidTrascinaDa((String) request
						.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA));
				currentForm.setTestoTrascinaDa((String) request
								.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));
				if (currentForm.getCidTrascinaDa() != null) {
					currentForm.setEnableCercaIndice(false);
					currentForm.setEnableElimina(false);
					currentForm.setEnableGestione(false);
					currentForm.setEnableInserisci(false);
					currentForm.setEnableModifica(false);
					currentForm.setEnableOk(true);
				}

				if (chiamante == null) {
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.FunzChiamNonImp"));
					this.setErrors(request, errors, null);
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
					return mapping.getInputForward();
				}

				currentForm.setSessione(true);
				currentForm.setAction(chiamante);

				if (request.getParameter("FROM_CATTURA") != null) {
					currentForm.setEnableIndice(true);
					currentForm.getRicercaComune().setPolo(false);

					currentForm.setNodoSelezionato(String.valueOf(root.getRepeatableId()));
					return mapping.getInputForward();
				}

				if (request.getAttribute(NavigazioneSemantica.ANALITICA) != null
						&& request
								.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA) == null) {
					currentForm.setTreeElementViewSoggetti((TreeElementViewSoggetti) request.getAttribute(NavigazioneSemantica.ANALITICA));
					return mapping.getInputForward();
				}

				int progr;

				try {
					progr = Integer
							.parseInt((String) request
									.getAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO)) - 1;
				} catch (Exception e) {
					progr = 0;
				}
				cidCorrente = (ElementoSinteticaSoggettoVO) ((RicercaSoggettoListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA))
						.getRisultati().get(progr);
				currentForm.setTesto(cidCorrente.getTesto());
				currentForm.setCid(cidCorrente.getCid());
				currentForm.setRicercaComune(ricSoggRicerca);
				currentForm.setDescrizione(ricSoggRicerca.getDescSoggettario());
				currentForm.setLivContr(cidCorrente.getStato());
				currentForm.setLegatoTit(cidCorrente.getIndicatore());
				// currentForm.setCategoriaSoggetto(cidCorrente.getCategoriaSoggetto());
				currentForm.setEnableIndice(isIndice);
				// qui carico il reticolo iniziale con il CID
				this.ricaricaReticolo(cidCorrente.getCid(),
						NavigazioneSemantica.TIPO_OGGETTO_CID, currentForm, request);
				currentForm.setEnableModifica(true);

				OggettoRiferimentoVO rifSintetica = ((SemanticaBaseForm)navi.getCallerForm()).getOggettoRiferimento();
				currentForm.setEnableCercaIndice(currentForm.getRicercaComune().isPolo() && !rifSintetica.isEnabled() &&
					ValidazioneDati.strIsNull(currentForm.getAreaDatiPassBiblioSemanticaVO().getBidPartenza()));

			} else {
				// Posso cercare in indice solo se è la prima volta che carico
				// il reticolo

				currentForm.setEnableCercaIndice(false);
				currentForm.setCidTrascinaDa((String) request
						.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA));
				if (currentForm.getCidTrascinaDa() == null) {
					currentForm.setEnableElimina(true);
					currentForm.setEnableGestione(true);
					currentForm.setEnableInserisci(true);
					currentForm.setEnableOk(false);
				}
			}
			currentForm.setCid(cidCorrente.getCid());
			request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, currentForm.getCid());
			ActionMessages errors = currentForm.validate(mapping, request);

			this.setErrors(request, errors, null);
			this.saveToken(request);

			currentForm.setOutput((RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
			currentForm.setOutputlistaprima((RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA_PRIMA));
			AreaDatiPassBiblioSemanticaVO datiGB = (AreaDatiPassBiblioSemanticaVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
			if (datiGB != null) {
				currentForm.setAreaDatiPassBiblioSemanticaVO(datiGB);
				currentForm.getCatalogazioneSemanticaComune()
						.setBid(datiGB.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(
						datiGB.getDescPartenza());
				currentForm.setFolder(folder);
			}
			if (currentForm.getFolder() != null
					&& currentForm.getFolder() == FolderType.FOLDER_SOGGETTI) {
				currentForm.setEnableTit(true);
				currentForm.setEnableCercaIndice(false);
				currentForm.setEnableIndice(false);
				currentForm.setEnableOk(true);
				currentForm.setEnableCrea(false);
				currentForm.setEnableElimina(false);
				currentForm.setEnableEsamina(false);
				currentForm.setEnableGestione(false);
				currentForm.setEnableInserisci(false);
				currentForm.setEnableModifica(false);
				currentForm.setEnableSogColl(false);
				currentForm.setEnableStampa(false);
			}

			currentForm.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
				.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));
			currentForm.setTitoliBiblio((List<?>) request
				.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));

			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();

		if (!ValidazioneDati.isFilled(currentForm.getNodoSelezionato()) ) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}

		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

		if ((currentForm.getCatalogazioneSemanticaComune().getBid() != null)
				|| currentForm.getCidTrascinaDa() != null) {

			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
					new Boolean(currentForm.getRicercaComune().isPolo()));
			request.setAttribute(
					NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
					currentForm.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
			request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, nodoSelezionato.getKey() );
			request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO,
					nodoSelezionato.getCategoriaSoggetto());
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA,
					nodoSelezionato.getLivelloAutorita());
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
					currentForm.getRicercaComune().clone() );
			request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
					currentForm.getRicercaComune().getCodSoggettario());
			request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
					currentForm.getRicercaComune().getDescSoggettario());
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataVariazione());
			request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
					nodoSelezionato.getTesto());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO,
					nodoSelezionato.getTesto());
			if (currentForm.getTitoliBiblio() != null) {
				request.setAttribute(
						NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());

				request.setAttribute(NavigazioneSemantica.TRASCINA_CID_ARRIVO, nodoSelezionato.getKey() );
				request.setAttribute(
						NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm.getTestoTrascinaDa());
				request.setAttribute(
						NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm.getCidTrascinaDa());
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
						mapping.getPath());
				request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA,
						currentForm.getOutput());
				request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
						currentForm.getDatiBibliografici());
				request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
						currentForm.getRicercaComune().clone() );
				return Navigation.getInstance(request).goForward(
						mapping.findForward("ok"));
			}
			return Navigation.getInstance(request).goForward(
					mapping.findForward("creaLegameSoggettoTitolo"));
		}

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (nodoSelezionato.isSoggetto()) {
			// // ho selezionato un CID
			request.setAttribute(TitoliCollegatiInvoke.codBiblio, currentForm.getBiblioteca());
			request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
					TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
			// per quanto riguarda il cid è quello della mappa
			request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
					currentForm.getCidTrascinaDa());
			request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc,
					currentForm.getTestoTrascinaDa());
			request.setAttribute(TitoliCollegatiInvoke.visualCall, "NO");
			// Viene settato il token per le transazioni successive
			this.saveToken(request);
			request.setAttribute(TitoliCollegatiInvoke.oggChiamante,
					mapping.getPath());
			request
					.setAttribute(
							TitoliCollegatiInvoke.oggDiRicerca,
							new Integer(
									TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
			return Navigation.getInstance(request).goForward(
					mapping.findForward("titoliCollegatiBiblio"));

		} else {
			if (nodoSelezionato.isDescrittore()) {
				// ho selezionato un DID
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();

			} else {
				// Errore da gestire
				return mapping.getInputForward();

			}

		}

	}

	public ActionForward simili(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();

		DettaglioSoggettoVO dettaglio =
			(DettaglioSoggettoVO) currentForm.getTreeElementViewSoggetti().getDettaglio();

		RicercaComuneVO ricercaComune = new RicercaComuneVO();
		String testo = dettaglio.getTesto();
		if (testo.length() > 80)
			testo = testo.substring(0, 80);	// max. 80 caratteri in ricerca
		ricercaComune.getRicercaSoggetto().setTestoSogg(testo);
		ricercaComune.setCodSoggettario(dettaglio.getCampoSoggettario() );
		ricercaComune.setRicercaDescrittore(null);
		ricercaComune.setPolo(false);

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(ricercaComune.isPolo()));

		try {

			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			delegate.eseguiRicerca(ricercaComune, mapping);
			RicercaSoggettoResult op = delegate.getOperazione();
			switch (op) {
			case analitica_1:// SoggettiDelegate.analitica:
				return Navigation.getInstance(request).goForward(mapping.findForward("listasintetica"));
			case crea_4:// SoggettiDelegate.crea:
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
						mapping.getPath());
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			case sintetica_3:// SoggettiDelegate.sintetica :
				return mapping.getInputForward();

			case lista_2:// SoggettiDelegate.lista soggetti:
				request.setAttribute(
						NavigazioneSemantica.DESCRITTORI_DEL_SOGGETTO, ricercaComune.getRicercaSoggetto());
				if (ricercaComune.isIndice())
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.livelloRicercaIndice"));
				else
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.livelloRicercaPolo"));

				this.saveMessages(request, errors);
				return Navigation.getInstance(request).goForward(mapping.findForward("listasintetica"));
			case diagnostico_0:// SoggettiDelegate.diagnostico:
				RicercaSoggettoListaVO output = delegate.getOutput();
				if (!output.isEsitoNonTrovato()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", output
									.getTestoEsito()));
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

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ElementoSinteticaSoggettoVO cidCorrente = null;
		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();
		RicercaComuneVO ricercaComune = currentForm.getRicercaComune().copy();
		ricercaComune.setPolo(false);

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(ricercaComune.isPolo()));
		// Verifico se la ricerca è stata impostata in input per descrittore e
		// in tal caso invio la mappa appropriata
		if (ricercaComune.getRicercaSoggetto().count() > 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.indice"));
			this.setErrors(request, errors, null);
			RicercaSoggettoDescrittoriVO tmp = (RicercaSoggettoDescrittoriVO) ricercaComune.getRicercaSoggetto();
			RicercaSoggettoParoleVO parole = new RicercaSoggettoParoleVO(tmp
					.getTestoSogg(), tmp.getCid(), tmp.getDescrittoriSogg(),
					tmp.getDescrittoriSogg1(), tmp.getDescrittoriSogg2(), tmp
							.getDescrittoriSogg3(), "", "");
			request.setAttribute(
					NavigazioneSemantica.RICERCA_SOGGETTI_PER_PAROLE, parole);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("cercaIndicePerParole"));
		}

		try {

			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			delegate.eseguiRicerca(ricercaComune, mapping);
			RicercaSoggettoResult op = delegate.getOperazione();
			switch (op) {
			case analitica_1:// SoggettiDelegate.analitica:

				cidCorrente = (ElementoSinteticaSoggettoVO) ((RicercaSoggettoListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA))
						.getRisultati().get(0);
				String cid = cidCorrente.getCid();
				this.ricaricaReticolo(cid,
								NavigazioneSemantica.TIPO_OGGETTO_CID, currentForm,
								request);
				currentForm.setEnableIndice(true);
				return mapping.getInputForward();
			case crea_4:// SoggettiDelegate.crea:
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
						mapping.getPath());
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.setErrors(request, errors, null);
				return mapping.getInputForward();
			case sintetica_3:// SoggettiDelegate.sintetica :
				currentForm.setEnableIndice(true);
				if (ricercaComune.getOperazione() instanceof RicercaDescrittoreVO) {
					if (ricercaComune.getRicercaDescrittore()
							.getTestoDescr() != null
							&& ricercaComune
									.getRicercaDescrittore().getTestoDescr()
									.length() > 0)
						return Navigation.getInstance(request).goForward(
								mapping.findForward("sinteticadescrittori"));
					else
						request.setAttribute(NavigazioneSemantica.PAROLE,
								ricercaComune
										.getRicercaDescrittore());
					return mapping.findForward("sinteticadescrittoriparole");
				} else
					request.setAttribute(
							NavigazioneSemantica.DESCRITTORI_DEL_SOGGETTO,
							ricercaComune.getRicercaSoggetto());
				return Navigation.getInstance(request).goForward(
						mapping.findForward("sinteticadescrittorisoggetto"));

			case lista_2:// SoggettiDelegate.lista soggetti:
				request.setAttribute(
						NavigazioneSemantica.DESCRITTORI_DEL_SOGGETTO, ricercaComune.getRicercaSoggetto());
				if (ricercaComune.isIndice())
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.livelloRicercaIndice"));
				else
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.livelloRicercaPolo"));

				this.saveMessages(request, errors);
				currentForm.setEnableIndice(true);
				return Navigation.getInstance(request).goBack(mapping.findForward("listasintetica"));
			case diagnostico_0:// SoggettiDelegate.diagnostico:
				RicercaSoggettoListaVO output = delegate.getOutput();
				if (!output.isEsitoNonTrovato()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", output
									.getTestoEsito()));
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

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		RicercaComuneVO ricerca = currentForm.getRicercaComune();
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, ricerca.getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.EDIZIONE_SOGGETTARIO, ricerca.getEdizioneSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, ricerca.getDescSoggettario());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	new Boolean(ricerca.isPolo()));
		request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutput());

		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm.getTreeElementViewSoggetti().getCategoriaSoggetto());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm.getTreeElementViewSoggetti().getLivelloAutorita());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataVariazione());
		if (currentForm.getCidTrascinaDa() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA,
					currentForm.getCidTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
					currentForm.getTestoTrascinaDa().trim());
			request.setAttribute(
					NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
					currentForm.getDatiBibliografici());
		}

		return Navigation.getInstance(request).goForward(
				mapping.findForward("creaSoggetto"));
	}

	public ActionForward importa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataVariazione());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(currentForm.getRicercaComune().isPolo()));
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutput());
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			// MANCANO LE NOTE AL SOGGETTO
			if (nodoSelezionato.isSoggetto() ) {
				// ho selezionato un CID
				DettaglioSoggettoVO dettSogGenVO =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglio();
				request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO,
						dettSogGenVO);
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
				resetToken(request);
				if (currentForm.getRicercaComune().isIndice())
					currentForm.setEnableEsamina(true);

				return Navigation.getInstance(request).goForward(
						mapping.findForward("importaSoggetto"));

			} else {
				if (nodoSelezionato.isDescrittore() ) {
					// ho selezionato un DID
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.importasog"));
					this.setErrors(request, errors, null);
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					return mapping.getInputForward();

				} else {
					// Errore da gestire
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
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}

	}

	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;

		if (ValidazioneDati.isFilled(currentForm.getNodoSelezionato()) ) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			if (nodoSelezionato.isSoggetto() ) {
				currentForm.setCid(nodoSelezionato.getKey() );
				currentForm.setTesto(nodoSelezionato.getDettaglio().getTesto());
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
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
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("titoliCollegatiFiltro"));
	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		ActionMessages errors = new ActionMessages();
		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;

		if (ValidazioneDati.isFilled(currentForm.getNodoSelezionato()) ) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			if (nodoSelezionato.isSoggetto() ) {

				DettaglioSoggettoVO dettaglioSog =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglio();
				currentForm.setCid(dettaglioSog.getCid() );
				currentForm.setTesto(dettaglioSog.getTesto() );
			} else {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
			}
		}

		else {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		// + ".do?FROM_SINT_TITOLI=Y");
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));

		return navi.goForward(mapping.findForward("titoliCollegati"));
	}


	public ActionForward biblioFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		if (ValidazioneDati.isFilled(currentForm.getNodoSelezionato()) ) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			if (nodoSelezionato.isSoggetto() ) {

				DettaglioSoggettoVO dettaglioSog =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglio();
				currentForm.setCid(dettaglioSog.getCid() );
				currentForm.setTesto(dettaglioSog.getTesto() );

			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
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
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, currentForm.getBiblioteca());
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("titoliCollegatiFiltro"));

	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		if (ValidazioneDati.isFilled(currentForm.getNodoSelezionato()) ) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			if (nodoSelezionato.isSoggetto() ) {

				DettaglioSoggettoVO dettaglioSog =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglio();
				currentForm.setCid(dettaglioSog.getCid() );
				currentForm.setTesto(dettaglioSog.getTesto() );

			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
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
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, utenteCollegato.getCodBib() );
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("titoliCollegati"));

	}



	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		if (ValidazioneDati.isFilled(currentForm.getNodoSelezionato()) ) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			if (nodoSelezionato.isSoggetto() ) {

				DettaglioSoggettoVO dettaglioSog =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglio();
				currentForm.setCid(dettaglioSog.getCid() );
				currentForm.setTesto(dettaglioSog.getTesto() );

			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
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
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("titoliCollegati"));
	}

	public ActionForward indiceFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		if (ValidazioneDati.isFilled(currentForm.getNodoSelezionato()) ) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			if (nodoSelezionato.isSoggetto() ) {

				DettaglioSoggettoVO dettaglioSog =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglio();
				currentForm.setCid(dettaglioSog.getCid() );
				currentForm.setTesto(dettaglioSog.getTesto() );

			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
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
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("titoliCollegatiFiltro"));
	}

	public ActionForward dettaglio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		currentForm.setEnableConferma(false);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataVariazione());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (ValidazioneDati.isFilled(currentForm.getNodoSelezionato())) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			// MANCANO LE NOTE AL SOGGETTO
			if (nodoSelezionato.isSoggetto() ) {
				// ho selezionato un CID
				DettaglioSoggettoVO dettSogGenVO =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglio();

				resetToken(request);
				if (currentForm.getRicercaComune().isIndice())
					currentForm.setEnableEsamina(true);

				request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO,
						dettSogGenVO);
				request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
						currentForm.getDataInserimento());
				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
						currentForm.getDataVariazione());
				return Navigation.getInstance(request).goForward(
						mapping.findForward("esaminaSoggetto"));

			} else {
				if (nodoSelezionato.isDescrittore() ) {
					// ho selezionato un DID
					SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
					// devo verificare se é automatico per altri soggetti
					currentForm.setEnableManuale(!delegate.isDescrittoreAutomaticoPerAltriSoggetti(nodoSelezionato.getKey()));

					currentForm.setCid(currentForm.getTreeElementViewSoggetti()
							.getKey());
					request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO,
							currentForm.getCid());
					request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO,
							currentForm.getCodCategoriaSoggetto());
					request.setAttribute(NavigazioneSemantica.TESTO_SOGGETTO,
							currentForm.getTreeElementViewSoggetti().getTesto());
					DettaglioDescrittoreVO dettDesGenVO = nodoSelezionato
							.getAreaDatiDettaglioOggettiVO()
							.getDettaglioDescrittoreGeneraleVO();
					dettDesGenVO.setCampoSoggettario(currentForm.getRicercaComune()
							.getCodSoggettario());
					dettDesGenVO.setDid(nodoSelezionato.getKey() );
					dettDesGenVO
							.setLivAut(nodoSelezionato.getLivelloAutorita());
					dettDesGenVO
							.setTesto(nodoSelezionato.getTesto());
					dettDesGenVO.setT005(nodoSelezionato.getT005());
					dettDesGenVO.setFormaNome(nodoSelezionato.getFormaNome());

					currentForm.setEnableAnaSog(true);
					request.setAttribute(
							NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
							currentForm.isEnableAnaSog());

					resetToken(request);
					request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
							dettDesGenVO.getDataAgg() );
					request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
							dettDesGenVO.getDataIns() );
					request.setAttribute(
							NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
							dettDesGenVO);
					currentForm.setDettDesGenVO(dettDesGenVO);
					request.setAttribute(
							NavigazioneSemantica.DESCRITTORE_MANUALE, currentForm.isEnableManuale());
					request.setAttribute(
							NavigazioneSemantica.DATA_MODIFICA_T005,
							nodoSelezionato.getT005());

					if (!nodoSelezionato.getDatiLegame().equals(
							SbnLegameAut.valueOf("931"))) {
						UserVO utenteCollegato = Navigation
								.getInstance(request).getUtente();
						FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

						AnaliticaSoggettoVO analitica = null;
						analitica = factory.getGestioneSemantica()
								.creaAnaliticaSoggettoPerDid(
										((AnaliticaSoggettoForm) form)
												.getRicercaComune().isPolo(),
										nodoSelezionato.getKey(), 0,
										utenteCollegato.getTicket());
						if (analitica.isEsitoOk()) {

							String dataVar1 = SBNMarcUtil
									.converteDataVariazione(analitica
											.getReticolo().getT005());
							request.setAttribute(
									NavigazioneSemantica.DATA_MODIFICA,
									dataVar1);
							dettDesGenVO.setDataAgg(dataVar1);
							request.setAttribute(
									NavigazioneSemantica.DATA_INSERIMENTO,
									analitica.getDataInserimento());
							request.setAttribute(
									NavigazioneSemantica.DATA_MODIFICA_T005,
									analitica.getReticolo().getT005());
							dettDesGenVO = analitica.getReticolo()
									.getAreaDatiDettaglioOggettiVO()
									.getDettaglioDescrittoreGeneraleVO();
							request.setAttribute(
									NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
									dettDesGenVO);
						}
					}
					return Navigation.getInstance(request).goForward(
							mapping.findForward("esaminaDescrittore"));

				} else {
					// Errore da gestire
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
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}

	}

	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		ActionMessages errors = new ActionMessages();
		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm
				.getDataVariazione());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		// //////////DEVO GESTIRE LE NOTE SIA DI SOGGETTO CHE DI
		// DESCRITTORE?????????????????????
		if (ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

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

		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);

		if (nodoSelezionato.isSoggetto()) {

			DettaglioSoggettoVO dettaglioSog = (DettaglioSoggettoVO) nodoSelezionato.getDettaglio();
			if (!delegate.isLivAutOkSO(dettaglioSog.getLivAut(), true))
				return mapping.getInputForward();

			request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO,
					dettaglioSog);
			resetToken(request);
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
					currentForm.getDataInserimento());
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
					currentForm.getDataVariazione());
			return Navigation.getInstance(request).goForward(
					mapping.findForward("gestioneSoggetto"));

		} else {
			if (nodoSelezionato.isDescrittore()) {

				if (!nodoSelezionato.isLivelloPolo()) { // non funz. in indice

					errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.gestioneSemantica.noFunz"));
					this.setErrors(request, errors, null);
					// nessun codice selezionato
					if (currentForm.getTreeElementViewSoggetti() != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								currentForm.getTreeElementViewSoggetti());
					return mapping.getInputForward();
				}

				// ho selezionato un DID
				boolean condiviso = nodoSelezionato
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioDescrittoreGeneraleVO().isCondiviso();

				// devo verificare se é automatico per altri soggetti
				currentForm.setEnableManuale(!delegate.isDescrittoreAutomaticoPerAltriSoggetti(nodoSelezionato.getKey()));

				currentForm.setCid(currentForm.getTreeElementViewSoggetti()
						.getKey());
				request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO,
						currentForm.getTreeElementViewSoggetti().getKey());
				request.setAttribute(NavigazioneSemantica.TESTO_SOGGETTO,
						currentForm.getTreeElementViewSoggetti().getTesto());
				request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO,
						currentForm.getTreeElementViewSoggetti()
								.getCategoriaSoggetto());
				request.setAttribute(NavigazioneSemantica.DESCRITTORE_MANUALE,
						currentForm.isEnableManuale());

				DettaglioDescrittoreVO dettDesGenVO = (DettaglioDescrittoreVO) nodoSelezionato
						.getDettaglio();

				if (!delegate.isLivAutOkDE(dettDesGenVO.getLivAut(), true))
					return mapping.getInputForward();

				currentForm.setEnableAnaSog(true);
				request.setAttribute(
						NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
						currentForm.isEnableAnaSog());
				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
						dettDesGenVO.getDataAgg() );
				request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
						dettDesGenVO.getDataIns() );
				request.setAttribute(
						NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
						dettDesGenVO);
				currentForm.setDettDesGenVO(dettDesGenVO);
				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_T005,
						nodoSelezionato.getT005());
				if (!nodoSelezionato.getDatiLegame().equals(
						SbnLegameAut.valueOf("931"))) {
					UserVO utenteCollegato = Navigation.getInstance(request)
							.getUtente();
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

					AnaliticaSoggettoVO analitica = null;
					analitica = factory.getGestioneSemantica()
							.creaAnaliticaSoggettoPerDid(
									((AnaliticaSoggettoForm) form)
											.getRicercaComune().isPolo(),
									nodoSelezionato.getKey(), 0,
									utenteCollegato.getTicket());
					if (analitica.isEsitoOk()) {

						String dataVar1 = SBNMarcUtil
								.converteDataVariazione(analitica.getReticolo().getT005());
						request.setAttribute(
								NavigazioneSemantica.DATA_MODIFICA, dataVar1);
						dettDesGenVO.setDataAgg(dataVar1);
						request.setAttribute(
								NavigazioneSemantica.DATA_INSERIMENTO,
								analitica.getDataInserimento());
						request.setAttribute(
								NavigazioneSemantica.DATA_MODIFICA_T005,
								analitica.getReticolo().getT005());
						condiviso = analitica.getReticolo()
								.getAreaDatiDettaglioOggettiVO()
								.getDettaglioDescrittoreGeneraleVO()
								.isCondiviso();
					}
				}
				request.setAttribute(
						NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE,
						condiviso);

				return Navigation.getInstance(request).goForward(
						mapping.findForward("gestioneDescrittore"));

			} else {
				// Errore da gestire
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();
			}
		}
	}





	public ActionForward inserisci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataVariazione());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		request.setAttribute(NavigazioneSemantica.ANALITICA, root);
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, currentForm.getRicercaComune().getDescSoggettario());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
		// Viene settato il token per le transazioni successive
		currentForm.setCid(root.getKey() );
		this.saveToken(request);

		if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			if (nodoSelezionato.isSoggetto() ) {
				// ho selezionato un CID
				DettaglioSoggettoVO dettSogGenVO =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglio();

				if (!SoggettiDelegate.getInstance(request).isLivAutOkSO(dettSogGenVO.getLivAut(), true) )
					return mapping.getInputForward();

				request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
						new OggettoRiferimentoVO(true, dettSogGenVO.getCid(), dettSogGenVO.getTesto()) );

				request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO, dettSogGenVO);
				resetToken(request);
				return Navigation.getInstance(request).goForward(
						mapping.findForward("inserisciLegameSogDescr"));
			} else {
				if (nodoSelezionato.isDescrittore() ) {

					// ho selezionato un DID
					if (nodoSelezionato.getDatiLegame().equals(
							SbnLegameAut.valueOf("UF"))
							|| nodoSelezionato.getDatiLegame().equals(
									SbnLegameAut.valueOf("USE"))) {
						ActionMessages errors = new ActionMessages();
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.noFunz"));
						this.setErrors(request, errors, null);
						// nessun codice selezionato
						if (root != null)
							request.setAttribute(
									NavigazioneSemantica.ANALITICA, root);
						return mapping.getInputForward();
					}

					DettaglioDescrittoreVO dettDesGenVO =
						(DettaglioDescrittoreVO) nodoSelezionato.getDettaglio();

					request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO,
							currentForm.getCid());
					request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO,
							currentForm.getCodCategoriaSoggetto());

					if (!SoggettiDelegate.getInstance(request).isLivAutOkDE(dettDesGenVO.getLivAut(), true) )
						return mapping.getInputForward();

					request.setAttribute(
							NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
							dettDesGenVO);
					request.setAttribute(
							NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE,
							dettDesGenVO.isCondiviso());
					request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
							new OggettoRiferimentoVO(true,
									dettDesGenVO.getDid(),
									dettDesGenVO.getTesto()));

					return Navigation.getInstance(request).goForward(
							mapping.findForward("inserisciDescrLegameDescr"));

				} else {
					// Errore da gestire
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA,
								root);
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
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();
		}
	}

	public ActionForward inviaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();
		this.saveToken(request);
		try {
			if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

				TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

				if (nodoSelezionato.isSoggetto() ) {

					UserVO utenteCollegato = Navigation.getInstance(request)
							.getUtente();
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

					DettaglioSoggettoVO dettaglio = nodoSelezionato
							.getAreaDatiDettaglioOggettiVO()
							.getDettaglioSoggettoGeneraleVO();
					CreaVariaSoggettoVO richiesta = new CreaVariaSoggettoVO();
					richiesta.setCodiceSoggettario(dettaglio
							.getCampoSoggettario());
					richiesta.setCid(dettaglio.getCid());
					richiesta.setLivello(dettaglio.getLivAut());
					richiesta.setT005(dettaglio.getT005());
					richiesta.setTipoSoggetto(dettaglio.getCategoriaSoggetto());
					richiesta.setTesto(dettaglio.getTesto());
					richiesta.setCondiviso(dettaglio.isCondiviso());
					richiesta.setLivelloPolo(false);
					//richiesta.setCreaDescrittori(checkProlifoDescrittori(request));
					richiesta.setCreaDescrittori(false);

					CreaVariaSoggettoVO soggettoExport = factory
							.getGestioneSemantica()
							.importaSoggettoConDescrittori(richiesta,
									currentForm.getTreeElementViewSoggetti(),
									utenteCollegato.getTicket());
					if (!soggettoExport.isEsitoOk()) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.incongruo",
								soggettoExport.getTestoEsito()));
						this.setErrors(request, errors, null);
						return mapping.getInputForward();
					}

					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.operOk"));
					this.setErrors(request, errors, null);
					nodoSelezionato.setFlagCondiviso(true);
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

	public ActionForward scambia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();
		this.saveToken(request);
		try {
			if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato())) {

				TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

				// vale solo per i descrittore in forma di rinvio
				if (!nodoSelezionato.isDescrittore() || !nodoSelezionato.isRinvio() ) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.selezionaRinvio"));
					this.setErrors(request, errors, null);
					return mapping.getInputForward();
				}

				UserVO utenteCollegato = Navigation.getInstance(request)
						.getUtente();
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
				DettaglioDescrittoreVO dettaglioRinvio = (DettaglioDescrittoreVO) nodoSelezionato.getDettaglio();

				TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato.getParent();
				DettaglioDescrittoreVO dettaglioPadre = (DettaglioDescrittoreVO) padre.getDettaglio();

				if (!SoggettiDelegate.getInstance(request).isLivAutOkDE(dettaglioPadre.getLivAut(), true) )
					return mapping.getInputForward();

				DatiLegameDescrittoreVO scambia = new DatiLegameDescrittoreVO();
				scambia.setCodiceSoggettario(dettaglioPadre.getCampoSoggettario());
				scambia.setDidPartenza(dettaglioPadre.getDid());
				scambia.setDidPartenzaFormaNome(dettaglioPadre.getFormaNome());
				scambia.setDidPartenzaLivelloAut(dettaglioPadre.getLivAut());
				scambia.setCondiviso(dettaglioPadre.isCondiviso());
				scambia.setT005(dettaglioPadre.getT005());
				scambia.setDidArrivo(dettaglioRinvio.getDid());
				scambia.setDidArrivoFormaNome(dettaglioRinvio.getFormaNome());
				scambia.setTipoLegame(nodoSelezionato.getDatiLegame().toString() ); //"UF");
				scambia.setLivelloPolo(padre.isLivelloPolo());

				CreaVariaDescrittoreVO scambioFormaDescrittori =
					factory.getGestioneSemantica().scambioFormaDescrittori(scambia , utenteCollegato.getTicket());

				if (scambioFormaDescrittori.isEsitoOk()) {

					this.ricaricaReticolo(currentForm.getTreeElementViewSoggetti().getKey(),
							NavigazioneSemantica.TIPO_OGGETTO_CID, currentForm,
							request);
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.operOk"));
					this.setErrors(request, errors, null);
				} else {

					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
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

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

		if (nodoSelezionato == null) {

			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune().getCodSoggettario());

		currentForm.setCid(root.getKey());
		if (nodoSelezionato.isSoggetto()) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noFunz"));
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();

		} else {
			if (nodoSelezionato.isDescrittore()) {

				if (!root.isLivelloPolo()) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noFunz"));
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
					return mapping.getInputForward();
				}

				// ho selezionato un DID

				currentForm.setTipoLegame(nodoSelezionato.getDatiLegame().toString());
				if (nodoSelezionato.getDatiLegame().equals(SbnLegameAut.valueOf("931"))) {
					// controllo liv.aut padre (SO)
					DettaglioSoggettoVO dettaglioPadre = (DettaglioSoggettoVO) nodoSelezionato
							.getDettaglioPadre();
					if (!SoggettiDelegate.getInstance(request).isLivAutOkSO(
							dettaglioPadre.getLivAut(), true))
						return mapping.getInputForward();

					/* almaviva5_20110926 #4640
					// devo verificare se é automatico per altri soggetti
					currentForm.setEnableManuale(!delegate.isDescrittoreAutomaticoPerAltriSoggetti(nodoSelezionato.getKey()));

					if (currentForm.isEnableManuale())
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.cancDesLegameSo"));
					else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.esisteLegameDescSog"));
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
						return mapping.getInputForward();
					}
					*/
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.cancDesLegameSo"));

				} else {

					// controllo liv.aut padre (DE)
					DettaglioDescrittoreVO dettaglioPadre = (DettaglioDescrittoreVO) nodoSelezionato.getDettaglioPadre();
					if (!SoggettiDelegate.getInstance(request).isLivAutOkDE(dettaglioPadre.getLivAut(), true))
						return mapping.getInputForward();

					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.cancDesLegame"));
				}

				if (root != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA, root);
				currentForm.setXidConferma(nodoSelezionato);
				currentForm.setEnableCercaIndice(false);
				currentForm.setEnableConferma(true);
				currentForm.getParametriSogg().put(
						SoggettiParamType.MODALITA_CONFERMA,
						ModalitaConfermaType.CANCELLA_LEGAME);
				this.preparaConferma(mapping, request, form);
				return mapping.getInputForward();

			} else {
				// Errore da gestire
				if (root != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA, root);
				return mapping.getInputForward();
			}
		}

	}

	public ActionForward cancellaSoggetto(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// passaggio diretto da analitica a trascina
		//request.setAttribute(GestioneSoggettoAction.CANCELLA_IMMEDIATE, "xxx");
		Navigation.getInstance(request).setAttribute(GestioneSoggettoAction.CANCELLA_IMMEDIATE, "xxx");
		return gestione(mapping, form, request, response);

	}

	public ActionForward cancellaDescrittore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();

		String nodoSelezionato = currentForm.getNodoSelezionato();
		if (ValidazioneDati.strIsNull(nodoSelezionato)) {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			request.setAttribute(NavigazioneSemantica.ANALITICA,
				currentForm.getTreeElementViewSoggetti() );
			return mapping.getInputForward();
		}

		currentForm.setXidConferma(getNodoSelezionato(request, form) );
		currentForm.setEnableCercaIndice(false);
		currentForm.setEnableConferma(true);
		currentForm.getParametriSogg().put(SoggettiParamType.MODALITA_CONFERMA, ModalitaConfermaType.CANCELLA_DESCRITTORE);
		this.preparaConferma(mapping, request, form);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"errors.gestioneSemantica.cancDesLegame"));
		this.setErrors(request, errors, null);
		return mapping.getInputForward();
	}

	private ActionForward eseguiCancellazioneDescrittore(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;

		SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
		TreeElementViewSoggetti padre = (TreeElementViewSoggetti)nodoSelezionato.getParent();
		// ricavo il dettaglio del nodo padre
		DettaglioOggettoSemanticaVO dettaglioPadre = padre.isSoggetto() ?
			padre.getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO() :
			padre.getAreaDatiDettaglioOggettiVO().getDettaglioDescrittoreGeneraleVO();
		DettaglioDescrittoreVO descrittore = nodoSelezionato
				.getAreaDatiDettaglioOggettiVO()
				.getDettaglioDescrittoreGeneraleVO();

		CreaVariaDescrittoreVO cancellaDes = delegate.cancellaLegameConDescrittore(nodoSelezionato.isLivelloPolo(),
				dettaglioPadre, descrittore,
				nodoSelezionato.getDatiLegame().toString(),
				true);
		if (cancellaDes == null) {
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();
		}

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));
		currentForm.setEnableConferma(false);
		currentForm.setEnableCercaIndice(false);
		this.ricaricaReticolo(currentForm.getTreeElementViewSoggetti().getKey(),
				NavigazioneSemantica.TIPO_OGGETTO_CID,
				currentForm, request);
		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		RicercaComuneVO ricercaComune = currentForm.getRicercaComune();
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, ricercaComune.getCodSoggettario());

		TreeElementViewSoggetti xid = currentForm.getXidConferma();
		String tipoLegame = currentForm.getTipoLegame();

		currentForm.setEnableConferma(false);

		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		if (xid == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			// nessun codice selezionato
			return mapping.getInputForward();
		}
		if (xid.isSoggetto() ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noFunz"));
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();
		}

		if (xid.isDescrittore()) {

			ParametriSoggetti parametriSogg = currentForm.getParametriSogg();
			ModalitaConfermaType modalitaConferma = (ModalitaConfermaType) parametriSogg.get(SoggettiParamType.MODALITA_CONFERMA);

			switch (modalitaConferma) {
			case CANCELLA_DESCRITTORE:
				parametriSogg.remove(SoggettiParamType.MODALITA_CONFERMA);
				return eseguiCancellazioneDescrittore(mapping, form, request, response);
			case CANCELLA_LEGAME:
				parametriSogg.remove(SoggettiParamType.MODALITA_CONFERMA);
				break;
			}

			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			if (tipoLegame.equals("931")) {
				try {
					RicercaSoggettoListaVO lista = factory
							.getGestioneSemantica()
							.ricercaSoggettiPerDidCollegato(
									ricercaComune.isPolo(), xid.getKey(),
									10, utenteCollegato.getTicket(),
									ricercaComune.getOrdinamentoSoggetto(), null );

					if (!lista.isEsitoOk()) {
						// errori indice
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", lista.getTestoEsito()));
						if (root != null)
							request.setAttribute(NavigazioneSemantica.ANALITICA, root);
						// nessun codice selezionato
						return mapping.getInputForward();
					}
					//boolean cancella = (lista.getTotRighe() == 1);
					CreaLegameSoggettoDescrittoreVO legame = new CreaLegameSoggettoDescrittoreVO();
					DettaglioSoggettoVO dettaglioSog = (DettaglioSoggettoVO) root.getDettaglio();

					legame.setCid(dettaglioSog.getCid());
					legame.setCodSoggettario(dettaglioSog.getCampoSoggettario());
					legame.setLivelloAutorita(dettaglioSog.getLivAut());
					legame.setCategoriaSoggetto(dettaglioSog.getCategoriaSoggetto() );
					legame.setT005(dettaglioSog.getT005());
					legame.setDid(xid.getKey());
					legame.setPolo(dettaglioSog.isLivelloPolo());
					legame.setCondiviso(dettaglioSog.isCondiviso() );

					CreaVariaDescrittoreVO descrittore = factory
							.getGestioneSemantica()
							.cancellaLegameSoggettoDescrittore(legame, utenteCollegato.getTicket());
					if (!descrittore.isEsitoOk()) {
						// errori indice
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", descrittore.getTestoEsito()));
						if (root != null)
							request.setAttribute(NavigazioneSemantica.ANALITICA, root);
						// nessun codice selezionato
						return mapping.getInputForward();
					}

					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));
					currentForm.setEnableConferma(false);
					currentForm.setEnableCercaIndice(false);
					this.ricaricaReticolo(currentForm.getCid(),
							NavigazioneSemantica.TIPO_OGGETTO_CID, currentForm,
							request);
					return mapping.getInputForward();

				} catch (ValidationException e) {
					// errori indice
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
					currentForm.setEnableConferma(false);
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
					log.error("", e);
					// nessun codice selezionato
					return mapping.getInputForward();

				} catch (DataException e) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
					currentForm.setEnableConferma(false);
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
					log.error("", e);
					return mapping.getInputForward();

				} catch (InfrastructureException e) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
					currentForm.setEnableConferma(false);
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
					log.error("", e);
					return mapping.getInputForward();

				} catch (Exception e) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
					currentForm.setEnableConferma(false);
					log.error("", e);
					return mapping.getInputForward();
				}

			} else { // non è 931 più esterno
				TreeElementViewSoggetti nodoSelezionato = (TreeElementViewSoggetti) root.findElementUnique(
								Integer.valueOf(currentForm.getNodoSelezionato()));

				if (xid.isSoggetto() ) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noFunz"));
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
					return mapping.getInputForward();
				}

				if (xid.isDescrittore() ) {

					if (nodoSelezionato.getDatiLegame().equals(SbnLegameAut.valueOf("931"))) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noFunz"));
						if (root != null)
							request.setAttribute(NavigazioneSemantica.ANALITICA, root);
						return mapping.getInputForward();
					} else {
						TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato.getParent();
						if (padre == null) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noTasto"));
							return mapping.getInputForward();
						}

						try {
							DatiLegameDescrittoreVO legame = new DatiLegameDescrittoreVO();
							legame.setDidPartenza(padre.getKey());
							legame.setDidPartenzaFormaNome(padre.getFormaNome());
							legame.setT005(padre.getT005());
							legame.setDidPartenzaLivelloAut(padre.getLivelloAutorita());
							legame.setDidArrivo(xid.getKey());
							legame.setDidArrivoFormaNome(nodoSelezionato.getFormaNome());
							legame.setTipoLegame(nodoSelezionato.getDatiLegame().toString());
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
								LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", descrittore.getTestoEsito()));
								if (root != null)
									request.setAttribute(NavigazioneSemantica.ANALITICA, root);
								// nessun codice selezionato
								return mapping.getInputForward();
							}

							LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));
							currentForm.setEnableConferma(false);
							currentForm.setEnableCercaIndice(false);
							this.ricaricaReticolo(currentForm.getCid(),
									NavigazioneSemantica.TIPO_OGGETTO_CID,
									currentForm, request);
							return mapping.getInputForward();

						} catch (ValidationException e) {
							// errori indice
							LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
							currentForm.setEnableConferma(false);
							if (root != null)
								request.setAttribute(NavigazioneSemantica.ANALITICA, root);
							log.error("", e);
							// nessun codice selezionato
							return mapping.getInputForward();

						} catch (DataException e) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
							currentForm.setEnableConferma(false);
							if (root != null)
								request.setAttribute(NavigazioneSemantica.ANALITICA, root);
							log.error("", e);
							return mapping.getInputForward();
						} catch (InfrastructureException e) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
							if (root != null)
								request.setAttribute(NavigazioneSemantica.ANALITICA, root);
							log.error("", e);
							return mapping.getInputForward();
						} catch (Exception e) {
							LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
							if (root != null)
								request.setAttribute(NavigazioneSemantica.ANALITICA, root);
							log.error("", e);
							return mapping.getInputForward();
						}

					}

				} else { // else più interno non è C e non è D
					// messaggio di errore.
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
					// nessun codice selezionato
					if (root != null)
						request.setAttribute(NavigazioneSemantica.ANALITICA, root);
					return mapping.getInputForward();
				}
			}

		} else { // else più esterno non è C e non è D
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();
		}

	}// fine del metodo

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune().getCodSoggettario());

		currentForm.setEnableConferma(false);
		currentForm.setEnableIndice(false);
		currentForm.setEnableCercaIndice(false);
		currentForm.setEnableModifica(true);
		currentForm.setEnableInserisci(true);
		currentForm.setEnableElimina(true);
		currentForm.setEnableOk(false);
		currentForm.setEnableCrea(true);
		currentForm.setEnableEsamina(true);
		currentForm.setEnableGestione(true);
		currentForm.setEnableSogColl(true);
		currentForm.setEnableStampa(true);

		currentForm.getParametriSogg().remove(SoggettiParamType.MODALITA_CONFERMA);
		return mapping.getInputForward();
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		ActionForm callerForm = navi.getCallerForm();
		return navi.goBack(SemanticaFormTypes.getFormType(callerForm) != SemanticaFormTypes.CATALOGAZIONE_SEMANTICA);
	}

	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataVariazione());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataVariazione());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, currentForm.getRicercaComune().getDescSoggettario());

		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		request.setAttribute(NavigazioneSemantica.ANALITICA, root);
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

		if (nodoSelezionato == null)
			return mapping.getInputForward();

		request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, root.getKey());
		request.setAttribute(NavigazioneSemantica.TESTO_SOGGETTO, root.getTesto());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, root.getCategoriaSoggetto());
		if (nodoSelezionato.isSoggetto() ) {
			// ho selezionato un CID anziche' un DID
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noFunz"));
			if (root != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, root);
			return mapping.getInputForward();
		}

		if (nodoSelezionato.isDescrittore() ) {
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			if (nodoSelezionato.getDatiLegame().toString().equals("931")) {
				// controllo liv.aut padre (SO)
				DettaglioSoggettoVO dettaglioPadre =
					(DettaglioSoggettoVO) nodoSelezionato.getDettaglioPadre();
				if (!delegate.isLivAutOkSO(dettaglioPadre.getLivAut(), true) )
					return mapping.getInputForward();

				// devo verificare se é automatico per altri soggetti
				currentForm.setEnableManuale(!delegate.isDescrittoreAutomaticoPerAltriSoggetti(nodoSelezionato.getKey()));

				currentForm.setCid(root.getKey());
				DettaglioDescrittoreVO dettDesGenVO = (DettaglioDescrittoreVO) nodoSelezionato.getDettaglio();

				currentForm.setEnableAnaSog(true);
				request.setAttribute(
						NavigazioneSemantica.ENABLE_ANALITICA_SOGGETTO,
						currentForm.isEnableAnaSog());
				request.setAttribute(
						NavigazioneSemantica.DETTAGLIO_DESCRITTORE,
						dettDesGenVO);
				request.setAttribute(
						NavigazioneSemantica.DESCRITTORE_MANUALE,
						currentForm.isEnableManuale());
				request.setAttribute(
						NavigazioneSemantica.DATA_MODIFICA, dettDesGenVO.getDataAgg() );
				request.setAttribute(
						NavigazioneSemantica.DATA_INSERIMENTO, dettDesGenVO.getDataIns() );
				resetToken(request);
				request.setAttribute(
						NavigazioneSemantica.DATA_MODIFICA_T005,
						nodoSelezionato.getT005());
				request.setAttribute(
						NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE,
						dettDesGenVO.isCondiviso());
				return Navigation.getInstance(request).goForward(mapping.findForward("gestioneDescrittore"));
			}

			TreeElementViewSoggetti padre = (TreeElementViewSoggetti) nodoSelezionato.getParent();
			if (padre == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noTasto"));
				return mapping.getInputForward();
			}

			// controllo liv.aut padre (DE)
			DettaglioDescrittoreVO dettaglioPadre =
				(DettaglioDescrittoreVO) nodoSelezionato.getDettaglioPadre();
			if (!delegate.isLivAutOkDE(dettaglioPadre.getLivAut(), true) )
				return mapping.getInputForward();

			request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME, dettaglioPadre.getDid());
			request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_TESTO, dettaglioPadre.getTesto());
			request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_FORMA_NOME, dettaglioPadre.getFormaNome());
			request.setAttribute(NavigazioneSemantica.DID_PARTENZA_LEGAME_LIVELLO_AUTORITA, dettaglioPadre.getLivAut());
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_T005, dettaglioPadre.getT005());
			request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE, dettaglioPadre.isCondiviso());

			request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME, nodoSelezionato.getKey() );
			request.setAttribute(NavigazioneSemantica.TIPO_LEGAME, nodoSelezionato.getDatiLegame().toString());
			request.setAttribute(NavigazioneSemantica.NOTE_AL_LEGAME, nodoSelezionato.getNotaLegame() );
			request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_TESTO, nodoSelezionato.getTesto());
			request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_FORMA_NOME, nodoSelezionato.getFormaNome());
			request.setAttribute(NavigazioneSemantica.DID_ARRIVO_LEGAME_LIVELLO_AUTORITA, nodoSelezionato.getLivelloAutorita());
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );

			//almaviva5_20120507 evolutive CFI
			ParametriSoggetti parametri = currentForm.getParametriSogg().copy();
			parametri.put(SoggettiParamType.DETTAGLIO_ID_PARTENZA, dettaglioPadre);
			parametri.put(SoggettiParamType.DETTAGLIO_ID_ARRIVO, nodoSelezionato.getDettaglio() );
			ParametriSoggetti.send(request, parametri);

			return Navigation.getInstance(request).goForward(mapping.findForward("modificaLegameTraDescr"));

		} else {
			// Errore da gestire
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
			HttpServletRequest request, ActionForm form) {
		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages messages = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("button.parameter", mapping
				.getParameter());
		messages.add("gestionesemantica.parameter.conferma", msg1);
		request.setAttribute(NavigazioneSemantica.TIPO_LEGAME, currentForm.getTipoLegame());
		this.saveMessages(request, messages);
	}

	public ActionForward soggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();

		currentForm.setEnableConferma(false);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataVariazione());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA_PRIMA,
				currentForm.getOutputlistaprima());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		if (!ValidazioneDati.strIsNull(currentForm.getNodoSelezionato() )) {

			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);

			// MANCANO LE NOTE AL SOGGETTO
			if (nodoSelezionato.isSoggetto() ) {
				// ho selezionato un CID
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.setErrors(request, errors, null);
				// nessun codice selezionato
				if (currentForm.getTreeElementViewSoggetti() != null)
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
				return mapping.getInputForward();

			} else {
				if (nodoSelezionato.isDescrittore() ) {
					// ho selezionato un DID
					currentForm.setCid(currentForm.getTreeElementViewSoggetti()
							.getKey());
					request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO,
							currentForm.getCid());
					request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO,
							currentForm.getCodCategoriaSoggetto());
					request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA,
							currentForm.getTreeElementViewSoggetti()
									.getLivelloAutorita());
					request.setAttribute(NavigazioneSemantica.ANALITICA,
							currentForm.getTreeElementViewSoggetti());
					request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
							currentForm.getDataInserimento());
					request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
							currentForm.getDataVariazione());
					request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
							mapping.getPath());

					DettaglioDescrittoreVO dettaglio = nodoSelezionato
							.getAreaDatiDettaglioOggettiVO()
							.getDettaglioDescrittoreGeneraleVO();
					request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
							new OggettoRiferimentoVO(true,
									dettaglio.getDid(),
									dettaglio.getTesto()));
					try {
						SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
						RicercaComuneVO ricercaComune = currentForm.getRicercaComune().copy();

						RicercaSoggettoDescrittoriVO ricDid = new RicercaSoggettoDescrittoriVO();
						ricDid.setDid(dettaglio.getDid() );
						ricercaComune.setRicercaSoggetto(ricDid);
						ricercaComune.setRicercaDescrittore(null);
						delegate.eseguiRicerca(ricercaComune, mapping);

						RicercaSoggettoResult op = delegate.getOperazione();
						switch (op) {
						case analitica_1:// SoggettiDelegate.analitica:
							currentForm.setEnableCercaIndice(false);
							request
									.setAttribute(
											NavigazioneSemantica.ABILITA_RICERCA_INDICE,
											currentForm.isEnableCercaIndice());
							return Navigation.getInstance(request).goForward(
									mapping.findForward("listasintetica"));
						case crea_4:// SoggettiDelegate.crea:
							request.setAttribute(
									NavigazioneSemantica.ACTION_CALLER, mapping
											.getPath());
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"errors.gestioneSemantica.nontrovato"));
							this.setErrors(request, errors, null);
							return mapping.getInputForward();
						case sintetica_3:// SoggettiDelegate.sintetica
							// :
							currentForm.setEnableCercaIndice(false);
							request
									.setAttribute(
											NavigazioneSemantica.ABILITA_RICERCA_INDICE,
											currentForm.isEnableCercaIndice());
							return Navigation.getInstance(request).goForward(
									mapping.findForward("listasintetica"));

						case lista_2:// SoggettiDelegate.lista
							// soggetti:
							currentForm.setEnableCercaIndice(false);
							request
									.setAttribute(
											NavigazioneSemantica.ABILITA_RICERCA_INDICE,
											currentForm.isEnableCercaIndice());
							return Navigation.getInstance(request).goForward(
									mapping.findForward("listasintetica"));

						case diagnostico_0:// SoggettiDelegate.diagnostico:
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"errors.gestioneSemantica.incongruo", delegate.getOutput().getTestoEsito() ));
							this.setErrors(request, errors, null);
							return mapping.getInputForward();
						default:
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"errors.gestioneSemantica.noselezione"));
							return mapping.getInputForward();
						}

					} catch (ValidationException e) {
						// errori indice

						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.incongruo", e
										.getMessage()));
						this.setErrors(request, errors, e);
						log.error("", e);
						// nessun codice selezionato
						return mapping.getInputForward();

					} catch (DataException e) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.incongruo", e
										.getMessage()));
						this.setErrors(request, errors, e);
						log.error("", e);
						return mapping.getInputForward();// gestione errori
						// java
					} catch (InfrastructureException e) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.incongruo", e
										.getMessage()));
						this.setErrors(request, errors, e);
						log.error("", e);
						return mapping.getInputForward();// gestione errori
						// java
					} catch (Exception e) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.gestioneSemantica.erroreSistema", e
										.getMessage()));
						this.setErrors(request, errors, e);
						log.error("", e);
						return mapping.getInputForward();// gestione errori
						// java
					}

				} else {
					// Errore da gestire
					return mapping.getInputForward();
				}
			}
		}

		else {
			// messaggio di errore.
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			return mapping.getInputForward();
		}

	}

	public ActionForward elementoPrecedente(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		String[] listaCidSelez = currentForm.getListaCidSelez();
		int pos = currentForm.getPosizioneCorrente();
		if (pos == 0) {
			request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.fineScorrimento"));
			this.setErrors(request, errors, null);
			resetToken(request);
			return mapping.getInputForward();
		}
		currentForm.setPosizioneCorrente(--pos);
		String xid = listaCidSelez[pos];

		this.ricaricaReticolo(xid, NavigazioneSemantica.TIPO_OGGETTO_CID,
				currentForm, request);

		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		String codice = root.getAreaDatiDettaglioOggettiVO()
				.getDettaglioSoggettoGeneraleVO().getCampoSoggettario();
		currentForm.getRicercaComune().setCodSoggettario(codice);
		return mapping.getInputForward();
	}

	public ActionForward elementoSuccessivo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;

		String[] listaCidSelez = currentForm.getListaCidSelez();
		int pos = currentForm.getPosizioneCorrente();

		if (pos == listaCidSelez.length - 1) {
			request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.fineScorrimento"));
			this.setErrors(request, errors, null);
			resetToken(request);
			return mapping.getInputForward();
		}
		currentForm.setPosizioneCorrente(++pos);
		String xid = listaCidSelez[pos];

		this.ricaricaReticolo(xid, NavigazioneSemantica.TIPO_OGGETTO_CID,
				currentForm, request);

		TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
		String codice = root.getAreaDatiDettaglioOggettiVO()
				.getDettaglioSoggettoGeneraleVO().getCampoSoggettario();
		currentForm.getRicercaComune().setCodSoggettario(codice);
		return mapping.getInputForward();
	}

	public ActionForward trascina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//passaggio diretto da analitica a trascina
		request.setAttribute(GestioneSoggettoAction.TRASCINA_IMMEDIATE, "xxx");
		return gestione(mapping, form, request, response);
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzione();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
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

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			// nessun codice selezionato
			if (currentForm.getTreeElementViewSoggetti() != null)
				request.setAttribute(NavigazioneSemantica.ANALITICA, currentForm.getTreeElementViewSoggetti());
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

	private enum TipoAttivita {
		CREA,
		CREA_INDICE,
		SCAMBIA,
		GESTIONE,
		CANCELLA,
		MODIFICA,
		LEGAME,
		IMPORTA,

		MODIFICA_SOGGETTO,
		CANCELLA_SOGGETTO,
		TRASCINA_SOGGETTO,
		FONDI_SOGGETTO,
		INS_LEGAME_SOG_DES,
		CONDIVIDI,

		MODIFICA_DESCRITTORE,
		CANCELLA_DESCRITTORE,
		INS_LEGAME_DES_DES,
		MOD_LEGAME_DES,
		CANCELLA_LEGAME_DES,
		SCAMBIA_FORMA,

		TITOLI_COLL_BIBLIO,
		TITOLI_COLL_BIBLIO_FILTRO,
		TITOLI_COLL_POLO,
		TITOLI_COLL_POLO_FILTRO,
		TITOLI_COLL_INDICE,
		TITOLI_COLL_INDICE_FILTRO,
		SOGGETTI_COLLEGATI,
		SIMILI_INDICE,

		JAVASCRIPT_ENABLED,
		JAVASCRIPT_DISABLED;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		AnaliticaSoggettoForm currentForm = (AnaliticaSoggettoForm) form;
		Navigation navi = Navigation.getInstance(request);

		try {
			TreeElementViewSoggetti root = currentForm.getTreeElementViewSoggetti();
			DettaglioSoggettoVO dettaglioSog = (DettaglioSoggettoVO) root.getDettaglio();
			TreeElementViewSoggetti nodoSelezionato = getNodoSelezionato(request, form);
			if (nodoSelezionato == null) {
				nodoSelezionato = root;
				currentForm.setNodoSelezionato(nodoSelezionato.getRepeatableId() + "");
			}

			String codSoggettario = dettaglioSog.getCampoSoggettario();
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			CodiciAttivita codiciAttivita = CodiciAttivita.getIstance();
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);

			switch (attivita) { //PULSANTI
			case CREA:
			case CREA_INDICE:
				if (delegate.countSoggettariGestiti() < 1)
					return false;
				return delegate.isAbilitatoSO(codiciAttivita.CREA_ELEMENTO_DI_AUTHORITY_1017);

			case IMPORTA:
				if (delegate.countSoggettariGestiti() < 1)
					return false;
				if (ValidazioneDati.isFilled(dettaglioSog.getDatiCondivisione()) ) // già condiviso in polo
					return false;
				return nodoSelezionato.isRoot() && delegate
						.isAbilitatoSO(codiciAttivita.CREA_ELEMENTO_DI_AUTHORITY_1017);

			case SCAMBIA:
				if (!delegate.isSoggettarioGestito(codSoggettario) )
					return false;
				return !!nodoSelezionato.isRoot() && delegate
						.isAbilitatoDE(codiciAttivita.SCAMBIO_FORMA_1029);

			case CANCELLA:
				return checkAttivita(request, currentForm, TipoAttivita.CANCELLA_SOGGETTO.name());

			case GESTIONE:
				if (!dettaglioSog.isLivelloPolo() ) // niente gestione in indice
					return false;
				if (!delegate.isSoggettarioGestito(codSoggettario) )
					return false;
				if (!delegate.isLivAutOkSO(dettaglioSog.getLivAut(), false))
					return false;
				return delegate.isAbilitatoSO(codiciAttivita.MODIFICA_SOGGETTO_1265);

			case LEGAME:
			case MODIFICA:
				if (!delegate.isSoggettarioGestito(codSoggettario) )
					return false;
				return delegate.isAbilitatoSO(codiciAttivita.MODIFICA_SOGGETTO_1265);
			}

			//check soggettario
			if (!delegate.isSoggettarioGestito(codSoggettario) )
				return false;

			switch (attivita) { //SOGGETTI

			case CANCELLA_SOGGETTO:
				if (!delegate.isLivAutOkSO(dettaglioSog.getLivAut(), false) )
					return false;
				if (!delegate.isAbilitatoSO(codiciAttivita.CANCELLA_ELEMENTO_AUTHORITY_1028) )
					return false;
				if (!nodoSelezionato.isRoot() )
					return false;

				//almaviva5_20120801 check per indice
				if (!dettaglioSog.isLivelloPolo())
					if (!delegate.isSoggettarioGestitoIndice(codSoggettario))
						return false;

				//solo se non legato a titoli
				return (dettaglioSog.getNumTitoliBiblio() == 0) && (dettaglioSog.getNumTitoliPolo() == 0);

			case INS_LEGAME_SOG_DES:
				if (!delegate.isLivAutOkSO(dettaglioSog.getLivAut(), false) )
					return false;
				if (!nodoSelezionato.isRoot() )
					return false;
				return delegate.isAbilitatoSO(codiciAttivita.MODIFICA_SOGGETTO_1265);

			case MODIFICA_SOGGETTO:
				if (!delegate.isLivAutOkSO(dettaglioSog.getLivAut(), false) )
					return false;
				if (!nodoSelezionato.isRoot() )
					return false;
//				if (navi.bookmarkExists(NavigazioneSemantica.SOGGETTAZIONE_ATTIVA) )
//					return false;
				return delegate.isAbilitatoSO(codiciAttivita.MODIFICA_SOGGETTO_1265);

			case FONDI_SOGGETTO:
			case TRASCINA_SOGGETTO:
				if (!nodoSelezionato.isRoot() )
					return false;
				if (!delegate.isLivAutOkSO(dettaglioSog.getLivAut(), false) )
					return false;
				if (!delegate.isAbilitatoSO(codiciAttivita.FONDE_SOGGETTO_1272) )
					return false;
//				if (navi.bookmarkExists(NavigazioneSemantica.SOGGETTAZIONE_ATTIVA) )
//					return false;

				//solo se legato a titoli
				return (dettaglioSog.getNumTitoliBiblio() > 0) || (dettaglioSog.getNumTitoliPolo() > 0);

			case CONDIVIDI:
				if (!delegate.isAbilitatoSO(codiciAttivita.CREA_ELEMENTO_DI_AUTHORITY_1017) )
					return false;
				return (!root.isFlagCondiviso() );
			}

			DettaglioDescrittoreVO dettaglioDES =
				nodoSelezionato.getAreaDatiDettaglioOggettiVO().getDettaglioDescrittoreGeneraleVO();

			switch (attivita) { //DESCRITTORI

			case MODIFICA_DESCRITTORE:
				if (!delegate.isLivAutOkDE(dettaglioDES.getLivAut(), false) )
					return false;
				//almaviva5_20100614 #3799
				//if (nodoSelezionato.hasChildren()) return false;
				return delegate.isAbilitatoDE(codiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);

			case CANCELLA_DESCRITTORE:
				if (!delegate.isLivAutOkDE(dettaglioDES.getLivAut(), false) )
					return false;
				if (nodoSelezionato.hasChildren())
					return false;
				if (dettaglioDES.getSoggettiCollegati() > 0)
					return false;
				if (nodoSelezionato.getNodeLevel() == 1 ) { // figlio diretto del soggetto
					if (nodoSelezionato.isDescrittoreAutomatico() )	// descrittore automatico
						return false;
					if (!delegate.isLivAutOkSO(dettaglioSog.getLivAut(), false) )
						return false;
					if (!delegate.isAbilitatoSO(codiciAttivita.MODIFICA_SOGGETTO_1265) )
						return false;
				} else {
					// ripeto controllo se legato ad altro descrittore
					if (!checkAttivita(request, form, TipoAttivita.CANCELLA_LEGAME_DES.name() ) )
						return false;
				}

				if (dettaglioDES.getSoggettiCollegati() > 1 )
					return false;
				return delegate.isAbilitatoDE(codiciAttivita.CANCELLA_ELEMENTO_AUTHORITY_1028);

			case INS_LEGAME_DES_DES:
				if (!delegate.isLivAutOkDE(dettaglioDES.getLivAut(), false) )
					return false;
				if (nodoSelezionato.isRinvio() )
					return false;
				return delegate.isAbilitatoDE(codiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);

			case MOD_LEGAME_DES:
				if (nodoSelezionato.getNodeLevel() == 1 )  // leg. sog-des
					//almaviva5_20111116 #4742 modifica legame non prevista
					return false;
				else {
					// è un legame tra descrittori
					DettaglioDescrittoreVO dettaglioPadre =
						(DettaglioDescrittoreVO) nodoSelezionato.getDettaglioPadre();
					if (!delegate.isLivAutOkDE(dettaglioPadre.getLivAut(), false) )
						return false;
					return delegate.isAbilitatoDE(codiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);
				}

			case CANCELLA_LEGAME_DES:
				if (nodoSelezionato.getNodeLevel() == 1 ) { // leg. sog-des
					if (!delegate.isLivAutOkSO(dettaglioSog.getLivAut(), false) )
						return false;
					if (nodoSelezionato.isDescrittoreAutomatico() )	//automatico?
						return false;
					return delegate.isAbilitatoSO(codiciAttivita.MODIFICA_SOGGETTO_1265);
				} else {
					// è un legame tra descrittori
					DettaglioDescrittoreVO dettaglioPadre =
						(DettaglioDescrittoreVO) nodoSelezionato.getDettaglioPadre();
					if (!delegate.isLivAutOkDE(dettaglioPadre.getLivAut(), false) )
						return false;
					return delegate.isAbilitatoDE(codiciAttivita.MODIFICA_ELEMENTO_DI_AUTHORITY_1026);
				}

			case SCAMBIA_FORMA:
				if (!nodoSelezionato.isRinvio())
					return false;
				return delegate.isAbilitatoDE(codiciAttivita.SCAMBIO_FORMA_1029);
			}

			//combo esamina
			switch (attivita) {
			case TITOLI_COLL_BIBLIO:
			case TITOLI_COLL_BIBLIO_FILTRO:
				return (nodoSelezionato.isRoot() && dettaglioSog.getNumTitoliBiblio() > 0 && root.isLivelloPolo());
			case TITOLI_COLL_POLO:
			case TITOLI_COLL_POLO_FILTRO:
				return true;//(nodoSelezionato.isRoot() && dettaglioSog.getNumTitoliPolo() > 0 && root.isLivelloPolo() );
			case TITOLI_COLL_INDICE:
			case TITOLI_COLL_INDICE_FILTRO:
				return !root.isLivelloPolo() || dettaglioSog.isCondiviso();

			case SOGGETTI_COLLEGATI:
				return nodoSelezionato.isDescrittore() &&
						(!nodoSelezionato.isLivelloPolo() || (dettaglioDES.getSoggettiCollegati() > 0) );

			case SIMILI_INDICE:
				return nodoSelezionato.isRoot() && nodoSelezionato.isLivelloPolo();

			case JAVASCRIPT_ENABLED:
				return true;//navi.isJavaScriptEnabled();
			case JAVASCRIPT_DISABLED:
				return false;//!navi.isJavaScriptEnabled();
			}


			return false;

		} catch (Exception e) {
			log.error("", e);
			navi.setExceptionLog(e);
			return false;
		}

	}

}
