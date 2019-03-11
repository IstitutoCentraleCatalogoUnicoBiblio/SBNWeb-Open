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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class DateUtil {

	static ThreadLocal<Calendar> CALENDAR_INSTANCE = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};

	private final static SimpleDateFormat _dataPicos = new SimpleDateFormat("yyMMdd");

	private final static SimpleDateFormat _dataOra = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private final static SimpleDateFormat _data = new SimpleDateFormat("dd/MM/yyyy");

	private final static SimpleDateFormat _dataISO = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat _dataCompleta = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	private final static SimpleDateFormat _dataCompletaTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");

	private final static SimpleDateFormat _Orario = new SimpleDateFormat("HH:mm");

	private static final long MILLISECS_PER_MINUTE = 60 * 1000;

	private static final long MILLISECS_PER_HOUR = 60 * MILLISECS_PER_MINUTE;

	private static final long MILLISECS_PER_DAY = 24 * MILLISECS_PER_HOUR;

	public static final Date MIN_DATE = of(1900, 1, 1).toDate();	//LocalDate.now().withYear(0).toDate();
	public static final Date MAX_DATE = LocalDate.now().withYear(Short.MAX_VALUE).toDate();

	public static final Timestamp MIN_TIMESTAMP = new Timestamp(MIN_DATE.getTime());


	public static final int getDay(Date date) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	public static final int getDayOfWeek(Date date) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static final int getYear(Date date) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static final int getMonth(Date date) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}

	public static final Date toDate(String date) {
		try {
			Date d = _data.parse(date);
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	public static final Date toDateISO(String date) {
		try {
			Date d = _dataISO.parse(date);
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	public static final Timestamp toTimestampISO8601(String date) {
		try {
			if (ValidazioneDati.strIsNull(date))
				return null;

			return new Timestamp(_dataCompletaTimestamp.parse(date).getTime() );

		} catch (Exception e) {
			return null;
		}
	}

	public static final Timestamp toTimestamp(String date) {
		try {
			if (ValidazioneDati.strIsNull(date))
				return null;

			Date d = _data.parse(date);
			return new Timestamp(removeTime(d).getTime());

		} catch (Exception e) {
			return null;
		}
	}

	public static final Timestamp toTimestampA(String date) {
		try {
			if (ValidazioneDati.strIsNull(date))
				return null;

			Date d = _data.parse(date);
			return withTimeAtEndOfDay(d);

		} catch (Exception e) {
			return null;
		}
	}

	public static final String toFormatoIso(String date) {
		try {
			Date d = _data.parse(date);
			String format = _dataISO.format(d);
			return format;

		} catch (Exception e) {
			return null;
		}
	}

	public static String toFormatoIso(Date dt) {
		try {
			String format = _dataISO.format(dt);
			return format;

		} catch (Exception e) {
			return null;
		}
	}

	public static final boolean isData(long date) {
		return !"".equals(formattaData(date));
	}

	public static final boolean isData(String date) {
		Date d = toDate(date);
		return d != null && _data.format(d).equals(date);
	}

	/*
	 * Format a long into a Date using the pattern MM/dd/yyyy - HH:mm:ss return
	 * empty if an error occurs
	 */
	public static final String formattaDataOra(long date) {
		try {
			return _dataOra.format(new Date(date));
		} catch (Exception e) {
			return "";
		}
	}

	/*
	 * dataOra di tipo "dd/MM/yyyy - HH:mm:ss"
	 */
	public static final Date getDataOra(String dataOra) {
		try {
			return _dataOra.parse(dataOra);
		} catch (Exception e) {
			return null;
		}
	}

	public static final String formattaOrario(Date date) {
		try {
			return _Orario.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static final String formattaData(long date) {
		try {
			return _data.format(new Date(date));
		} catch (Exception e) {
			return "";
		}
	}

	public static final String formattaData(Date date) {
		try {
			return _data.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static final String formattaDataCompleta(long date) {
		try {
			return _dataCompleta.format(new Date(date));
		} catch (Exception e) {
			return "";
		}
	}

	public static final String formattaDataCompletaTimestamp(Timestamp date) {
		try {
			return _dataCompletaTimestamp.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static final long diffDays(Calendar start, Calendar end) {
		long endL = end.getTimeInMillis()
				+ end.getTimeZone().getOffset(end.getTimeInMillis());
		long startL = start.getTimeInMillis()
				+ start.getTimeZone().getOffset(start.getTimeInMillis());
		return (endL - startL) / MILLISECS_PER_DAY;
	}

	public static final String getDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// df.setTimeZone ( TimeZone.getTimeZone ( "PST" ) ) ;
		return df.format(new Date());
	}

	public static final String getTime() {
		DateFormat df = new SimpleDateFormat("HH:mm:ss"); // :z
		// df.setTimeZone ( TimeZone.getTimeZone ( "PST" ) ) ;
		// df.setTimeZone ( TimeZone.getTimeZone ( "America/Los_Angeles" ) ) ;
		return df.format(new Date());
	}

	public static final String getDateSip2() {
		DateFormat df = new SimpleDateFormat("YYYYMMddZZZZHHmmss");
		// df.setTimeZone ( TimeZone.getTimeZone ( "PST" ) ) ;
		return df.format(new Date());
	}

	public static final String formattaDataOra(Timestamp timestamp) {
		try {
			return _dataOra.format(timestamp);
		} catch (Exception e) {
			return "";
		}

	}

	public static final String formattaDataPicos(Timestamp timestamp) {
		try {
			return _dataPicos.format(timestamp);
		} catch (Exception e) {
			return "";
		}

	}

	public static final String now() {
		try {
			return _dataOra.format(new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			return "";
		}

	}

	public static final Date dataOra(String dataOra) {
		try {
			return _dataOra.parse(dataOra);
		} catch (Exception e) {
			return null;
		}

	}

	// FUNZIONE PER AGGIUNGERE / TOGLIERE DEI GIORNI AD UNA DATA
	public static final String addDay(String dateDbf, int dayToAdd) {
		String dataNew = "";
		String DATE_FORMAT = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			Calendar c1 = CALENDAR_INSTANCE.get();
			int anno = Integer.parseInt(dateDbf.substring(0, 4));
			int mese = Integer.parseInt(dateDbf.substring(4, 6)) - 1;
			int giorno = Integer.parseInt(dateDbf.substring(6, 8));
			c1.set(anno, mese, giorno);
			c1.add(Calendar.DATE, dayToAdd);
			dataNew = sdf.format(c1.getTime());
		} catch (Exception e) {
			return "";
		}
		return dataNew;
	}

	public static final Date addDay(Date dateDbf, int dayToAdd) {
		try {
			if (dayToAdd == 0)
				return dateDbf;

			Calendar c1 = CALENDAR_INSTANCE.get();
			c1.setTime(dateDbf);
			c1.add(Calendar.DATE, dayToAdd);
			return c1.getTime();
		} catch (Exception e) {
			return null;
		}
	}

	public static final Timestamp addDay(Timestamp dateDbf, int dayToAdd) {
		try {
			if (dayToAdd == 0)
				return dateDbf;

			Calendar c1 = CALENDAR_INSTANCE.get();
			c1.setTime(dateDbf);
			c1.add(Calendar.DATE, dayToAdd);
			return new Timestamp(c1.getTimeInMillis());
		} catch (Exception e) {
			return null;
		}
	}

	public static final Timestamp addMillis(Timestamp dateDbf, int millis) {
		try {
			if (millis == 0)
				return dateDbf;

			Calendar c1 = CALENDAR_INSTANCE.get();
			c1.setTime(dateDbf);
			c1.add(Calendar.MILLISECOND, millis);
			return new Timestamp(c1.getTimeInMillis());
		} catch (Exception e) {
			return null;
		}
	}

	public static final Date addMonth(Date dateDbf, int monthToAdd) {
		try {
			Calendar c1 = CALENDAR_INSTANCE.get();
			c1.setTime(dateDbf);
			c1.add(Calendar.MONTH, monthToAdd);
			return c1.getTime();
		} catch (Exception e) {
			return null;
		}
	}

	public static final Date addYear(Date dateDbf, int yearToAdd) {
		try {
			Calendar c1 = CALENDAR_INSTANCE.get();
			c1.setTime(dateDbf);
			c1.add(Calendar.YEAR, yearToAdd);
			return c1.getTime();
		} catch (Exception e) {
			return null;
		}
	}

	public static final int diffDays(Date start, Date end) {
		if (start == null || end == null)
			return 0;

		int diff = (int) Math.ceil((double) (start.getTime() - end.getTime())
				/ MILLISECS_PER_DAY);

		return Math.abs(diff);
	}

	/**
	 * Copia la parte oraria fra due date
	 */
	public static final Date copiaOrario(Date dsrc, Date ddst) {
		Calendar src = CALENDAR_INSTANCE.get();
		src.setTime(dsrc);
		Calendar dst = Calendar.getInstance();	//attenzione al threadlocal!!
		dst.setTime(ddst);

		dst.set(Calendar.HOUR_OF_DAY, src.get(Calendar.HOUR_OF_DAY) );
		dst.set(Calendar.MINUTE, src.get(Calendar.MINUTE) );
		dst.set(Calendar.SECOND, src.get(Calendar.SECOND) );
		dst.set(Calendar.MILLISECOND, src.get(Calendar.MILLISECOND) );

		return dst.getTime();

	}

	public static boolean isDatesOverlapped(Date startDate1, Date endDate1, Date startDate2, Date endDate2)
			throws NullPointerException {
		if ((startDate1.before(startDate2) && endDate1.after(startDate2))
				|| (startDate1.before(endDate2) && endDate1.after(endDate2))
				|| (startDate1.before(startDate2) && endDate1.after(endDate2))
				|| (startDate1.equals(startDate2) && endDate1.equals(endDate2))) {
			return true;
		}
		return false;
	}

	public static LocalDate of(int y, int m, int d) {
		return LocalDate.now().withYear(y).withMonthOfYear(m).withDayOfMonth(d);
	}

	public static LocalTime ofTime(int hh, int mm, int ss) {
		return new LocalTime(hh, mm, ss);
	}

	public static Timestamp componiData(Date date, LocalTime time, int offset) {
		LocalDateTime data = LocalDateTime.fromDateFields(date);

		data = data.withHourOfDay(time.getHourOfDay());
		data = data.withMinuteOfHour(time.getMinuteOfHour());
		data = data.withSecondOfMinute(time.getSecondOfMinute());
		if (offset != 0)
			data = data.plusSeconds(offset);

		return new Timestamp(data.toDate().getTime());
	}

	public static final void validaRangeDate(String insFrom, String insTo) throws ValidationException {

		// validazione date
		if (ValidazioneDati.isFilled(insFrom) && ValidazioneDati.validaData(insFrom) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_INIZIO);

		if (ValidazioneDati.isFilled(insTo)	&& ValidazioneDati.validaData(insTo) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_FINE);

		Timestamp from = toTimestamp(insFrom);
		Timestamp to = toTimestampA(insTo);

		if (ValidazioneDati.isFilled(insFrom) && ValidazioneDati.isFilled(insTo) )
			if (to.before(from))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

	}

	public static final Date firstDayOfYear(int year) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return new Date(c.getTimeInMillis());
	}

	public static final Date lastDayOfYear(int year) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, 31);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return new Date(c.getTimeInMillis());
	}

	public static final boolean between(Date date, Date first, Date last) {
		if (date == null || first == null || last == null)
			return false;
		return (date.compareTo(first) >= 0) && (date.compareTo(last) <= 0);
	}

	public static Timestamp withTimeAtEndOfDay(Date dt) {
		if (dt == null)
			return null;

		Calendar c = CALENDAR_INSTANCE.get();
		c.setTimeInMillis(dt.getTime());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return new Timestamp(c.getTimeInMillis());
	}

	public static Date removeTime(Date dt) {
		if (dt == null)
			return null;

		Calendar c = CALENDAR_INSTANCE.get();
		c.setTimeInMillis(dt.getTime());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTime();
	}

	public static int compareWithoutTime(Date dt1, Date dt2) {
		Date d1 = removeTime(dt1);
		Date d2 = removeTime(dt2);
		return d1.compareTo(d2);
	}

	/**
	 * almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -
	 * Controlla che le date siano formalmente corrette in termini dei primi due caratteri numerici e dei successivi due numerici
	 * o uguale al carattere punto '.';
	 * @return string che conterrà eventuale diagnostico da inviare al bibliotecario
	 */

	//Evolutiva: Spostato da almaviva2 Marzo 2017 dall'oggetto UtilityDate in modo che tale metodo sia visibile a tutto l'applicativo
	public static String verificaFormatoDataParziale(String data, String fieldName) {
		char chr;
		boolean puntoPos2 = false;
		for (int i = 0; i < 2; i++) {
			if (!Character.isDigit(data.charAt(i))) {
				return ("ERROR >>" + "I primi due caratteri devono essere numerici per " + fieldName);
			}
		}

		chr = data.charAt(2);
		if (!Character.isDigit(chr)) {
			if (chr != '.') {
				return ("ERROR >>" + "Il terzo carattere deve essere numerico o '.' per " + fieldName);
			} else {
				puntoPos2 = true;
			}
		}

		chr = data.charAt(3);
		if (!Character.isDigit(chr)) {
			if (chr != '.') {
				return ("ERROR >>" + "Il quarto carattere deve essere numerico o '.' per " + fieldName);
			}
		} else {
			if (puntoPos2 == true) {
				return ("ERROR >>" + "Il quarto carattere, '"+chr+"', deve essere preceduto da un numero per " + fieldName);
			}
		}
		return ("");
	}

	public static boolean isSameDay(Date dt1, Date dt2) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.setTime(dt1);
		int y1 = c.get(Calendar.YEAR);
		int m1 = c.get(Calendar.MONTH);
		int d1 = c.get(Calendar.DAY_OF_MONTH);

		c.setTime(dt2);
		int y2 = c.get(Calendar.YEAR);
		int m2 = c.get(Calendar.MONTH);
		int d2 = c.get(Calendar.DAY_OF_MONTH);

		return y1 == y2 && m1 == m2 && d1 == d2;
	}

}
