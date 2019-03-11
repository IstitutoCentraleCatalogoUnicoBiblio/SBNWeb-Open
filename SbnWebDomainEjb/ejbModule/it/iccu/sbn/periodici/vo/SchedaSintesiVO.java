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
package it.iccu.sbn.periodici.vo;


import java.io.Serializable;

public class SchedaSintesiVO implements Serializable {

/**
	 *
	 */
	private static final long serialVersionUID = 5492349771950792992L;
	// Sintesi
	private String kbibl="";
	private String ksint="";
	private String segnatura="";
	private String descr="";

	public SchedaSintesiVO (){}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getKbibl() {
		return kbibl;
	}

	public void setKbibl(String kbibl) {
		this.kbibl = kbibl;
	}

	public String getKsint() {
		return ksint;
	}

	public void setKsint(String ksint) {
		this.ksint = ksint;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}

}
