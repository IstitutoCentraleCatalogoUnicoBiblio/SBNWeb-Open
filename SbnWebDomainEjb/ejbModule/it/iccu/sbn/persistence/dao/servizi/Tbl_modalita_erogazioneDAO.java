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
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_erogazione;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class Tbl_modalita_erogazioneDAO extends DaoManager {

	public List<Tbl_modalita_erogazione> getListaTariffeModalita(String codPolo,
			String codBib) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_modalita_erogazione.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			criteria.add(Restrictions.eq("cd_bib",  UtilitaDAO.creaIdBib(codPolo, codBib)) );
			criteria.addOrder(Order.asc("cod_erog"));

			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Object[]> getListaTariffeModalita(String codPolo,
			String codBib, String CodTipoServ) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Query query;
			if (ValidazioneDati.isFilled(CodTipoServ)) {
				query = session.getNamedQuery("modalitaErogazioneAssociateAlServizio");
				query.setString("tipoServ", CodTipoServ);
			}
			else
				query = session.getNamedQuery("modalitaErogazioneAssociateATuttiIServizi");

			query.setEntity("bib", UtilitaDAO.creaIdBib(codPolo, codBib));

			return query.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Object[]> getListaSupportiModalita(String codPolo,
			String codBib, String codSupporto) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {

			Query query = session.getNamedQuery("modalitaErogazioneAssociateAlSupporto");
			query.setString("codSupporto", codSupporto);
			query.setEntity("bib", UtilitaDAO.creaIdBib(codPolo, codBib));

			return query.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_modalita_erogazione getTariffeModalitaErogazione(String codPolo,
			String codBib, String codErog)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Tbl_modalita_erogazione.class);
			criteria.add(Restrictions.ne("fl_canc",          'S'))
					.add(Restrictions.eq("cod_erog",         codErog))
			        .add(Restrictions.eq("cd_bib",  UtilitaDAO.creaIdBib(codPolo, codBib)) );

			return (Tbl_modalita_erogazione)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_modalita_erogazione getTariffeModalitaErogazione_canc(String codPolo,
			String codBib, String codErog)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Tbl_modalita_erogazione.class);
			criteria.add(Restrictions.eq("fl_canc",          'S'))
					.add(Restrictions.eq("cod_erog",         codErog))
	        		.add(Restrictions.eq("cd_bib",  UtilitaDAO.creaIdBib(codPolo, codBib)) );

			return (Tbl_modalita_erogazione)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void aggiornamentoTariffeModalitaErogazione(Tbl_modalita_erogazione tariffe)  throws DaoManagerException  {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(tariffe);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void cancellazioneTariffeModalitaErogazione(Tbl_modalita_erogazione tariffe)  throws DaoManagerException  {
		tariffe.setFl_canc('S');
		try {
			aggiornamentoTariffeModalitaErogazione(tariffe);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}




}
