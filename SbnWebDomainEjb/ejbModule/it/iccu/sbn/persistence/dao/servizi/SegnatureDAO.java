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

import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.exception.IntervalloSegnaturaNonValidoException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_disponibilita_precatalogati;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;


public class SegnatureDAO extends ServiziBaseDAO {


	private boolean intervalloCorretto(Tbl_disponibilita_precatalogati segnatura) throws DaoManagerException {
		Session session = this.getCurrentSession();

		Criteria criteria = session.createCriteria(Tbl_disponibilita_precatalogati.class);
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("cd_bib", segnatura.getCd_bib()))
				.add(Restrictions.or(Restrictions.and(Restrictions.between("segn_da", segnatura.getSegn_da(), segnatura.getSegn_a()), Restrictions.gt("segn_a", segnatura.getSegn_a())),
									 Restrictions.and(Restrictions.between("segn_a",  segnatura.getSegn_da(), segnatura.getSegn_a()), Restrictions.lt("segn_da", segnatura.getSegn_da()))
									)
					);

		Number count = (Number) criteria.uniqueResult();
		return (count.intValue() == 0);
	}


	public void insert(Tbl_disponibilita_precatalogati segnatura)
	throws DaoManagerException, IntervalloSegnaturaNonValidoException, AlreadyExistsException {
		try {
			if (!this.intervalloCorretto(segnatura)) {
				//Errore. L'intervallo non è valido
				throw new IntervalloSegnaturaNonValidoException();
			}

			Tbl_disponibilita_precatalogati segn = this.selectCancellata(segnatura);
			if (segn != null && segn.getFl_canc()=='S') {
				//già esiste una riga con lo stesso codice ma cancellata
				segn.setFl_canc    ('N');
				segn.setCod_frui   (segnatura.getCod_frui());
				segn.setCod_no_disp(segnatura.getCod_no_disp());
				segn.setSegn_a     (segnatura.getSegn_a());
				segn.setSegn_da    (segnatura.getSegn_da());
				segn.setSegn_fine  (segnatura.getSegn_fine());
				segn.setSegn_inizio(segnatura.getSegn_inizio());
				segn.setTs_ins     (segnatura.getTs_ins());
				segn.setTs_var     (segnatura.getTs_var());
				segn.setUte_ins    (segnatura.getUte_ins());
				segn.setUte_var    (segnatura.getUte_var());

				this.save(segn);

			} else if (segn != null) {
						throw new AlreadyExistsException();
					} else {
						try {
							long appo=this.calcolaNextCodSegnatura(segnatura.getCd_bib().getCd_polo().getCd_polo(), segnatura.getCd_bib().getCd_biblioteca());
							segnatura.setCod_segn((int) appo);
						} catch (Exception e) {

						}
						this.save(segnatura);
					}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public long calcolaNextCodSegnatura(String codPolo, String codBib)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("calcolaNextCodSegnatura");
			query.setString("cd_polo", codPolo);
			query.setString("cd_bib", codBib);
			Long codSegn = (Long) query.uniqueResult();

			if (codSegn != null)
				return ++codSegn;
			else
				return 1;

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public void save(Tbl_disponibilita_precatalogati segnatura) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(segnatura);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public void update(Tbl_disponibilita_precatalogati segnatura)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			session.update(segnatura);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public void delete(Integer[] id, String uteVar)
	throws DaoManagerException {
		Tbl_disponibilita_precatalogati segnatura=null;

		for (int i=0; i<id.length; i++) {
			segnatura = this.select(id[i]);
			if (segnatura!=null)	{
				segnatura.setFl_canc('S');
				segnatura.setUte_var(uteVar);
			}
		}
	}


	public Tbl_disponibilita_precatalogati select(int id)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			return (Tbl_disponibilita_precatalogati)loadNoLazy(session, Tbl_disponibilita_precatalogati.class, new Integer(id));
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public int contaRangeSegnature(String codPolo, String codBib)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);

			Criteria criteria =
				session.createCriteria(Tbl_disponibilita_precatalogati.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cd_bib", bib))
					.setProjection(Projections.rowCount());

			Number count = (Number) criteria.uniqueResult();
			return count.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_disponibilita_precatalogati> select(String codPolo, String codBib, Tbl_disponibilita_precatalogati segnatura, String ordinamento)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		UtilitaDAO utilDao = new UtilitaDAO();
		try {
			Tbf_biblioteca_in_polo bib = utilDao.getBibliotecaInPolo(codPolo, codBib);

			Criteria criteria = session.createCriteria(Tbl_disponibilita_precatalogati.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cd_bib",  bib));

			String segn_da = segnatura.getSegn_da();
			if (ValidazioneDati.isFilled(segn_da)) {
				//almaviva5_20101104 #3959
				criteria.add(Restrictions.like("segn_da", segn_da.trim(), MatchMode.START));

				//criteria.add(Restrictions.le("segn_da", segn_da));
				//criteria.add(Restrictions.ge("segn_a",  segn_da));
			}

			String cod_frui = segnatura.getCod_frui();
			if (ValidazioneDati.isFilled(cod_frui))
				criteria.add(Restrictions.eq("cod_frui", cod_frui));

			String cod_no_disp = segnatura.getCod_no_disp();
			if (ValidazioneDati.isFilled(cod_no_disp))
				criteria.add(Restrictions.eq("cod_no_disp", cod_no_disp));

			if (ordinamento!=null && !ordinamento.equals(""))
				createCriteriaOrder(ordinamento, null, criteria);

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_disponibilita_precatalogati selectNarrowestRange(String codPolo, String codBib, String segnatura)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);

			Criteria c = session.createCriteria(Tbl_disponibilita_precatalogati.class);
			c.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("cd_bib",  bib))
				.add(Restrictions.le("segn_da", segnatura))
				.add(Restrictions.ge("segn_a",  segnatura));

			c.addOrder(Order.desc("segn_da"))
				.addOrder(Order.asc("segn_a"));


			List<Tbl_disponibilita_precatalogati> ranges = c.list();
			if (!ValidazioneDati.isFilled(ranges))
				return null;
			return ranges.get(0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	private Tbl_disponibilita_precatalogati selectCancellata(Tbl_disponibilita_precatalogati segnatura) throws DaoManagerException {
		Session session = this.getCurrentSession();

		Criteria criteria = session.createCriteria(Tbl_disponibilita_precatalogati.class);
		criteria = session.createCriteria(Tbl_disponibilita_precatalogati.class);
		criteria.add(Restrictions.eq("cd_bib", segnatura.getCd_bib()))
				.add(Restrictions.eq("id_disponibilita_precatalogati", segnatura.getId_disponibilita_precatalogati()));

		return (Tbl_disponibilita_precatalogati)criteria.uniqueResult();
	}


	public List<Tbl_disponibilita_precatalogati> selectAll() throws DaoManagerException {
		Session session = this.getCurrentSession();

		Criteria c = session.createCriteria(Tbl_disponibilita_precatalogati.class);
		c = session.createCriteria(Tbl_disponibilita_precatalogati.class);
		return c.list();
	}


}
