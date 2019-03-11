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

import java.util.Collection;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.XMLFieldDescriptor;

public class SbnMarcMarshalListener implements MarshalListener {

	private static Logger log = Logger.getLogger(SbnMarcMarshalListener.class);

	private static final ThreadLocal<MarshalListener> instances = new ThreadLocal<MarshalListener>() {
		@Override
		protected MarshalListener initialValue() {
			return new SbnMarcMarshalListener();
		}
	};

	private class SbnMarcDumpStyle extends ToStringStyle {

		private static final long serialVersionUID = 8300252226171796502L;

		public SbnMarcDumpStyle() {
			setArrayContentDetail(true);
			setUseShortClassName(true);
			setUseClassName(false);
			setUseIdentityHashCode(false);
			setFieldSeparator(", " + SystemUtils.LINE_SEPARATOR + "  ");
		}

		@Override
		public void appendDetail(StringBuffer buffer, String fieldName,	Object value) {
			String className = value.getClass().getName();
			if (!className.startsWith("java"))
				buffer.append(ReflectionToStringBuilder.toString(value,	this));
			else
				super.appendDetail(buffer, fieldName, value);
		}

		@Override
		public void appendDetail(StringBuffer buffer, String fieldName,	@SuppressWarnings("rawtypes") Collection value) {
			appendDetail(buffer, fieldName, value.toArray());
		}

	}

	private ToStringStyle style;

	public static final MarshalListener getInstance() {
		return instances.get();
	}

	private SbnMarcMarshalListener() {
		log.debug("Marshaling Listener istanziato per thread n. " + Thread.currentThread().getId());
		this.style = new SbnMarcDumpStyle();
	}

	public boolean preMarshal(Object object) {
		return true;
	}

	public void postMarshal(Object object) {
		return;
	}

	public void marshalError(Object object, XMLFieldDescriptor descriptor, Throwable t) {
		log.error("ERRORE Marshal XML: tag: "
				+ descriptor.getFieldName() + ", value: "
				+ ReflectionToStringBuilder.toString(object, style));
		return;
	}

	public void validateError(Object object) {
		log.error("ERRORE Validation XML: value: "
				+ ReflectionToStringBuilder.toString(object, style));
		return;

	}

}
