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
package it.finsiel.sbn.polo.factoring.util;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * CLASSE PER LA GESTIONE DELLE DATE
 * <p/>
 * Gestisce il tipo SbnDatavar (omonimo) Il tipo data ricevuto è particolarmente
 * esteso:yyyyMMddhhmmss.t Questo dettaglio è necessario per la gestione del
 * Timestamp Può ricevere l'input date nei seguenti formati - String -
 * java.sql.Date - long - Timestamp Si consiglia l'uso diretto del tipo
 * Timestamp (attributo time_stamp_orginal_value) L'informazioni viene comunque
 * spacchettata nei campi anno,mese,giorno,ora,minuti,secondi,decimi di secondo
 * <p/>
 *
 * @author almaviva
 */
public class SbnDatavar extends BaseDate {

	private static final long serialVersionUID = -5775171829881932900L;

	public static final String NANOS_SEPARATORE = ":";

	private static Logger log = Logger.getLogger("iccu.box.SbnDatavar");

	// Mi memorizzo comunque il valore originale in formato Timestamp
	// della data pervenuta
	private Timestamp time_stamp_orginal_value = null;

	// Dettaglio decimi di secondo per decodifica timestamp
	private Integer decimali = null;

	/**
	 * METODO DI CREATE INSTANCE VUOTO
	 * <p>
	 * METODO VUOTO MEGLIO SETTARE L'ISTANZA A NULL CHE ISTANZIARE UNA DATA
	 * SENZA VALORI
	 * </p>
	 *
	 * @param void
	 * @return nuova istanza data
	 */
	public SbnDatavar() {
		return;
	}

	/**
	 * METODO DI CREATE INSTANCE
	 * <p>
	 * SECONDA VERSIONE ACCETTA UN "supertipo" java.sql.Date Crea un'istanza
	 * della data nel formato yyyyMMddhhmmss (il .t è vuoto)
	 * </p>
	 *
	 * @param tipo
	 *            java.sql.Date
	 * @return nuova istanza per default
	 */
	public SbnDatavar(Date input_date) {

		if (input_date instanceof Timestamp) {
			setSbnDatavar((Timestamp)input_date);
			return;
		}

		String data_formato_corretto = null;

		// Converto nel formato opportuno
		SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");
		data_formato_corretto = Format.format(input_date);

		// Spacchetta la data di tipo yyyyMMddhhmmss
		setSbnDatavar(data_formato_corretto);
	}

	/**
	 * METODO DI CREATE INSTANCE
	 * <p>
	 * TERZA VERSIONE ACCETTA UN tipo String (yyyyMMddhhmmss.t) Crea un'istanza
	 * della data nel formato yyyyMMddhhmmss.t
	 *
	 * </p>
	 *
	 * @param tipo
	 *            String
	 * @return nuova istanza data
	 */
	public SbnDatavar(String input_date) {
		if (input_date != null) {
			setSbnDatavar(input_date);
		}
	}

	/**
	 * METODO DI CREATE INSTANCE
	 * <p>
	 * QUARTO FORMATO: Long di tipo time_millis Crea un'istanza della data nel
	 * formato yyyyMMddhhmmss.t
	 * </p>
	 *
	 * @param tipo
	 *            long
	 * @return nuova istanza data
	 */
	public SbnDatavar(long input_date) {
		if (input_date != 0) {
			Timestamp appoggio = new Timestamp(input_date);
			setSbnDatavar(appoggio);
		}
	}

	/**
	 * METODO DI CREATE INSTANCE
	 * <p>
	 * QUINTO FORMATO: Arriva direttamente il tipo TimeStamp Crea un'istanza
	 * della data nel formato yyyyMMddhhmmss.t
	 * </p>
	 *
	 * @param Timestamp
	 *            input_date_timestamp
	 * @return nuova istanza data
	 */
	public SbnDatavar(Timestamp input_date_timestamp) {
		if (input_date_timestamp != null) {
			setSbnDatavar(input_date_timestamp);
		}
	}

