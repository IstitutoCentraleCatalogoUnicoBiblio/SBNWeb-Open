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

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;

import org.joda.time.LocalTime;

public class FasciaVO extends UniqueIdentifiableVO {

	public FasciaVO() {
		super();
	}

	public FasciaVO(LocalTime inizio, LocalTime fine) {
		super();
		setInizio(inizio);
		setFine(fine);
	}

	public FasciaVO(String inizio, String fine) {
		this(LocalTime.parse(inizio), LocalTime.parse(fine));
	}

	public FasciaVO(FasciaVO f) {
		this.inizio = f.inizio;
		this.fine = f.fine;
	}

	public FasciaVO(ElementoCalendarioVO ec) {
		this.inizio = ec.getInizio();
		this.fine = ec.getFine();
	}

	private static final long serialVersionUID = 549243744061714273L;

	private LocalTime inizio;
	private LocalTime fine;

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

	public int compareTo(FasciaVO f2) {
		int cmp = inizio.compareTo(f2.inizio);
		cmp = cmp != 0 ? cmp : fine.compareTo(f2.fine);
		return cmp;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(32);
		buf.append(getInizio().toString()).append("-");
		buf.append(getFine().toString());

		return buf.toString();
	}

}
