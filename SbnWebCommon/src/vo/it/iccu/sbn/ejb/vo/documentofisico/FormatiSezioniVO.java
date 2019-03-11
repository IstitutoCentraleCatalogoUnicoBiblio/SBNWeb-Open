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

public class FormatiSezioniVO extends SerializableVO implements Comparable<FormatiSezioniVO>{

	/**
	 *
	 */
	private static final long serialVersionUID = -6855378542239883369L;

	/**
	 *
	 */
	public final static String SEPARATORE = " - ";
	public final static String SEPARATOREFORSEZ = ";";

	private String codBib;
	private String codPolo;
	private String codSez;
	private String codFormato;
	private String descrFor;
	private int dimensioneDa;
	private int dimensioneA;
	private int numeroPezzi;
	private int progSerie;
	private int progNum;
	private int numeroPezziMisc;
	private int progSerieNum1Misc;
	private int dalProgrMisc;
	private int progSerieNum2Misc;
	private int alProgrMisc;
	private String dataIns;
	private String dataAgg;
	private String uteIns;
	private String uteAgg;
	private int prg;

	public FormatiSezioniVO() {
	}

	public FormatiSezioniVO(FormatiSezioniVO forSez)
			throws Exception {

		this.dataIns = forSez.dataIns;
		this.dataAgg = forSez.dataAgg;
		this.codBib = forSez.codBib;
		this.codSez = forSez.codSez;
		this.codFormato = this.codFormato.toUpperCase();
		this.descrFor = forSez.descrFor;
		this.dimensioneDa = forSez.dimensioneDa;
		this.dimensioneA = forSez.dimensioneA;
		this.numeroPezzi = forSez.numeroPezzi;
		this.progSerie = forSez.progSerie;
		this.progNum = forSez.progNum;
		this.prg = forSez.prg;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodFormato() {
		return codFormato;
	}

	public void setCodFormato(String codFormato) {
		this.codFormato = codFormato;
		if (this.codFormato != null){
			this.codFormato = this.codFormato.toUpperCase();
		}
	}

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = codSez;
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

	public int getProgNum() {
		return progNum;
	}

	public void setProgNum(int progNum) {
		this.progNum = progNum;
	}

	public int getProgSerie() {
		return progSerie;
	}

	public void setProgSerie(int progSerie) {
		this.progSerie = progSerie;
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

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public int getNumeroPezzi() {
		return numeroPezzi;
	}

	public void setNumeroPezzi(int numeroPezzi) {
		this.numeroPezzi = numeroPezzi;
	}

	public String getDescrFor() {
		return descrFor;
	}

	public void setDescrFor(String descrFor) {
		this.descrFor = descrFor;
	}
	public String getDescrizioneEstesa(){
		if (this.getCodFormato() == null){
			return "";
		}
		try {
//			return this.getCodPolo() + SEPARATORE + this.getCodBib() + SEPARATORE + this.getCodSez() + SEPARATORE + this.getCodFormato() + SEPARATORE + this.getDescrFor() + SEPARATORE + String.valueOf(numeroPezzi);
		return this.getCodFormato() + SEPARATORE + this.getDescrFor() + SEPARATORE + String.valueOf(numeroPezzi);
		} catch (Exception e ) {
			return "";
		}
	}
	public int compareTo(FormatiSezioniVO o) {
		return (this.prg - o.prg);
	}

	public int getDimensioneDa() {
		return dimensioneDa;
	}

	public void setDimensioneDa(int dimensioneDa) {
		this.dimensioneDa = dimensioneDa;
	}

	public int getDimensioneA() {
		return dimensioneA;
	}

	public void setDimensioneA(int dimensioneA) {
		this.dimensioneA = dimensioneA;
	}

	public int getNumeroPezziMisc() {
		return numeroPezziMisc;
	}

	public void setNumeroPezziMisc(int numeroPezziMisc) {
		this.numeroPezziMisc = numeroPezziMisc;
	}

	public int getDalProgrMisc() {
		return dalProgrMisc;
	}

	public void setDalProgrMisc(int dalProgrMisc) {
		this.dalProgrMisc = dalProgrMisc;
	}

	public int getAlProgrMisc() {
		return alProgrMisc;
	}

	public void setAlProgrMisc(int alProgrMisc) {
		this.alProgrMisc = alProgrMisc;
	}

	public int getProgSerieNum1Misc() {
		return progSerieNum1Misc;
	}

	public void setProgSerieNum1Misc(int progSerieNum1Misc) {
		this.progSerieNum1Misc = progSerieNum1Misc;
	}

	public int getProgSerieNum2Misc() {
		return progSerieNum2Misc;
	}

	public void setProgSerieNum2Misc(int progSerieNum2Misc) {
		this.progSerieNum2Misc = progSerieNum2Misc;
	}
}
