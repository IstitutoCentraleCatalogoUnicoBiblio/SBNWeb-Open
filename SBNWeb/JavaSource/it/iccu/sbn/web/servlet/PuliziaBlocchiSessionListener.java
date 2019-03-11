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
package it.iccu.sbn.web.servlet;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.PoloVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.PuliziaSbnMarcBlocchi;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class PuliziaBlocchiSessionListener implements HttpSessionListener {

	private static Logger log = Logger.getLogger(PuliziaBlocchiSessionListener.class);

	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		//log.info("Creata sessione id: " + session.getId());
		try {
			int timeout = CommonConfiguration.getPropertyAsInteger(Configuration.SBNWEB_HTTP_SESSION_TIMEOUT, 30);
			session.setMaxInactiveInterval(timeout * 60);
			log.debug("Timeout sessione HTTP impostato a " + timeout + " minuti");

			PoloVO polo = FactoryEJBDelegate.getInstance().getPolo().getInfoPolo();
			session.setAttribute(Constants.BUILD_TIME, polo.getSbnWebBuildTime() );

		} catch (Exception e) {
			session.setAttribute(Constants.BUILD_TIME, DateUtil.now() );
		}

	}

	public void sessionDestroyed(HttpSessionEvent event) {

		HttpSession session = event.getSession();

		if (session != null) {
			Navigation navi = Navigation.getInstance(session);
			String ticket = navi.getUserTicket();

			if (ticket != null)
				try {
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					if (factory != null) {
						factory.getSistema().clearBlocchiAll(ticket);
						factory.getSistema().removeUserTicket(ticket);
					}

					PuliziaSbnMarcBlocchi.removeSbnMarcIdLista(navi);

				} catch (Exception e) {
						e.printStackTrace();
				}

		}

		log.info("Distruzione sessione id: " + event.getSession().getId());
	}

}
