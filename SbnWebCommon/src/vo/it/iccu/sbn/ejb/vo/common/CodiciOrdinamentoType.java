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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.HashMap;
import java.util.Map;

public enum CodiciOrdinamentoType {

	ORDINAMENTO_PER_CODICE_ASC			("CA"),
	ORDINAMENTO_PER_CODICE_DESC			("CD"),
	ORDINAMENTO_PER_DESCRIZIONE_ASC		("DA"),
	ORDINAMENTO_PER_DESCRIZIONE_DESC	("DD");

	private static final Map<String, CodiciOrdinamentoType> values;

	static {
		CodiciOrdinamentoType[] codici = CodiciOrdinamentoType.class.getEnumConstants();
		values = new HashMap<String, CodiciOrdinamentoType>();
		for (int i = 0; i < codici.length; i++)
			values.put(codici[i].shortCode.toUpperCase(), codici[i]);
	}

	private final String shortCode;

	private CodiciOrdinamentoType(String shortCode) {
		this.shortCode = shortCode;
	}

	public static final CodiciOrdinamentoType fromString(String value) {
		if (ValidazioneDati.strIsNull(value) )
			return null;
		return CodiciOrdinamentoType.values.get(value.toUpperCase());
	}
}
