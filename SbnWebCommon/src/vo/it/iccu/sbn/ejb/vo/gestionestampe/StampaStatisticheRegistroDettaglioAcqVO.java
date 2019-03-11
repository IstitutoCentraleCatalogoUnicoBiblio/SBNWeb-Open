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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StampaStatisticheRegistroDettaglioAcqVO extends StampaRegistroVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -8692006759933883200L;

	public StampaStatisticheRegistroDettaglioAcqVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}
	// Attributes
	private String codiceTipoAcq;
	private String descrTipoAcq;
	private String totInvAcq;
	String totValAcq;
	private List<StampaStatisticheRegistroDettaglioMatVO> listaMat = new ArrayList<StampaStatisticheRegistroDettaglioMatVO>();
	public String getCodiceTipoAcq() {
		return codiceTipoAcq;
	}
	public void setCodiceTipoAcq(String codiceTipoAcq) {
		this.codiceTipoAcq = codiceTipoAcq;
	}
	public String getDescrTipoAcq() {
		return descrTipoAcq;
	}
	public void setDescrTipoAcq(String descrTipoAcq) {
		this.descrTipoAcq = descrTipoAcq;
	}
	public String getTotInvAcq() {
		return totInvAcq;
	}
	public void setTotInvAcq(String totInvAcq) {
		this.totInvAcq = totInvAcq;
	}
	public String getTotValAcq() {
		return totValAcq;
	}
	public void setTotValAcq(String totValAcq) {
		this.totValAcq = totValAcq;
	}
	public List getListaMat() {
		return listaMat;
	}
	public void setListaMat(
			List<StampaStatisticheRegistroDettaglioMatVO> listaMat) {
		this.listaMat = listaMat;
	}
	public double getTotValAcqDouble() throws ParseException {
		return getValore(numberFormat, locale);
	}
	public double getTotValAcq(String format, Locale locale) throws ParseException {
		return ValidazioneDati
				.getDoubleFromString(totValAcq, format, locale, 9, 2);
	}
	public void setTotValAcqDouble(double totValAcq) {
		setTotValAcq(totValAcq, numberFormat, locale);
	}
	public void setTotValAcq(double totValAcq, String format, Locale locale) {
		this.totValAcq = ValidazioneDati.getStringFromDouble(totValAcq, format, locale);
	}
}
