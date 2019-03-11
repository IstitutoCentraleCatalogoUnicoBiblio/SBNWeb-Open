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
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni;
import it.iccu.sbn.polo.orm.servizi.Trl_autorizzazioni_servizi;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class AutorizzazioniDAO extends ServiziBaseDAO {

	private UtilitaDAO serviziHibDAO = new UtilitaDAO();

	public void inserimentoAutorizzazione(Tbl_tipi_autorizzazioni tipoAut, String codicePolo, String codiceBiblioteca) throws DaoManagerException {
		Session session = this.getCurrentSession();
		tipoAut.setFl_canc('N');
		try {
			Tbf_biblioteca_in_polo bibRicerca = UtilitaDAO.creaIdBib(codicePolo, codiceBiblioteca);

			tipoAut.setCd_bib(bibRicerca);

			session.saveOrUpdate(tipoAut);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void aggiornamentoAutorizzazione(Tbl_tipi_autorizzazioni tipoAut) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			//Tbl_tipi_autorizzazioni tipoAoutMerge = (Tbl_tipi_autorizzazioni)session.merge(tipoAut);
			session.saveOrUpdate(tipoAut);
			//session.update(tipoAut);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void aggiornamentoAutorizzazioneServizio(Trl_autorizzazioni_servizi aut_serv) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			session.saveOrUpdate(aut_serv);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public void inserimentoAutorizzazioneServizio(Trl_autorizzazioni_servizi autServ) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			session.saveOrUpdate(autServ);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public List getListaTipiAutorizzazione(Tbl_tipi_autorizzazioni tipoAut, String tipoOrd)
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			tipoAut.setFl_canc('N');
			Example example = Example.create(tipoAut);
			example.setPropertySelector(NOT_EMPTY);
			example.enableLike(MatchMode.START);
			Criteria criteria = session
					.createCriteria(Tbl_tipi_autorizzazioni.class);
			criteria.add(Restrictions.eq("cd_bib", tipoAut.getCd_bib()));
			if (tipoAut.getId_tipi_autorizzazione()==0)
			{
				criteria.add(example.excludeProperty("id_tipi_autorizzazione"));
			}
/*			if (tipoAut.getDescr()!=null && tipoAut.getDescr().trim().length()>0)
			{
				criteria.add(example.ignoreCase());
			}*/
			criteria.add(example.ignoreCase());
			// aggiungo tipo ordinamento richiesto
			if (!tipoOrd.equals("")) {

				createCriteriaOrderIC(tipoOrd, null, criteria);
			}
			else
			{
				criteria.addOrder(Order.asc("descr").ignoreCase());
			}
			return criteria.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_tipi_autorizzazioni getTipoAutorizzazioneById(int id_aut)
			throws DaoManagerException {

		try {
			Session session = getCurrentSession();
			Tbl_tipi_autorizzazioni aut =
				(Tbl_tipi_autorizzazioni) loadNoLazy(session, Tbl_tipi_autorizzazioni.class, new Integer(id_aut));
			return aut;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Date getDateAutorizzazione(String codPolo, String codBib, String codAutorizzazione, int idutenteBibl, String tipo ) throws DaoManagerException {
		Session session = this.getCurrentSession();
		UtilitaDAO dao = new UtilitaDAO();
		try {
			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Trl_utenti_biblioteca.class);
			criteria.add(Restrictions.eq("cd_biblioteca", bib));
			criteria.add(Restrictions.eq("cod_tipo_aut".trim(), codAutorizzazione.trim()).ignoreCase());
			criteria.add(Restrictions.eq("fl_canc", 'N'));
			//criteria.add(Restrictions.eq("id_utenti.id_utenti", idutente));
			criteria.add(Restrictions.eq("id_utenti_biblioteca", idutenteBibl));
			if (tipo.equals("AF"))
			{
				criteria.setProjection(Property.forName("data_fine_aut"));
			}
			else if(tipo.equals("AI"))
			{
				criteria.setProjection(Property.forName("data_inizio_aut"));
			}
			else if(tipo.equals("SF"))
			{
				criteria.setProjection(Property.forName("data_fine_sosp"));
			}
			else if(tipo.equals("SI"))
			{
				criteria.setProjection(Property.forName("data_inizio_sosp"));
			}
			List results = criteria.list();

			Date dataFine;

			if (results!=null && results.size()==1)
			{
				dataFine=(Date) results.get(0);
				return (dataFine);
			}
			else
			{
				return null;
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Trl_autorizzazioni_servizi> getListaServiziAutorizzazione(String codPolo, String codBib, String codTipoAut)
		throws DaoManagerException {

			Session session = this.getCurrentSession();
			try {
				Tbf_biblioteca_in_polo bib = serviziHibDAO.getBibliotecaInPolo(codPolo, codBib);
				Criteria c = session.createCriteria(Tbl_tipi_autorizzazioni.class);
				c.add(Restrictions.eq("cd_bib", bib));
				c.add(Restrictions.eq("fl_canc", 'N'));
				c.add(Restrictions.eq("cod_tipo_aut", ValidazioneDati.trimOrEmpty(codTipoAut)).ignoreCase());

				Tbl_tipi_autorizzazioni aut = (Tbl_tipi_autorizzazioni) c.uniqueResult();
				if (aut == null)
					return ValidazioneDati.emptyList(Trl_autorizzazioni_servizi.class);

				Set serviziAut = aut.getTrl_autorizzazioni_servizi();
				// elimino dalla lista i servizi cancellati logicamente
				Query query = session.createFilter(serviziAut, "where fl_canc='N'");

				return query.list();

			} catch (HibernateException he) {
				throw new DaoManagerException(he);
			}
		}

	public String  getListaAutorizzazioniServizio(String codPolo, String codBib, int idServ)
	throws DaoManagerException {

		Session session = this.getCurrentSession();
		String listaAutorizzazioni="";
		try {
			Tbf_biblioteca_in_polo bib = serviziHibDAO.getBibliotecaInPolo(codPolo, codBib);

			Criteria criteria = session.createCriteria(Tbl_tipi_autorizzazioni.class);
			DetachedCriteria childCriteriaSez = DetachedCriteria.forClass( Trl_autorizzazioni_servizi.class , "child_sez");
			childCriteriaSez.setProjection(Property.forName("child_sez.id_tipi_autorizzazione_id_tipi_autorizzazione"));
			childCriteriaSez.add(Restrictions.eq("child_sez.id_servizio_id_servizio",idServ));
			childCriteriaSez.add(Restrictions.eq("child_sez.fl_canc", 'N'));
			criteria.add(Subqueries.exists(childCriteriaSez));
			criteria.add(Property.forName("id_tipi_autorizzazione").in( childCriteriaSez));
			criteria.setProjection(Property.forName("cod_tipo_aut"));
			criteria.add(Restrictions.eq("fl_canc", 'N'));


			List results = criteria.list();

			if (results!=null && results.size()>0)
			{
				for (int index = 0; index < results.size(); index++)
				{
					if (index>0)
					{
						listaAutorizzazioni=listaAutorizzazioni + ",";
					}
					listaAutorizzazioni=listaAutorizzazioni+ results.get(index);
				}
			}
			return listaAutorizzazioni;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_tipi_autorizzazioni getTipoAutorizzazione(String codPolo, String codBib, String codTipoAut)
		throws DaoManagerException {

			Session session = this.getCurrentSession();

			try{
				Tbf_biblioteca_in_polo bib = UtilitaDAO.creaIdBib(codPolo, codBib);
				Criteria criteria = session.createCriteria(Tbl_tipi_autorizzazioni.class);
				criteria.add(Restrictions.eq("cd_bib", bib));
				criteria.add(Restrictions.eq("fl_canc", 'N'));
				criteria.add(Restrictions.eq("cod_tipo_aut".trim(), codTipoAut.trim()).ignoreCase());

				Tbl_tipi_autorizzazioni aut = (Tbl_tipi_autorizzazioni) criteria.uniqueResult();

				return aut;
			} catch (HibernateException he) {
				throw new DaoManagerException(he);
			}
	}


	public boolean esisteAutomaticoX(String codPolo, String codBib, String codTipoAut, char codAutomaticoX)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			Tbf_biblioteca_in_polo bib = serviziHibDAO.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Tbl_tipi_autorizzazioni.class);
			criteria.add(Restrictions.eq("cd_bib", bib));
			criteria.add(Restrictions.eq("fl_canc", 'N'));
			criteria.add(Restrictions.eq("flag_aut", codAutomaticoX));
			criteria.add(Restrictions.ne("cod_tipo_aut".trim(), codTipoAut.trim()).ignoreCase());

			return (criteria.list().size()>0);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_tipi_autorizzazioni getAutorizzazioneByProfessione(String codPolo, String codBib,int idOcc)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		Tbl_tipi_autorizzazioni autorizzazione=null;
		try{

			Tbf_biblioteca_in_polo bib = serviziHibDAO.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Tbl_tipi_autorizzazioni.class);
			criteria.add(Restrictions.eq("cd_bib", bib));
			criteria.add(Restrictions.eq("fl_canc", 'N'));
			if (idOcc>0 )
			{
				DetachedCriteria childCriteriaOcc = DetachedCriteria.forClass(Tbl_occupazioni.class , "child_occ");
				childCriteriaOcc.setProjection(Property.forName("child_occ.professione"));
				childCriteriaOcc.add(Restrictions.eq("child_occ.id_occupazioni",idOcc));
				childCriteriaOcc.add(Restrictions.eq("child_occ.fl_canc", 'N'));
				childCriteriaOcc.add(Restrictions.eq("child_occ.cd_biblioteca", bib));
				criteria.add(Subqueries.exists(childCriteriaOcc));
				criteria.add(Property.forName("flag_aut").in( childCriteriaOcc));
			}

			List autorizzazioni = criteria.list();
			// deve essere solo uno??
			if (autorizzazioni!=null && autorizzazioni.size()==1)
			{
				autorizzazione=(Tbl_tipi_autorizzazioni) autorizzazioni.get(0);
			}
			return autorizzazione;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}


	public Tbl_tipi_autorizzazioni esisteAutorizzazioneCancellata(String codPolo, String codBib, String codTipoAut)
	throws DaoManagerException {

		Session session = this.getCurrentSession();
		Tbl_tipi_autorizzazioni output=null;
		try{
			Tbf_biblioteca_in_polo bib = serviziHibDAO.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Tbl_tipi_autorizzazioni.class);
			criteria.add(Restrictions.eq("cd_bib", bib));
			criteria.add(Restrictions.eq("fl_canc", 'S'));
			criteria.add(Restrictions.eq("cod_tipo_aut".trim(), codTipoAut.trim()).ignoreCase());

			List aut = criteria.list();
			if (aut!=null && aut.size()==1)
			{
				output=(Tbl_tipi_autorizzazioni)aut.get(0);
			}
			return output;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esisteAutorizzazione(String codPolo, String codBib, String codTipoAut, String descr)
	throws DaoManagerException {

		Session session = this.getCurrentSession();

		try{
			Tbf_biblioteca_in_polo bib = serviziHibDAO.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Tbl_tipi_autorizzazioni.class);
			criteria.add(Restrictions.eq("cd_bib", bib));
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			//criteria.add(Restrictions.eq("cod_tipo_aut", codTipoAut).ignoreCase());
			//criteria.add(Restrictions.eq("descr", descr).ignoreCase());
			criteria.add(Restrictions.or(Restrictions.eq("cod_tipo_aut".trim(), codTipoAut.trim()).ignoreCase(),
					Restrictions.eq("descr", descr).ignoreCase()
			));
			List aut = criteria.list();

			return (aut.size()>0);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List getListaServiziAutorizzazione(String codPolo, String codBib, char flg_aut)
	throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbf_biblioteca_in_polo bib = serviziHibDAO.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Tbl_tipi_autorizzazioni.class);
			criteria.add(Restrictions.eq("cd_bib", bib));
			criteria.add(Restrictions.ne("fl_canc", 'S'));
			criteria.add(Restrictions.eq("flag_aut", flg_aut));

			Tbl_tipi_autorizzazioni aut = (Tbl_tipi_autorizzazioni) criteria.uniqueResult();
			if (aut == null)
				return new ArrayList<Trl_autorizzazioni_servizi>();
			Set serviziAut = aut.getTrl_autorizzazioni_servizi();
			// elimino dalla lista i servizi cancellati logicamente
			Query query = session.createFilter(serviziAut, "where fl_canc='N'");

			return query.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Trl_autorizzazioni_servizi getServizioAutorizzazione(String codPolo,
			String codBib, int idAut, int idServ, String codAut,
			Character flCanc) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Tbf_biblioteca_in_polo bib = serviziHibDAO.getBibliotecaInPolo(codPolo, codBib);
			Criteria c = session.createCriteria(Trl_autorizzazioni_servizi.class);
			if (idAut > 0) {
				c.add(Restrictions.eq("id_tipi_autorizzazione_id_tipi_autorizzazione", idAut));
				c.add(Restrictions.eq("id_servizio_id_servizio", idServ));
				if (flCanc != null)
					c.add(Restrictions.eq("fl_canc", flCanc.charValue() ));

				c.createCriteria("id_tipi_autorizzazione").add(Restrictions.eq("cd_bib", bib));
			}

			if (idAut == 0 && codAut != null && codAut.trim().length() > 0) {
				c.add(Restrictions.eq("id_servizio_id_servizio", idServ));
				if (flCanc != null)
					c.add(Restrictions.eq("fl_canc", flCanc.charValue() ));

				c.createCriteria("id_tipi_autorizzazione")
						.add(Restrictions.eq("cd_bib", bib))
						.add(Restrictions.eq("cod_tipo_aut".trim(), codAut.trim()).ignoreCase());
			}

			return (Trl_autorizzazioni_servizi) c.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaDirittoAutorizzazione(int idAut, int idServ,
			String firmaUtente) throws DaoManagerException {

		Session session = this.getCurrentSession();
		try {
			Criteria c = session.createCriteria(Trl_autorizzazioni_servizi.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			if (idAut > 0)
				c.add(Restrictions.eq("id_tipi_autorizzazione_id_tipi_autorizzazione", idAut));

			if (idServ > 0)
				c.add(Restrictions.eq("id_servizio_id_servizio", idServ));

			List<Trl_autorizzazioni_servizi> diritti = c.list();

			for (Trl_autorizzazioni_servizi diritto : diritti) {
				diritto.setFl_canc('S');
				diritto.setUte_var(firmaUtente);
				session.update(diritto);
			}

			session.flush();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


}

