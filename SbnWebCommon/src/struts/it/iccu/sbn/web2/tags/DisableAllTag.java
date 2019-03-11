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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class DisableAllTag extends BodyTagSupport {

	private static final long serialVersionUID = -7715088497163329544L;
	private static final Pattern pattern =
		Pattern.compile("<(textarea|input|select|button)(.[^>]*?)>", Pattern.CASE_INSENSITIVE);

	private boolean disabled = false;
	private String checkAttivita;
	private boolean inverted = false;


	private boolean doCheckAttivita(HttpServletRequest request, NavigationElement e) {

		if (ValidazioneDati.strIsNull(checkAttivita))
			return true;

		return LinkableTagUtils.checkAttivita((HttpServletRequest) pageContext.getRequest(), false, checkAttivita);
	}


	public void setDisabled(boolean test) {
		this.disabled = test;
	}

	public int doAfterBody() {
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			NavigationElement currentElement = Navigation.getInstance(request).getCache().getCurrentElement();

			boolean skip = !disabled && doCheckAttivita(request, currentElement);
			skip = inverted ? !skip : skip;
			if (skip)
				return SKIP_BODY;

			String bodyString = bodyContent.getString();

			//almaviva5_20110728 segn. ICCU: fix per carattere controllo regex ('$')
			bodyString = bodyString.replaceAll("\\$","\\\\\\$");

			Matcher m = pattern.matcher(bodyString);
			if (!m.find())
				return SKIP_BODY;

			bodyContent.clearBody();
			StringBuffer sb = new StringBuffer();

			m.reset();
			while (m.find()) {
				String tagContent = m.group(2);
				if (tagContent.toUpperCase().indexOf("DISABLED") < 0) {
					String newTag = "<" + m.group(1) + " disabled=\"disabled\" " + tagContent + ">";
					m.appendReplacement(sb, newTag);
				}
			}

			m.appendTail(sb);
			bodyContent.append(sb.toString());

		} catch (IOException e) {
			System.out.println("Error in BodyContentTag.doAfterBody()"
					+ e.getMessage());
			e.printStackTrace();
		} // end of catch

		return SKIP_BODY;

	}

	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().print(bodyContent.getString());
			return EVAL_PAGE;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}

	}


	public String getCheckAttivita() {
		return checkAttivita;
	}


	public void setCheckAttivita(String checkAttivita) {
		this.checkAttivita = checkAttivita;
	}


	public boolean isInverted() {
		return inverted;
	}


	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

}
