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
package it.iccu.sbn.persistence.dao.common;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.factory.ConnectionFactory;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_utente_professionale_biblioteca;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.util.Base64Util;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.jboss.logging.Logger;

public class DaoManager {

	private ConnectionFactory connectionFactory = null;
	private static ComparableVersion version;
	private static Logger log = Logger.getLogger(DaoManager.class);


	/**
	 * Il metodo {@code load()} della Session Hibernate ritorna un proxy
	 * nel caso l'entità sia configurata come lazy nel mapping.
	 * Questo metodo simula il comportamento di Hibernate nel caso di {@code lazy="false"}
	 * per garantire il funzionamento del codice già sviluppato.
	 * @param session Sessione Hibernate corrente
	 * @param entityClass Classe dell'entità da caricare
	 * @param entityId Chiave primaria dell'entità da caricare
	 * @return un oggetto che rappresenta l'entità caricata dal DB.
	 * @throws HibernateException
	 * @author almaviva5_20100330
	 */
	public static final Object loadNoLazy(Session session, Class<?> entityClass, Serializable entityId) throws HibernateException {
		Object entity = session.get(entityClass, entityId);
		if (entity == null)
			throw new ObjectNotFoundException(entityId, entityClass.getCanonicalName());

		return entity;
	}

	public static final void begin(UserTransaction tx) throws Exception {
		if (tx != null && tx.getStatus() == Status.STATUS_NO_TRANSACTION)
			tx.begin();
	}

	public static final void commit(UserTransaction tx) throws Exception {
		if (tx != null && tx.getStatus() == Status.STATUS_ACTIVE)
			tx.commit();
	}

	public static final void rollback(UserTransaction tx) {
		try {
			if (tx != null && ValidazioneDati.in(tx.getStatus(),
					Status.STATUS_ACTIVE,
					Status.STATUS_MARKED_ROLLBACK) )
				tx.rollback();

		} catch (Exception e) {
			log.error("", e);
		}
	}

