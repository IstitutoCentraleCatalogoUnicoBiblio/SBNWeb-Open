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
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_sez_acquis_bibliografiche;
import it.iccu.sbn.polo.orm.acquisizioni.Tra_sez_acq_storico;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


public class Tba_sez_acquis_bibliograficheDao extends DaoManager {
	//private static final int INNER_JOIN = org.hibernate.sql.JoinFragment.INNER_JOIN;
    //private static final int FULL_JOIN = org.hibernate.sql.JoinFragment.FULL_JOIN;
    //private static final int LEFT_JOIN = org.hibernate.sql.JoinFragment.LEFT_OUTER_JOIN;

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
	public List<SezioneVO> getRicercaListaSezioniDaCancHib (ListaSuppSezioneVO ricercaSezioni) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_sez_acquis_bibliografiche.class,"se");


			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaSezioni.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaSezioni.getCodBibl());
			bib.setCd_polo(polo);

			//Tba_ordini ord = new Tba_ordini();
			//ord.setFl_canc('N');

			//Tba_suggerimenti_bibliografici sugg=new Tba_suggerimenti_bibliografici();
			//sugg.setFl_canc('N');
			//sugg.setStato_sugg('R'); // deve essere <> R

			if (ricercaSezioni.getCodiceSezione()!=null && ricercaSezioni.getCodiceSezione().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cod_sezione", ricercaSezioni.getCodiceSezione().trim().toUpperCase()));
				//cr.add(Restrictions.eq("cod_sezione", "NAR"));
			}
			if (ricercaSezioni.getDescrizioneSezione()!=null && ricercaSezioni.getDescrizioneSezione().length()!=0)
			{
				cr.add(Restrictions.like("nome", ricercaSezioni.getDescrizioneSezione().trim().toUpperCase()+"%"));
			}
			if (!ricercaSezioni.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}

			//cr.add(Restrictions.eq("cd_bib", bib));
			if (ricercaSezioni.getCodBibl()!=null && ricercaSezioni.getCodBibl().trim().length()>0 &&  ricercaSezioni.getCodPolo()!=null && ricercaSezioni.getCodPolo().trim().length()>0)
			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}

			// presenza  di legami con ordini in qualsiasi stato oppure presenza  di legami con suggerimenti (non rifiutati TESTATO NEL BEAN)
			cr.add(Restrictions.or(Restrictions.isNotEmpty("Tba_ordini"),
					Restrictions.isNotEmpty("Tba_suggerimenti_bibliografici")
			));

			cr.addOrder(Order.asc("cod_sezione"));

			List<SezioneVO> results = cr.list();

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


	public List<Tba_sez_acquis_bibliografiche> getRicercaListaSezioniHib(ListaSuppSezioneVO ricercaSezioni) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_sez_acquis_bibliografiche.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaSezioni.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaSezioni.getCodBibl());
			bib.setCd_polo(polo);
			// aggiungere per la gestione della data di validità (se si cerca per id o codice specifico tale controllo è da escludersi)
			if (ricercaSezioni.getIdSezione()==0 && (ricercaSezioni.getCodiceSezione()==null || (ricercaSezioni.getCodiceSezione()!=null && ricercaSezioni.getCodiceSezione().trim().length()==0)))
			{
				if(ricercaSezioni.isChiusura()) // si vogliono solo quelle chiuse
				{
					//sql=sql + "(sez.data_val is not null and  sez.data_val<=(SELECT CURRENT_DATE ))";
					cr.add(Restrictions.and(Restrictions.isNotNull("data_val"),Restrictions.le("data_val", new Date(System.currentTimeMillis()))));
				}
				else
				{
					//sql=sql + "(sez.data_val is null or sez.data_val>(SELECT CURRENT_DATE ))";
					cr.add(Restrictions.or(Restrictions.isNull("data_val"),Restrictions.gt("data_val", new Date(System.currentTimeMillis()))));
				}
			}
			if (ricercaSezioni.getIdSezione()!=0)
			{
				//sql=sql + " sez.id_sez_acquis_bibliografiche=" + ricercaSezioni.getIdSezione();
				cr.add(Restrictions.eq("id_sez_acquis_bibliografiche", ricercaSezioni.getIdSezione()));
			}

			if (ricercaSezioni.getCodiceSezione()!=null && ricercaSezioni.getCodiceSezione().trim().length()!=0)
			{
				cr.add(Restrictions.eq("cod_sezione", ricercaSezioni.getCodiceSezione().trim().toUpperCase()));
			}
			if (ricercaSezioni.getDescrizioneSezione()!=null && ricercaSezioni.getDescrizioneSezione().length()!=0)
			{
				cr.add(Restrictions.ilike("nome", (ricercaSezioni.getDescrizioneSezione().trim()+"%").toUpperCase()));
			}
			if (!ricercaSezioni.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}
			//cr.add(Restrictions.eq("cd_bib", bib));
			if (ricercaSezioni.getCodBibl()!=null && ricercaSezioni.getCodBibl().trim().length()>0 &&  ricercaSezioni.getCodPolo()!=null && ricercaSezioni.getCodPolo().trim().length()>0)
			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}

			// ordinamento passato
			if (ricercaSezioni.getOrdinamento()==null || (ricercaSezioni.getOrdinamento()!=null && ricercaSezioni.getOrdinamento().equals("")))
			{
				cr.addOrder(Order.asc("cod_sezione"));
			}
			else if (ricercaSezioni.getOrdinamento().equals("1"))
			{
				cr.addOrder(Order.asc("cod_sezione"));
			}
			else if (ricercaSezioni.getOrdinamento().equals("2"))
			{
				//sql=sql + " order by sez.nome  ";
				cr.addOrder(Order.asc("nome"));
			}

			List<Tba_sez_acquis_bibliografiche> results = cr.list();
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

	public boolean inserisciSezioneHib(Tba_sez_acquis_bibliografiche sezione)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(sezione);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}
	public boolean  modificaSezioneHib(Tba_sez_acquis_bibliografiche sezione)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(sezione); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean  cancellaSezioneHib(Tba_sez_acquis_bibliografiche sezione)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(sezione); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List<Tra_sez_acq_storico> getRicercaListaSezioneStoriaHib(ListaSuppSezioneVO ricercaSezioni) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tra_sez_acq_storico.class);
			if (ricercaSezioni.getIdSezione()!=0)
			{
				//sql=sql + " sez.id_sez_acquis_bibliografiche=" + ricercaSezioni.getIdSezione();
				cr.add(Restrictions.eq("id_sez_acquis_bibliografiche", ricercaSezioni.getIdSezione()));
			}
			cr.add(Restrictions.eq("fl_canc", 'N'));
			cr.addOrder(Order.asc("data_var_bdg"));
			List<Tra_sez_acq_storico> results = cr.list();
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



	public boolean inserisciSezioneStoriaHib(Tra_sez_acq_storico sezioneStoria)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(sezioneStoria);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}


}
