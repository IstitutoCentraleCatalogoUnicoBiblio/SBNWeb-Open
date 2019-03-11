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
package it.finsiel.sbn.util;

import it.finsiel.sbn.exception.InfrastructureException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateUtil {
	static Category log = Category
			.getInstance("it.finsiel.sbn.util.HibernateUtil");

	private static SessionFactory sessionFactory;

	private static final ThreadLocal threadTransaction = new ThreadLocal();

	public static final ThreadLocal session = new ThreadLocal();

	static {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			sessionFactory  = (SessionFactory) ctx.lookup("java:/hibernate/SessionFactory");
		} catch (NamingException e) {
			log.fatal("Creazione iniziale di una SessionFactory Hibernate fallita",e);
		}
		/*
		try {
			// Create the SessionFactory
			sessionFactory = new Configuration().configure("SbnDB.cfg.xml")
					.buildSessionFactory();
		} catch (Throwable ex) {
			log
					.fatal(
							"Creazione iniziale di una SessionFactory Hibernate fallita",
							ex);
			throw new ExceptionInInitializerError(ex);
		}
		*/
	}

	public static Session getSession() throws InfrastructureException {
		Session s = (Session) session.get();
		// Open a new Session, if this Thread has none yet
		if (s == null) {
			try {
				s = sessionFactory.openSession();
				session.set(s);
			} catch (HibernateException e) {
				throw new InfrastructureException(e);
			}
		}
		return s;
	}
	public static Session getSession(Interceptor interceptor ) throws InfrastructureException {
		Session s = (Session) session.get();
		// Open a new Session, if this Thread has none yet
		if (s == null) {
			try {
				s = sessionFactory.openSession(interceptor);
				session.set(s);
			} catch (HibernateException e) {
				throw new InfrastructureException(e);
			}
		}
		return s;
	}

	public static void closeSession() throws InfrastructureException {
		log.debug("HibernateUtil::closeSession()");
		try {
			Session s = (Session) session.get();
			session.set(null);
			if (s != null && s.isOpen()) {
				s.close();
			}
		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}
	}

	public static void beginTransaction() throws InfrastructureException {
		log.debug("HibernateUtil::beginTransaction()");
		Transaction tx = (Transaction) threadTransaction.get();
		if (tx == null) {
			try {
				tx = getSession().beginTransaction();
				threadTransaction.set(tx);
			} catch (HibernateException e) {
				throw new InfrastructureException(e);
			}
		}
	}

	public static void commitTransaction() throws InfrastructureException {
		log.debug("HibernateUtil::commitTransaction()");
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				log.debug("HibernateUtil::commitTransaction() actual call");
				tx.commit();
				threadTransaction.set(null);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			rollbackTransaction();
			throw new InfrastructureException(e);
		}
	}

	public static void rollbackTransaction() throws InfrastructureException {
		log.debug("HibernateUtil::rollbackTransaction()");
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			threadTransaction.set(null);
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				tx.rollback();
			}
		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		} finally {
			closeSession();
		}
	}
}
