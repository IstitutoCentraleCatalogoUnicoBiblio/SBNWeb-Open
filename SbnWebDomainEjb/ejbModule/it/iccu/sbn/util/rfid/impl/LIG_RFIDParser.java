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
package it.iccu.sbn.util.rfid.impl;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.extension.rfid.InventarioRFIDParsingStrategy;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;

public class LIG_RFIDParser implements InventarioRFIDParsingStrategy {

	public InventarioVO parse(final String rfid) throws Exception {
		if (!ValidazioneDati.isFilled(rfid))
			return null;

		int len = ValidazioneDati.length(rfid);
		if (len < 4)	// minimo 4 car. (3 serie + num. inv)
			return null;

		InventarioVO inv = new InventarioVO();

		inv.setCodSerie(rfid.substring(0, 3));
		String codInv = ValidazioneDati.trimOrEmpty(rfid.substring(3, len));
		if (!ValidazioneDati.strIsNumeric(codInv))
			return null;

		//almaviva5_20130909 segnalazione NAP
		Integer num = Integer.valueOf(codInv);
		if (num < 1 || num > DocumentoFisicoCostant.MAX_PROGRESSIVO_INVENTARIO)
			return null;

		inv.setCodInvent(num);

		return inv;
	}

}
