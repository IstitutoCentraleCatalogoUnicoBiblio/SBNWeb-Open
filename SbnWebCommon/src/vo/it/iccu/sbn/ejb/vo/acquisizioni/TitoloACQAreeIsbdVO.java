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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class TitoloACQAreeIsbdVO extends SerializableVO {

	private static final long serialVersionUID = -545304218247755749L;

	private String bid;
	private String isbd;
	private String indiceISBD;
	private String area200Titolo;
	private String area205Edizione;
	private String area208Musica;
	private String area207Numerazione;
	private String area206DatiMat;
	private String area210Pubblicazione;
	private String area215DescrFisica;
	private String area300Note;
	private String collezione;

	public TitoloACQAreeIsbdVO() {
		super();
	}

	public String getArea200Titolo() {
		return area200Titolo;
	}

	public void setArea200Titolo(String area200Titolo) {
		this.area200Titolo = area200Titolo;
	}

	public String getArea205Edizione() {
		return area205Edizione;
	}

	public void setArea205Edizione(String area205Edizione) {
		this.area205Edizione = area205Edizione;
	}

	public String getArea206DatiMat() {
		return area206DatiMat;
	}

	public void setArea206DatiMat(String area206DatiMat) {
		this.area206DatiMat = area206DatiMat;
	}

	public String getArea207Numerazione() {
		return area207Numerazione;
	}

	public void setArea207Numerazione(String area207Numerazione) {
		this.area207Numerazione = area207Numerazione;
	}

	public String getArea208Musica() {
		return area208Musica;
	}

	public void setArea208Musica(String area208Musica) {
		this.area208Musica = area208Musica;
	}

	public String getArea210Pubblicazione() {
		return area210Pubblicazione;
	}

	public void setArea210Pubblicazione(String area210Pubblicazione) {
		this.area210Pubblicazione = area210Pubblicazione;
	}

	public String getArea215DescrFisica() {
		return area215DescrFisica;
	}

	public void setArea215DescrFisica(String area215DescrFisica) {
		this.area215DescrFisica = area215DescrFisica;
	}

	public String getArea300Note() {
		return area300Note;
	}

	public void setArea300Note(String area300Note) {
		this.area300Note = area300Note;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getCollezione() {
		return collezione;
	}

	public void setCollezione(String collezione) {
		this.collezione = collezione;
	}

	public String getIndiceISBD() {
		return indiceISBD;
	}

	public void setIndiceISBD(String indiceISBD) {
		this.indiceISBD = indiceISBD;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}

}
