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
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.taglib.html.BaseHandlerTag;

/**
 * BlocchiTag.java 04/lug/07
 *
 * @author almaviva
 */
public class LinkTastieraSbnTag extends BaseHandlerTag {

	private static final long serialVersionUID = -6819682763804369400L;
	//private static Log log = LogFactory.getLog(LinkTastieraSbnTag.class);
	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";

	public static final String KEYBOARD_RESPONSE = "it.iccu.sbn.vkbd.response";
	public static final String KEYBOARD_DATA_PARAM = "SBNVKBD";

	private String name = FORM_NAME;
	private String property;
	private int limit = Integer.MAX_VALUE;
	private boolean visible = true;
	private String buttonClass = "buttonLinkTastiera";

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

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int doStartTag() throws JspException {

		if (!visible)
			return EVAL_PAGE;

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Navigation navi = Navigation.getInstance(request);

		JspWriter out = pageContext.getOut();
		StringBuffer buf = new StringBuffer();

		ActionForm form = (ActionForm) request.getAttribute(this.name);
		NavigationElement element = navi.getCache().getElementByForm(form);

		String data =  element.getUniqueId()
			+ LinkableTagUtils.SEPARATORE + this.property
			+ LinkableTagUtils.SEPARATORE + this.limit
			+ LinkableTagUtils.SEPARATORE
			+ (new Integer(navi.getCache().getCurrentPosition())).toString();

		String button = LinkableTagUtils.getVirtualKeyboardButtonName()
			+ LinkableTagUtils.SEPARATORE
			+ ValidazioneDati.dumpBytes(data.getBytes());

		buf.append(this.renderTagWithoutJavaScript(button));
		buf.append(this.renderTagWithJavaScript(request, button));

		try {
			out.write(buf.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return EVAL_PAGE;
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	private static final boolean strIsNull(String value) {
		return (value == null || "".equals(value.trim()));
	}

	private Object renderTagWithoutJavaScript(String button) throws JspException {

		StringBuffer buf = new StringBuffer();

		buf.append("<noscript>");
		buf.append("\n<input type=\"submit\"");
		buf.append(" class=\"" + this.buttonClass + "\"");

		buf.append(" id=\"vkbd-").append(IdGenerator.getId()).append("\"");

		buf.append(" name=\"" + button );
		buf.append("\" value=\"kbd\"");
		buf.append(" tabindex=\"32767\"");
		buf.append(" title=\"").append(this.message(null, ".TastieraSbn.testo")).append("\"");
		buf.append("></input>");
		buf.append("</noscript>");

		return buf.toString();
	}

	private String renderTagWithJavaScript(HttpServletRequest request, String button) throws JspException {

		StringBuffer out = new StringBuffer();

		int id = IdGenerator.getId();
		String link = "javascript:validateSubmit('" + button + "','" + id + "');";

		out.append("<input type=\"hidden\" name=\"" + button + "\" value=\""
				+ LinkableTagUtils.SEPARATORE + "\" />");
		out.append("<a id=\"vkbd-" + id + "\" href=\"" + link + "\" title=\""
				+ this.message(null, ".TastieraSbn.testo") + "\">");
		out.append("<img src=\"" + request.getContextPath()
				+ "/images/vkb.gif\" alt=\""
				+ this.message(null, ".TastieraSbn.testo")
				+ "\" border=\"0\" /></a>");

		String output = "<script type=\"text/javascript\">"
				+ "document.write(\"" + escape(out.toString()) + "\");"
				+ "</script>";

		return output;
	}

	private String escape(String value) {
		if (strIsNull(value))
			return "";
		String replaceAll = value.replaceAll("\"", "\\\\\"");
		return replaceAll;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
