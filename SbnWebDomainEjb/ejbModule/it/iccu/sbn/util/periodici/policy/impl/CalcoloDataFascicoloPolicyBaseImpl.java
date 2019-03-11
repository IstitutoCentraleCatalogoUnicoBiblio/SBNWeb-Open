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
package it.iccu.sbn.util.periodici.policy.impl;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.PeriodicitaFascicoloType;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.periodici.policy.CalcoloDataFascicoloPolicy;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalcoloDataFascicoloPolicyBaseImpl implements CalcoloDataFascicoloPolicy {

	private static final int MONTH_LEN[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private static final int LEAP_MONTH_LEN[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };


	private static final Date addMonthWithFixedDay(Date start, int monthToAdd, int fixedDay) {
		try {
			GregorianCalendar gc1 = new GregorianCalendar();
			gc1.setTime(start);

			gc1.add(Calendar.MONTH, monthToAdd);
			int year = gc1.get(Calendar.YEAR);
			int month = gc1.get(Calendar.MONTH);
			int day = gc1.get(Calendar.DAY_OF_MONTH);
			if (fixedDay > day) {//febbraio?
				int lastDay = gc1.isLeapYear(year) ? LEAP_MONTH_LEN[month] : MONTH_LEN[month];
				gc1.set(Calendar.DAY_OF_MONTH, (fixedDay > lastDay) ? lastDay : fixedDay);
			}

			return gc1.getTime();

		} catch (Exception e) {
			return null;
		}
	}

	public Date calcola(String cd_per, Date dtPubbLast, List<Date> dtPrev, boolean first) throws Exception {

		if (first)
			return dtPubbLast;

		PeriodicitaFascicoloType type = PeriodicitaFascicoloType.fromString(cd_per);
		if (type == null)
			throw new ApplicationException(SbnErrorTypes.PER_ERRORE_TIPO_PERIODICITA_NON_GESTITO);

		Date dtFirst = ValidazioneDati.isFilled(dtPrev) ? dtPrev.get(0) : dtPubbLast;
		int day = DateUtil.getDay(dtFirst);

		switch (type) {
		case ALTRO:
		case IRREGOLARE:
		case VARIABILE:
		case SCONOSCIUTO:
			throw new ApplicationException(SbnErrorTypes.PER_ERRORE_TIPO_PERIODICITA_NON_GESTITO);

		case QUOTIDIANO:
		case BISETTIMANALE:
		case TRI_SETTIMANALE:
			return DateUtil.addDay(dtPubbLast, 1);

		case QUINDICINALE:
			return DateUtil.addDay(dtPubbLast, 14);

		case MENSILE:
			return addMonthWithFixedDay(dtPubbLast, 1, day);

		case BIMENSILE:
			day = DateUtil.getDay(dtPubbLast);
			if (day < 16)
				return DateUtil.addDay(dtPubbLast, 15);
			else
				return DateUtil.addMonth(DateUtil.addDay(dtPubbLast, -15), 1);

		case TRE_NUMERI_AL_MESE:
			List<Date> tail = ValidazioneDati.tail(dtPrev, 3);
			if (ValidazioneDati.size(tail) < 3)
				return DateUtil.addDay(dtPubbLast, 10);

			//ultimi tre numeri
			Date terz = tail.get(0);
			return DateUtil.addMonth(terz, 1);

		case BIMESTRALE:
			return DateUtil.addMonth(dtPubbLast, 2);

		case TRIMESTRALE:
			return DateUtil.addMonth(dtPubbLast, 3);

		case QUADRIMESTRALE:
			return DateUtil.addMonth(dtPubbLast, 4);

		case SEMESTRALE:
			return DateUtil.addMonth(dtPubbLast, 6);

		case ANNUALE:
			return DateUtil.addYear(dtPubbLast, 1);

		default:
			TB_CODICI cod = CodiciProvider.cercaCodice(cd_per, CodiciType.CODICE_PERIODICITA,
					CodiciRicercaType.RICERCA_CODICE_SBN);
			if (cod == null)
				throw new ApplicationException(SbnErrorTypes.PER_ERRORE_TIPO_PERIODICITA_NON_GESTITO);

			String ggPer = cod.getCd_flg2();
			int gg = ValidazioneDati.isFilled(ggPer) && ValidazioneDati.strIsNumeric(ggPer) ? Integer.valueOf(ggPer) : 0;

			//data presunta prossimo fascicolo
			return DateUtil.addDay(dtPubbLast, gg);
		}

	}

	public static void main(String[] args) throws Exception {
		List<Date> date = new ArrayList<Date>();
		Date dtPubb = DateUtil.toDate("8/01/2012");
		RicercaKardexPrevisionaleVO richiesta = new RicercaKardexPrevisionaleVO();
		ModelloPrevisionaleVO modello = new ModelloPrevisionaleVO();
		modello.setCd_per(PeriodicitaFascicoloType.BIMENSILE.getCd_per());
		richiesta.setModello(modello);
		boolean first = true;

		CalcoloDataFascicoloPolicyBaseImpl c = new CalcoloDataFascicoloPolicyBaseImpl();
		for (int cnt = 0; cnt < 24; cnt++) {
			dtPubb = c.calcola(modello.getCd_per(), dtPubb, date, first);
			first = false;
			date.add(dtPubb);
			System.out.println(dtPubb);
		}

	}

}
