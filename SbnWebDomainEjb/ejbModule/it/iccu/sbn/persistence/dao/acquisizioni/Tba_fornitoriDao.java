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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_profili_acquisto;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori;
import it.iccu.sbn.polo.orm.acquisizioni.Tbr_fornitori_biblioteche;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_sez_acquisizione_fornitori;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.bibliografica.viste.V_catalogo_editoria;
import it.iccu.sbn.web2.util.Constants;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;


public class Tba_fornitoriDao extends DaoManager {

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

	public List getRicercaListaFornitoriHibDaCancHIb(ListaSuppFornitoreVO ricercaFornitori) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria cr = session.createCriteria(Tbr_fornitori.class, "fo");
			List<FornitoreVO> results =null;


			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaFornitori.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaFornitori.getCodBibl());
			bib.setCd_polo(polo);

			Tbr_fornitori_biblioteche fb = new Tbr_fornitori_biblioteche ();
			//fb.setCd_biblioteca(bib);
			//fb.setFl_canc('N');

			if (ricercaFornitori.getCodFornitore()!=null && ricercaFornitori.getCodFornitore().trim().length()!=0)	{
				cr.add(Restrictions.eq("cod_fornitore", Integer.valueOf(ricercaFornitori.getCodFornitore().trim())));
			}
			if (!ricercaFornitori.isFlag_canc())	{
				cr.add(Restrictions.eq("fl_canc", 'N'));
			}	else {
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}

			// presenza  di legami con ordini in qualsiasi stato oppure presenza  di legami con suggerimenti (non rifiutati TESTATO NEL BEAN)
			cr.add(Restrictions.or(Restrictions.isNotEmpty("Tba_ordini"),
					Restrictions.isNotEmpty("Tba_offerte_fornitore")
			));

			cr.addOrder(Order.asc("cod_fornitore"));
			results = cr.list();

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



	public List<Tbr_fornitori> getRicercaListaFornitoriHib(ListaSuppFornitoreVO ricercaFornitori) throws DaoManagerException {
		List<Tbr_fornitori> results =null ;

		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbr_fornitori.class,"f");

			Tbf_polo polo = new Tbf_polo();
			if (ricercaFornitori.getCodPolo()!=null)
			{
				polo.setCd_polo(ricercaFornitori.getCodPolo());
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (ricercaFornitori.getCodBibl()!=null)
			{
				bib.setCd_biblioteca(ricercaFornitori.getCodBibl());
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}

			Tbr_fornitori_biblioteche fb = new Tbr_fornitori_biblioteche ();
			//fb.setCd_biblioteca(bib);
			//fb.setFl_canc('N');
			if (ricercaFornitori.getCodFornitore()!=null && ricercaFornitori.getCodFornitore().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cod_fornitore", Integer.valueOf(ricercaFornitori.getCodFornitore().trim())));
			}



			if (!ricercaFornitori.isFlag_canc() && (ricercaFornitori.getTipoOperazionePicos()==null || (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))))
			{
					cr.add(Restrictions.eq("fl_canc", 'N'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}

			if ((ricercaFornitori.getCodProfiloAcq()!=null && ricercaFornitori.getCodProfiloAcq().length()!=0) ||(ricercaFornitori.getCodSezione()!=null && ricercaFornitori.getCodSezione().length()!=0) || (ricercaFornitori.getCodLingua()!=null && ricercaFornitori.getCodLingua().length()!=0 ))
			{
				DetachedCriteria childCriteriaTraSezForn = DetachedCriteria.forClass( Tra_sez_acquisizione_fornitori.class , "child_Tra_sez_forn");
				childCriteriaTraSezForn.setProjection( Property.forName("child_Tra_sez_forn.cod_fornitore" ) );
				childCriteriaTraSezForn.add(Property.forName("f.cod_fornitore").eqProperty("child_Tra_sez_forn.cod_fornitore") );
				if (ricercaFornitori.getCodProfiloAcq()!=null && ricercaFornitori.getCodProfiloAcq().length()!=0)
				{
					childCriteriaTraSezForn.add(Restrictions.eq("child_Tra_sez_forn.cod_prac", BigDecimal.valueOf(Integer.parseInt(ricercaFornitori.getCodProfiloAcq()))));
				}

				if (ricercaFornitori.getCodFornitore()!=null && ricercaFornitori.getCodFornitore().trim().length()!=0)
				{
					childCriteriaTraSezForn.add(Restrictions.eq("f.cod_fornitore", Integer.parseInt(ricercaFornitori.getCodFornitore().trim())));
				}

				if ((ricercaFornitori.getCodSezione()!=null && ricercaFornitori.getCodSezione().length()!=0) || (ricercaFornitori.getCodLingua()!=null && ricercaFornitori.getCodLingua().length()!=0 ) )
				{
					DetachedCriteria childCriteriaProf = DetachedCriteria.forClass( Tba_profili_acquisto.class , "child_Profili");
					childCriteriaProf.setProjection( Property.forName("child_Profili.cod_prac" ) );
					if (ricercaFornitori.getCodSezione()!=null && ricercaFornitori.getCodSezione().length()!=0 )
					{
						DetachedCriteria childCriteriaSez = DetachedCriteria.forClass(Tba_sez_acquis_bibliografiche.class , "child_sez");
						childCriteriaSez.setProjection(Property.forName("child_sez.id_sez_acquis_bibliografiche" ) );
						childCriteriaSez.add(Property.forName("child_Profili.id_sez_acquis_bibliografiche.id_sez_acquis_bibliografiche").eqProperty("child_sez.id_sez_acquis_bibliografiche") );
						childCriteriaSez.add(Restrictions.eq("child_sez.cod_sezione",ricercaFornitori.getCodSezione().trim()));
						childCriteriaSez.add(Restrictions.eq("child_sez.fl_canc", 'N'));
						childCriteriaSez.add(Restrictions.eq("child_sez.cd_bib", bib));
						childCriteriaProf.add(Subqueries.exists(childCriteriaSez));
						childCriteriaProf.add(Property.forName("child_Profili.id_sez_acquis_bibliografiche.id_sez_acquis_bibliografiche").in( childCriteriaSez));
					}
					if (ricercaFornitori.getCodLingua()!=null && ricercaFornitori.getCodLingua().length()!=0 )
					{
						childCriteriaProf.add(Restrictions.eq("child_Profili.lingua", ricercaFornitori.getCodLingua()));
					}
					childCriteriaTraSezForn.add(Subqueries.exists(childCriteriaProf));
					childCriteriaTraSezForn.add(Property.forName("child_Tra_sez_forn.cod_prac").in( childCriteriaProf));

					ricercaFornitori.setLocale("1");
				}

				cr.add(Subqueries.exists(childCriteriaTraSezForn));
				cr.add(Property.forName( "f.cod_fornitore" ).in( childCriteriaTraSezForn));
				List profAcq = cr.list();

				ricercaFornitori.setLocale("1");

			}

			// per trovare il fornitore legato alla biblioteca
			//Boolean nonpassare=false;
			if (ricercaFornitori.getLocale()!=null &&  ricercaFornitori.getLocale().equals("1") ) // && nonpassare
			{

				//DetachedCriteria masterCriteria = DetachedCriteria.forClass( Tbr_fornitori.class , "master");
				DetachedCriteria childCriteria = DetachedCriteria.forClass( Tbr_fornitori_biblioteche.class , "child");
//				 Given restriction DetachedCriteria childCriteria = masterCriteria.createCriteria( "children" );
//				 This assumes that we have a Reference to the Master record called 'master'
				childCriteria.setProjection( Property.forName( "child.cod_fornitore" ) );
				childCriteria.add( Property.forName("f.cod_fornitore").eqProperty("child.cod_fornitore") );
				childCriteria.add(Restrictions.eq("child.fl_canc", 'N'));
				childCriteria.add(Restrictions.eq("child.cd_biblioteca", bib));

				cr.add(Subqueries.exists(childCriteria));
				cr.add(Property.forName( "f.cod_fornitore" ).in( childCriteria));

				List biblLoc = cr.list();
			}

			if (ricercaFornitori.getNomeFornitore()!=null && ricercaFornitori.getNomeFornitore().length()!=0 )
			{
				if (ricercaFornitori.getTipoRicerca()!=null && ricercaFornitori.getTipoRicerca().length()!=0 )
				{

					if (ricercaFornitori.getTipoRicerca().equals("inizio") )
					{
						if (ricercaFornitori.getChiaveFor()!=null && ricercaFornitori.getChiaveFor().length()!=0)
						{
							cr.add(Restrictions.ilike("chiave_for", ricercaFornitori.getChiaveFor()+"%"));

						}
						else
						{
							cr.add(Restrictions.ilike("nom_fornitore", (ricercaFornitori.getNomeFornitore().trim().replace("'","''")+"%").toUpperCase()));
						}
					}
					else if (ricercaFornitori.getTipoRicerca().equals("intero") )
					{

						if (ricercaFornitori.getChiaveFor()!=null && ricercaFornitori.getChiaveFor().length()!=0)
						{
							cr.add(Restrictions.eq("chiave_for", ricercaFornitori.getChiaveFor()).ignoreCase());
						}
						else
						{
							cr.add(Restrictions.eq("nom_fornitore", ricercaFornitori.getNomeFornitore().trim().replace("'","''")).ignoreCase());
						}
					}
					else if (ricercaFornitori.getTipoRicerca().equals("parole") )
					{

						if (ricercaFornitori.getChiaveFor()!=null && ricercaFornitori.getChiaveFor().length()!=0)
						{
							String[] parole = ricercaFornitori.getChiaveFor().split("\\s+");
							String tmp = parole[0];
							for (int i = 1; i < parole.length; i++)
								tmp = tmp + " & " + parole[i];


							//sql=sql + " forn.chiave_for = '" + ricercaFornitori.getChiaveFor()+"'";
							DaoManager hibDao = new DaoManager();
							 if (hibDao.getVersion().compareTo(ComparableVersion.of(Constants.POSTGRES_VERSION_83)) < 0)
							 {
								 //sql=sql + " tidx_vector @@ to_tsquery('default', '" + ricercaFornitori.getChiaveFor() +"')";
								 //sql=sql + " tidx_vector @@ to_tsquery('default', '" + tmp +"')";
								cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('default','" + tmp + "')"));

							 }
							 else
							 {
								 //sql=sql + " tidx_vector @@ to_tsquery('" + ricercaFornitori.getChiaveFor() +"')";
								//sql=sql + " tidx_vector @@ to_tsquery('" + tmp +"')";
								cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('" + tmp + "')"));
							 }

						}
						else
						{
							//** tidx_vector @@ 'aaa'::tsquery; .trim().toUpperCase()
							//fare do while
							String appo= ricercaFornitori.getNomeFornitore().trim();
							// eliminazione degli spazi di troppo
							while (appo.indexOf("  ")>0) {
								appo = appo.replaceAll("  "," ");
							}

							String[] nomeFornComposto=appo.split(" ");
							// String tit_primaParte=isbd_composto[0];
							// 'parola1 & parola2'
							// 'parola1 | parola2'
							String paroleForn="";
							for (int i=0;  i <  nomeFornComposto.length; i++)
							{
								if (i==0)
								{
									paroleForn=nomeFornComposto[0] ;
								}
								if (i>0)
								{
									paroleForn=paroleForn + " & " + nomeFornComposto[i] ;
								}

							}
							DaoManager hibDao = new DaoManager();
							 if (hibDao.getVersion().compareTo(ComparableVersion.of(Constants.POSTGRES_VERSION_83)) < 0)
							 {
								 //sql=sql + " tidx_vector @@ to_tsquery('default', '" + paroleForn +"')";
								cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('default','" + paroleForn + "')"));

							 }
							 else
							 {
								 //sql=sql + " tidx_vector @@ to_tsquery('" + paroleForn +"')";
								cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('" + paroleForn + "')"));

							 }
						}
					}
				}
			}

// inizio picos

			Boolean dataInizioPicos=false;
			Boolean dataFinePicos=false;

/*				Date oggi=new Date(System.currentTimeMillis());
			//ricercaFornitori.setDataInizioPicos(oggi);
			ricercaFornitori.setDataFinePicos(oggi);
			String[] statoOrdArr=new String[2];
			statoOrdArr[0]="E";
			statoOrdArr[1]="D";
			ricercaFornitori.setTipoFornPicosArr(statoOrdArr);
			ricercaFornitori.setTipoOperazionePicos("M");
*/
			if (ricercaFornitori.getDataInizioPicos()!=null )
			{
				dataInizioPicos=true;
			}
			if (ricercaFornitori.getDataFinePicos()!=null)
			{
				dataFinePicos=true;
			}
			if (dataInizioPicos && dataFinePicos )
			{
				if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
				{
					cr.add(Restrictions.or(Restrictions.between("ts_var", ricercaFornitori.getDataInizioPicos(), ricercaFornitori.getDataFinePicos()),Restrictions.between("ts_ins", ricercaFornitori.getDataInizioPicos(), ricercaFornitori.getDataFinePicos())));
				}
				else
				{
					cr.add(Restrictions.between("ts_var", ricercaFornitori.getDataInizioPicos(), ricercaFornitori.getDataFinePicos()));
				}
			}
			else if (dataInizioPicos )
			{
				if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
				{
					cr.add(Restrictions.or(Restrictions.ge("ts_var", ricercaFornitori.getDataInizioPicos()),Restrictions.ge("ts_ins", ricercaFornitori.getDataInizioPicos())));
				}
				else
				{
					cr.add(Restrictions.ge("ts_var", ricercaFornitori.getDataInizioPicos()));
				}
			}
			else if (dataFinePicos )
			{
				if (ricercaFornitori.getTipoOperazionePicos()!=null &&  !ricercaFornitori.getTipoOperazionePicos().equals("C"))
				{
					cr.add(Restrictions.or(Restrictions.le("ts_var", ricercaFornitori.getDataFinePicos()),Restrictions.le("ts_ins", ricercaFornitori.getDataFinePicos())));
				}
				else
				{
					cr.add(Restrictions.le("ts_var", ricercaFornitori.getDataFinePicos()));
				}
			}

			if (ricercaFornitori.getTipoFornPicosArr()!=null && ricercaFornitori.getTipoFornPicosArr().length!=0 )
			{
				cr.add(Restrictions.in("tipo_partner",  ricercaFornitori.getTipoFornPicosArr()));
			}


/*			if (ricercaFornitori.getTipoFornPicosArr()!=null && ricercaFornitori.getTipoFornPicosArr().length!=0 )
			{
				Boolean aggiungiSQL=false;
				String sqla="";
				sqla=sqla + " ( ";
				for (int index = 0; index < ricercaFornitori.getTipoFornPicosArr().length; index++) {
					String so = ricercaFornitori.getTipoFornPicosArr()[index] ;
					if (so!=null && so.length()!=0)
					{
				         if (!sqla.equals(" ( ")) {
				        	 sqla=sqla + " OR ";
				         }
				        sqla=sqla + " forn.tipo_partner='"+ so +"'";
						aggiungiSQL=true;
					}
				}
				sqla=sqla + " ) ";
				if (aggiungiSQL)
				{
					sql=this.struttura(sql);
					sql= sql + sqla;
				}
			}
*/

// fine picos



			if (ricercaFornitori.getTipoPartner()!=null && ricercaFornitori.getTipoPartner().length()!=0 )
			{
				cr.add(Restrictions.eq("tipo_partner", ricercaFornitori.getTipoPartner().charAt(0)));
			}
			if (ricercaFornitori.getProvincia()!=null && ricercaFornitori.getProvincia().length()!=0 )
			{
				String pro=ricercaFornitori.getProvincia().trim();
/*				String[] prochr=new String [ricercaFornitori.getProvincia().trim().length()];
				// string to char
				for (int i=0;  i <  ricercaFornitori.getProvincia().trim().length(); i++)
				{
					prochr[i]=ricercaFornitori.getProvincia().trim().substring(i,i+1);
				}
				char[] prochrappo = pro.toCharArray();
*/
				//char [] charray = you_said.toCharArray();

/*			      for (int k = 1 ; k<prochrappo.length; k++) {
			      for (int i = 1 ; i<prochrappo.length; i++) {
			      if (prochrappo[i] < prochrappo [i-1]) {
			           char hold = prochrappo[i-1];
			           prochrappo[i-1]=prochrappo[i];
			           prochrappo[i]=hold;
			      }}}

			      String ordered = String.copyValueOf(prochrappo);*/


/*				Character prochrappoele =new Character(pro.toCharArray());
				Character prochrappoUnico =null ;
				for (int i=0;  i <  prochrappo.length; i++)
				{
					Character prochrappoele =new Character ;
					prochr[i]=ricercaFornitori.getProvincia().trim().substring(i,i+1);

				}
*/

				//cr.add(Restrictions.eq("provincia",ricercaFornitori.getProvincia()));
				cr.add(Restrictions.eq("provincia",pro));


			}
			if (ricercaFornitori.getPaese()!=null && ricercaFornitori.getPaese().length()!=0 )
			{
				cr.add(Restrictions.eq("paese", ricercaFornitori.getPaese())); // .charAt(0)
			}

			// ordinamento passato
			if (ricercaFornitori.getOrdinamento()==null || (ricercaFornitori.getOrdinamento()!=null && ricercaFornitori.getOrdinamento().equals("")) )
			{
				cr.addOrder(Order.asc("tipo_partner"));
				cr.addOrder(Order.asc("nom_fornitore").ignoreCase());
			}
			else if (ricercaFornitori.getOrdinamento().equals("1"))
			{
				cr.addOrder(Order.asc("tipo_partner"));
				cr.addOrder(Order.asc("nom_fornitore").ignoreCase());
			}
/*				else if (ricercaFornitori.getOrdinamento().equals("2"))
			{
				sql=sql + "  order by forn.cod_fornitore ";
			}
*/				else if (ricercaFornitori.getOrdinamento().equals("3"))
			{
				cr.addOrder(Order.asc("nom_fornitore").ignoreCase());
			}
			else if (ricercaFornitori.getOrdinamento().equals("4"))
			{
				cr.addOrder(Order.asc("unit_org"));
				cr.addOrder(Order.asc("nom_fornitore").ignoreCase());
			}
			else if (ricercaFornitori.getOrdinamento().equals("5"))
			{
				cr.addOrder(Order.asc("paese"));
				cr.addOrder(Order.asc("provincia"));
				cr.addOrder(Order.asc("cap"));
				cr.addOrder(Order.asc("citta"));
				cr.addOrder(Order.asc("indirizzo"));
			}
			else if (ricercaFornitori.getOrdinamento().equals("6"))
			{
				cr.addOrder(Order.asc("cod_fornitore"));
			}
			else if (ricercaFornitori.getOrdinamento().equals("7"))
			{
				cr.addOrder(Order.desc("cod_fornitore"));
			}
			else if (ricercaFornitori.getOrdinamento().equals("8"))
			{
				cr.addOrder(Order.desc("nom_fornitore").ignoreCase());
			}
			results = cr.list();


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


	public List<Tba_profili_acquisto> getProfiliFornitoreHib(ListaSuppFornitoreVO ricercaFornitori) throws DaoManagerException {
		List<Tba_profili_acquisto> results =null ;

		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_profili_acquisto.class,"prof");

			Tbf_polo polo = new Tbf_polo();
			if (ricercaFornitori.getCodPolo()!=null)
			{
				polo.setCd_polo(ricercaFornitori.getCodPolo());
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (ricercaFornitori.getCodBibl()!=null)
			{
				bib.setCd_biblioteca(ricercaFornitori.getCodBibl());
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}

			Tbr_fornitori_biblioteche fb = new Tbr_fornitori_biblioteche ();
			//fb.setCd_biblioteca(bib);
			//fb.setFl_canc('N');
/*			if (ricercaFornitori.getCodFornitore()!=null && ricercaFornitori.getCodFornitore().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cod_fornitore", Integer.valueOf(ricercaFornitori.getCodFornitore().trim())));
			}
*/			if (!ricercaFornitori.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}

			//cr.setProjection( Property.forName("prof.cod_prac" ) );

			DetachedCriteria childCriteriaTraSezForn = DetachedCriteria.forClass( Tra_sez_acquisizione_fornitori.class , "child_Tra_sez_forn");
			childCriteriaTraSezForn.setProjection( Property.forName("child_Tra_sez_forn.cod_prac" ) );
			childCriteriaTraSezForn.add(Property.forName("prof.cod_prac").eqProperty("child_Tra_sez_forn.cod_prac") );
			if (ricercaFornitori.getCodFornitore()!=null && ricercaFornitori.getCodFornitore().trim().length()!=0)
			{
				childCriteriaTraSezForn.add(Restrictions.eq("child_Tra_sez_forn.cod_fornitore", Integer.parseInt(ricercaFornitori.getCodFornitore().trim())));
			}
			childCriteriaTraSezForn.add(Restrictions.eq("child_Tra_sez_forn.fl_canc", 'N'));
			childCriteriaTraSezForn.add(Restrictions.eq("child_Tra_sez_forn.cd_biblioteca", bib));

			cr.add(Subqueries.exists(childCriteriaTraSezForn));
			cr.add(Property.forName("prof.cod_prac").in( childCriteriaTraSezForn));
			List profAcq = cr.list();
			results = cr.list();
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


	public boolean inserisciFornitoreHib(Tbr_fornitori fornitore)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(fornitore);
			session.flush();
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}

	public boolean inserisciFornitoreBibliotecaHib(Tbr_fornitori_biblioteche fornitore)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(fornitore);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}


	public boolean  modificaFornitoreHib(Tbr_fornitori fornitore)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.clear();
			session.saveOrUpdate(fornitore); // SaveOrUpdateCopy
			session.flush();
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean  modificaFornitoreBibliotecaHib(Tbr_fornitori_biblioteche fornitoreBib)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(fornitoreBib); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean  cancellaFornitoreHib(Tbr_fornitori fornitore)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(fornitore); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}



	public boolean  cancellaProfFornitoreHib(Tra_sez_acquisizione_fornitori profFornitore)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.delete(profFornitore);
			//session.clear();
			//session.saveOrUpdate(profFornitore);
			session.flush();

			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}



	public String  getCodClienteDaTbr_fornitori_biblioteche(String cod_forn, String codBib, String codPolo)
	throws  DaoManagerException{
		String cod_cliente ="";
		try{
			Session session = this.getCurrentSession();
			Criteria crCod = session.createCriteria(Tbr_fornitori_biblioteche.class);
			crCod.add(Restrictions.eq("fl_canc", 'N'));
			if (cod_forn!=null && cod_forn.trim().length()>0 && Integer.valueOf(cod_forn)>0 )
			{
				crCod.add(Restrictions.eq("cod_fornitore",Integer.valueOf(cod_forn)));
			}

			Tbf_polo polo = new Tbf_polo();
			if (codPolo!=null)
			{
				polo.setCd_polo(codPolo);
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (codBib!=null)
			{
				bib.setCd_biblioteca(codBib);
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}
			crCod.add(Restrictions.eq("cd_biblioteca", bib));
			crCod.setProjection(Projections.projectionList()
					.add(Projections.property("cod_cliente"))
			);
			List<String> denoresults = crCod.list();
			if (denoresults!=null && denoresults.size()==1)
			{
				cod_cliente=denoresults.get(0).trim();
			}

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return cod_cliente;
	}

	public Tbr_fornitori getFornitore(String codPolo, String codBib, String codFornitore, String descrForn)
	throws DaoManagerException{
		Tbr_fornitori rec = null;

		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbr_fornitori.class);

        	if (codFornitore == null){
        		if (descrForn == null){
        		}else{
					cr.add(Restrictions.ilike("nom_fornitore", (descrForn.trim().replace("'","''")+"%").toUpperCase()));
        		}
        	}else{
        		if (descrForn == null){
        			cr.add(Restrictions.eq("cod_fornitore", Integer.valueOf(codFornitore)));
        		}else{
        			cr.add(Restrictions.eq("cod_fornitore", Integer.valueOf(codFornitore)));
					cr.add(Restrictions.ilike("nom_fornitore", (descrForn.trim().replace("'","''")+"%").toUpperCase()));
        		}
        	}
			cr.add(Restrictions.ne("fl_canc", 'S'));
			rec = (Tbr_fornitori) cr.uniqueResult();

		}catch (org.hibernate.ObjectNotFoundException e){
			return null;
		}catch (NonUniqueResultException e){
			throw new DaoManagerException(e);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
		return rec;
	}

	public List<V_catalogo_editoria> getEditori_vistaCatalogoEditoria(String codPolo, String codBib,
			String codEditore,	String isbn, String regione, String provincia,
			String dataPubbl1Da, String dataPubbl1A,
			String tipoRecord, String lingua,
			String natura, String checkTipoPosseduto, String checkEditore, String checkTipoRicerca, String paese)
	throws DaoManagerException	{
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cd_bib", codBib);
			//
			/*
			 * old
//*solo titoli posseduti dalla biblioteca operante P
select cep.*
from vl_catalogo_editoria_posseduto cep
where cep.cd_bib=' IC'
and cep.bid in ('TO00667588','RMS0009664','BVE0050600','BVE0495303')
order by cep.chiave_for, cep.ky_cles1_t, cep.ky_cles2_t;

//*titoli non posseduti dalla biblioteca operante NP*
select
from v_catalogo_editoria ce
left outer join vl_catalogo_editoria_posseduto cep on ce.bid=cep.bid and cep.cd_bib=' IC'
where cep isnull
and ce.bid in ('TO00667588','RMS0009664','BVE0050600','BVE0495303')
order by ce.chiave_for, ce.ky_cles1_t, ce.ky_cles2_t;


//*titoli posseduti e non posseduti dalla biblioteca operante con indicazione del n.ro di copie possedute dalla biblioteca operante T
select cep.*
from vl_catalogo_editoria_posseduto cep
where cep.cd_bib=' IC'
and cep.bid in ('TO00667588','RMS0009664','BVE0050600','BVE0495303')
UNION
select ce.*, NULL, NULL, NULL, NULL, NULL, cep.classe
from v_catalogo_editoria ce
left outer join vl_catalogo_editoria_posseduto cep on ce.bid=cep.bid and cep.cd_bib=' IC'
where cep.tot_inv isnull
and ce.bid in ('TO00667588','RMS0009664','BVE0050600','BVE0495303')
order by chiave_for, ky_cles1_t, ky_cles2_t;			 *
			 */
			//
			/*
			 * soltanto vista v_catalogo_editoria
//*solo titoli posseduti dalla biblioteca operante P
select cep.*
from v_catalogo_editoria ce
where cep.cd_bib=' IC'
order by cep.chiave_for, cep.ky_cles1_t, cep.ky_cles2_t;

//*titoli non posseduti dalla biblioteca operante NP*
select
from v_catalogo_editoria ce
where ce.cd_bib<>' IC'
order by ce.chiave_for, ce.ky_cles1_t, ce.ky_cles2_t;


//*titoli posseduti e non posseduti dalla biblioteca operante con indicazione del n.ro di copie possedute dalla biblioteca operante T
select cep.*
from v_catalogo_editoria ce
order by chiave_for, ky_cles1_t, ky_cles2_t;			 *
			 */
			List<V_catalogo_editoria> results = null;
			Session session = this.getCurrentSession();
			StringBuilder query = new StringBuilder(1024);
			query.append("select * from v_catalogo_editoria ce ");

			//almaviva5_20140527 #5552 filtro possesso
			if(ValidazioneDati.equals(checkTipoPosseduto, "P")) {
				query.append("inner join tr_tit_bib tb on tb.bid=ce.bid and tb.fl_possesso='S' and tb.fl_canc<>'S' ");
				query.append("where tb.cd_biblioteca=:codBib ");
				params.put("codBib", codBib);
			}

			if(ValidazioneDati.equals(checkTipoPosseduto, "T")) {
				query.append("where ce.bid<>'' ");
			}

			if(ValidazioneDati.equals(checkTipoPosseduto, "NP")) {
				query.append("where not exists ( ");
				query.append("select tb.bid from tr_tit_bib tb where tb.bid=ce.bid and tb.fl_possesso='S' and ");
				query.append("tb.fl_canc<>'S' and tb.cd_biblioteca=:codBib ) ");
				params.put("codBib", codBib);
			}

			if (codEditore!=null && codEditore.trim().length()!=0)	{
				query.append("and ce.cod_fornitore =:codEditore ");
				params.put("codEditore", Integer.valueOf(codEditore) );
			}
			if (regione!=null && regione.trim().length()!=0)	{//regione
				query.append("and ce.cod_regione =:regione ");
				params.put("regione", regione);
			}

			if (regione!=null && regione.trim().length()!=0)	{//regione
				query.append("and ce.cod_regione =:regione ");
				params.put("regione", regione);
			}

			//Ottobre 2013: gestione della stampa titoli per Editore anche per Paese
			if (paese!=null && paese.trim().length()!=0)	{//paese
				query.append("and ce.paese =:paese ");
				params.put("paese", paese);
			}
			if (isbn!=null && isbn.trim().length()!=0)	{//isbn_editore
				query.append("and ce.isbn_editore =:isbn ");
				params.put("isbn", isbn);
			}
			if (dataPubbl1Da!=null && dataPubbl1Da.trim().length()!=0)	{//annoPubblDa
				query.append(" and ce.aa_pubb_1 BETWEEN  :dataPubbl1Da and :dataPubbl1A ");
				params.put("dataPubbl1Da", dataPubbl1Da);
				params.put("dataPubbl1A", dataPubbl1A);
			}
			if (tipoRecord!=null && tipoRecord.trim().length()!=0)	{//tipoRecord
				query.append("and ce.tp_record_uni =:tipoRecord ");
				params.put("tipoRecord", tipoRecord);
			}
			if (lingua!=null && lingua.trim().length()!=0)	{//lingua
				query.append("and ce.cd_lingua_1 =:lingua ");
				params.put("lingua", lingua);
			}
			if (natura!=null && natura.trim().length()!=0)	{//natura
				query.append("and ce.cd_natura =:natura ");
				params.put("natura", natura);
			}
//			if (dataIngressoDa!=null && dataIngressoDa.trim().length()!=0)	{//dataIngressoDa
//				query.append(" and ce.ultima_data_ingresso BETWEEN  :dataIngressoDa and :dataIngressoA ");
//				params.put("dataIngressoDa", dataIngressoDa);
//				params.put("dataIngressoA", dataIngressoA);
//			}
//			if (tipoAcq!=null && tipoAcq.trim().length()!=0)	{//tipoAcq
//				query.append("and ce.tp_acquisizione =:tipoAcq ");
//				params.put("tipoAcq", tipoAcq);
//			}
			query.append("order by ce.chiave_for, ce.ky_cles1_t, ce.ky_cles2_t, ce.bid");
			SQLQuery queryI = session.createSQLQuery(query.toString());
			queryI.setProperties(params);
			queryI.addEntity(V_catalogo_editoria.class);
			results = queryI.list();
			return results;

		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

}
