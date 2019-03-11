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

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.EsaminaSoggettoForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
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
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class EsaminaSoggettoAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Log log = LogFactory.getLog(EsaminaSoggettoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.gestione", "gestione");
		map.put("button.importa", "importa");
		map.put("button.polo", "polo");
		map.put("button.biblio", "biblio");
		map.put("button.indice", "indice");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");

		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");
		return map;
	}

	private void initCombo(HttpServletRequest request, ActionForm form) throws Exception {
		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		String ticket = Navigation.getInstance(request).getUserTicket();
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, false));
		currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));
		currentForm.setListaTipoSoggetto(CaricamentoComboSemantica.loadComboCategoriaSoggetto(SbnAuthority.SO));
		//almaviva5_20111127 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		String chiamante = null;
		String dataInserimento = null;
		String dataModifica = null;
		DettaglioSoggettoVO dettaglio = null;
		boolean isPolo = false;
		FolderType folder = null;

		if (!currentForm.isSessione()) {
			log.info("EsaminaSoggettoAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			// inserito = (String)request.getAttribute("inserito");
			// modificato = (String)request.getAttribute("modificato");
			dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			dettaglio = (DettaglioSoggettoVO) request.getAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO);

			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();

			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			currentForm.setSessione(true);
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
			ActionMessages errors = currentForm.validate(mapping, request);
			try {
				this.initCombo(request, currentForm);

			} catch (Exception ex) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.Faild"));
			}
			this.saveErrors(request, errors);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setDettSogGenVO(dettaglio);

			currentForm.getRicercaComune().setPolo(isPolo);

			//combo esamina
			currentForm.setComboGestioneEsamina(LabelGestioneSemantica
					.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
							request, form, new String[]{"SO"}, this));
			currentForm.setIdFunzioneEsamina("");
		}


		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		currentForm.getRicercaComune().setCodSoggettario(
				currentForm.getDettSogGenVO().getCampoSoggettario());
		currentForm.setStatoControllo(currentForm.getDettSogGenVO().getLivAut());
		currentForm.setTipoSoggetto(currentForm.getDettSogGenVO()
				.getCategoriaSoggetto());
		currentForm.setCid(currentForm.getDettSogGenVO().getCid());
		currentForm.setTesto(currentForm.getDettSogGenVO().getTesto() );
		currentForm.setEnableGestione(currentForm.getRicercaComune().isPolo());
		currentForm.setEnableEsamina(currentForm.getRicercaComune().isPolo());
		currentForm.setTreeElementViewSoggetti((TreeElementViewSoggetti) request
				.getAttribute(NavigazioneSemantica.ANALITICA));
		if (currentForm.getRicercaComune().isIndice()) {
			currentForm.setEnableImporta(true);
		}
		if (currentForm.getFolder() != null && currentForm.getFolder()==FolderType.FOLDER_SOGGETTI) {
			currentForm.setEnableTit(true);
		}

		if (currentForm.getRicercaComune().isPolo()) {
			currentForm.setNumTitoliPolo(currentForm.getDettSogGenVO().getNumTitoliPolo());
			if (currentForm.getNumTitoliPolo()== 0){
				currentForm.setEnableNumPolo(false);
			}else {
				currentForm.setEnableNumPolo(true);
			}
			currentForm.setNumTitoliBiblio(currentForm.getDettSogGenVO().getNumTitoliBiblio());
			if (currentForm.getNumTitoliBiblio()== 0){
				currentForm.setEnableNumBiblio(false);
			}else {
				currentForm.setEnableNumBiblio(true);
			}

		}
