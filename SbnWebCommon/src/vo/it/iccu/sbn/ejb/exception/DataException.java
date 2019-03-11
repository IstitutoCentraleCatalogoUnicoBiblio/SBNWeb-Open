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
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class DataException extends SbnBaseException {

	private static final long serialVersionUID = 7469848915630770750L;

	public DataException() {
        super();
    }

    public DataException(String m) {
        super(m);
    }

    public DataException(Exception e) {
        super(e.getMessage());
        if(e != null)
  		  this.detail = e.getCause();
    }

	public DataException(SbnErrorTypes error) {
		super(error);
	}

	public DataException(SbnErrorTypes error, String message) {
		super(error, message);
	}

};
