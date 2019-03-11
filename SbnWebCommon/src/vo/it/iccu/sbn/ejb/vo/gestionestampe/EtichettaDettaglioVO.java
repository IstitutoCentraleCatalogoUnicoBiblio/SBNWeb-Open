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
import it.iccu.sbn.ejb.vo.SerializableVO;

public class EtichettaDettaglioVO extends SerializableVO implements	Comparable<EtichettaDettaglioVO> {

	private static final long serialVersionUID = -543900038710172232L;

	private String biblioteca;
	private String sezione;
	private String collocazione;
	private String specificazione;
	private String sequenza;
	private String serie;
	private String inventario;
	// per stampa schede
	private String precisazione;

	private String ord_loc;
	private String ord_spec;

	private String codSit;

	public String getOrd_loc() {
		return ord_loc;
	}

	public void setOrd_loc(String ord_loc) {
		this.ord_loc = trimAndSet(ord_loc);
	}

	public String getOrd_spec() {
		return ord_spec;
	}

	public void setOrd_spec(String ord_spec) {
		this.ord_spec = trimAndSet(ord_spec);
	}

	public EtichettaDettaglioVO() {
		super();
	}

	public EtichettaDettaglioVO(String descrBib1, /*
												 * String descrBib2, String
												 * descrBib3,
												 */
	String codSez, String codLoc, String specLoc, String seqColl,
			String codSerie, String codInv) {

		// gv: campi di detaglio di una singola etichetta
		// ---------------------------------------------------------------
		this.biblioteca = descrBib1; // descrizione della biblioteca
		this.sezione = codSez; // sezione (10)
		this.collocazione = codLoc; // collocazione (24)
		this.specificazione = specLoc; // specificazione (12)
		this.sequenza = seqColl; // sequenza della collocazione (24?)
		this.serie = codSerie; // serie (3)
		this.inventario = codInv; // inventario (9)
		// ---------------------------------------------------------------
	}

	public EtichettaDettaglioVO(String descrBib1, /*
												 * String descrBib2, String
												 * descrBib3,
												 */
	String codSez, String codLoc, String specLoc, String seqColl,
			String codSerie, String codInv, String prec) {

		// campi di detaglio di una singola scheda
		// ---------------------------------------------------------------
		this.biblioteca = descrBib1; // descrizione della biblioteca
		this.sezione = codSez; // sezione (10)
		this.collocazione = codLoc; // collocazione (24)
		this.specificazione = specLoc; // specificazione (12)
		this.sequenza = seqColl; // sequenza della collocazione (24?)
		this.serie = codSerie; // serie (3)
		this.inventario = codInv; // inventario (9)
		this.precisazione = prec; // inventario (9)
		// ---------------------------------------------------------------
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
		this.sezione = trimAndSet(sezione);
	}

	public String getCollocazione() {
		return collocazione;
	}

	public void setCollocazione(String collocazione) {
		this.collocazione = trimAndSet(collocazione);
	}

	public String getSpecificazione() {
		return specificazione;
	}

	public void setSpecificazione(String specificazione) {
		this.specificazione = trimAndSet(specificazione);
	}

	public String getSequenza() {
		return sequenza;
	}

	public void setSequenza(String sequenza) {
		this.sequenza = trimAndSet(sequenza);
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
		this.inventario = trimAndSet(inventario);
	}

	public String getPrecisazione() {
		return precisazione;
	}

	public void setPrecisazione(String precisazione) {
		this.precisazione = precisazione;
	}

	public int compareTo(EtichettaDettaglioVO o) {

		//almaviva5_20110914 #4621 aggiunti campi ordinamento collocazione
		int cmp = ValidazioneDati.compare(sezione, o.sezione);
		cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(ord_loc, o.ord_loc);
		cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(ord_spec, o.ord_spec);

		cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(serie, o.serie);

		if (cmp != 0)
			return cmp;
		else if (isNumeric(inventario) && isNumeric(o.inventario)) {
			int inv1 = Integer.parseInt(this.inventario);
			int inv2 = Integer.parseInt(o.inventario);
			cmp = inv1 - inv2;
		}

		return cmp;
	}

	public String getCodSit() {
		return codSit;
	}

	public void setCodSit(String codSit) {
		this.codSit = codSit;
	}

}
