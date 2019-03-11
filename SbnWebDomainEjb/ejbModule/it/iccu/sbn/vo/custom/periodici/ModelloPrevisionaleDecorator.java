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
package it.iccu.sbn.vo.custom.periodici;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.GiornoMeseVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;

import java.util.Iterator;
import java.util.List;

public class ModelloPrevisionaleDecorator extends ModelloPrevisionaleVO {

	private static final long serialVersionUID = 7201425642845296000L;

	private List<ComboVO> dateEscluse;
	private List<ComboVO> dateIncluse;
	private Integer[] giorniEsclusi;
	private Integer[] giorniInclusi;


	private static final List<ComboVO> fillElencoDate(List<GiornoMeseVO> date) {

		List<ComboVO> output = ValidazioneDati.emptyList();

		if (!isFilled(date))
			return output;

		Iterator<GiornoMeseVO> i = date.iterator();
		while (i.hasNext())
			output.add(new ComboVO(i.next().toString(), "") );

		return output;
	}

	public ModelloPrevisionaleDecorator(ModelloPrevisionaleVO modello) {
		copyCommonProperties(this, modello);

		this.dateEscluse = fillElencoDate(listaEscludiDate);
		this.dateIncluse = fillElencoDate(listaIncludiDate);
		this.giorniEsclusi = isFilled(listaEscludiGiorni) ? listaEscludiGiorni.toArray(giorniEsclusi) : null;
		this.giorniInclusi = isFilled(listaIncludiGiorni) ? listaIncludiGiorni.toArray(giorniInclusi) : null;
	}

	public Integer[] getGiorniEsclusi() {
		return giorniEsclusi;
	}

	public void setGiorniEsclusi(Integer[] giorniEsclusi) {
		this.giorniEsclusi = giorniEsclusi;
	}

	public List<ComboVO> getDateEscluse() {
		return dateEscluse;
	}

	public void setDateEscluse(List<ComboVO> dateEscluse) {
		this.dateEscluse = dateEscluse;
	}

	public List<ComboVO> getDateIncluse() {
		return dateIncluse;
	}

	public void setDateIncluse(List<ComboVO> dateIncluse) {
		this.dateIncluse = dateIncluse;
	}

	public Integer[] getGiorniInclusi() {
		return giorniInclusi;
	}

	public void setGiorniInclusi(Integer[] giorniInclusi) {
		this.giorniInclusi = giorniInclusi;
	}

}
