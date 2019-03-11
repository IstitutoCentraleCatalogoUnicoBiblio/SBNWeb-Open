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
package it.iccu.sbn.ejb.vo.amministrazionesistema.custom;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class BibliotecaSearch extends SerializableVO {

	private static final long serialVersionUID = -8121630426960023790L;
	private String codPolo;
	private String codBib;
	private String tipoBib;
	private String nomeBib;
	private String enteApp;
	private String provincia;
	private String paese;

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getEnteApp() {
		return enteApp;
	}

	public void setEnteApp(String enteApp) {
		this.enteApp = enteApp;
	}

	public String getNomeBib() {
		return nomeBib;
	}

	public void setNomeBib(String nomeBib) {
		this.nomeBib = nomeBib;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTipoBib() {
		return tipoBib;
	}

	public void setTipoBib(String tipoBib) {
		this.tipoBib = tipoBib;
	}
}
