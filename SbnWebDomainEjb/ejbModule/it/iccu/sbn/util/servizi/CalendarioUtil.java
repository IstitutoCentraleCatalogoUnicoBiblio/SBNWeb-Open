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
package it.iccu.sbn.util.servizi;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.periodici.previsionale.GiornoMeseVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.CalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ElementoCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.FasciaVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.IntervalloVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.TipoCalendario;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.matchers.Calendario;
import it.iccu.sbn.util.servizi.calendario.HolidaysEnumerator;
import it.iccu.sbn.vo.custom.servizi.calendario.FasciaDecorator;
import it.iccu.sbn.vo.validators.calendario.Interval;
import it.iccu.sbn.vo.validators.calendario.ModelloCalendarioValidator;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.spi.ServiceRegistry;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.asList;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;


public final class CalendarioUtil {

	static Logger log = Logger.getLogger(CalendarioUtil.class);
	static List<HolidaysEnumerator> holidays;

	static {
		holidays = new ArrayList<HolidaysEnumerator>();
		Iterator<HolidaysEnumerator> i = ServiceRegistry.lookupProviders(HolidaysEnumerator.class);
		while (i.hasNext()) {
			HolidaysEnumerator he = i.next();
			log.debug("HolidaysEnumerator impl: " + he);
			holidays.add(he);
		}
	}

	static HolidaysEnumerator getHolidays(Locale locale) {
		for (HolidaysEnumerator he : holidays) {
			if (he.country().equals(locale.getCountry()))
				return he;
		}

		//dummy implementation
		return new HolidaysEnumerator() {

			public List<GiornoMeseVO> fixedDays() {
				return Collections.emptyList();
			}

			public List<GiornoMeseVO> movableDays(int year) {
				return Collections.emptyList();
			}

			public String country() {
				return "";
			}
		};
	}

	public static Date getEasterDate(int year) {
		if (year == 0 || !String.valueOf(year).matches("\\d{4}"))
			return null;

		//Anonymous Gregorian algorithm (1876)
		int a = year % 19;
		int b = year / 100;
		int c = year % 100;
		int d = b / 4;
		int e = b % 4;
		int f = (b + 8) / 25;
		int g = (b - f + 1) / 3;
		int h = (19 * a + b - d - g + 15) % 30;
		int i = c / 4;
		int k = c % 4;
		int l = (32 + 2 * e + 2 * i - h - k) % 7;
		int m = (a + 11 * h + 22 * l) / 451;
		int n = (h + l - 7 * m + 114) / 31;
		int p = (h + l - 7 * m + 114) % 31;
		Calendar cal = GregorianCalendar.getInstance();
		cal.clear();
		cal.set(year, n - 1, p + 1);

		return cal.getTime();
	}

	public static int toMinutes(LocalTime time) {
		int h = time.getHourOfDay();
		int m = time.getMinuteOfHour();
	    int hoursInMins = (h * 60);
	    return (hoursInMins + m);
	}

	/**
	 * Restituisce la durata della fascia in minuti
	 * @param f la fascia da calcolare.
	 * @return il numero di minuti totali per la fascia.
	 */
	public static int getDurataFascia(FasciaVO f) {
		int start = toMinutes(f.getInizio());
		int end = toMinutes(f.getFine());
		return end - start;
	}

	public static int getDurataPrenotazione(PrenotazionePostoVO pp) {
		int start = toMinutes(LocalTime.fromDateFields(pp.getTs_inizio()));
		int end = toMinutes(LocalTime.fromDateFields(pp.getTs_fine()).plusSeconds(1));
		return end - start;
	}

	public static int getDurataTotaleGiorno(List<CalendarioVO> calendari, Date when) {
		int durata = 0;
		List<FasciaVO> fasce = getFasceDisponibiliGiorno(calendari, null, when);
		for (FasciaVO f : fasce)
			durata += getDurataFascia(f);

		return durata;
	}

