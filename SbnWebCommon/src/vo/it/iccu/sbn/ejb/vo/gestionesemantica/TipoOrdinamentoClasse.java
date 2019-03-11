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
package it.iccu.sbn.ejb.vo.gestionesemantica;

public enum TipoOrdinamentoClasse {

	PER_ID					("1", "Identificativo"),
	PER_TESTO				("2", "Equivalente verbale"),
	PER_DATA				("3", "Data var./ins. + Identificativo");


	public static final TipoOrdinamentoClasse fromSbnMarcValue(String value) {
		for (TipoOrdinamentoClasse ord : TipoOrdinamentoClasse.values() )
			if (ord.sbnMarcValue.equals(value))
				return ord;
		return null;
	}

	private final String descrizione;
	private final String sbnMarcValue;

	private TipoOrdinamentoClasse(String sbnMarcValue, String descrizione) {
		this.sbnMarcValue = sbnMarcValue;
		this.descrizione = descrizione;
	}

	public String getDescrizione() {
		return descrizione;
	}
}
