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
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.OggettoRiferimentoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.CreaVariaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ParamType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.TipoOperazione;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.ListaClassiForm;
import it.iccu.sbn.web.actions.gestionesemantica.utility.CaricamentoComboSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.LabelGestioneSemantica;
import it.iccu.sbn.web.actions.gestionesemantica.utility.ScorrimentoLabel;
import it.iccu.sbn.web.constant.NavigazioneSemantica;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate.RicercaClasseResult;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

public class ListaClassiAction extends SinteticaLookupDispatchAction implements	SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(ListaClassiAction.class);

	private static final String SINT_TIT_COLL_CLA = "SINT_TIT_COLL_CLA";

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.selTutti", "tutti");
		map.put("button.deselTutti", "deseleziona");
		map.put("button.blocco", "caricaBlocco");
		map.put("button.cercaIndice", "cercaIndice");
		map.put("button.crea", "crea");

		map.put("button.esamina", "esamina");
		map.put("button.esegui", "esamina");

		map.put("button.dettaglio", "dettaglio");
		map.put("button.importa", "importa");
		map.put("button.gestione", "gestione");
		map.put("button.stampa", "stampa");
		map.put("button.annulla", "annulla");
		map.put("button.scegli", "scegli");
		map.put("button.alto", "alto");
		map.put("button.basso", "basso");
		map.put("button.sinistra", "sinistra");
		map.put("button.destra", "destra");
		map.put("button.radice", "radice");
		map.put("button.polo", "polo");
		map.put("button.biblio", "biblio");

		map.put("button.titoli.indice", "titoliIndice");

		return map;
	}

	private boolean initCombo(HttpServletRequest request, ActionForm form,
			String ticket) {

		try {
			ListaClassiForm currentForm = (ListaClassiForm) form;
			currentForm.setListaSistemiClassificazione(CaricamentoComboSemantica
				.loadComboSistemaClassificazione(ticket, false));
			currentForm.setListaEdizioni(CaricamentoComboSemantica.loadComboEdizioneDewey());

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

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		if (navi.bookmarkExists(SINT_TIT_COLL_CLA)) {
			navi.removeBookmark(SINT_TIT_COLL_CLA);
			return mapping.getInputForward();
		}

		RicercaClasseListaVO classiLista = null;
		RicercaClassiVO parametriRicerca = null;
		String chiamante = null;
		FolderType folder = null;
		String sistema = null;
		String edizione = null;
		String idClasse = null;
		String parole = null;
		boolean isPolo = false;

		if (!currentForm.isSessione()) {
			this.init(request, currentForm);
			// devo inizializzare tramite le request.getAttribute(......)
			classiLista = (RicercaClasseListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
			parametriRicerca = (RicercaClassiVO) request
					.getAttribute(NavigazioneSemantica.PARAMETRI_RICERCA);
			currentForm
					.setNotazioneTrascinaDa((String) request
							.getAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA));
			currentForm
					.setTestoTrascinaDa((String) request
							.getAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA));

			chiamante = (String) request
					.getAttribute(NavigazioneSemantica.ACTION_CALLER);
			sistema = (String) request
					.getAttribute(NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE);
			edizione = (String) request
					.getAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY);
			idClasse = (String) request
					.getAttribute(NavigazioneSemantica.SIMBOLO_CLASSE);
			parole = (String) request.getAttribute(NavigazioneSemantica.PAROLE);
			folder = (FolderType) request
					.getAttribute(NavigazioneSemantica.FOLDER_CORRENTE);
			currentForm
					.setDatiBibliografici((AreaDatiPassaggioInterrogazioneTitoloReturnVO) request
							.getAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI));
			isPolo = ((Boolean) request
					.getAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO))
					.booleanValue();

			OggettoRiferimentoVO oggRif = (OggettoRiferimentoVO) request.getAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO);
			if (oggRif != null)
				currentForm.setOggettoRiferimento(oggRif);

			if (classiLista == null || classiLista == null) {
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

			List<SinteticaClasseVO> risultati = classiLista.getRisultati();
			if (ValidazioneDati.size(risultati) == 1)
				currentForm.setCodSelezionato(risultati.get(0).getIdentificativoClasse());

			currentForm.setSessione(true);

			currentForm.setOutput(classiLista);
			currentForm.setRicercaClasse(parametriRicerca);
			currentForm.getRicercaClasse().setPolo(isPolo);
			currentForm.getRicercaClasse().setCodSistemaClassificazione(sistema);
			currentForm.getRicercaClasse().setCodEdizioneDewey(edizione);
			currentForm.getRicercaClasse().setIdentificativoClasse(idClasse);
			if (ValidazioneDati.isFilled(idClasse)) {
				SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
				currentForm.getRicercaClasse().setSimbolo(sd.getSimbolo());
			}
			currentForm.getRicercaClasse().setParole(parole);
			currentForm.setEnableIndice(currentForm.getRicercaClasse().isIndice());

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
			if (currentForm.getFolder() != null
					&& currentForm.getFolder() == FolderType.FOLDER_CLASSI) {
				currentForm.setEnableTit(true);
				currentForm.setEnableOkTit(true);
			}
			if (currentForm.getNotazioneTrascinaDa() != null) {
				currentForm.setEnableCercaIndice(false);
				currentForm.setEnableOk(true);
			}
			//almaviva5_20111021 evolutive CFI
			ParametriThesauro parametriThes = currentForm.getParametriThes();
			TipoOperazione op = (TipoOperazione) parametriThes.get(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
			if (op != null)	{
				//sono in gestione legame termine-classe?
				currentForm.setEnableCercaIndice(false);
				currentForm.setEnableOk(true);
			}
			currentForm.setAction((String) request.getAttribute(NavigazioneSemantica.ACTION_CALLER));

			// combo esamina
			currentForm.setComboGestioneEsamina(LabelGestioneSemantica
					.getComboGestioneSematicaPerEsamina(servlet
							.getServletContext(), request, form, new String[]{"CL"}, this));
			currentForm.setIdFunzioneEsamina("");

		} else {

			currentForm.setOutput((RicercaClasseListaVO) request
					.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
			HashSet<Integer> appoggio = new HashSet<Integer>();
			appoggio.add(1);
			currentForm.setAppoggio(appoggio);
			if (currentForm.getOutput() != null) {
				String idLista = currentForm.getOutput().getIdLista();
				currentForm.setIdLista(idLista);
				super.addSbnMarcIdLista(request, idLista);
				currentForm.setMaxRighe(currentForm.getOutput().getMaxRighe());
				currentForm
						.setNumBlocco(currentForm.getOutput().getNumBlocco());
				currentForm.setTotBlocchi(currentForm.getOutput()
						.getTotBlocchi());
				currentForm.setTotRighe(currentForm.getOutput().getTotRighe());
			}
			currentForm.setEnableOk(false);
			// ??????QUANDO RITORNO DA TRASCINATITOLIACTION DOVREI REIMPOSTARE
			// LA LISTA E IL SISTEMA DI CLASSIFICAZIONE EDIZIONE
			// CON I VALORI CHE MI SONO STATI PASSATI DA TRASCINATITOLI
			currentForm.setEnableCercaIndice(false);

		}

		if (!this.initCombo(request, form, navi.getUserTicket()))
			return mapping.getInputForward();

		HashSet<Integer> appoggio = new HashSet<Integer>();
		appoggio.add(1);
		currentForm.setAppoggio(appoggio);
		if (classiLista != null) {
			String idLista = classiLista.getIdLista();
			currentForm.setIdLista(idLista);
			super.addSbnMarcIdLista(request, idLista);
			currentForm.setMaxRighe(classiLista.getMaxRighe());
			currentForm.setNumBlocco(classiLista.getNumBlocco());
			currentForm.setTotBlocchi(classiLista.getTotBlocchi());
			currentForm.setTotRighe(classiLista.getTotRighe());
		}
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		// ricSogg.setOffset(0);

		currentForm.setLivContr((String) request
				.getAttribute(NavigazioneSemantica.LIVELLO_AUTORITA));
		currentForm.setDataInserimento((String) request
				.getAttribute(NavigazioneSemantica.DATA_INSERIMENTO));
		currentForm.setDataVariazione((String) request
				.getAttribute(NavigazioneSemantica.DATA_MODIFICA));
		currentForm.setTitoliBiblio((List<?>) request
				.getAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA));

		if (currentForm.getRicercaClasse().getCodSistemaClassificazione()
				.equals("D")) {
			currentForm.setEnableScorrimento(true);
		}

		return mapping.getInputForward();

	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		log.info("ListaClassiAction::init");
		super.init(request, form);
		ListaClassiForm currentForm = (ListaClassiForm) form;
		ParametriThesauro paramThes = ParametriThesauro.retrieve(request);
		if (paramThes != null) {
			currentForm.setParametriThes(paramThes);
			OggettoRiferimentoVO rif = (OggettoRiferimentoVO) paramThes.get(ParamType.OGGETTO_RIFERIMENTO);
			if (rif == null)
				return;

			currentForm.setOggettoRiferimento(rif);
		}
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		List<String> items = getMultiBoxSelectedItems(currentForm.getCodClasse());

		if (ValidazioneDati.isFilled(items)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noBox"));
			return mapping.getInputForward();
		}

		Navigation navi = Navigation.getInstance(request);
		String codSelezionato = currentForm.getCodSelezionato();
		if (!ValidazioneDati.isFilled(codSelezionato) ) {
			// messaggio di errore.
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			// nessun codice selezionato
			return mapping.getInputForward();
		}


		if	(ValidazioneDati.isFilled(currentForm.getCatalogazioneSemanticaComune().getBid())) {

			SimboloDeweyVO sd = SimboloDeweyVO.parse(codSelezionato);

			currentForm.getRicercaClasse().setCodSistemaClassificazione(sd.getSistema());
			currentForm.getRicercaClasse().setCodEdizioneDewey(sd.getEdizione());

			String codSistema = currentForm.getRicercaClasse().getCodSistemaClassificazione();
			String codEdizione = currentForm.getRicercaClasse().getCodEdizioneDewey();
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);

			if (!delegate.isSistemaGestito(codSistema, codEdizione)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.sistemaClasseNonGestito"));
				return mapping.getInputForward();// gestione errori java
			}

			String idClasse = codSelezionato;
			CreaVariaClasseVO classe = ClassiDelegate.getInstance(request).analiticaClasse(currentForm.getRicercaClasse().isPolo(), idClasse);
			if (classe == null)
				return mapping.getInputForward();// gestione errori java

			DettaglioClasseVO dettClaGen = currentForm.getDettClaGen();
			dettClaGen.setLivAut(classe.getLivello());
			// inserire campo per indicatore legato a titoli
			dettClaGen.setDescrizione(classe.getDescrizione());
			dettClaGen.setCampoSistema(classe.getCodSistemaClassificazione());
			dettClaGen.setIdentificativo(idClasse.trim());
			dettClaGen.setCampoEdizione(classe.getCodEdizioneDewey());
			dettClaGen.setDataAgg(classe.getDataVariazione());
			dettClaGen.setDataIns(classe.getDataInserimento());
			dettClaGen.setIndicatore("NO");
			dettClaGen.setT005(classe.getT005());
			dettClaGen.setSimbolo(classe.getSimbolo().trim());

			dettClaGen.setUlterioreTermine(classe.getUlterioreTermine());
			request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE,	dettClaGen);

			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,	new Boolean(currentForm.getRicercaClasse().isPolo()));
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,	currentForm.getAreaDatiPassBiblioSemanticaVO());
			request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm.getFolder());
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaClasse().clone());

			//almaviva5_20111021 evolutive CFI
			ParametriThesauro parametriThes = currentForm.getParametriThes().copy();
			TipoOperazione op = (TipoOperazione) parametriThes.get(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
			if (op != null)	{ //sono in gestione legame termine-classe?
				DatiLegameTermineClasseVO datiLegame = new DatiLegameTermineClasseVO();
				parametriThes.put(ParamType.DETTAGLIO_LEGAME_CLASSE, datiLegame.new LegameTermineClasseVO(dettClaGen) );
				ParametriThesauro.send(request, parametriThes);
				return mapping.findForward("gestioneLegame");
			} else // legame titolo-classe
				return mapping.findForward("creaLegameClassificazioneTitolo");
		}

		if (currentForm.getTitoliBiblio() != null) {

			String xid = codSelezionato;
			if (xid.equals(currentForm.getNotazioneTrascinaDa())) {
				// sto tentando di trascinare su me stesso
				LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.incongruo", "Trascinamento non consentito"));
				return mapping.getInputForward();
			}

			request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA, currentForm.getTitoliBiblio());

			for (Object elemento : currentForm.getOutput().getRisultati()) {
				if (((SinteticaClasseVO) elemento).getIdentificativoClasse().equals(xid)) {
					request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_ARRIVO, ((SinteticaClasseVO) elemento).getParole());
					break;
				}
			}
			request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_ARRIVO, xid);
			request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA, currentForm.getTestoTrascinaDa());
			request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA, currentForm.getNotazioneTrascinaDa());
			request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping.getPath());
			request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA,	currentForm.getOutput());
			request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI, currentForm.getDatiBibliografici());
			request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA, currentForm.getRicercaClasse().clone());

			return navi.goForward(mapping.findForward("ok"));
		}

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		CreaVariaClasseVO classe = ClassiDelegate.getInstance(request).analiticaClasse(currentForm.getRicercaClasse().isPolo(), codSelezionato);
		if (classe == null)
			return mapping.getInputForward();// gestione errori java

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		currentForm.setBiblioteca(utenteCollegato.getCodBib());
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, currentForm.getBiblioteca());
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca, TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm.getNotazioneTrascinaDa());
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc, currentForm.getTestoTrascinaDa());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "NO");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));

		//almaviva5_20111021 evolutive CFI
		ParametriThesauro parametriThes = currentForm.getParametriThes().copy();
		TipoOperazione op = (TipoOperazione) parametriThes.get(ParamType.TIPO_OPERAZIONE_LEGAME_CLASSE);
		if (op != null)	{ //sono in gestione legame termine-classe?
			DatiLegameTermineClasseVO datiLegame = new DatiLegameTermineClasseVO();
			parametriThes.put(ParamType.DETTAGLIO_LEGAME_CLASSE, datiLegame.new LegameTermineClasseVO(new DettaglioClasseVO(classe)) );
			ParametriThesauro.send(request, parametriThes);
			return mapping.findForward("gestioneLegame");
		} else // legame titolo-classe
			//return mapping.findForward("creaLegameClassificazioneTitolo");
			return mapping.findForward("titoliCollegatiBiblio");
	}


	public ActionForward caricaBlocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
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

		HashSet<Integer> appoggio = currentForm.getAppoggio();
		int i = currentForm.getNumBlocco();

		if (appoggio != null) {
			if (appoggio.contains(i)) {
				return mapping.getInputForward();
			}
		}

		RicercaClasseListaVO areaDatiPass = new RicercaClasseListaVO();
		areaDatiPass.setNumPrimo(currentForm.getNumBlocco());
		areaDatiPass.setMaxRighe(currentForm.getMaxRighe());
		areaDatiPass.setIdLista(currentForm.getIdLista());
		areaDatiPass.setLivelloPolo(currentForm.getRicercaClasse().isPolo());
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		RicercaClasseListaVO areaDatiPassReturn = (RicercaClasseListaVO) factory
				.getGestioneSemantica().getNextBloccoClassi(areaDatiPass,
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
		appoggio = currentForm.getAppoggio();
		appoggio.add(numBlocco);
		currentForm.setAppoggio(appoggio);

		currentForm.setNumBlocco(numBlocco);

		currentForm.getOutput().getRisultati().addAll(
				areaDatiPassReturn.getRisultati());
		Collections.sort(currentForm.getOutput().getRisultati(),
				SinteticaClasseVO.ORDINA_PER_PROGRESSIVO);
		return mapping.getInputForward();
	}

	public ActionForward cercaIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		RicercaClassiVO ricercaClasse = currentForm.getRicercaClasse().copy();
		ricercaClasse.setPolo(false);
		ActionMessages errors = new ActionMessages();

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(ricercaClasse.isPolo()));

		try {
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			delegate.eseguiRicerca(ricercaClasse, mapping);
			currentForm.setEnableIndice(true);
			RicercaClasseResult op = delegate.getOperazione();
			HashSet<Integer> appoggio = new HashSet<Integer>();
			appoggio.add(1);
			currentForm.setAppoggio(appoggio);
			switch (op) {
			case analitica_1:// ClassiDelegate.analitica:
				currentForm.setOutput((RicercaClasseListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
				if (currentForm.getOutput().getTotRighe() > 0) {
					int numBlocchi = (currentForm.getOutput().getTotRighe() / currentForm
							.getOutput().getMaxRighe()) + 1;
					String idLista = currentForm.getOutput().getIdLista();
					currentForm.setIdLista(idLista);
					super.addSbnMarcIdLista(request, idLista);
					currentForm.setMaxRighe(currentForm.getOutput()
							.getMaxRighe());
					currentForm.setTotRighe(currentForm.getOutput()
							.getTotRighe());
					currentForm.setNumBlocco(1);
					currentForm.setNumNotizie(currentForm.getOutput()
							.getTotRighe());
					currentForm.setTotBlocchi(numBlocchi);
				}
				return mapping.getInputForward();
			case crea_3:// ClassiDelegate.crea:
				currentForm.setEnableIndice(false);
				request.setAttribute(NavigazioneSemantica.ACTION_CALLER,
						mapping.getPath());
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			case lista_2:// RicercaSoggettoDelegate.lista:
				// currentForm.getOutput().getRisultati().clear();
				currentForm.setOutput((RicercaClasseListaVO) request
						.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA));
				if (currentForm.getOutput().getTotRighe() > 0) {
					int numBlocchi = (currentForm.getOutput().getTotRighe() / currentForm
							.getOutput().getMaxRighe()) + 1;
					String idLista = currentForm.getOutput().getIdLista();
					currentForm.setIdLista(idLista);
					super.addSbnMarcIdLista(request, idLista);
					currentForm.setMaxRighe(currentForm.getOutput()
							.getMaxRighe());
					currentForm.setTotRighe(currentForm.getOutput()
							.getTotRighe());
					currentForm.setNumBlocco(1);
					currentForm.setNumNotizie(currentForm.getOutput()
							.getTotRighe());
					currentForm.setTotBlocchi(numBlocchi);
					currentForm.setRicercaClasse(ricercaClasse);
					//almaviva5_20090604 #2946 aggiorna combo esamina
					currentForm.setComboGestioneEsamina(LabelGestioneSemantica
							.getComboGestioneSematicaPerEsamina(servlet
									.getServletContext(), request, form, new String[]{"CL"}, this));
					currentForm.setIdFunzioneEsamina("");
				}
				return mapping.getInputForward();
			case diagnostico_0:// RicercaSoggettoDelegate.diagnostico:
				currentForm.setEnableIndice(false);
				RicercaClasseListaVO output =
					(RicercaClasseListaVO) request.getAttribute(NavigazioneSemantica.OUTPUT_SINTETICA);
				if (output.isEsitoNonTrovato())
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.nontrovato"));
				else
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.gestioneSemantica.incongruo", output.getTestoEsito()));
				this.saveErrors(request, errors);
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

	public ActionForward crea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
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
		}
		request.setAttribute(
				NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE,
				currentForm.getRicercaClasse().getCodSistemaClassificazione());
		request.setAttribute(
				NavigazioneSemantica.DESCRIZIONE_SISTEMA_CLASSIFICAZIONE,
				currentForm.getRicercaClasse().getDescSistemaClassificazione());
		// request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE,
		// currentForm.getRicercaClasse().getSimbolo());
		if (deweyEd != null) {
			request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY,
					deweyEd);
		} else {
			request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY,
					currentForm.getRicercaClasse().getCodEdizioneDewey());

		}
		String descrEdizione = this.getDescrizioneEdizione(currentForm
				.getRicercaClasse().getCodEdizioneDewey().toUpperCase(), form);
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY,
				descrEdizione);
		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm
				.getOutput());
		// Viene settato il token per le transazioni successive
		this.saveToken(request);

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(currentForm.getRicercaClasse().isPolo()));
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getFolder());
		request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA,
				currentForm.getTitoliBiblio());
		String xid = currentForm.getCodSelezionato();
		for (Object elemento : currentForm.getOutput().getRisultati()) {
			if (((SinteticaClasseVO) elemento).getIdentificativoClasse()
					.equals(xid)) {
				request.setAttribute(
						NavigazioneSemantica.TRASCINA_TESTO_ARRIVO,
						((SinteticaClasseVO) elemento).getParole());
				break;
			}
		}
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_ARRIVO, xid);
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
				currentForm.getTestoTrascinaDa());
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
				currentForm.getNotazioneTrascinaDa());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
				currentForm.getDatiBibliografici());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaClasse().clone());
		request.setAttribute(NavigazioneSemantica.TESTO_OGGETTO_CORRENTE,
				currentForm.getRicercaClasse().getParole());
		request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE, currentForm
				.getRicercaClasse().getSimbolo());

		OggettoRiferimentoVO oggRif = currentForm.getOggettoRiferimento();
		if (oggRif.isEnabled())
			request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO, oggRif);

		ParametriThesauro.send(request, currentForm.getParametriThes() );
		return mapping.findForward("creaClasse");
	}

	public ActionForward dettaglio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		ActionMessages errors = new ActionMessages();
		List<String> items = getMultiBoxSelectedItems(currentForm.getCodClasse());
		if (ValidazioneDati.isFilled(items)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noBox"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		RicercaClassiVO ricercaClasse = currentForm.getRicercaClasse();
		request.setAttribute(
				NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE,
				ricercaClasse.getCodSistemaClassificazione());
		request.setAttribute(
				NavigazioneSemantica.DESCRIZIONE_SISTEMA_CLASSIFICAZIONE,
				ricercaClasse.getDescSistemaClassificazione());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(ricercaClasse.isPolo()));
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm
				.getOutput());

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		String codSelezionato = currentForm.getCodSelezionato();
		DettaglioClasseVO dettaglio = currentForm.getDettClaGen();
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		if (ValidazioneDati.isFilled(codSelezionato)) {
			String deweyEd = null;
			String idClasse = ricercaClasse.getIdentificativoClasse();
			if (ValidazioneDati.isFilled(idClasse)) {
				SimboloDeweyVO sd = SimboloDeweyVO.parse(idClasse);
				ricercaClasse.setCodSistemaClassificazione(sd.getSistema());
				ricercaClasse.setCodEdizioneDewey(sd.getEdizione());
				ricercaClasse.setSimbolo(sd.getSimbolo());

				request.setAttribute(
						NavigazioneSemantica.CODICE_EDIZIONE_DEWEY, ricercaClasse.getCodEdizioneDewey());

				deweyEd = CodiciProvider.unimarcToSBN(
						CodiciType.CODICE_EDIZIONE_CLASSE,
						ricercaClasse.getCodEdizioneDewey());

				request.setAttribute(
						NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY,
						deweyEd);
				request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE,
						ricercaClasse.getSimbolo());
			}
			for (Object i : currentForm.getOutput().getRisultati()) {
				SinteticaClasseVO sintClasse = (SinteticaClasseVO) i;
				if (sintClasse.getIdentificativoClasse().trim().equals(
						codSelezionato.trim())) {
					dettaglio.setIdentificativo(
							sintClasse.getIdentificativoClasse().trim());
					dettaglio.setCampoSistema(
							sintClasse.getSistema());
					dettaglio.setSimbolo(
							sintClasse.getSimbolo());
					request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE,
							dettaglio.getSimbolo());
					break;
				}
			}

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();

		CreaVariaClasseVO classe = null;
		try {

			classe = factory.getGestioneSemantica().analiticaClasse(
					ricercaClasse.isPolo(),
					dettaglio.getIdentificativo(),
					utenteCollegato.getTicket());
		} catch (EJBException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(
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

		dettaglio.setLivAut(classe.getLivello());
		// inserire campo per indicatore legato a titoli
		dettaglio.setDescrizione(classe.getDescrizione());
		dettaglio.setCampoSistema(classe.getCodSistemaClassificazione());
		if (classe.getCodEdizioneDewey() == null)
			dettaglio.setCampoEdizione("  ");
		else
			dettaglio.setCampoEdizione(classe.getCodEdizioneDewey());

		dettaglio.setDataAgg(classe.getDataVariazione());
		dettaglio.setDataIns(classe.getDataInserimento());
		dettaglio.setIndicatore("NO");
		dettaglio.setT005(classe.getT005());
		dettaglio.setLivelloPolo(ricercaClasse.isPolo());
		dettaglio.setUlterioreTermine(classe.getUlterioreTermine());
		dettaglio.setCostruito(classe.isCostruito());
		dettaglio.setCondiviso(classe.isCondiviso());
		dettaglio.setNumTitoliBiblio(classe.getNumTitoliBiblio());
		dettaglio.setNumTitoliPolo(classe.getNumTitoliPolo());
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, dettaglio);
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getFolder());
		request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA,
				currentForm.getTitoliBiblio());
		String xid = codSelezionato;
		for (Object elemento : currentForm.getOutput().getRisultati()) {
			if (((SinteticaClasseVO) elemento).getIdentificativoClasse()
					.equals(xid)) {
				request.setAttribute(
						NavigazioneSemantica.TRASCINA_TESTO_ARRIVO,
						((SinteticaClasseVO) elemento).getParole());
				break;
			}
		}
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_ARRIVO, xid);
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
				currentForm.getTestoTrascinaDa());
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
				currentForm.getNotazioneTrascinaDa());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
				currentForm.getDatiBibliografici());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				ricercaClasse.clone());

		OggettoRiferimentoVO oggRif = currentForm.getOggettoRiferimento();
		if (oggRif.isEnabled())
			request.setAttribute(NavigazioneSemantica.OGGETTO_RIFERIMENTO, oggRif);

		return mapping.findForward("esaminaClasse");
	}

	public ActionForward importa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
		ActionMessages errors = new ActionMessages();
		//almaviva5_20110304 #4231
		List<String> items = getMultiBoxSelectedItems(currentForm.getCodClasse());
		if (ValidazioneDati.isFilled(items)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noBox"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(currentForm.getRicercaClasse().isPolo()));
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm
				.getOutput());

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		String codSistemaClassificazione = currentForm.getRicercaClasse()
				.getCodSistemaClassificazione();
		String codEdizione = currentForm.getRicercaClasse()
				.getCodEdizioneDewey();
		if (ValidazioneDati.isFilled(currentForm.getCodSelezionato())) {

			if (!codSistemaClassificazione.equals("D"))
				currentForm.getRicercaClasse().setCodEdizioneDewey(
						NavigazioneSemantica.EDIZIONE_NON_DEWEY);

			for (Object i : currentForm.getOutput().getRisultati()) {
				SinteticaClasseVO sintClasse = (SinteticaClasseVO) i;
				if (sintClasse.getIdentificativoClasse().trim().equals(
						currentForm.getCodSelezionato().trim())) {
					currentForm.getDettClaGen().setIdentificativo(
							sintClasse.getIdentificativoClasse().trim());
					currentForm.getDettClaGen().setCampoSistema(
							sintClasse.getSistema());
					currentForm.getDettClaGen().setCampoEdizione(codEdizione);
					currentForm.getDettClaGen().setSimbolo(
							sintClasse.getSimbolo());
					request.setAttribute(NavigazioneSemantica.SIMBOLO_CLASSE,
							currentForm.getDettClaGen().getSimbolo());
					break;
				}
			}

		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato

			return mapping.getInputForward();
		}

		ClassiDelegate delegate = ClassiDelegate.getInstance(request);
		if (!delegate.isSistemaGestito(codSistemaClassificazione, codEdizione)) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.sistemaClasseNonGestito"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();// gestione errori java
		}

		request.setAttribute(
				NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE,
				codSistemaClassificazione);
		request.setAttribute(
				NavigazioneSemantica.DESCRIZIONE_SISTEMA_CLASSIFICAZIONE,
				currentForm.getRicercaClasse().getDescSistemaClassificazione());
		request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY,
				codEdizione);
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();


		CreaVariaClasseVO classe = null;
		CreaVariaClasseVO classeInPolo = null;
		try {
			// cerco la classe prima in polo
			classeInPolo = factory.getGestioneSemantica().analiticaClasse(true,
					currentForm.getCodSelezionato().trim(),
					utenteCollegato.getTicket());

			if (classeInPolo.isEsitoOk()) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.duplicataCla", currentForm
								.getCodSelezionato().trim()));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			// Errore diverso da non trovato
			if (!classeInPolo.getEsito().equals("3001"))
				throw new EJBException(classeInPolo.getTestoEsito());

			classe = factory.getGestioneSemantica().analiticaClasse(
					currentForm.getRicercaClasse().isPolo(),
					currentForm.getDettClaGen().getIdentificativo(),
					utenteCollegato.getTicket());
		} catch (EJBException e) {
			// errori indice
			errors.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(
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

		currentForm.getDettClaGen().setLivAut(classe.getLivello());
		// inserire campo per indicatore legato a titoli
		currentForm.getDettClaGen().setDescrizione(classe.getDescrizione());
		currentForm.getDettClaGen().setCampoSistema(
				classe.getCodSistemaClassificazione());
		currentForm.getDettClaGen().setCampoEdizione(
				classe.getCodEdizioneDewey());
		currentForm.getDettClaGen().setDataAgg(classe.getDataVariazione());
		currentForm.getDettClaGen().setDataIns(classe.getDataInserimento());
		currentForm.getDettClaGen().setIndicatore("NO");
		currentForm.getDettClaGen().setT005(classe.getT005());
		currentForm.getDettClaGen().setUlterioreTermine(
				classe.getUlterioreTermine());
		String descrEdizione = this.getDescrizioneEdizione(currentForm
				.getDettClaGen().getCampoEdizione().toUpperCase(), form);
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY,
				descrEdizione);
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, currentForm
				.getDettClaGen());
		request.setAttribute(NavigazioneSemantica.DATA_INSERIMENTO, classe
				.getDataInserimento());
		request.setAttribute(NavigazioneSemantica.DATA_MODIFICA, classe
				.getDataVariazione());
		request.setAttribute(
				NavigazioneSemantica.DATI_BIBLIOGRAFICI_PER_SEMANTICA,
				currentForm.getAreaDatiPassBiblioSemanticaVO());
		request.setAttribute(NavigazioneSemantica.FOLDER_CORRENTE, currentForm
				.getFolder());
		request.setAttribute(NavigazioneSemantica.TRASCINA_OUTPUT_SINTETICA,
				currentForm.getTitoliBiblio());
		String xid = currentForm.getCodSelezionato();
		for (Object elemento : currentForm.getOutput().getRisultati()) {
			if (((SinteticaClasseVO) elemento).getIdentificativoClasse()
					.equals(xid)) {
				request.setAttribute(
						NavigazioneSemantica.TRASCINA_TESTO_ARRIVO,
						((SinteticaClasseVO) elemento).getParole());
				break;
			}
		}
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_ARRIVO, xid);
		request.setAttribute(NavigazioneSemantica.TRASCINA_TESTO_PARTENZA,
				currentForm.getTestoTrascinaDa());
		request.setAttribute(NavigazioneSemantica.TRASCINA_CLASSE_PARTENZA,
				currentForm.getNotazioneTrascinaDa());
		request.setAttribute(NavigazioneSemantica.DATI_BIBLIOGRAFICI,
				currentForm.getDatiBibliografici());
		request.setAttribute(NavigazioneSemantica.PARAMETRI_RICERCA,
				currentForm.getRicercaClasse().clone());
		ParametriThesauro.send(request, currentForm.getParametriThes() );
		return mapping.findForward("importaClasse");

	}

	public ActionForward gestione(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		List<String> items = getMultiBoxSelectedItems(currentForm.getCodClasse());
		if (ValidazioneDati.isFilled(items)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.noBox"));
			return mapping.getInputForward();
		}

		request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
				.getPath());
		RicercaClassiVO ricercaClasse = currentForm.getRicercaClasse();
		request.setAttribute(
				NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE,
				ricercaClasse.getCodSistemaClassificazione());
		request.setAttribute(
				NavigazioneSemantica.DESCRIZIONE_SISTEMA_CLASSIFICAZIONE,
				ricercaClasse.getDescSistemaClassificazione());
		request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY,
				ricercaClasse.getCodEdizioneDewey());
		request.setAttribute(NavigazioneSemantica.DESCRIZIONE_EDIZIONE_DEWEY,
				ricercaClasse.getDescEdizioneDewey());

		request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
				new Boolean(ricercaClasse.isPolo()));
		request.setAttribute(NavigazioneSemantica.OUTPUT_SINTETICA, currentForm
				.getOutput());

		if (!isTokenValid(request)) {
			saveToken(request);
			return mapping.getInputForward();
		}

		DettaglioClasseVO dettaglio = currentForm.getDettClaGen();
		if (currentForm.getCodSelezionato() != null) {
			String codSelezionato = currentForm.getCodSelezionato().trim();
			List<SinteticaClasseVO> risultati = currentForm.getOutput().getRisultati();
			for (SinteticaClasseVO sintClasse : risultati) {
				if (sintClasse.getIdentificativoClasse().trim().equals(codSelezionato)) {
					dettaglio.setIdentificativo(sintClasse.getIdentificativoClasse().trim());
					dettaglio.setLivAut(sintClasse.getLivelloAutorita());
					break;
				}
			}

		} else {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.codiceNessunaSelezione"));
			return mapping.getInputForward();
		}

		// controllo liv autorità su profilo
		ClassiDelegate delegate = ClassiDelegate.getInstance(request);
		if (!delegate.isLivAutOk(dettaglio.getLivAut(), true))
			return mapping.getInputForward();

		//almaviva5_20130128 #5238
		UserVO utente = Navigation.getInstance(request).getUtente();
		SimboloDeweyVO sd = SimboloDeweyVO.parse(dettaglio.getIdentificativo());
		if (!delegate.verificaSistemaEdizionePerBiblioteca(utente.getCodPolo(), utente.getCodBib(), sd.getSistema() + sd.getEdizione() ) ) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.gestioneSemantica.classi.sistemaEdizioneNonAbilitato"));
			return mapping.getInputForward();
		}

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CreaVariaClasseVO classe = null;
		try {

			classe = factory.getGestioneSemantica().analiticaClasse(
					ricercaClasse.isPolo(),
					dettaglio.getIdentificativo(),
					utente.getTicket());
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

		DettaglioClasseVO dettaglio2 = new DettaglioClasseVO(classe);
		currentForm.setDettClaGen(dettaglio2);
		request.setAttribute(NavigazioneSemantica.DETTAGLIO_CLASSE, dettaglio2);

		return mapping.findForward("gestioneClasse");
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
		ListaClassiForm currentForm = (ListaClassiForm) form;
		int i;
		for (i = 0; i < currentForm.getOutput().getRisultati().size(); i++) {
			currentForm.setCodClasse(null);
			currentForm.setCodSelezionato(null);
		}
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
		int i;
		List<SinteticaClasseVO> risultati = currentForm.getOutput().getRisultati();
		int size = risultati.size();
		String[] selezione = new String[size];
		for (i = 0; i < size; i++) {
			SinteticaClasseVO ric = risultati.get(i);
			//almaviva5_20090604 #2943
			selezione[i] = ric.getIdentificativoClasse();
		}

		currentForm.setCodClasse(selezione);
		currentForm.setCodSelezionato(null);

		return mapping.getInputForward();
	}

	// METODO PER OTTENERE DESCRIZIONE DEL SISTEMA CLASSIFICAZIONE DATO UN
	// CODICE
	// SISTEMA
	private String getDescrizioneEdizione(String codice, ActionForm form) {
		ListaClassiForm currentForm = (ListaClassiForm) form;
		for (int index = 0; index < currentForm.getListaEdizioni().size(); index++) {
			ComboCodDescVO edi = currentForm
					.getListaEdizioni().get(index);
			if (edi.getCodice().equals(codice))
				return edi.getDescrizione();
		}
		return "Non trovato";
	}

	public ActionForward alto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
		ActionMessages errors = new ActionMessages();
		String alto = ScorrimentoLabel.altoLabel(currentForm.getRicercaClasse()
				.getSimbolo());

		// Verifico che la stringa sia una notazione dewey valida
		if (alto == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noSimboloValido"));

			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		currentForm.getRicercaClasse().setSimbolo(alto);
		String ricerca = currentForm.getRicercaClasse().getSimbolo();
		ActionForward forward = this.scorrimento(mapping, form, request,
				response, ricerca);

		return forward;

	}

	public ActionForward basso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
		ActionMessages errors = new ActionMessages();

		String basso = ScorrimentoLabel.bassoLabel(currentForm
				.getRicercaClasse().getSimbolo());

		// Verifico che la stringa sia una notazione dewey valida
		if (basso == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noSimboloValido"));

			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		currentForm.getRicercaClasse().setSimbolo(basso);
		String ricerca = currentForm.getRicercaClasse().getSimbolo();
		ActionForward forward = this.scorrimento(mapping, form, request,
				response, ricerca);

		return forward;

	}

	public ActionForward sinistra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
		ActionMessages errors = new ActionMessages();

		String sinistra = ScorrimentoLabel.sinistraLabel(currentForm
				.getRicercaClasse().getSimbolo());

		if (sinistra == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noSimboloValido"));

			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		currentForm.getRicercaClasse().setSimbolo(sinistra);
		String ricerca = currentForm.getRicercaClasse().getSimbolo();
		ActionForward forward = this.scorrimento(mapping, form, request,
				response, ricerca);

		return forward;

	}

	public ActionForward destra(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
		ActionMessages errors = new ActionMessages();

		String destra = ScorrimentoLabel.destraLabel(currentForm
				.getRicercaClasse().getSimbolo());

		// Verifico che la stringa sia una notazione dewey valida
		if (destra == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noSimboloValido"));

			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		currentForm.getRicercaClasse().setSimbolo(destra);
		String ricerca = currentForm.getRicercaClasse().getSimbolo();
		ActionForward forward = this.scorrimento(mapping, form, request,
				response, ricerca);

		return forward;
	}

	public ActionForward radice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
		ActionMessages errors = new ActionMessages();

		String radice = ScorrimentoLabel.radiceLabel(currentForm
				.getRicercaClasse().getSimbolo());
		// Verifico che la stringa sia una notazione dewey valida
		if (radice == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.noSimboloValido"));

			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		ActionForward forward = this.scorrimento(mapping, form, request,
				response, radice);

		return forward;
	}

	// METODO PER OTTENERE DESCRIZIONE DEL SISTEMA CLASSIFICAZIONE DATO UN
	// CODICE
	// SISTEMA
	private String getDescrizione(String codice, ActionForm form) {
		ListaClassiForm currentForm = (ListaClassiForm) form;
		for (int index = 0; index < currentForm
				.getListaSistemiClassificazione().size(); index++) {
			ComboCodDescVO sist = (ComboCodDescVO) currentForm
					.getListaSistemiClassificazione().get(index);
			if (sist.getCodice().equals(codice))
				return sist.getDescrizione();
		}
		return "Non trovato";
	}

	private ActionForward scorrimento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String ricerca) throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;
		log.info("Scorrimento simbolo: "
				+ currentForm.getRicercaClasse().getSimbolo());
		ActionMessages errors = new ActionMessages();
		currentForm.getRicercaClasse().setIdentificativoClasse(null);

		try {
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);

			String descrSistema = this.getDescrizione(currentForm
					.getRicercaClasse().getCodSistemaClassificazione()
					.toUpperCase(), form);
			currentForm.getRicercaClasse().setDescSistemaClassificazione(
					descrSistema);

			delegate.eseguiRicerca(currentForm.getRicercaClasse(), mapping);

			RicercaClasseResult op = delegate.getOperazione();
			String deweyEd = null;
			deweyEd = CodiciProvider.unimarcToSBN(
					CodiciType.CODICE_EDIZIONE_CLASSE,
					currentForm.getRicercaClasse().getCodEdizioneDewey());

			request.setAttribute(
					NavigazioneSemantica.CODICE_SISTEMA_CLASSIFICAZIONE,
					currentForm.getRicercaClasse()
							.getCodSistemaClassificazione());
			request.setAttribute(NavigazioneSemantica.CODICE_EDIZIONE_DEWEY,
					deweyEd);
			request.setAttribute(NavigazioneSemantica.LIVELLO_RICERCA_POLO,
					new Boolean(currentForm.getRicercaClasse().isPolo()));

			request.setAttribute(NavigazioneSemantica.ACTION_CALLER, mapping
					.getPath());
			request.setAttribute(
					NavigazioneSemantica.DESCRIZIONE_SISTEMA_CLASSIFICAZIONE,
					currentForm.getRicercaClasse()
							.getDescSistemaClassificazione());

			switch (op) {
			case analitica_1:// RicercaClasseDelegate.analitica:
				return this.unspecified(mapping, form, request, response);
			case crea_3:// RicercaClasseDelegate.crea:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			case lista_2:// RicercaClasseDelegate.lista:
				return this.unspecified(mapping, form, request, response);

			case diagnostico_0:// RicercaClasseDelegate.diagnostico:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.nontrovato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			default:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.gestioneSemantica.noselezione"));
				this.saveErrors(request, errors);
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

	public ActionForward biblio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		for (SinteticaClasseVO elem : currentForm.getOutput().getRisultati()) {

			if (elem.getIdentificativoClasse().equals(codSelezionato)) {
				currentForm.setCodSelezionato(elem.getIdentificativoClasse());
				currentForm.setDescrizione(elem.getParole());
				break;
			}
		}

		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		request.setAttribute(TitoliCollegatiInvoke.codBiblio, utenteCollegato.getCodBib() );
		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO);
		request.setAttribute(TitoliCollegatiInvoke.xidDiRicerca, currentForm
				.getCodSelezionato());
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

	public ActionForward titoliIndice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		for (SinteticaClasseVO elem : currentForm.getOutput().getRisultati()) {

			if (elem.getIdentificativoClasse().equals(codSelezionato)) {
				currentForm.setCodSelezionato(elem.getIdentificativoClasse());
				currentForm.setDescrizione(elem.getParole());
				break;
			}
		}

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		request
				.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
						codSelezionato);

		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc,
				currentForm.getDescrizione());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));
		Navigation navi = Navigation.getInstance(request);
		navi.addBookmark(SINT_TIT_COLL_CLA);
		return navi.goForward(mapping.findForward("titoliCollegatiPolo"));
	}

	public ActionForward polo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		for (SinteticaClasseVO elem : currentForm.getOutput().getRisultati()) {

			if (elem.getIdentificativoClasse().equals(codSelezionato)) {
				currentForm.setCodSelezionato(elem.getIdentificativoClasse());
				currentForm.setDescrizione(elem.getParole());
				break;
			}
		}

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request
				.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
						codSelezionato);

		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc,
				currentForm.getDescrizione());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));

		navi.addBookmark(SINT_TIT_COLL_CLA);
		return navi.goForward(
				mapping.findForward("delegate_titoliCollegati"));
	}

	public ActionForward poloFiltro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		for (SinteticaClasseVO elem : currentForm.getOutput().getRisultati()) {

			if (elem.getIdentificativoClasse().equals(codSelezionato)) {
				currentForm.setCodSelezionato(elem.getIdentificativoClasse());
				currentForm.setDescrizione(elem.getParole());
				break;
			}
		}

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO);
		request
				.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
						codSelezionato);

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

	public ActionForward indice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() || navi.isFromBack())
			return mapping.getInputForward();

		String codSelezionato = currentForm.getCodSelezionato();
		if (ValidazioneDati.strIsNull(codSelezionato)) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		for (SinteticaClasseVO elem : currentForm.getOutput().getRisultati()) {

			if (elem.getIdentificativoClasse().equals(codSelezionato)) {
				currentForm.setCodSelezionato(elem.getIdentificativoClasse());
				currentForm.setDescrizione(elem.getParole());
				break;
			}
		}

		request.setAttribute(TitoliCollegatiInvoke.livDiRicerca,
				TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE);
		request
				.setAttribute(TitoliCollegatiInvoke.xidDiRicerca,
						codSelezionato);

		request.setAttribute(TitoliCollegatiInvoke.xidDiRicercaDesc,
				currentForm.getDescrizione());
		request.setAttribute(TitoliCollegatiInvoke.visualCall, "SI");
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		request.setAttribute(TitoliCollegatiInvoke.oggChiamante, mapping
				.getPath());
		request.setAttribute(TitoliCollegatiInvoke.oggDiRicerca, new Integer(
				TitoliCollegatiInvoke.TITOLI_COLLEGATI_A_CLASSE));

		navi.addBookmark(SINT_TIT_COLL_CLA);
		return navi.goForward(
				mapping.findForward("delegate_titoliCollegati"));
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaClassiForm currentForm = (ListaClassiForm) form;

		// Correzione Bug esercizio 6304: corretto il caso in cui, al ritorno dalla ricerca dei titoli collegati senza aver trovato nulla,
		// si va in errore invece di riprospettare la vecchia mappa con l'opportuno diagnostico
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar() )
			return mapping.getInputForward();

		ActionMessages errors = new ActionMessages();
		String idFunzione = currentForm.getIdFunzioneEsamina();
		if (ValidazioneDati.strIsNull(idFunzione)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			return mapping.getInputForward();
		}

		return LabelGestioneSemantica.invokeActionMethod(idFunzione, servlet
				.getServletContext(), this, mapping, form, request, response);

	}

	private enum TipoAttivita {
		CREA, MODIFICA,

		TITOLI_COLL_POLO, TITOLI_COLL_POLO_FILTRO, TITOLI_COLL_INDICE, SIMILI_INDICE;
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form,
			String idCheck) {
		try {
			ListaClassiForm currentForm = (ListaClassiForm) form;
			TipoAttivita tipoAtt = TipoAttivita.valueOf(idCheck);
			ClassiDelegate delegate = ClassiDelegate.getInstance(request);
			boolean livelloPolo = currentForm.getRicercaClasse().isPolo();

			switch (tipoAtt) {
			case CREA:
				return delegate
						.isAbilitato(CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017);
			case MODIFICA:
				return delegate
						.isAbilitato(CodiciAttivita.getIstance().MODIFICA_CLASSE_1266);

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
