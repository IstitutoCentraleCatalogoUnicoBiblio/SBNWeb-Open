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
import it.iccu.sbn.polo.orm.documentofisico.Trc_formati_sezioni;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Trc_formati_sezioniDao extends DaoManager {

	private static Log log = LogFactory.getLog(Trc_formati_sezioniDao.class);

	public Trc_formati_sezioniDao() {
		super();
	}

	public boolean inserimentoFormatiSezioni(Trc_formati_sezioni forSez)
	throws DaoManagerException	{
		try{
			boolean ret = false;
			Session session = this.getCurrentSession();
			session.saveOrUpdate(forSez);
			//session.flush();
			return ret = true;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}

	public Trc_formati_sezioni getFormatiSezioni00(String codPolo, String codBib, String codSez)
	throws DaoManagerException	{

		Trc_formati_sezioni rec = null;

		try {
			Session session = this.getCurrentSession();
			rec = new Trc_formati_sezioni();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
			sez.setCd_polo(bib);
			sez.setCd_sez(codSez.toUpperCase());
			rec.setCd_formato("00");
			rec.setCd_sezione(sez);
			rec = (Trc_formati_sezioni) loadNoLazy(session, Trc_formati_sezioni.class, rec);
			session.save(rec);
			//session.flush();
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
		return rec;
	}


	public Trc_formati_sezioni getFormatiSezioni4update(String codPolo, String codBib,
			String codSez, String codFor)
	throws DaoManagerException	{

		Trc_formati_sezioni rec = null;

		try {

			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_formati_sezioni.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
			sez.setCd_polo(bib);
			sez.setCd_sez(codSez);

			cr.add(Restrictions.eq("cd_sezione", sez));
			cr.add(Restrictions.eq("cd_formato", codFor));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			rec = (Trc_formati_sezioni)cr.setCacheMode(CacheMode.IGNORE).setLockMode(LockMode.UPGRADE).uniqueResult();

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}
	public Trc_formati_sezioni getFormatiSezioni(String codPolo, String codBib,
			String codSez, String codFor)
	throws DaoManagerException	{

		Trc_formati_sezioni rec = null;

		try {

			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_formati_sezioni.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
			sez.setCd_polo(bib);
			sez.setCd_sez(codSez);

			cr.add(Restrictions.eq("cd_sezione", sez));
			cr.add(Restrictions.eq("cd_formato", codFor));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			rec = (Trc_formati_sezioni)cr.uniqueResult();

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}
	public Trc_formati_sezioni getFormatiSezioni(String codSez, String codFor)
	throws DaoManagerException	{

		Trc_formati_sezioni rec = null;

		try {

			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_formati_sezioni.class);

			cr.add(Restrictions.eq("cd_sezione", codSez));
			cr.add(Restrictions.eq("cd_formato", codFor));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			rec = (Trc_formati_sezioni)cr.uniqueResult();

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public boolean cancellaFormatiSezioni(Trc_formati_sezioni forSez)
	throws DaoManagerException	{
		try{
			boolean ret = false;
			Session session = this.getCurrentSession();
			session.saveOrUpdate(forSez);

			return ret = true;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}
	public List getListaFormatiSezioni(String codPolo, String codBib)
	throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_formati_sezioni.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

//			cr.add(Restrictions.eq("cd_sez", sez));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			cr.addOrder(Order.asc("cd_formato"));
			cr.addOrder(Order.asc("cd_sezione"));
			List<Trc_formati_sezioni> results = cr.list();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}
	public List getListaFormatiSezioni(String codPolo, String codBib, String codSez)
	throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_formati_sezioni.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
			sez.setCd_polo(bib);
			sez.setCd_sez(codSez);

			cr.add(Restrictions.eq("cd_sezione", sez));
//			cr.add(Restrictions.eq("cd_sez", sez));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			cr.addOrder(Order.asc("cd_sezione"));
			cr.addOrder(Order.asc("cd_formato"));
			List<Trc_formati_sezioni> results = cr.list();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}
	public List getListaFormatiBib(String codPolo, String codBib, String codSez)
	throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_formati_sezioni.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
			sez.setCd_polo(bib);
			sez.setCd_sez(codSez);

			cr.add(Restrictions.eq("cd_sezione.cd_polo", sez.getCd_polo()));
			cr.add(Restrictions.ne("cd_sezione.cd_sez", sez.getCd_sez()));
//			cr.add(Restrictions.eq("cd_sez", sez));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			cr.addOrder(Order.asc("cd_formato"));
			cr.addOrder(Order.asc("cd_sezione"));
			List<Trc_formati_sezioni> results = cr.list();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}
		public List getListaFormatiSezioni00(String codPolo, String codBib, String codSez, String codFor)
		throws DaoManagerException	{
			try {
				Session session = this.getCurrentSession();
				Criteria cr = session.createCriteria(Trc_formati_sezioni.class);

				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(codPolo);
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(codBib);
				bib.setCd_polo(polo);
				Tbc_sezione_collocazione sezione = new Tbc_sezione_collocazione();
				sezione.setCd_polo(bib);
				sezione.setCd_sez(codSez);


				cr.add(Restrictions.eq("cd_sezione", sezione));
				cr.add(Restrictions.eq("cd_formato", codFor));
				cr.add(Restrictions.eq("fl_canc", 'N'));
				List<Trc_formati_sezioni> results = cr.list();
				return results;
			} catch (HibernateException he) {
				throw new DaoManagerException(he);

			}
	}
		public Integer getMaxPrgSerie(String codPolo, String codBib, String codSez, String codFor)
		throws DaoManagerException {

			Integer rec = null;
			try{
				Session session = this.getCurrentSession();

				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(codPolo);
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(codBib);
				bib.setCd_polo(polo);
				Tbc_sezione_collocazione sez = new Tbc_sezione_collocazione();
				sez.setCd_polo(bib);
				sez.setCd_sez(codSez);


				rec = (Integer) session.createCriteria(Trc_formati_sezioni.class)
				.setProjection(Projections.max("prog_serie"))
				.add(Restrictions.eq("cd_sez", sez))
				.add(Restrictions.eq("cd_formato", codFor)).uniqueResult();

			}catch (HibernateException e){
				throw new DaoManagerException(e);
			}
			return rec;
		}

		public boolean modificaFormatiSezioni(Trc_formati_sezioni forSez)
	throws DaoManagerException {
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			forSez = (Trc_formati_sezioni) loadNoLazy(session, Trc_formati_sezioni.class, forSez);
			session.saveOrUpdate(forSez);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
}
