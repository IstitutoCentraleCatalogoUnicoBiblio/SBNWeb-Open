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
package it.iccu.sbn.util.profiler;

import gnu.trove.THashMap;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbnEJB;
import it.iccu.sbn.SbnMarcFactory.factory.SbnProfileDao;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.model.unimarcmodel.AttivitaAbilitateType;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriAuthorityType;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriDocumenti;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriDocumentiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SottoAttivita;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.exception.UtenteNotProfiledException;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_bibliotecaDao;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_utenti_professionali_webDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;
import it.iccu.sbn.polo.orm.amministrazione.Trf_attivita_affiliate;
import it.iccu.sbn.polo.orm.amministrazione.Trf_utente_professionale_biblioteca;
import it.iccu.sbn.servizi.ProfilerManager;
import it.iccu.sbn.servizi.ProfilerManagerMBean;
import it.iccu.sbn.vo.custom.amministrazione.Attivita;
import it.iccu.sbn.vo.custom.amministrazione.AttivitaAffiliata;
import it.iccu.sbn.vo.custom.amministrazione.Default;
import it.iccu.sbn.vo.custom.amministrazione.Default.ProvenienzaDefault;
import it.iccu.sbn.vo.custom.amministrazione.ParametriAuthorityVO;
import it.iccu.sbn.vo.custom.amministrazione.ParametriDocumentiVO;
import it.iccu.sbn.vo.custom.amministrazione.UserProfile;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;



public final class SbnWebProfileCache implements Runnable {

	private static class ProfileReference extends SoftReference<UserProfile> {

		private final String userName;

		public ProfileReference(String userName, UserProfile referent, ReferenceQueue<UserProfile> queue) {
			super(referent, queue);
			this.userName = ValidazioneDati.trimOrEmpty(userName);

		}

		public String getUserName() {
			return userName;
		}

	}

	private static Logger log = Logger.getLogger(SbnWebProfileCache.class);

	private static final SbnWebProfileCache instance = new SbnWebProfileCache();

	private Map<String, ProfileReference> profiles = new THashMap<String, ProfileReference>();
	private ReferenceQueue<UserProfile> garbaged = new ReferenceQueue<UserProfile>();

	private AmministrazioneBiblioteca amministrazioneBiblioteca;

	private Map<String, List<AttivitaAffiliata>> attivitaAffiliate;

	private final Lock r;
	private final Lock w;


	public static final SbnWebProfileCache getInstance() {
		return instance;
	}


	private AmministrazioneBiblioteca getAmministrazioneBiblioteca() throws Exception {

		if (amministrazioneBiblioteca != null)
			return amministrazioneBiblioteca;

		this.amministrazioneBiblioteca = DomainEJBFactory.getInstance().getBiblioteca();

		return amministrazioneBiblioteca;

	}


	private SbnWebProfileCache() {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		r = lock.readLock();
		w = lock.writeLock();

		Thread t = new Thread(this);
		t.setName("SbnWebProfileCache-gc-daemon");
		t.setPriority(Thread.MIN_PRIORITY);
		t.setDaemon(true);
		t.start();
	}


	public final UserProfile getProfile(String ticket) throws UtenteNotFoundException, UtenteNotProfiledException {
		UserProfile u = new UserProfile();
		u.setPolo(DaoManager.codPoloFromTicket(ticket));
		u.setBiblioteca(DaoManager.codBibFromTicket(ticket));
		u.setUserName(DaoManager.userFromTicket(ticket));

		UserProfile utente = getProfile(u);
		return utente;
	}


