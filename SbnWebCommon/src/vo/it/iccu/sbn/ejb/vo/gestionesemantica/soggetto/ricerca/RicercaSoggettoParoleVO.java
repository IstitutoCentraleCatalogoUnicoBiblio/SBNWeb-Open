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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca;

import it.iccu.sbn.ejb.exception.ValidationException;

public class RicercaSoggettoParoleVO extends RicercaSoggettoVO {

	private static final long serialVersionUID = -1776842830001230269L;
	private String parola0 = "";
	private String parola1 = "";
	private String parola2 = "";
	private String parola3 = "";
	private String parola4 = "";
	private String parola5 = "";

	public RicercaSoggettoParoleVO() {
		super();
	}

	public RicercaSoggettoParoleVO(String testoSogg, String cid,
			String parola0, String parola1, String parola2, String parola3,
			String parola4, String parola5) {
		super(testoSogg, cid);
		this.parola0 = parola0;
		this.parola1 = parola1;
		this.parola2 = parola2;
		this.parola3 = parola3;
		this.parola4 = parola4;
		this.parola5 = parola5;
	}

	@Override
	public int count() {

		int count = 0;
		count += isFilled(parola0) ? 1 : 0;
		count += isFilled(parola1) ? 1 : 0;
		count += isFilled(parola2) ? 1 : 0;
		count += isFilled(parola3) ? 1 : 0;
		count += isFilled(parola4) ? 1 : 0;
		count += isFilled(parola5) ? 1 : 0;

		return count;
	}

	@Override
	public boolean isEmpty() {
		return (super.isEmpty() && count() == 0);
	}

	@Override
	public void validate() throws ValidationException {
		if (isFilled(this.getTestoSogg() )) {
			if (isFilled(this.getCid() ) || count() > 0 )
				throw new ValidationException("Validazione non corretta",
						ValidationException.errore);
		}
		else if (isFilled(this.getCid() ) ) {
			if (count() > 0)
				throw new ValidationException("Validazione non corretta",
						ValidationException.errore);
		} else if (count() == 0) {
			throw new ValidationException("Validazione non corretta",
					ValidationException.validazione);
		}
	}

	public String getParola0() {
		return parola0;
	}

	public void setParola0(String parola0) {
		this.parola0 = parola0;
	}

	public String getParola1() {
		return parola1;
	}

	public void setParola1(String parola1) {
		this.parola1 = parola1;
	}

	public String getParola2() {
		return parola2;
	}

	public void setParola2(String parola2) {
		this.parola2 = parola2;
	}

	public String getParola3() {
		return parola3;
	}

	public void setParola3(String parola3) {
		this.parola3 = parola3;
	}

	public String getParola4() {
		return parola4;
	}

	public void setParola4(String parola4) {
		this.parola4 = parola4;
	}

	public String getParola5() {
		return parola5;
	}

	public void setParola5(String parola5) {
		this.parola5 = parola5;
	}

}
