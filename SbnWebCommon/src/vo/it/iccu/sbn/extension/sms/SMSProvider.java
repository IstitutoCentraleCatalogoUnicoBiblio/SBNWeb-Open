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
package it.iccu.sbn.extension.sms;

/**
 * Interfaccia per l'implementazione di un provider di invio SMS
 * @author iccu
 *
 */

public interface SMSProvider {

	/**
	 * Invoca il provider per l'invio di un messaggio sms.
	 * @param sender Il numero mittente
	 * @param receiver Il numero destinatario
	 * @param text Il testo del messaggio
	 * @param rcvNotify Notifica di ricevimento (se supportata dal provider)
	 * @return un oggetto {@link SMSResult} con l'esito dell'operazione
	 * @throws Exception
	 *
	 * @see SMSResult
	 */
	public SMSResult send(String sender, String receiver, String text, boolean rcvNotify) throws Exception;


}
