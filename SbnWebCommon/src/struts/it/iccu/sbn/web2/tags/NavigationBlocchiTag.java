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

import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.navigation.NavigationBlocchiInfo;
import it.iccu.sbn.web2.navigation.NavigationElement;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.html.BaseHandlerTag;

/**
 * BlocchiTag.java 04/lug/07
 *
 * @author almaviva
 */
public class NavigationBlocchiTag extends BaseHandlerTag {

	private static final long serialVersionUID = -631998524099736504L;
	private static Log log = LogFactory.getLog(NavigationBlocchiTag.class);
	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";

	private String numBlocco;
	private String totBlocchi;
	private String numNotizie;
	private String elementiPerBlocco;
	private String parameter;
	private String livelloRicerca;
	private String styleId = "blocchiDefault";
	private boolean bottom = false;
	private boolean disabled = false;

	private boolean isButtonBlocco(HttpServletRequest request,
			NavigationElement element) throws JspException {

		String param = request.getParameter(LinkableTagUtils.BLOCCO_BUTTON_TOP);
		if (param == null) {
			param = request.getParameter(LinkableTagUtils.BLOCCO_BUTTON_BOTTOM);
			if (param == null)
				return false;
		}

		String message = this.message(null, LinkableTagUtils.BUTTON_BLOCCO);
		if ((message == null || message.startsWith("???")))
			return false;

		return (message.equals(param));
	}

	private String labelLivelloRicerca(String livelloRicerca)
			throws JspException {

		String blocchiLivelloRicerca = this.message(null, "blocchi.livelloRicerca");
		String blocchiIndice = this.message(null, "blocchi.indice");
		String blocchiPolo = this.message(null, "blocchi.polo");
		String blocchiBiblio = this.message(null, "blocchi.biblio");
		String blocchiPoloNew = this.message(null, "blocchi.poloNew");

		String label = "";

		// indice
		if (livelloRicerca.equalsIgnoreCase("I"))
			label = blocchiLivelloRicerca + "&nbsp;" + blocchiIndice;

		// polo
		if (livelloRicerca.equalsIgnoreCase("P"))
			label = blocchiLivelloRicerca + "&nbsp;" + blocchiPolo;

		if (livelloRicerca.equalsIgnoreCase("X"))
			label = blocchiLivelloRicerca + "&nbsp;" + blocchiPoloNew;

		// in biblioteca
		if (label.equals(""))
			label = blocchiLivelloRicerca + "&nbsp;" + blocchiBiblio + "&nbsp;" + livelloRicerca;

		return "<p class=\"" + styleId + "\">" + label + "</p>";

	}

	private boolean resetInfo(NavigationBlocchiInfo info, int numBloccoValue, int totBlocchiValue, int numNotizieValue) {

		return 	(totBlocchiValue != info.getTotBlocchi() ) ||
				(numNotizieValue != info.getNumNotizie() ) ||
				(this.livelloRicerca != null && !this.livelloRicerca.equals(info.getLivelloRicerca()));
	}

	public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Navigation navi = Navigation.getInstance(request);
		NavigationElement currentElement = navi.getCache().getCurrentElement();

		// messaggi parametrizzati
		String buttonBlocco = this.message(null, LinkableTagUtils.BUTTON_BLOCCO);
		String buttonBloccoTitle = this.message(null, "button.blocco.title");
		String blocchiNumeroNotizie = this.message(null, "blocchi.numeroNotizie");
		String blocchiPagina = this.message(null, "blocchi.pagina");
		String caricaPagina = this.message(null, "blocchi.caricaPagina");
		String di = this.message(null, "blocchi.di");

