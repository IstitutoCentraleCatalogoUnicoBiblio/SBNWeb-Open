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
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.GestioneClasseForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
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


public class GestioneClasseAction extends SinteticaLookupDispatchAction implements
		SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(GestioneClasseAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.ok", "ok");
		map.put("button.polo", "polo");
		map.put("button.biblio", "biblio");
		map.put("button.fondi", "fondi");
		map.put("button.trascina", "trascina");
		map.put("button.elimina", "elimina");
		map.put("button.stampa", "stampa");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.annulla", "annulla");

		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");
		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			GestioneClasseForm currentForm = (GestioneClasseForm) form;
			currentForm
					.setListaSistemiClassificazione(CaricamentoComboSemantica
							.loadComboSistemaClassificazione(ticket, true));
			currentForm.setListaEdizioni(CaricamentoComboSemantica.loadComboEdizioneDeweyPerGestione(request));
			currentForm.setListaStatoControllo(CaricamentoComboSemantica
					.loadComboStato(ClassiDelegate.getInstance(request).getMaxLivelloAutorita()));

			return true;
		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
			log.error("", e);
			// nessun codice selezionato
			return false;
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		String chiamante = null;
		DettaglioClasseVO dettaglio = null;
		boolean isPolo = false;

		if (!currentForm.isSessione()) {

			log.info("GestioneClasseAction::unspecified");
			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			dettaglio = (DettaglioClasseVO) request
					.getAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE);
			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();

			if (chiamante == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
				return mapping.getInputForward();
			}
			currentForm.setSessione(true);
			currentForm.setAction(chiamante);
			currentForm.setDettClaGen(dettaglio);
			currentForm.setDataInserimento(currentForm.getDettClaGen().getDataIns());
			currentForm.setDataModifica(currentForm.getDettClaGen().getDataAgg());
			currentForm.getRicercaClasse().setPolo(isPolo);
			currentForm.getRicercaClasse().setIdentificativoClasse(
					currentForm.getDettClaGen().getIdentificativo());
			currentForm.setUlterioreTermine(currentForm.getDettClaGen()
					.getUlterioreTermine());

			// combo esamina
			currentForm.setComboGestioneEsamina(LabelGestioneSemantica
					.getComboGestioneSematicaPerEsamina(servlet
							.getServletContext(), request, form, new String[]{"CL"}, this));
			currentForm.setIdFunzioneEsamina("");
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		if (!this.initCombo(request, form, navi.getUserTicket()))
			return mapping.getInputForward();

		currentForm.getRicercaClasse().setCodSistemaClassificazione(
				currentForm.getDettClaGen().getCampoSistema());
		currentForm.getRicercaClasse().setCodEdizioneDewey(
				currentForm.getDettClaGen().getCampoEdizione());
		currentForm.setStatoControllo(currentForm.getDettClaGen().getLivAut());
		currentForm.setIdentificativoClasse(currentForm.getDettClaGen()
				.getIdentificativo());
		currentForm
				.setDescrizione(currentForm.getDettClaGen().getDescrizione());
		currentForm.setModificato(false);

		currentForm.setEnableNumPolo(currentForm.getDettClaGen().getNumTitoliPolo() > 0);
		currentForm.setEnableNumBiblio(currentForm.getDettClaGen().getNumTitoliBiblio() > 0);

		if (!currentForm.isEnableNumBiblio()) {
			currentForm.setEnableElimina(true);
		} else {
			currentForm.setEnableElimina(false);
			currentForm.setEnableTrascina(true);
		}

		currentForm.setEnableFondi(false);

		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		CreaVariaClasseVO classe = new CreaVariaClasseVO();
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		DettaglioClasseVO dettaglio = currentForm.getDettClaGen();
		RicercaClassiVO ricercaClasse = currentForm.getRicercaClasse();
		/*
		String deweyEd = null;
		if (!dettaglio.getCampoEdizione().equals(NavigazioneSemantica.EDIZIONE_NON_DEWEY))
			deweyEd = CodiciProvider.SBNToUnimarc(CodiciType.CODICE_EDIZIONE_CLASSE, dettaglio.getCampoEdizione());
		else
			deweyEd = ricercaClasse.getCodEdizioneDewey();

		if (ValidazioneDati.isFilled(dettaglio.getSimbolo()) )
			ricercaClasse.setSimbolo(dettaglio.getSimbolo());

		if (!dettaglio.getCampoEdizione().equals(NavigazioneSemantica.EDIZIONE_NON_DEWEY)) {
			SimboloDeweyVO sd = SimboloDeweyVO.parse(dettaglio.getIdentificativo());
			ricercaClasse.setSimbolo(sd.getSimbolo());
		}
		 */
		SimboloDeweyVO sd = SimboloDeweyVO.parse(dettaglio.getIdentificativo());

		Navigation navi = Navigation.getInstance(request);
		try {
			classe.setCodSistemaClassificazione(ricercaClasse.getCodSistemaClassificazione());
			//classe.setCodEdizioneDewey(deweyEd)
			classe.setCodEdizioneDewey(dettaglio.getCampoEdizione());
			classe.setDescrizione(currentForm.getDescrizione());
			classe.setLivello(currentForm.getStatoControllo());
			classe.setSimbolo(sd.getSimbolo());
			classe.setUlterioreTermine(currentForm.getUlterioreTermine());
			classe.setDataInserimento(currentForm.getDataInserimento());
			classe.setDataVariazione(currentForm.getDataModifica());
			classe.setT005(dettaglio.getT005());
			classe.setT001(dettaglio.getIdentificativo());
			classe.setLivelloPolo(ricercaClasse.isPolo());
			classe.setCondiviso(dettaglio.isCondiviso());
			classe.setCostruito(dettaglio.isCostruito());

			classe = ClassiDelegate.getInstance(request).variaClasse(classe);

			if (classe.getEsitoType() != SbnMarcEsitoType.OK) {

				if (classe.getEsitoType() == SbnMarcEsitoType.TROVATI_SIMILI ) {
					if (currentForm.isEnableNumBiblio() ) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.fondi",	classe.getT001()));
						currentForm.setIdentificativoClasse(classe.getT001());
						currentForm.setDescrizione(classe.getDescrizione());
						currentForm.setEnableFondi(true);
						currentForm.setEnableElimina(false);
						currentForm.setEnableTrascina(false);
						return mapping.getInputForward();
					} else {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.trovatiSimili",	classe.getT001()));
						return mapping.getInputForward();
					}
				} else {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo",	classe.getTestoEsito()));
					return mapping.getInputForward();
				}
			}

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		}
		resetToken(request);
		dettaglio.setCampoSistema(classe.getCodSistemaClassificazione());
		ricercaClasse.setCodEdizioneDewey(classe.getCodEdizioneDewey());
		dettaglio.setDataIns(classe.getDataInserimento());
		dettaglio.setDataAgg(classe.getDataVariazione());
		request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE, ricercaClasse.getSimbolo());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	ricercaClasse.isPolo());
		dettaglio.setLivAut(currentForm.getStatoControllo());
		dettaglio.setDescrizione(classe.getDescrizione());
		dettaglio.setUlterioreTermine(classe.getUlterioreTermine());
		dettaglio.setIndicatore("NO");
		dettaglio.setT005(classe.getT005());
		dettaglio.setUlterioreTermine(classe.getUlterioreTermine());
		dettaglio.setCondiviso(classe.isCondiviso());
		dettaglio.setCostruito(classe.isCostruito());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, dettaglio);

		navi.purgeThis();
		return mapping.findForward("esaminaClasse");

	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//almaviva5_20141210
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());

		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc,
				currentForm.getDescrizione());
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

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());

		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc,
				currentForm.getDescrizione());
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

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, utenteCollegato.getCodBib() );
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc,
				currentForm.getDescrizione());
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

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getIdentificativoClasse());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc,
				currentForm.getDescrizione());
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

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward trascina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneClasseForm currentForm = (GestioneClasseForm) form;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliBiblio =
			this.listaTitoliCollegatiBiblio(request, currentForm.getIdentificativoClasse(),
					Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() ), form);
		currentForm.setTitoliBiblio(titoliBiblio.getListaSintetica());
		currentForm.setDatiBibliografici(titoliBiblio);

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
				currentForm.getIdentificativoClasse());
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
				currentForm.getDescrizione().trim());
		request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA,
				currentForm.getTitoliBiblio());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
				currentForm.getDatiBibliografici());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaClasse().isPolo());
		request.setAttribute(NavigazioneSemantica.ABILITA_TRASCINAMENTO,
				currentForm.isEnableTrascina());
		request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
				new OggettoRiferimentoVO(true, currentForm
						.getIdentificativoClasse(), currentForm
						.getDescrizione().trim()));

		Navigation navi = Navigation.getInstance(request);
		return navi.goForward(mapping.findForward("trascina"));
	}

	public ActionForward fondi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneClasseForm currentForm = (GestioneClasseForm) form;

		AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliBiblio =
			this.listaTitoliCollegatiBiblio(request, currentForm.getIdentificativoClasse(),
					Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() ), form);
		currentForm.setTitoliBiblio(titoliBiblio.getListaSintetica());
		currentForm.setDatiBibliografici(titoliBiblio);

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
				currentForm.getIdentificativoClasse());
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
				currentForm.getDescrizione().trim());
		request.setAttribute(NavigazioneSemantica.ABILITA_TRASCINAMENTO,
				currentForm.isEnableTrascina());
		request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA,
				currentForm.getTitoliBiblio());
		request.setAttribute(
				NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE,
				currentForm.getRicercaClasse().getCodSistemaClassificazione());
		request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY,
				currentForm.getRicercaClasse().getCodEdizioneDewey());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaClasse().isPolo());
		return mapping.findForward("fondi");
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.cancCla"));
		this.saveToken(request);
		currentForm.setEnableConferma(true);

		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		String xid = currentForm.getRicercaClasse().getIdentificativoClasse();

		// EJB DI CANCELLAZIONE DELLA CLASSE
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		try {
			factory.getGestioneSemantica().cancellaClasse(
					currentForm.getRicercaClasse().isPolo(), xid,
					utenteCollegato.getTicket());

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			// nessun codice selezionato
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));

		return mapping.findForward("ricerca");

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		GestioneClasseForm currentForm = (GestioneClasseForm) form;

		currentForm.setEnableConferma(false);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaClasse().isPolo());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, currentForm
				.getDettClaGen());
		ActionForward action = new ActionForward();
		action.setName("back");
		action.setPath(currentForm.getAction() + ".do");
		return action;
	}

	private AreaDatiPassaggioInterrogazioneTitoloReturnVO listaTitoliCollegatiBiblio(
			HttpServletRequest request, String identificativoClasse,
			int maxRighe, ActionForm form) throws Exception {

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		AreePassaggioSifVO areasif = new AreePassaggioSifVO();

		areasif.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		areasif.setCodBiblioteca(TitoliCollegatiInvoke.codBiblio);
		areasif.setOggettoDaRicercare(identificativoClasse);
		areasif.setDescOggettoDaRicercare("");
		areasif.setOggettoRicerca(TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE);
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

		areaDatiPass.setRicercaIndice(false);
		areaDatiPass.setRicercaPolo(true);
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().ricercaTitoli(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			return null;
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {

			return null;

		} else if (SbnMarcEsitoType.of(areaDatiPassReturn.getCodErr()) != SbnMarcEsitoType.OK) {

			return null;
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {
			return null;
		} else {
			currentForm.setNumTitoliBiblio(areaDatiPassReturn.getNumNotizie());
			addSbnMarcIdLista(request, areaDatiPassReturn.getIdLista());
		}

		return areaDatiPassReturn;
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//almaviva5_20141210
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		GestioneClasseForm currentForm = (GestioneClasseForm) form;
		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	private enum TipoAttivita {
		CANCELLA, TRASCINA,

		TITOLI_COLL_POLO, TITOLI_COLL_POLO_FILTRO, TITOLI_COLL_INDICE, SIMILI_INDICE;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		try {
			GestioneClasseForm currentForm = (GestioneClasseForm) form;
			TipoAttivita tipoAtt = TipoAttivita.valueOf(idCheck);
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			DettaglioClasseVO dettaglio = currentForm.getDettClaGen();
			boolean livelloPolo = dettaglio.isLivelloPolo();

			switch (tipoAtt) {
			case CANCELLA:
				if (dettaglio.getNumTitoliBiblio() > 0 || dettaglio.getNumTitoliPolo() > 0)
					return false;
				return delegate
						.isAbilitato(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028);
				// return delegate
				//.isAbilitato(CodiciAttivita.getIstance().CANCELLA_CLASSE_1280)
				// ;
			case TRASCINA:
				return delegate
						.isAbilitato(CodiciAttivita.getIstance().FONDE_CLASSE_1273);

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
