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
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_serie_inventariale;
import it.iccu.sbn.polo.orm.servizi.Tbl_iter_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca;
import it.iccu.sbn.polo.orm.servizi.Tbl_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_specificita_titoli_studio;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipi_autorizzazioni;
import it.iccu.sbn.polo.orm.servizi.Tbl_tipo_servizio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class UtilitaDAO extends DaoManager {

	public static Tbl_occupazioni creaIdOccupazione(String codPolo, String codBib,
			int idOccupazione) {
			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbl_occupazioni occup = new Tbl_occupazioni();
			occup.setCd_biblioteca(bib);
			occup.setId_occupazioni(idOccupazione);
			return occup;
		}

	public static Tbl_specificita_titoli_studio creaIdTitoloStudio(String codPolo, String codBib,
			int idTitoloStudio) {
			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(codPolo);
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(codBib);
			bib.setCd_polo(polo);
			Tbl_specificita_titoli_studio titoloStudio = new Tbl_specificita_titoli_studio();
			titoloStudio.setCd_biblioteca(bib);
			titoloStudio.setId_specificita_titoli_studio(idTitoloStudio);
			return titoloStudio;
		}

	public static Tbc_inventario creaIdInv(String codPolo, String codBib,
		String codSerie, int codInv) {
		Tbf_polo polo = new Tbf_polo();
		polo.setCd_polo(codPolo);
		Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
		bib.setCd_biblioteca(codBib);
		bib.setCd_polo(polo);
		Tbc_serie_inventariale serie = new Tbc_serie_inventariale();
		serie.setCd_polo(bib);
		Tbc_inventario inv = new Tbc_inventario();
		inv.setCd_serie(serie);
		inv.setCd_inven(codInv);
		return inv;
	}

	public static Tbl_servizio creaIdServizio(String codPolo, String codBib, String codTipoServizio, String codServizio) {
		Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
		Tbl_tipo_servizio tipo_servizio = new Tbl_tipo_servizio();
		tipo_servizio.setCd_bib(bib);
		tipo_servizio.setCod_tipo_serv(codTipoServizio);
		tipo_servizio.setFl_canc('N');

		Tbl_servizio servizio = new Tbl_servizio();
		servizio.setId_tipo_servizio(tipo_servizio);
		servizio.setCod_servizio(codServizio);
		servizio.setFl_canc('N');

		return servizio;
	}


	public Tbf_polo getPolo(String codPolo) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			criteria.add(Restrictions.eq("cd_polo",       codPolo));
			return (Tbf_polo)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbf_biblioteca_in_polo getBibliotecaInPolo(String codPolo, String codBib) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbf_polo polo = this.getPolo(codPolo);
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_polo",       polo))
					.add(Restrictions.eq("cd_biblioteca", codBib))
					.add(Restrictions.ne("fl_canc",       'S'));
			return (Tbf_biblioteca_in_polo)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public List<Tbf_biblioteca_in_polo> getBibliotecheInPolo(String codPolo) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbf_polo polo = this.getPolo(codPolo);
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_polo",       polo))
					.add(Restrictions.ne("fl_canc",       'S'));
			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_parametri_biblioteca getParametriBiblioteca(String codPolo, String codBib) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try{
			Tbf_biblioteca_in_polo bib = this.getBibliotecaInPolo(codPolo, codBib);
			if (bib==null)
				throw new DaoManagerException("Biblioteca non trovata in base dati. Cod. Polo:"+codPolo+"  Cod. Bib:"+codBib);

			Criteria criteria = session.createCriteria(Tbl_parametri_biblioteca.class);
			criteria.add(Restrictions.eq("cd_bib", bib));

			return (Tbl_parametri_biblioteca)criteria.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}
	public Tbl_parametri_biblioteca aggiornaParametriBiblioteca(Tbl_parametri_biblioteca parametri)
	throws DaoManagerException, DAOConcurrentException {
		try {
			Session session = this.getCurrentSession();
			if (parametri.getId_parametri_biblioteca() == 0)
				session.save(parametri);
			else
				parametri = (Tbl_parametri_biblioteca) session.merge(parametri);

			session.flush();
			return parametri;

		} catch (StaleStateException e) {
			throw new DAOConcurrentException(e);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean verificaAutoregistrazione(String codPolo, String codBib)
	throws DaoManagerException {

		Session session = this.getCurrentSession();
		UtilitaDAO dao = new UtilitaDAO();

		try {
			Tbf_biblioteca_in_polo bib = dao.getBibliotecaInPolo(codPolo, codBib);
			Criteria criteria = session.createCriteria(Tbl_tipi_autorizzazioni.class);

			criteria.add(Restrictions.eq("cd_bib", bib))
					.add(Restrictions.eq("flag_aut", '*'))
					.add(Restrictions.eq("fl_canc", 'N'));

			criteria.createCriteria("Trl_autorizzazioni_servizi")
					.add(Restrictions.eq("fl_canc", 'N'));

			criteria.setProjection(Projections.rowCount());

			Number rowCount = (Number) criteria.uniqueResult();
			return (rowCount.longValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public void filtraListaIterServizioPerAttivitaSucc(List<Tbl_iter_servizio> listaIter)
	throws DaoManagerException {
		List<Tbl_iter_servizio>     listaFiltrata = new ArrayList<Tbl_iter_servizio>();
		Iterator<Tbl_iter_servizio> iterator = listaIter.iterator();
		Tbl_iter_servizio           iterServizio=null;

		while (iterator.hasNext()) {
			iterServizio = iterator.next();
			listaFiltrata.add(iterServizio);
			//mi fermo al primo obbligatorio trovato
			if (iterServizio.getObbl()=='S') break;
		}
		listaIter=listaFiltrata;
	}
	//Massimimilano almaviva 2009
	public List listaBibliotecheInPolo(String codPolo) throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			Tbf_polo polo = this.getPolo(codPolo);
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_polo",       polo))
					.add(Restrictions.ne("fl_canc",       'S'));
			return criteria.list();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}


