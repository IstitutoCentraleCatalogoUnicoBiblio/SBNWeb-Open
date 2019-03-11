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
package it.iccu.sbn.vo.custom.amministrazione;

import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;

public class AttivitaAffiliata extends UniqueIdentifiableVO {

	private static final long serialVersionUID = 7781798591196144982L;

	private final String cd_attivita;
	private final String cd_bib_centro_sistema;
	private final String cd_bib_affiliata;

	public AttivitaAffiliata(String cd_attivita, String cd_bib_centro_sistema,	String cd_bib_affiliata) {
		super();
		this.cd_attivita = cd_attivita;
		this.cd_bib_centro_sistema = cd_bib_centro_sistema;
		this.cd_bib_affiliata = cd_bib_affiliata;
	}

	public String getCd_attivita() {
		return cd_attivita;
	}

	public String getCd_bib_centro_sistema() {
		return cd_bib_centro_sistema;
	}

	public String getCd_bib_affiliata() {
		return cd_bib_affiliata;
	}

	@Override
	public int getRepeatableId() {
		return (cd_attivita + cd_bib_centro_sistema + cd_bib_affiliata).hashCode();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cd_attivita == null) ? 0 : cd_attivita.hashCode());
		result = prime
				* result
				+ ((cd_bib_affiliata == null) ? 0 : cd_bib_affiliata.hashCode());
		result = prime
				* result
				+ ((cd_bib_centro_sistema == null) ? 0 : cd_bib_centro_sistema
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttivitaAffiliata other = (AttivitaAffiliata) obj;

		return (getRepeatableId() == other.getRepeatableId());
	}


}
