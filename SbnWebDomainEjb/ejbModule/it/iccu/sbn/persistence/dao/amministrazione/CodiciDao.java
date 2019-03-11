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
package it.iccu.sbn.persistence.dao.amministrazione;

import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.TabellaType;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class CodiciDao extends DaoManager {

	public CodiciDao() {
		super();
	}

	public List<Tb_codici> getConfigTabelleCodici(boolean root) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tb_codici.class);
			criteria.add(Expression.eq("tp_tabella", "0000"));
			if (!root)
				criteria.add(Restrictions.or(Restrictions.isNull("cd_flg3"), Restrictions.ne("cd_flg3", "root")));
			criteria.addOrder(Order.asc("cd_tabella"));
			return criteria.list();

		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tb_codici getConfigTabelleCodici(String tp_tabella) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tb_codici.class);
			criteria.add(Expression.eq("tp_tabella", "0000"));
			criteria.add(Restrictions.eq("cd_tabella", tp_tabella));
			return (Tb_codici) criteria.uniqueResult();

		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tb_codici> cercaTabellaCodici(String cdTabella, TabellaType type) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tb_codici.class);
			criteria.add(Expression.eq("tp_tabella", cdTabella));

			switch (type) {
			case DICT:
				criteria.addOrder(Order.asc("cd_tabella"));
				break;
			case CROSS:
				criteria.addOrder(Order.asc("cd_flg1"))
						.addOrder(Order.asc("cd_flg2"));
			}

			return criteria.list();

		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tb_codici getCodice(String cdTabella, CodiciType type) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tb_codici.class);
			c.add(Expression.eq("tp_tabella", type.getTp_Tabella()));
			c.add(Expression.eq("cd_tabella", cdTabella));

			return (Tb_codici) c.uniqueResult();

		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaCodici(String tabellaCodici) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			SQLQuery query = session.createSQLQuery("delete from Tb_codici where tp_tabella = \'" + tabellaCodici + "\'");
			query.executeUpdate();
			session.flush();
			session.clear();
			clearCache("Tb_codici");

		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void salvaTabellaCodici(Tb_codici tb_codici) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(tb_codici);
			session.flush();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
