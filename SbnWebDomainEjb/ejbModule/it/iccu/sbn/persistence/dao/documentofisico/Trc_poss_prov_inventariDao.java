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
package it.iccu.sbn.persistence.dao.documentofisico;

import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDiInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriRicercaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.documentofisico.Trc_poss_prov_inventari;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Trc_poss_prov_inventariDao extends DaoManager {

	private static Logger log = Logger.getLogger(Trc_poss_prov_inventariDao.class);

	public Trc_poss_prov_inventariDao() {
		super();
	}

	public List getListaPossessori(String codPolo, String codBib,
			PossessoriRicercaVO possRic) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_poss_prov_inventari.class);
			cr.add(Restrictions.ne("fl_canc", 'S'));
			Criteria cr3 = null;
			Criteria cr4 = null;

			if (possRic.isSoloBib()) {

				cr3 = cr.createAlias("cd_polo", "inventario");
				cr3.add(Restrictions.ne("fl_canc", 'S'));

				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(codPolo);
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(codBib);
				bib.setCd_polo(polo);

				cr4 = cr3.createAlias("cd_serie", "serie");
				cr4.add(Restrictions.ne("fl_canc", 'S'));
				cr4.add(Restrictions.eq("cd_polo", bib));

				//
				int i = 0;
				System.out.println(cr.getAlias());

			}

			List<Trc_poss_prov_inventari> results = cr
					.list();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}

	public Integer countInventariPerPossessore(String pid)
			throws DaoManagerException {

		Integer countPoss = 0;

		try {
			Session session = this.getCurrentSession();

			Tbc_possessore_provenienza poss = new Tbc_possessore_provenienza();
			poss.setPid(pid);
			countPoss = (Integer) session.createCriteria(
					Trc_poss_prov_inventari.class).setProjection(
					Projections.rowCount()).add(Restrictions.eq("p", poss))
					.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return countPoss;
	}

	public Integer countInventariPerPossessore(String pid, String codSerie,
			String codInv) throws DaoManagerException {

		Integer countPoss = 0;

		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session
					.createCriteria(Trc_poss_prov_inventari.class);

			Tbc_possessore_provenienza poss = new Tbc_possessore_provenienza();
			poss.setPid(pid);
			Criteria criteriaInv = criteria.createCriteria("cd_polo");
			criteriaInv.add(Restrictions.ne("fl_canc", 'S'));
			criteriaInv.add(Restrictions.eq("cd_polo.cd_serie.cd_serie",
					codSerie));
			criteriaInv.add(Restrictions.eq("cd_polo.cd_inven", codInv));

			countPoss = (Integer) session.createCriteria(
					Trc_poss_prov_inventari.class).setProjection(
					Projections.rowCount()).add(Restrictions.eq("p", poss))
					.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return countPoss;
	}

	public Integer countInventariPerPossessoreBib(String codPolo,
			String codBib, String pid) throws DaoManagerException {

		Number countInv = null;

		try {
			Session session = this.getCurrentSession();
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Tbc_possessore_provenienza poss = new Tbc_possessore_provenienza();
			poss.setPid(pid);
			String hql = "select count (pp) from Trc_poss_prov_inventari pp "
					+ "where pp.p.id = :pid "
					+ "and pp.cd_polo.cd_serie.cd_polo = :bib  "
					//almaviva5_20160613 #6216 inv. non cancellato
					+ "and pp.cd_polo.fl_canc <> 'S' "
					+ "and pp.fl_canc <> 'S'";
			countInv = (Number) session.createQuery(hql)
					.setString("pid", pid)
					.setEntity("bib", bib).uniqueResult();
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return countInv.intValue();
	}

	public Integer countInventariPerPossessoreBib(String codPolo,
			String codBib, String pid, String serie, String codInv)
			throws DaoManagerException {

		Number countInv = null;

		try {
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Tbc_possessore_provenienza poss = new Tbc_possessore_provenienza();
			poss.setPid(pid);
			String hql = "select count (pp) "
					+ "from Trc_poss_prov_inventari pp "
					+ "where pp.p.id = :pid "
					+ "and pp.cd_polo.cd_serie.cd_polo = :bib  "
					+ "and ((pp.cd_polo.cd_serie.cd_serie = :serie and pp.cd_polo.cd_inven > :codInv) "
					+ "or (pp.cd_polo.cd_serie.cd_serie > :serie)) "
					//almaviva5_20160613 #6216 inv. non cancellato
					+ "and pp.cd_polo.fl_canc <> 'S' "
					+ "and pp.fl_canc <> 'S'";
			countInv = (Number) session.createQuery(hql)
					.setString("pid", pid)
					.setString("serie", serie)
					.setInteger("codInv", Integer.valueOf(codInv))
					.setEntity("bib", bib)
					.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return countInv.intValue();
	}

	public Integer countPossessoriPerInventario(String codPolo, String codBib,
			String codSerie, String codInv, String codLegame)
			throws DaoManagerException {

		Number countInv = null;

		try {
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			Tbc_inventario inventario = new Tbc_inventario();
			inventario.setCd_serie(serie);
			inventario.setCd_inven(Integer.parseInt(codInv));

			countInv = (Integer) session.createCriteria(
					Trc_poss_prov_inventari.class).setProjection(
					Projections.rowCount()).add(
					Restrictions.eq("cd_polo.cd_serie", serie)).add(
					Restrictions.eq("cd_polo.cd_inven", Integer
							.parseInt(codInv))).add(
					Restrictions.eq("cd_legame", codLegame)).add(
					Restrictions.ne("fl_canc", 'S')).uniqueResult();
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return countInv.intValue();
	}

	public Integer countPossessoriPerInventario(String codPolo, String codBib,
			String pid, String codSerie, String codInv, String codLegame)
			throws DaoManagerException {

		Number countInv = null;

		try {
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			Tbc_inventario inventario = new Tbc_inventario();
			inventario.setCd_serie(serie);
			inventario.setCd_inven(Integer.parseInt(codInv));
			Tbc_possessore_provenienza poss = new Tbc_possessore_provenienza();
			poss.setPid(pid);

			countInv = (Integer) session.createCriteria(
					Trc_poss_prov_inventari.class).setProjection(
					Projections.rowCount()).add(
					Restrictions.eq("cd_serie", serie)).add(
					Restrictions.eq("cd_inven", codInv)).add(
					Restrictions.eq("pid", pid)).add(
					Restrictions.ne("fl_canc", 'S')).uniqueResult();

			// countInv = (Number)session.createQuery("select count (pp) " +
			// "from Trc_poss_prov_inventari pp " +
			// "where pp.p.id = :pid " +
			// "and pp.cd_polo.cd_serie.cd_polo = :bib  " +
			// "and ((pp.cd_polo.cd_serie.cd_serie = :serie and pp.cd_polo.cd_inven > :codInv) or (pp.cd_polo.cd_serie.cd_serie > :serie)) "
			// +
			// "and pp.cd_legame = :codLegame and pp.fl_canc <> 'S'")
			// .setString("pid", pid).setString("codLegame",
			// codLegame).setString("serie", serie).setInteger("codInv",
			// Integer.valueOf(codInv)).setEntity("bib", bib).uniqueResult();
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return countInv.intValue();
	}

	public Integer countLegamiPerInventario(String codPolo, String codBib, String codSerie, int codInv)
			throws DaoManagerException {

		Number countInv = null;

		try {
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			Tbc_inventario inventario = new Tbc_inventario();
			inventario.setCd_serie(serie);
			inventario.setCd_inven(codInv);

			countInv = (Integer) session.createCriteria(Trc_poss_prov_inventari.class)
				.setProjection(Projections.rowCount())
				.add(Restrictions.eq("cd_polo", inventario))
				.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return countInv.intValue();
	}

	public boolean deleteLegamiPerInventario(String codPolo, String codBib,
			String codSerie, int codInv, String uteVar) throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			Tbc_inventario inventario = new Tbc_inventario();
			inventario.setCd_serie(serie);
			inventario.setCd_inven(codInv);

			String hql = "update Trc_poss_prov_inventari set fl_canc='S', ts_var=:tsVar, ute_var=:uteVar where cd_polo=:inv";

			Query query = session.createQuery(hql);
			query.setTimestamp("tsVar", DaoManager.now() );
			query.setString("uteVar", uteVar);
			query.setEntity("inv", inventario);
			int rows = query.executeUpdate();

			return (rows > 0);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

	//
	public List getListaPossessoriInventario(String codPolo,
			String codBib, String codInve) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_poss_prov_inventari.class);
			cr.add(Restrictions.ne("fl_canc", 'S'));
			Criteria cr3 = null;
			Criteria cr4 = null;

			cr3 = cr.createAlias("cd_polo", "inventario");
			cr3.add(Restrictions.ne("fl_canc", 'S'));

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr4 = cr3.createAlias("cd_serie", "serie");
			cr4.add(Restrictions.ne("fl_canc", 'S'));
			cr4.add(Restrictions.eq("cd_polo", bib));

			Criteria cr5 = cr.createAlias("p", "possessore");
			cr5.add(Restrictions.ne("fl_canc", 'S'));

			List<Trc_poss_prov_inventari> results = cr
					.list();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}

	public List<Trc_poss_prov_inventari> getListaInventariPossessori(
			String pid, int limite) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();// HibernateUtil.getSession();

			String hql = " from Trc_poss_prov_inventari pp "
					+ "where pp.p.id = :pid "
					+ "and pp.fl_canc <> 'S' "
					+ "and pp.cd_polo.fl_canc <> 'S' "
					+ "order by pp.cd_polo.cd_serie.cd_serie, pp.cd_polo.cd_inven";

			List<Trc_poss_prov_inventari> results = session.createQuery(hql)
					.setString("pid", pid)
					.setMaxResults(limite).list();
			return results;

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Trc_poss_prov_inventari> getListaInventariPossessori(
			String pid, int limite, String ultCodSerie, String ultCodInv)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();// HibernateUtil.getSession();
			Criteria criteria = session
					.createCriteria(Trc_poss_prov_inventari.class);

			criteria.add(Restrictions.eq("p.id", pid));
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			Criteria criteriaInv = criteria.createCriteria("cd_polo");
			criteriaInv.add(Restrictions.ne("fl_canc", 'S'));
			criteriaInv.add(Restrictions.ne("cd_polo.cd_serie.cd_serie",
					ultCodSerie));
			criteriaInv.add(Restrictions.ne("cd_polo.cd_inven", ultCodInv))
					.addOrder(Order.asc("cd_serie")).addOrder(
							Order.asc("cd_inven"));

			List<Trc_poss_prov_inventari> results = criteria.setMaxResults(limite).list();
			return results;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Trc_poss_prov_inventari> getListaInventariPossessoriBib(
			String codPolo, String codBib, String codSerie, String codInv,
			String codLegame/* , String pid */) throws DaoManagerException {// rosa
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_poss_prov_inventari.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);
			Tbc_inventario inventario = new Tbc_inventario();
			inventario.setCd_serie(serie);
			inventario.setCd_inven(Integer.parseInt(codInv));
			// Tbc_possessore_provenienza poss = new
			// Tbc_possessore_provenienza();
			// poss.setPid(pid);

			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.eq("cd_polo", inventario));
			cr.add(Restrictions.eq("cd_legame", codLegame));
			// cr.add(Restrictions.eq("p.pid", poss.getPid()));
			List<Trc_poss_prov_inventari> results = cr
					.list();
			return results;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Trc_poss_prov_inventari> getListaInventariPossessori(
			String codPolo, String codBib, String pid)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			Tbc_possessore_provenienza poss = new Tbc_possessore_provenienza();
			poss.setPid(pid);

			Query query = session
					.createQuery(
							" from Trc_poss_prov_inventari pp where pp.p.id = :pid and pp.cd_polo.cd_serie.cd_polo = :bib and pp.fl_canc <> 'S'")
					.setString("pid", pid).setEntity("bib", bib);

			List<Trc_poss_prov_inventari> results = query
					.list();
			return results;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Trc_poss_prov_inventari> getListaInventariPossessoriBib(
			String codPolo, String codBib, String pid, int limite)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			Tbc_possessore_provenienza poss = new Tbc_possessore_provenienza();
			poss.setPid(pid);

			Query query = session
					.createQuery(
							" from Trc_poss_prov_inventari pp "
									+ "where pp.p.id = :pid "
									+ "and pp.cd_polo.cd_serie.cd_polo = :bib "
									+ "and pp.fl_canc <> 'S' "
									//almaviva5_20160613 #6216 inv. non cancellato
									+ "and pp.cd_polo.fl_canc <> 'S' "
									+ "order by pp.cd_polo.cd_serie.cd_serie, pp.cd_polo.cd_inven")
					.setString("pid", pid).setEntity("bib", bib);

			List<Trc_poss_prov_inventari> results = query.setMaxResults(limite).list();
			return results;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Trc_poss_prov_inventari> getListaInventariPossessoriBib(
			String codPolo, String codBib, String pid, int limite,
			String ultCodSerie, String ultCodInv) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Query query = session
					.createQuery(
							" from Trc_poss_prov_inventari pp "
									+ "where pp.p.id = :pid and pp.cd_polo.cd_serie.cd_polo = :bib and pp.fl_canc <> 'S' "
									//almaviva5_20160613 #6216 inv. non cancellato
									+ "and pp.cd_polo.fl_canc <> 'S' "
									+ "and ((pp.cd_polo.cd_serie.cd_serie = :serie and pp.cd_polo.cd_inven > :codInv) or (pp.cd_polo.cd_serie.cd_serie > :serie)) "
									+ "order by pp.cd_polo.cd_serie.cd_serie, pp.cd_polo.cd_inven")
					.setString("pid", pid).setString("serie", ultCodSerie)
					.setInteger("codInv", Integer.valueOf(ultCodInv))
					.setEntity("bib", bib);

			List<Trc_poss_prov_inventari> results = query.setMaxResults(limite).list();
			return results;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Object[]> getListaPossessoriDiInventario(
			String codPolo, String codBib, String codSerie, int codInv)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			Tbc_inventario inv = new Tbc_inventario();
			inv.setCd_serie(serie);
			inv.setCd_inven(codInv);

			StringBuilder queryHQL = new StringBuilder();
			queryHQL.append("from Trc_poss_prov_inventari poss_prov_inv ,Tbc_possessore_provenienza possprov ");

			queryHQL.append(" where poss_prov_inv.cd_polo =:inv");
			queryHQL.append(" and poss_prov_inv.fl_canc <> 'S' ");
			queryHQL.append(" and poss_prov_inv.p.id = possprov.pid ");
			queryHQL.append(" and possprov.fl_canc <> 'S' ");
			Query query = session.createQuery(queryHQL.toString());
			query.setEntity("inv", inv);

			return query.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Object[]> getListaPossessoriDiInventario(
			String codPolo, String codBib, String codSerie, String codInv,
			String codLegame) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(codSerie);

			Tbc_inventario inv = new Tbc_inventario();
			inv.setCd_serie(serie);
			inv.setCd_inven(Integer.parseInt(codInv));

			StringBuffer queryHQL = new StringBuffer();
			queryHQL
					.append("from Trc_poss_prov_inventari poss_prov_inv ,Tbc_possessore_provenienza possprov ");

			queryHQL.append(" where poss_prov_inv.cd_polo =:inv");
			queryHQL.append(" and poss_prov_inv.fl_canc <> 'S' ");
			queryHQL.append(" and poss_prov_inv.p.id = possprov.pid ");
			queryHQL.append(" and possprov.fl_canc <> 'S' ");
			Query query = session.createQuery(queryHQL.toString());
			query.setEntity("inv", inv);

			return query.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public String legamePossessoreInventario(PossessoriDiInventarioVO possInv, Trc_poss_prov_inventari possProvInv)
			throws DaoManagerException {
		String ret = null;
		try {
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();
			if (possProvInv == null){
				possProvInv = new Trc_poss_prov_inventari();
			}

			Tbc_possessore_provenienza possProv = new Tbc_possessore_provenienza();
			possProv.setPid(possInv.getPid());

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(possInv.getCodPolo());
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(possInv.getCodBib());
			bib.setCd_polo(polo);
			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(bib);
			serie.setCd_serie(possInv.getCodSerie());

			Tbc_inventario inv = new Tbc_inventario();
			inv.setCd_serie(serie);
			inv.setCd_inven(Integer.parseInt(possInv.getCodInv()));

			possProvInv.setP(possProv);
			possProvInv.setCd_polo(inv);
			possProvInv.setCd_legame(possInv.getCodLegame().charAt(0));
			possProvInv.setFl_canc(possInv.getFl_canc().charAt(0));
			possProvInv.setNota(possInv.getNotaLegame());
			possProvInv.setTs_ins(possInv.getDataIns());
			possProvInv.setTs_var(possInv.getDataAgg());
			possProvInv.setUte_ins(possInv.getUteIns());
			possProvInv.setUte_var(possInv.getUteAgg());

			session.saveOrUpdate(possProvInv);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return ret;
	}

	public String modNotaLegamePossInv(Trc_poss_prov_inventari poss_prov_inventari)
	throws DaoManagerException {
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			poss_prov_inventari.setTs_var(ts);
			session.saveOrUpdate(poss_prov_inventari);
			session.flush();
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ret;
	}

	// almaviva7 03/07/2008 14.24
	public String cancellaLegamePossessoreInventario(String cd_biblioteca,
			String pid, String codiceInventario, String uteFirma)
			throws DaoManagerException {
		String ret = null;
		try {
			Session session = this.getCurrentSession();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			/*
			 * causa problema di virgola dopo nome tabella..... StringBuffer
			 * queryHQL = new StringBuffer ();queryHQL.append(
			 * "update Trc_poss_prov_inventari pp set pp.fl_canc = 'S' ");
			 * queryHQL.append(",pp.ute_var =:uteFirma");
			 * queryHQL.append(",pp.ts_var =:ts");queryHQL.append(
			 * " where pp.p.id =:pid AND pp.cd_polo.cd_inven=:cd_inv" );
			 *
			 * Query query = session.createQuery(queryHQL.toString());
			 * query.setTimestamp("ts", new Date()); query.setString("uteFirma",
			 * uteFirma); query.setString("pid", pid);
			 * query.setInteger("cd_inv", Integer.valueOf(codiceInventario ));
			 */
			setSessionCurrentCfg();
			StringBuffer querySQL = new StringBuffer();
			querySQL
					.append("update Trc_poss_prov_inventari set fl_canc = 'S' ,ute_var='"
							+ uteFirma);
			querySQL.append("' ,ts_var='" + ts);
			querySQL.append("' where cd_biblioteca='" + cd_biblioteca
					+ "' AND pid='" + pid + "' AND cd_inven="
					+ Integer.valueOf(codiceInventario));
			Query query = session.createQuery(querySQL.toString());
			return ret = "" + query.executeUpdate();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	} // End cancellaLegamePossessoreInventario



	// almaviva2 Aprile 2015: gestione nuova funzionalità per fusione fra Possessori in uscita da una richiesta di
	// variazioneDescrizione (nuove funzionalità: Torna al dettaglio; Fondi oggetti;)
	public int spostaLegamiPerInventario(String pidPartenza, String pidArrivo, String uteVar) throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();

			String hql = "update Trc_poss_prov_inventari set" +
				" p.id='" + pidArrivo + "'" +
				", ute_var='"	+ uteVar + "'" +
				", ts_var='"	+ DaoManager.now() + "'" +
				" where p.id='" + pidPartenza + "' " +
				"and cd_polo not in " +
				"(select ppi.cd_polo from Trc_poss_prov_inventari ppi where ppi.p.id='" + pidArrivo + "')";

//			String hql = "update Trc_poss_prov_inventari set pid='" + pidArrivo + "' where pid='" + pidPartenza + "' " +
//			"and cd_polo.cd_biblioteca.cd_serie.cd_inven not in " +
//			"(select ppi.cd_polo||ppi.cd_biblioteca||ppi.cd_serie||ppi.cd_inven " +
//			"from Trc_poss_prov_inventari ppi where ppi.pid='" + pidArrivo + "')";

			Query query = session.createQuery(hql);
			int rows = query.executeUpdate();

			return rows;

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public int cancellaLegamePossInventPerPid(String pidPartenza, String uteVar) throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();

			String hql = "update Trc_poss_prov_inventari set" +
				" fl_canc = 'S'" +
				", ute_var='"	+ uteVar + "'" +
				", ts_var='"	+ DaoManager.now() + "'" +
				" where p.id='" + pidPartenza + "'";

			Query query = session.createQuery(hql);
			int rows = query.executeUpdate();

			return rows;

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

	public int aggiornaTipoLegamePossProv(String pidPartenza, String pidArrivo, String uteVar) throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();

			String hql = "update Trc_poss_prov_inventari set" +
				" cd_legame='R'" +
				", ute_var='"	+ uteVar + "'" +
				", ts_var='"	+ DaoManager.now() + "'" +
				" where p.id='" + pidArrivo + "' " +
				"and cd_polo in " +
				"(select ppi.cd_polo from Trc_poss_prov_inventari ppi where ppi.p.id='" + pidPartenza + "' and cd_legame = 'R')";

			Query query = session.createQuery(hql);
			int rows = query.executeUpdate();

			return rows;

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}



}