	public static ModelloCalendarioVO getTemplate(Date inizio, Date fine, String descr) {
		ModelloCalendarioVO m = new ModelloCalendarioVO();

		m.setDescrizione(descr);
		m.setInizio(inizio);
		m.setFine(fine);

		IntervalloVO i = new IntervalloVO();
		i.setBase(true);
		i.setId(1);
		i.setDescrizione("intervallo base");
		i.setInizio(m.getInizio());
		i.setFine(m.getFine());
		m.getIntervalli().add(i);

		i.getFasce().get(DateTimeConstants.MONDAY).add(new FasciaVO("09:00", "18:00"));
		i.getFasce().get(DateTimeConstants.TUESDAY).add(new FasciaVO("09:00", "18:00"));
		i.getFasce().get(DateTimeConstants.WEDNESDAY).add(new FasciaVO("09:00", "18:00"));
		i.getFasce().get(DateTimeConstants.THURSDAY).add(new FasciaVO("09:00", "18:00"));
		i.getFasce().get(DateTimeConstants.FRIDAY).add(new FasciaVO("09:00", "18:00"));
		//i.getFasce().get(DateTimeConstants.SATURDAY).add(new FasciaVO("09:00", "12:30"));
		//i.getFasce().get(DateTimeConstants.SUNDAY).add((FasciaVO) f.clone());

		i.getDateEscluse().addAll(getHolidays(Locale.getDefault()).fixedDays());

		return m;
	}

	public static CalendarioVO preparaCalendario(ModelloCalendarioVO modello, Date inizioIntervallo, Date fineIntervallo) throws ValidationException {
		HolidaysEnumerator holidays = getHolidays(Locale.getDefault());
		CalendarioVO c = new CalendarioVO();
		List<ElementoCalendarioVO> elementi = c.getElementi();

		log.debug(String.format("preparaCalendario(): %s - %s", inizioIntervallo, fineIntervallo) );

		c.setTipo(modello.getTipo());
		c.setDescrizione(modello.getDescrizione());
		Date inizio = inizioIntervallo != null ? inizioIntervallo : modello.getInizio();
		Date fine = fineIntervallo != null ? fineIntervallo : modello.getFine();
		c.setInizio(inizio);
		c.setFine(fine);

		modello.validate(new ModelloCalendarioValidator());

		List<IntervalloVO> intervalli = modello.getIntervalli();
		int level = 0;
		for (IntervalloVO i : intervalli) {

			if (!i.isAbsolute()) {
				//le date non sono assolute, riposiziona intervallo nel range scelto
				ricalcolaEstremiIntervallo(inizio, fine, i);
			}

			//esclusione intervalli fuori range
			if (!Interval.of(inizio, fine).overlaps(Interval.of(i)) ) {
				log.debug("intervallo " + i + " escluso dal calendario.");
				continue;
			}

			Map<Integer, List<FasciaVO>> fasce = i.getFasce();
			List<GiornoMeseVO> dateEscluse = i.getDateEscluse();

			LocalDate start = LocalDate.fromDateFields(inizio);
			LocalDate end = LocalDate.fromDateFields(fine);
			int tot_aperto = 0;
			int tot_chiuso = 0;

			while (!start.isAfter(end)) {
				List<FasciaVO> fasceGiorno = fasce.get(start.getDayOfWeek());
				Date current = start.toDate();
				int year = start.getYear();

				GiornoMeseVO gm = new GiornoMeseVO(start);
				if (i.contains(gm.withYear(year)) ) {
					if (dateEscluse.contains(gm) || holidays.movableDays(year).contains(gm) ) {
						//giorno festivo o di chiusura
						ElementoCalendarioVO e = new ElementoCalendarioVO(level, c, i);
						e.setData(current);
						e.setActive(false);
						elementi.add(e);
						tot_chiuso++;
					} else
						if (!isFilled(fasceGiorno)) {
							//nessuna fascia, giorno chiuso
							ElementoCalendarioVO e = new ElementoCalendarioVO(level, c, i);
							e.setData(current);
							e.setActive(false);
							elementi.add(e);
							tot_chiuso++;
						} else {
							//fascia presente
							for (FasciaVO f : fasceGiorno) {
								ElementoCalendarioVO e = new ElementoCalendarioVO(level, c, i);
								e.setData(current);
								e.setActive(true);

								e.setInizio(f.getInizio());
								e.setFine(f.getFine());

								elementi.add(e);
								tot_aperto++;
							}
						}
				}

				//giorno successivo
				start = start.plusDays(1);
			}
			log.debug(String.format("%s, attivi: %d, chiusi: %d", i, tot_aperto, tot_chiuso));
			level++;
		}

		Collections.sort(elementi);

		return c;
	}

