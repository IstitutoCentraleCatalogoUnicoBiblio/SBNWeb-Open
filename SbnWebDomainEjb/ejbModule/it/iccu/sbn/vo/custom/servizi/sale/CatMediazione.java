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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;

public class CatMediazione extends TB_CODICI {

	private static final long serialVersionUID = 5203681504194231194L;

	public static final CatMediazione of(TB_CODICI cod) {
		if (cod == null)
			return null;

		return new CatMediazione(cod);
	}

	private CatMediazione(TB_CODICI cod) {
		super(cod);
	}

	public boolean isRichiedeSupporto() {
		return ValidazioneDati.equals(getCd_flg2(), "S");
	}

}
