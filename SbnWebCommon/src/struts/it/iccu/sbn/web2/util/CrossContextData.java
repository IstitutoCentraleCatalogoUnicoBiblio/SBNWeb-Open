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
package it.iccu.sbn.web2.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

@SuppressWarnings("unchecked")
public class CrossContextData {

	public static final void set(ServletContext context,
			String sessionid, String key, Object data) {

		Map<String, Object> sessionData = (Map<String, Object>) context.getAttribute(sessionid);
		if (sessionData == null) {
			sessionData = new ConcurrentHashMap<String, Object>();
			context.setAttribute(sessionid, sessionData);
		}

		sessionData.put(key, data);
	}

	public static final Object get(ServletContext context, String sessionid, String key) {

		Map<String, Object> sessionData = (Map<String, Object>) context.getAttribute(sessionid);
		if (sessionData == null)
			return null;

		return sessionData.get(key);
	}

}
