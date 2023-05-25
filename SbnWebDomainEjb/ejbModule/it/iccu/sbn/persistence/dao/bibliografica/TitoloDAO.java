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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.bibliografica.Tr_bid_altroid;
import it.iccu.sbn.polo.orm.bibliografica.Tr_tit_bib;
import it.iccu.sbn.polo.orm.bibliografica.Tr_tit_tit;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class TitoloDAO extends DaoManager {

	public Tb_titolo getTitolo(String bid) throws DaoManagerException {
		return (Tb_titolo) loadNoLazy(getCurrentSession(), Tb_titolo.class, bid);
	}

	public Tb_titolo getTitoloLazy(String bid) throws DaoManagerException {
		return (Tb_titolo) getCurrentSession().get(Tb_titolo.class, bid);
	}

	public boolean esisteTitolo(String bid) throws DaoManagerException {
		Session session = getCurrentSession();
		Criteria c = session.createCriteria(Tb_titolo.class);
		c.add(Restrictions.eq("bid", bid));
		c.add(Restrictions.ne("fl_canc", 'S'));
		c.setProjection(Projections.rowCount());

		Number cnt = (Number) c.uniqueResult();
		return (cnt.intValue() > 0);
	}

	public Character getFlagCondivisioneTitolo(String bid) throws DaoManagerException {
		Session session = getCurrentSession();
		org.hibernate.Query q = session.getNamedQuery("flagCondivisioneTitolo");
		q.setString("bid", bid);

		return (Character) q.uniqueResult();
	}

	public Tb_titolo getMonografiaSuperiore(String bid) throws DaoManagerException {
		Tb_titolo inf = (Tb_titolo) loadNoLazy(getCurrentSession(), Tb_titolo.class, bid);
		return getMonografiaSuperiore(inf);
	}

	public Tb_titolo getMonografiaSuperiore(Tb_titolo inf) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tr_tit_tit.class);

			c.add(Restrictions.eq("Bid_base", inf));
			c.add(Restrictions.eq("tp_legame", "01"));
			c.add(Restrictions.eq("cd_natura_coll", 'M'));
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.setMaxResults(1);
			Tr_tit_tit tt = (Tr_tit_tit) c.uniqueResult();

			if (tt != null){
				return tt.getBid_coll();
			}else{
				return null;
			}

		} catch (org.hibernate.ObjectNotFoundException e) {
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

	public String concatenaAreeTitolo(String bid, String[] etichette, int maxLen, boolean isTitSupPrefisso) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			StringBuilder sql = new StringBuilder(512);

//			select cd_natura, concatena_aree(tit.isbd, tit.indice_isbd, ARRAY['200','210','215'], tit.bid, 0, ' [', '] ', 87)
//			from tb_titolo tit
//			where bid in ('SBW0077896','CFI0462204','CFI0436738','X110009154');


			sql.append("SELECT concatena_aree(tit.isbd, tit.indice_isbd");
			sql.append(", ARRAY[").append(ValidazioneDati.formatValueList(Arrays.asList(etichette), '\'', ",")).append(']');
			sql.append(", tit.bid");
			sql.append(", ").append(isTitSupPrefisso ? 0 : 1);
			sql.append(", ' [', '] '");
			sql.append(", ").append(maxLen).append(')');
			sql.append(" from tb_titolo tit");
			sql.append(" where tit.bid=:bid");

			SQLQuery query = session.createSQLQuery(sql.toString());
			query.setString("bid", bid);

			return (String) query.uniqueResult();

		} catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}

	public String getBidInventario(String codPolo, String codBib, String codSerie, int codInv) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbc_inventario.class);

			Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
			serie.setCd_polo(creaIdBib(codPolo, codBib));
			serie.setCd_serie(codSerie);
			c.add(Restrictions.eq("cd_serie", serie));
			c.add(Restrictions.eq("cd_inven", codInv));
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.in("cd_sit", new Character[] {'1', '2'}));
			c.setProjection(Projections.property("b.id"));
			String bid = (String) c.uniqueResult();

			return bid;

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Tr_tit_bib geLocalizzazioneBib(String codPolo, String codBib, String bid)  throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tr_tit_bib.class);
			c.add(Restrictions.eq("Cd_polo", creaIdBib(codPolo, codBib)) );
			c.add(Restrictions.eq("B.id", bid));
			c.add(Restrictions.ne("fl_canc", 'S'));

			return (Tr_tit_bib) c.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void aggiornaLocalizzazione(Tr_tit_bib loc) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(loc);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Tr_bid_altroid getInstitutionId(String cd_istituzione, String bid) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tr_bid_altroid.class);
			c.add(Restrictions.eq("cd_istituzione", cd_istituzione));
			c.add(Restrictions.eq("titolo.id", bid));
			//c.add(Restrictions.ne("fl_canc", 'S'));
			//c.createCriteria("titolo").add(Restrictions.idEq(bid));

			return (Tr_bid_altroid) c.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void aggiornaInstitutionId(Tr_bid_altroid altroid) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(altroid);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void aggiornaTitolo(Tb_titolo t) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(t);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Tr_bid_altroid> getListaInstitutionId(final String bid) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tr_bid_altroid.class);
			c.add(Restrictions.eq("titolo.id", bid));
			return c.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

}
