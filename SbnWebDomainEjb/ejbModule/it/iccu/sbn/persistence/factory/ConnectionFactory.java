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
package it.iccu.sbn.persistence.factory;

import javax.naming.InitialContext;

import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ConnectionFactory {

	private static ConnectionFactory instance = null;
	private SessionFactory sessionFactory;

	private ConnectionFactory() {

		sessionFactory = null;
		try {
			InitialContext ctx = new InitialContext();

			sessionFactory = (SessionFactory) ctx.lookup("java:/sbnWeb/HibernateSessionFactory");

		} catch (MappingException mappingexception) {
			System.err.println((new StringBuilder())
					.append("Mapping Exception: ").append(
							mappingexception.getMessage()).toString());
			throw new RuntimeException(mappingexception);
		} catch (Exception exception) {
			System.err.println((new StringBuilder()).append(
					"Hibernate Exception: ").append(exception.getMessage())
					.toString());
			throw new RuntimeException(exception);
		}
	}

	public static synchronized ConnectionFactory getInstance() {
		if (instance == null)
			instance = new ConnectionFactory();
		return instance;
	}

	public Session getCurrentSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception exception) {
			System.err.println((new StringBuilder()).append(
					"Hibernate Exception").append(exception.getMessage())
					.toString());
			throw new RuntimeException(exception);
		}
	}

	public SessionFactory getFactory() {
		return sessionFactory;
	}

}
