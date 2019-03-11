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
package it.iccu.sbn.vo.validators.servizi;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class DatiRichiestaILLValidator extends SerializableVO implements
		Validator<DatiRichiestaILLVO> {

	private static final long serialVersionUID = -29393915716471176L;

	public void validate(DatiRichiestaILLVO target) throws ValidationException {

		MessaggioValidator mv = new MessaggioValidator();

		for (MessaggioVO msg : target.getMessaggio()) {
			msg.validate(mv);
		}

		//almaviva5_20160301 check segnatura doc. fornitrice (se non legato a inventario)
		if (target.getRuolo() == RuoloBiblioteca.FORNITRICE) {
			DocumentoNonSbnVO doc = target.getDocumento();
			if (doc != null && !target.isInventarioPresente())
				if (doc.getTipo_doc_lett() == 'P') {
					StatoIterRichiesta stato = StatoIterRichiesta.of(target.getCurrentState());
					//controllo segnatura se non si sta rifiutando la richiesta
					if (stato != StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE && !isFilled(doc.getSegnatura()))
						throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.documenti.segnatura");
				}
		}

	}

}
