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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.allineamenti;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnHttpIndice;
import it.iccu.sbn.SbnMarcFactory.factory.ServerHttp;
import it.iccu.sbn.SbnMarcFactory.util.BuildSbnMarc;
import it.iccu.sbn.SbnMarcFactory.util.ParametriHttp;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.LocalizzazioneMassivaVO;
import it.iccu.sbn.persistence.dao.bibliografica.LocalizzaDAO;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_loc_indice;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;

public class LocalizzazioneMassiva {

	private static final String HTML_NBSP = "&nbsp;";
	private static final int RICHIESTA_INDICE_WAIT = 30 * 1000;	//30 sec.

	private final UserTransaction tx;
	private final LocalizzazioneMassivaVO richiesta;
	private final String firmaUtente;
	private final Logger log;

	private int wait_threshold;

	private int read = 0;
	private int updated = 0;
	private int errors = 0;

	private enum TipoInput {
		DB,
		FILE;
	}

	private AmministrazionePolo getAmministrazionePolo() {
		try {
			return DomainEJBFactory.getInstance().getPolo();

		} catch (CreateException e) {
			log.error("", e);
			throw new EJBException(e);
		} catch (NamingException e) {
			log.error("", e);
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}

	}

	private FactorySbn getFactoryIndice() throws RemoteException,
			DaoManagerException, SbnMarcException {
		ParametriHttp paramIndice = getAmministrazionePolo().getIndice();

		FactorySbn indice = new FactorySbnHttpIndice("INDICE", new ServerHttp(
				paramIndice, getAmministrazionePolo().getCredentials()));
		return indice;
	}

	public LocalizzazioneMassiva(UserTransaction tx, LocalizzazioneMassivaVO richiesta, BatchLogWriter blw) {
		this.tx = tx;
		this.richiesta = richiesta;
		this.log = blw.getLogger();
		this.firmaUtente = DaoManager.getFirmaUtente(richiesta.getTicket());
	}

	public ElaborazioniDifferiteOutputVo execute() throws ApplicationException, ValidationException {
		richiesta.validate();
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);

		BufferedReader r = null;
		BufferedWriter w = null;
		boolean success = false;

