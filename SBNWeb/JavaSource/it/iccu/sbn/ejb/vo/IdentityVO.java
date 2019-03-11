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
package it.iccu.sbn.ejb.vo;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

public class IdentityVO extends VersioneVO {

	private static final long serialVersionUID = 1457726046402693688L;

	private String codice;

	private String name;

	private String description;

	public IdentityVO() {

	}

	public IdentityVO(String codice, String description) {
		this.codice = codice;
		this.description = description;
	}

	public String getCodice() {
		return codice;
	}

	public int getCodiceAsInt() {
		return ValidazioneDati.strIsNumeric(codice) ? Integer.valueOf(codice) : -1;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descrizione) {
		this.description = descrizione;
	}
}
