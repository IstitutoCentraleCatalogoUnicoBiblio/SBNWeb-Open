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
package it.iccu.sbn.ejb.vo.gestionebibliografica.marca;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;


public class AreaDatiPerConfrontoCitazioniMarcheVO  extends SerializableVO {

	// = AreaDatiPerConfrontoCitazioniMarcheVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = -1503001830100801575L;
	List statoNewCitazioni = null;
	List statoOldCitazioni = null;

	public List getStatoNewCitazioni() {
		return statoNewCitazioni;
	}
	public void setStatoNewCitazioni(List statoNewCitazioni) {
		this.statoNewCitazioni = statoNewCitazioni;
	}
	public List getStatoOldCitazioni() {
		return statoOldCitazioni;
	}
	public void setStatoOldCitazioni(List statoOldCitazioni) {
		this.statoOldCitazioni = statoOldCitazioni;
	}
}
