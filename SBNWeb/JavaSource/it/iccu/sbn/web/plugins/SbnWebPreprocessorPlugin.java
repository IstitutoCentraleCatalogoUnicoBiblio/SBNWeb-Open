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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.PoloVO;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.util.PuliziaSbnMarcBlocchi;
import it.iccu.sbn.web.vo.DescrizioneFunzioneVO;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationHook;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.NavigationPreprocessor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;


public class SbnWebPreprocessorPlugin implements PlugIn, NavigationPreprocessor, NavigationHook {

	private static final Logger log = Logger.getLogger(SbnWebPreprocessorPlugin.class);

	public boolean preprocess(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		UserVO user = (UserVO) session.getAttribute(Constants.UTENTE_KEY);
		Navigation navi = Navigation.getInstance(request);
		navi.setHook(this);

		try {
			String servletPath = request.getServletPath();
			if (session != null
					&& user != null
					&& !servletPath.equals("/login.do")) {

				if (user.isNewPassword() && !servletPath.equals("/changePassword.do")) {

					request.getRequestDispatcher("/changePassword.do").forward(request, response);
					return false;
				}

				if (request.getParameter("language") != null) {
					Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
					locale = new Locale(request.getParameter("language")
							.toLowerCase(), request.getParameter("language")
							.toUpperCase());
					session.setAttribute(Globals.LOCALE_KEY, locale);
				}

				if (servletPath.equals("/gestioneVaiA.do")) {

					Map<String, List<DescrizioneFunzioneVO>> funzioni = (Map<String, List<DescrizioneFunzioneVO>>) session.getAttribute(Constants.USER_FUNZ);
					String mod_funz = (String) request.getAttribute("MODULO_VAI_A");
					String cod_funz = (String) request.getAttribute("FUNZIONE_VAI_A");
					List<DescrizioneFunzioneVO> listaFunzioni = funzioni.get(mod_funz);
					for (DescrizioneFunzioneVO funz : listaFunzioni) {
						if (funz.getCodice().equals(cod_funz)) {
							if (!funz.getActionPath().equals("")) {
								request.getRequestDispatcher(funz.getActionPath()).forward(request,	response);
								return false;
							}
						}
					}

				}
				return true;
			}

			if (session != null
					&& user != null
					&& servletPath.equals("/login.do")) {

				// redirect su pagina login
				request.getRequestDispatcher("/index.jsp").forward(request, response);

				return false;
			}

			// Se sto già tentando di arrivare al login non faccio altri controlli
			if (servletPath.equals("/login.do"))
				return true;

			request.getRequestDispatcher("/login.do").forward(request, response);

		} catch (Exception e) {
			log.error("", e);
			navi.setExceptionLog(e);
		}

		return false;

	}

	public void destroy() {
		log.info("destroy()");
		return;
	}

	public void init(ActionServlet servlet, ModuleConfig moduleConfig)
			throws ServletException {

		log.info("init()");
		servlet.getServletContext().setAttribute(Navigation.NAVIGATION_PREPROCESSOR, this);

		//inserito trattamento delle enum java nei tag struts
		setupPropertyUtils();
	}

	public boolean isNew() {
		return true;
	}

	public void resetNavigation(HttpServletRequest request) {

		if (request != null) {
			Navigation navi = Navigation.getInstance(request);
			String ticket = navi.getUserTicket();

			if (ticket != null)
				try {
					FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
					if (factory != null)
						factory.getSistema().clearBlocchiAll(ticket);

				} catch (Exception e) {
					e.printStackTrace();
				}

				PuliziaSbnMarcBlocchi.removeSbnMarcIdLista(navi);
		}

	}

	public void setSessionInfo(HttpSession session) {
		try {
			if (session.getAttribute(Constants.POLO_NAME) != null)
				return;

			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			PoloVO polo = factory.getSistema().getPolo();
			session.setAttribute(Constants.POLO_CODICE, polo.getCd_polo() );
			session.setAttribute(Constants.SBNMARC_BUILD_TIME, polo.getSbnMarcBuildTime() );

			String denominazione = polo.getDenominazione();
			if (ValidazioneDati.isFilled(denominazione))
				session.setAttribute(Constants.POLO_NAME, denominazione + " - "	+ Constants.APP_NAME);
			else
				session.setAttribute(Constants.POLO_NAME, Constants.APP_NAME);
		} catch (Exception e) {
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
		try {
			//almaviva5_20100204 messaggio avviso per redeploy sistema
			Boolean redeploy = false;//Boolean.valueOf(CommonConfiguration.getProperty(Configuration.REDEPLOY_WARNING_MESSAGE, "FALSE"));
			if (!redeploy )
				return;

			LinkableTagUtils.addError(request, new ActionMessage("errors.application.redeploy"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset(HttpServletRequest request) {
		Pulisci.PulisciVar(request);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static class EnumAwarePropertyUtilsBean extends PropertyUtilsBean {

		@Override
		public void setSimpleProperty(Object bean, String name, Object value)
				throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

			if (bean == null) {
				throw new IllegalArgumentException("No bean specified");
			}
			if (name == null) {
				throw new IllegalArgumentException("No name specified");
			}

			// Retrieve the property setter method for the specified property
			PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
			if (descriptor == null) {
				throw new NoSuchMethodException("Unknown property '" + name + "'");
			}

			Method writeMethod = getWriteMethod(descriptor);
			if (writeMethod == null) {
				throw new NoSuchMethodException("Property '" + name + "' has no setter method");
			}

			//conversione da valore stringa a equivalente enum.
			Class paramType = writeMethod.getParameterTypes()[0];
			if (paramType.isEnum() && (value instanceof String)) {
				value = Enum.valueOf(paramType, (String) value);
			}

			super.setSimpleProperty(bean, name, value);

		}

	}

	private void setupPropertyUtils() {
		//almaviva5_20161010 override standard beanutils
		BeanUtilsBean defaultBeanUtilsBean = BeanUtilsBean.getInstance();
		BeanUtilsBean enumAwareBeanUtilsBean = new BeanUtilsBean(
			defaultBeanUtilsBean.getConvertUtils(),
			new EnumAwarePropertyUtilsBean() );
		BeanUtilsBean.setInstance(enumAwareBeanUtilsBean);
	}

}
