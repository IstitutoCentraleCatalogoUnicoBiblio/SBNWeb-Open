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
package it.iccu.sbn.web.actions.servizi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import org.apache.log4j.Logger;

public class CodiceFiscaleComuni {

	private static Logger log = Logger.getLogger(CodiceFiscaleComuni.class);

	private Map<String, String> comuni;
	private int count = 0;

	private void caricaXMLComuni() {

		String path = "/it/iccu/sbn/web/xml/CodiceFiscaleComuni.xml";/*"/" + this.getClass().getCanonicalName();
		path = path.replace('.', '/') +  ".xml";*/
		Digester d = new Digester();
		InputStream input = this.getClass().getResourceAsStream(path);
		d.setValidating(false);
		d.push(this);
		d.addCallMethod("comuni/comune", "addComune", 2);
		d.addCallParam("comuni/comune", 0, "city");
		d.addCallParam("comuni/comune", 1, "code");

		try {
			d.parse(input);
		} catch (IOException e) {
			log.error("", e);
		} catch (SAXException e) {
			log.error("", e);
		}

	}

	public void addComune(String key, String value) {
		comuni.put(key.trim().toUpperCase(), value.trim().toUpperCase());
		count++;
	}

	public String getComune(String key) {
		return comuni.get(key.trim().toUpperCase());
	}


	public CodiceFiscaleComuni() {
		this.comuni = new HashMap<String, String>();
		this.caricaXMLComuni();
	}


	public int getCount() {
		return count;
	}

}
