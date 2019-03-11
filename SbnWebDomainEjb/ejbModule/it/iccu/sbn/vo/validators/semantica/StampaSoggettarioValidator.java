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
package it.iccu.sbn.vo.validators.semantica;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionesemantica.stampe.ParametriStampaSoggettarioVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class StampaSoggettarioValidator extends SerializableVO implements
		Validator<ParametriStampaSoggettarioVO> {

	private static final long serialVersionUID = -3378974526603805920L;

	public void validate(ParametriStampaSoggettarioVO target) throws ValidationException {

		target.validate();

		String codSoggettario = target.getCodSoggettario();
		if (isNull(codSoggettario))
			throw new ValidationException(SbnErrorTypes.GS_CODICE_SOGGETTARIO_OBBLIGATORIO);

		TB_CODICI cod = null;
		try {
			cod = CodiciProvider.cercaCodice(codSoggettario,
					CodiciType.CODICE_SOGGETTARIO,
					CodiciRicercaType.RICERCA_CODICE_SBN);
		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
		}
		if (cod == null)
			throw new ValidationException("Parametro codice soggettario errato");

		// almaviva5_20111125 evolutive CFI
		String edizioneSoggettario = target.getEdizioneSoggettario();
		boolean gestisceEdizione = ValidazioneDati.equals(cod.getCd_flg2(), "S");
		// edizione non prevista
		if (!gestisceEdizione && isFilled(edizioneSoggettario))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "ricerca.edizione");

		String ky_from = ValidazioneDati.trimOrEmpty(OrdinamentoUnicode.getInstance().convert(target.getTestoFrom()));
		String ky_to = ValidazioneDati.trimOrEmpty(OrdinamentoUnicode.getInstance().convert(target.getTestoTo()));
		if (ValidazioneDati.isFilled(ky_from) || ValidazioneDati.isFilled(ky_to))
			if (ky_from.compareTo(ky_to) > -1 )
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "soggettario.label.testo.da");


	}

}
