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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CalendarTag extends TagSupport {

	private static final long serialVersionUID = 2329651213245895677L;
	private String textField;

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

        try {
            out.println("<script type='text/javascript'>");
            out.println("<!--");
            out.print("  document.write(\"<input type='image' id='");
            out.print(getTextField());
            out.print("Button' alt='' src='");
            out.print(req.getContextPath());
            out.print("/images/calendar/calendario.gif' ");
            out.println("title='Seleziona la data' />\");");
            out.print("  Calendar.setup({");
            out.print("inputField : '");
            out.print(getTextField());
            out.print("', button : '");
            out.print(getTextField());
            out.print("Button', ");
            out.print("ifFormat : '%d/%m/%Y', ");
            out.print("firstDay : 1, ");
            out.print("weekNumbers : false, ");
            out.print("cache : true");
            out.println("});");
            out.println("// -->");
            out.println("</script>");

        } catch (IOException e) {
        }
        return 0;
    }

    public int doEndTag() throws JspException {
        return 0;
    }
}
