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
package it.iccu.sbn.ejb.vo.periodici.previsionale;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;

public class GiornoMeseVO extends SerializableVO implements Comparable<GiornoMeseVO> {

	private static final long serialVersionUID = -7058088854421700063L;

	static ThreadLocal<Calendar> CALENDAR_INSTANCE = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};

	private static final Pattern giornoMese = Pattern.compile("^(0*[1-9]|[12][0-9]|3[01])[-\\/.](0*[1-9]|1[012])$");

	protected int month;
	protected int day;

	public GiornoMeseVO() {
		super();
	}

	public GiornoMeseVO(Date date) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.setTime(date);
		this.month = c.get(Calendar.MONTH);
		this.day = c.get(Calendar.DAY_OF_MONTH);
	}

	public GiornoMeseVO(int month, int day) {
		super();
		this.month = (month - 1);	//calendar is 0-based
		this.day = day;
	}

	public GiornoMeseVO(String value) {
		String[] data = value.split("[-/.]");
		//gennaio == 0
		this.month = Integer.valueOf(data[1]) - 1;
		this.day = Integer.valueOf(data[0]);
	}

	public GiornoMeseVO(LocalDate date) {
		this.month = date.getMonthOfYear() - 1;
		this.day = date.getDayOfMonth();
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + month;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GiornoMeseVO other = (GiornoMeseVO) obj;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		return true;
	}

	@Override
	public String toString() {
		//gennaio == 0
		return String.format("%02d/%02d", day, month + 1);
	}


	public int compareTo(GiornoMeseVO o) {
		int cmp = this.month - o.month;
		cmp = (cmp != 0) ? cmp : this.day - o.day;

		return cmp;
	}

	public boolean before(GiornoMeseVO o) {
		return compareTo(o) < 0;
	}

	public boolean after(GiornoMeseVO o) {
		return compareTo(o) > 0;
	}

	public static boolean matches(String value) {
		return giornoMese.matcher(value).matches();
	}

	public Date withYear(int year) {
		Calendar c = CALENDAR_INSTANCE.get();
		c.set(year, month, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

}
