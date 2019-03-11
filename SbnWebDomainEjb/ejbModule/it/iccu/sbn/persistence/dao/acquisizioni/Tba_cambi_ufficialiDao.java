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
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_cambi_ufficiali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Tba_cambi_ufficialiDao extends DaoManager {

	public static final Date VALUTA_RIF_DATE = DateUtil.toDate("31/12/9999");
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

	public List<CambioVO> getRicercaListaCambiDaCancHib (ListaSuppCambioVO ricercaCambi) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_cambi_ufficiali.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaCambi.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaCambi.getCodBibl());
			bib.setCd_polo(polo);

			if (ricercaCambi.getCodValuta()!=null && ricercaCambi.getCodValuta().trim().length()!=0)
			{
				cr.add(Restrictions.eq("valuta", ricercaCambi.getCodValuta().trim()));
			}
			cr.add(Restrictions.ne("fl_canc", 'S'));
			//cr.add(Restrictions.eq("cd_bib", bib));
			if (ricercaCambi.getCodBibl()!=null && ricercaCambi.getCodBibl().trim().length()>0 &&  ricercaCambi.getCodPolo()!=null && ricercaCambi.getCodPolo().trim().length()>0)

			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}
			// presenza  di legami con ordini in qualsiasi stato
			cr.add(Restrictions.isNotEmpty("Tba_ordini"));

			List<CambioVO> results = cr.list();

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


	public List getRicercaListaCambiHib(ListaSuppCambioVO ricercaCambi) throws DaoManagerException {
		List results = null;

		try {
			Session session = this.getCurrentSession();
			Tbf_biblioteca_in_polo bib = creaIdBib(ricercaCambi.getCodPolo(), ricercaCambi.getCodBibl());

			String sqlString="select v, c.ds_tabella from Tba_cambi_ufficiali v,  Tb_codici c ";
			sqlString=sqlString +" where v.cd_bib=('"+ bib.getCd_biblioteca() + "', '" + bib.getCd_polo() +"')"  ; // tipologia Tbf_biblioteca_in_polo
			if (!ricercaCambi.isFlag_canc())
			{
				sqlString=sqlString +" and v.fl_canc='N' ";
			}
			sqlString=sqlString +" and  c.tp_tabella='AVAL' and c.cd_tabella=v.valuta";
			if (ricercaCambi.getCodValuta()!=null && ricercaCambi.getCodValuta().trim().length()!=0)
			{
				//sqlString=sqlString + " and v.valuta=:valu ";
				sqlString=sqlString + " and v.valuta='" + ricercaCambi.getCodValuta().trim() + "'";
			}
        	if (ricercaCambi.getDesValuta()!=null && ricercaCambi.getDesValuta().trim().length()!=0)
			{
				sqlString=sqlString + " and c.ds_tabella='" + ricercaCambi.getDesValuta().trim().toUpperCase() + "'";
			}
        	if (ricercaCambi.isEsisteRiferimento())
			{
				sqlString=sqlString + " and v.data_var=TO_DATE('31/12/9999','dd/MM/yyyy') ";
			}

			if (ricercaCambi.getOrdinamento()==null || (ricercaCambi.getOrdinamento()!=null && ricercaCambi.getOrdinamento().equals("")))
			{
				sqlString=sqlString + " order by  v.valuta";
			}
			else if (ricercaCambi.getOrdinamento().equals("1"))
			{
				sqlString=sqlString + " order by  v.valuta";
			}
			else if (ricercaCambi.getOrdinamento().equals("2"))
			{
				sqlString=sqlString + " order by  c.ds_tabella";
			}

			Query query = session.createQuery(sqlString);
			//query3.setEntity("bob", bib);		// tipologia Tbf_biblioteca_in_polo
			results  =  query.list();

			// ok la sottostante
/*
 			sqlString="select new list (t.bid, t.isbd, n.numero_std) from Tb_titolo t, Tb_numero_std  n ";
			sqlString=sqlString +" where t=n.b " 	  ; // tipologia Tbf_biblioteca_in_polo
			sqlString=sqlString +" and t.bid='ANA0019028' ";

			//sqlString=sqlString + " order by  v.valuta";
			Query query4 = session.createQuery(sqlString);
			List results5  =  (List) query4.list();
*/


			if (results==null || (results!=null && results.size()==0))
			{
				results=null;
			}

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	return results;

	}

/*	public List getTitoloOrdine(String bidPassato) throws DaoManagerException {
		List results = null;

		try {
			Session session = this.getCurrentSession();

			//String sqlString="select t, n.numero_std from Tb_titolo t,Tb_numero_std  n  join  n.b as prova "; //ok
			String sqlString="select t, n.numero_std from Tb_titolo t,Tb_numero_std  n   "; //ok
			//String sqlString="select t, (CASE WHEN  (n.numero_std is null) THEN ' '  else  n.numero_std END) from Tb_titolo t,Tb_numero_std  n left join  n.b as prova "; //ok2
			//String sqlString="select t, (CASE WHEN  (prova is null) THEN  ' '  else  n.numero_std END)  from Tb_titolo t,Tb_numero_std  n left join  n.b as prova "; //ok3
			//String sqlString="select (CASE WHEN  (prova is null) THEN  ' '  else  t END) , (CASE WHEN  (prova is null) THEN  ' '  else  n.numero_std END)  from Tb_titolo t,Tb_numero_std  n  join  n.b as prova ";
			//sqlString=sqlString +" where  prova=t " 	  ; // ok
			sqlString=sqlString +" where  n.b.bid=t.bid " 	  ; // ok la property b  è definita nel mapping
			sqlString=sqlString +" and t.fl_canc<>'S'"; //ok
			sqlString=sqlString +" and n.fl_canc<>'S'"; //ok
			sqlString=sqlString +" and n.tp_numero_std in ('I','J','M','L','E','G')"; //ok

			//bidPassato="BVE0036760"; // RAV0028330 CFI0232619 CFI0117938(ha 3 numeri standard)

			if (bidPassato!=null)
			{
				//sqlString=sqlString +" where  n.b=t ";
				sqlString=sqlString +" and t.bid='"+ bidPassato +"'"; //ok

			}
			//sqlString=sqlString + " order by  v.valuta";

			Query query = session.createQuery(sqlString);
			results  =  (List) query.list();

			if (results!=null && results.size()>1)
			{
				List results2=new ArrayList();
				results2.add(results.get(0)); // considero solo il primo
				results=results2;
			}
			// se non ci sono risultati della join cerco il titolo e creo l'oggetto con il numerostandard vuoto
			if ( results.size()==0 )
			{
				//String sqlString2="select  t, ' '  from Tb_titolo t ";
				String sqlString2="select  t,''  from Tb_titolo t ";
				sqlString2=sqlString2 +" where t.bid='"+ bidPassato +"'"; //ok
				sqlString2=sqlString2 +" and t.fl_canc<>'S'"; //ok
				Query query2 = session.createQuery(sqlString2);
				results  =  (List) query2.list();


			}
			//results  =  this.getOrdiniBid();
			//results  =  this.getOrdiniBid();

			if (results==null || (results!=null && results.size()==0) )
			{
				results=null;
			}

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	return results;

	}*/

	public List getTitoloOrdine(String bidPassato) throws DaoManagerException {
		List results = null;
		TitoloACQVO recTit=null;

		try {
			Session session = this.getCurrentSession();
			// il numero standard serve per la stampa del buono d’ordine (campo isbn/issn)
			String sqlString="select t, n.numero_std from Tb_titolo t,Tb_numero_std  n   "; //ok
			sqlString=sqlString +" where  n.b.bid=t.bid " 	  ; // ok la property b  è definita nel mapping
			sqlString=sqlString +" and t.fl_canc<>'S'"; //ok
			sqlString=sqlString +" and n.fl_canc<>'S'"; //ok
			sqlString=sqlString +" and n.tp_numero_std in ('I','J','M','L','E','G')"; //ok

			//System.out.println("ciaooooooooooooooooooooooooooooo1");

			//bidPassato="BVE0036760"; // RAV0028330 CFI0232619 CFI0117938(ha 3 numeri standard)

			if (bidPassato!=null)
			{
				//sqlString=sqlString +" where  n.b=t ";
				sqlString=sqlString +" and t.bid='"+ bidPassato +"'"; //ok

			}
			//sqlString=sqlString + " order by  v.valuta";

			Query query = session.createQuery(sqlString);
			results  =  query.list();

			if (results!=null && results.size()>1)
			{
				List results2=new ArrayList();
				results2.add(results.get(0)); // considero solo il primo
				results=results2;
			}
			// se non ci sono risultati della join cerco il titolo e creo l'oggetto con il numerostandard vuoto
			if ( results.size()==0 )
			{
				//String sqlString2="select  t, ' '  from Tb_titolo t ";
				String sqlString2="select  t,''  from Tb_titolo t ";
				sqlString2=sqlString2 +" where t.bid='"+ bidPassato +"'"; //ok
				sqlString2=sqlString2 +" and t.fl_canc<>'S'"; //ok
				Query query2 = session.createQuery(sqlString2);
				results  =  query2.list();


			}
			//results  =  this.getOrdiniBid();
			//results  =  this.getOrdiniBid();

			if (results==null || (results!=null && results.size()==0) )
			{
				results=null;
			}
			// inizio innesto
			List lista = new ArrayList(); // output di un singolo titolo ricavato
			String[] arrLingua=new String[3];
			//TitoloACQVO recTit=null;
			int codRet=0;
			String tit_isbd="";

			if (results!=null && results.size()>0)
			{
				recTit = new TitoloACQVO();

				Object[] arrivo =null;
				arrivo = (Object[]) results.get(0);
				Tb_titolo tit= (Tb_titolo) arrivo[0];
				recTit.setNumStandard((String) arrivo[1]); //  numero standard

				//Tb_titolo tit= (Tb_titolo)risposta.get(0);

				tit_isbd=tit.getIsbd();
				recTit.setIsbd(tit.getIsbd());
				if  (!tit_isbd.equals("") &&  tit_isbd.length()>200)
				{
					// gestione composizione titolo
					String indice_isbd=tit.getIndice_isbd();

					String[] isbd_composto=tit_isbd.split("\\. -");
					// se si splitta (lunghe>0) allora considero la prima parte, altrimenti tutto
					String tit_primaParte=isbd_composto[0];
					String tit_secondaParte=isbd_composto[1];
					String[] indice_isbd_composto=indice_isbd.split("\\;");
					String  tit_secondaParte_finale="";
					String  tit_primaParte_finale="";
					String  tit_isbd_finale="";

					if (indice_isbd_composto.length>0 && indice_isbd_composto.length>=1)
					{
						//for (int y = 0; y < indice_isbd_composto.length; y++)
						//{
						if (indice_isbd_composto[0]!=null && indice_isbd_composto[0].length()!=0)
						{
							String primaParte=indice_isbd_composto[0];
							String[] primaParte_composto=primaParte.split("-");
							String pos_primaParte=primaParte_composto[1];

							if (tit_primaParte.length()>100)
							{
								tit_primaParte_finale=tit_primaParte.substring(0,100);
							}
							else
							{
								tit_primaParte_finale=tit_primaParte;
							}
						}
						if (indice_isbd_composto[1]!=null && indice_isbd_composto[1].length()!=0)
						{
							String secondaParte=indice_isbd_composto[1];
							String[] secondaParte_composto=secondaParte.split("\\-");
							String pos_secondaParte=secondaParte_composto[1];
							if (tit_secondaParte.length()>100)
							{
								tit_secondaParte_finale=tit_secondaParte.substring(0,100);
							}
							else
							{
								tit_secondaParte_finale=tit_secondaParte;
							}
						}
						//}
					}
					if (!tit_primaParte_finale.equals("") &&  tit_primaParte_finale.length()>0)
					{
						tit_isbd_finale=tit_primaParte_finale.trim();
						if ( !tit_secondaParte_finale.equals("") &&  tit_secondaParte_finale.length()>0 )
						{
							tit_isbd_finale=tit_isbd_finale + ". - " +  tit_secondaParte_finale.trim();
						}
					}
					if (!tit_isbd_finale.equals("") &&  tit_isbd_finale.length()>0)
					{
						recTit.setIsbd(tit_isbd_finale);
					}
				}
				recTit.setNatura(String.valueOf(tit.getCd_natura()));
				if (tit!=null &&  tit.getCd_lingua_1() != null) {
						arrLingua[0] = tit.getCd_lingua_1().toString();
						if (tit.getCd_lingua_2() != null)
						{
							arrLingua[1] = tit.getCd_lingua_2().toString();
						}
						if (tit.getCd_lingua_3() != null)
						{
							arrLingua[2] = tit.getCd_lingua_3().toString();
						}
				}
				recTit.setArrCodLingua(arrLingua);
				recTit.setCodPaese(tit.getCd_paese());
			}
			// fine innesto






		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	return results;
	//return recTit;
	}


	public TitoloACQVO getTitoloOrdineBis(String bidPassato) throws DaoManagerException {
		List results = null;
		TitoloACQVO recTit=null;

		try {
			Session session = this.getCurrentSession();

			String sqlString="select t, n.numero_std from Tb_titolo t,Tb_numero_std  n   "; //ok
			sqlString=sqlString +" where  n.b.bid=t.bid " 	  ; // ok la property b  è definita nel mapping
			sqlString=sqlString +" and t.fl_canc<>'S'"; //ok
			sqlString=sqlString +" and n.fl_canc<>'S'"; //ok
			sqlString=sqlString +" and n.tp_numero_std in ('I','J','M','L','E','G')"; //ok

			//System.out.println("ciaooooooooooooooooooooooooooooo2");

			//bidPassato="BVE0036760"; // RAV0028330 CFI0232619 CFI0117938(ha 3 numeri standard)

			if (bidPassato!=null)
			{
				//sqlString=sqlString +" where  n.b=t ";
				sqlString=sqlString +" and t.bid='"+ bidPassato +"'"; //ok

			}
			//sqlString=sqlString + " order by  v.valuta";

			Query query = session.createQuery(sqlString);
			results  =  query.list();

			if (results!=null && results.size()>1)
			{
				List results2=new ArrayList();
				results2.add(results.get(0)); // considero solo il primo
				results=results2;
			}
			// se non ci sono risultati della join cerco il titolo e creo l'oggetto con il numerostandard vuoto
			if ( results.size()==0 )
			{
				//String sqlString2="select  t, ' '  from Tb_titolo t ";
				String sqlString2="select  t,''  from Tb_titolo t ";
				sqlString2=sqlString2 +" where t.bid='"+ bidPassato +"'"; //ok
				sqlString2=sqlString2 +" and t.fl_canc<>'S'"; //ok
				Query query2 = session.createQuery(sqlString2);
				results  =  query2.list();


			}
			//results  =  this.getOrdiniBid();
			//results  =  this.getOrdiniBid();

			if (results==null || (results!=null && results.size()==0) )
			{
				results=null;
			}
			// inizio innesto
			List lista = new ArrayList(); // output di un singolo titolo ricavato
			String[] arrLingua=new String[3];
			//TitoloACQVO recTit=null;
			int codRet=0;
			String tit_isbd="";

			if (results!=null && results.size()>0)
			{
				recTit = new TitoloACQVO();

				Object[] arrivo =null;
				arrivo = (Object[]) results.get(0);
				Tb_titolo tit= (Tb_titolo) arrivo[0];
				recTit.setNumStandard((String) arrivo[1]); //  numero standard

				//Tb_titolo tit= (Tb_titolo)risposta.get(0);

				tit_isbd=tit.getIsbd();
				recTit.setIsbd(tit.getIsbd());
				if  (!tit_isbd.equals("") &&  tit_isbd.length()>200)
				{
					// gestione composizione titolo
					String indice_isbd=tit.getIndice_isbd();

					String[] isbd_composto=tit_isbd.split("\\. -");
					// se si splitta (lunghe>0) allora considero la prima parte, altrimenti tutto
					String tit_primaParte=isbd_composto[0];
					String tit_secondaParte=isbd_composto[1];
					String[] indice_isbd_composto=indice_isbd.split("\\;");
					String  tit_secondaParte_finale="";
					String  tit_primaParte_finale="";
					String  tit_isbd_finale="";

					if (indice_isbd_composto.length>0 && indice_isbd_composto.length>=1)
					{
						//for (int y = 0; y < indice_isbd_composto.length; y++)
						//{
						if (indice_isbd_composto[0]!=null && indice_isbd_composto[0].length()!=0)
						{
							String primaParte=indice_isbd_composto[0];
							String[] primaParte_composto=primaParte.split("-");
							String pos_primaParte=primaParte_composto[1];

							if (tit_primaParte.length()>100)
							{
								tit_primaParte_finale=tit_primaParte.substring(0,100);
							}
							else
							{
								tit_primaParte_finale=tit_primaParte;
							}
						}
						if (indice_isbd_composto[1]!=null && indice_isbd_composto[1].length()!=0)
						{
							String secondaParte=indice_isbd_composto[1];
							String[] secondaParte_composto=secondaParte.split("\\-");
							String pos_secondaParte=secondaParte_composto[1];
							if (tit_secondaParte.length()>100)
							{
								tit_secondaParte_finale=tit_secondaParte.substring(0,100);
							}
							else
							{
								tit_secondaParte_finale=tit_secondaParte;
							}
						}
						//}
					}
					if (!tit_primaParte_finale.equals("") &&  tit_primaParte_finale.length()>0)
					{
						tit_isbd_finale=tit_primaParte_finale.trim();
						if ( !tit_secondaParte_finale.equals("") &&  tit_secondaParte_finale.length()>0 )
						{
							tit_isbd_finale=tit_isbd_finale + ". - " +  tit_secondaParte_finale.trim();
						}
					}
					if (!tit_isbd_finale.equals("") &&  tit_isbd_finale.length()>0)
					{
						recTit.setIsbd(tit_isbd_finale);
					}
				}
				recTit.setNatura(String.valueOf(tit.getCd_natura()));
				if (tit!=null &&  tit.getCd_lingua_1() != null) {
						arrLingua[0] = tit.getCd_lingua_1().toString();
						if (tit.getCd_lingua_2() != null)
						{
							arrLingua[1] = tit.getCd_lingua_2().toString();
						}
						if (tit.getCd_lingua_3() != null)
						{
							arrLingua[2] = tit.getCd_lingua_3().toString();
						}
				}
				recTit.setArrCodLingua(arrLingua);
				recTit.setCodPaese(tit.getCd_paese());
			}
			// fine innesto






		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	//return results;
	return recTit;
	}



	public List getOrdiniBid () throws DaoManagerException {
		List results = null;

		try {
			Session session = this.getCurrentSession();

 			String appo="'CFI0251860','LO10487929','LIA0151412','NAPE003611','CFI0119349' "; //, LO10487929','CFI0119349','NAPE003611' CFI0251860
			String sqlString="select o from Tba_ordini o ";
			sqlString=sqlString +" where o.bid in ("+ appo + ")" ;
			sqlString=sqlString +" and o.fl_canc<>'S' "; // non cancellato
			sqlString=sqlString +" and o.stato_ordine<>'N' "; // non annullato
			// vanno aggiunti i cod bibl e di polo?
			Query query = session.createQuery(sqlString);
			results  =  query.list();
			if (results==null || (results!=null && results.size()==0) || (results!=null && results.size()>1))
			{
				results=null;
			}

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	return results;

	}



	public boolean inserisciCambioHib(Tba_cambi_ufficiali cambio )
	throws  DaoManagerException{
		//Tba_cambi_ufficiali cu = null;
		boolean ret = false;
		try{
			//this.getCurrentSession().clear(); //
			Session session = this.getCurrentSession();
			session.saveOrUpdate(cambio);
			//session.flush();
			return ret = true;
		}catch (HibernateException e){
/*			e.printStackTrace();
			if (e.getCause().equals("ConstraintViolationException"))
			{
				// ConstraintViolationException record precedentemente cancellato

			}
*/			throw new DaoManagerException(e);
		}

	}
	public boolean  modificaCambioHib(Tba_cambi_ufficiali cambio)
	throws  DaoManagerException{
		//Tba_cambi_ufficiali cu = null;
		boolean ret = false;
		try{
			//this.getCurrentSession().clear(); //
			Session session = this.getCurrentSession();
			session.saveOrUpdate(cambio); // SaveOrUpdateCopy
			//session.flush();
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean  cancellaCambioHib(Tba_cambi_ufficiali cambio) throws DaoManagerException {
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(cambio); // SaveOrUpdateCopy
			//session.flush();
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public Tba_cambi_ufficiali cercaValutaRiferimento(String codPolo, String codBib) throws DaoManagerException {

		try {
			//almaviva5_20140612 #5078
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tba_cambi_ufficiali.class);
			c.add(Restrictions.ne("fl_canc", "S"));
			c.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));
			c.add(Restrictions.eq("data_var", VALUTA_RIF_DATE));

			return (Tba_cambi_ufficiali) c.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
}
	}

	public int countValuteNonRiferimento(String codPolo, String codBib) throws DaoManagerException {

		try {
			//almaviva5_20140617 #5078
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tba_cambi_ufficiali.class);
			c.add(Restrictions.ne("fl_canc", "S"));
			c.add(Restrictions.eq("cd_bib", creaIdBib(codPolo, codBib)));
			c.add(Restrictions.ne("data_var", VALUTA_RIF_DATE));
			c.setProjection(Projections.rowCount());

			Number cnt = (Number) c.uniqueResult();
			return cnt.intValue();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

}
