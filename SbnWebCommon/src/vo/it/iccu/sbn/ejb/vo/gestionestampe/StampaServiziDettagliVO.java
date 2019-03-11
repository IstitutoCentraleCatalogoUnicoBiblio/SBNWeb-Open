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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class StampaServiziDettagliVO extends StampaServiziVO{


	/**
	 *
	 */
	private static final long serialVersionUID = 8417350527744810791L;

	public StampaServiziDettagliVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	// Attributes
	//dati richiesta
	private String svolgimento;
	private String tipoServizio;
	private String modalitaErog;
	private String attivita;
	private String statoMov;
	private String statoRich;
	private String inventario;
	private String collocazione;
	private String dataDa;
	private String dataA;
	private String descrBib;
	private String data;
	//fine dati richiesta
	//dati comuni ai modelli dei servizi correnti e storicizzati
	private String dataIns;//DATA INSERIMENTO|x
	private String dataAgg;//DATA AGGIORNAMENTO|x
	private String codBib;//BIBLIOTECA|x
	private String progrRichiesta;//RICH. SERVIZIO|x
	private String tipoSrevizio;//TIPO SERVIZIO|x
	private String dataInizioMov;//DATA INIZIO MOV.|
	private String dataFineMov;//DATA FINE MOV.|
	private String dataFinePrevMov;
	private String numRinnovi;//NUM.RINNOVI|
	private String noteBilioteca;//NOTE BIBL.|x
	private String costoServizio;//COSTO SERVIZIO|
	private String statoMovimento;//STATO MOVIMENTO|x
	private String codBibUtente;//COD.BIBL.UTENTE|
	private String progrUtente;//PROGR.UTENTE|
	private String nominativo;//NOMINATIVO|
	private String codProfessione;//COD.PROFESSIONE|x
	private String codTitoloStudio;//COD.TIT.STUDIO|x
	private String codBibInventario;
	private String codSerie;//COD.SERIE|x
	private String codInventario;//PROGR.INVENTARIO|x
	private String tipoDocLettore;//TIPO DOC.LETTORE|x
	private String codDocLettore;//COD.DOC.LETTORE|x
	private String numFascicolo;//NUM.FASCICOLO|
	private String numVolume;//NUM.VOLUME|
	private String segnatura;//SEGNATURA|
	private String titolo;//TITOLO|
	private String bid;
	private String prenotazione;//PRENOTAZIONE|
	private Date dataInizioMovDate;//per ottenere il formato data su excel
	private Date dataFineMovDate;//per ottenere il formato data su excel
	private Date dataFinePrevMovDate;
	private Date dataInsDate;//per ottenere il formato data su excel
	private Date dataAggDate;//per ottenere il formato data su excel
	private String codBibTipoServ;
	private String descrBibTipoServ;



	private Locale locale = Locale.getDefault();
	private String numberFormatPrezzi = "###,###,###,##0.00";

	public String getDataDa() {
		return dataDa;
	}
	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}
	public String getDataA() {
		return dataA;
	}
	public void setDataA(String dataA) {
		this.dataA = dataA;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDataIns() {
		return dataIns;
	}
	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}
	public String getDataAgg() {
		return dataAgg;
	}
	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getProgrRichiesta() {
		return progrRichiesta;
	}
	public void setProgrRichiesta(String progrRichiesta) {
		this.progrRichiesta = progrRichiesta;
	}
	public String getTipoSrevizio() {
		return tipoSrevizio;
	}
	public void setTipoSrevizio(String tipoSrevizio) {
		this.tipoSrevizio = tipoSrevizio;
	}
	public String getDataInizioMov() {
		return dataInizioMov;
	}
	public void setDataInizioMov(String dataInizioMov) {
		this.dataInizioMov = dataInizioMov;
	}
	public String getDataFineMov() {
		return dataFineMov;
	}
	public void setDataFineMov(String dataFineMov) {
		this.dataFineMov = dataFineMov;
	}
	public String getNumRinnovi() {
		return numRinnovi;
	}
	public void setNumRinnovi(String numRinnovi) {
		this.numRinnovi = numRinnovi;
	}
	public String getNoteBilioteca() {
		return noteBilioteca;
	}
	public void setNoteBilioteca(String noteBilioteca) {
		this.noteBilioteca = noteBilioteca;
	}
	public String getStatoMovimento() {
		return statoMovimento;
	}
	public void setStatoMovimento(String statoMovimento) {
		this.statoMovimento = statoMovimento;
	}
	public String getCodBibUtente() {
		return codBibUtente;
	}
	public void setCodBibUtente(String codBibUtente) {
		this.codBibUtente = codBibUtente;
	}
	public String getProgrUtente() {
		return progrUtente;
	}
	public void setProgrUtente(String progrUtente) {
		this.progrUtente = progrUtente;
	}
	public String getNominativo() {
		return nominativo;
	}
	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}
	public String getCodProfessione() {
		return codProfessione;
	}
	public void setCodProfessione(String codProfessione) {
		this.codProfessione = codProfessione;
	}
	public String getCodTitoloStudio() {
		return codTitoloStudio;
	}
	public void setCodTitoloStudio(String codTitoloStudio) {
		this.codTitoloStudio = codTitoloStudio;
	}
	public String getCodBibInventario() {
		return codBibInventario;
	}
	public void setCodBibInventario(String codBibInventario) {
		this.codBibInventario = codBibInventario;
	}
	public String getCodSerie() {
		return codSerie;
	}
	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
	}
	public String getCodInventario() {
		return codInventario;
	}
	public void setCodInventario(String codInventario) {
		this.codInventario = codInventario;
	}
	public String getTipoDocLettore() {
		return tipoDocLettore;
	}
	public void setTipoDocLettore(String tipoDocLettore) {
		this.tipoDocLettore = tipoDocLettore;
	}
	public String getCodDocLettore() {
		return codDocLettore;
	}
	public void setCodDocLettore(String codDocLettore) {
		this.codDocLettore = codDocLettore;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getNumFascicolo() {
		return numFascicolo;
	}
	public void setNumFascicolo(String numFascicolo) {
		this.numFascicolo = numFascicolo;
	}
	public String getNumVolume() {
		return numVolume;
	}
	public void setNumVolume(String numVolume) {
		this.numVolume = numVolume;
	}
	public String getSegnatura() {
		return segnatura;
	}
	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
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
	public String getCostoServizio() {
		return costoServizio;
	}
	public void setCostoServizio(String costoServizio) {
		this.costoServizio = costoServizio;
	}
	public double getCostoServizioDouble()  throws ParseException {
		return getCostoServizio(this.numberFormatPrezzi, this.locale);
	}

	public double getCostoServizio(String format, Locale locale)  throws ParseException {
		return ValidazioneDati.getDoubleFromString(costoServizio,format,locale);
	}

	public void setCostoServizio(double costoServizio) {
		setCostoServizio(costoServizio, numberFormatPrezzi, locale);
	}

	public void setCostoServizio(double costoServizio, String format, Locale locale) {
		this.costoServizio = ValidazioneDati.getStringFromDouble(costoServizio, format, locale);
	}
	public String getSvolgimento() {
		return svolgimento;
	}
	public void setSvolgimento(String svolgimento) {
		this.svolgimento = svolgimento;
	}
	public String getTipoServizio() {
		return tipoServizio;
	}
	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}
	public String getModalitaErog() {
		return modalitaErog;
	}
	public void setModalitaErog(String modalitaErog) {
		this.modalitaErog = modalitaErog;
	}
	public String getAttivita() {
		return attivita;
	}
	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}
	public String getStatoMov() {
		return statoMov;
	}
	public void setStatoMov(String statoMov) {
		this.statoMov = statoMov;
	}
	public String getStatoRich() {
		return statoRich;
	}
	public void setStatoRich(String statoRich) {
		this.statoRich = statoRich;
	}
	public String getInventario() {
		return inventario;
	}
	public void setInventario(String inventario) {
		this.inventario = inventario;
	}
	public String getCollocazione() {
		return collocazione;
	}
	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}
	public String getPrenotazione() {
		return prenotazione;
	}
	public void setPrenotazione(String prenotazione) {
		this.prenotazione = prenotazione;
	}
	public Date getDataInizioMovDate() {
		return dataInizioMovDate;
	}
	public void setDataInizioMovDate(Date dataInizioMovDate) {
		this.dataInizioMovDate = dataInizioMovDate;
	}
	public Date getDataFineMovDate() {
		return dataFineMovDate;
	}
	public void setDataFineMovDate(Date dataFineMovDate) {
		this.dataFineMovDate = dataFineMovDate;
	}
	public Date getDataInsDate() {
		return dataInsDate;
	}
	public void setDataInsDate(Date dataInsDate) {
		this.dataInsDate = dataInsDate;
	}
	public Date getDataAggDate() {
		return dataAggDate;
	}
	public void setDataAggDate(Date dataAggDate) {
		this.dataAggDate = dataAggDate;
	}
	public String getDataFinePrevMov() {
		return dataFinePrevMov;
	}
	public void setDataFinePrevMov(String dataFinePrevMov) {
		this.dataFinePrevMov = dataFinePrevMov;
	}
	public Date getDataFinePrevMovDate() {
		return dataFinePrevMovDate;
	}
	public void setDataFinePrevMovDate(Date dataFinePrevMovDate) {
		this.dataFinePrevMovDate = dataFinePrevMovDate;
	}
	public String getCodBibTipoServ() {
		return codBibTipoServ;
	}
	public void setCodBibTipoServ(String codBibTipoServ) {
		this.codBibTipoServ = codBibTipoServ;
	}
	public String getDescrBibTipoServ() {
		return descrBibTipoServ;
	}
	public void setDescrBibTipoServ(String descrBibTipoServ) {
		this.descrBibTipoServ = descrBibTipoServ;
	}
}
