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
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tba_richieste_offerta;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


public class Tba_richieste_offertaDao extends DaoManager {

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


	public List<Tba_richieste_offerta> getRicercaListaGareHib(ListaSuppGaraVO ricercaGare) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tba_richieste_offerta.class);

			Tbf_polo polo = new Tbf_polo();
			polo.setCd_polo(ricercaGare.getCodPolo());

			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			bib.setCd_biblioteca(ricercaGare.getCodBibl());
			bib.setCd_polo(polo);

			if (!ricercaGare.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc", 'N'));
				//cr.add(Restrictions.ne("fl_canc", 'S'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc", 'S'));
			}
			//cr.add(Restrictions.eq("cd_bib", bib));
			if (ricercaGare.getCodBibl()!=null && ricercaGare.getCodBibl().trim().length()>0 &&  ricercaGare.getCodPolo()!=null && ricercaGare.getCodPolo().trim().length()>0)
			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}

			if (ricercaGare.getCodRicOfferta()!=null && ricercaGare.getCodRicOfferta().length()!=0)
			{
				cr.add(Restrictions.eq("cod_rich_off", Long.valueOf(ricercaGare.getCodRicOfferta())));
			}

			if (ricercaGare.getBid()!=null && ricercaGare.getBid().getCodice().trim().length()!=0)
			{
				cr.add(Restrictions.eq("bid.bid", ricercaGare.getBid().getCodice()));
			}

			if (ricercaGare.getStatoRicOfferta()!=null && ricercaGare.getStatoRicOfferta().trim().length()!=0)
			{
				cr.add(Restrictions.eq("stato_rich_off", ricercaGare.getStatoRicOfferta().charAt(0)));
			}

			// ordinamento passato
			if (ricercaGare.getOrdinamento()==null || (ricercaGare.getOrdinamento()!=null &&  ricercaGare.getOrdinamento().equals("")))
			{
				//sql=sql + " order by richOff.cd_bib, richOff.cod_rich_off  ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("cod_rich_off"));
			}
/*				else if (ricercaGare.getOrdinamento().equals("1"))
			{
				sql=sql + " order by richOff.bid   ";
			}
*/
			else if (ricercaGare.getOrdinamento().equals("1"))
			{
// controllare il contenuto del campo
				//sql=sql + " order by ky_cles1_t,ky_cles2_t ";
			}

			else if (ricercaGare.getOrdinamento().equals("2"))
			{
				//sql=sql + " order by richOff.data_rich_off desc";
				cr.addOrder(Order.desc("data_rich_off"));
			}
			else if (ricercaGare.getOrdinamento().equals("3"))
			{
				//sql=sql + "  order by richOff.cd_bib, richOff.data_rich_off ";
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("data_rich_off"));
			}

			List<Tba_richieste_offerta> results = cr.list();

			if (results!=null && results.size()>0 && ricercaGare.getOrdinamento()!=null && ricercaGare.getOrdinamento().equals("1"))
			{
				Comparator comp=null;
				comp =new TitComparator();
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

	private static class TitComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			try {
				String e1 = ((Tba_richieste_offerta) o1).getBid().getKy_cles1_t() + ((Tba_richieste_offerta) o1).getBid().getKy_cles2_t();
				String e2 = ((Tba_richieste_offerta) o2).getBid().getKy_cles1_t() + ((Tba_richieste_offerta) o1).getBid().getKy_cles2_t();

				return e1.compareTo(e2);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}


	public boolean inserisciGaraHib(GaraVO gara)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(gara);
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}

	}
	public boolean modificaGaraHib(GaraVO gara)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(gara); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public boolean cancellaGaraHib(GaraVO gara)
	throws  DaoManagerException{
		boolean ret = false;
		try{
			Session session = this.getCurrentSession();
			session.saveOrUpdate(gara); // SaveOrUpdateCopy
			return ret = true;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

}
