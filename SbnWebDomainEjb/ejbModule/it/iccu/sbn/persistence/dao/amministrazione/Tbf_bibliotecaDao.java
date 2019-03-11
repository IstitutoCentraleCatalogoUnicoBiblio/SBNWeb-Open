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
package it.iccu.sbn.persistence.dao.amministrazione;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO.BibliotecaType;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO.RicercaBibliotecaType;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.CheckVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_sem;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;
import it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_affiliate;
import it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_gruppo_attivita_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_profilo_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Trf_utente_professionale_biblioteca;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_sistemi_classi_biblioteche;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_soggettari_biblioteche;
import it.iccu.sbn.polo.orm.gestionesemantica.Tr_thesauri_biblioteche;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Isil;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.type.Type;

import ch.lambdaj.Lambda;

public class Tbf_bibliotecaDao extends DaoManager {

	private static Logger _logger = Logger.getLogger(Tbf_bibliotecaDao.class);

	@SuppressWarnings("serial")
	private static final class NotEmptyPropertySelector implements
			PropertySelector {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2746273942392862307L;

		public boolean include(Object object, String propertyName, Type type) {
			if (object == null)
				return false;
			if ((object instanceof Number)
					&& ((Number) object).longValue() == 0L)
				return false;
			if ((object instanceof Character)
					&& ((Character) object).charValue() == 0)
				return false;
			if ((object instanceof String)
					&& ((String) object).trim().equals(""))
				return false;
			return true;
		}

	}

	private static final PropertySelector NOT_EMPTY = new NotEmptyPropertySelector();

	public Tbf_bibliotecaDao() {
		super();
	}

