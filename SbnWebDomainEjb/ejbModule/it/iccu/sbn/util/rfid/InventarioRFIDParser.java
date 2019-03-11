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
package it.iccu.sbn.util.rfid;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.extension.rfid.InventarioRFIDParsingStrategy;
import it.iccu.sbn.extension.rfid.KeyInventario;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;

import org.apache.log4j.Logger;



public final class InventarioRFIDParser {

	private static Logger log = Logger.getLogger(InventarioRFIDParser.class);

	private static final List<InventarioRFIDParsingStrategy> parsers;

	static {
		parsers = new ArrayList<InventarioRFIDParsingStrategy>();
		Iterator<InventarioRFIDParsingStrategy> i = ServiceRegistry.lookupProviders(InventarioRFIDParsingStrategy.class);
		while (i.hasNext()) {
			InventarioRFIDParsingStrategy strategy = i.next();
			log.debug("InventarioRFIDParsingStrategy impl: " + strategy);
			parsers.add(strategy);
		}
	}

	public static final InventarioVO parse(final String rfid) throws Exception {

		if (!ValidazioneDati.isFilled(rfid))
			return null;

		KeyInventario kinv = null;

		for (InventarioRFIDParsingStrategy p : parsers) {
			log.debug("parsing strategy: " + p);
			kinv = p.parse(rfid);
			if (kinv != null)
				return new InventarioVO(kinv);
		}

		//errore
		throw new ValidationException(SbnErrorTypes.GDF_RFID_INVENTARIO_ERRATO, rfid);

	}

	public static void main(String[] args) throws Exception {
		String rfid = "BNVA1000000012";//"ICacq000012666";//"   0000030999";
		System.out.println(InventarioRFIDParser.parse(rfid));
	}

}
