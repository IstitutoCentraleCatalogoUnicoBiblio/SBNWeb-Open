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

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BaseVO extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 7185300312629580117L;

	public static final Comparator<BaseVO> ORDINAMENTO_PER_PROGRESSIVO =
		new Comparator<BaseVO>() {
		public int compare(BaseVO o1, BaseVO o2) {
			return o1.progr - o2.progr;
		}
	};

	protected int progr;
	protected Timestamp tsIns;
	protected String uteIns;
	protected Timestamp tsVar;
	protected String uteVar;
	protected String flCanc;

	public BaseVO() {
		super();
	}

	public BaseVO(BaseVO base) {
		super();
		this.progr = base.progr;
		this.tsIns = base.tsIns;
		this.tsVar = base.tsVar;
		this.uteIns = base.uteIns;
		this.uteVar = base.uteVar;
		this.flCanc = base.flCanc;
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public String getFlCanc() {
		return flCanc;
	}

	public void setFlCanc(String flCanc) {
		this.flCanc = flCanc;
	}

	public Timestamp getTsIns() {
		return tsIns;
	}

	public void setTsIns(Timestamp tsIns) {
		this.tsIns = tsIns;
	}

	public Timestamp getTsVar() {
		return tsVar;
	}

	public void setTsVar(Timestamp tsVar) {
		this.tsVar = tsVar;
	}

	public String getUteIns() {
		return uteIns;
	}

	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}

	public String getUteVar() {
		return uteVar;
	}

	public void setUteVar(String uteVar) {
		this.uteVar = uteVar;
	}

	public boolean isNuovo() {
		return true;
	}

	public boolean isCancellato() {
		return ValidazioneDati.in(flCanc, "s", "S");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flCanc == null) ? 0 : flCanc.hashCode());
		result = prime * result + progr;
		result = prime * result + ((tsIns == null) ? 0 : tsIns.hashCode());
		result = prime * result + ((tsVar == null) ? 0 : tsVar.hashCode());
		result = prime * result + ((uteIns == null) ? 0 : uteIns.hashCode());
		result = prime * result + ((uteVar == null) ? 0 : uteVar.hashCode());
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
		BaseVO other = (BaseVO) obj;
		if (flCanc == null) {
			if (other.flCanc != null)
				return false;
		} else if (!flCanc.equals(other.flCanc))
			return false;
		//if (progr != other.progr)
		//	return false;
		if (tsIns == null) {
			if (other.tsIns != null)
				return false;
		} else if (!tsIns.equals(other.tsIns))
			return false;
		if (tsVar == null) {
			if (other.tsVar != null)
				return false;
		} else if (!tsVar.equals(other.tsVar))
			return false;
		if (uteIns == null) {
			if (other.uteIns != null)
				return false;
		} else if (!uteIns.equals(other.uteIns))
			return false;
		if (uteVar == null) {
			if (other.uteVar != null)
				return false;
		} else if (!uteVar.equals(other.uteVar))
			return false;
		return true;
	}

	public static final <T extends BaseVO> void sortAndEnumerate(List<T> output, Comparator<T> c) {
		if (!isFilled(output) )
			return;

		if (c != null)
			Collections.sort(output, c);

		int prg = 0;
		for (BaseVO o : output)
			o.setProgr(++prg);
	}

}
