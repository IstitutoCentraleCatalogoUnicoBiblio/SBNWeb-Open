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
import it.iccu.sbn.polo.orm.documentofisico.Tbc_default_inven_schede;

import java.sql.Connection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class Tbc_default_inven_schedeDao extends DaoManager {

	Connection connection = null;

	public Tbc_default_inven_schedeDao() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public Tbc_default_inven_schede getModelloDefault(String codPolo, String codBib)
//	throws DaoManagerException {
//
//		try{
//			Tbc_default_inven_schede rec = null;
//
//			Session session = this.getCurrentSession();
//			rec = new Tbc_default_inven_schede();
//
//			Tbf_polo polo = new Tbf_polo();
//			polo.setCd_polo(codPolo);
//			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
//			bib.setCd_biblioteca(codBib);
//			bib.setCd_polo(polo);
//			rec = (Tbc_default_inven_schede) loadNoProxy(session, Tbc_default_inven_schede.class, rec);
//			return rec;
//		}catch (org.hibernate.ObjectNotFoundException e){
//			return null;
//		}catch (HibernateException e){
//			throw new DaoManagerException(e);
//		}
//	}

	public Tbc_default_inven_schede getModelloDefault(String codPolo, String codBib)
	throws DaoManagerException {

		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_default_inven_schede.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_biblioteca", bib));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			Tbc_default_inven_schede rec = (Tbc_default_inven_schede) cr.uniqueResult();
			return rec;
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean saveUpdateModelloDefault(Tbc_default_inven_schede modello)
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
