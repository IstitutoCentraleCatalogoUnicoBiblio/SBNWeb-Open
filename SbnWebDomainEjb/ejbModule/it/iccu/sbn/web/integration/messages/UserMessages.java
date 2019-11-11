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
package it.iccu.sbn.web.integration.messages;

import it.iccu.sbn.web.vo.UserMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class UserMessages {

	private static Map<String, List<UserMessage>> userMessages = new ConcurrentHashMap<String, List<UserMessage>>();

	public static List<UserMessage> get(String ticket) {
		if (!isFilled(ticket))
			return new ArrayList<UserMessage>();

		List<UserMessage> messages = userMessages.get(ticket);
		if (messages == null) {
			messages = new ArrayList<UserMessage>();
			userMessages.put(ticket, messages);
		}
		return messages;
	}

	public static void remove(String ticket) {
		if (isFilled(ticket))
			userMessages.remove(ticket);
	}
}