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

public class SerieVO extends SerializableVO implements Comparable<SerieVO>{

	/**
	 *
	 */
	private static final long serialVersionUID = 4661832072424455786L;
	/**
	 *
	 */
	private String	codPolo;
	private String	codBib;
	private String	codSerie;
	private String	descrSerie;
	private String	progAssInv;
	private String	pregrAssInv;
	private String	dataIngrPregr;
	private String	numMan;
	private String	dataIngrMan;
	private String	inizioMan1;
	private String	fineMan1;
	private String	dataIngrRis1;
	private String	inizioMan2;
	private String	fineMan2;
	private String	dataIngrRis2;
	private String	inizioMan3;
	private String	fineMan3;
	private String	dataIngrRis3;
	private String	inizioMan4;
	private String	fineMan4;
	private String	dataIngrRis4;
	private String	flChiusa;
	private String	flDefault;
	private String	buonoCarico;
	private String 	dataIns;
	private String 	dataAgg;
	private String 	uteIns;
	private String 	uteAgg;
	private int  prg;

	public SerieVO(){

	}
	public SerieVO(String codSerie)
			throws Exception {
		this.setCodSerie(codSerie);
				}

	public SerieVO(int prg, String codSerie, String progAssInv, String pregrAssInv, String numMan,
		String descrSerie)
	throws Exception {

		this.prg = prg;
		this.setCodSerie(codSerie);
		this.progAssInv = progAssInv;
		this.pregrAssInv = pregrAssInv;
		this.numMan = numMan;
		this.descrSerie = descrSerie;
	}

	public SerieVO(String codSerie, String progAssInv, String pregrAssInv, String dataIngrPregr, String numMan, String dataIngrMan,
			String descrSerie, String flChiusa, String flDefault, String inizioMan1, String fineMan1, String dataIngrRis1,
			 String inizioMan2, String fineMan2, String dataIngrRis2, String inizioMan3, String fineMan3, String dataIngrRis3,
			 String inizioMan4, String fineMan4, String dataIngrRis4, String buonoCarico, String dataAgg)
			throws Exception {

		this.setCodSerie(codSerie);
				this.progAssInv = progAssInv;
				this.dataIngrPregr = dataIngrPregr;
				this.pregrAssInv = pregrAssInv;
				this.dataIngrMan = dataIngrMan;
				this.numMan = numMan;
				this.descrSerie = descrSerie;
				this.flChiusa = flChiusa;
				this.flDefault = flDefault;
				this.inizioMan1 = inizioMan1;
				this.fineMan1 = fineMan1;
				this.dataIngrRis1 = dataIngrRis1;
				this.inizioMan2 = inizioMan1;
				this.fineMan2 = fineMan1;
				this.dataIngrRis2 = dataIngrRis1;
				this.inizioMan3 = inizioMan1;
				this.fineMan3 = fineMan1;
				this.dataIngrRis3 = dataIngrRis1;
				this.inizioMan4 = inizioMan1;
				this.fineMan4 = fineMan1;
				this.dataIngrRis4 = dataIngrRis1;
				this.buonoCarico = buonoCarico;
				this.dataAgg = dataAgg;
				}

