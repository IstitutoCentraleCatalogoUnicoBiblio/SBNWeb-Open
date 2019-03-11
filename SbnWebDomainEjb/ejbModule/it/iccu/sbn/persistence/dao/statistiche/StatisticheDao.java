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
package it.iccu.sbn.persistence.dao.statistiche;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.statistiche.DettVarStatisticaVO;
import it.iccu.sbn.ejb.vo.statistiche.StatisticaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.statistiche.Tb_stat_parameter;
import it.iccu.sbn.polo.orm.statistiche.Tbf_config_statistiche;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class StatisticheDao extends DaoManager {

	public StatisticheDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Il metodo restituisce l'elenco delle Aree per l'utente nel carico sia riempita la mappa attivita con i permessi utente,
	// altrimenti restituisce tutte le aree.
	public List<Tbf_config_statistiche> cercaAreeUtente(Map attivita) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_config_statistiche.class);
			criteria.add(Restrictions.isNull("parent"));
			List<Tbf_config_statistiche> listaAree = criteria.list();
			if (attivita != null) {
				for (int i = 0; i < listaAree.size(); i++) {
					String attArea = listaAree.get(i).getCodice_attivita().trim();
					if (!attivita.containsKey(attArea)) {
						listaAree.remove(i);
						i--;
					}
				}
			}
			return listaAree;
		}
		catch (HibernateException he) {
			throw new DaoManagerException();
		}
	}

	public List<Tbf_config_statistiche> getListaStatistiche(String area) throws DaoManagerException {
		try {

			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_config_statistiche.class);
			criteria.add(Restrictions.eq("parent", area));
			List<Tbf_config_statistiche> listaStatistiche = criteria.list();
			return listaStatistiche;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_config_statistiche getStatistica(int idStatistica) throws DaoManagerException {
		Tbf_config_statistiche rec = null;
		try {
			Session session = this.getCurrentSession();
			rec = (Tbf_config_statistiche) session.createCriteria(Tbf_config_statistiche.class)
			.add(Restrictions.eq("id_stat", idStatistica)).uniqueResult();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
		return rec;
	}

	public List<Tb_stat_parameter> getDettVariabili(Tbf_config_statistiche idStatistica) throws DaoManagerException {
		try {

			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tb_stat_parameter.class);
			criteria.add(Restrictions.eq("id_stat", idStatistica));
			criteria.addOrder(Order.asc("nome"));
			List<Tb_stat_parameter> listaVariabili = criteria.list();
			return listaVariabili;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tb_stat_parameter getVariabile(int idStatistica, String nomeVar) throws DaoManagerException {
		Tb_stat_parameter rec = null;
		try {
			Session session = this.getCurrentSession();
			rec = (Tb_stat_parameter) session.createCriteria(Tb_stat_parameter.class)
			.add(Restrictions.eq("id_stat.id", idStatistica))
			.add(Restrictions.eq("nome", nomeVar)).uniqueResult();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
		return rec;
	}

	public List creaQuery(Tbf_config_statistiche stat, List<DettVarStatisticaVO> listaVar, StatisticaVO statistica)
	throws DaoManagerException	{
		List<Object[]> lista = null;
		try{
			Session session = this.getCurrentSession();

			setSessionCurrentCfg();
			StringBuffer queryHQL = new StringBuffer ();
			queryHQL.append(stat.getQuery());

			SQLQuery query = session.createSQLQuery(queryHQL.toString());
			if (listaVar != null){
				for (int i = 0; i < listaVar.size(); i++) {
					DettVarStatisticaVO recVar = listaVar.get(i);
//					if (recVar.getTipoVar() != null){
//						if (recVar.getTipoVar().equals("data")){
//							query.setTimestamp(recVar.getNomeVar(), DateUtil.toTimestamp(recVar.getValore()));
//						}
//					}
					if (recVar.getTipoVar() != null){
						if (recVar.getTipoVar().equals("data")){
							if (recVar.getValore() == null){
								query.setTimestamp(recVar.getNomeVar(), null);
							}else{
								if(!recVar.getValore().trim().equals("")){
									query.setTimestamp(recVar.getNomeVar(), DateUtil.toTimestamp(recVar.getValore()));
								}
							}
						}
					}
					if (recVar.getTipoVar() != null){
						if (recVar.getTipoVar().equals("string") ||
								recVar.getTipoVar().equals("combo") ||
								recVar.getTipoVar().equals("comboLibera")){
							if (recVar.getValore() == null){
								query.setString(recVar.getNomeVar(), null);
							}else{
								if(!recVar.getValore().trim().equals("")){
									query.setString(recVar.getNomeVar(), recVar.getValore());
								}else{
									query.setString(recVar.getNomeVar(), recVar.getValore().trim());
								}
							}
						}
					}
					if (recVar.getTipoVar() != null){
						if (recVar.getTipoVar().equals("int4")){
							if (recVar.getValore() != null){
								query.setInteger(recVar.getNomeVar(), Integer.parseInt(recVar.getValore()));
							}else{
								query.setBigInteger(recVar.getNomeVar(), null);
							}
						}
					}
					if (recVar.getTipoVar() != null){
						if (recVar.getTipoVar().equals("filtroListaBib")){
							if (recVar.getValore() == null || (recVar.getValore() != null && recVar.getValore().trim().equals(""))){
								recVar.setValore("");
								query.setString(recVar.getNomeVar(), recVar.getValore());
							}else{

								String [] biblioLista = recVar.getValore().split(",");
								List<String> lsBib = new ArrayList<String>();
								String bib = null;
								for (int ib = 0; ib < biblioLista.length; ib++) {
									bib = new String(biblioLista[ib]);
									if (bib.length() > 3){
										bib = bib.substring(1);
									}
									lsBib.add(bib);
								}
								query.setParameterList(recVar.getNomeVar(), lsBib);
							}
						}
					}
					//almaviva5_20151215
					if (recVar.getTipoVar() != null){
						if (recVar.getTipoVar().equals("filtroListaBibNoSplit")){
							if (recVar.getValore() == null || (recVar.getValore() != null && recVar.getValore().trim().equals(""))){
								recVar.setValore("");
								query.setString(recVar.getNomeVar(), recVar.getValore());
							}else{

								query.setString(recVar.getNomeVar(), recVar.getValore() );
							}
						}
					}
				}
			}
			lista = query.list();
			//gestioneCreazioneExcelPar
		}catch (HibernateException e){
			throw new DaoManagerException(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public List<String> getListaCodDescrDaTabGenerica(String bib, String valori1/*tabella*/,
			String valori2/*codice*/, String valori4/*bibTabella*/) throws DaoManagerException {

		String hql = "select t." + valori2+
				" from " + valori1 + " t " +
				"where t.fl_canc<>'S' " +
				"and t." + bib + "=:bib";

		Session session = getCurrentSession();
		Query q = session.createQuery(hql);
		q.setParameter("bib", valori4);

		return q.list();
	}

		public List<CodiceVO> getListaCodDescrDaTabGenerica(Tbf_biblioteca_in_polo bib, String valori1/*tabella*/,
				String valori2/*codice*/, String valori3/*descrizione*/, String valori4/*bibTabella*/) throws DaoManagerException {

			String hql = "select t." + valori2 + " a, t." + valori3 + " b " +
					"from " + valori1 + " t " +
					"where t.fl_canc<>'S' " +
					"and t." + valori4 + "=:bib";

			Session session = getCurrentSession();
			Query q = session.createQuery(hql);
			q.setParameter("bib", bib);

			return q.list();
		}

}
