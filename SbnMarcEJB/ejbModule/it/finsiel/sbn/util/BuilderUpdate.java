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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

public class BuilderUpdate {

	protected static Log log = LogFactory.getLog("sbnmarcPolo");
	private static final int BUFFER_SIZE = 2048;

	// Contiene tutti i nome delle colonne + i valori su cui fare UPDATE
	private Map<String, Object> property = new HashMap<String, Object>();

	// Contiene tutti i nome delle colonne + i valori su cui fare Condizione di
	// WHERE con i relativi operatori
	private Map<String, Object> propertyWhere = new HashMap<String, Object>();
	private Map<String, String> propertyOperator = new HashMap<String, String>();

	// Contiene le espressioni like da aggiungere alla query
	private List<String> like = new ArrayList<String>();

	// Contiene le espressioni
	private List<Expression> expressionOr = new ArrayList<Expression>();

	private Session session;
	private String TableName;
	private Class<? extends Serializable> entity;

	public BuilderUpdate(Session session, String TableName,	Class<? extends Serializable> entity) {
		this.session = session;
		this.TableName = TableName;
		this.entity = entity;
	}

	public void addProperty(String field, Object value) {
		this.property.put(field, value);
	}

	public void addWhere(String field, Object value, String operator) {
		this.propertyWhere.put(field, value);
		this.propertyOperator.put(field, operator);
	}

	public void addWhereLikeEND(String colonna, String value) {
		String condizione = colonna + " LIKE '" + value + "' || '%'";
		like.add(condizione);
	}

	public void addExpressionOr(Expression expression) {
		this.expressionOr.add(expression);
	}

	/**
	 * Costruisce la condizione di SET
	 *
	 * @return String
	 */
	private StringBuilder buildColumnSetting() {

		StringBuilder hql = new StringBuilder(BUFFER_SIZE);
		Iterator<String> iterator = property.keySet().iterator();

		while (iterator.hasNext()) {
			String key = iterator.next();
			hql.append(key);

			Object obj = property.get(key);
			if (obj == null || obj instanceof String) {
				if (obj == null || obj.equals("NULL"))
					hql.append(" = NULL ");
				else {
					hql.append(" = :");
					hql.append(key);
					hql.append("_set");
				}
			} else {
				hql.append(" = :");
				hql.append(key);
				hql.append("_set");
			}
			if (iterator.hasNext()) {
				hql.append(",");
			}
		}
		return hql;
	}

	private StringBuilder buildWhere() {

		StringBuilder hql = new StringBuilder(BUFFER_SIZE);
		Iterator<String> iterator = propertyWhere.keySet().iterator();

		int likeSize = this.like.size();
		int exprSize = this.expressionOr.size();

		if (iterator.hasNext() || likeSize > 0 || exprSize > 0)
			hql.append(" WHERE ");

		int next = 0;
		while (iterator.hasNext()) {
			String key = iterator.next();
			hql.append(key);
			hql.append(" ");
			hql.append(propertyOperator.get(key));
			hql.append(" :");
			hql.append(key);
			if (iterator.hasNext())
				hql.append(" AND ");

			next = 1;
		}

		if (next == 1 && likeSize > 0)
			hql.append(" AND ");

		for (int index = 0; index < likeSize; index++) {
			hql.append(this.like.get(index));

			if (likeSize > index + 1)
				hql.append(" AND ");

		}

		if ((next == 1 || likeSize > 0) && exprSize > 0)
			hql.append(" AND ");


		if (exprSize > 0)
			hql.append(" ( ");

		for (int index = 0; index < exprSize; index++) {
			hql.append((this.expressionOr.get(index)).getCondition());

			if (exprSize > index + 1)
				hql.append(" OR ");
		}

		if (exprSize > 0)
			hql.append(" ) ");

		return hql;
	}

	private void buildValueSet(Query query) {

		Iterator<String> iterator = property.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object obj = property.get(key);
			if (obj != null && !obj.equals("NULL"))
				query.setParameter(key + "_set", obj);
		}
	}

	private void buildValueWhere(Query query) {

		Iterator<String> iterator = propertyWhere.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			query.setParameter(key, propertyWhere.get(key));
		}

		int exprSize = this.expressionOr.size();
		for (int index = 0; index < exprSize; index++) {
			Expression exp = this.expressionOr.get(index);
			iterator = exp.getProperty();
			while (iterator.hasNext()) {
				String key = iterator.next();
				query.setParameter(key + exp.getPrefix(), exp.getValue(key));
			}
		}

	}

	public int executeUpdate() {

		StringBuilder hql = new StringBuilder(BUFFER_SIZE);
		hql.append("UPDATE ");
		hql.append(TableName);
		hql.append(" SET ");

		hql.append(this.buildColumnSetting() );
		hql.append(this.buildWhere() );

		Query query = session.createQuery(hql.toString());
		this.buildValueSet(query);
		this.buildValueWhere(query);

		log.debug("QUESTO E VERO UPDATE=   " + query.getQueryString());

		return query.executeUpdate();
	}

	public int executeDelete() {

		StringBuilder hql = new StringBuilder(BUFFER_SIZE);
		hql.append("DELETE FROM ");
		hql.append(TableName);
		hql.append(" ");

		hql.append(this.buildWhere() );

		Query query = session.createQuery(hql.toString());
		this.buildValueSet(query);
		this.buildValueWhere(query);

		return query.executeUpdate();
	}

	public Class<? extends Serializable> getEntity() {
		return entity;
	}

}
