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

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class UTF8Decomposer {

	private static Logger log = Logger.getLogger(UTF8Decomposer.class);

	private static final String UNICODE_CANONICAL_DECOMPOSITION = "NFD";
	private static final UTF8Decomposer instance = new UTF8Decomposer();

	public enum DecomposerType {
		UNKNOWN,
		JDK5,
		JDK6
	};

	private DecomposerType type;
	private Method method;
	private Field field;

	public UTF8Decomposer() {
		type = DecomposerType.UNKNOWN;
		init();
		log.debug("DecomposerType: " + type);
		if (type == DecomposerType.UNKNOWN)
			throw new RuntimeException("UTF8Decomposer failed to initialize");
	}

	public final String decompose(final String utf8) {
		String norm = null;
		try {
			switch (type) {
			case JDK5:
				norm = (String) method.invoke(null, utf8, false, 0);
				break;

			// Java6-7
			case JDK6:
				norm = (String) method.invoke(null, utf8, field.get(null));
				break;

			case UNKNOWN:
				throw new RuntimeException("Decomposer is not initialized");
			}

		} catch (Exception e) {
			log.error("", e);
		}

		return norm;
	}

	public final DecomposerType getType() {
		return type;
	}

	private final void init() {
		// Check Java version
		String version = System.getProperty("java.specification.version");

		if (ValidazioneDati.equals(version, "1.5")) {
			try {
				Class<?> clazz = Class.forName("sun.text.Normalizer");

				Class<?>[] paramTypes = new Class<?>[3];
				paramTypes[0] = String.class;
				paramTypes[1] = boolean.class;
				paramTypes[2] = int.class;

				method = clazz.getMethod("decompose", paramTypes);

				if (method != null)
					type = DecomposerType.JDK5;

			} catch (Exception e) {
				log.error("", e);
			}
		}

		if (ValidazioneDati.in(version, "1.6", "1.7", "1.8", "1.9")) {
			try {
				Class<?> clazz = Class.forName("java.text.Normalizer");
				Class<?> formEnum = Class.forName("java.text.Normalizer$Form");

				field = formEnum.getField(UNICODE_CANONICAL_DECOMPOSITION);

				Class<?>[] paramTypes = new Class<?>[2];
				paramTypes[0] = CharSequence.class;
				paramTypes[1] = formEnum;

				method = clazz.getMethod("normalize", paramTypes);

				if (method != null)
					type = DecomposerType.JDK6;

			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	public static final UTF8Decomposer getInstance() {
		return instance;
	}
}
