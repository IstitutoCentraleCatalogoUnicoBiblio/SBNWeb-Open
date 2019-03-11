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
package it.iccu.sbn.polo.orm.bibliografica.viste;

import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;

public class Vl_catalogo_editoria_posseduto extends Tb_titolo {

	private static final long serialVersionUID = 1L;

	private String isbn;

	private String cod_fornitore;
	private String nom_fornitore;
	private String paese;
	private String cod_regione;
	private String provincia;
	private String chiave_for;
	private String isbn_editore;

	private String nota_legame;

	private String cd_bib;
	private String cd_mat_inv;
	private String ultima_data_ingresso;
	private String tot_inv;
	private String tp_acquisizione;

	private String classe;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCod_fornitore() {
		return cod_fornitore;
	}

	public void setCod_fornitore(String cod_fornitore) {
		this.cod_fornitore = cod_fornitore;
	}

	public String getNom_fornitore() {
		return nom_fornitore;
	}

	public void setNom_fornitore(String nom_fornitore) {
		this.nom_fornitore = nom_fornitore;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getCod_regione() {
		return cod_regione;
	}

	public void setCod_regione(String cod_regione) {
		this.cod_regione = cod_regione;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getChiave_for() {
		return chiave_for;
	}

	public void setChiave_for(String chiave_for) {
		this.chiave_for = chiave_for;
	}

	public String getIsbn_editore() {
		return isbn_editore;
	}

	public void setIsbn_editore(String isbn_editore) {
		this.isbn_editore = isbn_editore;
	}

	public String getNota_legame() {
		return nota_legame;
	}

	public void setNota_legame(String nota_legame) {
		this.nota_legame = nota_legame;
	}

	public String getCd_bib() {
		return cd_bib;
	}

	public void setCd_bib(String cd_bib) {
		this.cd_bib = cd_bib;
	}

	public String getCd_mat_inv() {
		return cd_mat_inv;
	}

	public void setCd_mat_inv(String cd_mat_inv) {
		this.cd_mat_inv = cd_mat_inv;
	}

	public String getUltima_data_ingresso() {
		return ultima_data_ingresso;
	}

	public void setUltima_data_ingresso(String ultima_data_ingresso) {
		this.ultima_data_ingresso = ultima_data_ingresso;
	}

	public String getTot_inv() {
		return tot_inv;
	}

	public void setTot_inv(String tot_inv) {
		this.tot_inv = tot_inv;
	}

	public String getTp_acquisizione() {
		return tp_acquisizione;
	}

	public void setTp_acquisizione(String tp_acquisizione) {
		this.tp_acquisizione = tp_acquisizione;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

}
