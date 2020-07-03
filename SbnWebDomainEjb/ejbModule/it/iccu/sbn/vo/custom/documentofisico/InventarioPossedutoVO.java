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
package it.iccu.sbn.vo.custom.documentofisico;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class InventarioPossedutoVO extends SerializableVO {

	private static final long serialVersionUID = -6393623947089061331L;

	private static final String NON_COLLOCATO = "[[:NO-COLL:]]";

	private String cd_serie;
	private int cd_inven;
	private String cd_sez;
	private String cd_loc;
	private String spec_loc;
	private String bid_coll;
	private String consis;
	private int key_loc;
	private String segnatura;

	private String stato_con;
	private int num_possessori;
	private String precis_inv;
	private String seq_coll;

	private String cd_bib;
	private String id_accesso_remoto;

	private String bid_inv;
	private String cd_frui;

	private String isbd_inv;
	private Character cd_natura_inv;

	private Integer anno_abb;

	private String cd_no_disp;
	private String cons_doc;

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

	public String getCd_sez() {
		return cd_sez;
	}

	public void setCd_sez(String cd_sez) {
		this.cd_sez = trimAndSet(cd_sez);
	}

	public String getCd_loc() {
		return cd_loc;
	}

	public void setCd_loc(String cd_loc) {
		this.cd_loc = trimAndSet(cd_loc);
	}

	public String getSpec_loc() {
		return spec_loc;
	}

	public void setSpec_loc(String spec_loc) {
		this.spec_loc = trimAndSet(spec_loc);
	}

	public String getBid_coll() {
		return bid_coll;
	}

	public void setBid_coll(String bid) {
		this.bid_coll = bid;
	}

	public String getConsis() {
		return consis;
	}

	public void setConsis(String consis) {
		this.consis = trimAndSet(consis);
	}

	public int getKey_loc() {
		return key_loc;
	}

	public void setKey_loc(int key_loc) {
		this.key_loc = key_loc;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = trimAndSet(segnatura);
	}

	public String getStato_con() {
		return stato_con;
	}

	public void setStato_con(String stato_con) {
		this.stato_con = trimAndSet(stato_con);
	}

	public int getNum_possessori() {
		return num_possessori;
	}

	public void setNum_possessori(int num_possessori) {
		this.num_possessori = num_possessori;
	}

	public String getPrecis_inv() {
		return precis_inv;
	}

	public void setPrecis_inv(String precis) {
		this.precis_inv = trimAndSet(precis);
	}

	public String getSeq_coll() {
		return seq_coll;
	}

	public void setSeq_coll(String seq_coll) {
		this.seq_coll = trimAndSet(seq_coll);
	}

	public String getCd_bib() {
		return cd_bib;
	}

	public void setCd_bib(String cd_bib) {
		this.cd_bib = cd_bib;
	}

	public String getId_accesso_remoto() {
		return id_accesso_remoto;
	}

	public void setId_accesso_remoto(String id_accesso_remoto) {
		this.id_accesso_remoto = trimAndSet(id_accesso_remoto);
	}

	public boolean isCollocato() {
		return !ValidazioneDati.equals(segnatura, NON_COLLOCATO);
	}

	public boolean hasPossessori() {
		return num_possessori > 0;
	}

	public String getBid_inv() {
		return bid_inv;
	}

	public void setBid_inv(String bidInv) {
		this.bid_inv = bidInv;
	}

	public String getCd_frui() {
		return cd_frui;
	}

	public void setCd_frui(String cd_frui) {
		this.cd_frui = cd_frui;
	}

	public String getIsbd_inv() {
		return isbd_inv;
	}

	public void setIsbd_inv(String isbd_inv) {
		this.isbd_inv = isbd_inv;
	}

	public Character getCd_natura_inv() {
		return cd_natura_inv;
	}

	public void setCd_natura_inv(Character cd_natura_inv) {
		this.cd_natura_inv = cd_natura_inv;
	}

	public Integer getAnno_abb() {
		return anno_abb;
	}

	public void setAnno_abb(Integer anno_abb) {
		this.anno_abb = anno_abb;
	}

	public String getCd_no_disp() {
		return cd_no_disp;
	}

	public void setCd_no_disp(String cd_no_disp) {
		this.cd_no_disp = cd_no_disp;
	}

	public String getCons_doc() {
		return cons_doc;
	}

	public void setCons_doc(String cons_doc) {
		this.cons_doc = cons_doc;
	}

}
