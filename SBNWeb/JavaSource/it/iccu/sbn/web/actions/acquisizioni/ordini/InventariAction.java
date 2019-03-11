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
package it.iccu.sbn.web.actions.acquisizioni.ordini;

import it.iccu.sbn.web.actionforms.acquisizioni.ordini.InventariForm;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

public class InventariAction extends  LookupDispatchAction {

	//private static String varIndietro = null;
	//private InventariForm invent;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ricerca.button.indietro","indietro");
		map.put("ricerca.button.salva","salva");
        map.put("servizi.bottone.si", "Si");
        map.put("servizi.bottone.no", "No");
		return map;
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			InventariForm invent = (InventariForm) form;
			if (Navigation.getInstance(request).isFromBar() )
		        return mapping.getInputForward();
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InventariForm invent = (InventariForm) form;
		try {
			return mapping.findForward("indietro");
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward salva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		InventariForm invent = (InventariForm) form;
		try {
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	public ActionForward Si(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws  Exception  {
		InventariForm invent = (InventariForm) form;
		try {
			invent.setConferma(false);
			return mapping.getInputForward();
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}


	public ActionForward No(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InventariForm invent = (InventariForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		invent.setConferma(false);
		return mapping.getInputForward();
	}


}
