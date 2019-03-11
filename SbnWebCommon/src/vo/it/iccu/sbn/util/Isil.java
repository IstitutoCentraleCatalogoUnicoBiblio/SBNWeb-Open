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
package it.iccu.sbn.util;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class Isil extends SerializableVO {

	private static final long serialVersionUID = -9072198465037858600L;

	public String paese;
	public String suffisso;

	public static final Isil parse(String id) {
		return new Isil(id);
	}

	public String getPaese() {
		return paese;
	}

	public String getSuffisso() {
		return suffisso;
	}

	private Isil(String id) {
		if (!ValidazioneDati.isFilled(id))
			return;

		id = id.trim().toUpperCase();

		// almaviva5_20150415 servizi ill: gestione isil con codice paese
		String[] tokens = id.split("\\u002D");	//trattino

		int size = size(tokens);

		if (size == 1) {
			suffisso = id;
		}

		if (size == 2) {
			// codice isil completo: <cod_paese>-<cod.anagrafe>
			paese = tokens[0];
			if (length(paese) != 2) {
				paese = null;
				suffisso = id;
			} else
				suffisso = tokens[1];
		}

		if (size > 2) {
			// codice isil completo: <cod_paese>-<cod.anagrafe>
			paese = tokens[0];
			if (length(paese) != 2) {
				paese = null;
				suffisso = id;
			} else
				suffisso = id.substring(tokens[0].length() + 1);
		}
	}

	public boolean withPaese() {
		return isFilled(paese);
	}

}
