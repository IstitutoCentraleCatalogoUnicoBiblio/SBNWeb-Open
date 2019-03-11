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

public class CollocazioniUltCollSpecVO extends CollocazioneVO implements Comparable<CollocazioniUltCollSpecVO>{

	/**
	 *
	 */
	private static final long serialVersionUID = 4968842534740260309L;
	private int prg;
	private Integer numOccorrenze;
	private Integer totInventari;

	public CollocazioniUltCollSpecVO(){
	}
	public CollocazioniUltCollSpecVO(int keyColloc)
	throws Exception {
		super(keyColloc);
	}
	public CollocazioniUltCollSpecVO(int prg, String codLoc, String specLoc, String ordLoc, Integer numOccorrenze, Integer totInventari)
	throws Exception {
		super(codLoc, specLoc, ordLoc);
		this.prg = prg;
		this.numOccorrenze = numOccorrenze;
		this.totInventari = totInventari;
		}

	public Integer getNumOccorrenze() {
		return numOccorrenze;
	}
	public void setNumOccorrenze(Integer numOccorrenze) {
		this.numOccorrenze = numOccorrenze;
	}
	public Integer getTotInventari() {
		return totInventari;
	}
	public void setTotInventari(Integer totInventari) {
		this.totInventari = totInventari;
	}
	public int getPrg() {
		return prg;
	}
	public void setPrg(int prg) {
		this.prg = prg;
	}
	public int compareTo(CollocazioniUltCollSpecVO o) {
		return (this.prg - o.prg);
	}

}
