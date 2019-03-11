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
package it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloSoggettoVO.LegameTitoloSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AnaliticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.SoggettiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SoggettiFolder extends Action implements SemanticaFolder {

	private static Logger log = Logger.getLogger(SoggettiFolder.class);

	private static final String ELEMENTO_SELEZIONATO = "sogg.folder.elem.selez";

	private void loadSoggettario(String ticket, ActionForm form)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		currentForm.setListaSoggettari(CaricamentoComboSemantica
				.loadComboSoggettario(ticket, false));
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors,
			Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	private ElementoSinteticaSoggettoVO getElemento(String cid, ActionForm form) {
		if (!ValidazioneDati.isFilled(cid))
			return null;

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		List<ElementoSinteticaSoggettoVO> listaSoggetti = currentForm
				.getCatalogazioneSemanticaComune().getCatalogazioneSoggetto()
				.getListaSoggetti();

		for (ElementoSinteticaSoggettoVO elem : listaSoggetti)
			if (elem.getCid().equals(cid))
				return elem;

		return null;
	}


	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// setto il token per le transazioni successive
		this.saveToken(request);
		ActionMessages errors = new ActionMessages();
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (!currentForm.isSessione()) {
			log.info("SoggettiFolder::init");
			String chiamante = null;
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			chiamante = navi.getActionCaller();

			if (chiamante == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.FunzChiamNonImp"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			currentForm.setSessione(true);
			currentForm.setAction(chiamante);

			try {
				this.loadSoggettario(navi.getUserTicket(), currentForm);
				// ATTENZIONE: IMPOSTARE DEFAULT DA PROFILO UTENTE
				String codSoggettario = ((ComboCodDescVO) currentForm.getListaSoggettari().get(0)).getCodice();
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneSoggetto().setCodSoggettario(codSoggettario);
				String desSoggettario = CodiciProvider
						.getDescrizioneCodiceSBN(CodiciType.CODICE_SOGGETTARIO,
								codSoggettario);
				currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneSoggetto().setDescSoggettario(
								desSoggettario);
				currentForm.setCodice(codSoggettario);
				currentForm.setDescrizione(desSoggettario);
				currentForm.getRicercaComune().setCodSoggettario(currentForm.getCodice());
				currentForm.getRicercaComune().setDescSoggettario(currentForm.getDescrizione());

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
		String codSelezionato = currentForm.getCatalogazioneSemanticaComune().getCatalogazioneSoggetto().getCodSelezionato();

		//almaviva5_20100127 #2952
		navi.setSuffissoTesto("/" + LinkableTagUtils.findMessage(request, request.getLocale(), "button.tag.soggetti"));

		currentForm.getCatalogazioneSemanticaComune().setPolo(true);
		currentForm.setEnableIndice(!currentForm
				.getAreaDatiPassBiblioSemanticaVO().isInserimentoPolo());
		// indico che ho caricato il folder Soggetti
		currentForm.setEnable(true);

		if (currentForm.getAreaDatiPassBiblioSemanticaVO().getTreeElement() == null) {
			currentForm.setEnableSoloEsamina(true);
			currentForm.setEnableModifica(false);
			currentForm.setEnableRecupera(false);
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utenteCollegato = navi.getUtente();
		try {
			CatSemSoggettoVO listaSoggetti = factory.getGestioneSemantica()
					.ricercaSoggettiPerBidCollegato(
							currentForm.getAreaDatiPassBiblioSemanticaVO()
									.isInserimentoPolo(),
							currentForm.getCatalogazioneSemanticaComune()
									.getBid(),
							currentForm.getCatalogazioneSemanticaComune()
									.getCatalogazioneSoggetto()
									.getCodSoggettario(),
							currentForm.getMaxRighe(),
							utenteCollegato.getTicket());

			currentForm.getCatalogazioneSemanticaComune()
					.setCatalogazioneSoggetto(listaSoggetti);

			if (listaSoggetti.getTotRighe() == 0) {
				currentForm.setEnableNulla(true);
				currentForm.setEnableElimina(false);
				currentForm.setEnableModifica(false);
				currentForm.setEnableRecupera(false);

				currentForm.setComboGestioneLegame(LabelGestioneSemantica
						.getComboGestioneSematicaPerLegame(servlet
								.getServletContext(), request, form,
								new String[] { "SO" }, this));
				currentForm.setIdFunzioneLegame("");

				return mapping.getInputForward();

			} else {
				HashSet<Integer> appoggio = new HashSet<Integer>();
				appoggio.add(1);
				currentForm.setBlocchiCaricati(appoggio);
				currentForm.setIdLista(listaSoggetti.getIdLista());
				currentForm.setMaxRighe(listaSoggetti.getMaxRighe());
				currentForm.setNumBlocco(listaSoggetti.getNumBlocco());
				currentForm.setTotBlocchi(listaSoggetti.getTotBlocchi());
				currentForm.setTotRighe(listaSoggetti.getTotRighe());

				List<ElementoSinteticaSoggettoVO> lista = listaSoggetti.getListaSoggetti();
				if (lista.size() == 1)
					codSelezionato = lista.get(0).getCid();

				currentForm.getCatalogazioneSemanticaComune().getCatalogazioneSoggetto().setCodSelezionato(codSelezionato);
			}

			currentForm.setComboGestioneLegame(LabelGestioneSemantica
					.getComboGestioneSematicaPerLegame(servlet
							.getServletContext(), request, form,
							new String[] { "SO" }, this));
			currentForm.setIdFunzioneLegame("");

			this.loadSoggettario(navi.getUserTicket(), currentForm);
			// ATTENZIONE: IMPOSTARE DEFAULT DA PROFILO UTENTE
			String codSoggettario = ((ComboCodDescVO) currentForm
					.getListaSoggettari().get(0)).getCodice();
			currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneSoggetto().setCodSoggettario(
							codSoggettario);
			String desSoggettario = ((ComboCodDescVO) currentForm
					.getListaSoggettari().get(0)).getDescrizione();
			currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneSoggetto().setDescSoggettario(
							desSoggettario);
			currentForm.setCodice(codSoggettario);
			currentForm.setDescrizione(desSoggettario);
			currentForm.getRicercaComune().setCodSoggettario(
					currentForm.getCodice());
			currentForm.getRicercaComune().setDescSoggettario(
					currentForm.getDescrizione());

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

		if (currentForm.isEnableSoloEsamina()) {
			currentForm.setEnableElimina(false);
			currentForm.setEnableModifica(false);
			currentForm.setEnableRecupera(false);
		}
		return mapping.getInputForward();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder
	 * .SemanticaFolder#caricaBlocco(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getRicercaComune().getCodSoggettario());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		int numBlocco = currentForm.getCatalogazioneSemanticaComune().getCatalogazioneSoggetto().getNumBlocco();
		if (numBlocco < 1 || numBlocco > currentForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noElementi"));
			resetToken(request);
			return mapping.getInputForward();
		}

		Set<Integer> appoggio = currentForm.getBlocchiCaricati();

		if (appoggio != null)
			if (appoggio.contains(numBlocco))
				return mapping.getInputForward();


		CatSemSoggettoVO areaDatiPass = new CatSemSoggettoVO();
		areaDatiPass.setNumPrimo(numBlocco);
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());
		areaDatiPass.setLivelloPolo(true);
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		CatSemSoggettoVO areaDatiPassReturn = (CatSemSoggettoVO) factory
				.getGestioneSemantica().getNextBloccoSoggetti(areaDatiPass,
						utenteCollegato.getTicket());

		if (areaDatiPassReturn == null
				|| areaDatiPassReturn.getNumNotizie() == 0) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noElementi"));
			resetToken(request);
			return mapping.getInputForward();
		}

		currentForm.setIdLista(areaDatiPassReturn.getIdLista());
		currentForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		appoggio = currentForm.getBlocchiCaricati();
		appoggio.add(numBlocco);
		currentForm.setNumBlocco(numBlocco);
		List<ElementoSinteticaSoggettoVO> listaSoggetti = currentForm.getCatalogazioneSemanticaComune().getCatalogazioneSoggetto().getListaSoggetti();
		listaSoggetti.addAll(areaDatiPassReturn.getListaSoggetti());
		Collections.sort(listaSoggetti, ElementoSinteticaSoggettoVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder
	 * .SemanticaFolder#gestione(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		Integer progressivo = currentForm.getCatalogazioneSemanticaComune()
				.getLinkProgressivo();
		if (progressivo != null) {
			for (int i = 0; i < currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneSoggetto().getListaSoggetti().size(); i++) {
				ElementoSinteticaSoggettoVO elem = currentForm
						.getCatalogazioneSemanticaComune()
						.getCatalogazioneSoggetto().getListaSoggetti().get(i);
				if (elem.getProgr() == progressivo) {
					currentForm.getCatalogazioneSemanticaComune()
							.getCatalogazioneSoggetto().setCodSelezionato(
									elem.getCid());
					break;
				}
			}
		}

		ActionMessages errors = new ActionMessages();
		String codSelezionato = currentForm.getCatalogazioneSemanticaComune()
				.getCatalogazioneSoggetto().getCodSelezionato();

		if (!ValidazioneDati.strIsNull(codSelezionato)) {
			//request.setAttribute(NavigazioneSemantica.RICARICA_RETICOLO_XID, codSelezionato);
			request.setAttribute(NavigazioneSemantica.TIPO_OGGETTO_PADRE,
					NavigazioneSemantica.TIPO_OGGETTO_CID);
			RicercaComuneVO reqRicercaComune = (RicercaComuneVO) currentForm
					.getRicercaComune().clone();
			reqRicercaComune.setPolo(true);
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
					reqRicercaComune);
			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
					reqRicercaComune.isPolo());

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getCatalogazioneSemanticaComune().getFolder());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getDescrizione());

		AnaliticaSoggettoVO analitica = SoggettiDelegate.getInstance(request)
				.caricaReticoloSoggetto(true, codSelezionato);
		if (analitica == null)
			return mapping.getInputForward();
		request.setAttribute(NavigazioneSemantica.ANALITICA, analitica
				.getReticolo());

		return Navigation.getInstance(request).goForward(
				mapping.findForward("analiticaSoggetto"));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder
	 * .SemanticaFolder#Indice(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		ActionMessages errors = new ActionMessages();

		currentForm.getRicercaComune().setPolo(false);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		try {
			CatSemSoggettoVO listaSoggetti = factory.getGestioneSemantica()
					.ricercaSoggettiPerBidCollegato(
							false,
							currentForm.getCatalogazioneSemanticaComune()
									.getBid(),
							currentForm.getCatalogazioneSemanticaComune()
									.getCatalogazioneSoggetto()
									.getCodSoggettario(),
							currentForm.getMaxRighe(),
							utenteCollegato.getTicket());

			// this.catalSem.getCatalogazioneSemanticaComune()
			// .setCatalogazioneSoggetto(listaSoggetti);
			if (listaSoggetti.getEsitoType() != SbnMarcEsitoType.OK) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.SBNMarc", listaSoggetti.getTestoEsito()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else {
				// currentForm.setEnableIndice(true);
				request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA,
						listaSoggetti);
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

		//almaviva5_20160924
		AreaDatiPassBiblioSemanticaVO datiGB = currentForm.getAreaDatiPassBiblioSemanticaVO();
		TreeElementViewTitoli reticoloTitoloIndice = SoggettiDelegate.getInstance(request).caricaReticoloTitolo(false, datiGB.getBidPartenza());
		if (reticoloTitoloIndice != null)
			datiGB.setPoloSoggettazioneIndice(reticoloTitoloIndice.getPoloSoggettazioneIndice());

		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				datiGB);
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getCatalogazioneSemanticaComune().getFolder());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaComune().clone());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getRicercaComune().isPolo());

		return mapping.findForward("catturaSoggettoDaIndice");

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder
	 * .SemanticaFolder#crea(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		int numNotizie = currentForm.getCatalogazioneSemanticaComune().getCatalogazioneSoggetto().getNumNotizie();
		currentForm.getAreaDatiPassBiblioSemanticaVO().setCountLegamiSoggetti(numNotizie);

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		currentForm.getCatalogazioneSemanticaComune().setPolo(true);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getCatalogazioneSemanticaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getCatalogazioneSemanticaComune().getFolder());
		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getDescrizione());
		return Navigation.getInstance(request).goForward(
				mapping.findForward("creaLegameSoggetto"));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder
	 * .SemanticaFolder#modifica(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		AreaDatiPassBiblioSemanticaVO datiGB = currentForm.getAreaDatiPassBiblioSemanticaVO();
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		currentForm.getCatalogazioneSemanticaComune().setPolo(true);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getCatalogazioneSemanticaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getDescrizione());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getCatalogazioneSemanticaComune().getFolder());
		ActionMessages errors = new ActionMessages();


		String codSelezionato = currentForm.getCatalogazioneSemanticaComune()
				.getCatalogazioneSoggetto().getCodSelezionato();
		if (ValidazioneDati.isFilled(codSelezionato)) {

			ElementoSinteticaSoggettoVO elemento = getElemento(codSelezionato, form);

			datiGB.setRankLegame(elemento.getRank());

			if (!SoggettiDelegate.getInstance(request).isSoggettarioGestito(elemento.getCodiceSoggettario())) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noFunz"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			TreeElementViewSoggetti reticolo = this.ricaricaReticolo(codSelezionato, currentForm, request);
			if (reticolo == null)
				return mapping.getInputForward();

			DettaglioSoggettoVO dettaglio = (DettaglioSoggettoVO) reticolo.getDettaglio();


			request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO,
					dettaglio.copy());
			request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
					dettaglio.getCampoSoggettario());
			request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO,
					dettaglio.getCid());
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		TreeElementViewTitoli reticoloTitolo = SoggettiDelegate.getInstance(request)
				.caricaReticoloTitolo(
						currentForm.getCatalogazioneSemanticaComune().isPolo(),
						datiGB.getBidPartenza());
		if (reticoloTitolo == null)
			return mapping.getInputForward();

		datiGB.setTreeElement(reticoloTitolo);
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB);

		return Navigation.getInstance(request).goForward(mapping.findForward("variaLegameSoggetto"));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder
	 * .SemanticaFolder#recupera(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward recupera(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getCatalogazioneSemanticaComune().getFolder());
		currentForm.getCatalogazioneSemanticaComune().setPolo(true);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getCatalogazioneSemanticaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO,
				currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getDescrizione());
		ActionMessages errors = new ActionMessages();

		String codSelezionato = currentForm.getCatalogazioneSemanticaComune()
				.getCatalogazioneSoggetto().getCodSelezionato();
		if (ValidazioneDati.isFilled(codSelezionato)) {

			List<ElementoSinteticaSoggettoVO> listaSoggetti = currentForm
					.getCatalogazioneSemanticaComune()
					.getCatalogazioneSoggetto().getListaSoggetti();
			for (ElementoSinteticaSoggettoVO soggetto : listaSoggetti) {

				if (soggetto.getCid().equals(codSelezionato)) {
					if (soggetto.getNumTitoliBiblio() == 0) {

						TreeElementViewSoggetti reticolo = this
								.ricaricaReticolo(codSelezionato, currentForm,
										request);
						if (reticolo == null)
							return mapping.getInputForward();

						DettaglioSoggettoVO dettaglio = (DettaglioSoggettoVO) reticolo
								.getDettaglio();

						if (!SoggettiDelegate.getInstance(request).isSoggettarioGestito(
								dettaglio.getCampoSoggettario())) {
							errors.add(ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"errors.gestioneSemantica.noFunz"));
							this.saveErrors(request, errors);

							return mapping.getInputForward();
						}

						request.setAttribute(
								NavigazioneSemantica.DETTAGLIO_SOGGETTO,
								dettaglio.copy());
						request.setAttribute(
								NavigazioneSemantica.CID_RIFERIMENTO,
								codSelezionato);

					} else {
						errors.add(ActionMessages.GLOBAL_MESSAGE,
								new ActionMessage(
										"errors.gestioneSemantica.noFunz"));
						this.saveErrors(request, errors);

						return mapping.getInputForward();
					}
				}

			}

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		return Navigation.getInstance(request).goForward(
				mapping.findForward("recuperaLegameSoggettoTitolo"));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder
	 * .SemanticaFolder#elimina(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());

		ActionMessages errors = new ActionMessages();

		String codSelezionato = currentForm.getCatalogazioneSemanticaComune()
				.getCatalogazioneSoggetto().getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		AreaDatiPassBiblioSemanticaVO datiGB = currentForm.getAreaDatiPassBiblioSemanticaVO();
		ElementoSinteticaSoggettoVO elemento = getElemento(codSelezionato, currentForm);
		datiGB.setRankLegame(elemento.getRank());

		request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO,
				codSelezionato);

		SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
		AnaliticaSoggettoVO reticolo = delegate.caricaReticoloSoggetto(true,
				codSelezionato);
		if (reticolo == null)
			return mapping.getInputForward();
		DettaglioSoggettoVO dettaglio = reticolo.getDettaglio();

		/*	almaviva5_20091130 non più significativo
		if (dettaglio.getNumTitoliBiblio() < 1) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.cancella.legame.noloc"));
			this.saveErrors(request, errors);

			return mapping.getInputForward();
		}
		*/

		if (!delegate.isSoggettarioGestito(dettaglio.getCampoSoggettario())) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.saveErrors(request, errors);

			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.DETTAGLIO_SOGGETTO, dettaglio.copy());

		TreeElementViewTitoli reticoloTitolo = delegate.caricaReticoloTitolo(
				currentForm.getCatalogazioneSemanticaComune().isPolo(), datiGB.getBidPartenza());
		if (reticoloTitolo == null)
			return mapping.getInputForward();

		datiGB.setTreeElement(reticoloTitolo);

		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, datiGB);
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getCatalogazioneSemanticaComune().getFolder());
		currentForm.getCatalogazioneSemanticaComune().setPolo(true);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getCatalogazioneSemanticaComune().isPolo());

		request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, dettaglio
				.getCampoSoggettario());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getDescrizione());

		return Navigation.getInstance(request).goForward(
				mapping.findForward("cancellaLegameSoggetto"));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder
	 * .SemanticaFolder#chiudi(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward chiudi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA, "P");
		request.setAttribute(NavigazioneSemantica.BID_RIFERIMENTO, currentForm
				.getCatalogazioneSemanticaComune().getBid());
		return mapping.findForward("analiticaTitolo");
	}

	private TreeElementViewSoggetti ricaricaReticolo(String cid,
			ActionForm form, HttpServletRequest request) throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		AnaliticaSoggettoVO analitica = SoggettiDelegate.getInstance(request)
				.caricaReticoloSoggetto(
						currentForm.getCatalogazioneSemanticaComune().isPolo(),
						cid);
		if (analitica == null)
			return null;

		if (!analitica.isEsitoOk()) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.SBNMarc", analitica
							.getTestoEsito()));
			this.saveErrors(request, errors);
			return null;
		}

		TreeElementViewSoggetti reticolo = analitica.getReticolo();

		String livelloAut = reticolo.getLivelloAutorita();
		request.setAttribute(NavigazioneSemantica.LIVELLO_AUTORITA, livelloAut);
		currentForm.setLivAut(livelloAut);
		String dataInserimento = analitica.getDataInserimento();
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO,
				dataInserimento);
		String dataVariazione = analitica.getDataVariazione();
		request
				.setAttribute(NavigazioneSemantica.DATA_MODIFICA,
						dataVariazione);

		String tipoSoggetto = reticolo.getCategoriaSoggetto();
		request.setAttribute(NavigazioneSemantica.TIPO_SOGGETTO, tipoSoggetto);
		request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_POLO,
				reticolo.getAreaDatiDettaglioOggettiVO()
						.getDettaglioSoggettoGeneraleVO().getNumTitoliPolo());
		request.setAttribute(NavigazioneSemantica.TITOLI_COLLEGATI_BIBLIO,
				reticolo.getAreaDatiDettaglioOggettiVO()
						.getDettaglioSoggettoGeneraleVO().getNumTitoliBiblio());
		request.setAttribute(NavigazioneSemantica.HAS_LEGAMI_POLO, reticolo
				.getAreaDatiDettaglioOggettiVO()
				.getDettaglioSoggettoGeneraleVO().isHas_num_tit_coll());
		request.setAttribute(NavigazioneSemantica.HAS_LEGAMI_BIBLIO, reticolo
				.getAreaDatiDettaglioOggettiVO()
				.getDettaglioSoggettoGeneraleVO().isHas_num_tit_coll_bib());
		request.setAttribute(NavigazioneSemantica.OGGETTO_CONDIVISO_INDICE,
				reticolo.getAreaDatiDettaglioOggettiVO()
						.getDettaglioSoggettoGeneraleVO().isCondiviso());
		String testo = reticolo.getTesto();
		request
				.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
						testo);
		request.setAttribute(NavigazioneSemantica.NOTE_AL_LEGAME, reticolo
				.getNotaLegame());
		currentForm.setTestoCid(testo);

		return reticolo;

	}

	public ActionForward invioInIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		CatalogazioneSemanticaComuneVO catSem = currentForm.getCatalogazioneSemanticaComune();
		String cid = catSem.getCatalogazioneSoggetto().getCodSelezionato();

		if (ValidazioneDati.strIsNull(cid)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		currentForm.setCid(cid);
		request.setAttribute(NavigazioneSemantica.CID_RIFERIMENTO, cid);

		try {
			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			ElementoSinteticaSoggettoVO elemento = getElemento(cid, currentForm);
			boolean isDebug = Boolean.parseBoolean(CommonConfiguration.getProperty(Configuration.SBNMARC_LOCAL_DEBUG, "false"));
			if (!isDebug)
				if (!delegate.isSoggettarioGestitoIndice(elemento.getCodiceSoggettario() )) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.soggettarioNonGestitoIndice"));
					return mapping.getInputForward();
				}

			TreeElementViewTitoli reticoloIndice = delegate.caricaReticoloTitolo(false, catSem.getBid());
			//almaviva5_20160421 #6163
			if (reticoloIndice == null)
				return mapping.getInputForward();


			// Inizio intervento almaviva2/ almaviva5 01.04.2011 - Modifica relativa al nuovo
			// protocollo (nuovo campo prioritàPoli il quale conterrà delle stringhe così composte:
			//	<prioritaPoli>S - BVE</prioritaPoli>
			//	<prioritaPoli>N - CSW</prioritaPoli>
			// Dove Si e No rappresentano la possibilità di “sostituibilità del legame” mentre dopo il trattino viene indicato
			// come informazione aggiuntiva il polo “proprietario”.
			if (!reticoloIndice.isProprietarioSoggettazioneIndice()) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.InvioIndiceNegato", reticoloIndice.getPoloSoggettazioneIndice()));
				return mapping.getInputForward();
			}
			// Fine intervento almaviva2/ almaviva5 01.04.2011


			List<ElementoSinteticaSoggettoVO> soggettiDaInviare = new ArrayList<ElementoSinteticaSoggettoVO>();
			soggettiDaInviare.add(new ElementoSinteticaSoggettoVO(
					delegate.caricaReticoloSoggetto(true,	cid).getDettaglio()));

			DatiLegameTitoloSoggettoVO risposta = delegate
					.invioInIndiceLegamiTitoloSoggetto(reticoloIndice,
							soggettiDaInviare);
			if (risposta == null)
				return mapping.getInputForward();

			//almaviva5_20120718 #47 LIG
			//aggiorno il reticolo di indice
			TreeElementViewTitoli reticolo = (TreeElementViewTitoli) catSem.getReticoloIndice();
			if (reticolo != null && risposta.isEsitoOk()) {
				reticolo.setT005(risposta.getT005());
				DettaglioTitoloParteFissaVO detTitoloPFissaVO = reticolo
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO();
				detTitoloPFissaVO.setVersione(risposta.getT005());
			}

			request.setAttribute(
					NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
					currentForm.getAreaDatiPassBiblioSemanticaVO());
			return this.init(mapping, form, request, response);

		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreValidazione", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();

		} catch (DataException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();
		} catch (InfrastructureException e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();
		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreSistema", e.getMessage()));
			log.error("", e);
			return mapping.getInputForward();
		}

	}

	public ActionForward aggiornaDati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		try {
			CatSemSoggettoVO listaSoggetti = factory.getGestioneSemantica()
					.ricercaSoggettiPerBidCollegato(
							true,
							currentForm.getCatalogazioneSemanticaComune()
									.getBid(),
							currentForm.getCatalogazioneSemanticaComune()
									.getCatalogazioneSoggetto()
									.getCodSoggettario(),
							currentForm.getMaxRighe(),
							utenteCollegato.getTicket());

			currentForm.getCatalogazioneSemanticaComune()
					.setCatalogazioneSoggetto(listaSoggetti);
			if (listaSoggetti.getTotRighe() == 0) {
				// catalSem.setEnableElimina(false);
				// catalSem.setEnableModifica(false);
				// catalSem.setEnableRecupera(false);
				return mapping.getInputForward();
			} else {
				HashSet<Integer> appoggio = new HashSet<Integer>();
				appoggio.add(1);
				currentForm.setBlocchiCaricati(appoggio);
				currentForm.setIdLista(listaSoggetti.getIdLista());
				currentForm.setMaxRighe(listaSoggetti.getMaxRighe());
				currentForm.setNumBlocco(listaSoggetti.getNumBlocco());
				currentForm.setTotBlocchi(listaSoggetti.getTotBlocchi());
				currentForm.setTotRighe(listaSoggetti.getTotRighe());
			}

			if (currentForm.getAreaDatiPassBiblioSemanticaVO().getTreeElement() == null) {
				currentForm.setEnableSoloEsamina(true);
				currentForm.setEnableModifica(false);
				currentForm.setEnableRecupera(false);
			}

		} catch (ValidationException e) {
			// errori indice
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.incongruo", e.getMessage()));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
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

		boolean abilitaModElimina = true;
		boolean abilitaRecupera = false;
		for (int i = 0; i < currentForm.getCatalogazioneSemanticaComune()
				.getCatalogazioneSoggetto().getListaSoggetti().size(); i++) {
			ElementoSinteticaSoggettoVO soggetto = currentForm
					.getCatalogazioneSemanticaComune()
					.getCatalogazioneSoggetto().getListaSoggetti().get(i);
			if (soggetto.getIndicatore().equals("NO")) {
				abilitaRecupera = true;
				break;
			}

		}
		currentForm.setEnableElimina(abilitaModElimina);
		currentForm.setEnableModifica(abilitaModElimina);
		currentForm.setEnableRecupera(abilitaRecupera);

		if (currentForm.isEnableSoloEsamina()) {
			currentForm.setEnableElimina(false);
			currentForm.setEnableModifica(false);

		}
		return mapping.getInputForward();
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzioneLegame();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		CatSemSoggettoVO catSoggetto =
			currentForm.getCatalogazioneSemanticaComune().getCatalogazioneSoggetto();
		String codSelezionato = catSoggetto.getCodSelezionato();
		request.setAttribute(ELEMENTO_SELEZIONATO, getElemento(codSelezionato, currentForm) );

		if (!checkAttivita(request, form, idFunzione)) {
			//controllo se ci sono già mesaggi impostati da checkAttivita
			ActionMessages err = this.getErrors(request);
			if (err != null && !err.isEmpty())
				return mapping.getInputForward();

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noFunz"));
			this.setErrors(request, errors, null);
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	private enum TipoAttivita {
		GESTIONE,
		RECUPERA_LEGAME_TITOLO,
		CREA_LEGAME_TITOLO,
		MODIFICA_LEGAME_TITOLO,
		ELIMINA_LEGAME_TITOLO,
		INVIA_LEGAME_TITOLO,
		RANKING;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);

			SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);
			Utente utente = delegate.getEJBUtente();

			CatSemSoggettoVO catSoggetto =
				currentForm.getCatalogazioneSemanticaComune().getCatalogazioneSoggetto();

			switch (attivita) {
			case GESTIONE:
				if (catSoggetto.getTotRighe() < 1)
					return false;

			case ELIMINA_LEGAME_TITOLO:
			case INVIA_LEGAME_TITOLO:
			case MODIFICA_LEGAME_TITOLO:
			case RECUPERA_LEGAME_TITOLO:
				if (currentForm.isEnableSoloEsamina())
					return false;

				if (catSoggetto.getTotRighe() < 1)
					return false;

				utente.isAbilitatoLegameTitoloAuthority("SO");
				//almaviva5_20120801
				//utente.isAbilitatoTipoMateriale(tipoMateriale);
				//utente.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);

				//almaviva5_20091030
				ElementoSinteticaSoggettoVO elemento = (ElementoSinteticaSoggettoVO) request.getAttribute(ELEMENTO_SELEZIONATO);
				if (elemento != null)
					try {
						utente.checkLivAutAuthority("SO", Integer.valueOf(elemento.getMaxLivAutLegame()) );
					} catch (UtenteNotAuthorizedException e) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.maxLivAutLegameTitSog"));
						request.removeAttribute(ELEMENTO_SELEZIONATO);
						return false;
					}

				break;

			case CREA_LEGAME_TITOLO:
				if (currentForm.isEnableSoloEsamina())
					return false;

				//almaviva5_20120801
				//utente.isAbilitatoTipoMateriale(tipoMateriale);
				//utente.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
				utente.isAbilitatoLegameTitoloAuthority("SO");
				break;

			case RANKING:
				//almaviva5_20120507 evolutive CFI
				if (currentForm.isEnableSoloEsamina())
					return false;

				if (catSoggetto.getTotRighe() <= 1)
					return false;

				//almaviva5_20120801
				//utente.isAbilitatoTipoMateriale(tipoMateriale);
				//utente.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
				utente.isAbilitatoLegameTitoloAuthority("SO");
				break;

			}
		} catch (Exception e) {
			// log.error("", e);
			return false;
		}

		return true;
	}

	private ActionForward modificaRank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			RankDirection direction) throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		CatalogazioneSemanticaComuneVO catSemantica = currentForm.getCatalogazioneSemanticaComune();
		CatSemSoggettoVO catSoggetti = catSemantica.getCatalogazioneSoggetto();
		String selected = catSoggetti.getCodSelezionato();

		ElementoSinteticaSoggettoVO sogg = getElemento(selected, currentForm);
		if (sogg == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		short newRank = sogg.getRank();
		int progr = sogg.getProgr();
		switch (direction) {
		case UP:
			if (progr == 1 || newRank < 2) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
				return mapping.getInputForward();
			}
			newRank--;
			break;

		case DOWN:
			if (progr == catSoggetti.getNumNotizie() ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.configurazioneControllo.parametriScambioErrati"));
				return mapping.getInputForward();
			}
			newRank++;
			break;
		}

		SoggettiDelegate delegate = SoggettiDelegate.getInstance(request);

		if (!delegate.isSoggettarioGestito(sogg.getCodiceSoggettario())) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noFunz"));
			return mapping.getInputForward();
		}

		AreaDatiPassBiblioSemanticaVO datiGB = currentForm.getAreaDatiPassBiblioSemanticaVO();
		boolean isPolo = catSemantica.isPolo();
		TreeElementViewTitoli reticoloTitolo = delegate.caricaReticoloTitolo(isPolo, datiGB.getBidPartenza());
		if (reticoloTitolo == null)
			return mapping.getInputForward();

		datiGB.setTreeElement(reticoloTitolo);	//aggiorna t005
		DettaglioTitoloParteFissaVO detTitoloPFissaVO = reticoloTitolo
				.getAreaDatiDettaglioOggettiVO().getDettaglioTitoloCompletoVO()
				.getDetTitoloPFissaVO();

		DatiLegameTitoloSoggettoVO legame = new DatiLegameTitoloSoggettoVO();
		legame.setBid(catSemantica.getBid());
		legame.setBidNatura(detTitoloPFissaVO.getNatura());
		legame.setT005(detTitoloPFissaVO.getVersione() );
		legame.setBidLivelloAut(detTitoloPFissaVO.getLivAut());
		legame.setBidTipoMateriale(detTitoloPFissaVO.getTipoMat() );
		legame.setLivelloPolo(catSemantica.isPolo());

		LegameTitoloSoggettoVO lts = legame.new LegameTitoloSoggettoVO();
		lts.setCid(selected);
		lts.setNotaLegame(currentForm.getNotaAlLegame());
		lts.setRank(newRank); //cambio rank

		legame.getLegami().add(lts);

		DatiLegameTitoloSoggettoVO risposta = delegate.modificaLegameTitoloSoggetto(legame);

		if (risposta == null)
			return mapping.getInputForward();

		//aggiorno timestamp del titolo soggettato
		datiGB.getTreeElement().setT005(risposta.getT005());
		detTitoloPFissaVO.setVersione(risposta.getT005());

		return init(mapping, currentForm, request, response);

	}

	public ActionForward moveUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return modificaRank(mapping, form, request, response, RankDirection.UP);
	}

	public ActionForward moveDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return modificaRank(mapping, form, request, response, RankDirection.DOWN);
	}

}
