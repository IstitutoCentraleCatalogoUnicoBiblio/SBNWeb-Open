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
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_buono_ordine;
import it.iccu.sbn.polo.orm.acquisizioni.Tbb_capitoli_bilanci;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_elementi_buono_ordine;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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




public class Tba_buono_ordineDao extends DaoManager {

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


	public List<Tba_buono_ordine> getRicercaListaBuoniOrdHib(ListaSuppBuoniOrdineVO ricercaBuoniOrd) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_buono_ordine.class);

/*			Criteria cr = session.createCriteria(Tbb_capitoli_bilanci.class);
			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaBuoniOrd.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaBuoniOrd.getCodBibl());
			bib.setCd_polo(polo);

			cr.setProjection(Property.forName("id_capitoli_bilanci" ) );
			// trasformare in bigdecimal
			cr.add(Restrictions.eq("esercizio",BigDecimal.valueOf(Double.valueOf(ricercaBuoniOrd.getBilancio().getCodice1().trim()))));
			cr.add(Restrictions.eq("fl_canc", 'N'));
			cr.add(Restrictions.eq("cd_bib", bib));
			cr.list();
*/



			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaBuoniOrd.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaBuoniOrd.getCodBibl());
			bib.setCd_polo(polo);

			if (!ricercaBuoniOrd.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}

			if (ricercaBuoniOrd.getIdBuoOrdList()!=null && ricercaBuoniOrd.getIdBuoOrdList().size()!=0 )
			{
				cr.add(Restrictions.in("id_buono_ordine",  ricercaBuoniOrd.getIdBuoOrdList()));
			}
				// n.b.  buono_ord è un char di 9

			if (ricercaBuoniOrd.getCodBibl()!=null && ricercaBuoniOrd.getCodBibl().trim().length()>0 &&  ricercaBuoniOrd.getCodPolo()!=null && ricercaBuoniOrd.getCodPolo().trim().length()>0)

			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}
			if ((ricercaBuoniOrd.getNumBuonoOrdineDa()!=null && ricercaBuoniOrd.getNumBuonoOrdineDa().trim().length()>0 &&  Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineDa().trim())>0) && (ricercaBuoniOrd.getNumBuonoOrdineA()!=null && ricercaBuoniOrd.getNumBuonoOrdineA().trim().length()>0  &&  Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineA().trim())>0))
			{
				//cr.add(Restrictions.between("buono_ord",Integer.valueOf(ricercaBuoniOrd.getNumBuonoOrdineDa()),Integer.valueOf(ricercaBuoniOrd.getNumBuonoOrdineA())));
				//cr.add(Restrictions.sqlRestriction("CAST(buono_ord as Integer) BETWEEN " + Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineDa() + " AND " + Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineA()))));
				cr.add(Restrictions.sqlRestriction("CAST({alias}.buono_ord as Integer) BETWEEN " + Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineDa().trim()) + " AND " + Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineA().trim()) ));
				//Restrictions.sqlRestriction("CAST(buono_ord as integer) BETWEEN " + Integer.valueOf(ricercaBuoniOrd.getNumBuonoOrdineDa() + " AND " + Integer.valueOf(ricercaBuoniOrd.getNumBuonoOrdineA())));

			}
			else if (ricercaBuoniOrd.getNumBuonoOrdineDa()!=null && ricercaBuoniOrd.getNumBuonoOrdineDa().trim().length()>0  &&  Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineDa().trim())>0)
			{
				//cr.add(Restrictions.ge("buono_ord",  Integer.valueOf(ricercaBuoniOrd.getNumBuonoOrdineDa())));
				cr.add(Restrictions.sqlRestriction("CAST({alias}.buono_ord as Integer) >=" + Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineDa().trim())));

			}
			else if (ricercaBuoniOrd.getNumBuonoOrdineA()!=null && ricercaBuoniOrd.getNumBuonoOrdineA().trim().length()>0  &&  Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineA().trim())>0)
			{
				//cr.add(Restrictions.le("buono_ord", Integer.valueOf(ricercaBuoniOrd.getNumBuonoOrdineA())));
				cr.add(Restrictions.sqlRestriction("CAST({alias}.buono_ord as Integer) <=" + Integer.parseInt(ricercaBuoniOrd.getNumBuonoOrdineA().trim())));
			}

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Boolean dataInizioBool=false;
			Boolean dataFineBool=false;

			if ((ricercaBuoniOrd.getDataBuonoOrdineDa()!=null && ricercaBuoniOrd.getDataBuonoOrdineDa().length()!=0) || (ricercaBuoniOrd.getDataBuonoOrdineA()!=null && ricercaBuoniOrd.getDataBuonoOrdineA().length()!=0))
			{
				//cr.add(Restrictions.between("data_ord", Integer.parseInt(ricercaOrdini.getDataOrdineDa()),Integer.parseInt(ricercaOrdini.getDataOrdineA())));
				Date startDate=new Date();
				try {
					startDate =formato.parse(ricercaBuoniOrd.getDataBuonoOrdineDa());
					dataInizioBool=true;

				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date endDate=new Date();
				try {
					endDate = formato.parse(ricercaBuoniOrd.getDataBuonoOrdineA());
					dataFineBool=true;
				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dataInizioBool && dataFineBool)
				{
					cr.add(Restrictions.between("data_buono", new Date(startDate.getTime()), new Date(endDate.getTime())));
				}
				else if (dataInizioBool)
				{
					cr.add(Restrictions.ge("data_buono", new Date(startDate.getTime())));
				}
				else if (dataFineBool)
				{
					cr.add(Restrictions.le("data_buono", new Date(endDate.getTime())));
				}

			}
			if (ricercaBuoniOrd.getFornitore()!=null && ricercaBuoniOrd.getFornitore().getDescrizione()!=null && ricercaBuoniOrd.getFornitore().getDescrizione().trim().length()!=0)
			{
				DetachedCriteria childCriteriaForn = DetachedCriteria.forClass(Tbr_fornitori.class , "child_forn");
				childCriteriaForn.setProjection(Property.forName("child_forn.cod_fornitore"));
				//childCriteriaForn.add(Property.forName("cod_fornitore.cod_fornitore").eqProperty("child_forn.cod_fornitore") );
				childCriteriaForn.add(Restrictions.eq("child_forn.nom_fornitore",ricercaBuoniOrd.getFornitore().getDescrizione().trim()).ignoreCase());
				childCriteriaForn.add(Restrictions.eq("child_forn.fl_canc", 'N'));
				cr.add(Subqueries.exists(childCriteriaForn));
				cr.add(Property.forName("cod_fornitore.cod_fornitore").in( childCriteriaForn));
			}

			if (ricercaBuoniOrd.getFornitore()!=null && ricercaBuoniOrd.getFornitore().getCodice()!=null && ricercaBuoniOrd.getFornitore().getCodice().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cod_fornitore.cod_fornitore", Integer.parseInt(ricercaBuoniOrd.getFornitore().getCodice())));
			}

			//ricercaBuoniOrd.getBilancio().setCodice3("4"); // solo per test

			if ((ricercaBuoniOrd.getBilancio()!=null &&  ricercaBuoniOrd.getBilancio().getCodice1()!=null && ricercaBuoniOrd.getBilancio().getCodice1().trim().length()!=0) || (ricercaBuoniOrd.getBilancio()!=null &&  ricercaBuoniOrd.getBilancio().getCodice2()!=null && ricercaBuoniOrd.getBilancio().getCodice2().trim().length()!=0) )
			{
				DetachedCriteria childCriteriaBil = DetachedCriteria.forClass(Tbb_capitoli_bilanci.class , "child_bil");
				childCriteriaBil.setProjection(Property.forName("child_bil.id_capitoli_bilanci" ) );
				//childCriteriaBil.add(Property.forName("cod_mat.id_capitoli_bilanci.id_capitoli_bilanci").eqProperty("child_bil.id_capitoli_bilanci"));
				if (ricercaBuoniOrd.getBilancio()!=null &&  ricercaBuoniOrd.getBilancio().getCodice1()!=null && ricercaBuoniOrd.getBilancio().getCodice1().trim().length()!=0)
				{
					childCriteriaBil.add(Restrictions.eq("child_bil.esercizio",BigDecimal.valueOf(Double.valueOf(ricercaBuoniOrd.getBilancio().getCodice1().trim()))));
				}
				if (ricercaBuoniOrd.getBilancio()!=null &&  ricercaBuoniOrd.getBilancio().getCodice2()!=null && ricercaBuoniOrd.getBilancio().getCodice2().trim().length()!=0)
				{
					childCriteriaBil.add(Restrictions.eq("child_bil.capitolo",BigDecimal.valueOf(Double.valueOf(ricercaBuoniOrd.getBilancio().getCodice2().trim()))));
				}
				childCriteriaBil.add(Restrictions.eq("child_bil.fl_canc", 'N'));
				childCriteriaBil.add(Restrictions.eq("child_bil.cd_bib", bib));
				cr.add(Subqueries.exists(childCriteriaBil));
				cr.add(Property.forName("cod_mat.id_capitoli_bilanci.id_capitoli_bilanci").in(childCriteriaBil));
			}

			if (ricercaBuoniOrd.getBilancio()!=null &&  ricercaBuoniOrd.getBilancio().getCodice3()!=null && ricercaBuoniOrd.getBilancio().getCodice3().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cod_mat.cod_mat", ricercaBuoniOrd.getBilancio().getCodice3().trim()));
			}

			//cr.add(Restrictions.eq("Tra_elementi_buono_ordine.cod_ord",5) );


			if ((ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice1()!=null && ricercaBuoniOrd.getOrdine().getCodice1().length()!=0)
					|| (ricercaBuoniOrd.getOrdine()!=null && String.valueOf(ricercaBuoniOrd.getOrdine().getCodice2()).length()!= 0)
					|| (ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice3()!=null &&  ricercaBuoniOrd.getOrdine().getCodice3().length()!=0)
				)
			{
				DetachedCriteria childCriteriaOrd = DetachedCriteria.forClass(Tra_elementi_buono_ordine.class, "child_ord");
				childCriteriaOrd.setProjection(Property.forName("child_ord.buono_ord" ) );
				childCriteriaOrd.add(Property.forName("buono_ord").eqProperty("child_ord.buono_ord"));
				if (ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice1()!=null && ricercaBuoniOrd.getOrdine().getCodice1().length()!=0)
				{
					childCriteriaOrd.add(Restrictions.eq("child_ord.cod_tip_ord",ricercaBuoniOrd.getOrdine().getCodice1().trim()));
				}
				if (ricercaBuoniOrd.getOrdine()!=null && String.valueOf(ricercaBuoniOrd.getOrdine().getCodice2()).length()!= 0)
				{
					childCriteriaOrd.add(Restrictions.eq("child_ord.anno_ord",BigDecimal.valueOf(Double.valueOf(ricercaBuoniOrd.getOrdine().getCodice2().trim()))));
				}
				if (ricercaBuoniOrd.getOrdine()!=null && ricercaBuoniOrd.getOrdine().getCodice3()!=null &&  ricercaBuoniOrd.getOrdine().getCodice3().length()!=0)
				{
					childCriteriaOrd.add(Restrictions.eq("child_ord.cod_ord",Integer.parseInt(ricercaBuoniOrd.getOrdine().getCodice3().trim())));
				}
				childCriteriaOrd.add(Restrictions.eq("child_ord.fl_canc", 'N'));
				childCriteriaOrd.add(Restrictions.eq("child_ord.cd_bib", bib));
				cr.add(Subqueries.exists(childCriteriaOrd));
				cr.add(Property.forName("buono_ord").in(childCriteriaOrd));
			}

			// ordinamento passato

			if (ricercaBuoniOrd.getOrdinamento()==null || (ricercaBuoniOrd.getOrdinamento()!=null && ricercaBuoniOrd.getOrdinamento().equals("")))
			{
				//sql=sql + " order by bor.cd_bib, bor.buono_ord, bor.data_buono ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("buono_ord"));
				cr.addOrder(Order.asc("data_buono"));
			}
			else if (ricercaBuoniOrd.getOrdinamento().equals("1"))
			{
				//sql=sql + " order by bor.cd_bib, bor.buono_ord ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("buono_ord"));

			}
			else if (ricercaBuoniOrd.getOrdinamento().equals("2"))
			{
				//sql=sql + " order by bor.cd_bib, bor.data_buono desc ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("data_buono"));
			}

			else if (ricercaBuoniOrd.getOrdinamento().equals("3"))
			{
				//sql=sql + " order by bor.cd_bib, bor.cod_fornitore ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_fornitore"));

			}

			List<Tba_buono_ordine> results = cr.list();
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

	public boolean inserisciBuonoOrdHib(BuoniOrdineVO buonoOrd)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(buonoOrd);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}
	public boolean  modificaBuonoOrdHib(BuoniOrdineVO buonoOrd)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(buonoOrd); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean cancellaBuonoOrdHib(BuoniOrdineVO buonoOrd)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(buonoOrd); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

}
