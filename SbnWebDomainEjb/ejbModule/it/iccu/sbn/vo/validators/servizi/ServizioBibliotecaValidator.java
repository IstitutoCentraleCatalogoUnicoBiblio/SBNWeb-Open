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
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import org.joda.time.LocalTime;

public class ServizioBibliotecaValidator extends SerializableVO implements Validator<ServizioBibliotecaVO> {

	private static final long serialVersionUID = -8851468254979619837L;

	public void validate(ServizioBibliotecaVO target) throws ValidationException {

		String orarioLimitePrepSupp = target.getOrarioLimitePrepSupp();
		boolean withOrarioLimite = isFilled(orarioLimitePrepSupp);
		if (withOrarioLimite) {
			try {
				LocalTime.parse(orarioLimitePrepSupp);
			} catch (IllegalArgumentException e) {
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "servizi.configurazione.dettaglioServizio.orarioLimitePrepSupp");
			}
		}

		Short numGgPrepSupp = target.getNumGgPrepSupp();
		if (isFilled(numGgPrepSupp) && !withOrarioLimite) {
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.configurazione.dettaglioServizio.orarioLimitePrepSupp");
		}
	}

}
