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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

public abstract class CreaVariaSogDesBaseVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = 7845845111378959039L;

	private String codiceSoggettario;
	private String edizioneSoggettario;
	private String testo;

	public CreaVariaSogDesBaseVO(SBNMarcCommonVO other) {
		super(other);
	}

	public CreaVariaSogDesBaseVO() {
		super();
	}

	public String getCodiceSoggettario() {
		return codiceSoggettario;
	}

	public void setCodiceSoggettario(String codiceSoggettario) {
		this.codiceSoggettario = codiceSoggettario;
	}

	public String getEdizioneSoggettario() {
		return edizioneSoggettario;
	}

	public void setEdizioneSoggettario(String edizioneSoggettario) {
		this.edizioneSoggettario = edizioneSoggettario;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = trimAndSet(testo);
	}

}
