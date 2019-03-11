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

public class ListaSuppSpeseVO  extends SerializableVO {

	private static final long serialVersionUID = -4898965400708008356L;
	/**
	 *
	 */
	private String ticket;
	private String utente;
	private String codPolo;
	private String codBibl;
	private String denoBibl;
	private String anno;
	private String dataOrdine;
	private String dataOrdineDa;
	private String dataOrdineA;
	private String tipoOrdine;
	private String[] tipoOrdineArr;
	private String ordinamento;
	private String imp="";
	private int idBil=0;
	private int idForn=0;
	private int idSez=0;
	private String naturaOrdine;
	private String codFornitore;  // utilizzato nel report come descrizione
	private String fornitore;	// utilizzato nel report come descrizione
	private String sezione; 	// utilizzato nel report come descrizione
	private String esercizio;	// utilizzato nel report come descrizione
	private String capitolo;	// utilizzato nel report come descrizione
	private String tipoMaterialeInv;
	private String tipoMaterialeInvDescr;
	private String supporto;
	private String supportoDescr;
	private String tipoRecord;
	private String tipoRecordDescr;
	private String tipoReport="1";
	private String testoListaSezioni;
	private String idListaSezioni;
	private Boolean ordNOinv=false;
	private String rangeDewey="";
	private String lingua;
	private String linguaDescr;
	private String paese;
	private String paeseDescr;


	public ListaSuppSpeseVO (){}



	public String getAnno() {
		return anno;
	}



	public void setAnno(String anno) {
		this.anno = anno;
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



	public String getDataOrdine() {
		return dataOrdine;
	}



	public void setDataOrdine(String dataOrdine) {
		this.dataOrdine = dataOrdine;
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



	public String getOrdinamento() {
		return ordinamento;
	}



	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}



	public String getTicket() {
		return ticket;
	}



	public void setTicket(String ticket) {
		this.ticket = ticket;
	}



	public String getTipoOrdine() {
		return tipoOrdine;
	}



	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}



	public String getUtente() {
		return utente;
	}



	public void setUtente(String utente) {
		this.utente = utente;
	}



	public String[] getTipoOrdineArr() {
		return tipoOrdineArr;
	}



	public void setTipoOrdineArr(String[] tipoOrdineArr) {
		this.tipoOrdineArr = tipoOrdineArr;
	}



	public int getIdForn() {
		return idForn;
	}



	public void setIdForn(int idForn) {
		this.idForn = idForn;
	}



	public int getIdSez() {
		return idSez;
	}



	public void setIdSez(int idSez) {
		this.idSez = idSez;
	}



	public String getImp() {
		return imp;
	}



	public void setImp(String imp) {
		this.imp = imp;
	}



	public String getNaturaOrdine() {
		return naturaOrdine;
	}



	public void setNaturaOrdine(String naturaOrdine) {
		this.naturaOrdine = naturaOrdine;
	}



	public int getIdBil() {
		return idBil;
	}



	public void setIdBil(int idBil) {
		this.idBil = idBil;
	}



	public String getCapitolo() {
		return capitolo;
	}



	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}



	public String getCodFornitore() {
		return codFornitore;
	}



	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}



	public String getEsercizio() {
		return esercizio;
	}



	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}



	public String getFornitore() {
		return fornitore;
	}



	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
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



	public String getTipoReport() {
		return tipoReport;
	}



	public void setTipoReport(String tipoReport) {
		this.tipoReport = tipoReport;
	}



	public String getIdListaSezioni() {
		return idListaSezioni;
	}



	public void setIdListaSezioni(String idListaSezioni) {
		this.idListaSezioni = idListaSezioni;
	}



	public String getTestoListaSezioni() {
		return testoListaSezioni;
	}



	public void setTestoListaSezioni(String testoListaSezioni) {
		this.testoListaSezioni = testoListaSezioni;
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
	};


}
