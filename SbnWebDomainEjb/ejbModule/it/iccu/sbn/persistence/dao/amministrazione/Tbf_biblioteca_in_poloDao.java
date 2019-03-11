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

import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

public class Tbf_biblioteca_in_poloDao extends DaoManager {

	public Tbf_biblioteca_in_poloDao() {
		super();
	}

	public List<Tbf_biblioteca_in_polo> selectAll()
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria c = session.createCriteria(Tbf_biblioteca_in_polo.class);
		return c.list();
	}

	public Tbf_biblioteca_in_polo select(String cd_polo, String cd_biblioteca)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_biblioteca_in_polo bib = creaIdBib(cd_polo, cd_biblioteca);
			bib = (Tbf_biblioteca_in_polo) loadNoLazy(session, Tbf_biblioteca_in_polo.class, bib);
			return bib;
		} catch (org.hibernate.ObjectNotFoundException e) {
			return null;
		}
	}

	public void save(Tbf_biblioteca_in_polo biblioteca)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		session.save(biblioteca);
	}

	public void update(Tbf_biblioteca_in_polo biblioteca)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		session.merge(biblioteca);
	}

}
