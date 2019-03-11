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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.vo.BaseVO;

public class ComboVO extends BaseVO {

	private static final long serialVersionUID = -70713765699659995L;

	private String codice;
	private String descrizione;

	public ComboVO(String codice, String descrizione) {
		super();
		this.codice = codice;
		this.descrizione = descrizione;
	}

	public ComboVO() {
		super();
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescrizioneCodice() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.codice).append(" - ").append(this.descrizione);
		return buf.toString();
	}
}
