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
package it.iccu.sbn.servizi.ticket.clustered;

import java.net.InetAddress;

import org.jboss.system.ServiceMBean;

public interface ClusteredTicketCheckerMBean extends ServiceMBean {

	public int getSessionTimeout();
	public void setSessionTimeout(int sessionTimeout);

	public void addTicket(String ticket, InetAddress addr);
	public void removeTicket(String ticket);
	public boolean checkTicket(String ticket);
	public int userLoggedCount();
	public String listAllTickets();
	public String ticketHistory();
	public int ticketHistoryCount();
	public String generateUniqueTicket(String codPolo, String codBib, String userId);

	public void singletonStart();
	public void singletonStop();

}
