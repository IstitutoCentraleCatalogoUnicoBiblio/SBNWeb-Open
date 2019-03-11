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

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;


public class SimboloDewey {

	private final boolean dewey;

	private String sistema;
	private String edizione;
	private String simbolo;

	public static final SimboloDewey parse(String id) throws EccezioneSbnDiagnostico {
		try {
			return new SimboloDewey(id);
		} catch (Exception e) {
			throw new EccezioneSbnDiagnostico(3077);	//identificativo della classificazione errato
		}
	}

	private SimboloDewey(String identificativoClasse) {
		dewey = ValidazioneDati.isT001Dewey(identificativoClasse);
		if (dewey) {
			sistema = identificativoClasse.substring(0, 1);
			edizione = identificativoClasse.substring(1, 3);
			simbolo = identificativoClasse.substring(3);
			//almaviva5_20141114 test edizione ridotta
			char ridotta = simbolo.charAt(0);
			if (ValidazioneDati.in(ridotta, 'r', 'R')) {
				edizione += ridotta;
				simbolo = simbolo.substring(1);
			}
		} else {
			sistema = identificativoClasse.substring(0, 3);
			edizione = "  ";
			simbolo = identificativoClasse.substring(5);
		}
	}

	public boolean isDewey() {
		return dewey;
	}

	public String getSistema() {
		return sistema;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public String getEdizione() {
		return edizione;
	}

}
