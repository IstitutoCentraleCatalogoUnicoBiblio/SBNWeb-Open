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
package it.iccu.sbn.persistence.dao.periodici;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.periodici.kardex.TipoRangeKardex;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_messaggi;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_numero_std;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_collocazione;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.periodici.Tbp_esemplare_fascicolo;
import it.iccu.sbn.polo.orm.periodici.Tbp_fascicolo;
import it.iccu.sbn.polo.orm.periodici.Trp_messaggio_fascicolo;
import it.iccu.sbn.util.cloning.ClonePool;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;


public class PeriodiciDAO extends DaoManager {

	public Tb_titolo getTitolo(String bid) throws DaoManagerException {
		return (Tb_titolo) loadNoLazy(getCurrentSession(), Tb_titolo.class, bid);
	}

	public List<Tbp_fascicolo> getListaFascicoliPerBid(String bid, List<Short> range, Date from, Date to) throws DaoManagerException {
		Session session = getCurrentSession();

		StringBuilder hql = new StringBuilder();
		hql.append("select distinct f ");
		hql.append("from Tbp_fascicolo f ");
		hql.append("where f.fl_canc<>'S' ");
		hql.append("and f.titolo.id=:bid ");
		hql.append("and f.anno_pub in (:range)");
		String dtRange = addDateRange("f.data_conv_pub", from, to);
		if (dtRange != null)
			hql.append(" and ").append(dtRange);

		Query q = session.createQuery(hql.toString());
		q.setString("bid", bid);
		q.setParameterList("range", range);

		return q.list();
	}