	public List<Tbf_biblioteca> selectAll() throws DaoManagerException
	{
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_biblioteca.class);
		criteria.addOrder(Order.asc("cd_ana_biblioteca"))
				.addOrder(Order.asc("nom_biblioteca"));
		criteria.add(Restrictions.ne("cd_bib", Constants.ROOT_BIB));
		List<Tbf_biblioteca> ret = criteria.list();
		return  ret;
	}

	public List<Tbf_biblioteca> select(Tbf_biblioteca filtri)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca.class);
			Example example = Example.create(filtri).excludeZeroes()
					.enableLike(MatchMode.ANYWHERE).setPropertySelector(
							NOT_EMPTY);
			criteria.add(example);
			criteria.addOrder(Order.asc("cd_ana_biblioteca")).addOrder(
					Order.asc("nom_biblioteca"));
			criteria.add(Restrictions.ne("cd_bib", Constants.ROOT_BIB));
			List<Tbf_biblioteca> ret = criteria
					.list();
			return ret;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void save(Tbf_biblioteca biblioteca) throws DaoManagerException {
		Session session = this.getCurrentSession();
		session.save(biblioteca);
	}

	public void update(Tbf_biblioteca biblioteca) throws DaoManagerException {
		Session session = this.getCurrentSession();
		session.merge(biblioteca);
	}

	public Tbf_biblioteca getBiblioteca(int idBiblioteca)
	throws DaoManagerException {
		Session session = this.getCurrentSession();

	 	try {
			return (Tbf_biblioteca) loadNoLazy(session, Tbf_biblioteca.class, new Integer(idBiblioteca));
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	class ProgComparator implements Comparator
	{
	public final int compare ( Object a, Object b )
	   {

		Tbf_attivita attivita = (Tbf_attivita)a;
		int sa = attivita.getPrg_ordimanento();
		attivita = (Tbf_attivita)b;
		int sb = attivita.getPrg_ordimanento();
//		return( (sa).compareToIgnoreCase( sb )); // Ascending
		if (sa > sb)
			return 1;
		else if (sa < sb)
			return -1;
		else
			return 0;

	   } // end compare
	} // end class StringComparator

	public List<Tbf_attivita> loadCodiciAttivitaPolo() throws DaoManagerException  {

		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo)criteria.uniqueResult();
			Tbf_gruppo_attivita gruppo = polo.getId_gruppo_attivita();
			criteria = session.createCriteria(Trf_gruppo_attivita_polo.class);
			criteria.add(Restrictions.eq("id_gruppo_attivita_polo", gruppo));
			List<Trf_gruppo_attivita_polo> ret = criteria.list();
			List<Tbf_attivita> output = new ArrayList<Tbf_attivita>();
			for (int i = 0; i < ret.size(); i++) {
				output.add(ret.get(i).getId_attivita_polo().getCd_attivita());
			}
			Collections.sort(output, new ProgComparator());
			return output;
		}
		catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

	public int getIdParametro(String codBib) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", codBib));
			Tbf_biblioteca_in_polo parametroBib = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			return parametroBib.getId_parametro().getId_parametro();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbf_biblioteca> getElencoBiblioteche() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.ne("cd_biblioteca", Constants.ROOT_BIB));
			List<Tbf_biblioteca_in_polo> elenco = criteria.list();
			List<Tbf_biblioteca> output = new ArrayList<Tbf_biblioteca>();
			for (int i = 0; i < elenco.size(); i++) {
				output.add(elenco.get(i).getId_biblioteca());
			}
			return output;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_parametro getParametri(int idParametro) throws DaoManagerException  {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_parametro.class);
			criteria.add(Restrictions.eq("id_parametro", idParametro));
			return (Tbf_parametro) criteria.uniqueResult();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List getAuthorities() throws DaoManagerException  {

		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tb_codici.class);
		cr.add(Expression.eq("tp_tabella", "TPAU"));
		List ret = cr.list();
		return ret;
	}

	public List getMateriali() throws DaoManagerException  {

		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tb_codici.class);
		cr.add(Expression.eq("tp_tabella", "MATE"));
		List ret = cr.list();
		return ret;
	}

	public int getLivelloParametro(int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametro = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, idParametro);
			return Integer.parseInt(parametro.getCd_livello());
		}
		catch (HibernateException he) {
			throw new DaoManagerException();
		}
	}

	public List loadAttivitaGruppoBiblioteca(String codBib) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", codBib));
			Tbf_biblioteca_in_polo bib = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			criteria = session.createCriteria(Trf_gruppo_attivita_polo.class);
			criteria.add(Restrictions.eq("id_gruppo_attivita_polo", bib.getId_gruppo_attivita_polo()));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaAttivitaDelGruppo(int idGruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session.createQuery("delete from Trf_gruppo_attivita_polo where id_gruppo_attivita_polo.id = " + idGruppo);
			query.executeUpdate();
			clearCache("Trf_gruppo_attivita_polo");
			session.flush();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciAttivitaDelGruppo(List<String> elencoAttivita, int idGruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
//			Criteria criteria = session.createCriteria(Tbf_gruppo_attivita.class);
//			criteria.add(Restrictions.eq("id_gruppo_attivita_polo", idGruppo));
//			List record = criteria.list();
//			Tbf_gruppo_attivita gruppo = (Tbf_gruppo_attivita)record.get(0);
			Tbf_gruppo_attivita gruppo = (Tbf_gruppo_attivita)loadNoLazy(session, Tbf_gruppo_attivita.class, idGruppo);
			for (int i = 0; i < elencoAttivita.size(); i++) {
				Tbf_attivita attivitaDB = (Tbf_attivita)loadNoLazy(session, Tbf_attivita.class, elencoAttivita.get(i));
				Criteria c = session.createCriteria(Trf_attivita_polo.class);
				c.add(Restrictions.eq("cd_attivita", attivitaDB));
				Trf_attivita_polo attivita = (Trf_attivita_polo)c.uniqueResult();
				if (attivita != null) {
					Trf_gruppo_attivita_polo tabella = new Trf_gruppo_attivita_polo();
					tabella.setId_attivita_polo(attivita);
					tabella.setId_gruppo_attivita_polo(gruppo);
					tabella.setFl_include('S');
					session.saveOrUpdate(tabella);
				}
				else {
					_logger.error("L'attivita con codice " + elencoAttivita.get(i) +
							" non è stata inserita nel profilo della biblioteca. Controllare la tabella Trf_attivita_polo!");
				}
				session.evict(attivitaDB);
			}
			session.flush();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int creaGruppoAttivitaBiblioteca(String codBib) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo)criteria.uniqueResult();
			int gruppoPolo = polo.getId_gruppo_attivita().getId_gruppo_attivita_polo();
			criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", codBib));
			Tbf_biblioteca_in_polo bib = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			if (bib.getId_gruppo_attivita_polo().getId_gruppo_attivita_polo() == gruppoPolo) {
				Tbf_gruppo_attivita gruppo = new Tbf_gruppo_attivita();
				gruppo.setCd_polo(polo);
				gruppo.setDs_name("Profilo biblioteca " + codBib);
				session.save(gruppo);
				return gruppo.getId_gruppo_attivita_polo();
			}
			else
				return bib.getId_gruppo_attivita_polo().getId_gruppo_attivita_polo();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void attivaProfiloAttivita(String codiceBib, int idGruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", codiceBib));
			Tbf_biblioteca_in_polo bib = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			Tbf_gruppo_attivita gruppoAtt = (Tbf_gruppo_attivita)loadNoLazy(session, Tbf_gruppo_attivita.class, idGruppo);
			bib.setId_gruppo_attivita_polo(gruppoAtt);
			session.saveOrUpdate(bib);
			session.flush();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int creaProfiloParametroPolo(String codiceBib, String codiceUtente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", codiceBib));
			Tbf_biblioteca_in_polo bib = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo parametroPolo = (Tbf_polo)criteria.uniqueResult();
			if ((parametroPolo.getId_parametro().getId_parametro() == bib.getId_parametro().getId_parametro()) ||
					bib.getId_parametro().getId_parametro() == 1) {
				String firma = getFirmaUtente(Integer.parseInt(codiceUtente));
				Tbf_parametro parametro = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, parametroPolo.getId_parametro().getId_parametro());
				Tbf_parametro profilo = new Tbf_parametro();
				profilo.setCd_livello(parametro.getCd_livello());
				profilo.setTp_ret_doc(parametro.getTp_ret_doc());
				profilo.setTp_all_pref(parametro.getTp_all_pref());
				profilo.setCd_liv_ade(parametro.getCd_liv_ade());
				profilo.setFl_spogli(parametro.getFl_spogli());
				profilo.setFl_aut_superflui(parametro.getFl_aut_superflui());
				profilo.setUte_ins(firma);
				profilo.setUte_var(firma);
				profilo.setFl_canc(parametro.getFl_canc());
				Timestamp tempo = DaoManager.now();
				profilo.setTs_ins(tempo);
				profilo.setTs_var(tempo);
				profilo.setSololocale(parametro.getSololocale());
				session.save(profilo);
				return profilo.getId_parametro();
			}
			return bib.getId_parametro().getId_parametro();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciParAuth(List<GruppoParametriVO> elencoGruppiParametri, int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametroDB = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, idParametro);
			for (int i = 0; i < elencoGruppiParametri.size(); i++) {
				GruppoParametriVO gruppo = elencoGruppiParametri.get(i);
				Tbf_par_auth authority = new Tbf_par_auth();
				authority.setCd_par_auth(gruppo.getCodice());
				authority.setId_parametro(parametroDB);
				authority.setCd_contr_sim("1");
				for (int j = 0; j < gruppo.getElencoParametri().size(); j++) {
					ParametroVO parametro = gruppo.getElencoParametri().get(j);
					if (parametro.getDescrizione().endsWith("abil")) {
						authority.setTp_abil_auth(parametro.getSelezione().charAt(0));
					}
					else if (parametro.getDescrizione().endsWith("abil_legame")) {
						authority.setFl_abil_legame((parametro.getSelezione().charAt(0)));
					}
					else if (parametro.getDescrizione().endsWith("leg_auth")) {
						authority.setFl_leg_auth((parametro.getSelezione().charAt(0)));
					}
					else if (parametro.getDescrizione().endsWith("livello")) {
						authority.setCd_livello((parametro.getSelezione().trim()));
					}
					else if (parametro.getDescrizione().endsWith("abil_forzat")) {
						authority.setFl_abil_forzat((parametro.getSelezione().charAt(0)));
					}
					else if (parametro.getDescrizione().endsWith("sololocale")) {
						authority.setSololocale((parametro.getSelezione().charAt(0)));
					}
				}
				session.saveOrUpdate(authority);
			}
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciParMat(List<GruppoParametriVO> elencoGruppiParametri, int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametroDB = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, idParametro);
			for (int i = 0; i < elencoGruppiParametri.size(); i++) {
				GruppoParametriVO gruppo = elencoGruppiParametri.get(i);
				Tbf_par_mat materiale = new Tbf_par_mat();
				materiale.setCd_par_mat(gruppo.getCodice().charAt(0));
				materiale.setId_parametro(parametroDB);
				materiale.setCd_contr_sim("1");
				for (int j = 0; j < gruppo.getElencoParametri().size(); j++) {
					ParametroVO parametro = gruppo.getElencoParametri().get(j);
					if (parametro.getDescrizione().endsWith("abil")) {
						materiale.setTp_abilitaz(parametro.getSelezione().charAt(0));
					}
					else if (parametro.getDescrizione().endsWith("livello")) {
						materiale.setCd_livello((parametro.getSelezione().trim()));
					}
					else if (parametro.getDescrizione().endsWith("abil_forzat")) {
						materiale.setFl_abil_forzat((parametro.getSelezione().charAt(0)));
					}
					else if (parametro.getDescrizione().endsWith("sololocale")) {
						materiale.setSololocale((parametro.getSelezione().charAt(0)));
					}
				}
				session.saveOrUpdate(materiale);
			}
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int inserisciParSem(List<GruppoParametriVO> elencoGruppiParametri, int idParametro, String codBib, String codiceUtente) throws Exception {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_biblioteca_in_polo.class);
			c.add(Restrictions.eq("cd_biblioteca", codBib));
			Tbf_biblioteca_in_polo bib = (Tbf_biblioteca_in_polo)c.uniqueResult();
			Tbf_parametro parametroDB = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, idParametro);
			String utente = getFirmaUtente(Integer.parseInt(codiceUtente));

			//Elimino i record dalle tabelle di relazione dei sistemi di classificazione, soggettari e thesauri
			Criteria crit = session.createCriteria(Tr_sistemi_classi_biblioteche.class);
			crit.add(Restrictions.eq("cd_biblioteca", bib));
			List<Tr_sistemi_classi_biblioteche> elencoSistemi = crit.list();
			crit = session.createCriteria(Tr_soggettari_biblioteche.class);
			crit.add(Restrictions.eq("cd_biblioteca", bib));
			List<Tr_soggettari_biblioteche> elencoSoggettari = crit.list();
			crit = session.createCriteria(Tr_thesauri_biblioteche.class);
			crit.add(Restrictions.eq("cd_biblioteca", bib));
			List<Tr_thesauri_biblioteche> elencoThesauri = crit.list();
			boolean procedi = true;
			/*
			for (int s =0; s<elencoSoggettari.size(); s++) {
				Tr_soggettari_biblioteche soggettario = elencoSoggettari.get(s);
				Set titoli = soggettario.getTr_tit_sog_bib();
				if (titoli.size() > 0) {
					procedi = false;
					break;
				}
			}
			*/
			Timestamp tempo = DaoManager.now();
			if (procedi) {
				for (int s =0; s<elencoSoggettari.size(); s++) {
					Tr_soggettari_biblioteche soggettario = elencoSoggettari.get(s);
					deleteAndEvict(soggettario);
					session.flush();
				}
			}
			else
				return 3;

			/*
			for (int s =0; s<elencoThesauri.size(); s++) {
				Tr_thesauri_biblioteche thesauro = elencoThesauri.get(s);
				Set termini = thesauro.getTrs_termini_titoli_biblioteche();
				if (termini.size() > 0) {
					procedi = false;
					break;
				}
			}
			*/
			if (procedi) {
				for (int s =0; s<elencoThesauri.size(); s++) {
					Tr_thesauri_biblioteche thesauro = elencoThesauri.get(s);
					deleteAndEvict(thesauro);
					session.flush();
				}
			}
			else
				return 4;

			for (int s =0; s<elencoSistemi.size(); s++) {
				deleteAndEvict(elencoSistemi.get(s));
				session.flush();
			}

			//Elimino i record della tabella tbf_par_sem
			c = session.createCriteria(Tbf_par_sem.class);
			c.add(Restrictions.eq("id_parametro", parametroDB));
			List<Tbf_par_sem> elencoParSem = c.list();
			for (int es =0; es<elencoParSem.size(); es++)
				deleteAndEvict(elencoParSem.get(es));
			session.flush();

			for (int i = 0; i < elencoGruppiParametri.size(); i++) {
				GruppoParametriVO gruppo = elencoGruppiParametri.get(i);
				Tbf_par_sem semantica = new Tbf_par_sem();
				String codice = gruppo.getDescrizione();
				String tipologia = "";
				if (codice.endsWith("classificazioni")) {
					semantica.setTp_tabella_codici("SCLA");
					tipologia = "SCLA";
				}
				else if (codice.endsWith("soggetti")) {
					semantica.setTp_tabella_codici("SOGG");
					tipologia = "SOGG";
				}
				else if (codice.endsWith("thesauri")) {
					semantica.setTp_tabella_codici("STHE");
					tipologia = "STHE";
				}
				semantica.setId_parametro(parametroDB);
				String tipoParametro = "";
				char slocale = ' ';
				char utilizzato = ' ';

				for (int j = 0; j < gruppo.getElencoParametri().size(); j++) {
					ParametroVO parametro = gruppo.getElencoParametri().get(j);
					if (parametro.getDescrizione().endsWith("classificazione") || parametro.getDescrizione().endsWith("soggetto") || parametro.getDescrizione().endsWith("thesauro")) {
						String codiceTipo = "";
						if (parametro.getDescrizione().endsWith("classificazione")) {
							codiceTipo = "SCLA";
						}
						else if (codice.endsWith("soggetti"))
							codiceTipo = "SOGG";
						else if (codice.endsWith("thesauri"))
							codiceTipo = "STHE";

						CodiciType cod = null;
						List<TB_CODICI> elencoCodici = null;
						try {
							cod = CodiciType.fromString(codiceTipo);
							elencoCodici = CodiciProvider.getCodici(cod);
						}
						catch (Exception e){
							e.printStackTrace();
						}
						for (int k = 1; k < elencoCodici.size(); k++) {
							TB_CODICI tabCodice = elencoCodici.get(k);
							if (tabCodice.getDs_tabella().trim().equals(parametro.getSelezione())) {
								tipoParametro = tabCodice.getCd_tabella();
								semantica.setCd_tabella_codici(tipoParametro.trim());
								break;
							}
						}

					}
					else if (parametro.getDescrizione().endsWith("sololocale")) {
						slocale = parametro.getSelezione().charAt(0);
						semantica.setSololocale(slocale);
					}
					else if (parametro.getDescrizione().endsWith("utilizzato")) {
						utilizzato = parametro.getSelezione().charAt(0);
						if (utilizzato == 'S')
							utilizzato = '1';
						else
							utilizzato = '0';
					}
				}
				session.saveOrUpdate(semantica);

				for (int j = 0; j < gruppo.getElencoParametri().size(); j++) {
					ParametroVO parametro = gruppo.getElencoParametri().get(j);
					if (parametro.getDescrizione().endsWith("edizione")) {
						for (int x=0; x <parametro.getElencoCheck().size(); x++) {
							CheckVO check = parametro.getElencoCheck().get(x);
							if (check.isSelezione()) {
								Tr_sistemi_classi_biblioteche sistema = new Tr_sistemi_classi_biblioteche();
								sistema.setCd_biblioteca(bib);
								//almaviva5_20141117 edizioni ridotte
								//sistema.setCd_edizione(check.getDescrizione());
								String edUnimarc = check.getDescrizione();
								TB_CODICI cod = CodiciProvider.cercaCodice(edUnimarc, CodiciType.CODICE_EDIZIONE_CLASSE, CodiciRicercaType.RICERCA_CODICE_UNIMARC);
								if (cod == null)
									throw new ApplicationException(SbnErrorTypes.GS_EDIZIONE_DEWEY_NON_VALIDA, edUnimarc);
								String edizione = cod.getCd_tabellaTrim();
								if (!ValidazioneDati.isFilled(edizione))
									throw new ApplicationException(SbnErrorTypes.GS_EDIZIONE_DEWEY_NON_VALIDA, edUnimarc);
								sistema.setCd_edizione(edizione);

								sistema.setCd_sistema(tipoParametro);
								sistema.setFl_canc('N');
								sistema.setFlg_att(utilizzato);
								sistema.setSololocale(slocale);
								sistema.setTs_ins(tempo);
								sistema.setTs_var(tempo);
								sistema.setUte_ins(utente);
								sistema.setUte_var(utente);
								session.saveOrUpdate(sistema);
							}
						}
					}
					else if (parametro.getDescrizione().endsWith("edizioneunica")) {
						Tr_sistemi_classi_biblioteche sistema = new Tr_sistemi_classi_biblioteche();
						sistema.setCd_biblioteca(bib);
						sistema.setCd_edizione("  ");
						sistema.setCd_sistema(tipoParametro);
						sistema.setFl_canc('N');
						sistema.setFlg_att(utilizzato);
						sistema.setSololocale(slocale);
						sistema.setTs_ins(tempo);
						sistema.setTs_var(tempo);
						sistema.setUte_ins(utente);
						sistema.setUte_var(utente);
						session.saveOrUpdate(sistema);
					}
					//almaviva5_20101011 #3929
					//else if (parametro.getDescrizione().endsWith("recupero")) {
					else if (parametro.getDescrizione().endsWith("sololocale")) {
						if (tipologia.equals("SOGG")) {
							Tr_soggettari_biblioteche soggettari = new Tr_soggettari_biblioteche();
							soggettari.setCd_biblioteca(bib);
							soggettari.setCd_sogg(tipoParametro);
							soggettari.setFl_att(utilizzato);
							/*//almaviva5_20101011 #3929
							char recupero = parametro.getSelezione().charAt(0);
							if (recupero == 'S')
								recupero = '1';
							else
								recupero = '0';
							soggettari.setFl_auto_loc(recupero);
							*/
							soggettari.setFl_auto_loc('1');
							soggettari.setFl_canc('N');
//							soggettari.setTr_tit_sog_bib(value);
							soggettari.setTs_ins(tempo);
							soggettari.setTs_var(tempo);
							soggettari.setUte_ins(utente);
							soggettari.setUte_var(utente);
							soggettari.setSololocale(slocale);
							session.saveOrUpdate(soggettari);
						}
						else {
							if (tipologia.equals("STHE") ) {
								Tr_thesauri_biblioteche thesauri = new Tr_thesauri_biblioteche();
								thesauri.setCd_biblioteca(bib);
								thesauri.setCd_the(tipoParametro);
								thesauri.setFl_att(utilizzato);
								/*//almaviva5_20101011 #3929
								char recupero = parametro.getSelezione().charAt(0);
								if (recupero == 'S')
									recupero = '1';
								else
									recupero = '0';
								thesauri.setFl_auto_loc(recupero);
								 */
								thesauri.setFl_auto_loc('1');
								thesauri.setFl_canc('N');
								//							thesauri.setTrs_termini_titoli_biblioteche(value);
								thesauri.setTs_ins(tempo);
								thesauri.setTs_var(tempo);
								thesauri.setUte_ins(utente);
								thesauri.setUte_var(utente);
								thesauri.setSololocale(slocale);
								session.saveOrUpdate(thesauri);
							}
						}
					}
				}
			}
			return 0;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
		catch (NullPointerException ne) {
			return 1;
		}
	}

	public void attivaProfiloParametri(String codiceBib, int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", codiceBib));
			Tbf_biblioteca_in_polo bib = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			Tbf_parametro parametro = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, idParametro);
			bib.setId_parametro(parametro);
			session.saveOrUpdate(bib);
			session.flush();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void pulisciGruppoAttitaPadri(int idGruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_gruppo_attivita gruppo = (Tbf_gruppo_attivita)loadNoLazy(session, Tbf_gruppo_attivita.class, idGruppo);
			Criteria criteria = session.createCriteria(Trf_gruppo_attivita_polo.class);
			criteria.add(Restrictions.eq("id_gruppo_attivita_polo", gruppo));

			List<Trf_gruppo_attivita_polo> elencoAbilit = criteria.list();
			criteria = session.createCriteria(Tbf_attivita.class);
			List<Tbf_attivita> allAttivita = criteria.list();

			for (int i = 0; i < elencoAbilit.size(); i++) {
				Trf_gruppo_attivita_polo abilit = elencoAbilit.get(i);
				Tbf_attivita attivita = abilit.getId_attivita_polo().getCd_attivita();
				if (attivita.getCd_funzione_parent() == null && this.controllaFigliAttivita(attivita, allAttivita)) {
					boolean trovato = false;
					for (int z=0; z < elencoAbilit.size(); z++) {
						Trf_gruppo_attivita_polo ab = elencoAbilit.get(z);
						if (ab.getId_attivita_polo().getCd_attivita().getCd_funzione_parent() != null &&
							ab.getId_attivita_polo().getCd_attivita().getCd_funzione_parent().equals
							(abilit.getId_attivita_polo().getCd_attivita().getCd_attivita())) {
								trovato = true;
								break;
						}
					}
					if (!trovato)
						deleteAndEvict(abilit);
					else
						session.evict(abilit);
				}
			}
			session.flush();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<String> aggiornaProfiloUtenti(String idBiblioteca, int idGruppo, List<String> elencoAttivita) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_biblioteca_in_polo.class);
			c.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)c.uniqueResult();
			c = session.createCriteria(Tbf_profilo_abilitazione.class);
			c.add(Restrictions.eq("cd_polo", biblioteca));
			List<Tbf_profilo_abilitazione> elencoProfili = c.list();

			//Tbf_gruppo_attivita gruppoNew = (Tbf_gruppo_attivita)loadNoLazy(session, Tbf_gruppo_attivita.class, idGruppo);
			List<String> elencoUtentiAggiornati = new ArrayList<String>();
			for (int i=0; i<elencoProfili.size(); i++) {
				Tbf_profilo_abilitazione profilo = elencoProfili.get(i);
				boolean rimosso = this.aggiornaAttivitaDelProfilo(elencoAttivita, profilo);
				if (rimosso) {
					List <Tbf_bibliotecario> elencoBibliotecari = new ArrayList<Tbf_bibliotecario>(profilo.getTbf_bibliotecario());
					for (Tbf_bibliotecario bib : elencoBibliotecari) {
						Tbf_anagrafe_utenti_professionali idUtente = bib.getId_utente_professionale();
						if (idUtente != null) {
							Tbf_utenti_professionali_web idWeb = idUtente.getTbf_utenti_professionali_web();
							if (idWeb != null)
								elencoUtentiAggiornati.add(idWeb.getUserid().trim());
						}

					}
				}
			}
			session.flush();
			session.clear();
			return elencoUtentiAggiornati;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean aggiornaAttivitaDelProfilo(List<String> elencoAttivita, Tbf_profilo_abilitazione profilo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Trf_profilo_biblioteca.class);
			c.add(Restrictions.eq("cd_prof", profilo));
			List<Trf_profilo_biblioteca> elencoAbilit = c.list();
			boolean rimosso = false;
			for (int i = 0; i < elencoAbilit.size(); i++) {
				Trf_profilo_biblioteca abilit = elencoAbilit.get(i);
				if (!elencoAttivita.contains(abilit.getCd_attivita().getCd_attivita())) {
					deleteAndEvict(abilit);
					rimosso = true;
				}
			}
			//eliminiamo le attività padri senza figli:
			this.pulisciProfiloAttitaPadri(profilo.getId_profilo_abilitazione());
			session.flush();
			return rimosso;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void pulisciProfiloAttitaPadri(int idProfilo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_profilo_abilitazione profilo = (Tbf_profilo_abilitazione)loadNoLazy(session, Tbf_profilo_abilitazione.class, idProfilo);
			Criteria criteria = session.createCriteria(Trf_profilo_biblioteca.class);
			criteria.add(Restrictions.eq("cd_prof", profilo));

			List<Trf_profilo_biblioteca> elencoAbilit = criteria.list();
			criteria = session.createCriteria(Tbf_attivita.class);
			List<Tbf_attivita> allAttivita = criteria.list();
			for (int i = 0; i < elencoAbilit.size(); i++) {
				Trf_profilo_biblioteca abilit = elencoAbilit.get(i);
				Tbf_attivita attivita = abilit.getCd_attivita();
				if (attivita.getCd_funzione_parent() == null && this.controllaFigliAttivita(attivita, allAttivita)) {
					boolean trovato = false;
					for (int z=0; z < elencoAbilit.size(); z++) {
						Trf_profilo_biblioteca ab = elencoAbilit.get(z);
						if (ab.getCd_attivita().getCd_funzione_parent() != null &&
							ab.getCd_attivita().getCd_funzione_parent().equals
							(abilit.getCd_attivita().getCd_attivita())) {
								trovato = true;
								break;
						}
					}
					if (!trovato)
						deleteAndEvict(abilit);
				}
			}
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	private boolean controllaFigliAttivita(Tbf_attivita attivita, List<Tbf_attivita> elencoAttivita) {
		for (int i = 0; i < elencoAttivita.size(); i++) {
			Tbf_attivita att = elencoAttivita.get(i);
			if (att.getCd_funzione_parent() != null && att.getCd_funzione_parent().trim().equals(attivita.getCd_attivita().trim()))
				return true;
		}
		return false;
	}

	public List<String> controllaAttivita(String codBib, List<String> elencoAttivita) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Set<String> output = new HashSet<String>();

			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", codBib));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			criteria = session.createCriteria(Tbf_profilo_abilitazione.class);
			criteria.add(Restrictions.eq("cd_polo", biblioteca));
			List<Tbf_profilo_abilitazione> elencoProfili = criteria.list();
			for (int y=0; y < elencoProfili.size(); y++) {
				Tbf_profilo_abilitazione profilo = elencoProfili.get(y);
				// Controllo le attivita' dei profili dei bibliotecari:
				criteria = session.createCriteria(Trf_profilo_biblioteca.class);
				criteria.add(Restrictions.eq("cd_prof", profilo));
				List<Trf_profilo_biblioteca> elencoAttivitaProfili = criteria.list();
				for (int i = 0; i<elencoAttivitaProfili.size(); i++) {
					Trf_profilo_biblioteca att = elencoAttivitaProfili.get(i);
					if (!elencoAttivita.contains(att.getCd_attivita().getCd_attivita()))
						output.add(att.getCd_attivita().getCd_attivita());
				}
			}
			return new ArrayList<String>(output);
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<String> getElencoEdizioni(String idBiblioteca, String sistema) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_biblioteca_in_polo.class);
			c.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)c.uniqueResult();
			c = session.createCriteria(Tr_sistemi_classi_biblioteche.class);
			c.add(Restrictions.eq("cd_biblioteca", biblioteca));
			c.add(Restrictions.eq("cd_sistema", sistema.trim()));
			List<Tr_sistemi_classi_biblioteche> elencoEdizioni = c.list();
			List<String> output = new ArrayList<String>();
			for (int i=0; i<elencoEdizioni.size(); i++) {
				Tr_sistemi_classi_biblioteche edizione = elencoEdizioni.get(i);
				output.add(edizione.getCd_edizione());
			}
			return output;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public char getUtilizzatoScla(String idBiblioteca, String sistema) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_biblioteca_in_polo.class);
			c.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)c.uniqueResult();
			c = session.createCriteria(Tr_sistemi_classi_biblioteche.class);
			c.add(Restrictions.eq("cd_biblioteca", biblioteca));
			c.add(Restrictions.eq("cd_sistema", sistema));
			List<Tr_sistemi_classi_biblioteche> locale = c.list();
			if (locale.size() > 0 )
				return locale.get(0).getFlg_att();
			else
				return '\u0000';
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public char getUtilizzatoSogg(String idBiblioteca, String sistema) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			criteria = session.createCriteria(Tr_soggettari_biblioteche.class);
			criteria.add(Restrictions.eq("cd_biblioteca", biblioteca));
			criteria.add(Restrictions.eq("cd_sogg", sistema));
			List<Tr_soggettari_biblioteche> locale = criteria.list();
			if (locale.size() > 0 )
				return locale.get(0).getFl_att();
			else
				return '\u0000';
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public char getUtilizzatoSthe(String idBiblioteca, String sistema) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			criteria = session.createCriteria(Tr_thesauri_biblioteche.class);
			criteria.add(Restrictions.eq("cd_biblioteca", biblioteca));
			criteria.add(Restrictions.eq("cd_the", sistema));
			List<Tr_thesauri_biblioteche> locale = criteria.list();
			if (locale.size() > 0 )
				return locale.get(0).getFl_att();
			else
				return '\u0000';
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public char getRecuperoSogg(String idBiblioteca, String sistema) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			criteria = session.createCriteria(Tr_soggettari_biblioteche.class);
			criteria.add(Restrictions.eq("cd_biblioteca", biblioteca));
			criteria.add(Restrictions.eq("cd_sogg", sistema));
			List<Tr_soggettari_biblioteche> locale = criteria.list();
			if (locale.size() > 0 )
				return locale.get(0).getFl_auto_loc();
			else
				return '\u0000';
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public char getRecuperoSthe(String idBiblioteca, String sistema) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			criteria = session.createCriteria(Tr_thesauri_biblioteche.class);
			criteria.add(Restrictions.eq("cd_biblioteca", biblioteca));
			criteria.add(Restrictions.eq("cd_the", sistema));
			List<Tr_thesauri_biblioteche> locale = criteria.list();
			if (locale.size() > 0 )
				return locale.get(0).getFl_auto_loc();
			else
				return '\u0000';
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbf_biblioteca> cercaBiblioteche(BibliotecaRicercaVO richiesta) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo)c.uniqueResult();
			c = session.createCriteria(Tbf_biblioteca.class, "bib");

			String codiceAna = richiesta.getCodiceAna();
			if (ValidazioneDati.isFilled(codiceAna)) {
				//almaviva5_20150415 servizi ill: gestione isil con codice paese
				Isil isil = Isil.parse(codiceAna);
				if (isil.withPaese() ) {
					//codice isil completo: <cod_paese>-<cod.anagrafe>
					c.add(Restrictions.eq("paese", isil.getPaese()));
					c.add(Restrictions.like("cd_ana_biblioteca", isil.getSuffisso(), MatchMode.START).ignoreCase());
				} else
					c.add(Restrictions.like("cd_ana_biblioteca", codiceAna, MatchMode.START).ignoreCase());

			} else {
				String codPolo = richiesta.getCodicePolo();
				if (ValidazioneDati.isFilled(codPolo)) {
					String codBib = richiesta.getCodiceBib();
					if (ValidazioneDati.isFilled(codBib))
						c.add(Restrictions.eq("cd_bib", codBib).ignoreCase());
					c.add(Restrictions.eq("cd_polo", codPolo).ignoreCase());
				}
				else {
					String nome = richiesta.getNome();
					if (!nome.equals("") && !nome.equals("*") && !nome.equals("%") && !nome.contains("*") && !nome.contains("%"))
						c.add(Restrictions.eq("nom_biblioteca", nome).ignoreCase());
					else if (!nome.equals("") && !nome.equals("*") && !nome.equals("%") && nome.contains("*") && !nome.contains("%"))
						c.add(Restrictions.ilike("nom_biblioteca", nome.replace("*", "%")));
					else if (!nome.equals("") && !nome.equals("*") && !nome.equals("%") && !nome.contains("*") && nome.contains("%")) {
						c.add(ricercaPerParoleAND("tidx_vector_nom_biblioteca", nome));
					}
					String indirizzo = richiesta.getIndirizzo();
					if (!indirizzo.equals("") && !indirizzo.equals("*") && !indirizzo.equals("%") && !indirizzo.contains("*") && !indirizzo.contains("%"))
						c.add(Restrictions.eq("indirizzo", indirizzo).ignoreCase());
					else if (!indirizzo.equals("") && !indirizzo.equals("*") && !indirizzo.equals("%") && indirizzo.contains("*") && !indirizzo.contains("%"))
						c.add(Restrictions.ilike("indirizzo", indirizzo.replace("*", "%")));
					else if (!indirizzo.equals("") && !indirizzo.equals("*") && !indirizzo.equals("%") && !indirizzo.contains("*") && indirizzo.contains("%")) {
						c.add(ricercaPerParoleAND("tidx_vector_indirizzo", indirizzo));
					}
					String cap = richiesta.getCap();
					if (!cap.equals("") && !cap.equals("*") && !cap.contains("*"))
						c.add(Restrictions.eq("cap", cap).ignoreCase());
					else if (!cap.equals("") && !cap.equals("*") && cap.contains("*"))
						c.add(Restrictions.ilike("cap", cap.replace("*", "%")));
					String citta = richiesta.getCitta();
					if (!citta.equals("") && !citta.equals("*") && !citta.contains("*"))
						c.add(Restrictions.eq("localita", citta).ignoreCase());
					else if (!citta.equals("") && !citta.equals("*") && citta.contains("*"))
						c.add(Restrictions.ilike("localita", citta.replace("*", "%")));
					String provincia = richiesta.getProvincia();
					if (!provincia.equals("") && !provincia.equals("*") && !provincia.contains("*"))
						c.add(Restrictions.eq("provincia", provincia).ignoreCase());
					else if (!provincia.equals("") && !provincia.equals("*") && provincia.contains("*"))
						c.add(Restrictions.ilike("provincia", provincia.replace("*", "%")));
					String tipoBib = richiesta.getTipoBib();
					if (!tipoBib.equals(""))
						c.add(Restrictions.eq("tipo_biblioteca", tipoBib));

					BibliotecaType tipoBibSBN = richiesta.getBibinpolo();
					RicercaBibliotecaType tipoRicerca = richiesta.getFlagSbn();
					//il filtro su tipo bib sbn é valido solo se non ho specificato
					//un filtro sulle abilitazioni
					if (tipoBibSBN != null && tipoRicerca == null)
						switch (tipoBibSBN) {
						case TUTTE:
							break;
						case SBN:
							//biblioteche che appartengono a un polo
							c.add(Restrictions.gt("cd_polo", ""));
							c.add(Restrictions.in("flag_bib", new Character[]{'C', 'S', 'A'}));
							break;
						case NON_SBN:
							c.add(Restrictions.or(Restrictions.isNull("cd_polo"), Restrictions.eq("cd_polo", "")));
							c.add(Restrictions.eq("flag_bib", 'N'));
							break;
						}

//					if (tipoBib.equals("POLO"))
//						criteria.add(Restrictions.eq("cd_polo", polo.getCd_polo()));
//					else if (tipoBib.equals("ESTERNE"))
//						criteria.add(Restrictions.ne("cd_polo", polo.getCd_polo()));


					if (tipoRicerca != null) {	//filtro su abilitazioni

						DetachedCriteria bibPolo = DetachedCriteria.forClass(Tbf_biblioteca_in_polo.class, "bibPolo");
						bibPolo.setProjection(Projections.property("id_biblioteca.id"));
						bibPolo.add(Restrictions.eqProperty("bib.id_biblioteca", "bibPolo.id_biblioteca.id"));
						bibPolo.add(Restrictions.ne("bibPolo.fl_canc", 'S'));

						//tutte le biblioteche iscritte con il polo corrente
						c.add(Restrictions.eq("cd_polo", polo.getCd_polo()));

						switch (tipoRicerca) {
						case TUTTE:
							break;

						case NON_ABILITATE:
							//la bib non deve esistere tra quelle registrate in polo
							c.add(Subqueries.notExists(bibPolo));
							break;

						case ABILITATE:
							//la bib deve esistere tra quelle registrate in polo
							c.add(Subqueries.exists(bibPolo));
							break;

						case CENTRO_SISTEMA:
							//la bib deve esistere tra quelle registrate in polo e flag_bib='C'
							c.add(Subqueries.exists(bibPolo));
							c.add(Restrictions.eq("flag_bib", 'C'));
							break;

						case AFFILIATE:
							//la bib deve esistere tra quelle registrate in polo e flag_bib='A'
							c.add(Subqueries.exists(bibPolo));
							c.add(Restrictions.eq("flag_bib", 'A'));
							break;
						}
					}

					String codSistemaMetro = richiesta.getCodSistemaMetro();
					if (ValidazioneDati.isFilled(codSistemaMetro)) {
						Criteria c2 = c.createCriteria("Tbf_biblioteca_in_polo");
						c2.add(Restrictions.ne("fl_canc", 'S'));
						c2.add(Restrictions.eq("cd_sistema_metropolitano", codSistemaMetro));
					}

				}
			}

			//servizi ill
			//filtro su ruolo biblioteca
			RuoloBiblioteca ruolo = richiesta.getRuoloILL();
			if (ruolo != null) {
				Criteria c3 = c.createCriteria("bibliotecheIll", "ill");
				c3.add(Restrictions.ne("ill.fl_canc", 'S'));

				//TODO da verificare
				switch (ruolo) {
				case FORNITRICE:
					c3.add(Restrictions.in("ill.fl_ruolo", new Character[] { 'E', 'D' }));
					//esclusione bib richiedente
					String isilBibRichiedente = richiesta.getIsilBibRichiedente();
					if (ValidazioneDati.isFilled(isilBibRichiedente))
						c3.add(Restrictions.ne("ill.cd_isil", isilBibRichiedente));
					break;
				case RICHIEDENTE:
					c3.add(Restrictions.in("ill.fl_ruolo", new Character[] { 'E', 'R' }));
					break;
				}

			}

			c.add(Restrictions.ne("fl_canc", 'S'));
			//almaviva5_20120319 #4842
			LogicalExpression or = Restrictions.or(Restrictions.ne("cd_bib", Constants.ROOT_BIB), Restrictions.isNull("cd_bib"));
			c.add(or);

			String ordinamento = ValidazioneDati.coalesce(richiesta.getOrdinamento(), "codicebib");
			if (ordinamento.equals("codicebib"))
				c.addOrder(Order.asc("cd_bib"));
			else if (ordinamento.equals("codiceana"))
				c.addOrder(Order.asc("cd_ana_biblioteca"));
			else if (ordinamento.equals("nome"))
				c.addOrder(Order.asc("nom_biblioteca"));
			else if (ordinamento.equals("indirizzo"))
				c.addOrder(Order.asc("indirizzo"));
			else if (ordinamento.equals("cap"))
				c.addOrder(Order.asc("cap"));
			else if (ordinamento.equals("citta"))
				c.addOrder(Order.asc("localita"));
			else if (ordinamento.equals("provincia"))
				c.addOrder(Order.asc("provincia"));
			else if (ordinamento.equals("polo"))
				c.addOrder(Order.asc("cd_polo"));
			else if (ordinamento.equals("tipobib"))
				c.addOrder(Order.asc("tipo_biblioteca"));
			else if (ordinamento.equals("flag"))
				c.addOrder(Order.asc("flag_bib"));

			return c.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbf_biblioteca> getBibliotecheCentroSistema() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca.class);
			criteria.add(Restrictions.eqProperty("cd_bib_cs", "cd_bib"));
			criteria.add(Restrictions.ne("cd_bib", Constants.ROOT_BIB));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbf_biblioteca> getBibliotecheAffiliatePerAttivita(String codPolo, String codBib, String codAttivita) throws DaoManagerException {
		try {

			Session session = this.getCurrentSession();
			List<Tbf_biblioteca> output = new ArrayList<Tbf_biblioteca>();

			if (ValidazioneDati.isFilled(codAttivita)) {
				Criteria criteria = session.createCriteria(Trf_attivita_affiliate.class);
				criteria.add(Restrictions.ne("fl_canc", 'S'));
				criteria.add(Restrictions.eq("cd_attivita.id", codAttivita));
				criteria.add(Restrictions.ne("cd_bib_affiliata.cd_biblioteca", codBib));
				criteria.add(Restrictions.eq("cd_bib_centro_sistema.cd_biblioteca", codBib));
				List<Trf_attivita_affiliate> elenco = criteria.list();

				for (int i = 0; i < elenco.size(); i++) {
					Trf_attivita_affiliate record = elenco.get(i);
					output.add(record.getCd_bib_affiliata().getId_biblioteca());
				}

			} else {

				Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
				criteria.add(Restrictions.ne("fl_canc", 'S'))
						.add(Restrictions.eq("cd_polo.id", codPolo))
						.add(Restrictions.ne("cd_biblioteca", codBib))
						.add(Restrictions.ne("cd_biblioteca", Constants.ROOT_BIB))
						.addOrder(Order.asc("cd_biblioteca"));
				List<Tbf_biblioteca_in_polo> elenco = criteria.list();

				for (int i = 0; i < elenco.size(); i++) {
					Tbf_biblioteca_in_polo record = elenco.get(i);
					output.add(record.getId_biblioteca());
				}
			}

			return output;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Trf_attivita_affiliate> getAttivitaAffiliate() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			Criteria c = session.createCriteria(Trf_attivita_affiliate.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.addOrder(Order.asc("cd_attivita.id"));
			List<Trf_attivita_affiliate> output = c.list();

			return output;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbf_biblioteca> getListaBibliotechePolo(String codPolo)  throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cd_polo", codPolo))
					.add(Restrictions.ne("cd_bib", Constants.ROOT_BIB))
					.addOrder(Order.asc("cd_bib"));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<Tbf_biblioteca> getListaBibliotecheAteneoInPolo(String codPolo,	String ateneo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cd_polo", codPolo))
					.add(Restrictions.eq("unit_org", ateneo))
					.add(Restrictions.ne("cd_bib", Constants.ROOT_BIB))
					.addOrder(Order.asc("cd_bib"));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public String getCodicePoloCorrente() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo)criteria.uniqueResult();
			return polo.getCd_polo();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	class StringComparator implements Comparator
	{
	public final int compare ( Object a, Object b )
	   {

		String sa = (String)a;
		String sb = (String)b;
		return( (sa).compareToIgnoreCase( sb )); // Ascending

	   } // end compare
	} // end class StringComparator

	public BibliotecaVO inserisciBiblioteca(BibliotecaVO bibVO, String utenteInseritore, boolean forzaInserimento, boolean abilitazione, String codPoloCorrente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();

			String codBib = bibVO.getCod_bib();
			String polo = bibVO.getCod_polo();
			String nome = bibVO.getNom_biblioteca();
			String citta = bibVO.getLocalita();

			Tbf_biblioteca biblioteca = new Tbf_biblioteca();
			Tbf_biblioteca_in_polo bibPolo = new Tbf_biblioteca_in_polo();
			boolean esisteNonInPolo = false;

			setSessionCurrentCfg();

			if (polo.equals(codPoloCorrente)) {
				Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
				criteria.add(Restrictions.eq("cd_biblioteca", codBib));
				List<Tbf_biblioteca_in_polo> bibInPolo = criteria.list();
				if (ValidazioneDati.isFilled(bibInPolo) && !forzaInserimento) {
					bibVO.setInserito(2);
					return bibVO;
				}
				if (ValidazioneDati.isFilled(bibInPolo) && forzaInserimento) {
					biblioteca = bibInPolo.get(0).getId_biblioteca();
					bibPolo = bibInPolo.get(0);
				}
				if (!ValidazioneDati.isFilled(bibInPolo) && forzaInserimento) {
//					criteria = session.createCriteria(Tbf_biblioteca.class);
//					criteria.add(Restrictions.eq("cd_bib", codBib));
//					criteria.add(Restrictions.eq("cd_polo", polo));
//					biblioteca = (Tbf_biblioteca)criteria.uniqueResult();
					biblioteca = (Tbf_biblioteca)loadNoLazy(session, Tbf_biblioteca.class, bibVO.getIdBiblioteca());
					esisteNonInPolo = true;
				}
			}
			else if (forzaInserimento && !polo.equals(codPoloCorrente) && bibVO.getIdBiblioteca() == 0) {
				Criteria criteria = session.createCriteria(Tbf_biblioteca.class);
				criteria.add(Restrictions.eq("cd_bib", codBib));
				criteria.add(Restrictions.eq("cd_polo", polo));
				biblioteca = (Tbf_biblioteca)criteria.uniqueResult();
				esisteNonInPolo = false;
			}


			Timestamp ts = DaoManager.now();

			Criteria c = session.createCriteria(Tbf_polo.class);
			Tbf_polo poloDB = (Tbf_polo)c.uniqueResult();

			Object sem[] = null;

			if ((!forzaInserimento && abilitazione) || esisteNonInPolo) {
				Tbf_parametro parametro = poloDB.getId_parametro();
				Tbf_parametro profilo = new Tbf_parametro();
				profilo.setCd_livello(parametro.getCd_livello());
				profilo.setTp_ret_doc(parametro.getTp_ret_doc());
				profilo.setTp_all_pref(parametro.getTp_all_pref());
				profilo.setCd_liv_ade(parametro.getCd_liv_ade());
				profilo.setFl_spogli(parametro.getFl_spogli());
				profilo.setFl_aut_superflui(parametro.getFl_aut_superflui());
				profilo.setUte_ins(utenteInseritore);
				profilo.setUte_var(utenteInseritore);
				profilo.setFl_canc(parametro.getFl_canc());
				profilo.setTs_ins(ts);
				profilo.setTs_var(ts);
				profilo.setSololocale(parametro.getSololocale());
				session.save(profilo);

				//Ricopio per la biblioteca i parametri del profilo del polo:
				Object auth[] = parametro.getTbf_par_auth().toArray();
				Object mat[] = parametro.getTbf_par_mat().toArray();
				sem = parametro.getTbf_par_sem().toArray();

				for (int i = 0; i < auth.length; i++) {
					Tbf_par_auth par = (Tbf_par_auth)auth[i];
					Tbf_par_auth newpar = new Tbf_par_auth();
					newpar.setCd_contr_sim(par.getCd_contr_sim());
					newpar.setCd_livello(par.getCd_livello());
					newpar.setCd_par_auth(par.getCd_par_auth());
					newpar.setFl_abil_forzat(par.getFl_abil_forzat());
					newpar.setFl_abil_legame(par.getFl_abil_legame());
					newpar.setFl_leg_auth(par.getFl_leg_auth());
					newpar.setId_parametro(profilo);
					newpar.setSololocale(par.getSololocale());
					newpar.setTp_abil_auth(par.getTp_abil_auth());
					session.save(newpar);
				}

				for (int i = 0; i < mat.length; i++) {
					Tbf_par_mat par = (Tbf_par_mat)mat[i];
					Tbf_par_mat newpar = new Tbf_par_mat();
					newpar.setCd_contr_sim(par.getCd_contr_sim());
					newpar.setCd_livello(par.getCd_livello());
					newpar.setCd_par_mat(par.getCd_par_mat());
					newpar.setFl_abil_forzat(par.getFl_abil_forzat());
					newpar.setId_parametro(profilo);
					newpar.setSololocale(par.getSololocale());
					newpar.setTp_abilitaz(par.getTp_abilitaz());
					session.save(newpar);
				}
				//almaviva5_20130418 i parametri semantici vanno riconfigurati dall'utente
				/*
				for (int i = 0; i < sem.length; i++) {
					Tbf_par_sem par = (Tbf_par_sem)sem[i];
					Tbf_par_sem newpar = new Tbf_par_sem();
					newpar.setCd_tabella_codici(par.getCd_tabella_codici());
					newpar.setId_parametro(profilo);
					newpar.setSololocale(par.getSololocale());
					newpar.setTp_tabella_codici(par.getTp_tabella_codici());
					session.save(newpar);
				}
				*/
				bibPolo.setId_parametro(profilo);

				//Attivita':
				Tbf_gruppo_attivita gruppo = new Tbf_gruppo_attivita();
				Tbf_gruppo_attivita attPolo = poloDB.getId_gruppo_attivita();
				gruppo.setCd_polo(poloDB);
				gruppo.setDs_name("Profilo biblioteca " + codBib);
				session.save(gruppo);
				bibPolo.setId_gruppo_attivita_polo(gruppo);

				List<Trf_gruppo_attivita_polo> elencoAtt = new ArrayList<Trf_gruppo_attivita_polo>(attPolo.getTrf_gruppo_attivita_polo());
				for (int i = 0; i < elencoAtt.size(); i++) {
					Trf_gruppo_attivita_polo nuovaAtt = new Trf_gruppo_attivita_polo();
					nuovaAtt.setId_gruppo_attivita_polo(gruppo);
					nuovaAtt.setFl_include(elencoAtt.get(i).getFl_include());
					nuovaAtt.setId_attivita_polo(elencoAtt.get(i).getId_attivita_polo());
					session.save(nuovaAtt);
					session.flush();
				}
			}

			biblioteca = ConversioneHibernateVO.toHibernate().anagraficaBiblioteca(biblioteca, bibVO);
			biblioteca.setUte_var(utenteInseritore);

			if (!forzaInserimento) {
				biblioteca.setUte_ins(utenteInseritore);
				biblioteca.setTs_ins(ts);
				biblioteca.setTs_var(ts);
			}

			session.saveOrUpdate(biblioteca);

			if (abilitazione) {
				if (!forzaInserimento || esisteNonInPolo) {
					bibPolo.setCd_biblioteca(codBib);
					bibPolo.setCd_polo(poloDB);
					bibPolo.setId_biblioteca(biblioteca);
					bibPolo.setUte_ins(utenteInseritore);
					bibPolo.setTs_ins(ts);
				}
//				bibPolo.setKy_biblioteca(value)
				bibPolo.setCd_ana_biblioteca(bibVO.getGruppo());
				bibPolo.setDs_biblioteca(nome);
				bibPolo.setDs_citta(citta);
				bibPolo.setUte_var(utenteInseritore);
				bibPolo.setTs_var(ts);
				bibPolo.setFl_canc('N');
				//almaviva5_20091015
				String cdSistemaMetro = bibVO.getCodSistemaMetropolitano();
				bibPolo.setCd_sistema_metropolitano(ValidazioneDati.isFilled(cdSistemaMetro) ? cdSistemaMetro : null);

				session.saveOrUpdate(bibPolo);
				session.flush();

				if (!forzaInserimento || esisteNonInPolo) {/*
					//almaviva5_20130418
					for (int i = 0; i<sem.length; i++) {
						Tbf_par_sem par = (Tbf_par_sem)sem[i];
						if (par.getTp_tabella_codici().equals("SCLA")) {
							Tr_sistemi_classi_biblioteche sistema = new Tr_sistemi_classi_biblioteche();
							sistema.setCd_biblioteca(bibPolo);
							sistema.setCd_sistema(par.getCd_tabella_codici());
							sistema.setCd_edizione("");
							if (par.getCd_tabella_codici().trim().equals("D")) {
								List<TB_CODICI> elencoCodici = null;
								List<String> elencoEdizioni = new ArrayList<String>();
								try {
									elencoCodici = CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_CLASSE);
								}
								catch (Exception e){
									e.printStackTrace();
								}
								for (int k = 1; k < elencoCodici.size(); k++) {
									TB_CODICI tabCodice = elencoCodici.get(k);
									elencoEdizioni.add(tabCodice.getCd_unimarc());
								}
								Collections.sort(elencoEdizioni, new StringComparator());
								sistema.setCd_edizione(elencoEdizioni.get(elencoEdizioni.size()-1));
							}
							sistema.setFlg_att('0');
							sistema.setFl_canc('N');
							sistema.setTs_ins(tempo);
							sistema.setTs_var(tempo);
							sistema.setUte_ins(utente);
							sistema.setUte_var(utente);
							sistema.setSololocale('N');
							session.saveOrUpdate(sistema);
							session.flush();
						}

						else if (par.getTp_tabella_codici().equals("SOGG")) {
							Tr_soggettari_biblioteche sogg = new Tr_soggettari_biblioteche();
							sogg.setCd_biblioteca(bibPolo);
							sogg.setCd_sogg(par.getCd_tabella_codici());
							sogg.setFl_att('0');
							//almaviva5_20101011 #3929
							sogg.setFl_auto_loc('1');
							sogg.setFl_canc('N');
							sogg.setTs_ins(tempo);
							sogg.setTs_var(tempo);
							sogg.setUte_ins(utente);
							sogg.setUte_var(utente);
							sogg.setSololocale('N');
							session.saveOrUpdate(sogg);
							session.flush();
						}

						else if (par.getTp_tabella_codici().equals("STHE")) {
							Tr_thesauri_biblioteche the = new Tr_thesauri_biblioteche();
							the.setCd_biblioteca(bibPolo);
							the.setCd_the(par.getCd_tabella_codici());
							the.setFl_att('0');
							//almaviva5_20101011 #3929
							the.setFl_auto_loc('1');
							the.setFl_canc('N');
							the.setSololocale('N');
							the.setTs_ins(tempo);
							the.setTs_var(tempo);
							the.setUte_ins(utente);
							the.setUte_var(utente);
							session.saveOrUpdate(the);
							session.flush();
						}
					}
				*/}
			}
			session.flush();
			bibVO = ConversioneHibernateVO.toWeb().anagraficaBiblioteca(biblioteca);

			if (forzaInserimento)
				bibVO.setInserito(3);
			else
				bibVO.setInserito(0);

			return bibVO;

		} catch (ConstraintViolationException e) {
			throw e;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_biblioteca caricaBiblioteca(int idBiblioteca) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_biblioteca bib = (Tbf_biblioteca)session.get(Tbf_biblioteca.class, idBiblioteca);
			return bib;
		}

		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean controllaAbilitazioneBiblioteca(int idBib) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_biblioteca bib = (Tbf_biblioteca)loadNoLazy(session, Tbf_biblioteca.class, idBib);
			List<Tbf_biblioteca_in_polo> bibInPolo = new ArrayList<Tbf_biblioteca_in_polo>(bib.getTbf_biblioteca_in_polo());
			if (bibInPolo.size() > 0)
				return true;
			return false;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void rimuoviParAuth(int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session.createQuery("delete from Tbf_par_auth where id_parametro = " + idParametro);
			query.executeUpdate();
			clearCache("Tbf_par_auth");
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void rimuoviParMat(int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session.createQuery("delete from Tbf_par_mat where id_parametro = " + idParametro);
			query.executeUpdate();
			clearCache("Tbf_par_mat");
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void rimuoviParSem(int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session.createQuery("delete from Tbf_par_sem where id_parametro = " + idParametro);
			query.executeUpdate();
			clearCache("Tbf_par_sem");
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getParametroPolo()throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			return ((Tbf_polo)criteria.uniqueResult()).getId_parametro().getId_parametro();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void rimuoviParametriBibliotecari(String idBiblioteca, boolean auth, boolean mat, boolean sem) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_biblioteca_in_polo biblioteca = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			criteria = session.createCriteria(Tbf_profilo_abilitazione.class);
			criteria.add(Restrictions.eq("cd_polo", biblioteca));
			List<Tbf_profilo_abilitazione> elencoProfili = criteria.list();

			for (int i=0; i<elencoProfili.size(); i++) {
				Tbf_profilo_abilitazione profilo = elencoProfili.get(i);
					List <Tbf_bibliotecario> elencoBibliotecari = new ArrayList<Tbf_bibliotecario>(profilo.getTbf_bibliotecario());
					for (int j=0; j<elencoBibliotecari.size(); j++) {
						int parametroUtente = elencoBibliotecari.get(j).getId_parametro().getId_parametro();
						if (auth) {
							this.rimuoviParAuth(parametroUtente);
						}
						if (mat) {
							this.rimuoviParMat(parametroUtente);
						}
						if (sem) {
							this.rimuoviParSem(parametroUtente);
						}
					}
			}
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<String> getElencoModelli() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_modello_profilazione_biblioteca.class);
			List<Tbf_modello_profilazione_biblioteca> elenco = criteria.list();
			List<String> output = new ArrayList<String>();
			for (int i = 0; i < elenco.size(); i++)
				output.add(elenco.get(i).getNome());
			return output;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List loadAttivitaProfiloModello(String idModello) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_modello_profilazione_biblioteca.class);
			criteria.add(Restrictions.eq("nome", idModello));
			Tbf_modello_profilazione_biblioteca modello = (Tbf_modello_profilazione_biblioteca)criteria.uniqueResult();
			criteria = session.createCriteria(Trf_gruppo_attivita_polo.class);
			criteria.add(Restrictions.eq("id_gruppo_attivita_polo", modello.getId_gruppo_attivita()));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_modello_profilazione_biblioteca getModello(String nomeModello) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_modello_profilazione_biblioteca.class);
			criteria.add(Restrictions.eq("nome", nomeModello));
			return (Tbf_modello_profilazione_biblioteca)criteria.uniqueResult();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean isCentroSistema(String codPolo, String codBib) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca.class);
			criteria.add(Restrictions.eq("cd_polo", codPolo));
			criteria.add(Restrictions.eq("cd_bib", codBib));
			Tbf_biblioteca biblioteca = (Tbf_biblioteca)criteria.uniqueResult();
			if (biblioteca.getFlag_bib() == 'C')
				return true;
			return false;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_biblioteca getBiblioteca(String codPolo, String codBib) throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_biblioteca.class);
		criteria.add(Restrictions.eq("cd_polo", codPolo));
		criteria.add(Restrictions.eq("cd_bib", codBib));
		criteria.add(Restrictions.ne("fl_canc", 'S'));
		return (Tbf_biblioteca) criteria.uniqueResult();
	}

	public List<Tbf_biblioteca> getListaBibliotecheSistemaMetropolitano(
			String codPolo, String codBib, String cdSistema) throws DaoManagerException {
		try {

			Session session = this.getCurrentSession();
			List<Tbf_biblioteca> output = new ArrayList<Tbf_biblioteca>();

			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cd_polo.id", codPolo))
					.add(Restrictions.ne("cd_biblioteca", codBib))
					.add(Restrictions.eq("cd_sistema_metropolitano", cdSistema))
					.addOrder(Order.asc("cd_biblioteca"));
				List<Tbf_biblioteca_in_polo> elenco = criteria.list();

				for (int i = 0; i < elenco.size(); i++) {
					Tbf_biblioteca_in_polo record = elenco.get(i);
					output.add(record.getId_biblioteca());
				}


			return output;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_biblioteca getBibliotecaCentroSistema(String codPolo, String codBib) throws DaoManagerException {
		try {

			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Criteria criteria = session.createCriteria(Trf_attivita_affiliate.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cd_bib_affiliata", bib));

			List<Trf_attivita_affiliate> listaBib = criteria.list();
			if (!ValidazioneDati.isFilled(listaBib))
				return null;

			//cod della bib centro sistema
			return listaBib.get(0).getCd_bib_centro_sistema().getId_biblioteca();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esisteBibliotecaAffiliata(String codPolo, String codBib) throws DaoManagerException {
		try {

			Session session = this.getCurrentSession();

			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			Criteria criteria = session.createCriteria(Trf_attivita_affiliate.class);
			criteria.add(Restrictions.ne("fl_canc", 'S'))
					.add(Restrictions.eq("cd_bib_centro_sistema", bib))
					.setProjection(Projections.rowCount());

			//esiste bib affiliata
			return ((Number)criteria.uniqueResult()).intValue() > 0;

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean esisteCodiceAnagrafe(BibliotecaVO bib) throws DaoManagerException {
		try {
			//almaviva5_20120529 #5005
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_biblioteca.class);
			c.setProjection(Projections.rowCount());
			c.add(Restrictions.eq("cd_ana_biblioteca", bib.getCd_ana_biblioteca()).ignoreCase() );
			//almaviva5_20151030 servizi ill
			String paese = bib.getPaese();
			if (ValidazioneDati.isFilled(paese))
				c.add(Restrictions.eq("paese", paese) );

			if (bib.getIdBiblioteca() > 0)
				c.add(Restrictions.not(Restrictions.idEq(bib.getIdBiblioteca())));

			//esiste bib
			Number cnt = (Number)c.uniqueResult();
			return (cnt.intValue() > 0);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void aggiornaParametriBibliotecari(String codiceBib) throws DaoManagerException {
		//almaviva5_20130417 #5269
		Session session = this.getCurrentSession();
		Criteria c = session.createCriteria(Tbf_biblioteca_in_polo.class);
		c.add(Restrictions.eq("cd_biblioteca", codiceBib));
		Tbf_biblioteca_in_polo bib = (Tbf_biblioteca_in_polo)c.uniqueResult();
		aggiornaParametriBibliotecari(bib);
	}

	public void aggiornaParametriBibliotecari(Tbf_biblioteca_in_polo bib) throws DaoManagerException {
		//almaviva5_20130417 #5269
		Session session = this.getCurrentSession();
		List<Trf_utente_professionale_biblioteca> bibliotecari =
			new ArrayList<Trf_utente_professionale_biblioteca>(bib.getTrf_utente_professionale_biblioteca());

		List<Tbf_par_sem> parSemBib = new ArrayList<Tbf_par_sem>(bib.getId_parametro().getTbf_par_sem());
		for (Trf_utente_professionale_biblioteca upb : bibliotecari) {
			Tbf_bibliotecario bibliotecario = upb
					.getId_utente_professionale()
					.getTbf_bibliotecario();
			if (bibliotecario == null)
				continue;
			Tbf_parametro id_parametro = bibliotecario.getId_parametro();

			Set<Tbf_par_sem> parSemBibliotecario = id_parametro.getTbf_par_sem();
			Iterator<Tbf_par_sem> i = parSemBibliotecario.iterator();
			while (i.hasNext()) {
				Tbf_par_sem p = i.next();
				if (!Lambda.exists(parSemBib, new TbfParSemMatcher(p))) {
					i.remove();
					session.delete(p);
				}
			}
			session.flush();

			session.evict(id_parametro);
			//session.evict(bibliotecario);
			session.evict(upb);

		}
	}

	public void eliminaAbilitazioneOggettoSemantico(Tbf_biblioteca_in_polo bib, Tbf_par_sem ps) throws DaoManagerException {
		//almaviva5_20130417 #5269
		Session session = this.getCurrentSession();
		CodiciType type = CodiciType.fromString(ps.getTp_tabella_codici().trim() );
		Criteria c = null;
		switch (type) {
		case CODICE_SOGGETTARIO:
			c = session.createCriteria(Tr_soggettari_biblioteche.class);
			c.add(Restrictions.eq("cd_sogg", ps.getCd_tabella_codici().trim()));
			break;

		case CODICE_SISTEMA_CLASSE:
			c = session.createCriteria(Tr_sistemi_classi_biblioteche.class);
			c.add(Restrictions.eq("cd_sistema",  ps.getCd_tabella_codici().trim()));
			break;

		case CODICE_THESAURO:
			c = session.createCriteria(Tr_thesauri_biblioteche.class);
			c.add(Restrictions.eq("cd_the",  ps.getCd_tabella_codici().trim()));
			break;

		default:
			return;
		}

		c.add(Restrictions.eq("cd_biblioteca", bib));
		for (Object row : c.list() )
			session.delete(row);

		session.flush();
	}

}
