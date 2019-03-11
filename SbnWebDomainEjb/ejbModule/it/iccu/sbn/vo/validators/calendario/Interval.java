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
package it.iccu.sbn.vo.validators.calendario;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.servizi.calendario.IntervalloVO;

import java.sql.Timestamp;
import java.util.Date;

public class Interval {

	private long msStart;
	private long msEnd;

	private Date start;
	private Date end;

	private long duration;

	public static Interval of(Date start, Date end) {
		return new Interval(start, end);
	}

	public static Interval of(IntervalloVO i) {
		return new Interval(i.getInizio(), i.getFine());
	}

	public static Interval of(Timestamp start, Timestamp end) {
		return new Interval(start, end);
	}

	protected Interval(Date start, Date end) {
		this.start = DateUtil.removeTime(start);
		this.end = DateUtil.withTimeAtEndOfDay(end);

		init();
	}

	protected Interval(Timestamp start, Timestamp end) {
		this.start = start;
		this.end = end;

		init();
	}

	private void init() {
		this.msStart = this.start.getTime();
		this.msEnd = this.end.getTime();
		this.duration = this.msEnd - this.msStart;
	}

	public boolean contains(Interval i) {
		boolean contains = (this.msStart <= i.msStart) && (this.msEnd >= i.msEnd);
		return contains;
	}

	public boolean contains(Date dt) {
		long msTarget = dt.getTime();
		boolean contains = (this.msStart <= msTarget) && (this.msEnd >= msTarget);
		return contains;
	}

	public boolean overlaps(Interval i) {
//		boolean overlapped1 = (this.msStart <= i.msStart && (this.msEnd >= i.msStart && this.msEnd <= i.msEnd) );
//		boolean overlapped2 = (this.msStart >= i.msStart && this.msStart <= i.msEnd) && (this.msEnd >= i.msEnd);
//
//		return overlapped1 || overlapped2;

        long thisStart = this.msStart;
        long thisEnd = this.msEnd;
        long otherStart = i.msStart;
        long otherEnd = i.msEnd;
        return (thisStart < otherEnd && otherStart < thisEnd);
	}


	public long getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Interval [").append(start).append("<-->").append(end).append("]");
		return buf.toString();
	}

}
