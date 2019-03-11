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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;

import com.google.gson.annotations.SerializedName;

public class CodiciIdentificativi extends SerializableVO {

	private static final long serialVersionUID = -3400082987398606775L;

	@SerializedName("isil")
	private String isil;
	@SerializedName("acnp")
	private String acnp;
	@SerializedName("cei")
	private String cei;
	@SerializedName("cmbs")
	private String cmbs;
	@SerializedName("rism")
	private String rism;
	@SerializedName("sbn")
	private String sbn;

	public String getIsil() {
		return isil;
	}

	public void setIsil(String isil) {
		this.isil = isil;
	}

	public String getAcnp() {
		return acnp;
	}

	public void setAcnp(String acnp) {
		this.acnp = acnp;
	}

	public String getCei() {
		return cei;
	}

	public void setCei(String cei) {
		this.cei = cei;
	}

	public String getCmbs() {
		return cmbs;
	}

	public void setCmbs(String cmbs) {
		this.cmbs = cmbs;
	}

	public String getRism() {
		return rism;
	}

	public void setRism(String rism) {
		this.rism = rism;
	}

	public String getSbn() {
		return sbn;
	}

	public void setSbn(String sbn) {
		this.sbn = sbn;
	}

	public boolean isSBN() {
		return length(sbn) == 5; //cod polo + bib
	}

	public String getCodPolo() {
		if (isSBN())
			return sbn.substring(0, 3);

		return null;
	}

	public String getCodBib() {
		if (isSBN())
			return ValidazioneDati.fillLeft(sbn.substring(3), ' ', 3);

		return null;
	}
}
