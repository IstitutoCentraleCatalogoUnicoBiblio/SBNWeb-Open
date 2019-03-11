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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BilancioVO  extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -1362766757278244311L;
	private Integer progressivo=0;
	private String utente;
	private String codPolo;
	private int IDBil ;
	private String codBibl;
	private String esercizio;
	private String capitolo;
	private double budgetDiCapitolo;
	private String budgetDiCapitoloStr;
	private String tipoVariazione;
	private List<BilancioDettVO> dettagliBilancio;
	private String selezioneImp="";
	private boolean flag_canc=false;
	private Timestamp dataUpd;

	public BilancioVO (){};
	public BilancioVO (String codP, String codB, String ese, String cap ,double bdgCap, String tipoVar ) throws Exception {
		this.codPolo = codP;
		this.codBibl = codB;
		this.esercizio = ese;
		this.capitolo = cap;
		this.budgetDiCapitolo = bdgCap;
		this.tipoVariazione=tipoVar;
		this.dettagliBilancio=new ArrayList() ;
	}

	public String getChiave() {
		String chiave=getCodBibl()+ "|" + getEsercizio()+ "|" +  getCapitolo() ;
		chiave=chiave.trim();
		return chiave;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public String getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}
	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getTipoVariazione() {
		return tipoVariazione;
	}
	public void setTipoVariazione(String tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}
	public double getBudgetDiCapitolo() {
		return budgetDiCapitolo;
	}
	public void setBudgetDiCapitolo(double budgetDiCapitolo) {
		this.budgetDiCapitolo = budgetDiCapitolo;
	}
	public List<BilancioDettVO> getDettagliBilancio() {
		return dettagliBilancio;
	}
	public void setDettagliBilancio(List<BilancioDettVO> dettagliBilancio) {
		this.dettagliBilancio = dettagliBilancio;
	}
	public String getSelezioneImp() {
		return selezioneImp;
	}
	public void setSelezioneImp(String selezioneImp) {
		this.selezioneImp = selezioneImp;
	}
	public Integer getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public int getIDBil() {
		return IDBil;
	}
	public void setIDBil(int bil) {
		IDBil = bil;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public String getBudgetDiCapitoloStr() {
		return budgetDiCapitoloStr;
	}
	public void setBudgetDiCapitoloStr(String budgetDiCapitoloStr) {
		this.budgetDiCapitoloStr = budgetDiCapitoloStr;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}




}
