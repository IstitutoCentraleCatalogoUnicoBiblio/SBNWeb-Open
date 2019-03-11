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
package it.iccu.sbn.ejb.vo.periodici.fascicolo;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

public enum StatoFascicolo {

	RILEGATURA		('B'),
	RECLAMO			('C'),
	LACUNA			('G'),
	SMARRITO		('L'),
	POSSEDUTO		('O'),
	RICEVUTO		('R'),
	DA_ASSOCIARE	('T'),
	SOLLECITO		('S'),
	ATTESO			('W');

	public static final boolean isPosseduto(StatoFascicolo stato) {
		return ValidazioneDati.in(stato,
			RICEVUTO,
			RILEGATURA,
			POSSEDUTO,
			SMARRITO);
	}

	private final char stato;

	private StatoFascicolo(char stato) {
		this.stato = stato;
	}

	public char getStato() {
		return stato;
	}

}
