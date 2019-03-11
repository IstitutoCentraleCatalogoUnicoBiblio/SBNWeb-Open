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

import it.iccu.sbn.ejb.bean.UtenteBean;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.web2.util.Constants;

import java.lang.reflect.Proxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class FactoryUtente {

	public static Utente getUtente(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		Utente utenteEjb = (Utente) session.getAttribute(Constants.UTENTE_BEAN);
		if (utenteEjb == null) {
			utenteEjb = (Utente) Proxy.newProxyInstance(
					Utente.class.getClassLoader(),
					new Class[] { Utente.class }, new UtenteBean());
			session.setAttribute(Constants.UTENTE_BEAN, utenteEjb);

		}
		return utenteEjb;
	}
}