	private static void ricalcolaEstremiIntervallo(Date inizio, Date fine, IntervalloVO i) {
		//estremi calendario
		GiornoMeseVO inizioCal = new GiornoMeseVO(inizio);
		GiornoMeseVO fineCal = new GiornoMeseVO(fine);
		//estremi intervallo
		GiornoMeseVO inizioInt = new GiornoMeseVO(i.getInizio());
		GiornoMeseVO fineInt = new GiornoMeseVO(i.getFine());

		if (inizioInt.after(fineCal))
			return;

		if (fineInt.before(inizioCal))
			return;

		Date newInizioIntervallo = inizioInt.withYear(DateUtil.getYear(inizio));
		Date newFineIntervallo = fineInt.withYear(DateUtil.getYear(fine));
		i.setInizio(newInizioIntervallo);
		i.setFine(newFineIntervallo);

		log.debug("ricalcolaEstremiIntervallo(): " + i);
	}

	public static boolean isPrenotabile(List<CalendarioVO> calendari, Date when, LocalTime inizio, LocalTime fine) {

		Date day = DateUtil.removeTime(when);

		boolean matched = true;
		Iterator<CalendarioVO> i = calendari.iterator();

		while (matched && i.hasNext() ) {
			CalendarioVO c = i.next();
			//intervalli che contengono la data
			Map<Integer, List<ElementoCalendarioVO>> intervalli =
				Stream.of(c.getElementi()).filter(Calendario.intervalloContains(day)).collect(Collectors.groupingBy(Calendario.byLevel()));
			//controllo su intervalli che contengano il range orario o che, in caso di prenotazione, escludano il range
			Set<Integer> keys = new TreeSet<Integer>(intervalli.keySet());
			for (Integer level : keys) {
				List<ElementoCalendarioVO> elementi = intervalli.get(level);
				Stream<ElementoCalendarioVO> s = Stream.of(elementi).filter(Calendario.isActive(day, inizio, fine));
				long cnt = s.count();

				matched = cnt > 0;
			}
		}

		return matched;
	}

	public static void checkCongruenzaCalendario(ModelloCalendarioVO inner, ModelloCalendarioVO outer) throws SbnBaseException {
		//si verifica che il calendario interno sia coerente con quello esterno
		List<CalendarioVO> innerCal = asList(CalendarioUtil.preparaCalendario(inner, inner.getInizio(), inner.getFine()));
		List<CalendarioVO> outerCal = asList(CalendarioUtil.preparaCalendario(outer, inner.getInizio(), inner.getFine()));

		//ciclo sui giorni
		LocalDate start = LocalDate.fromDateFields(inner.getInizio());
		LocalDate end = LocalDate.fromDateFields(inner.getFine());
		while (!start.isAfter(end)) {
			Date when = start.toDate();
			boolean innerTest = CalendarioUtil.isPrenotabile(innerCal, when, null, null);
			boolean outerTest = CalendarioUtil.isPrenotabile(outerCal, when, null, null);
			if (!outerTest && innerTest) {
				//prenotabile su quello interno ma non su quello esterno
				SimpleDateFormat _formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");
				throw new ApplicationException(SbnErrorTypes.SRV_ERROR_GIORNO_NON_INCLUSO_CALENDARIO_BIB, _formatter.format(when));
			}
			if (outerTest && innerTest) {
				//prenotabile in entrambi i calendari, test singole fasce
				List<FasciaVO> innerFasce = getFasceGiorno(innerCal, when);
				for (FasciaVO f : innerFasce)
					if (!isPrenotabile(outerCal, when, f.getInizio(), f.getFine())) {
						SimpleDateFormat _formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");
						FasciaDecorator fd = new FasciaDecorator(f);
						throw new ApplicationException(SbnErrorTypes.SRV_ERROR_FASCIA_NON_INCLUSA_CALENDARIO_BIB,
								fd.toString(), _formatter.format(when));
					}
			}

			//giorno successivo
			start = start.plusDays(1);
		}

	}

	public static List<FasciaVO> getFasceGiorno(List<CalendarioVO> calendari, Date when) {
		List<FasciaVO> fasce = new ArrayList<FasciaVO>();
		CalendarioVO c = ValidazioneDati.tail(calendari, 1).get(0);
		List<ElementoCalendarioVO> elementi = Stream.of(c.getElementi()).filter(Calendario.elementiAttiviGiorno(when)).toList();
		int max = Stream.of(elementi).mapToInt(Calendario.byLevelInt()).max().getAsInt();
		for (ElementoCalendarioVO ec : elementi)
			if (ec.getLevel() == max)
				fasce.add(new FasciaVO(ec));

		return fasce;
	}

