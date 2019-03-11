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

public class NotaInventarioVO extends SerializableVO {

	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getCodSerie() {
		return codSerie;
	}
	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}
	public NotaInventarioVO() {
	}
	public int getPrg() {
		return prg;
	}
	public void setPrg(int prg) {
		this.prg = prg;
	}
	public NotaInventarioVO(String codPolo, String codBib, String codSerie,
			int codInvent, String codNota, String descrNota, String dataIns,
			String dataAgg, String uteIns, String uteAgg, int prg) {
		super();
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.codSerie = codSerie;
		this.codInvent = codInvent;
		this.codNota = codNota;
		this.descrNota = descrNota;
		this.dataIns = dataIns;
		this.dataAgg = dataAgg;
		this.uteIns = uteIns;
		this.uteAgg = uteAgg;
		this.prg = prg;
	}
	public int getCodInvent() {
		return codInvent;
	}
	public void setCodInvent(int codInvent) {
		this.codInvent = codInvent;
	}
	public String getCodNota() {
		return codNota;
	}
	public void setCodNota(String codNota) {
		this.codNota = codNota;
	}
	public String getDescrNota() {
		return descrNota;
	}
	public void setDescrNota(String descrNota) {
		this.descrNota = descrNota;
	}
	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public String getDataAgg() {
		return dataAgg;
	}
	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}
	public String getUteIns() {
		return uteIns;
	}
	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}
	public String getUteAgg() {
		return uteAgg;
	}
	public void setUteAgg(String uteAgg) {
		this.uteAgg = uteAgg;
	}
	/**
	 *
	 */
	private static final long serialVersionUID = 2287467859079016764L;
	private String codPolo;
	private String codBib;
	private String codSerie;
	private int codInvent;
	private String codNota;
	private String descrNota;
	private String dataIns;
	private String dataAgg;
	private String uteIns;
	private String uteAgg;
	private int prg;
}
