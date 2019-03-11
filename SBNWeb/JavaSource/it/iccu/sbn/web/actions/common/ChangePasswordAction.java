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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.web.actionforms.common.ChangePasswordForm;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.FactoryUtente;
import it.iccu.sbn.web.integration.bd.LogoutDelegate;
import it.iccu.sbn.web.integration.bd.menu.MenuDelegate;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public final class ChangePasswordAction extends LookupDispatchAction {

	private static Logger log = Logger.getLogger(ChangePasswordAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.newpassword", "password");
		map.put("button.logout", "esci");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserVO user = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
		if (!user.isNewPassword())
			return (mapping.findForward("pagina_predefinita"));

		return mapping.getInputForward();
	}

	@SuppressWarnings("unchecked")
	public ActionForward password(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ChangePasswordForm currentForm = (ChangePasswordForm) form;
		String password = currentForm.getNewPassword();
		Utente utenteEjb = null;

		HttpSession session = request.getSession();
		UserVO user = (UserVO) session.getAttribute(Constants.UTENTE_KEY);
		log.debug("cambio password per utente: " + user.getUserId());

		ActionMessages errors = this.validate(request, currentForm);
		if (!errors.isEmpty()) {
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		try {
			utenteEjb = FactoryUtente.getUtente(request);
			if (!utenteEjb.changePassword(user.getUserId(), password))
				return mapping.getInputForward();

			user = utenteEjb.getUtente(user.getUserId(), password, Navigation.getInstance(request).getUserAddress());
			session.setAttribute(Constants.UTENTE_KEY, user);

		} catch (UtenteNotFoundException ute) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ute
					.getErrorCode().getErrorMessage()));
			this.saveErrors(request, errors);
			return (mapping.getInputForward());
		} catch (NoSuchObjectException e) {
			try {
				LogoutDelegate.logout(request, FactoryEJBDelegate.getInstance() );
			} catch (Exception ex) {
				log.error("", ex);
			}
			return mapping.getInputForward();
		}

		user.setPassword(password);
		user.setNewPassword(false);

		// 2) Carico il Menu interrogando l'EJB
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		Menu menuEjb = factory.getMenu();

		TreeElementView userMenu = (TreeElementView) session.getAttribute(Constants.USER_MENU);
		if (userMenu == null) {
			userMenu = MenuDelegate.getInstance(request).getUserRootMenu(utenteEjb, user);
			if (userMenu != null) {
				userMenu.open();
				session.setAttribute(Constants.USER_MENU, userMenu);
			}
		}

		// 3) Carico le funzioni utilizati per il "Vai a.."
		session.setAttribute(Constants.USER_FUNZ, menuEjb.getFunzioni(user.getTicket()) );

		return (mapping.findForward("pagina_predefinita"));

	}

	public ActionForward esci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
		if (factory != null) {
			try {
				LogoutDelegate.logout(request, factory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return (mapping.findForward("pagina_predefinita"));

	}

	private ActionMessages validate(HttpServletRequest request,	ActionForm form) {

		ChangePasswordForm currentForm = (ChangePasswordForm) form;
		ActionMessages errors = new ActionMessages();

		UserVO user = (UserVO) request.getSession().getAttribute(Constants.UTENTE_KEY);
		String oldPassword = ValidazioneDati.trimOrNull(currentForm.getOldPassword());
		String newPassword = ValidazioneDati.trimOrNull(currentForm.getNewPassword());
		String rePassword = ValidazioneDati.trimOrNull(currentForm.getRePassword());

		/*
		log.debug("user = '" + user.getPassword() + "'");
		log.debug("oldPassword = '" + oldPassword + "'");
		log.debug("newPassword = '" + newPassword + "'");
		log.debug("rePassword  = '" + rePassword + "'");
		*/

		if (!ValidazioneDati.isFilled(newPassword) ) {
			errors.add("Nuova Password", new ActionMessage("campo.obbligatorio", "Nuova Password"));
			return errors;
		}

		if (!ValidazioneDati.isFilled(oldPassword) ) {
			errors.add("Old Password", new ActionMessage("campo.obbligatorio", "Old Password"));
			return errors;
		}

		if (!ValidazioneDati.isFilled(rePassword) ) {
			errors.add("Conferma Password", new ActionMessage("campo.obbligatorio", "Conferma Password"));
			return errors;
		}

		//Modifica almaviva 09/10/2009
		if (ValidazioneDati.length(newPassword) < 8) {
			errors.add("Nuova Password", new ActionMessage("campo.minorediotto", "Nuova Password"));
			return errors;
		}

		if (ValidazioneDati.length(rePassword) < 8) {
			errors.add("Conferma Password", new ActionMessage("campo.minorediotto", "Conferma Password"));
			return errors;
		}
		//
		if (!ValidazioneDati.equals(newPassword, rePassword) ) {
			errors.add("Nuova Password", new ActionMessage("login.password.uguale", "Nuova Password", "Conferma Password"));
			return errors;
		}

		if (!ValidazioneDati.equals(user.getPassword(), oldPassword)) {
			errors.add("Old Password", new ActionMessage("login.password.errate", "Old Password"));
			return errors;
		}

		if (ValidazioneDati.equals(newPassword, oldPassword)) {
			errors.add("Nuova Password", new ActionMessage("login.password.errate", "Nuova Password"));
			return errors;
		}

		return errors;
	}

}
