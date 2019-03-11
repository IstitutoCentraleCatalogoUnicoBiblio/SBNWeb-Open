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

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.log4j.Logger;


public class MarcRecord {

	private static final String _INFO_SEPARATOR = "#";

	private static final String[] NO_INDICATOR_TAGS = {"001", "002", "003", "004", "005", "006", "007", "008", "009" };

	public static final String _SUBFIELD_SEPARATOR = "$";

	private String leader;
	private String info;
	private String data;
	private int numTags;
	private Map<String, String> recordMap = new THashMap<String, String>(50);
	private Map<Integer, String> chiavi = new THashMap<Integer, String>(50);

	private byte[] bytes;
	private Map<String, String[]> subfields = new THashMap<String, String[]>(50);

	private Logger log;
	private int nr;

	public MarcRecord() {
		super();
	}

	public MarcRecord loadTags(String leader, String info, String data, int nr, Logger log) throws Exception {
		this.leader = leader;
		this.info = info;
		this.data = data;
		bytes = data.getBytes("UTF8");
		this.nr = nr;
		this.log = log;
		// carica hash table con posizione e lunghezza (info) dei tag

		recordMap.clear();
		chiavi.clear();
		subfields.clear();
		numTags = 0;

		String tag;
		String appo;
		int numTagDist = 0;
		int len = this.info.length();
		for (int i = 0; i < len - 1; i += 12) {
			tag = this.info.substring(i, i + 3);
			if (recordMap.containsKey(tag)) {
				appo = recordMap.get(tag) + _INFO_SEPARATOR;
				recordMap.put(tag, appo + this.info.substring(i + 3, i + 12));
			} else {
				recordMap.put(tag, this.info.substring(i + 3, i + 12));
				// elenco dei tag senza duplicazioni
				chiavi.put(new Integer(numTagDist), tag);
				numTagDist++;
			}
			numTags++;
		}

		return this;
	}

	public Map<String, String> getHashTable(){
		return recordMap;
	}

	public Map<Integer, String> getChiavi(){
		return chiavi;
	}

	private boolean tagExist(String tagName){
		return recordMap.containsKey(tagName);
	}

	private int getTagLength(String tagName, int index){
		return (tagExist(tagName)) ? Integer.parseInt(recordMap.get(tagName).split(_INFO_SEPARATOR)[index].substring(0, 4)) : -1;
	}

	private int getTagPosition(String tagName, int index){
		return (tagExist(tagName)) ? Integer.parseInt(recordMap.get(tagName).split(_INFO_SEPARATOR)[index].substring(4)) : -1;
	}

	// leader non incluso nel conteggio
	public int getNumTags(){
		return numTags;
	}

	public String getLeader() {
		return leader;
	}

	public String getInfo() {
		return info;
	}

	public String getData() {
		return data;
	}

	public String[] getData(String tagName) {

		String[] result = subfields.get(tagName);
		if (result != null)
			return result;

		int pos = 0;
		int len = 0;
		String[] info = (tagExist(tagName)) ? recordMap.get(tagName).split(_INFO_SEPARATOR) : new String[0];
		result = new String[info.length];
		try {
			for (int i = 0; i < info.length; i++) {
				pos = getTagPosition(tagName, i);
				len = getTagLength(tagName, i);

				byte[] buf = new byte[len];
				System.arraycopy(bytes, pos, buf, 0, len);

				result[i] = new String(buf, "UTF8");
			}

		} catch (IndexOutOfBoundsException e) {
			String rec = String.format("record %06d", nr);
			log.error(rec + " errore tag: " + tagName + ", pos: " + pos + ", len: " + len);
			return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		}

		subfields.put(tagName, result);
		return result;
	}

