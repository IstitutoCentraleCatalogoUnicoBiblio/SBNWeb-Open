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

import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.servizi.ticket.clustered.ClusteredTicketCheckerMBean;

import java.net.InetAddress;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;

public abstract class TicketChecker implements SessionBean, EJBObject {

	private static final long serialVersionUID = 3218981899462003802L;

	private static final String SERVICE_NAME = "sbn:service=ClusteredTicketChecker";
	private static ClusteredTicketCheckerMBean checker = null;

	private static final ClusteredTicketCheckerMBean getChecker() {
		if (checker != null)
			return checker;

		try {
			MBeanServer mbserver = MBeanServerLocator.locateJBoss();
			ObjectName name = new ObjectName(SERVICE_NAME);
			checker = (ClusteredTicketCheckerMBean) MBeanProxyExt.create(ClusteredTicketCheckerMBean.class, name, mbserver);
			return checker;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected static final void addTicket(String ticket, InetAddress addr) {
		getChecker().addTicket(ticket, addr);
	}

	protected static final void removeTicket(String ticket) {
		getChecker().removeTicket(ticket);
	}

	protected static final void checkTicket(String ticket) throws TicketExpiredException {

		if (ticket == null || getChecker().checkTicket(ticket) )
			return;

		throw new TicketExpiredException();
	}

	protected static final String generateUniqueTicket(String codPolo, String codBib, String userId) {
		return getChecker().generateUniqueTicket(codPolo, codBib, userId);
	}

	public EJBLocalHome getEJBLocalHome() throws EJBException {
		return null;
	}

	public Object getPrimaryKey() throws EJBException {
		return null;
	}

	public void remove() throws RemoveException, EJBException {}

	public boolean isIdentical(EJBLocalObject arg0) throws EJBException {
		return false;
	}

	public void ejbActivate() throws EJBException, RemoteException {}

	public void ejbPassivate() throws EJBException, RemoteException {}

	public void ejbRemove() throws EJBException, RemoteException {}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {}

	public void ejbCreate() throws CreateException {}

	public EJBHome getEJBHome() throws RemoteException {
		return null;
	}

	public Handle getHandle() throws RemoteException {
		return null;
	}

	public boolean isIdentical(EJBObject ejb) throws RemoteException {
		return this == ejb;
	}

}
