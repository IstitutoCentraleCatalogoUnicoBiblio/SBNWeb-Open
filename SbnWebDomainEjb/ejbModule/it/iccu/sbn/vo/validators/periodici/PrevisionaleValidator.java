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
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.vo.SbnErrorTypes;


public class PrevisionaleValidator extends SerializableVO implements Validator<RicercaKardexPrevisionaleVO> {

	private static final long serialVersionUID = -7509876088097419531L;

	public void validate(RicercaKardexPrevisionaleVO target) throws ValidationException {

		if (!isFilled(target.getDurata()) )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.previsioni.mod.prevedi.per");

		if (target.getData_conv_pub() == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.previsioni.mod.data_primo_fasc");

		String annataFrom = target.getAnnataFrom();
		String annataTo = target.getAnnataTo();

		if ( (isFilled(annataFrom) && !isFilled(annataTo))
			|| (!isFilled(annataFrom) && isFilled(annataTo)))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.kardex.annata");

		if ( isFilled(annataFrom) && isFilled(annataTo)) {

			if (!annataFrom.matches(PeriodiciConstants.REGEX_FORMATO_ANNO))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.kardex.annata");

			if (!annataTo.matches(PeriodiciConstants.REGEX_FORMATO_ANNO))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.kardex.annata");

			int yFrom = new Integer(annataFrom);
			int yTo = new Integer(annataTo);

			if (yFrom >= yTo) //strettamente maggiore
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.kardex.annata");

			if (!DateUtil.between(target.getData_conv_pub(), DateUtil.firstDayOfYear(yFrom), DateUtil.lastDayOfYear(yTo)))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA, "periodici.kardex.annata");
		}

		ModelloPrevisionaleVO modello = target.getModello();
		modello.validate(new ModelloPrevisionaleValidator() );

		if (modello.getNum_fascicoli_per_volume() > 0)
			if (target.getPosizione_primo_fascicolo() > modello.getNum_fascicoli_per_volume())
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_POS_FASCICOLO_ECCEDE_FASCICOLI_PER_VOLUME);

		//almaviva5_20121026
		if (target.isAnnataDoppia() ) {
			if (modello.getNum_fascicoli_per_volume() < 1)
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_NUMERO_FASCICOLI_PER_VOL_ANNATE_DOPPIE);
			/*
			if (modello.getNum_fascicoli_per_volume() > 0)
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_NUMERO_FASCICOLI_PER_VOL_ANNATE_DOPPIE);

			//calcolo fascicoli per un anno
			try {
				TB_CODICI cod = CodiciProvider.cercaCodice(modello.getCd_per(), CodiciType.CODICE_PERIODICITA, CodiciRicercaType.RICERCA_CODICE_SBN);
				String ggPer = cod.getCd_flg2();
				int gg = ValidazioneDati.isFilled(ggPer) && ValidazioneDati.strIsNumeric(ggPer) ? Integer.valueOf(ggPer) : 0;
				modello.setNum_fascicoli_per_volume(Math.round((float)365 / gg));
			} catch (Exception e) {
				throw new ValidationException(SbnErrorTypes.DB_FALUIRE);
			}
			*/
		}

	}

}
