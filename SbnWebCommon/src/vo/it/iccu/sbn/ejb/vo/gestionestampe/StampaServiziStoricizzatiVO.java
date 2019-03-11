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

import java.util.Date;
import java.util.Locale;

public class StampaServiziStoricizzatiVO extends StampaServiziDettagliVO{


	/**
	 *
	 */
	private static final long serialVersionUID = 3304150814630761267L;

	public StampaServiziStoricizzatiVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	//servizi storicizzati
	private String descrTipoServ;//DESC.TIPO.SERV|
	private String dataRichiesta;//DATA RICHIESTA|
	private String noteUtente;//NOTE UTENTE|
	private String prezzoMassimo;//PREZZO MASSIMO|
	private String dataMassima;//DATA MASSIMA|
	private String dataProroga;//DATA PROROGA|
	private String dataInizioPrev;//DATA INIZIO PREV.|
	private String dataFinePrev;//DATA FINE PREV.|
	private String localeIll;//LOCALE-ILL|
	private String copyright;//COPYRIGHT|
	private String modErog;//MOD.EROGAZIONE|
	private String statoRichiesta;//STATO RICHIESTA|
	private String supporto;//SUPPORTO|
	private String risposta;//RISPOSTA|
	private String modPagamento;//MODALITA PAGAMENTO|
	private String servIllAltern;//SERV. ILL ALTERN.|
	private String codErogAltern;//COD.EROG. ALT.|
	private String codBiblDest;//BIBL.DESTINATARIA|
	private String noteBibliotecario;//NOTE BIBLIOTECARIO|
	private String numVolumi;//NUM.DI VOLUMI|
	private String numPezzi;//NUM.PEZZI|
	private String noteBibl;
	private String autore;
	private String editore;
	private String annoEdizione;
	private String luogoEdizione;
	private String numSolleciti;
	private String dataUltSoll;
	private String numVolumeMon;
	private String numRinnovi;
	private String descrStatoMov;
	private Date dataRichiestaDate;//per ottenere il formato data su excel
	private Date dataMassimaDate;//per ottenere il formato data su excel
	private Date dataProrogaDate;//per ottenere il formato data su excel
	private Date dataInizioPrevDate;//per ottenere il formato data su excel
	private Date dataFinePrevDate;//per ottenere il formato data su excel
	private Date dataUltSollDate;//per ottenere il formato data su excel

	private Locale locale = Locale.getDefault();
	private String numberFormatPrezzi = "###,###,###,##0.00";

