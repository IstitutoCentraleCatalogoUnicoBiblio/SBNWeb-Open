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
package it.iccu.sbn.persistence.dao.amministrazione;

import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class Tbf_utenti_professionali_webDao extends DaoManager {

	public static final String USER_ROOT = "root";

	public Tbf_utenti_professionali_webDao() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<Tbf_utenti_professionali_web> selectAll()
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria criteria = session
				.createCriteria(Tbf_utenti_professionali_web.class);
		List<Tbf_utenti_professionali_web> ret =
			criteria.list();
		return ret;
	}

	public Tbf_utenti_professionali_web select(int id_bibliotecario)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		Tbf_utenti_professionali_web uteWeb =
			(Tbf_utenti_professionali_web) loadNoLazy(session, Tbf_utenti_professionali_web.class, id_bibliotecario);

		return uteWeb;
	}

	public Tbf_utenti_professionali_web select(String userId, String password, boolean checkPassword)
			throws DaoManagerException, UtenteNotFoundException {

		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_utenti_professionali_web.class);
		criteria.add(Restrictions.eq("userid", userId).ignoreCase() );
		if (checkPassword)
			criteria.add(Restrictions.eq("password", password));

		return (Tbf_utenti_professionali_web) criteria.uniqueResult();
	}

	public Tbf_utenti_professionali_web select(String userId)
			throws DaoManagerException, UtenteNotFoundException {

		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_utenti_professionali_web.class);
		criteria.add(Restrictions.ne("fl_canc", 'S'))
				.add(Restrictions.eq("userid", userId).ignoreCase() );

		return (Tbf_utenti_professionali_web) criteria.uniqueResult();
	}

	public boolean update(Tbf_utenti_professionali_web bibliotecario)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			bibliotecario.setTs_var(DaoManager.now());
			bibliotecario.setUte_var(getFirmaUtente(bibliotecario
							.getId_utente_professionale()
							.getId_utente_professionale()));
			session.merge(bibliotecario);
		} catch (org.hibernate.ObjectNotFoundException e) {
			return false;
		}
		return true;
	}

	public Tbf_anagrafe_utenti_professionali getUtenteProfessionale(int idUtente) throws DaoManagerException {
		Session session = this.getCurrentSession();
		return (Tbf_anagrafe_utenti_professionali) session.get(
				Tbf_anagrafe_utenti_professionali.class, new Integer(idUtente));
	}

	public void resetRootPassword(String pwd) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_utenti_professionali_web.class);
			criteria.add(Restrictions.eq("userid", USER_ROOT));
			Tbf_utenti_professionali_web root = (Tbf_utenti_professionali_web) criteria.uniqueResult();

			root.setPassword(pwd);
			root.setChange_password('S');
			session.update(root);
			session.flush();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}
}
