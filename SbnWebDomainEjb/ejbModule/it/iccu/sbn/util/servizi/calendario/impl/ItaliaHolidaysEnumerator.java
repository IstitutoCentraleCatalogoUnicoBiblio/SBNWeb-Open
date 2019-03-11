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
package it.iccu.sbn.util.servizi.calendario.impl;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.periodici.previsionale.GiornoMeseVO;
import it.iccu.sbn.util.servizi.CalendarioUtil;
import it.iccu.sbn.util.servizi.calendario.HolidaysEnumerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItaliaHolidaysEnumerator implements HolidaysEnumerator {

	static final GiornoMeseVO[] FESTIVITA_ITALIA = new GiornoMeseVO[] {
		new GiornoMeseVO(1, 1),		//capodanno
		new GiornoMeseVO(1, 6),		//epifania
		//new GiornoMeseVO(4, 21),	//fondazione di Roma
		new GiornoMeseVO(4, 25),	//liberazione
		new GiornoMeseVO(5, 1),		//festa dei lavoratori
		new GiornoMeseVO(6, 2),		//festa della repubblica
		new GiornoMeseVO(8, 15),	//ferragosto
		new GiornoMeseVO(11, 1),	//festa dei santi
		new GiornoMeseVO(12, 8),	//immacolata concezione
		new GiornoMeseVO(12, 25),	//natale
		new GiornoMeseVO(12, 26),	//santo stefano
	};

	List<GiornoMeseVO> fixedDays = Arrays.asList(FESTIVITA_ITALIA);

	Map<Integer, List<GiornoMeseVO>> movableDays = new ConcurrentHashMap<Integer, List<GiornoMeseVO>>();

	public String country() {
		return Locale.ITALY.getCountry();
	}

	public List<GiornoMeseVO> fixedDays() {
		return fixedDays;
	}

	public List<GiornoMeseVO> movableDays(int year) {

		List<GiornoMeseVO> days = movableDays.get(year);
		if (days != null)
			return days;

		//pasqua & pasquetta
		Date easterDate = CalendarioUtil.getEasterDate(year);
		days = new ArrayList<GiornoMeseVO>();
		days.add(new GiornoMeseVO(easterDate));
		days.add(new GiornoMeseVO(DateUtil.addDay(easterDate, 1)));
		movableDays.put(year, days);

		return days;
	}

}
