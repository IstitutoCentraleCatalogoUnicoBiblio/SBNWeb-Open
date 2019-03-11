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
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_suggerimenti_bibliografici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;

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
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;




public class Tba_suggerimenti_bibliograficiDao extends DaoManager {

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


	public List<Tba_suggerimenti_bibliografici> getRicercaListaSuggerimentiHib(ListaSuppSuggerimentoVO ricercaSuggerimenti) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_suggerimenti_bibliografici.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaSuggerimenti.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaSuggerimenti.getCodBibl());
			bib.setCd_polo(polo);

			if (!ricercaSuggerimenti.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}
			//cr.add(Restrictions.eq("cd_bib", bib));
			if (ricercaSuggerimenti.getCodBibl()!=null && ricercaSuggerimenti.getCodBibl().trim().length()>0 &&  ricercaSuggerimenti.getCodPolo()!=null && ricercaSuggerimenti.getCodPolo().trim().length()>0)
			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}

			if (ricercaSuggerimenti.getCodiceSuggerimento()!=null && ricercaSuggerimenti.getCodiceSuggerimento().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cod_sugg_bibl", BigDecimal.valueOf(Double.valueOf(ricercaSuggerimenti.getCodiceSuggerimento().trim()))));
			}

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Boolean dataInizioBool=false;
			Boolean dataFineBool=false;

			if ((ricercaSuggerimenti.getDataSuggerimentoDa()!=null && ricercaSuggerimenti.getDataSuggerimentoDa().length()!=0) || (ricercaSuggerimenti.getDataSuggerimentoA()!=null && ricercaSuggerimenti.getDataSuggerimentoA().length()!=0))
			{
				//cr.add(Restrictions.between("data_ord", Integer.parseInt(ricercaOrdini.getDataOrdineDa()),Integer.parseInt(ricercaOrdini.getDataOrdineA())));
				Date startDate=new Date();
				try {
					startDate =formato.parse(ricercaSuggerimenti.getDataSuggerimentoDa());
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
					endDate = formato.parse(ricercaSuggerimenti.getDataSuggerimentoA());
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
					cr.add(Restrictions.between("data_sugg_bibl", new Date(startDate.getTime()), new Date(endDate.getTime())));
				}
				else if (dataInizioBool)
				{
					cr.add(Restrictions.ge("data_sugg_bibl", new Date(startDate.getTime())));
				}
				else if (dataFineBool)
				{
					cr.add(Restrictions.le("data_sugg_bibl", new Date(endDate.getTime())));
				}

			}

			if (ricercaSuggerimenti.getStatoSuggerimento()!=null && ricercaSuggerimenti.getStatoSuggerimento().length()!=0)
			{
				cr.add(Restrictions.eq("stato_sugg", ricercaSuggerimenti.getStatoSuggerimento()));
			}

			if (ricercaSuggerimenti.getIdSugList()!=null && ricercaSuggerimenti.getIdSugList().size()!=0 )
			{
				cr.add(Restrictions.in("cod_sugg_bibl",  ricercaSuggerimenti.getIdSugList()));
			}

/*				if (ricercaSuggerimenti.getBibliotecario().getDescrizione()!=null && ricercaSuggerimenti.getBibliotecario().getDescrizione().length()!=0 )
			{
				sql=this.struttura(sql);
				sql=sql + "( bibliot.cognome like '%" + ricercaSuggerimenti.getBibliotecario().getDescrizione() +"%'";
				sql=sql + " or bibliot.nome like '%" + ricercaSuggerimenti.getBibliotecario().getDescrizione() +"%')";

				//like '" + ricercaSezioni.getDescrizioneSezione() +"%'"
			}*/


			if (ricercaSuggerimenti.getBibliotecario() != null && ricercaSuggerimenti.getBibliotecario().getDescrizione()!=null && ricercaSuggerimenti.getBibliotecario().getDescrizione().length()!=0 )
			{
				cr.add(Restrictions.eq("cod_bibliotecario.id_utente_professionale", Integer.parseInt(ricercaSuggerimenti.getBibliotecario().getDescrizione().trim())));
			}

			if (ricercaSuggerimenti.getBibliotecario() != null && ricercaSuggerimenti.getBibliotecario().getCodice()!=null && ricercaSuggerimenti.getBibliotecario().getCodice().length()!=0 )
			{
				//sql=this.struttura(sql);
				//sql=sql + " uteprofweb.userid='" + ricercaSuggerimenti.getBibliotecario().getCodice() + "'" ; // username
				DetachedCriteria childCriteriaUte = DetachedCriteria.forClass(Tbf_utenti_professionali_web.class , "child_ute");
				childCriteriaUte.setProjection(Property.forName("child_ute.id_utente_professionale.id_utente_professionale"));
				//childCriteriaForn.add(Property.forName("cod_fornitore.cod_fornitore").eqProperty("child_forn.cod_fornitore") );
				childCriteriaUte.add(Restrictions.eq("child_ute.userid",ricercaSuggerimenti.getBibliotecario().getCodice().trim()));
				childCriteriaUte.add(Restrictions.eq("child_ute.fl_canc", 'N'));
				cr.add(Subqueries.exists(childCriteriaUte));
				cr.add(Property.forName("cod_bibliotecario").in( childCriteriaUte));

			}

			// temporaneamente
