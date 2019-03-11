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
package it.iccu.sbn.vo.validators.acquisizioni;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class OrdineCarrelloSpedizioneValidator extends SerializableVO implements
		Validator<OrdineCarrelloSpedizioneVO> {

	private static final long serialVersionUID = 2882122843828296110L;
	private final OrdiniVO ordine;

	public OrdineCarrelloSpedizioneValidator(OrdiniVO ordine) {
		this.ordine = ordine;
	}

	public void validate(OrdineCarrelloSpedizioneVO target) throws ValidationException {
		if (target.getDataSpedizione() == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "ordine.label.dataInvio");

		if (ordine.isGoogle()) {
			if (target.getPrgSpedizione() < 1)
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "ordine.label.prgSpedizione");
			if (!isFilled(target.getCartName()))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "ordine.label.cartName");
		}

	}

}
