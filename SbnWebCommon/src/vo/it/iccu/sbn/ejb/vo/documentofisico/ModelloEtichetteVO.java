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

public class ModelloEtichetteVO extends SerializableVO {

	private static final long serialVersionUID = -5699232952668631026L;

	private String codPolo;
	private String codBib;
	private String descrBib;
	private String codModello;
	private String descrModello;
	private String tipoModello;
	private String datiForm;
	private String uteIns;
	private String uteAgg;
	private String dataIns;
	private String dataAgg;
	private int prg;

	/**
	 *
	 */
	public ModelloEtichetteVO() {
	}

	// costruttore per modelloEtichetteLista
	public ModelloEtichetteVO(String codPolo, String codBib, int prg,
			String codModello, String descrModello) throws Exception {
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.prg = prg;
		this.codModello = codModello;
		this.descrModello = descrModello;
	}

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

	public String getCodModello() {
		return codModello;
	}

	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}

	public String getDescrModello() {
		return descrModello;
	}

	public void setDescrModello(String descrModello) {
		this.descrModello = descrModello;
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

	public String getTipoModello() {
		return tipoModello;
	}

	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}

	public String getDatiForm() {
		return datiForm;
	}

	public void setDatiForm(String datiForm) {
		this.datiForm = datiForm;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

}
