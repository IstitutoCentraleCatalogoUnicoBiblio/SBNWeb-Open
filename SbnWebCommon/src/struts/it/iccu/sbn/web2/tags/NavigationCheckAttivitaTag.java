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
package it.iccu.sbn.web2.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.logic.ConditionalTagBase;

import it.iccu.sbn.web2.util.LinkableTagUtils;

public class NavigationCheckAttivitaTag extends ConditionalTagBase {

	private static final long serialVersionUID = -7569252685608816819L;
	private String idControllo;
	private boolean inverted = false;

	@Override
	protected boolean condition() throws JspException {
		return LinkableTagUtils.checkAttivita((HttpServletRequest) pageContext.getRequest(), inverted, idControllo);
	}

	public String getIdControllo() {
		return idControllo;
	}

	public void setIdControllo(String idControllo) {
		this.idControllo = idControllo;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

}
