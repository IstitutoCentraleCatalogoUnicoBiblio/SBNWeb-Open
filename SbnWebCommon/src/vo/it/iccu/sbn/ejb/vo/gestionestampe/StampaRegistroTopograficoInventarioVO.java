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


/**
 * VO per il dettaglio della stampa registro topografico
 */
public class StampaRegistroTopograficoInventarioVO extends StampaRegistroTopograficoCollocazioneVO {

	private static final long serialVersionUID = -543900038710172232L;

	private String bidInv; //non stampare
	private String bidDescr;
	private String bidKyCles2t; //non stampare
	private String bidDescrSup;
	private String consistenzaDoc;
	private String numInventario;
	private String seqColl;

	// Attributes

	public StampaRegistroTopograficoInventarioVO(ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	}

	public StampaRegistroTopograficoInventarioVO(String bidInv, String bidDescr, String bidKyCles2t, String bidDescrSup, String consistezaDoc, String serie,
			String numInventario, String seqColl, ParametriRichiestaElaborazioneDifferitaVO input){
		super(input);
	this.bidInv = bidInv;
	this.bidDescr = bidDescr;
	this.bidKyCles2t = bidKyCles2t;
	this.bidDescrSup = bidDescrSup;
	this.consistenzaDoc = consistezaDoc;
	super.setSerie(serie);
	this.numInventario = numInventario;
	this.seqColl = seqColl;
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

	public String getConsistenzaDoc() {
		return consistenzaDoc;
	}

	public void setConsistenzaDoc(String consistezaDoc) {
		this.consistenzaDoc = consistezaDoc;
	}

	public String getNumInventario() {
		return numInventario;
	}

	public void setNumInventario(String numInventario) {
		this.numInventario = numInventario;
	}

	public String getSeqColl() {
		return seqColl;
	}

	public void setSeqColl(String seqColl) {
		this.seqColl = seqColl;
	}

}
