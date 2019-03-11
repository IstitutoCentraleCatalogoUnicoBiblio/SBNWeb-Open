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
package it.iccu.sbn.servizi.utenti.auth;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.extension.auth.utente.AuthenticationData;
import it.iccu.sbn.extension.auth.utente.LoginRequest;
import it.iccu.sbn.extension.auth.utente.LoginRequest.LoginRequestType;
import it.iccu.sbn.extension.auth.utente.LoginResponse;
import it.iccu.sbn.extension.auth.utente.LoginResponse.LoginResult;
import it.iccu.sbn.extension.auth.utente.LogoutRequest;
import it.iccu.sbn.extension.auth.utente.UserSessionInfo;
import it.iccu.sbn.extension.auth.utente.UtenteAuthException;
import it.iccu.sbn.extension.auth.utente.UtenteAuthenticationStrategy;
import it.iccu.sbn.servizi.utenti.auth.impl.LoginResponseImpl;
import it.iccu.sbn.servizi.utenti.auth.impl.PlainPasswordAuthData;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.spi.ServiceRegistry;

import org.apache.log4j.Logger;

public class UtenteLoginService {

	static Logger log = Logger.getLogger(UtenteLoginService.class);

	static Map<String, UserSessionInfo> loggedUsers = new ConcurrentHashMap<String, UserSessionInfo>();

	static List<UtenteAuthenticationStrategy> authStrategies;

	static {
		authStrategies = new ArrayList<UtenteAuthenticationStrategy>();
		Iterator<UtenteAuthenticationStrategy> i = ServiceRegistry.lookupProviders(UtenteAuthenticationStrategy.class);
		while (i.hasNext()) {
			authStrategies.add(i.next());
		}

		Collections.sort(authStrategies, new Comparator<UtenteAuthenticationStrategy>() {
			public int compare(UtenteAuthenticationStrategy uas1, UtenteAuthenticationStrategy uas2) {
				return uas1.priority() - uas2.priority();
			}
		});
	}

	private static void addUser(LoginResponse loginResponse) {
		loggedUsers.put(loginResponse.getUtente().getCodUtente(), loginResponse.getUserSessionInfo());
	}

	private static void removeUser(String userId) {
		loggedUsers.remove(userId);
	}

	public static LoginResponse login(String ticket, final String userId, final String password,
			final LoginRequestType loginRequestType) throws SbnBaseException {

		if (!ValidazioneDati.isFilled(userId))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "userId");

		if (loggedUsers.containsKey(userId) ) {
			//utente già loggato
		}

		LoginRequest loginRequest = new LoginRequest() {
			public String getUserId() { return userId.trim(); }
			public LoginRequestType getRequestType() { return loginRequestType;	}
			public AuthenticationData getAuthenticationData() { return new PlainPasswordAuthData(password); }
		};

		for (UtenteAuthenticationStrategy strategy : authStrategies)
		try {
			LoginResponse response = strategy.login(ticket, loginRequest);
			switch (response.getStatus()) {
			case LOGGED:
				if (loginRequestType == LoginRequestType.LOGIN) {
					addUser(response);
				}
				return response;

			case NOT_FOUND:
			case UNSUPPORTED:
				//tentativo con prossimo metodo di auth
				log.warn(String.format("login(): %s: utente '%s' non autenticato (%s).",
						strategy.getClass().getSimpleName(), userId, response.getStatus()));
				continue;

			case AUTH_DATA_EXPIRED:
			case DISABLED:
				return response;
			}

		} catch (UtenteAuthException e) {
			Throwable cause = e.getCause();
			throw cause instanceof SbnBaseException ? (SbnBaseException)cause : new ApplicationException(e);
		}

		return new LoginResponseImpl(LoginResult.NOT_FOUND, null, null);
	}

	public static void logout(String ticket, final String userId) throws SbnBaseException {

		if (!ValidazioneDati.isFilled(userId))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "userId");

		final UserSessionInfo userSessionInfo = loggedUsers.get(userId);

		LogoutRequest logoutRequest = new LogoutRequest() {
			public String getUserId() { return userId.trim(); }
			public UserSessionInfo getUserSessionInfo() { return userSessionInfo; }
		};

		for (UtenteAuthenticationStrategy loginStrategy : authStrategies)
		try {
			loginStrategy.logout(ticket, logoutRequest);

		} catch (UtenteAuthException e) {
			Throwable cause = e.getCause();
			throw cause instanceof SbnBaseException ? (SbnBaseException)cause : new ApplicationException(e);
		}

		removeUser(userId);
	}

}
