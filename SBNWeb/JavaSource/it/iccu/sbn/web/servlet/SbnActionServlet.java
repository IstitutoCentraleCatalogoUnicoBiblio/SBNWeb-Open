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
package it.iccu.sbn.web.servlet;

import it.iccu.sbn.extension.struts.StrutsAdditionalConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.formatValueList;

public class SbnActionServlet extends ActionServlet {


	private static final long serialVersionUID = -4019721652684788024L;

	private static final String CONFIG_SEPARATOR = ", ";

	static List<StrutsAdditionalConfig> additionalConfigs;

	static {
		additionalConfigs = new ArrayList<StrutsAdditionalConfig>();
		Iterator<StrutsAdditionalConfig> i = ServiceRegistry.lookupProviders(StrutsAdditionalConfig.class);
		while (i.hasNext()) {
			additionalConfigs.add(i.next());
		}
	}

	@Override
	protected void initOther() throws ServletException {
		super.initOther();

		// integrazione con action config importate da moduli esterni
		StringBuilder buf = new StringBuilder();
		buf.append(this.config);
		for (StrutsAdditionalConfig additionalConfig : additionalConfigs) {
			String paths = coalesce(formatValueList(additionalConfig.getPaths(), CONFIG_SEPARATOR), "");
			switch (buf.length()) {
			case 0:
				buf.append(paths);
				break;
			default:
				buf.append(CONFIG_SEPARATOR).append(paths);
				break;
			}
		}

		this.config = buf.toString();
	}

}
