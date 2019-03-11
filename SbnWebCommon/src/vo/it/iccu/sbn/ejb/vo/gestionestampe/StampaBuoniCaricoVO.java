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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.DocumentoFisicoElaborazioniDifferiteOutputVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StampaBuoniCaricoVO extends
		DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = 5688339218516682358L;

	public StampaBuoniCaricoVO(ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	// Attributes
	private String descrbib;
	private String tipoOrdinamento;
	private String aaFattura;
	private String fattura;
	private String buonoCarico;
	private String totValore;
	private boolean ristampaDaInv;
	private boolean ristampaDaFattura;
	private boolean ristampaDaNumBuono;
	private String ristampa;

	//almaviva5_20131118 #5100
	private String dataCarico;

	private List<StampaBuoniCaricoDettaglioVO> lista = new ArrayList<StampaBuoniCaricoDettaglioVO>();

	private String descrBib;

	protected Locale locale = Locale.getDefault();
	protected String numberFormat = "###,###,###,##0.00";

	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);
	}

	// output
	private List<String> errori = new ArrayList<String>();
	private String msg;

	/**
	 *
	 */
	public StampaBuoniCaricoVO(String codPolo, String codeBiblioteca,
			String codSerie, String daNum, String aNum, String buonoCarico,
			String annoFattura, String numFattura,
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
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

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getDallaCollocazione() {
		return dallaCollocazione;
	}

	public void setDallaCollocazione(String dallaCollocazione) {
		this.dallaCollocazione = dallaCollocazione;
	}

	public String getDallaSpecificazione() {
		return dallaSpecificazione;
	}

	public void setDallaSpecificazione(String dallaSpecificazione) {
		this.dallaSpecificazione = dallaSpecificazione;
	}

	public String getAllaCollocazione() {
		return allaCollocazione;
	}

	public void setAllaCollocazione(String allaCollocazione) {
		this.allaCollocazione = allaCollocazione;
	}

	public String getAllaSpecificazione() {
		return allaSpecificazione;
	}

	public void setAllaSpecificazione(String allaSpecificazione) {
		this.allaSpecificazione = allaSpecificazione;
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

	// public String getNumFattura() {
	// return numFattura;
	// }
	//
	// public void setNumFattura(String numFattura) {
	// this.numFattura = numFattura;
	// }

	public List<StampaBuoniCaricoDettaglioVO> getLista() {
		return lista;
	}

	public String getFattura() {
		return fattura;
	}

	public void setFattura(String fattura) {
		this.fattura = fattura;
	}

	public void setLista(List<StampaBuoniCaricoDettaglioVO> lista) {
		this.lista = lista;
	}

	public String getAaFattura() {
		return aaFattura;
	}

	public void setAaFattura(String aaFattura) {
		this.aaFattura = aaFattura;
	}

	public String getDescrbib() {
		return descrbib;
	}

	public void setDescrbib(String descrbib) {
		this.descrbib = descrbib;
	}

	public double getTotValoreDouble() throws ParseException {
		return getTotValore(numberFormat, locale);
	}

	public double getTotValore(String format, Locale locale)
			throws ParseException {
		return ValidazioneDati.getDoubleFromString(totValore, format, locale,
				9, 2);
	}

	public void setTotValoreDouble(double TotValore) {
		setTotValore(TotValore, numberFormat, locale);
	}

	public void setTotValore(double TotValore, String format, Locale locale) {
		this.totValore = ValidazioneDati.getStringFromDouble(TotValore, format,
				locale);
	}

	public boolean isRistampaDaInv() {
		return ristampaDaInv;
	}

	public void setRistampaDaInv(boolean ristampaDaInv) {
		this.ristampaDaInv = ristampaDaInv;
	}

	public boolean isRistampaDaFattura() {
		return ristampaDaFattura;
	}

	public void setRistampaDaFattura(boolean ristampaDaFattura) {
		this.ristampaDaFattura = ristampaDaFattura;
	}

	public boolean isRistampaDaNumBuono() {
		return ristampaDaNumBuono;
	}

	public void setRistampaDaNumBuono(boolean ristampaDaNumBuono) {
		this.ristampaDaNumBuono = ristampaDaNumBuono;
	}

	public String getBuonoCarico() {
		return buonoCarico;
	}

	public void setBuonoCarico(String buonoCarico) {
		this.buonoCarico = buonoCarico;
	}

	public String getTotValore() {
		return totValore;
	}

	public void setTotValore(String totValore) {
		this.totValore = totValore;
	}

	public String getRistampa() {
		return ristampa;
	}

	public void setRistampa(String ristampa) {
		this.ristampa = ristampa;
	}

	public String getDataCarico() {
		return dataCarico;
	}

	public void setDataCarico(String dataCarico) {
		this.dataCarico = dataCarico;
	}

}
