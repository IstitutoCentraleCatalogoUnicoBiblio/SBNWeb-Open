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
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.IntervalloVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

public class ModelloCalendarioValidator extends SerializableVO implements Validator<ModelloCalendarioVO> {

	private static final long serialVersionUID = -2371353960636995677L;

	IntervalloValidator iv = new IntervalloValidator();

	private Predicate<IntervalloVO> findOverlapped(final IntervalloVO i1) {
		final Date fine1 = ValidazioneDati.coalesce(i1.getFine(), DateUtil.MAX_DATE);
		return new Predicate<IntervalloVO>() {
			public boolean test(IntervalloVO i2) {
				if (i1 == i2)
					return false;
				Date fine2 = ValidazioneDati.coalesce(i2.getFine(), DateUtil.MAX_DATE);

				Interval int1 = Interval.of(i1.getInizio(), fine1);
				Interval int2 = Interval.of(i2.getInizio(), fine2);

				return int1.overlaps(int2) || int2.overlaps(int1);
			}
		};
	}

	private Predicate<IntervalloVO> findOpenInterval() {
		return new Predicate<IntervalloVO>() {
			public boolean test(IntervalloVO i) {
				return (i.getFine() == null);
			}
		};
	}

	public void validate1(ModelloCalendarioVO target) throws ValidationException {
		if (isNull(target.getCodPolo()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codPolo");
		if (isNull(target.getCodBib()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codBib");

		Date inizio = target.getInizio();
		if (inizio == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.calendario.inizio");

		Date fineMod = target.getFine();
		boolean calendarioChiuso = fineMod != null;

		if (calendarioChiuso && !fineMod.after(inizio))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

		List<IntervalloVO> intervalli = target.getIntervalli();
		int size = intervalli.size();
		for (int idx = 0; idx < size; idx++) {
			IntervalloVO i = intervalli.get(idx);
			i.validate(iv);
			Date fineIntv = i.getFine();
			if (calendarioChiuso && fineIntv != null && fineIntv.after(fineMod))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

			//solo il primo intervallo può essere aperto
			boolean first = (idx == 0);
			if (!calendarioChiuso && !first && fineIntv == null )
				throw new ValidationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_INTERVALLO_APERTO, i.getDescrizione() );
		}

		//check intervalli aperti
		long intervalliAperti = Stream.of(intervalli).filter(findOpenInterval()).count();
		if (calendarioChiuso && intervalliAperti > 0)
			throw new ValidationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_INTERVALLO_APERTO);
		if (!calendarioChiuso && intervalliAperti > 1)
			throw new ValidationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_TROPPI_INTERVALLI_APERTI);

		//check intervalli sovrapposti (escluso il primo)
		for (int idx = 1; idx < size; idx++) {
			IntervalloVO i = intervalli.get(idx);
			if (Stream.of(intervalli).skip(1).anyMatch(findOverlapped(i)) )
				throw new ValidationException(SbnErrorTypes.SRV_ERROR_CALENDARIO_INTERVALLI_SOVRAPPOSTI);
		}

	}

	public void validate2(ModelloCalendarioVO target) throws ValidationException {
		if (isNull(target.getCodPolo()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codPolo");
		if (isNull(target.getCodBib()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "codBib");

		Date inizio = target.getInizio();
		if (inizio == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.calendario.inizio");

		Date fine = target.getFine();
		if (fine == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.calendario.fine");

		if (!fine.after(inizio))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);

		List<IntervalloVO> intervalli = target.getIntervalli();
		int size = intervalli.size();
		Collections.sort(intervalli, IntervalloVO.ORDINAMENTO_INTERVALLO);
		for (int idx = 0; idx < size; idx++)
			intervalli.get(idx).setId(idx + 1);
	}

	public void validate(ModelloCalendarioVO target) throws ValidationException {
		validate2(target);
	}

}
