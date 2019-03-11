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
package it.iccu.sbn.util.sms.impl;

import it.iccu.sbn.extension.sms.SMSProvider;
import it.iccu.sbn.extension.sms.SMSResult;

import org.apache.log4j.Logger;

public class DummySMSProvider implements SMSProvider {

	private static Logger log = Logger.getLogger(SMSProvider.class);

	public SMSResult send(String sender, String rcvNumber, String text,	boolean rcvNotify) throws Exception {
		log.warn("invio sms non implementato.");
		SMSResult result = new SMSResult(false);
		result.setMessage("invio sms non implementato.");
		return result;
	}

}
