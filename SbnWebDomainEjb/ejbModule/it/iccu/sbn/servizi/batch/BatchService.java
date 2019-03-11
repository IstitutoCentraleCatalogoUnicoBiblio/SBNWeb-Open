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
package it.iccu.sbn.servizi.batch;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

public abstract class BatchService {

	public static final String STATO_EXEC = "EXEC";
	public static final String STATO_OK = "OK";
	public static final String STATO_ERROR = "ERROR";

	private final UserTransaction tx;

	private ParametriRichiestaElaborazioneDifferitaVO parametri;
	private ElaborazioniDifferiteOutputVo result;
	private String filename;
	private String path;
	private FileWriter logFile = null;

	public BatchService(UserTransaction tx) {
		super();
		this.tx = tx;
	}

	public final ElaborazioniDifferiteOutputVo execute(ParametriRichiestaElaborazioneDifferitaVO parametri) {
		try {

			this.parametri = parametri;
			this.result = new ElaborazioniDifferiteOutputVo(parametri);
			this.result.setStato(STATO_EXEC);

			path = StampeUtil.getBatchFilesPath() + File.separator;
			filename = parametri.getFirmaBatch() +".htm";
			this.result.addDownload(filename, path + filename);

			if (exec(parametri, result) ) {
				commitTx();
				exitStatusOK();
			} else {
				rollbackTx();
				exitStatusERROR();
			}


		} catch (Exception e) {
			e.printStackTrace();
			logWriteLine(e.toString());
			rollbackTx();
			exitStatusERROR();

		} finally {
			closeLogFile();
			commitTx();
		}

		return result;
	}

	protected void exitStatusOK() {
		this.result.setStato(STATO_OK);
	}

	protected void exitStatusERROR() {
		this.result.setStato(STATO_ERROR);
	}


	private void closeLogFile() {
		if (logFile != null)
			try {
				logFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	protected void logWriteLine(String line) {
   		FileWriter out;
		try {
			out = getLogFile();
	   		out.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void logWriteException(Exception ex) {
   		FileWriter out;
		try {
			out = getLogFile();
			PrintWriter stack = new PrintWriter(out);
			ex.printStackTrace(stack);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FileWriter getLogFile() throws IOException {
		if (logFile != null)
			return logFile;

		File f = new File(path + filename);
		logFile = new FileWriter(f);
		return logFile;
	}

	public ParametriRichiestaElaborazioneDifferitaVO getParametri() {
		return parametri;
	}

	protected final void beginTx() {
		try {
			if (tx != null && tx.getStatus() == Status.STATUS_NO_TRANSACTION)
				tx.begin();

		} catch (Exception e) {
			e.printStackTrace();
			logWriteException(e);
		}
	}

	protected final void commitTx() {
		try {
			if (tx != null && tx.getStatus() == Status.STATUS_ACTIVE)
				tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			logWriteException(e);
		}
	}

	protected final void rollbackTx() {
		try {
			if (tx != null && tx.getStatus() == Status.STATUS_ACTIVE)
				tx.rollback();

		} catch (Exception e) {
			e.printStackTrace();
			logWriteException(e);
		}
	}

	protected abstract boolean exec(ParametriRichiestaElaborazioneDifferitaVO params,
			ElaborazioniDifferiteOutputVo result) throws ApplicationException;

}
