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

import it.iccu.sbn.ejb.vo.SerializableVO;

public class BidInventarioSegnaturaVO extends SerializableVO implements Comparable<BidInventarioSegnaturaVO>{
	/**
	 *
	 */
	private static final long serialVersionUID = -543900038710172232L;

	private String bid;
	private String biblioteca;
	private String sezione;
	private String collocazione;
	private String specificazione;
	private String sequenza;
	private String serie;
	private String inventario;
	//per stampa schede
	private String precisazione;
	private String codSit;

	public BidInventarioSegnaturaVO(){
	}

	public BidInventarioSegnaturaVO(String bid, String descrBib1,
			String codSez, String codLoc, String specLoc, String seqColl, String codSerie, String codInv){

		//gv: campi di detaglio di una singola etichetta
		//---------------------------------------------------------------
		this.bid=bid; //bid
		this.biblioteca=descrBib1; //descrizione della biblioteca
		this.sezione=codSez;   //sezione (10)
		this.collocazione=codLoc;   //collocazione (24)
		this.specificazione=specLoc; //specificazione (12)
		this.sequenza=seqColl; //sequenza della collocazione (24?)
		this.serie=codSerie; //serie (3)
		this.inventario=codInv;     //inventario (9)
		//---------------------------------------------------------------
	}
	public BidInventarioSegnaturaVO(String bid, String descrBib1,
			String codSez, String codLoc, String specLoc, String seqColl, String codSerie, String codInv, String prec){

		//campi di detaglio di una singola scheda
		//---------------------------------------------------------------
		this.bid=bid; //bid
		this.biblioteca=descrBib1; //descrizione della biblioteca
		this.sezione=codSez;   //sezione (10)
		this.collocazione=codLoc;   //collocazione (24)
		this.specificazione=specLoc; //specificazione (12)
		this.sequenza=seqColl; //sequenza della collocazione (24?)
		this.serie=codSerie; //serie (3)
		this.inventario=codInv;     //inventario (9)
		this.precisazione=prec;     //inventario (9)
		//---------------------------------------------------------------
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = collocazione;
	}

	public String getSpecificazione() {
		return specificazione;
	}

	public void setSpecificazione(String specificazione) {
		this.specificazione = specificazione;
	}

	public String getSequenza() {
		return sequenza;
	}

	public void setSequenza(String sequenza) {
		this.sequenza = sequenza;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

	public String getPrecisazione() {
		return precisazione;
	}

	public void setPrecisazione(String precisazione) {
		this.precisazione = precisazione;
	}

	public int compareTo(BidInventarioSegnaturaVO o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getCodSit() {
		return codSit;
	}

	public void setCodSit(String codSit) {
		this.codSit = codSit;
	}

//	public int compareTo(EtichettaDettaglioVO o) {
//		int ord = 0;
//		if (this.serie != null)
//			ord = this.serie.compareTo(o.serie);
//
//		if (ord != 0)
//			return ord;
//		else
//			if (this.inventario != null && isNumeric(this.inventario)) {
//				int inv = Integer.parseInt(this.inventario);
//				int invO = Integer.parseInt(o.inventario);
//				ord = inv - invO;
//			}
//
//		return ord;
//	}
}
