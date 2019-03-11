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
package it.iccu.sbn.persistence.dao.acquisizioni;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_profili_acquisto;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_sez_acquisizione_fornitori;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;


public class Tba_profili_acquistoDao extends DaoManager {
	//private static final int INNER_JOIN = org.hibernate.sql.JoinFragment.INNER_JOIN;
    //private static final int FULL_JOIN = org.hibernate.sql.JoinFragment.FULL_JOIN;
    //private static final int LEFT_JOIN = org.hibernate.sql.JoinFragment.LEFT_OUTER_JOIN;

	private static Codici codici;

	static{
		try {
			codici = DomainEJBFactory.getInstance().getCodici();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Tba_profili_acquisto> getRicercaListaProfiliHib(ListaSuppProfiloVO ricercaProfili) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_profili_acquisto.class,"prof");

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaProfili.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaProfili.getCodBibl());
			bib.setCd_polo(polo);

			if (!ricercaProfili.isFlag_canc())
			{
				cr.add(Restrictions.eq("prof.fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("prof.fl_canc", 'S'));
			}
			//cr.add(Restrictions.eq("cd_bib", bib));

			if (ricercaProfili.getSezione()!=null &&  ricercaProfili.getSezione().getCodice()!=null  && ricercaProfili.getSezione().getCodice().trim().length()!=0)
			{
				DetachedCriteria childCriteriaSez = DetachedCriteria.forClass(Tba_sez_acquis_bibliografiche.class , "child_sez");
				childCriteriaSez.setProjection(Property.forName("child_sez.id_sez_acquis_bibliografiche" ) );
				childCriteriaSez.add(Property.forName("prof.id_sez_acquis_bibliografiche.id_sez_acquis_bibliografiche").eqProperty("child_sez.id_sez_acquis_bibliografiche") );
				childCriteriaSez.add(Restrictions.eq("child_sez.cod_sezione",ricercaProfili.getSezione().getCodice().trim()));
				childCriteriaSez.add(Restrictions.eq("child_sez.fl_canc", 'N'));
				childCriteriaSez.add(Restrictions.eq("child_sez.cd_bib", bib));
				cr.add(Subqueries.exists(childCriteriaSez));
				cr.add(Property.forName("prof.id_sez_acquis_bibliografiche.id_sez_acquis_bibliografiche").in( childCriteriaSez));
			}
			if (ricercaProfili.getProfilo()!=null  && ricercaProfili.getProfilo().getCodice()!=null && ricercaProfili.getProfilo().getCodice().trim().length()!=0)
			{
				cr.add(Restrictions.eq("prof.cod_prac", Integer.parseInt(ricercaProfili.getProfilo().getCodice()))); // 24.07.09 BigDecimal.valueOf(Integer.parseInt(ricercaProfili.getProfilo().getCodice()))
			}

			if (ricercaProfili.getProfilo()!=null && ricercaProfili.getProfilo().getDescrizione()!=null && ricercaProfili.getProfilo().getDescrizione().trim().length()!=0)
			{
				cr.add(Restrictions.like("prof.descr", ricercaProfili.getProfilo().getDescrizione().trim()+"%" ));
			}

			if (ricercaProfili.getFornitore()!=null  && ricercaProfili.getFornitore().getCodice()!=null && ricercaProfili.getFornitore().getCodice().trim().length()!=0)
			{
				DetachedCriteria childCriteriaTraSezForn = DetachedCriteria.forClass( Tra_sez_acquisizione_fornitori.class , "child_Tra_sez_forn");
				childCriteriaTraSezForn.setProjection(Property.forName("child_Tra_sez_forn.cod_prac"));
				childCriteriaTraSezForn.add(Property.forName("prof.cod_prac").eqProperty("child_Tra_sez_forn.cod_prac") );
				childCriteriaTraSezForn.add(Restrictions.eq("child_Tra_sez_forn.cod_fornitore", Integer.parseInt(ricercaProfili.getFornitore().getCodice().trim())));
				childCriteriaTraSezForn.add(Restrictions.eq("child_Tra_sez_forn.fl_canc", 'N'));
				childCriteriaTraSezForn.add(Restrictions.eq("child_Tra_sez_forn.cd_biblioteca", bib));
				cr.add(Subqueries.exists(childCriteriaTraSezForn));
				cr.add(Property.forName("prof.cod_prac").in( childCriteriaTraSezForn));

				//sql=sql + " sezAcqForn.cod_fornitore='" + ricercaProfili.getFornitore().getCodice() +"'";;
			}
			// ordinamento passato
			if (ricercaProfili.getOrdinamento()==null || (ricercaProfili.getOrdinamento()!=null && ricercaProfili.getOrdinamento().equals("")) )
			{
				cr.addOrder(Order.asc("prof.descr"));
			}
			else if (ricercaProfili.getOrdinamento().equals("1"))
			{
				cr.addOrder(Order.asc("prof.cod_prac"));
			}
			else if (ricercaProfili.getOrdinamento().equals("2"))
			{
				cr.addOrder(Order.asc("prof.descr"));
			}
			else if (ricercaProfili.getOrdinamento().equals("3"))
			{
				//cr.addOrder(Order.asc("prof.id_sez_acquis_bibliografiche.id_sez_acquis_bibliografiche")); // ok
			}
			List<Tba_profili_acquisto> results = cr.list();

			if (results!=null && results.size()>0 && ricercaProfili.getOrdinamento()!=null && ricercaProfili.getOrdinamento().equals("3"))
			{
				Comparator comp=null;
				comp =new SezComparator();
				Collections.sort(results, comp);
			}
			if (results==null || (results!=null && results.size()==0))
			{
				results=null;
			}

			return results;
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List<Tra_sez_acquisizione_fornitori> getRicercaListaFornitoriProfiloHib(ListaSuppProfiloVO ricercaProfili) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tra_sez_acquisizione_fornitori.class,"fornProf");

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaProfili.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaProfili.getCodBibl());
			bib.setCd_polo(polo);

