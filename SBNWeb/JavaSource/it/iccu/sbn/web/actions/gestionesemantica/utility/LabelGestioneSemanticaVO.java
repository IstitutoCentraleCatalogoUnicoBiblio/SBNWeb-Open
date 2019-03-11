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
package it.iccu.sbn.web.actions.gestionesemantica.utility;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class LabelGestioneSemanticaVO extends SerializableVO {


	private static final long serialVersionUID = -3990206535615722241L;
	private String id;
	private String tipoOggetto;
	private String key;
	private String livelloBaseDati;
	private String method;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipoOggetto() {
		return tipoOggetto;
	}

	public void setTipoOggetto(String tipoOggetto) {
		this.tipoOggetto = tipoOggetto;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLivelloBaseDati() {
		return livelloBaseDati;
	}

	public void setLivelloBaseDati(String livelloBaseDati) {
		this.livelloBaseDati = livelloBaseDati;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isGestione() {
		return type.equalsIgnoreCase("G");
	}

	public boolean isEsamina() {
		return type.equalsIgnoreCase("E");
	}

	public boolean isLegame() {
		return type.equalsIgnoreCase("L");
	}

}
