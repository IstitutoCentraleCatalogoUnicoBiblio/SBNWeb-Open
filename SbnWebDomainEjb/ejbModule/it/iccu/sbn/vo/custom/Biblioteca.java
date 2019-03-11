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
package it.iccu.sbn.vo.custom;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class Biblioteca extends SerializableVO {

	private static final long serialVersionUID = -8071583826998844022L;

	private String cd_biblioteca;
	private String ute_var;

	public Biblioteca() {
		super();
	}

	public String getCd_biblioteca() {
		return cd_biblioteca;
	}

	public void setCd_biblioteca(String cd_biblioteca) {
		this.cd_biblioteca = cd_biblioteca;
	}

	public String getUte_var() {
		return ute_var;
	}

	public void setUte_var(String ute_var) {
		this.ute_var = ute_var;
	}

}
