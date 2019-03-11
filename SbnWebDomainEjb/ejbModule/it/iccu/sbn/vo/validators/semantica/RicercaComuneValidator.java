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
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.semantica.SemanticaUtil;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class RicercaComuneValidator extends SerializableVO implements Validator<RicercaComuneVO> {

	private static final long serialVersionUID = -7469181445349636254L;

	public void validate(RicercaComuneVO target) throws ValidationException {
		try {
			target.validate();

			String codSoggettario = target.getCodSoggettario();
			String edizione = target.getEdizioneSoggettario();

			if (isFilled(codSoggettario)) {
				TB_CODICI cod = CodiciProvider.cercaCodice(codSoggettario,
						CodiciType.CODICE_SOGGETTARIO, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod == null)
					throw new ValidationException("Parametro codice soggettario errato");

				String soggUnimarc = cod.getCd_unimarc();

				if (isFilled(edizione)) {
					//almaviva5_20111125 evolutive CFI
					boolean gestisceEdizione = ValidazioneDati.equals(cod.getCd_flg2(), "S");
					if (!gestisceEdizione)
						throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "ricerca.edizione");

					soggUnimarc = target.isPolo() ?
							soggUnimarc :
							SemanticaUtil.getSoggettarioIndiceDaEdizione(edizione);
				}

				//edizione non prevista
				if (!isFilled(soggUnimarc))
					throw new ValidationException("Parametro codice soggettario errato");

				target.setCodSoggettario(soggUnimarc);

				//almaviva5_20120801 fix edizione per ricerca in indice
				if (target.isIndice() && !isFilled(edizione))
					target.setCodSoggettario(null);

			}

			if (!isFilled(codSoggettario) && isFilled(edizione))
				throw new ValidationException(SbnErrorTypes.GS_CODICE_SOGGETTARIO_OBBLIGATORIO_PER_EDIZIONE);


		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.UNRECOVERABLE);
		}

	}

}
