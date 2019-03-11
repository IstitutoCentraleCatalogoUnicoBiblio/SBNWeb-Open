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
import it.iccu.sbn.polo.orm.servizi.Tbl_modalita_erogazione;
import it.iccu.sbn.polo.orm.servizi.Tbl_supporti_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;
import it.iccu.sbn.polo.orm.servizi.Trl_supporti_modalita_erogazione;
import it.iccu.sbn.polo.orm.servizi.Trl_tariffe_modalita_erogazione;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ModalitaErogazioneDAO extends DaoManager {

	private TipoServizioDAO tipoServizioDAO    = new TipoServizioDAO();

	private SupportiBibliotecaDAO supportiBibliotecaDAO    = new SupportiBibliotecaDAO();

	public List<Trl_tariffe_modalita_erogazione> getListaTariffeModalitaPerTipoServizio(String codPolo,
			String codBib, String codTipoServ) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Trl_tariffe_modalita_erogazione.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
			.addOrder(Order.asc("cod_erog"));

			if (codTipoServ==null || codTipoServ.equalsIgnoreCase("")) {
				//iter relativi a tutti i servizi associati alla bib.
				List<Tbl_tipo_servizio> listaTipiServizio = tipoServizioDAO.getListaTipiServizio(codPolo, codBib);
				if (listaTipiServizio!=null && listaTipiServizio.size()>0) {
					criteria.add(Restrictions.in("id_tipo_servizio", listaTipiServizio));
				} else
					return new ArrayList<Trl_tariffe_modalita_erogazione>();
			} else {
				Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServ);
				criteria.add(Restrictions.eq("id_tipo_servizio", tipoServizio));
			}


			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Trl_supporti_modalita_erogazione> getListaSupportiModalitaPerSupporto(String codPolo,
			String codBib, String codSupporto) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Criteria criteria = session.createCriteria(Trl_supporti_modalita_erogazione.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
			.addOrder(Order.asc("cod_erog"));

			if (codSupporto==null || codSupporto.equalsIgnoreCase("")) {
				//iter relativi a tutti i supporti associati alla bib.
				List<Tbl_supporti_biblioteca> listaSupportiBiblioteca = supportiBibliotecaDAO.getListaSupporti(codPolo, codBib);
				if (listaSupportiBiblioteca!=null && listaSupportiBiblioteca.size()>0) {
					criteria.add(Restrictions.in("id_supporti_biblioteca", listaSupportiBiblioteca));
				} else
					return new ArrayList<Trl_supporti_modalita_erogazione>();
			} else {
				Tbl_supporti_biblioteca supporto = supportiBibliotecaDAO.getSupporto(codPolo, codBib, codSupporto);
				criteria.add(Restrictions.eq("id_supporti_biblioteca", supporto));
			}


			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Trl_tariffe_modalita_erogazione getTariffeModalitaErogazione(String codErog, Tbl_tipo_servizio tipoServizio)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Trl_tariffe_modalita_erogazione.class);
			criteria.add(Restrictions.ne("fl_canc",          'S'))
					.add(Restrictions.eq("id_tipo_servizio", tipoServizio))
					.add(Restrictions.eq("cod_erog",         codErog));

			return (Trl_tariffe_modalita_erogazione)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Trl_tariffe_modalita_erogazione getTariffeModalitaErogazione_canc(String codErog, Tbl_tipo_servizio tipoServizio)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Trl_tariffe_modalita_erogazione.class);
			criteria.add(Restrictions.eq("fl_canc",          'S'))
				    .add(Restrictions.eq("id_tipo_servizio", tipoServizio))
					.add(Restrictions.eq("cod_erog",         codErog));

			return (Trl_tariffe_modalita_erogazione)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Trl_supporti_modalita_erogazione getSupportiModalitaErogazione(String codErog, Tbl_supporti_biblioteca supporto)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Trl_supporti_modalita_erogazione.class);
			criteria.add(Restrictions.ne("fl_canc",          'S'))
					.add(Restrictions.eq("id_supporti_biblioteca", supporto))
					.add(Restrictions.eq("cod_erog",         codErog));

			return (Trl_supporti_modalita_erogazione)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Trl_supporti_modalita_erogazione getSupportiModalitaErogazione_canc(String codErog, Tbl_supporti_biblioteca supporto)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Trl_supporti_modalita_erogazione.class);
			criteria.add(Restrictions.eq("fl_canc",          'S'))
				    .add(Restrictions.eq("id_supporti_biblioteca", supporto))
					.add(Restrictions.eq("cod_erog",         codErog));

			return (Trl_supporti_modalita_erogazione)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public void aggiornamentoTariffeModalitaErogazione(Trl_tariffe_modalita_erogazione tariffe)  throws DaoManagerException  {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(tariffe);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void aggiornamentoSupportiModalitaErogazione(Trl_supporti_modalita_erogazione tariffe)  throws DaoManagerException  {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(tariffe);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void aggiornamentoModalitaErogazione(Tbl_modalita_erogazione tariffe)  throws DaoManagerException  {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(tariffe);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void cancellazioneTariffeModalitaErogazione(Trl_tariffe_modalita_erogazione tariffe)  throws DaoManagerException  {
		tariffe.setFl_canc('S');
		try {
			aggiornamentoTariffeModalitaErogazione(tariffe);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void cancellazioneSupportiModalitaErogazione(Trl_supporti_modalita_erogazione tariffe)  throws DaoManagerException  {
		tariffe.setFl_canc('S');
		try {
			aggiornamentoSupportiModalitaErogazione(tariffe);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void cancellazioneModalitaErogazione(Tbl_modalita_erogazione tariffe)  throws DaoManagerException  {
		tariffe.setFl_canc('S');
		try {
			aggiornamentoModalitaErogazione(tariffe);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Object[]> getListaModErogLegateASupporti(
			String codPolo, String codBib) throws DaoManagerException {
	try {
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("modalitaErogazioneAssociateASupporti");
		query.setEntity("bib", creaIdBib(codPolo, codBib));
		return query.list();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}
}
