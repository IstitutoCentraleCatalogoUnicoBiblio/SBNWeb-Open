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
package it.iccu.sbn.web.integration;

import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.NavigationMappingChecker;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ForwardConfig;

public class SbnActionMapping extends ActionMapping implements
		NavigationMappingChecker {


	private static final long serialVersionUID = 3202040406960100364L;
	private static final Log log = LogFactory.getLog(SbnActionMapping.class);
	private static final OrdinamentoUnicode unicode = new OrdinamentoUnicode();

	private String attivita;
	private HashSet<String> attivitaAbilitate = new HashSet<String>();

	public String getAttivita() {
		return attivita;
	}

	public void setAttivita(String attivita) {
		this.attivita = attivita;
		for (String token : attivita.split(","))
			attivitaAbilitate.add(unicode.convert(token).trim() );
	}

	public HashSet<String> getAttivitaAbilitate() {
		return attivitaAbilitate;
	}

	public boolean check(HttpServletRequest request,
			NavigationElement callerElement) {

		if (callerElement == null)
			return true;

		String callerPath = callerElement.getMapping().getPath();
		if (callerPath == null)
			return true;

		log.info(this.name + "::check() caller: " + callerElement);

		ActionConfig actionConfig = getModuleConfig().findActionConfig(callerPath);
		for (ForwardConfig forwardConfig : actionConfig.findForwardConfigs()) {
			String forwardPath = forwardConfig.getPath();
			if (forwardPath.indexOf(this.path) > -1)
				return true;
		}
		return false;
	}

}
