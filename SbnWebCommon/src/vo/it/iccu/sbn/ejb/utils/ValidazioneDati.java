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
package it.iccu.sbn.ejb.utils;


import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.common.SerializableComparator;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.util.Base64Util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.apache.commons.beanutils.PropertyUtils;

public class ValidazioneDati {

	private static final char BLANK = (char)32;

	public static final int DATA_OK = 0;
	public static final int DATA_ERRATA = 1;
	public static final int DATA_MAGGIORE = 2;
	public static final int DATA_PASSATA_ERRATA = 3;
	public static final int DATA1_EQ_DATA2 = 4;
	public static final int DATA1_GT_DATA2 = 5;
	public static final int DATA1_LT_DATA2 = 7;

	private static final Pattern XID_REGEX = Pattern.compile("([A-Z]{1}\\w{2}[E|M|V|C|D|L|P|0-9]\\d{6})");
	private static final Pattern INV_REGEX = Pattern.compile("^(.{0,3})(\\d{9})$|^(\\d{1,8})$");

	public static final boolean strIsNull(String data) {
		return (data == null) || (data.trim().equals(""));
	}

	public static final boolean strIsEmpty(String data) {
		return (data == null) || (data.length() == 0);
	}

	public static final boolean notEmpty(String data) {
		return (data != null) && (data.length() > 0);
	}

	public static final boolean isFilled(String value) {
		return (value != null && !"".equals(value.trim()) );
	}

	public static final boolean isFilled(Object[] value) {
		return (value != null && !(value.length < 1) );
	}

	public static boolean isFilled(byte[] value) {
		return (value != null && !(value.length < 1) );
	}

	public static final boolean isFilled(Collection<?> value) {
		return (value != null && value.size() > 0 );
	}

	public static final boolean isFilled(Map<?, ?> value) {
		return (value != null && value.size() > 0 );
	}

	public static final boolean isFilled(Character value) {
		return (value != null && value.charValue() != 0 && value.charValue() != BLANK);
	}

	public static final boolean isFilled(Number value) {
		return (value != null && (value.intValue() != 0 || value.doubleValue() != 0.000) );
	}

