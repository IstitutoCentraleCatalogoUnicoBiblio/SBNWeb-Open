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
package it.iccu.sbn.vo.validators.calendario;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.FasciaVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.IntervalloVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.Date;
import java.util.List;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

public class IntervalloValidator extends SerializableVO implements Validator<IntervalloVO> {

	private static final long serialVersionUID = -6097794857056233837L;
	FasciaValidator fv = new FasciaValidator();

	private Predicate<FasciaVO> findOverlapped(final FasciaVO f1) {
		return new Predicate<FasciaVO>() {
			public boolean test(FasciaVO f2) {
				if (f1 == f2)
					return false;

				return !(f1.getInizio().isAfter(f2.getFine()) ||
							f1.getFine().isBefore(f2.getInizio()));
			}
		};
	}

	public void validate(IntervalloVO target) throws ValidationException {
		String descr = target.getDescrizione();
		if (!isFilled(descr))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.calendario.descrizione");
		Date inizio = target.getInizio();
		if (inizio == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.calendario.inizio");
		Date fineIntv = target.getFine();
		if (fineIntv != null && fineIntv.compareTo(inizio) < 0)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

		for (List<FasciaVO> fasce : target.getFasce().values()) {
			for (FasciaVO f : fasce)
				f.validate(fv);
			for (FasciaVO f : fasce)
				if (Stream.of(fasce).anyMatch(findOverlapped(f)) )
					throw new ValidationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_FASCE_SOVRAPPOSTE);

		}

	}

}
