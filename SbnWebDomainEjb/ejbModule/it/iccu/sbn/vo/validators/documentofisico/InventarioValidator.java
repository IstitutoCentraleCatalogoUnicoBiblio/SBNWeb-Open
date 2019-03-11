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
package it.iccu.sbn.vo.validators.documentofisico;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class InventarioValidator extends SerializableVO implements Validator<InventarioVO> {

	private static final long serialVersionUID = 1760588114767541704L;

	public void validate(InventarioVO target) throws ValidationException {

		String grpFasc = target.getGruppoFascicolo();
		if (isFilled(grpFasc)) {
			if (!ValidazioneDati.strIsNumeric(grpFasc))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "Gruppo fascicolo");

			int value = Integer.valueOf(grpFasc);
			if (value < 1 || value > PeriodiciConstants.MAX_GRUPPO_FASCICOLO)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "Gruppo fascicolo");
		}

	}

}
