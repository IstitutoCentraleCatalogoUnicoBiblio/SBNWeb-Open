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
package it.iccu.sbn.vo.custom.servizi.calendario;

import it.iccu.sbn.ejb.vo.servizi.calendario.FasciaVO;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class FasciaDecorator extends FasciaVO {

	private static final long serialVersionUID = -2263771483430232046L;

	private static DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
			.appendHourOfDay(2)
			.appendLiteral(':')
			.appendMinuteOfHour(2)
			.toFormatter();

	private String start;
	private String end;

	public FasciaDecorator(FasciaVO f) {
		super(f);
		start = f.getInizio().toString(FORMATTER);
		end = f.getFine().toString(FORMATTER);
	}

	public FasciaDecorator() {
		super();
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = trimAndSet(start);
		try {
			setInizio(LocalTime.parse(start, FORMATTER));
		} catch (Exception e) {
			setInizio(null);
		}
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = trimAndSet(end);
		try {
			setFine(LocalTime.parse(end, FORMATTER));
		} catch (Exception e) {
			setFine(null);
		}
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(getInizio().toString(FORMATTER)).append("-");
		buf.append(getFine().toString(FORMATTER));

		return buf.toString();
	}

}
