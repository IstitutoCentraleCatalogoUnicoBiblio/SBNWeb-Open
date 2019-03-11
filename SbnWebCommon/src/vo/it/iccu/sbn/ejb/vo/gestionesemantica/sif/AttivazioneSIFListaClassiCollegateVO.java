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
package it.iccu.sbn.ejb.vo.gestionesemantica.sif;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class AttivazioneSIFListaClassiCollegateVO extends SerializableVO {

	private static final long serialVersionUID = -1724717983058640794L;
	private final String bid;
	private final String titolo;
	private final int elementiPerBlocco;
	private final String requestAttribute;
	private final String codSistema;


	public String getBid() {
		return bid;
	}

	public String getTitolo() {
		return titolo;
	}

	public int getElementiPerBlocco() {
		return elementiPerBlocco;
	}

	public String getRequestAttribute() {
		return requestAttribute;
	}

	/**
	 * @param bid
	 * @param titolo
	 * @param elementiPerBlocco
	 * @param requestAttribute
	 */
	public AttivazioneSIFListaClassiCollegateVO(String bid, String titolo, String codSistema, int elementiPerBlocco, String requestAttribute) {
		super();
		this.bid = bid;
		this.titolo = titolo;
		//almaviva5_20111114 #4694
		this.codSistema = trimAndSet(codSistema);
		this.elementiPerBlocco = elementiPerBlocco;
		this.requestAttribute = requestAttribute;
	}

	public String getCodSistema() {
		return codSistema;
	}

}
