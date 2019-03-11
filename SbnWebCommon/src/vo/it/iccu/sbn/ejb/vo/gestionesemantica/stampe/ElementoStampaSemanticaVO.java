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
package it.iccu.sbn.ejb.vo.gestionesemantica.stampe;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;

public abstract class ElementoStampaSemanticaVO extends SerializableVO {

	private static final long serialVersionUID = 1097518710587006708L;
	protected String id;
	protected String testo;
	protected String note;
	protected String livelloAutorita;
	protected List<LegameTitoloVO> legamiTitoli;

	public void setId(String id) {
		this.id = trimAndSet(id);
	}

	public String getId() {
		return id;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = trimAndSet(testo);
	}

	public void setLivelloAutorita(String cd_livello) {
		livelloAutorita = trimAndSet(cd_livello);
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public List<LegameTitoloVO> getLegamiTitoli() {
		return legamiTitoli;
	}

	public void setLegamiTitoli(List<LegameTitoloVO> legamiTitoli) {
		this.legamiTitoli = legamiTitoli;
	}

}
