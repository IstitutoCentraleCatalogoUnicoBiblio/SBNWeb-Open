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
package it.iccu.sbn.polo.orm.amministrazione;

import java.io.Serializable;

public class Tbf_intranet_range implements Serializable {

	private static final long serialVersionUID = -6189404307342786917L;

	private Tbf_biblioteca_in_polo cd_bib;
	private String address;
	private short bitmask;
	private String ute_ins;
	private java.sql.Timestamp ts_ins;
	private String ute_var;
	private java.sql.Timestamp ts_var;
	private char fl_canc;

	public Tbf_biblioteca_in_polo getCd_bib() {
		return cd_bib;
	}

	public void setCd_bib(Tbf_biblioteca_in_polo cdBib) {
		cd_bib = cdBib;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public short getBitmask() {
		return bitmask;
	}

	public void setBitmask(short bitmask) {
		this.bitmask = bitmask;
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setUte_ins(String uteIns) {
		ute_ins = uteIns;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_ins(java.sql.Timestamp tsIns) {
		ts_ins = tsIns;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setUte_var(String uteVar) {
		ute_var = uteVar;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setTs_var(java.sql.Timestamp tsVar) {
		ts_var = tsVar;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(char flCanc) {
		fl_canc = flCanc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + bitmask;
		result = prime * result + ((cd_bib == null) ? 0 : cd_bib.hashCode());
		result = prime * result + fl_canc;
		result = prime * result + ((ts_ins == null) ? 0 : ts_ins.hashCode());
		result = prime * result + ((ts_var == null) ? 0 : ts_var.hashCode());
		result = prime * result	+ ((ute_ins == null) ? 0 : ute_ins.hashCode());
		result = prime * result + ((ute_var == null) ? 0 : ute_var.hashCode());
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
		Tbf_intranet_range other = (Tbf_intranet_range) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (bitmask != other.bitmask)
			return false;
		if (cd_bib == null) {
			if (other.cd_bib != null)
				return false;
		} else if (!cd_bib.equals(other.cd_bib))
			return false;
		if (fl_canc != other.fl_canc)
			return false;
		if (ts_ins == null) {
			if (other.ts_ins != null)
				return false;
		} else if (!ts_ins.equals(other.ts_ins))
			return false;
		if (ts_var == null) {
			if (other.ts_var != null)
				return false;
		} else if (!ts_var.equals(other.ts_var))
			return false;
		if (ute_ins == null) {
			if (other.ute_ins != null)
				return false;
		} else if (!ute_ins.equals(other.ute_ins))
			return false;
		if (ute_var == null) {
			if (other.ute_var != null)
				return false;
		} else if (!ute_var.equals(other.ute_var))
			return false;
		return true;
	}

}
