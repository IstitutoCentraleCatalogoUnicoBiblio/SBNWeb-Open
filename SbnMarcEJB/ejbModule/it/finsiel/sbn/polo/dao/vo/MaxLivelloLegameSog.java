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
package it.finsiel.sbn.polo.dao.vo;

import java.io.Serializable;

public class MaxLivelloLegameSog implements Serializable {

	private static final long serialVersionUID = -1416775078940076267L;

	private String bid;
	private String cd_sogg;
	private String ute_var;
	private String maxLivAut;

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getCd_sogg() {
		return cd_sogg;
	}

	public void setCd_sogg(String cd_sogg) {
		this.cd_sogg = cd_sogg;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setUte_var(String ute_var) {
		this.ute_var = ute_var;
	}

	public String getMaxLivAut() {
		return maxLivAut;
	}

	public void setMaxLivAut(String maxLivAut) {
		this.maxLivAut = maxLivAut;
	}

}
