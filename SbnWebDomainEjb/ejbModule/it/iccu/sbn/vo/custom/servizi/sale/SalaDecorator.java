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
package it.iccu.sbn.vo.custom.servizi.sale;

import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;

import java.util.ArrayList;
import java.util.List;


public class SalaDecorator extends SalaVO {

	private static final long serialVersionUID = 8349515130189615967L;

	public SalaDecorator(SalaVO s) {
		super(s);
		List<GruppoPostiVO> decorated = new ArrayList<GruppoPostiVO>();
		List<GruppoPostiVO> gruppi = s.getGruppi();
		for (GruppoPostiVO gp : gruppi)
			decorated.add(new GruppoPostiDecorator(gp));

		this.setGruppi(decorated);

	}

	private static class Duration {

		int hours;
		int mins;

		public Duration(int duration) {
			addMinutes(duration);
		}

		void addMinutes(int value) {
			int tmp = mins;
			tmp += value;
			if (tmp > 59) {
				int h = tmp / 60;
				hours += h;
				mins += (tmp - (60 * h));
			} else
				mins = tmp;

		}

	}

	private String formattaMinuti(Duration d) {
		if (d.mins == 0)
			return "";
		if (d.mins == 1)
			return "un minuto";
		return d.mins + " minuti";
	}


	public String getDurataPrenotazione() {
		int duration = getDurataFascia() * getMaxFascePrenotazione();
		Duration d = new Duration(duration);

		StringBuilder buf = new StringBuilder(32);
		//buf.append(formatOrario)

		if (d.hours < 1) {
			return formattaMinuti(d);
		}

		if (d.hours == 1)
			buf.append("un'ora");
		else
			buf.append(d.hours).append(" ore");

		String minuti = formattaMinuti(d);
		if (isFilled(minuti))
			buf.append(" e ").append(minuti);
		return buf.toString();
	}

}
