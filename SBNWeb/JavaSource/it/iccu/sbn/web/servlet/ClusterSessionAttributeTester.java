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
package it.iccu.sbn.web.servlet;

import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.logging.FileLog;

import java.io.File;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.log4j.Logger;

/**
 * ClusterSessionAttributeTester.java
 * 24/ago/2008
 * @author almaviva
 */
public class ClusterSessionAttributeTester implements
		HttpSessionAttributeListener {

	private final Logger log;// = Logger.getLogger(ClusterSessionAttributeTester.class);

	public ClusterSessionAttributeTester() {
		String name = this.getClass().getSimpleName();
		String fileName = System.getProperty("user.home") + File.separator + name + ".log";
		FileLog fl = new FileLog(name, fileName, true);
		log = fl.getLogger();
	}

	public void attributeAdded(HttpSessionBindingEvent event) {
		Object value = event.getValue();
		if (!testSerialization(value))
			log.error("ATTENZIONE: Attributo '" + event.getName()
					+ "' di tipo '" + value.getClass().getCanonicalName()
					+ "' non serializzabile!");

	}

	private boolean testSerialization(Object value) {
		try {
			Object clone = ClonePool.deepCopy(value);
			return (clone != null);
		} catch (Exception e) {
			return false;
		}

	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		return;
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		attributeAdded(event);
	}

}
