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
package it.iccu.sbn.vo.custom.amministrazione.biblioteca.json;

import it.iccu.sbn.ejb.vo.SerializableVO;

import com.google.gson.annotations.SerializedName;

public class Accesso extends SerializableVO {

	private static final long serialVersionUID = -615539386783823104L;

	@SerializedName("riservato")
	private Boolean riservato;
	@SerializedName("portatori-handicap")
	private Boolean handicap;

	public Boolean getRiservato() {
		return riservato;
	}

	public void setRiservato(Boolean riservato) {
		this.riservato = riservato;
	}

	public Boolean getHandicap() {
		return handicap;
	}

	public void setHandicap(Boolean handicap) {
		this.handicap = handicap;
	}

}
