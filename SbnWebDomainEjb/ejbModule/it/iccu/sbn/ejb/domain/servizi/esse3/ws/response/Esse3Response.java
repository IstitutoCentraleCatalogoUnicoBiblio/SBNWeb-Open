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
package it.iccu.sbn.ejb.domain.servizi.esse3.ws.response;

/**
 * Classe response del WebService su ESSE3<br>
 * @author Luca Ferraro Visardi
 * @version 1.0
 * @since 12/07/2018
 */
public class Esse3Response {
	private Esse3ResponseType response;
	private String sessionId;
	/**
	 * Response di Esse3
	 *
	 * @param Esse3ResponseType response
	 * @param String sessionId
	 *
	 */
	public Esse3Response(Esse3ResponseType responseCode, String sessionId) {
		super();
		this.response = responseCode;
		this.sessionId = sessionId;
	}
	/**
	 * Response di Esse3
	 *
	 * @param Integer responseCode
	 * @param String sessionId
	 *
	 */
	public Esse3Response(Integer responseCode, String sessionId) {
		super();
		this.response = Esse3ResponseType.of(responseCode);
		this.sessionId = sessionId;
	}
	/**
	 * Response di Esse3
	 *
	 * @param Esse3ResponseType response
	 *
	 */
	public Esse3Response(Esse3ResponseType response) {
		super();
		this.response = response;
		this.sessionId = "";

	}
	/**
	 * Response di Esse3
	 *
	 * @param Integer responseCode
	 *
	 */
	public Esse3Response(Integer responseCode) {
		super();
		this.response = Esse3ResponseType.of(responseCode);
		this.sessionId = "";

	}
	/**
	 * Ritorna il session Id di Esse3
	 *
	 *@return String sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * Response di Esse3
	 *
	 *@return Esse3ResponseType response
	 */
	public Esse3ResponseType getResponseKey() {
		return response;
	}
	/**
	 * Ritorna il codice di response di Esse3
	 *
	 *@return int response
	 */
	public int getResponseCode() {
		return response.getResponseCode();
	}
	/**
	 * Set della response di Esse3
	 *
	 * @param Esse3ResponseType responseCode
	 *
	 */
	public void setResponseCode(Esse3ResponseType responseCode) {
		this.response = responseCode;
	}
	/**
	 * Set della response di Esse3
	 *
	 * @param Integer responseCode
	 *
	 */
	public void setResponseCode(Integer responseCode) {
		this.response = Esse3ResponseType.of(responseCode);
	}
	/**
	 * Set del sessionID ricevuto da login su esse3
	 *
	 * @param String sessionId
	 *
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
