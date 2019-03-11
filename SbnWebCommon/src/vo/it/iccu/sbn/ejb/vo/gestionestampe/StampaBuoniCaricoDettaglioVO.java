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


public class StampaBuoniCaricoDettaglioVO extends StampaBuoniCaricoVO{

	public StampaBuoniCaricoDettaglioVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
		// TODO Auto-generated constructor stub
	}
	/**
	 *
	 */
	private static final long serialVersionUID = -543900038710172232L;

	private String codPolo;
	private String codBib;
	private String codSerie;
	private String codInv;
	private String bid;
	private String isbd;
	private String codTipOrdDescr;
	private String valore;
	private String precInv;
	private String totale;
	private String annoFattura;//aaaa
	private String numFattura;
	private String progrFattura;
	private String fornitore;
	private String dataFattura;//gg/mm/aaaa

	private String numeroBuono;
	private String annoBuono;

	private Date dataIns;


	public StampaBuoniCaricoDettaglioVO( String codPolo, String codBib, String codSerie,
			String codInv, String bid, String isbd, String codTipOrdDescr, String valore, String precInv, String totale,
			String numeroBuono, Date dataIns, String annoFattura, String progrFattura, String fornitore, String dataFattura,
			String numFattura, String annoBuono,
			ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.codSerie = codSerie;
		this.codInv = codInv;
		this.bid = bid;
		this.isbd = isbd;
		this.codTipOrdDescr = codTipOrdDescr;
		this.valore = valore;
		this.precInv = precInv;
		this.totale = totale;
		this.annoFattura = annoFattura;
		this.progrFattura = progrFattura;
		this.numFattura = numFattura;
		this.fornitore = fornitore;
		this.dataFattura = dataFattura;
		this.numeroBuono = numeroBuono;
		this.annoBuono = annoBuono;
		this.dataIns = dataIns;
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

	public String getCodInv() {
		return codInv;
	}

	public void setCodInv(String codInv) {
		this.codInv = codInv;
	}

	public String getCodTipOrdDescr() {
		return codTipOrdDescr;
	}

	public void setCodTipOrdDescr(String codTipOrdDescr) {
		this.codTipOrdDescr = codTipOrdDescr;
	}

	public String getPrecInv() {
		return precInv;
	}

	public void setPrecInv(String precInv) {
		this.precInv = precInv;
	}

	public String getTotale() {
		return totale;
	}

	public void setTotale(String totale) {
		this.totale = totale;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public double getValore(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(valore, format, locale, 9, 2);
	}
	public void setValore(double valore, String format, Locale locale) {
		this.valore = ValidazioneDati.getStringFromDouble(valore, format, locale);
	}
	public void setValoreDouble(double valore) {
		setValore(valore, numberFormat, locale);
	}

	public double getValoreDouble() throws ParseException {
		return getValore(numberFormat, locale);
	}

	public String getAnnoFattura() {
		return annoFattura;
	}

	public void setAnnoFattura(String annoFattura) {
		this.annoFattura = annoFattura;
	}

	public String getProgrFattura() {
		return progrFattura;
	}

	public void setProgrFattura(String progrFattura) {
		this.progrFattura = progrFattura;
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public String getDataFattura() {
		return dataFattura;
	}

	public void setDataFattura(String dataFattura) {
		this.dataFattura = dataFattura;
	}

	public String getCodSerie() {
		return codSerie;
	}

	public void setCodSerie(String codSerie) {
		this.codSerie = codSerie;
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

	public String getNumeroBuono() {
		return numeroBuono;
	}

	public void setNumeroBuono(String numeroBuono) {
		this.numeroBuono = numeroBuono;
	}

	public Date getDataIns() {
		return dataIns;
	}

	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}

	public String getNumFattura() {
		return numFattura;
	}

	public void setNumFattura(String numFattura) {
		this.numFattura = numFattura;
	}

	public String getAnnoBuono() {
		return annoBuono;
	}

	public void setAnnoBuono(String annoBuono) {
		this.annoBuono = annoBuono;
	}

}