	/**
	 * Converte una prenotazione in un calendario con un solo elemento.
	 * @param prenotazione la prenotazione da convertire
	 * @return un {@code CalendarioVO} con un solo elemento
	 */
	public static CalendarioVO aggiungiPrenotazionePosto(PrenotazionePostoVO prenotazione) {
		Date inizio = DateUtil.removeTime(prenotazione.getTs_inizio());
		Date fine = DateUtil.removeTime(prenotazione.getTs_fine());

		CalendarioVO c = new CalendarioVO(inizio, fine);
		c.setTipo(TipoCalendario.PRENOTAZIONE);
		IntervalloVO i = new IntervalloVO(inizio, fine);

		ElementoCalendarioVO ec = new ElementoCalendarioVO(0, c, i);
		ec.setActive(false);
		ec.setData(inizio);
		ec.setInizio(LocalTime.fromDateFields(prenotazione.getTs_inizio()));
		ec.setFine(LocalTime.fromDateFields(prenotazione.getTs_fine()));

		c.getElementi().add(ec);

		return c;
	}

	public static List<FasciaVO> getFasceDisponibiliGiorno(List<CalendarioVO> calendari,
			List<PrenotazionePostoVO> prenotazioni, Date when) {
		List<FasciaVO> fasce = new ArrayList<FasciaVO>();

		CalendarioVO c = ValidazioneDati.tail(calendari, 1).get(0);
		List<ElementoCalendarioVO> elementi = Stream.of(c.getElementi()).filter(Calendario.elementiAttiviGiorno(when)).toList();
		if (!isFilled(elementi))
			return fasce;

		int max = Stream.of(elementi).mapToInt(Calendario.byLevelInt()).max().getAsInt();
		for (ElementoCalendarioVO ec : elementi)
			if (ec.getLevel() == max)
				fasce.add(new FasciaVO(ec));

//		if (isFilled(prenotazioni)) {
//			for (PrenotazionePostoVO pp : prenotazioni) {
//				//TODO
//			}
//		}

		return fasce;
	}

	public static FasciaVO getOrarioAperturaChiusura(CalendarioVO calendario, Date when) {
		if (calendario == null)
			return null;

		List<FasciaVO> fasce = getFasceDisponibiliGiorno(asList(calendario), null, when);
		if (isFilled(fasce)) {
			FasciaVO f = new FasciaVO();
			f.setInizio(fasce.get(0).getInizio() );
			f.setFine(ValidazioneDati.tail(fasce, 1).get(0).getFine());
			return f;
		}

		return null;
	}

	public static int calcolaGiorniPreparazioneSupporto(CalendarioVO calendario, Date when, ServizioBibliotecaVO servizio) {
		if (servizio == null)
			return 0;

		FasciaVO aperturaChiusura = getOrarioAperturaChiusura(calendario, when);
		//se la biblioteca è chiusa si avanza come se avesse superato il limite orario
		boolean bibChiusa = (aperturaChiusura == null);

		boolean limiteOrarioSuperato = false;
		String orarioLimitePrepSupp = servizio.getOrarioLimitePrepSupp();
		if (ValidazioneDati.isFilled(orarioLimitePrepSupp)) {
			//se la richiesta è arrivata prima dell'orario limite il giorno corrente non viene calcolato
			LocalTime limite = LocalTime.parse(orarioLimitePrepSupp);
			limiteOrarioSuperato = LocalTime.now().isAfter(limite);
		}

		//calcolo dei tempi di preparazione del supporto (inventario)
		short numGgPrepSupp = coalesce(servizio.getNumGgPrepSupp(), (short)0);
		if (numGgPrepSupp == 0) {
			if (limiteOrarioSuperato)
				numGgPrepSupp = 1;
		} else
			if (numGgPrepSupp > 0) {
				if (bibChiusa || limiteOrarioSuperato)
					numGgPrepSupp++;
			}

		return numGgPrepSupp;
	}

	public static void main(String[] args) throws Exception {

		//test1();

		test2();

		SimpleDateFormat _formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");
		System.out.println(_formatter.format(DaoManager.now()));

		FasciaVO f = new FasciaVO(LocalTime.parse("09:00"), LocalTime.parse("13:00") );
		System.out.println(new FasciaDecorator(f) + " = " + getDurataFascia(f));


	}

