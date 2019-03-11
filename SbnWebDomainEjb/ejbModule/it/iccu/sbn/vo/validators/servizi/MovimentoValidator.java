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
package it.iccu.sbn.vo.validators.servizi;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.validation.Validator;

public class MovimentoValidator extends SerializableVO implements Validator<MovimentoVO> {

	private static final long serialVersionUID = -5129928498211766614L;

	public void validate(MovimentoVO m) throws ValidationException {
/*
		String volume = m.getNumVolume();
		if (isFilled(volume) && !isNumeric(volume))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "Volume");
*/
	}


}
