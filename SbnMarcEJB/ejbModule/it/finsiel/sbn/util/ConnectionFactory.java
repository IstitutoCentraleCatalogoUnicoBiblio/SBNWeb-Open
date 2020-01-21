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

import java.util.StringTokenizer;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.SessionImpl;



public class ConnectionFactory {

	protected static Log log = LogFactory.getLog("sbnmarcPolo");

	private SessionFactory sessionFactory;
	protected Transaction transaction = null;
	//UserTransaction tx;

	private Session session;
	public static Integer version = null;

	private ConnectionFactory() throws RuntimeException{
		return;
	}

	public static synchronized ConnectionFactory getInstance() {
		return new ConnectionFactory();
	}

	private void createSesionFactoy()
	{
		if(this.sessionFactory != null)
		{
			return;
//			this.sessionFactory.close();
//			this.sessionFactory = null;
		}
		try {
			/*
			Hashtable env = new Hashtable();
			env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put("java.naming.factory.url.pkgs",
					"org.jboss.naming:org.jnp.interfaces");
			env.put(javax.naming.Context.PROVIDER_URL, "127.0.0.1");
*/
			InitialContext initialcontext = new InitialContext();
			this.sessionFactory = (SessionFactory) initialcontext
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

	public Session getCurrentSession() {
		try {
			this.createSesionFactoy();
//			return sessionFactory.openSession();
//			try {
//				tx = (UserTransaction)new InitialContext()
//				.lookup("UserTransaction");
//				//tx.begin();
//
//
//			} catch (NamingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return sessionFactory.getCurrentSession();
			if ( session == null )
				session = sessionFactory.openSession();
			return session;
		} catch (Exception exception) {
			log.error((new StringBuilder(512)).append(
					"Hibernate Exception").append(exception.getMessage())
					.toString());
			throw new RuntimeException(exception);
		}


	}

	public Session getSession() throws InfrastructureException {
		// log.info("=== DBMessaging.beginTransaction()");
		try {
			Session session = this.getCurrentSession();
//			String VersionDb = ResourceLoader.getPropertyString("VERSION_DB");
//			log.debug("VERSION DB=" + VersionDb);
	        // AVVIO IL DEFAULT DI CONF è provvisorio bisogna trovare una soluzione alternativa
// VERIFICARE SE FUNZIONE IL SET CURCFG in VALIDATORPROFILER
			if(!(getDbversion() >= 0)){
				beginTransaction();
				String qry = "set_curcfg";
		        SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		        query.list();
		        commitTransaction();
			}
// END VERIFICA
	        // FINE AVVIO DEFAULT DI CONF

			return session;
		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}
	}

	public Session getSession2() throws InfrastructureException {
		// log.info("=== DBMessaging.beginTransaction()");
		try {
			Session session = this.getCurrentSession();
			return session;
		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}
	}

    public Session getSessionUpdate(Interceptor interc) throws InfrastructureException {
        // log.info("=== DBMessaging.beginTransaction()");
        try {
        	Session session = sessionFactory.openSession(interc);
            return session;
        } catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

	public void beginTransaction() throws InfrastructureException {
		// log.info("=== DBMessaging.beginTransaction()");
		try {
			transaction = this.getCurrentSession().beginTransaction();
		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}
	}

	public void commitTransaction() throws InfrastructureException {
		// Flush the Session and commit the transaction
		// log.info("=== DBMessaging.endTransaction() : Commit ");
		try {
			if(transaction==null)
				throw new InfrastructureException("Transazione non settatta fare la beginTransaction()");
			transaction.commit();
			transaction = null;
		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}
	}

	public void rollbackTransaction() throws InfrastructureException {
		// Don't commit the transaction, can be faster for read-only operations
		// log.info("=== DBMessaging.endTransaction() : ROLLBACK !!! ");
		try {
			if(transaction==null)
				throw new InfrastructureException("Transazione non settatta fare la beginTransaction()");
			transaction.rollback();
			transaction = null;
		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}
	}

	public void closeSession() {
		// log.info("=== DBMessaging.endSession()");
		//this.commitTransaction();
		this.getCurrentSession().flush();
		this.getCurrentSession().clear();
		//this.sessionFactory.close();
		//this.sessionFactory = null;

//		UserTransaction tx = (UserTransaction)new InitialContext()
//        .lookup("java:comp/UserTransaction");


//		try {
//			tx.commit();
//			tx.begin();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}







    public static boolean isSessionPostgress(Session session) throws InfrastructureException {
        String dialect = ((SessionImpl) session).getFactory().getDialect().getClass().getName();
        if ("it.finsiel.sbn.util.OwnPostgreSQLDialect".equals(dialect)) {
            return true;
        } else
            return false;

    }
    public boolean isSessionPostgress(CriteriaImpl criteria) throws InfrastructureException {
		CriteriaImpl apses = criteria;
		Session ses = (Session)apses.getSession();


        String dialect = ((SessionImpl) ses).getFactory().getDialect().getClass().getName();
        if ("it.finsiel.sbn.util.OwnPostgreSQLDialect".equals(dialect)) {
            return true;
        } else
            return false;

    }

    public static boolean isSessionOracle(Session session) throws InfrastructureException {
        String dialect = ((SessionImpl) session).getFactory().getDialect().getClass().getName();
        if ("it.finsiel.sbn.util.OwnOracleDialect".equals(dialect)) {
            return true;
        } else
            return false;

    }


	public Integer getDbversion() throws InfrastructureException {

		if (version != null)
			return version;

		ComparableVersion dbVersion = null;
		try {
			Session session = this.getCurrentSession();
			beginTransaction();

			Query query = session.createSQLQuery("SELECT version() ");
			query.getQueryString();
	        String  result = (String) query.uniqueResult();

    		StringTokenizer versionParts = new StringTokenizer(result);

    		versionParts.nextToken(); /* "PostgreSQL" */
    		dbVersion = new ComparableVersion(versionParts.nextToken()); /* "X.Y.Z" */
    		//dbVersion = "8.3.1";
	        commitTransaction();
	        version = dbVersion.compareTo(new ComparableVersion("8.3.0"));
    		return version;

		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}
	}

}