	static void test1() throws ValidationException {

		List<CalendarioVO> calendari = new ArrayList<CalendarioVO>();

		LocalDate inizio = LocalDate.now().withMonthOfYear(1).withDayOfMonth(1);
		LocalDate fine = inizio.plusYears(1).minusDays(1);
		ModelloCalendarioVO m1 = CalendarioUtil.getTemplate(inizio.toDate(), fine.toDate(), "test");

		System.out.println(m1);
		String json = ClonePool.toJson(m1);
		System.out.println(json);
		CalendarioVO m2 = ClonePool.fromJson(json, ModelloCalendarioVO.class);
		System.out.println(m2);

		Calendar when = Calendar.getInstance();
		when.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

		CalendarioVO c1 = CalendarioUtil.preparaCalendario(m1, null, when.getTime());
		CalendarioVO c2 = CalendarioUtil.preparaCalendario(m1, null, when.getTime());

		calendari.add(c1);
		calendari.add(c2);

		boolean prenotabile = isPrenotabile(calendari, when.getTime(), LocalTime.parse("09:00"), LocalTime.parse("13:00") );

		System.out.println(prenotabile);
	}

	static void test2() throws Exception {

		LocalDate inizio = DateUtil.of(2017, 1, 1);
		LocalDate fine = inizio.plusYears(1).minusDays(1);
		ModelloCalendarioVO mc = CalendarioUtil.getTemplate(inizio.toDate(), fine.toDate(), "test");
		mc.setCodPolo("XXX");
		mc.setCodBib(" ZZ");

		IntervalloVO i2 = new IntervalloVO();
		i2.setDescrizione("chiusura marzo");
		inizio = DateUtil.of(2017, 3, 1);
		i2.setInizio(inizio.toDate());
		i2.setFine(inizio.plusDays(30).toDate());
		mc.addIntervallo(i2);

		IntervalloVO i3 = new IntervalloVO();
		i3.setDescrizione("aprile");
		inizio = DateUtil.of(2017, 4, 1);
		i3.setInizio(inizio.toDate());
		i3.setFine(inizio.withDayOfMonth(30).toDate());
		i3.getFasce().get(DateTimeConstants.MONDAY).add(new FasciaVO("09:30", "13:00"));
		i3.getFasce().get(DateTimeConstants.MONDAY).add(new FasciaVO("14:00", "18:00"));
		mc.addIntervallo(i3);

		CalendarioVO c = preparaCalendario(mc, null, fine.toDate() );
		List<CalendarioVO> calendari = asList(c);

		Date when = DateUtil.of(2017, 3, 15).toDate();
		boolean prenotabile = isPrenotabile(calendari, when, LocalTime.parse("10:00"), LocalTime.parse("11:00") );
		System.out.println(when + " --> " + prenotabile);

		when = DateUtil.of(2017, 4, 10).toDate();
		prenotabile = isPrenotabile(calendari, when, LocalTime.parse("08:00"), LocalTime.parse("11:00") );
		System.out.println(when + " --> " + prenotabile);

		prenotabile = isPrenotabile(calendari, when, LocalTime.parse("16:00"), LocalTime.parse("17:00") );
		System.out.println(when + " --> " + prenotabile);

		PrenotazionePostoVO pp = new PrenotazionePostoVO();
		LocalDateTime ptime = new LocalDateTime(2017, 4, 10, 15, 00);
		pp.setTs_inizio(new Timestamp(ptime.toDate().getTime()));	//dalle 15
		pp.setTs_fine(new Timestamp(ptime.plusHours(1).toDate().getTime()));	//alle 16
		calendari.add(aggiungiPrenotazionePosto(pp));

		prenotabile = isPrenotabile(calendari, when, LocalTime.parse("14:00"), LocalTime.parse("14:59") );
		System.out.println(when + " --> " + prenotabile);

		calendari = asList(c);
		List<FasciaVO> fasce = getFasceDisponibiliGiorno(calendari, asList(pp), ptime.toDate());
		System.out.println(when + " --> " + fasce);

		ptime = new LocalDateTime(2017, 3, 10, 15, 00);
		List<FasciaVO> fasce2 = getFasceDisponibiliGiorno(calendari, null, ptime.toDate());
		System.out.println(when + " --> " + fasce2);

		ptime = new LocalDateTime(2017, 4, 10, 15, 00);
		System.out.println("durata fasce: " + when + " --> " + getDurataTotaleGiorno(calendari, ptime.toDate()));
	}

}
