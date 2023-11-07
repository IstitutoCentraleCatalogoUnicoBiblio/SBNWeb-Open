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

import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriRicercaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza;
import it.iccu.sbn.polo.orm.documentofisico.Trc_poss_prov_inventari;
import it.iccu.sbn.web.keygenerator.GeneraChiave;
import it.iccu.sbn.web2.util.Constants;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class Tbc_possessore_provenienzaDao extends DaoManager {

	private static Log log = LogFactory.getLog(Tbc_possessore_provenienzaDao.class);

	Connection connection = null;

	public Tbc_possessore_provenienzaDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List getListaPossessori(String codPolo, String codBib,PossessoriRicercaVO possRic,GeneraChiave key) throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			List<Tbc_possessore_provenienza> results= null;

			Criteria cr = session.createCriteria(Tbc_possessore_provenienza.class,"poss_proven");
			cr.add(Restrictions.ne("fl_canc", 'S'));
			if (!possRic.getPid().trim().equals("")) {
				cr.add(Restrictions.eq("pid", possRic.getPid()));
			}

//			if (possRic.isSoloBib()) {
//				DetachedCriteria childCriteriaTrcPossProvInventari = DetachedCriteria.forClass( Trc_poss_prov_inventari.class , "child_Trc_poss_prv_inventari");
//				childCriteriaTrcPossProvInventari.setProjection( Property.forName("child_Trc_poss_prv_inventari.p.pid" ) );
//				childCriteriaTrcPossProvInventari.add(Property.forName("poss_proven.pid").eqProperty("child_Trc_poss_prv_inventari.p.pid") );
//				childCriteriaTrcPossProvInventari.add(Restrictions.ne("child_Trc_poss_prv_inventari.fl_canc", 'S'));
//
//				if (possRic.getPid()!=null && possRic.getPid().trim().length()!=0)	{
//					childCriteriaTrcPossProvInventari.add(Restrictions.eq("poss_proven.pid", possRic.getPid().trim().toUpperCase()));
//				}
//				cr.add(Subqueries.exists(childCriteriaTrcPossProvInventari));
//				cr.add(Property.forName( "poss_proven.pid" ).in( childCriteriaTrcPossProvInventari));
//			}


			if (possRic.isSoloBib()) {
				cr.add(filtroPossInvBiblioteca(codPolo, codBib));

				if (possRic.getPid()!=null && possRic.getPid().trim().length()!=0)	{
					cr.add(setPid(possRic.getPid().trim().toUpperCase()));
				}
			}
			if (!possRic.getFormaAutore().trim().equals("")){
				if (possRic.getFormaAutore().toLowerCase().trim().startsWith("r"))
					cr.add(Restrictions.eq("tp_forma_pp", 'R'));
//					queryHQL.append(" and possprov.tp_forma_pp = 'R'");
				else if (possRic.getFormaAutore().toLowerCase().trim().startsWith("a"))
					cr.add(Restrictions.eq("tp_forma_pp", 'A'));
//					queryHQL.append(" and possprov.tp_forma_pp = 'A'");
			}
			List<Character> checketti = new ArrayList<Character>();
			if (possRic.isChkTipoNomeA())
				checketti.add('A');
			if (possRic.isChkTipoNomeB() && !checketti.isEmpty())
				checketti.add('B');
			else if (possRic.isChkTipoNomeB() && checketti.isEmpty())
				checketti.add('B');

			if (possRic.isChkTipoNomeC() && !checketti.isEmpty())
				checketti.add('C');
			else if (possRic.isChkTipoNomeC() && checketti.isEmpty())
				checketti.add('C');

			if (possRic.isChkTipoNomeD() && !checketti.isEmpty())
				checketti.add('D');
			else if (possRic.isChkTipoNomeD() && checketti.isEmpty())
				checketti.add('D');

			if (possRic.isChkTipoNomeE() && !checketti.isEmpty())
				checketti.add('E');
			else if (possRic.isChkTipoNomeE() && checketti.isEmpty())
				checketti.add('E');

			if (possRic.isChkTipoNomeR() && !checketti.isEmpty())
				checketti.add('R');
			else if (possRic.isChkTipoNomeR() && checketti.isEmpty())
				checketti.add('R');

			if (possRic.isChkTipoNomeG() && !checketti.isEmpty())
				checketti.add('G');
			else if (possRic.isChkTipoNomeG() && checketti.isEmpty())
				checketti.add('G');
			if (checketti != null && checketti.size() > 0){
//				queryHQL.append(" and possprov.tp_nome_pp in ("+checketti+")");
				cr.add(Restrictions.in("tp_nome_pp", checketti));
			}

			if (possRic.getTipoOrdinamSelez().trim().equals("1")){
				cr.addOrder(Order.asc("pid").ignoreCase());
				//				queryHQL.append(" order by possprov.pid asc");
			}else if (possRic.getTipoOrdinamSelez().trim().equals("2")){
				// Bug Mantis Collaudo 5151 - Maggio 2013 - Correzione del campo di ordinamento delle sintetiche possessori per cui
				// l'ordinamento per nome deve essere fatto per ky_cles1_a e non per ds_nome_aut
//				cr.addOrder(Order.asc("ds_nome_aut"));
				cr.addOrder(Order.asc("ky_cles1_a"));
				cr.addOrder(Order.asc("tp_nome_pp"));
				//				queryHQL.append(" order by possprov.ds_nome_aut, possprov.tp_nome_pp asc");
			}else if (possRic.getTipoOrdinamSelez().trim().equals("3")){
				cr.addOrder(Order.asc("ts_var"));
				cr.addOrder(Order.asc("tp_nome_pp"));
				//				queryHQL.append(" order by possprov.ts_var,possprov.tp_nome_pp asc");
			}

					if (possRic.getTipoRicerca()!=null && possRic.getTipoRicerca().length()!=0 ){

						if (possRic.getTipoRicerca().equals("inizio") )	{
//							if (key.getKy_cles1_A().trim().length() > 50){
							if (key.getKy_cles2_A() != null){
								cr.add(Restrictions.ilike("ky_cles1_a", key.getKy_cles1_A()+"%"));
								cr.add(Restrictions.ilike("ky_cles2_a", key.getKy_cles2_A()+"%"));

								//queryHQL.append(" and possprov.ky_cles1_a like '" + key.getKy_cles1_A() + "%' and possprov.ky_cles2_a like '"+key.getKy_cles2_A()+"%' ");
							}else{
								cr.add(Restrictions.ilike("ky_cles1_a", key.getKy_cles1_A()+"%"));
//								queryHQL.append(" and possprov.ky_cles1_a like '" + key.getKy_cles1_A() + "%' ");
							}
						}else if (possRic.getTipoRicerca().equals("intero") ){
//							if (key.getKy_cles1_A().trim().length() > 50){
							if (key.getKy_cles2_A() != null){
								cr.add(Restrictions.eq("ky_cles1_a", key.getKy_cles1_A()));
								cr.add(Restrictions.eq("ky_cles2_a", key.getKy_cles2_A()));
//								queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a = '"+key.getKy_cles2_A()+"' ");
							}else{
								cr.add(Restrictions.eq("ky_cles1_a", key.getKy_cles1_A()));
								cr.add(Restrictions.isNull("ky_cles2_a"));
//								queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a is null ");
							}
						}else if (possRic.getTipoRicerca().equals("parole") ){

							if (possRic.getNome()!= null && !possRic.getNome().trim().equals("")){
								//introdotta per normalizzare la parola (non trasforma i caratteri diacritici)
//								GeneraChiave k = new GeneraChiave(possRic.getNome(),"");
//								try {
////									k.estraiChiavi(possRic.getTipoRicerca(),possRic.getNome());
//									k.calcoloClesToSearch(possRic.getNome());
//								} catch (Exception e) {
//									throw new DaoManagerException(e);
////									e.printStackTrace();
//								}
								String nome_manipolato = null;
								if (key.getKy_cles2_A() != null){
									nome_manipolato = key.getKy_cles1_A() + key.getKy_cles2_A();
								}else{
									if (key.getKy_cles1_A() != null){
									nome_manipolato = key.getKy_cles1_A();
									}
								}
								if (nome_manipolato.indexOf("'") != -1){
									int posApi = nome_manipolato.indexOf("'");
									nome_manipolato = nome_manipolato.substring(0, posApi+1) + "'" + nome_manipolato.substring(posApi+1);
								}

								String[] parole = nome_manipolato.split("\\s+");
								String tmp = parole[0];
								for (int i = 1; i < parole.length; i++)
									tmp = tmp + " & " + parole[i];

								DaoManager hibDao = new DaoManager();
								if (hibDao.getVersion().compareTo(ComparableVersion.of(Constants.POSTGRES_VERSION_83)) < 0) {
									cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector_ds_nome_aut @@ to_tsquery('default','" + tmp + "')"));
								} else {
									cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector_ds_nome_aut @@ to_tsquery('" + tmp + "')"));
								}
							} else	{
								String appo= possRic.getNome().trim();
								while (appo.indexOf("  ")>0) {
									appo = appo.replaceAll("  "," ");
								}

								String[] nomePossComposto=appo.split(" ");
								String parolePoss="";
								for (int i=0;  i <  nomePossComposto.length; i++) {
									if (i==0){
										parolePoss=nomePossComposto[0] ;
									}
									if (i>0){
										parolePoss=parolePoss + " & " + nomePossComposto[i] ;
									}
								}
								DaoManager hibDao = new DaoManager();
								if (hibDao.getVersion().compareTo(ComparableVersion.of(Constants.POSTGRES_VERSION_83)) < 0) {
									cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('default','" + parolePoss + "')"));
								} else {
									cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('" + parolePoss + "')"));
								}
							}
						}
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
//			StringBuffer queryHQL = new StringBuffer ();
//			queryHQL.append("from Tbc_possessore_provenienza possprov ");
//			if (possRic.isSoloBib()) {
//				queryHQL.append(" , Trc_poss_prov_inventari possprovinv , Tbc_inventario inventatio , Tbc_serie_inventariale serie ");
//			}
//			// inizio delle condizioni sulla tabella principale
//			queryHQL.append(" where possprov.fl_canc<>'S'");
//
//			if (!possRic.getPid().trim().equals(""))
//				queryHQL.append(" and upper(possprov.pid) = '" + possRic.getPid().toUpperCase() + "'");
//
//			if (possRic.getNome()!= null && !possRic.getNome().trim().equals("")){
//
//
//				String nome_manipolato=possRic.getNome().toUpperCase();
//				if (nome_manipolato.indexOf("'") != -1){
//					int posApi = nome_manipolato.indexOf("'");
//					nome_manipolato = nome_manipolato.substring(0, posApi+1) + "'" + nome_manipolato.substring(posApi+1);
//				}
//				/*
//					per inizio > 50
//					(KY_CLES1_A like SESSA GIOVANNI BATTISTA 2 SESSA MELCHIORRE 2 FRATE% and KY_CLES2_A like LLI%)), 0 = 0 ORDER BY this_.VID])
//
//					per intero > 50
//					(KY_CLES1_A=SESSA GIOVANNI BATTISTA 2 SESSA MELCHIORRE 2 FRATE and KY_CLES2_A=LLI)), KY_AUTEUR=SESSA, 0 = 0 ORDER BY this_.VID])
//
//					per inizio < 50
//					(KY_CLES1_A like S%)), 0 = 0 ORDER BY this_.VID])
//
//					per intero < 50
//					KY_CLES1_A=S and KY_CLES2_A is null))				 */
//				//gestione delle chiavi per la ricerca
//				if (possRic.getTipoRicerca().trim().equals("inizio")) {
//					if (key.getKy_cles1_A().trim().length() > 50){
//						queryHQL.append(" and possprov.ky_cles1_a like '" + key.getKy_cles1_A() + "%' and possprov.ky_cles2_a like '"+key.getKy_cles2_A()+"%' ");
//					}else{
//						queryHQL.append(" and possprov.ky_cles1_a like '" + key.getKy_cles1_A() + "%' ");
//					}
//				}else if (possRic.getTipoRicerca().trim().equals("intero")) {
//					if (key.getKy_cles1_A().trim().length() > 50){
//						queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a = '"+key.getKy_cles2_A()+"' ");
//					}else{
//						queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a is null ");
//					}
//				}else if (possRic.getTipoRicerca().trim().equals("parole")) {
//					String[] parole = nome_manipolato.split("\\s+");
//					String tmp = parole[0];
//					for (int i = 1; i < parole.length; i++)
//						tmp = tmp + " & " + parole[i];
////					if ((new DaoManager()).getVersion().compareTo("8.3") < 0){
////						queryHQL.append(" and possprov.tidx_vector_ds_nome_aut @@ to_tsquery('default','" + tmp + "')");
////					}else{
////						queryHQL.append(" and possprov.tidx_vector_ds_nome_aut @@ to_tsquery('" + tmp + "')");
////					}
//					queryHQL.append(" and tsearch_contains(tidx_vector_ds_nome_aut, '" + tmp + "')");
//
//				}
//			}
//			//fine gestione delle chiavi per la ricerca
//
//				if (possRic.getTipoRicerca().trim().equals("inizio")) {
//					if (key.getKy_cles2_A() != null && !key.getKy_cles2_A().trim().equals("")){
//						queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a like '%"+key.getKy_cles2_A()+"%' ");
//					} else if (key.getKy_cles2_A() == null || key.getKy_cles2_A().trim().equals("")){
//						queryHQL.append(" and possprov.ky_cles1_a like '%" + key.getKy_cles1_A() + "%' ");
//					}
//					//queryHQL.append(" and upper(possprov.ds_nome_aut) like '" + nome_manipolato + "%'");
//				} else if (possRic.getTipoRicerca().trim().equals("intero")) {
//					if (key.getKy_cles2_A() != null && !key.getKy_cles2_A().trim().equals("")){
//						queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a = '"+key.getKy_cles2_A()+"' ");
//					} else if (key.getKy_cles2_A() == null || key.getKy_cles2_A().trim().equals("")){
//						queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a is null ");
//					}
//					//queryHQL.append(" and upper(possprov.ds_nome_aut) = '" + nome_manipolato + "'");
//				} else if (possRic.getTipoRicerca().trim().equals("parole")) {
//					queryHQL.append(" and upper(possprov.ds_nome_aut) like '%" + nome_manipolato + "%'");
//				}

//			if (!possRic.getFormaAutore().trim().equals("")){
//				if (possRic.getFormaAutore().toLowerCase().trim().startsWith("r"))
//					queryHQL.append(" and possprov.tp_forma_pp = 'R'");
//				else if (possRic.getFormaAutore().toLowerCase().trim().startsWith("a"))
//					queryHQL.append(" and possprov.tp_forma_pp = 'A'");
//			}
//
//			String checketti="";
//			if (possRic.isChkTipoNomeA())
//				checketti+="'A'";
//
//			if (possRic.isChkTipoNomeB() && !checketti.trim().equals(""))
//				checketti+=",'B'";
//			else if (possRic.isChkTipoNomeB() && checketti.trim().equals(""))
//				checketti+="'B'";
//
//			if (possRic.isChkTipoNomeC() && !checketti.trim().equals(""))
//				checketti+=",'C'";
//			else if (possRic.isChkTipoNomeC() && checketti.trim().equals(""))
//				checketti+="'C'";
//
//			if (possRic.isChkTipoNomeD() && !checketti.trim().equals(""))
//				checketti+=",'D'";
//			else if (possRic.isChkTipoNomeD() && checketti.trim().equals(""))
//				checketti+="'D'";
//
//			if (possRic.isChkTipoNomeE() && !checketti.trim().equals(""))
//				checketti+=",'E'";
//			else if (possRic.isChkTipoNomeE() && checketti.trim().equals(""))
//				checketti+="'E'";
//
//			if (possRic.isChkTipoNomeR() && !checketti.trim().equals(""))
//				checketti+=",'R'";
//			else if (possRic.isChkTipoNomeR() && checketti.trim().equals(""))
//				checketti+="'R'";
//
//			if (possRic.isChkTipoNomeG() && !checketti.trim().equals(""))
//				checketti+=",'G'";
//			else if (possRic.isChkTipoNomeG() && checketti.trim().equals(""))
//				checketti+="'G'";
//			if (!checketti.trim().equals(""))
//				queryHQL.append(" and possprov.tp_nome_pp in ("+checketti+")");
////			 fine delle condizioni sulla tabella principale

//			if (possRic.isSoloBib()) {
//				//, Tbc_inventario inventatio , Tbc_serie_inventariale serie
//				queryHQL.append(" and possprov.pid = possprovinv.p ");
//				queryHQL.append(" and possprovinv.fl_canc<>'S' ");
////				queryHQL.append(" and possprov.pid = possprovinv.pid ");
//			}
//			if (possRic.getTipoOrdinamSelez().trim().equals("1"))
//				queryHQL.append(" order by possprov.pid asc");
//			else if (possRic.getTipoOrdinamSelez().trim().equals("2"))
//				queryHQL.append(" order by possprov.ds_nome_aut, possprov.tp_nome_pp asc");
//			else if (possRic.getTipoOrdinamSelez().trim().equals("3"))
//				queryHQL.append(" order by possprov.ts_var,possprov.tp_nome_pp asc");
//
//			log.info("Ricerca Possessori Select -> "+queryHQL.toString());
//			Query query = session.createQuery(queryHQL.toString());
//
//
//			results = (List<Tbc_possessore_provenienza>) query.list();
//			return results;
//		} catch (HibernateException he) {
//			throw new DaoManagerException(he);
//		}
	}

	public List getListaPossessori_1(String codPolo, String codBib,PossessoriRicercaVO possRic,GeneraChiave key) throws DaoManagerException	{
		//lista possessori con esclusione di se stesso
		try {
			Session session = this.getCurrentSession();
			List<Tbc_possessore_provenienza> results= null;

			Criteria cr = session.createCriteria(Tbc_possessore_provenienza.class,"poss_proven");
			cr.add(Restrictions.ne("fl_canc", 'S'));
			if (!possRic.getPid().trim().equals("")) {
				cr.add(Restrictions.eq("pid", possRic.getPid()));
			}

			if (possRic.isSoloBib()) {
				DetachedCriteria childCriteriaTrcPossProvInventari = DetachedCriteria.forClass( Trc_poss_prov_inventari.class , "child_Trc_poss_prv_inventari");
				childCriteriaTrcPossProvInventari.setProjection( Property.forName("child_Trc_poss_prv_inventari.p.pid" ) );
				childCriteriaTrcPossProvInventari.add(Property.forName("poss_proven.pid").neProperty("child_Trc_poss_prv_inventari.p.pid") );
				childCriteriaTrcPossProvInventari.add(Restrictions.ne("child_Trc_poss_prv_inventari.fl_canc", 'S'));

				if (possRic.getPid()!=null && possRic.getPid().trim().length()!=0)	{
					childCriteriaTrcPossProvInventari.add(Restrictions.eq("poss_proven.pid", possRic.getPid().trim().toUpperCase()));
				}
				cr.add(Subqueries.exists(childCriteriaTrcPossProvInventari));
				cr.add(Property.forName( "poss_proven.pid" ).in( childCriteriaTrcPossProvInventari));
			}
			if (!possRic.getFormaAutore().trim().equals("")){
				if (possRic.getFormaAutore().toLowerCase().trim().startsWith("r"))
					cr.add(Restrictions.eq("tp_forma_pp", 'R'));
//					queryHQL.append(" and possprov.tp_forma_pp = 'R'");
				else if (possRic.getFormaAutore().toLowerCase().trim().startsWith("a"))
					cr.add(Restrictions.eq("tp_forma_pp", 'A'));
//					queryHQL.append(" and possprov.tp_forma_pp = 'A'");
			}
			List<Character> checketti = new ArrayList<Character>();
			if (possRic.isChkTipoNomeA())
				checketti.add('A');
			if (possRic.isChkTipoNomeB() && !checketti.isEmpty())
				checketti.add('B');
			else if (possRic.isChkTipoNomeB() && checketti.isEmpty())
				checketti.add('B');

			if (possRic.isChkTipoNomeC() && !checketti.isEmpty())
				checketti.add('C');
			else if (possRic.isChkTipoNomeC() && checketti.isEmpty())
				checketti.add('C');

			if (possRic.isChkTipoNomeD() && !checketti.isEmpty())
				checketti.add('D');
			else if (possRic.isChkTipoNomeD() && checketti.isEmpty())
				checketti.add('D');

			if (possRic.isChkTipoNomeE() && !checketti.isEmpty())
				checketti.add('E');
			else if (possRic.isChkTipoNomeE() && checketti.isEmpty())
				checketti.add('E');

			if (possRic.isChkTipoNomeR() && !checketti.isEmpty())
				checketti.add('R');
			else if (possRic.isChkTipoNomeR() && checketti.isEmpty())
				checketti.add('R');

			if (possRic.isChkTipoNomeG() && !checketti.isEmpty())
				checketti.add('G');
			else if (possRic.isChkTipoNomeG() && checketti.isEmpty())
				checketti.add('G');
			if (checketti != null && checketti.size() > 0){
//				queryHQL.append(" and possprov.tp_nome_pp in ("+checketti+")");
				cr.add(Restrictions.in("tp_nome_pp", checketti));
			}

			if (possRic.getTipoOrdinamSelez().trim().equals("1")){
				cr.addOrder(Order.asc("pid").ignoreCase());
				//				queryHQL.append(" order by possprov.pid asc");
			}else if (possRic.getTipoOrdinamSelez().trim().equals("2")){
				cr.addOrder(Order.asc("ds_nome_aut"));
				cr.addOrder(Order.asc("tp_nome_pp"));
				//				queryHQL.append(" order by possprov.ds_nome_aut, possprov.tp_nome_pp asc");
			}else if (possRic.getTipoOrdinamSelez().trim().equals("3")){
				cr.addOrder(Order.asc("ts_var"));
				cr.addOrder(Order.asc("tp_nome_pp"));
				//				queryHQL.append(" order by possprov.ts_var,possprov.tp_nome_pp asc");
			}

					if (possRic.getTipoRicerca()!=null && possRic.getTipoRicerca().length()!=0 ){

						if (possRic.getTipoRicerca().equals("inizio") )	{
//							if (key.getKy_cles1_A().trim().length() > 50){
							if (key.getKy_cles2_A() != null){
								cr.add(Restrictions.ilike("ky_cles1_a", key.getKy_cles1_A()+"%"));
								cr.add(Restrictions.ilike("ky_cles2_a", key.getKy_cles2_A()+"%"));

								//queryHQL.append(" and possprov.ky_cles1_a like '" + key.getKy_cles1_A() + "%' and possprov.ky_cles2_a like '"+key.getKy_cles2_A()+"%' ");
							}else{
								cr.add(Restrictions.ilike("ky_cles1_a", key.getKy_cles1_A()+"%"));
//								queryHQL.append(" and possprov.ky_cles1_a like '" + key.getKy_cles1_A() + "%' ");
							}
						}else if (possRic.getTipoRicerca().equals("intero") ){
//							if (key.getKy_cles1_A().trim().length() > 50){
							if (key.getKy_cles2_A() != null){
								cr.add(Restrictions.eq("ky_cles1_a", key.getKy_cles1_A()));
								cr.add(Restrictions.eq("ky_cles2_a", key.getKy_cles2_A()));
//								queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a = '"+key.getKy_cles2_A()+"' ");
							}else{
								cr.add(Restrictions.eq("ky_cles1_a", key.getKy_cles1_A()));
								cr.add(Restrictions.isNull("ky_cles2_a"));
//								queryHQL.append(" and possprov.ky_cles1_a = '" + key.getKy_cles1_A() + "' and possprov.ky_cles2_a is null ");
							}
						}else if (possRic.getTipoRicerca().equals("parole") ){

							if (possRic.getNome()!= null && !possRic.getNome().trim().equals("")){
								//introdotta per normalizzare la parola (non trasforma i caratteri diacritici)
								GeneraChiave k = new GeneraChiave(possRic.getNome(),"");
								try {
									k.estraiChiavi(possRic.getTipoRicerca(),possRic.getNome());
								} catch (Exception e) {
									e.printStackTrace();
								}
								String nome_manipolato = null;
								if (k.getKy_cles2_A() != null){
									nome_manipolato = k.getKy_cles1_A() + k.getKy_cles2_A();
								}else{
									nome_manipolato = k.getKy_cles1_A();
								}
								if (nome_manipolato.indexOf("'") != -1){
									int posApi = nome_manipolato.indexOf("'");
									nome_manipolato = nome_manipolato.substring(0, posApi+1) + "'" + nome_manipolato.substring(posApi+1);
								}

								String[] parole = nome_manipolato.split("\\s+");
								String tmp = parole[0];
								for (int i = 1; i < parole.length; i++)
									tmp = tmp + " & " + parole[i];

								DaoManager hibDao = new DaoManager();
								if (hibDao.getVersion().compareTo(ComparableVersion.of(Constants.POSTGRES_VERSION_83)) < 0) {
									cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector_ds_nome_aut @@ to_tsquery('default','" + tmp + "')"));
								} else {
									cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector_ds_nome_aut @@ to_tsquery('" + tmp + "')"));
								}
							} else	{
								String appo= possRic.getNome().trim();
								while (appo.indexOf("  ")>0) {
									appo = appo.replaceAll("  "," ");
								}

								String[] nomePossComposto=appo.split(" ");
								String parolePoss="";
								for (int i=0;  i <  nomePossComposto.length; i++) {
									if (i==0){
										parolePoss=nomePossComposto[0] ;
									}
									if (i>0){
										parolePoss=parolePoss + " & " + nomePossComposto[i] ;
									}
								}
								DaoManager hibDao = new DaoManager();
								if (hibDao.getVersion().compareTo(ComparableVersion.of(Constants.POSTGRES_VERSION_83)) < 0) {
									cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('default','" + parolePoss + "')"));
								} else {
									cr.add(Restrictions.sqlRestriction("{alias}.tidx_vector @@ to_tsquery('" + parolePoss + "')"));
								}
							}
						}
					}

					cr.add(Restrictions.ne("pid", possRic.getPid()));
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

	public String calcolaCodPID(String codPolo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			String codePossNextVal = "" ;
			String PID = "";
			Query query = session.getNamedQuery("Progressivo_possessori_sequenceNextVal");

			codePossNextVal = "" + ((Integer) query.uniqueResult()).intValue();
			if ( codePossNextVal != null ){
				PID = codPolo + "P"  ;
				if ( (PID.length() + codePossNextVal.length())<10 ){
					codePossNextVal = filler(codePossNextVal , (PID.length() + codePossNextVal.length()) );
				}
				PID = PID + codePossNextVal ;
			}
			return  PID;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public int getCountPossessoreByPid (String codePID) throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_possessore_provenienza.class);
			cr.add(Restrictions.eq("pid", codePID));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			int results = ((List<Tbc_possessore_provenienza>) cr.list()).size();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}

	public Tbc_possessore_provenienza getPossessoreByPid (String codePID) throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbc_possessore_provenienza.class);
			cr.add(Restrictions.eq("pid", codePID));
			cr.add(Restrictions.ne("fl_canc", 'S'));
			return (Tbc_possessore_provenienza) cr.uniqueResult();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public String inserimentoPossessore(PossessoriDettaglioVO possDett,String uteFirma,GeneraChiave key)
	throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();
			Timestamp ts = DaoManager.now();
			Tbc_possessore_provenienza possProv = new Tbc_possessore_provenienza();

			possProv.setPid(possDett.getPid());
			possProv.setTp_nome_pp(possDett.getTipoTitolo().charAt(0));
			possProv.setCd_livello(possDett.getLivello());
			possProv.setTp_forma_pp(possDett.getForma().charAt(0));
			possProv.setDs_nome_aut(possDett.getNome());
			possProv.setNote(possDett.getNota());
			possProv.setFl_canc('N');
			//possProv.setFl_speciale('N');
			possProv.setTs_ins(ts);
			possProv.setUte_ins(uteFirma);
			possProv.setTs_var(ts);
			possProv.setUte_var(uteFirma);

//			GeneraChiave key = new GeneraChiave();
//			key.estraiChiavi(possDett.getTipoTitolo(),possDett.getNome());
//
//			//chiavi calcolate
			possProv.setKy_cautun(key.getKy_cautun());
			possProv.setKy_auteur(key.getKy_auteur());
			possProv.setKy_el1(key.getKy_el1());
			possProv.setKy_el2(key.getKy_el2());
			possProv.setKy_el3(key.getKy_el3());
			possProv.setKy_el4(key.getKy_el4());
			possProv.setKy_el5(key.getKy_el5());
			possProv.setKy_cles1_a(key.getKy_cles1_A());
			possProv.setKy_cles2_a(key.getKy_cles2_A());
//
			session.saveOrUpdate(possProv);
			return (ret = possDett.getPid());
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public String modificaPossessore(PossessoriDettaglioVO possDett,String uteFirma,GeneraChiave key,Tbc_possessore_provenienza possProv) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();
			Timestamp ts = DaoManager.now();
			possProv.setTp_nome_pp(possDett.getTipoTitolo().charAt(0));
			possProv.setCd_livello(possDett.getLivello());
			possProv.setTp_forma_pp(possDett.getForma().charAt(0));
			possProv.setDs_nome_aut(possDett.getNome());
			possProv.setNote(possDett.getNota());
			possProv.setFl_canc('N');
			possProv.setFl_speciale('N');
			possProv.setTs_var(ts);
			possProv.setUte_var(uteFirma);

//			//chiavi calcolate
			possProv.setKy_cautun(key.getKy_cautun());
			possProv.setKy_auteur(key.getKy_auteur());
			possProv.setKy_el1(key.getKy_el1a());
			possProv.setKy_el2(key.getKy_el2a());
			possProv.setKy_el3(key.getKy_el3());
			possProv.setKy_el4(key.getKy_el4());
			possProv.setKy_el5(key.getKy_el5());
			possProv.setKy_cles1_a(key.getKy_cles1_A());
			possProv.setKy_cles2_a(key.getKy_cles2_A());
//
			session.update(possProv);

			return (ret = possDett.getPid());
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	private String filler (String temp , int diff) {
		String fill = "";
		int zero = 10 - diff ;
		for (int x=0;x<zero;x++){
			fill += "0";
		}
		fill = fill + temp ;
		return fill ;
	}

	public List getListaPossessoriPerCodiciPid(String codiciPid) throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			List<Tbc_possessore_provenienza> results= null;
			StringBuffer queryHQL = new StringBuffer ();
			queryHQL.append("from Tbc_possessore_provenienza possprov ");

			// inizio delle condizioni sulla tabella principale
			queryHQL.append(" where possprov.fl_canc<>'S'");

			if (!codiciPid.trim().equals(""))
				queryHQL.append(" and possprov.pid in " + codiciPid + "");

			Query query = session.createQuery(queryHQL.toString());

			results = query.list();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}

	public String cancellaPossessore(String pid ,String uteFirma) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();

			setSessionCurrentCfg();
			StringBuffer queryHQL = new StringBuffer ();
			queryHQL.append("update Tbc_possessore_provenienza possprov ");
			queryHQL.append("set possprov.fl_canc = 'S' ");
			queryHQL.append(", possprov.ute_var = '"+uteFirma+"'");
			queryHQL.append(", possprov.ts_var = :ts");

			if (!pid.trim().equals("") && pid.trim().length()>10)
				queryHQL.append(" where possprov.pid in " + pid );
			else
				queryHQL.append(" where possprov.pid = '" + pid + "'");

			Query query = session.createQuery(queryHQL.toString());

			query.setTimestamp("ts", new Date());

			return ret = ""+query.executeUpdate();
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public String modificaPossessorePerScambioForma(Tbc_possessore_provenienza possProv) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			Timestamp ts = DaoManager.now();
			// imposto la data di variazione
			setSessionCurrentCfg();
			possProv.setTs_var(ts);
			session.saveOrUpdate(possProv);
			return (ret = "");
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

    private Criterion filtroPossInvBiblioteca(String codPolo, String codBib) {
        StringBuilder sql = new StringBuilder();
        sql.append(" exists (select 1 from trc_poss_prov_inventari possinv ");
        sql.append("where possinv.pid={alias}.pid ");
        sql.append("and possinv.fl_canc<>'S' ");
        sql.append("and possinv.cd_polo='").append(codPolo).append("' ");
        sql.append("and possinv.cd_biblioteca='").append(codBib).append("')");

        return Restrictions.sqlRestriction(sql.toString());
  }

    private Criterion setPid(String pid) {
        StringBuilder sql = new StringBuilder();
        sql.append(" {alias}.pid='").append(pid).append("'");

        return Restrictions.sqlRestriction(sql.toString());
  }

	public List<String> getPossessori() throws DaoManagerException	{
		try {
			final Session session = this.getCurrentSession();
			final Criteria c = session.createCriteria(Tbc_possessore_provenienza.class);
			c.setProjection(Projections.property("pid"));
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.addOrder(Order.asc("pid"));
			List<String> ret = c.list();
			return ret;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
