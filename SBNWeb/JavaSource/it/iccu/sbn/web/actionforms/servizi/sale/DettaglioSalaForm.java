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
package it.iccu.sbn.web.actionforms.servizi.sale;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;

import java.util.List;

public class DettaglioSalaForm extends SinteticaSaleForm {


	private static final long serialVersionUID = -846409512084966178L;
	private SalaVO sala;
	private SalaVO salaOld;

	private String action_index;

	private List<TB_CODICI> strumentiMediazione;

	public SalaVO getSala() {
		return sala;
	}

	public void setSala(SalaVO sala) {
		this.sala = sala;
	}

	public SalaVO getSalaOld() {
		return salaOld;
	}

	public void setSalaOld(SalaVO salaOld) {
		this.salaOld = salaOld;
	}

	public GruppoPostiVO getGruppo(int index) {
		List<GruppoPostiVO> gruppi = sala.getGruppi();
		while (index >= gruppi.size()) {
			gruppi.add(new GruppoPostiVO());
		}
		return gruppi.get(index);
	}

	public String getAction_index() {
		return action_index;
	}

	public void setAction_index(String action_index) {
		this.action_index = action_index;
	}

	public List<TB_CODICI> getStrumentiMediazione() {
		return strumentiMediazione;
	}

	public void setStrumentiMediazione(List<TB_CODICI> strumentiMediazione) {
		this.strumentiMediazione = strumentiMediazione;
	}

}
