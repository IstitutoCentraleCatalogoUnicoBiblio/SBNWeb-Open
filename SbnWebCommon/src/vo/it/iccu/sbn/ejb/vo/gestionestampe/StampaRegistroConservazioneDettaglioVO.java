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
import java.util.Locale;


/**
 * @author Alessandro
 * VO per il dettaglio della stampa registro topografico
 */
public class StampaRegistroConservazioneDettaglioVO extends StampaRegistroConservazioneVO{

	/**
	 *
	 */
	private static final long serialVersionUID = -543900038710172232L;

	private String ordLoc; //non stampare
	private String keyLoc; //non stampare
//	private String sezione;
//	private String collocazione;
//	private String specificazione;
	private String segnatura;
	private String serie;
	private String inventario;
	private String sequenza;
	private String precisazione;
	private String dataIngresso;
	private String provenienza;
	private String valore;
	private String statoConservazione;
	private String descrStatoConservazione;
	private String tipoMateriale;
	private String descrTipoMateriale;
	private String bidInv; //non stampare
	private String bidDescr;
	private String bidKyCles2t; //non stampare
	private String bidDescrSup;

	// Attributes

	public StampaRegistroConservazioneDettaglioVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	public StampaRegistroConservazioneDettaglioVO(String ordLoc, String keyLoc,/* String sezione, String collocazione,
			String specificazione,*/ String segnatura, String serie, String inventario, String sequenza, String precisazione,
			String dataIngresso, String provenienza, String valore, String statoConservazione, String tipoMateriale,
			String bidInv, String bidDescr, String bidKyCles2t, String bidDescrSup, ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
		this.ordLoc = ordLoc;
		this.keyLoc = keyLoc;
//		this.sezione = sezione;
		this.segnatura = segnatura;
//		this.collocazione = collocazione;
//		this.specificazione = specificazione;
		this.serie = serie;
		this.inventario = inventario;
		this.sequenza = sequenza;
		this.precisazione = precisazione;
		this.dataIngresso = dataIngresso;
		this.provenienza = provenienza;
		this.valore = valore;
		this.statoConservazione = statoConservazione;
		this.tipoMateriale = tipoMateriale;
		this.bidInv = bidInv;
		this.bidDescr = bidDescr;
		this.bidKyCles2t = bidKyCles2t;
		this.bidDescrSup = bidDescrSup;
		this.keyLoc = keyLoc;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getOrdLoc() {
		return ordLoc;
	}

	public void setOrdLoc(String ordLoc) {
		this.ordLoc = ordLoc;
	}

	public String getKeyLoc() {
		return keyLoc;
	}

	public void setKeyLoc(String keyLoc) {
		this.keyLoc = keyLoc;
	}

	public String getBidInv() {
		return bidInv;
	}

	public void setBidInv(String bidInv) {
		this.bidInv = bidInv;
	}

	public String getBidDescr() {
		return bidDescr;
	}

	public void setBidDescr(String bidDescr) {
		this.bidDescr = bidDescr;
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

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

	public String getSequenza() {
		return sequenza;
	}

	public void setSequenza(String sequenza) {
		this.sequenza = sequenza;
	}

	public String getPrecisazione() {
		return precisazione;
	}

	public void setPrecisazione(String precisazione) {
		this.precisazione = precisazione;
	}

	public String getDataIngresso() {
		return dataIngresso;
	}

	public void setDataIngresso(String dataIngresso) {
		this.dataIngresso = dataIngresso;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getStatoConservazione() {
		return statoConservazione;
	}

	public void setStatoConservazione(String statoConservazione) {
		this.statoConservazione = statoConservazione;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public String getDescrStatoConservazione() {
		return descrStatoConservazione;
	}

	public void setDescrStatoConservazione(String descrStatoConservazione) {
		this.descrStatoConservazione = descrStatoConservazione;
	}

	public String getDescrTipoMateriale() {
		return descrTipoMateriale;
	}

	public void setDescrTipoMateriale(String descrTipoMateriale) {
		this.descrTipoMateriale = descrTipoMateriale;
	}

	public double getValore(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(valore, format, locale, 9, 2);
	}
	public void setValoreDouble(double valore) {
		setValore(valore, numberFormat, locale);
	}

	public void setValore(double valore, String format, Locale locale) {
		this.valore = ValidazioneDati.getStringFromDouble(valore, format, locale);
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
	public String getSegnatura() {
		return segnatura;
	}
	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}
	public String getSerie() {
		return serie;
	}
	public double getValoreDouble() throws ParseException {
		return getValore(numberFormat, locale);
	}
}
