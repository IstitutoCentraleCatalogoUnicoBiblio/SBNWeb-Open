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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ModelloDefaultVO extends SerializableVO {

	private static final long serialVersionUID = 8821979009054893747L;

	private String codPolo;
	private String codBib;
	private String codModello;
	private String descrModello;
	private String formatoStampa;
	//
	private String descrBibModello;
	//
	private String codScarico;

	private String n_copie;
	private String n_copie_richiamo;

	private String n_copie_tit;
	private String n_copie_edi;
	private String n_copie_poss;
	private String n_copie_aut;
	private String n_copie_top;
	private String n_copie_sog;
	private String n_copie_cla;


	private String sch_autori;
	private String sch_topog;
	private String sch_classi;
	private String sch_titoli;
	private String sch_edit;
	private String sch_poss;
	private String sch_soggetti;

	private String fl_coll_aut;
	private String fl_coll_top;
	private String fl_coll_sog;
	private String fl_coll_cla;
	private String fl_coll_tit;
	private String fl_coll_edi;
	private String fl_coll_poss;
	private String flg_coll_richiamo;

	private String fl_tipo_leg;
	private String fl_non_inv;

	private String n_serie;
	private String n_piste;
	//
	private String uteIns;
	private String uteAgg;
	private String dataIns;
	private String dataAgg;


	/**
	 *
	 */
	public ModelloDefaultVO(){
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


	public String getCodModello() {
		return codModello;
	}


	public void setCodModello(String codModello) {
		this.codModello = trimAndSet(codModello);;
	}


	public String getDescrModello() {
		return descrModello;
	}


	public void setDescrModello(String descrModello) {
		this.descrModello = descrModello;
	}


	public String getUteIns() {
		return uteIns;
	}


	public void setUteIns(String uteIns) {
		this.uteIns = uteIns;
	}


	public String getUteAgg() {
		return uteAgg;
	}


	public void setUteAgg(String uteAgg) {
		this.uteAgg = uteAgg;
	}


	public String getDataIns() {
		return dataIns;
	}


	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}


	public String getDataAgg() {
		return dataAgg;
	}


	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}


	public String getFormatoStampa() {
		return formatoStampa;
	}


	public void setFormatoStampa(String formatoStampa) {
		this.formatoStampa = formatoStampa;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public String getCodScarico() {
		return codScarico;
	}


	public void setCodScarico(String codScarico) {
		this.codScarico = codScarico;
	}


	public String getN_copie_tit() {
		return n_copie_tit;
	}


	public void setN_copie_tit(String n_copie_tit) {
		this.n_copie_tit = n_copie_tit;
	}


	public String getN_copie_edi() {
		return n_copie_edi;
	}


	public void setN_copie_edi(String n_copie_edi) {
		this.n_copie_edi = n_copie_edi;
	}


	public String getN_copie_poss() {
		return n_copie_poss;
	}


	public void setN_copie_poss(String n_copie_poss) {
		this.n_copie_poss = n_copie_poss;
	}


	public String getN_copie_richiamo() {
		return n_copie_richiamo;
	}


	public void setN_copie_richiamo(String n_copie_richiamo) {
		this.n_copie_richiamo = n_copie_richiamo;
	}


	public String getSch_autori() {
		return sch_autori;
	}


	public void setSch_autori(String sch_autori) {
		this.sch_autori = sch_autori;
	}


	public String getFl_coll_aut() {
		return fl_coll_aut;
	}


	public void setFl_coll_aut(String fl_coll_aut) {
		this.fl_coll_aut = fl_coll_aut;
	}


	public String getFl_tipo_leg() {
		return fl_tipo_leg;
	}


	public void setFl_tipo_leg(String fl_tipo_leg) {
		this.fl_tipo_leg = fl_tipo_leg;
	}


	public String getSch_topog() {
		return sch_topog;
	}


	public void setSch_topog(String sch_topog) {
		this.sch_topog = sch_topog;
	}


	public String getFl_coll_top() {
		return fl_coll_top;
	}


	public void setFl_coll_top(String fl_coll_top) {
		this.fl_coll_top = fl_coll_top;
	}


	public String getSch_soggetti() {
		return sch_soggetti;
	}


	public void setSch_soggetti(String sch_soggetti) {
		this.sch_soggetti = sch_soggetti;
	}


	public String getFl_coll_sog() {
		return fl_coll_sog;
	}


	public void setFl_coll_sog(String fl_coll_sog) {
		this.fl_coll_sog = fl_coll_sog;
	}


	public String getSch_classi() {
		return sch_classi;
	}


	public void setSch_classi(String sch_classi) {
		this.sch_classi = sch_classi;
	}


	public String getFl_coll_cla() {
		return fl_coll_cla;
	}


	public void setFl_coll_cla(String fl_coll_cla) {
		this.fl_coll_cla = fl_coll_cla;
	}


	public String getSch_titoli() {
		return sch_titoli;
	}


	public void setSch_titoli(String sch_titoli) {
		this.sch_titoli = sch_titoli;
	}


	public String getFl_coll_tit() {
		return fl_coll_tit;
	}


	public void setFl_coll_tit(String fl_coll_tit) {
		this.fl_coll_tit = fl_coll_tit;
	}


	public String getSch_edit() {
		return sch_edit;
	}


	public void setSch_edit(String sch_edit) {
		this.sch_edit = sch_edit;
	}


	public String getFl_coll_edi() {
		return fl_coll_edi;
	}


	public void setFl_coll_edi(String fl_coll_edi) {
		this.fl_coll_edi = fl_coll_edi;
	}


	public String getSch_poss() {
		return sch_poss;
	}


	public void setSch_poss(String sch_poss) {
		this.sch_poss = sch_poss;
	}


	public String getFl_coll_poss() {
		return fl_coll_poss;
	}


	public void setFl_coll_poss(String fl_coll_poss) {
		this.fl_coll_poss = fl_coll_poss;
	}


	public String getFlg_coll_richiamo() {
		return flg_coll_richiamo;
	}


	public void setFlg_coll_richiamo(String flg_coll_richiamo) {
		this.flg_coll_richiamo = flg_coll_richiamo;
	}


	public String getFl_non_inv() {
		return fl_non_inv;
	}


	public void setFl_non_inv(String fl_non_inv) {
		this.fl_non_inv = fl_non_inv;
	}


	public String getN_serie() {
		return n_serie;
	}


	public void setN_serie(String n_serie) {
		this.n_serie = n_serie;
	}


	public String getN_piste() {
		return n_piste;
	}


	public void setN_piste(String n_piste) {
		this.n_piste = n_piste;
	}


	public String getN_copie() {
		return n_copie;
	}


	public void setN_copie(String n_copie) {
		this.n_copie = n_copie;
	}


	public String getN_copie_aut() {
		return n_copie_aut;
	}


	public void setN_copie_aut(String n_copie_aut) {
		this.n_copie_aut = n_copie_aut;
	}


	public String getN_copie_top() {
		return n_copie_top;
	}


	public void setN_copie_top(String n_copie_top) {
		this.n_copie_top = n_copie_top;
	}


	public String getN_copie_sog() {
		return n_copie_sog;
	}


	public void setN_copie_sog(String n_copie_sog) {
		this.n_copie_sog = n_copie_sog;
	}


	public String getN_copie_cla() {
		return n_copie_cla;
	}


	public void setN_copie_cla(String n_copie_cla) {
		this.n_copie_cla = n_copie_cla;
	}


	public String getDescrBibModello() {
		return descrBibModello;
	}


	public void setDescrBibModello(String descrBibModello) {
		this.descrBibModello = descrBibModello;
	}

}
