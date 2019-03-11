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
package it.iccu.sbn.servizi.pagination.test;

import it.iccu.sbn.servizi.pagination.QueryExecutionParams;

import java.io.Serializable;

public class TestRangeCollQueryParams implements QueryExecutionParams {

	private static final long serialVersionUID = 337189251628185675L;
	private final String codPolo;
	private String codBib;
	private String codSez;
	private String startLoc;
	private String endLoc;

	public TestRangeCollQueryParams(String codPolo, String codBib,
			String codSez, String startLoc, String endLoc) {
		super();
		this.codPolo = codPolo;
		this.codBib = codBib;
		this.codSez = codSez;
		this.startLoc = startLoc;
		this.endLoc = endLoc;
	}

	public Serializable getCountParams() {
		// TODO Auto-generated method stub
		return null;
	}

	public Serializable getQueryParams() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public String getCodSez() {
		return codSez;
	}

	public String getStartLoc() {
		return startLoc;
	}

	public String getEndLoc() {
		return endLoc;
	}

}
