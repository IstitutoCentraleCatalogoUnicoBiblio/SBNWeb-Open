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
package it.iccu.sbn.polo.orm;

import java.io.Serializable;

public abstract class Tb_base implements Serializable {

	private static final long serialVersionUID = 7508740124520339604L;

	protected String ute_ins;
	protected java.sql.Timestamp ts_ins;
	protected String ute_var;
	protected java.sql.Timestamp ts_var;
	protected char fl_canc;

	public Tb_base() {
		super();
	}

	public String getUte_ins() {
		return ute_ins;
	}

	public void setUte_ins(String ute_ins) {
		this.ute_ins = ute_ins;
	}

	public java.sql.Timestamp getTs_ins() {
		return ts_ins;
	}

	public void setTs_ins(java.sql.Timestamp ts_ins) {
		this.ts_ins = ts_ins;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setUte_var(String ute_var) {
		this.ute_var = ute_var;
	}

	public java.sql.Timestamp getTs_var() {
		return ts_var;
	}

	public void setTs_var(java.sql.Timestamp ts_var) {
		this.ts_var = ts_var;
	}

	public char getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(char fl_canc) {
		this.fl_canc = fl_canc;
	}

}
