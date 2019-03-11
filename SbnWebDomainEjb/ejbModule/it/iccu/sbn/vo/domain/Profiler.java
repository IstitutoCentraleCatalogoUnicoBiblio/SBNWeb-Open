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
package it.iccu.sbn.vo.domain;

import it.iccu.sbn.exception.AttivitaNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class Profiler {

	private Map<String, Attivita> attivita;

	public Profiler() {
		this.attivita = new HashMap<String, Attivita>();
	}

	public void addAttivita(Attivita attivita) {
		this.attivita.put(attivita.getCd_attivita(), attivita);
	}

	public void removeAttivita(String cd_attivita)
			throws AttivitaNotFoundException {
		if (!this.attivita.containsKey(cd_attivita)) {
			throw new AttivitaNotFoundException();
		}

		this.attivita.remove(cd_attivita);

	}

	public Attivita getAttivita(String cd_attivita)
			throws AttivitaNotFoundException {
		if (!this.attivita.containsKey(cd_attivita)) {
			throw new AttivitaNotFoundException();
		}
		Attivita attivita = this.attivita.get(cd_attivita);
		return attivita;
	}
}
