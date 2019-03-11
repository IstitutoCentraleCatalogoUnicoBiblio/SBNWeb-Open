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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.Date;

public class EsemplareFascicoloVO extends BaseVO {

	private static final long serialVersionUID = -5088109442618399592L;

	private String codPolo;
	protected String codBib;

	private String bid;
	private int fid;

	// dati esemplare
	private int id_ese_fascicolo;

	private String cd_bib_abb;
	private Date data_arrivo;
	private Integer grp_fasc;
	private String note;

	private Character cd_no_disp;

	// ordine
	private String cod_bib_ord;
	protected char cod_tip_ord;
	protected int anno_ord;
	protected int cod_ord;
	private int id_ordine;

	// inventario
	protected String cd_serie;
	protected int cd_inven;

	// collocazione
	protected String codSez;
	protected int key_loc;
	protected String cd_loc;
	protected String spec_loc;


	public EsemplareFascicoloVO(EsemplareFascicoloVO ef) {
		super(ef);
	    this.codPolo = ef.codPolo;
	    this.codBib = ef.codBib;
	    this.bid = ef.bid;
	    this.fid = ef.fid;
	    this.id_ese_fascicolo = ef.id_ese_fascicolo;
	    this.cd_bib_abb = ef.cd_bib_abb;
	    this.data_arrivo = ef.data_arrivo;
	    this.grp_fasc = ef.grp_fasc;
	    this.note = ef.note;
	    this.cd_no_disp = ef.cd_no_disp;
	    this.cod_bib_ord = ef.cod_bib_ord;
	    this.cod_tip_ord = ef.cod_tip_ord;
	    this.anno_ord = ef.anno_ord;
	    this.cod_ord = ef.cod_ord;
	    this.id_ordine = ef.id_ordine;
	    this.cd_serie = ef.cd_serie;
	    this.cd_inven = ef.cd_inven;
	    this.codSez = ef.codSez;
	    this.key_loc = ef.key_loc;
	    this.cd_loc = ef.cd_loc;
	    this.spec_loc = ef.spec_loc;
	}

	public EsemplareFascicoloVO(FascicoloVO f) {
		this.codPolo = f.getCodPolo();
		this.codBib = f.getCodBib();
		this.bid = f.getBid();
		this.fid = f.getFid();
	}

	public EsemplareFascicoloVO() {
		super();
	}



	public boolean isNuovo() {
		return (id_ese_fascicolo == 0);
	}

	public boolean isCancellato() {
		return ValidazioneDati.equalsIgnoreCase(flCanc, "S");
	}

	public boolean isLegatoOrdine() {
		return id_ordine > 0 || (isFilled(anno_ord) && isFilled(cod_tip_ord) && cod_ord > 0);
	}

	public boolean isLegatoInventario() {
		return isFilled(codPolo) && isFilled(codBib) && cd_inven > 0;
	}

	public int getId_ese_fascicolo() {
		return id_ese_fascicolo;
	}

	public void setId_ese_fascicolo(int id_ese_fascicolo) {
		this.id_ese_fascicolo = id_ese_fascicolo;
	}

	public String getCd_bib_abb() {
		return cd_bib_abb;
	}

	public void setCd_bib_abb(String cd_bib_abb) {
		this.cd_bib_abb = cd_bib_abb;
	}

	public Date getData_arrivo() {
		return data_arrivo;
	}

	public void setData_arrivo(Date data_arrivo) {
		this.data_arrivo = data_arrivo;
	}

	public Integer getGrp_fasc() {
		return grp_fasc;
	}

	public void setGrp_fasc(Integer grp_fasc) {
		this.grp_fasc = grp_fasc;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
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

	public int getKey_loc() {
		return key_loc;
	}

	public void setKey_loc(int key_loc) {
		this.key_loc = key_loc;
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

	public String getCodSez() {
		return codSez;
	}

	public void setCodSez(String codSez) {
		this.codSez = trimAndSet(codSez);
	}

	public Character getCd_no_disp() {
		return cd_no_disp;
	}

	public void setCd_no_disp(Character cd_no_disp) {
		this.cd_no_disp = cd_no_disp;
	}

}
