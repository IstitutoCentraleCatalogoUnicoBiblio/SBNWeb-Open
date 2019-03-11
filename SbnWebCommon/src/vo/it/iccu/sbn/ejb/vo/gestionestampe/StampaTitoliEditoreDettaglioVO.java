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

import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * VO per il dettaglio della stampa registro topografico
 */
public class StampaTitoliEditoreDettaglioVO extends
		StampaTitoliEditoreVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -543900038710172232L;

	// ////////////////////////
	private String editore;
	private String nomeEditore;
	private String regione;
	private String descrRegione;

	private String paese;
	private String descrPaese;

	private String provincia;
	private String descrProvincia;
	private String comune;
	private String descrComune;
	private String isbn;
	private String bid;
	private String dataPubbl;
	private String lingua;
	private String descrLingua;
	private String titolo;
	private String bidKyCles2t;
	private String bidDescrSup;
	private String tipoRecord;
	private String descrTipoRecord;
	private String codPolo;
	private String codBib;
	private String codSerie;
	private String inventario;
	private String dataIngresso;
	private Date   dataIngressoDate;
	private String codTipoAcq;
	private String descrTipoAcq;
	private String codTipoMat;
	private String descrTipoMat;
	private String classe;
	private String simbolo;
	private String dirittoDiStampa;
	private String altriTipiAcq;

	private List<CodiceVO> indiciClassificazione = new ArrayList();

	public StampaTitoliEditoreDettaglioVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public StampaTitoliEditoreDettaglioVO(String editore, String nomeEditore,
			String regione, String descrRegione, String comune, String descrComune,	String provincia, String descrProvincia,
			String isbn, String dataPubbl, String lingua, String descrLingua,
			String bid, String titolo, String bidKyCles2t, String bidDescrSup, String tipoRecord, String descrTipoRecord,
			String codPolo, String codBib, String codSerie, String inventario, String dataIngresso,	Date dataIngressoDate, String codTipoAcq, String descrTipoAcq, String codTipoMat, String descrTipoMat,
			String classe, String simbolo,
			String dirittoDiStampa, String altriTipiAcq,
			ParametriRichiestaElaborazioneDifferitaVO input,
			String paese, String descrPaese) {
		super(input);
		this.editore = editore;
		this.nomeEditore = nomeEditore;

		this.paese = paese;
		this.descrPaese = descrPaese;

		this.regione = regione;
		this.descrRegione = descrRegione;
		this.provincia = provincia;
		this.descrProvincia = descrProvincia;
		this.comune = comune;
		this.descrComune = descrComune;
		this.isbn = isbn;
		this.bid = bid;
		this.dataPubbl = dataPubbl;
		this.lingua = lingua;
		this.descrLingua = descrLingua;
		this.titolo = titolo;
		this.bidKyCles2t = bidKyCles2t;
		this.bidDescrSup = bidDescrSup;
		this.bidDescrSup = bidDescrSup;
		this.descrTipoRecord = descrTipoRecord;
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.codSerie = codSerie;
		this.inventario = inventario;
		this.dataIngresso = dataIngresso;
		this.dataIngressoDate = dataIngressoDate;
		this.codTipoAcq = codTipoAcq;
		this.descrTipoAcq = descrTipoAcq;
		this.codTipoMat = codTipoMat;
		this.descrTipoMat = descrTipoMat;
		this.classe = classe;
		this.simbolo = simbolo;
		this.dirittoDiStampa = dirittoDiStampa;
		this.altriTipiAcq = altriTipiAcq;
	}

	public Date getDataIngressoDate() {
		return dataIngressoDate;
	}

	public void setDataIngressoDate(Date dataIngressoDate) {
		this.dataIngressoDate = dataIngressoDate;
	}

	public String getDataPubbl() {
		return dataPubbl;
	}

	public void setDataPubbl(String dataPubbl) {
		this.dataPubbl = dataPubbl;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getNomeEditore() {
		return nomeEditore;
	}

	public void setNomeEditore(String nomeEditore) {
		this.nomeEditore = nomeEditore;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
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

	public String getCodSerie() {
		return codSerie;
	}

	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

	public String getCodTipoAcq() {
		return codTipoAcq;
	}

	public void setCodTipoAcq(String codTipoAcq) {
		this.codTipoAcq = codTipoAcq;
	}

	public String getDescrTipoAcq() {
		return descrTipoAcq;
	}

	public void setDescrTipoAcq(String descrTipoAcq) {
		this.descrTipoAcq = descrTipoAcq;
	}

	public String getCodTipoMat() {
		return codTipoMat;
	}

	public void setCodTipoMat(String codTipoMat) {
		this.codTipoMat = codTipoMat;
	}

	public String getDescrTipoMat() {
		return descrTipoMat;
	}

	public void setDescrTipoMat(String descrTipoMat) {
		this.descrTipoMat = descrTipoMat;
	}

	public String getDescrLingua() {
		return descrLingua;
	}

	public void setDescrLingua(String descrLingua) {
		this.descrLingua = descrLingua;
	}

	public String getDescrTipoRecord() {
		return descrTipoRecord;
	}

	public void setDescrTipoRecord(String descrTipoRecord) {
		this.descrTipoRecord = descrTipoRecord;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public String getDescrRegione() {
		return descrRegione;
	}

	public void setDescrRegione(String descrRegione) {
		this.descrRegione = descrRegione;
	}

	public String getDescrProvincia() {
		return descrProvincia;
	}

	public void setDescrProvincia(String descrProvincia) {
		this.descrProvincia = descrProvincia;
	}

	public String getDataIngresso() {
		return dataIngresso;
	}

	public void setDataIngresso(String dataIngresso) {
		this.dataIngresso = dataIngresso;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public List<CodiceVO> getIndiciClassificazione() {
		return indiciClassificazione;
	}

	public void setIndiciClassificazione(List<CodiceVO> indiciClassificazione) {
		this.indiciClassificazione = indiciClassificazione;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getDescrComune() {
		return descrComune;
	}

	public void setDescrComune(String descrComune) {
		this.descrComune = descrComune;
	}

	public String getBidKyCles2t() {
		return bidKyCles2t;
	}

	public void setBidKyCles2t(String bidKyCles2t) {
		this.bidKyCles2t = bidKyCles2t;
	}

	public String getBidDescrSup() {
		return bidDescrSup;
	}

	public void setBidDescrSup(String bidDescrSup) {
		this.bidDescrSup = bidDescrSup;
	}

	public String getDirittoDiStampa() {
		return dirittoDiStampa;
	}

	public void setDirittoDiStampa(String dirittoDiStampa) {
		this.dirittoDiStampa = dirittoDiStampa;
	}

	public String getAltriTipiAcq() {
		return altriTipiAcq;
	}

	public void setAltriTipiAcq(String altriTipiAcq) {
		this.altriTipiAcq = altriTipiAcq;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getDescrPaese() {
		return descrPaese;
	}

	public void setDescrPaese(String descrPaese) {
		this.descrPaese = descrPaese;
	}


}
