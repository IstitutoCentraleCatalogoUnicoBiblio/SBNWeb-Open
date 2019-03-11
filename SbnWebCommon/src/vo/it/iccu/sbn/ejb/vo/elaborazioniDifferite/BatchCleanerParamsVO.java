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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class BatchCleanerParamsVO extends SerializableVO {

	private static final long serialVersionUID = -7235697437298047389L;

	private boolean active = false;
	private boolean deleteOutputs = false;
	private int daysInterval = 15;

	public BatchCleanerParamsVO(boolean active, boolean deleteOutputs, int daysInterval) {
		this.active = active;
		this.deleteOutputs = deleteOutputs;
		this.daysInterval = daysInterval;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isDeleteOutputs() {
		return deleteOutputs;
	}

	public int getDaysInterval() {
		return daysInterval;
	}

}