	public static void endTransaction(UserTransaction tx, boolean success) {
		try {
			if (success) {
				commit(tx);
				rollback(tx);
			} else {
				rollback(tx);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public static final void closeCursor(ScrollableResults cursor) {
		try {
			if (cursor != null)
				cursor.close();
		} catch (HibernateException e) {
			log.error("", e);
		}
	}

	public static final String getFirmaUtente(String ticket) {
		try {
			String[] tokens = ticket.split("_");
			if (tokens.length == 8) {
				return tokens[0] + Constants.ROOT_BIB + new String(Base64Util.decode(tokens[4]));
			}
			return tokens[0] + tokens[1] + new String(Base64Util.decode(tokens[2]));

		} catch (Exception e) {
			return null;
		}
	}

	public static final String getFirmaUtente(int idUtenteProfessionale) {
		try {
			Session session = new DaoManager().getCurrentSession();
			Criteria criteria = session.createCriteria(Trf_utente_professionale_biblioteca.class);
			criteria.add(Restrictions.eq("id_utente_professionale.id", idUtenteProfessionale));

			Trf_utente_professionale_biblioteca utente = (Trf_utente_professionale_biblioteca) criteria.uniqueResult();
			Tbf_biblioteca_in_polo bib = utente.getCd_polo();
			String firma = bib.getCd_polo().getCd_polo()
					+ bib.getCd_biblioteca()
					+ utente.getId_utente_professionale()
							.getTbf_utenti_professionali_web().getUserid();
			return firma.trim();

		} catch (Exception e) {
			return "sbnweb";
		}
	}


	public static final String codBibFromTicket(String ticket) {
		return ticket.substring(4, 7);
	}

	public static final String codPoloFromTicket(String ticket) {
		return ticket.substring(0, 3);
	}

	public static final String userFromTicket(String ticket) {
		try {
			String[] tokens = ticket.split("_");
			return new String(Base64Util.decode(tokens[2]));

		} catch (Exception e) {
			return null;
		}
	}

	public static final Criterion ricercaPerParoleAND(String campoTabella, String valore) throws DaoManagerException {
		return ricercaPerParole(campoTabella, valore, '&');
	}

	private static final Criterion ricercaPerParole(String campoTabella, String valore, char operator) throws DaoManagerException {

		OrdinamentoUnicode u = new OrdinamentoUnicode();
		String[] parole = valore.split("\\s+");

		List<String> tmp = new ArrayList<String>();
    	for (String parola : parole) {
    		String norm = ValidazioneDati.trimOrEmpty(u.convert(parola) );
    		// rieseguo lo split sulla singola stringa, questo perché
    		// la normalizzazione può aver sostituito dei separatori con spazio
    		// e risulta necessario trattare le parole composte (es. "nord-ovest", "d'amato")
    		// come due parole distinte
    		tmp.addAll(Arrays.asList(norm.split("\\s")));
    	}

    	parole = tmp.toArray(new String[0]);

    	StringBuilder buf = new StringBuilder();
		buf.append("({alias}.");
		buf.append(campoTabella);
		if ((new DaoManager()).getVersion().compareTo(ComparableVersion.of("8.3")) < 0)
			buf.append(" @@ to_tsquery('default', '");
		else
			buf.append(" @@ to_tsquery('");

		buf.append(parole[0]); // prima parola
		for (int i = 1; i < parole.length; i++)
			if (ValidazioneDati.isFilled(parole[i]))
				buf.append(" ").append(operator).append(" ").append(parole[i]);

		buf.append("'))");

 		return Restrictions.sqlRestriction(buf.toString());
	}

	public static final Criterion ricercaPerParoleOR(String campoTabella, String valore) throws DaoManagerException {
		return ricercaPerParole(campoTabella, valore, '|');
	}

	public static final Tbf_biblioteca_in_polo creaIdBib(String codPolo, String codBib) {
		if (!ValidazioneDati.isFilled(codPolo) || !ValidazioneDati.isFilled(codBib))
			return null;

		Tbf_polo polo = new Tbf_polo();
		polo.setCd_polo(codPolo);
		Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
		bib.setCd_biblioteca(codBib);
		bib.setCd_polo(polo);
		bib.setFl_canc('N');
		return bib;
	}

	public static final Tb_titolo creaIdTitolo(String bid) {
		Tb_titolo t = new Tb_titolo();
		t.setFl_canc('N');
		t.setBid(bid);
		return t;
	}

	public static final Criteria addTimeStampRange(Criteria criteria, String property,
			Timestamp da, Timestamp a) {
		if (da != null && a != null)
			return criteria.add(Restrictions.between(property, da, a));

		if (da != null && a == null)
			return criteria.add(Restrictions.ge(property, da));

		if (da == null && a != null)
			return criteria.add(Restrictions.le(property, a));

		// tutto a null;
		return criteria;
	}

	public static final Criteria addDateRange(Criteria criteria, String property, Date da, Date a) {
		if (da != null && a != null)
			return criteria.add(Restrictions.between(property, da, a));

		if (da != null && a == null)
			return criteria.add(Restrictions.ge(property, da));

		if (da == null && a != null)
			return criteria.add(Restrictions.le(property, a));

		// tutto a null;
		return criteria;
	}

	public static final DetachedCriteria addDateRange(DetachedCriteria criteria, String property, Date da, Date a) {
		if (da != null && a != null)
			return criteria.add(Restrictions.between(property, da, a));

		if (da != null && a == null)
			return criteria.add(Restrictions.ge(property, da));

		if (da == null && a != null)
			return criteria.add(Restrictions.le(property, a));

		// tutto a null;
		return criteria;
	}

	public static final String addTimeStampRange(String property, Timestamp from, Timestamp to) {
		StringBuilder sql = new StringBuilder();
		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		if (from != null && to != null) {
			sql.append(property).append(" between ");
			sql.append("'").append(f.format(from)).append("'");
			sql.append(" and ");
			sql.append("'").append(f.format(to)).append("'");
			return sql.toString();
		}

		if (from != null && to == null) {
			sql.append(property).append(">=");
			sql.append("'").append(f.format(from)).append("'");
			return sql.toString();
		}

		if (from == null && to != null) {
			sql.append(property).append("<=");
			sql.append("'").append(f.format(to)).append("'");
			return sql.toString();
		}

		return null;
	}

	public static final String addDateRange(String property, Date from, Date to) {
		StringBuilder sql = new StringBuilder();
		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
		if (from != null && to != null) {
			sql.append(property).append(" between ");
			sql.append("'").append(f.format(from)).append("'");
			sql.append(" and ");
			sql.append("'").append(f.format(to)).append("'");
			return sql.toString();
		}

		if (from != null && to == null) {
			sql.append(property).append(">=");
			sql.append("'").append(f.format(from)).append("'");
			return sql.toString();
		}

		if (from == null && to != null) {
			sql.append(property).append("<=");
			sql.append("'").append(f.format(to)).append("'");
			return sql.toString();
		}

		return null;
	}

	public static final void checkMaxResults(int cnt) throws Exception {
		int max = CommonConfiguration.getPropertyAsInteger(Configuration.MAX_RESULT_ROWS, Integer.MAX_VALUE);
		if (cnt > max)
			throw new ApplicationException(SbnErrorTypes.ERROR_DB_MAX_ROWS_EXCEEDED, String.valueOf(cnt));
	}

	public DaoManager() {
		this.connectionFactory = ConnectionFactory.getInstance();
	}

	public Session getCurrentSession() throws DaoManagerException {
		try {
			return connectionFactory.getCurrentSession();

		} catch (Exception ex) {
			throw new DaoManagerException(
					"Errore nel recuperare la Session", ex);
		}
	}

	public void clearSession() throws DaoManagerException {
		try {
			connectionFactory.getCurrentSession().clear();

		} catch (Exception ex) {
			throw new DaoManagerException(
					"Errore nel recuperare la Session", ex);
		}
	}

	public StatelessSession getStatelessSession() throws DaoManagerException {
		try {
			return connectionFactory.getFactory().openStatelessSession();

		} catch (Exception ex) {
			throw new DaoManagerException(
					"Errore nel recuperare la Session", ex);
		}
	}



	public SessionFactory getFactory() throws DaoManagerException {
		try {
			return connectionFactory.getFactory();

		} catch (Exception ex) {
			throw new DaoManagerException(
					"Errore nel recuperare la SessionFactory", ex);
		}
	}

	public void setSessionCurrentCfg() throws DaoManagerException {
		try {
			ComparableVersion version = getVersion();
			if (version.compareTo(ComparableVersion.of(Constants.POSTGRES_VERSION_83)) < 0) // config TSearch2 (solo se ver. < 8.3)
				getCurrentSession().getNamedQuery("set_curcfg").list();

		} catch (Exception ex) {
			throw new DaoManagerException(
					"Errore nell'impostazione della configurazione di Sessione",
					ex);
		}
	}

	public ComparableVersion getVersion() throws DaoManagerException {
		try {
			if (version != null)
				return version;

			Session session = getCurrentSession();
			String tmp = (String) session.createSQLQuery("select version()").uniqueResult();
			String[] tokens = tmp.split("\\s");
			version = ComparableVersion.of(tokens[1]);
			return version;

		} catch (Exception ex) {
			throw new DaoManagerException(
					"Errore nell'impostazione della configurazione di Sessione",
					ex);
		}
	}

	public void deleteAndEvict(Serializable entity) throws HibernateException, DaoManagerException {
		if (entity != null) {
			connectionFactory.getCurrentSession().delete(entity);
			getFactory().evict(entity.getClass(), entity);
		}
	}

	public void evict(Serializable entity) throws HibernateException, DaoManagerException {
		if (entity != null)// && !entity.getClass().getCanonicalName().toLowerCase().contains("javassist"))
			getCurrentSession().evict(entity);
	}

	@SuppressWarnings("unchecked")
	public void clearCache(String pattern) {

		if (ValidazioneDati.isFilled(pattern))
			pattern = pattern.toUpperCase();

		SessionFactory factory = connectionFactory.getFactory();
		Map<String, CollectionMetadata> roleMap = factory.getAllCollectionMetadata();
		Map<String, ClassMetadata> entityMap = factory.getAllClassMetadata();

		if (ValidazioneDati.isFilled(pattern)) {

			for (String roleName : roleMap.keySet())
				if (roleName.toUpperCase().indexOf(pattern) > -1)
					factory.evictCollection(roleName);

			for (String entityName : entityMap.keySet())
				if (entityName.toUpperCase().indexOf(pattern) > -1)
					factory.evictEntity(entityName);

		} else { // cancello tutto

			for (String roleName : roleMap.keySet())
				factory.evictCollection(roleName);

			for (String entityName : entityMap.keySet())
				factory.evictEntity(entityName);

		}
	}

	@SuppressWarnings("deprecation")
	public Connection getConnection() throws SQLException {
		try {
			Connection connection = getCurrentSession().connection();
			ComparableVersion version = getVersion();
			if (version.compareTo(ComparableVersion.of(Constants.POSTGRES_VERSION_83)) < 0) {// config TSearch2 (solo se ver. < 8.3)
				Statement st = connection.createStatement();
				st.execute("select set_curcfg('default')");
				st.close();
			}
			return connection;
		} catch (Exception ex) {
			throw new SQLException(
					"Errore nell'impostazione della configurazione di Sessione");
		}
	}

	public void flush() throws HibernateException, DaoManagerException {
		getCurrentSession().flush();
	}

	public static final Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String escapeSql(String sql) {
		if (!ValidazioneDati.isFilled(sql))
			return sql;

		// ' --> ''
		sql = sql.replaceAll("'", "''");
		// " --> ""
		//sql = sql.replaceAll("\"", "\"\"");
		// \ --> (remove backslashes)
		sql = sql.replaceAll("\\\\", "");
		return sql;
	}

}
