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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;

public abstract class RicercaBaseTimeRangeVO<T> extends RicercaBaseVO<T> {

	private static final long serialVersionUID = 4008651515795491845L;
	protected String dataFrom;
	protected String dataTo;

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = trimAndSet(dataFrom);
	}

	public String getDataTo() {
		return dataTo;
	}

	public void setDataTo(String dataTo) {
		this.dataTo = trimAndSet(dataTo);
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (isFilled(dataFrom) && ValidazioneDati.validaData(dataFrom) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_INIZIO);

		if (isFilled(dataTo) && ValidazioneDati.validaData(dataTo) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_FINE);

		if (isFilled(dataTo) && isFilled(dataFrom) ) {
			Timestamp from = DateUtil.toTimestamp(dataFrom);
			Timestamp to   = DateUtil.toTimestampA(dataTo);
			if (to.before(from))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);
		}

	}

}
