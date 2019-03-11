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
package it.iccu.sbn.ejb.vo.servizi.erogazione.ill;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

public enum RuoloBiblioteca {
	RICHIEDENTE,
	FORNITRICE;

	public char getFl_ruolo() {
		return name().charAt(0);
	}

	public static RuoloBiblioteca fromString(String value) {
		if (!ValidazioneDati.isFilled(value))
			return null;

		char c = value.charAt(0);
		for (RuoloBiblioteca ruolo : values())
			if (ruolo.getFl_ruolo() == c)
				return ruolo;

		return null;
	}
}
