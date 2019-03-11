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
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.servizi.Tbl_documenti_lettori;
import it.iccu.sbn.polo.orm.servizi.Tbl_utenti;

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




public class Tbl_documenti_lettoriAcqDao extends DaoManager {

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


	public List<Tbl_documenti_lettori> getRicercaListaDocumentiHib(ListaSuppDocumentoVO ricercaDocumenti) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbl_documenti_lettori.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaDocumenti.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaDocumenti.getCodBibl());
			bib.setCd_polo(polo);

			if (!ricercaDocumenti.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}
			cr.add(Restrictions.eq("tipo_doc_lett", 'S')); // solo per suggerimenti
			if (ricercaDocumenti.getCodBibl()!=null && ricercaDocumenti.getCodBibl().trim().length()>0 &&  ricercaDocumenti.getCodPolo()!=null && ricercaDocumenti.getCodPolo().trim().length()>0)
			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}

			if (ricercaDocumenti.getCodDocumento() !=null &&  ricercaDocumenti.getCodDocumento().length()!=0)
			{
				cr.add(Restrictions.eq("cod_doc_lett", Long.parseLong(ricercaDocumenti.getCodDocumento().trim())));
			}
			if (ricercaDocumenti.getTitolo()!=null && ricercaDocumenti.getTitolo().getCodice()!=null && ricercaDocumenti.getTitolo().getCodice().length()!=0 )
			{
				cr.add(Restrictions.eq("bid", ricercaDocumenti.getTitolo().getCodice()));
			}

			if (ricercaDocumenti.getIdDocList()!=null && ricercaDocumenti.getIdDocList().size()!=0 )
			{
				cr.add(Restrictions.in("id_documenti_lettore",  ricercaDocumenti.getIdDocList()));
			}

			if (ricercaDocumenti.getStatoSuggerimentoDocumento()!=null && ricercaDocumenti.getStatoSuggerimentoDocumento().trim().length()!=0)
			{
				cr.add(Restrictions.eq("stato_sugg",  ricercaDocumenti.getStatoSuggerimentoDocumento()));
			}
			if (ricercaDocumenti.getUtente()!=null && ricercaDocumenti.getUtente().getCodice1()!=null && ricercaDocumenti.getUtente().getCodice1().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cd_bib",  ricercaDocumenti.getUtente().getCodice1().trim()));
			}
			if (ricercaDocumenti.getUtente()!=null && ricercaDocumenti.getUtente().getCodice2()!=null && ricercaDocumenti.getUtente().getCodice2().trim().length()!=0)
			{
				//cr.add(Restrictions.eq("id_utenti.cod_utente",  ricercaDocumenti.getUtente().getCodice2().trim()));
				DetachedCriteria childCriteriaUte = DetachedCriteria.forClass(Tbl_utenti.class , "child_ute");
				childCriteriaUte.setProjection(Property.forName("child_ute.id_utenti"));
				//childCriteriaForn.add(Property.forName("cod_fornitore.cod_fornitore").eqProperty("child_forn.cod_fornitore") );
				childCriteriaUte.add(Restrictions.eq("child_ute.cod_utente",ricercaDocumenti.getUtente().getCodice2().trim().trim()));
				childCriteriaUte.add(Restrictions.eq("child_ute.fl_canc", 'N'));
				cr.add(Subqueries.exists(childCriteriaUte));
				cr.add(Property.forName("id_utenti").in( childCriteriaUte));
			}
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Boolean dataInizioBool=false;
			Boolean dataFineBool=false;

			if ((ricercaDocumenti.getDataSuggerimentoDocDa()!=null && ricercaDocumenti.getDataSuggerimentoDocDa().length()!=0) || (ricercaDocumenti.getDataSuggerimentoDocA()!=null && ricercaDocumenti.getDataSuggerimentoDocA().length()!=0))
			{
				//cr.add(Restrictions.between("data_ord", Integer.parseInt(ricercaOrdini.getDataOrdineDa()),Integer.parseInt(ricercaOrdini.getDataOrdineA())));
				Date startDate=new Date();
				try {
					startDate =formato.parse(ricercaDocumenti.getDataSuggerimentoDocDa());
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
					endDate = formato.parse(ricercaDocumenti.getDataSuggerimentoDocA());
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
					//cr.add(Restrictions.between("ts_ins", new Date(startDate.getTime()), new Date(endDate.getTime())));
					//cr.add(Restrictions.between(Restrictions.sqlRestriction(" CAST({alias}.ts_ins AS date"), new Date(startDate.getTime()), new Date(endDate.getTime())));
					cr.add(Restrictions.sqlRestriction(" CAST({alias}.ts_ins AS date) BETWEEN '"+ new Date(startDate.getTime()) + "' AND '" + new Date(endDate.getTime())+ "'"));
				}
				else if (dataInizioBool)
				{
					//cr.add(Restrictions.ge("ts_ins", new Date(startDate.getTime())));
					cr.add(Restrictions.sqlRestriction(" CAST({alias}.ts_ins AS date) >= '"+ new Date(startDate.getTime()) + "'"));

				}
				else if (dataFineBool)
				{
					//cr.add(Restrictions.le("ts_ins", new Date(endDate.getTime())));
					cr.add(Restrictions.sqlRestriction(" CAST({alias}.ts_ins AS date) <= '"+ new Date(endDate.getTime()) + "'"));
				}

			}

