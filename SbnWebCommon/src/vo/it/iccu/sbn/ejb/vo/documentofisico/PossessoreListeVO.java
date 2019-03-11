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

import java.io.Serializable;

public class PossessoreListeVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3122182156033520712L;

	private String prg;
	private String pid;
	private String nome;
	private String nomeFormaAccettata;
	private String forma;
	private String tipoNome;
	private String livello;
	private String note;

	public String getPrg() {
		return prg;
	}
	public void setPrg(String prg) {
		this.prg = prg;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getForma() {
		return forma;
	}
	public void setForma(String forma) {
		this.forma = forma;
	}
	public String getTipoNome() {
		return tipoNome;
	}
	public void setTipoNome(String tipoNome) {
		this.tipoNome = tipoNome;
	}
	public String getLivello() {
		return livello;
	}
	public void setLivello(String livello) {
		this.livello = livello;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNomeFormaAccettata() {
		return nomeFormaAccettata;
	}
	public void setNomeFormaAccettata(String nomeFormaAccettata) {
		this.nomeFormaAccettata = nomeFormaAccettata;
	}

	}
