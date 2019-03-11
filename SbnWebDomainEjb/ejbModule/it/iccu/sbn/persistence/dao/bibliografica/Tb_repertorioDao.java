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
package it.iccu.sbn.persistence.dao.bibliografica;

import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_repertorio;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class Tb_repertorioDao extends DaoManager {

	public Tb_repertorioDao() {
		super();
	}

	public List<Tb_repertorio> selectAll() throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria c = session.createCriteria(Tb_repertorio.class);
		//almaviva5_20140129 #5479
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.addOrder(Order.asc("cd_sig_repertorio"));
		return c.list();
	}

	public void saveOrUpdate(Tb_repertorio repertorio)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		// session.beginTransaction();
		try {
			Tb_repertorio tb_repertorio1 = (Tb_repertorio) loadNoLazy(session,
					Tb_repertorio.class, new Integer(repertorio.getORMID()));
			session.merge(repertorio);
		} catch (org.hibernate.ObjectNotFoundException e) {
			repertorio.setUte_ins(repertorio.getUte_var());
			repertorio.setTs_ins(repertorio.getTs_ins());
			session.save(repertorio);
		}
	}

	public void truncate(String ticket) throws DaoManagerException {
		Session session = this.getCurrentSession();
		SQLQuery q = session.createSQLQuery("update tb_repertorio set ute_var=:uteVar, ts_var=:tsVar, fl_canc='S' where fl_canc<>'S'");
		q.setParameter("uteVar", DaoManager.getFirmaUtente(ticket));
		q.setParameter("tsVar", DaoManager.now() );
		q.executeUpdate();
	}
}
