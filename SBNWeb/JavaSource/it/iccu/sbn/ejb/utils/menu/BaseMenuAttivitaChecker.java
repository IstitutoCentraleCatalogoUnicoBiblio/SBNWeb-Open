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
package it.iccu.sbn.ejb.utils.menu;

import it.iccu.sbn.ejb.model.attivita.MenuAttivitaChecker;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UserWrapper;

public class BaseMenuAttivitaChecker implements MenuAttivitaChecker {

	protected final String[] codAttivita;

	public BaseMenuAttivitaChecker(String... codAttivita) {
		this.codAttivita = codAttivita;
	}

	public boolean check(UserWrapper uw) {
		if (!ValidazioneDati.isFilled(codAttivita))
			//nessun cod. attivita associato
			return true;

		for (String attivita : codAttivita)
			try {
				uw.getUtenteEjb().checkAttivita(attivita);
				return true;
			} catch (Exception e) {
				continue;
			}

		return false;
	}

}
