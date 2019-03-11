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

public class Indirizzo extends SerializableVO {

	private static final long serialVersionUID = -2544196758279729742L;

	@SerializedName("via-piazza")
	private String indirizzo;
	@SerializedName("frazione")
	private String frazione;
	@SerializedName("cap")
	private String cap;

	@SerializedName("regione")
	private String regione;

	@SerializedName("comune")
	private ProvComune comune;
	@SerializedName("provincia")
	private ProvComune provincia;

	@SerializedName("coordinate")
	private List<Double> coordinate;

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getFrazione() {
		return frazione;
	}

	public void setFrazione(String frazione) {
		this.frazione = frazione;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public ProvComune getComune() {
		return comune;
	}

	public void setComune(ProvComune comune) {
		this.comune = comune;
	}

	public ProvComune getProvincia() {
		return provincia;
	}

	public void setProvincia(ProvComune provincia) {
		this.provincia = provincia;
	}

	public List<Double> getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(List<Double> coordinate) {
		this.coordinate = coordinate;
	}

	public String getLat() {
		try {
			return coordinate.get(1).toString();
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	public String getLong() {
		try {
			return coordinate.get(0).toString();
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}
}
