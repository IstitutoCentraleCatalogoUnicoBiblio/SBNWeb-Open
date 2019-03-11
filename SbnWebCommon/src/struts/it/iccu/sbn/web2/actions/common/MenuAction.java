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
package it.iccu.sbn.web2.actions.common;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.TreeElementViewMenu;
import it.iccu.sbn.web2.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class MenuAction extends Action {

	private static final Logger log = Logger.getLogger(MenuAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();

        String menuKey = request.getParameter("id");
        if (!ValidazioneDati.isFilled(menuKey))
        	return mapping.findForward("blank");

        log.debug("MenuAction::execute() --> " + menuKey);

        String currentMenuKey = (String) session.getAttribute(Constants.CURRENT_MENU);
        ActionForward forward = mapping.findForward("blank");
        TreeElementViewMenu currentMenu = null;
        TreeElementViewMenu userMenu = (TreeElementViewMenu) session.getAttribute(Constants.USER_MENU);

        if (menuKey != null && !menuKey.equals(currentMenuKey)) {
            currentMenu = (TreeElementViewMenu) userMenu.findElement(currentMenuKey);
            if (currentMenu != null) {
                if (currentMenu.getParent() != userMenu)
                    currentMenu = (TreeElementViewMenu) currentMenu.getParent();

                if (currentMenu.isOpened())
                    currentMenu.closeTree();

            }
        }
        currentMenu = (TreeElementViewMenu) userMenu.findElement(menuKey);
        if (currentMenu != null) {
            if (!currentMenu.isOpened())
                currentMenu.open();

            // azzera path di navigazione
            session.setAttribute(Constants.CURRENT_MENU, menuKey);
            if (ValidazioneDati.isFilled(currentMenu.getUrl()) ) {
                forward = new ActionForward();
                forward.setPath(currentMenu.getUrl());//impostazione url
                forward.setRedirect(true);
            }
        }
        return forward;
    }
}
