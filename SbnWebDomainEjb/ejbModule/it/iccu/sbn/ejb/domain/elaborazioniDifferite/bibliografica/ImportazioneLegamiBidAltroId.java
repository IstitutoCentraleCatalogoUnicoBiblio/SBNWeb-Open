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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.bibliografica;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ImportaLegamiBidAltroIdVO;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.bibliografica.Tr_bid_altroid;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.regex.Pattern;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.leggiXID;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrNull;

public class ImportazioneLegamiBidAltroId {

	private UserTransaction tx;
	private ImportaLegamiBidAltroIdVO richiesta;
	private Logger log;

	private static final String SEPARATOR = "\\s";

	/*
		OCN Format
		Previous	 	"ocm"+8 digits
		Current 		"ocn"+9 digits
		Future 			"on"+10 or more digits
	*/

	//private static final Pattern OCLC_CONTROL_NUMBER = Pattern.compile("ocm\\d{8}|ocn\\d{9}|on\\d{10,14}");
	private static final Pattern INSTITUTION_ID_FORMAT = Pattern.compile("\\d{8,14}");

	public ImportazioneLegamiBidAltroId(UserTransaction tx, ImportaLegamiBidAltroIdVO richiesta,
			BatchLogWriter blw) {
		this.tx = tx;
		this.richiesta = richiesta;
		log = blw.getLogger();
	}

	public ElaborazioniDifferiteOutputVo execute() throws ApplicationException {

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);
		BufferedReader r = null;
		BufferedWriter w = null;
		int read = 0;
		int errors = 0;
		int inserted = 0;
		int updated = 0;
		int not_updated = 0; //read - (errors + inserted + updated);

		try {
			try {
				output.setStato(ConstantsJMS.STATO_ERROR);

				String inputFile = richiesta.getInputFile();
				if (!FileUtil.exists(inputFile))
					throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

				File f = new File(inputFile);
				try {
					BatchManager.getBatchManagerInstance().markForDeletion(f);
				} catch (Exception e) {
					throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
				}

				r = new BufferedReader(new FileReader(f));

				//setup report file
				String fileName = richiesta.getFirmaBatch() + ".htm";
				FileOutputStream out = new FileOutputStream(StampeUtil.getBatchFilesPath() + File.separator + fileName);
				w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				output.addDownload(fileName, fileName);

				//String codPolo = richiesta.getCodPolo();
				//String codBib = richiesta.getCodBib();
				String codIstituzione = richiesta.getCodIstituzione().getCd_istituzione();
				String firmaUtente = DaoManager.getFirmaUtente(richiesta.getTicket());

				log.debug("File di ingresso per i legami: " + richiesta.getNomeInputFile());

				writeReportHeader(w, "Importazione relazioni Titolo - Altro Id");

				TitoloDAO dao = new TitoloDAO();
				String line;

				while ((line = r.readLine()) != null) {
					try {
						read++;
						if ((read % 100) == 0) {
							log.debug("Legami elaborati: " + read);
							BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
						}

						DaoManager.begin(tx);
						line = trimOrEmpty(line).replaceAll("\\s+",  " ");
						String[] tokens = line.split(SEPARATOR);
						if (tokens.length != 2) {
							errors++;
							log.error(String.format("riga %d, formato errato.", read));
							writeReportRow(w, null, null, read, false, "formato riga errato");
							continue;
						}

						String altroId = tokens[0];
						String bid = tokens[1];

						if (!leggiXID(bid)) {
							errors++;
							writeReportRow(w, bid, altroId, read, false, "identificativo SBN non valido");
							continue;
						}

						if (!checkInstitutionId(altroId)) {
							errors++;
							writeReportRow(w, bid, altroId, read, false, String.format("identificativo %s non valido", richiesta.getCodIstituzione()));
							continue;
						}

						Tb_titolo t = dao.getTitoloLazy(bid);
						if (t == null) {
							errors++;
							log.error("titolo non trovato: " + bid);
							writeReportRow(w, bid, altroId, read, false, "titolo non trovato");
							continue;
						}

						String msg;
						try {
							boolean _new;
							Tr_bid_altroid tr_bid_altroid = dao.getInstitutionId(codIstituzione, bid);
							if (tr_bid_altroid == null) {
								//inserimento
								tr_bid_altroid = new Tr_bid_altroid();
								tr_bid_altroid.setTitolo(t);

								tr_bid_altroid.setCd_istituzione(codIstituzione);
								tr_bid_altroid.setUte_ins(firmaUtente);
								tr_bid_altroid.setTs_ins(DaoManager.now());

								msg = "legame inserito";
								_new = true;
							} else {
								msg = "legame aggiornato";
								_new = false;
							}

							BigInteger new_id = new BigInteger(altroId);
							BigInteger old_id = ValidazioneDati.coalesce(tr_bid_altroid.getIst_id(), BigInteger.ZERO);
							if (!old_id.equals(new_id)) {
								//aggiornamento
								tr_bid_altroid.setIst_id(new_id);
								tr_bid_altroid.setUte_var(firmaUtente);
								tr_bid_altroid.setFl_canc('N');

								dao.aggiornaInstitutionId(tr_bid_altroid);

								if (_new)
									inserted++;
								else
									updated++;

							} else {
								not_updated++;
								msg = "legame già presente";
							}

						} catch (DaoManagerException e) {
							errors++;
							DaoManager.rollback(tx);
							log.error("errore aggiornamento titolo: " + bid, e);
							writeReportRow(w, bid, altroId, read, false, "errore aggiornamento");
							continue;
						}

						//tutto ok
						DaoManager.commit(tx);

						//scrittura report
						writeReportRow(w, bid, altroId, read, true, msg);

					} catch (BatchInterruptedException e) {
						throw new ApplicationException(SbnErrorTypes.BATCH_MANUAL_STOP);

					} catch (Exception e) {
						errors++;
						DaoManager.rollback(tx);
						log.error("errore lettura identificativo: " + line);
						writeReportRow(w, null, null, read, false, "errore lettura identificativo");
						continue;
					}
				}

				log.debug("righe lette:          " + read);
				log.debug("righe inserite:       " + inserted);
				log.debug("righe aggiornate:     " + updated);
				log.debug("righe non aggiornate: " + not_updated);
				log.debug("righe con errori:     " + errors);

				writeReportFooter(w, read, inserted, updated, not_updated, errors);

				output.setStato(ConstantsJMS.STATO_OK);

			} catch (ApplicationException e) {
				DaoManager.rollback(tx);
				throw e;
			} catch (FileNotFoundException e) {
				DaoManager.rollback(tx);
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
			} catch (IOException e) {
				DaoManager.rollback(tx);
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
			}

		} finally {
			try {
				DaoManager.commit(tx);
			} catch (Exception e) {}

			FileUtil.close(r);
			FileUtil.flush(w);
			FileUtil.close(w);
		}

