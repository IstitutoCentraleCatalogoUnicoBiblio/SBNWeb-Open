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
package it.iccu.sbn.batch.unimarc;

import org.apache.log4j.Logger;

public class MarcFile {

	static Logger log = Logger.getLogger(MarcFile.class);

	private static final int _ESA_DOLLAR_SEPARATOR = 0x1F;
	public static final int _ESA_RECORD_END_SEPARATOR_HASHCODE = 30;
	public static final int _ESA_RECORD_START_SEPARATOR_HASHCODE = 29;

	private String leader = "";
	private String tagsInfo = "";
	private String data = "";
	private String badRecords = "";
	private int recordsNumber = 0;
	private int badRecordsNumber = 0;
	private String msgError = "";
	private StringBuilder buf = new StringBuilder(1024);


	/**
	 * Istanzia una classe MarcFile
	 *
	 * @param marc record unimarc
	 */
	public MarcFile() {
		super();
	}

	public static void main (String[] args){
		String ch;
		String appo = "0470";
		char[] chars = appo.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			ch = String.valueOf(chars[i]);
			log.debug("string: " + ch);
			log.debug("hex string: " + Integer.toHexString(ch.hashCode()));
			log.debug("hash code: " + ch.hashCode());
			if (ch.equalsIgnoreCase(Integer.toHexString(MarcFile._ESA_RECORD_END_SEPARATOR_HASHCODE))){
				log.debug(" --> END SEPARATOR");
			}
			if (ch.equalsIgnoreCase(Integer.toHexString(MarcFile._ESA_RECORD_START_SEPARATOR_HASHCODE))){
				log.debug(" --> START SEPARATOR");
			}
			log.debug("\n");
		}
	}

	public MarcFile parse(String input, Logger _log) {

		leader = "";
		tagsInfo = "";
		data = "";
		badRecords = "";
		recordsNumber = 0;
		badRecordsNumber = 0;
		msgError = "";
		buf.setLength(0);

		int inizioDati;
		String leader;
		String info;
		String dati;
		boolean error;
		String msgRecordError = null;

		inizioDati = 0;
		error = false;

		if (_log == null)
			_log = log;

		try {
			char[] chars = input.toCharArray();
			int len = chars.length;
			if (len > buf.capacity())
				buf.ensureCapacity(len);

			for (int i = 0; i < len; i++) {
				char ch = chars[i];

				// Caratteri esadecimali
				if ( ch == _ESA_DOLLAR_SEPARATOR)
					ch = '$';

				buf.append(ch);
			}

			String record = buf.toString();
			if (!error) {
				try {
					// 20120312
					// Errore lunghezza --> WARNING
					int declaredLength = Integer.parseInt(record.substring(0, 5)); //Eccezione: formato record non corretto
					int realLength = record.getBytes("UTF8").length;
					if (declaredLength != realLength) {
						// WARNING: lunghezza tracciata <> lunghezza reale
						msgRecordError = String.format("La lunghezza tracciata nel leader (%d) non corrisponde alla lunghezza reale (%d) del record.", declaredLength, realLength);
					}
					// 12-16 posizione di inizio dei dati
					try {
						inizioDati = Integer.parseInt(record.substring(12, 17).trim());
					} catch (Exception e) {
						_log.error("errore parsing record: ", e);
						error = true;
						msgRecordError = "Impossibile trovare la posizione di inizio dei dati";
					}

				} catch (Exception e) {
					_log.error("errore parsing record: ", e);
					error = true;
					msgRecordError = "Formato record non corretto";
				}

				// ok
				info = record.substring(0, inizioDati);
				// leader + tags info + dati
				//leader = info.substring(0, info.indexOf(" 450") + 5);
				leader = info.substring(0, 24);
				info = info.substring(leader.length());
				dati = record.substring(leader.length() + info.length());
				this.leader = leader;
				this.tagsInfo = info;
				this.data = dati;

				// eventuali warning (lunghezza)
				if (msgRecordError != null)
					msgError = msgRecordError;

				recordsNumber++;
			} else {
				// error
				// malformed - conserva record
				badRecords += record;
				badRecordsNumber++;
				msgError = msgRecordError;
			}

		} catch (Exception e) {
			_log.error("errore parsing record: ", e);
			badRecords += input;
			badRecordsNumber++;
			msgError = (msgRecordError != null) ? msgRecordError : "Errore non previsto";
		}

		return this;
	}

	public int getRecordsNumber() {
		return recordsNumber;
	}

	public String getLeader() {
		return leader;
	}

	public String getTagsInfo() {
		return tagsInfo;
	}

	public String getData() {
		return data;
	}

	/**
	 *
	 * @return bad records (marc format) or null
	 */
	public String getBadRecords(){
		return ("".equals(badRecords)) ? "" : badRecords;
	}

	public int getBadRecordsNumber() {
		return badRecordsNumber;
	}

	/**
	 *
	 * @return error message or null
	 */
	public String getMsgError(){
		return msgError;
	}
}
