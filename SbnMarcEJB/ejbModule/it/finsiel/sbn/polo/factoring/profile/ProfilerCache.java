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
package it.finsiel.sbn.polo.factoring.profile;

import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.max;
import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.min;
import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.setToMap;

import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_profilo_abilitazione;
import it.finsiel.sbn.polo.orm.amministrazione.Trf_profilo_biblioteca;
import it.finsiel.sbn.util.AmministrazioneFactory;
import it.finsiel.sbn.util.CodiciAttivita;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class ProfilerCache {

	private static Logger log = Logger.getLogger(ProfilerCache.class);
	private static ProfilerCache instance;

	private Map<String, SoftReference<ValidatorContainerUser>> profiles;
	private Map<String, SoftReference<Tbf_biblioteca_in_polo>> biblioteche;
	private CodiciAttivita attivita = null;
	private Tbf_polo polo = null;
	private Lock r;
	private Lock w;

	private static final ProfilerCache getInstance() {
		if (instance == null)
			instance = new ProfilerCache();
		return instance;
	}

	private ProfilerCache() {
		profiles = new HashMap<String, SoftReference<ValidatorContainerUser>>();
		biblioteche = new HashMap<String, SoftReference<Tbf_biblioteca_in_polo>>();

		ReadWriteLock lock = new ReentrantReadWriteLock();
		w = lock.writeLock();
		r = lock.readLock();
	}

	public static final ValidatorContainerUser getUserProfile(String userId, boolean force) {

		ProfilerCache instance = getInstance();
		if (force)
			return instance.loadProfilo(userId);

		ValidatorContainerUser user = null;
		instance.r.lock();
		try {
			SoftReference<ValidatorContainerUser> ref = instance.profiles.get(userId);
			user = (ref != null) ? ref.get() : null;
		} finally {
			instance.r.unlock();
		}

		if (user == null)
			return instance.loadProfilo(userId);

		return user;
	}

	public static final void clear() {
		ProfilerCache instance = getInstance();
		log.debug("richiesta pulizia cache");
		instance.w.lock();
		try {
			instance.profiles.clear();
			instance.biblioteche.clear();
			instance.attivita = null;
			instance.polo = null;
		} finally {
			instance.w.unlock();
		}
	}

	public static final CodiciAttivita getCodiciAttivitaInstance() {
		ProfilerCache instance = getInstance();
		return instance.getCodiciAttivita();
	}

	public static final Tbf_polo getPolo() {
		return getInstance().loadPolo();
	}

	private Tbf_polo loadPolo() {
		if (polo != null)
			return polo;

		Session session = AmministrazioneFactory.getInstance().getCurrentSession();

		Criteria c = session.createCriteria(Tbf_polo.class);
		List<Tbf_polo> poli = c.list();

		polo = poli.get(0);
		return polo;
	}

	private ValidatorContainerUser loadProfilo(String userId) {
		w.lock();
		try {
			log.info("reloadProfilo() per utente: " + userId);
			// Carica profilo utente in albero JNDI
			Session session = AmministrazioneFactory.getInstance().getCurrentSession();
			Query query = session
					.createQuery("select bib from Tbf_bibliotecario bib"
							+ " where bib.fl_canc<>'S'"
							+ " and bib.id_utente_professionale.Tbf_utenti_professionali_web.userid=:uid");
			query.setString("uid", userId);
			Tbf_bibliotecario bibliotecario = (Tbf_bibliotecario) query.uniqueResult();
			ValidatorContainerUser utente = loadBibliotecario(bibliotecario);

			profiles.put(userId, new SoftReference<ValidatorContainerUser>(utente));

			return utente;
		} finally {
			w.unlock();
		}
	}

	private ValidatorContainerUser loadProfiloPolo(String cd_polo) {
		w.lock();
		try {
			log.info("reloadProfilo() per polo: " + cd_polo);
			// Carica profilo utente in albero JNDI
			Session session = AmministrazioneFactory.getInstance().getCurrentSession();
			Query query = session.createQuery("select polo from Tbf_polo polo where polo.cd_polo=:id");
			query.setString("id", cd_polo);
			Tbf_polo polo = (Tbf_polo) query.uniqueResult();

			ValidatorContainerUser profilo_polo = loadPolo(polo);

			profiles.put(cd_polo, new SoftReference<ValidatorContainerUser>(profilo_polo));

			return profilo_polo;
		} finally {
			w.unlock();
		}
	}

	private ValidatorContainerUser loadPolo(Tbf_polo polo) {
		w.lock();
		try {
			//ATTENZIONE: per ora il metodo carica solo i parametri e non le attività
			Tbf_parametro id_parametro = polo.getId_parametro();
			ValidatorContainerUser vcu = mergeParametri(id_parametro, id_parametro, id_parametro);

			return vcu;

		} finally {
			w.unlock();
		}

	}

	private ValidatorContainerUser mergeParametri(Tbf_parametro polo, Tbf_parametro bib, Tbf_parametro user) {
		ValidatorContainerUser vcu = new ValidatorContainerUser();
		List<Tbf_parametro> elenco_parametri = new ArrayList<Tbf_parametro>();
		elenco_parametri.add(bib);
		vcu.setElenco_parametri(elenco_parametri);

		//almaviva5_20140702 segnalazione PAL: i parametri per i nuovi utenti vanno impostati a livello 01
		Set<Tbf_par_auth> parAuthBib = bib.getTbf_par_auth();
		Set<Tbf_par_auth> parAuthUte = user.getTbf_par_auth();
		if (!ValidazioneDati.isFilled(parAuthUte)) {
			parAuthUte = new HashSet<Tbf_par_auth>();
			for (Tbf_par_auth aut :  parAuthBib) {
				Tbf_par_auth aut01 = new Tbf_par_auth(aut);
				aut01.setCd_livello("01");
				parAuthUte.add(aut01);
			}
		}
		vcu.setElenco_parametri_authority(mergeParAuth(polo.getTbf_par_auth(), parAuthBib, parAuthUte));

		//almaviva5_20140702 segnalazione PAL: i parametri per i nuovi utenti vanno impostati a livello 01
		Set<Tbf_par_mat> parMatBib = bib.getTbf_par_mat();
		Set<Tbf_par_mat> parMatUte = user.getTbf_par_mat();
		if (!ValidazioneDati.isFilled(parMatUte)) {
			parMatUte = new HashSet<Tbf_par_mat>();
			for (Tbf_par_mat mat :  parMatBib) {
				Tbf_par_mat mat01 = new Tbf_par_mat(mat);
				mat01.setCd_livello("01");
				parMatUte.add(mat01);
			}
		}
		vcu.setElenco_parametri_materiale(mergeParMat(polo.getTbf_par_mat(), parMatBib, parMatUte));

		vcu.setElenco_parametri_sem(new ArrayList(user.getTbf_par_sem()));
		return vcu;
	}

	private List<Tbf_par_mat> mergeParMat(Set<Tbf_par_mat> polo, Set<Tbf_par_mat> bib, Set<Tbf_par_mat> user) {
		Map<Character, Tbf_par_mat> matPolo = setToMap(polo, Character.class, "cd_par_mat");
		Map<Character, Tbf_par_mat> matBibl = setToMap(bib, Character.class, "cd_par_mat");
		Map<Character, Tbf_par_mat> matUser = setToMap(user, Character.class, "cd_par_mat");

		List<Tbf_par_mat> out = new ArrayList<Tbf_par_mat>();
		for (Tbf_par_mat parUser : matUser.values()) {

			char mat = parUser.getCd_par_mat();
			Tbf_par_mat parPolo = matPolo.get(mat);
			Tbf_par_mat parBibl = matBibl.get(mat);

			Tbf_par_mat merged = new Tbf_par_mat();
			merged.setCd_par_mat(mat);
			/*
				il profilo viene unificato in cascata dando precedenza ai profili di polo e biblioteca.
				Si sfrutta l'ordinamento ASCII dei campi in tabella. Esempio:
					min('S', 'N')='N'
					max('05', '71')='71'
			*/
			merged.setTp_abilitaz(min(parPolo.getTp_abilitaz(), parBibl.getTp_abilitaz(), parUser.getTp_abilitaz()));
			merged.setCd_contr_sim("1");
			merged.setFl_abil_forzat(min(parPolo.getFl_abil_forzat(), parBibl.getFl_abil_forzat(), parUser.getFl_abil_forzat()));
			merged.setCd_livello(min(parPolo.getCd_livello(), parBibl.getCd_livello(), parUser.getCd_livello()));
			merged.setSololocale(max(parPolo.getSololocale(), parBibl.getSololocale(), parUser.getSololocale()));

			out.add(merged);
		}

		return out;
	}

	private List<Tbf_par_auth> mergeParAuth(Set<Tbf_par_auth> polo, Set<Tbf_par_auth> bib, Set<Tbf_par_auth> user) {
		Map<String, Tbf_par_auth> authPolo = setToMap(polo, String.class, "cd_par_auth");
		Map<String, Tbf_par_auth> authBibl = setToMap(bib, String.class, "cd_par_auth");
		Map<String, Tbf_par_auth> authUser = setToMap(user, String.class, "cd_par_auth");

		List<Tbf_par_auth> out = new ArrayList<Tbf_par_auth>();
		for (Tbf_par_auth parUser : authUser.values()) {

			String auth = parUser.getCd_par_auth();
			Tbf_par_auth parPolo = authPolo.get(auth);
			Tbf_par_auth parBibl = authBibl.get(auth);

			Tbf_par_auth merged = new Tbf_par_auth();
			merged.setCd_par_auth(auth);
			/*
				il profilo viene unificato in cascata dando precedenza ai profili di polo e biblioteca.
				Si sfrutta l'ordinamento ASCII dei campi in tabella. Esempio:
					min('S', 'N')='N'
					max('05', '71')='71'
			*/
			merged.setTp_abil_auth(min(parPolo.getTp_abil_auth(), parBibl.getTp_abil_auth(), parUser.getTp_abil_auth()));
			merged.setFl_abil_legame(min(parPolo.getFl_abil_legame(), parBibl.getFl_abil_legame(), parUser.getFl_abil_legame()));
			merged.setFl_leg_auth(min(parPolo.getFl_leg_auth(), parBibl.getFl_leg_auth(), parUser.getFl_leg_auth()));
			merged.setCd_livello(min(parPolo.getCd_livello(), parBibl.getCd_livello(), parUser.getCd_livello()));

			merged.setCd_contr_sim("1");
			merged.setFl_abil_forzat(min(parPolo.getFl_abil_forzat(), parBibl.getFl_abil_forzat(), parUser.getFl_abil_forzat()));
			merged.setSololocale(max(parPolo.getSololocale(), parBibl.getSololocale(), parUser.getSololocale()));

			out.add(merged);
		}

		return out;
	}

	private Map<String, SoftReference<Tbf_biblioteca_in_polo>> loadBiblioteche() {
		w.lock();
		try {
			Session session = AmministrazioneFactory.getInstance().getCurrentSession();
			Criteria c = session.createCriteria(Tbf_biblioteca_in_polo.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			List<Tbf_biblioteca_in_polo> bibliotechePolo = c.list();

			for (Tbf_biblioteca_in_polo bib : bibliotechePolo) {
				String key = bib.getCod_polo() + bib.getCd_biblioteca();
				biblioteche.put(key, new SoftReference<Tbf_biblioteca_in_polo>(bib));
			}

			return biblioteche;
		} finally {
			w.unlock();
		}
	}

	private ValidatorContainerUser loadBibliotecario(Tbf_bibliotecario user) {

		w.lock();
		try {
			Tbf_profilo_abilitazione profilo = user.getCd_prof();

			//almaviva5_20140218 evolutive google3
			ValidatorContainerUser vcu = mergeParametri(getPolo().getId_parametro(), profilo.getCd_polo().getId_parametro(), user.getId_parametro() );

			//attività
			Set<Trf_profilo_biblioteca> list = profilo.getTrf_profilo_biblioteca();
			Iterator<Trf_profilo_biblioteca> i = list.iterator();
			List<Tbf_attivita> elenco_attivita = new ArrayList<Tbf_attivita>();
			while (i.hasNext()) {
				Trf_profilo_biblioteca bibpr = i.next();
				elenco_attivita.add(bibpr.getCd_attivita());
			}
			vcu.setElenco_attivita(elenco_attivita);

			vcu.setTbf_bibliotecario(user);

			return vcu;

		} finally {
			w.unlock();
		}
	}

	private CodiciAttivita getCodiciAttivita() {

		r.lock();
		try {
			if (attivita != null)
				return attivita;
		} finally {
			r.unlock();
		}

		w.lock();
		try {
			Class<?> cls = null;
			Object codici = null;

			Session session = AmministrazioneFactory.getInstance().getCurrentSession();
			Criteria c = session.createCriteria(Tbf_attivita.class);
			List<Tbf_attivita> listaAttivita = c.list();

			for (Tbf_attivita attivita : listaAttivita) {

				if (attivita.getClasse_java_sbnmarc() != null) {
					if (cls == null) {
						cls = Class.forName(attivita.getClasse_java_sbnmarc().substring(0,
								attivita.getClasse_java_sbnmarc().lastIndexOf('.')));
						codici = cls.newInstance();
					}
					try {
						Field fld = cls.getField(attivita.getClasse_java_sbnmarc()
								.substring(attivita.getClasse_java_sbnmarc().lastIndexOf('.') + 1));
						fld.set(codici, attivita.getCd_attivita().trim());
					} catch (NoSuchFieldException e) {
						continue;
					}
				}

			}
			attivita = (CodiciAttivita) codici;
			return attivita;

		} catch (Exception e) {
			log.error("", e);
			return null;
		} finally {
			w.unlock();
		}

	}

	public static final Tbf_biblioteca_in_polo getBiblioteca(String cod_polo, String cod_biblio) {

		ProfilerCache instance = ProfilerCache.getInstance();
		String key = cod_polo + cod_biblio;

		instance.r.lock();
		Tbf_biblioteca_in_polo bib = null;
		try {
			SoftReference<Tbf_biblioteca_in_polo> ref = instance.biblioteche.get(key);
			bib = (ref != null) ? ref.get() : null;
		} finally {
			instance.r.unlock();
		}

		if (bib == null)
			instance.biblioteche = instance.loadBiblioteche();

		return instance.biblioteche.get(key).get();
	}

	public static final ValidatorContainerUser getPoloProfile(String cd_polo, boolean force) {

		ProfilerCache instance = getInstance();
		if (force)
			return instance.loadProfiloPolo(cd_polo);

		ValidatorContainerUser polo = null;
		instance.r.lock();
		try {
			SoftReference<ValidatorContainerUser> ref = instance.profiles.get(cd_polo);
			polo = (ref != null) ? ref.get() : null;
		} finally {
			instance.r.unlock();
		}

		if (polo == null)
			return instance.loadProfiloPolo(cd_polo);

		return polo;
	}

}
