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
package it.iccu.sbn.vo.custom.periodici;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UnionEsamePeriodico extends SerializableVO {

	private static final long serialVersionUID = 6932289417075020276L;
	private int id_ordine;
	private String chiaveOrdine;
	private BigDecimal anno_abb_ord;
	private String primoOrdine;

	private int key_loc;
	private String consis;

	private String esemplare;
	private String cons_doc;

	private char cd_sit;
	private String seq_coll;
	private int anno_abb_inv;
	private Timestamp ts_ins_prima_coll;
	private String cd_serie;
	private int cd_inven;

	public int getId_ordine() {
		return id_ordine;
	}

	public void setId_ordine(int id_ordine) {
		this.id_ordine = id_ordine;
	}

	public String getChiaveOrdine() {
		return chiaveOrdine;
	}

	public void setChiaveOrdine(String chiaveOrdine) {
		this.chiaveOrdine = trimAndSet(chiaveOrdine);
	}

	public BigDecimal getAnno_abb_ord() {
		return anno_abb_ord;
	}

	public void setAnno_abb_ord(BigDecimal anno_abb_ord) {
		this.anno_abb_ord = anno_abb_ord;
	}

	public String getPrimoOrdine() {
		return primoOrdine;
	}

	public void setPrimoOrdine(String primoOrdine) {
		this.primoOrdine = trimAndSet(primoOrdine);
	}

	public int getKey_loc() {
		return key_loc;
	}

	public void setKey_loc(int key_loc) {
		this.key_loc = key_loc;
	}

	public String getConsis() {
		return consis;
	}

	public void setConsis(String consis) {
		this.consis = trimAndSet(consis);
	}

	public String getEsemplare() {
		return esemplare;
	}

	public void setEsemplare(String esemplare) {
		this.esemplare = trimAndSet(esemplare);
	}

	public String getCons_doc() {
		return cons_doc;
	}

	public void setCons_doc(String cons_doc) {
		this.cons_doc = trimAndSet(cons_doc);
	}

	public char getCd_sit() {
		return cd_sit;
	}

	public void setCd_sit(char cd_sit) {
		this.cd_sit = cd_sit;
	}

	public String getSeq_coll() {
		return seq_coll;
	}

	public void setSeq_coll(String seq_coll) {
		this.seq_coll = trimAndSet(seq_coll);
	}

	public int getAnno_abb_inv() {
		return anno_abb_inv;
	}

	public void setAnno_abb_inv(int anno_abb_inv) {
		this.anno_abb_inv = anno_abb_inv;
	}

	public Timestamp getTs_ins_prima_coll() {
		return ts_ins_prima_coll;
	}

	public void setTs_ins_prima_coll(Timestamp ts_ins_prima_coll) {
		this.ts_ins_prima_coll = ts_ins_prima_coll;
	}

	public String getCd_serie() {
		return cd_serie;
	}

	public void setCd_serie(String cd_serie) {
		this.cd_serie = cd_serie;
	}

	public int getCd_inven() {
		return cd_inven;
	}

	public void setCd_inven(int cd_inven) {
		this.cd_inven = cd_inven;
	}

}
