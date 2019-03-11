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
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio_web_dati_richiesti;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Restrictions;

public class Tbl_servizio_web_dati_richiestiDAO extends DaoManager {

	private UtilitaDAO serviziDAO = new UtilitaDAO();

	public List<Tbl_servizio_web_dati_richiesti> getServizioWebDatiRichiesti(
			String codPolo, String codBib, String codTipoServizio) throws DaoManagerException {

		TipoServizioDAO servizioDAO = new TipoServizioDAO();
		Tbl_tipo_servizio tipoServizio = servizioDAO.getTipoServizio(codPolo, codBib, codTipoServizio);

		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Tbl_servizio_web_dati_richiesti.class);
		criteria.add(Restrictions.eq("id_tipo_servizio", tipoServizio));
		//criteria.add(Restrictions.ne("fl_canc", 'S'));

		return criteria.list();
	}

	public void aggiornamentoModuloRichiesta(Tbl_servizio_web_dati_richiesti servizioWebDatiRichiesti) throws DaoManagerException, DAOConcurrentException  {
		try {
			Session session = getCurrentSession();
			session.saveOrUpdate(servizioWebDatiRichiesti);
			session.flush();
		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public void cancellaServizioWebDatiRichiesti(String codPolo, String codBib,
			String codTipoServ, short campo_richiesta, String firmaUtente) throws DaoManagerException {
		Session session = getCurrentSession();
		List<Tbl_servizio_web_dati_richiesti> dati_richiesti = getServizioWebDatiRichiesti(codPolo, codBib, codTipoServ);
		for (Tbl_servizio_web_dati_richiesti dati : dati_richiesti) {
			// se campo_richiesta == 0 cancella tutte le occorrenze
			if (ValidazioneDati.in(campo_richiesta, (short)0, dati.getCampo_richiesta())) {
				dati.setFl_canc('S');
				dati.setUte_var(firmaUtente);
				session.update(dati);
			}
		}

		session.flush();
	}

}