		try {
			// valori numerici delle properties passate in input
			int numBloccoValue = Integer.parseInt(this.lookupProperty(FORM_NAME, this.numBlocco));
			int totBlocchiValue = Integer.parseInt(this.lookupProperty(FORM_NAME, this.totBlocchi));
			int numNotizieValue = Integer.parseInt(this.lookupProperty(FORM_NAME, this.numNotizie));

			// caso limite: se passo da polo a indice nella stessa pagina
			// devo forzare l'aggiornamento dei dati salvati in navigazione
			if (currentElement.getInfoBlocchi() != null && this.resetInfo(currentElement.getInfoBlocchi(), numBloccoValue, totBlocchiValue, numNotizieValue))
				currentElement.setInfoBlocchi(null);

			// Salvo sulla navigazione le informazioni sulla gestione blocchi
			if (currentElement.getInfoBlocchi() == null) {

				int elemBloccoValue = 10;
				if (this.elementiPerBlocco != null) {
					String tmp = this.lookupProperty(FORM_NAME, this.elementiPerBlocco);
					if (tmp != null) elemBloccoValue = Integer.parseInt(tmp);
				} else
					elemBloccoValue = (int)	Math.ceil((double)numNotizieValue / (double)totBlocchiValue);

				NavigationBlocchiInfo blocchiInfo = new NavigationBlocchiInfo(numBloccoValue,
					totBlocchiValue, numNotizieValue, elemBloccoValue,
					this.livelloRicerca, this.numBlocco, this.totBlocchi, this.numNotizie);
				currentElement.setInfoBlocchi(blocchiInfo);
				UserVO utente = navi.getUtente();
				log.info("(userid: " + utente.getUserId() + ") - Impostato step blocchi a " + blocchiInfo.getElementiPerBlocco() +
						" per '" + currentElement.getName() + "'");
			}

			// se non ho premuto il bottone carica blocco devo mantenere la
			// situazione pregressa
			if (!this.isButtonBlocco(request, currentElement) ) {
				NavigationBlocchiInfo info = currentElement.getInfoBlocchi();
				numBloccoValue = info.getNumBlocco();
				totBlocchiValue = info.getTotBlocchi();
				numNotizieValue = info.getNumNotizie();
			}

			if (bottom && totBlocchiValue < 2)
				return EVAL_PAGE;

			//almaviva5_20080125
			if (!bottom) {
				numBlocco = LinkableTagUtils.BLOCCO_VALUE_TOP;
				parameter = LinkableTagUtils.BLOCCO_BUTTON_TOP;
			} else {
				numBlocco = LinkableTagUtils.BLOCCO_VALUE_BOTTOM;
				parameter = LinkableTagUtils.BLOCCO_BUTTON_BOTTOM;
			}

			JspWriter out = pageContext.getOut();

			out.print("<table class=\"" + styleId + "\">");
			// controllo livello di ricerca
			if (livelloRicerca != null) {
				out.print("<tr><td colspan=\"2\">"
						+ this.labelLivelloRicerca(livelloRicerca)
						+ "</td></tr>");
			}

			out.print("<tr>");
			out.print("<td>");
			out.print(blocchiNumeroNotizie + ":&nbsp;");
			out.print("<input  class=\"" + styleId + "\" name=\"" + numNotizie
					+ "\" size=\"5\" value=\"" + numNotizieValue
					+ "\" disabled=\"disabled\" type=\"text\">");
			out.print("</td>");
			if (numNotizieValue > 0) {
				out.print("<td>");
				out.print(blocchiPagina + ":&nbsp;");
				// controllo che il blocco richiesto sia nel range valido
				// memorizzo l'ultimo blocco valido richiesto nella navigazione
				if (numBloccoValue < 1 || numBloccoValue > totBlocchiValue || navi.isFromBar())
					numBloccoValue = currentElement.getInfoBlocchi().getNumBlocco();
				else
					currentElement.getInfoBlocchi().setNumBlocco(numBloccoValue);

				String label = numBloccoValue + " " + di + " " + totBlocchiValue;
				out.print("<input class=\"" + styleId + "\" name=\""
						+ totBlocchi + "\" size=\"" + label.length()
						+ "\" value=\"" + label
						+ "\" disabled=\"disabled\" type=\"text\">");

				if (totBlocchiValue > 1) {
					StringBuilder buf = new StringBuilder(128);
					int buttonId = IdGenerator.getId();
					out.print("&nbsp;&nbsp;&nbsp;" + caricaPagina + ":&nbsp;");
					// seleziono blocco arbitrario (se non bottom)
					if (numBloccoValue < totBlocchiValue) {
						++numBloccoValue;
						buf.setLength(0);
						buf.append("<input class=\"");
						buf.append(styleId);
						buf.append("\" name=\"");
						buf.append(numBlocco);
						buf.append("\" size=\"5\" value=\"");
						buf.append(numBloccoValue);
						buf.append("\" ");
						buf.append("onkeydown=\"submitOnEnter(event, '").append(buttonId).append("')\" ");
						if (disabled)
							buf.append("disabled=\"disabled\" ");
						buf.append("type=\"text\">");
						out.print(buf.toString());
					} else {
						buf.setLength(0);
						buf.append("<input class=\"");
						buf.append(styleId);
						buf.append("\" name=\"");
						buf.append(numBlocco);
						buf.append("\" size=\"5\" ");
						if (disabled)
							buf.append("disabled=\"disabled\" ");
						buf.append("type=\"text\">");
						out.print(buf.toString());
					}
					buf.setLength(0);
					buf.append("<input class=\"");
					buf.append(styleId);
					buf.append("\" name=\"");
					buf.append(parameter);
					buf.append("\" value=\"");
					buf.append(buttonBlocco);
					buf.append("\" " );
					buf.append("id=\"").append(buttonId).append("\" ");
					if (disabled)
						buf.append("disabled=\"disabled\" ");
					buf.append("type=\"submit\" title=\"");
					buf.append(buttonBloccoTitle);
					buf.append("\" >");
					out.print(buf.toString());
				} // totBlocchiValue

				out.print("</td>");

			} // numNotizieValue
			out.print("</tr>");
			out.print("</table>");

		} catch (IOException e) {
			return EVAL_PAGE;
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public String getLivelloRicerca() {
		return livelloRicerca;
	}

	public void setLivelloRicerca(String livelloRicerca) {
		this.livelloRicerca = livelloRicerca;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		if (styleId != null && !styleId.trim().equals(""))
			this.styleId = styleId;
	}

	public String getTotBlocchi() {
		return totBlocchi;
	}

	public String getNumBlocco() {
		return numBlocco;
	}

	public void setNumBlocco(String numBlocco) {
		this.numBlocco = numBlocco;
	}

	public String getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(String numNotizie) {
		this.numNotizie = numNotizie;
	}

	public void setTotBlocchi(String totBlocchi) {
		this.totBlocchi = totBlocchi;
	}

	public String getElementiPerBlocco() {
		return elementiPerBlocco;
	}

	public void setElementiPerBlocco(String elementiPerBlocco) {
		this.elementiPerBlocco = elementiPerBlocco;
	}


	public boolean isBottom() {
		return bottom;
	}


	public void setBottom(boolean visible) {
		this.bottom = visible;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
