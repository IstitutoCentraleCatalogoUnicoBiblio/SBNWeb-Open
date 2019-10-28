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
package it.iccu.sbn.util.sbnmarc;


import it.finsiel.gateway.exception.SbnMarcDiagnosticoException;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

import ch.lambdaj.function.matcher.LambdaJMatcher;


public final class SBNMarcUtil {

	public static final String SBNMARC_DEFAULT_ID = "0000000000";

	private static final String[] HOURS = new String[] { "H", "HH", "HR", "HOUR", "ORA", "ORE" };
	private static final String[] MINS = new String[] { "M", "MM", "MIN" };
	private static final String[] SECS = new String[] { "S", "SS", "SEC" };

	private static final Pattern TAG_127 = Pattern.compile("\\d{6}");
	private static final Pattern NUMBERS = Pattern.compile("[^\\d]*(\\d+)[^\\d]*");

	private enum Type {
		HOUR,
		MINUTE,
		SECOND;
	};

	private static class Duration {

		int hours;
		int mins;
		int secs;

		void addHours(int value) {
			hours += value;
		}

		void addMinutes(int value) {
			int tmp = mins;
			tmp += value;
			if (tmp > 59) {
				int h = tmp / 60;
				addHours(h);
				mins += (tmp - (60 * h));
			} else
				mins = tmp;

		}

		void addSeconds(int value) {
			int tmp = secs;
			tmp += value;
			if (tmp > 59) {
				int m = tmp / 60;
				addMinutes(m);
				secs += (tmp - (60 * m));
			} else
				secs = tmp;
		}

		@Override
		public String toString() {
			return String.format("%02d%02d%02d", hours, mins, secs);
		}

	}

	/*
	 * Matcher che restituisce true se la lista fornita ha un elemento uguale a l'item target
	 */
	private static class InversionIn<T> extends BaseMatcher<T> {

		private static final String NOT_USED = "*";

		private final T operand;
		private final String separator;

		public InversionIn(T operand, String separator) {
			this.operand = operand;
			this.separator = separator;
		}

		public void describeTo(Description description) {
			description.appendValue(operand);
		}

		public boolean matches(Object item) {
			String value = operand.toString();
			String condition = item.toString();
			if (condition.equals(NOT_USED))
				return true;

			boolean neg = false;
			if (condition.startsWith("!")) {
				condition = condition.substring(1);
				neg = true;
			}

			String[] tokens = condition.split(separator);
			for (String token : tokens)
				if (token.trim().equalsIgnoreCase(value))
					return !neg;


			return neg;
		}
	}

	@Factory
	private static <T> org.hamcrest.Matcher<T> iin(T operand) {
		return new InversionIn<T>(operand, ";");
	}


	private static Type getType(String value) {
		String v = value.trim().toUpperCase();
		for (String h : HOURS)
			if (v.contains(h))
				return Type.HOUR;

		for (String m : MINS)
			if (v.contains(m))
				return Type.MINUTE;

		for (String s : SECS)
			if (v.contains(s))
				return Type.SECOND;

		return null;
	}

	public static String parseTag127(final String tag) {
		//almaviva5_20141030 evolutive area zero
		if (TAG_127.matcher(tag).matches())
			return tag;

		Duration d = new Duration();

		for (String token : tag.split(",|\\.")) {
			token = token.trim();
			Type type = getType(token);
			if (type == null)
				continue;

			//ricerca gruppo numerico;
			Matcher m = NUMBERS.matcher(token);

			if (m.find()) {
				String group = m.group(1);
				int duration = Integer.valueOf(group);
				switch (type) {
				case HOUR:
					d.addHours(duration);
					break;
				case MINUTE:
					d.addMinutes(duration);
					break;
				case SECOND:
					d.addSeconds(duration);
					break;
				}
			}
		}

		String t127 = d.toString();
		return TAG_127.matcher(t127).matches() ? t127 : "000000";
	}



