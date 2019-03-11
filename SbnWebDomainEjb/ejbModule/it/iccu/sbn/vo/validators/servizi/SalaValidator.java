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
import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.util.matchers.Sale;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.List;

import com.annimon.stream.Stream;

public class SalaValidator extends SerializableVO implements Validator<SalaVO>{

	private static final long serialVersionUID = -5871207762857427334L;

	public void validate(SalaVO target) throws ValidationException {
		if (!isFilled(target.getCodSala()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.sale.codiceSala");

		if (!isFilled(target.getDescrizione()))
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.sale.descrizione");

		short numeroPosti = target.getNumeroPosti();
		if (numeroPosti < 1)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "servizi.sale.numPostiTotale");

		short maxUtentiPerPrenotazione = target.getMaxUtentiPerPrenotazione();
		if (maxUtentiPerPrenotazione < 1)
			throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "servizi.sale.maxUtentiPerPrenotazione");

		if (!target.isNuovo()) {

			List<GruppoPostiVO> gruppi = target.getGruppi();
			Stream<GruppoPostiVO> s = Stream.of(gruppi);
			int totalePosti = 0;
			for (GruppoPostiVO gp : gruppi) {
				int posto_a = gp.getPosto_a();
				int posto_da = gp.getPosto_da();

				if (posto_a < posto_da)
					throw new ValidationException(SbnErrorTypes.SRV_SALA_INTERVALLO_POSTI_ERRATO);

				if (posto_a > numeroPosti)
					throw new ValidationException(SbnErrorTypes.SRV_SALA_INTERVALLO_POSTI_ERRATO);

				//cerca gruppo sovrapposto
				if (s.anyMatch(Sale.gruppoOverlaps(gp)))
					throw new ValidationException(SbnErrorTypes.SRV_SALA_GRUPPO_POSTI_SOVRAPPOSTO);

				totalePosti += (posto_a - posto_da) + 1;
			}
			//check totale posti
			if (totalePosti != numeroPosti)
				throw new ValidationException(SbnErrorTypes.SRV_SALA_TOTALE_POSTI_ERRATO);
		}

	}

}
