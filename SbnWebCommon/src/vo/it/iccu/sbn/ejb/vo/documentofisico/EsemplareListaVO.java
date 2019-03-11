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

public class EsemplareListaVO extends EsemplareVO implements Comparable<EsemplareListaVO> {

	private static final long serialVersionUID = -1918025546376304085L;
	private String bidDescr;
	private int prg;

	public EsemplareListaVO() {
	}

	public EsemplareListaVO(String codPolo, String codBib, String bid,
			String bidDescr, int codDoc, String consDoc, int prg)
			throws Exception {
		super(codPolo, codBib, bid, codDoc, consDoc);
		this.bidDescr = bidDescr;
		this.prg = prg;
	}

	public String getBidDescr() {
		return bidDescr;
	}

	public void setBidDescr(String bidDescr) {
		this.bidDescr = bidDescr;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public int compareTo(EsemplareListaVO o) {
		return (this.prg - o.prg);
	}
}
