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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoDescrittoriVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.RicercaSoggettoForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.SinteticaDescrittoriForm;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.SinteticaDescrittoriParoleForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate.RicercaSoggettoResult;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SinteticaDescrittoriAction extends SinteticaLookupDispatchAction {

	private static Log log = LogFactory
			.getLog(SinteticaDescrittoriAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.selTutti", "tutti");
		map.put("button.deselTutti", "deseleziona");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.soggetti", "soggetti");
		// map.put("button.soggetti.link", "utilizzati");
		map.put("button.analitica", "analitica");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private SoggettiDelegate getDelegate(HttpServletRequest request) throws Exception {
		return new SoggettiDelegate(
				FactoryEJBDelegate.getInstance(), Navigation
						.getInstance(request).getUtente(), request);
	}

	private void loadSoggettario(String ticket, ActionForm form)
			throws Exception {
		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica.loadComboSoggettario(ticket, false));
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors,
			Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private ElementoSinteticaDescrittoreVO getElementoSelezionato(
			ActionForm form) {

		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;

		for (Object o : currentForm.getOutputDescrittori().getRisultati()) {
			ElementoSinteticaDescrittoreVO elem = (ElementoSinteticaDescrittoreVO) o;
			if (elem.getDid().equals(currentForm.getCodSoggettoRadio()))
				return elem;
		}
		return null;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;
		Navigation navi = Navigation.getInstance(request);

		if (navi.isFromBar())
			return mapping.getInputForward();

		RicercaSoggettoListaVO output = null;
		RicercaComuneVO currentFormRicerca = null;
		RicercaSoggettoListaVO currentFormListaSog = null;
		RicercaSoggettoListaVO currentFormListaDescr = null;
		RicercaSoggettoListaVO currentFormListaPar = null;
		String chiamante = null;
		FolderType folder = null;
		boolean isPolo = false;

		if (!currentForm.isSessione()) {
			log.info("SinteticaDescrittoriAction::unspecified");
			// devo inizializzare tramite le request.getAttribute(......)
			output = (RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			currentFormListaSog = (RicercaSoggettoListaVO) request
					.getAttribute("outputlistadescrsog");
			currentFormListaPar = (RicercaSoggettoListaVO) request
					.getAttribute("outputlistadescrpar");
			currentFormListaDescr = (RicercaSoggettoListaVO) request
					.getAttribute("outputlistadescr");
			currentFormRicerca = (RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			folder = (FolderType) request
					.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);

			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();

			if (output == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.Faild"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
			if (chiamante == null) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			List<ElementoSinteticaDescrittoreVO> risultati = output
					.getRisultati();
			if (risultati.size() == 1)
				currentForm.setCodSoggettoRadio(risultati.get(0).getDid());

			currentForm.setSessione(true);
			currentForm.setOutputDescrittori(output);
			currentForm.setOutputDescrittoriSog(currentFormListaSog);
			currentForm.setOutputDescrittoriPar(currentFormListaPar);
			currentForm.setRicercaComune(currentFormRicerca);
			currentForm.getRicercaComune().setPolo(isPolo);

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
				currentForm.setFolder(folder);
			}
			if (currentForm.getFolder() != null
					&& currentForm.getFolder() == FolderType.FOLDER_SOGGETTI) {
				currentForm.setEnableTit(true);
				currentForm.setEnableCercaIndice(false);
				currentForm.setEnableDeselTutti(false);
				currentForm.setEnableSelTutti(false);
				currentForm.setEnableEsamina(false);
				currentForm.setEnableStampa(false);
			}
			currentForm.setAction((String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER));
			currentForm.setEnableIndice(currentForm.getRicercaComune()
					.isIndice());

			OggettoRiferimentoVO oggettoRiferimento = (OggettoRiferimentoVO) request
					.getAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO);
			if (oggettoRiferimento != null)
				currentForm.setOggettoRiferimento(oggettoRiferimento);

			boolean testForm = (navi.getCallerForm() instanceof RicercaSoggettoForm)
					|| (navi.getCallerForm() instanceof SinteticaDescrittoriParoleForm);
			currentForm.setEnableCercaIndice(testForm
					&& currentForm.getRicercaComune().isPolo()
					&& !currentForm.getOggettoRiferimento().isEnabled());

		} else {
			// Posso cercare in indice solo se è la prima volta che carico
			// il reticolo
			currentForm.setRicercaComune((RicercaComuneVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA));
			output = (RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			if (output != null)
				currentForm.setOutputDescrittori(output);

			currentForm
					.setOutputDescrittoriSog((RicercaSoggettoListaVO) request
							.getAttribute("outputlistadescrsog"));
			currentForm
					.setOutputDescrittoriPar((RicercaSoggettoListaVO) request
							.getAttribute("outputlistadescrpar"));
			currentForm.setLivContr((String) request
					.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA));
			currentForm.setDataInserimento((String) request
					.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO));
			currentForm.setDataVariazione((String) request
					.getAttribute(NavigazioneSemantica.DATA_MODIFICA));
			currentForm
					.setTreeElementViewSoggetti((TreeElementViewSoggetti) request
							.getAttribute(NavigazioneSemantica.ANALITICA));

			boolean testForm = (navi.getCallerForm() instanceof RicercaSoggettoForm)
					|| (navi.getCallerForm() instanceof SinteticaDescrittoriParoleForm);
			currentForm.setEnableCercaIndice(testForm
					&& currentForm.getRicercaComune().isPolo());

			currentForm.setEnableIndice(currentForm.getRicercaComune()
					.isIndice());

		}

		ActionMessages errors = currentForm.validate(mapping, request);
		try {
			this.loadSoggettario(navi.getUserTicket(), currentForm);
		} catch (Exception ex) {
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
		}

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		currentForm.setBlocchiCaricati(appoggio);
		if (output != null) {
			String idLista = output.getIdLista();
			currentForm.setIdLista(idLista);
			super.addSbnMarcIdLista(request, idLista);
			currentForm.setMaxRighe(output.getMaxRighe());
			currentForm.setNumBlocco(output.getNumBlocco());
			currentForm.setTotBlocchi(output.getTotBlocchi());
			currentForm.setTotRighe(output.getTotRighe());

		} else if (currentFormListaSog != null) {
			String idLista = currentFormListaSog.getIdLista();
			currentForm.setIdLista(idLista);
			super.addSbnMarcIdLista(request, idLista);
			currentForm.setMaxRighe(currentFormListaSog.getMaxRighe());
			currentForm.setNumBlocco(currentFormListaSog.getNumBlocco());
			currentForm.setTotBlocchi(currentFormListaSog.getTotBlocchi());
			currentForm.setTotRighe(currentFormListaSog.getTotRighe());

		} else if (currentFormListaDescr != null) {
			String idLista = currentFormListaDescr.getIdLista();
			currentForm.setIdLista(idLista);
			super.addSbnMarcIdLista(request, idLista);
			currentForm.setMaxRighe(currentFormListaDescr.getMaxRighe());
			currentForm.setNumBlocco(currentFormListaDescr.getNumBlocco());
			currentForm.setTotBlocchi(currentFormListaDescr.getTotBlocchi());
			currentForm.setTotRighe(currentFormListaDescr.getTotRighe());

		} else if (currentFormListaPar != null) {
			String idLista = currentFormListaPar.getIdLista();
			currentForm.setIdLista(idLista);
			super.addSbnMarcIdLista(request, idLista);
			currentForm.setMaxRighe(currentFormListaPar.getMaxRighe());
			currentForm.setNumBlocco(currentFormListaPar.getNumBlocco());
			currentForm.setTotBlocchi(currentFormListaPar.getTotBlocchi());
			currentForm.setTotRighe(currentFormListaPar.getTotRighe());
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setLivContr((String) request
				.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA));
		currentForm
				.setTreeElementViewSoggetti((TreeElementViewSoggetti) request
						.getAttribute(NavigazioneSemantica.ANALITICA));
		currentForm.setDataInserimento((String) request
				.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO));
		currentForm.setDataVariazione((String) request
				.getAttribute(NavigazioneSemantica.DATA_MODIFICA));
		return mapping.getInputForward();

	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;
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

		RicercaSoggettoListaVO areaDatiPass = new RicercaSoggettoListaVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());
		areaDatiPass.setLivelloPolo(currentForm.getRicercaComune().isPolo());
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		RicercaSoggettoListaVO areaDatiPassReturn = factory
				.getGestioneSemantica().getNextBloccoDescrittori(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.sogNotFound"));
			this.saveErrors(request, errors);
			resetToken(request);
			return mapping.getInputForward();
		}

		String idLista = areaDatiPassReturn.getIdLista();
		currentForm.setIdLista(idLista);
		super.addSbnMarcIdLista(request, idLista);
		currentForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = currentForm.getNumBlocco();
		appoggio = currentForm.getBlocchiCaricati();
		appoggio.add(numBlocco);
		currentForm.setBlocchiCaricati(appoggio);

		currentForm.setNumBlocco(numBlocco);

		currentForm.getOutputDescrittori().getRisultati().addAll(
				areaDatiPassReturn.getRisultati());
		Collections.sort(currentForm.getOutputDescrittori().getRisultati(),
				ElementoSinteticaDescrittoreVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();

	}

	public ActionForward soggetti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;
		ActionMessages errors = new ActionMessages();

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (currentForm.getCodSoggetto() == null
				&& currentForm.getCodSoggettoRadio() == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (currentForm.getCodSoggetto() != null
				&& currentForm.getCodSoggetto().length > 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noBox"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			this.deselezionaTutti(form);
			return mapping.getInputForward();
		}

		try {

			if (currentForm.getCodSoggettoRadio() != null) {

				request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
						mapping.getPath());
				SoggettiDelegate delegate = getDelegate(request);
				RicercaComuneVO ricercaComune = currentForm.getRicercaComune()
						.copy();

				RicercaSoggettoDescrittoriVO ricDid = new RicercaSoggettoDescrittoriVO();
				String didSel = currentForm.getCodSoggettoRadio();
				ricDid.setDid(didSel);
				ricercaComune.setRicercaSoggetto(ricDid);
				ricercaComune.setRicercaDescrittore(null);

				delegate.eseguiRicerca(ricercaComune, mapping);
				RicercaSoggettoListaVO lista = delegate.getOutput();
				if (lista.isEsitoNonTrovato()) {

					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.gestioneSemantica.nontrovato"));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}

				if (!lista.isEsitoOk()) {

					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo", lista
											.getTestoEsito()));
					this.saveErrors(request, errors);
					// nessun codice selezionato
					return mapping.getInputForward();
				}

				ElementoSinteticaDescrittoreVO elementoSelezionato = getElementoSelezionato(form);

				request.setAttribute("outputlistadescr", currentForm
						.getOutputDescrittori());
				request.setAttribute("outputlistadescrsog", currentForm
						.getOutputDescrittoriSog());
				request.setAttribute(NavigazioneSemantica.BID_RIFERIMENTO,
						currentForm.getBid());
				request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
						currentForm.getRicercaComune().isPolo());

				request.setAttribute(
						NavigazioneSemantica.TESTO_BID_RIFERIMENTO, currentForm
								.getTestoBid());
				request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE,
						currentForm.getFolder());
				//currentForm.setEnableCercaIndice(false);
				request.setAttribute(
						NavigazioneSemantica.ABILITA_RICERCA_INDICE,
						currentForm.isEnableCercaIndice());
				request.setAttribute(
						NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
						currentForm.getAreaDatiPassBiblioSemanticaVO());
				request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE,
						currentForm.getFolder());

				request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO,
						new OggettoRiferimentoVO(true, elementoSelezionato
								.getDid(), elementoSelezionato
								.getNomeSenzaRinvio()));

				return Navigation.getInstance(request).goForward(mapping.findForward("soggetti"));
			} else {
				// messaggio di errore.
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.codiceNessunaSelezione"));
				this.saveErrors(request, errors);
				// nessun codice selezionato
				this.deselezionaTutti(form);
				return mapping.getInputForward();
			}

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

	}

	public ActionForward utilizzati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		Integer progressivo = currentForm.getLinkProgressivo();
		if (progressivo != null) {
			for (int i = 0; i < currentForm.getOutputDescrittori()
					.getRisultati().size(); i++) {
				ElementoSinteticaDescrittoreVO elem = (ElementoSinteticaDescrittoreVO) currentForm
						.getOutputDescrittori().getRisultati().get(i);
				if (elem.getProgr() == progressivo) {
					currentForm.setCodSoggettoRadio(elem.getDid());
					break;
				}
			}
		}

		if (currentForm.getCodSoggetto() == null
				&& currentForm.getCodSoggettoRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (currentForm.getCodSoggetto() != null
				&& currentForm.getCodSoggetto().length > 0) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noBox"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			this.deselezionaTutti(form);
			return mapping.getInputForward();
		}

		if (currentForm.getCodSoggettoRadio() != null) {
			request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
					.getPath());
			// request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_INDICE,
			// new Boolean(currentForm
			// .getRicercaComune().isIndice()));
			SoggettiDelegate ricerca = getDelegate(request);
			RicercaComuneVO ricercaComune = new RicercaComuneVO();
			ricercaComune.setCodSoggettario(currentForm.getRicercaComune()
					.getCodSoggettario());
			ricercaComune.setDescSoggettario(currentForm.getRicercaComune()
					.getDescSoggettario());
			ricercaComune.setPolo(currentForm.getRicercaComune().isPolo());

			ricercaComune.setElemBlocco(currentForm.getRicercaComune()
					.getElemBlocco());
			// ricercaComune.setOrdinamentoSoggetto("S");
			// ricercaComune.setPolo(true);
			// ricercaComune.setIndice(false);

			RicercaSoggettoDescrittoriVO ricDid = new RicercaSoggettoDescrittoriVO();
			String didSel = currentForm.getCodSoggettoRadio();
			ricDid.setDid(didSel);
			ricercaComune.setRicercaSoggetto(ricDid);
			ricercaComune.setRicercaDescrittore(null);
			// ricercaComune.setElemBlocco("10");
			ricerca.eseguiRicerca(ricercaComune, mapping);
			RicercaSoggettoListaVO lista = (RicercaSoggettoListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			if (lista.getEsito().equals("3001")) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			request.setAttribute("outputlistadescr", currentForm
					.getOutputDescrittori());
			request.setAttribute("outputlistadescrsog", currentForm
					.getOutputDescrittoriSog());
			request.setAttribute(NavigazioneSemantica.BID_RIFERIMENTO,
					currentForm.getBid());
			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
					currentForm.getRicercaComune().isPolo());

			request.setAttribute(NavigazioneSemantica.TESTO_BID_RIFERIMENTO,
					currentForm.getTestoBid());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE,
					currentForm.getFolder());
			currentForm.setEnableCercaIndice(false);
			request.setAttribute(NavigazioneSemantica.ABILITA_RICERCA_INDICE,
					currentForm.isEnableCercaIndice());
			request.setAttribute(
					NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
					currentForm.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE,
					currentForm.getFolder());
			return mapping.findForward("soggetti");
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			this.deselezionaTutti(form);
			return mapping.getInputForward();
		}

	}

	public ActionForward analitica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaComune().clone() );
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getRicercaComune().getCodSoggettario());
		String descr = this.getDescrizione(currentForm.getRicercaComune()
				.getCodSoggettario(), currentForm);
		currentForm.getRicercaComune().setDescSoggettario(descr);
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getRicercaComune().getDescSoggettario());
		request.setAttribute("outputlistaDescrittori", currentForm
				.getOutputDescrittori());
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm
				.getOutputDescrittori());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(currentForm.getRicercaComune().isPolo()));
		request.setAttribute("outputlistadescr", currentForm
				.getOutputDescrittori());

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		if (currentForm.getCodSoggetto() == null
				&& currentForm.getCodSoggettoRadio() == null) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.selObblOggSint"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		if (currentForm.getCodSoggetto() != null
				&& currentForm.getCodSoggetto().length > 0) {
			String[] listaDidSelez = currentForm.getCodSoggetto();
			String xid = listaDidSelez[0];
			request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
					xid);
			request.setAttribute(
					NavigazioneSemantica.LISTA_OGGETTI_SELEZIONATI,
					listaDidSelez);
			String tipo = NavigazioneSemantica.TIPO_OGGETTO_DID;
			this.ricaricaReticolo(xid, tipo, currentForm, request);
			resetToken(request);
			return Navigation.getInstance(request).goForward(
					mapping.findForward("analiticaDescrittore"));
		}

		if (currentForm.getCodSoggettoRadio() != null) {
			String progr = currentForm.getCodSoggettoRadio();
			request.setAttribute(NavigazioneSemantica.PROGRESSIVO_SELEZIONATO,
					progr);
			String xid = currentForm.getCodSoggettoRadio();
			String tipo = NavigazioneSemantica.TIPO_OGGETTO_DID;
			AnaliticaSoggettoVO reticolo = this.ricaricaReticolo(xid, tipo,
					currentForm, request);
			if (reticolo == null)
				return mapping.getInputForward();

			return mapping.findForward("analiticaDescrittore");
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}
	}

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;
		RicercaComuneVO ricercaComune = currentForm.getRicercaComune().copy();

		ElementoSinteticaDescrittoreVO didCorrente = null;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				ricercaComune.getCodSoggettario());
		String descr = this.getDescrizione(ricercaComune.getCodSoggettario(),
				currentForm);
		ricercaComune.setDescSoggettario(descr);
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				ricercaComune);

		ricercaComune.setPolo(false);

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(ricercaComune.isPolo()));
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				ricercaComune);
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();
		try {
			SoggettiDelegate delegate = getDelegate(request);
			delegate.eseguiRicerca(ricercaComune, mapping);

			RicercaSoggettoResult op = delegate.getOperazione();
			switch (op) {
			case analitica_1:// SoggettiDelegate.analitica:
				didCorrente = (ElementoSinteticaDescrittoreVO) ((RicercaSoggettoListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA))
						.getRisultati().get(0);
				String did = didCorrente.getDid();
				this.ricaricaReticolo(did,
						NavigazioneSemantica.TIPO_OGGETTO_DID, currentForm,
						request);
				return mapping.findForward("analiticaSoggetto");
			case crea_4:// SoggettiDelegate.crea:
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
						mapping.getPath());
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);

				ricercaComune.setPolo(true);
				currentForm.setEnableIndice(ricercaComune.isIndice());
				return mapping.getInputForward();
			case sintetica_3:// SoggettiDelegate.sintetica:
				if (ricercaComune.getOperazione() instanceof RicercaDescrittoreVO) {
					if (ValidazioneDati.isFilled(ricercaComune
							.getRicercaDescrittore().getTestoDescr())
							|| ricercaComune.getRicercaDescrittore()
									.countParole() > 0)
						currentForm.setOutputDescrittori(delegate.getOutput());

					if (currentForm.getOutputDescrittori().getTotRighe() > 0) {
						int numBlocchi = (currentForm.getOutputDescrittori()
								.getTotRighe() / currentForm
								.getOutputDescrittori().getMaxRighe()) + 1;
						String idLista = currentForm
								.getOutputDescrittori().getIdLista();
						currentForm.setIdLista(idLista);
						super.addSbnMarcIdLista(request, idLista);
						currentForm.setMaxRighe(currentForm
								.getOutputDescrittori().getMaxRighe());
						currentForm.setTotRighe(currentForm
								.getOutputDescrittori().getTotRighe());
						currentForm.setNumBlocco(1);
						currentForm.setNumNotizie(currentForm
								.getOutputDescrittori().getTotRighe());
						currentForm.setTotBlocchi(numBlocchi);

						currentForm.setRicercaComune(ricercaComune);
						currentForm.setEnableIndice(true);
						return mapping.getInputForward();
					} else
						request.setAttribute(NavigazioneSemantica.PAROLE,
								ricercaComune.getRicercaDescrittore());
					return mapping.findForward("sinteticadescrittoriparole");
				} else
					request.setAttribute(
							NavigazioneSemantica.DESCRITTORI_DEL_SOGGETTO,
							ricercaComune.getRicercaSoggetto());
				return mapping.findForward("sinteticadescrittorisoggetto");
			case lista_2:// SoggettiDelegate.lista:
				return mapping.findForward("soggetti");
			case diagnostico_0:// SoggettiDelegate.diagnostico:
				RicercaSoggettoListaVO output = delegate.getOutput();
				if (!output.isEsitoNonTrovato()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage(
									"errors.gestioneSemantica.incongruo",
									output.getTestoEsito()));
					this.saveErrors(request, errors);
					return mapping.getInputForward();
				}
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);

				ricercaComune.setPolo(true);
				currentForm.setEnableIndice(ricercaComune.isIndice());
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

	public ActionForward deseleziona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		this.deselezionaTutti(form);

		return mapping.getInputForward();
	}

	private void deselezionaTutti(ActionForm form) {
		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;
		// int i;
		// for (i = 0; i < currentForm.getOutputDescrittori().getRisultati()
		// .size(); i++) {
		// Arrays.fill(currentForm.getCodSoggetto(), null);
		String[] tmp = null;
		currentForm.setCodSoggetto(tmp);
		// currentForm.setCodSoggetto(null);
		currentForm.setCodSoggettoRadio(null);
		// }
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;
		int i;
		String[] selezione = new String[currentForm.getOutputDescrittori()
				.getRisultati().size()];
		for (i = 0; i < currentForm.getOutputDescrittori().getRisultati()
				.size(); i++) {
			ElementoSinteticaDescrittoreVO ric = (ElementoSinteticaDescrittoreVO) currentForm
					.getOutputDescrittori().getRisultati().get(i);
			// selezione[i] = String.valueOf(ric.getProgr());
			selezione[i] = String.valueOf(ric.getDid());
		}
		currentForm.setCodSoggetto(selezione);
		currentForm.setCodSoggettoRadio(null);
		return mapping.getInputForward();
	}

	private AnaliticaSoggettoVO ricaricaReticolo(String xid, String tipo,
			ActionForm form, HttpServletRequest request) throws Exception {

		if (tipo.equals(NavigazioneSemantica.TIPO_OGGETTO_DID)) {
			String did = xid;
			// chiamata a EJB con tipo oggetto padre a CID
			UserVO utenteCollegato = Navigation.getInstance(request)
					.getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			AnaliticaSoggettoVO analitica = null;
			try {
				analitica = factory.getGestioneSemantica()
						.creaAnaliticaSoggettoPerDid(
								((SinteticaDescrittoriForm) form)
										.getRicercaComune().isPolo(), did, 0,
								utenteCollegato.getTicket());
			} catch (Exception e) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.erroreSistema", e
								.getMessage()));
				this.setErrors(request, errors, e);
				log.error("", e);
				return null;
			}

			if (!analitica.isEsitoOk()) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", analitica
								.getTestoEsito()));
				this.setErrors(request, errors, null);
				return null;
			}

			request.setAttribute(NavigazioneSemantica.DID_RADICE_LEGAMI, xid);
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA_DID_RADICE,
					analitica.getT005());
			((SinteticaDescrittoriForm) form)
					.setTreeElementViewSoggetti(analitica.getReticolo());
			request.setAttribute(NavigazioneSemantica.ANALITICA,
					((SinteticaDescrittoriForm) form)
							.getTreeElementViewSoggetti());
			String livelloAut = analitica.getReticolo().getLivelloAutorita();
			request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA,
					livelloAut);
			String dataInserimento = analitica.getDataInserimento();
			request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
					dataInserimento);
			String dataVariazione = analitica.getDataVariazione();
			request.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
					dataVariazione);

			return analitica;

		}

		return null;
	}

	private String getDescrizione(String codice, ActionForm form) {
		SinteticaDescrittoriForm currentForm = (SinteticaDescrittoriForm) form;
		for (int index = 0; index < currentForm.getListaSoggettari().size(); index++) {
			ComboCodDescVO sog = (ComboCodDescVO) currentForm
					.getListaSoggettari().get(index);
			if (sog.getCodice().equals(codice))
				return sog.getDescrizione();
		}
		return "Non trovato";
	}

}
