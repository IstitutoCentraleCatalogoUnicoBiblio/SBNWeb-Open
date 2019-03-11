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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AmministrazioneFactory {

	protected static Log log = LogFactory.getLog("sbnmarcPolo");

	private static AmministrazioneFactory instance = null;
	private SessionFactory sessionFactory;

	private AmministrazioneFactory() {
		sessionFactory = null;
		try {
			InitialContext initialcontext = new InitialContext();
			//sessionFactory = (SessionFactory) initialcontext.lookup("java:/SbnWeb/HibernateSessionFactory");

			sessionFactory = (SessionFactory) initialcontext
				.lookup("java:/sbn/HibernateSessionFactory");

		} catch (MappingException mappingexception) {
			log.error((new StringBuilder(512))
					.append("Mapping Exception").append(
							mappingexception.getMessage()).toString());
			throw new RuntimeException(mappingexception);
		} catch (Exception exception) {
			log.error((new StringBuilder(512)).append(
					"Hibernate Exception").append(exception.getMessage())
					.toString());
			throw new RuntimeException(exception);
		}
	}

	public static synchronized AmministrazioneFactory getInstance() {
		if (instance == null)
			instance = new AmministrazioneFactory();
		return instance;
	}

	public Session getCurrentSession() {
		try {
//			return sessionFactory.openSession();
			return sessionFactory.getCurrentSession();
//			org.hibernate.classic.Session session = sessionFactory
//					.getCurrentSession();
//			return session;
		} catch (Exception exception) {
			log.error((new StringBuilder(512)).append(
					"Hibernate Exception").append(exception.getMessage())
					.toString());
			throw new RuntimeException(exception);
		}
	}

}
