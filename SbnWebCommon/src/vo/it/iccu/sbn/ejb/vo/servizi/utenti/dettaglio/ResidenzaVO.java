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
package it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio;


public class ResidenzaVO extends IndirizzoVO {

	private static final long serialVersionUID = -8442321183725179551L;
	private String nazionalita = "";

	public ResidenzaVO() {
		this.clearInd();
		this.clearRes();
	}


	public void clearRes() {
		super.clearInd();
		this.nazionalita = "";
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = trimAndSet(nazionalita);
	}

	public boolean impostato() {
		return (  ((isFilled(nazionalita) && this.nazionalita.equals("IT"))
				&& super.impostato()) || ((isFilled(nazionalita) && !this.nazionalita.equals("IT"))
						&& super.impostatoXResidenzaEstera())
				);
	}

	public boolean almenoUnaProprietaImpostata() {
		return (   (isFilled(nazionalita))
				|| super.almenoUnaProprietaImpostata()
				);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ResidenzaVO other = (ResidenzaVO) obj;
		if (nazionalita == null) {
			if (other.nazionalita != null)
				return false;
		} else if (!nazionalita.equals(other.nazionalita))
			return false;
		return super.equals(obj);
		//return true;
	}

}
