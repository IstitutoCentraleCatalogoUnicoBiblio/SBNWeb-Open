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
package it.iccu.sbn.util.config;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.lang.ref.SoftReference;

public class ConfigInterceptor extends SerializableVO {

	private static final long serialVersionUID = 1419632053751543196L;

	private final String[] props;
	private final transient SoftReference<ConfigChangeInterceptor> ref;

	public ConfigInterceptor(ConfigChangeInterceptor i, String... props) {
		this.ref = new SoftReference<ConfigChangeInterceptor>(i);
		this.props = props;
	}

	public ConfigChangeInterceptor getReference() {
		return (ref == null ? null : ref.get() );
	}

	public String[] getProperties() {
		return props;
	}

	public boolean isInterested(String key) {
		if (!isFilled(props))
			return true;

		for (String prop : props)
			if (prop.equals(key))
				return true;

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigInterceptor other = (ConfigInterceptor) obj;
		if (ref == null) {
			if (other.ref != null)
				return false;

		} else {	// check weak ref
			ConfigChangeInterceptor i1 = ref.get();
			ConfigChangeInterceptor i2 = other.getReference();
			if (i1 != null && i2 != null && i1 == i2)
				return true;
		}

		return false;
	}

}