	private String stripEsaDecimal(String data){
		String datanew = "";
		char[] chars = data.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if ((String.valueOf(chars[i]).hashCode() != Integer.parseInt("1E",	16)) && // record-end char
					(String.valueOf(chars[i]).hashCode() != Integer.parseInt("1D", 16))) { // record-start char
				datanew += String.valueOf(chars[i]);
			}
		}
		return datanew;
	}

	public String getIdRecord(){

		// Inizio Intervento interno 11.05.2012 nel trattamento di import dei documenti nella stringa IT\ICCU\CFIV\010140
		// devono essere sostituiti i caratteri / i caratteri | con niente e , se presente la stringa ITICCU, questa deve essere eliminata
		// cosi' come nella costruzione dell'idLink
		String idRecord = stripEsaDecimal(getData("001")[0]);
		// Check standard SBN
		if (!isStandardSBN(idRecord, "001")) {
			idRecord = idRecord.replaceAll("\\\\", "");	// es. IT\ICCU\CFIV\010140
			idRecord = idRecord.replaceAll("|", "");	// es. IT|ICCU|CFIV|010140
			if (idRecord.startsWith("ITICCU")) idRecord = idRecord.replaceAll("ITICCU", "");
		}
		return ValidazioneDati.trunc(idRecord, 30);
//		return stripEsaDecimal(getData("001")[0]);
		// Fine
	}


	public String getLivelloBibliografico() {
		return getLeader().substring(7, 8);
	}

	public String getLivelloGerarchico() {
		return getLeader().substring(8, 9);
	}

	public String getIndicatore1(String tag, int index) {
		return !ValidazioneDati.in(tag, NO_INDICATOR_TAGS) ? getData(tag)[index].substring(0, 1) : "";
	}

	public String getIndicatore2(String tag, int index) {
		return !ValidazioneDati.in(tag, NO_INDICATOR_TAGS) ? getData(tag)[index].substring(1, 2) : "";
	}

	public String getIdLink(String tag, int index) {
		String idLink = stripEsaDecimal(getData(tag)[index]);
		int pos = idLink.indexOf(_SUBFIELD_SEPARATOR + "1001");
		if (pos > -1) {
			String tmp = idLink.substring(pos + 5);// (pos+2);
			pos = tmp.indexOf(_SUBFIELD_SEPARATOR);
			return (pos > -1) ? tmp.substring(0, pos) : tmp;
		} else {
			// $3 presente come id_link in 4xx, 5xx, 6xx, 7xx
			if ((!tag.startsWith("4")) && (!tag.startsWith("5"))
					&& (!tag.startsWith("6")) && (!tag.startsWith("7"))) {
				return "";
			}
			pos = idLink.indexOf(_SUBFIELD_SEPARATOR + "3");
			if (pos > -1) {
				String tmp = idLink.substring(pos + 2);
				pos = tmp.indexOf(_SUBFIELD_SEPARATOR);
				// return (pos>-1) ? tmp.substring(0, pos) : tmp;

				// 20120316
				// Check standard SBN
				String idValue = (pos > -1) ? tmp.substring(0, pos) : tmp;
				if (!isStandardSBN(idValue, tag)) {
					//idValue = idValue.replaceAll(arg0, arg1)
					idValue = idValue.replaceAll("\\\\", "");	// es. IT\ICCU\CFIV\010140
					idValue = idValue.replaceAll("|", "");	// es. IT|ICCU|CFIV|010140
					if (idValue.startsWith("ITICCU")) idValue = idValue.replaceAll("ITICCU", "");

//					String sep = (idValue.indexOf("|")>-1) ? "|" : "\\\\";
//					String[] tokens = idValue.split(sep);
//					if (tokens.length>3){
//						// Es. Id non standard
//						// IT\ICCU\CFIV\010140
//						// IT|ICCU|CFIV|010140
//						idValue = tokens[2] + tokens[3];
//					}
				}
				return idValue;
			}
		}
		return "";
	}

	public String getData(String tag, int index, boolean stripIndicator) {
		String data = getData(tag)[index];
		if (stripIndicator && !ValidazioneDati.in(tag, NO_INDICATOR_TAGS))
			data = data.substring(2);

		return data;
	}

	public String getData(String tag, int index){
		return getData(tag, index, false);
	}

	/**
	 * Controlla se l'identificativo in input rispetta o meno lo standard SBN.
	 *
	 * @param id identificativo da esaminare
	 * @param tagUnimarc tag unimarc a cui appartiene l'identificativo
	 * @return true (standard) ; false (non standard)
	 */
	public static boolean isStandardSBN(String id, String tagUnimarc){
		boolean esito = false;
		if (id.length() == 10) {
			String ch1_3 = id.substring(0, 3);
			String ch4 = id.substring(3, 4);
			String ch5_10 = id.substring(4);
			// primi 3 caratteri alfanumerici
			if (ch1_3.matches("[0-9a-zA-Z][0-9a-zA-Z][0-9a-zA-Z]")) {
				// caratteri numerici dal quinto in poi
				if (ch5_10.matches("[0-9][0-9][0-9][0-9][0-9][0-9]")) {
					// ulteriori controlli a seconda del tag unimarc di appartenenza
					if (("001".equals(tagUnimarc))
							|| (tagUnimarc.startsWith("4"))
							|| (tagUnimarc.startsWith("5"))) {
						esito = ((ch4.matches("[0-9]")) || ("E".equals(ch4))) ? true
								: false;
					} else {
						if (tagUnimarc.startsWith("6")) {
							esito = ("C".equals(ch4)) ? true : false;
						} else if (tagUnimarc.startsWith("7")) {
							esito = ("V".equals(ch4)) ? true : false;
						}
					}
				}
			}
		}
		return esito;
	}

	/**
	 * Restituisce il valore dei sottocampi unimarc ricevuti in
	 * input (parameter subfields) ed eventualmente presenti nel
	 * tag unimarc (parameter data).
	 *
	 * @param data String valore del tag unimarc completo di sottocampi
	 * @param subfields String[] elenco dei sottocampi separati da underscore (es. _a_b_c)
	 * @return string - stringa vuota in caso di assenza di risultati
	 */
	private static String getString(String data, String subfields){
		int pos = -1;
		int pos2 = -1;
		String tmp = data;
		String result = "";
		String subf = "";
		pos = tmp.indexOf(_SUBFIELD_SEPARATOR);
		while (pos > -1) {
			// verifica presenza separatori di sottocampo
			// scorre tutti i sottocampi presenti
			subf = ((tmp.length() > (pos + 1))) ? tmp.substring(pos + 1, pos + 2) : " ";
			if (subfields.indexOf("_" + subf) > -1) {
				// sottocampo richiesto individuato
				// aggiunge
				pos2 = tmp.substring(pos + 2).indexOf(_SUBFIELD_SEPARATOR) + (pos + 2);
				if (pos2 > -1) {
					result += tmp.substring(pos + 2).substring(0, pos2 - pos - 2);
				} else {
					// fine dati
					result += tmp.substring(pos + 2);// .substring(0);
				}
				pos = pos2;
			} else {
				// sottocampo non richiesto
				pos = (tmp.substring(pos + 1).indexOf(_SUBFIELD_SEPARATOR) > 1) ?
						tmp.substring(pos + 1).indexOf(_SUBFIELD_SEPARATOR) + (pos + 1) : -1;
			}
		}
		return result;
	}

	/**
	 * Blocco dei legami alle intestazioni (unimarc 7xx)
	 *
	 * @param data String valore del tag unimarc completo di sottocampi
	 * @return autore
	 */
	public static String getAutoreString(String data){
		return MarcRecord.getString(data, "_a_b_c");
	}
	/**
	 * Blocco dei legami (unimarc 4xx)
	 *
	 * @param tag
	 * @param data String valore del tag unimarc completo di sottocampi
	 * @return
	 */
	public static String getTitoloLegameString(String tag, String data) {
/*
- dati del documento linkato dalla 463

=LDR  01504naM2a2200265   450
=001  PUV0320378
=005  20051205
=100  \\$a20000831e19611911|||y0itaa0103    ba
=101  \\$alat
=102  \\$ade
=200  1\$aH5: IAemilia sive Provincia Ravennas$fcongessit Paulus Fridolinus Kehr

=463  \0$1001PUV0320378$1101  $alat$1102  $ade$12001 $aH5: IAemilia sive Provincia Ravennas$fcongessit Paulus Fridolinus Kehr$1205  $aRist. anast$1210  $aBerolini$cWeidmann$d1961$1215  $aLIV, 534 p.$d26 cm.$v5
*/
//		return MarcRecord.getString(data, "_a_b_c");
		return "";
	}
	/**
	 * Blocco dei titoli in relazione (unimarc 5xx)
	 *
	 * @param tag
	 * @param data String valore del tag unimarc completo di sottocampi
	 * @return
	 */
	public static String getTitoloRelazioneString(String tag, String data) {
//		return MarcRecord.getString(data, "_a_b_c");
		return "";
	}

