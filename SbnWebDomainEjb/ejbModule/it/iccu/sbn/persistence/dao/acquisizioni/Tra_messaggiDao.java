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
import it.iccu.sbn.ejb.vo.acquisizioni.AppoCompVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_messaggi;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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




public class Tra_messaggiDao extends DaoManager {

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


	public List<Tra_messaggi> getRicercaListaComunicazioniHib(ListaSuppComunicazioneVO ricercaComunicazioni) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tra_messaggi.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaComunicazioni.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaComunicazioni.getCodBibl());
			bib.setCd_polo(polo);

			//cr.add(Restrictions.eq("cd_bib", bib));
			if (ricercaComunicazioni.getCodBibl()!=null && ricercaComunicazioni.getCodBibl().trim().length()>0 &&  ricercaComunicazioni.getCodPolo()!=null && ricercaComunicazioni.getCodPolo().trim().length()>0)
			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}

			if (!ricercaComunicazioni.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}

			if (ricercaComunicazioni.getTipoMessaggio() !=null &&  ricercaComunicazioni.getTipoMessaggio().length()!=0)
			{
				cr.add(Restrictions.eq("tipo_msg", ricercaComunicazioni.getTipoMessaggio()));
			}

			if (ricercaComunicazioni.getCodiceMessaggio()!=null && ricercaComunicazioni.getCodiceMessaggio().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cod_msg", Integer.parseInt(ricercaComunicazioni.getCodiceMessaggio().trim())));
			}

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Boolean dataInizioBool=false;
			Boolean dataFineBool=false;

			if ((ricercaComunicazioni.getDataComunicazioneDa()!=null && ricercaComunicazioni.getDataComunicazioneDa().length()!=0) || (ricercaComunicazioni.getDataComunicazioneA()!=null && ricercaComunicazioni.getDataComunicazioneA().length()!=0))
			{
				//cr.add(Restrictions.between("data_ord", Integer.parseInt(ricercaOrdini.getDataOrdineDa()),Integer.parseInt(ricercaOrdini.getDataOrdineA())));
				Date startDate=new Date();
				try {
					startDate =formato.parse(ricercaComunicazioni.getDataComunicazioneDa());
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
					endDate = formato.parse(ricercaComunicazioni.getDataComunicazioneA());
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
					cr.add(Restrictions.between("data_msg", new Date(startDate.getTime()), new Date(endDate.getTime())));
				}
				else if (dataInizioBool)
				{
					cr.add(Restrictions.ge("data_msg", new Date(startDate.getTime())));
				}
				else if (dataFineBool)
				{
					cr.add(Restrictions.le("data_msg", new Date(endDate.getTime())));
				}

			}

			if (ricercaComunicazioni.getTipoInvioComunicazione()!=null && ricercaComunicazioni.getTipoInvioComunicazione().length()!=0)
			{
				cr.add(Restrictions.eq("tipo_invio", ricercaComunicazioni.getTipoInvioComunicazione()));
			}

			if (ricercaComunicazioni.getFornitore().getDescrizione()!=null && ricercaComunicazioni.getFornitore().getDescrizione().length()!=0 )
			{
				DetachedCriteria childCriteriaForn = DetachedCriteria.forClass(Tbr_fornitori.class , "child_forn");
				childCriteriaForn.setProjection(Property.forName("child_forn.cod_fornitore"));
				//childCriteriaForn.add(Property.forName("cod_fornitore.cod_fornitore").eqProperty("child_forn.cod_fornitore") );
				childCriteriaForn.add(Restrictions.eq("child_forn.nom_fornitore",ricercaComunicazioni.getFornitore().getDescrizione().trim()).ignoreCase());
				childCriteriaForn.add(Restrictions.eq("child_forn.fl_canc", 'N'));
				cr.add(Subqueries.exists(childCriteriaForn));
				cr.add(Property.forName("cod_fornitore.cod_fornitore").in( childCriteriaForn));
			}

			if (ricercaComunicazioni.getFornitore().getCodice()!=null && ricercaComunicazioni.getFornitore().getCodice().length()!=0 )
			{
				cr.add(Restrictions.eq("cod_fornitore.cod_fornitore",  Integer.parseInt(ricercaComunicazioni.getFornitore().getCodice())));
			}
			if (ricercaComunicazioni.getTipoDocumento()!=null && ricercaComunicazioni.getTipoDocumento().equals("O"))
			{
				//cr.add(Restrictions.eq("anno_fattura", bd));
				cr.add(Restrictions.eq("progr_fattura", 0));

				// tipo ordine
				if (ricercaComunicazioni.getIdDocumento().getCodice1()!=null && ricercaComunicazioni.getIdDocumento().getCodice1()!=null && ricercaComunicazioni.getIdDocumento().getCodice1().length()!=0)
				{
					cr.add(Restrictions.eq("cod_tip_ord", ricercaComunicazioni.getIdDocumento().getCodice1()));
				}
				// anno ord
				if (ricercaComunicazioni.getIdDocumento().getCodice2()!=null && ricercaComunicazioni.getIdDocumento().getCodice2()!=null && ricercaComunicazioni.getIdDocumento().getCodice2().length()!=0)
				{
					cr.add(Restrictions.eq("anno_ord", BigDecimal.valueOf(Double.valueOf(ricercaComunicazioni.getIdDocumento().getCodice2()))));
				}
				// codice ordine
				if (ricercaComunicazioni.getIdDocumento().getCodice3()!=null && ricercaComunicazioni.getIdDocumento().getCodice3()!=null && ricercaComunicazioni.getIdDocumento().getCodice3().length()!=0)
				{
					cr.add(Restrictions.eq("cod_ord", Integer.parseInt(ricercaComunicazioni.getIdDocumento().getCodice3())));
				}
			}
			if (ricercaComunicazioni.getTipoDocumento()!=null && ricercaComunicazioni.getTipoDocumento().equals("F") )
			{
				//cr.add(Restrictions.eq("anno_ord", 0));
				cr.add(Restrictions.eq("cod_ord", 0));

				// anno fatt
				if (ricercaComunicazioni.getIdDocumento().getCodice2()!=null && ricercaComunicazioni.getIdDocumento().getCodice2()!=null && ricercaComunicazioni.getIdDocumento().getCodice2().length()!=0)
				{
					cr.add(Restrictions.eq("anno_fattura", BigDecimal.valueOf(Double.valueOf(ricercaComunicazioni.getIdDocumento().getCodice2()))));
				}
				// codice ordine
				if (ricercaComunicazioni.getIdDocumento().getCodice3()!=null && ricercaComunicazioni.getIdDocumento().getCodice3()!=null && ricercaComunicazioni.getIdDocumento().getCodice3().length()!=0)
				{
					cr.add(Restrictions.eq("progr_fattura", Integer.parseInt(ricercaComunicazioni.getIdDocumento().getCodice3())));
				}
			}
			if (ricercaComunicazioni.getStatoComunicazione()!=null && ricercaComunicazioni.getStatoComunicazione().length()!=0)
			{
				cr.add(Restrictions.eq("stato_msg", ricercaComunicazioni.getStatoComunicazione()));
			}

			if (ricercaComunicazioni.getDirezioneComunicazione()!=null && ricercaComunicazioni.getDirezioneComunicazione().trim().length()!=0 )
			{
				if (ricercaComunicazioni.getDirezioneComunicazione().trim().equals("A") )
				{
					cr.add(Restrictions.ne("stato_msg", '1' ));
				}

				if (ricercaComunicazioni.getDirezioneComunicazione().trim().equals("D") )
				{
					cr.add(Restrictions.eq("stato_msg", '1' ));
				}
			}

			// ordinamento passato
			if (ricercaComunicazioni.getOrdinamento()==null || ( ricercaComunicazioni.getOrdinamento()!=null && ricercaComunicazioni.getOrdinamento().equals("")))
			{
				//sql=sql + " order by messgg.data_msg desc ";
				cr.addOrder(Order.desc("data_msg"));
			}
			else if (ricercaComunicazioni.getOrdinamento().equals("1"))
			{
				//sql=sql + " order by messgg.data_msg desc  ";
				cr.addOrder(Order.desc("data_msg"));
			}
			else if (ricercaComunicazioni.getOrdinamento().equals("2"))
			{
				//sql=sql + " order by messgg.data_msg ";
				cr.addOrder(Order.asc("data_msg"));
			}
			else if (ricercaComunicazioni.getOrdinamento().equals("3"))
			{
				//sql=sql + " order by lower(forn.nom_fornitore)  ";
			}
			else if (ricercaComunicazioni.getOrdinamento().equals("4"))
			{
				//sql=sql + " order by lower(codi.ds_tabella)  ";
			}
			else if (ricercaComunicazioni.getOrdinamento().equals("5"))
			{
				//sql=sql + " order by  messgg.anno_ord "; //messgg.cod_tip_ord
				cr.addOrder(Order.asc("anno_ord"));
			}
			else if (ricercaComunicazioni.getOrdinamento().equals("6"))
			{
				//sql=sql + " order by messgg.cod_ord  ";
				cr.addOrder(Order.asc("cod_ord"));
			}

			List<Tra_messaggi> results = cr.list();

			if (results!=null && results.size()>0 && ricercaComunicazioni.getOrdinamento()!=null && ricercaComunicazioni.getOrdinamento().equals("3"))
			{
				Comparator comp=null;
				comp =new FornComparator();
				Collections.sort(results, comp);
			}
			if (results!=null && results.size()>0 && ricercaComunicazioni.getOrdinamento()!=null && ricercaComunicazioni.getOrdinamento().equals("4"))
			{

				List<AppoCompVO> appoResults=new ArrayList<AppoCompVO> ();
				for (int x=0; x< results.size(); x++)
				{
					AppoCompVO eleAppoComp=new AppoCompVO();
					eleAppoComp.setEle1(results.get(x));
					String kCdMsg=String.valueOf(results.get(x).getCod_msg());
					if (kCdMsg!=null && kCdMsg.length()>0)
				 	{
				 		if (kCdMsg.length()<2)
				 		{
				 			// aggiunta di zeri iniziali
				 			String cbibl="00" + kCdMsg;
				 			int posizStart = cbibl.length()-2;
				 			cbibl=cbibl.substring(posizStart,posizStart+2);
				 			//String cbibl=kbibl.substring(kbibl.length()-3, kbibl.length());
				 			kCdMsg=cbibl;
				 		}
				 	}
					eleAppoComp.setEle2(this.getCodiciDaTbCodiciMsg("ATME", kCdMsg));
					appoResults.add(eleAppoComp);
				}

				Comparator comp=null;
				comp =new DesMsgComparator();
				Collections.sort(appoResults, comp);

				List<Tra_messaggi> resultsNew = new ArrayList<Tra_messaggi>();
				for (int y=0; y< appoResults.size(); y++)
				{
					resultsNew.add(appoResults.get(y).getEle1());
				}
				results=resultsNew;
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

	private static class FornComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((Tra_messaggi) o1).getCod_fornitore().getNom_fornitore().toLowerCase();
				String e2 = ((Tra_messaggi) o2).getCod_fornitore().getNom_fornitore().toLowerCase();

				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	private static   class DesMsgComparator implements Comparator {
		public int compare(Object o1, Object o2 ) {
			try {
				String e1 = ((AppoCompVO) o1).getEle2();
				String e2 = ((AppoCompVO) o2).getEle2();
				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	private String  getCodiciDaTbCodiciMsg(String tp_tabella, String cd_tabella)
	throws  DaoManagerException{
		String ds_tabella_str ="";
		try{
			Session session = getCurrentSession();
			Criteria crCod = session.createCriteria(Tb_codici.class, "c");
			crCod.add(Restrictions.eq("c.tp_tabella",tp_tabella));
			crCod.add(Restrictions.eq("c.cd_tabella",cd_tabella.trim() ));
			crCod.setProjection(Projections.projectionList()
					.add(Projections.property("c.ds_tabella"))
			);
			List<String> denoresults = crCod.list();
			if (denoresults!=null && denoresults.size()>0)
			{
				ds_tabella_str=denoresults.get(0).trim();
			}

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return ds_tabella_str;
	}

	public boolean inserisciComunicazioneHib(ComunicazioneVO comunicazione)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(comunicazione);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}
	public boolean modificaComunicazioneHib(ComunicazioneVO comunicazione)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(comunicazione); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean cancellaComunicazioneHib(ComunicazioneVO comunicazione)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(comunicazione); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public Tra_messaggi getComunicazione(String codPolo, String codBib,	int cod_msg)
	throws  DaoManagerException{
		try{
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tra_messaggi.class);
			c.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));
			c.add(Restrictions.eq("cod_msg", cod_msg));
			return (Tra_messaggi) c.uniqueResult();

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}



}
