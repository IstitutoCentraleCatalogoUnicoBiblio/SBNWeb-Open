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
package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.ClassiUtil;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class SimboloDeweyVO extends SerializableVO {

	private static final long serialVersionUID = -2430293265236129929L;

	private String sistema;
	private String edizione;
	private String simbolo;
	private boolean dewey;

	public static final SimboloDeweyVO parse(String id) throws ValidationException {
		try {
			return new SimboloDeweyVO(id);
		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.GS_IDENTIFICATIVO_CLASSE_ERRATO, id);
		}
	}

	private SimboloDeweyVO(String identificativoClasse) {
		dewey = ClassiUtil.isT001Dewey(identificativoClasse);
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

	public SimboloDeweyVO() {
		sistema = "";
		edizione = "";
		simbolo = "";
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

	public boolean isDewey() {
		return dewey;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(32);
		buf.append(sistema).append(edizione).append(simbolo);
		return buf.toString().trim();
	}

}
