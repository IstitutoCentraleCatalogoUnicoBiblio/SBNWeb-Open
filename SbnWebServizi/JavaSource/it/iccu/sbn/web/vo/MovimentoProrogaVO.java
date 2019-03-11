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
package it.iccu.sbn.web.vo;

import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;

import java.util.Date;

public class MovimentoProrogaVO extends MovimentoListaVO {

	private static final long serialVersionUID = -6377008077228193764L;
	private boolean prorogabile;
	private Date dataProrogaMin;
	private Date dataProrogaMax;

	public MovimentoProrogaVO(MovimentoListaVO mov) {
		copyCommonProperties(this, mov);
	}

	public boolean isProrogabile() {
		return prorogabile;
	}

	public void setProrogabile(boolean prorogabile) {
		this.prorogabile = prorogabile;
	}

	public Date getDataProrogaMin() {
		return dataProrogaMin;
	}

	public void setDataProrogaMin(Date dataProrogaMinima) {
		this.dataProrogaMin = dataProrogaMinima;
	}

	public Date getDataProrogaMax() {
		return dataProrogaMax;
	}

	public void setDataProrogaMax(Date dataProrogaMassima) {
		this.dataProrogaMax = dataProrogaMassima;
	}

}
