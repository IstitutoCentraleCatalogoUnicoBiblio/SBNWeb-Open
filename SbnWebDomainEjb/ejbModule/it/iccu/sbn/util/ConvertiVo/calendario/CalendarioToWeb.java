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
package it.iccu.sbn.util.ConvertiVo.calendario;

import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.polo.orm.servizi.Tbl_modello_calendario;

public class CalendarioToWeb extends CalendarioDataBinder {

	private static final long serialVersionUID = 4696454536419243305L;

	public final ModelloCalendarioVO modelloCalendario(Tbl_modello_calendario hVO) {
		if (hVO == null)
			return null;

		String json = hVO.getJson_modello();
		ModelloCalendarioVO webVO = builder.create().fromJson(json, ModelloCalendarioVO.class);
		webVO.setId(hVO.getId_modello());

		fillBaseWeb(hVO, webVO);

		return webVO;
	}

}
