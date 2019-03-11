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
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class MessaggioValidator extends SerializableVO implements Validator<MessaggioVO> {

	private static final long serialVersionUID = -5330333862825190521L;

	public void validate(MessaggioVO msg) throws ValidationException {
		StatoIterRichiesta stato = StatoIterRichiesta.of(msg.getStato());
		if (stato == null)
			throw new ValidationException(SbnErrorTypes.SRV_ERRORE_CONFIGURAZIONE_ITER);

		String note = msg.getNote();
		if (!isFilled(note) && stato.withNotes() )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.erogazione.ill.messaggio");

		String requesterId = msg.getRequesterId();
		if (!isFilled(requesterId) )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "requesterId");

		String responderId = msg.getResponderId();
		if (!isFilled(responderId) )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "responderId");

		/*
		String condizione = msg.getCondizione();
		if (!isFilled(condizione) && stato.withCondition() )
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.erogazione.ill.condizione");
		*/
	}

}
