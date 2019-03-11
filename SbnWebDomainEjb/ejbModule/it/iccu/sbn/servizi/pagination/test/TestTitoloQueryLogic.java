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
package it.iccu.sbn.servizi.pagination.test;

import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.servizi.pagination.QueryExecutionLogic;
import it.iccu.sbn.servizi.pagination.QueryExecutionParams;
import it.iccu.sbn.util.cloning.ClonePool;

import java.io.Serializable;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class TestTitoloQueryLogic extends DaoManager implements QueryExecutionLogic {

	private DetachedCriteria template;
	private ScrollableResults cursor;

	public int count(QueryExecutionParams params) throws Exception {

		Session session = getCurrentSession();
		Criteria c = ClonePool.deepCopy(template).getExecutableCriteria(session);
		c.setProjection(Projections.rowCount());
		Number cnt = (Number) c.uniqueResult();
		return cnt.intValue();
	}

	public Serializable nextId(QueryExecutionParams params) throws Exception {
		if (cursor.next())
			return (String)cursor.get(0);

		return null;
	}

	public Serializable getData(QueryExecutionParams params, Serializable id) throws Exception {
		return (Serializable) loadNoLazy(getCurrentSession(), Tb_titolo.class, id);
	}

	public void begin(QueryExecutionParams params) throws Exception {
		Session session = getCurrentSession();
		Query q = session.createQuery("select t.bid from Tb_titolo t where t.fl_canc<>'S' order by t.bid");
		cursor = q.setCacheMode(CacheMode.IGNORE).setFetchSize(100).scroll(ScrollMode.FORWARD_ONLY);
	}

	public void end(QueryExecutionParams params) throws Exception {
		if (cursor != null)
			cursor.close();

	}

	public void init(QueryExecutionParams params) throws Exception {
		template = DetachedCriteria.forClass(Tb_titolo.class);
		template.add(Restrictions.ne("fl_canc", 'S'));
	}

}
