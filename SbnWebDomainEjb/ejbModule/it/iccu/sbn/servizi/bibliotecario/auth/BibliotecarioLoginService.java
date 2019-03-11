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
package it.iccu.sbn.servizi.bibliotecario.auth;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.extension.auth.bibliotecario.BibLoginResponse;
import it.iccu.sbn.extension.auth.bibliotecario.BibLoginResponse.LoginResult;
import it.iccu.sbn.extension.auth.bibliotecario.BibliotecarioAuthException;
import it.iccu.sbn.extension.auth.bibliotecario.BibliotecarioAuthStrategy;
import it.iccu.sbn.servizi.bibliotecario.auth.impl.BibLoginResponseImpl;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;

import org.apache.log4j.Logger;

public class BibliotecarioLoginService {

	static Logger log = Logger.getLogger(BibliotecarioLoginService.class);

	static List<BibliotecarioAuthStrategy> authStrategies;

	static {
		authStrategies = new ArrayList<BibliotecarioAuthStrategy>();
		Iterator<BibliotecarioAuthStrategy> i = ServiceRegistry.lookupProviders(BibliotecarioAuthStrategy.class);
		while (i.hasNext()) {
			authStrategies.add(i.next());
		}

		Collections.sort(authStrategies, new Comparator<BibliotecarioAuthStrategy>() {
			public int compare(BibliotecarioAuthStrategy bas1, BibliotecarioAuthStrategy bas2) {
				return bas1.priority() - bas2.priority();
			}
		});
	}

	public static BibLoginResponse login(final String userId, final String password) throws SbnBaseException {

		if (!ValidazioneDati.isFilled(userId))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "userId");

		if (!ValidazioneDati.isFilled(password))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "password");

		for (BibliotecarioAuthStrategy strategy : authStrategies)
		try {
			BibLoginResponse response = strategy.login(userId.trim(), password.trim());
			switch (response.getStatus()) {
			case LOGGED:
				return response;

			case NOT_FOUND:
			case UNSUPPORTED:
				//tentativo con prossimo metodo di auth
				log.warn(String.format("login(): %s: bibliotecario '%s' non autenticato (%s).",
						strategy.getClass().getSimpleName(), userId, response.getStatus()));
				continue;

			case AUTH_DATA_EXPIRED:
			case DISABLED:
				return response;
			}

		} catch (BibliotecarioAuthException e) {
			Throwable cause = e.getCause();
			throw cause instanceof SbnBaseException ? (SbnBaseException)cause : new ApplicationException(e);
		}

		return new BibLoginResponseImpl(LoginResult.NOT_FOUND);
	}

	public static void logout(String ticket, final String userId) throws SbnBaseException {

		if (!ValidazioneDati.isFilled(userId))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "userId");

		for (BibliotecarioAuthStrategy loginStrategy : authStrategies)
		try {
			loginStrategy.logout(userId);

		} catch (BibliotecarioAuthException e) {
			Throwable cause = e.getCause();
			throw cause instanceof SbnBaseException ? (SbnBaseException)cause : new ApplicationException(e);
		}

	}

}