		try {
			try {
				this.wait_threshold = CommonConfiguration.getPropertyAsInteger(Configuration.LOC_MASSIVA_INDICE_WAIT_TIMEOUT,
						RICHIESTA_INDICE_WAIT);
				log.debug(String.format("Tempo di attesa per richiesta indice: %dms", wait_threshold));

				output.setStato(ConstantsJMS.STATO_ERROR);

				String tipoInput = richiesta.getTipoInput();
				log.debug("Tipo input: " + tipoInput);
				TipoInput type = TipoInput.valueOf(tipoInput);

				//setup report file
				String fileName = richiesta.getFirmaBatch() + ".htm";
				FileOutputStream out = new FileOutputStream(StampeUtil.getBatchFilesPath() + File.separator + fileName);
				w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				output.addDownload(fileName, fileName);
				writeReportHeader(w);

				switch (type) {
				case DB:
					localizzaFromDB(w);
					break;

				case FILE:
					String inputFile = richiesta.getInputFile();
					if (!FileUtil.exists(inputFile))
						throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

					File f = new File(inputFile);
					try {
						BatchManager.getBatchManagerInstance().markForDeletion(f);
					} catch (Exception e) {
						throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
					}

					//almaviva5_20140826 #5629
					String charset = FileUtil.guessCharset(f);
					InputStreamReader isr = new InputStreamReader(new FileInputStream(f), charset);
					r = new BufferedReader(isr);
					localizzaFromFile(r, w, charset);
					break;
				}

				DaoManager.commit(tx);
				output.setStato(ConstantsJMS.STATO_OK);
				success = true;

			} catch (ApplicationException e) {
				log.error("", e);
				throw e;

			} catch (FileNotFoundException e) {
				log.error("", e);
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);

			} catch (IOException e) {
				log.error("", e);
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);

			} catch (DaoManagerException e) {
				log.error("", e);

				throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
			} catch (Exception e) {
				log.error("", e);
			}

		} finally {
			try {
				writeReportFooter(w, read, updated, errors);
			} catch (Exception e) {}

			DaoManager.endTransaction(tx, success);
			FileUtil.close(r);
			FileUtil.flush(w);
			FileUtil.close(w);
		}

		return output;

	}

	private void localizzaFromFile(BufferedReader r, Writer w, String charset) throws Exception {
		FactorySbn indice = getFactoryIndice();
		LocalizzaDAO dao = new LocalizzaDAO();
		TitoloDAO tdao = new TitoloDAO();
		String idBatch = richiesta.getIdBatch();
		String bid = null;
		SbnUserType sbnUser = null;
		int tp_loc = -1;
		DaoManager.begin(tx);

		String line;
		while ((line = r.readLine()) != null)
			try {
				if ( (++read % 100) == 0)
					BatchManager.getBatchManagerInstance().checkForInterruption(idBatch);

				String xml = new String(ValidazioneDati.trimOrEmpty(line).getBytes(), charset);
				if (!ValidazioneDati.isFilled(xml)) {
					++errors;
					writeReportRow(w, null, xml, -1, read, false, "riga vuota.");
					continue;
				}

				SBNMarc request = BuildSbnMarc.unmarshal(xml);
				if (!isLocalizza(request)) {
					++errors;
					writeReportRow(w, null, xml, -1, read, false, StringEscapeUtils.escapeHtml("Tag <Localizza> non trovato in questo xml.") );
					continue;
				}

				Tb_loc_indice loc = localizzazioneFromSbnMarc(request, xml);
				bid = loc.getTitolo().getBid();
				tp_loc = loc.getTp_loc();
				sbnUser = request.getSbnUser();

				DaoManager.begin(tx);
				//almaviva5_20140826 #5629
				if (!tdao.esisteTitolo(bid) )  {
					++errors;
					log.error(String.format("Titolo non trovato in polo per BID %s.", bid));
					writeReportRow(w, sbnUser, bid, tp_loc, read, false, "Titolo non trovato in polo");
					continue;
				}
			/*
				else {
					dao.save(loc);
					DaoManager.commit(tx);
				}

				DaoManager.begin(tx);
			*/

				indice.setMessage(request.getSbnMessage(), sbnUser);
				SBNMarc sbnMarcResult = indice.eseguiRichiestaServer();
				if (sbnMarcResult == null) {
					log.error(String.format("Errore richiesta per BID %s: nessuna risposta.", bid));
					writeReportRow(w, sbnUser, bid, tp_loc, read, false, "nessuna risposta");
					continue;
				}

				SbnResultType result = sbnMarcResult.getSbnMessage().getSbnResponse().getSbnResult();
				SbnMarcEsitoType esito = SbnMarcEsitoType.of(result.getEsito());
				switch (esito) {
				case OK:
				case LOCALIZZA_CORREZIONE:
					loc.setFl_stato('1');
					loc.setUte_var(firmaUtente);
					dao.save(loc);
					DaoManager.commit(tx);
					++updated;
					writeReportRow(w, sbnUser, bid, tp_loc, read, true, "localizzazione aggiornata");
					break;
				default:
					log.error(String.format("Errore richiesta per BID %s: esito: %s: %s.",
									bid, result.getEsito(),
									result.getTestoEsito()));
					++errors;
					writeReportRow(w, sbnUser, bid, tp_loc, read, false, result.getEsito() + ": "
							+ StringEscapeUtils.escapeHtml(result.getTestoEsito()));
					continue;
				}

				await();

			} catch (MarshalException e) {
				DaoManager.rollback(tx);
				++errors;
				writeReportRow(w, null, null, -1, read, false, "errore formato xml");
			} catch (org.exolab.castor.xml.ValidationException e) {
				DaoManager.rollback(tx);
				++errors;
				writeReportRow(w, null, null, -1, read, false, "errore validazione xml");
			} catch (SbnMarcException e) {
				DaoManager.rollback(tx);
				++errors;
				writeReportRow(w, sbnUser, bid, tp_loc, read, false, StringEscapeUtils.escapeHtml(e.getMessage()) );
			} catch (DaoManagerException e) {
				DaoManager.rollback(tx);
				++errors;
				writeReportRow(w, sbnUser, bid, tp_loc, read, false, "Errore db: " + StringEscapeUtils.escapeHtml(e.getMessage()) );
			}

	}

	private Tb_loc_indice localizzazioneFromSbnMarc(SBNMarc request, String xml) {
		SbnMessageType sbnMessage = request.getSbnMessage();
		SbnRequestType sbnRequest = sbnMessage.getSbnRequest();
		LocalizzaType localizza = sbnRequest.getLocalizza();
		LocalizzaInfoType locInfo = localizza.getLocalizzaInfo(0);

		Tb_loc_indice loc = new Tb_loc_indice();
		Tb_titolo t = new Tb_titolo();
		t.setBid(locInfo.getSbnIDLoc());
		loc.setTitolo(t);
		loc.setFl_stato('0');
		loc.setTp_loc((short) locInfo.getTipoInfo().getType());
		loc.setSbnmarc_xml(xml);
		loc.setUte_ins(firmaUtente);
		loc.setUte_var(firmaUtente);
		loc.setTs_ins(DaoManager.now());
		loc.setFl_canc('N');

		//almaviva5_20140826 #5629
		String bib = request.getSbnUser().getBiblioteca();
		loc.setBiblioteca(DaoManager.creaIdBib(bib.substring(0, 3), bib.substring(3)));

		return loc;
	}

	private static boolean isLocalizza(SBNMarc sbnMarc) {
		try {
			SbnMessageType sbnMessage = sbnMarc.getSbnMessage();
			SbnRequestType sbnRequest = sbnMessage.getSbnRequest();
			LocalizzaType localizza = sbnRequest.getLocalizza();
			if (localizza == null)
				return false;

			return (localizza.getLocalizzaInfoCount() > 0);

		} catch (Exception e) {	}

		return false;
	}

	private void localizzaFromDB(Writer w) throws Exception {
		FactorySbn indice = getFactoryIndice();
		LocalizzaDAO dao = new LocalizzaDAO();
		String idBatch = richiesta.getIdBatch();
		String bid = null;
		SbnUserType sbnUser = null;
		int tp_loc = -1;
		DaoManager.begin(tx);

		List<Number> locPendenti = dao.getLocalizzazioniPendentiIDs();
		log.debug("localizzazioni pendenti trovate: " + ValidazioneDati.size(locPendenti));
		for (Number locId : locPendenti)
			try {
				if ( (++read % 100) == 0) {
					BatchManager.getBatchManagerInstance().checkForInterruption(idBatch);
					log.debug("localizzazioni trattate: " + read);
				}

				DaoManager.begin(tx);
				Tb_loc_indice loc = dao.getLocalizzazionePendenteById(locId.intValue());
				if (loc == null) {
					log.error(String.format("localizzazione non trovata."));
					writeReportRow(w, null, null, -1, read, false, "localizzazione non trovata");
					continue;
				}

				bid = loc.getTitolo().getBid();
				tp_loc = loc.getTp_loc();

				String xml = loc.getSbnmarc_xml();
				SBNMarc request = BuildSbnMarc.unmarshal(xml);
				sbnUser = request.getSbnUser();

				indice.setMessage(request.getSbnMessage(), request.getSbnUser());
				SBNMarc sbnMarcResult = indice.eseguiRichiestaServer();
				if (sbnMarcResult == null) {
					log.error(String.format("Errore richiesta per BID %s: nessuna risposta.", bid));
					writeReportRow(w, sbnUser, bid, tp_loc, read, false, "nessuna risposta");
					continue;
				}

				SbnResultType result = sbnMarcResult.getSbnMessage().getSbnResponse().getSbnResult();
				SbnMarcEsitoType esito = SbnMarcEsitoType.of(result.getEsito());
				switch (esito) {
				case OK:
				case LOCALIZZA_CORREZIONE:
					loc.setFl_stato('1');
					loc.setUte_var(firmaUtente);
					dao.save(loc);
					DaoManager.commit(tx);
					++updated;
					writeReportRow(w, sbnUser, bid, tp_loc, read, true, "localizzazione aggiornata");
					break;
				default:
					log.error(String.format("Errore richiesta per BID %s: esito: %s: %s.",
									bid, result.getEsito(),
									result.getTestoEsito()));
					++errors;
					writeReportRow(w, sbnUser, bid, tp_loc, read, false, result.getEsito() + ": "
							+ StringEscapeUtils.escapeHtml(result.getTestoEsito()));
					continue;
				}

				await();

			} catch (MarshalException e) {
				DaoManager.rollback(tx);
				++errors;
				writeReportRow(w, sbnUser, bid, tp_loc, read, false, "errore formato xml");
			} catch (org.exolab.castor.xml.ValidationException e) {
				DaoManager.rollback(tx);
				++errors;
				writeReportRow(w, sbnUser, bid, tp_loc, read, false, "errore validazione xml");
			} catch (SbnMarcException e) {
				DaoManager.rollback(tx);
				++errors;
				writeReportRow(w, sbnUser, bid, tp_loc, read, false, StringEscapeUtils.escapeHtml(e.getMessage()) );
			} catch (DaoManagerException e) {
				DaoManager.rollback(tx);
				++errors;
				writeReportRow(w, sbnUser, bid, tp_loc, read, false, "Errore db: " + StringEscapeUtils.escapeHtml(e.getMessage()) );
			}

	}

	protected void writeReportHeader(Writer w) throws IOException {
		w.append("<!DOCTYPE html>");
		w.append("<html>");
		w.append("<head>");
		w.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		w.append("<title>Localizzazione massiva</title>");
		w.append("</head>");
		w.append("<body>");

		w.append("<h2>Localizzazione massiva</h2>");
		w.append("<h4>Data:&nbsp;").append(DateUtil.formattaData(System.currentTimeMillis())).append("</h4>");
		w.append("<hr/><br/>");

		w.append("<table width=\"90%\" border=\"1\">");

		w.append("<tr>");
		w.append("<th>n.ro</th>");
		w.append("<th>Biblioteca</th>");
		w.append("<th>BID</th>");
		w.append("<th>Tipo Loc.</th>");
		w.append("<th>Esito</th>");
		w.append("<th>Messaggio</th>");
		w.append("</tr>");
	}

	protected void writeReportRow(Writer w, SbnUserType user, String bid, int type, int idx, boolean esito, String msg) throws IOException {
		w.append("<tr>");
		w.append("<td>").append(String.valueOf(idx)).append("</td>");
		w.append("<td>").append(user != null ? coalesce(user.getBiblioteca(), HTML_NBSP) : HTML_NBSP).append("</td>");
		w.append("<td>").append(coalesce(bid, HTML_NBSP) ).append("</td>");

		String tipoLoc = null;
		switch (type) {
		case SbnTipoLocalizza.GESTIONE_TYPE:
			tipoLoc = SbnTipoLocalizza.GESTIONE.toString();
			break;

		case SbnTipoLocalizza.POSSESSO_TYPE:
			tipoLoc = SbnTipoLocalizza.POSSESSO.toString();
			break;

		case SbnTipoLocalizza.TUTTI_TYPE:
			tipoLoc = SbnTipoLocalizza.TUTTI.toString();
			break;
		}
		w.append("<td>").append(coalesce(tipoLoc, HTML_NBSP)).append("</td>");

		w.append("<td>").append(esito ? "OK" : "ERROR").append("</td>");
		w.append("<td>").append(coalesce(msg, HTML_NBSP)).append("</td>");

		w.flush();
	}

	protected void writeReportFooter(Writer w, int read, int updated, int errors) throws IOException {
		w.append("</table>");
		w.append("<br/>");
		w.append("<h4>Localizzazioni lette:&nbsp;").append(String.valueOf(read)).append("</h4>");
		w.append("<h4>Localizzazioni aggiornate:&nbsp;").append(String.valueOf(updated)).append("</h4>");
		w.append("<h4>Localizzazioni non aggiornate:&nbsp;").append(String.valueOf(errors)).append("</h4>");
		w.append("</body>");
		w.append("</html>");
	}

	private void await() throws Exception {
		final long now = System.currentTimeMillis();
		BatchManager.getBatchManagerInstance().checkForInterruption(richiesta.getIdBatch());

		while ( (System.currentTimeMillis() - now) < wait_threshold)
			Thread.sleep(500);
	}

	public static final boolean salvaLocalizzazione(String codPolo,
			String codBib, SbnMessageType sbnmessage, SbnUserType user) {
		LocalizzaDAO dao = new LocalizzaDAO();
		BuildSbnMarc build = new BuildSbnMarc();
		try {
			String firmaUtente = user.getBiblioteca() + user.getUserId();

			SBNMarc sbnmarc = build.creaIntestazione(user);
			sbnmarc.setSbnMessage(sbnmessage);
			SBNMarc xml_copy = ClonePool.deepCopy(sbnmarc);
			LocalizzaType localizza_copy = xml_copy.getSbnMessage().getSbnRequest().getLocalizza();

			LocalizzaType localizza = ClonePool.deepCopy(sbnmarc.getSbnMessage().getSbnRequest().getLocalizza());
			for (LocalizzaInfoType lit : localizza.getLocalizzaInfo() )	{
				String bid = lit.getSbnIDLoc();
				SbnTipoLocalizza sbnTipoLoc = lit.getTipoInfo();
				//l'xml con localizzazioni multiple viene spezzato in xml singoli
				localizza_copy.setLocalizzaInfo(new LocalizzaInfoType[] { lit });

				switch (sbnTipoLoc.getType()) {
				case SbnTipoLocalizza.POSSESSO_TYPE:
					//almaviva5_20140304 evolutive google3
					// in caso di richiesta per POSSESSO si controlla l'esistenza di una riga per polo+bib+bid e tipoLoc=GESTIONE,
					// se viene trovata allora la nuova richiesta viene accorpata in una riga con tipoLoc=TUTTI.
					Tb_loc_indice loc = dao.getLocalizzazione(codPolo, codBib, bid, (short) SbnTipoLocalizza.GESTIONE_TYPE);
					if (loc != null) {
						//richiesta per tipoLoc=GESTIONE trovata
						lit.setTipoInfo(SbnTipoLocalizza.TUTTI);
						loc.setTp_loc((short) SbnTipoLocalizza.TUTTI.getType());
						loc.setUte_var(firmaUtente);
						loc.setSbnmarc_xml(build.marshalSbnMarc(xml_copy));
						dao.save(loc);
					} else
						dao.salvaLocalizzazione(codPolo, codBib, bid, (short) sbnTipoLoc.getType(),
								build.marshalSbnMarc(xml_copy), firmaUtente);
					break;

				case SbnTipoLocalizza.GESTIONE_TYPE:
				case SbnTipoLocalizza.TUTTI_TYPE:
					dao.salvaLocalizzazione(codPolo, codBib, bid, (short) sbnTipoLoc.getType(),
							build.marshalSbnMarc(sbnmarc), firmaUtente);
					break;
				}

			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void rimuoviLocalizzazione(String codPolo,
			String codBib, SbnMessageType sbnmessage, SbnUserType user) {
		LocalizzaDAO dao = new LocalizzaDAO();

		try {
			String firmaUtente = user.getBiblioteca() + user.getUserId();
			LocalizzaType localizza = sbnmessage.getSbnRequest().getLocalizza();
			for (LocalizzaInfoType lit : localizza.getLocalizzaInfo() )	{
				String bid = lit.getSbnIDLoc();
				SbnTipoLocalizza sbnTipoLoc = lit.getTipoInfo();
				Tb_loc_indice loc = dao.getLocalizzazione(codPolo, codBib, bid, (short) sbnTipoLoc.getType());
				if (loc != null) {
					loc.setFl_canc('S');
					loc.setUte_var(firmaUtente);
					dao.save(loc);
				}
			}

		} catch (Exception e) {

		}

	}

}
