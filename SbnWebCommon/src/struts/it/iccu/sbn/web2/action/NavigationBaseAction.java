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
package it.iccu.sbn.web2.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;

public abstract class NavigationBaseAction extends LookupDispatchAction {

	public NavigationBaseAction() {
		super();
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;

		Navigation navi = Navigation.getInstance(request);
		NavigationElement currentElement = navi.getCache().getCurrentElement();

		if (currentElement != null && currentElement.isBack()) {
			currentElement.setBack(false);
			forward = navigationBack(mapping, form, request, response);
		}

		if (forward != null)
			return forward;

		if (!navi.isFromBack() && !isAuthorized())
			return mapping.findForward("blank");

		return super.execute(mapping, form, request, response);
	}

	protected boolean isAuthorized() {
		return true;
	}

	protected ActionForward navigationBack(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		return null;
	}

	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		return;
	}

	protected void loadDefault(HttpServletRequest request, ActionForm form) throws Exception {
		return;
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		return;
	}

}
