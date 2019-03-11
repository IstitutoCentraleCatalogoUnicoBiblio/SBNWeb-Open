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

public class CollocazioneReticoloVO extends CollocazioneVO implements Comparable<CollocazioneReticoloVO> {

	private static final long serialVersionUID = 3644171865005673110L;

	private int prg;
	private String descrBid;
	private String flgEsemplare;

	public CollocazioneReticoloVO(){
	}

	public CollocazioneReticoloVO(String dataIns, String dataAgg, String codBib, String bid, String codColloc, String specColloc, int totInv,
			String consistenza, String ord1Colloc, String ord2Colloc, int keyColloc, String bidDoc,
			int codDoc, String codSez, String descrBid, int prg, String flgEsemplare)
	throws Exception {

		super(dataIns, dataAgg, codBib, bid, codColloc, specColloc, totInv,
				consistenza, ord1Colloc, ord2Colloc, keyColloc, bidDoc,
				codDoc, codSez);
		this.flgEsemplare = flgEsemplare;
		this.descrBid = descrBid;
		this.prg = prg;
		}

	public String getDescrBid() {
		return descrBid;
	}

	public void setDescrBid(String descrBid) {
		this.descrBid = descrBid;
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

	public int compareTo(CollocazioneReticoloVO o) {
		return (this.prg - o.prg);
	}

}
