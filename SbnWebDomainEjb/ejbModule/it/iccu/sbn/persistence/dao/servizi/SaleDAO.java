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

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.sale.StatoPrenotazionePosto;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.servizi.Tbl_posti_sala;
import it.iccu.sbn.polo.orm.servizi.Tbl_prenotazione_posto;
import it.iccu.sbn.polo.orm.servizi.Tbl_sale;
import it.iccu.sbn.polo.orm.servizi.Trl_posto_sala_categoria_mediazione;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class SaleDAO extends ServiziBaseDAO {

	private static Tbc_inventario creaIdInventario(InventarioVO inv) {
		Tbc_inventario i = new Tbc_inventario();
		Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
		serie.setCd_polo(creaIdBib(inv.getCodPolo(), inv.getCodBib()));
		serie.setCd_serie(inv.getCodSerie());
		i.setCd_serie(serie);
		i.setCd_inven(inv.getCodInvent());

		return i;
	}

	@SuppressWarnings("unchecked")
	public List<Tbl_sale> getListaSale(String codPolo, String codBib, String codSala, String descr,
			boolean conPostiLiberi, String ordinamento) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbl_sale.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		c.add(Restrictions.eq("cd_bib", bib));

		if (isFilled(codSala))
			c.add(Restrictions.eq("cod_sala", codSala));

		if (isFilled(descr))
			c.add(Restrictions.eq("descr", descr));

		if (conPostiLiberi)
			c.add(Restrictions.ltProperty("num_prg_posti", "num_max_posti"));

		c.addOrder(Order.asc("cod_sala"));

		return c.list();
	}

	public Tbl_sale aggiornaSala(Tbl_sale sala)
			throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		this.setSessionCurrentCfg();
		try {
			if (sala.getId_sale() == 0) {
				session.save(sala);
			} else
				sala = (Tbl_sale) session.merge(sala);

			session.flush();
			return sala;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_sale esisteSala(Tbl_sale sala) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Tbl_sale.class);
			int id = sala.getId_sale();
			c.add(Restrictions.eq("cd_bib", sala.getCd_bib()));
			c.add(Restrictions.eq("cod_sala", sala.getCod_sala()));
			if (id > 0)
				c.add(Restrictions.ne("id_sale", id));

			return (Tbl_sale) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public Tbl_sale getSalaById(int idSala) throws DaoManagerException {
		Session session = getCurrentSession();
		return (Tbl_sale) session.get(Tbl_sale.class, idSala);
	}

	public Tbl_posti_sala aggiornaPosto(Tbl_posti_sala posto)
			throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		this.setSessionCurrentCfg();
		try {
			if (posto.getId_posti_sala() == 0) {
				session.save(posto);
			} else
				posto = (Tbl_posti_sala) session.merge(posto);

			session.flush();
			return posto;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_posti_sala> getPostiSala(Tbl_sale sala) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Tbl_posti_sala.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.eq("id_sale", sala));
			c.addOrder(Order.asc("gruppo")).addOrder(Order.asc("num_posto"));

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public Tbl_posti_sala esistePosto(Tbl_posti_sala tbl_posti_sala) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Tbl_posti_sala.class);
			c.add(Restrictions.eq("id_sale", tbl_posti_sala.getId_sale()));
			c.add(Restrictions.eq("num_posto", tbl_posti_sala.getNum_posto()));
			int id = tbl_posti_sala.getId_posti_sala();
			if (id > 0)
				c.add(Restrictions.ne("id_posti_sala", id));

			return (Tbl_posti_sala) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public void cancellaMediazioni(Tbl_posti_sala tbl_posti_sala) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query q = session.createQuery("delete Trl_posto_sala_categoria_mediazione scm where scm.id_posto_sala=:posto");
			q.setEntity("posto", tbl_posti_sala);
			q.executeUpdate();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public void inserisciMediazione(Trl_posto_sala_categoria_mediazione cat_med) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.save(cat_med);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_prenotazione_posto> getListaPrenotazioniPosto(String codPolo, String codBib, int id_posto,
			String codUtente, Timestamp ts_inizio, Timestamp ts_fine, String ordinamento, InventarioVO inv,
			List<Character> stati, List<String> cd_cat_mediazione, int maxRichiesteAssociate,
			boolean escludiPrenotSenzaSupporto, List<Integer> prenotazioniEscluse) throws DaoManagerException {
		Session session = getCurrentSession();
		StringBuilder hql = new StringBuilder(1024);
		Map<String, Object> params = new HashMap<String, Object>();

		hql.append("select pp from Tbl_prenotazione_posto pp");

		if (isFilled(cd_cat_mediazione)) {
			hql.append(" join pp.posto.trl_posto_sala_categoria_mediazione cat_med");
		}

		if (inv != null) {
			hql.append(" join pp.richieste rs");
		}

		hql.append(" where pp.fl_canc<>'S' and pp.biblioteca=:bib");
		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		params.put("bib", bib);

		if (isFilled(id_posto)) {
			hql.append(" and pp.posto.id=:id_posto");
			params.put("id_posto", id_posto);
		}

		if (isFilled(codUtente)) {
			hql.append(" and lower(pp.utente.cod_utente)=lower(:cod_utente)");
			params.put("cod_utente", codUtente);
		}

		if (ts_inizio == null)
			ts_inizio = new Timestamp(DateUtil.MIN_DATE.getTime());
		if (ts_fine == null)
			ts_fine = new Timestamp(DateUtil.MAX_DATE.getTime());

		if (ts_inizio != null && ts_fine != null) {
//			String r1 = addTimeStampRange("pp.ts_inizio", ts_inizio, ts_fine);
//			String r2 = addTimeStampRange("pp.ts_fine", ts_inizio, ts_fine);
//			hql.append(" and (").append(r1).append(" or ").append(r2).append(")");

//			ts_inizio = DateUtil.addMillis(ts_inizio, 1);
//			ts_fine = DateUtil.addMillis(ts_fine, -1);	//estremo superiore escluso

			hql.append(" and (");

//			hql.append("(pp.ts_inizio <= :ts_inizio and pp.ts_fine between :ts_inizio and :ts_fine) or ");
//			hql.append("(pp.ts_inizio between :ts_inizio and :ts_fine and pp.ts_fine <= :ts_fine) or ");
//			hql.append("(pp.ts_inizio <= :ts_inizio and pp.ts_fine >= :ts_fine) or ");
//			hql.append("(pp.ts_inizio between :ts_inizio and :ts_fine and pp.ts_fine >= :ts_fine)");

			hql.append("(pp.ts_inizio between :ts_inizio and :ts_fine) or ");
			hql.append("(pp.ts_fine between :ts_inizio and :ts_fine)");

			hql.append(")");
			params.put("ts_inizio", ts_inizio);
			params.put("ts_fine", ts_fine);
		}

		if (inv != null) {
			hql.append(" and rs.cd_polo_inv=:inv");
			params.put("inv", creaIdInventario(inv));
		}

		if (isFilled(stati)) {
			hql.append(" and pp.cd_stato in (:stati)");
			params.put("stati", stati);
		}

		if (isFilled(cd_cat_mediazione)) {
			hql.append(" and cat_med.cd_cat_mediazione in (:med)");
			params.put("med", cd_cat_mediazione);
		}

		if (isFilled(maxRichiesteAssociate)) {
			hql.append(" and :max > (");
			hql.append("select count(req) from Tbl_richiesta_servizio req");
			hql.append(" join req.prenotazioni_posti rpp");
			hql.append(" where req.cod_stato_rich in ('A', 'C', 'G', 'I', 'M', 'N', 'P', 'R') and rpp.id = pp.id");
			hql.append(" and req.fl_canc<>'S'");
			hql.append(")");
			params.put("max", new Long(maxRichiesteAssociate));
		}

		if (escludiPrenotSenzaSupporto) {
			hql.append(" and pp.cd_cat_mediazione in (");
			hql.append("select c.cd_tabella from Tb_codici c");
			hql.append(" where c.tp_tabella='").append(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE.getTp_Tabella()).append("'");
			hql.append(" and c.cd_flg2='S'");
			hql.append(")");
		}

		if (escludiPrenotSenzaSupporto) {
			if (isFilled(stati)) {
				hql.append(" and pp.cd_stato in (:stati)");
				params.put("stati", stati);
			}
		}

		if (isFilled(prenotazioniEscluse)) {
			hql.append(" and pp.id not in (:escluse)");
			params.put("escluse", prenotazioniEscluse);
		}

		hql.append(" order by pp.ts_inizio");

		Query q = session.createQuery(hql.toString() );
		q.setProperties(params);

		return q.list();
	}

	public Tbl_prenotazione_posto aggiornaPrenotazionePosto(Tbl_prenotazione_posto pp)
			throws DaoManagerException, DAOConcurrentException {

		Session session = this.getCurrentSession();
		this.setSessionCurrentCfg();
		try {
			if (pp.getId_prenotazione() == 0) {
				session.save(pp);
			} else
				pp = (Tbl_prenotazione_posto) session.merge(pp);

			session.flush();
			return pp;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_posti_sala> getPostiSalaByCatMediazione(String codPolo, String codBib, String cd_cat_mediazione, boolean utenteRemoto) throws DaoManagerException {
		try {
			Criteria c = preparaCriteriaGetPostiSalaByCatMediazione(codPolo, codBib, cd_cat_mediazione, utenteRemoto);
			c.addOrder(Order.asc("sala.cod_sala"))
				.addOrder(Order.asc("gruppo"))
				.addOrder(Order.asc("num_posto"));
			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public int countPostiSalaByCatMediazione(String codPolo, String codBib, String cd_cat_mediazione) throws DaoManagerException {
		try {
			Criteria c = preparaCriteriaGetPostiSalaByCatMediazione(codPolo, codBib, cd_cat_mediazione, false);
			c.setProjection(Projections.rowCount());
			Number cnt = (Number) c.uniqueResult();
			return cnt.intValue();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	private Criteria preparaCriteriaGetPostiSalaByCatMediazione(String codPolo, String codBib, String cd_cat_mediazione, boolean utenteRemoto) throws DaoManagerException {
		Session session = this.getCurrentSession();

		Criteria c = session.createCriteria(Tbl_posti_sala.class);
		c.add(Restrictions.ne("fl_canc", 'S'));

		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		Criteria c_sale = c.createCriteria("id_sale", "sala");
		c_sale.add(Restrictions.eq("sala.cd_bib", bib));
		if (utenteRemoto)
			//sala prenotabile da remoto
			c_sale.add(Restrictions.eq("fl_prenot_remoto", 'S'));

		if (isFilled(cd_cat_mediazione))
			c.createCriteria("trl_posto_sala_categoria_mediazione", "med").add(Restrictions.eq("cd_cat_mediazione", cd_cat_mediazione));

		return c;
	}

	public Tbl_prenotazione_posto getPrenotazionePostoById(int idPrenotazione) throws DaoManagerException {
		Session session = getCurrentSession();
		return (Tbl_prenotazione_posto) session.get(Tbl_prenotazione_posto.class, idPrenotazione);
	}

	public List<Integer> getListaIdPrenotazioniPostoScadute(String codPolo, String codBib, Timestamp dataFinePrev) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbl_prenotazione_posto.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		if (bib != null)
			c.add(Restrictions.eq("biblioteca", bib));

		c.add(Restrictions.le("ts_fine", dataFinePrev) );

		List<Character> stati = Arrays.asList(StatoPrenotazionePosto.IMMESSA.getStato(), StatoPrenotazionePosto.FRUITA.getStato());
		c.add(Restrictions.in("cd_stato", stati) );

		c.addOrder(Order.asc("ts_inizio"));

		c.setProjection(Projections.property("id_prenotazione"));

		return c.list();
	}

	public int countRichiesteCollegatePrenotazione(Tbl_prenotazione_posto pp) throws DaoManagerException {
		Session session = getCurrentSession();
		Query filter = session.createFilter(pp.getRichieste(),
				"select count(*) where cod_stato_mov not in ('C', 'E') and cod_stato_rich not in( 'B', 'D', 'F', 'H')");
		Number cnt = (Number) filter.uniqueResult();
		return cnt.intValue();
	}

	public int countPrenotazioniSala(int idSala) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbl_prenotazione_posto.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.in("cd_stato", new Character[] { StatoPrenotazionePosto.IMMESSA.getStato(),
				StatoPrenotazionePosto.FRUITA.getStato() }));
		c.createCriteria("posto", "p").add(Restrictions.eq("p.id_sale.id", idSala));

		c.setProjection(Projections.rowCount());
		Number cnt = (Number) c.uniqueResult();

		return cnt.intValue();

	}

	public int countPostiLiberi(String codPolo, String codBib) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tbl_posti_sala.class);
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.add(Restrictions.ne("occupato", 'S'));
		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		c.createCriteria("id_sale").add(Restrictions.eq("cd_bib", bib));

		c.setProjection(Projections.rowCount());
		Number cnt = (Number) c.uniqueResult();

		//TODO inserire range date???

		return cnt.intValue();
	}

	public void eliminaPrenotazionePostoStoricizzata(Tbl_prenotazione_posto prenot_posto) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			prenot_posto.getRichieste().clear();
			prenot_posto.getUtenti().clear();
			session.delete(prenot_posto);
			session.flush(); // non rimuovere
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
