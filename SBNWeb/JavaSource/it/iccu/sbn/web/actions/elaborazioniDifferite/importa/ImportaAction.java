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
package it.iccu.sbn.web.actions.elaborazioniDifferite.importa;

import it.iccu.sbn.batch.unimarc.ImportUnimarcBatch;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.importa.ImportaVO;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.importa.ImportaForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.actions.common.documentofisico.RicercaInventariCollocazioniAction;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

public class ImportaAction  extends RicercaInventariCollocazioniAction {

	private static Logger log = Logger.getLogger(ImportaAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = super.getKeyMethodMap();
		map.put("button.caricafile", "caricaFile");					// metodo per caricamento files (non attiva batch)
		map.put("button.richiestaImport", "importa"); 				// metodo per caricamento record unimarc su db
		map.put("button.richiestaVerificaBid", "verificaBid");		// metodo per verifica bid in polo/indice
		map.put("button.richiestaEtichette", "importaEtichette"); 	// metodo per trattamento etichette
		map.put("button.richiestacancellaTabelleAppoggio", "cancellazioneTabLavoro"); 	// metodo per cancellazione record su import1 e varie
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		ImportaForm currentForm = (ImportaForm) form;
		try{
			super.unspecified(mapping, currentForm, request, response);
			this.saveMessages(request, ConfermaDati.bottoneGenerico(this, mapping, request));

			// controllo se ho già i dati in sessione;
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();

			this.saveToken(request);
			// controllo se ho già i dati in sessione;
			if(!currentForm.isSessione())	{
				currentForm.setTicket(Navigation.getInstance(request).getUserTicket());
				currentForm.setCodBib(Navigation.getInstance(request).getUtente().getCodBib());
				currentForm.setCodPolo(Navigation.getInstance(request).getUtente().getCodPolo());
				currentForm.setDescrBib(Navigation.getInstance(request).getUtente().getBiblioteca());
//				this.loadPagina(form);
				currentForm.setSessione(true);
			}

//			if (!currentForm.isInitialized()) {
//				ImportaVO impVO = new ImportaVO();
//				currentForm.setImporta(impVO);
//				UserVO utenteCollegato = navi.getUtente();
//				currentForm.setInitialized(true);
//			}

			// default polo
			currentForm.setVerificaBidPoloIndice("P");


			// Modifica almaviva2 05.07.2012
			// Richiesta Contardi per accodare più file sulla tabella import1 con lo stesso numero richiesta
			currentForm.setVerificaTipoCaricamento("F");
			currentForm.setIdRichiestaCaricamento("");

			this.loadEtichette(currentForm);

			// Viene settato il token per le transazioni successive
			this.saveToken(request);

		} catch (ValidationException ve) {
			log.error(ve.getMessage(), ve);
			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ ve.getMessage()));

