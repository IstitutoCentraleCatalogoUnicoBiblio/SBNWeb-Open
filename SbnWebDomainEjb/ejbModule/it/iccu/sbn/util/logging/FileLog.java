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
package it.iccu.sbn.util.logging;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;

import java.util.Collection;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;


public final class FileLog {

	private Logger _log;
	private FileAppender _appender;
	private final String logName;
	private final String filename;

	public FileLog(String logName, String fileName, boolean append) {
		super();
		this.logName = logName;
		filename = fileName;

		this._log = configureLogger(append);

	}

	private Logger configureLogger(boolean append) {
		Logger log = Logger.getLogger(logName);

		_appender = new FileAppender();
		_appender.setName(logName);
		_appender.setLayout(new PatternLayout("%d %-5p - [%c{3}] %m%n"));
		_appender.setAppend(append);
		_appender.setFile(filename);
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

	public void logWriteException(Exception ex) {
		_log.error(" ", ex);
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
