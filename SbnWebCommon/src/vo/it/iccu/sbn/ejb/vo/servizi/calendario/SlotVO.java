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
package it.iccu.sbn.ejb.vo.servizi.calendario;

import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

public class SlotVO extends FasciaVO {

	private static final long serialVersionUID = -5862739496724697840L;

	private int slot;
	private String descrizione;
	private boolean active;
	private List<PostoSalaVO> posti = new ArrayList<PostoSalaVO>();

	public SlotVO(int h) {
		this.slot = h;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int s) {
		this.slot = s;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<PostoSalaVO> getPosti() {
		return posti;
	}

	public void setPosti(List<PostoSalaVO> posti) {
		this.posti = posti;
	}

	private String format(LocalTime time) {
		return String.format("%02d:%02d", time.getHourOfDay(), time.getMinuteOfHour() );
	}

	public String getStart() {
		return format(getInizio());
	}

	public String getEnd() {
		return format(getFine());
	}

	public String getLabel() {
		return String.format("%s - %s", format(getInizio()), format(getFine()) );
	}

	@Override
	public boolean isEmpty() {
		return !isFilled(posti);
	}

}
