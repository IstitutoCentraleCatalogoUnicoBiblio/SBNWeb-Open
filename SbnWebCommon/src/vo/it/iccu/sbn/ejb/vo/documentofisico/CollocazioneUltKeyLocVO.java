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
package it.iccu.sbn.ejb.vo.documentofisico;

public class CollocazioneUltKeyLocVO extends CollocazioneVO implements Comparable<CollocazioneUltKeyLocVO>{

	private static final long serialVersionUID = 7835703941296634215L;

	private int prg;
	private String titolo;
	private String titoloDoc;
	private String ultCollOrd1;
	private String ultCollOrd2;
	private String flgEsemplare;

	public CollocazioneUltKeyLocVO(){
	}

	public CollocazioneUltKeyLocVO(int key_loc, int prg) {
		super();
		setKeyColloc(key_loc);
		this.prg = prg;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getUltCollOrd1() {
		return ultCollOrd1;
	}

	public void setUltCollOrd1(String ultCollOrd1) {
		this.ultCollOrd1 = ultCollOrd1;
	}

	public String getUltCollOrd2() {
		return ultCollOrd2;
	}

	public void setUltCollOrd2(String ultCollOrd2) {
		this.ultCollOrd2 = ultCollOrd2;
	}

	public String getTitoloDoc() {
		return titoloDoc;
	}

	public void setTitoloDoc(String titoloDoc) {
		this.titoloDoc = titoloDoc;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public String getFlgEsemplare() {
		return flgEsemplare;
	}

	public void setFlgEsemplare(String flgEsemplare) {
		this.flgEsemplare = flgEsemplare;
	}

	public int compareTo(CollocazioneUltKeyLocVO o) {
		return (this.prg - o.prg);
	}
}
