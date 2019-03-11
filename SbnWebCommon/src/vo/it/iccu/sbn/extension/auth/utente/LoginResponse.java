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
package it.iccu.sbn.extension.auth.utente;

import java.sql.Timestamp;

public interface LoginResponse {

	public enum LoginResult {
		LOGGED,
		DISABLED,
		AUTH_DATA_EXPIRED,
		NOT_FOUND,
		UNSUPPORTED;
	}

	LoginResult getStatus();
	UtenteData getUtente();
	Timestamp getWhen();
	UserSessionInfo getUserSessionInfo();
}
