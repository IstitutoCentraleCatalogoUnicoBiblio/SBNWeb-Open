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
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A_250;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author
 *
 */
public class NormalizzaNomi {

    /** log del factoring */
	private static Logger log = Logger.getLogger("it.finsiel.sbn.polo.factoring.base.NormalizzaNomi");

	private static final char spazioC = '\u0020';
	private static final char[] separatoriC = { spazioC, '\'', '-', '\"', '+', '/', ':', '<', '=', '>', '\\', '&', '@', '#' };
	private static final String spazio = "" + spazioC;

	public static final String REGEX_SEPARATORE_TERMINI_SOGGETTO = ResourceLoader.getPropertyString("REGEX_SEPARATORE_TERMINI_SOGGETTO");
	public static final String REGEX_DESCRITTORI_SEP_ULTERIORI = "\\[.*?\\]|\\s\\.\\s";

	private static final Pattern REGEX_SEPARATORI_SOGGETTO = Pattern.compile("(\\s-\\s)|(\\s\\[.*?\\]\\s)|(\\s\\.\\s)");

	/**
	 * questo tipo di normalizzazione elimina i doppi spazi e i simboli di punteggiatura
	 */
	public static final String normalizzazioneGenerica(final String input)
			throws EccezioneSbnDiagnostico {
		String value = "";
		try {
			value = ValidazioneDati.trimOrEmpty(input);
			StringBuilder buf = new StringBuilder(240);
			StringBuilder scartati = new StringBuilder(240);

			for (int i = 0; i < separatoriC.length; i++)
				value = value.replace(separatoriC[i], spazioC);

			int len = value.length();
			for (int i = 0; i < len; i++) {
				char c = value.charAt(i);
				if ((c != spazioC) && !Character.isLetter(c) && !Character.isDigit(c)) {
					scartati.append(c);
					continue;
				}

				buf.append(c);
			}

			value = UnicodeForOrdinamento2.convert(buf.toString());

			//almaviva5_20170320 controllo elementi scartati
			if (scartati.length() > 0)
				UnicodeForOrdinamento2.convert(scartati.toString());

		} catch (EccezioneSbnDiagnostico e) {
			log.error(e);
			throw e;
		}

		return value.replaceAll("\\s{2,}", spazio).trim(); // doppi spazi
	}

	public static final String eliminaAsterischi(String value) {
		int idx = -1;
		do {
			idx = value.indexOf('*');
			value = value.substring(idx + 1);
		} while (idx >= 0);

		return value;
	}

	static final String normalizzaDescrittore(final String input) throws EccezioneSbnDiagnostico {

		if (!ValidazioneDati.isFilled(input))
			return null;

		//almaviva5_20120416 evolutive CFI
		//nuova suddivisione descrittori (da Indice)
		String result = eliminaAsterischi(input);
		result = normalizzazioneGenerica(result);

		return result.replaceAll("\\s+", " ");

	}

	static final String normalizzaSoggetto(A250 t250) throws EccezioneSbnDiagnostico {
		String ds_sogg = FormatoSoggetto.componiSoggetto(t250);

		List<String> descrittori = new ArrayList<String>();
		String tmp = new String(ds_sogg);
		Matcher m = REGEX_SEPARATORI_SOGGETTO.matcher(tmp);

		String lastSep = "";
		boolean found = m.find();
		while (found) {
			String d = lastSep + spazioC + eliminaAsterischi(tmp.substring(0, m.start()) );
			descrittori.add(d);
			//i separatori vengono normalizzati in stringa vuota mentre
			//i connettori [...] perdono le parentesi
			lastSep = normalizzazioneGenerica(m.group());

			tmp = tmp.substring(m.end());
			m = REGEX_SEPARATORI_SOGGETTO.matcher(tmp);
			found = m.find();
		}
		if (ValidazioneDati.isFilled(tmp))
			descrittori.add(lastSep + spazioC + eliminaAsterischi(tmp) );

		return normalizzaSoggetto(descrittori.toArray(new String[0]));
	}

	static final String normalizzaSoggetto(String input) throws EccezioneSbnDiagnostico {
		if (!ValidazioneDati.isFilled(input))
			return null;

		A250 t250 = new A250();
		A_250 a250 = new A_250();
		a250.setContent(input);
		t250.setA_250(a250);

		return normalizzaSoggetto(t250);
	}

	static final String normalizzaSoggetto(final String[] descrittori) throws EccezioneSbnDiagnostico {

		if (!ValidazioneDati.isFilled(descrittori))
			return null;

		StringBuilder buf = new StringBuilder(240);
		Iterator<String> i = Arrays.asList(descrittori).iterator();

		for (;;) {
			String d = normalizzaDescrittore(i.next());
			buf.append(d);
			if (i.hasNext())
				//almaviva5_20121012 i descrittori sono separati da 2 spazi??
				buf.append(spazioC);
			else
				break;
		}

		return buf.toString();
	}

	public static void main(String arg[]) throws EccezioneSbnDiagnostico {
		System.out.println(normalizzazioneGenerica("arte - ucraina"));
		System.out.println(normalizzazioneGenerica("arte <ucraina>"));
		System.out.println(normalizzazioneGenerica("arte [degli] ucraini"));
		System.out.println(normalizzaSoggetto("Herrad, von#Landsberg . Hortus deliciarum - Herrad, von_Landsberg"));
	}

}