			cr.add(Restrictions.eq("cd_biblioteca", bib));

			if (!ricercaProfili.isFlag_canc())
			{
				cr.add(Restrictions.eq("fornProf.fl_canc", 'N'));
			}
			else
			{
				cr.add(Restrictions.eq("fornProf.fl_canc", 'S'));
			}
			if (ricercaProfili.getFornitore()!=null  && ricercaProfili.getFornitore().getCodice()!=null && ricercaProfili.getFornitore().getCodice().trim().length()!=0)
			{
				cr.add(Restrictions.eq("fornProf.cod_fornitore", Integer.parseInt(ricercaProfili.getFornitore().getCodice().trim())));
			}
			if (ricercaProfili.getProfilo()!=null  && ricercaProfili.getProfilo().getCodice()!=null && ricercaProfili.getProfilo().getCodice().trim().length()!=0)
			{
				cr.add(Restrictions.eq("fornProf.cod_prac", BigDecimal.valueOf(Integer.parseInt(ricercaProfili.getProfilo().getCodice()))));
			}
			// ordinamento passato
/*			if (ricercaProfili.getOrdinamento()==null || (ricercaProfili.getOrdinamento()!=null && ricercaProfili.getOrdinamento().equals("")) )
			{
				cr.addOrder(Order.asc("prof.descr"));
			}
			else if (ricercaProfili.getOrdinamento().equals("1"))
			{
				cr.addOrder(Order.asc("prof.cod_prac"));
			}
			else if (ricercaProfili.getOrdinamento().equals("2"))
			{
				cr.addOrder(Order.asc("prof.descr"));
			}
			else if (ricercaProfili.getOrdinamento().equals("3"))
			{
				//cr.addOrder(Order.asc("prof.id_sez_acquis_bibliografiche.id_sez_acquis_bibliografiche")); // ok
			}
*/			List<Tra_sez_acquisizione_fornitori> results = cr.list();

			if (results==null || (results!=null && results.size()==0))
			{
				results=null;
			}

			return results;
		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	private static class SezComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((Tba_profili_acquisto) o1).getId_sez_acquis_bibliografiche().getCod_sezione();
				String e2 = ((Tba_profili_acquisto) o2).getId_sez_acquis_bibliografiche().getCod_sezione();

				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}



	public boolean inserisciProfiloHib(Tba_profili_acquisto profilo)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			//session.save(profilo.getId_sez_acquis_bibliografiche());
			session.saveOrUpdate(profilo);
			//session.flush();
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}

	public boolean inserisciFornProfiloHib(Tra_sez_acquisizione_fornitori fornProfilo)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			//session.save(profilo.getId_sez_acquis_bibliografiche());
			session.saveOrUpdate(fornProfilo);
			//session.flush();
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}



	public boolean  modificaProfiloHib(Tba_profili_acquisto profilo)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(profilo); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean  cancellaProfiloHib(Tba_profili_acquisto profilo)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(profilo); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

}
