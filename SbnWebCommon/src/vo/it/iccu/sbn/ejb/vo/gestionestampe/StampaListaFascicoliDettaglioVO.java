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

import java.util.Locale;


public class StampaListaFascicoliDettaglioVO extends StampaListaFascicoliVO {

	private static final long serialVersionUID = -543900038710172232L;

	private String fornitore;
	private String ordine;
	private String statoOrd;
	private String descrFascicolo;
	private String statoFormato;
	private String dataPubblicazione;
	private String note;
	private String bid;
	private String isbd;


	public StampaListaFascicoliDettaglioVO(ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
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

	public String getOrdine() {
		return ordine;
	}

	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public String getStatoOrd() {
		return statoOrd;
	}

	public void setStatoOrd(String statoOrd) {
		this.statoOrd = statoOrd;
	}

	public String getDescrFascicolo() {
		return descrFascicolo;
	}

	public void setDescrFascicolo(String descrFascicolo) {
		this.descrFascicolo = descrFascicolo;
	}

	public String getStatoFormato() {
		return statoFormato;
	}

	public void setStatoFormato(String statoFormato) {
		this.statoFormato = statoFormato;
	}

	public String getDataPubblicazione() {
		return dataPubblicazione;
	}

	public void setDataPubblicazione(String dataPubblicazione) {
		this.dataPubblicazione = dataPubblicazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

}
