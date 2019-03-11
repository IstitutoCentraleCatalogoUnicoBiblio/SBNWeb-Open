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
package it.finsiel.sbn.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Expression {

	private static final int BUFFER_SIZE = 2048;

	private Map<String, Object> property = new HashMap<String, Object>();
	private Map<String, String> operator = new HashMap<String, String>();

	private String prefix = "";

	public Expression() {
		super();
	}

	public Expression(String prefix) {
		super();
		this.prefix = prefix;
	}

	public void addProperty(String field, Object value, String operator) {
		this.property.put(field, value);
		this.operator.put(field, operator);
	}

	public Iterator<String> getProperty() {
		return property.keySet().iterator();
	}

	public Object getValue(String key) {
		return this.property.get(key);
	}

	private String value(String key) {
		String ret = "";
		if (prefix.length() == 0)
			ret += key + " " + operator.get(key) + " :" + key;
		else
			ret += key + " " + operator.get(key) + " :" + key + "_" + prefix;
		return ret;
	}

	public String getCondition() {

		StringBuilder condition = new StringBuilder(BUFFER_SIZE);
		condition.append(" ( ");

		Iterator<String> iterator = property.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			condition.append(this.value(key) );

			if (iterator.hasNext())
				condition.append(" AND ");
		}

		condition.append(" ) ");

		return condition.toString();
	}

	public String getPrefix() {
		if (prefix.length() == 0)
			return prefix;
		return "_" + prefix;
	}

}
