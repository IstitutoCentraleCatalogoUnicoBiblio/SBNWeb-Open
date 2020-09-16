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
package it.iccu.sbn.web.actions.servizi.elaborazioniDifferite;

import it.iccu.sbn.batch.servizi.BatchImportaUtenti;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchImportaUtentiVO;
import it.iccu.sbn.util.cipher.PasswordEncrypter;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.actionforms.servizi.elaborazioniDifferite.ImportaUtentiForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.action.NavigationBaseAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

public class ImportaUtentiAction extends NavigationBaseAction implements SbnAttivitaChecker {

	private static Logger log = Logger.getLogger(ImportaUtentiAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		// map.put("servizi.bottone.conferma", "conferma");
		// map.put("servizi.bottone.indietro", "indietro");
		map.put("servizi.bottone.caricaFileImportaUtenti", "caricaFileImportaUtenti");
		map.put("servizi.bottone.richiestaImportaUtenti", "richiestaImportaUtenti");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ImportaUtentiForm currentForm = (ImportaUtentiForm) form;
		if (currentForm.getDataDa() == null) {
			currentForm.setDataDa("");
		}

		return mapping.getInputForward();
	}

	// carica il file di input
	public ActionForward caricaFileImportaUtenti(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ImportaUtentiForm currentForm = (ImportaUtentiForm) form;
		FormFile file = currentForm.getUploadImmagine();
		byte[] fileData = currentForm.getUploadImmagine().getFileData();
		if (file == null || fileData == null || fileData.length == 0) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.importaUtenti.fileNotValid"));
			return mapping.getInputForward();
		}
		String fileName = createFileName(file.getFileName());
		String folderName = BatchImportaUtenti.getImportaUtentiHome() + File.separator;

		// LFV 18/07/2018 upload file CSV Esse3
		if (currentForm.isEsse3()) {
			log.info("Importazione da CSV Esse3");
			importaEsse3File(file, folderName + File.separator + fileName);
			currentForm.setNomeFile(fileName);

			LinkableTagUtils.addError(request,
					new ActionMessage("errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile",
							file.getFileName()));

			return mapping.getInputForward();
		}
		UserVO ute = Navigation.getInstance(request).getUtente();
		String polo = ute.getCodPolo();

