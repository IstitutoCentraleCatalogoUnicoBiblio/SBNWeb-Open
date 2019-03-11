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

public class GestioneBlocchiVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -773629920637091053L;
	private int inizio;
	private int fine;
	private boolean caricato;

	public GestioneBlocchiVO ()
	{
		this.inizio = 0;
		this.fine = 0;
		this.caricato = false;

	};
	public GestioneBlocchiVO(int ini, int fin, boolean caric) throws Exception {
/*		if (cod == null) {
			throw new Exception("Codice non valido");
		}
		if (des == null) {
			throw new Exception("Descrizione non valida");
		}
*/
		this.inizio = ini;
		this.fine = fin;
		this.caricato = caric;

	}
	public boolean isCaricato() {
		return caricato;
	}
	public void setCaricato(boolean caricato) {
		this.caricato = caricato;
	}
	public int getFine() {
		return fine;
	}
	public void setFine(int fine) {
		this.fine = fine;
	}
	public int getInizio() {
		return inizio;
	}
	public void setInizio(int inizio) {
		this.inizio = inizio;
	}

}
