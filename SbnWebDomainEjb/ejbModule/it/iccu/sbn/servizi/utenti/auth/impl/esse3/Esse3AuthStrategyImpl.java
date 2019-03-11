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
package it.iccu.sbn.servizi.utenti.auth.impl.esse3;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.Esse3ServicesImpl;
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.response.Esse3Response;
import it.iccu.sbn.ejb.domain.servizi.esse3.ws.response.Esse3ResponseType;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.extension.auth.utente.LoginRequest;
import it.iccu.sbn.extension.auth.utente.LoginResponse;
import it.iccu.sbn.extension.auth.utente.LoginResponse.LoginResult;
import it.iccu.sbn.extension.auth.utente.LogoutRequest;
import it.iccu.sbn.extension.auth.utente.UtenteAuthException;
import it.iccu.sbn.extension.auth.utente.UtenteAuthenticationStrategy;
import it.iccu.sbn.servizi.utenti.auth.impl.LoginResponseImpl;
import it.iccu.sbn.servizi.utenti.auth.impl.PlainPasswordAuthData;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import org.apache.log4j.Logger;
/**
 * Classe UtenteAuthenticationStrategy chiamata WebService su ESSE3<br>
 * Manca da fare il logout e TODO: gestione UtenteBaseVO
 * @author Luca Ferraro Visardi
 * @version 1.0
 * @since 13/07/2018
 */
public class Esse3AuthStrategyImpl implements UtenteAuthenticationStrategy {
	//per utilizzo vedi Esse3Test
	private Esse3ServicesImpl esse3 = null;

	static Logger log = Logger.getLogger(UtenteAuthenticationStrategy.class);
	public Esse3AuthStrategyImpl() {
		//LFV 10/10/2018
		//Inserire ESSE3_WS_URL=https://servizi.lumsa.it/services/ESSE3WS in sbnweb.conf
		try {
			String endpointEsse3Conf = CommonConfiguration.getProperty("ESSE3_WS_URL");
				esse3 = new Esse3ServicesImpl(endpointEsse3Conf);
		} catch (Exception e) {
			log.error(e);
			esse3 = new Esse3ServicesImpl();
		}
	}

	private LoginResponseImpl buildResponse(Esse3Response response, String cod_polo, String ticket, String codUtente) {
		UtenteBaseVO utente = new UtenteBaseVO();
		try {
			utente = DomainEJBFactory.getInstance().getServizi().getUtente(ticket, codUtente);
		} catch (Exception e) {
			log.error(e);
			response.setResponseCode(Esse3ResponseType.ERRORE_RECUPERO_DATI);
		}

		switch (response.getResponseKey()) {
		case OK:
			return new LoginResponseImpl(LoginResult.LOGGED, utente, new Esse3SessionInfo(response.getSessionId()));

		default:
			return new LoginResponseImpl(LoginResult.NOT_FOUND, null, null);
		}
	}

	public LoginResponse login(String ticket, LoginRequest loginRequest) throws UtenteAuthException {
		Esse3Response login = new Esse3Response(1500);
		String cdPolo = "";
		try {
			log.info("Tentativo di login ESSE3");
			cdPolo = DomainEJBFactory.getInstance().getPolo().getInfoPolo().getCd_polo();
			PlainPasswordAuthData authenticationData = (PlainPasswordAuthData) loginRequest.getAuthenticationData();
			login = esse3.login(loginRequest.getUserId(), authenticationData.getPassword());
		} catch (Exception e) {
			throw new UtenteAuthException(new ApplicationException(SbnErrorTypes.DB_FALUIRE));
		}

		log.info("Login ESSE3: " + login.getResponseKey());
		return buildResponse(login, cdPolo, ticket, loginRequest.getUserId());

	}

	public void logout(String ticket, LogoutRequest logoutRequest) throws UtenteAuthException {
		Esse3SessionInfo userSessionInfo = (Esse3SessionInfo) logoutRequest.getUserSessionInfo();

		//TODO: Sid?
		esse3.logout(userSessionInfo.getSessionId());
	}

	public int priority() {
		return 3;
	}

}
