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
package it.iccu.sbn.ejb.vo.periodici.previsionale;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.Map;

public enum PeriodicitaFascicoloType {

	QUOTIDIANO("A", 7),
	BISETTIMANALE("B", 2),
	SETTIMANALE("C"),
	QUINDICINALE("D"),
	BIMENSILE("E"),
	MENSILE("F"),
	BIMESTRALE("G"),
	TRIMESTRALE("H"),
	QUADRIMESTRALE("I"),
	SEMESTRALE("J"),
	ANNUALE("K"),
	BIENNALE("L"),
	TRIENNALE("M"),
	ALTRO("Z"),
	IRREGOLARE("Y"),
	TRE_NUMERI_AL_MESE("O"),
	TRI_SETTIMANALE("N", 3),
	SCONOSCIUTO("U"),
	VARIABILE("P");


	private static Map<String, PeriodicitaFascicoloType> values;
	private final String cd_per;
	private final int numFascicoliSettimana;


	static {
		PeriodicitaFascicoloType[] p = PeriodicitaFascicoloType.class.getEnumConstants();
		values = new THashMap<String, PeriodicitaFascicoloType>();
		for (int i = 0; i < p.length; i++)
			values.put(p[i].cd_per.trim(), p[i]);
	}

	public static final PeriodicitaFascicoloType fromString(String value) {
		if (ValidazioneDati.strIsNull(value) )
			return null;
		return PeriodicitaFascicoloType.values.get(value.trim() );
	}

	public final boolean isLowerSettimanale() {
		return ValidazioneDati.in(this,
				//PeriodicitaFascicoloType.QUOTIDIANO,
				PeriodicitaFascicoloType.BISETTIMANALE,
				PeriodicitaFascicoloType.TRI_SETTIMANALE);
	}


	private PeriodicitaFascicoloType(String cd_per) {
		this.cd_per = cd_per;
		this.numFascicoliSettimana = 0;
	}

	private PeriodicitaFascicoloType(String cd_per, int numFascicoliSettimana) {
		this.cd_per = cd_per;
		this.numFascicoliSettimana = numFascicoliSettimana;
	}

	public String getCd_per() {
		return cd_per;
	}

	public int getNumFascicoliSettimana() {
		return numFascicoliSettimana;
	}
}
