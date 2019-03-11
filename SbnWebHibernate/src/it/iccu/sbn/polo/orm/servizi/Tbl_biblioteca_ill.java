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
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;

public class Tbl_biblioteca_ill extends Tb_base {

	private static final long serialVersionUID = 7417514775599197940L;

	private int id_biblioteca_ill;
	private String cd_isil;
	private Tbf_biblioteca id_biblioteca;
	private Tbl_utenti id_utente;
	private String ds_biblioteca;
	private char fl_ruolo;
	private Character fl_servprestito;
	private Character fl_servdd;

	public int getId_biblioteca_ill() {
		return id_biblioteca_ill;
	}

	public void setId_biblioteca_ill(int id_biblioteca_ill) {
		this.id_biblioteca_ill = id_biblioteca_ill;
	}

	public String getCd_isil() {
		return cd_isil;
	}

	public void setCd_isil(String cd_isil) {
		this.cd_isil = cd_isil;
	}

	public Tbf_biblioteca getId_biblioteca() {
		return id_biblioteca;
	}

	public void setId_biblioteca(Tbf_biblioteca id_biblioteca) {
		this.id_biblioteca = id_biblioteca;
	}

	public Tbl_utenti getId_utente() {
		return id_utente;
	}

	public void setId_utente(Tbl_utenti id_utente) {
		this.id_utente = id_utente;
	}

	public String getDs_biblioteca() {
		return ds_biblioteca;
	}

	public void setDs_biblioteca(String ds_biblioteca) {
		this.ds_biblioteca = ds_biblioteca;
	}

	public char getFl_ruolo() {
		return fl_ruolo;
	}

	public void setFl_ruolo(char fl_ruolo) {
		this.fl_ruolo = fl_ruolo;
	}

	public Character getFl_servprestito() {
		return fl_servprestito;
	}

	public void setFl_servprestito(Character fl_servprestito) {
		this.fl_servprestito = fl_servprestito;
	}

	public Character getFl_servdd() {
		return fl_servdd;
	}

	public void setFl_servdd(Character fl_servdd) {
		this.fl_servdd = fl_servdd;
	}
}
