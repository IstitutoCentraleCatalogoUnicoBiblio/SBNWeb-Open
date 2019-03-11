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

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseHandlerTag;

/**
 * AlternateColorTag.java
 * @author almaviva
 */
public class AlternateColorTag extends BaseHandlerTag {

	private static final long serialVersionUID = -4686393304354824375L;
	private static final String EVEN_COLOR_VAR = "even.row.color";
	private static final String ODD_COLOR_VAR  = "odd.row.color";

	private String index;
	private String var;
	private boolean auto = false;

	@Override
	public int doStartTag() throws JspException {

		Integer indexValue = null;
		TagUtils tag = TagUtils.getInstance();
		Object tmp = tag.lookup(pageContext, this.index, null);
		if (tmp instanceof Number)
			indexValue  = ((Number)tmp).intValue();
		else
			indexValue = Integer.valueOf((String) tmp);

		if (this.auto) {
			indexValue++;
			pageContext.setAttribute(this.index, indexValue);
		}

		String color = null;
		if ( (indexValue % 2) == 0) {
			String message = this.message(null, EVEN_COLOR_VAR);
			color = message;
		} else
			color = this.message(null, ODD_COLOR_VAR);

		pageContext.setAttribute(this.var, color);
		return EVAL_PAGE;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}



}