	public List<Tbp_fascicolo> getListaFascicoliPerBid(String bid, Date from, Date to) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbp_fascicolo.class, "f");
		c.add(Restrictions.eq("titolo.id", bid));
		c.add(Restrictions.ne("fl_canc", 'S'));
		addDateRange(c, "data_conv_pub", from, to);

		return c.list();
	}

	public int count_listaFascicoliPerBid(String bid, Date begin, Date end) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbp_fascicolo.class, "f");
		c.setProjection(Projections.rowCount());
		c.add(Restrictions.eq("titolo.id", bid));
		c.add(Restrictions.ne("fl_canc", 'S'));
		addDateRange(c, "data_conv_pub", begin, end);

		Number cnt = (Number) c.uniqueResult();

		return cnt.intValue();
	}


	public ScrollableResults getListaFascicoliPerEsemplareCollocazione(String codPolo, String codBib, String bid) throws DaoManagerException {

		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaFascicoliPerEsemplareCollocazione");
		q.setParameter("bib", bib);
		q.setParameter("bid", bid);

		return q.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
	}

	public ScrollableResults getListaFascicoliPerCollocazione(String codPolo, String codBib, String bid)  throws DaoManagerException {

		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaFascicoliPerCollocazione");
		q.setParameter("bib", bib);
		q.setParameter("bid", bid);

		return q.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
	}

	public Tba_ordini getOrdinePiuRecentePerEsemplare(Tbc_esemplare_titolo e) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("ordinePiuRecentePerEsemplare");

		Tbf_biblioteca_in_polo bib = e.getCd_polo();
		q.setParameter("codPolo", bib.getCd_polo().getCd_polo());
		q.setParameter("codBib", bib.getCd_biblioteca());
		q.setParameter("bid", e.getB().getBid());
		q.setParameter("cod_doc", e.getCd_doc());
		q.setMaxResults(1);

		return (Tba_ordini) q.uniqueResult();
	}

	public Tba_ordini getOrdinePiuRecentePerCollocazione(Tbc_collocazione c) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("ordinePiuRecentePerCollocazione");

		q.setParameter("key_loc", c.getKey_loc());
		q.setMaxResults(1);

		return (Tba_ordini) q.uniqueResult();
	}

	public ScrollableResults getListaOrdiniConFascicoli(String codPolo,
			String codBib, String bid, List<Integer> list) throws DaoManagerException {

		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);

		Session session = getCurrentSession();
		Criteria o = session.createCriteria(Tba_ordini.class);
		o.add(Restrictions.ne("fl_canc", 'S'));
		o.add(Restrictions.eq("continuativo", '1'));
		o.add(Restrictions.eq("cd_bib", bib));

		if (ValidazioneDati.isFilled(list))
			o.add(Restrictions.in("id", list));

		Criteria ef = o.createCriteria("Tbp_esemplare_fascicolo");
		ef.add(Restrictions.ne("fl_canc", 'S'));

		Criteria f = ef.createCriteria("fascicolo");
		f.add(Restrictions.ne("fl_canc", 'S'));
		f.add(Restrictions.eq("titolo.id", bid));

		//o.addOrder(Order.desc("anno_abb"));
		o.addOrder(Order.desc("anno_ord"));
		o.addOrder(Order.asc("cod_tip_ord"));
		o.addOrder(Order.desc("cod_ord"));

		return o.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
	}

	public ScrollableResults getListaEsemplareCollocazioneSenzaFascicoli(String codPolo, String codBib, String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaEsemplareCollocazioneSenzaFascicoli");

		q.setParameter("codPolo", codPolo);
		q.setParameter("codBib", codBib);
		q.setParameter("bid", bid);

		return q.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
	}

	public ScrollableResults getListaCollocazioniSenzaFascicoli(String codPolo, String codBib, String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaCollocazioniSenzaFascicoli");

		q.setParameter("codPolo", codPolo);
		q.setParameter("codBib", codBib);
		q.setParameter("bid", bid);

		return q.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
	}

	public ScrollableResults getListaOrdiniSenzaFascicoli(String codPolo,
			String codBib, String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaOrdiniSenzaFascicoli");

		q.setParameter("codPolo", codPolo);
		q.setParameter("codBib", codBib);
		q.setParameter("bid", bid);

		return q.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
	}

	public List<Object[]> getCatenaRinnoviOrdine(String codPolo,
			String codBib, String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("catenaRinnoviOrdine");

		q.setParameter("codPolo", codPolo);
		q.setParameter("codBib", codBib);
		q.setParameter("bid", bid);

		return q.list();
	}

	public List<Object[]> getListaFascicoliPerOrdine(Tba_ordini ordine, Date from, Date to) throws DaoManagerException {

		StringBuilder hql = new StringBuilder(1024);

		hql.append("select f,ef ")
			.append("from Tbp_fascicolo f ")
			.append("left join f.Tbp_esemplare_fascicolo ef with (ef.ordine.id=:ordine) ")
			.append("where f.fl_canc<>'S' ")
			.append("and f.titolo.id=:bid ");
			//.append("and f.data_conv_pub between :from and :to ")

		String range = addDateRange("f.data_conv_pub", from, to);
		if (ValidazioneDati.isFilled(range))
			hql.append("and ").append(range).append(' ');

		hql.append("order by f.data_conv_pub desc, f.annata desc,f.num_volume desc,f.num_in_fasc desc");

		Session session = getCurrentSession();
		Query q = session.createQuery(hql.toString());
		q.setParameter("ordine", ordine.getId_ordine());
		//q.setParameter("from", from);
		//q.setParameter("to", to);
		q.setParameter("bid", ordine.getBid());

		return q.list();
	}

	public List<Object[]> getListaFascicoliPerStampa(String codPolo,
			String codBib, int id_ordine, int id_fornitore, String cd_per,
			Character stato_ordine,
			char cod_tip_ord, Date begin, Date end) throws DaoManagerException {

				StringBuilder sql = new StringBuilder(1024);
				Map<String, Object> params = new HashMap<String, Object>();
				sql.append("select distinct {f.*},{ef.*} from tbp_fascicolo f ");

				params.put("from", begin);
				params.put("to", end);

				sql.append("left join (");
				sql.append("tbp_esemplare_fascicolo ef ");
				sql.append("join tba_ordini o on o.id_ordine=ef.id_ordine and (o.cd_bib=:bib and o.cd_polo=:polo) ");
				params.put("bib", codBib);
				params.put("polo", codPolo);

				if (id_ordine == 0) {
					if (ValidazioneDati.isFilled(stato_ordine)) {
						sql.append("and o.stato_ordine=:stato_ord ");
						params.put("stato_ord", stato_ordine);
					}
					if (ValidazioneDati.isFilled(cod_tip_ord)) {
						sql.append("and o.cod_tip_ord=:tipo_ord ");
						params.put("tipo_ord", cod_tip_ord);
					}
				}

				if (id_ordine > 0) {
					sql.append("and o.id_ordine=:ordine ");
					params.put("ordine", id_ordine);
				} else
					if (id_fornitore > 0) {
						sql.append("and o.cod_fornitore=:cod_forn ");
						params.put("cod_forn", id_fornitore);
					}
				sql.append(") on ef.bid=f.bid and ef.fid=f.fid ");
				sql.append("where f.fl_canc<>'S' ");

				if (ValidazioneDati.isFilled(cd_per)) {
					sql.append("and f.cd_per=:cd_per ");
					params.put("cd_per", cd_per);
				}

				sql.append("and f.data_conv_pub between :from and :to ")
					.append("order by f.data_conv_pub desc, f.annata desc,f.num_volume desc,f.num_in_fasc desc");

				Session session = getCurrentSession();
				SQLQuery q = session.createSQLQuery(sql.toString())
					.addEntity("f", Tbp_fascicolo.class)
					.addEntity("ef", Tbp_esemplare_fascicolo.class);
				q.setProperties(params);

				return q.list();
			}

	public List<Object[]> getListaFascicoliPerOrdineDaComunicazione(Tba_ordini ordine, Date from, Date to, int cod_msg) throws DaoManagerException {

		Tra_messaggi msg = new Tra_messaggi();
		msg.setCd_bib(ordine.getCd_bib());
		msg.setCod_msg(cod_msg);

		String hql = "select f,ef " +
				"from Tbp_fascicolo f " +
				"left join f.Tbp_esemplare_fascicolo ef with (ef.ordine.id=:ordine) " +
				"where f.fl_canc<>'S' " +
				"and f.titolo.id=:bid " +
				"and f.data_conv_pub between :from and :to " +
				"and not exists (select mf from Trp_messaggio_fascicolo mf " +
				"where mf.fl_canc<>'S' and mf.fascicolo=f and mf.messaggio=:msg) " +
				"order by f.data_conv_pub desc, f.annata desc,f.num_volume desc," +
				"f.num_in_fasc desc";

		Session session = getCurrentSession();
		Query q = session.createQuery(hql);
		q.setParameter("ordine", ordine.getId_ordine());
		q.setParameter("msg", msg);
		q.setParameter("from", from);
		q.setParameter("to", to);
		q.setParameter("bid", ordine.getBid());

		return q.list();
	}

	public List<Object[]> getListaFascicoliPerCollocazione(Tbc_collocazione coll, Date from, Date to, List<Integer> rangeAnnoPubb, TipoRangeKardex tipo) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaFascicoliPerCollocazione");

		int key_loc = coll.getKey_loc();
		q.setInteger("coll", key_loc);
		q.setDate("from", from);
		q.setDate("to", to);
		//almaviva5_20121105 #58 LIG
		q.setParameterList("bid", getBidInventariCollocazione(key_loc));
		q.setParameterList("rangeAnnoPubb", ValidazioneDati.isFilled(rangeAnnoPubb) ? rangeAnnoPubb : ValidazioneDati.asSingletonList(0));

		q.setInteger("tipo", tipo.getType());

		return q.list();
	}

	public int countListaFascicoliPerCollocazione(Tbc_collocazione coll, Date from, Date to, List<Integer> rangeAnnoPubb, TipoRangeKardex tipo) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("count_listaFascicoliPerCollocazione");

		q.setInteger("coll", coll.getKey_loc());
		q.setDate("from", from);
		q.setDate("to", to);
		q.setString("bid", coll.getB().getBid());
		q.setParameterList("rangeAnnoPubb", ValidazioneDati.isFilled(rangeAnnoPubb) ? rangeAnnoPubb : ValidazioneDati.asSingletonList(0));

		q.setInteger("tipo", tipo.getType());
		Number cnt = (Number) q.uniqueResult();

		return cnt.intValue();
	}

	public List<Object[]> getEsamePeriodicoPerBiblioteca(String codPolo,
			String codBib, String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("union_esamePeriodico");

		q.setParameter("codPolo", codPolo);
		q.setParameter("codBib", codBib);
		q.setParameter("bid", bid);

		return q.list();
	}

	private int calcolaProgressivoFascicolo(Tbp_fascicolo f) throws DaoManagerException {
		Session session = getCurrentSession();

		Query q = session.getNamedQuery("calcolaFidFascicolo");
		Number next = (Number) q.uniqueResult();
		return next != null ? next.intValue() : 1;

		/*
		Criteria c = session.createCriteria(Tbp_fascicolo.class);
		c.add(Restrictions.eq("titolo", f.getTitolo()));
		c.setProjection(Projections.max("fid"));
		Number max = (Number) c.uniqueResult();
		return max != null ? max.intValue() + 1 : 1;
		*/
	}

	public Tbp_fascicolo saveFascicolo(Tbp_fascicolo f) throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		try {
			if (f.getFid() == 0) {
				int fid = calcolaProgressivoFascicolo(f);
				f.setFid(fid);
				session.save(f);
			} else
				f = (Tbp_fascicolo) session.merge(f);

			session.flush();
			return f;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbp_esemplare_fascicolo saveEsemplareFascicolo(Tbp_esemplare_fascicolo ef) throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		try {
			try {
				if (ef.getId_ese_fascicolo() == 0) {
					session.save(ef);
				} else
					ef = (Tbp_esemplare_fascicolo) session.merge(ef);

				return ef;

			} catch (StaleStateException e) {
				throw new DAOConcurrentException(e);
			} catch (HibernateException he) {
				throw new DaoManagerException(he);
			}
		} finally {
			session.flush();
		}
	}

	public Trp_messaggio_fascicolo saveMessaggioFascicolo(Trp_messaggio_fascicolo mf) throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		try {
			if (mf.getTs_var() == null)
				session.save(mf);
			 else
				mf = (Trp_messaggio_fascicolo) session.merge(mf);

			session.flush();
			return mf;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbp_fascicolo getFascicolo(String bid, int fid) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbp_fascicolo.class);
		c.add(Restrictions.eq("titolo.id", bid));
		c.add(Restrictions.eq("fid", fid));
		return (Tbp_fascicolo) c.uniqueResult();
	}

	public Tbp_esemplare_fascicolo getEsemplareFascicoloById(int id) throws DaoManagerException {
		Session session = getCurrentSession();
		return (Tbp_esemplare_fascicolo) loadNoLazy(session, Tbp_esemplare_fascicolo.class, id);
	}

	public List<Object[]> getListaFascicoliPerEsemplare(Tbc_esemplare_titolo e,	Date from, Date to, List<Integer> rangeAnnoPubb, TipoRangeKardex tipo) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaFascicoliPerEsemplare");

		q.setDate("from", from);
		q.setDate("to", to);
		q.setString("codPolo", e.getCd_polo().getCd_polo().getCd_polo());
		q.setString("codBib", e.getCd_polo().getCd_biblioteca());
		q.setString("bid_doc", e.getB().getBid());
		q.setInteger("cod_doc", e.getCd_doc());

		//almaviva5_20121105 #58 LIG
		q.setParameterList("bid", getBidInventariEsemplare(e));

		q.setParameterList("rangeAnnoPubb", ValidazioneDati.isFilled(rangeAnnoPubb) ? rangeAnnoPubb : ValidazioneDati.asSingletonList(0));
		q.setInteger("tipo", tipo.getType());

		return q.list();
	}

	public List<Tba_ordini> getListaOrdiniPeriodicoEsemplare(String codPolo, String codBib, String bid, int cd_doc) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaOrdiniPeriodicoEsemplare");

		q.setParameter("codPolo", codPolo);
		q.setParameter("codBib", codBib);
		q.setParameter("bid", bid);
		q.setParameter("cod_doc", cd_doc);

		return q.list();
	}

	public List<Tba_ordini> getListaOrdiniPeriodicoCollocazione(int key_loc) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaOrdiniPeriodicoCollocazione");

		q.setParameter("key_loc", key_loc);

		return q.list();
	}

	public boolean deleteLegamiEsemplariFascicolo(Tbc_inventario inv, String uteVar) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbp_esemplare_fascicolo.class);
		c.add(Restrictions.eq("inventario", inv));
		c.add(Restrictions.ne("fl_canc", 'S'));
		List<Tbp_esemplare_fascicolo> esemplari = c.list();
		for (Tbp_esemplare_fascicolo ef : esemplari) {

			ef.setUte_var(uteVar);
			ef.setInventario(null);
			if (ef.getOrdine() == null)
				ef.setFl_canc('S');
			session.save(ef);
		}
		session.flush();

		return true;
	}

	public List<Tbp_fascicolo> verificaEsistenzaFascicolo(Tbp_fascicolo f) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbp_fascicolo.class);
		//c.setProjection(Projections.rowCount());
		c.add(Restrictions.ne("fid", f.getFid()));	//diverso da se stesso
		c.add(Restrictions.eq("titolo.id", f.getTitolo().getBid()));
		c.add(Restrictions.eq("data_in_pubbl", f.getData_in_pubbl()));
		c.add(Restrictions.eq("cd_tipo_fasc", f.getCd_tipo_fasc()));
		c.add(Restrictions.ne("fl_canc", 'S'));

		return c.list();
	}

	public boolean verificaEsistenzaAltriEsemplariFascicolo(Tbp_fascicolo f, Tbp_esemplare_fascicolo ef) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbp_esemplare_fascicolo.class);
		c.setProjection(Projections.rowCount());
		c.add(Restrictions.eq("fascicolo", f));	//stesso fid
		if (ef != null)	//solo altri esemplari ricevuti sul fid
			c.add(Restrictions.ne("id_ese_fascicolo", ef.getId_ese_fascicolo()));

		c.add(Restrictions.ne("fl_canc", 'S'));

		Number count = (Number)c.uniqueResult();
		return (count.intValue() > 0);
	}

	public List<Tbc_inventario> getListaInventariCollocazione(int keyLoc) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbc_inventario.class);
			c.add(Restrictions.eq("key_loc.id", keyLoc));
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.addOrder(Order.desc("anno_abb"));
			c.addOrder(Order.desc("seq_coll"));
			c.addOrder(Order.asc("cd_serie"));
			c.addOrder(Order.desc("cd_inven"));
			return c.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public boolean esisteSollecitoFascicolo(Tbp_fascicolo fascicolo, Tba_ordini ordine) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Trp_messaggio_fascicolo.class);
			c.setProjection(Projections.rowCount());
			c.add(Restrictions.eq("fascicolo", fascicolo));
			c.add(Restrictions.eq("ordine", ordine));
			c.add(Restrictions.ne("fl_canc", 'S'));

			Number cnt = (Number) c.uniqueResult();
			return (cnt.intValue() > 0);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Trp_messaggio_fascicolo> getListaMessaggiComunicazioneFascicolo(
			String codPolo, String codBib, int cod_msg)
			throws DaoManagerException {
		try {
			Tra_messaggi msg = new Tra_messaggi();
			msg.setCd_bib(creaIdBib(codPolo, codBib));
			msg.setCod_msg(cod_msg);

			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Trp_messaggio_fascicolo.class);
			c.add(Restrictions.eq("messaggio", msg));
			c.add(Restrictions.ne("fl_canc", 'S'));

			return c.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public String cercaPeriodicitaTitolo(String bid) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbp_fascicolo.class);
			ProjectionList plist = Projections.projectionList();
			plist.add(Projections.groupProperty("cd_per")).add(Projections.alias(Projections.max("data_conv_pub"), "dtmax") );
			c.setProjection(plist);
			c.add(Restrictions.eq("titolo.id", bid));
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.addOrder(Order.desc("dtmax"));
			c.setMaxResults(1);

			Object[] result = (Object[]) c.uniqueResult();
			return  (result != null && result[0] != null ? result[0].toString() : null);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Tb_numero_std> getListaNumeriISSN(Tb_titolo t) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tb_numero_std.class);
			c.add(Restrictions.eq("b", t));
			c.add(Restrictions.eq("tp_numero_std", "J"));
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.addOrder(Order.desc("ts_ins"));

			return c.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Object[]> getListaFascicoliPerRangeDate(String bid, Date from, Date to) throws DaoManagerException {

		StringBuilder hql = new StringBuilder();
		hql.append("select f,ef ");
		hql.append("from Tbp_fascicolo f ");
		hql.append("left join f.Tbp_esemplare_fascicolo ef ");
		hql.append("where f.fl_canc<>'S' ");
		hql.append("and f.titolo.id=:bid ");
		hql.append("and f.data_conv_pub between :from and :to ");
		hql.append("order by f.data_conv_pub desc, f.annata desc,f.num_volume desc,");
		hql.append("f.num_in_fasc desc");

		Session session = getCurrentSession();
		Query q = session.createQuery(hql.toString());
		q.setParameter("from", from);
		q.setParameter("to", to);
		q.setParameter("bid", bid);

		return q.list();
	}

	public Object[] getRangeAnnoAbbonamentoCollocazione(int key_loc) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("rangeAnnoAbbonamentoCollocazione");
		q.setParameter("coll", key_loc);

		return (Object[]) q.uniqueResult();
	}

	public Object[] getRangeAnnoPubbFascicoliOrdine(int id_ordine) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("rangeDataPubbFascicoliOrdine");
		q.setParameter("ordine", id_ordine);

		return (Object[]) q.uniqueResult();
	}

	public Object[] getRangeAnnoAbbonamentoEsemplare(String codPolo, String codBib, String bid, int cd_doc) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("rangeAnnoAbbonamentoEsemplare");

		q.setParameter("codPolo", codPolo);
		q.setParameter("codBib", codBib);
		q.setParameter("bid", bid);
		q.setParameter("cod_doc", cd_doc);

		return (Object[]) q.uniqueResult();
	}

	public List<Integer> getRangeAnnoPubbFascicoliCollocazione(int key_loc) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("rangeAnnoPubbFascicoliCollocazione");

		q.setParameter("coll", key_loc);

		return q.list();
	}

	public List<Integer> getRangeAnnoPubbFascicoliEsemplare(String codPolo, String codBib, String bid, int cd_doc) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("rangeAnnoPubbFascicoliEsemplare");

		q.setParameter("codPolo", codPolo);
		q.setParameter("codBib", codBib);
		q.setParameter("bid", bid);
		q.setParameter("cod_doc", cd_doc);

		return q.list();
	}

	public Map<String, Integer> getListaBibliotecheEsemplareFascicolo(String bid, int fid) throws DaoManagerException {

		//Session session = getCurrentSession();
		StatelessSession session = getStatelessSession();
		Query q = session.getNamedQuery("bibliotecheEsemplareFascicolo");
		q.setCacheMode(null);

		q.setParameter("bid", bid);
		q.setParameter("fid", fid);

		List<Object[]> list = q.list();
		Map<String, Integer> map = new THashMap<String, Integer>();
		for (Object[] oo : list)
			map.put((String)oo[0], (Integer)oo[1]);

		return map;
	}

	public Tbc_inventario getUnicoInventario(Tbc_esemplare_titolo e) throws Exception {

		Session session = getCurrentSession();
		DetachedCriteria c = DetachedCriteria.forClass(Tbc_inventario.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.createCriteria("key_loc").add(Restrictions.eq("cd_biblioteca_doc", e));

		Criteria count = ClonePool.deepCopy(c).getExecutableCriteria(session);
		count.setProjection(Projections.rowCount());
		if (((Number)count.uniqueResult()).intValue() != 1)
			return null;

		return (Tbc_inventario) c.getExecutableCriteria(session).uniqueResult();
	}

	public Tbc_inventario getUnicoInventario(Tbc_collocazione coll) throws Exception {

		Session session = getCurrentSession();
		DetachedCriteria c = DetachedCriteria.forClass(Tbc_inventario.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("key_loc", coll));

		Criteria count = ClonePool.deepCopy(c).getExecutableCriteria(session);
		count.setProjection(Projections.rowCount());
		if (((Number)count.uniqueResult()).intValue() != 1)
			return null;

		return (Tbc_inventario) c.getExecutableCriteria(session).uniqueResult();
	}

	public Tbc_inventario getUnicoInventario(String codBib, int annoOrd, char tipoOrd, int codOrd) throws Exception {

		Session session = getCurrentSession();
		DetachedCriteria c = DetachedCriteria.forClass(Tbc_inventario.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("cd_bib_ord", codBib));
		c.add(Restrictions.eq("anno_ord", annoOrd));
		c.add(Restrictions.eq("cd_tip_ord", tipoOrd));
		c.add(Restrictions.eq("cd_ord", codOrd));

		Criteria count = ClonePool.deepCopy(c).getExecutableCriteria(session);
		count.setProjection(Projections.rowCount());
		if (((Number)count.uniqueResult()).intValue() != 1)
			return null;

		return (Tbc_inventario) c.getExecutableCriteria(session).uniqueResult();
	}

	public int countInventariOrdine(String codBib, int annoOrd, char tipoOrd, int codOrd) throws Exception {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbc_inventario.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("cd_bib_ord", codBib));
		c.add(Restrictions.eq("anno_ord", annoOrd));
		c.add(Restrictions.eq("cd_tip_ord", tipoOrd));
		c.add(Restrictions.eq("cd_ord", codOrd));
		c.setProjection(Projections.rowCount());

		return ((Number)c.uniqueResult()).intValue();
	}

	public int countEsemplariFascicoloOrdine(int idOrdine) throws Exception {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbp_esemplare_fascicolo.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("ordine.id", idOrdine));
		c.setProjection(Projections.rowCount());

		return ((Number)c.uniqueResult()).intValue();
	}

	public List<Short> getRangeAnnoPubbFascicoliPerEsame(String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("rangeAnnoPubbFascicoliPerEsame");

		q.setParameter("bid", bid);

		return q.list();
	}

	public List<Short> rangeAnnoPubbFascicoliPerTitolo(String bid) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("rangeAnnoPubbFascicoliPerTitolo");

		q.setParameter("bid", bid);

		return q.list();
	}

	public List<Object[]> getListaEsemplariPerFid(String codPolo, String codBib, String bid, int fid, boolean polo) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q;
		if (polo)
			q = session.getNamedQuery("listaEsemplariPerFidPolo");
		else {
			q = session.getNamedQuery("listaEsemplariPerFidBiblio");
			q.setString("codPolo", codPolo);
			q.setString("codBib", codBib);
		}

		q.setString("bid", bid);
		q.setInteger("fid", fid);

		return q.list();
	}

	public void testInsertMassivo(String bid, int annoStart, int numFascicoli) throws DaoManagerException {

		Session session = getCurrentSession();
		Timestamp ts = DaoManager.now();
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.firstDayOfYear(annoStart));

		for (int idx = 0; idx < numFascicoli; idx++) {
			Tbp_fascicolo f = new Tbp_fascicolo();
			f.setTitolo((Tb_titolo) session.load(Tb_titolo.class, bid));
			f.setFid(idx + 1);
			f.setNum_in_fasc(idx + 1);
			f.setCd_per('A');
			f.setCd_tipo_fasc('S');

			c.add(Calendar.DATE, 1);
			Date dtPub = c.getTime();
			f.setData_conv_pub(dtPub);
			f.setData_in_pubbl(dtPub);

			f.setUte_ins("test");
			f.setUte_var("test");
			f.setTs_ins(ts);

			f.setFl_canc('N');

			session.save(f);
			if ((idx %20) == 0) {
				session.flush();
				session.clear();
			}

		}

	}

	public int spostaFascicoliPerFusione(Tb_titolo old, Tb_titolo _new, String uteVar) throws DaoManagerException, DAOConcurrentException {
		Session session = getCurrentSession();

		try {
			String hql = "update versioned Tbp_fascicolo set titolo=:new,ute_var=:uteVar  where titolo=:old";
			int updCnt = session.createQuery(hql)
			        .setEntity("new", _new)
			        .setEntity("old", old)
			        .setString("uteVar", uteVar)
			        .executeUpdate();
			session.flush();
			return updCnt;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Integer> getListaIdOrdiniPerStampa(String codPolo,
			String codBib, int id_ordine, int id_fornitore,
			Character stato_ordine, char cod_tip_ord, Date begin, Date end) throws DaoManagerException {

		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tba_ordini.class, "ord");
		c.setProjection(Projections.id());
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("natura", 'S'));
		c.add(Restrictions.eq("continuativo", '1'));

		if (id_ordine > 0)
			c.add(Restrictions.idEq(id_ordine));

		else {
			c.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));
			if (id_fornitore > 0)
				c.add(Restrictions.eq("cod_fornitore.id", id_fornitore));
			if (ValidazioneDati.isFilled(stato_ordine))
				c.add(Restrictions.eq("stato_ordine", stato_ordine));
			if (ValidazioneDati.isFilled(cod_tip_ord))
				c.add(Restrictions.eq("cod_tip_ord", cod_tip_ord));
		}

		DetachedCriteria dc = DetachedCriteria.forClass(Tbp_fascicolo.class, "f");
		dc.setProjection(Projections.property("f.titolo.id"));
		dc.add(Restrictions.ne("f.fl_canc", 'S'));
		addDateRange(dc, "f.data_conv_pub", begin, end);
		dc.add(Restrictions.eqProperty("f.titolo.id", "ord.bid"));

		c.add(Subqueries.exists(dc));

		c.addOrder(Order.desc("anno_ord"))
			.addOrder(Order.asc("cod_tip_ord"))
			.addOrder(Order.desc("cod_ord"));

		return c.list();

	}

	public List<Object[]> getListaFascicoliRicevutiPerBiblioteca(
			String cod_polo, String cod_bib, String bid, Date from, Date to) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaFascicoliRicevutiPerBiblioteca");
		q.setParameter("bid", bid);
		q.setParameter("codPolo", cod_polo);
		q.setParameter("codBib", cod_bib);
		q.setDate("from", from);
		q.setDate("to", to);

		return q.list();
	}

	private List<String> getBidInventariCollocazione(int key_loc) throws DaoManagerException {
		//almaviva5_20121105 #58 LIG
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbc_inventario.class);
		c.setProjection(Projections.distinct(Projections.property("b.id")));
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.eq("key_loc.id", key_loc));

		return c.list();
	}

	private List<String> getBidInventariEsemplare(Tbc_esemplare_titolo e) throws DaoManagerException {
		//almaviva5_20121105 #58 LIG
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbc_inventario.class);
		c.setProjection(Projections.distinct(Projections.property("b.id")));
		c.add(Restrictions.ne("fl_canc", 'S'));

		Criteria c_coll = c.createCriteria("key_loc", "c");
		c_coll.add(Restrictions.eq("c.cd_biblioteca_doc", e));

		return c.list();
	}

	public List<Object[]> countFascicoliPerInventariCollocazione(String codPolo,
			String codBib, String bid, String codSez, String ord_loc, String ord_spec) throws DaoManagerException {

		try {

			Session session = getCurrentSession();
			Query q = session.getNamedQuery("countFascicoliPerInventariCollocazione");
			q.setString("bid", bid);
			q.setString("codPolo", codPolo);
			q.setString("codBib", codBib);
			q.setString("codSez", codSez);
			q.setString("ordLoc", ord_loc);
			q.setString("ordSpec", ord_spec);

			return q.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Object[]> countFascicoliPerInventariTitolo(String codPolo,
			String codBib, String bid) throws DaoManagerException {

		try {

			Session session = getCurrentSession();
			Query q = session.getNamedQuery("countFascicoliPerInventariTitolo");
			q.setString("bid", bid);
			q.setString("codPolo", codPolo);
			q.setString("codBib", codBib);

			return q.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Object[]> getListaFascicoliRicevutiPerInventario(
			String cod_polo, String cod_bib, String bid, String cd_serie,
			int cd_inven, Date from, Date to) throws DaoManagerException {

		Session session = getCurrentSession();
		Query q = session.getNamedQuery("listaFascicoliRicevutiPerInventario");
		q.setString("bid", bid);
		q.setString("codPolo", cod_polo);
		q.setString("codBib", cod_bib);
		q.setString("codSerie", cd_serie);
		q.setLong("codInv", cd_inven);
		q.setDate("from", from);
		q.setDate("to", to);

		return q.list();
	}

	public Map<String, Integer> countEsemplariBiblioteche(String bid) throws DaoManagerException {
		//almaviva5_20170721 #5612
		StatelessSession session = getStatelessSession();
		Query q = session.getNamedQuery("countEsemplariBiblioteche");
		q.setCacheMode(null);

		q.setParameter("bid", bid);

		List<Object[]> list = q.list();
		Map<String, Integer> map = new THashMap<String, Integer>();
		for (Object[] oo : list)
			map.put((String)oo[0], (Integer)oo[1]);

		return map;
	}

}
