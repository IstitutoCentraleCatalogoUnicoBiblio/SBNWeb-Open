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

/**
 * @author almaviva
 */
public final class NormalizzaSequenza {

	private static final NormalizzaSequenza instance = new NormalizzaSequenza();

	private final static char BLANK = (char) 32;

	private NormalizzaSequenza() {
		super();
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

	private String _normalizza(String src) {

		if (!ValidazioneDati.isFilled(src) )
			return "";

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

		return buf.toString();
	}

	public static final String normalizza(String src) {
		return instance._normalizza(src);
	}

}
