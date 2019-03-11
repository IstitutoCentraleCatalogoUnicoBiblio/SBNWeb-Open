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

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.ImportaSoggettoForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
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
import org.apache.struts.actions.LookupDispatchAction;

public class ImportaSoggettoAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(ImportaSoggettoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.importa", "importa");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private boolean initCombo(HttpServletRequest request, String ticket,
			ActionForm form) throws Exception {
		try {
			ImportaSoggettoForm currentForm = (ImportaSoggettoForm) form;
			currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
			//almaviva5_20120725 limite liv.aut. al bibliotecario operante
			currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(SoggettiDelegate.getInstance(request).getMaxLivelloAutoritaSO()));
			currentForm.setListaTipoSoggetto(CaricamentoComboSemantica.loadComboCategoriaSoggetto(SbnAuthority.SO));
			//almaviva5_20120404 evolutive CFI
			currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
			return true;

		} catch (Exception ex) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
			return false;
		}
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ImportaSoggettoForm currentForm = (ImportaSoggettoForm) form;

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		String chiamante = null;
		// String inserito = null;
		// String modificato = null;
		String dataInserimento = null;
		String dataModifica = null;
		DettaglioSoggettoVO dettaglio = null;
		RicercaSoggettoListaVO currentFormLista = null;
		boolean isPolo = false;

		if (!currentForm.isSessione()) {
			log.info("ImportaSoggettoAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)

			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			dataInserimento = (String) request
					.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
			currentFormLista = (RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			dettaglio = (DettaglioSoggettoVO) request
					.getAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO);

			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();
			currentForm.setDescrizioneSoggettario((String) request
					.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO));

			if (chiamante == null) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.FunzChiamNonImp"));
				return mapping.getInputForward();
			}

			if (!initCombo(request, Navigation.getInstance(request).getUserTicket(), form))
				return mapping.getInputForward();

			currentForm.setSessione(true);
			currentForm.setAction(chiamante);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setDettSogGenVO(dettaglio);
			currentForm.setOutput(currentFormLista);
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
				currentForm.setFolder((FolderType) request
						.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE));
			}
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.getRicercaComune().setPolo(isPolo);

		currentForm.getRicercaComune().setCodSoggettario(
				currentForm.getDettSogGenVO().getCampoSoggettario());
		currentForm.setCodStatoControllo(currentForm.getDettSogGenVO()
				.getLivAut());
		currentForm.setCodTipoSoggetto(currentForm.getDettSogGenVO()
				.getCategoriaSoggetto());
		currentForm.setCid(currentForm.getDettSogGenVO().getCid());
		currentForm.setTesto(currentForm.getDettSogGenVO()
				.getTesto().trim());
		currentForm
				.setTreeElementViewSoggetti((TreeElementViewSoggetti) request
						.getAttribute(NavigazioneSemantica.ANALITICA));
		if (currentForm.getFolder() != null
				&& currentForm.getFolder() == FolderType.FOLDER_SOGGETTI) {
			currentForm.setEnableTit(true);
		}
		currentForm.setModificato(false);

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

		ImportaSoggettoForm currentForm = (ImportaSoggettoForm) form;
		Navigation navi = Navigation.getInstance(request);

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		try {
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			AnaliticaSoggettoVO soggettoDaIndice = delegate.importaSoggettoDaIndice(currentForm.getDettSogGenVO());
			if (soggettoDaIndice == null)
				return mapping.getInputForward();

			if (soggettoDaIndice.isEsitoOk()) {

				DettaglioSoggettoVO dettaglio = soggettoDaIndice.getDettaglio();

				request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO, dettaglio);

				request.setAttribute(NavigazioneSemantica.ANALITICA, soggettoDaIndice.getReticolo());
				String livelloAut = soggettoDaIndice.getReticolo().getLivelloAutorita();
				request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, livelloAut);

				String tipoSoggetto = soggettoDaIndice.getReticolo().getCategoriaSoggetto();
				request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, tipoSoggetto);
				String testo = soggettoDaIndice.getReticolo().getTesto();
				request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE, testo);
				request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, dettaglio.getCid());
				request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, true);

				request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune().getCodSoggettario());
				request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO, currentForm.getDescrizioneSoggettario());
				String dataInserimento = soggettoDaIndice.getDataInserimento();
				request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, dataInserimento);
				String dataVariazione = soggettoDaIndice.getDataVariazione();
				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dataVariazione);
				request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, dataVariazione);
				request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE, dettaglio.isCondiviso());
				request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_POLO, dettaglio.getNumTitoliPolo());
				request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_BIBLIO, dettaglio.getNumTitoliBiblio());

				currentForm.setEnableNumPolo(false);
				request.setAttribute("isNumPolo", currentForm.isEnableNumPolo());
				currentForm.setEnableNumBiblio(false);
				request.setAttribute("isNumBiblio", currentForm.isEnableNumBiblio());
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
				request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,	currentForm.getAreaDatiPassBiblioSemanticaVO());
				request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
				if (currentForm.getFolder() != null && currentForm.getFolder() == FolderType.FOLDER_SOGGETTI) {
					navi.purgeThis();
					return mapping.findForward("catalogazioneSemantica");
				} else {
					if (currentForm.getTitoliBiblio() != null) {
						request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());
						request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, testo);
						request.setAttribute(NavigazioneSemantica.TRASCINA_CID_ARRIVO, dettaglio.getCid());
						request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm.getTestoTrascinaDa());
						request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, currentForm.getCidTrascinaDa());
						request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
						request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutput());
						request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm.getDatiBibliografici());
						currentForm.getRicercaComune().setPolo(true);

						request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaComune().clone());
						navi.purgeThis();
						return navi.goForward(mapping.findForward("trascina"));
					} else {
						navi.purgeThis();
						return mapping.findForward("ok");
					}

				}
			}
			// errore SBNMarc

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.SBNMarc", soggettoDaIndice.getTestoEsito()));
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (InfrastructureException e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
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

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noImpl"));
		// nessun codice selezionato
		return mapping.getInputForward();
	}



}
