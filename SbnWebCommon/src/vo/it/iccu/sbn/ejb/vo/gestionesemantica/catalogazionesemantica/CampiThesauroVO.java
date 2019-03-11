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
package it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.vo.SerializableVO;


public class CampiThesauroVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 3018661897912513999L;
	private String did;
	private String utilizzato;
	private String termine;
	private String codSelezionato;

	public String getCodSelezionato() {
		return codSelezionato;
	}

	public void setCodSelezionato(String codSelezionato) {
		this.codSelezionato = codSelezionato;
	}

	public CampiThesauroVO() {
		super();
	}

	public CampiThesauroVO(String did, String utilizzato, String termine,
			String codSelezionato ) throws Exception {

		this.did = did;
		this.utilizzato = utilizzato;
		this.termine = termine;
		this.codSelezionato = codSelezionato;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getTermine() {
		return termine;
	}

	public void setTermine(String termine) {
		this.termine = termine;
	}

	public String getUtilizzato() {
		return utilizzato;
	}

	public void setUtilizzato(String utilizzato) {
		this.utilizzato = utilizzato;
	}


}
