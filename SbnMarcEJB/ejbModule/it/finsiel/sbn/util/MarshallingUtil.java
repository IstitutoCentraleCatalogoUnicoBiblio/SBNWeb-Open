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
package it.finsiel.sbn.util;

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;

import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

public class MarshallingUtil {

	private static Logger log = Logger.getLogger("sbnmarcPolo");

	public static final String marshal(SBNMarc root) throws EccezioneSbnDiagnostico {
		StringWriter sw = new StringWriter();
		try {
			Marshaller.marshal(root, sw, SbnMarcMarshalListener.getInstance());

		} catch (MarshalException e) {
			log.error("Errore marshalling", e);
			throw new EccezioneSbnDiagnostico(101);
		} catch (ValidationException e) {
			log.error("Errore marshalling", e);
			// e.printStackTrace();
			throw new EccezioneSbnDiagnostico(101);
		}
		return sw.toString();
	}

}
