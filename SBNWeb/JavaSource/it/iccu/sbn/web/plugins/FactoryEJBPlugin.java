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
package it.iccu.sbn.web.plugins;

import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

public class FactoryEJBPlugin implements PlugIn {

	public static final String FACTORY_EJB = "FACTORY_EJB";

	public FactoryEJBPlugin() {
		super();
	}

	public void destroy() {
		return;
	}

	public void init(ActionServlet servlet, ModuleConfig moduleConfig)
			throws ServletException {
		try {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			ServletContext ctx = servlet.getServletContext();
			ctx.setAttribute(FACTORY_EJB, factory);

		} catch (Exception e) {
			throw new ServletException("BusinessDelegateException: \n",	e.getCause());
		}

	}

}
