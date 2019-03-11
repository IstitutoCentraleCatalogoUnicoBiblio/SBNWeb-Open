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
import it.iccu.sbn.ejb.vo.servizi.serviziweb.ListaServiziVO;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Trl_diritti_utente;
import it.iccu.sbn.polo.orm.servizi.Trl_utenti_biblioteca;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class Trl_diritti_utenteDAO extends ServiziBaseDAO {



	public Trl_diritti_utenteDAO() {
		super();
	}


	public List select(Integer idUte) throws DaoManagerException,UtenteNotFoundException
	{

		Session session = this.getCurrentSession();

		Trl_diritti_utente dirittiUte = new Trl_diritti_utente();

		//dirittiUte.setId_utenti_id_utenti(idUte);
		//dirittiUte.setFl_canc('N');

		Example example = Example.create(dirittiUte)
	    .excludeZeroes()           //exclude zero valued properties
	    .ignoreCase().setPropertySelector(NOT_EMPTY);           //perform case insensitive string comparisons
		//.excludeProperty("sesso");
//	    .enableLike();             //use like for string comparisons
		Criteria criteria = session.createCriteria(Trl_diritti_utente.class);
		criteria.add(Restrictions.eq("id_utenti.id",idUte));
		List results = criteria.list();
		//List results = session.createCriteria(Trl_diritti_utente.class)
	    //.add(example)
	    //.list();
		List servizi = new ArrayList();

		Iterator iterator = results.iterator();
		while (iterator.hasNext()) {
			Trl_diritti_utente diritti = (Trl_diritti_utente)iterator.next();
			ListaServiziVO serv = new ListaServiziVO();
			serv.setIdServizio(diritti.getId_servizio_id_servizio());
			serv.setServizio(diritti.getId_servizio().getDescr());
			servizi.add(serv);


		}


		return servizi;
	}

	public List getDirittoUtenteByID(Tbf_biblioteca_in_polo bib, int idServizio, String codAut, boolean del) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Trl_diritti_utente.class);
			c.add(Restrictions.eq("id_servizio_id_servizio", idServizio));
			if (del)
				c.add(Restrictions.eq("fl_canc", 'S'));
			else
				c.add(Restrictions.eq("fl_canc", 'N'));

			if (ValidazioneDati.isFilled(codAut)  )	{
				DetachedCriteria dcUte = DetachedCriteria.forClass(Trl_utenti_biblioteca.class , "child_ute");
				dcUte.setProjection(Property.forName("child_ute.id_utenti.id_utenti"));
				dcUte.add(Restrictions.eq("child_ute.cod_tipo_aut".trim(),codAut.trim()).ignoreCase());
				dcUte.add(Restrictions.eq("child_ute.fl_canc", 'N'));
				dcUte.add(Restrictions.eq("cd_biblioteca", bib));

				c.add(Subqueries.exists(dcUte));
				c.add(Property.forName("id_utenti_id_utenti").in(dcUte));
			}

			List dirittiUtenti = c.list();
			return dirittiUtenti;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Integer[]> getIdDirittiUtentePerAut(Tbf_biblioteca_in_polo bib, int idServizio, String codAut, boolean del) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Trl_diritti_utente.class);
			c.add(Restrictions.eq("id_servizio_id_servizio", idServizio));
			ProjectionList prjList = Projections.projectionList();
			prjList.add(Projections.property("id_utenti.id")).add(Projections.property("id_servizio.id"));
			c.setProjection(prjList);

			if (del)
				c.add(Restrictions.eq("fl_canc", 'S'));
			else
				c.add(Restrictions.eq("fl_canc", 'N'));

			if (ValidazioneDati.isFilled(codAut)  )	{
				DetachedCriteria dcUte = DetachedCriteria.forClass(Trl_utenti_biblioteca.class , "child_ute");
				dcUte.setProjection(Property.forName("child_ute.id_utenti.id_utenti"));
				dcUte.add(Restrictions.eq("child_ute.cod_tipo_aut".trim(),codAut.trim()).ignoreCase());
				dcUte.add(Restrictions.eq("child_ute.fl_canc", 'N'));
				dcUte.add(Restrictions.eq("cd_biblioteca", bib));

				c.add(Subqueries.exists(dcUte));
				c.add(Property.forName("id_utenti_id_utenti").in(dcUte));
			}

			return c.list();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}



	public Trl_diritti_utente getDirittoUtenteByID(int idUtente, int idServizio) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Trl_diritti_utente.class);
			c.add(Restrictions.eq("id_servizio.id", idServizio));
			c.add(Restrictions.eq("id_utenti.id", idUtente));

			return (Trl_diritti_utente) c.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

}

