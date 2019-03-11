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
package it.iccu.sbn.util.matchers;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.servizi.calendario.ElementoCalendarioVO;

import java.util.Date;

import org.joda.time.LocalTime;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.ToIntFunction;

public class Calendario extends BaseMatchers {

	public static Predicate<ElementoCalendarioVO> isActive(final Date day, final LocalTime start,
			final LocalTime end) {
		return new Predicate<ElementoCalendarioVO>() {
			boolean withTime = (start != null) && (end != null);
			public boolean test(ElementoCalendarioVO e) {
				boolean test = false;
				switch (e.getCalendario().getTipo()) {
				default:
					//l'intervallo cercato deve essere incluso nel range del calendario
					test = e.isActive() && (e.getData().equals(day));
					if (withTime)
						test = test && (start.compareTo(e.getInizio()) >= 0 && end.compareTo(e.getFine()) <= 0 );
					break;
				case PRENOTAZIONE:
					if (!withTime)
						return true;
					//nel caso di prenotazione i due intevalli non devono sovrapporsi
					test = (e.getData().equals(day)) && (end.isBefore(e.getInizio()) || start.isAfter(e.getFine()));
					break;
				}
				return test;
			}
		};
	}

	public static Function<ElementoCalendarioVO, Integer> byLevel() {
		return new Function<ElementoCalendarioVO, Integer>() {
			public Integer apply(ElementoCalendarioVO e) {
				return e.getLevel();
			}
		};
	}

	public static ToIntFunction<ElementoCalendarioVO> byLevelInt() {
		return new ToIntFunction<ElementoCalendarioVO>() {
			public int applyAsInt(ElementoCalendarioVO e) {
				return e.getLevel();
			}
		};
	}

	public static Predicate<ElementoCalendarioVO> intervalloContains(final Date when) {
		return new Predicate<ElementoCalendarioVO>() {
			public boolean test(ElementoCalendarioVO e) {
				return e.getIntervallo().contains(when);
			}
		};
	}

	public static Predicate<ElementoCalendarioVO> elementiAttiviGiorno(Date when) {
		final Date target = DateUtil.removeTime(when);
		return new Predicate<ElementoCalendarioVO>() {
			public boolean test(ElementoCalendarioVO e) {
				return (e.isActive() && e.getData().equals(target));
			}
		};
	}

}
