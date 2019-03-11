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

import it.iccu.sbn.ejb.vo.servizi.calendario.TipoCalendario;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_modello_calendario;
import it.iccu.sbn.polo.orm.servizi.Tbl_sale;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Restrictions;

public class CalendarioDAO extends ServiziBaseDAO {

	public Tbl_modello_calendario getModelloCalendarioById(long id_modello) throws DaoManagerException {
		Session session = getCurrentSession();
		return (Tbl_modello_calendario) session.get(Tbl_modello_calendario.class, id_modello);
	}

	public Tbl_modello_calendario aggiornaCalendario(Tbl_modello_calendario modello) throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		this.setSessionCurrentCfg();
		try {
			if (modello.getId_modello() == 0) {
				session.save(modello);
			} else
				modello = (Tbl_modello_calendario) session.merge(modello);

			session.flush();
			return modello;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_modello_calendario getCalendarioBiblioteca(String codPolo, String codBib) throws DaoManagerException {
		try {
			Session session = getCurrentSession();
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Criteria c = session.createCriteria(Tbl_modello_calendario.class);
			c.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("biblioteca", bib))
				.add(Restrictions.eq("cd_tipo", TipoCalendario.BIBLIOTECA.getCd_tipo()));

			return (Tbl_modello_calendario) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_modello_calendario getCalendarioCategoriaMediazione(String codPolo, String codBib,
			String cd_cat_mediazione) throws DaoManagerException {
		try {
			Session session = getCurrentSession();
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Criteria c = session.createCriteria(Tbl_modello_calendario.class);
			c.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("biblioteca", bib))
				.add(Restrictions.eq("cd_tipo", TipoCalendario.MEDIAZIONE.getCd_tipo()))
				.add(Restrictions.eq("cd_cat_mediazione", cd_cat_mediazione));

			return (Tbl_modello_calendario) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_modello_calendario> getCalendariSala(Tbl_sale sala) throws DaoManagerException {
		try {
			Session session = getCurrentSession();
			Criteria c = session.createCriteria(Tbl_modello_calendario.class);
			c.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("sala", sala))
				.add(Restrictions.eq("cd_tipo", TipoCalendario.SALA.getCd_tipo()));

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


}
