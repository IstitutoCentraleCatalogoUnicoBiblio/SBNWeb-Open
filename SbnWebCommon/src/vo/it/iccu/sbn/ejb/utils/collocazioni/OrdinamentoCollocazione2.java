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
package it.iccu.sbn.ejb.utils.collocazioni;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.ClassiUtil;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;

/**
 * @author almaviva
 */
public final class OrdinamentoCollocazione2 {

	private static final OrdinamentoCollocazione2 instance = new OrdinamentoCollocazione2();

	private final static char BLANK = (char) 32;
	private final static String STR_BLANK = String.valueOf(BLANK);
	private final OrdinamentoUnicode unicode;

	private OrdinamentoCollocazione2() {
		this.unicode    = new OrdinamentoUnicode();
	}

	private String gruppoNumerico(String gruppo) {

		// Calcolo la lunghezza del gruppo numerico e del carattere
		// di controllo
		int limit;
		int sestine = 0;

		//tolgo gli zeri non significativi
		//almaviva5_20120510 #30 LIG
		gruppo = Long.valueOf(gruppo).toString();

		int len = gruppo.length();
		if ( (len % 6) == 0)
			return gruppo;

		for (limit = 5; limit < len; limit += 6)
			sestine++;

		// Formatta il numero a 6 (...12....18..24 Etc) cifre
		// mettendo davanti tutti '0'
		if (sestine > 0)
			return String.valueOf(sestine) + ValidazioneDati.fillLeft(gruppo, '0', limit);
		else
			return ValidazioneDati.fillLeft(gruppo, '0', limit + 1);
	}


	@SuppressWarnings("unused")
	private String gruppoNumericoDewey(String gruppo) {

		// Calcolo la lunghezza del gruppo numerico e del carattere
		// di controllo
		int limit;

		//tolgo gli zeri non significativi
		gruppo = Integer.valueOf(gruppo).toString();

		int len = gruppo.length();
		if ( (len % 6) == 0)
			return gruppo;

		for (limit = 6; limit < len; limit += 6)
			continue;

		return ValidazioneDati.fillRight(gruppo, '0', limit);
	}

	String normalizzaCollocazioneDewey(String coll) {

		coll = coll.trim();
		if (!ClassiUtil.isDeweyColl(coll))
			// torno alla normalizzazione classica
			return normalizzaCollocazione(coll);

		String[] token = coll.split("\\.");
		if (token.length == 1)
			return gruppoNumerico(token[0]);

		String norm = gruppoNumerico(token[0]);
		for (int t = 1; t < token.length; t++) {
			if (ValidazioneDati.strIsAlfabetic(token[t]) && t == 1) //solo 2° gruppo
				norm += STR_BLANK + STR_BLANK + unicode.convert(token[t]);
			else
				//if (ValidazioneDati.strIsNumeric(token[t]))
					norm += STR_BLANK + unicode.convert(token[t]);
		}

		return norm;
	}

	String normalizzaCollocazione(String src) {

		if (!ValidazioneDati.isFilled(src) )
			return null;

		StringBuilder buf = new StringBuilder(160);
		String coll = src.toUpperCase().trim();

		boolean isSpace = false;
		boolean isNum = false;
		StringBuilder numeri = new StringBuilder(24);
		char curr = BLANK; // spazio
		char prev = (char)0;

		int length = coll.length();
		for (int pos = 0; pos < length; pos++) {
			curr = coll.charAt(pos);

			if (Character.isDigit(curr) ) {
				if (Character.isLetter(prev))
					buf.append(BLANK);
				isSpace = false;
				isNum = true;
				numeri.append(curr);
			} else {
				if (isNum) {
					buf.append(gruppoNumerico(numeri.toString()));
					buf.append(BLANK);
					isNum = false;
					isSpace = true;
					numeri.setLength(0);
				}

				switch (curr) {
				case '.':
				case '-':
				case '/':
					if ((!isSpace) && (!isNum)) {
						isSpace = true;
						isNum = false;
						buf.append(BLANK);
					}
					break;

				case BLANK:
					if (!isSpace) {
						isSpace = true;
						isNum = false;
						buf.append(BLANK);
					}
					break;

				default:
					isSpace = false;
					isNum = false;
					buf.append(curr);
				}
			} // end else
			prev = curr;
		}

		// Verifico se devo aggiornare la stringa
		if (isNum) {
			buf.append(gruppoNumerico(numeri.toString()));
			isNum = false;
			numeri.setLength(0);
		}

		//Elimino caratteri unicode non previsti
		String norm = unicode.convert(buf.toString()).trim().replaceAll("\\s+", STR_BLANK);

		return ValidazioneDati.trunc(norm, 80);
	}

	public static final String normalizza(String src) {
		return normalizza(src, false);
	}

	public static final String normalizza(String src, boolean isDewey) {
		if (isDewey)
			return instance.normalizzaCollocazioneDewey(src);
		else
			return instance.normalizzaCollocazione(src);
	}

	private static void testDewey(String prima) {
		String coll = OrdinamentoCollocazione2.normalizza(prima, true);
		System.out.println("'" + prima + "' --> '" + coll + "'");
	}

	private static void test(String prima) {
		String coll = OrdinamentoCollocazione2.normalizza(prima);
		System.out.println("'" + prima + "' --> '" + coll + "'");
	}

	public static void main(String[] args) {

		test("016.3484518022");

		test("lti.a.100bb");
		test("lti.100bb");
		test("lma.22/09/2009 DOC.");
		test("abc.123456");
		test("abc.1234567");
		test("ddd.00005678");

		testDewey("708 ITI /");
		testDewey("123.00200001");
		testDewey("123.222");
		testDewey("123.1239991a");
		testDewey("004.CIOTF");
		testDewey("123.003.TEST.abc");
		testDewey("708");
		testDewey("708.");
	}

}
