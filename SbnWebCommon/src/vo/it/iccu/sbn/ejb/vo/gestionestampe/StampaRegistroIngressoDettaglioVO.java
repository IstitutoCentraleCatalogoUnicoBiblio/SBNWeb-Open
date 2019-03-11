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

public class StampaRegistroIngressoDettaglioVO extends StampaRegistroVO {

	private static final long serialVersionUID = -2601441469955418098L;

	public static final String INVENTARI = "INVENTARI";

	// private String dataIns;
	private String nroInvReg;
	private String descrProven;
	private String codTipoAcq;
	private String bid;
	private String titolo;
	private String codTipoMat;
	private String precisazione;
	private String numFattura;
	private String dataFattura;
	private String codCollocazione;// è una descrizione
	private String sitAmm;
	private Date dataInsDate;

	public StampaRegistroIngressoDettaglioVO(ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public String getDescrProven() {
		return descrProven;
	}

	public void setDescrProven(String descrProven) {
		this.descrProven = descrProven;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getCodTipoMat() {
		return codTipoMat;
	}

	public void setCodTipoMat(String codTipoMat) {
		this.codTipoMat = codTipoMat;
	}

	public String getPrecisazione() {
		return precisazione;
	}

	public void setPrecisazione(String precisazione) {
		this.precisazione = precisazione;
	}

	public String getNumFattura() {
		return numFattura;
	}

	public void setNumFattura(String numFattura) {
		this.numFattura = numFattura;
	}

	public String getDataFattura() {
		return dataFattura;
	}

	public void setDataFattura(String dataFattura) {
		this.dataFattura = dataFattura;
	}

	public String getCodCollocazione() {
		return codCollocazione;
	}

	public void setCodCollocazione(String codCollocazione) {
		this.codCollocazione = codCollocazione;
	}

	public String getCodTipoAcq() {
		return codTipoAcq;
	}

	public void setCodTipoAcq(String codTipoAcq) {
		this.codTipoAcq = codTipoAcq;
	}

	// public String getDataIns() {
	// return dataIns;
	// }
	// public void setDataIns(String dataIns) {
	// this.dataIns = dataIns;
	// }
	public String getSitAmm() {
		return sitAmm;
	}

	public void setSitAmm(String sitAmm) {
		this.sitAmm = sitAmm;
	}

	public String getNroInvReg() {
		return nroInvReg;
	}

	public void setNroInvReg(String nroInvReg) {
		this.nroInvReg = nroInvReg;
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

	public double getValoreDouble() throws ParseException {
		return getValore(numberFormat, locale);
	}

	public double getPrezzoDouble() throws ParseException {
		return getPrezzo(numberFormat, locale);
	}

	public double getValore(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(valore, format, locale, 9, 2);
	}

	public double getPrezzo(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(prezzo, format, locale, 9, 2);
	}

	public void setValoreDouble(double valore) {
		setValore(valore, numberFormat, locale);
	}

	public void setValore(double valore, String format, Locale locale) {
		this.valore = ValidazioneDati.getStringFromDouble(valore, format, locale);
	}

	public void setPrezzoDouble(double prezzo) {
		setPrezzo(prezzo, numberFormat, locale);
	}

	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public void setPrezzo(double prezzo, String format, Locale locale) {
		this.prezzo = ValidazioneDati.getStringFromDouble(prezzo, format, locale);
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public Date getDataInsDate() {
		return dataInsDate;
	}

	public void setDataInsDate(Date dataInsDate) {
		this.dataInsDate = dataInsDate;
	}
}
