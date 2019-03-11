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
package it.iccu.sbn.ejb.utils.unicode;

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.SbnUnicodeKey;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;


/**
 * SbnUnicodeMapping.java
 * 21/mag/07
 * @author almaviva
 */
public class SbnUnicodeMapping extends SerializableVO {

	private static final long serialVersionUID = -4254874183716098206L;
	private static final Logger log = Logger.getLogger(SbnUnicodeMapping.class);

	private List<SbnUnicodeKey> keys;
	private Map<String, List<SbnUnicodeKey>> categories;
	private Map<Integer, String> codePoints;
	private AtomicBoolean initialized;

	private static SbnUnicodeMapping instance = null;

	public static final synchronized SbnUnicodeMapping getInstance() throws InfrastructureException {
		if (instance == null)
			instance = new SbnUnicodeMapping();

		if (!instance.initialized.get())
			instance.initialize();

		return instance;
	}

	private boolean caricaXML(SbnUnicodeMapping target) {

		try {
			Class<SbnUnicodeMapping> clazz = SbnUnicodeMapping.class;
			String path = "/" + clazz.getCanonicalName().replace('.', '/') + ".xml";
			InputStream input = clazz.getResourceAsStream(path);

			Digester d = new Digester();
			d.setValidating(false);
			d.push(target);
			d.addObjectCreate("sbn/keys/key", SbnUnicodeKey.class);
			d.addSetNext("sbn/keys/key", "addKey");
			d.addSetProperties("sbn/keys/key", "ordkey", "ordKey");
			d.addSetProperties("sbn/keys/key", "name", "descrizione");
			d.addSetProperties("sbn/keys/key", "category", "category");
			d.addBeanPropertySetter("sbn/keys/key", "key");
			d.parse(input);
			return true;

		} catch (IOException e) {
			return false;
		} catch (SAXException e) {
			return false;
		}
	}

	private SbnUnicodeMapping() {
		super();
		this.keys = new ArrayList<SbnUnicodeKey>();
		this.categories = new HashMap<String, List<SbnUnicodeKey>>();
		this.codePoints = new HashMap<Integer, String>();
		this.initialized = new AtomicBoolean(false);
	}

	private void initialize() throws InfrastructureException {

		if (!caricaXML(this))
			throw new InfrastructureException("Errore mapping unicode");

		int pad = String.valueOf(keys.size()).length();
		int count = 0;

		for (SbnUnicodeKey key : keys) {
			this.codePoints.put(key.getUChar(), key.getOrdKey() );
			log.info(String.format("%0" + pad + "d: %s", ++count, key.lock().toString() ));
		}

		this.initialized.set(true);	// init ok
	}

	public void addKey(SbnUnicodeKey key) {

		this.keys.add(key);

		List<SbnUnicodeKey> list = null;
		String category = key.getCategory();
		if (category == null)
			return;

		if (this.categories.containsKey(category))
			list = this.categories.get(category);
		else {
			list = new ArrayList<SbnUnicodeKey>();
			this.categories.put(category, list);
		}

		list.add(key);

	}

	public List<SbnUnicodeKey> getKeys() {
		return Collections.unmodifiableList(keys);
	}

	public Map<String, List<SbnUnicodeKey>> getCategories() {
		return Collections.unmodifiableMap(categories);
	}

	public Map<Integer, String> getCodePoints() {
		return Collections.unmodifiableMap(codePoints);
	}

}
