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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts.taglib.html.BaseHandlerTag;

import it.iccu.sbn.web2.util.LinkableTagUtils;

/**
 * BlocchiTag.java 04/lug/07
 *
 * @author almaviva
 */
public class NavigationAnchorTag extends BaseHandlerTag {

	private static final long serialVersionUID = -2814082430309707683L;

	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";
	private String name = null;
	private String property = null;


	public int doStartTag() throws JspException {
		if (name == null)
			name = FORM_NAME;
		String progressivoValue = this.lookupProperty(name, this.property);
		JspWriter out = this.pageContext.getOut();
		try {
			out.write("\n<a name=\"" + LinkableTagUtils.ANCHOR_PREFIX + progressivoValue + "\"></a>");
		} catch (IOException e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
