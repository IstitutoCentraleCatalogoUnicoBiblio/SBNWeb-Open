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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.periodici.previsionale.GiornoMeseVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.PeriodicitaFascicoloType;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.periodici.ModelloPrevisionaleDecorator;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ModelloPrevisionaleValidator extends SerializableVO implements Validator<ModelloPrevisionaleVO> {

	private static final long serialVersionUID = 3653313074741140224L;



	private static final List<GiornoMeseVO> fill(String fieldName, List<ComboVO> list) throws ValidationException {

		List<GiornoMeseVO> output = ValidazioneDati.emptyList();

		if (!isFilled(list))
			return output;

		for (ComboVO c : list) {
			String v = c.getCodice();
			if (!GiornoMeseVO.matches(trimOrEmpty(v)))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, fieldName);

			output.add(new GiornoMeseVO(v));
		}

		Collections.sort(output);
		return output;
	}

	private void validateDecorator(ModelloPrevisionaleDecorator target) throws ValidationException {

		List<GiornoMeseVO> escludiDate = fill("Escludi Date", target.getDateEscluse());
		List<GiornoMeseVO> includiDate = fill("Includi Date", target.getDateIncluse());

		//date duplicate?
		if (!Collections.disjoint(escludiDate, includiDate))
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_PREVISIONALE_GRUPPI_DATE_INCONGRUENTI);

		target.setListaEscludiDate(escludiDate);
		target.setListaIncludiDate(includiDate);

		Integer[] giorniEsclusi = target.getGiorniEsclusi();
		if (isFilled(giorniEsclusi) ) {
			List<Integer> giorni = target.getListaEscludiGiorni();
			giorni.clear();
			for (Integer g : giorniEsclusi)
				if (g > 0)
					giorni.add(g);
		}

		//almaviva5_20110704 #4522
		Integer[] giorniInclusi = target.getGiorniInclusi();
		if (isFilled(giorniInclusi) ) {
			List<Integer> giorni = target.getListaIncludiGiorni();
			giorni.clear();
			for (Integer g : giorniInclusi)
				if (g > 0)
					giorni.add(g);
		}

		//giorni duplicati?
		if (!Collections.disjoint(target.getListaEscludiGiorni(), target.getListaIncludiGiorni()))
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_PREVISIONALE_GRUPPI_DATE_INCONGRUENTI);

		//almaviva5_20110704 #4522
		//obbligatorio specificare i giorni per periodicità < settimanale
		PeriodicitaFascicoloType cd_per = PeriodicitaFascicoloType.fromString(target.getCd_per());
		if (cd_per.isLowerSettimanale()) {
			if (ValidazioneDati.size(target.getListaIncludiGiorni()) != cd_per.getNumFascicoliSettimana())
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_GIORNI_OBBLIGATORI_PERIODICITA,
					String.valueOf(cd_per.getNumFascicoliSettimana()));
		}

	}

	public void validate(ModelloPrevisionaleVO target) throws ValidationException {

		String cd_per = target.getCd_per();
		if (!isFilled(cd_per))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.kardex.tipo.per");
		/*
		if (!isFilled(target.getNum_primo_volume()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.previsioni.mod.primo_vol");
		*/

		//almaviva5_20110622 segn. ICCU: per periodicita K annuale, L biennale o M triennale
		//il numero fascicolo non è obbligatorio
		if (!ValidazioneDati.in(PeriodicitaFascicoloType.fromString(cd_per),
				PeriodicitaFascicoloType.ANNUALE,
				PeriodicitaFascicoloType.BIENNALE,
				PeriodicitaFascicoloType.TRIENNALE) )
			if (!isFilled(target.getNum_primo_fascicolo()))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.previsioni.mod.primo_fasc");

		if (!isFilled(target.getCd_tipo_num_fascicolo()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.previsioni.mod.numerazione");

		/*
		if (!isFilled(target.getNum_fascicoli_per_volume()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "periodici.previsioni.mod.num_fasc_per_vol");
		*/

		if (isFilled(target.getNum_primo_fascicolo()))
			validateTipoNumerazioneFascicolo(target);

		if (target instanceof ModelloPrevisionaleDecorator)
			validateDecorator((ModelloPrevisionaleDecorator) target);
	}

	private void validateTipoNumerazioneFascicolo(ModelloPrevisionaleVO target) throws ValidationException {

		TB_CODICI cod = null;
		String tipo_num = target.getCd_tipo_num_fascicolo();
		try {
			cod = CodiciProvider.cercaCodice(tipo_num, CodiciType.CODICE_TIPO_NUMERAZIONE_FASCICOLO, CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.DB_FALUIRE);
		}

		//il tipo numerazione non esiste
		if (cod == null)
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_TIPO_NUMERAZIONE_NON_VALIDO);

		String num_primo_fascicolo = target.getNum_primo_fascicolo();
		String model = ValidazioneDati.trimOrEmpty(cod.getCd_flg3());
		/*
		Progressivo progr = new Progressivo(num_primo_fascicolo, model);
		if (!target.isNum_fascicolo_continuativo() && isFilled(target.getNum_primo_volume()) )
			if (progr.getInitialValuePosition() > target.getNum_fascicoli_per_volume())
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_NUM_PRIMO_FASCICOLO_SUPERIORE_MAX_VOLUME);
		*/

		boolean numeric = ValidazioneDati.equals(cod.getCd_flg2(), PeriodiciConstants.PROGRESSIVO);
		if (numeric)
			if (isNumeric(num_primo_fascicolo))
				return;	//tutto ok
			else
				throw new ValidationException(SbnErrorTypes.PER_ERRORE_TIPO_NUMERAZIONE_NON_VALIDO);

		if (target.isNum_fascicolo_continuativo())
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_TIPO_NUMERAZIONE_MODELLO_NON_CONT);

		//se non numerico deve avere un modello

		if (!isFilled(model))
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_CONFIG_TIPO_NUMERAZIONE);

		List<String> values = Arrays.asList(model.split(PeriodiciConstants.SEPARATORE_NUMERAZIONE));
		int size = ValidazioneDati.size(values);
		//almeno due valori possibili
		if (size < 2)
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_CONFIG_TIPO_NUMERAZIONE);

		//check non case sensitive
		List<String> lowerValues = new ArrayList<String>(size);
		for (String v : values)
			lowerValues.add(v.toLowerCase());

		Set<String> duplicati = new HashSet<String>(lowerValues);
		//il set non ammette duplicati, altrimenti errore
		if (duplicati.size() < size)
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_CONFIG_TIPO_NUMERAZIONE);

		if (!lowerValues.contains(num_primo_fascicolo.toLowerCase()))
			throw new ValidationException(SbnErrorTypes.PER_ERRORE_NUMERO_FASCICOLO_NON_PRESENTE);
	}

}
