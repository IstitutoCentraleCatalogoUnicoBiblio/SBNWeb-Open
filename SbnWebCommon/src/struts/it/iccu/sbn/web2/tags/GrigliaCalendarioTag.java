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
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.GiornoVO.StatoGiorno;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseHandlerTag;
import org.joda.time.LocalDate;

public class GrigliaCalendarioTag extends BaseHandlerTag {

	private static final long serialVersionUID = 2506833137638220155L;

	// stili da calendario.css
	private static final String STYLE_DAY_TODAY = "oggi";
	private static final String STYLE_DAY_HOLIDAY = "festivo";
	private static final String STYLE_DAY_OPENED = "aperto";
	private static final String STYLE_DAY_CLOSED = "chiuso";
	private static final String STYLE_DAY = "giorno";

	private static final String FORM_NAME = "org.apache.struts.taglib.html.BEAN";

	//private static final SimpleDateFormat _formatter = new SimpleDateFormat("EEE dd/MM/yyyy");
	static SimpleDateFormat _format_dayName = new SimpleDateFormat("EEEE");
	static SimpleDateFormat _formatDayNumber = new SimpleDateFormat("d");

	private String name;
	private Date inizio;
	private Date fine;
	private String target;

	private String giorni;
	private List<GiornoVO> days;
	private int daysCounter;
	private int totalDays;

	private LocalDate now;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getInizio() {
		return inizio;
	}

	public void setInizio(Date inizio) {
		this.inizio = inizio;
	}

	public Date getFine() {
		return fine;
	}

	public void setFine(Date fine) {
		this.fine = fine;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getGiorni() {
		return giorni;
	}

	public void setGiorni(String giorni) {
		this.giorni = giorni;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		try {
			if (ValidazioneDati.isFilled(name))
				name = FORM_NAME;

			now = LocalDate.now();
			days = (List<GiornoVO>) myLookupProperty(name, giorni);
			totalDays = ValidazioneDati.size(days);

			return renderGriglia();

		} catch (JspException e) {
			TagUtils.getInstance().saveException(super.pageContext, e);
			throw e;
		} catch (IOException e) {
			TagUtils.getInstance().saveException(super.pageContext, e);
			e.printStackTrace();
			throw new JspException(e);
		}
	}

	@Override
	public int doEndTag() throws JspException {
		now = null;
		days = null;
		daysCounter = 0;
		totalDays = 0;
		return super.doEndTag();
	}

	private int renderGriglia() throws IOException {
		JspWriter out = this.pageContext.getOut();

		out.write("<table class=\"calendario\">");
		while (daysCounter < totalDays) {
			renderSettimana();
		}
		out.write("</table>");

		return EVAL_PAGE;
	}

	private void renderSettimana() throws IOException {
		JspWriter out = this.pageContext.getOut();
		GiornoVO first = ValidazioneDati.first(days);
		out.write("<tr class=\"settimana\">");
		for (int day = 1; day < 8; day++) {
			//check primo giorno
			if (daysCounter == 0) {
				LocalDate firstDay = LocalDate.fromDateFields(first.getDate());
				if (firstDay.getDayOfWeek() != day) {	// 1 == MONDAY
					renderGiornoVuoto();
					continue;
				}
			}

			if (daysCounter == totalDays) {
				renderGiornoVuoto();
				continue;
			}

			GiornoVO g = days.get(daysCounter);
			renderGiorno(g);
			daysCounter++;
		}
		out.write("</tr>");
	}

	private void renderGiornoVuoto() throws IOException {
		JspWriter out = this.pageContext.getOut();
		StringBuffer style = new StringBuffer(64);
		style.append(STYLE_DAY);

		out.append("<td class=\"").append(style.toString()).append("\">");
		out.append("</td>");
	}

	void renderGiorno_Old(GiornoVO g) throws IOException {
		JspWriter out = this.pageContext.getOut();
		String style = buildCssStyle(g);

		out.append("<td class=\"").append(style).append("\"");
		if (g.getStato() == StatoGiorno.DISPONIBILE)
			out.append(" onclick=\"javascript:validateSubmit('").append(target).append("', '")
					.append(String.valueOf(daysCounter)).append("');\"");

		out.append(">");
		out.append("<span class=\"day-header\">");
		//out.append(_formatter.format(g.getDate()));
		out.append("</span>");
		out.append("<span class=\"stato\">");
		if (g.getStato() == StatoGiorno.DISPONIBILE) {
			//out.append("<h2>").append(String.format("%d/%d", g.getSlotDisponibili(), g.getSlotTotali())).append("</h2>");
		}

		out.append("</span>");
		out.append("</td>");
	}

	private void renderGiorno(GiornoVO g) throws IOException {
		JspWriter out = this.pageContext.getOut();
		String style = buildCssStyle(g);

		out.append("<td class=\"").append(style).append("\"");
		if (g.getStato() == StatoGiorno.DISPONIBILE)
			out.append(" onclick=\"javascript:validateSubmit('").append(target).append("', '")
					.append(String.valueOf(daysCounter)).append("');\"");

		Date currDate = g.getDate();
		boolean isToday = LocalDate.fromDateFields(currDate).isEqual(now);
		if (isToday)
			out.append(" id=\"today\"");
		out.append(">");
		out.append("<span class=\"day-header\">");
		out.append(_format_dayName.format(currDate));
		out.append("</span>");
		out.append("<span class=\"stato\">");

		out.append("<h2>").append(_formatDayNumber.format(currDate)).append("</h2>");

		out.append("</span>");
		out.append("</td>");
	}

	private String buildCssStyle(GiornoVO g) {
		StringBuffer style = new StringBuffer(64);
		style.append(STYLE_DAY);

		boolean isToday = LocalDate.fromDateFields(g.getDate()).isEqual(now);
		if (isToday)
			style.append(' ').append(STYLE_DAY_TODAY);

		switch (g.getStato()) {
		case CHIUSO:
			style.append(' ').append(STYLE_DAY_CLOSED);
			break;
		case DISPONIBILE:
			style.append(' ').append(STYLE_DAY_OPENED);
			break;
		case NON_DISPONIBILE:
			style.append(' ').append(STYLE_DAY_CLOSED);
			break;
		case FESTIVO:
			style.append(' ').append(STYLE_DAY_HOLIDAY);
			break;
		}

		return style.toString();
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
