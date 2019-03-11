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
package it.iccu.sbn.web.integration.bd.menu;
import it.iccu.sbn.ejb.model.tree.TreeElement;
import it.iccu.sbn.ejb.remote.Menu;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UserWrapper;
import it.iccu.sbn.ejb.vo.common.TreeElementViewMenu;
import it.iccu.sbn.web.integration.BusinessDelegateException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.UserVO;

import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class MenuDelegate {
    private static Logger log = Logger.getLogger(MenuDelegate.class);

    private static final String MENU_DELEGATE = "req.menu.delegate";

    private Menu remote;

    /** Constructor */
    private MenuDelegate() throws BusinessDelegateException {
        try {
            remote = FactoryEJBDelegate.getInstance().getMenu();

        } catch (Exception e) {
            log.error("Impossibile creare l'istanza", e);
            throw new BusinessDelegateException("BusinessDelegateException: \n"
                    + e.getMessage());
        }
    }

	public static final MenuDelegate getInstance(HttpServletRequest request) throws Exception {
		MenuDelegate delegate = (MenuDelegate) request.getAttribute(MENU_DELEGATE);
		if (delegate == null) {
			delegate = new MenuDelegate();
			request.setAttribute(MENU_DELEGATE, delegate);
		}
		return delegate;
	}

    public TreeElement getRootMenu() throws BusinessDelegateException {
        TreeElement root = null;
        try {
            root = remote.getRootMenu();
            log.info("Caricato menu");
        } catch (RemoteException e) {
            log.error("Impossibile creare l'istanza", e);
        }
        return root;
    }

    public boolean isUserEnabled(UserVO utente, TreeElement element) {
        try {
            return remote.isUserEnabled(utente.getUserId(),
                    utente.getCodBib(), element.getKey());
        } catch (RemoteException de) {
            log.error("MenuDelegate: failed getting isUserEnabled: ");
            return false;
        }
    }

	private TreeElementViewMenu getUserMenu(TreeElement menu, Utente utenteEjb, UserVO user) {

		TreeElementViewMenu menuView = null;
		if (menu != null) {
			//almaviva5_20100720 verifica autorizzazioni
			if (!menu.check(new UserWrapper(utenteEjb, user)) )
				return null;

			menuView = new TreeElementViewMenu();
			menuView.setKey(menu.getKey());
			menuView.setText(menu.getText());
			menuView.setDescription(menu.getDescription());
			menuView.setUrl(menu.getUrl());
			List<TreeElementView> children = menuView.getChildren();
			for (TreeElement child : menu.getChildren()) {
				TreeElementViewMenu childView = getUserMenu(child, utenteEjb, user);
				if (childView == null)
					continue;

				children.add(childView);
				childView.setParent(menuView);
			}
		}
		return menuView;
	}

	public TreeElementViewMenu getUserRootMenu(Utente utenteEjb, UserVO user) {

		TreeElementViewMenu root = null;
		TreeElement rootMenu = null;

		try {
			rootMenu = getRootMenu();
		} catch (BusinessDelegateException e) {
			log.error("", e);
		}

		root = getUserMenu(rootMenu, utenteEjb, user);

		if (root != null)
			root.open();

		return root;
	}

}
