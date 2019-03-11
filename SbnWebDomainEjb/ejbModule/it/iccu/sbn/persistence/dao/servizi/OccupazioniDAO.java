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
package it.iccu.sbn.persistence.dao.servizi;

import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.servizi.Tbl_occupazioni;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

public class OccupazioniDAO extends ServiziBaseDAO {

	private UtilitaDAO serviziHibDAO = new UtilitaDAO();


	public List getListaOccupazioni(Tbl_occupazioni occupazione, String ordinamento)
	throws DaoManagerException {
		try {
			//ordinamento="composto";
			if (ordinamento!=null && ordinamento.equals("composto"))
			{
				List occupazioni=new ArrayList();
/*				Session session = getCurrentSession();
				Query query = session.getNamedQuery("specicifitaTitoliOrdinamentoComposto");
				query.setEntity("bib", titoloStudio.getCd_biblioteca());
*/
				Session session = this.getCurrentSession();
				//String sqlString="select distinct specTit, cod from Tbl_specificita_titoli_studio specTit, Tb_codici cod " ;
				String sqlString="select  occ, cod.ds_tabella  from Tbl_occupazioni occ, Tb_codici cod " ;
				sqlString=sqlString +" where ";
				sqlString=sqlString +" occ.cd_biblioteca=:bib";
				//sqlString=sqlString +" specTit.cd_biblioteca='" + titoloStudio.getCd_biblioteca().getCd_biblioteca() +  "'";
				//sqlString=sqlString +" and specTit.cd_polo='" + titoloStudio.getCd_biblioteca().getCd_polo() +  "'";
				sqlString=sqlString +" and occ.fl_canc<>'S' ";
				sqlString=sqlString +" and cod.tp_tabella='RPRF' ";
				sqlString=sqlString +" and cod.cd_tabella=occ.professione ";
				if(occupazione.getOccupazione()!=null && occupazione.getOccupazione().trim().length()>0 )
				{
					sqlString=sqlString +" and upper(occ.occupazione)=:occu ";
				}
				if(String.valueOf(occupazione.getProfessione())!=null && String.valueOf(occupazione.getProfessione()).trim().length()>0 )
				{
					sqlString=sqlString +" and occ.professione=:prof ";
				}
				if(occupazione.getDescr()!=null && occupazione.getDescr().trim().length()>0 )
				{
					sqlString=sqlString +" and upper(occ.descr) like  :descr ";
				}

				sqlString=sqlString +" order by lower(cod.ds_tabella), lower(occ.descr)  ";

				Query query = session.createQuery(sqlString);
				query.setEntity("bib", occupazione.getCd_biblioteca());
				if(occupazione.getOccupazione()!=null && occupazione.getOccupazione().trim().length()>0 )
				{
					query.setString("occu", occupazione.getOccupazione().toUpperCase());
				}
				if(String.valueOf(occupazione.getProfessione())!=null &&  String.valueOf(occupazione.getProfessione()).trim().length()>0  )
				{
					query.setCharacter("prof", occupazione.getProfessione());
				}
				if(occupazione.getDescr()!=null && occupazione.getDescr().trim().length()>0 )
				{
					query.setString("descr", occupazione.getDescr().trim().toUpperCase() + "%");
				}

				//query3.setEntity("bob", bib);		// tipologia Tbf_biblioteca_in_polo
//				return query.list();
				List results  =  query.list();

				if (results!=null && results.size()>0)
				{
					for (int x = 0; x < results.size(); x++)
					{
						Object[] arrivo = (Object[]) results.get(x);
						Tbl_occupazioni occupt= (Tbl_occupazioni) arrivo[0];
						occupazioni.add(occupt);
						//recTit.setNumStandard((String) arrivo[1]); //  numero standard
					}
				}
				return occupazioni;

			} else	{
				Session session = this.getCurrentSession();
				Criteria criteria = session.createCriteria(Tbl_occupazioni.class);
				Example example = Example.create(occupazione);
				example.setPropertySelector(NOT_EMPTY);
				//example.enableLike(MatchMode.START);
				example.ignoreCase();

				criteria.add(example);
				criteria.add(Restrictions.eq("cd_biblioteca", occupazione.getCd_biblioteca()));
				criteria.add(Restrictions.ne("fl_canc", 'S'));

				if (ordinamento != null && !ordinamento.equals("")) {
					createCriteriaOrderIC(ordinamento, null, criteria);
				}

				return criteria.list();
			}


		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbl_occupazioni getOccupazioneById(int idOccupazione) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			return (Tbl_occupazioni) loadNoLazy(session, Tbl_occupazioni.class, new Integer(idOccupazione));

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public void cancellaOccupazioni(Integer id[], String uteVar)
	throws DaoManagerException {
		Tbl_occupazioni occupazione;

		for(int i=0; i<id.length; i++) {
			occupazione = this.getOccupazioneById(id[i]);
			if (occupazione!=null) {
				occupazione.setFl_canc('S');
				occupazione.setUte_var(uteVar);
			}
		}
	}


	public void aggiornaOccupazione(Tbl_occupazioni occupazione)
	throws DaoManagerException	{
		Session session = this.getCurrentSession();
		try {
			session.saveOrUpdate(occupazione);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}


	public Tbl_occupazioni getOccupazione(String codPolo, String codBib,
										String codProfessione, String codOccupazione)
	throws DaoManagerException	{
		Session session = this.getCurrentSession();

		Criteria criteria = session.createCriteria(Tbl_occupazioni.class);
		criteria.add(Restrictions.eq("cd_biblioteca", serviziHibDAO.getBibliotecaInPolo(codPolo, codBib)))
				.add(Restrictions.eq("professione",   codProfessione))
				.add(Restrictions.eq("occupazione",   codOccupazione));

		return (Tbl_occupazioni)criteria.uniqueResult();
	}

	public Tbl_occupazioni getOccupazioneDescr(String codPolo, String codBib,
			String codProfessione,String codOccupazione, String desOccupazione)
			throws DaoManagerException	{
			Session session = this.getCurrentSession();

			Criteria criteria = session.createCriteria(Tbl_occupazioni.class);
			criteria.add(Restrictions.eq("cd_biblioteca", serviziHibDAO.getBibliotecaInPolo(codPolo, codBib)))
				.add(Restrictions.eq("professione",   codProfessione))
				.add(Restrictions.or(Restrictions.eq("descr".trim(),   desOccupazione.trim()).ignoreCase(),
						Restrictions.eq("occupazione".trim(),   codOccupazione.trim())));
				//.add(Restrictions.eq("descr".trim(),   desOccupazione.trim()));

			return (Tbl_occupazioni)criteria.uniqueResult();
			}

}

