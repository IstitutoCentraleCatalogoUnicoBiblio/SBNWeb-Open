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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.DettaglioTitoloParteFissaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TreeElementViewTitoli;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTitoloClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



public class ClassiFolder extends Action implements SemanticaFolder {

	private static Logger log = Logger.getLogger(ClassiFolder.class);

	private boolean initCombo(HttpServletRequest request, ActionForm form, String ticket) {

		try {
			CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
			currentForm.setListaSistemiClassificazione(CaricamentoComboSemantica
							.loadComboSistemaClassificazione(ticket, false));

			currentForm.setListaEdizioni(CaricamentoComboSemantica.loadComboEdizioneDewey() );

			currentForm.setComboGestioneLegame(LabelGestioneSemantica
					.getComboGestioneSematicaPerLegame(servlet
							.getServletContext(), request, form,
							new String[] { "CL" }, this));
			currentForm.setIdFunzioneLegame("");

			return true;
		} catch (Exception e) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.Faild"));
			log.error("", e);
			// nessun codice selezionato
			return false;
		}
	}

	private void setErrors(HttpServletRequest request, ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);
		super.saveErrors(request, errors);
	}

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		ActionMessages errors = new ActionMessages();
		this.saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CatSemClassificazioneVO catClassi = currentForm.getCatalogazioneSemanticaComune().getCatalogazioneClassificazione();
		try {
			if (!this.initCombo(request, form, Navigation.getInstance(request).getUserTicket()) )
				return mapping.getInputForward();

			//almaviva5_20100127 #2952
			Navigation navi = Navigation.getInstance(request);
			navi.setSuffissoTesto("/" + LinkableTagUtils.findMessage(request, request.getLocale(), "button.tag.classificazioni"));

			// ATTENZIONE: IMPOSTARE DEFAULT DA PROFILO UTENTE
			String codSistemaClassificazione = ((ComboCodDescVO) currentForm.getListaSistemiClassificazione().get(0)).getCodice();
			catClassi.setCodClassificazione(codSistemaClassificazione);
			String descSistemaClassificazione = ((ComboCodDescVO) currentForm.getListaSistemiClassificazione().get(0)).getDescrizione();
			catClassi.setDescClassificazione(descSistemaClassificazione);
			currentForm.setCodice(codSistemaClassificazione);
			currentForm.setDescrizione(descSistemaClassificazione);
			currentForm.getRicercaClasse().setCodSistemaClassificazione(currentForm.getCodice());
			currentForm.getRicercaClasse().setDescSistemaClassificazione(currentForm.getDescrizione());

			String codEdizioneDewey = currentForm.getListaEdizioni().get(0).getCodice();

			catClassi.setCodEdizione(codEdizioneDewey);

			String descEdizioneDewey = CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_EDIZIONE_CLASSE, codEdizioneDewey);
			catClassi.setDescEdizione(descEdizioneDewey);
			currentForm.setCodice(codEdizioneDewey);
			currentForm.setDescrizione(descEdizioneDewey);
			currentForm.getRicercaClasse().setCodEdizioneDewey(currentForm.getCodice());
			currentForm.getRicercaClasse().setDescEdizioneDewey(currentForm.getDescrizione());
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return mapping.getInputForward();
		}
		currentForm.getCatalogazioneSemanticaComune().setPolo(true);
		currentForm.setEnableIndice(!currentForm.getAreaDatiPassBiblioSemanticaVO().isInserimentoPolo() );
		// indico che ho caricato il folder Classificazioni
		currentForm.getCatalogazioneSemanticaComune().setFolder(
				FolderType.FOLDER_CLASSI);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		try {
			CatSemClassificazioneVO listaClassi = factory
					.getGestioneSemantica().ricercaClassiPerBidCollegato(
							currentForm.getAreaDatiPassBiblioSemanticaVO().isInserimentoPolo(),
							currentForm.getCatalogazioneSemanticaComune().getBid(),
							null, //catClassi.getCodClassificazione(),
							null, //catClassi.getCodEdizione(),
							currentForm.getMaxRighe(),
							utenteCollegato.getTicket());

			currentForm.getCatalogazioneSemanticaComune()
					.setCatalogazioneClassificazione(listaClassi);

			catClassi = listaClassi;

			if (currentForm.getAreaDatiPassBiblioSemanticaVO().getTreeElement() == null) {
				currentForm.setEnableSoloEsamina(true);
				currentForm.setEnableModifica(false);
			}

			if (listaClassi.getTotRighe() == 0) {

				currentForm.setComboGestioneLegame(LabelGestioneSemantica
						.getComboGestioneSematicaPerLegame(servlet
								.getServletContext(), request, form,
								new String[] { "CL" }, this));
				currentForm.setIdFunzioneLegame("");

				currentForm.setEnableNulla(true);
				currentForm.setEnableElimina(false);
				currentForm.setEnableModifica(false);
				return mapping.getInputForward();
			} else {

				currentForm.setComboGestioneLegame(LabelGestioneSemantica
						.getComboGestioneSematicaPerLegame(servlet
								.getServletContext(), request, form,
								new String[] { "CL" }, this));
				currentForm.setIdFunzioneLegame("");

				HashSet<Integer> appoggio = new HashSet<Integer>();
				appoggio.add(1);
				currentForm.setBlocchiCaricati(appoggio);
				currentForm.setIdLista(listaClassi.getIdLista());
				currentForm.setMaxRighe(listaClassi.getMaxRighe());
				currentForm.setNumBlocco(listaClassi.getNumBlocco());
				currentForm.setTotBlocchi(listaClassi.getTotBlocchi());
				currentForm.setTotRighe(listaClassi.getTotRighe());

				List<SinteticaClasseVO> lista = listaClassi.getListaClassi();
				if (ValidazioneDati.size(lista) == 1) {
					SinteticaClasseVO classe = ValidazioneDati.first(lista);
					catClassi.setCodNotazioneSelezionato(classe.getIdentificativoClasse());
				}

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
		boolean abilitaModElimina = true;
		currentForm.setEnableElimina(abilitaModElimina);
		currentForm.setEnableModifica(abilitaModElimina);
		currentForm.setEnableRecupera(false);
		return mapping.getInputForward();
	}

	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		ActionMessages errors = new ActionMessages();
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		if (currentForm.getNumBlocco() > currentForm.getTotBlocchi()) {
			// Diagnostico "NON ESISTONO ELEMENTI PER IL BLOCCO SELEZIONATO"
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noElementi"));
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

		CatSemClassificazioneVO areaDatiPass = new CatSemClassificazioneVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());
		areaDatiPass.setLivelloPolo(currentForm
				.getCatalogazioneSemanticaComune().isPolo());
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CatSemClassificazioneVO areaDatiPassReturn = (CatSemClassificazioneVO) factory
				.getGestioneSemantica().getNextBloccoClassi(areaDatiPass,
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

		currentForm.setIdLista(areaDatiPassReturn.getIdLista());
		currentForm.setNumPrimo(areaDatiPassReturn.getNumPrimo());
		int numBlocco = currentForm.getNumBlocco();
		appoggio = currentForm.getBlocchiCaricati();
		appoggio.add(numBlocco);
		currentForm.setBlocchiCaricati(appoggio);

		currentForm.setNumBlocco(numBlocco);

		currentForm.getOutputClassi().getListaClassi().addAll(
				areaDatiPassReturn.getListaClassi());
		Collections.sort(currentForm.getOutputClassi().getListaClassi(),
				SinteticaClasseVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#gestione(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		CatalogazioneSemanticaComuneVO catSem = currentForm.getCatalogazioneSemanticaComune();
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, catSem.getFolder());
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		catSem.setPolo(true);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	catSem.isPolo());

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		Integer progressivo = catSem.getLinkProgressivo();
		if (progressivo != null) {
			List<SinteticaClasseVO> listaClassi = currentForm.getCatalogazioneSemanticaComune().getCatalogazioneClassificazione().getListaClassi();
			for (int i = 0; i < listaClassi.size(); i++) {
				SinteticaClasseVO sc = listaClassi.get(i);
				if (sc.getProgr() == progressivo) {
					currentForm.getCatalogazioneSemanticaComune()
							.getCatalogazioneClassificazione()
							.setCodNotazioneSelezionato(sc.getIdentificativoClasse());
					break;
				}
			}
		}

		ActionMessages errors = new ActionMessages();
		String xid = catSem.getCatalogazioneClassificazione().getCodNotazioneSelezionato();
		if (ValidazioneDati.isFilled(xid)) {
			currentForm.getRicercaClasse().setIdentificativoClasse(xid);
			String deweyEd = null;
			String idClasse = currentForm.getRicercaClasse().getIdentificativoClasse();
			if (ValidazioneDati.isFilled(idClasse) ) {
				SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
				currentForm.getRicercaClasse().setCodSistemaClassificazione(sd.getSistema());
				currentForm.getRicercaClasse().setCodEdizioneDewey(sd.getEdizione());
				currentForm.getRicercaClasse().setSimbolo(sd.getSimbolo());

				deweyEd = CodiciProvider.unimarcToSBN(
						CodiciType.CODICE_EDIZIONE_CLASSE,
						currentForm.getRicercaClasse().getCodEdizioneDewey());

				request.setAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY, deweyEd);
			}

			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
					currentForm.getRicercaClasse().clone() );

			CreaVariaClasseVO classe = null;
			try {
				classe = ClassiDelegate.getInstance(request).analiticaClasse(catSem.isPolo(), xid);
				if (classe == null)
					return mapping.getInputForward();
			} catch (EJBException e) {
				// errori indice
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.indiceTestoEsito", e
								.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				// nessun codice selezionato
				return mapping.getInputForward();

			} catch (ValidationException ve) {
				// errori indice
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.erroreValidazione", ve
								.getMessage()));
				this.saveErrors(request, errors);
				log.error("", ve);
				// nessun codice selezionato
				return mapping.getInputForward();
			}

			DettaglioClasseVO dettaglio = new DettaglioClasseVO(classe);
			currentForm.setDettClaGen(dettaglio);
			catSem.setFolder(FolderType.FOLDER_CLASSI);
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, currentForm
				.getDettClaGen());
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, catSem.getFolder());
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				catSem.isPolo());

		return mapping.findForward("esaminaClasse");
	}

	public ActionForward aggiornaDati(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		// this.eseguiFiltroSelezionati(this.catalSem
		// .getCatalogazioneSemanticaComune()
		// .getCatalogazioneClassificazione());
		ActionMessages errors = new ActionMessages();
		if (currentForm.getCatalogazioneSemanticaComune()
				.getCatalogazioneClassificazione().getCodClassificazione() != null
				&& currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneClassificazione()
						.getCodClassificazione().length() > 0) {
			if (!currentForm.getCatalogazioneSemanticaComune()
					.getCatalogazioneClassificazione().getCodClassificazione()
					.equals("D")
					&& currentForm.getCatalogazioneSemanticaComune()
							.getCatalogazioneClassificazione().getCodEdizione() != null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noIdentificativoValido"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		try {
			CatSemClassificazioneVO listaClassi = factory
					.getGestioneSemantica().ricercaClassiPerBidCollegato(
							true,
							currentForm.getCatalogazioneSemanticaComune()
									.getBid(),
							currentForm.getCatalogazioneSemanticaComune()
									.getCatalogazioneClassificazione()
									.getCodClassificazione(),
							currentForm.getCatalogazioneSemanticaComune()
									.getCatalogazioneClassificazione()
									.getCodEdizione(),
							currentForm.getMaxRighe(),
							utenteCollegato.getTicket());
			if (listaClassi.getTotRighe() == 0) {
				// catalSem.setEnableElimina(false);
				// catalSem.setEnableModifica(false);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();

			} else {
				HashSet<Integer> appoggio = new HashSet<Integer>();
				appoggio.add(1);
				// this.catalSem.getCatalogazioneSemanticaComune()
				// .setCatalogazioneClassificazione(listaClassi);
				// this.eseguiFiltroSelezionati(this.catalSem
				// .getCatalogazioneSemanticaComune()
				// .getCatalogazioneClassificazione());
				currentForm.setBlocchiCaricati(appoggio);
				currentForm.setIdLista(listaClassi.getIdLista());
				currentForm.setMaxRighe(listaClassi.getMaxRighe());
				currentForm.setNumBlocco(listaClassi.getNumBlocco());
				currentForm.setTotBlocchi(listaClassi.getTotBlocchi());
				currentForm.setTotRighe(listaClassi.getTotRighe());
			}

			if (currentForm.getAreaDatiPassBiblioSemanticaVO().getTreeElement() == null) {
				currentForm.setEnableSoloEsamina(true);
				currentForm.setEnableModifica(false);
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
		boolean abilitaModElimina = true;
		currentForm.setEnableElimina(abilitaModElimina);
		currentForm.setEnableModifica(abilitaModElimina);

		return mapping.getInputForward();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#Indice(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
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
		currentForm.getCatalogazioneSemanticaComune().setPolo(true);
		currentForm.getCatalogazioneSemanticaComune().setFolder(FolderType.FOLDER_CLASSI);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		try {
			CatSemClassificazioneVO listaClassi = factory
					.getGestioneSemantica().ricercaClassiPerBidCollegato(
							false,
							currentForm.getCatalogazioneSemanticaComune()
									.getBid(),
							currentForm.getCatalogazioneSemanticaComune()
									.getCatalogazioneClassificazione()
									.getCodClassificazione(),
							currentForm.getCatalogazioneSemanticaComune()
									.getCatalogazioneClassificazione()
									.getCodEdizione(),
							currentForm.getMaxRighe(),
							utenteCollegato.getTicket());

			if (listaClassi.getTotRighe() == 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			} else {
				// currentForm.setEnableIndice(true);
				request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA,
						listaClassi);
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

		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getCatalogazioneSemanticaComune().getFolder());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaClasse().clone() );
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				currentForm.getCatalogazioneSemanticaComune().isPolo());

		return mapping.findForward("catturaClasseDaIndice");

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#crea(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
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
		//request.setAttribute(NavigazioneSemantica.CODICE_SOGGETTARIO, currentForm.getCodice());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_OGGETTO,
				currentForm.getDescrizione());
		return Navigation.getInstance(request).goForward(
				mapping.findForward("creaLegameClasse"));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#modifica(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward modifica(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		CatalogazioneSemanticaComuneVO catSem = currentForm.getCatalogazioneSemanticaComune();
		catSem.setPolo(true);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	catSem.isPolo());

		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, catSem.getFolder());
		ActionMessages errors = new ActionMessages();

		String xid = catSem.getCatalogazioneClassificazione().getCodNotazioneSelezionato();
		if (ValidazioneDati.isFilled(xid) ) {
			currentForm.getRicercaClasse().setIdentificativoClasse(xid);
			String deweyEd = null;
			String idClasse = currentForm.getRicercaClasse().getIdentificativoClasse();
			if (ValidazioneDati.isFilled(idClasse)) {
				SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
				currentForm.getRicercaClasse().setCodSistemaClassificazione(sd.getSistema());
				currentForm.getRicercaClasse().setCodEdizioneDewey(sd.getEdizione());
				currentForm.getRicercaClasse().setSimbolo(sd.getSimbolo());

				deweyEd = CodiciProvider.unimarcToSBN(
						CodiciType.CODICE_EDIZIONE_CLASSE,
						currentForm.getRicercaClasse().getCodEdizioneDewey());

				request.setAttribute(
						NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY,
						deweyEd);
			}
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
					currentForm.getRicercaClasse().clone() );

			CreaVariaClasseVO classe = null;
			try {
				classe = ClassiDelegate.getInstance(request).analiticaClasse(catSem.isPolo(), xid);
				if (classe == null)
					return mapping.getInputForward();

			} catch (EJBException e) {
				// errori indice
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.indiceTestoEsito", e
								.getMessage()));
				this.saveErrors(request, errors);
				log.error("", e);
				// nessun codice selezionato
				return mapping.getInputForward();

			} catch (ValidationException ve) {
				// errori indice
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.erroreValidazione", ve
								.getMessage()));
				this.saveErrors(request, errors);
				log.error("", ve);
				// nessun codice selezionato
				return mapping.getInputForward();
			}

			DettaglioClasseVO dettaglio = new DettaglioClasseVO(classe);
			currentForm.setDettClaGen(dettaglio);
		/*	currentForm.getDettClaGen().setLivAut(classe.getLivello());
			currentForm.getDettClaGen().setIdentificativo(xid);
			currentForm.getDettClaGen().setDescrizione(classe.getDescrizione());
			currentForm.getDettClaGen().setCampoSistema(
					classe.getCodSistemaClassificazione());
			currentForm.getDettClaGen().setCampoEdizione(
					classe.getCodEdizioneDewey());
			currentForm.getDettClaGen().setDataAgg(classe.getDataVariazione());
			currentForm.getDettClaGen().setDataIns(classe.getDataInserimento());
			currentForm.getDettClaGen().setIndicatore("NO");
			currentForm.getDettClaGen().setT005(classe.getT005()); */
			request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE,	currentForm.getDettClaGen());

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		return mapping.findForward("variaLegameClasse");

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#elimina(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		CatalogazioneSemanticaComuneVO catSem = currentForm.getCatalogazioneSemanticaComune();
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, catSem.getFolder());
		catSem.setPolo(true);
		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	catSem.isPolo());

		String xid = catSem.getCatalogazioneClassificazione().getCodNotazioneSelezionato();
		if (ValidazioneDati.isFilled(xid) ) {
			currentForm.getRicercaClasse().setIdentificativoClasse(xid);
			String deweyEd = null;
			String idClasse = currentForm.getRicercaClasse().getIdentificativoClasse();
			if (ValidazioneDati.isFilled(idClasse) ) {
				SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
				currentForm.getRicercaClasse().setCodSistemaClassificazione(sd.getSistema());
				currentForm.getRicercaClasse().setCodEdizioneDewey(sd.getEdizione());
				currentForm.getRicercaClasse().setSimbolo(sd.getSimbolo());

				deweyEd = CodiciProvider.unimarcToSBN(
						CodiciType.CODICE_EDIZIONE_CLASSE,
						currentForm.getRicercaClasse().getCodEdizioneDewey());

				request.setAttribute(
						NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY,
						deweyEd);
			}
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaClasse().clone() );

			CreaVariaClasseVO classe = null;
			try {
				classe = ClassiDelegate.getInstance(request).analiticaClasse(catSem.isPolo(), xid);
				if (classe == null)
					return mapping.getInputForward();
			} catch (EJBException e) {
				// errori indice
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.indiceTestoEsito", e.getMessage()));
				log.error("", e);
				// nessun codice selezionato
				return mapping.getInputForward();

			} catch (ValidationException ve) {
				// errori indice
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.erroreValidazione", ve.getMessage()));
				log.error("", ve);
				// nessun codice selezionato
				return mapping.getInputForward();
			}

			DettaglioClasseVO dettaglio = new DettaglioClasseVO(classe);
			currentForm.setDettClaGen(dettaglio);
			request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE,	currentForm.getDettClaGen());
		} else {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		return mapping.findForward("cancellaLegameClasse");

	}

	public ActionForward recupera(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.gestioneSemantica.noImpl"));
		this.saveErrors(request, errors);

		return mapping.getInputForward();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.iccu.sbn.web.actions.gestionesemantica.catalogazionesemantica.folder.SemanticaFolder#chiudi(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
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

	public ActionForward invioInIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;

		CatalogazioneSemanticaComuneVO catSem = currentForm.getCatalogazioneSemanticaComune();
		String xid = catSem.getCatalogazioneClassificazione().getCodNotazioneSelezionato();

		if (ValidazioneDati.strIsNull(xid)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		try {
			log.debug("invio in indice classe: " + xid);
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);

			// RECUPERO LE INFORMAZIONI RELATIVE AL BID PRESENTI IN INDICE
			TreeElementViewTitoli reticoloIndice = delegate.caricaReticoloTitolo(false, catSem.getBid());

			// Inizio intervento almaviva2/ almaviva5 01.04.2011 - Modifica relativa al nuovo
			// protocollo (nuovo campo prioritÃ Poli il quale conterrÃ  delle stringhe cosÃ¬ composte:
			//	<prioritaPoli>S - BVE</prioritaPoli>
			//	<prioritaPoli>N - CSW</prioritaPoli>
			// Dove Si e No rappresentano la possibilitÃ  di "sostituibilitÃ  del legame" mentre dopo il trattino viene indicato
			// come informazione aggiuntiva il polo "proprietario".
			if (!reticoloIndice.isProprietarioClassificazioneIndice()) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.classi.InvioIndiceNegato",
						reticoloIndice.getPoloClassificazioneIndice()));
				return mapping.getInputForward();
			}
			// Fine intervento almaviva2/ almaviva5 01.04.2011

			List<SinteticaClasseVO> classiDaInviare = new ArrayList<SinteticaClasseVO>();
			CreaVariaClasseVO analitica = delegate.analiticaClasse(true, xid);
			if (analitica == null)
				return mapping.getInputForward();

			classiDaInviare.add(new SinteticaClasseVO(analitica));
			DatiLegameTitoloClasseVO risposta = delegate.invioInIndiceLegamiTitoloClasse(reticoloIndice, classiDaInviare );
			if (risposta == null)
				return mapping.getInputForward();

			if (!risposta.isEsitoOk() ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", risposta.getTestoEsito()));
				return mapping.getInputForward();
			}

			//almaviva5_20120718 #47 LIG
			//aggiorno il reticolo di indice
			TreeElementViewTitoli reticolo = (TreeElementViewTitoli) catSem.getReticoloIndice();
			if (reticolo != null) {
				reticolo.setT005(risposta.getT005());
				DettaglioTitoloParteFissaVO detTitoloPFissaVO = reticolo
						.getAreaDatiDettaglioOggettiVO()
						.getDettaglioTitoloCompletoVO().getDetTitoloPFissaVO();
				detTitoloPFissaVO.setVersione(risposta.getT005());
			}

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

		LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());

		return this.init(mapping, form, request, response);

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
		GESTIONE,
		RECUPERA_LEGAME_TITOLO,
		CREA_LEGAME_TITOLO,
		MODIFICA_LEGAME_TITOLO,
		ELIMINA_LEGAME_TITOLO,
		INVIA_LEGAME_TITOLO;
	}


	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {

		try {
			CatalogazioneSemanticaForm currentForm = (CatalogazioneSemanticaForm) form;
			//TreeElementViewTitoli rootTitolo = currentForm.getAreaDatiPassBiblioSemanticaVO().getTreeElement();
			//String tipoMateriale = rootTitolo.getTipoMateriale();
			TipoAttivita attivita = TipoAttivita.valueOf(idCheck);

			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			Utente utente = delegate.getEJBUtente();

			switch (attivita) {
			case RECUPERA_LEGAME_TITOLO:
				return false;

			case GESTIONE:
				if (currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneClassificazione().getTotRighe() < 1)
							return false;

			case ELIMINA_LEGAME_TITOLO:
			case INVIA_LEGAME_TITOLO:
			case MODIFICA_LEGAME_TITOLO:
				if (currentForm.isEnableSoloEsamina())
					return false;

				if (currentForm.getCatalogazioneSemanticaComune()
						.getCatalogazioneClassificazione().getTotRighe() < 1)
							return false;

				//almaviva5_20130520 #5316
				//utente.isAbilitatoTipoMateriale(tipoMateriale);
				//utente.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
				utente.isAbilitatoLegameTitoloAuthority("CL");
				break;

			case CREA_LEGAME_TITOLO:
				if (currentForm.isEnableSoloEsamina())
					return false;

				//almaviva5_20130520 #5316
				//utente.isAbilitatoTipoMateriale(tipoMateriale);
				//utente.checkAttivita(CodiciAttivita.getIstance().MODIFICA_DOCUMENTO_1023);
				utente.isAbilitatoLegameTitoloAuthority("CL");
				break;

			}
		} catch (Exception e) {
			// log.error("", e);
			return false;
		}

		return true;
	}

	public ActionForward moveUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

	public ActionForward moveDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}

}
