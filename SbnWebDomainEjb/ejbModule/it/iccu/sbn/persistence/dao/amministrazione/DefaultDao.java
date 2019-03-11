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
import it.iccu.sbn.ejb.vo.amministrazionesistema.gestionedefault.DefaultVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_anagrafe_utenti_professionali;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_bibliotecario_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_config_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_gruppi_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_polo_default;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_utenti_professionali_web;
import it.iccu.sbn.util.ConvertiVo.ServiziConversioneVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class DefaultDao extends DaoManager {

	public DefaultDao() {
		super();
	}

	public List<Tbf_bibliotecario_default> cercaDefaultBibliotecarioPerArea(int idUtente, String idArea) throws DaoManagerException{
		try {
			Session session = this.getCurrentSession();
			Tbf_utenti_professionali_web utente =
				(Tbf_utenti_professionali_web) loadNoLazy(session, Tbf_utenti_professionali_web.class, new Integer(idUtente));
			Criteria criteria = session.createCriteria(Tbf_bibliotecario_default.class);
			criteria.add(Restrictions.eq("id_utente_professionale", utente));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_bibliotecario_default> cercaDefaultBibliotecario(int idUtente, int idDefault) throws DaoManagerException{
		try {
			Session session = this.getCurrentSession();
			Tbf_utenti_professionali_web utente = (Tbf_utenti_professionali_web) loadNoLazy(session, Tbf_utenti_professionali_web.class, new Integer(idUtente));
			Tbf_default def = (Tbf_default) loadNoLazy(session, Tbf_default.class, new Integer(idDefault));
			Criteria criteria = session.createCriteria(Tbf_bibliotecario_default.class);

			criteria.add(Restrictions.eq("id_utente_professionale", utente));
			criteria.add(Restrictions.eq("id_default", def));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_biblioteca_default> cercaDefaultBiblioteca(String idBiblioteca, String idPolo, int idDefault) throws DaoManagerException{
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_polo polo = (Tbf_polo)loadNoLazy(session, Tbf_polo.class, idPolo);
			criteria.add(Restrictions.eq("cd_polo", polo));
			Tbf_biblioteca_in_polo bibInPolo = (Tbf_biblioteca_in_polo)criteria.uniqueResult();

			Tbf_default def = (Tbf_default) loadNoLazy(session, Tbf_default.class, new Integer(idDefault));
			criteria = session.createCriteria(Tbf_biblioteca_default.class);

			criteria.add(Restrictions.eq("cd_biblioteca", bibInPolo));
			criteria.add(Restrictions.eq("id_default", def));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_default> cercaTuttiDefaultArea (String idArea) throws DaoManagerException{

		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_default.class);
			criteria.createCriteria("tbf_config_default__id_config")
					.add(Restrictions.eq("parent", idArea));
			criteria.addOrder(Order.asc("seq_ordinamento"));
			List<Tbf_default> lista = criteria.list();

//			for (int i=0; i<lista.size(); i++) {
//				if ((lista.get(i).getTbf_config_default__id_config() != null) && (!lista.get(i).getTbf_config_default__id_config().getParent().trim().equals(idArea))) {
//					lista.remove(i);
//					i--;
//				}
//			}
			return lista;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_default> cercaTuttiDefaultArea(String idArea, Set<String> attivita) throws DaoManagerException{

		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_default.class);

			if (ValidazioneDati.isFilled(attivita))
				criteria.add(Restrictions.in("codice_attivita", attivita));

			criteria.createCriteria("tbf_config_default__id_config")
					.add(Restrictions.eq("parent", idArea));
			criteria.addOrder(Order.asc("seq_ordinamento"));
			List<Tbf_default> lista = criteria.list();

			return lista;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_default> cercaTuttiDefaultAttivita (Set<String> attivita) throws DaoManagerException{

		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_default.class);
			criteria.add(Restrictions.in("codice_attivita", attivita));
			criteria.addOrder(Order.asc("seq_ordinamento"));
			List<Tbf_default> lista = criteria.list();

//			for (int i=0; i<lista.size(); i++) {
//				if ((lista.get(i).getCodice_attivita() != null) && (!attivita.containsKey(lista.get(i).getCodice_attivita().trim()))) {
//					lista.remove(i);
//					i--;
//				}
//			}
			return lista;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_bibliotecario_default> cercaTuttiDefaultBibliotecario() throws DaoManagerException{
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_bibliotecario_default.class);
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_biblioteca_default> cercaTuttiDefaultBiblioteca(String codBib, String codPolo) throws DaoManagerException{
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_default.class);
			Tbf_biblioteca_in_polo bib = DaoManager.creaIdBib(codPolo, codBib);
			criteria.add(Restrictions.eq("cd_biblioteca", bib));

			List<Tbf_biblioteca_default> output = criteria.list();
//			for (int i=0; i<output.size(); i++) {
//				if ((!output.get(i).getCd_biblioteca().getCd_biblioteca().equals(idBiblioteca)) || (!output.get(i).getCd_biblioteca().getCd_polo().getCd_polo().equals(idPolo))) {
//					output.remove(i);
//					i--;
//				}
//			}
			return output;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_polo_default> cercaTuttiDefaultPolo(String idPolo) throws DaoManagerException{
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_polo_default.class);
			List<Tbf_polo_default> output = criteria.list();
			for (int i=0; i<output.size(); i++) {
				if (!output.get(i).getCd_polo().getCd_polo().equals(idPolo)) {
					output.remove(i);
					i--;
				}
			}
			return output;
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}

	}

	public List<Tbf_default> cercaDefaultPerGruppo(int idGruppo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_gruppi_default gruppi = (Tbf_gruppi_default) loadNoLazy(session, Tbf_gruppi_default.class, new Integer(idGruppo));
			Criteria criteria = session.createCriteria(Tbf_default.class);
			criteria.add(Restrictions.eq("tbf_gruppi_default", gruppi));
			return criteria.list();
		}
		catch (HibernateException he) {
			throw new DaoManagerException(he);
		}
	}

	public boolean inserisciDefaultUtente(List<DefaultVO> elencoDef, int idUtente) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Tbf_anagrafe_utenti_professionali utenteWeb =
				(Tbf_anagrafe_utenti_professionali) loadNoLazy(session, Tbf_anagrafe_utenti_professionali.class, new Integer(idUtente));
			Tbf_bibliotecario_default record = new Tbf_bibliotecario_default();

			for (DefaultVO def : elencoDef) {

				//almaviva5_20140528 #4781
				String value = def.getSelezione();
				if (ValidazioneDati.length(value) > 0 ) {
					Tbf_default defDB = (Tbf_default) loadNoLazy(session, Tbf_default.class, new Integer(def.getIdDefault()));
					record = ServiziConversioneVO.daWebAHibernateDefaultUtente(value, utenteWeb.getTbf_bibliotecario(), defDB);
					session.saveOrUpdate(record);
				}
				// almaviva5_20090320 #2716 corretto controllo su null
				else if (ValidazioneDati.strIsNull(value) ) {
					List<Tbf_bibliotecario_default> bibDef = cercaDefaultBibliotecario(idUtente, def.getIdDefault());
					if (bibDef.size() > 0) {
						deleteAndEvict(bibDef.get(0));
					}
				}
			}
			return pulisciDefaultBibliotecario();
		}
		catch (HibernateException he) {
			he.printStackTrace();
			return false;
		}
	}

	public boolean inserisciDefaultBiblioteca(List<DefaultVO> elencoDef, String idBiblioteca, String idPolo) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_biblioteca_in_polo.class);
			criteria.add(Restrictions.eq("cd_biblioteca", idBiblioteca));
			Tbf_polo polo = (Tbf_polo)loadNoLazy(session, Tbf_polo.class, idPolo);
			criteria.add(Restrictions.eq("cd_polo", polo));
			Tbf_biblioteca_in_polo bibInPolo = (Tbf_biblioteca_in_polo)criteria.uniqueResult();

			for (DefaultVO def : elencoDef) {
				//almaviva5_20140528 #4781
				String value = def.getSelezione();
				if (ValidazioneDati.length(value) > 0) {
					Tbf_default defDB = (Tbf_default) loadNoLazy(session, Tbf_default.class, new Integer(def.getIdDefault()));

					Tbf_biblioteca_default record = ServiziConversioneVO.daWebAHibernateDefaultBiblioteca(value, bibInPolo , defDB);
					session.saveOrUpdate(record);
				}
				else if (ValidazioneDati.strIsNull(value) ) {
					List<Tbf_biblioteca_default> bibDef = cercaDefaultBiblioteca(idBiblioteca, idPolo, def.getIdDefault());
					if (bibDef.size() > 0) {
						deleteAndEvict(bibDef.get(0));
					}
				}
			}

			return pulisciDefaultBiblioteca(idBiblioteca, idPolo);
		}
		catch (HibernateException he) {
			he.printStackTrace();
			return false;
		}
	}

	// Il metodo elimina dalla tabella Tbf_biblioteca_default tutti i record, appartenenti a campi di tipo RADIO o CHECK,
	// che hanno tutti i valori FALSE.
	private boolean pulisciDefaultBiblioteca(String idBiblioteca, String idPolo) throws DaoManagerException {

		List<Tbf_biblioteca_default> elencoDelete = new ArrayList<Tbf_biblioteca_default>();
		try {
			List<Tbf_biblioteca_default> elenco = cercaTuttiDefaultBiblioteca(idBiblioteca, idPolo);

			for (int i = 0; i<elenco.size(); i++) {
				Tbf_default def = elenco.get(i).getId_default();
				if (def.getId_etichetta().equals("default.superiore") && def.getTipo().toUpperCase().equals("CHECK") && elenco.get(i).getValue().equals("TRUE")) {
					int idGruppo = def.getTbf_gruppi_default().getId();
					List<Tbf_biblioteca_default> valori = new ArrayList<Tbf_biblioteca_default>();
					for (int j= 0; j<elenco.size(); j++) {
						Tbf_default defParagone = elenco.get(j).getId_default();
						if (defParagone.getTbf_gruppi_default() != null && defParagone.getTbf_gruppi_default().getId() == idGruppo) {
							valori.add(elenco.get(j));
						}
					}
					for (int r= 0; r<valori.size(); r++) {
						elencoDelete.add(valori.get(r));
						for (int t = 0 ; t<elenco.size(); t++) {
							if (elenco.get(t).equals(valori.get(r)))
								elenco.remove(t);
						}
					}
					i = 0;
				}

				else if (def.getTipo().toUpperCase().equals("RADIO")) {
					if (def.getTbf_gruppi_default() != null) {
						int idGruppo = def.getTbf_gruppi_default().getId();
						List<Tbf_biblioteca_default> valori = new ArrayList<Tbf_biblioteca_default>();
						boolean selezionato = false;
						for (int j= 0; j<elenco.size(); j++) {
							Tbf_default defParagone = elenco.get(j).getId_default();
							if (defParagone.getTbf_gruppi_default() != null && defParagone.getTbf_gruppi_default().getId() == idGruppo) {
								if (!elenco.get(j).getValue().trim().toUpperCase().equals("FALSE")) {
									selezionato = true;
									break;
								}
								else valori.add(elenco.get(j));
							}
						}
						if (!selezionato) {
							for (int r= 0; r<valori.size(); r++) {
								elencoDelete.add(valori.get(r));
								for (int t = 0 ; t<elenco.size(); t++) {
									if (elenco.get(t).equals(valori.get(r)))
										elenco.remove(t);
								}
							}
							i = 0;
						}
					}
					else {
						elencoDelete.add(elenco.get(i));
						elenco.remove(i);
						i = 0;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			//Eliminazione fisica dal database dei record:
			for (int m = 0; m<elencoDelete.size(); m++)
				deleteAndEvict(elencoDelete.get(m));

			return true;
		}
		catch (HibernateException he) {
			he.printStackTrace();
			return false;
		}
	}

	// Il metodo elimina dalla tabella Tbf_bibliotecario_default tutti i record, appartenenti a campi di tipo RADIO o CHECK,
	// che hanno tutti i valori FALSE.
	private boolean pulisciDefaultBibliotecario() throws DaoManagerException {

		List<Tbf_bibliotecario_default> elencoDelete = new ArrayList<Tbf_bibliotecario_default>();
		try {
			List<Tbf_bibliotecario_default> elenco = cercaTuttiDefaultBibliotecario();

			for (int i = 0; i<elenco.size(); i++) {
				Tbf_default def = elenco.get(i).getId_default();
				if (def.getId_etichetta().equals("default.superiore") && def.getTipo().toUpperCase().equals("CHECK") && elenco.get(i).getValue().equals("TRUE")) {
					int idGruppo = def.getTbf_gruppi_default().getId();
					List<Tbf_bibliotecario_default> valori = new ArrayList<Tbf_bibliotecario_default>();
					for (int j= 0; j<elenco.size(); j++) {
						Tbf_default defParagone = elenco.get(j).getId_default();
						if (defParagone.getTbf_gruppi_default() != null && defParagone.getTbf_gruppi_default().getId() == idGruppo) {
							valori.add(elenco.get(j));
						}
					}
					for (int r= 0; r<valori.size(); r++) {
						elencoDelete.add(valori.get(r));
						for (int t = 0 ; t<elenco.size(); t++) {
							if (elenco.get(t).equals(valori.get(r)))
								elenco.remove(t);
						}
					}
					i = 0;
				}

				else if (def.getTipo().toUpperCase().equals("RADIO")) {
					if (def.getTbf_gruppi_default() != null) {
						int idGruppo = def.getTbf_gruppi_default().getId();
						List<Tbf_bibliotecario_default> valori = new ArrayList<Tbf_bibliotecario_default>();
						boolean selezionato = false;
						for (int j= 0; j<elenco.size(); j++) {
							Tbf_default defParagone = elenco.get(j).getId_default();
							if (defParagone.getTbf_gruppi_default() != null && defParagone.getTbf_gruppi_default().getId() == idGruppo) {
								if (!elenco.get(j).getValue().trim().toUpperCase().equals("FALSE")) {
									selezionato = true;
									break;
								}
								else valori.add(elenco.get(j));
							}
						}
						if (!selezionato) {
							for (int r= 0; r<valori.size(); r++) {
								elencoDelete.add(valori.get(r));
								for (int t = 0 ; t<elenco.size(); t++) {
									if (elenco.get(t).equals(valori.get(r)))
										elenco.remove(t);
								}
							}
							i = 0;
						}
					}
					else {
						elencoDelete.add(elenco.get(i));
						elenco.remove(i);
						i = 0;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			//Eliminazione fisica dal database dei record:
			for (int m = 0; m<elencoDelete.size(); m++)
				deleteAndEvict(elencoDelete.get(m));

			return true;
		}
		catch (HibernateException he) {
			he.printStackTrace();
			return false;
		}
	}

	// Il metodo restituisce l'elenco delle Aree per l'utente nel carico sia riempita la mappa attivita con i permessi utente,
	// altrimenti restituisce tutte le aree.
	public List<Tbf_config_default> cercaAreeUtente(Map attivita) throws DaoManagerException {
		try {
			Session session = this.getCurrentSession();
			Criteria criteria = session.createCriteria(Tbf_config_default.class);
			criteria.add(Restrictions.isNull("parent"));
			List<Tbf_config_default> listaAree = criteria.list();
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

	public void inserisciDefaultBiblioteca(String codPolo, String codBib,
			ConstantDefault def, String value) throws DaoManagerException {
		try {
			//almaviva5_20140701 #3198
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_biblioteca_default.class);
			Tbf_biblioteca_in_polo bib = creaIdBib(codPolo, codBib);
			c.add(Restrictions.eq("cd_biblioteca", bib));
			Tbf_default defaultId = getDefaultId(def.name());
			c.add(Restrictions.eq("id_default", defaultId));

			Tbf_biblioteca_default defBib = (Tbf_biblioteca_default) c.uniqueResult();
			if (defBib == null) {
				defBib = new Tbf_biblioteca_default();
				defBib.setId_default(defaultId);
				defBib.setCd_biblioteca(bib);
			}
			defBib.setValue(value);

			session.saveOrUpdate(defBib);

		} catch (HibernateException he) {
			throw new DaoManagerException();
		}
	}

	public Tbf_default getDefaultId(String name) throws DaoManagerException {
		try {
			//almaviva5_20140701 #3198
			Session session = this.getCurrentSession();
			Criteria c = session.createCriteria(Tbf_default.class);
			c.add(Restrictions.eq("key", name));

			return (Tbf_default) c.uniqueResult();

		} catch (HibernateException he) {
			throw new DaoManagerException();
		}
	}

}
