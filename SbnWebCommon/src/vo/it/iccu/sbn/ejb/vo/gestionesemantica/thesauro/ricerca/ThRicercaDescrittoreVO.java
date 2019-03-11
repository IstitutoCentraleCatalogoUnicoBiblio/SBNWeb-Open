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
package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;

public class ThRicercaDescrittoreVO extends SerializableVO {

	private static final long serialVersionUID = -729834548832217256L;

	private String testoDescr = "";
	private String did = "";
	private String parole0 = "";
	private String parole1 = "";
	private String parole2 = "";
	private String parole3 = "";
	private boolean utilizzati;

	public boolean isUtilizzati() {
		return utilizzati;
	}

	public void setUtilizzati(boolean utilizzati) {
		this.utilizzati = utilizzati;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did.toUpperCase();
	}

	public String getParole1() {
		return parole1;
	}

	public void setParole1(String parole1) {
		this.parole1 = parole1;
	}

	public String getParole2() {
		return parole2;
	}

	public void setParole2(String parole2) {
		this.parole2 = parole2;
	}

	public String getParole3() {
		return parole3;
	}

	public void setParole3(String parole3) {
		this.parole3 = parole3;
	}

	public String getParole0() {
		return parole0;
	}

	public void setParole0(String parole0) {
		this.parole0 = parole0;
	}

	public String getTestoDescr() {
		return testoDescr;
	}

	public void setTestoDescr(String testoDescr) {
		this.testoDescr = testoDescr;
	}

	public int count() {
		int count = 0;
		count += isFilled(parole0) ? 1 : 0;
		count += isFilled(parole1) ? 1 : 0;
		count += isFilled(parole2) ? 1 : 0;
		count += isFilled(parole3) ? 1 : 0;

		return count;
	}


	@Override
	public boolean isEmpty() {
		return (count() == 0 && isNull(testoDescr) && isNull(did) && super.isEmpty() );
	}

	public void validate() throws ValidationException {
		// ricerca per testo
		if (isFilled(testoDescr)) {
			if (isFilled(did) || isFilled(parole0) || isFilled(parole1)
					|| isFilled(parole2) || isFilled(parole3)) {
				throw new ValidationException("Validazione non corretta",
						ValidationException.errore);
			}
		} else

		// ricerca per did
		if (isFilled(did)) {
			if (isFilled(parole0) || isFilled(parole1) || isFilled(parole2)
					|| isFilled(parole3)) {
				throw new ValidationException("Validazione non corretta",
						ValidationException.errore);
			}
		} else

		// ricerca per parole
		if (isNull(parole0) && isNull(parole1) && isNull(parole2)
				&& isNull(parole3)) {
			throw new ValidationException("Nessun parametro impostato",
					ValidationException.validazione);
		}
	}

}