	public static final boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null)
			return true;
		if (o1 == null || o2 ==  null)
			return false;
		if (!o1.getClass().equals(o2.getClass()))
			return false;

		return o1.equals(o2);
	}

	public static final boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 == null && str2 == null)
			return true;
		if (str1 == null || str2 ==  null)
			return false;

		return str1.equalsIgnoreCase(str2);
	}

	public static final <T> boolean in(T obj, T... values) {
		if (obj == null || !isFilled(values))
			return false;
		for (Object v : values)
			if (obj.equals(v))
				return true;

		return false;
	}

	public static final <T> boolean in(T obj, Collection<T> values) {
		if (obj == null || !isFilled(values))
			return false;
		for (Object v : values)
			if (obj.equals(v))
				return true;

		return false;
	}

	public static final <T> boolean between (Comparable<T> c, T from, T to) {
		return (compare(c, from) >= 0) && (compare(c, to) <= 0);
	}

	public static final <T> int size(Collection<T> value) {
		if (value != null)
			return value.size();

		return 0;
	}

	public static final <T, V> int size(Map<T, V> value) {
		if (value != null)
			return value.size();

		return 0;
	}

	public static final int size(Object[] value) {
		if (value != null)
			return value.length;

		return 0;
	}

	public static final int length(String value) {
		if (value != null)
			return value.length();

		return 0;
	}

	public static final <K, V> Set<K> getMapKeySet(Map<K, V> map) {
		if (!isFilled(map))
			return null;

		return map.keySet();
	}
	/**
	 * validaData()
	 * controllo FORMALE della data (dd/mm/yyyy)
	 * @param data
	 * @return
	 */
	public static final int validaData(String data) {

		int codRitorno = -1;
		if (strIsNull(data)) {
			codRitorno = DATA_PASSATA_ERRATA;
			return codRitorno;
		}
		try {
			if (Pattern.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", data)) {
				codRitorno = DATA_OK;
				return codRitorno; // tutto OK
			} else {
				codRitorno = DATA_ERRATA;
				throw new Exception(); // formato data errato
			}
		} catch (Exception e) {
			return codRitorno;
		}
	}

	public static final int validaData_1(String data) {
		//come 	validaDataPassata ma senza in controllo sulla data corrente

		int codRitorno = -1;
		if (strIsNull(data)) {
			codRitorno = DATA_PASSATA_ERRATA;
			return codRitorno;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.setLenient(false); // Date date =
			format.parse(data);

			if (java.util.regex.Pattern.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", data)) {
				codRitorno = DATA_OK;
				return codRitorno; // tutto OK
			} else {
				codRitorno = DATA_ERRATA;
				throw new Exception(); // formato data errato
			}
		} catch (Exception e) {

			return codRitorno;
		}
	}

	public static final int validaDataPassata(String data) {

		int codRitorno = -1;
		if (strIsNull(data)) {
			codRitorno = DATA_PASSATA_ERRATA;
			return codRitorno;
		}

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.setLenient(false); // Date date =
			// DateParser.parseDate(data);
			Date date = format.parse(data);
			if (java.util.regex.Pattern.matches(
					"[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", data)) {
				Date oggi = new Date(System.currentTimeMillis());
				if (date.after(oggi)) {
					codRitorno = DATA_MAGGIORE;
					throw new Exception(); // data > data odierna
				}
				codRitorno = DATA_OK;
				return codRitorno; // tutto OK
			} else {
				codRitorno = DATA_ERRATA;
				throw new Exception(); // formato data errato
			}
		} catch (Exception e) {
			return codRitorno;
		}
	}

	public static final boolean strIsAlfabetic(String data) {
		return (Pattern.matches("[^0-9]+", data));
	}

	public static final boolean strIsNumeric(String data) {
		return (Pattern.matches("^\\d+$", data));
	}

	public static final boolean strIsFloat(String data) {
		return (Pattern.matches("^[0-9]*\\.[0-9]+$", data));
	}

	public static final String fillLeft(String src, char filler, int limit) {

		if (src == null)
			return null;

		int len = src.length();
		if (len >= limit)
			return src;

		int diff = limit - len;

		StringBuilder tmp = new StringBuilder(limit);
		for (int i = 0; i < diff; i++)
			tmp.append(filler);

		tmp.append(src);
		return tmp.toString();
	}

	public static final String fillRight(String src, char filler, int limit) {

		if (src == null)
			return null;

		int len = src.length();
		if (len >= limit)
			return src;

		int diff = limit - len;

		StringBuilder tmp = new StringBuilder(limit);
		tmp.append(src);
		for (int i = 0; i < diff; i++)
			tmp.append(filler);

		return tmp.toString();
	}

	public static final String rtrim(String value) {
		if (value == null)
			return null;

		if (strIsNull(value))
			return "";

		int len = value.length();
		while (value.charAt(len - 1) == BLANK) {
			value = value.substring(0, len - 1);
			len--;
		}

		return value;
	}

	public static final String ltrim(String value) {
		if (value == null)
			return null;

		if (strIsNull(value))
			return "";

		while(value.charAt(0) == BLANK)
			value = value.substring(1);

		return value;
	}

	public static final String trunc(String value, int max) {
		if (value == null)
			return null;

		return value.length() > max ? value.substring(0, max) : value;
	}

	public static final double getDoubleFromString(String stringValue, String format, Locale locale)
	throws ParseException, IllegalArgumentException, NullPointerException {
		NumberFormat formatter = ValidazioneDati.getNumberFormatter(format, locale);

		return formatter.parse(stringValue).doubleValue();
	}

	public static final double getDoubleFromString(String stringValue, String format, Locale locale, int maxFraction)
	throws ParseException, IllegalArgumentException, NullPointerException {
		NumberFormat formatter = ValidazioneDati.getNumberFormatter(format, locale);
		formatter.setMaximumFractionDigits(maxFraction);

		return formatter.parse(stringValue).doubleValue();
	}

	public static final double getDoubleFromString(String stringValue, String format, Locale locale, int maxInt, int maxFraction)
	throws ParseException, IllegalArgumentException, NullPointerException {
		if (stringValue == null)
			return 0;
		NumberFormat formatter = ValidazioneDati.getNumberFormatter(format, locale);
		formatter.setMaximumIntegerDigits(maxInt);
		formatter.setMaximumFractionDigits(maxFraction);

		return formatter.parse(stringValue).doubleValue();
	}


	public static final String getStringFromDouble(double value, String format, Locale locale)
	throws IllegalArgumentException, NullPointerException {
		NumberFormat formatter = ValidazioneDati.getNumberFormatter(format, locale);

		return formatter.format(value);
	}
	public static final String getStringFromDouble(double value, String format, Locale locale, int maxFraction)
	throws IllegalArgumentException, NullPointerException {
		NumberFormat formatter = ValidazioneDati.getNumberFormatter(format, locale);
		formatter.setMaximumFractionDigits(maxFraction);

		return formatter.format(value);
	}


	public static final NumberFormat getNumberFormatter(String format, Locale locale) throws IllegalArgumentException, NullPointerException
	{
		NumberFormat formatter;
		if (locale==null)
			formatter = NumberFormat.getNumberInstance();
		else
			formatter = NumberFormat.getNumberInstance(locale);
		if (formatter instanceof DecimalFormat){
			((DecimalFormat)formatter).applyPattern(format);
			((DecimalFormat)formatter).setDecimalSeparatorAlwaysShown(true);
		}
		return formatter;
	}

	public static final int confrontaLeDate(String strdata1, String strdata2, String tipoOP) {
		//after restituisce "true" se la data é > "false" se <=
		//before restituisce "true" se la data é < "false" se >=
		// -1 errore OK a seconda di tipoOP
		// se tipoOP=1 allora data1 > data2 restituisce OK = 5
		// se tipoOP=2 allora data1 < data2 restituisce OK = 6
		// se tipoOP=3 allora data1 = data2 restituisce OK = 4
		// almaviva4 14.12.09 se tipoOP=4 allora data1 < data2 restituisce OK = 4

		int codRitorno = -1;
		if (strIsNull(strdata1) || strIsNull(strdata2)) {
			codRitorno = DATA_PASSATA_ERRATA;
			return codRitorno;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.setLenient(false); // Date date =
			// DateParser.parseDate(data);
			Date date1 = format.parse(strdata1);
			Date date2 = format.parse(strdata2);
			// Date oggi = new Date(System.currentTimeMillis());
			if (java.util.regex.Pattern.matches(
					"[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", strdata1)
					|| java.util.regex.Pattern.matches(
							"[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", strdata2)) {
				if (tipoOP.equals("1")) {
					if (!date1.after(date2)) {
						throw new Exception();
					}
					codRitorno = DATA1_GT_DATA2;
					return codRitorno; // tutto OK
				}
				if (tipoOP.equals("2")) {
					if (date2.equals(date1)) {
						codRitorno = DATA1_EQ_DATA2;
						throw new Exception();
					}
					return codRitorno;
				}
				if (tipoOP.equals("4")) {
					if (date2.after(date1)) {
						throw new Exception();
					}
					codRitorno = DATA1_LT_DATA2;
					return codRitorno; // tutto OK
				}

				return codRitorno;
			} else {
				codRitorno = DATA_ERRATA;
				throw new Exception();
			}
		} catch (Exception e) {
			return codRitorno;
		}
	}

	public static final int confrontaLeDateBest(String strdata1, String strdata2) {
		//after restituisce "true" se la data é > "false" se <=
		//before restituisce "true" se la data é < "false" se >=
		// -1 errore
		// se data1 > data2 restituisce OK = 5
		// se data1 < data2 restituisce OK = 7
		// se data1 = data2 restituisce OK = 4

		int codRitorno = -1;
		if (strIsNull(strdata1) || strIsNull(strdata2)) {
			codRitorno = DATA_PASSATA_ERRATA;
			return codRitorno;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			format.setLenient(false); // Date date =
			// DateParser.parseDate(data);
			Date date1 = format.parse(strdata1);
			Date date2 = format.parse(strdata2);
			// Date oggi = new Date(System.currentTimeMillis());
			if (java.util.regex.Pattern.matches(
					"[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", strdata1)
					|| java.util.regex.Pattern.matches(
							"[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", strdata2)) {
				if (date2.equals(date1)) {
					codRitorno = DATA1_EQ_DATA2;
					return codRitorno; // tutto OK
				}
				if (date1.after(date2)) {
					codRitorno = DATA1_GT_DATA2;
					return codRitorno; // tutto OK
				}
				else
				{
					codRitorno = DATA1_LT_DATA2;
					return codRitorno; // tutto OK
				}

			} else {
				codRitorno = DATA_ERRATA;
				throw new Exception();
			}
		} catch (Exception e) {
			return codRitorno;
		}
	}

	public static final <T extends Comparable<T>> T min(T minValue, T... values) {
		for (T c : values)
			minValue = (c != null && c.compareTo(minValue) < 0) ? c : minValue;
		return minValue;
	}

	public static final <T extends Comparable<T>> T max(T maxValue, T... values) {
		for (T c : values)
			maxValue = (c != null && c.compareTo(maxValue) > 0) ? c : maxValue;
		return maxValue;
	}

	public static final String unmaskString(String value) {
		return new String(Base64Util.decode(value) );
	}

	public static final String dumpBytes(byte[] bs) {
		return Base64Util.encode(bs);
	}

	public static final Map<String, String> dump(Object src) {
		return dump(src, false, false);
	}

	public static final Map<String, String> dump(Object src, boolean excludeNulls, boolean excludeSuper) {

		if (src == null)
			return null;

		Map<String, String> properties = new HashMap<String, String>();
		List<Field> fields = new ArrayList<Field>();
		Class<?> clazz = src.getClass();

		if (!excludeSuper) {
			Class<?> zuper = clazz;
			while ( (zuper = zuper.getSuperclass()) != null)
				fields.addAll(Arrays.asList(zuper.getDeclaredFields()));
		}

		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		for (Field field : fields) {
			try {
				if (Modifier.isStatic(field.getModifiers()) )
					continue;
				field.setAccessible(true);
				Object value = field.get(src);
				if (value == null && excludeNulls)
					continue;
				properties.put(field.getName(), String.valueOf(field.get(src)) );

			} catch (IllegalAccessException ex) {
				continue;
			}

		}

		return properties;
	}

	public static final CodiceVO leggiInventario(String line) throws Exception {

		if (strIsNull(line))
			return null;

		Matcher m = INV_REGEX.matcher(line.trim().toUpperCase() );

		if (!m.matches() )
			return null;

		String serie = m.group(1) == null ? "   " : fillRight(m.group(1), BLANK, 3);
		String inv1 = m.group(2);
		String inv2 = m.group(3);

		return new CodiceVO(serie, inv1 != null ? inv1 : inv2 );
	}

	public static final CodiceVO leggiBid(String line) throws Exception {

		if (strIsNull(line))
			return null;

		Matcher m = INV_REGEX.matcher(line.trim() );

		if (!m.matches() )
			return null;
		String bid = null;
		if (m.group(1) != null ){
			bid = m.group(1);
		}

		return new CodiceVO(bid);
	}

	public static final boolean leggiXID(String xid) {
		return isFilled(xid) && XID_REGEX.matcher(xid).matches();
	}

	public static final String trimOrEmpty(String value) {
		String tmp;
		if (value == null || "".equals((tmp = value.trim())) )
			return "";
		return tmp;
	}

	public static final String trimOrNull(String value) {
		String tmp;
		if (value == null || "".equals((tmp = value.trim())) )
			return null;
		return tmp;
	}

	public static final String trimOrBlank(String value) {
		String tmp;
		if (value == null || "".equals((tmp = value.trim())) )
			return "\u0020";
		return tmp;
	}

	public static final <T> Comparator<T> invertiComparatore(final Comparator<T> c) {
		return new Comparator<T>() {
			public int compare(T o1, T o2) {
				return -(c.compare(o1, o2));
			}
		};
	}

	public static final <T> SerializableComparator<T> invertiComparatore(final SerializableComparator<T> c) {
		return new SerializableComparator<T>() {

			private static final long serialVersionUID = 1L;

			public int compare(T o1, T o2) {
				return -(c.compare(o1, o2));
			}
		};
	}

	public static final <T> int compare(Comparable<T> o1, T o2) {
		//NB: il valore NULL é inteso come minimo valore possibile
		if (o1 == null && o2 == null)
			return 0;
		if (o1 != null && o2 == null)	//o1 > null
			return 1;
		if (o1 == null && o2 != null)	//null < o2
			return -1;

		return o1.compareTo(o2);
	}

	public static final int firstIndexOf(String value, String... strs) {
		if (!isFilled(value))
			return -1;

		int min = Integer.MAX_VALUE;
		for (String s : strs) {
			int idx = value.indexOf(s);
			if (idx > -1 && idx < min)
				min = idx;
		}

		return min != Integer.MAX_VALUE ? min : -1;
	}

	public static final <T> List<T> asSingletonList(T target) {
		List<T> list = new ArrayList<T>();
		list.add(target);
		return list;
	}

	public static final <T> List<T> asList(T target) {
		List<T> list = new ArrayList<T>();
		list.add(target);
		return list;
	}

	public static final <T> List<T> emptyList() {
		return new ArrayList<T>();
	}

	public static final <T> List<T> emptyList(Class<T> c) {
		return new ArrayList<T>();
	}

	public static final <T> T last(List<T> list) {
		if (!isFilled(list))
			return null;

		return list.get(list.size() - 1);
	}

	public static final <T> List<T> tail(List<T> list, int cnt) {
		if (!isFilled(list))
			return null;

		int size = list.size();
		if (size <= cnt)
			return list;

		return list.subList(size - cnt, size);
	}

	public static final <T> T coalesce(T v, T def) {
		return v != null ? v : def;
	}

	public static final <T> T first(Collection<T> c) {
		if (!isFilled(c))
			return null;
		Iterator<T> i = c.iterator();
		return i.hasNext() ? i.next() : null;
	}

	public static final <T> T first(T[] c) {
		if (!isFilled(c))
			return null;
		return c[0];
	}

	public static final String formatValueList(List<String> values, String separator) {
		return formatValueList(values, null, separator);
	}

	public static final String formatValueList(List<String> values, Character quote, String separator) {
		if (!isFilled(values))
			return null;

		StringBuilder buf = new StringBuilder();
		Iterator<String> i = values.iterator();
		for (;;) {
			if (quote != null)
				buf.append(quote).append(i.next()).append(quote);
			else
				buf.append(i.next());
			if (i.hasNext())
				buf.append(separator);
			else
				break;
		}

		return buf.toString();
	}

	public static final boolean eqAuthority(SbnAuthority authority, SbnAuthority... authorities) {
		if (authority == null || !isFilled(authorities))
			return false;
		for (SbnAuthority auth : authorities)
			if (authority.getType() == auth.getType())
				return true;

		return false;
	}

	public static final int checksum(byte[] value) {
		if (value == null)
			return -1;

		Checksum c = new Adler32();
		c.update(value, 0, value.length);

		return (int)c.getValue();
	}

	public static final String livelloSoglia(final String cd_livello) {
		try {
			int livello = Integer.parseInt(cd_livello.trim() );
			if (livello == 1)
				return "01";
			if (livello == 2)
				return "02";
			if (livello == 3)
				return "03";
			if (livello == 4)
				return "04";
			else if ((livello == 5) || (livello == 96) || (livello == 97))
				return cd_livello;
			else if ((livello > 5) && (livello <= 51))
				return "51";
			else if ((livello > 51) && (livello <= 71))
				return "71";
			else if ((livello > 71) && (livello <= 90))
				return "90";
			else if ((livello > 90) && (livello <= 95))
				return "95";

		} catch (Exception e) {}

		return "05";
	}

	public static final <T> T remove(List<T> list, int idx) {
		if (list != null && list.size() > idx)
			return list.remove(idx);

		return null;
	}

	public static final String substring(final String str, int start, int end) {
		int len = length(str);
		if (len < 1)
			return "";

		if (start > end)
			return "";

		if ( (start + 1) > len)
			return "";

		if ( (end + 1) > len)
			end = len;

		return str.substring(start, end);
	}

	public static final <K, T> Map<K, T> setToMap(Set<T> set, Class<K> keyClass, String keyField) {
		Map<K, T> out = new HashMap<K, T>(set.size());
		try {
			for (T o : set) {
				K key = keyClass.cast(PropertyUtils.getProperty(o, keyField));
				out.put(key, o);
			}
		} catch (Exception e) {
			return null;
		}

		return out;
	}

	public static final <K, T> Map<K, T> listToMap(List<T> list, Class<K> keyClass, String keyField) {
		Map<K, T> out = new HashMap<K, T>(list.size());
		try {
			for (T o : list) {
				K key = keyClass.cast(PropertyUtils.getProperty(o, keyField));
				out.put(key, o);
			}
		} catch (Exception e) {
			return null;
		}

		return out;
	}
}
