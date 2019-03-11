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

import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_pagamento;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ModalitaPagamentoDAO extends DaoManager {

	private UtilitaDAO serviziDAO = new UtilitaDAO();

	public List<Tbl_modalita_pagamento> getModalitaPagamento(String codPolo,
			String codBib) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(
					codPolo, codBib);
			if (bib == null)
				throw new DaoManagerException(
						"Biblioteca non trovata in base dati. Cod. Polo:"
								+ codPolo + "  Cod. Bib:" + codBib);

			Criteria criteria = session
					.createCriteria(Tbl_modalita_pagamento.class);
			criteria.add(Restrictions.eq("cd_bib", bib)).add(
					Restrictions.ne("fl_canc", "S")).addOrder(
					Order.asc("cod_mod_pag"));

			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_modalita_pagamento getModalitaPagamentoById(int id)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			return (Tbl_modalita_pagamento) loadNoLazy(session,
					Tbl_modalita_pagamento.class, new Integer(id));
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_modalita_pagamento getModalitaPagamento(String codPolo,
			String codBib, short codModPag, String fl_canc)
			throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(
					codPolo, codBib);
			if (bib == null)
				throw new DaoManagerException(
						"Biblioteca non trovata in base dati. Cod. Polo:"
								+ codPolo + "  Cod. Bib:" + codBib);

			Criteria criteria = session
					.createCriteria(Tbl_modalita_pagamento.class);
			criteria.add(Restrictions.eq("cd_bib", bib)).add(
					Restrictions.eq("fl_canc", fl_canc)).add(
					Restrictions.eq("cod_mod_pag", new Short(codModPag)));

			return (Tbl_modalita_pagamento) criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_modalita_pagamento aggiornaModalitaPagamento(
			Tbl_modalita_pagamento modalita) throws DaoManagerException, DAOConcurrentException {

		try {
			Session session = this.getCurrentSession();
			if (modalita.getId_modalita_pagamento() == 0)
				session.save(modalita);
			else
				modalita = (Tbl_modalita_pagamento) session.merge(modalita);

			session.flush();
			return modalita;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaModalitaPagamento(Tbl_modalita_pagamento modalita)
			throws DaoManagerException {
		modalita.setFl_canc('S');
	}

}
