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
package it.iccu.sbn.ejb.vo.gestionestampe;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StampaTitoliEditoreVO extends ParametriRichiestaElaborazioneDifferitaVO{

    /**
	 *
	 */
	private static final long serialVersionUID = -7066350997985012826L;

	// Attributes

	private String descrBib;
	private String codEditore;
	private String descrEditore;
	private String isbn;
	private String paese;
	private String regione;
	private String provincia;
	private String dataPubbl1Da;
	private String dataPubbl1A;
	private String tipoRecord;
	private String lingua;
	private String natura;
	private String dataIngressoDa;
	private String dataIngressoA;
	private String tipoAcq;
	private String codiceTipoMateriale;
	private String sistema;
	private String simbolo;
	private String checkTipoRicerca;
	private String checkTipoPosseduto;
	private String checkEditore;
	private String tipoFormato;
	private String tipoModello;
	private List<StampaTitoliEditoreDettaglioVO> lista = new ArrayList<StampaTitoliEditoreDettaglioVO>();
	private String dataDiElaborazione;

	protected Locale locale = Locale.getDefault();
	protected String numberFormat = "###,###,###,##0.00";

//	public void validate(List<String> errori) throws ValidationException {
//		super.validate(errori);
//	}
	//output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	/**
	 *
	 */
	public StampaTitoliEditoreVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super();
	}
	public StampaTitoliEditoreVO(String codPolo, String codeBiblioteca, String sez,
			String coll1,String spec1,String coll2,String spec2, ParametriRichiestaElaborazioneDifferitaVO input){
		super();
		this.codPolo = codPolo;
		this.codBib = codeBiblioteca;
	}

	public List<String> getErrori() {
		return errori;
	}

	public void setErrori(List<String> errori) {
		this.errori = errori;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getNumberFormat() {
		return numberFormat;
	}
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}
	public List<StampaTitoliEditoreDettaglioVO> getLista() {
		return lista;
	}
	public void setLista(List<StampaTitoliEditoreDettaglioVO> lista) {
		this.lista = lista;
	}
	public String getCodEditore() {
		return codEditore;
	}
	public void setCodEditore(String codEditore) {
		this.codEditore = codEditore;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getRegione() {
		return regione;
	}
	public void setRegione(String regione) {
		this.regione = regione;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getDataPubbl1Da() {
		return dataPubbl1Da;
	}
	public void setDataPubbl1Da(String dataPubbl1Da) {
		this.dataPubbl1Da = dataPubbl1Da;
	}
	public String getDataPubbl1A() {
		return dataPubbl1A;
	}
	public void setDataPubbl1A(String dataPubbl1A) {
		this.dataPubbl1A = dataPubbl1A;
	}
	public String getTipoRecord() {
		return tipoRecord;
	}
	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	public String getLingua() {
		return lingua;
	}
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}
	public String getNatura() {
		return natura;
	}
	public void setNatura(String natura) {
		this.natura = natura;
	}
	public String getDataIngressoDa() {
		return dataIngressoDa;
	}
	public void setDataIngressoDa(String dataIngressoDa) {
		this.dataIngressoDa = dataIngressoDa;
	}
	public String getDataIngressoA() {
		return dataIngressoA;
	}
	public void setDataIngressoA(String dataIngressoA) {
		this.dataIngressoA = dataIngressoA;
	}
	public String getTipoAcq() {
		return tipoAcq;
	}
	public void setTipoAcq(String tipoAcq) {
		this.tipoAcq = tipoAcq;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
	public String getCheckTipoRicerca() {
		return checkTipoRicerca;
	}
	public void setCheckTipoRicerca(String checkTipoRicerca) {
		this.checkTipoRicerca = checkTipoRicerca;
	}
	public String getCheckTipoPosseduto() {
		return checkTipoPosseduto;
	}
	public void setCheckTipoPosseduto(String checkTipoPosseduto) {
		this.checkTipoPosseduto = checkTipoPosseduto;
	}
	public String getCheckEditore() {
		return checkEditore;
	}
	public void setCheckEditore(String checkEditore) {
		this.checkEditore = checkEditore;
	}
	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getTipoModello() {
		return tipoModello;
	}
	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}
	public String getDataDiElaborazione() {
		return dataDiElaborazione;
	}
	public void setDataDiElaborazione(String dataDiElaborazione) {
		this.dataDiElaborazione = dataDiElaborazione;
	}
	public String getDescrEditore() {
		return descrEditore;
	}
	public void setDescrEditore(String descrEditore) {
		this.descrEditore = descrEditore;
	}
	public String getCodiceTipoMateriale() {
		return codiceTipoMateriale;
	}
	public void setCodiceTipoMateriale(String codiceTipoMateriale) {
		this.codiceTipoMateriale = codiceTipoMateriale;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
}
