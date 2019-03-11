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

import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web.vo.TreeElementViewFunzioni;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class TreeFunzioniTag extends TagSupport {

	private static final long serialVersionUID = -2516440619174571298L;

	private String name;
	private String scope;
	private String property;
	private String styleClass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	private void printTreeElement(TreeElementViewFunzioni element,
			String contextPath, JspWriter out) throws IOException {

		out.print("<li>");
		out.print("<label for='FN_");
		out.print(element.getKey());
		out.print("' title='" + element.getDescription());
		out.print("'>" + element.toString() + "</label>");

		out.print("<input type='checkbox' id='FN_");
		out.print(element.getKey());
		out.print("'");
		if (element.isSelected()) {
			out.print(" checked='checked'");
		}
		out.print(" name='" + getProperty());
		out.print("' value='" + element.getKey() + "' />");
		if (!element.getChildren().isEmpty()) {
			out.print("<ul>");
			for (TreeElementView child : element.getChildren()) {
				printTreeElement((TreeElementViewFunzioni) child, contextPath,
						out);
			}
			out.print("</ul>");
		}

		out.println("</li>");

	}

	private void printFirstElement(TreeElementViewFunzioni element,
			String contextPath, JspWriter out) throws IOException {

		if (!element.getChildren().isEmpty()) {
			for (TreeElementView child : element.getChildren()) {
				printTreeElement((TreeElementViewFunzioni) child, contextPath,
						out);
			}
		}
	}

	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		HttpSession session = request.getSession();
		TreeElementViewFunzioni currentMenu = null;

		if ("session".equalsIgnoreCase(getScope())) {
			currentMenu = (TreeElementViewFunzioni) session
					.getAttribute(getName());
		} else if ("request".equalsIgnoreCase(getScope())) {
			currentMenu = (TreeElementViewFunzioni) request
					.getAttribute(getName());
		}

		try {
			String scriptPrefix = "<script type='text/javascript' src='"
					+ request.getContextPath() + "/scripts/";
			out.print(scriptPrefix + "treeFunzioni.js'></script>");

			out.print("<div class='");
			out.print(getStyleClass());
			out.println("'>");
			out.println("<ul class='xx' id='tree3'>");
			printFirstElement(currentMenu, request.getContextPath(), out);
			out.println("</ul>");
			out.print("<script type='text/javascript'>");
			out.print("new TreeView('tree3', undefined, 'green');");
			out.print("</script>");

			out.println("</div>");
		} catch (IOException e) {
		}
		return 0;
	}

	public int doEndTag() throws JspException {
		return 0;
	}

}
