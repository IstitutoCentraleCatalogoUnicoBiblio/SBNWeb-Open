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
package it.iccu.sbn.web.actionforms.gestionestampe.bibliotecari;

import org.apache.struts.action.ActionForm;

public class StampaBibliotecariForm extends ActionForm {

	private static final long serialVersionUID = -3811553029789149318L;
	private String elemBlocco;
	private String codiceBiblioteca;
	private String cognomeNome;
	private String ufficioAppartenenza;
	private String tipoFormato;

	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getElemBlocco() {
		return elemBlocco;
	}
	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}
	public String getCodiceBiblioteca() {
		return codiceBiblioteca;
	}
	public void setCodiceBiblioteca(String codiceBiblioteca) {
		this.codiceBiblioteca = codiceBiblioteca;
	}
	public String getCognomeNome() {
		return cognomeNome;
	}
	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}
	public String getUfficioAppartenenza() {
		return ufficioAppartenenza;
	}
	public void setUfficioAppartenenza(String ufficioAppartenenza) {
		this.ufficioAppartenenza = ufficioAppartenenza;
	}


}
