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
package it.iccu.sbn.ejb.vo.periodici.previsionale;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.List;

public class ModelloPrevisionaleVO extends BaseVO {

	private static final long serialVersionUID = -6733521442294181744L;

	private int id_modello;
	private String nome;
	private String descrizione;

	protected String cd_per;
	protected String num_primo_volume;
	protected int num_fascicoli_per_volume;
	protected String num_primo_fascicolo;
	protected boolean num_fascicolo_continuativo;
	protected String cd_tipo_num_fascicolo;

	protected List<GiornoMeseVO> listaEscludiDate = ValidazioneDati.emptyList();
	protected List<GiornoMeseVO> listaIncludiDate = ValidazioneDati.emptyList();

	protected List<Integer> listaEscludiGiorni =  ValidazioneDati.emptyList();

	//almaviva5_20110704 #4522
	protected List<Integer> listaIncludiGiorni =  ValidazioneDati.emptyList();




	public int getId_modello() {
		return id_modello;
	}

	public void setId_modello(int id_modello) {
		this.id_modello = id_modello;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCd_per() {
		return cd_per;
	}

	public void setCd_per(String cd_per) {
		this.cd_per = cd_per;
	}

	public String getNum_primo_volume() {
		return num_primo_volume;
	}

	public void setNum_primo_volume(String num_primo_volume) {
		this.num_primo_volume = num_primo_volume;
	}

	public int getNum_fascicoli_per_volume() {
		return num_fascicoli_per_volume;
	}

	public void setNum_fascicoli_per_volume(int num_fascicoli_per_volume) {
		this.num_fascicoli_per_volume = num_fascicoli_per_volume;
	}

	public String getNum_primo_fascicolo() {
		return num_primo_fascicolo;
	}

	public void setNum_primo_fascicolo(String num_primo_fascicolo) {
		this.num_primo_fascicolo = num_primo_fascicolo;
	}

	public List<GiornoMeseVO> getListaEscludiDate() {
		return listaEscludiDate;
	}

	public void setListaEscludiDate(List<GiornoMeseVO> listaEscludiDate) {
		this.listaEscludiDate = listaEscludiDate;
	}

	public List<GiornoMeseVO> getListaIncludiDate() {
		return listaIncludiDate;
	}

	public void setListaIncludiDate(List<GiornoMeseVO> listaIncludiDate) {
		this.listaIncludiDate = listaIncludiDate;
	}

	public List<Integer> getListaEscludiGiorni() {
		return listaEscludiGiorni;
	}

	public void setListaEscludiGiorni(List<Integer> listaEscludiGiorni) {
		this.listaEscludiGiorni = listaEscludiGiorni;
	}

	public boolean isNum_fascicolo_continuativo() {
		return num_fascicolo_continuativo;
	}

	public void setNum_fascicolo_continuativo(boolean num_fascicolo_continuativo) {
		this.num_fascicolo_continuativo = num_fascicolo_continuativo;
	}

	public void setCd_tipo_num_fascicolo(String cd_tipo_num_fascicolo) {
		this.cd_tipo_num_fascicolo = cd_tipo_num_fascicolo;
	}

	public String getCd_tipo_num_fascicolo() {
		return cd_tipo_num_fascicolo;
	}

	public List<Integer> getListaIncludiGiorni() {
		return listaIncludiGiorni;
	}

	public void setListaIncludiGiorni(List<Integer> listaIncludiGiorni) {
		this.listaIncludiGiorni = listaIncludiGiorni;
	}

}
