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
package it.iccu.sbn.vo.custom.semantica;

import it.iccu.sbn.web.exception.SbnBaseException;

import java.io.Serializable;

public class UserMessage implements Serializable {

	private static final long serialVersionUID = -5978144469642784215L;

	protected String key;
	protected Object[] values;
	protected boolean resource;
	protected SbnBaseException exception;

	public UserMessage(String key) {
		this(key, (Object[]) null);
	}

	public UserMessage(String key, Object value0) {
		this(key, new Object[] { value0 });
	}

	public UserMessage(String key, Object value0, Object value1) {
		this(key, new Object[] { value0, value1 });
	}

	public UserMessage(String key, Object value0, Object value1, Object value2) {
		this(key, new Object[] { value0, value1, value2 });
	}

	public UserMessage(String key, Object value0, Object value1, Object value2, Object value3) {
		this(key, new Object[] { value0, value1, value2, value3 });
	}

	public UserMessage(String key, Object[] values) {
		this.key = null;
		this.values = null;
		this.resource = true;
		this.key = key;
		this.values = values;
		this.resource = true;
	}

	public UserMessage(String key, boolean resource) {
		this.key = null;
		this.values = null;
		this.resource = true;
		this.key = key;
		this.resource = resource;
	}

	public UserMessage(SbnBaseException e) {
		this.exception = e;
	}

	public String getKey() {
		return this.key;
	}

	public Object[] getValues() {
		return this.values;
	}

	public boolean isResource() {
		return this.resource;
	}

	public SbnBaseException getException() {
		return exception;
	}

	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append(this.key);
		buff.append("[");
		if (this.values != null) {
			for (int i = 0; i < this.values.length; ++i) {
				buff.append(this.values[i]);
				if (i < this.values.length - 1) {
					buff.append(", ");
				}
			}
		}

		buff.append("]");
		return buff.toString();
	}
}
