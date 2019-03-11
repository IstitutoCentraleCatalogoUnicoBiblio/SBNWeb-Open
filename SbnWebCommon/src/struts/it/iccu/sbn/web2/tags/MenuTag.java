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

import it.iccu.sbn.ejb.vo.common.TreeElementViewMenu;
import it.iccu.sbn.web.vo.TreeElementView;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

public class MenuTag extends TagSupport {

	private static final long serialVersionUID = 4539612954685685762L;

	private void renderArea(TreeElementView menuArea, JspWriter out,
			String contextPath, MessageResources boundle, HttpServletResponse response) throws IOException {
		String key = menuArea.getKey();
		String text = boundle.getMessage(key);
		out.println("<li>");
		if (menuArea.isOpened()) {
			out.print("<span>");
			out.print(text);
			out.println("</span>");
			if (menuArea.hasChildren()) {
				out.println("<ul class='sottoaree'>");
				for (TreeElementView menuSottoarea : menuArea.getChildren()) {
					renderSottoarea(menuSottoarea, out, contextPath, boundle, response);
				}
				out.println("</ul>");
			}
		} else {
			out.print("<a title='" + text + "' ");
			out.print("href='");
			// almaviva5_20070316 url rewriting per session id
			String actionPath = contextPath + "/menu.do?id=" + key + "&"
					+ LinkableTagUtils.FORM_TARGET_PARAM + "=0";
			String myPath = response.encodeURL(actionPath);
			out.print(myPath);
			out.print("'>");
			out.print(text);
			out.println("</a>");
		}
		out.println("</li>");
	}

	private void renderSottoarea(TreeElementView menuSottoarea, JspWriter out,
			String contextPath, MessageResources boundle, HttpServletResponse response) throws IOException {
		String key = menuSottoarea.getKey();
		String text = boundle.getMessage(key);
		out.println("<li>");
		out.print("<a title=\"" + text + "\" ");
		out.print("href=\"");

		//almaviva5_20091229 check su indirizzo remoto (per OPAC)
		String myPath;
		TreeElementViewMenu menu = (TreeElementViewMenu) menuSottoarea;
		if (menu.isRemoto()) {
			myPath = menu.getUrl().startsWith("http") ? menu.getUrl() : "http://" + menu.getUrl();
			out.print(myPath);
			out.print("\" target=\"opac\">"); // apre in finestra esterna

		} else {
		// 	almaviva5_20070316 url rewriting per session id
			String actionPath = contextPath + "/menu.do?id=" + key + "&" + LinkableTagUtils.FORM_TARGET_PARAM + "=0";
			myPath = response.encodeURL(actionPath);
			out.print(myPath);
			out.print("\">");
		}

		out.print(text);
		out.println("</a>");
		out.println("</li>");
	}

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			HttpSession session = request.getSession();
			String contextPath = request.getContextPath();

			TreeElementView userMenu = (TreeElementView) session.getAttribute(Constants.USER_MENU);
			if (userMenu == null)
				return EVAL_PAGE;

			MessageResources boundle = (MessageResources) pageContext.getServletContext().getAttribute(Globals.MESSAGES_KEY);
			out.println("<ul class='aree'>");
			for (TreeElementView menuArea : userMenu.getChildren()) {
				renderArea(menuArea, out, contextPath, boundle, response);

			}
			out.println("</ul>");

		} catch (IOException e)
		{
		}
		return 0;
	}

	public int doEndTag() throws JspException {
		return 0;
	}
}