	/**
	 * METODO DI SETTING DATA:
	 * <p>
	 * Riceve il Timestamp e attiva il setting per stringa generico
	 * </p>
	 *
	 * @param Timestamp
	 *            input_date_timestamp
	 * @return void
	 */
	public void setSbnDatavar(Timestamp ts) {
		if (ts == null)
			return;

		// Tengo la versione originale del TimeStamp ricevuto
		time_stamp_orginal_value = ts;

		// Converto in string e setto i valori
		String tmp = ts.toString();
		// Convero il formato togliendo i separatori tranne l'ultimo punto
		tmp = tmp.substring(0, 4)
				+ tmp.substring(5, 7)
				+ tmp.substring(8, 10)
				+ tmp.substring(11, 13) +
				// string_data.substring(14,16)+string_data.substring(17,21);
				tmp.substring(14, 16) + tmp.substring(17);

		setSbnDatavar(tmp);
	}

	/**
	 * METODO DI SETTING DATA:
	 * <p>
	 * Spacchetto la data nel formato yyyyMMddhhmmss.t (eventualmente senza .t
	 * per java.sql.Date)
	 * </p>
	 *
	 * @param value
	 *            (yyyyMMddhhmmss)|| (yyyyMMddhhmmss.t) data in input
	 * @return void
	 */
	public void setSbnDatavar(String value) {
		Integer _giorno, _mese, _anno;
		Integer _ora, _minuti, _secondi, _decimali;

		//almaviva5_20100209 split per nanosecondi
		//tokens[0] --> input_date classica
		//tokens[1] --> contiene i nanosecondi del timestamp
		String[] tokens = value.split(NANOS_SEPARATORE);
		value = tokens[0];	// per retro-compatibilità
		boolean hasNanos = (tokens.length == 2);
		//

		if (verifyDateFormat(value)) {
			_anno = new Integer(value.substring(0, 4));
			_mese = new Integer(value.substring(4, 6));
			_giorno = new Integer(value.substring(6, 8));
			_ora = new Integer(value.substring(8, 10));
			_minuti = new Integer(value.substring(10, 12));
			_secondi = new Integer(value.substring(12, 14));
			String milli = "0";
			if (value.length() > 14) {
				_decimali = new Integer(value.substring(15));
				milli = value.substring(15);
			} else
				_decimali = new Integer(0);

			if (verifySbnDatavar(_giorno, _mese, _anno, _ora, _minuti,
					_secondi, _decimali)) {
				anno = _anno;
				mese = _mese;
				giorno = _giorno;
				ora = _ora;
				minuti = _minuti;
				secondi = _secondi;
				decimali = _decimali;

				// Ricostruisco la ORIGINAL DATE (compresi gli 0)
				// Solito Formato yyyyMMddhhmmss.t
				original_date = anno.toString() + aggiungiZero(mese)
						+ aggiungiZero(giorno) + aggiungiZero(ora)
						+ aggiungiZero(minuti) + aggiungiZero(secondi) + "."
						+ milli;// decimali.toString();
				// String dec = decimali.toString();
				// while (dec.length()<3)
				// dec+="0";
				//
				// original_date +=dec;

			} else
				ok_date = false;

			ok_date = true;
			//almaviva5_20100209 check nanosecondi
			if (hasNanos) {
				time_stamp_orginal_value = new Timestamp(getMillis());
				time_stamp_orginal_value.setNanos(Integer.valueOf(tokens[1]));
			}
			log.debug("SbnDatavar: T005=" + getT005Date() + ", completo=" + getDate() );

		} else
			ok_date = false;
	}

	/**
	 * METODO DI CONFRONTO DATE (giorni)
	 * <p>
	 * Versione operativa sull'oggetto corrente riceve una sola data di
	 * riferimento
	 * </p>
	 *
	 * @param seconda
	 *            data da confrontare
	 * @return result risultato (999999999 se data precedente 1,1,1970)
	 */
	public long diffGiorniDate(SbnDatavar data_due) throws EccezioneFactoring {
		return diffGiorniDate(this, data_due);
	}

