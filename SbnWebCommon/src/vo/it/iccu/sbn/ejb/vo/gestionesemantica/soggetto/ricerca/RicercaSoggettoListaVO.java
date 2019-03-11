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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

import java.util.List;

public class RicercaSoggettoListaVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -558837690945331773L;

	private SoggettarioVO soggettario;

	@SuppressWarnings("unchecked")
	private List risultati;

	@SuppressWarnings("unchecked")
	public List getRisultati() {
		return risultati;
	}

	@SuppressWarnings("unchecked")
	public void setRisultati(List risultati) {
		this.risultati = risultati;
	}

	public SoggettarioVO getSoggettario() {
		return soggettario;
	}

	public void setSoggettario(SoggettarioVO soggettario) {
		this.soggettario = soggettario;
	}

}
