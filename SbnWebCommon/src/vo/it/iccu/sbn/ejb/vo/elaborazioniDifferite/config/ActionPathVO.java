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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite.config;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class ActionPathVO extends SerializableVO {

	private static final long serialVersionUID = -7174842761801840565L;

	private final String codAttivita;
	private final String actionPath;
	private final TipoAttivita tipo;

	private String descrizione;
	private boolean statico = true;

	public ActionPathVO(String codAttivita, String descrizione, String actionPath, TipoAttivita tipo) {
		this.codAttivita = codAttivita;
		this.descrizione = descrizione;
		this.actionPath = actionPath;
		this.tipo = tipo;
	}

	public String getCodAttivita() {
		return codAttivita;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getActionPath() {
		return actionPath;
	}

	public TipoAttivita getTipo() {
		return tipo;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public void setStatico(boolean statico) {
		this.statico = statico;
	}

	public boolean isStatico() {
		return statico;
	}

}