	/**
	 * METODO DI CONFRONTO DATE
	 * <p>
	 * Torna il numero dei giorni di differenza tra due date Controlla SOLO per
	 * i primi 14 caratteri che siano TUTTI NUMERI
	 * </p>
	 *
	 * @param prima
	 *            data da confrontare (la maggiore per non ottenere un valore
	 *            negativo)
	 * @param seconda
	 *            data da confrontare nota importante: il mese deve essere
	 *            compreso tra 0 e 11 le date che provengono da xml sono nella
	 *            versione da 1 a 12 per cui sottraiamo 1 al valore del mese
	 * @return result risultato (999999999 se data precedente 1,1,1970)
	 */
	public long diffGiorniDate(SbnDatavar data_uno, SbnDatavar data_due)
			throws EccezioneFactoring {
		long result = 999999999;

		try {
			result = (data_uno.getMillis() - data_due.getMillis())
					/ one_day_mills;
		} catch (Exception ex) {
			log.error(ex);
			throw new EccezioneFactoring(411, "Formato data non previsto", ex);
		}

		// log.debug("GIORNI DIFFERENZA TRA LE DATE:"+result+":") ;
		return result;
	}

	/**
	 * METODO DI CONFRONTO DATE (completo)
	 * <p>
	 * Versione operativa sull'oggetto corrente riceve una sola data di
	 * riferimento
	 * </p>
	 *
	 * @param seconda
	 *            data da confrontare
	 * @return result risultato (999999999 se data precedente 1,1,1970)
	 */
	public long diffDate(SbnDatavar data_due) throws EccezioneFactoring {
		return diffDate(this, data_due);
	}

	/**
	 * METODO DI CONFRONTO DATE
	 * <p>
	 * Torna la differenza tra due date
	 * </p>
	 *
	 * @param prima
	 *            data da confrontare (la maggiore per non ottenere un valore
	 *            negativo)
	 * @param seconda
	 *            data da confrontare nota importante: il mese deve essere
	 *            compreso tra 0 e 11 le date che provengono da xml sono nella
	 *            versione da 1 a 12 per cui sottraiamo 1 al valore del mese
	 * @return result risultato (999999999 se data precedente 1,1,1970)
	 */
	public long diffDate(SbnDatavar data_uno, SbnDatavar data_due)
			throws EccezioneFactoring {
		long result = 999999999;

		try {
			result = data_uno.getMillis() - data_due.getMillis();
		} catch (Exception ex) {
			log.error(ex);
			throw new EccezioneFactoring(411, "Formato data non previsto", ex);
		}
		return result;
	}

	/**
	 * METODO DI VERIFICA FORMATO DATA
	 * <p>
	 * Verifica la data in base al formato standard yyyyMMddhhmmss Controlla
	 * SOLO per i primi 14 caratteri che siano TUTTI NUMERI
	 * </p>
	 *
	 * @param input_date
	 *            data in input
	 * @return result risultato (true = data ok /false = data ko)
	 */
	public boolean verifyDateFormat(String input_date) {
		if (input_date == null)
			return false;
		if (input_date.length() == 0)
			return false;

		// VERIFICA "FISSA" PER I SOLI PRIMI 15 CARATTERI
		// DOPO C'E' IL PUNTO!!
		for (int indice = 0; indice < 14; indice++) {
			if (!Character.isDigit(input_date.charAt(indice)))
				return false;
		}
		return true;
	}

