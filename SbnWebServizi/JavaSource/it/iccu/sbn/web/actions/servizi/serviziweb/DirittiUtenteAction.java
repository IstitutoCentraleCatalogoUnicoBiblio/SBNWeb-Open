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

import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.web.actionforms.servizi.serviziweb.DirittiUtenteForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class DirittiUtenteAction extends LookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}
	//
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//
		DirittiUtenteForm currentForm = (DirittiUtenteForm) form;
		Navigation navi = Navigation.getInstance(request);
		//
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			HttpSession session = request.getSession();
			UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);

			currentForm.setUtenteCon(utente.getCognome() + " " + utente.getNome());
			currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
			currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));
			currentForm.setAmbiente((String) session.getAttribute(Constants.POLO_NAME));
			Integer idUtente = (Integer)session.getAttribute(Constants.ID_UTE_BIB);
			ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();

			if (idUtente == null) {
				//invio msg:"selezionare una biblioteca e premere "Ok".
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.dirittiUtente"));
				this.saveErrors(request, errors);
				Navigation.getInstance(request).purgeThis();
				return (mapping.findForward("selezioneBiblioteca"));
				//return mapping.getInputForward();
			}else {
				//valorizzo
				List dirittiUtente  = factory.getGestioneServiziWeb().dirittiUtente(idUtente);
				if (dirittiUtente.size() == 0) {
					//invio msg:"Momentaneamente non sono presenti diritti utente"
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.servizi.utenti.no.dirittiUtente"));
					this.saveErrors(request, errors);
				}
				currentForm.setListaDir(dirittiUtente);
				return mapping.getInputForward();
			}
		}
		catch (Exception ex) {
			//Errore caricamento lista diritti utente
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.lista.diritti.utente"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return (mapping.getInputForward());
		}
	}
}
