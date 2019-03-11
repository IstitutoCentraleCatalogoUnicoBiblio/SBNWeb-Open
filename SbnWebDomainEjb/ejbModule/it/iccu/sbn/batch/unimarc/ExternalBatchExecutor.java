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
package it.iccu.sbn.batch.unimarc;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class ExternalBatchExecutor implements BatchExecutor {

	protected static class AsyncStreamCapture extends Thread {
		final InputStream is;
		final Level type;
		final OutputStream os;
		final AtomicBoolean failed = new AtomicBoolean(false);
		final Logger _log;

		private static final Pattern FAIL_EXCEPTION_REGEX = Pattern.compile("(?i).*exception.*");

		public AsyncStreamCapture(InputStream is, Level type, BatchLogWriter log) {
			this(is, type, null, log);
		}

		public AsyncStreamCapture(InputStream is, Level type, OutputStream redirect, BatchLogWriter log) {
			this.is = is;
			this.type = type;
			this.os = redirect;
			this._log = log.getLogger();
			this.setName(type.toString() + "-" + this.getId());
		}

		public void run() {
			try {
				PrintWriter pw = null;
				if (os != null)
					pw = new PrintWriter(os);

				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;

				while ((line = br.readLine()) != null) {
					if (pw != null)
						pw.println(line);
					_log.log(type, line);
					if (!failed.get())
						failed.set(FAIL_EXCEPTION_REGEX.matcher(line).matches());
				}

				if (pw != null)
					pw.flush();

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		public boolean isFailed() {
			return failed.get();
		}
	}

	protected static final String getDataPrepHome() throws Exception {
		return CommonConfiguration.getProperty(Configuration.SBNWEB_EXPORT_UNIMARC_HOME, "/export/exportUnimarc/dp");
	}

	protected static final String getExportHome() throws Exception {
		return getDataPrepHome() + File.separator + "export";
	}

	public static final String getImportaUtentiHome() throws Exception {
		return CommonConfiguration.getProperty(Configuration.SBNWEB_IMPORTA_UTENTI_HOME, "/db/migrazione/importaUtenti");	//server
	}

	//almaviva5_20121001 #5126
	protected static final boolean fileExists(String path, String name) throws Exception {
		return (new File(ValidazioneDati.coalesce(path, ".") + File.separator + name)).exists();
	}

	protected final int exec(BatchLogWriter log, String homePath, String pname, String... args)	throws Exception {

		List<String> params = new ArrayList<String>();
		params.add(pname);
		if (ValidazioneDati.isFilled(args))
			params.addAll(Arrays.asList(args));

		ProcessBuilder pb = new ProcessBuilder(params);
		pb.directory(new File(homePath));

		Process p = pb.start();
		AsyncStreamCapture debug = new AsyncStreamCapture(p.getInputStream(), Level.DEBUG, log);
		debug.start();

		AsyncStreamCapture error = new AsyncStreamCapture(p.getErrorStream(), Level.ERROR, log);
		error.start();

		int exit = p.waitFor();
		if (exit == 0 && (debug.isFailed() || error.isFailed()))
			exit = -1;

		log.getLogger().debug(pname + " exit code: " + exit);

		return exit;
	}

	protected int checkFile(BatchLogWriter log, long startProcess, String name)	throws Exception {
		File f = new File(name);
		if (!f.exists())
			throw new Exception("File '" + name + "' non trovato");

		if (f.lastModified() < startProcess)
			throw new Exception("File '" + name + "' non modificato");

		log.getLogger().debug("grep inizio processo");
		int exit = exec(log, getExportHome(), "grep", "Inizio processo", name);
		if (exit != 0)
			return exit;

		log.getLogger().debug("grep fine processo");
		exit = exec(log, getExportHome(), "grep", "Fine processo", name);

		return exit;
	}

}
