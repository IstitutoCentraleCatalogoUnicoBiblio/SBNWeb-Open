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
package it.iccu.sbn.vo.custom.servizi.sale;

public enum StatoPrenotazionePosto2 {

	//stati fittizzi
	IMMESSA			('I'),
	ANNULLATA		('R'),
	NON_FRUITA		('S'),
	IN_CORSO		('A'),
	CONCLUSA		('C'),
	DISDETTA		('D');

	private final char stato;

	private StatoPrenotazionePosto2(char stato) {
		this.stato = stato;
	}

	public char getStato() {
		return stato;
	}

	public static StatoPrenotazionePosto2 of(char stato) {
		for (StatoPrenotazionePosto2 spp : StatoPrenotazionePosto2.values())
			if (stato == spp.stato)
				return spp;

		return null;
	}

}
