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

import java.sql.Timestamp;

public class Tbl_accesso_utente extends Tb_base {

	private static final long serialVersionUID = -8878107691115131752L;

	private int id;
	private Tbf_biblioteca_in_polo biblioteca;
	private Tbl_utenti utente;
	private Tbl_posti_sala posto;
	private String id_tessera;
	private Timestamp ts_evento;
	private char fl_evento;
	private char fl_autenticato;
	private char fl_forzatura;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tbf_biblioteca_in_polo getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(Tbf_biblioteca_in_polo biblioteca) {
		this.biblioteca = biblioteca;
	}

	public Tbl_utenti getUtente() {
		return utente;
	}

	public void setUtente(Tbl_utenti utente) {
		this.utente = utente;
	}

	public Tbl_posti_sala getPosto() {
		return posto;
	}

	public void setPosto(Tbl_posti_sala posto) {
		this.posto = posto;
	}

	public String getId_tessera() {
		return id_tessera;
	}

	public void setId_tessera(String tessera) {
		this.id_tessera = tessera;
	}

	public Timestamp getTs_evento() {
		return ts_evento;
	}

	public void setTs_evento(Timestamp ts_evento) {
		this.ts_evento = ts_evento;
	}

	public char getFl_evento() {
		return fl_evento;
	}

	public void setFl_evento(char fl_evento) {
		this.fl_evento = fl_evento;
	}

	public char getFl_autenticato() {
		return fl_autenticato;
	}

	public void setFl_autenticato(char fl_autenticato) {
		this.fl_autenticato = fl_autenticato;
	}

	public char getFl_forzatura() {
		return fl_forzatura;
	}

	public void setFl_forzatura(char fl_forzatura) {
		this.fl_forzatura = fl_forzatura;
	}

}
