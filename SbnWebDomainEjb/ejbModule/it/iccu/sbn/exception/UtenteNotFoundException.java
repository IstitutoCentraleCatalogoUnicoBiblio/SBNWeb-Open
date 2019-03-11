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
package it.iccu.sbn.exception;

import it.iccu.sbn.web.vo.SbnErrorTypes;

public class UtenteNotFoundException extends Exception {

	private SbnErrorTypes errorCode;

	private static final long serialVersionUID = 8308263386514211274L;

	public UtenteNotFoundException(SbnErrorTypes errorCode) {
		this.errorCode = errorCode;
	}

	public UtenteNotFoundException(SbnErrorTypes errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public UtenteNotFoundException(String message) {
		super(message);
	}

	public UtenteNotFoundException() {
		super();
	}

	public SbnErrorTypes getErrorCode() {
		return errorCode;
	}

}
