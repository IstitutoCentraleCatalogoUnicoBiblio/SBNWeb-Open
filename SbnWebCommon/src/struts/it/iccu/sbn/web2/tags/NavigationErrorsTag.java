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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.ErrorsTag;

import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.LinkableTagUtils;

public class NavigationErrorsTag extends ErrorsTag {

	private static final long serialVersionUID = 2506833137638220155L;
	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";

	public static final String EXCEPTION_LOG_TAG = "it.iccu.sbn.web.tags.ExceptionLogTag";

	private String buttonClass;
	private String buttonProperty;
	private String buttonBundle;
	private String buttons;
	private boolean enabled;

	public NavigationErrorsTag() {
		this.enabled = false;
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

	public String getButtonBundle() {
		return buttonBundle;
	}

	public void setButtonBundle(String buttonBundle) {
		this.buttonBundle = buttonBundle;
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
			// provo a stampare il log dell'eccezione (se impostata)
			renderExceptionLog();

			// stampo i messaggi come da default
			defaultTag();

			ActionMessages errors = null;
			errors = TagUtils.getInstance().getActionMessages(super.pageContext, name);
			if (!this.enabled || errors == null || errors.isEmpty() || this.buttons == null)
				return EVAL_PAGE;

			// se il parameter è null lo sostituisco con quello di default
			if (strIsNull(this.buttonProperty)) {
				HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
				Navigation navi = Navigation.getInstance(request);
				NavigationElement currentElement = navi.getCache().getCurrentElement();
				this.buttonProperty = currentElement.getMapping().getParameter();
			}

			// se il bundle è null lo sostituisco con quello di default
			if (strIsNull(this.buttonBundle))
				this.buttonBundle = this.bundle;

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

	private void defaultTag() throws JspException {
		ActionMessages errors = null;
		TagUtils instance = TagUtils.getInstance();
		try {
			errors = instance.getActionMessages(super.pageContext, name);
		} catch (JspException e) {
			instance.saveException(super.pageContext, e);
			throw e;
		}
		if (errors == null || errors.isEmpty())
			return;

		boolean headerPresent = instance.present(super.pageContext, bundle, locale, getHeader());
		boolean footerPresent = instance.present(super.pageContext, bundle, locale, getFooter());
		boolean prefixPresent = instance.present(super.pageContext, bundle, locale, getPrefix());
		boolean suffixPresent = instance.present(super.pageContext, bundle, locale, getSuffix());
		StringBuffer results = new StringBuffer();
		boolean headerDone = false;
		String message = null;
		for (Iterator<?> reports = property != null ? errors.get(property) : errors.get(); reports.hasNext();) {
			ActionMessage report = (ActionMessage) reports.next();
			if (!headerDone) {
				if (headerPresent) {
					message = instance.message(super.pageContext, bundle, locale, getHeader());
					results.append(message);
				}
				headerDone = true;
			}
			if (prefixPresent) {
				message = instance.message(super.pageContext, bundle, locale, getPrefix());
				results.append(message);
			}
			if (report.isResource())
				message = LinkableTagUtils.findMessage(super.pageContext, locale, report.getKey(), report.getValues());
			else
				message = report.getKey();
			if (message != null)
				results.append(message);
			if (suffixPresent) {
				message = instance.message(super.pageContext, bundle, locale, getSuffix());
				results.append(message);
			}
		}

		if (headerDone && footerPresent) {
			message = instance.message(super.pageContext, bundle, locale, getFooter());
			results.append(message);
		}

		instance.write(super.pageContext, results.toString());
		return;

	}

	private void renderExceptionLog() throws IOException {

		Throwable t = (Throwable) this.pageContext.getRequest().getAttribute(EXCEPTION_LOG_TAG);
		if (t == null)
			return;

		StringWriter log = new StringWriter();
		PrintWriter s = new PrintWriter(log, true);
		t.printStackTrace(s);

		JspWriter out = this.pageContext.getOut();
		StringBuilder buf = new StringBuilder();

		buf.append("<!--\n");
		buf.append("Log errore creato il: ");
		buf.append(Calendar.getInstance().getTime().toString());
		buf.append("\n");
		buf.append(log.toString());
		buf.append("\n-->\n");

		out.write(buf.toString());
	}

	private int renderButtons() throws IOException, JspException {

		if (strIsNull(buttons))
			return EVAL_PAGE;

		JspWriter out = this.pageContext.getOut();
		Object property = this.lookupProperty(FORM_NAME, this.buttons);
		if (property == null)
			return EVAL_PAGE;

		Collection<?> myButtons = null;
		if (property.getClass().isArray())
			myButtons = Arrays.asList((Object[])property);
		else
			myButtons = (Collection<?>) property;

		if (myButtons.size() > 0)
			out.write("<br>");

		for (Object o : myButtons) {
			String buttonKey = (String) o;
			String buttonValue = LinkableTagUtils.findMessage(pageContext, locale, buttonKey, null);

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

	private Object lookupProperty(String beanName, String property)
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
