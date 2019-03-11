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

public class StampaStatisticheRegistroDettaglioMatVO extends StampaStatisticheRegistroDettaglioAcqVO{

	/**
	 *
	 */
	private static final long serialVersionUID = -8692006759933883200L;

	public StampaStatisticheRegistroDettaglioMatVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	// Attributes
	private String codiceTipoMat;
	private String descrTipoMat;
	private String totInvMat;
	private String totValMat;

	public String getCodiceTipoMat() {
		return codiceTipoMat;
	}
	public void setCodiceTipoMat(String codiceTipoMat) {
		this.codiceTipoMat = codiceTipoMat;
	}
	public String getDescrTipoMat() {
		return descrTipoMat;
	}
	public void setDescrTipoMat(String descrTipoMat) {
		this.descrTipoMat = descrTipoMat;
	}
	public String getTotInvMat() {
		return totInvMat;
	}
	public void setTotInvMat(String totInvMat) {
		this.totInvMat = totInvMat;
	}
	public String getTotValMat() {
		return totValMat;
	}
	public void setTotValMat(String totValMat) {
		this.totValMat = totValMat;
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

	public double getTotValMatDouble() throws ParseException {
		return getValore(numberFormat, locale);
	}

	public double getTotValMat(String format, Locale locale) throws ParseException {
		return ValidazioneDati
				.getDoubleFromString(totValMat, format, locale, 9, 2);
	}
	public void setTotValMatDouble(double totValMat) {
		setTotValMat(totValMat, numberFormat, locale);
	}

	public void setTotValMat(double totValMat, String format, Locale locale) {
		this.totValMat = ValidazioneDati.getStringFromDouble(totValMat, format, locale);
	}



}
