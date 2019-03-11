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

public class TO0_RFIDParser implements InventarioRFIDParsingStrategy {

	public InventarioVO parse(final String rfid) throws Exception {
		String kinv = rfid.toUpperCase();
		int len = ValidazioneDati.length(kinv);

		//codice serie ( 3 caratteri ) + numero inventario ( 10 caratteri
		//		allineati a Sx con riempimento di Spazi a Dx + Facoltativo : biblioteca ( 6
		//		caratteri - TO0 52 ) 		     es   : PER125       TO0 52
		//					serie PER numero inventario 125 della biblioteca TO0 52 ,

		if (!ValidazioneDati.in(len, 13, 14, 15, 18, 19))
			return null;

		InventarioVO inv = new InventarioVO();
		switch (len) {
		case 14:	//key con biblioteca in testa (senza spazi)
			inv.setCodBib(ValidazioneDati.fillLeft(kinv.substring(0, 2), ' ', 3));
			inv.setCodSerie(kinv.substring(2,5));
			String codInv = kinv.substring(5);
			if (!ValidazioneDati.strIsNumeric(codInv))
				return null;
			inv.setCodInvent(Integer.valueOf(codInv));
			break;

		case 15:	//key con biblioteca in testa (con spazio)
			inv.setCodBib(ValidazioneDati.fillLeft(kinv.substring(0, 3), ' ', 3));
			inv.setCodSerie(kinv.substring(3,6));
			codInv = kinv.substring(6);
			if (!ValidazioneDati.strIsNumeric(codInv))
				return null;
			inv.setCodInvent(Integer.valueOf(codInv));
			break;

		case 18:	//key con polo in testa
			inv.setCodPolo(kinv.substring(0, 3));
			inv.setCodBib(kinv.substring(3, 6));
			inv.setCodSerie(kinv.substring(6, 9));
			codInv = kinv.substring(9);
			if (!ValidazioneDati.strIsNumeric(codInv))
				return null;
			inv.setCodInvent(Integer.valueOf(codInv));
			break;

		case 13:
			inv.setCodSerie(kinv.substring(0, 3));
			codInv = ValidazioneDati.trimOrEmpty(kinv.substring(3, len));
			if (!ValidazioneDati.strIsNumeric(codInv))
				return null;
			inv.setCodInvent(Integer.valueOf(codInv));
			break;

		case 19:
			inv.setCodSerie(kinv.substring(0, 3));
			codInv = ValidazioneDati.trimOrEmpty(kinv.substring(3, 13));
			if (!ValidazioneDati.strIsNumeric(codInv))
				return null;
			inv.setCodInvent(Integer.valueOf(codInv));
			inv.setCodPolo(kinv.substring(13, 16));
			inv.setCodBib(kinv.substring(16));
			break;
		}

		//almaviva5_20130909 segnalazione NAP
		int num = inv.getCodInvent();
		if (num < 1 || num > DocumentoFisicoCostant.MAX_PROGRESSIVO_INVENTARIO)
			return null;

		return inv;
	}

}
