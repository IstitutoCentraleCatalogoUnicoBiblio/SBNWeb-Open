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
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppo_attivita;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_modello_profilazione_bibliotecario;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_sem;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;
import it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_gruppo_attivita_polo;
import it.iccu.sbn.polo.orm.amministrazione.Trf_profilo_biblioteca;
import it.iccu.sbn.polo.orm.amministrazione.Trf_utente_professionale_biblioteca;
import it.iccu.sbn.servizi.codici.CodiciProvider;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class Tbf_bibliotecarioDao extends DaoManager {

	private static Logger log = Logger.getLogger(Tbf_bibliotecarioDao.class);



	public Tbf_bibliotecarioDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Tbf_anagrafe_utenti_professionali> cercaUtenteProfessionaleWeb(String cognome, String nome, String username, String biblioteca, String dataAccesso, String abilitato, String ordinamento) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_anagrafe_utenti_professionali.class);
			if (cognome != null && !cognome.equals("") && !cognome.equals("*") && !cognome.contains("*")) {
				criteria.add(Restrictions.eq("cognome", cognome).ignoreCase());
			}
			else if (cognome != null && !cognome.equals("") && !cognome.equals("*") && cognome.contains("*"))
				criteria.add(Restrictions.ilike("cognome", cognome.replace("*", "%")));

			if (nome != null && !nome.equals("") && !nome.equals("*") && !nome.contains("*")) {
				criteria.add(Restrictions.eq("nome", nome).ignoreCase());
			}
			else if (nome != null && !nome.equals("") && !nome.equals("*") && nome.contains("*"))
				criteria.add(Restrictions.ilike("nome", nome.replace("*", "%")));

			if (ordinamento.equals("cognome")) {
				criteria.addOrder(Order.asc("cognome"));
			}
			else if (ordinamento.equals("nome")) {
				criteria.addOrder(Order.asc("nome"));
			}
			criteria.add(Restrictions.ne("fl_canc", 'S'));

			List<Tbf_anagrafe_utenti_professionali> output = criteria.list();

			for (int i = 0; i < output.size(); i++) {
				Tbf_anagrafe_utenti_professionali utente = output.get(i);
				if (utente.getTbf_utenti_professionali_web() != null && utente.getTbf_utenti_professionali_web().getUserid().toUpperCase().trim().equals("ROOT")) {
					output.remove(i);
					break;
				}
			}

			if (username != null && !username.equals("") && !username.equals("*")) {
				for (int i = 0; i < output.size(); i++) {
					Tbf_anagrafe_utenti_professionali utente = output.get(i);
					if (utente.getTbf_utenti_professionali_web() == null) {
						output.remove(i);
						i--;
					}
					else if (!username.contains("*")) {
						if (utente.getTbf_utenti_professionali_web() != null && !utente.getTbf_utenti_professionali_web().getUserid().trim().toLowerCase().equals(username.trim().toLowerCase())) {
							output.remove(i);
							i--;
						}
					}
					else {
						if (utente.getTbf_utenti_professionali_web() != null && !utente.getTbf_utenti_professionali_web().getUserid().toLowerCase().contains(username.trim().toLowerCase().replace("*", ""))) {
							output.remove(i);
							i--;
						}
					}
				}
			}

			if (biblioteca != null && !biblioteca.equals("") && !biblioteca.equals("*")) {
				for (int i = 0; i < output.size(); i++) {
					Tbf_anagrafe_utenti_professionali utente = output.get(i);
					List<Trf_utente_professionale_biblioteca> elencoBib = new ArrayList<Trf_utente_professionale_biblioteca>(utente.getTrf_utente_professionale_biblioteca());
					boolean trovato = false;
					for (int y = 0; y < elencoBib.size(); y++) {
						Trf_utente_professionale_biblioteca utenteBib = elencoBib.get(y);
						if (utenteBib.getCd_polo().getCd_biblioteca().equals(biblioteca)) {
							trovato = true;
						}
					}
					if (!trovato) {
						output.remove(i);
						i--;
					}
				}
			}
			if (dataAccesso != null && !dataAccesso.equals("")) {
				for (int i = 0; i < output.size(); i++) {
					if (output.get(i).getTbf_utenti_professionali_web() != null) {
						Tbf_utenti_professionali_web utente = output.get(i).getTbf_utenti_professionali_web();
						Timestamp dataDB = utente.getLast_access();
						int giorno = Integer.parseInt(dataAccesso.substring(0, 2));
						int mese = Integer.parseInt(dataAccesso.substring(3, 5)) - 1;
						int anno = Integer.parseInt(dataAccesso.substring(6, 10)) - 1900;
						Timestamp dataRic = new Timestamp(anno, mese, giorno, 0, 0, 0, 0);
						if (dataDB.compareTo(dataRic) >= 0 ) {
							output.remove(i);
							i--;
						}
					}
					else {
						output.remove(i);
						i--;
					}
				}
			}

			if (abilitato.equals("TRUE")) {
				for (int i = 0; i < output.size(); i++) {
					if (output.get(i).getTbf_utenti_professionali_web() == null) {
						output.remove(i);
						i--;
					}
				}
			}
			else if (abilitato.equals("FALSE")) {
				for (int i = 0; i < output.size(); i++) {
					if (output.get(i).getTbf_utenti_professionali_web() != null) {
						output.remove(i);
						i--;
					}
				}
			}

			return output;

		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	class StringComparator implements Comparator
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

	public List<Tbf_attivita> loadCodiciAttivitaBiblioteca(String idUtente) throws DaoManagerException  {

		try {
			Session session = this.getCurrentSession();
			Tbf_utenti_professionali_web utente = (Tbf_utenti_professionali_web)loadNoLazy(session, Tbf_utenti_professionali_web.class, Integer.parseInt(idUtente));
			List<Trf_utente_professionale_biblioteca> elencoBib = new ArrayList<Trf_utente_professionale_biblioteca>(utente.getId_utente_professionale().getTrf_utente_professionale_biblioteca());
			if (elencoBib.size() == 0)
				return null;
			Trf_utente_professionale_biblioteca biblioteca = elencoBib.get(0);
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", biblioteca.getCd_polo().getCd_biblioteca()));
			Tbf_biblioteca_in_polo biblio = (Tbf_biblioteca_in_polo)criteria.uniqueResult();
			Tbf_gruppo_attivita gruppo = biblio.getId_gruppo_attivita_polo();
			criteria = session.createCriteria(Trf_gruppo_attivita_polo.class);
			criteria.add(Restrictions.eq("id_gruppo_attivita_polo", gruppo));
			List<Trf_gruppo_attivita_polo> ret = criteria.list();
			List<Tbf_attivita> output = new ArrayList<Tbf_attivita>();
			for (int i = 0; i < ret.size(); i++) {
				output.add(ret.get(i).getId_attivita_polo().getCd_attivita());
			}
			Collections.sort(output, new StringComparator());
			return output;
		}
		catch (HibernateException e) {
			throw new DaoManagerException(e);
		}

	}

	public List loadAttivitaProfiloBibliotecario(String idUtente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_anagrafe_utenti_professionali utente = (Tbf_anagrafe_utenti_professionali)loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, Integer.parseInt(idUtente));
			Criteria criteria = session.createCriteria(Trf_profilo_biblioteca.class);
			criteria.add(Restrictions.eq("cd_prof", utente.getTbf_bibliotecario().getCd_prof()));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List loadAttivitaProfiloModello(String idModello) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_modello_profilazione_bibliotecario.class);
			criteria.add(Restrictions.eq("nome", idModello));
			Tbf_modello_profilazione_bibliotecario modello = (Tbf_modello_profilazione_bibliotecario)criteria.uniqueResult();
			criteria = session.createCriteria(Trf_gruppo_attivita_polo.class);
			criteria.add(Restrictions.eq("id_gruppo_attivita_polo", modello.getId_gruppo_attivita()));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getIdParametro(String idUtente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_anagrafe_utenti_professionali utente = (Tbf_anagrafe_utenti_professionali)loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, Integer.parseInt(idUtente));
			return utente.getTbf_bibliotecario().getId_parametro().getId_parametro();
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

	public List<Tb_codici> getAuthorities() throws DaoManagerException  {

		Session session = this.getCurrentSession();
		Criteria cr = session.createCriteria(Tb_codici.class);
		cr.add(Expression.eq("tp_tabella", "TPAU"));
		List ret = cr.list();
		return ret;
	}

	public List<Tb_codici> getMateriali() throws DaoManagerException  {

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

	public int getProfiloBibliotecario(String idUtente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_anagrafe_utenti_professionali utente = (Tbf_anagrafe_utenti_professionali)loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, Integer.parseInt(idUtente));
			return utente.getTbf_bibliotecario().getCd_prof().getId_profilo_abilitazione();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void eliminaAttivitaDelProfilo(int idProfilo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Query query = session.createQuery("delete from Trf_profilo_biblioteca where cd_prof.id = " + idProfilo);
			query.executeUpdate();
			clearCache("Trf_profilo_biblioteca");
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

	public void inserisciAttivitaDelProfilo(List<String> elencoAttivita, int idProfilo, String utenteInseritore) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_profilo_abilitazione profilo = (Tbf_profilo_abilitazione)loadNoLazy(session, Tbf_profilo_abilitazione.class, idProfilo);
			String utente = getFirmaUtente(Integer.parseInt(utenteInseritore));
			Timestamp now = now();
			for (int i = 0; i < elencoAttivita.size(); i++) {
				Tbf_attivita attivitaDB = (Tbf_attivita)loadNoLazy(session, Tbf_attivita.class, elencoAttivita.get(i));
				if (attivitaDB != null) {
					Trf_profilo_biblioteca tabella = new Trf_profilo_biblioteca();
					tabella.setCd_attivita(attivitaDB);
					tabella.setCd_prof(profilo);
					tabella.setFl_canc('N');
					tabella.setUte_ins(utente);
					tabella.setUte_var(utente);
					tabella.setTs_ins(now);
					tabella.setTs_var(now);
					session.saveOrUpdate(tabella);
				}
				else {
					log.error("L'attivita con codice " + elencoAttivita.get(i) +
							" non è stata inserita nel profilo del bibliotecario.");
				}
			}
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int creaProfiloParametroBibliotecario(String idUtente, String codiceUtenteInseritore) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_anagrafe_utenti_professionali utente = (Tbf_anagrafe_utenti_professionali)loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, Integer.parseInt(idUtente));
			int id_parametro = utente.getTbf_bibliotecario().getId_parametro().getId_parametro();
			if (id_parametro == 1) {
				String utenteInseritore = getFirmaUtente(Integer.parseInt(codiceUtenteInseritore));
				Tbf_parametro parametro = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, 1);
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
				Timestamp tempo = now();
				profilo.setTs_ins(tempo);
				profilo.setTs_var(tempo);
				profilo.setSololocale(parametro.getSololocale());
				session.save(profilo);
				return profilo.getId_parametro();
			}
			else
				return id_parametro;
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

	public void inserisciParSem(List<GruppoParametriVO> elencoGruppiParametri, int idParametro) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_parametro parametroDB = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, idParametro);

			//Elimino i record della tabella tbf_par_sem
			Criteria criteria = session.createCriteria(Tbf_par_sem.class);
			criteria.add(Restrictions.eq("id_parametro", parametroDB));
			List<Tbf_par_sem> elencoParSem = criteria.list();
			for (int es =0; es<elencoParSem.size(); es++)
				deleteAndEvict(elencoParSem.get(es));
			session.flush();

			for (int i = 0; i < elencoGruppiParametri.size(); i++) {
				GruppoParametriVO gruppo = elencoGruppiParametri.get(i);
				Tbf_par_sem semantica = new Tbf_par_sem();
				String codice = gruppo.getDescrizione();
				if (codice.endsWith("classificazioni"))
					semantica.setTp_tabella_codici("SCLA");
				else if (codice.endsWith("soggetti"))
					semantica.setTp_tabella_codici("SOGG");
				else if (codice.endsWith("thesauri"))
					semantica.setTp_tabella_codici("STHE");
				semantica.setId_parametro(parametroDB);
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
								semantica.setCd_tabella_codici(tabCodice.getCd_tabella().trim());
								break;
							}
						}

					}
					else if (parametro.getDescrizione().endsWith("sololocale")) {
						semantica.setSololocale((parametro.getSelezione().charAt(0)));
					}
				}
				session.saveOrUpdate(semantica);
			}
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void attivaProfiloParametri(String idUtente, int idParametro, int codiceUtenteInseritore) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			int id = Integer.parseInt(idUtente);
			Tbf_anagrafe_utenti_professionali utente = (Tbf_anagrafe_utenti_professionali)loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, id);
			Tbf_parametro parametro = (Tbf_parametro)loadNoLazy(session, Tbf_parametro.class, idParametro);
			Tbf_bibliotecario bibliotecario = utente.getTbf_bibliotecario();
			bibliotecario.setId_parametro(parametro);
			session.saveOrUpdate(utente);

			//almaviva5_20140627 update bibliotecario
			bibliotecario.setTs_var(now());
			bibliotecario.setUte_var(getFirmaUtente(codiceUtenteInseritore));
			session.saveOrUpdate(bibliotecario);

		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public List<String> getNomeBibliotecario(int idUtente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_anagrafe_utenti_professionali utente = (Tbf_anagrafe_utenti_professionali)loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, new Integer(idUtente));
			List<String> output = new ArrayList<String>();
			output.add(utente.getNome().trim() + " " + utente.getCognome().trim());
			output.add(utente.getTbf_utenti_professionali_web().getUserid());
			List<Trf_utente_professionale_biblioteca> elencoBib = new ArrayList<Trf_utente_professionale_biblioteca>(utente.getTrf_utente_professionale_biblioteca());
			Trf_utente_professionale_biblioteca biblioteca = elencoBib.get(0);
			Tbf_biblioteca bib = biblioteca.getCd_polo().getId_biblioteca();
			output.add(bib.getNom_biblioteca());
			//almaviva5_20111207 #4722
			output.add(bib.getCd_bib());
			return output;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public String getNomeBibliotecario(String userId) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_utenti_professionali_web.class);
			criteria.add(Restrictions.eq("userid", userId))
					.add(Restrictions.ne("fl_canc", 'S'));
			Tbf_utenti_professionali_web utente = (Tbf_utenti_professionali_web) criteria.uniqueResult();
			if (utente == null)
				return "Utente non registrato";

			Tbf_anagrafe_utenti_professionali idUte = utente.getId_utente_professionale();
			return idUte.getCognome().trim() + " " + idUte.getNome().trim();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	// Questo metodo inserisce un nuovo bibliotecario o ne modifica i suoi dati.
	//
	// utenteInseritore = id dell'utente amministratore che sta effettuando la modifica;
	// forzaInserimento = flag che indica che si sta modificando un bibliotecario gia' esistente;
	// abilitazione = flag che indica di salvare anche la parte di abilitazione sulle tabelle Tbf_utenti_professionali_web e Tbf_bibliotecario
	//
	public UtenteVO inserisciBibliotecario(UtenteVO bibliotecario, int utenteInseritore, boolean forzaInserimento, boolean abilitazione) throws DaoManagerException {
		try {
			String nome = bibliotecario.getNome();
			String cognome = bibliotecario.getCognome();
			String username = bibliotecario.getUsername();
			String biblioteca = bibliotecario.getBiblioteca();
			String ruolo = bibliotecario.getRuolo();
			String ufficio = bibliotecario.getUfficio();
			String note = bibliotecario.getNote();
			int id = bibliotecario.getId();
			char reset = bibliotecario.getChange_password();
			String password = bibliotecario.getPassword();
			Session session = this.getCurrentSession();
			Tbf_utenti_professionali_web utProf = new Tbf_utenti_professionali_web();
			utProf.setUserid(username);
			//Cerco il bibliotecario tramite username:
			Criteria criteria = session.createCriteria(Tbf_utenti_professionali_web.class);
			criteria.add(Restrictions.eq("userid", username).ignoreCase());
			List elenco = criteria.list();

			// Se la username già esiste e si stava cercando di inserire un nuovo bibliotecario, rispondo con errore.
			if (ValidazioneDati.isFilled(elenco) && !forzaInserimento) {
				bibliotecario.setInserito(2);
				return bibliotecario;
			}

			Tbf_utenti_professionali_web utentiProfessionali = new Tbf_utenti_professionali_web();
			Tbf_bibliotecario bibliotecarioReturn = new Tbf_bibliotecario();
			Tbf_anagrafe_utenti_professionali utenteAna = new Tbf_anagrafe_utenti_professionali();
			Trf_utente_professionale_biblioteca utenteBib = new Trf_utente_professionale_biblioteca();
			boolean esisteNonAbilitato = false;

			// Se la username esiste e si sta modificando un bibliotecario allora mi carico i suoi dati dalle relative tabelle.
			if (ValidazioneDati.isFilled(elenco) && forzaInserimento) {
				utentiProfessionali = (Tbf_utenti_professionali_web)elenco.get(0);
				bibliotecarioReturn = utentiProfessionali.getId_utente_professionale().getTbf_bibliotecario();
				utenteAna = utentiProfessionali.getId_utente_professionale();
				utenteBib = new ArrayList<Trf_utente_professionale_biblioteca>(utentiProfessionali.getId_utente_professionale().getTrf_utente_professionale_biblioteca()).get(0);
			}
			// Se la username non esiste ma il bibliotecario sì allora carico i dati a partire dall'anagrafica
			else if (ValidazioneDati.size(elenco) == 0 && forzaInserimento) {
				esisteNonAbilitato = true;
				utenteAna = (Tbf_anagrafe_utenti_professionali)loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, id);
				utenteBib = new ArrayList<Trf_utente_professionale_biblioteca>(utenteAna.getTrf_utente_professionale_biblioteca()).get(0);
				if (utenteAna.getTbf_utenti_professionali_web() != null) {
					utentiProfessionali = utenteAna.getTbf_utenti_professionali_web();
					bibliotecarioReturn = utenteAna.getTbf_bibliotecario();
				}
			}
			// Se il bibliotecario esiste e ne elimino la username, allora devo cancellare dal db le abilitazioni all sistema e il profilo:
			if (ValidazioneDati.isFilled(elenco) && forzaInserimento && !abilitazione) {
				SQLQuery query = session.createSQLQuery("delete from Tbf_utenti_professionali_web where userid = '" + username + "'");
				query.executeUpdate();
				clearCache("Tbf_utenti_professionali_web");

				query = session.createSQLQuery("delete from Tbf_par_auth where id_parametro = '" + bibliotecarioReturn.getId_parametro().getId_parametro() + "'");
				query.executeUpdate();
				clearCache("Tbf_par_auth");

				query = session.createSQLQuery("delete from Tbf_par_mat where id_parametro = '" + bibliotecarioReturn.getId_parametro().getId_parametro() + "'");
				query.executeUpdate();
				clearCache("Tbf_par_mat");

				query = session.createSQLQuery("delete from Tbf_par_sem where id_parametro = '" + bibliotecarioReturn.getId_parametro().getId_parametro() + "'");
				query.executeUpdate();
				clearCache("Tbf_par_sem");

				query = session.createSQLQuery("delete from Trf_profilo_biblioteca where id_profilo_abilitazione = '" + bibliotecarioReturn.getCd_prof().getId_profilo_abilitazione() + "'");
				query.executeUpdate();
				clearCache("Trf_profilo_biblioteca");

				//almaviva5_20100121 #3297
				query = session.createSQLQuery("delete from tbf_bibliotecario_default where id_utente_professionale = '" + bibliotecarioReturn.getId_utente_professionale().getId_utente_professionale() + "'");
				query.executeUpdate();
				clearCache("tbf_bibliotecario_default");

				//almaviva5_20110331 segnalazione AQ1: se ci sono abilitazioni ad-personam su iter servizio vanno cancellate fisicamente
				query = session.createSQLQuery("delete from Trl_attivita_bibliotecario where id_bibliotecario=" + utenteAna.getId_utente_professionale());
				query.executeUpdate();
				clearCache("Trl_attivita_bibliotecario");

				query = session.createSQLQuery("delete from Tbf_bibliotecario where id_utente_professionale = '" + bibliotecarioReturn.getId_utente_professionale().getId_utente_professionale() + "'");
				query.executeUpdate();
				clearCache("Tbf_bibliotecario");

				query = session.createSQLQuery("delete from Tbf_parametro where id_parametro = '" + bibliotecarioReturn.getId_parametro().getId_parametro() + "'");
				query.executeUpdate();
				clearCache("Tbf_parametro");

				query = session.createSQLQuery("delete from Tbf_profilo_abilitazione where id_profilo_abilitazione = '" + bibliotecarioReturn.getCd_prof().getId_profilo_abilitazione() + "'");
				query.executeUpdate();
				clearCache("Tbf_profilo_abilitazione");

				bibliotecario.setUsername("");
			}

			//Carico l'utente inseritore e la biblioteca del bibliotecario:
			String utente = getFirmaUtente(utenteInseritore);
			criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", biblioteca));
			Tbf_biblioteca_in_polo bibliotecaPolo = (Tbf_biblioteca_in_polo)criteria.uniqueResult();

			Timestamp ts = now();

			//Creo un nuovo parametro a partire da quello della biblioteca selezionata:
			if ((!forzaInserimento && abilitazione) || esisteNonAbilitato) {
				Tbf_parametro parametro = bibliotecaPolo.getId_parametro();
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
	//			tempo.setTime(System.currentTimeMillis());
				profilo.setTs_ins(ts);
				profilo.setTs_var(ts);
				profilo.setSololocale(parametro.getSololocale());
				session.save(profilo);
			/*
				//Ricopio per il bibliotecario i parametri del profilo della biblioteca:
				Object auth[] = parametro.getTbf_par_auth().toArray();
				Object mat[] = parametro.getTbf_par_mat().toArray();
				Object sem[] = parametro.getTbf_par_sem().toArray();

				for (int i = 0; i<auth.length; i++) {
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

				for (int i = 0; i<mat.length; i++) {
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

				for (int i = 0; i<sem.length; i++) {
					Tbf_par_sem par = (Tbf_par_sem)sem[i];
					Tbf_par_sem newpar = new Tbf_par_sem();
					newpar.setCd_tabella_codici(par.getCd_tabella_codici());
					newpar.setId_parametro(profilo);
					newpar.setSololocale(par.getSololocale());
					newpar.setTp_tabella_codici(par.getTp_tabella_codici());
					session.save(newpar);
				}
			*/
				bibliotecarioReturn.setId_parametro(profilo);

				//Creo il profilo per il bibliotecario senza attivita' selezionate:
				Tbf_profilo_abilitazione profiloAb = new Tbf_profilo_abilitazione();
				profiloAb.setCd_polo(bibliotecaPolo);
//				profiloAb.setCd_prof("000");
				profiloAb.setDsc_profilo("Profilo del bibliotecario " + nome.toUpperCase() + " " + cognome.toUpperCase());
				profiloAb.setFl_canc('N');
				profiloAb.setNota_profilo("Profilo di bibliotecario");
				profiloAb.setUte_ins(utente);
				profiloAb.setUte_var(utente);
				profiloAb.setTs_ins(ts);
				profiloAb.setTs_var(ts);
				session.save(profiloAb);

				bibliotecarioReturn.setCd_prof(profiloAb);
			}


			//Creo l'anagrafica dell'utente:
			utenteAna.setCognome(cognome);
			utenteAna.setNome(nome);
			utenteAna.setFl_canc('N');
			if (!forzaInserimento) {
				utenteAna.setTs_ins(ts);
				utenteAna.setUte_ins(utente);
			}
			utenteAna.setTs_var(ts);
			utenteAna.setUte_var(utente);
			session.saveOrUpdate(utenteAna);
			bibliotecario.setId(utenteAna.getId_utente_professionale());

			//Creo l'utente professionale web nel caso siano stati inseriti i dati di abilitazione:
			if (abilitazione) {
				utentiProfessionali.setChange_password(reset);
				utentiProfessionali.setFl_canc('N');
				// Nel caso in cui sto trattando un nuovo bibliotecario oppure uno esistente ma mai profilato,
				// creo la nuova istanza per la Tbf_utenti_professionali_web
				if (!forzaInserimento || esisteNonAbilitato) {
					utentiProfessionali.setId_utente_professionale(utenteAna);
					utentiProfessionali.setLast_access(ts);
					utentiProfessionali.setTs_ins(ts);
					utentiProfessionali.setUte_ins(utente);
					utentiProfessionali.setPassword(password);
					utentiProfessionali.setTs_var(ts);
					utentiProfessionali.setUserid(username);
					utentiProfessionali.setUte_var(utente);
					//almaviva5_20110126
					utentiProfessionali.setChange_password('S');
				}
				if (forzaInserimento && reset == 'S' && !esisteNonAbilitato) {
					utentiProfessionali.setPassword(password);
					utentiProfessionali.setTs_var(ts);
					utentiProfessionali.setUte_var(utente);
					utentiProfessionali.setFl_canc('N');
					utentiProfessionali.setLast_access(ts);
				}

				session.saveOrUpdate(utentiProfessionali);

				bibliotecario.setDataAccesso(utentiProfessionali.getLast_access().toString());
				bibliotecario.setTempoAccesso(utentiProfessionali.getLast_access());
				bibliotecario.setDataVariazione(utentiProfessionali.getTs_var().toString());
				bibliotecario.setTempoVariazione(utentiProfessionali.getTs_var());

				// Come sopra ma per la Tbf_bibliotecario
				if (!forzaInserimento || esisteNonAbilitato) {
					bibliotecarioReturn.setTs_ins(ts);
					bibliotecarioReturn.setUte_ins(utente);
					bibliotecarioReturn.setUte_var(utente);
					bibliotecarioReturn.setTs_var(ts);
					bibliotecarioReturn.setFl_canc('N');
					bibliotecarioReturn.setId_utente_professionale(utenteAna);
					session.saveOrUpdate(bibliotecarioReturn);
				}
			}

			//Creo l'associazione utente professionale/biblioteca:
			utenteBib.setCd_polo(bibliotecaPolo);
			utenteBib.setId_utente_professionale(utenteAna);
			utenteBib.setNote_competenze(note);
			utenteBib.setTp_ruolo(ruolo);
			utenteBib.setUfficio_appart(ufficio);
			session.saveOrUpdate(utenteBib);

			if (forzaInserimento)
				bibliotecario.setInserito(3);
			else
				bibliotecario.setInserito(0);

			return bibliotecario;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public UtenteVO caricaBibliotecario(int utente) throws DaoManagerException {
		try {
			UtenteVO bibliotecario = new UtenteVO();
			Session session = this.getCurrentSession();
			Tbf_anagrafe_utenti_professionali utenteProfessionale =
				(Tbf_anagrafe_utenti_professionali) loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, utente);
			if (utenteProfessionale == null) {
				bibliotecario.setInserito(1);
				return bibliotecario;
			}
			bibliotecario.setNome(utenteProfessionale.getNome().trim());
			bibliotecario.setCognome(utenteProfessionale.getCognome().trim());

			String username = "";
			Tbf_utenti_professionali_web uteWeb = utenteProfessionale.getTbf_utenti_professionali_web();
			if (uteWeb != null)
				username = uteWeb.getUserid();
			bibliotecario.setUsername(username.trim());

			List<Trf_utente_professionale_biblioteca> bibUtente = new ArrayList<Trf_utente_professionale_biblioteca>(utenteProfessionale.getTrf_utente_professionale_biblioteca());
			Trf_utente_professionale_biblioteca uteBib = bibUtente.get(0);

			String ufficio = "";
			if (uteBib != null && uteBib.getUfficio_appart() != null)
				ufficio = uteBib.getUfficio_appart();
			bibliotecario.setUfficio(ufficio.trim());

			String ruolo = "";
			if (uteBib != null && uteBib.getTp_ruolo() != null)
				ruolo = uteBib.getTp_ruolo();
			bibliotecario.setRuolo(ruolo.trim());

			String note = "";
			if (uteBib != null && uteBib.getNote_competenze() != null)
				note = uteBib.getNote_competenze();

			bibliotecario.setNote(note.trim());
			bibliotecario.setBiblioteca(uteBib.getCd_polo().getCd_biblioteca());
			//almaviva5_20100719 #3751
			bibliotecario.setCodPolo(uteBib.getCd_polo().getCd_polo().getCd_polo());
			if (uteWeb != null) {
				bibliotecario.setChange_password(uteWeb.getChange_password());
				bibliotecario.setDataAccesso(uteWeb.getLast_access().toString().trim());
				bibliotecario.setDataVariazione(uteWeb.getTs_var().toString().trim());
				bibliotecario.setTempoAccesso(uteWeb.getLast_access());
				bibliotecario.setTempoVariazione(uteWeb.getTs_var());
			}
			bibliotecario.setId(utente);
			bibliotecario.setInserito(0);
			return bibliotecario;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean controllaAbilitazioneBibliotecario(int utente) throws DaoManagerException {
		try {
			UtenteVO bibliotecario = new UtenteVO();
			Session session = this.getCurrentSession();
			Tbf_anagrafe_utenti_professionali utenteProfessionale =
				(Tbf_anagrafe_utenti_professionali) loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, utente);
			if (utenteProfessionale.getTbf_utenti_professionali_web() == null)
				return false;
			else
				return true;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public int getDurataPassword() throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			return 90;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_utenti_professionali_web getDatiUtente(String idUtente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			return (Tbf_utenti_professionali_web)loadNoLazy(session, Tbf_utenti_professionali_web.class, Integer.parseInt(idUtente));
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_modello_profilazione_bibliotecario getModello(String nomeModello) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_modello_profilazione_bibliotecario.class);
			criteria.add(Restrictions.eq("nome", nomeModello));
			return (Tbf_modello_profilazione_bibliotecario)criteria.uniqueResult();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_gruppo_attivita creaProfiloModello(String nomeModello) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_gruppo_attivita gruppoAttivita = new Tbf_gruppo_attivita();
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo)criteria.uniqueResult();
			gruppoAttivita.setCd_polo(polo);
			gruppoAttivita.setDs_name("Profilo del modello: " + nomeModello);
			session.save(gruppoAttivita);
			session.flush();
			return gruppoAttivita;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_parametro creaParametroModello(String utenteInseritore) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			String utente = getFirmaUtente(Integer.parseInt(utenteInseritore));
			Criteria criteria = session.createCriteria(Tbf_polo.class);
			Tbf_polo polo = (Tbf_polo)criteria.uniqueResult();
			Tbf_parametro parametro = polo.getId_parametro();
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
			Timestamp tempo = DaoManager.now();
			profilo.setTs_ins(tempo);
			profilo.setTs_var(tempo);
			profilo.setSololocale(parametro.getSololocale());
			session.save(profilo);
			session.flush();
			return profilo;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public Tbf_modello_profilazione_bibliotecario creaModello(String nomeModello, Tbf_gruppo_attivita profilo, Tbf_parametro parametro, String utenteInseritore) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			String utente = getFirmaUtente(Integer.parseInt(utenteInseritore));
			Tbf_modello_profilazione_bibliotecario modello = new Tbf_modello_profilazione_bibliotecario();
			modello.setNome(nomeModello);
			modello.setId_gruppo_attivita(profilo);
			modello.setId_parametro(parametro);
			modello.setFl_canc('N');
			Timestamp tempo = DaoManager.now();
			modello.setTs_ins(tempo);
			modello.setTs_var(tempo);
			modello.setUte_ins(utente);
			modello.setUte_var(utente);
			session.save(modello);
			return modello;
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
			session.flush();
			session.clear();
			clearCache("Trf_gruppo_attivita_polo");
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void inserisciAttivitaDelGruppo(List<String> elencoAttivita, Tbf_gruppo_attivita gruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			for (int i = 0; i < elencoAttivita.size(); i++) {
				Tbf_attivita attivitaDB = (Tbf_attivita)loadNoLazy(session, Tbf_attivita.class, elencoAttivita.get(i));
				Criteria criteria = session.createCriteria(Trf_attivita_polo.class);
				criteria.add(Restrictions.eq("cd_attivita", attivitaDB));
				Trf_attivita_polo attivita = (Trf_attivita_polo)criteria.uniqueResult();
				if (attivita != null) {
					Trf_gruppo_attivita_polo tabella = new Trf_gruppo_attivita_polo();
					tabella.setId_attivita_polo(attivita);
					tabella.setId_gruppo_attivita_polo(gruppo);
					tabella.setFl_include('S');
					//session.saveOrUpdate(tabella);
//					session.merge(tabella);
					session.save(tabella);
				}
				else {
					log.error("L'attivita con codice " + elencoAttivita.get(i) +
							" non è stata inserita nel profilo della biblioteca. Controllare la tabella Trf_attivita_polo!");
				}
			}
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public void pulisciGruppoAttivitaPadri(Tbf_gruppo_attivita gruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
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
			Criteria criteria = session.createCriteria(Tbf_modello_profilazione_bibliotecario.class);
			List<Tbf_modello_profilazione_bibliotecario> elenco = criteria.list();
			List<String> output = new ArrayList<String>();
			for (int i = 0; i < elenco.size(); i++)
				output.add(elenco.get(i).getNome());
			return output;
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

	public int getParametroBibliotecaUtente(String idUtente)throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_bibliotecario bibliotecario = (Tbf_bibliotecario)loadNoLazy(session, Tbf_bibliotecario.class, Integer.parseInt(idUtente));
			return bibliotecario.getCd_prof().getCd_polo().getId_parametro().getId_parametro();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

}
