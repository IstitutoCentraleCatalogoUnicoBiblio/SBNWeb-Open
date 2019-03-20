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
package it.iccu.sbn.ejb.domain.servizi.esse3.test;

import it.iccu.sbn.ejb.domain.servizi.esse3.ws.Esse3ServicesImpl;
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.response.Esse3Response;
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.response.Esse3ResponseType;

/**
 * Classe Test del WebService su ESSE3<br>
 * @author almaviva3
 * @version 1.0
 * @since 12/07/2018
 */
public class Esse3Test {
	private static Esse3ServicesImpl esse3api = new Esse3ServicesImpl(null);


	private static void logout(String sid, Esse3Response loginResponse) {
		//logout
		if(loginResponse.getResponseKey() == Esse3ResponseType.OK) {
			Esse3Response logoutResponse = esse3api.logout(sid);
			System.out.println("Logout done. response: " + logoutResponse.getResponseKey());
		}
	}

	private static Esse3Response login(String username, String password) {
		//login
		Esse3Response loginResponse = esse3api.login(username, password);
		System.out.println("Login response:"+loginResponse.getResponseKey()+" sid: " + loginResponse.getSessionId());
		return loginResponse;
	}

	public static void main(String[] args) {

		System.out.println("Test Connecting to Esse3...");


		Esse3Response loginResponse = login("c.testi", "a1810475");
		//Esse3Response loginResponse2 = login("c.testi", "a1810475");
		logout(loginResponse.getSessionId(), loginResponse);



		System.out.println("Done");

	}

}
