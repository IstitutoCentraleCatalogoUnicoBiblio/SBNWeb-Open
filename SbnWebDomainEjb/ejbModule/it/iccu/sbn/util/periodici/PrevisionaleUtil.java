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
package it.iccu.sbn.util.periodici;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.SerializableComparator;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieTitoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.GiornoMeseVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.PeriodicitaFascicoloType;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.periodici.policy.CalcoloDataFascicoloPolicy;
import it.iccu.sbn.util.periodici.policy.impl.CalcoloDataFascicoloPolicyBaseImpl;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


public final class PrevisionaleUtil {

	public static final SerializableComparator<FascicoloVO> ORDINAMENTO_PREVISIONALE = new SerializableComparator<FascicoloVO>() {

		private static final long serialVersionUID = 2254346074759329861L;

		public int compare(FascicoloVO f1, FascicoloVO f2) {
			int cmp = ValidazioneDati.compare(f1.getData_conv_pub(), f2.getData_conv_pub());
			cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(f1.getBid(), f2.getBid());
			cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(f1.getCd_tipo_fasc(), f2.getCd_tipo_fasc());
			return cmp;
		}
	};

	private final RicercaKardexPrevisionaleVO richiesta;
	private final int maxRows;

	private int totFascicoli;
	private int totVolumi;
	private Date dtPubbLast;
	private Progressivo volLast;
	private Progressivo numFascLast;
	private CalcoloDataFascicoloPolicy policy;

	private Map<String, Integer> fascicoliPerVolume = new THashMap<String, Integer>();
	private Set<Date> dateIncluse = new THashSet<Date>();
	private List<Date> datePrecedenti = new ArrayList<Date>();

	//almaviva5_20121026
	private int annataFrom;
	private int annataTo;

	private static Map<String, CalcoloDataFascicoloPolicy> policies = new THashMap<String, CalcoloDataFascicoloPolicy>();


	public static final List<FascicoloVO> calcola(RicercaKardexPrevisionaleVO richiesta) throws Exception {
		PrevisionaleUtil instance = new PrevisionaleUtil(richiesta);
		return instance.calcola();

	}

	public static final CalcoloDataFascicoloPolicy getPolicy(String cd_per) {

		CalcoloDataFascicoloPolicy policy = policies.get(cd_per);
		if (policy == null)
			policy = new CalcoloDataFascicoloPolicyBaseImpl();

		return policy;
	}

	private static final Date getDate(GiornoMeseVO gm, Date target) {
		Calendar c = Calendar.getInstance();
		c.setTime(target);
		//c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, gm.getMonth());
		c.set(Calendar.DAY_OF_MONTH, gm.getDay());

		return c.getTime();
	}

	private PrevisionaleUtil(RicercaKardexPrevisionaleVO richiesta) throws Exception {
		this.richiesta = richiesta;
		ModelloPrevisionaleVO modello = richiesta.getModello();
		this.dtPubbLast = ClonePool.deepCopy(richiesta.getData_conv_pub());
		this.volLast = new Progressivo(ValidazioneDati.isFilled(modello.getNum_primo_volume()) ? modello.getNum_primo_volume() : "1", null);

		this.maxRows = CommonConfiguration.getPropertyAsInteger(Configuration.MAX_RESULT_ROWS, Integer.MAX_VALUE);

		if (richiesta.isAnnataDoppia()) {
			this.annataFrom = Integer.valueOf(richiesta.getAnnataFrom());
			this.annataTo = Integer.valueOf(richiesta.getAnnataTo());
		}

		//strategia calcolo data fascicoli
		this.policy = getPolicy(modello.getCd_per());

		String tipo_num = modello.getCd_tipo_num_fascicolo();
		TB_CODICI cod = CodiciProvider.cercaCodice(tipo_num, CodiciType.CODICE_TIPO_NUMERAZIONE_FASCICOLO, CodiciRicercaType.RICERCA_CODICE_SBN);

		boolean numeric = ValidazioneDati.equals(cod.getCd_flg2(), PeriodiciConstants.PROGRESSIVO);
		this.numFascLast = new Progressivo(modello.getNum_primo_fascicolo(), numeric ? null : ValidazioneDati.trimOrEmpty(cod.getCd_flg3()) );
	}

