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

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.ticket.TicketEntry;
import it.iccu.sbn.util.AtomicCyclicCounter;
import it.iccu.sbn.util.Base64Util;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.ConfigChangeInterceptor;
import it.iccu.sbn.util.config.Configuration;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheMBean;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.system.ServiceMBeanSupport;


public class ClusteredTicketChecker extends ServiceMBeanSupport implements ClusteredTicketCheckerMBean, ConfigChangeInterceptor {

	private final class Checker implements Runnable {

		private final ClusteredTicketChecker service;
		private final long timeout;
		private final AtomicBoolean interrupted;
		private long loop;

		public Checker(ClusteredTicketChecker service, long timeout) {
			this.service = service;
			this.timeout = timeout;
			this.interrupted = new AtomicBoolean(false);
		}

		public void interrupt() {
			log.info("ClusteredTicketCheck interrupted");
			interrupted.set(true);
		}

		public void run() {
			log.debug("TicketChecker Started");
			while (!interrupted.get())
				try {
					Thread.sleep(timeout);
					service.checkAll(++loop);
				} catch (InterruptedException e) {
					log.error("", e);
					this.interrupt();
				} catch (Exception e) {
					log.error("", e);
					if (!Thread.currentThread().isInterrupted())
						continue;
					this.interrupt();
				}
				log.debug("TicketChecker Stopped");
		}
	} // Checker;

	private static final int CHECKER_TIMEOUT = 10000;
	private static final String CACHE_SERVICE_NAME = "sbn.service:name=TicketClusteredCache";
	private static final Fqn NODE_PREFIX_CURRENT = Fqn.fromString("it/iccu/sbn/cache/ticket/current");
	private static final Fqn NODE_PREFIX_HISTORY = Fqn.fromString("it/iccu/sbn/cache/ticket/history");
	private static final Fqn NODE_PREFIX_COUNTER = Fqn.fromString("it/iccu/sbn/cache/ticket/counter");
	private static final String COUNTER = "ticket.counter";

	private static final String[] OBSERVED_PROPS = new String[] {
		Configuration.SBNWEB_HTTP_SESSION_TIMEOUT,
		Configuration.SERVIZI_HTTP_SESSION_TIMEOUT
	};


	private static final Random idLoginGenerator = new java.security.SecureRandom();
	private static TreeCache _cache;

	private static Logger log = Logger.getLogger(ClusteredTicketChecker.class);

	private Lock lock;
	private int mSessionTimeout = 30; //minuti
	private AtomicLong realTimeout = new AtomicLong( (long) (Math.ceil(mSessionTimeout * 1.3) * 60 * 1000)); // +30%
	private Checker checker;