	public final UserProfile getProfile(UserProfile ute) throws UtenteNotFoundException, UtenteNotProfiledException {

		try {
			String userName = ute.getUserName();
			UserProfile up = getReference(userName);
			if (up != null)
				return up;

			try {
				w.lock();

				up = getReference(userName);	//read lock!!
				if (up != null)
					return up;

				//non trovato
				log.warn("getProfile: Profilo per utente '" + userName + "' non trovato nella cache. Invocato reload.");
				SbnUserType user = new SbnUserType();
				user.setUserId(userName);
				user.setBiblioteca(ute.getPolo() + ute.getBiblioteca());
				FactorySbn polo = new FactorySbnEJB("POLO");
				SbnProfileDao profiler = new SbnProfileDao(polo, user, user);
				SbnProfileType poloProfile = profiler.profile();
				ute.setProfile(poloProfile);
				ute.setAttivita(this.getAttivita(poloProfile));
				ute.setParametriDocumenti(this.getParametriDocumenti(poloProfile));
				ute.setParametriAuthority(this.getParametriAuthority(poloProfile));
				//default
				Tbf_utenti_professionali_webDao dao = new Tbf_utenti_professionali_webDao();
				Tbf_utenti_professionali_web uteWeb = dao.select(userName);
				Tbf_anagrafe_utenti_professionali aup = uteWeb.getId_utente_professionale();
				ute.setIdUtenteProfessionale(aup.getId_utente_professionale());
				Set<?> utentiProf = aup.getTrf_utente_professionale_biblioteca();
				if (utentiProf == null) //non trovato
					throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_FOUND);

				Trf_utente_professionale_biblioteca bibUteProf = (Trf_utente_professionale_biblioteca) ValidazioneDati.first(utentiProf);
				Tbf_biblioteca_in_polo bib = bibUteProf.getCd_polo();
				ute.setPolo(bib.getCd_polo().getCd_polo());
				ute.setBiblioteca(bib.getCd_biblioteca());
				ute.setDescrizioneBiblioteca(bib.getDs_biblioteca());
				ute.setDefault(loadDefault(aup.getTbf_bibliotecario()));
				String[] attivitaBib = getAmministrazioneBiblioteca().getElencoAttivitaProfilo(ute.getBiblioteca());
				if (ValidazioneDati.isFilled(attivitaBib))
					ute.setAttivitaBib(Arrays.asList(attivitaBib));
				//almaviva5_20080717 carico profilo indice
				SbnProfileType indiceProfile = lookupProfiloIndice();
				if (indiceProfile != null) {
					ute.setAttivitaIndice(this.getAttivita(indiceProfile));
					ute.setParametriDocumentiIndice(this.getParametriDocumenti(indiceProfile));
					ute.setParametriAuthorityIndice(this.getParametriAuthority(indiceProfile));
				} else {
					log.warn("Attenzione: Impossibile leggere il profilo di Indice");
					//almaviva5_20121025 #5160
					ute.setAttivitaIndice(Collections.<String, Attivita>emptyMap());
					ute.setParametriDocumentiIndice(Collections.<String, ParametriDocumentiVO>emptyMap());
					ute.setParametriAuthorityIndice(Collections.<String, ParametriAuthorityVO>emptyMap());

				}
				//almaviva5_20111110 attivita affiliate
				ute.setAttivitaAffiliate(this.getAttivitaAffiliate());

				profiles.put(userName, new ProfileReference(userName, ute, garbaged));
				log.debug("getProfile: Profilo per utente '" + userName + "' caricato nella cache.");

			} finally {
				w.unlock();
			}

		} catch (SbnMarcException e) {
			log.error("", e);
			throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_PROFILED);

		} catch (UtenteNotProfiledException e) {
			log.error("", e);
			throw new UtenteNotFoundException(SbnErrorTypes.AMM_USER_NOT_PROFILED);

		} catch (Exception e) {
			log.error("", e);
			throw new UtenteNotFoundException(SbnErrorTypes.AMM_GENERIC, e.getMessage());
		}

		return ute;

	}

	private UserProfile getReference(String userName) {
		try {
			r.lock();
			ProfileReference ref = profiles.get(userName);
			if (ref != null) {
				UserProfile stored = ref.get();
				if (stored != null) {
					log.debug("getProfile: Profilo per utente '" + userName
							+ "' trovato nella cache.");
					return stored.copyThis();
				}
			}
			return null;
		} finally {
			r.unlock();
		}
	}


	private Map<String, Default> loadDefault(Tbf_bibliotecario bibliotecario) {

		Map<String, Default> output = new THashMap<String, Default>();

		//leggo prima i default di biblioteca
		Iterator<?> i = bibliotecario.getCd_prof().getCd_polo().getTbf_biblioteca_default().iterator();
		while (i.hasNext()) {
			Tbf_biblioteca_default bib_def = (Tbf_biblioteca_default) i.next();
			Default def = new Default();
			def.setKey(bib_def.getId_default().getKey());
			def.setTipo(bib_def.getId_default().getTipo());
			def.setValue(bib_def.getValue());
			def.setProvenienza(ProvenienzaDefault.BIBLIOTECA);
			output.put(bib_def.getId_default().getKey(), def);
		}

		//leggo i default di utente che sovrascrivono quelli duplicati per la biblioteca
		i = bibliotecario.getTbf_bibliotecario_default().iterator();

		while (i.hasNext()) {
			Tbf_bibliotecario_default bib_def = (Tbf_bibliotecario_default) i.next();
			Default def = new Default();
			def.setKey(bib_def.getId_default().getKey());
			def.setTipo(bib_def.getId_default().getTipo());
			def.setValue(bib_def.getValue());
			def.setProvenienza(ProvenienzaDefault.UTENTE);
			output.put(bib_def.getId_default().getKey(), def);
		}

		for (Entry<String, Default> entry : output.entrySet())
			log.debug("loadDefault: " + entry.getValue());

		return output;
	}

	private Map<String, Attivita> getAttivita(SbnProfileType poloProfile) throws Exception {

		Map<String, Attivita> ret = new THashMap<String, Attivita>();
		AttivitaAbilitateType[] listaAttivita = poloProfile.getAttivitaAbilitate();

		for (AttivitaAbilitateType attivita : listaAttivita) {

			String codAttivita = attivita.getAttivita().getCodAttivita();
			String descrAttivita = attivita.getAttivita().getContent().trim();
			if (ValidazioneDati.strIsNull(codAttivita))	// in indice non c'è questo dato!!
				codAttivita = descrAttivita.toUpperCase().replace(' ', '_');

			Attivita attBib = new Attivita(codAttivita.trim(), descrAttivita);
			ret.put(codAttivita, attBib);

			SottoAttivita[] listaSottoAttivita = attivita.getSottoAttivita();
			// SOTTOATTIVITA
			for (SottoAttivita sottoAttivita : listaSottoAttivita) {

				String codSottoAttivita = sottoAttivita.getCodAttivita();
				String descrSottoAttivita = sottoAttivita.getContent().trim();

				if (ValidazioneDati.strIsNull(codSottoAttivita))	// in indice non c'è questo dato!!
					codSottoAttivita = descrSottoAttivita.toUpperCase().replace(' ', '_');

				Attivita sottoAttBib = new Attivita(codSottoAttivita.trim(), descrSottoAttivita);
				ret.put(codSottoAttivita, sottoAttBib);

			}
		}
		return ret;
	}

	private Map<String, ParametriDocumentiVO> getParametriDocumenti(SbnProfileType poloProfile) throws Exception
	{
		Map<String, ParametriDocumentiVO> ret = new THashMap<String, ParametriDocumentiVO>();
		ParametriDocumentiType[] listaParametriDocumenti = poloProfile.getParametriDocumenti();

		 for (int i = 0; i < listaParametriDocumenti.length; i++) {
			 ParametriDocumenti parametri = (ParametriDocumenti) listaParametriDocumenti[i];

			 ParametriDocumentiVO parametriDocumenti = new ParametriDocumentiVO( parametri.getTipoMateriale().toString().trim(),
																			 Integer.parseInt(parametri.getLivelloAut().toString().trim()),
																			 parametri.getAbilitaOggetto().toString().trim(),
																			 parametri.getAbilitatoForzatura().toString().trim(),
																			 parametri.getSololocale());
             ret.put(parametriDocumenti.getTipoMateriale(), parametriDocumenti);
         }
		 return ret;
	}

	private Map<String, ParametriAuthorityVO> getParametriAuthority(SbnProfileType poloProfile) throws Exception
	{
		Map<String, ParametriAuthorityVO> ret = new THashMap<String, ParametriAuthorityVO>();
		ParametriAuthorityType[] listaParametriAuthority = poloProfile.getParametriAuthority();

		 for (int i = 0; i < listaParametriAuthority.length; i++) {
			 ParametriAuthority parametri = (ParametriAuthority) listaParametriAuthority[i];

			 ParametriAuthorityVO parametriAuthority = new ParametriAuthorityVO( parametri.getTipoAuthority().toString().trim(),
					 														 parametri.getAbilitaAuthority().toString().trim(),
					 														 parametri.getAbilitaLegamiDoc().toString().trim(),
					 														 parametri.getReticoloLegamiDoc().toString().trim(),
																			 Integer.parseInt(parametri.getLivelloAut().toString().trim()),
																			 parametri.getAbilitatoForzatura().toString().trim(),
																			 parametri.getSololocale() );
             ret.put(parametriAuthority.getTipoAuthority(), parametriAuthority);
         }
		 return ret;
	}


	private SbnProfileType lookupProfiloIndice() {
		try {
			ProfilerManagerMBean pm = ProfilerManager.getProfilerManagerInstance();
			return pm.getProfiloIndice(false);

		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}


	public Map<String, Default> reloadDefault(int idUtenteProfessionale) throws UtenteNotFoundException, UtenteNotProfiledException, DaoManagerException {

		Tbf_utenti_professionali_webDao dao = new Tbf_utenti_professionali_webDao();
		Tbf_anagrafe_utenti_professionali anagProf = dao.getUtenteProfessionale(idUtenteProfessionale);
		Tbf_bibliotecario bib = anagProf.getTbf_bibliotecario();

		Map<String, Default> _default = loadDefault(bib);

		String userid = ValidazioneDati.trimOrEmpty(anagProf.getTbf_utenti_professionali_web().getUserid());
		UserProfile utente = getReference(userid);
		try {
			w.lock();
			if (utente == null) {
				utente = new UserProfile();
				@SuppressWarnings("unchecked")
				Trf_utente_professionale_biblioteca biblioteca = (Trf_utente_professionale_biblioteca) ValidazioneDati.first(anagProf.getTrf_utente_professionale_biblioteca());
				utente.setPolo(biblioteca.getCd_polo().getCd_polo().getCd_polo());
				utente.setBiblioteca(biblioteca.getCd_polo().getCd_biblioteca());
				//almaviva5_20120125 fix descr. bib a null??
				utente.setDescrizioneBiblioteca(biblioteca.getCd_polo().getDs_biblioteca());
				utente.setUserName(userid);
				utente = getProfile(utente);
			}

			utente.setDefault(_default);
			profiles.put(userid, new ProfileReference(userid, utente, garbaged));
		} finally {
			w.unlock();
		}
		return _default;
	}

	private Map<String, List<AttivitaAffiliata>> getAttivitaAffiliate() throws DaoManagerException {
		if (attivitaAffiliate != null)
			return attivitaAffiliate;

		attivitaAffiliate = new ConcurrentHashMap<String, List<AttivitaAffiliata>>();

		Tbf_bibliotecaDao dao = new Tbf_bibliotecaDao();
		List<Trf_attivita_affiliate> attivita = dao.getAttivitaAffiliate();
		if (!ValidazioneDati.isFilled(attivita))
			return attivitaAffiliate;

		for (Trf_attivita_affiliate aa : attivita) {
			String cd_attivita = aa.getCd_attivita().getCd_attivita();
			List<AttivitaAffiliata> list = attivitaAffiliate.containsKey(cd_attivita) ?
					attivitaAffiliate.get(cd_attivita) :
					new ArrayList<AttivitaAffiliata>();

			list.add(new AttivitaAffiliata(cd_attivita,
					aa.getCd_bib_centro_sistema().getCd_biblioteca(),
					aa.getCd_bib_affiliata().getCd_biblioteca()));

			attivitaAffiliate.put(cd_attivita, list);
		}

		return attivitaAffiliate;
	}


	public void clear() {
		try {
			w.lock();

			log.debug("richiesta pulizia cache");
			attivitaAffiliate = null;
			profiles.clear();

		} finally {
			w.unlock();
		}
	}


	public void clear(List<String> users) {
		try {
			w.lock();
			for (String u : users) {
				String userName = ValidazioneDati.trimOrEmpty(u);
				profiles.remove(userName);
				log.warn("Profilo per utente '" + userName
						+ "' rimosso dalla cache.");
			}
		} finally {
			w.unlock();
		}
	}


	public void run() {
		ProfileReference ref;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				while ( (ref = (ProfileReference) garbaged.remove(1000)) != null) {
					try {
						String userName = ref.getUserName();
						w.lock();
						ProfileReference old = profiles.get(userName);
						if (old != null && old == ref) {
							log.warn("Profilo per utente '" + userName + "' rimosso dalla cache.");
							profiles.remove(userName);
						}
					} finally {
						w.unlock();
					}
				}

			} catch (InterruptedException e) {
				return;
			}
		}
	}

}