	public SerieVO(	String codPolo, String	codBib, String	codSerie, String descrSerie, String progAssInv, String pregrAssInv, String dataIngrPregr, String numMan, String dataIngrMan,
			String flChiusa, String flDefault, String inizioMan1, String fineMan1, String dataIngrRis1,
			String inizioMan2, String fineMan2, String dataIngrRis2, String inizioMan3, String fineMan3, String dataIngrRis3,
			String inizioMan4, String fineMan4, String dataIngrRis4,
			String	buonoCarico, String dataIns, String dataAgg, String uteIns, String 	uteAgg)
			throws Exception {
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.setCodSerie(codSerie);
		this.descrSerie = descrSerie;
		this.flChiusa = flChiusa;
		this.flDefault = flDefault;
		this.progAssInv = progAssInv;
		this.pregrAssInv = pregrAssInv;
		this.dataIngrPregr = dataIngrPregr;
		this.numMan = numMan;
		this.dataIngrMan = dataIngrMan;
		this.inizioMan1 = inizioMan1;
		this.fineMan1 = fineMan1;
		this.dataIngrRis1 = dataIngrRis1;
		this.inizioMan2 = inizioMan1;
		this.fineMan2 = fineMan1;
		this.dataIngrRis2 = dataIngrRis1;
		this.inizioMan3 = inizioMan1;
		this.fineMan3 = fineMan1;
		this.dataIngrRis3 = dataIngrRis1;
		this.inizioMan4 = inizioMan1;
		this.fineMan4 = fineMan1;
		this.dataIngrRis4 = dataIngrRis1;
		this.buonoCarico = buonoCarico;
		this.dataIns = buonoCarico;
		this.dataAgg = dataAgg;
		this.uteIns = uteIns;
		this.uteAgg = uteAgg;
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
		if (this.codSerie != null){
			this.codSerie = this.codSerie.toUpperCase();
		}
	}
	public String getDescrSerie() {
		return descrSerie;
	}
	public void setDescrSerie(String descrSerie) {
		this.descrSerie = descrSerie;
	}
	public String getFlChiusa() {
		return flChiusa;
	}
	public void setFlChiusa(String flChiusa) {
		this.flChiusa = flChiusa;
	}
	public String getBuonoCarico() {
		return buonoCarico;
	}
	public void setBuonoCarico(String buonoCarico) {
		this.buonoCarico = buonoCarico;
	}
	public String getNumMan() {
		return numMan;
	}
	public void setNumMan(String numMan) {
		this.numMan = numMan;
	}
	public String getPregrAssInv() {
		return pregrAssInv;
	}
	public void setPregrAssInv(String pregrAssInv) {
		this.pregrAssInv = pregrAssInv;
	}
	public String getProgAssInv() {
		return progAssInv;
	}
	public void setProgAssInv(String progAssInv) {
		this.progAssInv = progAssInv;
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
	public String getDataIngrPregr() {
		return dataIngrPregr;
	}
	public void setDataIngrPregr(String dataIngrPregr) {
		this.dataIngrPregr = dataIngrPregr;
	}
	public String getDataIngrMan() {
		return dataIngrMan;
	}
	public void setDataIngrMan(String dataIngrMan) {
		this.dataIngrMan = dataIngrMan;
	}
	public String getInizioMan1() {
		return inizioMan1;
	}
	public void setInizioMan1(String inizioMan1) {
		this.inizioMan1 = inizioMan1;
	}
	public String getFineMan1() {
		return fineMan1;
	}
	public void setFineMan1(String fineMan1) {
		this.fineMan1 = fineMan1;
	}
	public String getDataIngrRis1() {
		return dataIngrRis1;
	}
	public void setDataIngrRis1(String dataIngrRis1) {
		this.dataIngrRis1 = dataIngrRis1;
	}
	public String getInizioMan2() {
		return inizioMan2;
	}
	public void setInizioMan2(String inizioMan2) {
		this.inizioMan2 = inizioMan2;
	}
	public String getFineMan2() {
		return fineMan2;
	}
	public void setFineMan2(String fineMan2) {
		this.fineMan2 = fineMan2;
	}
	public String getDataIngrRis2() {
		return dataIngrRis2;
	}
	public void setDataIngrRis2(String dataIngrRis2) {
		this.dataIngrRis2 = dataIngrRis2;
	}
	public String getInizioMan3() {
		return inizioMan3;
	}
	public void setInizioMan3(String inizioMan3) {
		this.inizioMan3 = inizioMan3;
	}
	public String getFineMan3() {
		return fineMan3;
	}
	public void setFineMan3(String fineMan3) {
		this.fineMan3 = fineMan3;
	}
	public String getDataIngrRis3() {
		return dataIngrRis3;
	}
	public void setDataIngrRis3(String dataIngrRis3) {
		this.dataIngrRis3 = dataIngrRis3;
	}
	public String getInizioMan4() {
		return inizioMan4;
	}
	public void setInizioMan4(String inizioMan4) {
		this.inizioMan4 = inizioMan4;
	}
	public String getFineMan4() {
		return fineMan4;
	}
	public void setFineMan4(String fineMan4) {
		this.fineMan4 = fineMan4;
	}
	public String getDataIngrRis4() {
		return dataIngrRis4;
	}
	public void setDataIngrRis4(String dataIngrRis4) {
		this.dataIngrRis4 = dataIngrRis4;
	}
	public String getFlDefault() {
		return flDefault;
	}
	public void setFlDefault(String flDefault) {
		this.flDefault = flDefault;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codBib == null) ? 0 : codBib.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime * result
				+ ((codSerie == null) ? 0 : codSerie.hashCode());
		result = prime * result
				+ ((pregrAssInv == null) ? 0 : pregrAssInv.hashCode());
		result = prime * result
				+ ((progAssInv == null) ? 0 : progAssInv.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerieVO other = (SerieVO) obj;
		if (codBib == null) {
			if (other.codBib != null)
				return false;
		} else if (!codBib.equals(other.codBib))
			return false;
		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;
		if (codSerie == null) {
			if (other.codSerie != null)
				return false;
		} else if (!codSerie.equals(other.codSerie))
			return false;
		if (pregrAssInv == null) {
			if (other.pregrAssInv != null)
				return false;
		} else if (!pregrAssInv.equals(other.pregrAssInv))
			return false;
		if (progAssInv == null) {
			if (other.progAssInv != null)
				return false;
		} else if (!progAssInv.equals(other.progAssInv))
			return false;
		return true;
	}

	public int compareTo(SerieVO o) {
		return (this.prg - o.prg);
	}
	public int getPrg() {
		return prg;
	}
	public void setPrg(int prg) {
		this.prg = prg;
	}
}
