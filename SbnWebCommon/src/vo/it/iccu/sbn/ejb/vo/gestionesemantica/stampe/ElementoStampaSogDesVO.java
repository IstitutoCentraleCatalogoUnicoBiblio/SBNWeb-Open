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
package it.iccu.sbn.ejb.vo.gestionesemantica.stampe;

public abstract class ElementoStampaSogDesVO extends ElementoStampaSemanticaVO {

	private static final long serialVersionUID = 3802747219438596596L;

	private String codSoggettario;
	private String edizioneSoggettario;

	public ElementoStampaSogDesVO() {
		super();
	}

	public String getCodSoggettario() {
		return codSoggettario;
	}

	public void setCodSoggettario(String codSoggettario) {
		this.codSoggettario = trimAndSet(codSoggettario);
	}

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

}
