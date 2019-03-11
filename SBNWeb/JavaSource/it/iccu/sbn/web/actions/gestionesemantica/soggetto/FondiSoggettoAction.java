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
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaLegameSoggettoDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.FondiSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ListaTitoliTrascinaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.ModalitaConfermaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ParametriSoggetti.SoggettiParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.FondiSoggettoForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
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

public class FondiSoggettoAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(FondiSoggettoAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.blocco", "caricaBlocco");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		map.put("button.ok", "ok");
		map.put("button.si", "si");
		map.put("button.no", "no");
		return map;
	}

	private void loadSoggettario(String ticket, ActionForm form)
			throws Exception {
		FondiSoggettoForm currentForm = (FondiSoggettoForm) form;
		currentForm
				.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, true));
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		FondiSoggettoForm currentForm = (FondiSoggettoForm) form;

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		RicercaComuneVO currentFormRicerca = null;
		String chiamante = null;
		String cidTrascinaDa = null;
		String testoTrascinaDa = null;
		String cidTrascinaA = null;
		String testoTrascinaA = null;
		boolean isCancellaCondiviso = false;
		AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici = null;

		if (!currentForm.isSessione()) {
			log.info("FondiSoggettoAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			cidTrascinaDa = (String) request
					.getAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA);
			testoTrascinaDa = (String) request
					.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA);
			cidTrascinaA = (String) request
					.getAttribute(NavigazioneSemantica.TRASCINA_CID_ARRIVO);
			testoTrascinaA = (String) request
					.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO);
			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			currentFormRicerca = (RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
			List<?> sinteticaTrascina = (List<?>) request
					.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA);
			currentForm
					.getRicercaComune()
					.setPolo(
							((Boolean) request
									.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
									.booleanValue());
			datiBibliografici = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
					.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI);
			isCancellaCondiviso = ((Boolean) request
					.getAttribute("cancellaCondiviso")).booleanValue();
			List<ListaTitoliTrascinaVO> appoArray = new ArrayList<ListaTitoliTrascinaVO>();
			if (sinteticaTrascina != null && sinteticaTrascina.size() > 0) {
				for (int i = 0; i < sinteticaTrascina.size(); i++) {
					ListaTitoliTrascinaVO lisTrasVO = new ListaTitoliTrascinaVO(
							(SinteticaTitoliView) sinteticaTrascina.get(i));
					lisTrasVO.setUtilizzato("SI");
					appoArray.add(lisTrasVO);

				}

			}

			currentForm.setListaSintetica(appoArray);

			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setAction(chiamante);
			currentForm.setRicercaComune(currentFormRicerca);
			currentForm.setCidTrascinaDa(cidTrascinaDa);
			currentForm.setTestoTrascinaDa(testoTrascinaDa.trim());
			currentForm.setCidTrascinaA(cidTrascinaA);
			currentForm.setTestoTrascinaA(testoTrascinaA.trim());
			currentForm.setCondivisoIndice(isCancellaCondiviso);

			int i1;
			for (i1 = 0; i1 < currentForm.getListaSintetica().size(); i1++) {
				ListaTitoliTrascinaVO richOff = (ListaTitoliTrascinaVO) currentForm
						.getListaSintetica().get(i1);
				richOff.setSelezBox(String.valueOf(i1));
			}

			ActionMessages errors = currentForm.validate(mapping, request);
			try {
				this.loadSoggettario(Navigation.getInstance(request)
						.getUserTicket(), currentForm);
			} catch (Exception ex) {
				errors.clear();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
			}

			HashSet<Integer> appoggio = new HashSet<Integer>();
			appoggio.add(1);
			currentForm.setBlocchiCaricati(appoggio);
			if (datiBibliografici != null) {
				currentForm.setIdLista(datiBibliografici.getIdLista());
				currentForm.setMaxRighe(datiBibliografici.getMaxRighe());
				currentForm.setNumBlocco(datiBibliografici.getNumBlocco());
				currentForm.setTotBlocchi(datiBibliografici.getTotBlocchi());
				currentForm.setTotRighe(datiBibliografici.getTotRighe());
			}
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));

			//almaviva5_20120419 evolutive CFI
			ParametriSoggetti parametri = ParametriSoggetti.retrieve(request);
			if (parametri != null)
				currentForm.setParametriSogg(parametri);
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		return mapping.getInputForward();

	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		FondiSoggettoForm currentForm = (FondiSoggettoForm) form;
		ActionMessages errors = new ActionMessages();
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		if (currentForm.getNumBlocco() > currentForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.sogNotFound"));
			this.saveErrors(request, errors);
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

		AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass = new AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());
		areaDatiPass.setTipoOrdinam("1");
		areaDatiPass.setTipoOutput("001");
		areaDatiPass.setRicercaPolo(true);
		areaDatiPass.setRicercaIndice(false);
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = factory
				.getGestioneBibliografica().getNextBloccoTitoli(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		int i1;
		for (i1 = 0; i1 < areaDatiPassReturn.getListaSintetica().size(); i1++) {
			ListaTitoliTrascinaVO lisTrasVO = new ListaTitoliTrascinaVO();
			lisTrasVO.setProgr(((SinteticaTitoliView) areaDatiPassReturn
					.getListaSintetica().get(i1)).getProgressivo());
			lisTrasVO.setBid(((SinteticaTitoliView) areaDatiPassReturn
					.getListaSintetica().get(i1)).getBid());
			lisTrasVO.setIsbdELegami(((SinteticaTitoliView) areaDatiPassReturn
					.getListaSintetica().get(i1)).getDescrizioneLegami());
			lisTrasVO.setStato(((SinteticaTitoliView) areaDatiPassReturn
					.getListaSintetica().get(i1)).getLivelloAutorita());
			lisTrasVO.setUtilizzato("SI");
			currentForm.getListaSintetica().add(lisTrasVO);

		}

		currentForm.setIdLista(areaDatiPassReturn.getIdLista());
		currentForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = currentForm.getNumBlocco();
		appoggio = currentForm.getBlocchiCaricati();
		appoggio.add(numBlocco);
		currentForm.setBlocchiCaricati(appoggio);

		currentForm.setNumBlocco(numBlocco);

		Collections.sort(currentForm.getListaSintetica(),
				ListaTitoliTrascinaVO.ORDINA_PER_PROGRESSIVO);
		int i2;
		for (i2 = 0; i2 < currentForm.getListaSintetica().size(); i2++) {
			ListaTitoliTrascinaVO richOff = (ListaTitoliTrascinaVO) currentForm
					.getListaSintetica().get(i2);
			richOff.setSelezBox(String.valueOf(i2));
		}
		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		FondiSoggettoForm currentForm = (FondiSoggettoForm) form;
		FondiSoggettoVO area = new FondiSoggettoVO();
		area.setIdElementoEliminato(currentForm.getCidTrascinaDa());
		area.setIdElementoAccorpante(currentForm.getCidTrascinaA());

		RicercaComuneVO ricercaComune = currentForm.getRicercaComune();
		try {
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			AnaliticaSoggettoVO analitica =
					delegate.caricaReticoloSoggetto(currentForm.getRicercaComune().isPolo(), currentForm.getCidTrascinaDa());

			if (analitica.isEsitoOk() ) {
				TreeElementViewSoggetti root = analitica.getReticolo();
				DettaglioSoggettoVO dettaglio = (DettaglioSoggettoVO) root.getDettaglio();
				if (root.hasChildren()) {
					for (TreeElementView des : root.getChildren()) {
						RicercaSoggettoListaVO lista = factory
								.getGestioneSemantica().ricercaSoggettiPerDidCollegato(
										ricercaComune.isPolo(),
										des.getKey(), 10,
										utenteCollegato.getTicket(),
										ricercaComune.getOrdinamentoSoggetto(), null );
						if (lista.isEsitoOk() ) {
							boolean cancella = (lista.getTotRighe() == 1);
							if (cancella) {
								CreaLegameSoggettoDescrittoreVO legame = new CreaLegameSoggettoDescrittoreVO();
								legame.setCid(dettaglio.getCid() );
								legame.setCodSoggettario(dettaglio.getCampoSoggettario() );
								legame.setLivelloAutorita(dettaglio.getLivAut() );
								legame.setT005(dettaglio.getT005());
								legame.setDid(des.getKey());
								legame.setPolo(dettaglio.isLivelloPolo() );
								legame.setCategoriaSoggetto(dettaglio.getCategoriaSoggetto() );
								CreaVariaDescrittoreVO d = factory
										.getGestioneSemantica()
										.cancellaLegameSoggettoDescrittore(
												legame,
												utenteCollegato.getTicket());
								if (!d.isEsitoOk() ) {
									LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", d.getTestoEsito()));
									return mapping.getInputForward();
								}

								AnaliticaSoggettoVO analiticaDesc = factory
										.getGestioneSemantica()
										.creaAnaliticaSoggettoPerDid(
												((FondiSoggettoForm) form)
														.getRicercaComune()
														.isPolo(),
												des.getKey(), 0,
												utenteCollegato.getTicket());
								if (analiticaDesc.isEsitoOk() ) {
									if (analiticaDesc.getReticolo()
											.hasChildren())
										break;
									// cancello il descrittore inutilizzato
									factory
											.getGestioneSemantica()
											.cancellaSoggettoDescrittore(
													((FondiSoggettoForm) form)
															.getRicercaComune()
															.isPolo(),
													des.getKey(),
													utenteCollegato.getTicket(),
													des.getTipoAuthority() );
								}

							} // if cancella

						} // end if lista

					} // // end for
				} // soggetto.hasChildren()


			} // if analitica

			//almaviva5_20120419 evolutive CFI
			ParametriSoggetti parametri = currentForm.getParametriSogg();
			if (ValidazioneDati.equals(ModalitaConfermaType.FONDI_CAMBIO_EDIZIONE, parametri.get(SoggettiParamType.MODALITA_CONFERMA) ))
				area.setEdizioneSoggettoAccorpante("E"); //cambio edizione

			AreaDatiAccorpamentoReturnVO ritorno = factory
					.getGestioneSemantica().fondiSoggetti(
							ricercaComune.isPolo(), area,
							utenteCollegato.getTicket());
			if (!ritorno.getIdOk().equals("0000")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", ritorno.getIdErrore()));
				this.saveErrors(request, errors);
				// nessun codice selezionato
				currentForm.setEnableConferma(false);
				return mapping.getInputForward();
			}

		} catch (ValidationException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();

		} catch (DataException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		} catch (InfrastructureException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		} catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			currentForm.setEnableConferma(false);
			return mapping.getInputForward();// gestione errori java
		}

		ricercaComune.setPolo(true);

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(ricercaComune.isPolo()));
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				ricercaComune.getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				ricercaComune.getDescSoggettario());
		request.setAttribute(NavigazioneSemantica.TRASCINA_CID_PARTENZA, null);
		String xid = currentForm.getCidTrascinaA();
		String tipo = NavigazioneSemantica.TIPO_OGGETTO_CID;
		this.ricaricaReticolo(xid, tipo, currentForm, request);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.operOk"));
		this.saveErrors(request, errors);
		return mapping.findForward("analiticaSoggetto");
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

	private void ricaricaReticolo(String xid, String tipo, ActionForm form,
			HttpServletRequest request) throws Exception {

		if (tipo.equals(NavigazioneSemantica.TIPO_OGGETTO_CID)) {
			String cid = xid;
			// chiamata a EJB con tipo oggetto padre a CID
			// currentForm.setElementiReticolo(CaricamentoComboSemantica.emulazioneReticolo());
			UserVO utenteCollegato = Navigation.getInstance(request)
					.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			AnaliticaSoggettoVO analitica = null;
			try {
				analitica = factory.getGestioneSemantica()
						.creaAnaliticaSoggettoPerCid(
								((FondiSoggettoForm) form).getRicercaComune()
										.isPolo(), cid,
								utenteCollegato.getTicket());

			} catch (ValidationException e) {
				// errori indice
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				return;

			} catch (DataException e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				return;
			} catch (InfrastructureException e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.incongruo", e.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				return;
			} catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.erroreSistema", e
								.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				return;
			}
			if (!analitica.isEsitoOk() ) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", analitica
								.getTestoEsito()));
				this.saveErrors(request, errors);
			}

			((FondiSoggettoForm) form).setTreeElementViewSoggetti(analitica
					.getReticolo());
			request.setAttribute(NavigazioneSemantica.ANALITICA,
					((FondiSoggettoForm) form).getTreeElementViewSoggetti());
			String livelloAut = analitica.getReticolo().getLivelloAutorita();
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA,
					livelloAut);
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA,
					livelloAut);
			String dataInserimento = analitica.getDataInserimento();
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
					dataInserimento);
			String dataVariazione = analitica.getDataVariazione();
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
					dataVariazione);
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
					dataVariazione);
			String tipoSoggetto = analitica.getReticolo()
					.getCategoriaSoggetto();
			request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_POLO,
					analitica.getReticolo().getAreaDatiDettaglioOggettiVO()
							.getDettaglioSoggettoGeneraleVO()
							.getNumTitoliPolo());
			request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_BIBLIO,
					analitica.getReticolo().getAreaDatiDettaglioOggettiVO()
							.getDettaglioSoggettoGeneraleVO()
							.getNumTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO,
					tipoSoggetto);
			String testo = analitica.getReticolo().getTesto();
			request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
					testo);

		}

	}

}
