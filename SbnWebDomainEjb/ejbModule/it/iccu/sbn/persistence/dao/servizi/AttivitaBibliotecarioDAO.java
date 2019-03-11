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
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Trl_attivita_bibliotecario;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class AttivitaBibliotecarioDAO extends DaoManager {

	public Trl_attivita_bibliotecario getAttivitaBibliotecario(Tbl_iter_servizio iterServizio, int id_utente_professionale, boolean esisteCancellato)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Trl_attivita_bibliotecario.class);
			criteria.add(Restrictions.eq("id_iter_servizio", iterServizio))
					.add(Restrictions.eq("id_bibliotecario_id_utente_professionale", id_utente_professionale));
			if (esisteCancellato) {
				criteria.add(Restrictions.eq("fl_canc", 'S'));
			}
			return (Trl_attivita_bibliotecario)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public void aggiornamentoAttivitaBibliotecario(Trl_attivita_bibliotecario attivita_bibliotecario) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			session.saveOrUpdate(attivita_bibliotecario);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public void cancellazioneAttivitaBibliotecario(Trl_attivita_bibliotecario attivita_bibliotecario) throws DaoManagerException {
		attivita_bibliotecario.setFl_canc('S');

		try {
			aggiornamentoAttivitaBibliotecario(attivita_bibliotecario);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public void inserimentoAttivitaBibliotecario(Trl_attivita_bibliotecario attivita_bibliotecario) throws DaoManagerException {
		attivita_bibliotecario.setFl_canc('N');

		try {
			aggiornamentoAttivitaBibliotecario(attivita_bibliotecario);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void cancellaAttivitaBibliotecario(Tbl_iter_servizio iterServizio, String utenteVar)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			if (iterServizio != null) {
				Set<Trl_attivita_bibliotecario> attivitaBibliotecario = iterServizio.getTrl_attivita_bibliotecario();
				Query query = session.createFilter(attivitaBibliotecario, "where fl_canc<>'S'");

				List<Trl_attivita_bibliotecario> attivitaBibliotecari = query.list();
				for (Trl_attivita_bibliotecario attivita : attivitaBibliotecari) {
					attivita.setFl_canc('S');
					attivita.setUte_var(utenteVar);
				}
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}
}
