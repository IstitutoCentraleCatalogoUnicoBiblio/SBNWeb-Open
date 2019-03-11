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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.batch;


import java.sql.Timestamp;

public class ParametriCancellazioneSoggettiNonUtilizzatiVO extends ParametriBatchSoggettoBaseVO {

	private static final long serialVersionUID = 5227480907653209914L;
	private Timestamp tsVar_Da;
	private Timestamp tsVar_A;

	public Timestamp getTsVar_Da() {
		return tsVar_Da;
	}

	public void setTsVar_Da(Timestamp tsVar_Da) {
		this.tsVar_Da = tsVar_Da;
	}

	public Timestamp getTsVar_A() {
		return tsVar_A;
	}

	public void setTsVar_A(Timestamp tsVar_A) {
		this.tsVar_A = tsVar_A;
	}

}