/*
			if (ricercaSuggerimenti.getBibliotecario().getCodice()!=null && ricercaSuggerimenti.getBibliotecario().getCodice().length()!=0 )
			{
				sql=this.struttura(sql);
				sql=sql + "  suggBibl.ute_var='" + ricercaSuggerimenti.getBibliotecario().getCodice() + "'" ;
			}
*/
			if (ricercaSuggerimenti.getTitolo()!=null && ricercaSuggerimenti.getTitolo().getCodice()!=null && ricercaSuggerimenti.getTitolo().getCodice().length()!=0 )
			{
				cr.add(Restrictions.eq("bid", ricercaSuggerimenti.getTitolo().getCodice().trim()));

			}


			/*
			if (ricercaSuggerimenti.getTitolo().getDescrizione()!=null && ricercaSuggerimenti.getTitolo().getDescrizione().length()!=0 )
			{
				sql=this.struttura(sql);
				sql=sql + " bibliot.cognome_nome like '%" + ricercaSuggerimenti.getTitolo().getDescrizione() +"%'";
				//like '" + ricercaSezioni.getDescrizioneSezione() +"%'"
			}
			List listaTit=(List)this.getTitolo(rs.getString("bid"),"");
			TitoloACQVO recTit = (TitoloACQVO) listaTit.get(0);
			if (rs.getString("bid")!=null) {
				String isbd=recTit.getIsbd();
				//isbd=recTit.getCodice();
				//rec.setCambioIsbn(recTit.getIsbd());
				rec.setTitolo(new StrutturaCombo(rs.getString("bid"),isbd));
			}

*/
			// ordinamento passato
			if (ricercaSuggerimenti.getOrdinamento()==null || (ricercaSuggerimenti.getOrdinamento()!=null &&  ricercaSuggerimenti.getOrdinamento().equals("")))
			{
				//sql=sql + " order by suggBibl.cd_bib,suggBibl.cod_sugg_bibl  ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_sugg_bibl"));
			}
			else if (ricercaSuggerimenti.getOrdinamento().equals("1"))
			{
				//sql=sql + " order by suggBibl.cd_bib,suggBibl.cod_sugg_bibl   ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_sugg_bibl"));
			}
			else if (ricercaSuggerimenti.getOrdinamento().equals("2"))
			{
				//sql=sql + " order by suggBibl.cd_bib, suggBibl.stato_sugg  ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("stato_sugg"));

			}
			else if (ricercaSuggerimenti.getOrdinamento().equals("3"))
			{
				//sql=sql + " order by suggBibl.cd_bib,  suggBibl.data_sugg_bibl desc";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("data_sugg_bibl"));

			}
			else if (ricercaSuggerimenti.getOrdinamento().equals("4"))
			{
				//suggBibl.cod_bibliotecario
				//sql=sql + " order by suggBibl.cd_bib, suggBibl.ute_var  ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("ute_var"));
			}
			else if (ricercaSuggerimenti.getOrdinamento().equals("5"))
			{
				//sql=sql + " order by suggBibl.cd_bib, sez.cod_sezione ";
				//cr.addOrder(Order.asc("cd_bib"));
				//cr.addOrder(Order.desc("id_sez_acquis_bibliografiche.cod_sezione"));
			}



			List<Tba_suggerimenti_bibliografici> results = cr.list();

			if (results!=null && results.size()>0 && ricercaSuggerimenti.getOrdinamento()!=null && ricercaSuggerimenti.getOrdinamento().equals("5"))
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

	private static class SezComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((Tba_suggerimenti_bibliografici) o1).getId_sez_acquis_bibliografiche().getCod_sezione().toLowerCase();
				String e2 = ((Tba_suggerimenti_bibliografici) o2).getId_sez_acquis_bibliografiche().getCod_sezione().toLowerCase();

				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	public boolean inserisciSuggerimentoHib(SuggerimentoVO suggerimento)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(suggerimento);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}
	public boolean modificaSuggerimentoHib(SuggerimentoVO suggerimento)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(suggerimento); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean cancellaSuggerimentoHib(SuggerimentoVO suggerimento)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(suggerimento); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

}
