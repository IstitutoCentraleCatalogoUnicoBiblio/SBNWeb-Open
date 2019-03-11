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
package it.iccu.sbn.batch.unimarc;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Date;

public class Inventario950 extends SerializableVO {

	private static final long serialVersionUID = 2301645976858699262L;

	private String codInventario;
	private String codFrui;
	private Date dataIngresso;

	public String getCodInventario() {
		return codInventario;
	}

	public Inventario950(String codInventario, String codFrui,	Date dataIngresso) {
		super();
		this.codInventario = codInventario;
		this.codFrui = codFrui;
		this.dataIngresso = dataIngresso;
	}

	public void setCodInventario(String codInventario) {
		this.codInventario = codInventario;
	}

	public String getCodFrui() {
		return codFrui;
	}

	public void setCodFrui(String codFrui) {
		this.codFrui = codFrui;
	}

	public Date getDataIngresso() {
		return dataIngresso;
	}

	public void setDataIngresso(Date dataIngresso) {
		this.dataIngresso = dataIngresso;
	}

}
