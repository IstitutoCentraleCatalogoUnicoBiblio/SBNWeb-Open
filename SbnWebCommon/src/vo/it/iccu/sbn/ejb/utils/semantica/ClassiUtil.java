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
package it.iccu.sbn.ejb.utils.semantica;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.Constants;

public class ClassiUtil {

	private static final String DEWEY_SYMBOL_REGEX = "^\\d{3}$|^\\d{3}\\.\\d+$|^\\d{3}\\.\\d+[a-zA-Z]{1}$";
	private static final String DEWEY_SYMBOL_COLL_REGEX = "^\\d{3}$|^\\d{3}\\.\\d+$|^\\d{3}\\..+$";

	public static final int MAX_LEN_CLASSE_INDICE = 160;


	public static final boolean isDewey(String id) {
		if (ValidazioneDati.strIsNull(id))
			return false;
		return id.matches(DEWEY_SYMBOL_REGEX);
	}

	public static final boolean isT001Dewey(String id) {
		if (ValidazioneDati.strIsNull(id))
			return false;
		if (id.length() < 5)
			return false;

		return
			id.substring(0, 1).equalsIgnoreCase(Constants.Semantica.Classi.SISTEMA_CLASSE_DEWEY) &&
			ValidazioneDati.strIsNumeric(id.substring(1, 3)) &&
			//almaviva5_20141114 test edizione ridotta
			(ValidazioneDati.in(id.charAt(3), 'r', 'R') || !id.substring(3, 5).equals("  ") );
	}

	public static final boolean isDeweyColl(String id) {
		if (ValidazioneDati.strIsNull(id))
			return false;
		return id.matches(DEWEY_SYMBOL_COLL_REGEX);
	}

	public static boolean checkClassePerIndice(String classe) {
		// controllo lunghezza per invio in indice
		if (ValidazioneDati.trimOrEmpty(classe).length() > MAX_LEN_CLASSE_INDICE) {
			return false;
		}

		return true;
	}

}
