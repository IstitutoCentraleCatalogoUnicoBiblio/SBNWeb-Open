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

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.ModalitaConfermaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.GestioneSoggettoForm;
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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;


public class GestioneSoggettoAction extends LookupDispatchAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(GestioneSoggettoAction.class);

	public static final String TRASCINA_IMMEDIATE = "gs.trascina.immediate";
	public static final String CANCELLA_IMMEDIATE = "gs.elimina.sogg";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.ok", "ok");
		map.put("button.polo", "polo");
		map.put("button.crea", "crea");
		map.put("button.biblio", "biblio");
		map.put("button.indice", "indice");
		map.put("button.trascina", "trascina");
		map.put("button.fondi", "fondi");
		//almaviva5_20110928 #4643
		map.put("button.cancella", "elimina");
		map.put("button.elimina", "elimina");
		map.put("button.si", "si");
		map.put("button.no", "no");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");

		map.put("button.esamina", "esamina");
		return map;
	}

	private void initCombo(HttpServletRequest request, ActionForm form) throws Exception {
		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
		String ticket = Navigation.getInstance(request).getUserTicket();
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
		currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(SoggettiDelegate.getInstance(request).getMaxLivelloAutoritaSO() ));
		currentForm.setListaTipoSoggetto(CaricamentoComboSemantica.loadComboCategoriaSoggetto(SbnAuthority.SO));
		//almaviva5_20111125 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;

		if (navi.isFromBack() )
			currentForm.setAbilita(true);

		String dataInserimento = null;
		String dataModifica = null;
		String chiamante = null;
		DettaglioSoggettoVO dettaglio = null;
		boolean isPolo = false;

		if (!currentForm.isSessione()) {
			log.info("GestioneSoggettoAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			dettaglio = (DettaglioSoggettoVO) request.getAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO);

			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();

			if (chiamante == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
				return mapping.getInputForward();
			}
			currentForm.setSessione(true);
			currentForm.setAction(chiamante);

			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setDettSogGenVO(dettaglio);

			currentForm.getRicercaComune().setPolo(isPolo);

			navi.setAttribute(NavigazioneSemantica.ACTION_CALLER, chiamante);

			//combo esamina
			currentForm.setComboGestioneEsamina(LabelGestioneSemantica
					.getComboGestioneSematicaPerEsamina(servlet.getServletContext(),
							request, form, new String[]{"SO"}, this));
			currentForm.setIdFunzioneEsamina("");

			ParametriSoggetti parametri = ParametriSoggetti.retrieve(request);
			if (parametri != null)
				currentForm.setParametriSogg(parametri);
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		try {
			this.initCombo(request, currentForm);

		} catch (Exception ex) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
		}

		DettaglioSoggettoVO dettSogGenVO = currentForm.getDettSogGenVO();
		currentForm.getRicercaComune().setCodSoggettario(
				dettSogGenVO.getCampoSoggettario());
		currentForm.setStatoControllo(dettSogGenVO.getLivAut());
		currentForm.setTipoSoggetto(dettSogGenVO.getCategoriaSoggetto());
		currentForm.setCid(dettSogGenVO.getCid());
		currentForm.setTesto(dettSogGenVO.getTesto().trim());
		currentForm.setNote(dettSogGenVO.getNote());
		currentForm.setOldTesto(dettSogGenVO.getTesto().trim());

		currentForm.setNumTitoliPolo(dettSogGenVO.getNumTitoliPolo());
		if (currentForm.getNumTitoliPolo() == 0) {
			currentForm.setEnableNumPolo(false);
			currentForm.setEnableElimina(true);

		} else {
			currentForm.setEnableNumPolo(true);
			currentForm.setEnableElimina(false);
		}
		currentForm.setNumTitoliBiblio(dettSogGenVO
				.getNumTitoliBiblio());
		if (currentForm.getNumTitoliBiblio() == 0) {
			currentForm.setEnableElimina(true);
			currentForm.setEnableNumBiblio(false);
		} else {
			currentForm.setEnableNumBiblio(true);
			currentForm.setEnableElimina(false);
		}

//		AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliBiblio = this
//				.listaTitoliCollegatiBiblio(request, currentForm.getCid(), 10);
//		if (titoliBiblio != null) {
//			currentForm.setTitoliBiblio(titoliBiblio.getListaSintetica());
//		}

		if (currentForm.isEnableNumBiblio())
			currentForm.setEnableTrascina(true);

		currentForm.setEnableFondi(false);
		currentForm.setEnableOk(true);
		//currentForm.setDatiBibliografici(titoliBiblio);

		//passaggio diretto da analitica a trascina
		if (request.getAttribute(GestioneSoggettoAction.TRASCINA_IMMEDIATE) != null) {
			navi.purgeThis();
			return trascina(mapping, form, request, response);
		}

		if (Navigation.getInstance(request).getAttribute(GestioneSoggettoAction.CANCELLA_IMMEDIATE) != null)
		//if (request.getAttribute(GestioneSoggettoAction.CANCELLA_IMMEDIATE) != null)
			return elimina(mapping, form, request, response);

		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
		CreaVariaSoggettoVO soggetto = new CreaVariaSoggettoVO();

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		soggetto.setCid(currentForm.getCid());
		soggetto.setCodiceSoggettario(currentForm.getRicercaComune().getCodSoggettario());
		soggetto.setTesto(currentForm.getTesto().trim());
		soggetto.setLivello(currentForm.getStatoControllo());
		soggetto.setTipoSoggetto(currentForm.getTipoSoggetto());
		soggetto.setNote(currentForm.getNote());
		soggetto.setDataInserimento(currentForm.getDataInserimento());
		soggetto.setDataVariazione(currentForm.getDataModifica());
		DettaglioSoggettoVO dettaglio = currentForm.getDettSogGenVO();
		soggetto.setT005(dettaglio.getT005());
		//almaviva5_20120322 evolutive CFI
		soggetto.setEdizioneSoggettario(dettaglio.getEdizioneSoggettario());
		if (!dettaglio.isCondiviso()) {
			soggetto.setCondiviso(false);
			currentForm.setFlagCancellaCondiviso(false);
		} else {
			soggetto.setCondiviso(true);
			currentForm.setFlagCancellaCondiviso(true);
		}
		soggetto.setLivelloPolo(currentForm.getRicercaComune().isPolo());

		// se sono in indice non vado oltre
		if (!soggetto.isLivelloPolo()) {
			//altro errore SBNMarc
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.SBNMarc", soggetto.getTestoEsito() ));
			return mapping.getInputForward();
		}

		CreaVariaSoggettoVO soggettoVariato = SoggettiDelegate.getInstance(request).variaSoggetto(soggetto);
		if (soggettoVariato == null)
			return mapping.getInputForward();

		if (soggettoVariato.getEsitoType() != SbnMarcEsitoType.OK) {

			if (ValidazioneDati.in(soggettoVariato.getEsitoType(),
					SbnMarcEsitoType.TROVATI_SIMILI,
					SbnMarcEsitoType.TROVATO_SIMILE_EDIZIONE_DIVERSA) )
				return gestioneSimili(mapping, currentForm, request, response, soggettoVariato);

			//altro errore SBNMarc
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.SBNMarc", soggettoVariato.getTestoEsito() ));
			return mapping.getInputForward();
		}


		currentForm.setCodice(currentForm.getRicercaComune().getCodSoggettario());
		currentForm.setDataInserimento(soggettoVariato.getDataInserimento());
		currentForm.setDataModifica(soggettoVariato.getDataVariazione());
		resetToken(request);
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm.getStatoControllo());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID, currentForm.getCid());
		request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE, NavigazioneSemantica.TIPO_OGGETTO_CID);

		return mapping.findForward("analiticasoggetto");

	}

	private ActionForward gestioneSimili(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, CreaVariaSoggettoVO soggetto) throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
		List<ElementoSinteticaSoggettoVO> listaSimili = soggetto.getListaSimili();
		if (ValidazioneDati.size(listaSimili) > 1) {
			// errore troppi soggetti
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruenzaSogg"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);

		ElementoSinteticaSoggettoVO simile = ValidazioneDati.first(listaSimili);
		//almaviva5_20131009
//		if (!delegate.isLivAutOkSO(simile.getStato(), true) )
//			return mapping.getInputForward();

		switch (soggetto.getEsitoType()) {
		case TROVATI_SIMILI:
			// il soggetto é legato a titoli?
			if (currentForm.getNumTitoliBiblio() > 0 || currentForm.getNumTitoliPolo() > 0) {

				//CONTROLLO FONDI
				if (!delegate.isAbilitatoSO(CodiciAttivita.getIstance().FONDE_SOGGETTO_1272) ) {
					LinkableTagUtils.addError(request, new ActionMessage("error.authentication.non_abilitato"));
					return mapping.getInputForward();
				}

				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.fondi", simile.getCid()));
				currentForm.setCidFusione(simile.getCid());
				currentForm.setTestoCidFusione(simile.getTesto());
				currentForm.setEnableFondi(true);
				currentForm.setEnableElimina(false);
				currentForm.setEnableOk(false);
				currentForm.setEnableTrascina(false);
				currentForm.setAbilita(false);
				return mapping.getInputForward();

			} else { // non legato a titoli

				//CONTROLLO MODIFICA E CANCELLAZIONE
				if (!delegate.isAbilitatoSO(CodiciAttivita.getIstance().MODIFICA_SOGGETTO_1265) ||
						!delegate.isAbilitatoSO(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028) ) {
					LinkableTagUtils.addError(request, new ActionMessage("error.authentication.non_abilitato"));
					return mapping.getInputForward();
				}

				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.trovatoECancella", simile.getCid()));
				currentForm.setEnableCrea(false);
				currentForm.setEnableFondi(false);
				currentForm.setEnableElimina(true);
				currentForm.setEnableOk(false);
				currentForm.setEnableTrascina(false);
				currentForm.setTesto(currentForm.getOldTesto());
				currentForm.setAbilita(false);
				return mapping.getInputForward();
			}

		case TROVATO_SIMILE_EDIZIONE_DIVERSA:
			/*
			if (ValidazioneDati.equals(simile.getEdizioneSoggettario(), "E") ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.soggetto.simile.tutte.edizioni"));
				return mapping.getInputForward();
			}
			*/
			if (currentForm.getNumTitoliBiblio() > 0 || currentForm.getNumTitoliPolo() > 0) {
				//CONTROLLO FONDI
				if (!delegate.isAbilitatoSO(CodiciAttivita.getIstance().FONDE_SOGGETTO_1272) ) {
					LinkableTagUtils.addError(request, new ActionMessage("error.authentication.non_abilitato"));
					return mapping.getInputForward();
				}

				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.soggetto.fondi.cambio.edizione", simile.getCid(),
						CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_EDIZIONE_SOGGETTARIO, simile.getEdizioneSoggettario())) );
				currentForm.setEnableConferma(true);
				currentForm.setCidFusione(simile.getCid());
				currentForm.getParametriSogg().put(SoggettiParamType.MODALITA_CONFERMA, ModalitaConfermaType.FONDI_CAMBIO_EDIZIONE);
				this.preparaConferma(mapping, request);
				return mapping.getInputForward();
			}

			//CONTROLLO MODIFICA E CANCELLAZIONE
			if (!delegate.isAbilitatoSO(CodiciAttivita.getIstance().MODIFICA_SOGGETTO_1265) ||
					!delegate.isAbilitatoSO(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028) ) {
				LinkableTagUtils.addError(request, new ActionMessage("error.authentication.non_abilitato"));
				return mapping.getInputForward();
			}

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.soggetto.cambio.edizione.conferma", simile.getCid(),
				CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_EDIZIONE_SOGGETTARIO, simile.getEdizioneSoggettario())) );
			currentForm.setEnableConferma(true);
			currentForm.getParametriSogg().put(SoggettiParamType.MODALITA_CONFERMA, ModalitaConfermaType.CAMBIO_EDIZIONE);
			currentForm.getParametriSogg().put(SoggettiParamType.SOGGETTO_SIMILE, simile);

			this.preparaConferma(mapping, request);
			return mapping.getInputForward();

		}

		return mapping.getInputForward();
	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// Viene settato il token per le transazioni successive

		if (currentForm.getDettSogGenVO().isLivelloPolo())
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.cancSog"));
		else
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.cancSogIndice"));

		currentForm.getParametriSogg().put(SoggettiParamType.MODALITA_CONFERMA, ModalitaConfermaType.CANCELLA_SOGGETTO);

		this.saveToken(request);
		currentForm.setEnableConferma(true);
		currentForm.setAbilita(false);
		this.preparaConferma(mapping, request);
		return mapping.getInputForward();
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		ParametriSoggetti parametri = currentForm.getParametriSogg();
		ModalitaConfermaType mct = (ModalitaConfermaType) parametri.get(SoggettiParamType.MODALITA_CONFERMA);

		try {
			RicercaComuneVO ricerca = currentForm.getRicercaComune();
			switch (mct) {
			case CANCELLA_SOGGETTO:
				currentForm.setAbilita(true);
				factory.getGestioneSemantica().cancellaSoggettoDescrittore(
						ricerca.isPolo(), currentForm.getCid(),
						utenteCollegato.getTicket(), SbnAuthority.SO);
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
				request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, ricerca.clone());
				request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, ricerca.getCodSoggettario());
				request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, ricerca.getDescSoggettario());
				request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	ricerca.isPolo());

				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));
				return mapping.findForward("ricerca");

			case CAMBIO_EDIZIONE:
				currentForm.setEnableConferma(false);
				ElementoSinteticaSoggettoVO simile = (ElementoSinteticaSoggettoVO) parametri.get(SoggettiParamType.SOGGETTO_SIMILE);
				SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
				CreaVariaSoggettoVO result = (CreaVariaSoggettoVO) delegate.eseguiCambioEdizioneSoggetto(currentForm.getDettSogGenVO(), simile.getCid());
				if (result == null)
					return mapping.getInputForward();

				AnaliticaSoggettoVO analitica = delegate.caricaReticoloSoggetto(currentForm.getDettSogGenVO().isLivelloPolo(), result.getCid());
				if (analitica == null)
					return mapping.getInputForward();
				if (analitica.isEsitoOk()) {
					request.setAttribute(NavigazioneSemantica.ANALITICA, analitica.getReticolo());
					return mapping.findForward("analiticasoggetto");
				}

				//errore
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", analitica.getTestoEsito()));
				return mapping.getInputForward();

			case FONDI_CAMBIO_EDIZIONE:
				currentForm.setEnableConferma(false);
				ParametriSoggetti.send(request, parametri);
				return fondi(mapping, currentForm, request, response);
			}

		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, e);
			log.error("", e);
			// nessun codice selezionato
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			// nessun codice selezionato
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		}

		return mapping.getInputForward();

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
		currentForm.setCodice(currentForm.getRicercaComune().getCodSoggettario());
		resetToken(request);
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm.getStatoControllo());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID, currentForm.getCid());
		request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE, NavigazioneSemantica.TIPO_OGGETTO_CID);
		return mapping.findForward("analiticasoggetto");

	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto().trim());
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

	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto().trim());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("delegate_titoliCollegatiFiltro"));
	}

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, utenteCollegato.getCodBib() );
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto().trim());
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

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		// per quanto riguarda il cid è quello della mappa
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCid());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm
				.getTesto().trim());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_SOGGETTO));
		return Navigation.getInstance(request).goForward(
				mapping.findForward("delegate_titoliCollegatiFiltro"));
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	public ActionForward trascina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliBiblio = this
			.listaTitoliCollegatiBiblio(request, currentForm.getCid(), 10);
		if (titoliBiblio != null) {
			currentForm.setTitoliBiblio(titoliBiblio.getListaSintetica());
			currentForm.setDatiBibliografici(titoliBiblio);
		}
		else
			return mapping.getInputForward();// gestione errori java

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
		request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm.getCid());
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm.getTesto().trim());
		request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm.getDatiBibliografici());
		request.setAttribute(NavigazioneSemantica.ABILITA_TRASCINAMENTO, currentForm.isEnableTrascina());

		DettaglioSoggettoVO dettSogGenVO = currentForm.getDettSogGenVO();
		request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
				new OggettoRiferimentoVO(true, dettSogGenVO.getCid(), dettSogGenVO.getTesto()) );

		ParametriSoggetti parametri = currentForm.getParametriSogg().copy();
		parametri.put(SoggettiParamType.DETTAGLIO_ID_PARTENZA, dettSogGenVO);
		ParametriSoggetti.send(request, parametri);

		return Navigation.getInstance(request).goForward(mapping.findForward("trascina"));
	}

	public ActionForward fondi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm.getCid());
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm.getDettSogGenVO().getTesto().trim());
		request.setAttribute(NavigazioneSemantica.ABILITA_TRASCINAMENTO, currentForm.isEnableFondi());
		if (currentForm.isFlagNuovoEFondi()) {
			try {
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

				CreaVariaSoggettoVO soggetto = new CreaVariaSoggettoVO();
				soggetto.setTesto(currentForm.getTesto());
				soggetto.setLivello(currentForm.getStatoControllo());
				soggetto.setLivelloPolo(true);
				soggetto.setTipoSoggetto(currentForm.getTipoSoggetto());
				soggetto.setCodiceSoggettario(currentForm.getRicercaComune().getCodSoggettario());
				soggetto = factory.getGestioneSemantica().creaSoggetto(soggetto, utenteCollegato.getTicket());
				if (soggetto.isEsitoOk() ) {
					request.setAttribute(NavigazioneSemantica.TRASCINA_CID_ARRIVO, soggetto.getCid());
					request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, currentForm.getTesto().trim());
				}
			} catch (ValidationException e) {
				// errori indice
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return mapping.getInputForward();

			} catch (DataException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return mapping.getInputForward();// gestione errori java

			} catch (InfrastructureException e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
				log.error("", e);
				return mapping.getInputForward();// gestione errori java

			} catch (Exception e) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
				log.error("", e);
				return mapping.getInputForward();// gestione errori java
			}
		} else {
			AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliBiblio = this
					.listaTitoliCollegatiBiblio(request, currentForm.getCid(), 10);
			if (titoliBiblio != null) {
				currentForm.setTitoliBiblio(titoliBiblio.getListaSintetica());
				currentForm.setDatiBibliografici(titoliBiblio);
			} else
				return mapping.getInputForward();// gestione errori java

			request.setAttribute(NavigazioneSemantica.TRASCINA_CID_ARRIVO, currentForm.getCidFusione());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, currentForm.getTesto().trim());
		}
		request.setAttribute("cancellaCondiviso", new Boolean(currentForm.isFlagCancellaCondiviso()));
		request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone() );
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm.getDatiBibliografici());

		return mapping.findForward("fondi");
	}

	public ActionForward stampa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		return mapping.getInputForward();
	}


	private AreaDatiPassaggioInterrogazioneTitoloReturnVO listaTitoliCollegatiBiblio(
			HttpServletRequest request, String cid, int maxRighe)
			throws Exception {

		AreePassaggioSifVO areasif = new AreePassaggioSifVO();

		areasif.setLivelloRicerca(TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		areasif.setCodBiblioteca(TitoliCollegatiInvoke.codBiblio);
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

		if (areaDatiPassReturn.getCodErr().equals("9999") || areaDatiPassReturn.getTestoProtocollo() != null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica.testoProtocollo",
					areaDatiPassReturn.getTestoProtocollo()));
			return null;

		} else if (!areaDatiPassReturn.getCodErr().equals("0000")) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneBibliografica." + areaDatiPassReturn.getCodErr()));
			return null;
		}

		if (areaDatiPassReturn.getNumNotizie() == 0)
			return null;


		return areaDatiPassReturn;
	}

	private void preparaConferma(ActionMapping mapping,
			HttpServletRequest request) {
		ActionMessages messages = new ActionMessages();
		ActionMessage msg1 = new ActionMessage("button.parameter", mapping
				.getParameter());
		messages.add("gestionesemantica.parameter.conferma", msg1);
		this.saveMessages(request, messages);
	}

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
		try {
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			CreaVariaSoggettoVO soggetto = new CreaVariaSoggettoVO();
			soggetto.setTesto(currentForm.getTesto());
			soggetto.setLivello(currentForm.getStatoControllo());
			soggetto.setLivelloPolo(true);
			soggetto.setTipoSoggetto(currentForm.getTipoSoggetto());
			soggetto.setCodiceSoggettario(currentForm.getRicercaComune()
					.getCodSoggettario());
			soggetto = factory.getGestioneSemantica().creaSoggetto(soggetto, utenteCollegato.getTicket());
			if (soggetto.isEsitoOk() ) {
				currentForm.setNewCid(soggetto.getCid());
				currentForm.setOldCid(currentForm.getCid());
				factory.getGestioneSemantica().cancellaSoggettoDescrittore(
						currentForm.getRicercaComune().isPolo(), currentForm.getCid(),
						utenteCollegato.getTicket(),
						SbnAuthority.SO);
			}
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();// gestione errori java

		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();// gestione errori java

		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}

		currentForm.setCodice(currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, currentForm.getStatoControllo());
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, currentForm.getTipoSoggetto());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, currentForm.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, currentForm.getDataModifica());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, currentForm.getRicercaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID,
				currentForm.getNewCid());
		request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
				NavigazioneSemantica.TIPO_OGGETTO_CID);
		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.okCreaECancella",
				currentForm.getNewCid(), currentForm.getOldCid()));

		return mapping.findForward("analiticasoggetto");
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
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
		TRASCINA,
		CANCELLA,

		TITOLI_COLL_POLO,
		TITOLI_COLL_POLO_FILTRO,
		TITOLI_COLL_INDICE,
		SIMILI_INDICE;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			GestioneSoggettoForm currentForm = (GestioneSoggettoForm) form;
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			boolean livelloPolo = currentForm.getDettSogGenVO().isLivelloPolo();
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);

			switch (attivita) {
			case CREA:
				return delegate.isAbilitatoSO(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);
			case CANCELLA:
				return delegate.isAbilitatoSO(CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028);
			case TRASCINA:
				return delegate.isAbilitatoSO(CodiciAttivita.getIstance().FONDE_SOGGETTO_1272);

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
