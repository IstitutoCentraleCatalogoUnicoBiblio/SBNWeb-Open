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
package it.iccu.sbn.ejb.domain.servizi.esse3.ws;

import it.iccu.sbn.ejb.domain.servizi.esse3.ws.response.Esse3Response;

/**
 * Interfaccia di chiamata del WebService di login su ESSE3
 * @author almaviva3
 * @version 1.0
 * @since 12/07/2018
 */
public interface Esse3 {
	/**
	 * Esegue il login di Esse3
	 *
	 * @param String
	 *            username
	 * @param String
	 *            password
	 * @return Esse3Response
	 */
	Esse3Response login(String username, String password);

	/**
	 * Esegue il logout di Esse3 da sessionID
	 *
	 * @param String
	 *            sid
	 * @return Esse3Response
	 */
	Esse3Response logout(String sid);

	/**
	 * Esegue il login di Esse3
	 *
	 * @param Esse3Bean
	 *            esse3
	 * @return Esse3Response
	 */
	Esse3Response login(Esse3Bean esse3);

	/**
	 * Esegue il logout di Esse3 da sessionID
	 *
	 * @param Esse3Bean
	 *            esse3
	 * @return Esse3Response
	 */
	Esse3Response logout(Esse3Bean esse3);
}
