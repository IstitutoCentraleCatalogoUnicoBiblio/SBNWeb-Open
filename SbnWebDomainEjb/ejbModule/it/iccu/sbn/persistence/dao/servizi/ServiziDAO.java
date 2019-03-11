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
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_penalita_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;
import it.iccu.sbn.servizi.codici.CodiciProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ServiziDAO extends ServiziBaseDAO {

	private UtilitaDAO  serviziDAO    = new UtilitaDAO();
	private TipoServizioDAO tipoServizioDAO    = new TipoServizioDAO();


	public List getListaServiziBiblioteca(List listaTipiServizi, String codPolo, String codBib) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Tbl_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
			.addOrder(Order.asc("tipoServ.cod_tipo_serv"))
			.addOrder(Order.asc("cod_servizio"));
			// inserire il not in ????
			Criteria criteria2 = criteria.createCriteria("id_tipo_servizio", "tipoServ");
			criteria2.add(Restrictions.ne("fl_canc", 'S'));
			criteria2.add(Restrictions.eq("cd_bib", bib));
			if (listaTipiServizi.size() > 0)
				criteria2.add(Restrictions.not(Restrictions.in("cod_tipo_serv", listaTipiServizi)));

			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbl_servizio> getListaServiziPerTipoServizio(String codPolo, String codBib, String codTipoServ)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Tbl_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
			.addOrder(Order.asc("cod_servizio"));

			if (codTipoServ==null || codTipoServ.equalsIgnoreCase("")) {
				return new ArrayList<Tbl_servizio>();
			} else {
				Tbl_tipo_servizio tipoServizio = tipoServizioDAO.getTipoServizio(codPolo, codBib, codTipoServ);
				criteria.add(Restrictions.eq("id_tipo_servizio", tipoServizio));
			}

			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void aggiornamentoServizio(Tbl_servizio servizio) throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(servizio);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Tbl_servizio getServizioBiblioteca(String codPolo, String codBib,
								String codTipoServizio, String codServizio)
	throws DaoManagerException {

		try {
			Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(codPolo, codBib);

			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbl_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			criteria.add(Restrictions.eq("cod_servizio", codServizio));
			criteria.createCriteria("id_tipo_servizio")
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cod_tipo_serv", codTipoServizio))
					.add(Restrictions.eq("cd_bib", bib));

			return (Tbl_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_servizio getServizioBibliotecaCancellato(String codPolo,
			String codBib, String codTipoServizio, String codServizio)
			throws DaoManagerException {

		try {
			Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(codPolo, codBib);

			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbl_servizio.class);
			criteria.add(Restrictions.eq("fl_canc", 'S'));	// solo cancellati!!!
			criteria.add(Restrictions.eq("cod_servizio", codServizio));
			criteria.createCriteria("id_tipo_servizio").add(
					Restrictions.ne("fl_canc", 'S')).add(
					Restrictions.eq("cod_tipo_serv", codTipoServizio)).add(
					Restrictions.eq("cd_bib", bib));

			return (Tbl_servizio) criteria.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_servizio getServizioBibliotecaById(int idServizio)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			Tbl_servizio servizio = (Tbl_servizio) loadNoLazy(session,
					Tbl_servizio.class, new Integer(idServizio));
			return servizio;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_penalita_servizio getPenalitaServizio(String codPolo, String codBib,
									String codTipoServizio, String codServizio)
	throws DaoManagerException {

		try {
			Tbf_biblioteca_in_polo bib = serviziDAO.getBibliotecaInPolo(codPolo, codBib);

			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbl_servizio.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			criteria.add(Restrictions.eq("cod_servizio", codServizio));
			criteria.createCriteria("id_tipo_servizio")
					.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cod_tipo_serv", codTipoServizio))
					.add(Restrictions.eq("cd_bib", bib));

			Tbl_servizio servizio = (Tbl_servizio) criteria.uniqueResult();
			if (servizio != null)
				return servizio.getTbl_penalita_servizio();
			else return null;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbl_servizio> getServiziAttivi(String codPolo, String codBib, String codFruizione)
	throws DaoManagerException {
		List<Tbl_servizio> listaServiziAttivi = new ArrayList<Tbl_servizio>();

		try {
			List<TB_CODICI> listaRelazioni = CodiciProvider.getCodiciCross(CodiciType.CODICE_CAT_FRUIZIONE_TIPO_SERVIZIO, codFruizione, true);

			if (!ValidazioneDati.isFilled(listaRelazioni) )
				return listaServiziAttivi;

			//Iterator<Trl_relazioni_servizi> iterator = listaRelazioni.iterator();
			Iterator<TB_CODICI> i = listaRelazioni.iterator();
			//Tbl_tipo_servizio tipoServizio = null;

			while (i.hasNext()) {
				//tipoServizio = tipoServizioDAO.getTipoServizioById(iterator.next().getId_relazione_tabella_di_relazione());
				//listaServiziAttivi.addAll(this.getListaServiziPerTipoServizio(codPolo, codBib, tipoServizio.getCod_tipo_serv()));
				listaServiziAttivi.addAll(this.getListaServiziPerTipoServizio(codPolo, codBib, i.next().getCd_tabella()));
			}

			return listaServiziAttivi;
		} catch (Exception he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaPenalitaServizio(int idServizio, String firmaUtente)
			throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();
			Tbl_penalita_servizio penalita = (Tbl_penalita_servizio) session.get(Tbl_penalita_servizio.class, idServizio);
			if (penalita != null) {
				penalita.setFl_canc('S');
				penalita.setUte_var(firmaUtente);
				session.update(penalita);
			}

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaServizio(int idServizio, String firmaUtente)
		throws DaoManagerException {

		try {
			Session session = this.getCurrentSession();
			Tbl_servizio servizio = (Tbl_servizio) session.get(Tbl_servizio.class, idServizio);
			if (servizio != null) {
				servizio.setFl_canc('S');
				servizio.setUte_var(firmaUtente);
				session.update(servizio);
			}

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
