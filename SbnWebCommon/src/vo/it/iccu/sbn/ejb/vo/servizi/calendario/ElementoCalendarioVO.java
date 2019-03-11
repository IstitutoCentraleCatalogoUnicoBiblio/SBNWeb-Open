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
package it.iccu.sbn.ejb.vo.servizi.calendario;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Date;

import org.joda.time.LocalTime;

public class ElementoCalendarioVO extends SerializableVO implements Comparable<ElementoCalendarioVO> {

	private static final long serialVersionUID = -5797729537037318534L;

	private final int level;
	private final IntervalloVO intervallo;
	private final CalendarioVO calendario;

	private LocalTime inizio = LocalTime.MIDNIGHT;
	private LocalTime fine = LocalTime.MIDNIGHT;
	private Date data;

	private boolean active = true;

	public ElementoCalendarioVO(int level, CalendarioVO calendario, IntervalloVO intervallo) {
		super();
		this.level = level;
		this.calendario = calendario;
		this.intervallo = intervallo;
	}

	public int getLevel() {
		return level;
	}

	public IntervalloVO getIntervallo() {
		return intervallo;
	}

	public CalendarioVO getCalendario() {
		return calendario;
	}

	public LocalTime getInizio() {
		return inizio;
	}

	public void setInizio(LocalTime inizio) {
		this.inizio = inizio;
	}

	public LocalTime getFine() {
		return fine;
	}

	public void setFine(LocalTime fine) {
		this.fine = fine;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date date) {
		this.data = DateUtil.removeTime(date);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int compareTo(ElementoCalendarioVO ec2) {
		int cmp = 0;
		//cmp = cmp != 0 ? cmp : this.id - ec2.id;
		cmp = cmp != 0 ? cmp : getData().compareTo(ec2.getData());
		cmp = cmp != 0 ? cmp : getInizio().compareTo(ec2.getInizio());
		cmp = cmp != 0 ? cmp : getFine().compareTo(ec2.getFine());
		return cmp;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("ElementoCalendarioVO [level=").append(level).append(", inizio=").append(inizio).append(", fine=")
				.append(fine).append(", data=").append(data).append(", active=").append(active).append(", tipo=")
				.append(calendario.getTipo()).append("]");
		return buf.toString();
	}

}
