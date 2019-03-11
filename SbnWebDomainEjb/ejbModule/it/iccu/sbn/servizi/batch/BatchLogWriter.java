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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;


public final class BatchLogWriter implements Serializable {

	private static final long serialVersionUID = -2613835171931889486L;

	private String filename;
	private String path;
	private final ParametriRichiestaElaborazioneDifferitaVO parametri;

	private Logger _log;
	private FileAppender _appender;

	public BatchLogWriter(ParametriRichiestaElaborazioneDifferitaVO parametri) {
		super();
		this.parametri = parametri;
		this.path = StampeUtil.getBatchFilesPath() + File.separator;
		try {
			this.filename = parametri.getFirmaBatch() +".log";
			this._log = configureLogger();
		} catch (ValidationException e) {
			filename = "error_" + System.currentTimeMillis() +".log";
		}
	}

	private Logger configureLogger() throws ValidationException {
		String firmaBatch = parametri.getFirmaBatch();
		Logger log = Logger.getLogger(firmaBatch);

		_appender = new FileAppender();
		_appender.setName(firmaBatch);
		_appender.setLayout(new PatternLayout("%d %-5p - [%c{3}] %m%n"));
		_appender.setAppend(true);
		_appender.setFile(path + filename);
		_appender.setBufferSize(2048);
		_appender.setImmediateFlush(true);
		_appender.setBufferedIO(false);
		_appender.setEncoding("UTF-8");
		_appender.setThreshold(Priority.DEBUG);
		_appender.activateOptions();

		log.addAppender(_appender);
		log.setLevel(Level.DEBUG);

		return log;
	}

	public void closeLogFile() {
		_appender.close();
		_log.removeAppender(_appender);
	}

	public void logWriteLine(String line) {
   		_log.debug(line);
	}

	public void logWriteException(Throwable t) {
		_log.error("", t);
	}

	public boolean exists() {
		return true;
	}

	public DownloadVO getFilename() {
		return new DownloadVO(filename, filename);
	}

	public Logger getLogger() {
		return _log;
	}

	public void logWriteCollection(Collection<?> list) {
		if (!ValidazioneDati.isFilled(list) )
			return;
		for (Object o : list)
			_log.debug(o);
	}

}
