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
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.response.Esse3ResponseType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.lumsa.servizi.services.ESSE3WS.Esse3WsProxy;

import java.rmi.RemoteException;

import javax.xml.rpc.holders.StringHolder;

import org.apache.log4j.Logger;

/**
 * Chiamata del WebService su ESSE3 (Utilizza apache Axis 1.x)<br>
 * Classe di connessione al WS di ESSE3 x bib LUMSA (IEILU)
 * @author Luca Ferraro Visardi
 * @version 1.0
 * @since 12/07/2018
 */
public class Esse3ServicesImpl implements Esse3 {
	private Logger log = Logger.getLogger(Esse3ServicesImpl.class);
	private Esse3WsProxy esse3WSServices = null;
	private final Esse3Response error_dati_non_validi = new Esse3Response(1501);

	private boolean isValid(String data) {
		return ValidazioneDati.isFilled(data);
	}
	public Esse3ServicesImpl(String url_lumsa) {
		if(url_lumsa == null || "".equals(url_lumsa))
			esse3WSServices = new Esse3WsProxy();
		else
			esse3WSServices = new Esse3WsProxy(url_lumsa);
	}
	public Esse3ServicesImpl() {
		esse3WSServices = new Esse3WsProxy();
	}

	/**
	 * login di Esse3
	 *
	 * @param String
	 *            username
	 * @param String
	 *            password
	 * @return Esse3Response
	 */
	public Esse3Response login(String username, String password) {

		Esse3Response response = new Esse3Response(1000);

		if (!isValid(username) || !isValid(password))
			return error_dati_non_validi;

		try {
			// Sugli xml di esempio imposta il sid a "?",
			// a runtime con le credenziali esate funziona anche vuoto
			// nel dubbio lo imposto così
			StringHolder sid = new StringHolder("?");

			int response_code = esse3WSServices.fn_dologin(username, password, sid);
			response = new Esse3Response(response_code, sid.value);

		} catch (RemoteException e) {
			log.error("Errore login Esse3", e);
			response = new Esse3Response(Esse3ResponseType.ERRORE_LDAP, "");
		}
		log.info("login esse3 response " + response.getResponseKey());
		return response;
	}

	/**
	 * logout di Esse3 da sessionID
	 *
	 * @param String
	 *            sid
	 * @return Esse3Response
	 */
	public Esse3Response logout(String sid) {

		Esse3Response response = new Esse3Response(1500);
		if (!isValid(sid))
			return new Esse3Response(1501);

		try {
			int response_code = esse3WSServices.fn_dologout(sid);
			response = new Esse3Response(response_code, "");

		} catch (RemoteException e) {
			log.error("Errore login Esse3", e);
			response = new Esse3Response(Esse3ResponseType.ERRORE_LDAP, "");

		}
		log.info("Logout esse3 response  " + response.getResponseKey());

		return response;

	}

	/**
	 * Esegue il login di Esse3
	 *
	 * @param Esse3Bean
	 *            esse3
	 * @return Esse3Response
	 */
	public Esse3Response login(Esse3Bean esse3) {
		return (esse3 != null) ? login(esse3.getUsername(), esse3.getPassword()) : error_dati_non_validi;
	}

	/**
	 * Esegue il logout di Esse3 da sessionID
	 *
	 * @param Esse3Bean
	 *            esse3
	 * @return Esse3Response
	 */
	public Esse3Response logout(Esse3Bean esse3) {

		return (esse3 != null) ? logout(esse3.getSid()) : error_dati_non_validi;
	}

}
