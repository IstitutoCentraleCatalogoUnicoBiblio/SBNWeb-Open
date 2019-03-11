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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.servizi.Tbl_materie;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_materie_utenti;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class MaterieDAO extends ServiziBaseDAO {

	private UtilitaDAO serviziHibDAO = new UtilitaDAO();


	public void aggiornaMateria(Tbl_materie materia)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(materia);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Tbl_materie> ricercaMaterie(Tbl_materie materia, String ordinamento) throws DaoManagerException {
		Session session = this.getCurrentSession();
		materia.setFl_canc('N');
		Example example = Example.create(materia);
		example.setPropertySelector(NOT_EMPTY);
		example.enableLike(MatchMode.START);
		example.ignoreCase();

		try {
			Criteria c = session.createCriteria(Tbl_materie.class);
			c.add(example);
			c.add(Restrictions.ne("fl_canc", "S"));
			if (ValidazioneDati.isFilled(ordinamento) )
				createCriteriaOrderIC(ordinamento, null, c);

			//almaviva5_20120308 #4885
			c.add(Restrictions.eq("cd_biblioteca", materia.getCd_biblioteca()));

			return c.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Trl_materie_utenti> getListaMaterieUtenteBiblioteca(Trl_utenti_biblioteca utenteBib) throws DaoManagerException {

		if (utenteBib == null)
			return null;

		Session session = this.getCurrentSession();

		try {
			Tbl_utenti utente = utenteBib.getId_utenti();
			Criteria criteria = session.createCriteria(Trl_materie_utenti.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			criteria.add(Restrictions.eq("id_utenti", utente));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Trl_materie_utenti> getListaMaterieUtenteBiblioteca(Tbl_utenti id_utente) throws DaoManagerException {

		if (id_utente == null)
			return null;

		Session session = this.getCurrentSession();

		try {
			Tbl_utenti utente = id_utente;
			Criteria criteria = session.createCriteria(Trl_materie_utenti.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			criteria.add(Restrictions.eq("id_utenti", utente));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}



	public void cancellaMaterie(Integer[] idMaterie, String uteVar)
			throws DaoManagerException {
		Tbl_materie materia;

		for (int i = 0; i < idMaterie.length; i++) {
			materia = this.getMateriaBibliotecaById(idMaterie[i]);
			if (materia != null) {
				materia.setFl_canc('S');
				materia.setUte_var(uteVar);
			}
		}
	}

	public List<Tbl_materie> getListaMaterie(int[] idMaterie)
	throws DaoManagerException {
		List<Tbl_materie> listaMaterie = new ArrayList<Tbl_materie>();
		Tbl_materie materia;

		for (int i=0; i<idMaterie.length; i++) {
			materia = this.getMateriaBibliotecaById(idMaterie[i]);
			if (materia!=null)	listaMaterie.add(materia);
		}

		return listaMaterie;
	}

	public Tbl_materie getMateriaBiblioteca(String codPolo, String codBib, String codMateria)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		Criteria criteria = session.createCriteria(Tbl_materie.class);
		criteria.add(Restrictions.eq("cd_biblioteca", serviziHibDAO.getBibliotecaInPolo(codPolo, codBib)))
				.add(Restrictions.eq("cod_mat", codMateria));

		return (Tbl_materie)criteria.uniqueResult();
	}

	public Tbl_materie getMateriaBibliotecaById(int idMateria) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			return (Tbl_materie) loadNoLazy(session, Tbl_materie.class, new Integer(idMateria));
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Trl_materie_utenti verificaEsistenzaMateriaUtenteBiblioteca(
			Tbl_utenti utente, Tbl_materie materia) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trl_materie_utenti.class);
			criteria.add(Restrictions.eq("id_utenti", utente));
			criteria.add(Restrictions.eq("id_materia", materia));

			Trl_materie_utenti materie_utenti = (Trl_materie_utenti) criteria
					.uniqueResult();
			return materie_utenti;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciMateriaUtente(Trl_materie_utenti materia_utente)
	throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(materia_utente);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciMateriaUtente(Tbl_utenti utente, Tbl_materie materia,
			Trl_materie_utenti materia_utente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			//utente.getTrl_materie_utenti().add(materia_utente);
			materia_utente.setId_utenti(utente);
			//materia.getTrl_materie_utenti().add(materia_utente);
			materia_utente.setId_materia(materia);

			session.saveOrUpdate(materia_utente);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Trl_materie_utenti getMateriaUtenteBiblioteca(Tbl_utenti utente, Tbl_materie materia) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trl_materie_utenti.class);
			criteria.add(Restrictions.eq("fl_canc", 'N'));
			criteria.add(Restrictions.eq("id_utenti", utente));
			criteria.add(Restrictions.eq("id_materia", materia));

			Trl_materie_utenti materie_utenti = (Trl_materie_utenti) criteria
					.uniqueResult();
			return materie_utenti;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esisteMateriaUtenteBiblioteca(int idUte, int idMat) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trl_materie_utenti.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			criteria.add(Restrictions.eq("id_utenti_id_utenti", idUte));
			criteria.add(Restrictions.eq("id_materia_id_materia", idMat));
			criteria.setProjection(Projections.rowCount());

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public void aggiornaMateriaUtente(Trl_materie_utenti materia_utente)  throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(materia_utente);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public int countUtentiLegatiMateria(int id) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Trl_materie_utenti.class);
			c.add(Restrictions.ne("fl_canc", 'S'));

			c.add(Restrictions.eq("id_materia.id", id));
			c.setProjection(Projections.rowCount());

			Number rowCount = (Number) c.uniqueResult();
			return rowCount.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
