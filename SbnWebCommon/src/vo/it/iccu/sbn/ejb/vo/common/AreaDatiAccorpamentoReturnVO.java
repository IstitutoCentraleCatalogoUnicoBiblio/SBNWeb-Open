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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class AreaDatiAccorpamentoReturnVO extends SerializableVO {

	private static final long serialVersionUID = -857531223181608839L;
	private String codiceRitorno;
	private String idErrore;
	private String idOk;

	private String codErr;
	private String testoProtocollo;

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}

	public String getCodiceRitorno() {
		return codiceRitorno;
	}

	public void setCodiceRitorno(String codiceRitorno) {
		this.codiceRitorno = codiceRitorno;
	}

	public String getIdErrore() {
		return idErrore;
	}

	public void setIdErrore(String idErrore) {
		this.idErrore = idErrore;
	}

	public String getIdOk() {
		return idOk;
	}

	public void setIdOk(String idOk) {
		this.idOk = idOk;
	}

	public boolean isEsitoOk() {
		return (isFilled(codiceRitorno) && codiceRitorno.equals("0000") );
	}



}
