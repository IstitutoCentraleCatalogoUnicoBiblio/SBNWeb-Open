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
import it.iccu.sbn.ejb.vo.periodici.ElementoSeriePeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoPerCreaOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class RicercaKardexPeriodicoValidator extends SerializableVO implements Validator<RicercaKardexPeriodicoVO<FascicoloVO>> {

	private static final long serialVersionUID = -998982471323208003L;

	public void validate(RicercaKardexPeriodicoVO<FascicoloVO> target) throws ValidationException {

		//gestione digitazione solo annata
		//data inizio
		String dataFrom = target.getDataFrom();
		if (isFilled(dataFrom) && dataFrom.matches(PeriodiciConstants.REGEX_FORMATO_ANNO))
			target.setDataFrom(DateUtil.formattaData(DateUtil.firstDayOfYear(Integer.valueOf(dataFrom))));

		//data fine
		String dataTo = target.getDataTo();
		if (isFilled(dataTo) && dataTo.matches(PeriodiciConstants.REGEX_FORMATO_ANNO))
			target.setDataTo(DateUtil.formattaData(DateUtil.lastDayOfYear(Integer.valueOf(dataTo))));
		//

		target.validate(); //validazione range date

		//cambio oggetto ricerca selezionato da oggetto contenitore
		SerieBaseVO oggettoRicerca = target.getOggettoRicerca();
		if (oggettoRicerca == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_OBJECT_IS_NULL, "oggettoRicerca");

		if (oggettoRicerca instanceof ElementoSeriePeriodicoVO) {
			ElementoSeriePeriodicoVO esp = (ElementoSeriePeriodicoVO)oggettoRicerca;
			switch (esp.getTipo() ) {
			case ESEMPLARE:
				oggettoRicerca = esp.getEsemplare();
				target.setOggettoRicerca(oggettoRicerca);
				break;
			case COLLOCAZIONE:
				oggettoRicerca = esp.getCollocazione();
				target.setOggettoRicerca(oggettoRicerca);
				break;
			case ORDINE:
				oggettoRicerca = esp.getOrdine();
				target.setOggettoRicerca(oggettoRicerca);
				break;
			}
		}

		/*
		if (!(oggettoRicerca instanceof SerieOrdineVO))
			if (!isFilled(target.getDataFrom()) || !isFilled(target.getDataTo()))
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_INTERVALLO_DATE_OBBLIGATORIO);
		*/

		//almaviva5_20101202 anno abb obbligatorio per kardex da crea ordine
		if (target instanceof RicercaKardexPeriodicoPerCreaOrdineVO) {
			RicercaKardexPeriodicoPerCreaOrdineVO<FascicoloVO> ricerca = (RicercaKardexPeriodicoPerCreaOrdineVO<FascicoloVO>) target;
			if (!isFilled(ricerca.getAnnoAbb()))
				throw new ValidationException(SbnErrorTypes.PER_ANNO_RIFERIMENTO_ORDINE_OBBLIGATORIO);
			if (!ricerca.isContinuativo())
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_ORDINE_NON_CONTINUATIVO);
		}
	}

}
