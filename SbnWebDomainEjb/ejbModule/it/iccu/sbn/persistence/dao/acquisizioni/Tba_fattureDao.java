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
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_fatture;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_ordini;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_righe_fatture;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;




public class Tba_fattureDao extends DaoManager {

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


	public List<Tba_fatture> getRicercaListaFattureHib(ListaSuppFatturaVO ricercaFatture) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Tbf_polo polo = new Tbf_polo();
			if (ricercaFatture.getCodPolo()!=null)
			{
				polo.setCd_polo(ricercaFatture.getCodPolo());
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (ricercaFatture.getCodBibl()!=null)
			{
				bib.setCd_biblioteca(ricercaFatture.getCodBibl());
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}

			Criteria cr = session.createCriteria(Tba_fatture.class, "fatt");


/*			Tbf_polo polo = new Tbf_polo();
			if (ricercaFatture.getCodPolo()!=null)
			{
				polo.setCd_polo(ricercaFatture.getCodPolo());
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (ricercaFatture.getCodBibl()!=null)
			{
				bib.setCd_biblioteca(ricercaFatture.getCodBibl());
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}
*/
			if (!ricercaFatture.isFlag_canc())
			{
				cr.add(Restrictions.eq("fatt.fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("fatt.fl_canc", 'S'));
			}
			if (ricercaFatture.getCodBibl()!=null)
			{
				cr.add(Restrictions.eq("fatt.cd_bib", bib));
			}

			if (ricercaFatture.getAnnoFattura()!=null && ricercaFatture.getAnnoFattura().trim().length()!=0)
			{
				cr.add(Restrictions.eq("fatt.anno_fattura",  BigDecimal.valueOf(Double.valueOf(ricercaFatture.getAnnoFattura().trim()))));
			}

			if (ricercaFatture.getProgrFattura()!=null && ricercaFatture.getProgrFattura().length()!=0)
			{
				cr.add(Restrictions.eq("fatt.progr_fattura", Integer.parseInt(ricercaFatture.getProgrFattura())));
			}
			//ricercaFatture.setDataFattura("18/09/2008"); solo per test
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			if (ricercaFatture.getDataFattura()!=null && ricercaFatture.getDataFattura().length()!=0)
			{
				Date dataFattConv=new Date();
				try {
					dataFattConv=formato.parse(ricercaFatture.getDataFattura());
					cr.add(Restrictions.eq("fatt.data_fattura",dataFattConv));
				} catch (ParseException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//sql=sql + " fatt.data_fattura = TO_DATE('" +  ricercaFatture.getDataFattura() + "','dd/MM/yyyy')";
			}

			Boolean dataInizioBool=false;
			Boolean dataFineBool=false;
			if ((ricercaFatture.getDataFatturaDa()!=null && ricercaFatture.getDataFatturaDa().length()!=0) || (ricercaFatture.getDataFatturaA()!=null && ricercaFatture.getDataFatturaA().length()!=0))
			{
				Date startDate=new Date();
				try {
					startDate =formato.parse(ricercaFatture.getDataFatturaDa());
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
					endDate = formato.parse(ricercaFatture.getDataFatturaA());
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
					cr.add(Restrictions.between("fatt.data_fattura", new Date(startDate.getTime()), new Date(endDate.getTime())));
				}
				else if (dataInizioBool)
				{
					cr.add(Restrictions.ge("fatt.data_fattura", new Date(startDate.getTime())));
				}
				else if (dataFineBool)
				{
					cr.add(Restrictions.le("fatt.data_fattura", new Date(endDate.getTime())));
				}

				//cr.add(Restrictions.between("fatt.data_fattura", Integer.parseInt(ricercaFatture.getDataFatturaDa()),Integer.parseInt(ricercaFatture.getDataFatturaA())));
			}

			if (ricercaFatture.getStatoFattura()!=null && ricercaFatture.getStatoFattura().length()!=0)
			{
				cr.add(Restrictions.eq("fatt.stato_fattura", ricercaFatture.getStatoFattura()));
			}
			if (ricercaFatture.getTipoFattura()!=null && ricercaFatture.getTipoFattura().length()!=0)
			{
				cr.add(Restrictions.eq("fatt.tipo_fattura", ricercaFatture.getTipoFattura()));
			}

			if (ricercaFatture.getFornitore()!=null && ricercaFatture.getFornitore().getCodice()!=null && ricercaFatture.getFornitore().getCodice().trim().length()!=0)
			{
				cr.add(Restrictions.eq("fatt.cod_fornitore.cod_fornitore", Integer.valueOf(ricercaFatture.getFornitore().getCodice().trim())));
			}

			if (ricercaFatture.getFornitore()!=null && ricercaFatture.getFornitore().getDescrizione()!=null && ricercaFatture.getFornitore().getDescrizione().trim().length()!=0)
			{
				DetachedCriteria childCriteriaForn = DetachedCriteria.forClass(Tbr_fornitori.class , "child_forn");
				childCriteriaForn.setProjection(Property.forName("child_forn.cod_fornitore"));
				//childCriteriaForn.add(Property.forName("cod_fornitore.cod_fornitore").eqProperty("child_forn.cod_fornitore") );
				childCriteriaForn.add(Restrictions.eq("child_forn.nom_fornitore",ricercaFatture.getFornitore().getDescrizione().trim()).ignoreCase());
				childCriteriaForn.add(Restrictions.eq("child_forn.fl_canc", 'N'));
				cr.add(Subqueries.exists(childCriteriaForn));
				cr.add(Property.forName("cod_fornitore.cod_fornitore").in( childCriteriaForn));
			}



			if (ricercaFatture.getNumFattura()!=null && ricercaFatture.getNumFattura().length()!=0)
			{
				cr.add(Restrictions.eq("fatt.num_fattura", ricercaFatture.getNumFattura()));
			}
			if (ricercaFatture.getIDFatt()>0 )
			{
				cr.add(Restrictions.eq("fatt.id_fattura", ricercaFatture.getIDFatt()));
			}
			//ricercaFatture.setIDFattNC(1); // solo per test
			if ((ricercaFatture.getIDFattNC()>0) || (ricercaFatture.getOrdine()!=null && ((ricercaFatture.getOrdine().getCodice1()!=null && ricercaFatture.getOrdine().getCodice1().trim().length()>0) || (ricercaFatture.getOrdine().getCodice2()!=null && ricercaFatture.getOrdine().getCodice2().trim().length()>0) || (ricercaFatture.getOrdine().getCodice3()!=null && ricercaFatture.getOrdine().getCodice3().trim().length()>0)) ) )
			{
				DetachedCriteria childCriteriaRigheFatt = DetachedCriteria.forClass(Tba_righe_fatture.class , "child_righeFatt");
				childCriteriaRigheFatt.setProjection(Property.forName("child_righeFatt.id_fattura" ) );
				childCriteriaRigheFatt.add(Property.forName("fatt.id_fattura").eqProperty("child_righeFatt.id_fattura") );
				if (ricercaFatture.getIDFattNC()>0 )
				{
					childCriteriaRigheFatt.add(Restrictions.eq("child_righeFatt.id_fattura_in_credito",ricercaFatture.getIDFattNC()));
				}
				childCriteriaRigheFatt.add(Restrictions.eq("child_righeFatt.fl_canc", 'N'));
				childCriteriaRigheFatt.add(Restrictions.eq("child_righeFatt.cd_biblioteca", bib));
				if (ricercaFatture.getOrdine()!=null && ((ricercaFatture.getOrdine().getCodice1()!=null && ricercaFatture.getOrdine().getCodice1().trim().length()>0) || (ricercaFatture.getOrdine().getCodice2()!=null && ricercaFatture.getOrdine().getCodice2().trim().length()>0) || (ricercaFatture.getOrdine().getCodice3()!=null && ricercaFatture.getOrdine().getCodice3().trim().length()>0)) )
				{
					DetachedCriteria childCriteriaOrd = DetachedCriteria.forClass(Tba_ordini.class , "child_ord");
					childCriteriaOrd.setProjection(Property.forName("child_ord.id_ordine" ) );
					//childCriteriaOrd.add(Property.forName("child_ord.id_ordine").eqProperty("child_righeFatt.id_ordine.id_ordine") );
					if	(ricercaFatture.getOrdine().getCodice1()!=null && ricercaFatture.getOrdine().getCodice1().trim().length()>0)
					{
						childCriteriaOrd.add(Restrictions.eq("child_ord.cod_tip_ord",ricercaFatture.getOrdine().getCodice1().trim().charAt(0)));
					}
					if	(ricercaFatture.getOrdine().getCodice2()!=null && ricercaFatture.getOrdine().getCodice2().trim().length()>0)
					{
						childCriteriaOrd.add(Restrictions.eq("child_ord.anno_ord",BigDecimal.valueOf(Integer.parseInt(ricercaFatture.getOrdine().getCodice2().trim()))));
					}
					if	(ricercaFatture.getOrdine().getCodice3()!=null && ricercaFatture.getOrdine().getCodice3().trim().length()>0)
					{
						childCriteriaOrd.add(Restrictions.eq("child_ord.cod_ord",Integer.parseInt(ricercaFatture.getOrdine().getCodice3().trim())));
					}
					childCriteriaOrd.add(Restrictions.eq("child_ord.fl_canc", 'N'));
					childCriteriaOrd.add(Restrictions.eq("child_ord.cd_bib", bib));
					childCriteriaOrd.add(Restrictions.ne("child_ord.stato_ordine",'N'));
					childCriteriaRigheFatt.add(Subqueries.exists(childCriteriaOrd));
					childCriteriaRigheFatt.add(Property.forName("child_righeFatt.id_ordine.id_ordine").in( childCriteriaOrd));
				}


				cr.add(Subqueries.exists(childCriteriaRigheFatt));
				cr.add(Property.forName("fatt.id_fattura").in( childCriteriaRigheFatt));
			}
/*			if (ricercaFatture.getOrdine()!=null && ((ricercaFatture.getOrdine().getCodice1()!=null && ricercaFatture.getOrdine().getCodice1().trim().length()>0) || (ricercaFatture.getOrdine().getCodice2()!=null && ricercaFatture.getOrdine().getCodice2().trim().length()>0) || (ricercaFatture.getOrdine().getCodice3()!=null && ricercaFatture.getOrdine().getCodice3().trim().length()>0)) )
				{
				DetachedCriteria childCriteriaRigheFatt = DetachedCriteria.forClass(Tba_righe_fatture.class , "child_righeFatt");
				childCriteriaRigheFatt.setProjection(Property.forName("child_righeFatt.id_fattura" ) );
				childCriteriaRigheFatt.add(Property.forName("fatt.id_fattura").eqProperty("child_righeFatt.id_fattura") );

				if	(ricercaFatture.getOrdine().getCodice1()!=null && ricercaFatture.getOrdine().getCodice1().trim().length()>0)
				{
					childCriteriaRigheFatt.add(Restrictions.eq("child_righeFatt.id_ordine.cod_tip_ord",ricercaFatture.getOrdine().getCodice1().trim().charAt(0)));
				}
				if	(ricercaFatture.getOrdine().getCodice2()!=null && ricercaFatture.getOrdine().getCodice2().trim().length()>0)
				{
					childCriteriaRigheFatt.add(Restrictions.eq("child_righeFatt.id_ordine.anno_ord",BigDecimal.valueOf(Integer.parseInt(ricercaFatture.getOrdine().getCodice2().trim()))));
				}
				if	(ricercaFatture.getOrdine().getCodice3()!=null && ricercaFatture.getOrdine().getCodice3().trim().length()>0)
				{
					childCriteriaRigheFatt.add(Restrictions.eq("child_righeFatt.id_ordine.cod_ord",Integer.parseInt(ricercaFatture.getOrdine().getCodice3().trim())));
				}
				childCriteriaRigheFatt.add(Restrictions.eq("child_righeFatt.fl_canc", 'N'));
				childCriteriaRigheFatt.add(Restrictions.eq("child_righeFatt.cd_biblioteca", bib));
				cr.add(Subqueries.exists(childCriteriaRigheFatt));
				cr.add(Property.forName("fatt.id_fattura").in( childCriteriaRigheFatt));
			}*/


			// ordinamento passato
			if (ricercaFatture.getOrdinamento()==null || ( ricercaFatture.getOrdinamento()!=null && ricercaFatture.getOrdinamento().equals("")))
			{
				//sql=sql + " order by fatt.cd_bib, fatt.anno_fattura, fatt.progr_fattura  ";
				cr.addOrder(Order.asc("fatt.cd_bib"));
				cr.addOrder(Order.asc("fatt.anno_fattura"));
				cr.addOrder(Order.asc("fatt.progr_fattura"));

			}
			else if (ricercaFatture.getOrdinamento().equals("1"))
			{
				//sql=sql + " order by fatt.cd_bib, fatt.anno_fattura, fatt.num_fattura ";
				cr.addOrder(Order.asc("fatt.cd_bib"));
				cr.addOrder(Order.asc("fatt.anno_fattura"));
				cr.addOrder(Order.asc("fatt.num_fattura"));

			}
			else if (ricercaFatture.getOrdinamento().equals("2"))
			{
				//sql=sql + " order by fatt.cd_bib, fatt.data_fattura desc  ";
				cr.addOrder(Order.asc("fatt.cd_bib"));
				cr.addOrder(Order.desc("fatt.data_fattura"));

			}
			else if (ricercaFatture.getOrdinamento().equals("3"))
			{
				//sql=sql + " order by fatt.cd_bib, fatt.stato_fattura ";
				cr.addOrder(Order.asc("fatt.cd_bib"));
				cr.addOrder(Order.asc("fatt.stato_fattura"));

			}
			else if (ricercaFatture.getOrdinamento().equals("4"))
			{
				//sql=sql + " order by fatt.cd_bib, lower(forn.nom_fornitore) ";
				cr.addOrder(Order.asc("fatt.cd_bib"));
				//cr.addOrder(Order.asc("fatt.cod_fornitore.nom_fornitore").ignoreCase());
			}

			List<Tba_fatture> results = cr.list();

			if (results!=null && results.size()>0 && ricercaFatture.getOrdinamento()!=null &&  ricercaFatture.getOrdinamento().equals("4"))
			{
				Comparator comp=null;
				comp =new FattComparator();
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

	public boolean inserisciFatturaHib(FatturaVO fattura)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(fattura);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}
	public boolean modificaFatturaHib(FatturaVO fattura)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(fattura); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean cancellaFatturaHib(FatturaVO fattura)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(fattura); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	private static class FattComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((Tba_fatture) o1).getCod_fornitore().getNom_fornitore().toLowerCase();
				String e2 = ((Tba_fatture) o2).getCod_fornitore().getNom_fornitore().toLowerCase();

				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	public int countRigheFatturaOrdine(int idOrdine) throws DaoManagerException {
		try {
			Session session = getCurrentSession();
			Criteria c = session.createCriteria(Tba_righe_fatture.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.eq("id_ordine.id", idOrdine));
			c.setProjection(Projections.rowCount());

			Number cnt = (Number) c.uniqueResult();
			return cnt.intValue();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

}