		return output;

	}

	private boolean checkInstitutionId(String id) {
		return INSTITUTION_ID_FORMAT.matcher(id).matches();

	}

	protected void writeReportHeader(Writer w, String title) throws IOException {
		w.append("<!DOCTYPE html>");
		w.append("<html>");
		w.append("<head>");
		w.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		w.append("<title>").append(title).append("</title>");
		w.append("</head>");
		w.append("<body>");

		w.append("<h2>").append(title).append("</h2>");
		w.append("<h4>Biblioteca:&nbsp;").append(richiesta.getCodPolo()).append(richiesta.getCodBib()).append("</h4>");
		w.append("<h4>Istituzione:&nbsp;").append(richiesta.getCodIstituzione().name()).append("</h4>");
		w.append("<h4>Data:&nbsp;").append(DateUtil.formattaData(System.currentTimeMillis())).append("</h4>");
		w.append("<hr/><br/>");

		w.append("<table width=\"90%\" border=\"1\">");

		w.append("<tr>");
		w.append("<th>n.ro</th>");
		w.append("<th>BID</th>");
		w.append("<th>ALTRO ID</th>");
		w.append("<th>Esito</th>");
		w.append("<th>Messaggio</th>");
		w.append("</tr>");
	}

	protected static final String HTML_NBSP = "&nbsp;";

	protected void writeReportRow(Writer w, String bid, String altroId, int idx, boolean esito, String msg) throws IOException {
		w.append("<tr>");
		w.append("<td>").append(String.valueOf(idx)).append("</td>");
		w.append("<td>").append(coalesce(trimOrNull(bid), HTML_NBSP) ).append("</td>");
		w.append("<td>").append(coalesce(trimOrNull(altroId), HTML_NBSP) ).append("</td>");
		w.append("<td>").append(esito ? "OK" : "ERROR").append("</td>");
		w.append("<td>").append(coalesce(msg, HTML_NBSP)).append("</td>");
	}

	protected void writeReportFooter(Writer w, int read, int inserted, int updated, int not_updated, int errors) throws IOException {
		w.append("</table>");
		w.append("<br/>");
		w.append("<h4>relazioni lette:&nbsp;").append(String.valueOf(read)).append("</h4>");
		w.append("<h4>relazioni inserite:&nbsp;").append(String.valueOf(inserted)).append("</h4>");
		w.append("<h4>relazioni aggiornate:&nbsp;").append(String.valueOf(updated)).append("</h4>");
		w.append("<h4>relazioni non aggiornate:&nbsp;").append(String.valueOf(not_updated)).append("</h4>");
		w.append("<h4>relazioni con errori:&nbsp;").append(String.valueOf(errors)).append("</h4>");
		w.append("</body>");
		w.append("</html>");
	}

}
