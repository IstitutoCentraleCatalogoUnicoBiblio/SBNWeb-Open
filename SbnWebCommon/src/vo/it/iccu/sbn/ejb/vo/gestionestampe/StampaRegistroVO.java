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
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StampaRegistroVO extends DocumentoFisicoElaborazioniDifferiteOutputVO {

    /**
	 *
	 */
	private static final long serialVersionUID = -2601441469955418098L;


	// Attributes
	private String codPolo;
	private String codeBiblioteca;
	private String serieInv;
	private String codeInvDa;
	private String codeInvA;
	private String dataDa;
	private String dataA;
	private String registro;
	private String descrBib;
	private String codeTipoOrdine;

	private int sizeInv;
	private int sizeLog;
	private SubReportVO recInventario = null;
	private SubReportVO recLog = null;

	public StampaRegistroVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	//output
	private List<String> errori = new ArrayList<String>();
	private String msg;


	protected String data;


	protected Locale locale = Locale.getDefault();


	protected String numberFormat = "###,###,###,##0.00";


	protected String valore;


	protected String prezzo;


	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);
	}
	public StampaRegistroVO(String codPolo, String codeBiblioteca, String serieInv,
			String codeInvDa, String codeInvA, String dataDa, String dataA, String registro, ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
		this.codPolo = codPolo;
		this.codeBiblioteca = codeBiblioteca;
		this.serieInv = serieInv;
		this.codeInvDa = codeInvDa;
		this.codeInvA = codeInvA;
		this.dataDa = dataDa;
		this.dataA = dataA;
		this.registro = registro;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public int getSizeInv() {
		return sizeInv;
	}
	public void setSizeInv(int sizeInv) {
		this.sizeInv = sizeInv;
	}
	public SubReportVO getRecInventario() {
		return recInventario;
	}
	public void setRecInventario(SubReportVO recInventario) {
		this.recInventario = recInventario;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getCodeBiblioteca() {
		return codeBiblioteca;
	}
	public void setCodeBiblioteca(String codeBiblioteca) {
		this.codeBiblioteca = codeBiblioteca;
	}
	public String getSerieInv() {
		return serieInv;
	}
	public void setSerieInv(String serieInv) {
		this.serieInv = serieInv;
	}
	public String getCodeInvDa() {
		return codeInvDa;
	}
	public void setCodeInvDa(String codeInvDa) {
		this.codeInvDa = codeInvDa;
	}
	public String getCodeInvA() {
		return codeInvA;
	}
	public void setCodeInvA(String codeInvA) {
		this.codeInvA = codeInvA;
	}
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
	public String getRegistro() {
		return registro;
	}
	public void setRegistro(String registro) {
		this.registro = registro;
	}
	public String getCodeTipoOrdine() {
		return codeTipoOrdine;
	}
	public void setCodeTipoOrdine(String codeTipoOrdine) {
		this.codeTipoOrdine = codeTipoOrdine;
	}
	public SubReportVO getRecLog() {
		return recLog;
	}
	public void setRecLog(SubReportVO recLog) {
		this.recLog = recLog;
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
	public int getSizeLog() {
		return sizeLog;
	}
	public void setSizeLog(int sizeLog) {
		this.sizeLog = sizeLog;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
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
		return ValidazioneDati
				.getDoubleFromString(valore, format, locale, 9, 2);
	}
	public double getPrezzo(String format, Locale locale) throws ParseException {
		return ValidazioneDati.getDoubleFromString(prezzo, format, locale, 9,
				2);
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
}
