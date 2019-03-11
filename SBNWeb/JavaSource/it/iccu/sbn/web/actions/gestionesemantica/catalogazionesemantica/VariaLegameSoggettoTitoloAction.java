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
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO.LegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.VariaLegameSoggettoTitoloForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate.RicercaSoggettoResult;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
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

public class VariaLegameSoggettoTitoloAction extends LookupDispatchAction {

	private static Log log = LogFactory
			.getLog(VariaLegameSoggettoTitoloAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.ok", "ok");
		map.put("button.stampa", "stampa");
		map.put("button.invioIndice", "invioInIndice");
		map.put("button.chiudi", "chiudi");

		return map;
	}

	private void initCombo(String ticket, ActionForm form) throws Exception {
		VariaLegameSoggettoTitoloForm currentForm = (VariaLegameSoggettoTitoloForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
		currentForm.setListaStatoControllo(CaricamentoComboSemantica.loadComboStato(null));
		currentForm.setListaTipoSoggetto(CaricamentoComboSemantica.loadComboCategoriaSoggetto(SbnAuthority.SO));
		//almaviva5_20111123 evolutive CFI
		currentForm.setListaEdizioni(CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VariaLegameSoggettoTitoloForm currentForm = (VariaLegameSoggettoTitoloForm) form;
		// setto il token per le transazioni successive
		this.saveToken(request);

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();


		String codice = null;
		String descrizione = null;
		String cid = null;
		String testo = null;
		String tipoSoggetto = null;
		String statoControllo = null;
		String chiamante = null;
		String dataInserimento = null;
		String dataModifica = null;

		if (!currentForm.isSessione()) {
			log.info("VariaLegameSoggettoTitoloAction::unspecified");
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			codice = (String) request.getAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO);
			descrizione = (String) request.getAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO);
			cid = (String) request.getAttribute(NavigazioneSemantica.CID_RIFERIMENTO);
			testo = (String) request.getAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE);
			tipoSoggetto = (String) request.getAttribute(NavigazioneSemantica.TIPO_SOGGETTO);
			statoControllo = (String) request.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA);
			dataInserimento = (String) request.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
			dataModifica = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);

			DettaglioSoggettoVO dettaglio =
				(DettaglioSoggettoVO) request.getAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO);
			currentForm.setDettaglio(dettaglio);

			chiamante = navi.getActionCaller();
			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.getRicercaComune().setCodSoggettario(codice);
			currentForm.getRicercaComune().setDescSoggettario(descrizione);
			currentForm.setDataInserimento(dataInserimento);
			currentForm.setDataModifica(dataModifica);
			currentForm.setAction(chiamante);
			currentForm
					.setAreaDatiPassBiblioSemanticaVO((AreaDatiPassBiblioSemanticaVO) request
							.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA));
			currentForm.getCatalogazioneSemanticaComune().setBid(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getBidPartenza());
			currentForm.getCatalogazioneSemanticaComune().setTestoBid(
					currentForm.getAreaDatiPassBiblioSemanticaVO()
							.getDescPartenza());
			DettaglioTitoloParteFissaVO detTitoloPFissaVO =
				currentForm.getAreaDatiPassBiblioSemanticaVO()
					.getTreeElement()
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO()
					.getDetTitoloPFissaVO();

			currentForm.getCatalogazioneSemanticaComune().setLivAutBid(
					detTitoloPFissaVO.getLivAut());
			currentForm.getCatalogazioneSemanticaComune().setNaturaBid(
					detTitoloPFissaVO.getNatura());
			currentForm.getCatalogazioneSemanticaComune()
					.setTipoMaterialeBid(
							detTitoloPFissaVO.getTipoMat());
			currentForm.getCatalogazioneSemanticaComune().setT005(
					detTitoloPFissaVO.getVersione());
			// ATTENZIONE : ATTUALMENTE IL PROTOCOLLO NON GESTISCE LE NOTE AL
			// LEGAME TITOLO/SOGGETTO NELL'ANALITICA!!!!
			TreeElementViewTitoli titolo = (TreeElementViewTitoli) currentForm
					.getAreaDatiPassBiblioSemanticaVO().getTreeElement()
					.findElement(cid);
			currentForm.setNotaAlLegame(titolo.getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO().getNotaAlLegame());
			ActionMessages errors = new ActionMessages();
			try {
				this.initCombo(navi.getUserTicket(), currentForm);
				currentForm.setStatoControllo(statoControllo);
				currentForm.setTipoSoggetto(tipoSoggetto);
			} catch (Exception e) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
				this.saveErrors(request, errors);
				log.error("", e);
				// nessun codice selezionato
				return mapping.getInputForward();
			}

		}
		currentForm.getCatalogazioneSemanticaComune().setPolo(
				((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue());
		currentForm.setCid(cid);
		currentForm.setTesto(testo);

		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		VariaLegameSoggettoTitoloForm currentForm = (VariaLegameSoggettoTitoloForm) form;
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		AreaDatiPassBiblioSemanticaVO datiGB =
			currentForm.getAreaDatiPassBiblioSemanticaVO();
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = datiGB.getTreeElement()
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();

		try {
			String cid = currentForm.getDettaglio().getCid();

			DatiLegameTitoloSoggettoVO legame = new DatiLegameTitoloSoggettoVO();
			legame.setBid(currentForm.getCatalogazioneSemanticaComune()
					.getBid());
			legame.setBidNatura(currentForm.getCatalogazioneSemanticaComune()
					.getNaturaBid());
			legame.setT005(detTitoloPFissaVO.getVersione() );
			legame.setBidLivelloAut(currentForm
					.getCatalogazioneSemanticaComune().getLivAutBid());
			legame.setBidTipoMateriale(currentForm
					.getCatalogazioneSemanticaComune().getTipoMaterialeBid());
			legame.setLivelloPolo(currentForm
					.getCatalogazioneSemanticaComune().isPolo());

			LegameTitoloSoggettoVO datiLegame = legame.new LegameTitoloSoggettoVO(cid, currentForm.getNotaAlLegame());
			datiLegame.setRank(datiGB.getRankLegame());
			legame.getLegami().add(datiLegame);
			DatiLegameTitoloSoggettoVO risposta = factory.getGestioneSemantica()
					.modificaLegameTitoloSoggetto(legame, utenteCollegato.getTicket());

			if (!risposta.isEsitoOk() ) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", risposta
								.getTestoEsito()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			//aggiorno timestamp del titolo soggettato
			datiGB.getTreeElement().setT005(risposta.getT005());
			detTitoloPFissaVO.setVersione(risposta.getT005());
			//aggiorno nota
			TreeElementViewTitoli titolo = (TreeElementViewTitoli) datiGB.getTreeElement().findElement(cid);
			if (titolo != null)
				titolo.getAreaDatiDettaglioOggettiVO().getDettaglioSoggettoGeneraleVO().setNotaAlLegame(currentForm.getNotaAlLegame());

		} catch (ValidationException e) {
			// errori indice

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();

		} catch (DataException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();// gestione errori java
		}

		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.operOk"));
		this.saveErrors(request, errors);

		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB);
		return mapping.findForward("catalogazioneSemantica");
	}

	public ActionForward invioInIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		VariaLegameSoggettoTitoloForm currentForm = (VariaLegameSoggettoTitoloForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		try {
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			RicercaComuneVO ricercaComune = new RicercaComuneVO();
			ricercaComune.setCodSoggettario(currentForm.getRicercaComune()
					.getCodSoggettario());
			ricercaComune.setDescSoggettario(currentForm.getRicercaComune()
					.getDescSoggettario());
			ricercaComune.setPolo(false);

			ricercaComune.getRicercaSoggetto().setTestoSogg(
					currentForm.getTesto().trim());
			ricercaComune.setRicercaDescrittore(null);
			ricercaComune.setOrdinamentoSoggetto(TipoOrdinamento.PER_TESTO);
			ricercaComune.setRicercaTipo(TipoRicerca.STRINGA_ESATTA);
			ricercaComune.setElemBlocco("10");
			delegate.eseguiRicerca(ricercaComune, mapping);
			ActionMessages errors = new ActionMessages();
			RicercaSoggettoResult op = delegate.getOperazione();
			switch (op) {

			case analitica_1:// SoggettiDelegate.analitica:
				RicercaSoggettoListaVO lista = (RicercaSoggettoListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);

				ElementoSinteticaSoggettoVO cidTrovato = (ElementoSinteticaSoggettoVO) lista.getRisultati().get(0);
				currentForm.setCid(cidTrovato.getCid());
				break;
			case crea_4:// SoggettiDelegate.crea:
				CreaVariaSoggettoVO soggetto1 = null;
				CreaVariaSoggettoVO richiesta1 = new CreaVariaSoggettoVO();
				try {
					richiesta1.setCodiceSoggettario(currentForm.getCatalogazioneSemanticaComune()
							.getCatalogazioneSoggetto().getCodSoggettario());
					richiesta1.setTesto(currentForm.getTesto().trim());
					richiesta1.setLivello(currentForm.getStatoControllo());
					richiesta1.setLivelloPolo(currentForm.getRicercaComune().isPolo());
					soggetto1 = factory.getGestioneSemantica().creaSoggetto(richiesta1,
							utenteCollegato.getTicket());
					if (soggetto1.isEsitoOk() ){
						currentForm.setCid(soggetto1.getCid());
						currentForm.setCreatoInIndice(true);
					}
				} catch (ValidationException e) {
					// errori indice
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.erroreValidazione", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();

				} catch (DataException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", e.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				} catch (InfrastructureException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", e.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				} catch (Exception e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.erroreSistema", e.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				}
				break;
			case sintetica_3:// SoggettiDelegate.sintetica:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruenzaSoggInd"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			case lista_2:// SoggettiDelegate.lista:

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruenzaSoggInd"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

			case diagnostico_0:// SoggettiDelegate.diagnostico:
				CreaVariaSoggettoVO soggetto = null;
				CreaVariaSoggettoVO richiesta = new CreaVariaSoggettoVO();
				try {
					richiesta.setCodiceSoggettario(currentForm.getCatalogazioneSemanticaComune()
							.getCatalogazioneSoggetto().getCodSoggettario());
					richiesta.setTesto(currentForm.getTesto().trim());
					richiesta.setLivello(currentForm.getStatoControllo());
					richiesta.setLivelloPolo(currentForm.getRicercaComune().isPolo());
					soggetto = factory.getGestioneSemantica().creaSoggetto(richiesta,
							utenteCollegato.getTicket());
					if (soggetto.isEsitoOk() ){
						currentForm.setCid(soggetto.getCid());
						currentForm.setCreatoInIndice(true);
					}
				} catch (ValidationException e) {
					// errori indice
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.erroreValidazione", e
									.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();

				} catch (DataException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", e.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				} catch (InfrastructureException e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", e.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				} catch (Exception e) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.erroreSistema", e.getMessage()));
					this.saveErrors(request, errors);
					log.error("", e);
					return mapping.getInputForward();
				}
				break;
			default:

			}

			CatSemSoggettoVO listaSoggetti = factory.getGestioneSemantica()
					.ricercaSoggettiPerBidCollegato(
							false,
							currentForm.getCatalogazioneSemanticaComune()
									.getBid(),
							currentForm.getRicercaComune()
									.getCodSoggettario(),
							currentForm.getMaxRighe(),
							utenteCollegato.getTicket());

//			this.variaCatalSem.getCatalogazioneSemanticaComune()
//					.setCatalogazioneSoggetto(listaSoggetti);
			if (listaSoggetti.getTotRighe()== 1) {
				if (!listaSoggetti.getListaSoggetti().get(0).getCid().equals(
						currentForm.getCid())) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.noInvioIndice"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}

			// RECUPERO LE INFORMAZIONI RELATIVE AL BID PRESENTI IN INDICE
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO analitica = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO();

			// Inizio Modifica almaviva2 16.07.2010 - Gestione delle localizzazioni del reticolo per la biblioteca richiedente e non per quella
			// operante che nel caso di centro Sistema non coincidono
			String codBiblioSbn = Navigation.getInstance(request).getUtente().getCodPolo() + Navigation.getInstance(request).getUtente().getCodBib();
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO(codBiblioSbn);
//			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO();
			// Fine Modifica almaviva2 16.07.2010


			areaDatiPass.setBidRicerca(currentForm
					.getCatalogazioneSemanticaComune().getBid());
			areaDatiPass.setRicercaIndice(true);
			areaDatiPass.setRicercaPolo(false);
			analitica = factory.getGestioneBibliografica()
					.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass,
							utenteCollegato.getTicket());
			TreeElementViewTitoli reticolo = analitica
					.getTreeElementViewTitoli();
			// SE RETICOLO E' UGUALE A NULL VUOL DIRE CHE IL BID NON E' PRESENTE
			// IN INDICE
			// SQUADRATURA
			if (reticolo == null)
				// houston abbiamo un problema
				throw new Exception(
						"Documento non presente sulla base dati di Indice");
			// CREO IL LEGAME TRA TITOLO E SOGGETTO SULLA BASE DATI DI INDICE
			String T005 = reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
					.getVersione();
			DatiLegameTitoloSoggettoVO legame = new DatiLegameTitoloSoggettoVO();
			legame.setBid(currentForm.getCatalogazioneSemanticaComune()
					.getBid());
			legame.setBidNatura(reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
					.getNatura());
			legame.setT005(T005);
			legame.setBidLivelloAut(reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
					.getLivAut());
			legame.setBidTipoMateriale(reticolo.getAreaDatiDettaglioOggettiVO()
					.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO()
					.getTipoMat());
			legame.setLivelloPolo(false);
			legame.getLegami().add(legame.new LegameTitoloSoggettoVO(currentForm.getCid(), currentForm.getNotaAlLegame()) );
			if (currentForm.isCreatoInIndice()){
				DatiLegameTitoloSoggettoVO risposta = factory.getGestioneSemantica()
				.invioInIndiceLegameTitoloSoggetto(legame,
						utenteCollegato.getTicket());
				if (!risposta.isEsitoOk() ) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", risposta
									.getTestoEsito()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}else {
				DatiLegameTitoloSoggettoVO risposta = factory.getGestioneSemantica()
				.modificaInvioInIndiceLegameTitoloSoggetto(legame,
						utenteCollegato.getTicket());
				if (!risposta.isEsitoOk() ) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.gestioneSemantica.incongruo", risposta
									.getTestoEsito()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
			}

		} catch (ValidationException e) {
			// errori indice
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreValidazione", e
							.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		} catch (InfrastructureException e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			return mapping.getInputForward();
		}
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.operOk"));
		this.saveErrors(request, errors);
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm
				.getAreaDatiPassBiblioSemanticaVO());
		return mapping.findForward("catalogazioneSemantica");
	}

	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
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
}
