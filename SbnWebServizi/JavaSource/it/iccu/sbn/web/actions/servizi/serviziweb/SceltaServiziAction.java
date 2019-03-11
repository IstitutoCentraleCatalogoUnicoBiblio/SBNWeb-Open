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
import it.iccu.sbn.web.actionforms.servizi.serviziweb.SceltaServiziForm;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web2.util.Constants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
public class SceltaServiziAction extends LookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.indietro", "indietro");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SceltaServiziForm currentForm = (SceltaServiziForm)form;
		HttpSession session = request.getSession();
		//
		currentForm.setTitolo((String)session.getAttribute(Constants.DOCUMENTO));
		currentForm.setSegnatura((String)request.getAttribute("segnatura"));
		currentForm.setUtenteCon((String)session.getAttribute(Constants.UTENTE_CON));
		currentForm.setBiblioSel((String)session.getAttribute(Constants.BIBLIO_SEL));
		UtenteWeb ute = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		Integer idUte = ute.getIdUtente();
		UtenteWeb utenteEjb = null;
		ServiziFactoryEJBDelegate factory = ServiziFactoryEJBDelegate.getInstance();;
		utenteEjb = factory.getGestioneServiziWeb().listaServ(idUte);
		currentForm.setListaServ(utenteEjb.getListaServ());
		request.setAttribute("segnatura",currentForm.getSegnatura());
		return mapping.getInputForward();
	}
	//
	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SceltaServiziForm currentForm = (SceltaServiziForm)form;
		request.setAttribute("segnatura",currentForm.getSegnatura());
		return (mapping.findForward("menuServizi"));
	}
}
