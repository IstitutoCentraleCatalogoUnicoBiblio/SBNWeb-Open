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
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts.taglib.html.BaseHandlerTag;

/**
 * Generated tag handler class.
 *
 * @author almaviva
 * @version 2
 */

public class NavigationBarTag extends BaseHandlerTag {

	private static final long serialVersionUID = 7387777037087435203L;
	private static final String SEPARATORE = " -> ";

	@SuppressWarnings("unused")
	private String styleId;


	public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		NavigationCache cache = Navigation.getInstance(request).getCache();
		if (cache.isEmpty())
			return EVAL_PAGE;

		try {
			JspWriter out = pageContext.getOut();
			List<NavigationElement> elements = cache.getLockedElements();
			for (NavigationElement e : elements) {
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				String testo = this.message(null, e.getTesto());
				if (testo == null)
					testo = e.getTesto();
				if (!ValidazioneDati.isFilled(testo))
					testo = e.getName();
				String suffisso = e.getSuffissoTesto();
				if (!!ValidazioneDati.isFilled(suffisso))
					testo += suffisso;
				if (e.isHref()) {
					String descrizione = this.message(null, e.getDescrizione());
					if (!ValidazioneDati.isFilled(descrizione))
						descrizione = e.getName();
					String link = request.getContextPath() + e.getUri()
							+ ".do?"
							+ LinkableTagUtils.FORM_TARGET_PARAM + "=" + e.getUniqueId()
							+ "&navigation=" + e.getPosition();
					String anchor = e.getAnchorId();
					if (!!ValidazioneDati.isFilled(anchor))
						link += "#" + anchor;

					out.write("<a href=\"" + response.encodeURL(link) + "\"");
					out.write(" title=\"" + descrizione + "\">");
					out.write(testo + "</a>");

				} else
					out.write(testo);

				if (!e.isLast())
					out.write(SEPARATORE);
			}

		} catch (Exception e) {
			return EVAL_PAGE;
		} finally {
			cache.unlockElements();
		}

		return EVAL_PAGE;
	}

	/**
	 * Setter for the styleId attribute.
	 */
	public void setStyleId(java.lang.String value) {
		this.styleId = value;
	}

}
