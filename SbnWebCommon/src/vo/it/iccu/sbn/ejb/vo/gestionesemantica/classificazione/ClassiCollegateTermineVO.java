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
package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

import java.util.List;

public class ClassiCollegateTermineVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = 5292671816138656632L;

	private String codThesauro;
	private String did;

	private List<SinteticaClasseVO> risultati;

	public List<SinteticaClasseVO> getRisultati() {
		return risultati;
	}

	public void setRisultati(List<SinteticaClasseVO> risultati) {
		this.risultati = risultati;
	}

	public String getCodThesauro() {
		return codThesauro;
	}

	@Override
	public int getRepeatableId() {
		return (codThesauro + '-' + did).hashCode();
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	@Override
	public boolean isEmpty() {
		return !isFilled(risultati);
	}

}
