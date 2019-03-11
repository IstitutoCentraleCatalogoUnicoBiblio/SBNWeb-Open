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

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Denominazioni extends SerializableVO {

	private static final long serialVersionUID = -4649537706603030362L;

	@SerializedName("ufficiale")
	private String ufficiale;
	@SerializedName("precedenti")
	private List<String> precedenti;
	@SerializedName("alternative")
	private List<String> alternative;

	public String getUfficiale() {
		return ufficiale;
	}

	public void setUfficiale(String ufficiale) {
		this.ufficiale = ufficiale;
	}

	public List<String> getPrecedenti() {
		return precedenti;
	}

	public void setPrecedenti(List<String> precedenti) {
		this.precedenti = precedenti;
	}

	public List<String> getAlternative() {
		return alternative;
	}

	public void setAlternative(List<String> alternative) {
		this.alternative = alternative;
	}

}
