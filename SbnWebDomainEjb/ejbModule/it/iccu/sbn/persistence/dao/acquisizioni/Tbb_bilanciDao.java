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

import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.acquisizioni.Tbb_capitoli_bilanci;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


public class Tbb_bilanciDao extends DaoManager {

	public List<Tbb_capitoli_bilanci> getRicercaListaBilanciHib(ListaSuppBilancioVO ricercaBilanci) throws DaoManagerException {
		List<Tbb_capitoli_bilanci> results =null ;
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Tbb_capitoli_bilanci.class);

			Tbf_polo polo = new Tbf_polo();
			if (ricercaBilanci.getCodPolo()!=null)
			{
				polo.setCd_polo(ricercaBilanci.getCodPolo());
			}
			Tbf_biblioteca_in_polo bib = new Tbf_biblioteca_in_polo();
			if (ricercaBilanci.getCodBibl()!=null)
			{
				bib.setCd_biblioteca(ricercaBilanci.getCodBibl());
				if (polo!=null){
					bib.setCd_polo(polo);
				}
			}

/*			Tbb_bilanci impegn = new Tbb_bilanci();
			cr.add(Restrictions.eq("id_capitoli_bilanci", impegn.getId_capitoli_bilanci()));*/


			if (ricercaBilanci.getEsercizio()!=null && ricercaBilanci.getEsercizio().trim().length()!=0)
			{
				cr.add(Restrictions.eq("esercizio", BigDecimal.valueOf(Double.valueOf(ricercaBilanci.getEsercizio().trim()))));
			}

			if (ricercaBilanci.getCapitolo()!=null && ricercaBilanci.getCapitolo().length()!=0)
			{
				cr.add(Restrictions.eq("capitolo", BigDecimal.valueOf(Double.valueOf(ricercaBilanci.getCapitolo().trim()))));
			}

/*			if (ricercaBilanci.getImpegno()!=null && ricercaBilanci.getImpegno().length()!=0)
			{
				Tbb_bilanci impegn = new Tbb_bilanci();
				impegn.setCod_mat(ricercaBilanci.getImpegno());
				cr.add(Restrictions.eq("capitolo", Integer.parseInt(ricercaBilanci.getCapitolo().trim())));
			}
*/
			if (!ricercaBilanci.isFlag_canc())
			{
				cr.add(Restrictions.eq("fl_canc".trim(), 'N'));
			}
			else
			{
				cr.add(Restrictions.eq("fl_canc".trim(), 'S'));
			}
			//cr.add(Restrictions.eq("cd_bib", bib));
			if (ricercaBilanci.getCodBibl()!=null && ricercaBilanci.getCodBibl().trim().length()>0 &&  ricercaBilanci.getCodPolo()!=null && ricercaBilanci.getCodPolo().trim().length()>0)
			{
				cr.add(Restrictions.eq("cd_bib", bib));
			}

			if (ricercaBilanci.getOrdinamento()==null || (ricercaBilanci.getOrdinamento()!=null && ricercaBilanci.getOrdinamento().equals("")))
			{
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("esercizio"));
				cr.addOrder(Order.asc("capitolo"));
			}
			else if (ricercaBilanci.getOrdinamento().equals("1"))
			{
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.desc("esercizio"));
				cr.addOrder(Order.asc("capitolo"));
			}
			else if (ricercaBilanci.getOrdinamento().equals("2"))
			{
				cr.addOrder(Order.asc("cd_bib"));
				cr.addOrder(Order.asc("capitolo"));
				cr.addOrder(Order.desc("esercizio"));
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

	public boolean inserisciBilancioHib(BilancioVO bilancio)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(bilancio);
			return true;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}
	public boolean modificaBilancio(BilancioVO bilancio)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(bilancio); // SaveOrUpdateCopy
			return true;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public boolean cancellaBilancioHib(BilancioVO bilancio)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(bilancio); // SaveOrUpdateCopy
			return true;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public boolean esisteLegameRigaBilancio(int idBilancio, char cod_mat) throws DaoManagerException {
		try {
			// almaviva5_20130604 #4757
			Session session = this.getCurrentSession();

			Query q = session.getNamedQuery("esisteLegameRigaBilancio");
			q.setInteger("idBilancio", idBilancio);
			q.setCharacter("cod_mat", cod_mat);

			Number cnt = (Number) q.uniqueResult();
			return (cnt.intValue() > 0);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

}
