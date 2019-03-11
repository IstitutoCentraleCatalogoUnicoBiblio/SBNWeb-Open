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

import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

public class InitSessionFactory {
	/**
	 * Default constructor.
	 */
	private InitSessionFactory() {
	}

	/**
	 * Location of hibernate.cfg.xml file. NOTICE: Location should be on the
	 * classpath as Hibernate uses #resourceAsStream style lookup for its
	 * configuration file. That is place the config file in a Java package - the
	 * default location is the default Java package.<br>
	 * <br>
	 * Examples: <br>
	 * <code>CONFIG_FILE_LOCATION = "/hibernate.conf.xml".
	 * CONFIG_FILE_LOCATION = "/com/foo/bar/myhiberstuff.conf.xml".</code>
	 */
	private static String CONFIG_FILE_LOCATION = "/SbnDB.cfg.xml";

	/** The single instance of hibernate configuration */
	private static final Configuration cfg = new Configuration();

	/** The single instance of hibernate SessionFactory */
	private static org.hibernate.SessionFactory sessionFactory;

	/**
	 * initialises the configuration if not yet done and returns the current
	 * instance
	 *
	 * @return
	 */
	public static SessionFactory getInstance() {
		if (sessionFactory == null)
			initSessionFactory();
		return sessionFactory;
	}

	/**
	 * Returns the ThreadLocal Session instance. Lazy initialize the
	 * <code>SessionFactory</code> if needed.
	 *
	 * @return Session
	 * @throws HibernateException
	 */
	public Session openSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * The behaviour of this method depends on the session context you have
	 * configured. This factory is intended to be used with a hibernate.cfg.xml
	 * including the following property <property
	 * name="current_session_context_class">thread</property> This would return
	 * the current open session or if this does not exist, will create a new
	 * session
	 *
	 * @return
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * initializes the sessionfactory in a safe way even if more than one thread
	 * tries to build a sessionFactory
	 */
	private static synchronized void initSessionFactory() {
		/*
		 * [laliluna] check again for null because sessionFactory may have been
		 * initialized between the last check and now
		 *
		 */
		Logger log = Logger.getLogger(InitSessionFactory.class);
		if (sessionFactory == null) {


			try {
				cfg.configure(CONFIG_FILE_LOCATION);
				String sessionFactoryJndiName = cfg
				.getProperty(Environment.SESSION_FACTORY_NAME);
				if (sessionFactoryJndiName != null) {
					cfg.buildSessionFactory();
					log.debug("get a jndi session factory");
					sessionFactory = (SessionFactory) (new InitialContext())
							.lookup(sessionFactoryJndiName);
				} else{
					log.debug("classic factory");
					sessionFactory = cfg.buildSessionFactory();
				}

			} catch (Exception e) {
				System.err
						.println("%%%% Error Creating HibernateSessionFactory %%%%");
				e.printStackTrace();
				throw new HibernateException(
						"Could not initialize the Hibernate configuration");
			}
		}
	}

	public static void close(){
		if (sessionFactory != null)
			sessionFactory.close();
		sessionFactory = null;

	}
}
