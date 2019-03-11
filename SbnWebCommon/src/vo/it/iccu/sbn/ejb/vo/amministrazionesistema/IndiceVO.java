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
package it.iccu.sbn.ejb.vo.amministrazionesistema;

import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;

import java.io.Serializable;

public class IndiceVO implements Serializable{


	private static final long serialVersionUID = -3006263457323393793L;
	private SbnProfileType profilo;

	public IndiceVO(SbnProfileType profilo) {
		this.profilo = profilo;
	}

	public SbnProfileType getProfilo() {
		return profilo;
	}

	public void setProfilo(SbnProfileType profilo) {
		this.profilo = profilo;
	}

}
