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

public class StrutturaTerna extends SerializableVO{
	/**
	 *
	 */
	private static final long serialVersionUID = 8035809165241506488L;
	private String codice1;
	private String codice2;
	private String codice3;

	public StrutturaTerna ()
	{
		this.codice1 = "";
		this.codice2 = "";
		this.codice3 = "";

	};
	public StrutturaTerna(String uno, String due, String tre) throws Exception {
/*		if (cod == null) {
			throw new Exception("Codice non valido");
		}
		if (des == null) {
			throw new Exception("Descrizione non valida");
		}
*/
		this.codice1 = uno;
		this.codice2 = due;
		this.codice3 = tre;

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
		String terna=getCodice1()+ "|" + getCodice2()+ "|" +  getCodice3();
		return terna;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codice1 == null) ? 0 : codice1.hashCode());
		result = prime * result + ((codice2 == null) ? 0 : codice2.hashCode());
		result = prime * result + ((codice3 == null) ? 0 : codice3.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StrutturaTerna other = (StrutturaTerna) obj;
		if (codice1 == null) {
			if (other.codice1 != null)
				return false;
		} else if (!codice1.equals(other.codice1))
			return false;
		if (codice2 == null) {
			if (other.codice2 != null)
				return false;
		} else if (!codice2.equals(other.codice2))
			return false;
		if (codice3 == null) {
			if (other.codice3 != null)
				return false;
		} else if (!codice3.equals(other.codice3))
			return false;
		return true;
	}
}
