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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.web2.util.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public abstract class AbstractJDBCManager {

	private static Logger log = Logger.getLogger(AbstractJDBCManager.class);

	private static String DATA_SOURCE;

	private DataSource ds = null;

	static {
		try {
			DATA_SOURCE = CommonConfiguration.getProperty(Configuration.DATA_SOURCE);
			log.info("Istanziato il Data Source:" + DATA_SOURCE);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public static final String buildINClause(String target, Collection<String> values) {
		if (!ValidazioneDati.isFilled(values))
			return null;

		StringBuilder sql = new StringBuilder();
		sql.append(target).append(" in (");
		Iterator<String> i = values.iterator();
		for (;;) {
			String value = i.next();
			sql.append("'").append(value).append("'");
			if (i.hasNext())
				sql.append(", ");
			else
				break;
		}

		sql.append(")");

		return sql.toString();
	}

	public static final String buildTimeStampRange(String property,	Timestamp from, Timestamp to) {
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


	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			if (ds == null) {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup(DATA_SOURCE);
			}
			conn = ds.getConnection();
			Statement st = conn.createStatement();
			if (st.execute("select version()") )  {
				ResultSet rs = st.getResultSet();
				rs.next();
				String version = rs.getString(1);
				rs.close();
				if (version.substring(11).compareTo(Constants.POSTGRES_VERSION_83) < 0) // config TSearch2 (solo se ver. < 8.3)
					st.execute("select set_curcfg('default')");
			}

			st.close();

		} catch (NamingException e) {
			log.error("", e);
			conn = null;
		}

		return conn;
	}

	public void close(Connection c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void close(PreparedStatement prepStmt) {
		try {
			if (prepStmt != null) {
				prepStmt.close();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
