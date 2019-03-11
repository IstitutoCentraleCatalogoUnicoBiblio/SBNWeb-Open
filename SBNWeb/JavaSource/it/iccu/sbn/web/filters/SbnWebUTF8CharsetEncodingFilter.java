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
package it.iccu.sbn.web.filters;

import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.LogoutDelegate;
import it.iccu.sbn.web.integration.filters.UTF8CharsetEncodingFilter;
import it.iccu.sbn.web2.util.Constants;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SbnWebUTF8CharsetEncodingFilter extends UTF8CharsetEncodingFilter {

	private static final String NOOP_LOOP = "logged.user.session.nop";
	private static final int NOOP_THRESHOLD = 2 * 60 * 1000;	// 2 min
	private static Logger log = Logger.getLogger(SbnWebUTF8CharsetEncodingFilter.class);


	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		if (!refresh(request) )
			try {
				LogoutDelegate.logout(request, FactoryEJBDelegate.getInstance() );
			} catch (Exception e) {
				throw new ServletException(e);
			}

		super.doFilter(servletRequest, servletResponse, filterChain);
	}

	protected boolean refresh(HttpServletRequest request) {

		try {
			HttpSession session = request.getSession();
			Utente utenteEjb = (Utente) session.getAttribute(Constants.UTENTE_BEAN);
			if (utenteEjb == null)
				return true;

			long tick = System.currentTimeMillis();
			Long last = (Long) session.getAttribute(NOOP_LOOP);
			if (last == null) {
				session.setAttribute(NOOP_LOOP, tick);
				return true;
			}

			if ( (tick - last) < NOOP_THRESHOLD )
				return true;

			log.debug(session.getId() + " -> UtenteBean::refresh() [" + tick + "]");
			utenteEjb.refresh();
			session.setAttribute(NOOP_LOOP, System.currentTimeMillis() );

		} catch (TicketExpiredException e) {
			// l'ejb utente è stato distrutto
			log.error("", e);
			return false;
		}

		return true;
	}

}
