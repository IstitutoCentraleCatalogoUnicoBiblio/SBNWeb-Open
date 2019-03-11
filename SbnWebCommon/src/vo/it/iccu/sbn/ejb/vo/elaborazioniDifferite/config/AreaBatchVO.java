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

import java.util.ArrayList;
import java.util.List;

public class AreaBatchVO extends SerializableVO {

	private static final long serialVersionUID = -4855172721981908103L;

	private String id;
	private List<ActionPathVO> attivita = new ArrayList<ActionPathVO>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ActionPathVO> getAttivita() {
		return attivita;
	}

	public void setAttivita(List<ActionPathVO> attivita) {
		this.attivita = attivita;
	}

	public List<String> getCodiciAttivita() {
		List<String> output = new ArrayList<String>();
		for (ActionPathVO action : attivita)
			output.add(action.getCodAttivita());
		return output;
	}

}
