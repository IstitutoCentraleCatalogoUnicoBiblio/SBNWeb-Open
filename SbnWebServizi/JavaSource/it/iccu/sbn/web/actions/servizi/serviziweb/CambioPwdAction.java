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
package it.iccu.sbn.web.actions.servizi.serviziweb;

import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.CambioPwdForm;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.serviziweb.LogoutDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

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

// almaviva
public final class CambioPwdAction extends ServiziModuloWebAction {

	private static Logger log = Logger.getLogger(CambioPwdAction.class);

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.newpassword", "password");
		map.put("button.logout", "esci");
		return map;
	}
	//
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		UtenteWeb utenteLettore = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);
		log.debug(utenteLettore);
		CambioPwdForm currentForm = (CambioPwdForm) form;
		//
		currentForm.setUtenteCon(utenteLettore.getCognome() + " " + utenteLettore.getNome());
		currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));
		//
		currentForm.setNewPassword("");
		currentForm.setOldPassword("");
		currentForm.setRePassword("");
		return mapping.getInputForward();
	}

	public ActionForward password(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		CambioPwdForm currentForm = (CambioPwdForm) form;
		String password = currentForm.getNewPassword();
		currentForm.setUser(ServiziUtil.espandiCodUtente(currentForm.getUser()));

		HttpSession session = request.getSession();
		UtenteWeb user = (UtenteWeb) session.getAttribute(Constants.UTENTE_WEB_KEY);

		//
		log.debug("cambio password per utente: " + user.getUserId());
		//
		ActionMessages errors = this.validateForm(request, currentForm);
		if (errors.size() > 0) {
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
		//******************************
		//eseguo metodo EJB per aggiornamento del campo "password" sulla tab. "tbl_utenti";
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();

		//(user.getUserId(), password);
		Navigation navi = Navigation.getInstance(request);
		boolean ritpwd = factory.getGestioneServiziWeb().cambioPwd(navi.getUserTicket(), user.getUserId(), password);

		if (ritpwd) {
			//se aggiornamento password OK
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.codiceAggiornamentoEffettuato"));
			saveErrors(request, errors);

			UtenteWeb uteWeb = factory.getGestioneServiziWeb().login(navi.getUserTicket(), user.getUserId(), password, null, navi.getUserAddress());

			session.setAttribute(Constants.UTENTE_WEB_KEY, uteWeb);
			session.setAttribute(Constants.UTENTE_CON, user.getUserId());
			session.setAttribute(Constants.PASSWORD, password);

			navi.purgeThis();

			//se avevo precedentemente impostato i dati per una richiesta da opac
			//continuo con la scelta del servizio
			RichiestaOpacVO richiesta = (RichiestaOpacVO) session.getAttribute(Constants.RICHIESTA_OPAC);
			if (richiesta != null) {
				return super.selectNextForwardRichiesta(request, mapping, uteWeb, richiesta);
			}

			return mapping.findForward("selezioneBiblioteca");

		}else {
			//altrimenti messaggio errore e ricarico la pagina corrente
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.passwordErrore"));

			return mapping.getInputForward();
		}

	}

	public ActionForward esci(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LogoutDelegate.clear(request);

		return (mapping.findForward("login"));

	}

	private ActionMessages validateForm(HttpServletRequest request,
			ActionForm form) {

		CambioPwdForm currentForm = (CambioPwdForm) form;
		ActionMessages errors = new ActionMessages();

		if (currentForm.getOldPassword() != null
				&& currentForm.getNewPassword() != null
				&& currentForm.getRePassword() != null) {
			if (currentForm.getOldPassword().length() <= 0) {
				errors.add("Vecchia Password", new ActionMessage(
						"campo.obbligatorio", "Vecchia Password"));
				return errors;
			}

			if (currentForm.getNewPassword().length() <= 0) {
				errors.add("Nuova Password", new ActionMessage(
						"campo.obbligatorio", "Nuova Password"));
				return errors;
			}

			if (currentForm.getRePassword().length() <= 0) {
				errors.add("Conferma Password", new ActionMessage(
						"campo.obbligatorio", "Conferma Password"));
				return errors;
			}
			if (currentForm.getNewPassword().length() < 8) {
				errors.add("Nuova Password", new ActionMessage(
						"campo.minorediotto", "Nuova Password"));
				return errors;
			}

			if (currentForm.getRePassword().length() < 8) {
				errors.add("Conferma Password", new ActionMessage(
						"campo.minorediotto", "Conferma Password"));
				return errors;
			}
			if (currentForm.getNewPassword()
					.equals(currentForm.getRePassword())) {

				HttpSession session = request.getSession();
				String oldPwd = (String) session.getAttribute(Constants.PASSWORD);
				if (!oldPwd.equals(currentForm.getOldPassword())) {
					errors.add("Vecchia Password", new ActionMessage(
							"login.password.errate", "Vecchia Password"));
					return errors;
				}

				if (currentForm.getNewPassword().equals(oldPwd)) {
					errors.add("Nuova Password", new ActionMessage(
							"login.password.errate", "Nuova Password"));
					return errors;
				}

			} else {
				errors.add("Nuova Password", new ActionMessage(
						"login.password.uguale", "Nuova Password",
						"Conferma Password"));
			}
		}
		return errors;
	}
}
