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

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ProvenienzaInventarioVO extends SerializableVO implements Comparable<ProvenienzaInventarioVO>{

	/**
	 *
	 */
	private static final long serialVersionUID = -5591702320861179242L;
	private String 	codPolo;
	private String	codBib;
	private String	codProvenienza;
	private String	descrProvenienza;
	private String 	dataIns;
	private String 	dataAgg;
	private String 	uteIns;
	private String 	uteAgg;
	private int 	prg;

	public ProvenienzaInventarioVO(){

	}

	//costruttore lista Proven
	public ProvenienzaInventarioVO(int prg, String codProvenienza, String descr, String dataIns, String dataAgg)
	throws Exception {
		this.prg = prg;
		this.codProvenienza = codProvenienza;
		this.descrProvenienza = descr;
		this.dataIns = dataIns;
		this.dataAgg = dataAgg;

	}
	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodProvenienza() {
		return codProvenienza;
	}

	public void setCodProvenienza(String codProvenienza) {
		this.codProvenienza = codProvenienza;
	}

	public String getDataAgg() {
		return dataAgg;
	}

	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getDescrProvenienza() {
		return descrProvenienza;
	}

	public void setDescrProvenienza(String descrProvenienza) {
		this.descrProvenienza = trimAndSet(descrProvenienza);
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getUteAgg() {
		return uteAgg;
	}

	public void setUteAgg(String uteAgg) {
		this.uteAgg = uteAgg;
	}

	public String getUteIns() {
		return uteIns;
	}

	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}

	public int compareTo(ProvenienzaInventarioVO o) {
		return (this.prg - o.prg);
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}
}