/*				if (ricercaSuggerimenti.getTitolo().getCodice()!=null && ricercaSuggerimenti.getTitolo().getCodice().length()!=0 )
			{
				sql=this.struttura(sql);
				sql=sql + " suggBibl.bid='" + ricercaSuggerimenti.getTitolo().getCodice() +"'" ;
			}*/

/*				if (ricercaDocumenti.getTitolo()!=null && ricercaDocumenti.getTitolo().getDescrizione()!=null && ricercaDocumenti.getTitolo().getDescrizione().length()!=0 )
			{
				sql=this.struttura(sql);
				sql=sql + " docLet.titolo like '%" + ricercaDocumenti.getTitolo().getDescrizione() +"%'";
				//like '" + ricercaSezioni.getDescrizioneSezione() +"%'"
			}
*/
			if (ricercaDocumenti.getTitolo()!=null && ricercaDocumenti.getTitolo().getDescrizione()!=null && ricercaDocumenti.getTitolo().getDescrizione().length()!=0 )
			{
					//** tidx_vector @@ 'aaa'::tsquery; .trim().toUpperCase()
					//fare do while
					String appo=ricercaDocumenti.getTitolo().getDescrizione().trim();
					// eliminazione degli spazi di troppo
					while (appo.indexOf("  ")>0) {
						appo = appo.replaceAll("  "," ");
					}

					String[] nomeTitComposto=appo.split(" ");
					// String tit_primaParte=isbd_composto[0];
					// 'parola1 & parola2'
					// 'parola1 | parola2'
					String paroleTit="";
					for (int i=0;  i <  nomeTitComposto.length; i++)
					{
						if (i==0)
						{
							paroleTit=nomeTitComposto[0] ;
						}
						if (i>0)
						{
							paroleTit=paroleTit + " & " + nomeTitComposto[i] ;
						}

					}
					DaoManager hibDao = new DaoManager();
					 if (hibDao.getVersion().compareTo("8.3") < 0)
					 {
						 //sql=sql + " tidx_vector @@ to_tsquery('default', '" + paroleForn +"')";
						cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('default','" + paroleTit + "')"));
					 }
					 else
					 {
						 //sql=sql + " tidx_vector @@ to_tsquery('" + paroleForn +"')";
						cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('" + paroleTit + "')"));
					 }
			}


/*				List listaTit=(List)this.getTitolo(rs.getString("bid"),"");
			TitoloACQVO recTit = (TitoloACQVO) listaTit.get(0);
			if (rs.getString("bid")!=null) {
				String isbd=recTit.getIsbd();
				//isbd=recTit.getCodice();
				//rec.setCambioIsbn(recTit.getIsbd());
				rec.setTitolo(new StrutturaCombo(rs.getString("bid"),isbd));
			}
*/

			// ordinamento passato
			if (ricercaDocumenti.getOrdinamento()==null || (ricercaDocumenti.getOrdinamento()!=null && ricercaDocumenti.getOrdinamento().equals("")))
			{
				//sql=sql + " order by docLet.cd_bib,docLet.cod_doc_lett  ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_doc_lett"));
			}
			else if (ricercaDocumenti.getOrdinamento().equals("1"))
			{
				//sql=sql + " order by docLet.cd_bib,docLet.cod_doc_lett  ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_doc_lett"));
			}
			else if (ricercaDocumenti.getOrdinamento().equals("2"))
			{
				//sql=sql + " order by docLet.cd_bib, docLet.stato_sugg  ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("stato_sugg"));
			}
			else if (ricercaDocumenti.getOrdinamento().equals("3"))
			{
				//sql=sql + " order by docLet.cd_bib, ts_ins desc ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("ts_ins"));
			}




			List<Tbl_documenti_lettori> results = cr.list();
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

	public boolean inserisciDocumentoHib(DocumentoVO documento)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(documento);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}
	public boolean modificaDocumentoHib(DocumentoVO documento)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(documento); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean cancellaDocumentoHib(DocumentoVO documento)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(documento); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

}
