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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.servizi.sale.Sale;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.BatchInterruptedException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.servizi.batch.RifiutaPrenotazioniScaduteVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.StatoPrenotazionePosto;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.servizi.SaleDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_prenotazione_posto;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.ConvertiVo.ConvertToWeb;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;

public class RifiutaPrenotazioniScadute {

	static Logger log = Logger.getLogger(RifiutaPrenotazioniScadute.class);

	static SimpleDateFormat _formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");

	static Reference<Sale> sale = new Reference<Sale>() {
		@Override
		protected Sale init() throws Exception {
			return DomainEJBFactory.getInstance().getSale();
		}};

	private UserTransaction tx;
	private RifiutaPrenotazioniScaduteVO richiesta;
	private Logger _log;

	private PrenotazionePostoVO prenotazione = new PrenotazionePostoVO();

	public RifiutaPrenotazioniScadute(UserTransaction tx, RifiutaPrenotazioniScaduteVO richiesta,
			BatchLogWriter blw) {
		this.tx = tx;
		this.richiesta = richiesta;
		this._log = blw != null ? blw.getLogger() : log;
	}

	public ElaborazioniDifferiteOutputVo execute() throws SbnBaseException {

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);
		BufferedWriter w = null;
		int read = 0;
		int errors = 0;
		int updated = 0;

		try {
			try {
				output.setStato(ConstantsJMS.STATO_ERROR);

				richiesta.validate();

				String firmaUtente = richiesta.isLivelloPolo() ?
						richiesta.getCodPolo() + Constants.ROOT_BIB + richiesta.getUser() :
						DaoManager.getFirmaUtente(richiesta.getTicket());

				//setup report file
				String fileName = richiesta.getFirmaBatch() + ".htm";
				FileOutputStream out = new FileOutputStream(StampeUtil.getBatchFilesPath() + File.separator + fileName);
				w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				output.addDownload(fileName, fileName);

				Timestamp dataFinePrev = DateUtil.withTimeAtEndOfDay(richiesta.getDataFinePrevista());
				log.debug("Data limite: " + dataFinePrev);

				writeReportHeader(w, "Rifiuto prenotazioni posti scadute");

				SaleDAO dao = new SaleDAO();
				DaoManager.begin(tx);
				List<Integer> prenotazioni = richiesta.isLivelloPolo() ?
						dao.getListaIdPrenotazioniPostoScadute(null, null, dataFinePrev) :
						dao.getListaIdPrenotazioniPostoScadute(richiesta.getCodPolo(), richiesta.getCodBib(), dataFinePrev);

				for (Integer id : prenotazioni) {
					try {
						read++;
						if ((read % 100) == 0) {
							_log.debug("prenotazioni elaborate: " + read);
							BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());
						}

						DaoManager.begin(tx);
						Tbl_prenotazione_posto pp = dao.getPrenotazionePostoById(id);
						prenotazione = ConvertToWeb.Sale.prenotazione(pp, null);
						prenotazione.setUteVar(firmaUtente);

						switch (prenotazione.getStato()) {
						case IMMESSA:
							prenotazione.setStato(StatoPrenotazionePosto.ANNULLATA);
							prenotazione = sale.get().rifiutaPrenotazionePosto(richiesta.getTicket(), prenotazione, false);
							break;
						case FRUITA:
							prenotazione.setStato(StatoPrenotazionePosto.CONCLUSA);
							prenotazione = sale.get().aggiornaPrenotazionePosto(richiesta.getTicket(), prenotazione, true);
							break;
						default:
							log.warn("stato non previsto: " + prenotazione.getStato());
							continue;
						}

						//tutto ok
						DaoManager.commit(tx);
						updated++;

						writeReportRow(w, prenotazione, read, true, prenotazione.getStato().name().toLowerCase() );

					} catch (BatchInterruptedException e) {
						throw new ApplicationException(SbnErrorTypes.BATCH_MANUAL_STOP);

					} catch (Exception e) {
						errors++;
						DaoManager.rollback(tx);
						prenotazione.setId_prenotazione(id);
						_log.error("errore lettura identificativo: " + prenotazione);
						writeReportRow(w, prenotazione, read, false, "errore lettura identificativo");
						continue;
					}
				}

				_log.debug("righe lette:          " + read);
				_log.debug("righe aggiornate:     " + updated);
				_log.debug("righe non aggiornate: " + errors);

				writeReportFooter(w, read, updated, errors);

				output.setStato(ConstantsJMS.STATO_OK);

			} catch (SbnBaseException e) {
				DaoManager.rollback(tx);
				throw e;
			} catch (IOException e) {
				DaoManager.rollback(tx);
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
			} catch (Exception e) {
				DaoManager.rollback(tx);
				throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
			}

		} finally {
			try {
				DaoManager.commit(tx);
			} catch (Exception e) {}

			FileUtil.flush(w);
			FileUtil.close(w);
		}

		return output;

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
		w.append("<h4>Data:&nbsp;").append(DateUtil.formattaData(DaoManager.now())).append("</h4>");
		w.append("<hr/><br/>");

		w.append("<table width=\"90%\" border=\"1\">");

		w.append("<tr>");
		w.append("<th>n.ro</th>");
		w.append("<th>SALA</th>");
		w.append("<th>POSTO</th>");
		w.append("<th>UTENTE</th>");
		w.append("<th>INIZIO</th>");
		w.append("<th>FINE</th>");
		w.append("<th>Esito</th>");
		w.append("<th>Messaggio</th>");
		w.append("</tr>");
	}

	protected static final String HTML_NBSP = "&nbsp;";

	protected void writeReportRow(Writer w, PrenotazionePostoVO pp, int idx, boolean esito, String msg) throws IOException {
		w.append("<tr>");
		w.append("<td>").append(String.valueOf(pp.getId_prenotazione())).append("</td>");
		w.append("<td>").append(pp.getPosto().getSala().getDescrizione()).append("</td>");
		w.append("<td>").append(String.valueOf(pp.getPosto().getNum_posto())).append("</td>");
		w.append("<td>").append(pp.getUtente().getCognomeNome()).append("</td>");
		w.append("<td>").append(_formatter.format(pp.getTs_inizio())).append("</td>");
		w.append("<td>").append(_formatter.format(pp.getTs_fine())).append("</td>");
		w.append("<td>").append(esito ? "OK" : "ERROR").append("</td>");
		w.append("<td>").append(coalesce(msg, HTML_NBSP)).append("</td>");
	}

	protected void writeReportFooter(Writer w, int read, int updated, int errors) throws IOException {
		w.append("</table>");
		w.append("<br/>");
		w.append("<h4>relazioni lette:&nbsp;").append(String.valueOf(read)).append("</h4>");
		w.append("<h4>relazioni aggiornate:&nbsp;").append(String.valueOf(updated)).append("</h4>");
		w.append("<h4>relazioni non aggiornate:&nbsp;").append(String.valueOf(errors)).append("</h4>");
		w.append("</body>");
		w.append("</html>");
	}


}
