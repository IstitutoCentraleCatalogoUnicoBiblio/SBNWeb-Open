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
package it.iccu.sbn.SbnMarcFactory.factory;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;

public interface FactorySbn {

	public static final String URL_INDICE = "URL_INDICE";
	public static final String URL_POLO = "URL_POLO";

	public static final String INDICE_USER = "INDICE_USER";
	public static final String INDICE_PWD = "INDICE_PWD";

	public abstract SBNMarc eseguiRichiestaServer() throws SbnMarcException;

	public abstract void setMessage(SbnMessageType sbnmessage, SbnUserType user)
			throws SbnMarcException;

}