	/**
	 * METODO DI VERIFICA FORMATO DATA
	 * <p>
	 * Verifica la data in base al formato standard yyyyMMddhhmmss.t
	 * </p>
	 *
	 * @param input_date
	 *            Integer con le varie porzioni della data
	 * @return result risultato (true = data ok /false = data ko)
	 */
	public boolean verifySbnDatavar(Integer dummy_giorno, Integer dummy_mese,
			Integer dummy_anno, Integer dummy_ora, Integer dummy_minuti,
			Integer dummy_secondi, Integer dummy_decimi_sec) {
		int mesi_anno[] = new int[12];

		// Setto il calendario
		mesi_anno[0] = mesi_anno[2] = mesi_anno[4] = mesi_anno[6] = mesi_anno[7] = mesi_anno[9] = mesi_anno[11] = 31;
		mesi_anno[3] = mesi_anno[5] = mesi_anno[8] = mesi_anno[10] = 30;

		// Anche il bisestile
		if ((dummy_anno.intValue() % 4) != 0
				|| (!((dummy_anno.intValue() % 4) != 0)
						&& (dummy_anno.intValue() % 400) != 0 && !((dummy_anno
						.intValue() % 100) != 0)))
			mesi_anno[1] = 28;
		else
			mesi_anno[1] = 29;

		// Verifico la data
		if ((dummy_anno.intValue() < 1900 || dummy_anno.intValue() > 2100)
				|| (dummy_mese.intValue() > 12 || dummy_mese.intValue() < 1)
				|| (dummy_giorno.intValue() > mesi_anno[dummy_mese.intValue() - 1])
				|| (dummy_giorno.intValue() < 1) || (dummy_ora.intValue() > 24)
				|| (dummy_ora.intValue() < 0) || (dummy_minuti.intValue() > 59)
				|| (dummy_minuti.intValue() < 0)
				|| (dummy_secondi.intValue() > 59)
				|| (dummy_secondi.intValue() < 0) ||
				// (dummy_decimi_sec.intValue() >9) ||
				(dummy_decimi_sec.intValue() < 0))
			return false;

		return true;
	}

	/**
	 * METODO DI GETTING DATA:
	 * <p>
	 * TORNO LA DATA IN FORMATO STRINGA {ts 'yyyy-MM-dd hh:mm:ss.t'}
	 * </p>
	 *
	 * @param NESSUNO
	 * @return Stringa
	 */
	public String getJdbcDate() {
		// Coincide con original_date
		// return getDate() ;

		log.info("-------->DATA ORIGINALE:" + original_date + ":");
		return "{ts '" + original_date.substring(0, 4) + "-"
				+ original_date.substring(4, 6) + "-"
				+ original_date.substring(6, 8) + " "
				+ original_date.substring(8, 10) + ":"
				+ original_date.substring(10, 12) + ":"
				+ original_date.substring(12, 16) + "'}";
	}

	/**
	 * METODO DI GETTING DATA UTILIZZATO NEI REPORT
	 * <p>
	 * TORNO LA DATA IN FORMATO STRINGA dd/MM/yyyy(hh:mm:ss.t)
	 * </p>
	 *
	 * @param NESSUNO
	 * @return Stringa
	 */
	public String getReportDate() {
		if (original_date.length() > 15) {
			return (original_date.substring(6, 8) + "/"
					+ original_date.substring(4, 6) + "/"
					+ original_date.substring(0, 4) + "("
					+ original_date.substring(8, 10) + ":"
					+ original_date.substring(10, 12) + ":"
					+ original_date.substring(12, 16) + ")");
		}

		return (" ");
	}

	/**
	 * METODO DI GETTING DATA:
	 * <p>
	 * Torno la data da formato yyyyMMddhhmmss.t
	 * </p>
	 *
	 * @param NESSUNO
	 * @return input_date data in input
	 */
	public String getDate() {
		String date = (getNanos() == 0 ? original_date : original_date + NANOS_SEPARATORE + getNanos() );
		return date;
	}

