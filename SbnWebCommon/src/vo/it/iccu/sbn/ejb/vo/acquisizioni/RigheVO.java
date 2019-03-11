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

public class RigheVO extends SerializableVO {

	private static final long serialVersionUID = -4785033706661889184L;

	private String esercizio;
	private String esercizioDescr; // utilizzato nel report come intestazione
	private String capitolo;
	private String capitoloDescr; // utilizzato nel report come intestazione
	private String impegno;
	private String impegnoDescr; // utilizzato nel report come intestazione
	private String tipologia;
	private double impegnato;
	private double acquisito;
	private double valoreInventariale;
	private String valoreInventarialeStr;
	private String impegnatoStr;
	private String acquisitoStr;
	private Integer annoOrdine;
	private String annoOrdineDescr;
	private String codPolo;
	private String codBibl;
	private String denoBibl;
	private String dataOrdineDa;
	private String dataOrdineA;
	private String tipoOrdineCercato;
	private String naturaOrdine;
	private String codFornitore; // utilizzato nel report come descrizione
	private String fornitore; // utilizzato nel report come descrizione
	private String fornitoreDescr; // utilizzato nel report come intestazione
	private String sezione; // utilizzato nel report come descrizione di riga
	private String sezioneDescr; // utilizzato nel report come intestazione
	private String tipoMaterialeInv;
	private String tipoMaterialeInvDescr;
	private String supporto;
	private String supportoDescr;
	private String tipoRecord;
	private String tipoRecordDescr;
	private String lingua;
	private String linguaDescr;
	private String paese;
	private String paeseDescr;

	private int IDOrd;
	private String annoOrd;
	private String tipoOrdine;
	private String codOrdine;
	private String dataOrdine;
	private String stampato;
	private String statoOrdine;
	private String bid;
	private String titolo;
	private String natura;
	private String continuativo;
	private Boolean ordNOinv = false;
	private String rangeDewey = "";

