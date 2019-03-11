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
import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * ExceptionLogTag.java
 * 20/apr/07
 * @author almaviva
 */
public class ExceptionLogTag extends TagSupport {

	private static final long serialVersionUID = 610639450732796610L;
	public static final String EXCEPTION_LOG_TAG = "it.iccu.sbn.web.tags.ExceptionLogTag";

	@Override
	public int doStartTag() throws JspException {

		Throwable t = (Throwable) this.pageContext.getRequest().getAttribute(EXCEPTION_LOG_TAG);
		if (t == null)
			return EVAL_PAGE;

		StringWriter log = new StringWriter();
		PrintWriter s = new PrintWriter(log, true);
		t.printStackTrace(s);

		JspWriter out = this.pageContext.getOut();
		StringBuffer buf = new StringBuffer();

		buf.append("<!--\n");
		buf.append("Log errore creato il: ");
		Calendar calendar = Calendar.getInstance();
		buf.append(calendar.getTime().toString());
		buf.append("\n");
		buf.append(log.toString());
		buf.append("--!>\n");

		try {
			out.write(buf.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}



}
