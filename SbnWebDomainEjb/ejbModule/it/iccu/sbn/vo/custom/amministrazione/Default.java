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
package it.iccu.sbn.vo.custom.amministrazione;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class Default extends SerializableVO {

	public enum ProvenienzaDefault {
		BIBLIOTECA,
		UTENTE;
	}

	private static final long serialVersionUID = -1229854414756644731L;

	private String key;
	private String descrizione;
	private String tipo;
	private String value;
	private ProvenienzaDefault provenienza;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ProvenienzaDefault getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(ProvenienzaDefault provenienza) {
		this.provenienza = provenienza;
	}

	public String toString() {
		return String.format("[key: '%s', prov: %s, tipo: %s, value: '%s']", key, provenienza, tipo, value);
	}


}
