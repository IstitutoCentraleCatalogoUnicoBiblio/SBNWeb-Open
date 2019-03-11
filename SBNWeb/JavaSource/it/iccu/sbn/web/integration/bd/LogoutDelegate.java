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
package it.iccu.sbn.web.integration.bd;

import it.iccu.sbn.ejb.remote.AmministrazioneSistema;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogoutDelegate {

	public static final void logout(HttpServletRequest request, FactoryEJBDelegate factory)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		String ticket = navi.getUserTicket();
		if (ticket != null) {
			if (factory != null) {
				try {
					AmministrazioneSistema sistemaEjb = factory.getSistema();
					sistemaEjb.clearBlocchiAll(ticket);
					sistemaEjb.removeUserTicket(ticket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		HttpSession session = request.getSession();
		session.removeAttribute(Constants.UTENTE_BEAN);
		session.removeAttribute(Constants.UTENTE_KEY);
		session.removeAttribute(Constants.USER_MENU);
		session.invalidate();

	}

}
