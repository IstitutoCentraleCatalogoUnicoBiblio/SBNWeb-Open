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
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.OrdineCarrelloSpedizioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.StampaShippingManifestVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class StampaShippingManifestValidator extends SerializableVO implements
		Validator<StampaShippingManifestVO> {

	private static final long serialVersionUID = -3587513777065502238L;

	public void validate(StampaShippingManifestVO target)
			throws ValidationException {
		target.validate();

		OrdineCarrelloSpedizioneVO ocs = target.getOrdineCarrelloSpedizione();

		if (ocs.getDataSpedizione() == null)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "ordine.label.dataInvio");

		if (ocs.getPrgSpedizione() < 0)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "ordine.label.prgSpedizione");

	}

}
