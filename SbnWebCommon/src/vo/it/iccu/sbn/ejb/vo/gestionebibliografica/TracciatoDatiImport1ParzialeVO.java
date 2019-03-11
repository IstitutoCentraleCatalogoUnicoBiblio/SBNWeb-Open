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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class TracciatoDatiImport1ParzialeVO extends SerializableVO {

	private static final long serialVersionUID = 6894606913746818094L;

	private String id;
	private String idInput;
	private String tag;
	private char indicatore1;
	private char indicatore2;
	private String idLink;

	// esempio di leader "00667 n am0# 2 2 00241 ### 450#"
	private String tipoRecord; // deriva dal settimo carattere del campo leader
								// della tabella import1
	private String natura; // deriva dall'ottavo carattere del campo leader
							// della tabella import1
	private String level; // deriva dal nono carattere del campo leader della
							// tabella import1
	private String dati;
	private String dati210; // viene utilizzato solo dalle collane e contiene
							// l'area 210 della M a cui sono collegate
	private String titoloFormattato;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getIdLink() {
		return idLink;
	}

	public void setIdLink(String idLink) {
		this.idLink = idLink;
	}

	public String getDati() {
		return dati;
	}

	public void setDati(String dati) {
		this.dati = dati;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public char getIndicatore1() {
		return indicatore1;
	}

	public void setIndicatore1(char indicatore1) {
		this.indicatore1 = indicatore1;
	}

	public char getIndicatore2() {
		return indicatore2;
	}

	public void setIndicatore2(char indicatore2) {
		this.indicatore2 = indicatore2;
	}

	public String getIdInput() {
		return idInput;
	}

	public void setIdInput(String idInput) {
		this.idInput = idInput;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getTitoloFormattato() {
		return titoloFormattato;
	}

	public void setTitoloFormattato(String titoloFormattato) {
		this.titoloFormattato = titoloFormattato;
	}

	public String getDati210() {
		return dati210;
	}

	public void setDati210(String dati210) {
		this.dati210 = dati210;
	}

}
