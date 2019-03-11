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

import it.iccu.sbn.persistence.dao.documentofisico.DFCommonDAO;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;
import it.iccu.sbn.servizi.pagination.QueryExecutionLogic;
import it.iccu.sbn.servizi.pagination.QueryExecutionParams;

import java.io.Serializable;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class TestRangeCollQueryLogic extends DFCommonDAO implements QueryExecutionLogic {

	private ScrollableResults cursor;

	private static final Tbc_sezione_collocazione creaIdSezione(String codPolo, String codBib, String codSez) {
		Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
		sez.setCd_polo(creaIdBib(codPolo, codBib));
		sez.setCd_sez(codSez);

		return sez;
	}

	public void init(QueryExecutionParams params) throws Exception {
		return;
	}

	public int count(QueryExecutionParams params) throws Exception {
		TestRangeCollQueryParams p = (TestRangeCollQueryParams) params;

		Criteria c = getCurrentSession().createCriteria(Tbc_collocazione.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("cd_sez", creaIdSezione(p.getCodPolo(), p.getCodBib(), p.getCodSez())));
		c.add(intervalloColloc1(p.getStartLoc(), "", p.getEndLoc(), ""));
		c.setProjection(Projections.rowCount());
		Number cnt = (Number) c.uniqueResult();
		return cnt.intValue();
	}

	public Serializable nextId(QueryExecutionParams params) throws Exception {
		if (cursor.next())
			return (Integer)cursor.get(0);

		return null;
	}

	public Serializable getData(QueryExecutionParams params, Serializable id) throws Exception {
		return (Serializable) loadNoLazy(getCurrentSession(), Tbc_collocazione.class, id);
	}

	public void begin(QueryExecutionParams params) throws Exception {
		TestRangeCollQueryParams p = (TestRangeCollQueryParams) params;

		Criteria c = getCurrentSession().createCriteria(Tbc_collocazione.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("cd_sez", creaIdSezione(p.getCodPolo(), p.getCodBib(), p.getCodSez())));
		c.add(intervalloColloc1(p.getStartLoc(), "", p.getEndLoc(), ""));
		c.setProjection(Projections.id());
		c.addOrder(Order.asc("ord_loc")).addOrder(Order.asc("ord_spec"));
		c.addOrder(Order.asc("cd_loc")).addOrder(Order.asc("spec_loc"));

		cursor = c.setCacheMode(CacheMode.IGNORE).setFetchSize(100).scroll(ScrollMode.FORWARD_ONLY);

	}

	public void end(QueryExecutionParams params) throws Exception {
		if (cursor != null)
			cursor.close();

	}

}
