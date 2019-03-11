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

import it.iccu.sbn.ejb.vo.common.SbnUnicodeKey;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseHandlerTag;

/**
 * BlocchiTag.java 04/lug/07
 *
 * @author almaviva
 */
public class TastieraSbnTag extends BaseHandlerTag {

	private static final long serialVersionUID = 7832227363334799884L;
	private static Log log = LogFactory.getLog(TastieraSbnTag.class);
	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";
	private static final int BUTTONS_PER_ROW = 21;

	private String keys;
	private String buttonClass = "buttonTastiera";
	private String name = FORM_NAME;
	private String inputField;

	public int doStartTag() throws JspException {

		log.debug("render tastiera sbn");

		@SuppressWarnings("unchecked")
		List<SbnUnicodeKey> keyList = (List<SbnUnicodeKey>) this.myLookupProperty(name, keys);

		int size = keyList.size();
		if (keyList == null || size == 0)
			return EVAL_PAGE;

		int rows = (int) Math.ceil((double)size / BUTTONS_PER_ROW);

		JspWriter out = pageContext.getOut();

		try {
			// out.write(buf.toString());
			out.write(this.renderWithScript(keyList, (size - 1), rows));
			out.write(this.renderWithoutScript(keyList, (size - 1), rows));
		} catch (IOException e) {
			e.printStackTrace();
			return EVAL_PAGE;
		}
		return EVAL_PAGE;
	}

	private String renderWithoutScript(List<SbnUnicodeKey> keyList, int maxIdx, int rows) {
		StringBuffer buf = new StringBuffer();

		buf.append("<noscript>");
		buf.append("<table width=\"100%\" border=\"0\">");
		int idx = -1;
		for (int r = 0; r < rows; r++) {
			buf.append("<tr>");
			for (int b = 0; b < BUTTONS_PER_ROW; b++) {
				buf.append("<td>");
				idx++;
				if (idx > maxIdx) break;
				SbnUnicodeKey key = keyList.get(idx);
				// String entity = new String(key.getKey(), "UTF8");
				buf.append("\n<span");
				buf.append(" title=\"" + key.getDescrizione() + "\"");
				if (!strIsNull(this.buttonClass))
					buf.append(" class=\"" + this.buttonClass + "\"");
				buf.append(">" + key.getKey());
				buf.append("</span");
				buf.append("</td>");

			}
			buf.append("</tr>");
		}
		buf.append("</table>");
		buf.append("</noscript>");

		return buf.toString();

	}

	private String renderWithScript(List<SbnUnicodeKey> keyList, int maxIdx, int rows) {
		StringBuffer buf = new StringBuffer();

		buf.append("<script language=\"JavaScript\">");
		buf.append("document.write(\"");
		buf.append("<table width=\\\"100%\\\" border=\\\"0\\\">");
		int idx = -1;
		for (int r = 0; r < rows; r++) {
			buf.append("<tr>");
			for (int b = 0; b < BUTTONS_PER_ROW; b++) {
				buf.append("<td>");
				idx++;
				if (idx > maxIdx) break;
				SbnUnicodeKey key = keyList.get(idx);
				// String entity = new String(key.getKey(), "UTF8");
				buf.append("<button type=\\\"button\\\"");
				buf.append(" title=\\\"" + key.getDescrizione() + "\\\"");
				buf.append(" onclick=\\\"insertAtCursor(" + this.inputField
						+ ", '" + key.getKey() + "');\\\"");
				if (!strIsNull(this.buttonClass))
					buf.append(" class=\\\"" + this.buttonClass + "\\\"");
				buf.append(">" + key.getKey());
				buf.append("</button>");
				buf.append("</td>");

			}
			buf.append("</tr>");
		}
		buf.append("</table>");
		buf.append("\");");
		buf.append("</script>");

		return buf.toString();
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
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

	private static final boolean strIsNull(String value) {
		return (value == null || "".equals(value.trim()));
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getButtonClass() {
		return buttonClass;
	}

	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}

	public String getInputField() {
		return inputField;
	}

	public void setInputField(String inputField) {
		this.inputField = inputField;
	}

}
