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
package it.iccu.sbn.persistence.dao.documentofisico;

import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_provenienza_inventario;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class Tbc_provenienza_inventarioDao extends DaoManager {

	Connection connection = null;

	public Tbc_provenienza_inventarioDao() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Tbc_provenienza_inventario getProvenienza(String codPolo, String codBib, String codProven)
	throws DaoManagerException {

		Tbc_provenienza_inventario rec = null;
		try{
		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tbc_provenienza_inventario.class);


		Tbf_polo polo = new Tbf_polo();
		polo.setCd_polo(codPolo);
		Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
		bib.setCd_biblioteca(codBib);
		bib.setCd_polo(polo);
		rec = new Tbc_provenienza_inventario();
		rec.setCd_polo(bib);
		rec.setCd_proven(codProven.toUpperCase());
		cr.add(Restrictions.ne("fl_canc", 'S'));

		rec = (Tbc_provenienza_inventario) loadNoLazy(session, Tbc_provenienza_inventario.class, rec);
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public List getListaProvenienze(String codPolo, String codBib, String filtroProvenienza)
	throws DaoManagerException {

			try{
				Session session = this.getCurrentSession();
				Criteria cr = session.createCriteria(Tbc_provenienza_inventario.class);

				Tbf_polo polo = new Tbf_polo();
				polo.setCd_polo(codPolo);
				Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
				bib.setCd_biblioteca(codBib);
				bib.setCd_polo(polo);

				cr.add(Restrictions.eq("cd_polo", bib));
				cr.add(Restrictions.ne("fl_canc", 'S'));


				// evolutiva Mail Carla del 16.06.2017 - nuova gestione del cartiglio relativo alla Provenienza:
				// se nel campo Provenienza si inserisce una parola/stringa la pressione del cartiglio attiva una ricerca filtrata
				// per stringa inserita altrimenti se il campo rimane vuoto la ricerca rimane uguale a quella attuale.
//				criteria.add(Restrictions.like("column", value, MatchMode.ANYWHERE));
//				and UPPER(provInv.descr) like '%FONDO%'
//				cr.add(Restrictions.like("provInv.descr", parolaChiave, MatchMode.ANYWHERE));
//				cr.add(Restrictions.like("descr", parolaChiave).ignoreCase());

				if (filtroProvenienza.length() > 0) {
					filtroProvenienza = "%" + filtroProvenienza + "%";
					cr.add(Restrictions.like("descr", filtroProvenienza).ignoreCase());
				}

				cr.addOrder(Order.asc("cd_proven"));

				List<Tbc_provenienza_inventario> results = cr.list();
				return results;
			}catch (HibernateException e){
				throw new DaoManagerException(e);
			}
		}
	public boolean inserimentoProvenienza(ProvenienzaInventarioVO recProven)
	throws DaoManagerException	{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			Tbc_provenienza_inventario recTab = new Tbc_provenienza_inventario();

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(recProven.getCodPolo());
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(recProven.getCodBib());
			bib.setCd_polo(polo);

			recTab.setCd_polo(bib);
			recTab.setCd_proven(recProven.getCodProvenienza().toUpperCase());
			recTab.setDescr(recProven.getDescrProvenienza());
			recTab.setTs_ins(ts);
			recTab.setTs_var(ts);
			recTab.setUte_ins(recProven.getUteIns());
			recTab.setUte_var(recProven.getUteIns());
			recTab.setFl_canc('N');
			session.saveOrUpdate(recTab);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean modificaProvenienza(Tbc_provenienza_inventario recProven)
	throws DaoManagerException {
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(recProven);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
}
