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
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class PrenotazionePostoValidator extends SerializableVO implements Validator<PrenotazionePostoVO>{

	private static final long serialVersionUID = -6932234780729629924L;

	public void validate(PrenotazionePostoVO target) throws ValidationException {
		PostoSalaVO posto = target.getPosto();
		if (posto == null)
			throw new ValidationException(SbnErrorTypes.SRV_ERROR_POSTO_PRENOTAZIONE_ASSENTE);

	}

}
