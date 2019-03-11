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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.bean.WriteTag;


public class NavigationLinkWriteTag extends WriteTag {

	private static final long serialVersionUID = 8359571201307127865L;
	private static final String OBJECT_XID = "key\\{(.+?)\\}";
	private Pattern pattern = Pattern.compile(OBJECT_XID, Pattern.CASE_INSENSITIVE);
	private String title;
	private String keyProperty;
	private String buttonKey;
	private String linkStyleId = "sintButtonLinkDefault";
	private String checkAttivita;
	private boolean disabled = false;

	@Override
	protected String formatValue(Object valueToFormat) throws JspException {
		String value = super.formatValue(valueToFormat);
		this.filter = false;
		return this.createXidLinks(value);
	}

	private String renderUrl(NavigationElement element, String propertyValue, String value) throws JspException {

		StringBuffer out = new StringBuffer();

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		boolean render = !disabled && doCheckAttivita(request, element);
		if (!render)
			return value;

		out.append("\n<a name=\"" + LinkableTagUtils.ANCHOR_PREFIX + propertyValue + "\"></a>");
		element.getLinkButtonProperties().add(this.keyProperty);
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		String myPath = request.getContextPath()
				+ element.getMapping().getPath()
				+ ".do?"
				+ LinkableTagUtils.getUniqueButtonName(element, propertyValue, keyProperty,
						this.message(null, buttonKey) );

		myPath += "&" + LinkableTagUtils.FORM_TARGET_PARAM + "=" + element.getUniqueId();
		String token = LinkableTagUtils.getTokenUrl(request);
		if (token != null)
			myPath += token;
		out.append("\n<a class=\"" + this.linkStyleId + "\" href=\"" + response.encodeURL(myPath) + "\"" );
		if (title != null)
			out.append(" title=\"" + title + "\"");
		out.append(">" + value + "</a>");
		return out.toString();
	}


	private String createXidLinks(String value) throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		NavigationElement element = Navigation.getInstance(request).getCache().getCurrentElement();
		String tmp = value;

		while (true) {
			Matcher m = this.pattern.matcher(tmp);
			if (!m.find()) break;

			String key = m.group().substring(4, m.group().length() - 1);
			String[] tokens = key.split("\\|");
			if (tokens.length > 1)
				tmp = m.replaceFirst( this.renderUrl(element, tokens[0], tokens[1]) );
			else
				tmp = m.replaceFirst( this.renderUrl(element, key, key) );
		}
		return tmp;
	}

	private boolean doCheckAttivita(HttpServletRequest request, NavigationElement e) {

		if (ValidazioneDati.strIsNull(checkAttivita))
			return true;

		return LinkableTagUtils.checkAttivita(request, false, checkAttivita);
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	protected String message(String literal, String key) throws JspException {
		if (literal != null)
			if (key != null) {
				JspException e = new JspException(messages
						.getMessage("common.both"));
				TagUtils.getInstance().saveException(super.pageContext, e);
				throw e;
			} else {
				return literal;
			}
		if (key != null)
			return TagUtils.getInstance().message(super.pageContext,
					getBundle(), getLocale(), key);
		else
			return null;
	}

	public String getButtonKey() {
		return buttonKey;
	}

	public void setButtonKey(String key) {
		this.buttonKey = key;
	}

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}

	public String getLinkStyleId() {
		return linkStyleId;
	}

	public void setLinkStyleId(String linkStyleId) {
		this.linkStyleId = linkStyleId;
	}

	public String getCheckAttivita() {
		return checkAttivita;
	}

	public void setCheckAttivita(String checkAttivita) {
		this.checkAttivita = checkAttivita;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
