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

import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelloCalendarioVO extends CalendarioVO {

	private static final long serialVersionUID = 9079138428665861683L;

	private SalaVO sala;
	private String cd_cat_mediazione;

	private List<IntervalloVO> intervalli = new ArrayList<IntervalloVO>();
	public ModelloCalendarioVO(ModelloCalendarioVO mc) {
		super(mc);
		this.cd_cat_mediazione = mc.cd_cat_mediazione;
		this.tipo = mc.tipo;
	}

	public ModelloCalendarioVO() {
		super();
	}

	public List<IntervalloVO> getIntervalli() {
		return intervalli;
	}

	public void setIntervalli(List<IntervalloVO> intervalli) {
		this.intervalli = intervalli;
	}

	public SalaVO getSala() {
		return sala;
	}

	public void setSala(SalaVO sala) {
		this.sala = sala;
	}

	public String getCd_cat_mediazione() {
		return cd_cat_mediazione;
	}

	public void setCd_cat_mediazione(String cd_cat_mediazione) {
		this.cd_cat_mediazione = cd_cat_mediazione;
	}

	public void addIntervallo(IntervalloVO i) {
		intervalli.add(i);
		Collections.sort(intervalli, IntervalloVO.ORDINAMENTO_INTERVALLO);
		int size = intervalli.size();
		for (int idx = 0; idx < size; idx++)
			intervalli.get(idx).setId(idx + 1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cd_cat_mediazione == null) ? 0 : cd_cat_mediazione.hashCode());
		result = prime * result + ((intervalli == null) ? 0 : intervalli.hashCode());
		result = prime * result + ((sala == null) ? 0 : sala.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ModelloCalendarioVO other = (ModelloCalendarioVO) obj;
		if (cd_cat_mediazione == null) {
			if (other.cd_cat_mediazione != null) {
				return false;
			}
		} else if (!cd_cat_mediazione.equals(other.cd_cat_mediazione)) {
			return false;
		}
		if (intervalli == null) {
			if (other.intervalli != null) {
				return false;
			}
		} else if (!intervalli.equals(other.intervalli)) {
			return false;
		}
		if (sala == null) {
			if (other.sala != null) {
				return false;
			}
		} else if (!sala.equals(other.sala)) {
			return false;
		}
		return true;
	}

}
