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
package it.iccu.sbn.persistence.dao.servizi;

import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_solleciti;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Restrictions;


public class SollecitiDAO extends DaoManager {

	public Tbl_solleciti saveSollecito(Tbl_solleciti sollecito)
			throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		try {
			if (sollecito.getTs_var() == null)
				session.save(sollecito);
			else
				sollecito = (Tbl_solleciti) session.merge(sollecito);

			session.flush();
			return sollecito;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_solleciti deleteSollecito(Tbl_solleciti sollecito, String uteVar)
		throws DaoManagerException, DAOConcurrentException {

		sollecito.setFl_canc('S');
		sollecito.setUte_var(uteVar);
		return saveSollecito(sollecito);
	}

	public Tbl_solleciti getSollecito(Tbl_richiesta_servizio req) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_solleciti.class);

			criteria.add(Restrictions.eq("cod_rich_serv", req))
					.add(Restrictions.ne("fl_canc", 'S'));

			return (Tbl_solleciti) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_solleciti getSollecitoById(Tbl_richiesta_servizio req, short prg) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbl_solleciti s = new Tbl_solleciti();
			s.setCod_rich_serv(req);
			s.setProgr_sollecito(prg);

			return (Tbl_solleciti) session.get(Tbl_solleciti.class, s);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
