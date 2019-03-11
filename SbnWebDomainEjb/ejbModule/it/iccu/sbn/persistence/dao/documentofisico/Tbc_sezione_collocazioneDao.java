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
import it.iccu.sbn.polo.orm.documentofisico.Tbc_sezione_collocazione;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class Tbc_sezione_collocazioneDao extends DaoManager {

	private static Log log = LogFactory.getLog(Tbc_sezione_collocazioneDao.class);

	Connection connection = null;


	public Tbc_sezione_collocazioneDao() {
		super();
	}

	public Tbc_sezione_collocazione getSezione(String codPolo, String codBib, String codSez)
	throws DaoManagerException{

		Tbc_sezione_collocazione rec = null;

		try {
			Session session = this.getCurrentSession();
			rec = new Tbc_sezione_collocazione();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			rec.setCd_polo(bib);
			rec.setCd_sez(codSez.trim().toUpperCase());
			rec.setFl_canc('N');
			rec = (Tbc_sezione_collocazione) loadNoLazy(session, Tbc_sezione_collocazione.class, rec);
//			per creare un filtro
//			Set listaColl = rec.getTbc_collocazione();
//			Query query = session.createFilter(listaColl, "where key_loc=:keyloc");
//			query.setInteger("keyloc", 111);
//			query.uniqueResult();

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}


	public List<Tbc_sezione_collocazione> getListaSezioni(String codPolo, String codBib)
	throws DaoManagerException	{

		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_sezione_collocazione.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_polo", bib));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.addOrder(Order.asc("cd_sez"));
			List<Tbc_sezione_collocazione> results = cr.list();
			return results;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);

		}
	}

	public List<Tbc_sezione_collocazione> selectAll()
	throws DaoManagerException
	{
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbc_sezione_collocazione.class);
		List<Tbc_sezione_collocazione> ret = criteria.list();
		return  ret;
	}

//	public boolean inserimentoModificaSezione(Tbc_sezione_collocazione sezione)
//	throws  DaoManagerException{
//		boolean ret = false;
//		try{
//			Session session = this.getCurrentSession();
//			session.saveOrUpdate(sezione);
//			return ret = true;
//		}catch (HibernateException e){
//			throw new DaoManagerException(e);
//		}
//	}
//
	public boolean saveUpdateSezione(Tbc_sezione_collocazione sezione)
	throws DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(sezione);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ret = true;
	}
//	public boolean cancellaSezione(Tbc_sezione_collocazione sezione)
//	throws DaoManagerException	{
//		boolean ret = false;
//		try{
//			Session session = this.getCurrentSession();
//			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
//			session.saveOrUpdate(sezione);
//		}catch (HibernateException e){
//			throw new DaoManagerException(e);
//		}
//		return ret = true;
//	}
}