	private List<FascicoloVO> calcola() throws Exception {
		List<FascicoloVO> output = ValidazioneDati.emptyList();
		boolean first = true;	//primo fascicolo
		while (true) {
			Date dtPubb = calcolaDataFascicolo(first);
			if (dtPubb == null)
				break;

			String numFasc = null;
			/*
			if (richiesta.isPrevedeNumFascicolo() ) {
				numFasc = calcolaNumeroFascicolo(dtPubb, first);
				if (!ValidazioneDati.isFilled(numFasc))
					break;
			}
			*/
			//almaviva5_20160321 #6132
			//anche se la richiesta non prevede il n. fascicolo si esegue comunque il calcolo
			//per avanzare, eventualmente, il n. del volume.
			numFasc = calcolaNumeroFascicoloVolume(dtPubb, first);
			if (richiesta.isPrevedeNumFascicolo()) {
				if (!ValidazioneDati.isFilled(numFasc))
					break;
			} else
				numFasc = null;

			String vol = null;
			if (richiesta.isPrevedeVolume()) {
				vol = calcolaNumeroVolume();
				if (!ValidazioneDati.isFilled(vol))
					break;
			}

			totFascicoli++;

			if (stop(dtPubbLast))
				break;

			//almaviva5_20121026 #4704
			String annata = preparaAnnata(richiesta, dtPubb);
			output.add(preparaFascicolo(dtPubb, vol, numFasc, annata));
			first = false;
		}
		return output;
	}

	private boolean stop(Date dtCurr) throws ValidationException {

		if (totFascicoli > maxRows) //max righe sintetica
			return true;

		Calendar c = Calendar.getInstance();
		c.setTime(richiesta.getData_conv_pub());
		int durata = richiesta.getDurata();

		switch(richiesta.getTipo()) {
		case VOLUMI:
			return (totVolumi >= durata);

		case FASCICOLI:
			return (totFascicoli > durata);

		case ANNI:
			c.add(Calendar.YEAR, durata);
			c.add(Calendar.DATE, -1);
			return (dtCurr.after(c.getTime()) );

		case GIORNI:
			c.add(Calendar.DATE, durata);
			return (dtCurr.compareTo(c.getTime()) >= 0);

		case MESI:
			c.add(Calendar.MONTH, durata);
			return (dtCurr.compareTo(c.getTime()) >= 0);

		}

		return false;
	}

	private FascicoloVO preparaFascicolo(Date dtPubb, String vol, String numFasc, String annata) {
		FascicoloVO f = new FascicoloVO();
		SerieTitoloVO tit = (SerieTitoloVO) richiesta.getOggettoRicerca();
		f.setBid(tit.getBid());
		f.setData_conv_pub(dtPubb);
		f.setData_in_pubbl(dtPubb);
		f.setNum_volume(vol != null ? Short.valueOf(vol) : null);

		if (ValidazioneDati.isFilled(numFasc))
			if (ValidazioneDati.strIsNumeric(numFasc))
				f.setNum_in_fasc(Integer.valueOf(numFasc));
			else
				f.setNum_alter(numFasc);

		f.setAnnata(ValidazioneDati.isFilled(annata) ? annata : String.valueOf(DateUtil.getYear(dtPubb)));
		f.setCd_per(richiesta.getModello().getCd_per());
		f.setCd_tipo_fasc("S"); //semplice

		return f;
	}

	private String calcolaNumeroFascicoloVolume(Date dtPubb, boolean first) {

		ModelloPrevisionaleVO modello = richiesta.getModello();

		if (first) {
			int start = richiesta.getPosizione_primo_fascicolo() > 1 ? richiesta.getPosizione_primo_fascicolo() : 1;
			fascicoliPerVolume.put(volLast.getCurrentValue(), start);
			String value = numFascLast.getCurrentValue();
			/*
			if (numFascLast.isCustom())
				//prendo la posizione del valore iniziale al'interno della lista dei valori possibili
				fascicoliPerVolume.put(volLast.getCurrentValue(), numFascLast.getValues().indexOf(modello.getNum_primo_fascicolo()) + 1);
			else
				fascicoliPerVolume.put(volLast.getCurrentValue(), ValidazioneDati.strIsNumeric(value) ? new Integer(value) : 1);
			*/
			return value;
		}

		boolean changeVol = false;

		Integer cntFasc = ValidazioneDati.coalesce(fascicoliPerVolume.get(volLast.getCurrentValue()), 0);
		//se il volume é completo
		if ((modello.getNum_fascicoli_per_volume() < 1)) {
			//se non è stato specificato un numero esatto di fascicoli
			//il volume cambia allo scattare dell'anno tra due fascicoli
			if (ValidazioneDati.size(datePrecedenti) > 1) {
				List<Date> tail = ValidazioneDati.tail(datePrecedenti, 2);
				changeVol = DateUtil.getYear(dtPubb) > DateUtil.getYear(tail.get(0)) ;
			}
		} else
			changeVol = (cntFasc == modello.getNum_fascicoli_per_volume());

		if (changeVol) {
			if (!modello.isNum_fascicolo_continuativo())
				numFascLast.restart();

			totVolumi++;
			volLast.incrementAndGet();
			cntFasc = 0;
		}

		numFascLast.incrementAndGet();
		fascicoliPerVolume.put(volLast.getCurrentValue(), ++cntFasc);

		return numFascLast.getCurrentValue();
	}

