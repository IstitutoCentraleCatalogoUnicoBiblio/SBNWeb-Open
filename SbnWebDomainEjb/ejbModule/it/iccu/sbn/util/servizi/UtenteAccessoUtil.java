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
package it.iccu.sbn.util.servizi;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.utenti.EventoAccessoVO.TipoEvento;
import it.iccu.sbn.web.vo.SbnErrorTypes;

public class UtenteAccessoUtil {

	/**
	 * Check compatibilità tra due eventi di accesso
	 * @param _old il primo evento
	 * @param _new il secondo evento o {@code null}
	 * @return {@link TipoEvento} da associare al nuovo evento
	 * @throws ValidationException
	 */
	public static TipoEvento checkEvento(TipoEvento _old, TipoEvento _new) throws ValidationException {
		if (_old == null || _old == _new)
			throw new ValidationException(SbnErrorTypes.SRV_TIPO_ACCESSO_UTENTE_NON_VALIDO);

		return ValidazioneDati.coalesce(getEventoInverso(_old), _new);
	}

	public static TipoEvento getEventoInverso(TipoEvento _old) {
		switch (_old) {
		case ENTRATA:
			return TipoEvento.USCITA;

		case USCITA:
			return TipoEvento.ENTRATA;
		}

		return null;
	}

}
