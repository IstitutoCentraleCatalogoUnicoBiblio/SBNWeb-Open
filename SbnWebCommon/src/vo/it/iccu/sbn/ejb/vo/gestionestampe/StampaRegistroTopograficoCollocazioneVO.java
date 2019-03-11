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
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;


/**
 * VO per il dettaglio della stampa registro topografico
 */
public class StampaRegistroTopograficoCollocazioneVO extends StampaRegistroTopograficoVO{
	/**
	 *
	 */
	private static final long serialVersionUID = -543900038710172232L;

	private String ordLoc; //non stampare
	private String keyLoc; //non stampare
	private String colloc;
	private String specificazione;
	private String totInventari;
	private String consistenzaColl;
	private SubReportVO recInventario = null;

	// Attributes

	public StampaRegistroTopograficoCollocazioneVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}

	public StampaRegistroTopograficoCollocazioneVO(String ordLoc, String keyLoc, String colloc, String specificazione, String totInventari,
			String consistenzaColl, ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	this.ordLoc = ordLoc;
	this.keyLoc = keyLoc;
	this.colloc = colloc;
	this.specificazione = specificazione;
	this.totInventari = totInventari;
	this.consistenzaColl = consistenzaColl;

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

	public String getColloc() {
		return colloc;
	}

	public void setCollocazione(String collocazione) {
		this.colloc = collocazione;
	}

	public String getSpecificazione() {
		return specificazione;
	}

	public void setSpecificazione(String specificazione) {
		this.specificazione = specificazione;
	}

	public String getTotInventari() {
		return totInventari;
	}

	public void setTotInventari(String totInventari) {
		this.totInventari = totInventari;
	}

	public String getConsistenzaColl() {
		return consistenzaColl;
	}

	public void setConsistenzaColl(String consistenzaColl) {
		this.consistenzaColl = consistenzaColl;
	}

	public SubReportVO getRecInventario() {
		return recInventario;
	}

	public void setRecInventario(SubReportVO inventario) {
		this.recInventario = inventario;
	}


}
