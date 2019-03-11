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
import it.iccu.sbn.polo.orm.bibliografica.Tb_loc_indice;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class LocalizzaDAO extends DaoManager {

	public List<Tb_loc_indice> getLocalizzazioniPendenti() throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_loc_indice.class);
		c.add(Restrictions.eq("fl_stato", '0'));
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.addOrder(Order.asc("titolo.id")).addOrder(Order.asc("id"));

		return c.list();
	}

	public List<Number> getLocalizzazioniPendentiIDs() throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_loc_indice.class);
		c.add(Restrictions.eq("fl_stato", '0'));
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.addOrder(Order.asc("titolo.id")).addOrder(Order.asc("id"));
		c.setProjection(Projections.id());

		return c.list();
	}

	public Tb_loc_indice getLocalizzazionePendenteById(int idLoc) throws DaoManagerException {

		Session session = getCurrentSession();
		return (Tb_loc_indice) session.get(Tb_loc_indice.class, idLoc);
	}

	public int countLocalizzazioniPendenti() throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_loc_indice.class);
		c.add(Restrictions.eq("fl_stato", '0'));
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.setProjection(Projections.rowCount());

		Number cnt = (Number) c.uniqueResult();
		return cnt.intValue();
	}

	public void salvaLocalizzazione(String codPolo, String codBib, String bid, short type, String xml,
			String firmaUtente) throws DaoManagerException {

		Tb_loc_indice loc = getLocalizzazione(codPolo, codBib, bid, type);
		if (loc == null) {
			loc = new Tb_loc_indice();
			loc.setTitolo(creaIdTitolo(bid));
			loc.setBiblioteca(creaIdBib(codPolo, codBib));
			loc.setUte_ins(firmaUtente);
			loc.setTs_ins(DaoManager.now());
		}

		loc.setFl_stato('0');
		loc.setTp_loc(type);
		loc.setSbnmarc_xml(xml);
		loc.setUte_var(firmaUtente);
		loc.setFl_canc('N');

		save(loc);
	}

	public void save(Tb_loc_indice loc) throws DaoManagerException {
		Session session = getCurrentSession();
		try {
			if (loc.getId_loc() > 0)
				session.update(loc);
			else
				session.save(loc);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Tb_loc_indice getLocalizzazione(String codPolo, String codBib, String bid, short type) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_loc_indice.class);
		c.add(Restrictions.eq("fl_stato", '0'));
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("biblioteca", creaIdBib(codPolo, codBib)));
		c.add(Restrictions.eq("titolo", creaIdTitolo(bid)));
		c.add(Restrictions.eq("tp_loc", type));
		c.addOrder(Order.asc("titolo.id")).addOrder(Order.asc("id"));

		return (Tb_loc_indice) c.uniqueResult();
	}

}
