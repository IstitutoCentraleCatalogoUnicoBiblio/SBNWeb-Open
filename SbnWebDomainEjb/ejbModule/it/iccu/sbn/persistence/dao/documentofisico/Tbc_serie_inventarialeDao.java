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
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class Tbc_serie_inventarialeDao extends DaoManager {

	Connection connection = null;

	public Tbc_serie_inventarialeDao() {
		super();
	}

	public List<Tbc_serie_inventariale> getListaSerie(String codPolo, String codBib)
	throws DaoManagerException	{
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_serie_inventariale.class);

			cr.add(Restrictions.eq("cd_polo", creaIdBib(codPolo, codBib)));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.addOrder(Order.asc("cd_serie"));
			List<Tbc_serie_inventariale> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
//			return null;
		}
	}

	public Tbc_serie_inventariale getSerieForUpdate(String codPolo, String codBib, String codSerie)
	throws DaoManagerException {

		try{
			Tbc_serie_inventariale rec = null;

			Session session = this.getCurrentSession();
			rec = new Tbc_serie_inventariale();

			rec.setCd_polo(creaIdBib(codPolo, codBib));
			rec.setCd_serie(codSerie);
			rec = (Tbc_serie_inventariale) session.get(Tbc_serie_inventariale.class, rec, LockMode.UPGRADE);
			return rec;
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public Tbc_serie_inventariale getSerie(String codPolo, String codBib, String codSerie)
	throws DaoManagerException {

		try{
			Tbc_serie_inventariale rec = null;

			Session session = this.getCurrentSession();
			rec = new Tbc_serie_inventariale();

			rec.setCd_polo(creaIdBib(codPolo, codBib));
			rec.setCd_serie(codSerie);
			rec = (Tbc_serie_inventariale) loadNoLazy(session, Tbc_serie_inventariale.class, rec);
			return rec;
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}



	public boolean inserimentoSerie(Tbc_serie_inventariale serie)
	throws DaoManagerException	{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(serie);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean modificaSerie(Tbc_serie_inventariale serie)
	throws DaoManagerException {
		try{
			Session session = this.getCurrentSession();
			Timestamp ts = DaoManager.now();
			serie.setTs_var(ts);
			session.saveOrUpdate(serie);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
//			return null;
		}
		return true;
	}

}
