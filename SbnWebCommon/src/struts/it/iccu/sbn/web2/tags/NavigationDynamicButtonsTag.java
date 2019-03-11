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
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseHandlerTag;

import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.LinkableTagUtils;

public class NavigationDynamicButtonsTag extends BaseHandlerTag {

	private static final long serialVersionUID = 2506833137638220155L;
	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";

	private String buttonClass;
	private String buttonProperty;
	private String buttons = null;
	private boolean enabled = true;

	public NavigationDynamicButtonsTag() {
		this.enabled = true;
	}

	public String getButtonClass() {
		return buttonClass;
	}

	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}

	public String getButtonProperty() {
		return buttonProperty;
	}

	public void setButtonProperty(String buttonProperty) {
		this.buttonProperty = buttonProperty;
	}

	public String getButtons() {
		return buttons;
	}

	public void setButtons(String buttons) {
		this.buttons = buttons;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			if (!this.enabled || this.buttons == null)
				return EVAL_PAGE;

			// se il parameter è null lo sostituisco con quello di default
			if (strIsNull(this.buttonProperty)) {
				HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
				Navigation navi = Navigation.getInstance(request);
				NavigationElement currentElement = navi.getCache().getCurrentElement();
				this.buttonProperty = currentElement.getMapping().getParameter();
			}

			return renderButtons();

		} catch (JspException e) {
			TagUtils.getInstance().saveException(super.pageContext, e);
			throw e;
		} catch (IOException e) {
			TagUtils.getInstance().saveException(super.pageContext, e);
			e.printStackTrace();
			throw new JspException(e);
		}
	}

	private int renderButtons() throws IOException, JspException {

		if (strIsNull(buttons))
			return EVAL_PAGE;

		JspWriter out = this.pageContext.getOut();
		Object property = this.myLookupProperty(FORM_NAME, this.buttons);
		if (property == null)
			return EVAL_PAGE;

		Collection<?> myButtons = null;
		if (property.getClass().isArray())
			myButtons = Arrays.asList((Object[]) property);
		else
			myButtons = (Collection<?>) property;

		for (Object o : myButtons) {

			String buttonKey = (String) o;
			if (!check(buttonKey) )
				continue;

			String buttonValue = LinkableTagUtils.findMessage(pageContext, getLocale(),
					buttonKey, null);

			// render del submit
			out.write("<input type=\"submit\" name=\"" + this.buttonProperty
					+ "\"");
			out.write(" value=\"" + buttonValue + "\"");
			if (!strIsNull(this.buttonClass))
				out.write(" class=\"" + this.buttonClass + "\"");
			out.write(">&nbsp");
		}

		return EVAL_PAGE;
	}

	private static final boolean strIsNull(String value) {
		return (value == null || "".equals(value.trim()));
	}

	private boolean check(String idControllo) throws JspException {

		return LinkableTagUtils.checkAttivita((HttpServletRequest) pageContext.getRequest(), false, idControllo);

	}

	private Object myLookupProperty(String beanName, String property)
			throws JspException {
		Object bean = TagUtils.getInstance().lookup(super.pageContext,
				beanName, null);
		if (bean == null)
			throw new JspException(messages.getMessage("getter.bean", beanName));
		try {
			return PropertyUtils.getProperty(bean, property);
		} catch (IllegalAccessException e) {
			throw new JspException(messages.getMessage("getter.access",
					property, beanName));
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			throw new JspException(messages.getMessage("getter.result",
					property, t.toString()));
		} catch (NoSuchMethodException e) {
			throw new JspException(messages.getMessage("getter.method",
					property, beanName));
		}
	}

}