	public RigheVO() {
		this.esercizio = "";
		this.capitolo = "";
		this.impegno = "";
		this.tipologia = "";
		this.impegnato = 0;
		this.acquisito = 0;
		this.valoreInventariale = 0;
		this.valoreInventarialeStr = "0,00";
		this.impegnatoStr = "0,00";
		this.acquisitoStr = "0,00";
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

	public double getImpegnato() {
		return impegnato;
	}

	public void setImpegnato(double impegnato) {
		this.impegnato = impegnato;
	}

	public String getImpegno() {
		return impegno;
	}

	public void setImpegno(String impegno) {
		this.impegno = impegno;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getImpegnatoStr() {
		return impegnatoStr;
	}

	public void setImpegnatoStr(String impegnatoStr) {
		this.impegnatoStr = impegnatoStr;
	}

	public double getAcquisito() {
		return acquisito;
	}

	public void setAcquisito(double acquisito) {
		this.acquisito = acquisito;
	}

	public String getAcquisitoStr() {
		return acquisitoStr;
	}

	public void setAcquisitoStr(String acquisitoStr) {
		this.acquisitoStr = acquisitoStr;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getDataOrdineA() {
		return dataOrdineA;
	}

	public void setDataOrdineA(String dataOrdineA) {
		this.dataOrdineA = dataOrdineA;
	}

	public String getDataOrdineDa() {
		return dataOrdineDa;
	}

	public void setDataOrdineDa(String dataOrdineDa) {
		this.dataOrdineDa = dataOrdineDa;
	}

	public String getDenoBibl() {
		return denoBibl;
	}

	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}

	public String getTipoOrdineCercato() {
		return tipoOrdineCercato;
	}

	public void setTipoOrdineCercato(String tipoOrdineCercato) {
		this.tipoOrdineCercato = tipoOrdineCercato;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getAnnoOrdineStr() {
		return new Integer(annoOrdine).toString();
	}

	public Integer getAnnoOrdine() {
		return annoOrdine;
	}

	public void setAnnoOrdine(Integer annoOrdine) {
		this.annoOrdine = annoOrdine;
	}

	public double getValoreInventariale() {
		return valoreInventariale;
	}

	public void setValoreInventariale(double valoreInventariale) {
		this.valoreInventariale = valoreInventariale;
	}

	public String getValoreInventarialeStr() {
		return valoreInventarialeStr;
	}

	public void setValoreInventarialeStr(String valoreInventarialeStr) {
		this.valoreInventarialeStr = valoreInventarialeStr;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public String getNaturaOrdine() {
		return naturaOrdine;
	}

	public void setNaturaOrdine(String naturaOrdine) {
		this.naturaOrdine = naturaOrdine;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getSupporto() {
		return supporto;
	}

	public void setSupporto(String supporto) {
		this.supporto = supporto;
	}

	public String getSupportoDescr() {
		return supportoDescr;
	}

	public void setSupportoDescr(String supportoDescr) {
		this.supportoDescr = supportoDescr;
	}

	public String getTipoMaterialeInv() {
		return tipoMaterialeInv;
	}

	public void setTipoMaterialeInv(String tipoMaterialeInv) {
		this.tipoMaterialeInv = tipoMaterialeInv;
	}

	public String getTipoMaterialeInvDescr() {
		return tipoMaterialeInvDescr;
	}

	public void setTipoMaterialeInvDescr(String tipoMaterialeInvDescr) {
		this.tipoMaterialeInvDescr = tipoMaterialeInvDescr;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getTipoRecordDescr() {
		return tipoRecordDescr;
	}

	public void setTipoRecordDescr(String tipoRecordDescr) {
		this.tipoRecordDescr = tipoRecordDescr;
	}

	public String getSezioneDescr() {
		return sezioneDescr;
	}

	public void setSezioneDescr(String sezioneDescr) {
		this.sezioneDescr = sezioneDescr;
	}

	public String getCapitoloDescr() {
		return capitoloDescr;
	}

	public void setCapitoloDescr(String capitoloDescr) {
		this.capitoloDescr = capitoloDescr;
	}

	public String getFornitoreDescr() {
		return fornitoreDescr;
	}

	public void setFornitoreDescr(String fornitoreDescr) {
		this.fornitoreDescr = fornitoreDescr;
	}

	public String getImpegnoDescr() {
		return impegnoDescr;
	}

	public void setImpegnoDescr(String impegnoDescr) {
		this.impegnoDescr = impegnoDescr;
	}

	public String getEsercizioDescr() {
		return esercizioDescr;
	}

	public void setEsercizioDescr(String esercizioDescr) {
		this.esercizioDescr = esercizioDescr;
	}

	public String getAnnoOrdineDescr() {
		return annoOrdineDescr;
	}

	public void setAnnoOrdineDescr(String annoOrdineDescr) {
		this.annoOrdineDescr = annoOrdineDescr;
	}

	public String getAnnoOrd() {
		return annoOrd;
	}

	public void setAnnoOrd(String annoOrd) {
		this.annoOrd = annoOrd;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getCodOrdine() {
		return codOrdine;
	}

	public void setCodOrdine(String codOrdine) {
		this.codOrdine = codOrdine;
	}

	public String getContinuativo() {
		return continuativo;
	}

	public void setContinuativo(String continuativo) {
		this.continuativo = continuativo;
	}

	public String getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(String dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getStampato() {
		return stampato;
	}

	public void setStampato(String stampato) {
		this.stampato = stampato;
	}

	public String getStatoOrdine() {
		return statoOrdine;
	}

	public void setStatoOrdine(String statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	public String getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public int getIDOrd() {
		return IDOrd;
	}

	public void setIDOrd(int ord) {
		IDOrd = ord;
	}

	public Boolean getOrdNOinv() {
		return ordNOinv;
	}

	public void setOrdNOinv(Boolean ordNOinv) {
		this.ordNOinv = ordNOinv;
	}

	public String getRangeDewey() {
		return rangeDewey;
	}

	public void setRangeDewey(String rangeDewey) {
		this.rangeDewey = rangeDewey;
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	public String getLinguaDescr() {
		return linguaDescr;
	}

	public void setLinguaDescr(String linguaDescr) {
		this.linguaDescr = linguaDescr;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getPaeseDescr() {
		return paeseDescr;
	}

	public void setPaeseDescr(String paeseDescr) {
		this.paeseDescr = paeseDescr;
	}

}
