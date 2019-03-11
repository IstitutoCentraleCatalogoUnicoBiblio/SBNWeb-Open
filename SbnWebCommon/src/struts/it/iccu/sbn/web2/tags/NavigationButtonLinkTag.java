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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseHandlerTag;

/**
 * NavigationButtonLinkTag.java 04/lug/07
 *
 * @author almaviva
 */
public class NavigationButtonLinkTag extends BaseHandlerTag {

	private static final long serialVersionUID = 5855206415376574986L;

	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";

	private String buttonClass = "navBtn";
	private String index;
	private String styleId = "sintButtonLinkDefault";
	private String name = null;
	private String title = null;
	private String value = null;
	private String key = null;
	private String property = null;
	private boolean disabled = false;
	private String checkAttivita;
	private boolean submit = false;

	//almaviva5_20160919
	private boolean withAnchor = true;

	private String getProgressivo(String beanName, String propertyName) throws JspException {

		String progressivoValue = null;
		try {
			progressivoValue = this.lookupProperty(beanName, propertyName);

		} catch (JspException e) {

			TagUtils tag = TagUtils.getInstance();
			Object tmp = tag.lookup(pageContext, propertyName, null);
			if (tmp instanceof Integer)
				return String.valueOf(tmp);
			else
				return (String) tmp;
		}

		return progressivoValue;
	}


	@Override
	public int doStartTag() throws JspException {

		JspWriter out = pageContext.getOut();
		try {

			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			NavigationElement currentElement = Navigation.getInstance(request).getCache().getCurrentElement();
			currentElement.getLinkButtonProperties().add(this.property);

			if (name == null)
				name = FORM_NAME;
			String progressivoValue = this.getProgressivo(name, this.index);
			String value = this.lookupProperty(name, this.value);
			//almaviva5_20111221 segnalazione TO0: decodifica entità HTML
			String filtered = TagUtils.getInstance().filter(value);
			this.title = this.message(null, this.key);

			//link anchor
			if (isWithAnchor())
				out.write("\n<a name=\"" + LinkableTagUtils.ANCHOR_PREFIX + progressivoValue + "\"></a>");

			if (disabled || !doCheckAttivita(request, currentElement) ) {
				out.write("\n<span>");
				out.write(filtered);
				out.write("</span>");
				return EVAL_PAGE;
			}

			//almaviva5_20101011
			if (submit)
				out.write(renderSubmit(currentElement, progressivoValue, filtered));
			else
				out.write(renderUrl(currentElement, progressivoValue, filtered));

			return EVAL_PAGE;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private boolean doCheckAttivita(HttpServletRequest request, NavigationElement e) {

		if (ValidazioneDati.strIsNull(checkAttivita))
			return true;

		return LinkableTagUtils.checkAttivita(request, false, checkAttivita);
	}


	private String escape(String value) {
		if (ValidazioneDati.strIsNull(value))
			return "";
		String replaceAll = value.replaceAll("\"", "\\\\\"");
		return replaceAll;
	}


	private String renderSubmit(NavigationElement element, String propertyValue, String value) throws IOException, JspException {

		StringBuffer buf = new StringBuffer();
		buf.append(renderTagWithoutJavaScript(element, propertyValue, value));
		buf.append(renderTagWithJavaScript(element, propertyValue, value));
		return buf.toString();
	}

	private String renderTagWithJavaScript(NavigationElement e,	String propertyValue, String value) throws JspException {

		StringBuffer buf = new StringBuffer();

		String fieldName = LinkableTagUtils.SINTETICA_LINK_SUBMIT + "-" + IdGenerator.getId();
		String fieldValue = LinkableTagUtils.getUniqueLinkableValue(e, propertyValue, property, this.message(null, key) );
		String link = "javascript:navigate('" + fieldName + "', '" + ValidazioneDati.dumpBytes(fieldValue.getBytes()) + "');";

		buf.append("<input type=\"hidden\" name=\"").append(fieldName).append("\" value=\"").append(LinkableTagUtils.SINTETICA_LINK_NULL).append("\" />");
		buf.append("<a class=\"").append(this.styleId).append("\" href=\"");
		buf.append(link).append("\" title=\"").append(title).append("\">");
		buf.append(value).append("</a>");

		String output = "<script type=\"text/javascript\">" + "document.write(\""
				+ escape(buf.toString()) + "\");" + "</script>";
		return output;
	}


	private String renderTagWithoutJavaScript(NavigationElement e, String propertyValue, String value) throws IOException, JspException {
		StringBuffer out = new StringBuffer();
		out.append("<noscript>");
		out.append(renderUrl(e, propertyValue, value));
		out.append("</noscript>");
		return out.toString();
	}


	private String renderUrl(NavigationElement element, String propertyValue, String value) throws IOException, JspException {

		StringBuffer buf = new StringBuffer();
		buf.append("<a class=\"").append(this.styleId).append("\"");

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		String myPath = request.getContextPath()
				+ element.getMapping().getPath()
				+ ".do?"
				+ LinkableTagUtils.getUniqueButtonName(element, propertyValue, property,
						this.message(null, key) );
		myPath += "&" + LinkableTagUtils.FORM_TARGET_PARAM + "=" + element.getUniqueId();

		String token = LinkableTagUtils.getTokenUrl(request);
		if (token != null)
			myPath += token;
		buf.append(" href=\"" + response.encodeURL(myPath) + "\"" );
		if (title != null)
			buf.append(" title=\"" + title + "\"");
		buf.append(">" + value + "</a>");
		return buf.toString();
	}

	public String getIndex() {
		return index;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getProperty() {
		return property;
	}

	public String getStyleId() {
		return styleId;
	}

	public String getTitle() {
		return title;
	}

	public String getValue() {
		return value;
	}

	public void setIndex(String progressivo) {
		this.index = progressivo;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setStyleId(String styleId) {
		if (styleId != null && !styleId.trim().equals(""))
			this.styleId = styleId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getCheckAttivita() {
		return checkAttivita;
	}

	public void setCheckAttivita(String checkAttivita) {
		this.checkAttivita = checkAttivita;
	}


	public boolean isSubmit() {
		return submit;
	}


	public void setSubmit(boolean submit) {
		this.submit = submit;
	}


	public String getButtonClass() {
		return buttonClass;
	}


	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}


	public boolean isWithAnchor() {
		return withAnchor;
	}


	public void setWithAnchor(boolean withAnchor) {
		this.withAnchor = withAnchor;
	}

}