	private String calcolaNumeroVolume() {
		return volLast.getCurrentValue();
	}

	private Date calcolaDataFascicolo(boolean first) throws Exception {

		ModelloPrevisionaleVO modello = richiesta.getModello();
		List<GiornoMeseVO> listaEscludi = modello.getListaEscludiDate();
		List<GiornoMeseVO> listaIncludi = modello.getListaIncludiDate();
		List<Integer> escludiGiorni = modello.getListaEscludiGiorni();
		int loop = 0;

		Date dtCurr = (Date) dtPubbLast.clone();
		while (!stop(dtCurr)) {

			if (++loop > maxRows)
				throw new ApplicationException(SbnErrorTypes.PER_ERRORE_PREVISIONALE_DEADLOCK);

			String cd_per = modello.getCd_per();
			dtCurr = policy.calcola(cd_per, dtCurr, datePrecedenti, first);
			datePrecedenti.add(dtCurr);
			first = false;

			//Verifica date puntuali da escludere/includere
			//ogni volta che ricalcolo la data pub. devo ripetere i controlli su tutte
			//le date della lista, nel caso il nuovo calcolo avesse incrementato l'anno.
			if (listaEscludi.contains(new GiornoMeseVO(dtCurr)))
				continue;

			// se la data corrente supera quella di un giorno da includere
			//ritorno indietro alla data specificata, senza però variare la data
			//pubb. attualmente raggiunta
			for (GiornoMeseVO gm : listaIncludi) {
				Date dtTmp = getDate(gm, dtCurr);
				if (dtTmp.before(dtCurr) && !dateIncluse.contains(dtTmp) && !richiesta.getData_conv_pub().after(dtTmp)) {
					dateIncluse.add(dtTmp);
					//fix per bimensile
					datePrecedenti.remove(dtCurr);
					return dtTmp;
				}
			}

			if (escludiGiorni.contains(DateUtil.getDayOfWeek(dtCurr)))
				continue;

			//almaviva5_20110704 #4522
			//obbligatorio specificare i giorni per periodicità < settimanale
			PeriodicitaFascicoloType tipoPer = PeriodicitaFascicoloType.fromString(cd_per);
			if (tipoPer.isLowerSettimanale())
				if (!modello.getListaIncludiGiorni().contains(DateUtil.getDayOfWeek(dtCurr)))
					continue;

			break;
		}
		dtPubbLast = dtCurr;
		return dtPubbLast;
	}

	private String preparaAnnata(RicercaKardexPrevisionaleVO richiesta, Date dtCurr) throws Exception {
		if (!richiesta.isAnnataDoppia())
			return null;

		//almaviva5_20130312 #4704 il valore del campo annata viene ricavato dagli estremi forniti
		//e il numero dei volumi prodotti dalla previsione fascicoli
		int delta = totVolumi;//Integer.valueOf(volLast.getCurrentValue()) - 1;
		if (delta > 0)
			delta = (annataTo - annataFrom) * delta;

		int yFrom = annataFrom + delta;
		int yTo = annataTo + delta;
		String annata = String.format("%d/%d", yFrom, yTo);

//		if (!DateUtil.between(dtCurr, DateUtil.firstDayOfYear(yFrom), DateUtil.lastDayOfYear(yTo)))
//			throw new ValidationException(SbnErrorTypes.PER_ERRORE_DATA_FASCICOLO_ECCEDE_ANNATA,
//					DateUtil.formattaData(dtCurr), annata);

		return annata;
	}

	public static final List<FascicoloVO> selezionaNuoviFascicoli(List<FascicoloVO> fascicoli) {
		List<FascicoloVO> nuoviFascicoli = new ArrayList<FascicoloVO>();
		for (FascicoloVO f : fascicoli)
			if (f.isNuovo())
				nuoviFascicoli.add(f);
		return nuoviFascicoli;
	}

}
