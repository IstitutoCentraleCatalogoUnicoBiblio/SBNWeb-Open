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

import it.iccu.sbn.ejb.vo.documentofisico.PossessoriLegameVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_possessore_provenienza;
import it.iccu.sbn.polo.orm.documentofisico.Trc_possessori_possessori;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class Trc_possessori_possessoriDao extends DaoManager {

	private static Log log = LogFactory.getLog(Trc_possessori_possessoriDao.class);

	Connection connection = null;

	public Trc_possessori_possessoriDao() {
		super();
		// TODO Auto-generated constructor stub
	}


	public List getListaRinvii (String codePID) throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_possessori_possessori.class);

			cr.add(Restrictions.eq("pid_base.id", codePID));
			cr.add(Restrictions.ne("fl_canc", 'S'));

			List<Trc_possessori_possessori> results = cr.list();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}

	public List getListaRinviiFigli (String codePID) throws DaoManagerException	{
		try {
			Session session = this.getCurrentSession();
			Criteria cr = session.createCriteria(Trc_possessori_possessori.class);

			cr.add(Restrictions.eq("pid_coll.id", codePID));
			cr.add(Restrictions.ne("fl_canc", 'S'));

			List<Trc_possessori_possessori> results = cr.list();
			return results;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);

		}
	}

	public String inserimentoLegamePossessore(PossessoriLegameVO possLegame,String uteFirma) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			Trc_possessori_possessori possPoss = new Trc_possessori_possessori();
			Tbc_possessore_provenienza possProvBase = new Tbc_possessore_provenienza();
			Tbc_possessore_provenienza possProvColl = new Tbc_possessore_provenienza();

			possProvBase.setPid(possLegame.getPidPadre());
			possProvColl.setPid(possLegame.getPidFiglio());

			possPoss.setPid_base(possProvBase);
			possPoss.setPid_coll(possProvColl);
			possPoss.setNota(possLegame.getNotaAlLegame());
			possPoss.setTp_legame(possLegame.getTipoLegame().charAt(0));
			possPoss.setFl_canc('N');
			possPoss.setTs_ins(ts);
			possPoss.setUte_ins(uteFirma);
			possPoss.setTs_var(ts);
			possPoss.setUte_var(uteFirma);

			session.saveOrUpdate(possPoss);

			return (ret = possLegame.getPidPadre());
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}


	public String cancellaLegamePossessore(String PidDaCancellare,String PidPadre,String uteFirma,Trc_possessori_possessori possPoss) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();

			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			//Trc_possessori_possessori possPoss = new Trc_possessori_possessori();
			Tbc_possessore_provenienza possProvBase = new Tbc_possessore_provenienza();
			Tbc_possessore_provenienza possProvColl = new Tbc_possessore_provenienza();

			possProvBase.setPid(PidPadre);
			possProvColl.setPid(PidDaCancellare);

			possPoss.setPid_base(possProvBase);
			possPoss.setPid_coll(possProvColl);
			possPoss.setFl_canc('S');
			possPoss.setTs_var(ts);
			possPoss.setUte_var(uteFirma);

			session.saveOrUpdate(possPoss);

			return (ret = PidPadre);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

	public List getPossessorePossessore(String PidFiglio,String PidPadre) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			Criteria cr = session.createCriteria(Trc_possessori_possessori.class);

			Tbc_possessore_provenienza possProvBase = new Tbc_possessore_provenienza();
			Tbc_possessore_provenienza possProvColl = new Tbc_possessore_provenienza();

			possProvBase.setPid(PidPadre);
			possProvColl.setPid(PidFiglio);

			cr.add(Restrictions.eq("pid_base", possProvBase));
			cr.add(Restrictions.eq("pid_coll", possProvColl));

			List<Trc_possessori_possessori> results = cr.list();
			return results;
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public String cancellaPossPoss(String pid ,String uteFirma) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());

			StringBuffer queryHQL = new StringBuffer ();
			queryHQL.append("update Trc_possessori_possessori possposs ");
			queryHQL.append("set possposs.fl_canc = 'S' ");
			queryHQL.append(", possposs.ute_var = '"+uteFirma+"'");
			queryHQL.append(", possposs.ts_var = :ts");

			if (!pid.trim().equals("") && pid.trim().length()>10)
				queryHQL.append(" where possposs.pid_coll.id in " + pid );
			else
				queryHQL.append(" where possposs.pid_coll.id = '" + pid + "'");

			Query query = session.createQuery(queryHQL.toString());
//			query.setEntity("ts", ts);

			query.setTimestamp("ts", new Date());

			return ret = ""+query.executeUpdate();
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public String modificaNotaLegame(String pid_padre,String pid_figlio,String nota ,String uteFirma ,Trc_possessori_possessori possPoss) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			//Trc_possessori_possessori possPoss = new Trc_possessori_possessori();
			Tbc_possessore_provenienza possProvBase = new Tbc_possessore_provenienza();
			Tbc_possessore_provenienza possProvColl = new Tbc_possessore_provenienza();

			possProvBase.setPid(pid_padre);
			possProvColl.setPid(pid_figlio);

			possPoss.setPid_base(possProvBase);
			possPoss.setPid_coll(possProvColl);

			possPoss.setNota(nota);
			possPoss.setTs_var(ts);
			possPoss.setUte_var(uteFirma);

			session.saveOrUpdate(possPoss);

			return (ret = pid_padre);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public String inserisciNuovoLegameDopoScambio(String pid_padre,String pid_figlio,String uteFirma ,Trc_possessori_possessori possPoss) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			//Trc_possessori_possessori possPoss = new Trc_possessori_possessori();
			Tbc_possessore_provenienza possProvBase = new Tbc_possessore_provenienza();
			Tbc_possessore_provenienza possProvColl = new Tbc_possessore_provenienza();

			possProvBase.setPid(pid_padre);
			possProvColl.setPid(pid_figlio);
			possPoss.setPid_base(possProvBase);
			possPoss.setPid_coll(possProvColl);
			possPoss.setTs_var(ts);
			possPoss.setUte_var(uteFirma);

			session.saveOrUpdate(possPoss);
			return (ret = pid_padre);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}
	public String eliminaLegamePossessore(String PidDaCancellare,String PidPadre,String uteFirma,Trc_possessori_possessori possPoss) throws DaoManagerException	{
		String ret = null;
		try{
			Session session = this.getCurrentSession();
			setSessionCurrentCfg();
			Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
			//Trc_possessori_possessori possPoss = new Trc_possessori_possessori();
			Tbc_possessore_provenienza possProvBase = new Tbc_possessore_provenienza();
			Tbc_possessore_provenienza possProvColl = new Tbc_possessore_provenienza();

			possProvBase.setPid(PidPadre);
			possProvColl.setPid(PidDaCancellare);

			possPoss.setPid_base(possProvBase);
			possPoss.setPid_coll(possProvColl);
//			possPoss.setFl_canc('S');
//			possPoss.setTs_var(ts);
//			possPoss.setUte_var(uteFirma);

			session.delete(possPoss);

			return (ret = PidPadre);
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		}
	}

}
