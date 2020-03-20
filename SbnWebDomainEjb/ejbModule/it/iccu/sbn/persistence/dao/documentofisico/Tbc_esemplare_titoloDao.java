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

import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_esemplare_titolo;

import java.sql.Connection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Tbc_esemplare_titoloDao extends DaoManager {

	Connection connection = null;

	public Tbc_esemplare_titoloDao() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Tbc_esemplare_titolo getEsemplare(String codPolo, String codBib, String bid, int codDoc)
	throws DaoManagerException {

		Tbc_esemplare_titolo rec = null;
		Tbc_esemplare_titolo rec1 = null;
		try{
		Session session = this.getCurrentSession();
		Transaction tx = session.beginTransaction();
		tx.begin();
		Tbf_polo polo = new Tbf_polo();
		polo.setCd_polo(codPolo);
		Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
		bib.setCd_biblioteca(codBib);
		bib.setCd_polo(polo);
		Tb_titolo titolo = new Tb_titolo();
		titolo.setBid(bid);
		rec = new Tbc_esemplare_titolo();
		rec.setCd_polo(bib);
		rec.setB(titolo);
		rec.setCd_doc(codDoc);

		//session.createCriteria(Tbc_esemplare_titolo.class).list();
//		.add(Restrictions.eq("cd_polo", bib))
//		.add(Restrictions.eq("b", titolo))
//		.add(Restrictions.eq("c", titolo))
//		.uniqueResult();
		session.flush();
		rec1 = (Tbc_esemplare_titolo) session.get(Tbc_esemplare_titolo.class, rec);
		tx.commit();

//		session.refresh(rec.getB());
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec1;
	}

	public Integer getMaxEsemplare(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		Integer rec = null;
		try{
			Session session = this.getCurrentSession();

			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);
			rec = (Integer) session.createCriteria(Tbc_esemplare_titolo.class)
			.setProjection(Projections.max("cd_doc"))
			.add(Restrictions.eq("cd_polo", creaIdBib(codPolo, codBib)))
			.add(Restrictions.eq("b", titolo)).uniqueResult();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public List getListaEsemplari(String bid) throws DaoManagerException {

		List lista = null;
		try {
			Session session = this.getCurrentSession();
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);
			Criteria c = session.createCriteria(Tbc_esemplare_titolo.class);
			c.add(Restrictions.eq("b", titolo)).add(Restrictions.ne("fl_canc", 'S'));
			//almaviva5_20140311
			c.addOrder(Order.asc("cd_polo")).addOrder(Order.asc("cd_doc"));
			lista = c.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return lista;
	}

	public List<Tbc_esemplare_titolo> getListaEsemplari(String codPolo, String codBib, String bid)
			throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_polo(polo);
			bib.setCd_biblioteca(codBib);
			Tb_titolo titolo = new Tb_titolo();
			titolo.setBid(bid);

			return session.createCriteria(Tbc_esemplare_titolo.class)
				.add(Restrictions.eq("cd_polo", bib))
				.add(Restrictions.eq("b", titolo))
				.add(Restrictions.ne("fl_canc", 'S')).list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public boolean inserimentoEsemplare(Tbc_esemplare_titolo recEsempl)
	throws DaoManagerException	{
		boolean ret = false;
		try{

			Session session = this.getCurrentSession();
			if (recEsempl.getCd_doc() > 0){
				session.saveOrUpdate(recEsempl);
				ret = true;
			}
			return ret;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean modificaEsemplare(Tbc_esemplare_titolo recEsempl)
			throws DaoManagerException {
		boolean ret = false;
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(recEsempl);
			session.flush();
			ret = true;

			return ret;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public int countEsemplariPerPosseduto(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		Number countEsem = null;

		try{
			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			countEsem = (Number)session.createCriteria(Tbc_esemplare_titolo.class).setProjection(Projections.rowCount())
			.add(Restrictions.eq("cd_polo", bib))
			.add(Restrictions.eq("b.id", bid))
			.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countEsem.intValue();
	}

	public int countEsemplariPerPossedutoAltreBib(String codPolo, String codBib, String bid)
	throws DaoManagerException {

		Number countEsem = null;

		try{
			Session session = this.getCurrentSession();

			countEsem = (Number)session.createCriteria(Tbc_esemplare_titolo.class).setProjection(Projections.rowCount())
				.add(Restrictions.ne("cd_polo.cd_biblioteca", codBib))
				.add(Restrictions.eq("b.id", bid))
				.add(Restrictions.ne("fl_canc", 'S')).uniqueResult();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return countEsem.intValue();
	}

	public List getAltreBibEsempl(String bid)
	throws DaoManagerException	{
		List lista = null;
		try{
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("contaBibliotecheBidEsempl");
			query.setString("bid", bid);
			lista = query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}
	public List getAltreBibEsempl(String bid, String codBib)
	throws DaoManagerException	{
		List lista = null;
		try{
			Session session = this.getCurrentSession();
			Query query = session.getNamedQuery("contaBibliotecheBidBibEsempl");
			query.setString("bid", bid);
			query.setString("codBib", codBib);
			lista = query.list();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return lista;
	}
}