	/**
	 * Converte la data variazione in formato SBN al formato dell'utente.
	 *
	 * @param dataformatoSBN
	 *            data variazione in formato SBN (es.20010418115015.0)
	 *
	 * @return data variazione formato utente (es: 2001-04-18)
	 */
	public static String converteDataVariazione(String dataformatoSBN) {
		String data = null;

		if (dataformatoSBN != null) {
			// Controlla che la stringa sia formata almeno da
			// 8 caratteri in quando questa è la somma dei caratteri
			// da estrarre: anno(4) + mese(2) + giorno(2)
			if (dataformatoSBN.length() >= 8) {
				String year = dataformatoSBN.substring(0, 4);
				String month = dataformatoSBN.substring(4, 6);
				String day = dataformatoSBN.substring(6, 8);
				data = day + "-" + month + "-" + year;
			}
		}

		return data;
	}

	/**
	 * Converte la data da formato utente (dd-mm-yyyy) al formato SBN
	 * (yyyy-mm-dd).
	 *
	 * @param dataFormatoUtente
	 * @return data in formato SBN
	 */
	public static String converteDataUtente(String dataFormatoUtente) {
		String data = null;
		if (dataFormatoUtente != null) {
			if (dataFormatoUtente.length() > 6) {
				String day = dataFormatoUtente.substring(0, 2);
				String month = dataFormatoUtente.substring(3, 5);
				String year = dataFormatoUtente.substring(6);
				data = year + "-" + month + "-" + day;
			}
		}
		return data;
	}

	/**
	 * Converte la data da formato SBN (yyyy-mm-dd) al formato utente
	 * (dd-mm-yyyy).
	 *
	 * @param dataFormatoUtente
	 * @return data in formato Utente
	 */
	public static String converteDataSBN(String dataFormatoSBN) {
		String data = null;
		if (dataFormatoSBN != null) {
			if (dataFormatoSBN.length() > 8) {
				String day = dataFormatoSBN.substring(8);
				String month = dataFormatoSBN.substring(5, 7);
				String year = dataFormatoSBN.substring(0, 4);
				data = day + "-" + month + "-" + year;
			}
		}
		return data;
	}

	public static String converteDataInT005(Date data) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String t005 = format.format(data);

