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
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaFormTypes;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.EsaminaClasseForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.HashMap;
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
import org.apache.struts.actions.LookupDispatchAction;

public class EsaminaClasseAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(EsaminaClasseAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.gestione", "gestione");
		map.put("button.ok", "ok");
		map.put("button.polo", "polo");
		map.put("button.importa", "importa");
		map.put("button.biblio", "biblio");
		map.put("button.indice", "indice");
		map.put("button.indice.filtro", "indiceFiltro");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		map.put("button.elimina", "elimina");

		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");
		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
			currentForm.setListaSistemiClassificazione(CaricamentoComboSemantica.loadComboSistemaClassificazione(ticket, false));
			currentForm.setListaEdizioni(CaricamentoComboSemantica.loadComboEdizioneDewey());
			currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));

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

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		String chiamante = null;
		Boolean isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO));
		DettaglioClasseVO dettaglio = null;
		FolderType folder = null;
		String simbolo = null;
		RicercaClasseListaVO currentFormListaClassi = null;
		RicercaClassiVO currentFormRicerca = null;

		if (!currentForm.isSessione()) {
			log.debug("EsaminaClasseAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);

			isPolo = isPolo != null ? isPolo.booleanValue() : true;
			dettaglio = (DettaglioClasseVO) request
					.getAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE);
			simbolo = (String) request.getAttribute(NavigazioneSemantica.SIMBOLO_CLASSE);
			currentFormListaClassi = (RicercaClasseListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			currentFormRicerca = (RicercaClassiVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);

			OggettoRiferimentoVO oggRif =
				(OggettoRiferimentoVO) request.getAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO);
			if (oggRif != null)
				currentForm.setOggettoRiferimento(oggRif);


			currentForm.setSessione(true);
			currentForm.setOutput(currentFormListaClassi);
			if (currentFormRicerca != null){
				currentForm.setRicercaClasse(currentFormRicerca);
			}

			folder = (FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
			AreaDatiPassBiblioSemanticaVO datiGB = (AreaDatiPassBiblioSemanticaVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA);
			if (datiGB != null) {
				currentForm.setAreaDatiPassBiblioSemanticaVO(datiGB);
				currentForm.getCatalogazioneSemanticaComune().setBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getBidPartenza());
				currentForm.getCatalogazioneSemanticaComune().setTestoBid(
						currentForm.getAreaDatiPassBiblioSemanticaVO()
								.getDescPartenza());
				currentForm.setFolder(folder);
			}

			currentForm.setAction(chiamante);

			currentForm.setDettClaGen(dettaglio);
			if (currentForm.getDettClaGen().getDataIns() != null) {
				currentForm.setDataInserimento(currentForm.getDettClaGen().getDataIns());
			}
			currentForm.setDataModifica(currentForm.getDettClaGen().getDataAgg());
			currentForm.getRicercaClasse().setPolo(isPolo);

			//combo esamina
			currentForm.setComboGestioneEsamina(LabelGestioneSemantica
					.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
							request, form, new String[]{"CL"}, this));
			currentForm.setIdFunzioneEsamina("");
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setStatoControllo(currentForm.getDettClaGen().getLivAut());
		currentForm
				.setUlterioreTermine(currentForm.getDettClaGen()
						.getUlterioreTermine());
		currentForm.setDataInserimento(currentForm.getDettClaGen().getDataIns());
		currentForm.setDataModifica(currentForm.getDettClaGen().getDataAgg());

		if (isPolo != null)
			currentForm.getRicercaClasse().setPolo(isPolo.booleanValue());
		currentForm.getRicercaClasse().setCodSistemaClassificazione(
				currentForm.getDettClaGen().getCampoSistema());
		if (currentForm.getDettClaGen().getCampoEdizione() == null) {
			currentForm.getDettClaGen().setCampoEdizione("  ");
		} else {
			currentForm.getRicercaClasse().setCodEdizioneDewey(
					currentForm.getDettClaGen().getCampoEdizione());
		}
		currentForm.setStatoControllo(currentForm.getDettClaGen().getLivAut());
		currentForm.setIdentificativoClasse(currentForm.getDettClaGen()
				.getIdentificativo().trim());
		if (simbolo != null && simbolo.length() > 0) {
			currentForm.setSimbolo(simbolo.trim());
		}
		currentForm.setDescrizione(currentForm.getDettClaGen().getDescrizione());
		currentForm.setEnableGestione(currentForm.getRicercaClasse().isPolo());
		currentForm.setEnableEsamina(currentForm.getRicercaClasse().isPolo());

		if (currentForm.getRicercaClasse().isIndice()) {
			currentForm.setEnableImporta(true);
		}

		if (currentForm.getFolder() != null
				&& currentForm.getFolder() == FolderType.FOLDER_CLASSI) {
			currentForm.setEnableTit(true);
		}

		UserVO utenteCollegato = navi.getUtente();
		currentForm.setBiblioteca(utenteCollegato.getCodPolo()
				+ utenteCollegato.getCodBib());

		if (currentForm.getRicercaClasse().isPolo()) {

			currentForm.setEnableNumPolo(currentForm.getDettClaGen().getNumTitoliPolo() > 0);
			currentForm.setEnableNumBiblio(currentForm.getDettClaGen().getNumTitoliBiblio() > 0);

		} else {
			List<?> titoliIndice = this.listaTitoliCollegatiIndice(request,
					currentForm.getIdentificativoClasse(), form);
			if (titoliIndice == null) {
				currentForm.setEnableNumIndice(false);
			} else {
				currentForm.setEnableNumIndice(true);
			}
		}

		if (!this.initCombo(request, form, navi.getUserTicket()))
			return mapping.getInputForward();

		currentForm.setTitoliBiblio((List<?>) request
				.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));
		if (currentForm.getTitoliBiblio()!= null){
			currentForm.setEnableOk(true);
		}
		currentForm.setNotazioneTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA));
		currentForm.setTestoTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));
		currentForm.setEnableIndice(currentForm.getRicercaClasse().isIndice());
		currentForm
				.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
						.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));
		currentForm.setAction((String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER));
		return mapping.getInputForward();
	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//almaviva5_20141210
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());

		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getDescrizione());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("delegate_titoliCollegati"));

	}

	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//almaviva5_20141210
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());

		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getDescrizione());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("delegate_titoliCollegatiFiltro"));

	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, utenteCollegato.getCodBib() );
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getDescrizione());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("titoliCollegatiBiblio"));

	}

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getDescrizione());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("delegate_titoliCollegati"));

	}

	public ActionForward indiceFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getDescrizione());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("titoliCollegatiIndiceFiltro"));

	}


	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		//controllo liv autorità su profilo
		DettaglioClasseVO dettaglio = currentForm.getDettClaGen();
		if (!ClassiDelegate.getInstance(request).isLivAutOk(dettaglio.getLivAut(), true))
			return mapping.getInputForward();

		resetToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaClasse().isPolo());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, dettaglio);
		resetToken(request);
		Navigation.getInstance(request).purgeThis();
		return mapping.findForward("gestioneClasse");
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		ActionForm callerForm = navi.getCallerForm();
		return navi.goBack(SemanticaFormTypes.getFormType(callerForm) != SemanticaFormTypes.CATALOGAZIONE_SEMANTICA);
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
		//String xid = currentForm.getRicercaClasse().getIdentificativoClasse();
		String xid = currentForm.getIdentificativoClasse();

		// EJB DI CANCELLAZIONE DELLA CLASSE
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		try {
			factory.getGestioneSemantica().cancellaClasse(
					currentForm.getRicercaClasse().isPolo(), xid,
					utenteCollegato.getTicket());
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
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));

			log.error("", e);

			return mapping.getInputForward();// gestione errori java
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.gestioneSemantica.operOk"));

		return  mapping.findForward("ricerca");
		//Navigation.getInstance(request).goForward(mapping.findForward("ricerca"));
	}



	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		LinkableTagUtils.addError(request, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));

		// nessun codice selezionato
		return mapping.getInputForward();
	}

	private List<?> listaTitoliCollegatiIndice(HttpServletRequest request,
			String cid, ActionForm form) throws Exception {

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
		AreePassaggioSifVO areasif = new AreePassaggioSifVO();

		areasif.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		areasif.setOggettoDaRicercare(cid);
		areasif.setDescOggettoDaRicercare("");
		areasif
				.setOggettoRicerca(TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE);
		areasif.setOggettoChiamante("");
		areasif.setVisualCall(false);

		// CHIAMATA ALL'EJB DI INTERROGAZIONE
		AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloVO();
		areaDatiPass.setOggChiamante(TitoliCollegatiInvoke.ANALITICA_DETTAGLIO);
		areaDatiPass.setTipoOggetto(areasif.getOggettoRicerca());
		areaDatiPass.setNaturaTitBase("");
		areaDatiPass.setTipMatTitBase("");
		areaDatiPass.setCodiceLegame("");
		areaDatiPass.setCodiceSici("");
		areaDatiPass.setOggDiRicerca(areasif.getOggettoDaRicercare());
		areaDatiPass.clear();

		areaDatiPass.setRicercaIndice(true);
		areaDatiPass.setRicercaPolo(false);
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().ricercaTitoli(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null) {

			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));

			return null;
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			//
			// LinkableTagUtils.addError(request, new ActionMessage(
			// "errors.gestioneBibliografica.testoProtocollo",
			// areaDatiPassReturn.getTestoProtocollo()));
			//
			// ActionForward actionForward = new ActionForward();
			return null;

		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			//
			// LinkableTagUtils.addError(request, new ActionMessage(
			// "errors.gestioneBibliografica."
			// + areaDatiPassReturn.getCodErr()));
			//
			return null;
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {
			return null;
		} else {
			currentForm.setNumTitoliIndice(areaDatiPassReturn.getNumNotizie());
		}

		return areaDatiPassReturn.getListaSintetica();
	}

	public ActionForward importa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;

		String codSistemaClassificazione = currentForm.getRicercaClasse().getCodSistemaClassificazione();
		String codEdizione = currentForm.getRicercaClasse().getCodEdizioneDewey();
		ClassiDelegate delegate = ClassiDelegate.getInstance(request);
		if (!delegate.isSistemaGestito(codSistemaClassificazione, codEdizione) ) {

			LinkableTagUtils.addError(request, new ActionMessage(
			"errors.gestioneSemantica.sistemaClasseNonGestito"));

			return mapping.getInputForward();// gestione errori java
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE, codSistemaClassificazione);
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_SISTEMA_CLASSIFICAZIONE, currentForm.getRicercaClasse()
				.getDescSistemaClassificazione());
		request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY, codEdizione);
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY, currentForm.getRicercaClasse()
				.getDescEdizioneDewey());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, new Boolean(currentForm.getRicercaClasse()
				.isPolo()));
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());

		SimboloDeweyVO sd = SimboloDeweyVO.parse(currentForm.getIdentificativoClasse());
		currentForm.setSimbolo(sd.getSimbolo());

		request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE, currentForm.getSimbolo());
		currentForm.getDettClaGen().setIdentificativo(
				currentForm.getIdentificativoClasse());
		currentForm.getDettClaGen().setLivAut(currentForm.getStatoControllo());
		currentForm.getDettClaGen()
				.setUlterioreTermine(currentForm.getUlterioreTermine());
		currentForm.getDettClaGen().setDescrizione(currentForm.getDescrizione());
		currentForm.getDettClaGen().setCampoSistema(
				codSistemaClassificazione);
		currentForm.getDettClaGen().setCampoEdizione(
				codEdizione);
		currentForm.getDettClaGen().setDataAgg(currentForm.getDataModifica());
		currentForm.getDettClaGen().setDataIns(currentForm.getDataInserimento());
		currentForm.getDettClaGen().setIndicatore("NO");
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, currentForm.getDettClaGen());

		if (currentForm.getFolder() != null) {//soggettazione attiva
			request.setAttribute(
					NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
					currentForm.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
					.getFolder());
		}

		return mapping.findForward("importaClasse");
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
		request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());
		String xid = currentForm.getIdentificativoClasse();
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, currentForm.getDescrizione());
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_ARRIVO, xid);
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm.getTestoTrascinaDa());
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA, currentForm
				.getNotazioneTrascinaDa());
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request
				.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm
						.getDatiBibliografici());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaClasse().clone() );
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutput());
		return Navigation.getInstance(request).goForward(
				mapping.findForward("ok"));

	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//almaviva5_20141210
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		EsaminaClasseForm currentForm = (EsaminaClasseForm) form;

		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));

			// nessun codice selezionato
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	private enum TipoAttivita {
		CREA,
		MODIFICA,
		TRASCINA,

		TITOLI_COLL_POLO,
		TITOLI_COLL_POLO_FILTRO,
		TITOLI_COLL_INDICE,
		SIMILI_INDICE;
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		try {
			EsaminaClasseForm currentForm = (EsaminaClasseForm) form;
			TipoAttivita tipoAtt = TipoAttivita.valueOf(idCheck);
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			boolean livelloPolo = currentForm.getDettClaGen().isLivelloPolo();

			switch (tipoAtt) {
			case CREA:
				return delegate.isAbilitato(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);

			case MODIFICA:
				if (!delegate.isAbilitato(CodiciAttivita.getIstance().MODIFICA_CLASSE_1266))
					return false;

				DettaglioClasseVO dettaglio = currentForm.getDettClaGen();

				if (!delegate.isSistemaGestito(dettaglio.getCampoSistema(),	dettaglio.getCampoEdizione()))
					return false;

				//almaviva5_20130128 #5238
				UserVO utente = Navigation.getInstance(request).getUtente();
				SimboloDeweyVO sd = SimboloDeweyVO.parse(dettaglio.getIdentificativo());
				if (!delegate.verificaSistemaEdizionePerBiblioteca(utente.getCodPolo(), utente.getCodBib(),
						sd.getSistema() + sd.getEdizione() ) )
					return false;

				return delegate.isLivAutOk(dettaglio.getLivAut(), false);

			case TRASCINA:
				return delegate.isAbilitato(CodiciAttivita.getIstance().FONDE_CLASSE_1273);

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
