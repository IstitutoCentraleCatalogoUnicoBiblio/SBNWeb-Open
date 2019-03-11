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
package it.iccu.sbn.servizi.utenti.auth.impl;

import it.iccu.sbn.persistence.dao.servizi.Tbl_utentiDAO;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;

public class UserIdAuthStrategyImpl extends SbnWebUtenteLettoreLoginBaseImpl {

	protected Tbl_utenti getUtente(String userId, String password, boolean checkPassword) throws Exception {
		Tbl_utentiDAO dao = new Tbl_utentiDAO();
		Tbl_utenti utente = dao.select(userId, password, checkPassword);

		return utente;
	}

	public int priority() {
		return 1;
	}

}
