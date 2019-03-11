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
package it.iccu.sbn.web.actionforms.servizi.calendario;

import it.iccu.sbn.ejb.vo.servizi.calendario.FasciaVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.IntervalloVO;

import java.util.List;

import org.joda.time.DateTimeConstants;

public class IntervalloForm extends CalendarioForm {


	private static final long serialVersionUID = -2173861162423892650L;

	private IntervalloVO intervallo = new IntervalloVO();

	private String dateEscluse;

	private String action_index;

	private FasciaVO getFascia(List<FasciaVO> fasce, int index) {
		while (index >= fasce.size()) {
			fasce.add(new FasciaVO());
		}
		return fasce.get(index);
	}

	public List<FasciaVO> getLun() {
		return intervallo.getFasce().get(DateTimeConstants.MONDAY);
	}

	public List<FasciaVO> getMar() {
		return intervallo.getFasce().get(DateTimeConstants.TUESDAY);
	}

	public List<FasciaVO> getMer() {
		return intervallo.getFasce().get(DateTimeConstants.WEDNESDAY);
	}

	public List<FasciaVO> getGio() {
		return intervallo.getFasce().get(DateTimeConstants.THURSDAY);
	}

	public List<FasciaVO> getVen() {
		return intervallo.getFasce().get(DateTimeConstants.FRIDAY);
	}

	public List<FasciaVO> getSab() {
		return intervallo.getFasce().get(DateTimeConstants.SATURDAY);
	}

	public List<FasciaVO> getDom() {
		return intervallo.getFasce().get(DateTimeConstants.SUNDAY);
	}

	public String getDateEscluse() {
		return dateEscluse;
	}

	public void setDateEscluse(String dateEscluse) {
		this.dateEscluse = dateEscluse;
	}

	public String getAction_index() {
		return action_index;
	}

	public void setAction_index(String action_index) {
		this.action_index = action_index;
	}

	public IntervalloVO getIntervallo() {
		return intervallo;
	}

	public void setIntervallo(IntervalloVO intervallo) {
		this.intervallo = intervallo;
	}

	public FasciaVO getFasciaLun(int index) {
		return getFascia(getLun(), index);
	}

	public FasciaVO getFasciaMar(int index) {
		return getFascia(getMar(), index);
	}

	public FasciaVO getFasciaMer(int index) {
		return getFascia(getMer(), index);
	}

	public FasciaVO getFasciaGio(int index) {
		return getFascia(getGio(), index);
	}

	public FasciaVO getFasciaVen(int index) {
		return getFascia(getVen(), index);
	}

	public FasciaVO getFasciaSab(int index) {
		return getFascia(getSab(), index);
	}

	public FasciaVO getFasciaDom(int index) {
		return getFascia(getDom(), index);
	}
}
