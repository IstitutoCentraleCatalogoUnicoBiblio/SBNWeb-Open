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
package it.iccu.sbn.web.actions.gestionesemantica.utility;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.ClassiUtil;


public final class ScorrimentoLabel {

	private static boolean check(String radice) {
		return (ValidazioneDati.isFilled(radice) &&	ClassiUtil.isDewey(radice));
	}

	public static final String radiceLabel(String radice) {
		if (!check(radice))
			return null;
		return radice;

	}

	public static final String altoLabel(String radice) {
		if (!check(radice))
			return null;
		// Sinistra
		char ultimo2 = radice.charAt(radice.length() - 1);
		if (ultimo2 > '0')
			return radice.substring(0, radice.length() - 1)
					+ (char) (ultimo2 - 1);
		else
			return radice;
	}

	public static final String bassoLabel(String radice) {
		if (!check(radice))
			return null;
		// Destra
		char ultimo = radice.charAt(radice.length() - 1);
		if (ultimo < '9')
			return radice.substring(0, radice.length() - 1)
					+ (char) (ultimo + 1);
		else
			return radice;
	}

	public static final String sinistraLabel(String radice) {
		if (!check(radice))
			return null;
		if (radice.length() == 3)
			return radice;
		else {
			radice = radice.substring(0, radice.length() - 1);
			if (radice.endsWith(".")) // tolgo il punto se ultimo carattere
				radice = radice.substring(0, radice.length() - 1);
		}

		return radice;
	}

	public static final String destraLabel(String radice) {
		if (!check(radice))
			return null;
		if (radice.length() == 3)
			return radice + ".0";
		else
			return radice + "0";
	}
}
