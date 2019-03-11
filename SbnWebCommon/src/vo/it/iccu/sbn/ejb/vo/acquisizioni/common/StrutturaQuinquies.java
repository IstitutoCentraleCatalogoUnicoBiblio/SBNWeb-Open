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
package it.iccu.sbn.ejb.vo.acquisizioni.common;

import it.iccu.sbn.ejb.vo.SerializableVO;

public class StrutturaQuinquies extends SerializableVO {

	private static final long serialVersionUID = 689257905819180801L;
	private String codice1;
	private String codice2;
	private String codice3;
	private String codice4;
	private String codice5;

	public StrutturaQuinquies() {
		this.codice1 = "";
		this.codice2 = "";
		this.codice3 = "";
		this.codice4 = "";
		this.codice5 = "";

	};

	public StrutturaQuinquies(String uno, String due, String tre,
			String quattro, String cinque) throws Exception {
		/*
		 * if (cod == null) { throw new Exception("Codice non valido"); } if
		 * (des == null) { throw new Exception("Descrizione non valida"); }
		 */
		this.codice1 = uno;
		this.codice2 = due;
		this.codice3 = tre;
		this.codice4 = quattro;
		this.codice5 = cinque;

	}

	public String getCodice1() {
		return codice1;
	}

	public void setCodice1(String codice1) {
		this.codice1 = codice1;
	}

	public String getCodice2() {
		return codice2;
	}

	public void setCodice2(String codice2) {
		this.codice2 = codice2;
	}

	public String getCodice3() {
		return codice3;
	}

	public void setCodice3(String codice3) {
		this.codice3 = codice3;
	}

	public String getTerna() {
		String terna = getCodice1() + "|" + getCodice2() + "|" + getCodice3();
		return terna;
	}

	public String getCodice4() {
		return codice4;
	}

	public void setCodice4(String codice4) {
		this.codice4 = codice4;
	}

	public String getCodice5() {
		return codice5;
	}

	public void setCodice5(String codice5) {
		this.codice5 = codice5;
	}
}
