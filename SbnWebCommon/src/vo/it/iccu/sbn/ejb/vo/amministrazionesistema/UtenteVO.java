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
package it.iccu.sbn.ejb.vo.amministrazionesistema;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.sql.Timestamp;

public class UtenteVO extends BaseVO {

	private static final long serialVersionUID = 692418502562000796L;

	private String codPolo;
	private String biblioteca;
	private String nome;
	private String cognome;
	private String username;
	private String password;
	private String indice;
	private String acceso;
	private char change_password;
	private String dataVariazione;
	private String dataAccesso;
	private Timestamp tempoAccesso;
	private Timestamp tempoVariazione;
	private int inserito;
	private String ruolo;
	private String ufficio;
	private String note;
	private int id;
	private String abilitato;

	public String getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(String abilitato) {
		this.abilitato = abilitato;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = trimAndSet(nome);
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = trimAndSet(cognome);
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public String getAcceso() {
		return acceso;
	}

	public void setAcceso(String acceso) {
		this.acceso = acceso;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = trimAndSet(username);
	}

	public char getChange_password() {
		return change_password;
	}

	public void setChange_password(char change_password) {
		this.change_password = change_password;
	}

	public String getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public String getDataAccesso() {
		return dataAccesso;
	}

	public void setDataAccesso(String dataAccesso) {
		this.dataAccesso = dataAccesso;
	}

	public int getInserito() {
		return inserito;
	}

	public void setInserito(int inserito) {
		this.inserito = inserito;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getUfficio() {
		return ufficio;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getTempoAccesso() {
		return tempoAccesso;
	}

	public void setTempoAccesso(Timestamp tempoAccesso) {
		this.tempoAccesso = tempoAccesso;
	}

	public Timestamp getTempoVariazione() {
		return tempoVariazione;
	}

	public void setTempoVariazione(Timestamp tempoVariazione) {
		this.tempoVariazione = tempoVariazione;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

}
