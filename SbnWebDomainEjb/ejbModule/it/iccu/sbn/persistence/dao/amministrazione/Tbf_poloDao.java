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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita_sbnmarc;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_js_script;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_sem;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione;
import it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_gruppo_attivita_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_profilo_biblioteca;
import it.iccu.sbn.servizi.codici.CodiciProvider;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

import ch.lambdaj.Lambda;

public class Tbf_poloDao extends DaoManager {

	private static Logger log = Logger.getLogger(Tbf_poloDao.class);

	public Tbf_poloDao() {
		super();
	}

	public List<Tbf_polo> selectAll() throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_polo.class);
		List<Tbf_polo> ret = criteria.list();
		return ret;
	}

	public Tbf_polo select(String cd_polo) throws DaoManagerException {
		Session session = this.getCurrentSession();
		Tbf_polo polo = (Tbf_polo) loadNoLazy(session, Tbf_polo.class, cd_polo);
		return polo;
	}

	public void saveOrUpdate(Tbf_polo polo) throws DaoManagerException {
		Session session = this.getCurrentSession();
		// session.beginTransaction();
		try {
			Tbf_polo tb_polo1 = (Tbf_polo) loadNoLazy(session, Tbf_polo.class, polo
					.getCd_polo());
			// session.flush();
			// polo.setUte_ins(tb_polo1.getUte_ins());
			session.merge(polo);
		} catch (org.hibernate.ObjectNotFoundException e) {
			// e.printStackTrace();
			polo.setUte_ins(polo.getUte_var());
			polo.setTs_ins(polo.getTs_ins());
			session.save(polo);
		}

		// session.save(polo);
	}

	public List<Tbf_attivita> loadCodiciAttivita() throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_attivita.class);

		return criteria.list();

	}

	class StringComparator implements Comparator<Object> {
		public final int compare(Object a, Object b) {

			Tbf_attivita attivita = (Tbf_attivita) a;
			int sa = attivita.getPrg_ordimanento();
			attivita = (Tbf_attivita) b;
			int sb = attivita.getPrg_ordimanento();
			// return( (sa).compareToIgnoreCase( sb )); // Ascending
			if (sa > sb)
				return 1;
			else if (sa < sb)
				return -1;
			else
				return 0;

		} // end compare
	} // end class StringComparator

	public List loadCodiciAttivitaDelPolo() throws DaoManagerException {
		Class cls = null;
		Object codici = null;

		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Trf_attivita_polo.class);
		List ret = criteria.list();
		List output = new ArrayList();
		for (int i = 0; i < ret.size(); i++) {
			output.add(((Trf_attivita_polo) ret.get(i)).getCd_attivita());
		}
		Collections.sort(output, new StringComparator());
		return output;

	}

	public List<Trf_attivita_polo> loadAttivitaDelPolo()
			throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Trf_attivita_polo.class);
		List ret = criteria.list();
		List<Trf_attivita_polo> output = new ArrayList<Trf_attivita_polo>();
		for (int i = 0; i < ret.size(); i++) {
			output.add((Trf_attivita_polo) ret.get(i));
		}
		return output;

	}

	public List<Trf_attivita_polo> loadAttivitaProfiloPolo()
			throws DaoManagerException {
		List<Trf_attivita_polo> output = new ArrayList<Trf_attivita_polo>();
		List<Trf_gruppo_attivita_polo> elencoGruppoAttivita = this
				.loadAttivitaGruppoPolo();
		for (int i = 0; i < elencoGruppoAttivita.size(); i++) {
			output.add(elencoGruppoAttivita.get(i).getId_attivita_polo());
		}
		return output;
	}

	public List loadAttivitaGruppoPolo() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo) criteria.uniqueResult();
			Tbf_gruppo_attivita gruppo = (Tbf_gruppo_attivita) loadNoLazy(session,
					Tbf_gruppo_attivita.class, polo.getId_gruppo_attivita()
							.getId_gruppo_attivita_polo());
			// if (polo.getId_gruppo_attivita().getId_gruppo_attivita_polo() !=
			// 1) {
			criteria = session.createCriteria(Trf_gruppo_attivita_polo.class);
			criteria.add(Restrictions.eq("id_gruppo_attivita_polo", gruppo));
			return criteria.list();
			// }
			// else
			// return null;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getLivelloParametro(int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametro = (Tbf_parametro) loadNoLazy(session,
					Tbf_parametro.class, idParametro);
			return Integer.parseInt(parametro.getCd_livello());
		} catch (HibernateException he) {
			throw new DaoManagerException();
		}
	}

	public List getAttivitaPolo(String cod_polo, String cod_attivita)
			throws DaoManagerException {
		try {
			Tbf_polo polo = new Tbf_polo();

			polo.setCd_polo(cod_polo);

			Tbf_attivita attivita = new Tbf_attivita();
			attivita.setCd_attivita(cod_attivita);

			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Trf_attivita_polo.class);
			Criteria suppCrit = criteria.createCriteria("cd_polo");
			suppCrit.add(Restrictions.eq("cd_polo", cod_polo));
			Criteria suppCrit1 = criteria.createCriteria("cd_attivita");
			suppCrit1.add(Restrictions.eq("cd_attivita", cod_attivita));
			// criteria.add(Restrictions.eq("cd_polo.cd_polo", polo));
			// criteria.add(Restrictions.eq("cd_attivita.cd_attivita",
			// attivita));
			List<Trf_attivita_polo> results = criteria
					.list();
			return results;
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public Tbf_parametro getParametri(int idParametro) throws DaoManagerException {
		// Class cls = null;
		// Object codici = null;

		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Tbf_parametro.class);
		criteria.add(Restrictions.eq("id_parametro", idParametro));
		return (Tbf_parametro) criteria.uniqueResult();
	}

	public List<Tb_codici> getAuthorities() throws DaoManagerException {

		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tb_codici.class);
		cr.add(Expression.eq("tp_tabella", CodiciType.CODICE_TIPO_AUTHORITY.getTp_Tabella() ));
		return cr.list();
	}

	public List<Tb_codici> getMateriali() throws DaoManagerException {

		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tb_codici.class);
		cr.add(Expression.eq("tp_tabella", CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO.getTp_Tabella() ));
		return cr.list();
	}

	public List getAttivitaGruppo(int idGruppo) throws DaoManagerException {
		{
			Session session = this.getCurrentSession();
			Criteria cr = session
					.createCriteria(Trf_gruppo_attivita_polo.class);
			// cr.add(Restrictions.eq("id_gruppo_attivita_polo",Integer.valueOf(idGruppo)));

			Tbf_gruppo_attivita id_gruppo_attivita_polo = new Tbf_gruppo_attivita();
			// ATTENTO.... da verificare id_gruppo_attivita_polo.set
			// setId_gruppo_attivita_polo(idGruppo);

			cr.add(Restrictions.eq("id_gruppo_attivita_polo",
					id_gruppo_attivita_polo));

			// Expression.eq("id_gruppo_attivita_polo", idGruppo));
			List ret = cr.list();
			return ret;
		}
	}

	public List getGruppi() throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tbf_gruppo_attivita.class);
		List ret = cr.list();
		return ret;
	}

	/**
	 * Chiedi l'identificativo di un gruppo identificato dal nome
	 *
	 * @param nomeGruppo
	 * @return id del gruppo o -1 in caso di gruppo non presente
	 * @throws DaoManagerException
	 */
	public int getGruppo(String nomeGruppo) throws DaoManagerException {
		Tbf_gruppo_attivita gruppoAttivita;

		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tbf_gruppo_attivita.class);
		cr.add(Restrictions.eq("ds_name", nomeGruppo));
		List ret = cr.list();
		if (ret.size() == 0)
			return -1;
		gruppoAttivita = (Tbf_gruppo_attivita) ret.get(0);
		return gruppoAttivita.getId_gruppo_attivita_polo();
	}

	/**
	 * Chiedi il codice polo
	 *
	 * @return istanza di Tbf_polo
	 * @throws DaoManagerException
	 */
	public Tbf_polo getPolo() throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tbf_polo.class);
		List ret = cr.list();
		if (ret.size() == 0)
			return null;
		return (Tbf_polo) ret.get(0);
	}

	public boolean inserisciModificaGruppoAttivita(
			Tbf_gruppo_attivita gruppo_attivita) throws DaoManagerException {
		boolean ret = false;
		try {
			Session session = this.getCurrentSession();
			// session.save(gruppo_attivita);
			session.saveOrUpdate(gruppo_attivita);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return ret = true;
	}

	public boolean inserisciModificaGruppoAttivita(Tbf_polo polo)
			throws DaoManagerException {
		boolean ret = false;
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(polo);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return ret = true;
	}

	public boolean inserisciModificaGruppoAttivitaPolo(
			Trf_gruppo_attivita_polo gruppo_attivita_polo)
			throws DaoManagerException {
		boolean ret = false;
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(gruppo_attivita_polo);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return ret = true;
	}

	public boolean inserisciModificaGruppoAttivitaPolo(Tbf_polo polo)
			throws DaoManagerException {
		boolean ret = false;
		try {
			Session session = this.getCurrentSession();
			session.saveOrUpdate(polo);
		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
		return ret = true;
	}

	public void rimuoviGruppoAttivitaPolo(int id_gruppo_attivita_polo)
			throws DaoManagerException {
		boolean ret = false;
		try {
			Session session = this.getCurrentSession();

			// Rimuovi il gruppo dalla lista delle gruppi
			// fatto da HIB quando si elimina un gruppo in tbf_gruppo_attivita
			// deleteAndEvict("select book FROM trf_gruppo_attivita_polo where id_gruppo_attivita_polo = "
			// + id_gruppo_attivita_polo);

			// Rimuovi il gruppo dalla lista dei gruppi
			deleteAndEvict("select book FROM tbf_gruppo_attivita where id_gruppo_attivita_polo = "
					+ id_gruppo_attivita_polo);

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

	public List getElencoBiblioteche() throws DaoManagerException {
		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tbf_biblioteca.class);
		List ret = cr.list();
		return ret;
	}

	public int creaGruppoAttivitaPolo(String codicePolo)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_polo polo = (Tbf_polo) loadNoLazy(session, Tbf_polo.class, codicePolo);
			Tbf_gruppo_attivita gruppo = polo.getId_gruppo_attivita();
			if (gruppo.getId_gruppo_attivita_polo() == 1) {
				Tbf_gruppo_attivita gruppoNew = new Tbf_gruppo_attivita();
				gruppoNew.setCd_polo(polo);
				gruppoNew.setDs_name("POLO");
				session.save(gruppoNew);
				return gruppoNew.getId_gruppo_attivita_polo();
			} else
				return gruppo.getId_gruppo_attivita_polo();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaAttivitaDelGruppo(int idGruppo)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session
					.createQuery("delete from Trf_gruppo_attivita_polo where id_gruppo_attivita_polo.id = "
							+ idGruppo);
			query.executeUpdate();
			session.flush();
			clearCache("Trf_gruppo_attivita_polo");
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciAttivitaDelGruppo(List<String> elencoAttivita,
			int idGruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			// Criteria criteria =
			// session.createCriteria(Tbf_gruppo_attivita.class);
			// criteria.add(Restrictions.eq("id_gruppo_attivita_polo",
			// idGruppo));
			// List record = criteria.list();
			// Tbf_gruppo_attivita gruppo = (Tbf_gruppo_attivita)record.get(0);
			Tbf_gruppo_attivita gruppo = (Tbf_gruppo_attivita) loadNoLazy(session,
					Tbf_gruppo_attivita.class, idGruppo);
			for (int i = 0; i < elencoAttivita.size(); i++) {
				Tbf_attivita attivitaDB = (Tbf_attivita) loadNoLazy(session, Tbf_attivita.class, elencoAttivita.get(i));
				Criteria c = session.createCriteria(Trf_attivita_polo.class);
				c.add(Restrictions.eq("cd_attivita", attivitaDB));
				Trf_attivita_polo attivita = (Trf_attivita_polo) c.uniqueResult();
				if (attivita != null) {
					Trf_gruppo_attivita_polo tabella = new Trf_gruppo_attivita_polo();
					tabella.setId_attivita_polo(attivita);
					tabella.setId_gruppo_attivita_polo(gruppo);
					tabella.setFl_include('S');
					session.saveOrUpdate(tabella);
					session.evict(attivitaDB);
				} else
					log.error("L'attivita con codice "
							+ elencoAttivita.get(i)
							+ " non è stata inserita nel profilo del polo. Controllare la tabella Trf_attivita_polo!");

			}

			session.flush();
			session.clear();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int creaProfiloParametroPolo(String codicePolo, String codiceUtente)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			String utente = getFirmaUtente(Integer.parseInt(codiceUtente));
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo parametroPolo = (Tbf_polo) criteria.uniqueResult();
			if (parametroPolo.getId_parametro().getId_parametro() == 1) {
				Tbf_parametro parametro = (Tbf_parametro) loadNoLazy(session,
						Tbf_parametro.class, 1);
				Tbf_parametro profilo = new Tbf_parametro();
				profilo.setCd_livello(parametro.getCd_livello());
				profilo.setTp_ret_doc(parametro.getTp_ret_doc());
				profilo.setTp_all_pref(parametro.getTp_all_pref());
				profilo.setCd_liv_ade(parametro.getCd_liv_ade());
				profilo.setFl_spogli(parametro.getFl_spogli());
				profilo.setFl_aut_superflui(parametro.getFl_aut_superflui());
				profilo.setUte_ins(utente);
				profilo.setUte_var(utente);
				profilo.setFl_canc(parametro.getFl_canc());
				Timestamp now = DaoManager.now();
				profilo.setTs_ins(now);
				profilo.setTs_var(now);
				profilo.setSololocale(parametro.getSololocale());
				session.save(profilo);
				return profilo.getId_parametro();
			}
			return parametroPolo.getId_parametro().getId_parametro();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciParAuth(
			List<GruppoParametriVO> elencoGruppiParametri, int idParametro)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametroDB = (Tbf_parametro) loadNoLazy(session,
					Tbf_parametro.class, idParametro);
			for (int i = 0; i < elencoGruppiParametri.size(); i++) {
				GruppoParametriVO gruppo = elencoGruppiParametri.get(i);
				Tbf_par_auth authority = new Tbf_par_auth();
				authority.setCd_par_auth(gruppo.getCodice());
				authority.setId_parametro(parametroDB);
				authority.setCd_contr_sim("1");
				for (int j = 0; j < gruppo.getElencoParametri().size(); j++) {
					ParametroVO parametro = gruppo.getElencoParametri().get(j);
					if (parametro.getDescrizione().endsWith("abil")) {
						authority.setTp_abil_auth(parametro.getSelezione()
								.charAt(0));
					} else if (parametro.getDescrizione().endsWith(
							"abil_legame")) {
						authority.setFl_abil_legame((parametro.getSelezione()
								.charAt(0)));
					} else if (parametro.getDescrizione().endsWith("leg_auth")) {
						authority.setFl_leg_auth((parametro.getSelezione()
								.charAt(0)));
					} else if (parametro.getDescrizione().endsWith("livello")) {
						authority.setCd_livello((parametro.getSelezione()
								.trim()));
					} else if (parametro.getDescrizione().endsWith(
							"abil_forzat")) {
						authority.setFl_abil_forzat((parametro.getSelezione()
								.charAt(0)));
					} else if (parametro.getDescrizione()
							.endsWith("sololocale")) {
						authority.setSololocale((parametro.getSelezione()
								.charAt(0)));
					}
				}
				session.saveOrUpdate(authority);
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciParMat(
			List<GruppoParametriVO> elencoGruppiParametri, int idParametro)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametroDB = (Tbf_parametro) loadNoLazy(session,
					Tbf_parametro.class, idParametro);
			for (int i = 0; i < elencoGruppiParametri.size(); i++) {
				GruppoParametriVO gruppo = elencoGruppiParametri.get(i);
				Tbf_par_mat materiale = new Tbf_par_mat();
				materiale.setCd_par_mat(gruppo.getCodice().charAt(0));
				materiale.setId_parametro(parametroDB);
				materiale.setCd_contr_sim("1");
				for (int j = 0; j < gruppo.getElencoParametri().size(); j++) {
					ParametroVO parametro = gruppo.getElencoParametri().get(j);
					if (parametro.getDescrizione().endsWith("abil")) {
						materiale.setTp_abilitaz(parametro.getSelezione()
								.charAt(0));
					} else if (parametro.getDescrizione().endsWith("livello")) {
						materiale.setCd_livello((parametro.getSelezione()
								.trim()));
					} else if (parametro.getDescrizione().endsWith(
							"abil_forzat")) {
						materiale.setFl_abil_forzat((parametro.getSelezione()
								.charAt(0)));
					} else if (parametro.getDescrizione()
							.endsWith("sololocale")) {
						materiale.setSololocale((parametro.getSelezione()
								.charAt(0)));
					}
				}
				session.saveOrUpdate(materiale);
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciParSem(
			List<GruppoParametriVO> elencoGruppiParametri, int idParametro)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametroDB = (Tbf_parametro) loadNoLazy(session,
					Tbf_parametro.class, idParametro);

			// Elimino i record della tabella tbf_par_sem
			Criteria criteria = session.createCriteria(Tbf_par_sem.class);
			criteria.add(Restrictions.eq("id_parametro", parametroDB));
			List<Tbf_par_sem> elencoParSem = criteria.list();
			for (int es = 0; es < elencoParSem.size(); es++)
				deleteAndEvict(elencoParSem.get(es));
			session.flush();

			for (int i = 0; i < elencoGruppiParametri.size(); i++) {
				GruppoParametriVO gruppo = elencoGruppiParametri.get(i);
				Tbf_par_sem semantica = new Tbf_par_sem();
				String codice = gruppo.getDescrizione();
				if (codice.endsWith("classificazioni"))
					semantica.setTp_tabella_codici(CodiciType.CODICE_SISTEMA_CLASSE.getTp_Tabella());
				else if (codice.endsWith("soggetti"))
					semantica.setTp_tabella_codici(CodiciType.CODICE_SOGGETTARIO.getTp_Tabella());
				else if (codice.endsWith("thesauri"))
					semantica.setTp_tabella_codici(CodiciType.CODICE_THESAURO.getTp_Tabella());
				semantica.setId_parametro(parametroDB);
				for (int j = 0; j < gruppo.getElencoParametri().size(); j++) {
					ParametroVO parametro = gruppo.getElencoParametri().get(j);
					if (parametro.getDescrizione().endsWith("classificazione")
							|| parametro.getDescrizione().endsWith("soggetto")
							|| parametro.getDescrizione().endsWith("thesauro")) {
						String codiceTipo = "";
						if (parametro.getDescrizione().endsWith("classificazione")) {
							codiceTipo = CodiciType.CODICE_SISTEMA_CLASSE.getTp_Tabella();
						} else if (codice.endsWith("soggetti"))
							codiceTipo = CodiciType.CODICE_SOGGETTARIO.getTp_Tabella();
						else if (codice.endsWith("thesauri"))
							codiceTipo = CodiciType.CODICE_THESAURO.getTp_Tabella();

						CodiciType cod = null;
						List<TB_CODICI> elencoCodici = null;
						try {
							cod = CodiciType.fromString(codiceTipo);
							elencoCodici = CodiciProvider.getCodici(cod);
						} catch (Exception e) {
							e.printStackTrace();
						}
						for (int k = 1; k < elencoCodici.size(); k++) {
							TB_CODICI tabCodice = elencoCodici.get(k);
							if (tabCodice.getDs_tabella().trim().equals(
									parametro.getSelezione())) {
								semantica.setCd_tabella_codici(tabCodice
										.getCd_tabella().trim());
								break;
							}
						}

					} else if (parametro.getDescrizione()
							.endsWith("sololocale")) {
						semantica.setSololocale((parametro.getSelezione()
								.charAt(0)));
					}
				}
				session.saveOrUpdate(semantica);
			}
			session.flush();

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void attivaProfiloAttivita(int idGruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_polo.class);
			Tbf_polo parametroPolo = (Tbf_polo) c.uniqueResult();
			Tbf_gruppo_attivita gruppoAtt = (Tbf_gruppo_attivita) loadNoLazy(session, Tbf_gruppo_attivita.class, idGruppo);
			parametroPolo.setId_gruppo_attivita(gruppoAtt);
			session.saveOrUpdate(parametroPolo);
			session.flush();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void attivaProfiloParametri(int idParametro)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametro = (Tbf_parametro) loadNoLazy(session, Tbf_parametro.class, idParametro);
			Criteria c = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo) c.uniqueResult();
			polo.setId_parametro(parametro);
			session.saveOrUpdate(polo);

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getIdParametro() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo parametroPolo = (Tbf_polo) criteria.uniqueResult();
			return parametroPolo.getId_parametro().getId_parametro();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getIdGruppoAttivita() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo parametroPolo = (Tbf_polo) criteria.uniqueResult();
			return parametroPolo.getId_gruppo_attivita()
					.getId_gruppo_attivita_polo();
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void aggiornaProfiloBiblioteche(int idGruppoOld, int idGruppoNew,
			List<String> elencoAttivita, String codiceUtente) throws DaoManagerException {
		try {
			boolean isChangeGrp = (idGruppoOld != idGruppoNew);

			Session session = this.getCurrentSession();
			Tbf_gruppo_attivita gruppoNew = (Tbf_gruppo_attivita) loadNoLazy(session, Tbf_gruppo_attivita.class, idGruppoNew);

			Criteria c = session.createCriteria(Tbf_biblioteca_in_polo.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			List<Tbf_biblioteca_in_polo> biblioteche = c.list();
			//almaviva5_20140702
			String utente = getFirmaUtente(Integer.parseInt(codiceUtente));
			for (Tbf_biblioteca_in_polo bib : biblioteche) {
				Tbf_gruppo_attivita gruppoBiblioteca = bib.getId_gruppo_attivita_polo();
				//le biblioteche che condividono già il profilo di polo non vanno aggiornate
				int idGruppoBib = gruppoBiblioteca.getId_gruppo_attivita_polo();
				if (idGruppoBib == idGruppoNew) {
					session.evict(gruppoBiblioteca);
					session.evict(bib);
					continue;
				}

				//se il polo sta cambiando gruppo le biblioteche che ne condividono il profilo cambiano gruppo insieme al polo
				if (isChangeGrp && (idGruppoBib == idGruppoOld) ) {
					bib.setId_gruppo_attivita_polo(gruppoNew);
				}

				//aggiorno biblioteche con un gruppo separato
				if (!ValidazioneDati.in(idGruppoBib, idGruppoOld, idGruppoNew))
					this.aggiornaAttivitaDelGruppo(elencoAttivita, gruppoBiblioteca);

				bib.setTs_var(DaoManager.now());
				bib.setUte_var(utente);
				//session.update(bib);
				session.flush();
				session.evict(gruppoBiblioteca);
				session.evict(bib);
			}

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<String> aggiornaProfiliBibliotecari(
			List<String> elencoAttivita) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_profilo_abilitazione.class);
			List<Tbf_profilo_abilitazione> elencoProfili = c.list();
			List<String> elencoUtentiAggiornati = new ArrayList<String>();
			for (int i = 0; i < elencoProfili.size(); i++) {
				Tbf_profilo_abilitazione profilo = elencoProfili.get(i);
				boolean rimosso = this.aggiornaAttivitaDelProfilo(elencoAttivita, profilo);
				if (rimosso) {
					List<Tbf_bibliotecario> elencoBibliotecari =
							new ArrayList<Tbf_bibliotecario>(profilo.getTbf_bibliotecario());
					for (int j = 0; j < elencoBibliotecari.size(); j++) {
						Tbf_bibliotecario bibliotecario = elencoBibliotecari.get(j);
						String useridUtente = bibliotecario
								.getId_utente_professionale()
								.getTbf_utenti_professionali_web().getUserid();
						elencoUtentiAggiornati.add(useridUtente.trim());
						//session.evict(bibliotecario);
					}
				}
				session.evict(profilo);
			}
			session.flush();
			session.clear();
			return elencoUtentiAggiornati;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean aggiornaAttivitaDelProfilo(List<String> elencoAttivita,
			Tbf_profilo_abilitazione profilo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Trf_profilo_biblioteca.class);
			c.add(Restrictions.eq("cd_prof", profilo));
			List<Trf_profilo_biblioteca> abilitazioni = c.list();
			boolean rimosso = false;
			for (Trf_profilo_biblioteca attivita : abilitazioni) {
				if (!elencoAttivita.contains(attivita.getCd_attivita().getCd_attivita())) {
					deleteAndEvict(attivita);
					rimosso = true;
				}
			}
			// eliminiamo le attività padri senza figli:
			session.flush();
			this.pulisciProfiloAttivitaPadri(profilo.getId_profilo_abilitazione());
			return rimosso;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void pulisciProfiloAttivitaPadri(int idProfilo)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_profilo_abilitazione profilo = (Tbf_profilo_abilitazione) loadNoLazy(session, Tbf_profilo_abilitazione.class, idProfilo);
			Criteria c = session.createCriteria(Trf_profilo_biblioteca.class);
			c.add(Restrictions.eq("cd_prof", profilo));

			List<Trf_profilo_biblioteca> abilitazioni = c.list();
			c = session.createCriteria(Tbf_attivita.class);
			List<Tbf_attivita> allAttivita = c.list();
			int size = ValidazioneDati.size(abilitazioni);
			for (int i = 0; i < size; i++) {
				Trf_profilo_biblioteca abilit = abilitazioni.get(i);
				Tbf_attivita attivita = abilit.getCd_attivita();
				if (attivita.getCd_funzione_parent() == null && this.controllaFigliAttivita(attivita, allAttivita)) {
					boolean trovato = false;
					for (int z = 0; z < size; z++) {
						Trf_profilo_biblioteca ab = abilitazioni.get(z);
						if (ab.getCd_attivita().getCd_funzione_parent() != null
								&& ab.getCd_attivita().getCd_funzione_parent().equals(abilit.getCd_attivita().getCd_attivita())) {
							trovato = true;
							break;
						}
					}
					if (!trovato)
						deleteAndEvict(abilit);
				}
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	private boolean controllaFigliAttivita(Tbf_attivita attivita,
			List<Tbf_attivita> elencoAttivita) {
		for (int i = 0; i < elencoAttivita.size(); i++) {
			Tbf_attivita att = elencoAttivita.get(i);
			if (att.getCd_funzione_parent() != null
					&& att.getCd_funzione_parent().trim().equals(
							attivita.getCd_attivita().trim()))
				return true;
		}
		return false;
	}

	public void aggiornaAttivitaDelGruppo(List<String> elencoAttivita,
			Tbf_gruppo_attivita gruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Trf_gruppo_attivita_polo.class);
			c.add(Restrictions.eq("id_gruppo_attivita_polo", gruppo));
			List<Trf_gruppo_attivita_polo> elencoAbilit = c.list();
			for (int i = 0; i < elencoAbilit.size(); i++) {
				Trf_gruppo_attivita_polo abilit = elencoAbilit.get(i);
				if (!elencoAttivita.contains(abilit.getId_attivita_polo().getCd_attivita().getCd_attivita())) {
					deleteAndEvict(abilit);
				}
			}
			// eliminiamo le attività padri senza figli:
			this.pulisciGruppoAttivitaPadri(gruppo.getId_gruppo_attivita_polo());
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void pulisciGruppoAttivitaPadri(int idGruppo)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_gruppo_attivita gruppo = (Tbf_gruppo_attivita) loadNoLazy(session,
					Tbf_gruppo_attivita.class, idGruppo);
			Criteria c = session.createCriteria(Trf_gruppo_attivita_polo.class);
			c.add(Restrictions.eq("id_gruppo_attivita_polo", gruppo));

			List<Trf_gruppo_attivita_polo> elencoAbilit = c.list();
			c = session.createCriteria(Tbf_attivita.class);
			List<Tbf_attivita> allAttivita = c.list();

			for (int i = 0; i < elencoAbilit.size(); i++) {
				Trf_gruppo_attivita_polo abilit = elencoAbilit.get(i);
				Tbf_attivita attivita = abilit.getId_attivita_polo().getCd_attivita();
				if (attivita.getCd_funzione_parent() == null
						&& this.controllaFigliAttivita(attivita, allAttivita)) {
					boolean trovato = false;
					for (int z = 0; z < elencoAbilit.size(); z++) {
						Trf_gruppo_attivita_polo ab = elencoAbilit.get(z);
						if (ab.getId_attivita_polo().getCd_attivita()
								.getCd_funzione_parent() != null
								&& ab.getId_attivita_polo().getCd_attivita()
										.getCd_funzione_parent().equals(
												abilit.getId_attivita_polo()
														.getCd_attivita()
														.getCd_attivita())) {
							trovato = true;
							session.evict(ab);
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

		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<String> controllaAttivita(List<String> elencoAttivita)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			List<String> output = new ArrayList<String>();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo) criteria.uniqueResult();
			Tbf_gruppo_attivita gruppoPolo = polo.getId_gruppo_attivita();
			// Controllo le attivita' dei gruppi, escluse quelle attive per il
			// polo:
			criteria = session.createCriteria(Trf_gruppo_attivita_polo.class);
			criteria
					.add(Restrictions.ne("id_gruppo_attivita_polo", gruppoPolo));
			Tbf_gruppo_attivita gruppoDef = (Tbf_gruppo_attivita) loadNoLazy(session,
					Tbf_gruppo_attivita.class, 1);
			criteria.add(Restrictions.ne("id_gruppo_attivita_polo", gruppoDef));
			List<Trf_gruppo_attivita_polo> elencoAttivitaGruppo = criteria
					.list();
			for (int i = 0; i < elencoAttivitaGruppo.size(); i++) {
				Trf_gruppo_attivita_polo att = elencoAttivitaGruppo.get(i);
				if (!elencoAttivita.contains(att.getId_attivita_polo()
						.getCd_attivita().getCd_attivita()))
					output.add(att.getId_attivita_polo().getCd_attivita()
							.getCd_attivita());
			}
			// // Controllo le attivita' dei profili dei bibliotecari:
			// criteria = session.createCriteria(Trf_profilo_biblioteca.class);
			// List<Trf_profilo_biblioteca> elencoAttivitaProfili =
			// criteria.list();
			// for (int i = 0; i<elencoAttivitaProfili.size(); i++) {
			// Trf_profilo_biblioteca att = elencoAttivitaProfili.get(i);
			// if
			// (!elencoAttivita.contains(att.getCd_attivita().getCd_attivita()))
			// return true;
			// }
			return output;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void rimuoviParAuth(int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session
					.createQuery("delete from Tbf_par_auth where id_parametro != 1");
			query.executeUpdate();
			clearCache("Tbf_par_auth");
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void rimuoviParMat(int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session
					.createQuery("delete from Tbf_par_mat where id_parametro != 1");
			query.executeUpdate();
			clearCache("Tbf_par_mat");
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void rimuoviParSem(int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session
					.createQuery("delete from Tbf_par_sem where id_parametro != 1");
			query.executeUpdate();
			clearCache("Tbf_par_sem");
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean checkSoloLocaleAuthPolo() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_polo polo = this.getPolo();
			Tbf_parametro parametro = polo.getId_parametro();
			List<Tbf_par_auth> elencoAuth = new ArrayList<Tbf_par_auth>(
					parametro.getTbf_par_auth());
			for (int i = 0; i < elencoAuth.size(); i++) {
				Tbf_par_auth auth = elencoAuth.get(i);
				if (auth.getSololocale() == 'N')
					return false;
			}
			return true;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean checkSoloLocaleMatPolo() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_polo polo = this.getPolo();
			Tbf_parametro parametro = polo.getId_parametro();
			List<Tbf_par_mat> elencoMat = new ArrayList<Tbf_par_mat>(
					parametro.getTbf_par_mat());
			for (int i = 0; i < elencoMat.size(); i++) {
				Tbf_par_mat mat = elencoMat.get(i);
				if (mat.getSololocale() == 'N')
					return false;
			}
			return true;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	// Controlla l'esistenza delle attivita' provenienti dall'indice con quelle
	// presenti nel sistema.
	// Il controllo viene effettuato con la descrizione attivita'.
	public boolean controllaEsistenzaAttivita(List<String> attivitaAggiunte)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session
					.createCriteria(Tbf_attivita_sbnmarc.class);
			List<Tbf_attivita_sbnmarc> elencoAttivita = criteria.list();
			for (int i = 0; i < attivitaAggiunte.size(); i++) {
				String att = attivitaAggiunte.get(i).trim().toUpperCase();
				boolean trovata = false;
				for (int p = 0; p < elencoAttivita.size(); p++) {
					if (elencoAttivita.get(p).getDs_attivita().trim()
							.toUpperCase().equals(att)) {
						trovata = true;
						break;
					}
				}
				if (!trovata)
					return false;
			}
			return true;
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaAttivitaPolo(List<Trf_attivita_polo> elencoAttivita)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			for (int i = 0; i < elencoAttivita.size(); i++) {
				session.delete(elencoAttivita.get(i));
				session.flush();
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void aggiungiAttivitaPolo(List<String> elencoAttivita)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			for (int i = 0; i < elencoAttivita.size(); i++) {
				String descAttivita = elencoAttivita.get(i);
				Criteria criteria = session
						.createCriteria(Tbf_attivita_sbnmarc.class);
				criteria.add(Restrictions.ilike("ds_attivita", descAttivita));
				List<Tbf_attivita_sbnmarc> elencoAttMarc = criteria.list();
				if (elencoAttMarc == null || elencoAttMarc.size() != 1)
					throw new DaoManagerException();
				Tbf_attivita_sbnmarc attMarc = elencoAttMarc.get(0);
				Trf_attivita_polo attPolo = new Trf_attivita_polo();
				attPolo.setCd_polo(this.getPolo());
				attPolo.setCd_attivita(attMarc.getTbf_attivita());
				attPolo.setFl_canc('N');
				Timestamp tempo = DaoManager.now();
				attPolo.setTs_ins(tempo);
				attPolo.setTs_var(tempo);
				attPolo.setUte_ins("INDICE");
				attPolo.setUte_var("INDICE");
				session.save(attPolo);
				session.flush();
			}
		} catch (HibernateException he) {
			throw new DaoManagerException(he);
		} catch (DaoManagerException de) {
			de.printStackTrace();
		}
	}

	public Tbf_attivita getAttivita(String codAttivita)
			throws DaoManagerException {
		Session session = getCurrentSession();
		return (Tbf_attivita) session.get(Tbf_attivita.class, codAttivita);
	}

	public void aggiornaParametriBiblioteche(String codicePolo, String codiceUtente) throws DaoManagerException {
		Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		Session session = this.getCurrentSession();
		Criteria c = session.createCriteria(Tbf_polo.class);
		c.add(Restrictions.idEq(codicePolo));
		Tbf_polo polo = (Tbf_polo) c.uniqueResult();
		List<Tbf_par_sem> parSemPolo = new ArrayList<Tbf_par_sem>(polo.getId_parametro().getTbf_par_sem()) ;
		Set<Tbf_biblioteca_in_polo> biblioteche = polo.getTbf_biblioteca_in_polo();
		String utente = getFirmaUtente(Integer.parseInt(codiceUtente));
		//allineamento parametri semantici biblioteca al polo
		for (Tbf_biblioteca_in_polo bib : biblioteche) {
			boolean deleted = false;
			Iterator<Tbf_par_sem> i = ((Set<Tbf_par_sem>) bib.getId_parametro().getTbf_par_sem()).iterator();
			while (i.hasNext()) {
				Tbf_par_sem p = i.next();
				if (!Lambda.exists(parSemPolo, new TbfParSemMatcher(p)) ) {
					dao.eliminaAbilitazioneOggettoSemantico(bib, p);
					i.remove();
					session.delete(p);
					deleted = true;
				}
			}
			session.flush();

			if (deleted) {
				//aggiornamento utenti della biblioteca
				dao.aggiornaParametriBibliotecari(bib);
				bib.setUte_var(utente);
				bib.setTs_var(now());
				session.update(bib);
			}

		}

		session.flush();
	}

	public Tbf_js_script loadScript(String scriptKey)
			throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_js_script.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.idEq(scriptKey));
			return (Tbf_js_script) c.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoManagerException(e);
		}
	}

}
