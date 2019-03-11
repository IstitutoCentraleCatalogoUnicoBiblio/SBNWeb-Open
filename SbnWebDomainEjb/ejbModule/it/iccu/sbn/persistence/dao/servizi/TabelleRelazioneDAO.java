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

import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.servizi.Trl_relazioni_servizi;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class TabelleRelazioneDAO extends ServiziBaseDAO {

	public void insert(Trl_relazioni_servizi relazione)
	throws DaoManagerException, AlreadyExistsException {
		Trl_relazioni_servizi relazioneGiaPresente = this.select(relazione);

		if (relazioneGiaPresente!=null) {
			//controllo se è stata cancellata, allora ripristino
			if (relazioneGiaPresente.getFl_canc()=='S') {
				relazioneGiaPresente.setFl_canc('N');

				this.save(relazioneGiaPresente);
			} else throw new AlreadyExistsException();

		} else this.save(relazione);
	}


	public Trl_relazioni_servizi select(Trl_relazioni_servizi relazione)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		Example example = Example.create(relazione);
		example.setPropertySelector(NOT_EMPTY);
		example.ignoreCase();
		example.excludeProperty("id")
				.excludeProperty("ute_ins")
				.excludeProperty("ts_ins")
				.excludeProperty("ute_var")
				.excludeProperty("ts_var");

		Criteria criteria = session.createCriteria(Trl_relazioni_servizi.class);
		criteria.add(example);

		return (Trl_relazioni_servizi)criteria.uniqueResult();
	}


	public List<Trl_relazioni_servizi> select(String codPolo, String codBib, String codiceRelazione)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		UtilitaDAO utilDao = new UtilitaDAO();

		Tbf_biblioteca_in_polo bib = utilDao.getBibliotecaInPolo(codPolo, codBib);

		Criteria criteria = session.createCriteria(Trl_relazioni_servizi.class);
		criteria.add(Restrictions.eq("cd_bib", bib))
				.add(Restrictions.eq("cd_relazione", codiceRelazione))
				.addOrder(Order.asc("id_relazione_tabella_di_relazione"))
				.addOrder(Order.asc("id_relazione_tb_codici"));

		return criteria.list();
	}


	public List<Trl_relazioni_servizi> select(String codPolo, String codBib, String codiceRelazione, String codiceTbCodici)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		UtilitaDAO utilDao = new UtilitaDAO();

		Tbf_biblioteca_in_polo bib = utilDao.getBibliotecaInPolo(codPolo, codBib);

		Criteria criteria = session.createCriteria(Trl_relazioni_servizi.class);
		criteria.add(Restrictions.eq("cd_bib", bib))
				.add(Restrictions.eq("cd_relazione", codiceRelazione))
				.add(Restrictions.eq("id_relazione_tb_codici", codiceTbCodici));

		return criteria.list();
	}

	public Trl_relazioni_servizi select(int id)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			return (Trl_relazioni_servizi)loadNoLazy(session, Trl_relazioni_servizi.class, new Integer(id));
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public void save(Trl_relazioni_servizi relazione)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(relazione);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void update(Trl_relazioni_servizi relazione)
	throws DaoManagerException {
		Session session = this.getCurrentSession();
		try {
			session.update(relazione);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void delete(Integer[] id, String uteVar)
	throws DaoManagerException {
		Trl_relazioni_servizi relazione=null;

		for (int i=0; i<id.length; i++) {
			relazione=this.select(id[i]);
			if (relazione!=null){
				relazione.setFl_canc('S');
				relazione.setUte_var(uteVar);
			}
		}
	}


	public void restore(Integer[] id, String uteVar)
	throws DaoManagerException {
		Trl_relazioni_servizi relazione=null;

		for (int i=0; i<id.length; i++) {
			relazione=this.select(id[i]);
			if (relazione!=null){
				relazione.setFl_canc('N');
				relazione.setUte_var(uteVar);
			}
		}
	}

}
