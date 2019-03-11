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
package it.iccu.sbn.ejb.vo.amministrazionesistema.codici;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.lang.reflect.Field;

public class CodiceVO extends SerializableVO {

	private static final long serialVersionUID = 5599709707589163005L;

	private String nomeTabella = null;
	private String cdTabella = null;
	private String permessi = null;
	private String descrizione = null;
	private String dataAttivazione;
	private String lunghezza = null;
	private String tipo = null;

	private String materiale = null;
	private String cd_unimarc = null;
	private String cd_marc21 = null;
	private String ds_cdsbn_ulteriore = null;

	private String flag1 = null;
	private String flag2 = null;
	private String flag3 = null;
	private String flag4 = null;
	private String flag5 = null;
	private String flag6 = null;
	private String flag7 = null;
	private String flag8 = null;
	private String flag9 = null;
	private String flag10 = null;
	private String flag11 = null;
	private String[] descrFlag = new String[12];

	private boolean salva;
	private boolean nuovo = false;
	private boolean attivo;

	private String descrP;
	private String descrC;

	public boolean isSalva() {
		return salva;
	}

	public void setSalva(boolean salva) {
		this.salva = salva;
	}

	public String getNomeTabella() {
		return nomeTabella;
	}

	public void setNomeTabella(String nomeTabella) {
		this.nomeTabella = trimAndSet(nomeTabella);
	}

	public String getCdTabella() {
		return cdTabella;
	}

	public void setCdTabella(String cdTabella) {
		this.cdTabella = trimAndSet(cdTabella);
	}

	public String getPermessi() {
		return permessi;
	}

	public void setPermessi(String permessi) {
		this.permessi = trimAndSet(permessi);
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public String getLunghezza() {
		return lunghezza;
	}

	public void setLunghezza(String lunghezza) {
		this.lunghezza = trimAndSet(lunghezza);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = trimAndSet(tipo);
	}

	public String getDataAttivazione() {
		return dataAttivazione;
	}

	public void setDataAttivazione(String dataAttivazione) {
		this.dataAttivazione = trimAndSet(dataAttivazione);
	}

	public String getMateriale() {
		return materiale;
	}

	public void setMateriale(String materiale) {
		this.materiale = materiale;
	}

	public String getCd_unimarc() {
		return cd_unimarc;
	}

	public void setCd_unimarc(String cd_unimarc) {
		this.cd_unimarc = trimAndSet(cd_unimarc);
	}

	public String getCd_marc21() {
		return cd_marc21;
	}

	public void setCd_marc21(String cd_marc21) {
		this.cd_marc21 = trimAndSet(cd_marc21);
	}

	public String getDs_cdsbn_ulteriore() {
		return ds_cdsbn_ulteriore;
	}

	public void setDs_cdsbn_ulteriore(String ds_cdsbn_ulteriore) {
		this.ds_cdsbn_ulteriore = trimAndSet(ds_cdsbn_ulteriore);
	}

	public String getFlag4() {
		return flag4;
	}

	public void setFlag4(String flag4) {
		this.flag4 = trimAndSet(flag4);
	}

	public String getFlag5() {
		return flag5;
	}

	public void setFlag5(String flag5) {
		this.flag5 = trimAndSet(flag5);
	}

	public String getFlag6() {
		return flag6;
	}

	public void setFlag6(String flag6) {
		this.flag6 = trimAndSet(flag6);
	}

	public String getFlag7() {
		return flag7;
	}

	public void setFlag7(String flag7) {
		this.flag7 = trimAndSet(flag7);
	}

	public String getFlag8() {
		return flag8;
	}

	public void setFlag8(String flag8) {
		this.flag8 = trimAndSet(flag8);
	}

	public String getFlag9() {
		return flag9;
	}

	public void setFlag9(String flag9) {
		this.flag9 = trimAndSet(flag9);
	}

	public String getFlag10() {
		return flag10;
	}

	public void setFlag10(String flag10) {
		this.flag10 = trimAndSet(flag10);
	}

	public String getFlag11() {
		return flag11;
	}

	public void setFlag11(String flag11) {
		this.flag11 = trimAndSet(flag11);
	}

	public String getFlag2() {
		return flag2;
	}

	public void setFlag2(String flag2) {
		this.flag2 = trimAndSet(flag2);
	}

	public String getFlag3() {
		return flag3;
	}

	public void setFlag3(String flag3) {
		this.flag3 = trimAndSet(flag3);
	}

	public String getFlag(int idx) {
		try {
			Field f = this.getClass().getDeclaredField("flag" + idx);
			return (String) f.get(this);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isNuovo() {
		return nuovo;
	}

	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}

	public String getFlag1() {
		return flag1;
	}

	public void setFlag1(String flag1) {
		this.flag1 = flag1;
	}

	public String getDescrP() {
		return descrP;
	}

	public void setDescrP(String descrP) {
		this.descrP = descrP;
	}

	public String getDescrC() {
		return descrC;
	}

	public void setDescrC(String descrC) {
		this.descrC = descrC;
	}

	public void setDescrizioneFlag(int idx, String value) {
		descrFlag[idx] = value;
	}

	public String getDescrizioneFlag(int idx) {
		return descrFlag[idx];
	}

}
