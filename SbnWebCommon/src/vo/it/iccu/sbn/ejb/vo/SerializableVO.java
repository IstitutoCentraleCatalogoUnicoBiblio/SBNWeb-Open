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
package it.iccu.sbn.ejb.vo;

import gnu.trove.THashSet;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.validation.Validable;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class SerializableVO implements Serializable, Validable {

	private static final long serialVersionUID = -6900261304564588627L;

	private static final String NEW_LINE = System.getProperty("line.separator");

	private static class DumpDriver {
		Set<Object> objects = new THashSet<Object>();
		int level = 0;
	}

	private static final ThreadLocal<DumpDriver> dumpDriver = new ThreadLocal<DumpDriver>() {
		@Override
		protected DumpDriver initialValue() {
			return new DumpDriver();
		}
	};

	/* (non-Javadoc)
	 * @see it.iccu.sbn.ejb.vo.Validable#validate()
	 */
	public void validate() throws ValidationException {
		return;
	}

	/* (non-Javadoc)
	 * @see it.iccu.sbn.ejb.vo.Validable#validate(it.iccu.sbn.ejb.vo.SerializableVO.Validator)
	 */
	@SuppressWarnings("unchecked")
	public <T extends SerializableVO> void validate(Validator<T> v) throws ValidationException {
		if (v == null)
			throw new ValidationException(SbnErrorTypes.ERROR_VALIDATOR_IS_NULL);

		v.validate((T) this);
	}

	public ValidationException[] getValidationExceptions() {
		return null;
	}

	public boolean isEmpty() {
		return true;
	}

	public Object clone() {
		try {
			return ClonePool.deepCopy(this);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public final <T extends SerializableVO> T copy() {
		Object clone = this.clone();
		return (T) clone;
	}

	protected static final boolean notEmpty(String data) {
		return ValidazioneDati.notEmpty(data);
	}


	protected static final boolean isFilled(String value) {
		return ValidazioneDati.isFilled(value);
	}

	protected static final boolean isFilled(Collection<?> value) {
		return ValidazioneDati.isFilled(value);
	}

	protected static final boolean isFilled(Map<?, ?> value) {
		return ValidazioneDati.isFilled(value);
	}

	protected static final boolean isFilled(Object[] value) {
		return ValidazioneDati.isFilled(value);
	}

	protected static final boolean isFilled(Character value) {
		return ValidazioneDati.isFilled(value);
	}

	protected static final boolean isFilled(Number value) {
		return ValidazioneDati.isFilled(value);
	}

	protected static final boolean isNull(String value) {
		return (value == null || "".equals(value.trim()));
	}

	protected static final boolean isEmpty(String value) {
		return (value != null && "".equals(value.trim()));
	}

	protected static final boolean isNumeric(String value) {
		return (Pattern.matches("^[-+]?[0-9]*[.,]?[0-9]+$", trimOrEmpty(value)));
	}

	protected static final boolean isAlphabetic(String value) {
		return (Pattern.matches("^[a-zA-Z]+$", trimOrEmpty(value)));
	}

	protected static final int length(String value) {
		if (value == null)
			return -1;
		return value.length();
	}

	protected static final String trimAndSet(String value) {
		if (value == null)
			return null;
		return value.trim();
	}

	protected static final String trimOrBlank(String value) {
		String tmp;
		if (value == null || "".equals((tmp = value.trim())) )
			return " ";
		return tmp;
	}

	protected static final String trimOrEmpty(String value) {
		String tmp;
		if (value == null || "".equals((tmp = value.trim())) )
			return "";
		return tmp;
	}

	protected static final String trimOrFill(String value, char filler, int length) {
		String tmp;
		if (value == null || "".equals((tmp = value.trim())) )
			return ValidazioneDati.fillRight("", filler, length);
		return tmp;
	}

	protected static final String trimOrNull(String value) {
		String tmp;
		if (value == null || "".equals((tmp = value.trim())) )
			return null;
		return tmp;
	}

	protected static final String noNull(String value) {
		return (value == null ? "" : value);
	}

	protected static final String toUpperCase(String value) {
		if (value == null)
			return null;
		return value.toUpperCase();
	}

	protected static final String toLowerCase(String value) {
		if (value == null)
			return null;
		return value.toLowerCase();
	}

	protected static boolean listEquals(List<?> list1,
			List<?> list2, Class<?> elementType) {

		if (list1 == null) {
			if (list2 != null)
				return false;
		} else {

			if (list1.size() != list2.size())
				return false;

			for (int i = 0; i < list1.size(); i++) {
				Object o1 = list1.get(i);
				Object o2 = list2.get(i);
				if ((o1.getClass() != elementType)
						|| (o2.getClass() != elementType))
					return false;

				if (!o1.equals(o2))
					return false;
			}
		}
		return true;
	}

	public static final <T> int size(Collection<T> value) {
		if (value != null)
			return value.size();

		return 0;
	}

	public static final int size(Object[] value) {
		if (value != null)
			return value.length;

		return 0;
	}

	protected static final boolean copyCommonProperties(SerializableVO dest, Serializable source) {
		return ClonePool.copyCommonProperties(dest, source);
	}

	public static String wrap(String value) {
		return value != null ? value : "";
	}

	@Override
	public String toString() {
		return dump(this, false);
	}

	public static String dump(Serializable o, boolean excludeNulls) {
		DumpDriver dd = dumpDriver.get();
		if (dd.objects.contains(o))	//fix loop ricorsivo infinito
			return "\u2026";	//horizontal ellipsis

		dd.objects.add(o);
		//spaziatura su livello
		String closer = ValidazioneDati.fillRight("", '\t', dd.level);
		String filler = ValidazioneDati.fillRight("", '\t', ++dd.level);
		try {
			StringBuilder result = new StringBuilder(512);

			Class<?> clazz = o.getClass();
			result.append(clazz.getName());
			result.append("@");
			result.append(Integer.toHexString(o.hashCode()));
			result.append(" [");
			result.append(NEW_LINE);

			List<Field> fields = new ArrayList<Field>();

			Class<?> zuper = clazz;
			while ( (zuper = zuper.getSuperclass()) != null)
				fields.addAll(Arrays.asList(zuper.getDeclaredFields()));

			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

			StringBuilder buf = new StringBuilder(512);
			for (Field field : fields) {
				try {
					int modifiers = field.getModifiers();
					if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers))
						continue;

					field.setAccessible(true);
					Object value = field.get(o);
					if (value == null && excludeNulls)
						continue;

					buf.setLength(0);
					buf.append(filler);
					buf.append(field.getName());
					buf.append(": ");
					buf.append(field.getType().isAssignableFrom(String.class) ? "'" + value + "'" : value);
				} catch (IllegalAccessException ex) {
					continue;
				}
				buf.append(NEW_LINE);
				result.append(buf);
			}
			result.append(closer).append("]");

			return result.toString();

		} finally {
			dd.objects.remove(o);
			dd.level--;
		}
	}

}
