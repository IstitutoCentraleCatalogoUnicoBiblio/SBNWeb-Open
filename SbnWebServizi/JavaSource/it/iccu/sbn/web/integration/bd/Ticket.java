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
package it.iccu.sbn.web.integration.bd;

import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.web2.util.Constants;

import java.net.InetAddress;

public class Ticket extends TicketChecker {

	private static final long serialVersionUID = 6116051742225956123L;

	public static final String getUtenteWebTicket(String codPolo, String codBib, InetAddress addr) {
		String ticket = generateUniqueTicket(codPolo, codBib, Constants.UTENTE_WEB_TICKET);
		addTicket(ticket, addr);
		return ticket;
	}
}
