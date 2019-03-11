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
package it.iccu.sbn.vo.validators.periodici;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class SerieOrdineValidator extends SerializableVO implements	Validator<SerieOrdineVO> {

	private static final long serialVersionUID = 3891601701899960948L;

	public void validate(SerieOrdineVO target) throws ValidationException {
//		if (!isFilled(target.getBid()))
//			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "bid");
		if (!isFilled(target.getCod_bib_ord()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "cod_bib_ord");
		if (!isFilled(target.getAnno_ord()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "anno_ord");
		if (!isFilled(target.getCod_ord()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "cod_ord");
		if (!isFilled(target.getCod_tip_ord()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "cod_tip_ord");
//		if (!isFilled(target.getId_ordine()))
//			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "id_ordine");
	}

}
