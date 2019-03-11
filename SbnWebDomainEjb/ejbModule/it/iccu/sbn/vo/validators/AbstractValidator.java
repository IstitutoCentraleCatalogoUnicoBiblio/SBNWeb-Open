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
package it.iccu.sbn.vo.validators;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.validation.Validable;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.util.Date;

public abstract class AbstractValidator<T extends Validable> extends SerializableVO implements Validator<T> {

	private static final long serialVersionUID = -7596651050731477801L;

	public AbstractValidator() {
		super();
	}

	protected void checkDataRange(String dataFrom, String dataTo) throws ValidationException {

		if (isFilled(dataFrom) && ValidazioneDati.validaData(dataFrom) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_INIZIO);

		if (isFilled(dataTo) && ValidazioneDati.validaData(dataTo) != ValidazioneDati.DATA_OK)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_DATA_FINE);

		if (isFilled(dataTo) && isFilled(dataFrom)) {
			Timestamp from = DateUtil.toTimestamp(dataFrom);
			Timestamp to = DateUtil.toTimestampA(dataTo);
			if (to.before(from))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);
		}
	}

	protected void checkDataRange(Date dataFrom, Date dataTo) throws ValidationException {

		if (dataTo != null && dataFrom != null)
			if (dataTo.before(dataFrom))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

	}

}
