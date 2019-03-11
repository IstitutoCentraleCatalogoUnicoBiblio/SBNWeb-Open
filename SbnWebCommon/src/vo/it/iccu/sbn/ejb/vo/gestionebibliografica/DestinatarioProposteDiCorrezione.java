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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class DestinatarioProposteDiCorrezione extends SerializableVO {

	private static final long serialVersionUID = -2140673960332152713L;

	private String destinatariData;
	private String destinatariBiblio;
	private String destinatariNote;

	public String getDestinatariBiblio() {
		return destinatariBiblio;
	}
	public void setDestinatariBiblio(String destinatariBiblio) {
		this.destinatariBiblio = destinatariBiblio;
	}
	public String getDestinatariData() {
		return destinatariData;
	}
	public void setDestinatariData(String destinatariData) {
		this.destinatariData = destinatariData;
	}
	public String getDestinatariNote() {
		return destinatariNote;
	}
	public void setDestinatariNote(String destinatariNote) {
		this.destinatariNote = destinatariNote;
	}

}
