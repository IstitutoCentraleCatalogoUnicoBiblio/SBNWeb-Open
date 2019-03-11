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

public class SezioneCollocazioneVO extends SerializableVO implements Comparable<SezioneCollocazioneVO> {

	private static final long serialVersionUID = -4866119452283447237L;

	public static final String SEZRECUP = "R";

	private String codSezione;
	private String codBib;
	private String codPolo;
	private String noteSezione;
	private int inventariCollocati;
	private String descrSezione;
	private String tipoColloc;
	private String tipoSezione;
	private String classific;
	private int inventariPrevisti;
	private String uteIns;
	private String dataIns;
	private String uteAgg;
	private String dataAgg;
	// progressivo per assegnazione numero per tipo colloc = N
	private int progNum;
	// flag recupero nome sezione dal db
	private String flRecSez = "";
	private int prg;
	private String descrTipoSez;
	private String descrTipoColl;

	private boolean sezioneSpazio;

	public SezioneCollocazioneVO() {
	}

	public SezioneCollocazioneVO(String dataIns, String dataAgg, String codBib, String codSezione, String noteSezione,
			int inventariCollocati, String descrSezione, String tipoColloc, String tipoSezione, String classific,
			int inventariPrevisti, int progNum, String descrTipoColloc, String descrTipoSezione) throws Exception {

		this.dataIns = dataIns;
		this.dataAgg = dataAgg;
		this.codBib = codBib;
		this.setCodSezione(codSezione);
		this.noteSezione = noteSezione;
		this.inventariCollocati = inventariCollocati;
		this.descrSezione = descrSezione;
		this.tipoColloc = tipoColloc;
		this.tipoSezione = tipoSezione;
		this.classific = classific;
		this.inventariPrevisti = inventariPrevisti;
		this.progNum = progNum;
		this.descrTipoColl = descrTipoColloc;
		this.descrTipoSez = descrTipoSezione;
	}

	// lista sezioni
	public SezioneCollocazioneVO(int prg, String codSezione, String descrSezione, int inventariCollocati,
			String descrTipoColloc, String descrTipoSezione) throws Exception {

		this.prg = prg;
		this.setCodSezione(codSezione);
		this.inventariCollocati = inventariCollocati;
		this.descrSezione = descrSezione;
		this.descrTipoColl = descrTipoColloc;
		this.descrTipoSez = descrTipoSezione;
	}

	public String getClassific() {
		return classific;
	}

	public void setClassific(String classific) {
		this.classific = classific;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodSezione() {
		return codSezione;
	}

	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
		if (this.codSezione != null) {
			this.codSezione = this.codSezione.toUpperCase();
		}
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

	public String getDescrSezione() {
		return descrSezione;
	}

	public void setDescrSezione(String descrSezione) {
		this.descrSezione = descrSezione;
	}

	public int getInventariCollocati() {
		return inventariCollocati;
	}

	public void setInventariCollocati(int inventariCollocati) {
		this.inventariCollocati = inventariCollocati;
	}

	public int getInventariPrevisti() {
		return inventariPrevisti;
	}

	public void setInventariPrevisti(int inventariPrevisti) {
		this.inventariPrevisti = inventariPrevisti;
	}

	public String getNoteSezione() {
		return noteSezione;
	}

	public void setNoteSezione(String noteSezione) {
		this.noteSezione = noteSezione;
	}

	public int getProgNum() {
		return progNum;
	}

	public void setProgNum(int progNum) {
		this.progNum = progNum;
	}

	public String getTipoColloc() {
		return tipoColloc;
	}

	public void setTipoColloc(String tipoColloc) {
		this.tipoColloc = tipoColloc;
	}

	public String getTipoSezione() {
		return tipoSezione;
	}

	public void setTipoSezione(String tipoSezione) {
		this.tipoSezione = tipoSezione;
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

	public String getFlRecSez() {
		return flRecSez;
	}

	public void setFlRecSez(String flRecSez) {
		this.flRecSez = flRecSez;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public String getDescrTipoColl() {
		return descrTipoColl;
	}

	public void setDescrTipoColl(String descrTipoColl) {
		this.descrTipoColl = descrTipoColl;
	}

	public String getDescrTipoSez() {
		return descrTipoSez;
	}

	public void setDescrTipoSez(String descrTipoSez) {
		this.descrTipoSez = descrTipoSez;
	}

	public boolean isSezioneSpazio() {
		return sezioneSpazio;
	}

	public void setSezioneSpazio(boolean sezioneSpazio) {
		this.sezioneSpazio = sezioneSpazio;
	}

	public int compareTo(SezioneCollocazioneVO o) {
		return (this.prg - o.prg);
	}
}
