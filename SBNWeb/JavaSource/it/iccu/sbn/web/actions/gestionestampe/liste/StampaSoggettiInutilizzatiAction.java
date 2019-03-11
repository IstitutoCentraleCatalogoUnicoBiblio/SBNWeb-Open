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
package it.iccu.sbn.web.actions.gestionestampe.liste;

import it.iccu.sbn.web.actionforms.gestionestampe.liste.StampaSoggettiInutilizzatiForm;
//import it.iccu.sbn.web.integration.bo.NavigationPathBO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class StampaSoggettiInutilizzatiAction extends Action {
	private StampaSoggettiInutilizzatiForm ricOrdini;






	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


			HttpSession httpSession = request.getSession();
			//NavigationPathBO.updateForm(httpSession, form, mapping.getPath());
			ricOrdini = (StampaSoggettiInutilizzatiForm) form;








		ricOrdini.setTipoRicerca("");


//		NavigationPathBO.updateNavigationPath(httpSession, form, mapping.getPath(), false);
		return mapping.getInputForward();

	}

}

