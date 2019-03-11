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
package it.iccu.sbn.web.plugins;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.PoloVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.ServiziWebMenuDelegate;
import it.iccu.sbn.web.vo.FakeUserWeb;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.NavigationPreprocessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

public class SbnWebServiziPreprocessorPlugin implements PlugIn,	NavigationPreprocessor {

	private static Logger log = Logger.getLogger(SbnWebServiziPreprocessorPlugin.class);
	private AmministrazionePolo amministrazionePolo;

	private AmministrazionePolo getAmministrazionePolo() throws Exception {
		if (amministrazionePolo != null)
			return amministrazionePolo;

		amministrazionePolo = DomainEJBFactory.getInstance().getPolo();

		return amministrazionePolo;
	}

	public void destroy() {
		log.info("destroy()");
		return;
	}

	public void init(ActionServlet servlet, ModuleConfig arg1)
			throws ServletException {
		log.info("init()");
		servlet.getServletContext().setAttribute(
				Navigation.NAVIGATION_PREPROCESSOR, this);
		try {
			servlet.getServletContext().setAttribute(
					FactoryEJBPlugin.FACTORY_EJB,
					ServiziFactoryEJBDelegate.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean preprocess(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		UtenteWeb user = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);
		Navigation navi = Navigation.getInstance(request);
		try {

			String servletPath = request.getServletPath();
			if (user == null
				&& !ValidazioneDati.in(servletPath,
						"/login.do",
						"/serviziweb/listaBiblio.do",
						"/serviziweb/cambioPwd.do",
						"/serviziweb/inserimentoutenteWeb.do") ) {

				request.getRequestDispatcher("/login.do").forward(request, response);
				return false;
			}

			TreeElementView menu = (TreeElementView) session.getAttribute(Constants.USER_MENU);
			if (menu == null && user != null) {
				menu = ServiziWebMenuDelegate.renderMenu(request);
				session.setAttribute(Constants.USER_MENU, menu);
			}

			return true;

		} catch (Exception e) {
			log.error("", e);
			navi.setExceptionLog(e);
		}

		return false;
	}

	public boolean isNew() {
		return true;
	}

	public void resetNavigation(HttpServletRequest request) {
		return;
	}

	public void setSessionInfo(HttpSession session) {
		try {
			if (session.getAttribute(Constants.POLO_NAME) != null)
				return;

			int timeout = CommonConfiguration.getPropertyAsInteger(Configuration.SERVIZI_HTTP_SESSION_TIMEOUT, 5);
			session.setMaxInactiveInterval(timeout * 60);
			log.debug("Timeout sessione HTTP impostato a " + timeout + " minuti");

			PoloVO polo = getAmministrazionePolo().getInfoPolo();
			session.setAttribute(Constants.POLO_CODICE, polo.getCd_polo());
			session.setAttribute(Constants.SBNMARC_BUILD_TIME, polo.getSbnMarcBuildTime());

			String denominazione = polo.getDenominazione();
			if (ValidazioneDati.isFilled(denominazione))
				session.setAttribute(Constants.POLO_NAME, denominazione + " - "
						+ Constants.APP_NAME);
			else
				session.setAttribute(Constants.POLO_NAME, Constants.APP_NAME);
			// almaviva5_20091111 fake utente per servizi web
			session.setAttribute(Constants.UTENTE_KEY, new FakeUserWeb(polo.getCd_polo()));


			//build time
			session.setAttribute(Constants.BUILD_TIME, polo.getSbnWebBuildTime());

		} catch (Exception e) {
			session.setAttribute(Constants.BUILD_TIME, DateUtil.now() );
			session.setAttribute(Constants.POLO_NAME, Constants.APP_NAME);
		}
	}

	public void beforeAction(HttpServletRequest request,
			HttpServletResponse response, Action action, ActionForm form,
			ActionMapping mapping) {
		return;
	}

	public void afterAction(HttpServletRequest request,
			HttpServletResponse response, Action action, ActionForm form,
			ActionMapping mapping) {
		return;
	}

}
