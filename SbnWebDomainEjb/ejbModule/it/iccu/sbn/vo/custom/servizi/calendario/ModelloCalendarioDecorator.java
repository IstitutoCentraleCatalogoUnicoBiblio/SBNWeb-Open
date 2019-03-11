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
package it.iccu.sbn.vo.custom.servizi.calendario;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.servizi.calendario.IntervalloVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;

import java.util.Date;

public class ModelloCalendarioDecorator extends ModelloCalendarioVO {

	private static final long serialVersionUID = 8817448354336856064L;

	protected ModelloCalendarioDecorator(ModelloCalendarioVO mc) {
		super(mc);
		for (IntervalloVO i : mc.getIntervalli()) {
			IntervalloVO id = new IntervalloDecorator(i);
			this.getIntervalli().add(id);
		}
	}

	public static ModelloCalendarioVO decorate (ModelloCalendarioVO mc) {
		return new ModelloCalendarioDecorator(mc);
	}

	public String getDataInizio() {
		Date inizio = getInizio();
		return (inizio == null ? null : DateUtil.formattaData(inizio));
	}

	public void setDataInizio(String dataInizio) {
		this.setInizio(DateUtil.toDate(dataInizio));
	}

	public String getDataFine() {
		Date fine = getFine();
		return (fine == null ? null : DateUtil.formattaData(fine));
	}

	public void setDataFine(String dataFine) {
		this.setFine(DateUtil.toDate(dataFine));
	}

}
