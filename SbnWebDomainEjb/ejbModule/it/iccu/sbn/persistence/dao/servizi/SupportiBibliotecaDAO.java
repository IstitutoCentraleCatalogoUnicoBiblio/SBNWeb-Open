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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Trl_supporti_modalita_erogazione;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class SupportiBibliotecaDAO extends DaoManager {

	private UtilitaDAO serviziDAO      = new UtilitaDAO();

	public Tbl_supporti_biblioteca getSupportoBibliotecaById(int id)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			return (Tbl_supporti_biblioteca)loadNoLazy(session, Tbl_supporti_biblioteca.class, new Integer(id));
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}
	public Tbl_supporti_biblioteca getSupportoBiblioteca(String codPolo, String codBib, String codSupporto, String fl_canc)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);//serviziDAO.getBibliotecaInPolo(codPolo, codBib);

			Criteria criteria = session.createCriteria(Tbl_supporti_biblioteca.class);
			criteria.add(Restrictions.eq("cd_bib", bib))
					//.add(Restrictions.eq("fl_canc", fl_canc))
					.add(Restrictions.eq("cod_supporto", codSupporto));
			if (ValidazioneDati.isFilled(fl_canc))
				criteria.add(Restrictions.eq("fl_canc", fl_canc));

			return (Tbl_supporti_biblioteca)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_supporti_biblioteca> getSupportiBiblioteca(String codPolo, String codBib, String fl_svolg)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);//serviziDAO.getBibliotecaInPolo(codPolo, codBib);

			Criteria criteria = session.createCriteria(Tbl_supporti_biblioteca.class);
			criteria.add(Restrictions.eq("cd_bib", bib))
					.add(Restrictions.ne("fl_canc", "S"))
					.addOrder(Order.asc("cod_supporto"));

			if (ValidazioneDati.isFilled(fl_svolg))
				criteria.add(Restrictions.eq("fl_svolg", fl_svolg));

			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void aggiornaSupportoBiblioteca(Tbl_supporti_biblioteca supporto)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try{
			session.saveOrUpdate(supporto);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaSupportoBiblioteca(Tbl_supporti_biblioteca supporto)
	throws DaoManagerException {
		supporto.setFl_canc('S');
		//almaviva5_20110415 #4342
		for (Object o : supporto.getTrl_supporti_modalita_erogazione()) {
			Trl_supporti_modalita_erogazione sme = (Trl_supporti_modalita_erogazione) o;
			sme.setUte_var(supporto.getUte_var());
			sme.setFl_canc('S');
		}
		//aggiornaSupportoBiblioteca(supporto);
	}

	public Tbl_supporti_biblioteca getSupportoById(int id_supporti_biblioteca)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbl_supporti_biblioteca supporto = (Tbl_supporti_biblioteca) loadNoLazy(session,
					Tbl_supporti_biblioteca.class, new Integer(id_supporti_biblioteca));
			return supporto;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_supporti_biblioteca> getListaSupporti(String codPolo, String codBib)
	throws DaoManagerException {
		Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(codPolo, codBib);
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_supporti_biblioteca.class);
			criteria.add(Restrictions.ne("fl_canc", 'S')).add(
					Restrictions.eq("cd_bib", bib))
			.addOrder(Order.asc("cod_supporto"));
			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_supporti_biblioteca getSupporto(String codPolo, String codBib,
			String codSupporto) throws DaoManagerException {
		Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(codPolo, codBib);
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_supporti_biblioteca.class);
			criteria.add(Restrictions.ne("fl_canc", 'S')).add(
					Restrictions.eq("cd_bib", bib)).add(
					Restrictions.eq("cod_supporto", codSupporto));

			return (Tbl_supporti_biblioteca) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
