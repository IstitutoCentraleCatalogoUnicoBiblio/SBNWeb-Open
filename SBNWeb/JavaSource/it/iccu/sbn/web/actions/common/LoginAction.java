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
package it.iccu.sbn.web.actions.common;

import it.iccu.sbn.ejb.remote.Menu;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.exception.UtenteNotProfiledException;
import it.iccu.sbn.web.actionforms.common.LoginForm;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.FactoryUtente;
import it.iccu.sbn.web.integration.bd.LogoutDelegate;
import it.iccu.sbn.web.integration.bd.menu.MenuDelegate;
import it.iccu.sbn.web.vo.DescrizioneFunzioneVO;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public final class LoginAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(LoginAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.login", "login");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	@SuppressWarnings("unchecked")
	public ActionForward login(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();

		LoginForm currentForm = (LoginForm) form;
		Utente utenteEjb = null;

		String username = currentForm.getUserName().toLowerCase();
		String password = currentForm.getPassword();

		log.debug("tentativo login: " + username);

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		Menu menuEjb = factory.getMenu();

		UserVO user = null;
		// 1) Faccio l'autenticazione sull EJB
		HttpSession session = request.getSession();
		try {
			utenteEjb = FactoryUtente.getUtente(request);

			user = utenteEjb.getUtente(username, password, Navigation.getInstance(request).getUserAddress());
			session.setAttribute(Constants.UTENTE_KEY, user);

			Object lang = utenteEjb.getDefault(ConstantDefault.LANGUAGE);
			Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
			locale = new Locale(((String)lang).toLowerCase(),((String)lang).toUpperCase());
            session.setAttribute(Globals.LOCALE_KEY, locale);

    		if (user.isNewPassword() )
    			return (mapping.findForward("password"));


		} catch (UtenteNotFoundException ute) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ute.getErrorCode().getErrorMessage()));
			this.saveErrors(request, errors);
			log.error("Login fallito per " + username +": " + ute.getMessage() );
			return (mapping.getInputForward());

		}
			catch (UtenteNotProfiledException ute) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.authentication.noprofiled", ute.getMessage()));
				this.saveErrors(request, errors);
				log.error("Login fallito per " + username +": " + ute.getMessage() );
				return (mapping.getInputForward());

		} catch (DefaultNotFoundException ute) {
			log.error("", ute);
		} catch (NoSuchObjectException e) {
			try {
				LogoutDelegate.logout(request, factory);
			} catch (Exception ex) {
				log.error("", ex);
			}
			return mapping.getInputForward();
		}

		// 2) Carico il Menu interrogando l'EJB
		TreeElementView userMenu = (TreeElementView) session.getAttribute(Constants.USER_MENU);
		if (userMenu == null) {
			userMenu = MenuDelegate.getInstance(request).getUserRootMenu(utenteEjb, user);
			if (userMenu != null) {
				userMenu.open();
				session.setAttribute(Constants.USER_MENU, userMenu);
			}
		}

		// 3) Carico le funzioni utilizati per il "Vai a.."
		Map<String, List<DescrizioneFunzioneVO>> funzioni = menuEjb.getFunzioni(user.getTicket());
		session.setAttribute(Constants.USER_FUNZ, funzioni);

		return (mapping.findForward("pagina_predefinita"));

	}
}
