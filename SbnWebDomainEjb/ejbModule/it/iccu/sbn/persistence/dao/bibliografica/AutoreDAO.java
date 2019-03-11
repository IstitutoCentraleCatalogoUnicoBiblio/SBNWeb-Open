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
import it.iccu.sbn.polo.orm.bibliografica.Tb_autore;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class AutoreDAO extends DaoManager {

	public boolean esisteAutore(String vid) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_autore.class);
		c.add(Restrictions.eq("vid", vid));
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.setProjection(Projections.rowCount());

		Number cnt = (Number) c.uniqueResult();
		return (cnt.intValue() > 0);
	}

	public String getTipoNomeAutore(String vid) throws DaoManagerException {
		Session session = getCurrentSession();
		org.hibernate.Query q = session.getNamedQuery("tipoNomeAutore");
		q.setString("vid", vid);

		Character tipoNome = (Character) q.uniqueResult();
		return  tipoNome != null ? tipoNome.toString() : "";
	}

}