	private static final TreeCache getCache() {

		if (_cache != null)
			return _cache;

		MBeanServer mbserver = MBeanServerLocator.locateJBoss();
		ObjectName name;
		try {
			name = new ObjectName(CACHE_SERVICE_NAME);
			TreeCacheMBean intf = (TreeCacheMBean) MBeanProxyExt.create(TreeCacheMBean.class, name, mbserver);
			_cache = intf.getInstance();
			return _cache;

		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private static final Collection<?> getOrderedNodeChildren(Fqn prefix) throws CacheException {

		Collection<TicketEntry> children = (Collection<TicketEntry>) getNodeChildren(prefix);
		if (children == null)
			return null;

		TreeSet<TicketEntry> orderedSet = new TreeSet<TicketEntry>(children);

		return orderedSet;
	}

	private static final Collection<?> getNodeChildren(Fqn prefix) throws CacheException {
		Node node = getCache().get(prefix);
		if (node == null)
			return null;

		Map<?, ?> children = node.getData();
		if (!ValidazioneDati.isFilled(children))
			return null;

		return children.values();
	}

	public int getSessionTimeout() {
		return mSessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		log.info("=== sessionTimeout = " + sessionTimeout + " minuti.");
		this.mSessionTimeout = sessionTimeout;
		this.realTimeout.set( (long) (Math.ceil(mSessionTimeout * 1.3) * 60 * 1000)); // +30%
	}

	public void startService() throws Exception {

		log.info("=== SchedulableTicketCheck.startService()");
		srvStart();
	}

	private void srvStart() {
		lock = new ReentrantLock();
		CommonConfiguration.addInterceptor(this, OBSERVED_PROPS);
		//almaviva5_20121206 #5118
		try {
			onConfigReload(null);
		} catch (Exception e) {
			log.error("", e);
		}
		startChecker();
	}

	private void startChecker() {
		if (checker != null && !checker.interrupted.get()) // già attivo
			return;

		checker = new Checker(this, CHECKER_TIMEOUT);
		Thread thread = new Thread(checker, "ClusteredTicketCheck@CheckThread");
		thread.start();
	}

	private void srvStop() {
		lock.lock();
		try {
			if (checker != null)
				checker.interrupt();
		} finally {
			lock.unlock();
		}
	}

	public void stopService() throws Exception {
		log.info("=== ClusteredTicketCheck.stopService()");
		srvStop();
	}

	public String generateUniqueTicket(String codPolo, String codBib, String userId) {
		lock.lock();
		try {
			TreeCache cache = getCache();
			AtomicCyclicCounter counter = (AtomicCyclicCounter) cache.get(NODE_PREFIX_COUNTER, COUNTER);
			if (counter == null)
				counter = new AtomicCyclicCounter();

			StringBuilder ticket = new StringBuilder();
			ticket.append(codPolo);
			ticket.append('_');
			ticket.append(codBib);
			ticket.append('_');
			ticket.append(Base64Util.encode(userId.getBytes()));
			ticket.append('_');
			ticket.append(DateUtil.formattaDataCompleta(System.currentTimeMillis()));
			ticket.append(String.format("%06d", idLoginGenerator.nextInt(999999) ));
			ticket.append('_');
			ticket.append(userId);
			ticket.append('_');
			ticket.append(counter.getNextValue());

			cache.put(NODE_PREFIX_COUNTER, COUNTER, counter);

			return ticket.toString();

		} catch (CacheException e) {
			log.error("", e);
		} finally {
			lock.unlock();
		}

		return null;
	}

	public void addTicket(String ticket, InetAddress addr) {
		lock.lock();
		try {
			TicketEntry entry = new TicketEntry(ticket, System.currentTimeMillis(), addr);
			try {
				TreeCache cache = getCache();
				cache.put(NODE_PREFIX_CURRENT, ticket, entry);
				cache.put(NODE_PREFIX_HISTORY, ticket, entry);
			} catch (CacheException e) {
				log.error("", e);
			}
		} finally {
			lock.unlock();
		}
	}

	public void removeTicket(String ticket) {
		lock.lock();
		try {
			try {
				getCache().remove(NODE_PREFIX_CURRENT, ticket);
			} catch (CacheException e) {
				log.error("", e);
			}
			log.info(ticket + " rimosso.");
		} finally {
			lock.unlock();
		}
	}

	public boolean checkTicket(String ticket) {

		lock.lock();
		try {
			TreeCache cache = getCache();
			TicketEntry t = (TicketEntry) cache.get(NODE_PREFIX_CURRENT, ticket);
			if (t == null)
				return false;

			t.incrementHitCount();
			long now = System.currentTimeMillis();
			if (isExpired(t, now, realTimeout.get())) {
				log.info(t + " expired.");
				cache.remove(NODE_PREFIX_CURRENT, ticket);
				return false;
			}

			t.setLastAccess(now);
			cache.put(NODE_PREFIX_CURRENT, ticket, t);
			cache.put(NODE_PREFIX_HISTORY, ticket, t);
			return true;

		} catch (CacheException e) {
			log.error("", e);
		} finally {
			startChecker();
			lock.unlock();
		}

		return false;
	}

	private void checkAll(long loop) {
		lock.lock();
		try {
			Collection<?> children = getNodeChildren(NODE_PREFIX_CURRENT);
			if (!ValidazioneDati.isFilled(children))
				return;

			TreeCache cache = getCache();

			Iterator<?> i = children.iterator();
			long now = System.currentTimeMillis();
			long timeout = realTimeout.get();

			while (i.hasNext() ) {
				TicketEntry t = (TicketEntry) i.next();
				t.incrementHitCount();
				String ticket = t.getTicket();
				if (isExpired(t, now, timeout)) {
					log.info(t + " expired.");
					cache.remove(NODE_PREFIX_CURRENT, ticket);
				}
				else {
					cache.put(NODE_PREFIX_CURRENT, ticket, t);
					cache.put(NODE_PREFIX_HISTORY, ticket, t);
				}
			}

			//almaviva5_20111220
			if ((loop % CHECKER_TIMEOUT) == 0) {
				children = getNodeChildren(NODE_PREFIX_HISTORY);
				if (!ValidazioneDati.isFilled(children))
					return;

				log.debug("pulizia history ticket.");

				i = children.iterator();
				Timestamp tsNow = DaoManager.now();
				while (i.hasNext() ) {
					TicketEntry t = (TicketEntry) i.next();
					Timestamp tsLast = new Timestamp(t.getLastAccess());
					if (DateUtil.diffDays(tsNow, tsLast) > 15 )
						cache.remove(NODE_PREFIX_HISTORY, t.getTicket() );
				}

			}
		} catch (CacheException e) {
			log.error("", e);
		} finally {
			lock.unlock();
		}

	}

	private static final boolean isExpired(TicketEntry t, long now, long timeout) {

		long limit = t.getLastAccess() + timeout;
		return (limit < now);
	}

	public int userLoggedCount() {
		lock.lock();
		try {
			Collection<?> children = getNodeChildren(NODE_PREFIX_CURRENT);
			if (!ValidazioneDati.isFilled(children))
				return 0;

			return children.size();

		} catch (CacheException e) {
			log.error("", e);
		} finally {
			lock.unlock();
		}
		return 0;
	}

	public String listAllTickets() {
		StringBuffer buf = new StringBuffer();
		lock.lock();
		try {
			Collection<?> children = getOrderedNodeChildren(NODE_PREFIX_CURRENT);
			if (!ValidazioneDati.isFilled(children))
				return "";

			int loggedCount = children.size();
			if (loggedCount < 1)
				return "";

			int pad = String.valueOf(loggedCount).length();
			int count = 0;

			for (Object o : children) {
				TicketEntry t = (TicketEntry) o;
				buf.append(String.format("%0" + pad + "d: %s\n", ++count, t.toString()));
			}

		} catch (CacheException e) {
			log.error("", e);
		} finally {
			lock.unlock();
		}
		return buf.toString();
	}

	public String ticketHistory() {
		StringBuffer buf = new StringBuffer();
		lock.lock();
		try {
			Collection<?> children = getOrderedNodeChildren(NODE_PREFIX_HISTORY);
			if (!ValidazioneDati.isFilled(children))
				return "";

			int historyCount = children.size();
			if (historyCount < 1)
				return "";

			int pad = String.valueOf(historyCount).length();
			int count = 0;

			for (Object o : children) {
				TicketEntry t = (TicketEntry) o;
				buf.append(String.format("%0" + pad + "d: %s\n", ++count, t.toString()));
			}

		} catch (CacheException e) {
			log.error("", e);
		} finally {
			lock.unlock();
		}
		return buf.toString();
	}

	public int ticketHistoryCount() {
		lock.lock();
		try {
			Collection<?> children = getNodeChildren(NODE_PREFIX_HISTORY);
			if (!ValidazioneDati.isFilled(children))
				return 0;

			return children.size();

		} catch (CacheException e) {
			log.error("", e);
		} finally {
			lock.unlock();
		}
		return 0;
	}

	public void singletonStart() {
		log.info("=== ClusteredTicketCheck.singletonStart()");
		srvStart();
	}

	public void singletonStop() {
		log.info("=== ClusteredTicketCheck.singletonStop()");
		srvStop();
	}

	public void onConfigPropertyChange(String key) throws Exception {
		//almaviva5_20111220
		onConfigReload(null);
	}

	public void onConfigReload(Set<String> changedProperties) throws Exception {
		//almaviva5_20111220
		int webSessionTimeout = CommonConfiguration.getPropertyAsInteger(Configuration.SBNWEB_HTTP_SESSION_TIMEOUT, 30);
		int srvSessionTimeout = CommonConfiguration.getPropertyAsInteger(Configuration.SERVIZI_HTTP_SESSION_TIMEOUT, 30);

		//almaviva5_20121206 #5118
		int max = Math.max(webSessionTimeout, srvSessionTimeout);
		if (max != mSessionTimeout)
			setSessionTimeout(max);
	}

}
