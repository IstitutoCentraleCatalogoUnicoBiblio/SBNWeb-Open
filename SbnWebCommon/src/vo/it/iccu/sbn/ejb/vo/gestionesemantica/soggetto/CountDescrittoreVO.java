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

import it.iccu.sbn.ejb.vo.SerializableVO;

public class CountDescrittoreVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 4022474576252291605L;
	private String descrittore;
	private int countSoggetti;
	private int countDescrittori;


	public int getCountDescrittori() {
		return countDescrittori;
	}
	public void setCountDescrittori(int countDescrittori) {
		this.countDescrittori = countDescrittori;
	}
	public int getCountSoggetti() {
		return countSoggetti;
	}
	public void setCountSoggetti(int countSoggetti) {
		this.countSoggetti = countSoggetti;
	}
	public String getDescrittore() {
		return descrittore;
	}
	public void setDescrittore(String descrittore) {
		this.descrittore = descrittore;
	}

}
