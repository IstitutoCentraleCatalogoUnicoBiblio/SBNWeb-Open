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
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Trl_attivita_bibliotecario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UtentiProfessionaliDAO extends DaoManager {

	private IterServizioDAO     iterServizioDAO = new IterServizioDAO();
	private UtilitaDAO serviziDAO      = new UtilitaDAO();

	/**
	 *
	 * @param  idTipoServizio
	 * @param  codAttivita
	 * @return Lista di Tbf_anagrafe_utenti_professionali associati all'attività in input
	 * @throws DaoManagerException
	 *
	 *
	 */
	public List<Tbf_anagrafe_utenti_professionali> getUtentiProfessionali(int idTipoServizio, String codAttivita)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbl_iter_servizio iterServizio = iterServizioDAO.getIterServizio(idTipoServizio, codAttivita);

			//Ricerca dei bibliotecari associati all'attività
			List<Tbf_anagrafe_utenti_professionali> output = new ArrayList<Tbf_anagrafe_utenti_professionali>();
			if (iterServizio != null) {
				Set attivitaBibliotecario = iterServizio.getTrl_attivita_bibliotecario();
				Query query = session.createFilter(attivitaBibliotecario, "where fl_canc!='S'");

				List<Trl_attivita_bibliotecario> list = query.list();
				Iterator<Trl_attivita_bibliotecario> i = list.iterator();
				while (i.hasNext())
					output.add((i.next().getId_bibliotecario().getId_utente_professionale()));

			}

			return output;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	/**
	 *
	 * @param codPolo
	 * @param codBib
	 * @return lista di Tbf_anagrafe_utenti_professionali appartenenti all abiblioteca in input
	 * @throws DaoManagerException
	 */
	public List getUtentiProfessionali(String codPolo, String codBib)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Criteria profili = session.createCriteria(Tbf_profilo_abilitazione.class);
			profili.add(Restrictions.eq("cd_polo", bib))
				   .add(Restrictions.ne("fl_canc", 'S'));

			Criteria bibliotecario = session.createCriteria(Tbf_bibliotecario.class);
			bibliotecario.add(Restrictions.ne("fl_canc", 'S'))
						 .add(Restrictions.in("cd_prof", profili.list()));
			List bibliotecari = bibliotecario.list();

			Iterator iterator = bibliotecari.iterator();
			List anagrafeUtentiProfessionali = new ArrayList();
			while (iterator.hasNext()) {
				anagrafeUtentiProfessionali.add(((Tbf_bibliotecario)iterator.next()).getId_utente_professionale());
			}

			return anagrafeUtentiProfessionali;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
