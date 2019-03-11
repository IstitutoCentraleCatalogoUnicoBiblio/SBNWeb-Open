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
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.ClassiUtil;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.TipoOperazione;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.CreaClasseForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class CreaClasseAction extends NavigationBaseAction {

	private static Logger log = Logger.getLogger(CreaClasseAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("button.salvaSog", "ok");
		map.put("button.stampa", "Stampa");
		map.put("button.annulla", "annulla");
		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			CreaClasseForm currentForm = (CreaClasseForm) form;
			currentForm.setListaSistemiClassificazione(CaricamentoComboSemantica
							.loadComboSistemaClassificazione(ticket, true));
			currentForm.setListaEdizioni(CaricamentoComboSemantica.loadComboEdizioneDeweyPerGestione(request));
			currentForm.setListaStatoControllo(CaricamentoComboSemantica
					.loadComboStato(ClassiDelegate.getInstance(request).getMaxLivelloAutorita()));

			return true;
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.clear();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.Faild"));
			this.saveErrors(request, errors);
			log.error("", e);
			// nessun codice selezionato
			return false;
		}
	}

	protected void loadDefault(HttpServletRequest request, ActionForm form) {

		CreaClasseForm currentForm = (CreaClasseForm) form;
		Utente utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

		try {
			String livAut = (String)utenteEjb.getDefault(ConstantDefault.CREA_CLA_LIVELLO_AUTORITA);
			int maxLivAut = Integer.parseInt(ClassiDelegate.getInstance(request).getMaxLivelloAutorita() );
			if (Integer.parseInt(livAut) <= maxLivAut )
				currentForm.setCodStatoControllo(livAut);

		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.default"));
			this.saveErrors(request, errors);
		}
	}



	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CreaClasseForm currentForm = (CreaClasseForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		currentForm.setNumNotiziePolo(0);
		currentForm.setNumNotizieBiblio(0);
		String sistema = null;
		String simbolo = null;
		String edizione = null;
		String descredizione = null;
		String currentFormTesto = null;
		String chiamante = null;
		boolean isPolo = false;
		RicercaClasseListaVO currentFormLista = null;
		String dataInserimento = (String) request
				.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO);
		String dataVariazione = (String) request.getAttribute(NavigazioneSemantica.DATA_MODIFICA);
		FolderType folder = null;

		if (!currentForm.isSessione()) {
			this.init(request, currentForm);

			// devo inizializzare tramite le request.getAttribute(......)
			sistema = (String) request.getAttribute(NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE);
			edizione = (String) request.getAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY);
			descredizione = (String) request.getAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY);
			simbolo = (String) request.getAttribute(NavigazioneSemantica.SIMBOLO_CLASSE);
			currentFormTesto = (String) request.getAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE);
			chiamante = (String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			currentFormLista = (RicercaClasseListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			isPolo = ((Boolean) request.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO)).booleanValue();
			folder = (FolderType) request.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);

			if (sistema == null) {
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
					&& currentForm.getFolder() == FolderType.FOLDER_CLASSI) {
				currentForm.setEnableTit(true);
			}

			currentForm.setOutputLista(currentFormLista);
			currentForm.setAction(chiamante);
			if (currentFormTesto != null) {
				currentForm.setDescrizione(currentFormTesto.trim());
			}
			currentForm.getRicercaClasse().setCodSistemaClassificazione(sistema);
			currentForm.getRicercaClasse().setCodEdizioneDewey(edizione);
			currentForm.getRicercaClasse().setDescEdizioneDewey(descredizione);
			currentForm.getRicercaClasse().setPolo(isPolo);
			currentForm.getRicercaClasse().setSimbolo(simbolo);
			currentForm.setSimbolo(simbolo);
			currentForm.setAbilita(true);
			currentForm.setEnableSimbolo(true);

			loadDefault(request, form);
		}

		currentForm.setSessione(true);
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		ActionMessages errors = currentForm.validate(mapping, request);
		if (!this.initCombo(request, form, navi.getUserTicket()) )
			return mapping.getInputForward();

		currentForm.setLivelloAutorita((String) request.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA));
		currentForm.getRicercaClasse().setDescEdizioneDewey(
				(String) request.getAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY));
		currentForm.setDataInserimento(dataInserimento);
		currentForm.setDataModifica(dataVariazione);
		this.saveErrors(request, errors);
		currentForm
				.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
						.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));
		currentForm.setTitoliBiblio((List<?>) request
				.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));
		currentForm.setNotazioneTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA));
		currentForm.setTestoTrascinaDa((String) request
				.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));
		currentForm.getRicercaClasse().setCodEdizioneDewey(
				(String) request.getAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY));
		return mapping.getInputForward();
	}

	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CreaVariaClasseVO nuovaClasse = null;
		CreaVariaClasseVO richiesta = new CreaVariaClasseVO();

		CreaClasseForm currentForm = (CreaClasseForm) form;

		RicercaClassiVO ricercaClasse = currentForm.getRicercaClasse().copy();
		if (ValidazioneDati.strIsNull(ricercaClasse.getSimbolo()) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.siSimbolo"));
			return mapping.getInputForward();
		}

		if (ricercaClasse.getCodSistemaClassificazione().equals("D")
			&& ValidazioneDati.strIsNull(ricercaClasse.getCodEdizioneDewey()) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.siEdizione"));
			return mapping.getInputForward();
		}

		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		// Verifico che il campo simbolo sia stato correttamente composto tre
		// cifre o tre cifre seguite da un '.'
		if (ricercaClasse.getCodSistemaClassificazione().equals("D")
				&& !ClassiUtil.isDewey(ricercaClasse.getSimbolo()) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noSimboloValido"));
			return mapping.getInputForward();
		}

		// Verifico che per sistemi di classificazione diversi da "D" l'edizione
		// sia uguale a space
		if (!ricercaClasse.getCodSistemaClassificazione().equals("D")) {
			if (ValidazioneDati.isFilled(ricercaClasse.getCodEdizioneDewey()) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noIdentificativoValido"));
				return mapping.getInputForward();
			}
		}

		// Verifico che per sistemi di classificazione uguali a "D" l'edizione
		// risulti impostata
		if (ricercaClasse.getCodSistemaClassificazione().equals("D")) {
			if (ValidazioneDati.strIsNull(ricercaClasse.getCodEdizioneDewey()) ) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.siEdizione"));
				return mapping.getInputForward();
			}
		}

		if (!ricercaClasse.getCodSistemaClassificazione().equals("D"))
			ricercaClasse.setCodEdizioneDewey(NavigazioneSemantica.EDIZIONE_NON_DEWEY);


		if (!ricercaClasse.getCodSistemaClassificazione().equals("D")) {
			ricercaClasse.setIdentificativoClasse(
					ricercaClasse.getCodSistemaClassificazione()
							+ NavigazioneSemantica.EDIZIONE_NON_DEWEY + ricercaClasse.getSimbolo().trim());
		} else {
			ricercaClasse.setIdentificativoClasse(
					ricercaClasse.getCodSistemaClassificazione()
							+ ricercaClasse.getCodEdizioneDewey()
							+ ricercaClasse.getSimbolo().trim());
		}

		try {
			request.setAttribute(NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE, ricercaClasse
					.getCodSistemaClassificazione());
			request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY, ricercaClasse
					.getCodEdizioneDewey());
			request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
			richiesta.setCodSistemaClassificazione(ricercaClasse
					.getCodSistemaClassificazione());
			richiesta.setCodEdizioneDewey(ricercaClasse
					.getCodEdizioneDewey());
			richiesta.setDescrizione(currentForm.getDescrizione().trim());
			richiesta.setUlterioreTermine(currentForm.getUlterioreTermine().trim());
			richiesta.setSimbolo(ricercaClasse.getSimbolo().trim());
			richiesta.setLivello(currentForm.getCodStatoControllo());
			richiesta.setLivelloPolo(ricercaClasse.isPolo());
			richiesta.setT001(ricercaClasse.getIdentificativoClasse());
			richiesta.setCostruito(currentForm.isCostruito());
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			nuovaClasse = factory.getGestioneSemantica().creaClasse(richiesta,
					utenteCollegato.getTicket());
			if (nuovaClasse.isEsitoOk()
					&& (ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune()
							.getBid()) && ValidazioneDati.strIsNull(currentForm
							.getNotazioneTrascinaDa()))) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.operOk"));
			}

			if (!nuovaClasse.isEsitoOk() ) {
				//almaviva5_20120911 #5032
				if (nuovaClasse.getEsitoType() == SbnMarcEsitoType.SISTEMA_EDIZIONE_NON_ABILITATE) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", nuovaClasse.getTestoEsito()));
					return mapping.getInputForward();
				}
				if (nuovaClasse.getEsitoType() == SbnMarcEsitoType.CARATTERE_NON_VALIDO) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", nuovaClasse.getTestoEsito()));
					return mapping.getInputForward();
				}
				if (nuovaClasse.getEsitoType() == SbnMarcEsitoType.TROVATI_SIMILI) {
					if (ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid())
							&& ValidazioneDati.strIsNull(currentForm.getNotazioneTrascinaDa())) {
						LinkableTagUtils.addError(request, new ActionMessage(
								"errors.gestioneSemantica.duplicataCla", nuovaClasse.getListaSimili().get(0).getIdentificativoClasse()));
						return mapping.getInputForward();
					}
				} else {
					if (ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid())
							&& ValidazioneDati.strIsNull(currentForm.getNotazioneTrascinaDa())) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", nuovaClasse.getTestoEsito()));
						return mapping.getInputForward();
					}
				}
			}
		} catch (ValidationException e) {
			// errori indice
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", e.getMessage()));
			log.error("", e);
			// nessun codice selezionato
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
			return mapping.getInputForward();// gestione errori java
		}

		currentForm.setNumNotiziePolo(0);
		currentForm.setNumNotizieBiblio(0);
		currentForm.setAbilita(false);
		currentForm.setEnableSalvaSog(false);

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CreaVariaClasseVO analiticaClasse = null;
		try {
			analiticaClasse = factory.getGestioneSemantica().analiticaClasse(
					ricercaClasse.isPolo(),
					nuovaClasse.getT001(),
					utenteCollegato.getTicket());
		} catch (EJBException e) {
			// errori indice
			LinkableTagUtils.addError(request, 	new ActionMessage("errors.gestioneSemantica.indiceTestoEsito", e.getMessage()));
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

		Navigation navi = Navigation.getInstance(request);

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO, new Boolean(ricercaClasse.isPolo()));
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, ricercaClasse.clone() );
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm.getOutputLista());
		currentForm.getDettClaGen().setCondiviso(analiticaClasse.isCondiviso());
		currentForm.getDettClaGen().setLivAut(analiticaClasse.getLivello());
		// inserire campo per indicatore legato a titoli
		currentForm.getDettClaGen().setDescrizione(analiticaClasse.getDescrizione());
		currentForm.getDettClaGen().setCampoSistema(analiticaClasse.getCodSistemaClassificazione());
		if (analiticaClasse.getCodEdizioneDewey() == null)
			currentForm.getDettClaGen().setCampoEdizione(NavigazioneSemantica.EDIZIONE_NON_DEWEY);
		else
			currentForm.getDettClaGen().setCampoEdizione(analiticaClasse.getCodEdizioneDewey());

		currentForm.getDettClaGen().setDataAgg(analiticaClasse.getDataVariazione());
		currentForm.getDettClaGen().setDataIns(analiticaClasse.getDataInserimento());
		currentForm.getDettClaGen().setIndicatore("NO");
		currentForm.getDettClaGen().setT005(analiticaClasse.getT005());
		currentForm.getDettClaGen().setUlterioreTermine(analiticaClasse.getUlterioreTermine());
		currentForm.getDettClaGen().setIdentificativo(analiticaClasse.getT001());
		currentForm.getDettClaGen().setSimbolo(ricercaClasse.getSimbolo().trim());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, currentForm.getDettClaGen());

		//almaviva5_20111021 evolutive CFI
		ParametriThesauro parametriThes = currentForm.getParametriThes().copy();
		TipoOperazione op = (TipoOperazione) parametriThes.get(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
		if (op != null)	{ //sono in gestione legame termine-classe?
			DatiLegameTermineClasseVO datiLegame = new DatiLegameTermineClasseVO();
			parametriThes.put(ParamType.DETTAGLIO_LEGAME_CLASSE, datiLegame.new LegameTermineClasseVO(new DettaglioClasseVO(analiticaClasse)) );
			ParametriThesauro.send(request, parametriThes);
			return mapping.findForward("gestioneLegame");
		}

		if (currentForm.getNotazioneTrascinaDa() != null) {
			request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA, currentForm.getNotazioneTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm.getTestoTrascinaDa().trim());
			request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm.getDatiBibliografici());
			request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_ARRIVO, nuovaClasse.getT001());
			if (nuovaClasse.getDescrizione() != null)
				request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, nuovaClasse.getDescrizione().trim());
			else
				request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, currentForm.getDescrizione().trim());

			navi.purgeThis();
			return navi.goForward(mapping.findForward("ok"));

		}else if (ValidazioneDati.strIsNull(currentForm.getCatalogazioneSemanticaComune().getBid())) {
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm.getAreaDatiPassBiblioSemanticaVO());
			navi.purgeThis();
			return navi.goForward(mapping.findForward("esaminaClasse"));

		} else {
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA, currentForm.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
			navi.purgeThis();
			return navi.goForward(mapping.findForward("creaLegameClasseTitolo"));
		}

	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return Navigation.getInstance(request).goBack(true);
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		log.info("CreaClasseAction::init");
		super.init(request, form);
		CreaClasseForm currentForm = (CreaClasseForm) form;
		ParametriThesauro paramThes = ParametriThesauro.retrieve(request);
		if (paramThes != null) {
			currentForm.setParametriThes(paramThes);
			OggettoRiferimentoVO rif = (OggettoRiferimentoVO) paramThes.get(ParamType.OGGETTO_RIFERIMENTO);
			if (rif == null)
				return;

			currentForm.setOggettoRiferimento(rif);
		}
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
