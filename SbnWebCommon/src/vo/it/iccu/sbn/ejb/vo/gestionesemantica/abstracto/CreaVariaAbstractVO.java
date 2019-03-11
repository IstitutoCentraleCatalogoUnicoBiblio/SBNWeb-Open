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
package it.iccu.sbn.ejb.vo.gestionesemantica.abstracto;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

public class CreaVariaAbstractVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -3638389559451534210L;

	private String descrizione;
	private String livello;
	private String action;
	private String T001;


	public String getT001() {
		return T001;
	}

	public void setT001(String t001) {
		T001 = t001;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	public String getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}


	public String getLivello() {
		return livello;
	}


	public void setLivello(String livello) {
		this.livello = livello;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();

		if (!isFilled(descrizione))
			throw new ValidationException("Digitare il campo descrizione");

		if (length(descrizione) > 2160)
			throw new ValidationException("La descrizione supera i 2160 caratteri");

		if (!isFilled(livello) )
			throw new ValidationException("Digitare il campo stato di controllo");

		if (length(T001) != 10)
			throw new ValidationException("Parametro Bid non valido");

	}

}
