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

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class TipoServizioDAO extends DaoManager {

	private UtilitaDAO serviziDAO = new UtilitaDAO();


	public Tbl_tipo_servizio getTipoServizio(String codPolo, String codBib,
			String codTipoServ) throws DaoManagerException {

		return getTipoServizio(codPolo, codBib, codTipoServ, true);
	}

	public Tbl_tipo_servizio getTipoServizio(String codPolo, String codBib,
			String codTipoServ, boolean attivo) throws DaoManagerException {
		Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(codPolo, codBib);
		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Tbl_tipo_servizio.class);
			c.add(Restrictions.eq("cd_bib", bib))
				.add(Restrictions.eq("cod_tipo_serv", codTipoServ));

			if (attivo)
				c.add(Restrictions.ne("fl_canc", 'S'));

			return (Tbl_tipo_servizio) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_tipo_servizio getTipoServizioById(int id_tipo_servizio)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbl_tipo_servizio tipoServizio = (Tbl_tipo_servizio) loadNoLazy(session,
					Tbl_tipo_servizio.class, new Integer(id_tipo_servizio));
			return tipoServizio;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_tipo_servizio> getListaTipiServizio(String codPolo, String codBib)
			throws DaoManagerException {
		Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(codPolo, codBib);
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Tbl_tipo_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S')).add(
					Restrictions.eq("cd_bib", bib))
			.addOrder(Order.asc("cod_tipo_serv"));
			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void aggiornamentoTipoServizio(Tbl_tipo_servizio tipoServizio) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(tipoServizio);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void cancellazioneTipoServizio(int idTipoServizio, String firmaUtente) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbl_tipo_servizio tipoServizio = (Tbl_tipo_servizio) session.get(Tbl_tipo_servizio.class, idTipoServizio);
			tipoServizio.setUte_var(firmaUtente);
			tipoServizio.setFl_canc('S');

			session.saveOrUpdate(tipoServizio);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<String> getTipiServizioSollecitabili(String codPolo,
			String codBib) throws DaoManagerException {

		try {
			List<Tbl_tipo_servizio> listaTipiServizio = getListaTipiServizio(codPolo, codBib);
			List<String> serviziSollecitabili = new ArrayList<String>();

			// preparo una lista con i tipi servizio sollecitabili per la biblioteca
			for (Tbl_tipo_servizio tipoServizio : listaTipiServizio) {

				String codTipoServ = tipoServizio.getCod_tipo_serv().trim();
				CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(codTipoServ,
								CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
				if (ts == null)
					continue;

				if (ts.isPrestito() )
					serviziSollecitabili.add(codTipoServ);
			}

			return serviziSollecitabili;

		} catch (DaoManagerException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoManagerException(e);
		}
	}

}