	/**
	 * METODO DI GETTING TIME STAMP ORIGINALE:
	 * <p>
	 * Torno l'oggetto di tipo Timestamp (formato yyyyMMddhhmmss.t)
	 * </p>
	 *
	 * @param NESSUNO
	 * @return input_date data in input
	 */
	public Timestamp getTimestamp() {
		return (time_stamp_orginal_value);
	}

	/**
	 * METODO DI CLEAR ATTRIBUTI DATA
	 * <p>
	 * Effettua la pulizia campi dettaglio data Utile ad esempio per un clear se
	 * data errata
	 * </p>
	 *
	 * @param NESSUNO
	 * @return void nessun ritorno
	 */
	public void clearDate() {
		super.clearDate();
		decimali = null;
		time_stamp_orginal_value = null;
	}

	/**
	 * METODO DI COMPARE DI DUE DATE
	 * <p>
	 * Confronta formato completo (solito yyyyMMddhhmmss.t)
	 * </p>
	 *
	 * @param second_date
	 *            data da confrontare con la corrente
	 * @return result risultato (-1 or 0 or 1) -1 se istanza MINORE data input 0
	 *         se istanze UGUALE data input 1 se istanza MAGGIORE data input
	 */
	public int compareTo(SbnDatavar second_date) {
		int result_difference = 0;

		result_difference = super.compareTo(second_date);

		if (result_difference == 0)
			result_difference = ora.toString().compareTo(
					second_date.getOra().toString());

		if (result_difference == 0)
			result_difference = minuti.toString().compareTo(
					second_date.getMinuti().toString());

		if (result_difference == 0)
			result_difference = secondi.toString().compareTo(
					second_date.getSecondi().toString());

		if (result_difference == 0)
			result_difference = decimali.toString().compareTo(
					second_date.getDecimali().toString());

		return (result_difference);
	}

	/**
	 * METODO DI COMPARE DI DUE DATE
	 * <p>
	 * Versione 2: La data in input per il confronto è una stringa (in formato
	 * standard yyyyMMddhhmmss.t)
	 *
	 * Converto la stringa in data ed eseguo il metodo "gemello" di confronto
	 * </p>
	 *
	 * @param second_date
	 *            stringa da confrontare con la corrente
	 * @return result risultato (-1 or 0 or 1) -1 se istanza MINORE data input 0
	 *         se istanze UGUALE data input 1 se istanza MAGGIORE data input
	 */
	public int compareTo(String input_date) {
		SbnDatavar dummy_date = new SbnDatavar(input_date);

		return (compareTo(dummy_date));
	}

	/**
	 * METODO DI GET CAMPO DECIMALI
	 *
	 */
	protected Integer getDecimali() {
		return (decimali);
	}

	public long getMillis() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		long time = 0;
		try {
			// time =
			// sdf.parse(getAnno()+getMese()+getGiorno()+getOra()+getMinuti()+getSecondi()+trasformaInMillis(getDecimali())).getTime();
			String date = original_date;
			String decimali = original_date.substring(original_date
					.lastIndexOf(".") + 1);
			for (int index = decimali.length(); index < 3; index++)
				date += "0";

			time = sdf.parse(date).getTime();
			// sdf.parse(original_date)
		} catch (ParseException e) {
			log.error("", e);
		}

		return time;

	}

	@Override
	public String toString() {
		String string = "[SbnDatavar = " + getDate() + "]";
		return string;
	}

	public int getNanos() {
		// almaviva5_20100209
		if (time_stamp_orginal_value == null)
			return 0;
		else
			return time_stamp_orginal_value.getNanos();
	}

	public String getT005Date() {
    	if (original_date.length() > 16)
    		return original_date.substring(0, 16);

    	return original_date;
	}

	public static void main(String arg[]) throws InterruptedException {

		Timestamp ts = TableDao.now();
		ts.setNanos(5377);
		SbnDatavar data = new SbnDatavar(ts);

		System.out.println("SbnDataVar T005: " + data.getT005Date());
		System.out.println("SbnDataVar nano:  " + data.getDate());
	}

}
