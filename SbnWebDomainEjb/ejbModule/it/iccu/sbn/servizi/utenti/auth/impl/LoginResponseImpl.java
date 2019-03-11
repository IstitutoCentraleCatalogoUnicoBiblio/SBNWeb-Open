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

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.extension.auth.utente.LoginResponse;
import it.iccu.sbn.extension.auth.utente.UserSessionInfo;
import it.iccu.sbn.extension.auth.utente.UtenteData;
import it.iccu.sbn.persistence.dao.common.DaoManager;

import java.sql.Timestamp;

public class LoginResponseImpl extends SerializableVO implements LoginResponse {

	private static final long serialVersionUID = -2384029165324103767L;

	private final LoginResult status;
	private final UtenteBaseVO utente;
	private final Timestamp when;
	private final UserSessionInfo userSessionInfo;

	public LoginResponseImpl(LoginResult status, UtenteBaseVO utente, UserSessionInfo userSessionInfo) {
		this.status = status;
		this.utente = utente;
		this.userSessionInfo = userSessionInfo;
		this.when = DaoManager.now();
	}

	public LoginResult getStatus() {
		return status;
	}

	public UtenteData getUtente() {
		return utente;
	}

	public Timestamp getWhen() {
		return when;
	}

	public UserSessionInfo getUserSessionInfo() {
		return userSessionInfo;
	}

}
