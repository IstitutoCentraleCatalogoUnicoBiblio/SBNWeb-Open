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
package it.iccu.sbn.polo.orm.servizi;

import java.util.Date;

public class Vl_richiesta_servizio extends Tbl_richiesta_servizio {

	private static final long serialVersionUID = 1L;

	private String cod_tipo_serv;
	private short progr_iter;
	private boolean fl_inventario;
	private String isbd;
	private String indice_isbd;
	private String kcles;

	private String bib_doc_lett;
	private Character tipo_doc_lett;
	private Long cod_doc_lett;
	private String segnatura;

	private String cd_sez;
	private String cd_loc;
	private String spec_loc;
	private String cognome;
	private String nome;
	private Character cd_natura;
	private String seq_coll;
	private Integer anno_abb;

	private String cd_polo;
	private String cd_bib;

	private Short progr_sollecito;
	private Date data_invio;

	public String getCod_tipo_serv() {
		return cod_tipo_serv;
	}

	public void setCod_tipo_serv(String cod_tipo_serv) {
		this.cod_tipo_serv = cod_tipo_serv;
	}

	public short getProgr_iter() {
		return progr_iter;
	}

	public void setProgr_iter(short progr_iter) {
		this.progr_iter = progr_iter;
	}

	public boolean isFl_inventario() {
		return fl_inventario;
	}

	public void setFl_inventario(boolean fl_inventario) {
		this.fl_inventario = fl_inventario;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = isbd;
	}

	public String getKcles() {
		return kcles;
	}

	public void setKcles(String kcles) {
		this.kcles = kcles;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}

	public String getCd_sez() {
		return cd_sez;
	}

	public void setCd_sez(String cd_sez) {
		this.cd_sez = cd_sez;
	}

	public String getCd_loc() {
		return cd_loc;
	}

	public void setCd_loc(String cd_loc) {
		this.cd_loc = cd_loc;
	}

	public String getSpec_loc() {
		return spec_loc;
	}

	public void setSpec_loc(String spec_loc) {
		this.spec_loc = spec_loc;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndice_isbd() {
		return indice_isbd;
	}

	public void setIndice_isbd(String indice_isbd) {
		this.indice_isbd = indice_isbd;
	}

	public Character getCd_natura() {
		return cd_natura;
	}

	public void setCd_natura(Character cd_natura) {
		this.cd_natura = cd_natura;
	}

	public String getSeq_coll() {
		return seq_coll;
	}

	public void setSeq_coll(String seq_coll) {
		this.seq_coll = seq_coll;
	}

	public Integer getAnno_abb() {
		return anno_abb;
	}

	public void setAnno_abb(Integer anno_abb) {
		this.anno_abb = anno_abb;
	}

	public String getCd_polo() {
		return cd_polo;
	}

	public void setCd_polo(String cd_polo) {
		this.cd_polo = cd_polo;
	}

	public String getCd_bib() {
		return cd_bib;
	}

	public void setCd_bib(String cd_bib) {
		this.cd_bib = cd_bib;
	}

	public Short getProgr_sollecito() {
		return progr_sollecito;
	}

	public void setProgr_sollecito(Short progr_sollecito) {
		this.progr_sollecito = progr_sollecito;
	}

	public Date getData_invio() {
		return data_invio;
	}

	public void setData_invio(Date data_invio) {
		this.data_invio = data_invio;
	}

	public String getBib_doc_lett() {
		return bib_doc_lett;
	}

	public void setBib_doc_lett(String bib_doc_lett) {
		this.bib_doc_lett = bib_doc_lett;
	}

	public Character getTipo_doc_lett() {
		return tipo_doc_lett;
	}

	public void setTipo_doc_lett(Character tipo_doc_lett) {
		this.tipo_doc_lett = tipo_doc_lett;
	}

	public Long getCod_doc_lett() {
		return cod_doc_lett;
	}

	public void setCod_doc_lett(Long cod_doc_lett) {
		this.cod_doc_lett = cod_doc_lett;
	}

}
