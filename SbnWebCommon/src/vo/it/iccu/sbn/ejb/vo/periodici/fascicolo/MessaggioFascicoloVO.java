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
package it.iccu.sbn.ejb.vo.periodici.fascicolo;

import it.iccu.sbn.ejb.vo.BaseVO;

public class MessaggioFascicoloVO extends BaseVO {

	private static final long serialVersionUID = 8890158268816953935L;
	private String codPolo;
	protected String codBib;

	private int cod_msg;

	private String bid;
	private int fid;

	// ordine
	private String cod_bib_ord;
	protected char cod_tip_ord;
	protected int anno_ord;
	protected int cod_ord;
	private int id_ordine;

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public int getCod_msg() {
		return cod_msg;
	}

	public void setCod_msg(int cod_msg) {
		this.cod_msg = cod_msg;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getCod_bib_ord() {
		return cod_bib_ord;
	}

	public void setCod_bib_ord(String cod_bib_ord) {
		this.cod_bib_ord = cod_bib_ord;
	}

	public char getCod_tip_ord() {
		return cod_tip_ord;
	}

	public void setCod_tip_ord(char cod_tip_ord) {
		this.cod_tip_ord = cod_tip_ord;
	}

	public int getAnno_ord() {
		return anno_ord;
	}

	public void setAnno_ord(int anno_ord) {
		this.anno_ord = anno_ord;
	}

	public int getCod_ord() {
		return cod_ord;
	}

	public void setCod_ord(int cod_ord) {
		this.cod_ord = cod_ord;
	}

	public int getId_ordine() {
		return id_ordine;
	}

	public void setId_ordine(int id_ordine) {
		this.id_ordine = id_ordine;
	}

}
