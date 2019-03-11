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
package it.iccu.sbn.servizi.biblioteca.importer;

import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.vo.custom.amministrazione.biblioteca.json.Biblioteche;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class JSONAnagrafeBinderImpl implements JSONAnagrafeBinder {

	private static Logger log = Logger.getLogger(JSONAnagrafeBinder.class);

	private static final Biblioteche EMPTY = new Biblioteche();

	private static final String DEFAULT_JSON = FileUtil.getUserHomeDir() + File.separator + "biblioteche.json";

	private Gson gson = new Gson();

	private Reader createReader() throws Exception {
		try {
			String fileName = CommonConfiguration.getProperty(Configuration.JSON_ANAGRAFE_BIBLIOTECHE_PATH,	DEFAULT_JSON);
			log.debug("Opening anagrafe json file: " + fileName);
			return new FileReader(fileName);
		} catch (FileNotFoundException e) {
			log.error("Json anagrafe non trovato: " + e.getLocalizedMessage());
			return null;
		}
	}

	public Biblioteche bind() {
		Reader reader = null;
		try {
			reader = createReader();
			if (reader != null)
				return gson.fromJson(reader, Biblioteche.class);

		} catch (Exception e) {
			log.error("", e);
		} finally {
			FileUtil.close(reader);
		}

		return EMPTY;	//vuoto
	}

}
