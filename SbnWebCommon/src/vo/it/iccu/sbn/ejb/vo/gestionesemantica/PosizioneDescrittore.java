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


public enum PosizioneDescrittore {

	QUALUNQUE_POSIZIONE		("0", "In qualunque posizione"),
	PRIMA_POSIZIONE			("1", "In prima posizione"),
	NON_PRIMA_POSIZIONE		("2", "Non in prima posizione");

	public static final PosizioneDescrittore fromSbnMarcValue(String value) {
		for (PosizioneDescrittore pos : PosizioneDescrittore.values() )
			if (pos.sbnMarcValue.equals(value))
				return pos;
		return null;
	}

	private final String descrizione;
	private final String sbnMarcValue;

	private PosizioneDescrittore(String sbnMarcValue, String descrizione) {
		this.sbnMarcValue = sbnMarcValue;
		this.descrizione = descrizione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getSBNMarcValue() {
		return sbnMarcValue;
	}

}