//		else {
//			List<?> titoliIndice = this.listaTitoliCollegatiIndice(request,
//					currentForm.getCid(), currentForm);
//			if (titoliIndice == null) {
//				currentForm.setEnableNumIndice(false);
//			} else {
//				currentForm.setEnableNumIndice(true);
//			}
//		}
		return mapping.getInputForward();
	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(mapping.findForward("delegate_titoliCollegati"));

	}

	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(mapping.findForward("delegate_titoliCollegatiFiltro"));

	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, utenteCollegato.getCodBib() );
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(mapping.findForward("delegate_titoliCollegati"));
	}

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(mapping.findForward("delegate_titoliCollegatiFiltro"));

	}

	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		resetToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO, currentForm.getDettSogGenVO());

		resetToken(request);
		Navigation.getInstance(request).purgeThis();
		return mapping.findForward("gestioneSoggetto");
	}

	public ActionForward importa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO, currentForm.getDettSogGenVO());

		resetToken(request);
		// request.setAttribute(NavigazioneSemantica.NOTE_OGGETTO, currentForm.getNote());
		currentForm.getRicercaComune().setCodSoggettario(
				currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
		return mapping.findForward("importaSoggetto");
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

	protected List<?> listaTitoliCollegatiPolo(HttpServletRequest request,
			String cid, ActionForm form) throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		AreePassaggioSifVO areasif = new AreePassaggioSifVO();

		areasif.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		areasif.setOggettoDaRicercare(cid);
		areasif.setDescOggettoDaRicercare("");
		areasif
				.setOggettoRicerca(TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO);
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
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return null;
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);

			return null;

		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return null;
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {
			return null;
		} else {
			currentForm.setNumTitoliPolo(areaDatiPassReturn.getNumNotizie());
		}

		return areaDatiPassReturn.getListaSintetica();
	}

	protected List<?> listaTitoliCollegatiBiblio(HttpServletRequest request,
			String cid, ActionForm form) throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		AreePassaggioSifVO areasif = new AreePassaggioSifVO();

		areasif.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		areasif.setCodBiblioteca(TitoliCollegatiInvoke.codBiblio);
		areasif.setOggettoDaRicercare(cid);
		areasif.setDescOggettoDaRicercare("");
		areasif
				.setOggettoRicerca(TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO);
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
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.noConnessione"));
			this.saveErrors(request, errors);
			return null;
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
			this.saveErrors(request, errors);
			return null;

		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneBibliografica."
							+ areaDatiPassReturn.getCodErr()));
			this.saveErrors(request, errors);
			return null;
		}

		if (areaDatiPassReturn.getNumNotizie() == 0) {
			return null;
		} else {
			currentForm.setNumTitoliBiblio(areaDatiPassReturn.getNumNotizie());
		}

		return areaDatiPassReturn.getListaSintetica();
	}

	protected List<?> listaTitoliCollegatiIndice(HttpServletRequest request,
			String cid, ActionForm form) throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		AreePassaggioSifVO areasif = new AreePassaggioSifVO();

		areasif.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		areasif.setOggettoDaRicercare(cid);
		areasif.setDescOggettoDaRicercare("");
		areasif.setOggettoRicerca(TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO);
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
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.noConnessione"));
			return null;
		}

		if (areaDatiPassReturn.getCodErr().equals("9999")
				|| areaDatiPassReturn.getTestoProtocollo() != null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
			return null;

		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + areaDatiPassReturn.getCodErr()));
			return null;
		}

		if (areaDatiPassReturn.getNumNotizie() == 0)
			return null;
		else
			currentForm.setNumTitoliIndice(areaDatiPassReturn.getNumNotizie());

		return areaDatiPassReturn.getListaSintetica();
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	private enum TipoAttivita {
		CREA,
		MODIFICA,

		TITOLI_COLL_POLO,
		TITOLI_COLL_POLO_FILTRO,
		TITOLI_COLL_INDICE,
		SIMILI_INDICE;
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			EsaminaSoggettoForm currentForm = (EsaminaSoggettoForm) form;
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			DettaglioSoggettoVO dettaglio = currentForm.getDettSogGenVO();
			boolean livelloPolo = dettaglio.isLivelloPolo();
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);

			switch (attivita) {
			case CREA:
				if (!dettaglio.isLivelloPolo() ) // niente gestione in indice
					return false;
				if (!delegate.isSoggettarioGestito(dettaglio.getCampoSoggettario() ) )
					return false;
				if (!delegate.isLivAutOkSO(dettaglio.getLivAut(), false))
					return false;
				return delegate.isAbilitatoSO(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);

			case MODIFICA:
				if (!dettaglio.isLivelloPolo() ) // niente gestione in indice
					return false;
				if (!delegate.isSoggettarioGestito(dettaglio.getCampoSoggettario()) )
					return false;
				if (!delegate.isLivAutOkSO(dettaglio.getLivAut(), false))
					return false;
				return delegate.isAbilitatoSO(CodiciAttivita.getIstance().MODIFICA_SOGGETTO_1265);

			case TITOLI_COLL_POLO:
				return livelloPolo;
			case TITOLI_COLL_POLO_FILTRO:
				return true;
			case TITOLI_COLL_INDICE:
				return !livelloPolo;
			}

			return false;

		} catch (Exception e) {
			return false;
		}

	}

}