	public String getDescrTipoServ() {
		return descrTipoServ;
	}
	public void setDescrTipoServ(String descrTipoServ) {
		this.descrTipoServ = descrTipoServ;
	}
	public String getDataRichiesta() {
		return dataRichiesta;
	}
	public void setDataRichiesta(String dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}
	public String getNoteUtente() {
		return noteUtente;
	}
	public void setNoteUtente(String noteUtente) {
		this.noteUtente = noteUtente;
	}
	public String getPrezzoMassimo() {
		return prezzoMassimo;
	}
	public void setPrezzoMassimo(String prezzoMassimo) {
		this.prezzoMassimo = prezzoMassimo;
	}
	public String getDataMassima() {
		return dataMassima;
	}
	public void setDataMassima(String dataMassima) {
		this.dataMassima = dataMassima;
	}
	public String getDataProroga() {
		return dataProroga;
	}
	public void setDataProroga(String dataProroga) {
		this.dataProroga = dataProroga;
	}
	public String getDataInizioPrev() {
		return dataInizioPrev;
	}
	public void setDataInizioPrev(String dataInizioPrev) {
		this.dataInizioPrev = dataInizioPrev;
	}
	public String getDataFinePrev() {
		return dataFinePrev;
	}
	public void setDataFinePrev(String dataFinePrev) {
		this.dataFinePrev = dataFinePrev;
	}
	public String getLocaleIll() {
		return localeIll;
	}
	public void setLocaleIll(String localeIll) {
		this.localeIll = localeIll;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getModErog() {
		return modErog;
	}
	public void setModErog(String modErog) {
		this.modErog = modErog;
	}
	public String getStatoRichiesta() {
		return statoRichiesta;
	}
	public void setStatoRichiesta(String statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}
	public String getSupporto() {
		return supporto;
	}
	public void setSupporto(String supporto) {
		this.supporto = supporto;
	}
	public String getRisposta() {
		return risposta;
	}
	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}
	public String getModPagamento() {
		return modPagamento;
	}
	public void setModPagamento(String modPagamento) {
		this.modPagamento = modPagamento;
	}
	public String getServIllAltern() {
		return servIllAltern;
	}
	public void setServIllAltern(String servIllAltern) {
		this.servIllAltern = servIllAltern;
	}
	public String getCodErogAltern() {
		return codErogAltern;
	}
	public void setCodErogAltern(String codErogAltern) {
		this.codErogAltern = codErogAltern;
	}
	public String getCodBiblDest() {
		return codBiblDest;
	}
	public void setCodBiblDest(String codBiblDest) {
		this.codBiblDest = codBiblDest;
	}
	public String getNoteBibliotecario() {
		return noteBibliotecario;
	}
	public void setNoteBibliotecario(String noteBibliotecario) {
		this.noteBibliotecario = noteBibliotecario;
	}
	public String getNumVolumi() {
		return numVolumi;
	}
	public void setNumVolumi(String numVolumi) {
		this.numVolumi = numVolumi;
	}
	public String getNumPezzi() {
		return numPezzi;
	}
	public void setNumPezzi(String numPezzi) {
		this.numPezzi = numPezzi;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getNumberFormatPrezzi() {
		return numberFormatPrezzi;
	}
	public void setNumberFormatPrezzi(String numberFormatPrezzi) {
		this.numberFormatPrezzi = numberFormatPrezzi;
	}
	public String getNoteBibl() {
		return noteBibl;
	}
	public void setNoteBibl(String noteBibl) {
		this.noteBibl = noteBibl;
	}
	public String getAutore() {
		return autore;
	}
	public void setAutore(String autore) {
		this.autore = autore;
	}
	public String getEditore() {
		return editore;
	}
	public void setEditore(String editore) {
		this.editore = editore;
	}
	public String getAnnoEdizione() {
		return annoEdizione;
	}
	public void setAnnoEdizione(String annoEdizione) {
		this.annoEdizione = annoEdizione;
	}
	public String getLuogoEdizione() {
		return luogoEdizione;
	}
	public void setLuogoEdizione(String luogoEdizione) {
		this.luogoEdizione = luogoEdizione;
	}
	public String getNumSolleciti() {
		return numSolleciti;
	}
	public void setNumSolleciti(String numSolleciti) {
		this.numSolleciti = numSolleciti;
	}
	public String getDataUltSoll() {
		return dataUltSoll;
	}
	public void setDataUltSoll(String dataUltSoll) {
		this.dataUltSoll = dataUltSoll;
	}
	public String getNumVolumeMon() {
		return numVolumeMon;
	}
	public void setNumVolumeMon(String numVolumeMon) {
		this.numVolumeMon = numVolumeMon;
	}
	public String getDescrStatoMov() {
		return descrStatoMov;
	}
	public void setDescrStatoMov(String descrStatoMov) {
		this.descrStatoMov = descrStatoMov;
	}
	public String getNumRinnovi() {
		return numRinnovi;
	}
	public void setNumRinnovi(String numRinnovi) {
		this.numRinnovi = numRinnovi;
	}
	public Date getDataRichiestaDate() {
		return dataRichiestaDate;
	}
	public void setDataRichiestaDate(Date dataRichiestaDate) {
		this.dataRichiestaDate = dataRichiestaDate;
	}
	public Date getDataMassimaDate() {
		return dataMassimaDate;
	}
	public void setDataMassimaDate(Date dataMassimaDate) {
		this.dataMassimaDate = dataMassimaDate;
	}
	public Date getDataProrogaDate() {
		return dataProrogaDate;
	}
	public void setDataProrogaDate(Date dataProrogaDate) {
		this.dataProrogaDate = dataProrogaDate;
	}
	public Date getDataInizioPrevDate() {
		return dataInizioPrevDate;
	}
	public void setDataInizioPrevDate(Date dataInizioPrevDate) {
		this.dataInizioPrevDate = dataInizioPrevDate;
	}
	public Date getDataFinePrevDate() {
		return dataFinePrevDate;
	}
	public void setDataFinePrevDate(Date dataFinePrevDate) {
		this.dataFinePrevDate = dataFinePrevDate;
	}
	public Date getDataUltSollDate() {
		return dataUltSollDate;
	}
	public void setDataUltSollDate(Date dataUltSollDate) {
		this.dataUltSollDate = dataUltSollDate;
	}

}
