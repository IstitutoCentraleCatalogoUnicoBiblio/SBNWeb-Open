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
package it.iccu.sbn.ejb.vo.stampe;

import java.util.List;

public class StampaOnLineVO extends StampaVo {

	private static final long serialVersionUID = -7690103744996553017L;

	private List datiStampa;
	private String etichetteVuoteIniziali;

	public List getDatiStampa() {
		return datiStampa;
	}

	public void setDatiStampa(List datiStampa) {
		this.datiStampa = datiStampa;
	}

	public String getEtichetteVuoteIniziali() {
		return etichetteVuoteIniziali;
	}

	public void setEtichetteVuoteIniziali(String etichetteVuoteIniziali) {
		this.etichetteVuoteIniziali = etichetteVuoteIniziali;
	}

}