		return t005 + ".5";
	}


	public static String timestampToT005(Timestamp ts) {
		if (ts == null)
			return null;

		SbnMarcT005Spec sts = new SbnMarcT005Spec(ts);
		return sts.getT005Date();
	}

	public static String formattaSbnId(String bid) {
		if (!ValidazioneDati.leggiXID(bid))
			return bid;

		StringBuilder buf = new StringBuilder(32);
		String polo = bid.substring(0, 3);
		String progr = bid.substring(3);
		String antico = "";
		if (progr.startsWith("E")) {
			antico = "E";
			progr = progr.substring(1);
		}
		buf.append("IT\\ICCU\\").append(polo).append(antico).append('\\').append(progr);

		return buf.toString();
	}

	public static BigDecimal getSchemaVersion() {
		double sv;
		try {
			sv = Double.valueOf(CommonConfiguration.getProperty(Configuration.SBNMARC_SCHEMA_VERSION, "1.10"));
		} catch (Exception e) {
			sv = 1.10;
		}
		return BigDecimal.valueOf(sv);
	}

	public static String getDescrizioneArea5(String tipoRecord,
			String tipoVideo, String formaDistrib, String velocita,
			String formaPres, Boolean plurale) throws Exception {

		if (plurale == null) {
			return "";
		}

		//almaviva5_20150505 evolutiva area5
		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_SUPPORTO_AUDIOVIDEO);

		TB_CODICI p = on(TB_CODICI.class);

		//flg2 = tipo record
		//flg3 = tipo video
		//flg4 = forma di distribuzione
		//flg5 = velocità
		//flg6 = Formato di presentazione
		LambdaJMatcher<Object> cnd = having(p.getCd_flg2(), iin(tipoRecord));
		if (isFilled(tipoVideo))
			cnd = cnd.and(having(p.getCd_flg3(), iin(tipoVideo)) );
		if (isFilled(formaDistrib))
			cnd = cnd.and(having(p.getCd_flg4(), iin(formaDistrib)) );
		if (isFilled(velocita))
			cnd = cnd.and(having(p.getCd_flg5(), iin(velocita)) );
		if (isFilled(formaPres))
			cnd = cnd.and(having(p.getCd_flg6(), iin(formaPres)) );

		List<TB_CODICI> select = select(codici, cnd);
		if (!isFilled(select))
			return null;

		TB_CODICI f = ValidazioneDati.first(select);
		return plurale ? (isFilled(f.getDs_cdsbn_ulteriore()) ? f.getDs_cdsbn_ulteriore() : f.getDs_tabella()) : f.getDs_tabella();
	}


	/**
	 * Modifiche maggio 2016 - almaviva2 in collaborazione con  almaviva5
	 * Costruisce il codice URL dell'etichetta 321

	 */

	private static final Map<String, String> URL321_TEMPLATE = new HashMap<String, String>();

	static {
		//British museum
		//URL321_TEMPLATE.put("a", "???");
		//EDIT16
		URL321_TEMPLATE.put("b", "http://edit16.iccu.sbn.it/scripts/iccu_ext.dll?fn=10&i=[ID]");
		//ESTC
		URL321_TEMPLATE.put("c", "http://estc.bl.uk/[ID]");
		//ISTC
		URL321_TEMPLATE.put("d", "http://istc.bl.uk/search/search.html?fieldidx1=dc.identifier&fieldcont1=[ID]&fieldrel1=exact&operation=search");
		//WD16
		URL321_TEMPLATE.put("e", "http://gateway-bayern.de/VD16+[ID]");
		//WD17
		URL321_TEMPLATE.put("f", "http://gso.gbv.de/DB=1.28/SET=2/TTL=1/CMD?MATCFILTER=N&MATCSET=N&ACT0=&IKT0=&TRM0=&ACT3=*&IKT3=8183&ACT=SRCHA&IKT=8002&SRT=YOP&ADI_BIB=&TRM=[ID]&REC=*&TRM3=");
	}

	public static String generatoreURL321(String bd321, String id321) {
		String url321 = "URL CALCOLATA " + bd321 + " " + id321;

		String template = URL321_TEMPLATE.get(bd321);
		if (!isFilled(template))
			return url321;

		try {
			url321 = template.replace("[ID]", URLEncoder.encode(id321, "UTF-8"));
		} catch (UnsupportedEncodingException e) {}


		return url321;
	}

	public static SBNMarc buildMessaggioErrore(SbnMarcDiagnosticoException e, SbnUserType user) {
		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		SbnResponseType response = new SbnResponseType();
		SbnResultType result = new SbnResultType();
		result.setTestoEsito(e.getMessage());
		SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		result.setEsito(String.format("%04d", e.getErrorID()));
		sbnmarc.setSbnMessage(message);
		sbnmarc.setSbnUser(user);
		sbnmarc.setSchemaVersion(getSchemaVersion());
		message.setSbnResponse(response);
		response.setSbnResult(result);
		response.setSbnResponseTypeChoice(responseChoice);
		responseChoice.setSbnOutput(output);

		return sbnmarc;
	}

	public static final String livelloSoglia(final String cd_livello) {
		try {
			int livello = Integer.parseInt(cd_livello.trim() );
			if (livello == 1)
				return "01";
			if (livello == 2)
				return "02";
			if (livello == 3)
				return "03";
			if (livello == 4)
				return "04";
			else if ((livello == 5) || (livello == 96) || (livello == 97))
				return cd_livello;
			else if ((livello > 5) && (livello <= 51))
				return "51";
			else if ((livello > 51) && (livello <= 71))
				return "71";
			else if ((livello > 71) && (livello <= 90))
				return "90";
			else if ((livello > 90) && (livello <= 95))
				return "95";

		} catch (Exception e) {}

		return "05";
	}

	public static void main(String... args) {
		System.out.println(timestampToT005(DaoManager.now()));
		System.out.println(formattaSbnId("XXX1234567"));
		System.out.println(formattaSbnId("XXXE654321"));

		System.out.println(parseTag127("102min. 3hr. 34s"));
		System.out.println(parseTag127("102m3h34s"));

		for (String k : URL321_TEMPLATE.keySet())
			System.out.println(generatoreURL321(k, "BVEE0 06210"));
	}

}