// -- inizio area documento fisico
	/**
	 * Informazioni sul documento fisico (nome biblioteca)
	 *
	 * @param data String valore del tag unimarc 950 completo di sottocampi
	 * @return nome della biblioteca
	 */
	public static String getDocFisicoNomeBiblioteca(String data) {
		return MarcRecord.getString(data, "_a");
	}
	/**
	 * Informazioni sul documento fisico (consistenza esemplare)
	 *
	 * @param data String valore del tag unimarc 950 completo di sottocampi
	 * @return consistenza dell'esemplare
	 */
	public static String getDocFisicoConsistenzaEsemplare(String data) {
		return MarcRecord.getString(data, "_b");
	}
	/**
	 * Informazioni sul documento fisico (consistenza collocazione)
	 *
	 * @param data String valore del tag unimarc 950 completo di sottocampi
	 * @return consistenza di collocazione
	 */
	public static String getDocFisicoConsistenzaCollocazione(String data) {
		return MarcRecord.getString(data, "_c");
	}
	/**
	 * Informazioni sul documento fisico (collocazione)
	 *
	 * @param data String valore del tag unimarc 950 completo di sottocampi
	 * @return collocazione
	 */
	public static String getDocFisicoCollocazione(String data) {
		return MarcRecord.getString(data, "_d");
	}
	/**
	 * Informazioni sul documento fisico (inventario)
	 *
	 * @param data String valore del tag unimarc 950 completo di sottocampi
	 * @return inventario
	 */
	public static String getDocFisicoInventario(String data) {
		return MarcRecord.getString(data, "_e");
	}
// -- fine area documento fisico

}
