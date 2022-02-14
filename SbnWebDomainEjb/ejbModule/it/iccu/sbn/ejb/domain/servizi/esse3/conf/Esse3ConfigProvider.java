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
package it.iccu.sbn.ejb.domain.servizi.esse3.conf;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class Esse3ConfigProvider {

	private static Logger log = Logger.getLogger(Esse3ConfigProvider.class);

	private static final Type JSON_TYPE = new TypeToken<List<Esse3BibConfigEntry>>() {}.getType();
	public static final Esse3BibConfigEntry EMPTY = new Esse3BibConfigEntry();

	private static final Esse3ConfigProvider INSTANCE = new Esse3ConfigProvider();

	private List<Esse3BibConfigEntry> configs = new ArrayList<Esse3BibConfigEntry>();

	private Esse3ConfigProvider() {
		try {
			String apiKeyFile = CommonConfiguration.getProperty(Configuration.ESSE3_BIB_APIKEY_FILE);
			if (isFilled(apiKeyFile) && FileUtil.exists(apiKeyFile)) {
				Gson gson = new Gson();
				configs = gson.fromJson(new FileReader(apiKeyFile), JSON_TYPE);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		log.debug("configurazioni ESSE3 caricate: " + configs.size());
	}

	/**
	 * Cerca per id
	 * @param id
	 * @return
	 */
	public static Esse3BibConfigEntry fromId(String id) {
		for (Esse3BibConfigEntry conf : INSTANCE.configs) {
			if (ValidazioneDati.equals(conf.getId(), id))
				return conf;
		}
		return EMPTY;
	}

	/**
	 * Cerca per apikey
	 * @param pass
	 * @return
	 */
	public static Esse3BibConfigEntry fromApikey(String pass) {
		log.debug("Esse3ConfigProvider.fromApikey(): pass=" + pass);
		for (Esse3BibConfigEntry conf : INSTANCE.configs) {
			if (ValidazioneDati.equals(conf.getApikey(), pass))
				return conf;
		}
		return EMPTY;
	}

	/**
	 * Cerca per cod Bib
	 * @param cdBib
	 * @return
	 */
	public static Esse3BibConfigEntry fromCdBib(String cdBib) {
		log.debug("Esse3ConfigProvider.fromCdBib(): bib=" + cdBib);
		for (Esse3BibConfigEntry conf : INSTANCE.configs) {
			if (ValidazioneDati.equals(conf.getCd_bib(), cdBib))
				return conf;
		}
		return EMPTY;
	}

	public static boolean empty() {
		return INSTANCE.configs.isEmpty();
	}

}
