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
import it.iccu.sbn.polo.orm.servizi.Tbl_specificita_titoli_studio;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

public class TitoliDiStudioDAO extends ServiziBaseDAO {

	private UtilitaDAO serviziHibDAO = new UtilitaDAO();



	public void aggiorna(Tbl_specificita_titoli_studio titoloStudio)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		try {
			session.saveOrUpdate(titoloStudio);
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}


	public Tbl_specificita_titoli_studio getSpecificitaTitoloStudio(String codPolo, String codBib, String titoloStudio, String specTitolo)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		Criteria criteria = session.createCriteria(Tbl_specificita_titoli_studio.class);
		criteria.add(Restrictions.eq("cd_biblioteca", serviziHibDAO.getBibliotecaInPolo(codPolo, codBib)))
				.add(Restrictions.eq("tit_studio",   titoloStudio))
				.add(Restrictions.eq("specif_tit",   specTitolo));

		return (Tbl_specificita_titoli_studio)criteria.uniqueResult();
	}

	public Tbl_specificita_titoli_studio getSpecificitaTitoloStudioDescr(String codPolo, String codBib, String titoloStudio, String specTitolo, String specTitoloDescr)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

		Criteria criteria = session.createCriteria(Tbl_specificita_titoli_studio.class);
		criteria.add(Restrictions.eq("cd_biblioteca", serviziHibDAO.getBibliotecaInPolo(codPolo, codBib)));
		criteria.add(Restrictions.eq("tit_studio",   titoloStudio));
		criteria.add(Restrictions.or(Restrictions.eq("descr".trim(),   specTitoloDescr.trim()).ignoreCase(),
						Restrictions.eq("specif_tit".trim(),   specTitolo.trim()).ignoreCase()));

		return (Tbl_specificita_titoli_studio)criteria.uniqueResult();
	}



	public List getListaTitoliStudio(Tbl_specificita_titoli_studio titoloStudio, String ordinamento) throws DaoManagerException {
		try {
			//ordinamento="composto";
			if (ordinamento!=null && ordinamento.equals("composto"))
			{
				List titoliStud=new ArrayList();
/*				Session session = getCurrentSession();
				Query query = session.getNamedQuery("specicifitaTitoliOrdinamentoComposto");
				query.setEntity("bib", titoloStudio.getCd_biblioteca());
*/
				Session session = this.getCurrentSession();
				//String sqlString="select distinct specTit, cod from Tbl_specificita_titoli_studio specTit, Tb_codici cod " ;
				String sqlString="select specTit, cod.ds_tabella from Tbl_specificita_titoli_studio specTit, Tb_codici cod " ;
				sqlString=sqlString +" where ";
				sqlString=sqlString +" specTit.cd_biblioteca=:bib";
				//sqlString=sqlString +" specTit.cd_biblioteca='" + titoloStudio.getCd_biblioteca().getCd_biblioteca() +  "'";
				//sqlString=sqlString +" and specTit.cd_polo='" + titoloStudio.getCd_biblioteca().getCd_polo() +  "'";
				sqlString=sqlString +" and specTit.fl_canc<>'S' ";
				sqlString=sqlString +" and cod.tp_tabella='RTST' ";
				sqlString=sqlString +" and cod.cd_tabella=specTit.tit_studio ";

				if(titoloStudio.getSpecif_tit()!=null && titoloStudio.getSpecif_tit().trim().length()>0 )
				{
					sqlString=sqlString +"and upper(specTit.specif_tit)=:spect";
				}
				if(String.valueOf(titoloStudio.getTit_studio())!=null && String.valueOf(titoloStudio.getTit_studio()).trim().length()>0 )
				{
					sqlString=sqlString +"and specTit.tit_studio=:tito";
				}
				if(titoloStudio.getDescr()!=null && titoloStudio.getDescr().trim().length()>0 )
				{
					sqlString=sqlString +"and upper(specTit.descr) like  :descr";
				}


				sqlString=sqlString +" order by  lower(cod.ds_tabella), lower(specTit.descr) ";


				Query query = session.createQuery(sqlString);
				query.setEntity("bib", titoloStudio.getCd_biblioteca());


				if(titoloStudio.getSpecif_tit()!=null && titoloStudio.getSpecif_tit().trim().length()>0 )
				{
					query.setString("spect", titoloStudio.getSpecif_tit().toUpperCase());
				}
				if(String.valueOf(titoloStudio.getTit_studio())!=null && String.valueOf(titoloStudio.getTit_studio()).trim().length()>0 )
				{
					query.setCharacter("tito", titoloStudio.getTit_studio());
				}
				if(titoloStudio.getDescr()!=null && titoloStudio.getDescr().trim().length()>0 )
				{
					query.setString("descr", titoloStudio.getDescr().trim().toUpperCase() + "%");
				}


				//query3.setEntity("bob", bib);		// tipologia Tbf_biblioteca_in_polo
//				return query.list();
				List results  =  query.list();

				if (results!=null && results.size()>0)
				{
					for (int x = 0; x < results.size(); x++)
					{
						Object[] arrivo = (Object[]) results.get(x);
						Tbl_specificita_titoli_studio tit= (Tbl_specificita_titoli_studio) arrivo[0];
						titoliStud.add(tit);
						//recTit.setNumStandard((String) arrivo[1]); //  numero standard
					}
				}
				return titoliStud;

			} else	{
				Session session = this.getCurrentSession();
				Criteria criteria = session.createCriteria(Tbl_specificita_titoli_studio.class);
				Example example = Example.create(titoloStudio);
				example.setPropertySelector(NOT_EMPTY);
				//example.enableLike(MatchMode.ANYWHERE);
				criteria.add(example);
				criteria.add(Restrictions.eq("cd_biblioteca", titoloStudio.getCd_biblioteca()));
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

	public Tbl_specificita_titoli_studio getTitoloStudioById(int idTitoloStudio)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			return (Tbl_specificita_titoli_studio) loadNoLazy(session,
					Tbl_specificita_titoli_studio.class, new Integer(
							idTitoloStudio));

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void cancellaSpecTitoloStudio(Integer[] id, String uteVar)
	throws DaoManagerException {
		Tbl_specificita_titoli_studio speTitoloStudio;

		for (int i=0; i<id.length; i++) {
			speTitoloStudio = this.getTitoloStudioById(id[i]);
			if (speTitoloStudio!=null)	{
				speTitoloStudio.setFl_canc('S');
				speTitoloStudio.setUte_var(uteVar);
			}
		}
	}

}