		String fileNameNonValidi = "";
		int recordLetti = 0;
		byte[] buf;
		try {
			buf = currentForm.getUploadImmagine().getFileData();

			if (buf == null || buf.length == 0) {
				LinkableTagUtils.addError(request,
						new ActionMessage("errors.servizi.importaUtenti.erroreAcquisizioneRecordDaFile"));
				return mapping.getInputForward();
			}

			List<String> listaUtentiDaFile = new ArrayList<String>();

			String line = null;
			String lineOut = null;

			// fileName = "lettori.txt";
			fileNameNonValidi = "nonValidi_" + fileName;

			// Carica file su server
			BufferedReader br = null;
			OutputStreamWriter osw = null;
			// creo file per le segnalazioni di errore in fase di caricamento
			OutputStreamWriter osw_log = null;

			// DEVO Cancellare il vecchio file se esiste
			/*
			 * while ((line = reader.readLine()) != null) { line = line.trim(); //validare
			 * il record per lunghezza e campi obbligatori if
			 * (!ValidazioneDati.validaRecordUtenti(line)) { log.
			 * warn("caricaFileImportaUtenti() - Riga file input non valida o già presente: '"
			 * + line + "'"); continue; } listaUtentiDaFile.add(line); }
			 */

			try {
				br = new BufferedReader(new InputStreamReader(file.getInputStream()));
				osw = new OutputStreamWriter(new FileOutputStream(folderName + fileName, true));
				osw_log = new OutputStreamWriter(new FileOutputStream(folderName + fileNameNonValidi, true));

				while ((line = br.readLine()) != null) {
					// validare il record per lunghezza e campi obbligatori
					// if (!ValidazioneDati.validaRecordUtenti(line, recordLetti)) {
					recordLetti++;
					lineOut = validaRecUtenti(line, recordLetti, polo);
					if (lineOut.substring(0, 1).equals("1")) {
						// log.warn("caricaFileImportaUtenti() - Riga file input non valida o già
						// presente: '" + line + "'");
						// osw_log.write("caricaFileImportaUtenti() - Riga file input non valida o già
						// presente: \n'");
						osw_log.write(lineOut + "\n");
						osw_log.write(line + "\n");
						osw_log.flush();
						continue;
					}

					// listaUtentiDaFile.add(line);
					// osw.write(line+"\n");
					listaUtentiDaFile.add(lineOut);
					osw.write(lineOut + "\n");
					osw.flush();
				}

			} catch (Exception e) {
				throw e;
			} finally {
				FileUtil.close(br);
				FileUtil.close(osw);
				FileUtil.close(osw_log);
			} // end finally

			if (!ValidazioneDati.isFilled(listaUtentiDaFile)) {
				LinkableTagUtils.addError(request,
						new ActionMessage("errors.servizi.importaUtenti.erroreAcquisizioneRecordDaFile"));
				return mapping.getInputForward();
			}

			currentForm.setListaUtentiDaFile(listaUtentiDaFile);
			currentForm.setNomeFile(fileName);

			LinkableTagUtils.addError(request,
					new ActionMessage("errors.gestioneBibliografica.ricConfermaAcquisizioneBidDaFile",
							currentForm.getUploadImmagine().getFileName()));

			return mapping.getInputForward();

		} catch (Exception e) {

			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.fileNotValid"));

			log.error("ECCEZIONE: ", e);
			return mapping.getInputForward();
		}

	}
	// LFV 18/07/2018 Copia del file in locale
	private void importaEsse3File(FormFile file, String path) throws Exception, FileNotFoundException, IOException {
		FileUtil.uploadFile(file.getInputStream(), path, null);
	}

	private String createFileName(String name) {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + "_" + name;
	}

	// effettua la prenotazione del batch di importazione utenti
	public ActionForward richiestaImportaUtenti(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ImportaUtentiForm currentForm = (ImportaUtentiForm) form;
		// LFV 18/07/2018 Prenotazione batch import esse3
		if (currentForm.isEsse3()) {
			log.info("Prenotazione import utenti da CSV Esse3");
			String nomeFile = currentForm.getNomeFile();
			if (!ValidazioneDati.isFilled(nomeFile)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));
				return mapping.getInputForward();
			}
			UserVO utente = Navigation.getInstance(request).getUtente();
			String userTicket = Navigation.getInstance(request).getUserTicket();
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ParametriBatchImportaUtentiVO parametriBatch = preparaEsse3BatchParams(nomeFile, utente, userTicket);
			String idMessaggio = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(),
					parametriBatch, null);
			if (idMessaggio != null)
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idMessaggio));
			else
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));
			return mapping.getInputForward();

		}
		// verifica caricamento file
		if (!ValidazioneDati.isFilled(currentForm.getListaUtentiDaFile())) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.importaUtenti.fileMancante"));
			return mapping.getInputForward();
		}

		// Inizio verifica corretta impostazione data inizio autorizzazione
		Date now = new Date();
		if (currentForm.getDataDa().trim().equals("")) {
			/*
			 *
			 * LinkableTagUtils.addError(request, new
			 * ActionMessage("errors.servizi.importaUtenti.data"));
			 *
			 * resetToken(request); return mapping.getInputForward();
			 */
			// imposta data sistema ???
			Date dataOdierna = now;
			currentForm.setDataDa(DateUtil.formattaData(dataOdierna));
		}

		if (!currentForm.getDataDa().equals("")) {

			int ret = ValidazioneDati.validaData_1(currentForm.getDataDa());
			if (ret == 0) {
				Date dataAut = DateUtil.toDate(currentForm.getDataDa());
				dataAut = DateUtil.copiaOrario(now, dataAut);
				if (dataAut.compareTo(now) < 0) {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.importaUtenti.dataOdierna"));
					return mapping.getInputForward();
				}
			}
			if (ret != 0) {

				LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.importaUtenti.formatoData"));

				resetToken(request);
				return mapping.getInputForward();
			}

			/*
			 * SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); long
			 * longData1 = 0;
			 *
			 * try { Date dataAut = simpleDateFormat.parse(currentForm.getDataDa());
			 * longData1 = dataAut.getTime(); //inserire controllo data (gg > 31 mm > 13
			 * etc.) //non funziona
			 *
			 * if (DateUtil.isData(longData1)) {
			 *
			 * LinkableTagUtils.addError(request, new
			 * ActionMessage("errors.servizi.importaUtenti.formatoData"));
			 *
			 * resetToken(request); return mapping.getInputForward(); }
			 *
			 *
			 * } catch (ParseException e) {
			 *
			 * LinkableTagUtils.addError(request, new
			 * ActionMessage("errors.servizi.importaUtenti.formatoData"));
			 *
			 * resetToken(request); return mapping.getInputForward(); }
			 */

		}

		// Fine verifica corretta impostazione data inizio autorizzazione

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		UserVO utente = Navigation.getInstance(request).getUtente();

		ParametriBatchImportaUtentiVO ParametriBatchImportaUtentiVO = new ParametriBatchImportaUtentiVO();
		ParametriBatchImportaUtentiVO.setCodPolo(utente.getCodPolo());
		ParametriBatchImportaUtentiVO.setCodBib(utente.getCodBib());
		ParametriBatchImportaUtentiVO.setUser(utente.getUserId());
		ParametriBatchImportaUtentiVO.setCodAttivita(CodiciAttivita.getIstance().SRV_IMPORTA_UTENTI);

		String basePath = this.servlet.getServletContext().getRealPath(File.separator);
		ParametriBatchImportaUtentiVO.setBasePath(basePath);
		String downloadPath = StampeUtil.getBatchFilesPath();
		log.info("download path: " + downloadPath);
		String downloadLinkPath = "/"; // File.separator+"sbn"+File.separator+"download"+File.separator;
		ParametriBatchImportaUtentiVO.setDownloadPath(downloadPath);
		ParametriBatchImportaUtentiVO.setDownloadLinkPath(downloadLinkPath);
		ParametriBatchImportaUtentiVO.setTicket(Navigation.getInstance(request).getUserTicket());
		ParametriBatchImportaUtentiVO.setListaUtentiDaFile(currentForm.getListaUtentiDaFile());
		ParametriBatchImportaUtentiVO.setDataAut(currentForm.getDataDa());
		ParametriBatchImportaUtentiVO.setNomeF(currentForm.getNomeFile());

		String idMessaggio = "";

		try {
			idMessaggio = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utente.getTicket(),
					ParametriBatchImportaUtentiVO, null);
		} catch (ApplicationException e) {
			LinkableTagUtils.addError(request, new ActionMessage(e.getErrorCode().getErrorMessage()));

			return mapping.getInputForward();
		}

		if (idMessaggio != null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idMessaggio));

			return mapping.getInputForward();
		} else {
			LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));

			return mapping.getInputForward();
		}
	}

	private ParametriBatchImportaUtentiVO preparaEsse3BatchParams(String nomeFile, UserVO utente, String userTicket) {
		ParametriBatchImportaUtentiVO batchParams = new ParametriBatchImportaUtentiVO();

		batchParams.setCodPolo(utente.getCodPolo());
		batchParams.setCodBib(utente.getCodBib());
		batchParams.setUser(utente.getUserId());
		batchParams.setCodAttivita(CodiciAttivita.getIstance().SRV_IMPORTA_UTENTI);
		batchParams.setTicket(userTicket);
		batchParams.setImportFileName(nomeFile);
		String basePath = this.servlet.getServletContext().getRealPath(File.separator);
		batchParams.setBasePath(basePath);
		String downloadPath = StampeUtil.getBatchFilesPath();
		log.info("download path: " + downloadPath);
		String downloadLinkPath = "/"; // File.separator+"sbn"+File.separator+"download"+File.separator;
		batchParams.setDownloadPath(downloadPath);
		batchParams.setDownloadLinkPath(downloadLinkPath);
		batchParams.setIsEsse3(true);
		return batchParams;
	}

	// validazione record
	public static final String validaRecUtenti(String value, int nrec, String polo) {

		String ret = null;
		if (value == null)
			return "1 - Record null";

		String[] recordFields = value.split("\\|");
		if (recordFields.length != 31) {
			log.error("Record non valido alla riga " + nrec + ". Numero di campi errato.");
			return "1 - Record non valido alla riga " + nrec + ". Numero di campi errato.";
		}

		// almaviva5_20140714 #5578
		if (recordFields[28 - 1].trim().length() == 0) { // paese residenza
			if (recordFields[10 - 1].trim().length() > 0) {
				// se il paese non è impostato ma è presente la provincia si assume = IT
				recordFields[28 - 1] = "IT";
			}
		}

		if (recordFields[1 - 1].trim().equals("") // biblioteca
				|| recordFields[3 - 1].trim().equals("") // cognome
				|| recordFields[5 - 1].trim().equals("") // indirizzo_res
				|| recordFields[6 - 1].trim().equals("") // citta_res
				|| recordFields[7 - 1].trim().equals("") // cap_res
				|| recordFields[10 - 1].trim().equals("") // prov_res
				|| recordFields[17 - 1].trim().equals("") // sesso
				|| recordFields[18 - 1].trim().equals("") // data_nascita
				|| recordFields[19 - 1].trim().equals("") // luogo_nascita
				|| recordFields[20 - 1].trim().equals("") // cod_nascita
				|| recordFields[21 - 1].trim().equals("") // cod_ateneo
				|| recordFields[22 - 1].trim().equals("") // matricola
				|| recordFields[26 - 1].trim().equals("") // professione
				|| recordFields[31 - 1].trim().equals("") // data_fine_aut
				|| recordFields[28 - 1].trim().equals("") // paese residenza
		) {
			log.error("Record non valido alla riga " + nrec + ". Campi obbligatori NON impostati.");
			return "1 - Record non valido alla riga " + nrec + ". Campi obbligatori NON impostati.";
		}

		// controllo indirizzo posta elettronica
		if (recordFields[24 - 1].trim().equals("")) // impostare a null
		{
			recordFields[24 - 1] = "";
		}

		// controllo data nascita e trasformazione da ggmmaaaa in mm/gg/aaaa
		String appodata = recordFields[18 - 1].substring(0, 2) + "/" + recordFields[18 - 1].substring(2, 4) + "/"
				+ recordFields[18 - 1].substring(4, 8);

		if (ValidazioneDati.validaData_1(appodata) != 0) {
			log.error("Record non valido alla riga " + nrec + ". Data di nascita in formato errato: "
					+ recordFields[18 - 1]);
			return "1 - Record non valido alla riga " + nrec + ". Data di nascita in formato errato: "
					+ recordFields[18 - 1];
		} else {
			recordFields[18 - 1] = appodata.substring(3, 6) + appodata.substring(0, 3) + appodata.substring(6, 10);
		}

		if (recordFields[20 - 1].length() != 16) // codice fiscale
		{
			log.error("Record non valido alla riga " + nrec + ". Codice fiscale errato.");
			return "1 - Record non valido alla riga " + nrec + ". Codice fiscale errato.";
		}
		/*
		 * if (recordFields[4-1].length() > 25) //nome troppo lungo (troncare?) {
		 * log.error("Record non valido alla riga " + nrec + ". Nome > 25 caratteri.");
		 * return "1 - Record non valido alla riga " + nrec + ". Nome > 25 caratteri.";
		 * }
		 */

		// password di default e encrypt
		if (recordFields[2 - 1].trim().equals(""))
			recordFields[2 - 1] = recordFields[20 - 1];
		PasswordEncrypter crypt = new PasswordEncrypter(recordFields[2 - 1].trim());
		String password = crypt.encrypt(recordFields[2 - 1].trim());
		recordFields[2 - 1] = password;

		ret = StringUtils.join(recordFields, "|");

		// calcola chiave utente
		GeneraChiave key = new GeneraChiave();
		String cognomenome = recordFields[3 - 1] + " " + recordFields[4 - 1];
		try {
			key.estraiChiavi("", cognomenome);
			ret = ret + "|" + key.getKy_cles1_A() + "|";
		} catch (Exception e) {
			e.printStackTrace();
		}

		////// ret = ret + polo + "|";

		return ret;
	}// End validaRecUtenti

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		if (ValidazioneDati.equals(idCheck, "ESSE3")) {
			try {
				String esse3Bib = CommonConfiguration.getProperty(Configuration.ESSE3_BIB_APIKEY_FILE);
				return ValidazioneDati.isFilled(esse3Bib);
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return false;
	}

}
