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
import it.iccu.sbn.polo.orm.servizi.Tbl_accesso_utente;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class UtenteAccessoDAO extends DaoManager {

	public Tbl_accesso_utente aggiornaEventoAccesso(Tbl_accesso_utente accesso) throws DaoManagerException, DAOConcurrentException {
		try {
			setSessionCurrentCfg();
			Session session = this.getCurrentSession();
			if (accesso.getId() == 0)
				session.save(accesso);
			else
				accesso = (Tbl_accesso_utente) session.merge(accesso);

			return accesso;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Tbl_accesso_utente getUltimoEventoUtente(String codPolo, String codBib, String idTessera, Date limit) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbl_accesso_utente.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("biblioteca", creaIdBib(codPolo, codBib)));
		c.add(Restrictions.eq("id_tessera", idTessera).ignoreCase());
		if (limit != null)
			c.add(Restrictions.ge("ts_evento", limit));
		c.addOrder(Order.desc("ts_evento")).addOrder(Order.desc("id"));
		c.setMaxResults(1);

		return (Tbl_accesso_utente) c.uniqueResult();
	}

}
