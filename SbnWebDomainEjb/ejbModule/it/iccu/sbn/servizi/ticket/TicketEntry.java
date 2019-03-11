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

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.net.InetAddress;
import java.sql.Timestamp;

public final class TicketEntry extends SerializableVO implements Comparable<TicketEntry> {

	private static final long serialVersionUID = -7581689278141409198L;
	final String ticket;
	final long firstAccess;
	long lastAccess;
	long hitCount;
	final InetAddress addr;

	public TicketEntry(String ticket, long lastAccess, InetAddress addr) {
		super();
		this.ticket = ticket;
		this.lastAccess = lastAccess;
		this.firstAccess = lastAccess;
		this.addr = addr;
		this.hitCount = 1;
	}

	public String getTicket() {
		return ticket;
	}

	public long getFirstAccess() {
		return firstAccess;
	}

	public long getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(long lastAccess) {
		this.lastAccess = lastAccess;
	}

	public String toString() {
		return ticket + ", login: " + new Timestamp(firstAccess)
				+ ", last access: " + new Timestamp(lastAccess)
				+ ", addr: " + addr.toString()
				+ ", hits: " + hitCount;
	}

	public long getHitCount() {
		return hitCount;
	}

	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}

	public long incrementHitCount() {
		return ++hitCount;
	}

	public int compareTo(TicketEntry o) {
		return (int)(firstAccess - o.firstAccess);
	}

	public InetAddress getAddr() {
		return addr;
	}

}
