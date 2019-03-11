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

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.periodici.previsionale.GiornoMeseVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTimeConstants;

public class IntervalloVO extends CalendarioVO {

	private static final long serialVersionUID = -7722167682196268965L;

	private Map<Integer, List<FasciaVO>> fasce = new HashMap<Integer, List<FasciaVO>>();

	{
		clear();
	}

	public static final Comparator<IntervalloVO> ORDINAMENTO_INTERVALLO = new Comparator<IntervalloVO>() {
		public int compare(IntervalloVO i1, IntervalloVO i2) {
			int cmp = i1.base && !i2.base ? -1 : !i1.base && i2.base ? 1 : 0;
			cmp = cmp != 0 ? cmp : i1.absolute && !i2.absolute ? 1 : !i1.absolute && i2.absolute ? -1 : 0;
			cmp = cmp != 0 ? cmp : i1.getInizio().compareTo(i2.getInizio());
			cmp = cmp != 0 ? cmp : i1.getFine().compareTo(i2.getFine());
			return cmp;
		}
	};

	protected List<GiornoMeseVO> dateEscluse = ValidazioneDati.emptyList();
	private boolean escludiPasqua = true;
	private boolean base;
	private boolean absolute = false;

	public IntervalloVO(IntervalloVO i) {
		super(i);
		this.escludiPasqua = i.escludiPasqua;
		this.base = i.base;
		this.dateEscluse = new ArrayList<GiornoMeseVO>(i.dateEscluse);
		this.absolute = i.absolute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (base ? 1231 : 1237);
		result = prime * result + ((dateEscluse == null) ? 0 : dateEscluse.hashCode());
		result = prime * result + (escludiPasqua ? 1231 : 1237);
		result = prime * result + ((fasce == null) ? 0 : fasce.hashCode());
		return result;
	}

	public IntervalloVO(Date inizio, Date fine) {
		super(inizio, fine);
	}

	public IntervalloVO() {
		super();
	}

	public Map<Integer, List<FasciaVO>> getFasce() {
		return fasce;
	}

	public void setFasce(Map<Integer, List<FasciaVO>> fasce) {
		this.fasce = fasce;
	}

	public List<GiornoMeseVO> getDateEscluse() {
		return dateEscluse;
	}

	public void setDateEscluse(List<GiornoMeseVO> listaEscludiDate) {
		this.dateEscluse = listaEscludiDate;
	}

	public boolean isEscludiPasqua() {
		return escludiPasqua;
	}

	public void setEscludiPasqua(boolean escludiPasqua) {
		this.escludiPasqua = escludiPasqua;
	}

	public boolean isBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}

	public boolean isNuovo() {
		return getId() == 0;
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
		IntervalloVO other = (IntervalloVO) obj;
		if (base != other.base) {
			return false;
		}
		if (dateEscluse == null) {
			if (other.dateEscluse != null) {
				return false;
			}
		} else if (!dateEscluse.equals(other.dateEscluse)) {
			return false;
		}
		if (escludiPasqua != other.escludiPasqua) {
			return false;
		}
		if (fasce == null) {
			if (other.fasce != null) {
				return false;
			}
		} else if (!fasce.equals(other.fasce)) {
			return false;
		}
		return true;
	}

	public boolean isAbsolute() {
		return absolute;
	}

	public void setAbsolute(boolean exactDate) {
		this.absolute = exactDate;
	}

	public boolean contains(Date date) {
		return DateUtil.between(date, getInizio(), getFine());
	}

	public void clear() {
		//fasce orarie per ogni giorno
		fasce.clear();
		fasce.put(DateTimeConstants.MONDAY, new ArrayList<FasciaVO>());
		fasce.put(DateTimeConstants.TUESDAY, new ArrayList<FasciaVO>());
		fasce.put(DateTimeConstants.WEDNESDAY, new ArrayList<FasciaVO>());
		fasce.put(DateTimeConstants.THURSDAY, new ArrayList<FasciaVO>());
		fasce.put(DateTimeConstants.FRIDAY, new ArrayList<FasciaVO>());
		fasce.put(DateTimeConstants.SATURDAY, new ArrayList<FasciaVO>());
		fasce.put(DateTimeConstants.SUNDAY, new ArrayList<FasciaVO>());
	}

}
