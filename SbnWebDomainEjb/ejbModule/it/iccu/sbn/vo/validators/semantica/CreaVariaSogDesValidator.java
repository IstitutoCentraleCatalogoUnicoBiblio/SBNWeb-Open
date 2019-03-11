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
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSogDesBaseVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.semantica.SemanticaUtil;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class CreaVariaSogDesValidator extends SerializableVO implements Validator<CreaVariaSogDesBaseVO> {

	private static final long serialVersionUID = 9109201997600954506L;

	public void validate(CreaVariaSogDesBaseVO target) throws ValidationException {

		try {
			TB_CODICI cod = CodiciProvider.cercaCodice(target.getCodiceSoggettario(),
					CodiciType.CODICE_SOGGETTARIO, CodiciRicercaType.RICERCA_CODICE_SBN);
			if (cod == null)
				throw new ValidationException("Parametro codice soggettario errato");

			//almaviva5_20111125 evolutive CFI
			String edizioneSoggettario = target.getEdizioneSoggettario();
			boolean gestisceEdizione = ValidazioneDati.equals(cod.getCd_flg2(), "S");
			//edizione prevista
			if (gestisceEdizione && !isFilled(edizioneSoggettario))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "ricerca.edizione");

			//edizione non prevista
			if (!gestisceEdizione && isFilled(edizioneSoggettario))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_UNMANAGED_FIELD, "ricerca.edizione");

			if (!target.isLivelloPolo()) {
				//se il soggetto è in indice devo tradurre
				//l'edizione in un codice soggettario apposito
				target.setCodiceSoggettario(SemanticaUtil.getSoggettarioIndiceDaEdizione(edizioneSoggettario));
			} else {
				String soggUnimarc = cod.getCd_unimarc();
				if (!isFilled(soggUnimarc))
					throw new ValidationException("Parametro codice soggettario errato");
				target.setCodiceSoggettario(soggUnimarc);
			}

		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.UNRECOVERABLE);
		}

	}

}
