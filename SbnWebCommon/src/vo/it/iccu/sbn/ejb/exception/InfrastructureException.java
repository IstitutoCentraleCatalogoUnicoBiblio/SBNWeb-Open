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
package it.iccu.sbn.ejb.exception;

import it.iccu.sbn.web.exception.SbnBaseException;

public class InfrastructureException extends SbnBaseException {

	private static final long serialVersionUID = 7767166934495888997L;

	public InfrastructureException() {
		super();
	}

	public InfrastructureException(String message) {
		super(message);
	}

	public InfrastructureException(Exception e) {
		super();
		if (e != null)
			this.detail = e.getCause();
	}

	public InfrastructureException(Throwable t) {
		super();
		this.detail = t;
	}
}
