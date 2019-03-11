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
package it.iccu.sbn.servizi.ticket;

import gnu.trove.THashMap;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.system.ServiceMBeanSupport;

public class SchedulableTicketCheck extends ServiceMBeanSupport implements
		SchedulableTicketCheckMBean {

	private final class Checker implements Runnable {

		private final SchedulableTicketCheck service;
		private final long timeout;
		private final AtomicBoolean interrupted;

		public Checker(SchedulableTicketCheck service, long timeout) {
			this.service = service;
			this.timeout = timeout;
			interrupted = new AtomicBoolean(false);
		}

		public void interrupt() {
			log.info("SchedulableTicketCheck interrupted");
			interrupted.set(true);
		}

		public void run() {
			while (!interrupted.get())
				try {
					Thread.sleep(timeout);
					service.checkAll();
				} catch (InterruptedException e) {
					e.printStackTrace();
					this.interrupt();
				}
		}
	} // Checker;


	private Map<String, TicketEntry> tickets;
	private List<TicketEntry> ticketHistory;
	private Lock lock;
	private int mSessionTimeout = 30; //minuti
	private long realTimeout = mSessionTimeout * 60 * 1000;
	private Checker checker;

	public int getSessionTimeout() {
		return mSessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		log.info("=== sessionTimeout = " + sessionTimeout + "minuti.");
		this.mSessionTimeout = sessionTimeout;
		this.realTimeout = mSessionTimeout * 60 * 1000;
	}

	public void startService() throws Exception {

		log.info("=== SchedulableTicketCheck.startService()");

		tickets = new THashMap<String, TicketEntry>();
		ticketHistory = new LinkedList<TicketEntry>();
		lock = new ReentrantLock();

		checker = new Checker(this, 10000);
		Thread thread = new Thread(checker, "SchedulableTicketCheck@CheckThread");
		thread.start();
	}

	public void stopService() throws Exception {
		log.info("=== SchedulableTicketCheck.stopService()");
		lock.lock();
		try {
			checker.interrupt();
			tickets.clear();
			ticketHistory.clear();
		} finally {
			lock.unlock();
		}

	}

	public void addTicket(String ticket, InetAddress addr) {
		lock.lock();
		try {
			TicketEntry entry = new TicketEntry(ticket, System.currentTimeMillis(), addr);
			tickets.put(ticket, entry);
			ticketHistory.add(entry);
		} finally {
			lock.unlock();
		}
	}

	public void removeTicket(String ticket) {
		lock.lock();
		try {
			tickets.remove(ticket);
			log.info(ticket + " rimosso.");
		} finally {
			lock.unlock();
		}
	}

	public boolean checkTicket(String ticket) {

		lock.lock();
		try {
			TicketEntry t = tickets.get(ticket);
			if (t == null)
				return false;

			t.hitCount++;
			long now = System.currentTimeMillis();
			if (isExpired(t, now, realTimeout)) {
				log.info(t + " expired.");
				tickets.remove(t.getTicket());
				return false;
			}

			t.setLastAccess(now);
			return true;

		} finally {
			lock.unlock();
		}
	}

	private void checkAll() {
		lock.lock();
		try {
			Iterator<TicketEntry> iterator = tickets.values().iterator();
			long now = System.currentTimeMillis();
			while (iterator.hasNext() ) {
				TicketEntry t = iterator.next();
				t.hitCount++;
				if (isExpired(t, now, realTimeout)) {
					log.info(t + " expired.");
					iterator.remove();
				}
			}
		} finally {
			lock.unlock();
		}

	}

	private static final boolean isExpired(TicketEntry t, long now, long timeout) {

		long limit = t.lastAccess + timeout;
		return (limit < now);
	}

	public int userLoggedCount() {
		lock.lock();
		try {
			if (tickets != null)
				return tickets.size();

		} finally {
			lock.unlock();
		}
		return 0;
	}

	public String listAllTickets() {
		StringBuffer buf = new StringBuffer();
		lock.lock();
		try {
			if (tickets != null) {
				int pad = String.valueOf(tickets.size()).length();
				int count = 0;
				for (TicketEntry t : tickets.values())
					buf.append(String.format("%0" + pad + "d: %s\n", ++count, t.toString()));
			}

		} finally {
			lock.unlock();
		}
		return buf.toString();
	}

	public String ticketHistory() {
		StringBuffer buf = new StringBuffer();
		lock.lock();
		try {
			if (ticketHistory != null) {
				int pad = String.valueOf(ticketHistory.size()).length();
				int count = 0;
				for (TicketEntry t : ticketHistory)
					buf.append(String.format("%0" + pad + "d: %s\n", ++count, t.toString()));
			}

		} finally {
			lock.unlock();
		}
		return buf.toString();
	}

	public int ticketHistoryCount() {
		lock.lock();
		try {
			if (ticketHistory != null)
				return ticketHistory.size();

		} finally {
			lock.unlock();
		}
		return 0;
	}

	public String generateUniqueTicket(String codPolo, String codBib,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
