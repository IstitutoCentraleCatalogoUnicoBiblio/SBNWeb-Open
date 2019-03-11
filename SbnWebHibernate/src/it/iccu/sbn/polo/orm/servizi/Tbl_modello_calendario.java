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

import it.iccu.sbn.polo.orm.Tb_base;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;

import java.util.Date;

public class Tbl_modello_calendario extends Tb_base {

	private static final long serialVersionUID = 5725835919990282779L;

	private int id_modello;
	private Tbf_biblioteca_in_polo biblioteca;
	private Tbl_sale sala;
	private char cd_tipo;
	private String cd_cat_mediazione;
	private String descrizione;
	private Date dt_inizio;
	private Date dt_fine;
	private String json_modello;

	public int getId_modello() {
		return id_modello;
	}

	public void setId_modello(int id_modello) {
		this.id_modello = id_modello;
	}

	public Tbf_biblioteca_in_polo getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(Tbf_biblioteca_in_polo biblioteca) {
		this.biblioteca = biblioteca;
	}

	public Tbl_sale getSala() {
		return sala;
	}

	public void setSala(Tbl_sale sala) {
		this.sala = sala;
	}

	public char getCd_tipo() {
		return cd_tipo;
	}

	public void setCd_tipo(char cd_tipo) {
		this.cd_tipo = cd_tipo;
	}

	public String getCd_cat_mediazione() {
		return cd_cat_mediazione;
	}

	public void setCd_cat_mediazione(String cd_cat_mediazione) {
		this.cd_cat_mediazione = cd_cat_mediazione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getDt_inizio() {
		return dt_inizio;
	}

	public void setDt_inizio(Date dt_inizio) {
		this.dt_inizio = dt_inizio;
	}

	public Date getDt_fine() {
		return dt_fine;
	}

	public void setDt_fine(Date dt_fine) {
		this.dt_fine = dt_fine;
	}

	public String getJson_modello() {
		return json_modello;
	}

	public void setJson_modello(String json_modello) {
		this.json_modello = json_modello;
	}

}
