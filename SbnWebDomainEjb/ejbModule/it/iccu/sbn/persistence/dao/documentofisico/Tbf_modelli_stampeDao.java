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
import it.iccu.sbn.polo.orm.amministrazione.Tbf_modelli_stampe;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.sql.Connection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class Tbf_modelli_stampeDao extends DaoManager {

	Connection connection = null;

	public Tbf_modelli_stampeDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Tbf_modelli_stampe> getListaModelli(String codPolo, String codBib)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbf_modelli_stampe.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_bib", bib));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			cr.addOrder(Order.asc("modello"));
			List<Tbf_modelli_stampe> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public Tbf_modelli_stampe getModello(String codPolo, String codBib)
	throws DaoManagerException {

		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbf_modelli_stampe.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_biblioteca", bib));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			Tbf_modelli_stampe rec = (Tbf_modelli_stampe) cr.uniqueResult();
			return rec;
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public Tbf_modelli_stampe getModello(String codPolo, String codBib, String codModello)
	throws DaoManagerException {

		Tbf_modelli_stampe rec = null;
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbf_modelli_stampe.class);


		Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbf_modelli_stampe modello = new Tbf_modelli_stampe();
			modello.setCd_bib(bib);
			modello.setModello(codModello);

			cr.add(Restrictions.eq("cd_bib", bib));
			cr.add(Restrictions.eq("modello", codModello));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			rec = (Tbf_modelli_stampe) cr.uniqueResult();
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public Tbf_modelli_stampe getModelloN(String codPolo, String codBib, String codModello)
	throws DaoManagerException {

		Tbf_modelli_stampe rec = null;
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbf_modelli_stampe.class);


		Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbf_modelli_stampe modello = new Tbf_modelli_stampe();
			modello.setCd_bib(bib);
			modello.setModello(codModello.trim());

			cr.add(Restrictions.eq("cd_bib", bib));
			cr.add(Restrictions.eq("modello", codModello.trim()));
			rec = (Tbf_modelli_stampe) cr.uniqueResult();
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public boolean saveUpdateModello(Tbf_modelli_stampe modello)
	throws DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(modello);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ret = true;
	}

}