			return mapping.getInputForward();
		} catch (Exception e) { // altri tipi di errore
			log.error(e.getMessage(), e);

			LinkableTagUtils.addError(request, new ActionMessage("error.documentofisico."+ e.getMessage())); if (!ValidazioneDati.isFilled(e.getMessage())) log.error("", e);

			return mapping.getInputForward();
		}
		return mapping.getInputForward();
	}

	public ActionForward caricaFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		ImportaForm currentForm = (ImportaForm) form;
		ImportaVO importaVO = currentForm.getImporta().copy();

		FormFile file = currentForm.getFileIdList();

		if (file == null || file.getFileSize() == 0) {
			return mapping.getInputForward();
		}

		try {

			// nome univoco del file da importare
			String fileName = String.format("%s_%s", new SimpleDateFormat("yyyyMMddHHmmss").format(DaoManager.now()), file.getFileName());

			// Carica file su cartella temporanea
			try {
				String absFileName = ImportUnimarcBatch.getImportHome() + File.separator + fileName;
				FileUtil.uploadFile(file.getInputStream(), absFileName, null);

			} catch (Exception e) {
				throw e;
			}

			importaVO.setImportFileName(fileName);
			currentForm.setImporta(importaVO);

			LinkableTagUtils.addError(request,
				new ActionMessage(
					"errors.importa.ricConfermaAcquisizioneFile",
					file.getFileName())
			);

			return mapping.getInputForward();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			LinkableTagUtils.addError(request, new ActionMessage("errors.importa.marFileNotValid"));

			return mapping.getInputForward();
		}

	}


	public ActionForward importa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ImportaForm currentForm = (ImportaForm) form;
		String idBatch = null;


		// Modifica almaviva2 05.07.2012
		// Richiesta Contardi per accodare più file sulla tabella import1 con lo stesso numero richiesta
		if (currentForm.getVerificaTipoCaricamento().equals("F") && !currentForm.getIdRichiestaCaricamento().equals("")) {
			String msg;
			msg = "Attenzione: Nel caso di prima richiesta il campo Numero richiesta non deve essere impostato";
			LinkableTagUtils.addError(request, new ActionMessage(msg));

			return mapping.getInputForward();
		} else if (currentForm.getVerificaTipoCaricamento().equals("N")	&& currentForm.getIdRichiestaCaricamento().equals("")) {
			String msg;
			msg = "Attenzione: Nel caso di richiesta successiva è necessario impostare il campo Numero richiesta";
			LinkableTagUtils.addError(request, new ActionMessage(msg));

			return mapping.getInputForward();
		}

		try {
			resetToken(request);

//			// Prenota import
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

//			// Setting biblioteca corrente (6 caratteri)
//			// codice polo + codice biblioteca
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			ImportaVO importaVO = currentForm.getImporta().copy();

			importaVO.setStatoImport(ImportaVO._STATO_CARICA_RECORD);

			importaVO.setCodPolo(utenteCollegato.getCodPolo());
			importaVO.setCodBib(utenteCollegato.getCodBib());
			importaVO.setUser(utenteCollegato.getUserId());
			importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

//			if (currentForm.isUnimarc() )
//				importaVO.validate();

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			importaVO.setBasePath(basePath);
			String downloadPath = StampeUtil.getBatchFilesPath();
			importaVO.setDownloadPath(downloadPath);
			importaVO.setDownloadLinkPath("/"); // eliminato

			// Modifica almaviva2 05.07.2012
			// Richiesta Contardi per accodare più file sulla tabella import1 con lo stesso numero richiesta
			importaVO.setNumRichiestaCaricamento(currentForm.getIdRichiestaCaricamento());

			idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);

		} catch (ValidationException e) {
			log.error(e.getMessage(), e);
			String msg;
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.GDF_GENERIC)
				msg = error.getErrorMessage();
			else
				msg = "error.documentofisico." + e.getMsg();

			LinkableTagUtils.addError(request, new ActionMessage(msg));

			return mapping.getInputForward();
		}

		if (idBatch != null) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.prenotazione.ok", idBatch));

			return mapping.getInputForward();
		} else {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.prenotazione.failed"));

			return mapping.getInputForward();
		}

	}


	public ActionForward verificaBid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ImportaForm currentForm = (ImportaForm) form;
		String idBatch = null;


		try {
			resetToken(request);

			// Prenota import
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// Setting biblioteca corrente (6 caratteri)
			// codice polo + codice biblioteca
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			ImportaVO importaVO = currentForm.getImporta().copy();

			importaVO.setStatoImport(ImportaVO._STATO_VERIFICA_BID);
			importaVO.setRicercaInPolo( "P".equalsIgnoreCase(currentForm.getVerificaBidPoloIndice()) );
			importaVO.setNumRichiestaVerificaBid(currentForm.getIdRichiestaVerificaBid());

			importaVO.setCodPolo(utenteCollegato.getCodPolo());
			importaVO.setCodBib(utenteCollegato.getCodBib());
			importaVO.setUser(utenteCollegato.getUserId());
			importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

			importaVO.setDownloadPath(StampeUtil.getBatchFilesPath());
			importaVO.setDownloadLinkPath("/");
			importaVO.setTicket(utenteCollegato.getTicket());

			idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);

		} catch (ValidationException e) {
			log.error(e.getMessage(), e);
			String msg;
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.GDF_GENERIC)
				msg = error.getErrorMessage();
			else
				msg = "error.documentofisico." + e.getMsg();

			LinkableTagUtils.addError(request, new ActionMessage(msg));

			return mapping.getInputForward();
		}

		if (idBatch != null) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.prenotazione.ok", idBatch));

			return mapping.getInputForward();
		} else {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.prenotazione.failed"));

			return mapping.getInputForward();
		}

	}


	public ActionForward importaEtichette(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ImportaForm currentForm = (ImportaForm) form;
		String idBatch = null;


		try {
			resetToken(request);

//			// Prenota import
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// controllo fisso su 950 (da riformulare)
			if ( ("950".equals(currentForm.getEtichettaSelez())) 	// etichetta 950
					&& (currentForm.getIdRichiesta()!=null)			// nr richiesta valorizzato
			){

//				// Setting biblioteca corrente (6 caratteri)
//				// codice polo + codice biblioteca
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ImportaVO importaVO = currentForm.getImporta().copy();

				importaVO.setStatoImport(ImportaVO._STATO_UNI_950);
				importaVO.setNumRichiesta(currentForm.getIdRichiesta());

				importaVO.setCodPolo(utenteCollegato.getCodPolo());
				importaVO.setCodBib(utenteCollegato.getCodBib());
				importaVO.setUser(utenteCollegato.getUserId());
				importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				importaVO.setBasePath(basePath);
				importaVO.setDownloadPath(StampeUtil.getBatchFilesPath());
				importaVO.setDownloadLinkPath("/"); // eliminato

				importaVO.setTicket(utenteCollegato.getTicket());

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);
			} else if ( ("2xx".equals(currentForm.getEtichettaSelez())) && (currentForm.getIdRichiesta()!=null)){

				// Setting biblioteca corrente (6 caratteri) codice polo + codice biblioteca
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ImportaVO importaVO = currentForm.getImporta().copy();

				importaVO.setStatoImport(ImportaVO._STATO_UNI_200);
				importaVO.setNumRichiesta(currentForm.getIdRichiesta());

				importaVO.setCodPolo(utenteCollegato.getCodPolo());
				importaVO.setCodBib(utenteCollegato.getCodBib());
				importaVO.setUser(utenteCollegato.getUserId());
				importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				importaVO.setBasePath(basePath);
				importaVO.setDownloadPath(StampeUtil.getBatchFilesPath());
				importaVO.setDownloadLinkPath("/"); // eliminato
				importaVO.setTicket(utenteCollegato.getTicket());

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);


			} else if ( ("7xx".equals(currentForm.getEtichettaSelez())) && (currentForm.getIdRichiesta()!=null)){

				// Setting biblioteca corrente (6 caratteri) codice polo + codice biblioteca
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ImportaVO importaVO = currentForm.getImporta().copy();

				importaVO.setStatoImport(ImportaVO._STATO_UNI_700);
				importaVO.setNumRichiesta(currentForm.getIdRichiesta());

				importaVO.setCodPolo(utenteCollegato.getCodPolo());
				importaVO.setCodBib(utenteCollegato.getCodBib());
				importaVO.setUser(utenteCollegato.getUserId());
				importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				importaVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				importaVO.setDownloadPath(downloadPath);
				importaVO.setDownloadLinkPath("/"); // eliminato
				importaVO.setTicket(utenteCollegato.getTicket());

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);

			} else if ( ("4xx".equals(currentForm.getEtichettaSelez())) && (currentForm.getIdRichiesta()!=null)){

				// Setting biblioteca corrente (6 caratteri) codice polo + codice biblioteca
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ImportaVO importaVO = currentForm.getImporta().copy();

				importaVO.setStatoImport(ImportaVO._STATO_UNI_410);
				importaVO.setNumRichiesta(currentForm.getIdRichiesta());

				importaVO.setCodPolo(utenteCollegato.getCodPolo());
				importaVO.setCodBib(utenteCollegato.getCodBib());
				importaVO.setUser(utenteCollegato.getUserId());
				importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				importaVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				importaVO.setDownloadPath(downloadPath);
				importaVO.setDownloadLinkPath("/"); // eliminato

				importaVO.setTicket(utenteCollegato.getTicket());

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);

			} else if ( ("5xx".equals(currentForm.getEtichettaSelez())) && (currentForm.getIdRichiesta()!=null)){
				// Setting biblioteca corrente (6 caratteri) codice polo + codice biblioteca
				// ATTENZIONE --> In questo momento l'importazione dei titoli Uniformi non viene considerata
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ImportaVO importaVO = currentForm.getImporta().copy();

				importaVO.setStatoImport(ImportaVO._STATO_UNI_500);
				importaVO.setNumRichiesta(currentForm.getIdRichiesta());

				importaVO.setCodPolo(utenteCollegato.getCodPolo());
				importaVO.setCodBib(utenteCollegato.getCodBib());
				importaVO.setUser(utenteCollegato.getUserId());
				importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				importaVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				importaVO.setDownloadPath(downloadPath);
				importaVO.setDownloadLinkPath("/"); // eliminato
				importaVO.setTicket(utenteCollegato.getTicket());

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);

			} else if ( ("6xx".equals(currentForm.getEtichettaSelez())) && (currentForm.getIdRichiesta()!=null)){
				// Setting biblioteca corrente (6 caratteri) codice polo + codice biblioteca
				// ATTENZIONE --> In questo momento l'importazione di Classi/Soggetti non viene considerata
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ImportaVO importaVO = currentForm.getImporta().copy();

				importaVO.setStatoImport(ImportaVO._STATO_UNI_600);
				importaVO.setNumRichiesta(currentForm.getIdRichiesta());

				importaVO.setCodPolo(utenteCollegato.getCodPolo());
				importaVO.setCodBib(utenteCollegato.getCodBib());
				importaVO.setUser(utenteCollegato.getUserId());
				importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				importaVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				importaVO.setDownloadPath(downloadPath);
				importaVO.setDownloadLinkPath("/"); // eliminato

				importaVO.setTicket(utenteCollegato.getTicket());

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);

			} else if ( ("ALL".equals(currentForm.getEtichettaSelez())) && (currentForm.getIdRichiesta()!=null)){

				// Setting biblioteca corrente (6 caratteri) codice polo + codice biblioteca
				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ImportaVO importaVO = currentForm.getImporta().copy();

				importaVO.setStatoImport(ImportaVO._STATO_UNI_1000);
				importaVO.setNumRichiesta(currentForm.getIdRichiesta());

				importaVO.setCodPolo(utenteCollegato.getCodPolo());
				importaVO.setCodBib(utenteCollegato.getCodBib());
				importaVO.setUser(utenteCollegato.getUserId());
				importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				importaVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				importaVO.setDownloadPath(downloadPath);
				importaVO.setDownloadLinkPath("/"); // eliminato

				importaVO.setTicket(utenteCollegato.getTicket());

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);
			}


		} catch (ValidationException e) {
			log.error(e.getMessage(), e);
			String msg;
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.GDF_GENERIC)
				msg = error.getErrorMessage();
			else
				msg = "error.documentofisico." + e.getMsg();

			LinkableTagUtils.addError(request, new ActionMessage(msg));

			return mapping.getInputForward();
		}

		if (idBatch != null) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.prenotazione.ok", idBatch));

			return mapping.getInputForward();
		} else {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.prenotazione.failed"));

			return mapping.getInputForward();
		}

	}


	// Modifica almaviva2 09.07.2012 - Inserimento nuovo valore per richiesta cancellazione tabelle lavoro per nr_richiesta
	public ActionForward cancellazioneTabLavoro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ImportaForm currentForm = (ImportaForm) form;
		String idBatch = null;


		if (currentForm.getIdRichiestaCancellazione() == null || currentForm.getIdRichiestaCancellazione().trim().equals("")) {
			String msg;
			msg = "Attenzione: Valorizzare il Numero richiesta per attivare la cancellazione";
			LinkableTagUtils.addError(request, new ActionMessage(msg));

			return mapping.getInputForward();
		}

		try {
			resetToken(request);
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			if (currentForm.getIdRichiestaCancellazione()!=null) {

				UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
				ImportaVO importaVO = currentForm.getImporta().copy();

				importaVO.setStatoImport(ImportaVO._STATO_PER_CANCELLAZIONE);
				importaVO.setNumRichiestaCancellazione(currentForm.getIdRichiestaCancellazione());

				importaVO.setCodPolo(utenteCollegato.getCodPolo());
				importaVO.setCodBib(utenteCollegato.getCodBib());
				importaVO.setUser(utenteCollegato.getUserId());
				importaVO.setCodAttivita(CodiciAttivita.getIstance().IMPORTA_DOCUMENTI_1037);

				String basePath = this.servlet.getServletContext().getRealPath(File.separator);
				importaVO.setBasePath(basePath);
				String downloadPath = StampeUtil.getBatchFilesPath();
				importaVO.setDownloadPath(downloadPath);
				importaVO.setDownloadLinkPath("/"); // eliminato
				importaVO.setTicket(utenteCollegato.getTicket());

				idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), importaVO, null);
			}

		} catch (ValidationException e) {
			log.error(e.getMessage(), e);
			String msg;
			SbnErrorTypes error = e.getErrorCode();
			if (error != SbnErrorTypes.GDF_GENERIC)
				msg = error.getErrorMessage();
			else
				msg = "error.documentofisico." + e.getMsg();

			LinkableTagUtils.addError(request, new ActionMessage(msg));

			return mapping.getInputForward();
		}

		if (idBatch != null) {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.prenotazione.ok", idBatch));

			return mapping.getInputForward();
		} else {
			LinkableTagUtils.addError(request, new ActionMessage(
					"errors.prenotazione.failed"));

			return mapping.getInputForward();
		}
	}


	private void loadEtichette(ImportaForm importaForm) throws Exception {
		List<StrutturaCombo> lista = new ArrayList<StrutturaCombo>();
		//
		// Inizio modifica almaviva2 Per gestione caricamento delle etichette UNIMARC Bibligrafiche/Semantiche
		// BUG MANTIS 5035 (collaudo) 26.06.2012 All'etichetta 950 nella maschera di Import aggiunta la decodifica: (dati gestionali).
		// almaviva2 marzo 2016 - intervento su richiesta mail Scognamiglio:
		// nella tabella a tendina “Elabora etichette UNIMARC” della mappa dell’import
		// (Invio richieste funzioni di servizio > Area UNIMARC > Importa documenti), l’ordinamento (che rispecchia così la
		// sequenza in cui si fanno le operazioni) è questo: 700, 200, 400, 500, 600, 001, 950.
		lista.add(new StrutturaCombo(" ", " ") );
		lista.add(new StrutturaCombo("7xx", "7xx (autori legati a documento)") );
		lista.add(new StrutturaCombo("2xx", "2xx (documenti con etichetta 200 propria)") );
		lista.add(new StrutturaCombo("4xx", "4xx (titoli legati a documento con etichetta 200 linkata)") );
		lista.add(new StrutturaCombo("5xx", "5xx (titoli uniformi legati a documento)") );
		lista.add(new StrutturaCombo("6xx", "6xx (soggetti e classi legati a documento)") );
		lista.add(new StrutturaCombo("ALL", "001 (ricostruzione reticolo)") );
		lista.add(new StrutturaCombo("950", "950 (dati gestionali)") );
		// Fine modifica almaviva2 Per gestione caricamento delle etichette UNIMARC Bibligrafiche/Semantiche

		importaForm.setListaEtichette(lista);
	}

}
