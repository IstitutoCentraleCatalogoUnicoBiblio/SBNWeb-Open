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
package it.iccu.sbn.vo.custom.servizi;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;

public class CodAttivitaIterRichiesta extends TB_CODICI {

	private static final long serialVersionUID = -2221429464512937492L;

	public static final CodAttivitaIterRichiesta get(TB_CODICI cod) {
		if (cod == null)
			return null;

		return new CodAttivitaIterRichiesta(cod);
	}

	private CodAttivitaIterRichiesta(TB_CODICI cod) {
		super(cod);
	}

	public boolean isILL() {
		return !ValidazioneDati.equals(getCd_flg5(), "I") || trimOrEmpty(getCd_tabella()).startsWith("F");
	}

	public boolean isLocale() {
		return !isILL();
	}

}
