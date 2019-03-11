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
package it.iccu.sbn.web2.navigation;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import org.apache.struts.util.MessageResources;
import org.apache.strutsel.taglib.html.ELFormTag;

public class NavigationFormTag extends ELFormTag {

	private static final long serialVersionUID = 7136856529876408340L;
	private static final String SEPARATORE = "<span>&nbsp;&gt;&nbsp;</span>";

	private String buttonClass = "navBtn";
	private String divId = "divNavPath";
	private boolean bar = true;

	protected static final String OLDBAR = "oldbar";
	private static final String NAV_CURRENT_FORM = "navForm";
	private static final String NAV_ELEMENT_BUTTON = "navButtons";

	public boolean isBar() {
		return bar;
	}

	public void setBar(boolean bar) {
		this.bar = bar;
	}

	private String renderNavigationBar() {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		NavigationCache cache = Navigation.getInstance(request).getCache();
		if (cache.isEmpty())
			return "";

		NavigationElement currentElement = cache.getCurrentElement();
		request.setAttribute(NAV_CURRENT_FORM, currentElement.getForm() );
		request.setAttribute(NAV_ELEMENT_BUTTON, currentElement.getMapping().getParameter() );

		MessageResources messages =
				(MessageResources) this.pageContext.getRequest().getAttribute("org.apache.struts.action.MESSAGE");
		StringBuffer out = new StringBuffer();

		try {
			List<NavigationElement> elements = cache.getLockedElements();

			out.append("<div id=\"").append(divId).append("\">");
			out.append("<input type=\"hidden\" name=\"")
				.append(Navigation.NAVIGATION_CURRENT_ELEMENT)
				.append("\" value=\"")
				.append(currentElement.getUniqueId())
				.append("\" />");

			out.append(renderTagWithJavaScript(elements, messages));
			out.append(renderTagWithoutJavaScript(elements, messages));
			out.append("</div>");

			//almaviva5_20160915
			out.append("<div style=\"display: none;\" id=\"toBottom\"><a href=\"#divFooter\">bottom</a></div>");

		} catch (Exception e) {
			return "";
		} finally {
			cache.unlockElements();
		}

		return out.toString();
	}

	private String renderTagWithoutJavaScript(List<NavigationElement> elements,
			MessageResources messages) {

		StringBuffer out = new StringBuffer();
		out.append("<noscript>");
		for (NavigationElement e : elements) {

			String testo = messages.getMessage(e.getTesto());
			if (testo == null)
				testo = e.getTesto();
			if (!ValidazioneDati.isFilled(testo))
				testo = e.getName();
			String suffisso = e.getSuffissoTesto();
			if (!!ValidazioneDati.isFilled(suffisso))
				testo += suffisso;
			if (e.isHref()) {
				String descrizione = messages.getMessage(e.getDescrizione());
				if (!ValidazioneDati.isFilled(descrizione))
					descrizione = e.getName();

				String fieldName = Navigation.NAVIGATIONBAR_SUBMIT + "-" + e.getUniqueId();
				out.append("<input type=\"submit\" name=\"" + fieldName + "\"");
				out.append(" value=\"" + testo + "\" title=\""
						+ descrizione + "\"");
				out.append(" class=\"" + this.buttonClass + "\" />");

			} else
				out.append("<span>" + testo + "</span>");

			if (!e.isLast())
				out.append(SEPARATORE);
		}
		out.append("</noscript>");
		return out.toString();
	}

	private String renderTagWithJavaScript(List<NavigationElement> elements, MessageResources messages) {

		StringBuffer out = new StringBuffer();

		for (NavigationElement e : elements) {

			String testo = messages.getMessage(e.getTesto());
			if (testo == null)
				testo = e.getTesto();
			if (!ValidazioneDati.isFilled(testo))
				testo = e.getName();
			String suffisso = e.getSuffissoTesto();
			if (!!ValidazioneDati.isFilled(suffisso))
				testo += suffisso;
			if (e.isHref()) {
				String descrizione = messages.getMessage(e.getDescrizione());
				if (!ValidazioneDati.isFilled(descrizione))
					descrizione = ValidazioneDati.isFilled(e.getDescrizione()) ? e.getDescrizione() : e.getName();

				String fieldName = Navigation.NAVIGATIONBAR_SUBMIT + "-" + e.getUniqueId();
				String link = "javascript:navigate('" + fieldName + "', '"
						+ Integer.toHexString(e.hashCode()) + "');";

				out.append("<input type=\"hidden\" name=\"")
					.append(fieldName)
					.append("\" value=\"").append(Navigation.NULL)
					.append("\" />");
				out.append("<a href=\"").append(link).append("\" title=\"").append(descrizione).append("\">");
				out.append(testo + "</a>");

			} else
				out.append("<span>" + testo + "</span>");

			if (!e.isLast())
				out.append(SEPARATORE);
		}

		String output = "<script type=\"text/javascript\">" + "document.write(\""
				+ escape(out.toString()) + "\");" + "</script>";
		return output;

	}

	private String escape(String value) {
		if (!ValidazioneDati.isFilled(value))
			return "";
		String replaceAll = value.replaceAll("\"", "\\\\\"");
		return replaceAll;
	}

	private boolean oldBar() {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = request.getSession();
		String test = request.getParameter(OLDBAR);
		if (test != null ) {
			if (test.equalsIgnoreCase("S") || test.equalsIgnoreCase("Y"))
				session.setAttribute(OLDBAR, "xxxxxxxxx");
			else
				session.removeAttribute(OLDBAR);
		}

		String test2 = (String) session.getAttribute(OLDBAR);
		if (test2 == null)
			return false;

		try {
			JspWriter out = pageContext.getOut();
			out.write("<div id=\"" + divId + "\">");
			NavigationBarTag oldBar = new NavigationBarTag();
			oldBar.setPageContext(pageContext);
			oldBar.setParent(this.getParent());
			oldBar.doStartTag();
			out.write("</div>");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	protected String renderFormStartElement() {
		String tag = super.renderFormStartElement();

			if (bar && !oldBar() )
				tag += this.renderNavigationBar();

		return tag;
	}

	public String getButtonClass() {
		return buttonClass;
	}

	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}

}
