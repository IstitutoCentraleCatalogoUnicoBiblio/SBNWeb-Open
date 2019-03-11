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
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_nota_inv;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class Tbc_nota_invDao extends DaoManager {
	private static Log log = LogFactory.getLog(Tbc_nota_invDao.class);

	Connection connection = null;

	public Tbc_nota_invDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Tbc_nota_inv> selectAll()
	throws DaoManagerException	{
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbc_nota_inv.class);
		List<Tbc_nota_inv> ret = criteria.list();
		return  ret;
	}

	public boolean inserimentoNota(Tbc_nota_inv nota)
	throws DaoManagerException	{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(nota);
			session.flush();
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public Tbc_nota_inv getNota(Tbc_inventario inventario, String codNota)
	throws DaoManagerException {

		Tbc_nota_inv rec = null;
		try{
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_inventario.class);

			cr.add(Restrictions.ne("fl_canc", 'S'));
			cr.add(Restrictions.ne("cd_polo", inventario));
			cr.add(Restrictions.ne("cd_nota", codNota));
			rec = (Tbc_nota_inv) cr.uniqueResult();
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public Tbc_nota_inv getNotaNoFlCanc(Tbc_inventario inventario, String codNota)
	throws DaoManagerException {

		Tbc_nota_inv rec = null;
		try{

			Session session = this.getCurrentSession();
			rec = new Tbc_nota_inv();
			rec.setCd_polo(inventario);
			rec.setCd_nota(codNota);
			rec = (Tbc_nota_inv) loadNoLazy(session, Tbc_nota_inv.class, rec);
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public List<Tbc_nota_inv> getNote(Tbc_inventario inventario)
	throws DaoManagerException {
		try{
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbc_nota_inv.class);
			criteria.add(Restrictions.eq("cd_polo", inventario));
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			List<Tbc_nota_inv> lista = criteria.list();
			return lista;
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean modificaNota(Tbc_nota_inv recNota)
	throws DaoManagerException {
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			recNota.setTs_var(ts);
			session.saveOrUpdate(recNota);
			session.flush();
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ret = true;
	}

}

