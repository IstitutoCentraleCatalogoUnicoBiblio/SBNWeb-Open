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
package it.finsiel.sbn.polo.factoring.util;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.BasicTableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_contatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Classe Progressivi.java
 * <p>
 * Gestisce i progressivi nelle tabelle del DB. Per creare un nuovo id si deve:
 * leggere dalla tabella ts_progressivi_id utilizzando la chiave che identifica
 * la tabella che si vuole aggiornare, sommare 1 a progr_ultimo e fare l'update
 * del record, chiudere la transazione per liberare il lock sul record
 * ts_progressivi_id
 * </p>
 *
 * @author
 * @author
 *
 * @version 14-gen-2003
 */
public class Progressivi {

	private static final int XID6_MAX_VALUE = ResourceLoader.getPropertyInteger("XID6_MAX_VALUE");
	private static final int XID7_MAX_VALUE = ResourceLoader.getPropertyInteger("XID7_MAX_VALUE");

	private static Logger log = Logger.getLogger("iccu.serversbnmarc.Progressivi");
	protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();

	private static final Map<String, Character> map = new HashMap<String, Character>();

	static {
		map.put(SbnAuthority.SO.toString(), 'C');
		map.put(SbnAuthority.DE.toString(), 'D');
		map.put(SbnAuthority.AU.toString(), 'V');
		map.put(SbnAuthority.TH.toString(), 'D');
		map.put(SbnAuthority.LU.toString(), 'L');
		map.put(SbnAuthority.PP.toString(), 'P');
		map.put(SbnAuthority.MA.toString(), 'M');
		map.put("E", 'E');
	}

	private static final void check6(int last) throws EccezioneDB {
		if (last > XID6_MAX_VALUE)
			throw new EccezioneDB(5009);
	}

	private static final void check7(int last) throws EccezioneDB {
		if (last > XID7_MAX_VALUE)
			throw new EccezioneDB(5009);
	}

	public Progressivi() {
		super();
	}

	/**
	 * Metodo per avere l'identificatore progressivo di un autore. Restituisce
	 * una stringa formata nel modo seguente: sigla + "V" + -idNumerico di 6
	 * cifre- ove sigla è la property SIGLA_INDICE ATTENZIONE Accede
	 * direttamente al resultSet
	 *
	 * @throws InfrastructureException
	 */
	public String getNextIdAutore() throws EccezioneDB, SQLException,
			InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";
		String qry = pre + "Tb_Autore_sequenceNextVal";

		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;
		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di tb_autore");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {
			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}

		return buildId(" __", SbnAuthority.AU, id);
	}

	/**
	 * Metodo per avere l'ISADN progressivo di un autore. Restituisce una
	 * stringa formata nel modo seguente: sigla + "IT/ICCU/" + -idNumerico di 22
	 * cifre- ATTENZIONE Accede direttamente al resultSet
	 *
	 * @throws InfrastructureException
	 */
	public String getNextIsadnAutore() throws EccezioneDB,
			InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_autore_sequenceIsadnNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di isadn tb_autore");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}

			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}
		String idString = "" + id;
		while (idString.length() < 10)
			idString = "0" + idString;

		String sigla = "IT\\ICCU\\";// ResourceLoader.getPropertyString("SIGLA_ISADN");
		return sigla + idString;
	}

	/**
	 * Metodo per avere l'identificatore progressivo di un titolo. Restituisce
	 * una stringa formata nel modo seguente: sigla + -idNumerico di 7 cifre-
	 * oppure per gli antichi sigla + E + -idNumerico di 6 cifre- o per
	 * manoscritti musicali MSM + -idNumerico di 7 cifre- oppure per altra
	 * musica MUS + -idNumerico di 7 cifre- ove sigla è la property SIGLA_INDICE
	 * ATTENZIONE Accede direttamente al resultSet
	 *
	 * @throws InfrastructureException
	 */
	public String getNextIdTitolo(String tipo_materiale, String tipoRecord)
			throws EccezioneDB, InfrastructureException {

		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		Number next;

		boolean isAntico = ValidazioneDati.equals(tipo_materiale, "E");
		if (isAntico) {
			String qry = pre + "Tb_titolo_sequenceENextVal";
			SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
			next = (Number) query.uniqueResult();

			check6(next.intValue());
		}
		// modifica del 01/10/2007 eliminate le casistiche dei prefissi MSM e
		// MUS LARA
		/*
		else if (tipo_materiale.equals("U")) {
			if ("D".equalsIgnoreCase(tipoRecord)) {
				sigla = "MSM";
				String qry = pre + "Tb_titolo_sequenceMSMNextVal";
				SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
				results = (List<Integer>) query.list();
			} else {
				sigla = "MUS";
				String qry = pre + "Tb_titolo_sequenceMUSNextVal";
				SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
				results = (List<Integer>) query.list();
			}
		}
		*/
		else {
			String qry = pre + "Tb_titolo_sequenceNextVal";
			SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
			next = (Number) query.uniqueResult();

			check7(next.intValue());
		}
		int id;
		try {
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}

		return buildId(" __", isAntico ? "E" : null, id);
	}

	// La funzione viene sostituita in tutto con getNextIdTitolo
	// almaviva2 01/10/2007

	public String getNextIdTitoloCMP() throws EccezioneDB,
			InfrastructureException {
		String sigla;
		int num_length = 7;
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_titolo_sequenceCMPNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;
		sigla = "CMP";
		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di tb_titolo");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}
		String idString = "" + id;
		while (idString.length() < num_length)
			idString = "0" + idString;
		return sigla + idString;
	}

	/**
	 * Metodo per avere l'ISADN progressivo di un titolo. Restituisce una
	 * stringa formata nel modo seguente: sigla + "IT/ICCU/" + -idNumerico di 22
	 * cifre- ATTENZIONE Accede direttamente al resultSet
	 *
	 * @throws InfrastructureException
	 */
	public String getNextIsadnTitolo() throws EccezioneDB,
			InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_titolo_sequenceIsadnNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di tb_titolo");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}
		String idString = "" + id;
		while (idString.length() < 10)
			idString = "0" + idString;
		String sigla = "IT\\ICCU\\"; // String sigla =
										// ResourceLoader.getPropertyString("SIGLA_ISADN");
		return sigla + idString;
	}

	/**
	 * Metodo per avere l'identificatore progressivo di un luogo. Restituisce
	 * una stringa formata nel modo seguente: sigla + "L" + -idNumerico di 6
	 * cifre- ove sigla è la property SIGLA_INDICE ATTENZIONE Accede
	 * direttamente al resultSet
	 *
	 * @throws InfrastructureException
	 */
	public String getNextIdLuogo() throws EccezioneDB, InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_luogo_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di tb_luogo");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}

		return buildId(" __", SbnAuthority.LU, id);
	}

	/**
	 * Metodo per avere l'identificatore progressivo di una marca. Restituisce
	 * una stringa formata nel modo seguente: sigla + "M" + -idNumerico di 6
	 * cifre- ove sigla è la property SIGLA_INDICE ATTENZIONE Accede
	 * direttamente al resultSet
	 *
	 * @throws InfrastructureException
	 */
	public String getNextIdMarca() throws EccezioneDB, InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_marca_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di tb_marca");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}

		return buildId(" __", SbnAuthority.MA, id);
	}

	/**
	 * metodo per generare l'id della parola
	 *
	 * @throws InfrastructureException
	 **/
	public String getNextIdParola() throws EccezioneDB, InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_parola_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di tb_parola");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}

		String idString = "" + id;
		return idString;
	}

	public String getNextIdLinkMultim() throws EccezioneDB,
			InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Ts_link_multim_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di Ts_link_multimCommonDao");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}
		String idString = "" + id;
		return idString;
	}

	/**
	 * Metodo per generare il nome che identifica la lista dei blocchi. Tale
	 * nome è creato da una sequence.
	 *
	 * @throws InfrastructureException
	 */
	public String getIdLista() throws EccezioneDB, InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_tim_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di Tb_tim");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			//almaviva5_20200124 #7326 segnalazione TO0: eliminato controllo valore max.
			//check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}
		String idString = "" + id;
		return idString;
	}

	public String getNextIdDescrittore() throws EccezioneDB,
			InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_descrittore_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di Tb_descrittoreCommonDao");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}

		return buildId(" __", SbnAuthority.DE, id);
	}

	public String getNextIdTermineThesauro() throws EccezioneDB,
			InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_termine_thesauro_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di Tb_termine_thesauroCommonDao");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}

		return buildId(" __", SbnAuthority.TH, id);
	}

	/**
	 * Legge la sequence su tb_personaggio e crea un intero da utilizzarsi come
	 * chiave
	 *
	 * @return
	 * @throws EccezioneDB
	 * @throws InfrastructureException
	 */
	public int getNextIdPersonaggio() throws EccezioneDB,
			InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_personaggio_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;
		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di Tb_descrittoreCommonDao");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			//almaviva5_20150914 la seq. del personaggio non va limitata a 6 digit.
			//check6(id);

			return id;
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}
	}

	/**
	 *
	 * Legge la sequence su ts_proposta_marc e crea un intero da utilizzarsi
	 * come chiave
	 *
	 * @return
	 * @throws EccezioneDB
	 * @throws InfrastructureException
	 */
	public int getNextIdProposta() throws EccezioneDB, InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Ts_proposta_marc_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;
		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di Ts_proposta_marc");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);

			return id;
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}
	}

	public String getNextIdSoggetto() throws EccezioneDB,
			InfrastructureException {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		dao.beginTransaction();
		String pre = BasicTableDao.isSessionOracle(session) ? "" : "Postgress_";

		String qry = pre + "Tb_soggetto_sequenceNextVal";
		SQLQuery query = (SQLQuery) session.getNamedQuery(qry);
		Number next = (Number) query.uniqueResult();
		int id = 0;

		try {
			if (next == null) {
				log.error("Errore nella lettura della sequence di tb_soggetto");
				dao.commitTransaction();
				dao.closeSession();
				throw new EccezioneDB(1204);
			}
			id = next.intValue();
			dao.commitTransaction();
			dao.closeSession();

			check6(id);
		} catch (InfrastructureException e) {

			log.error("", e);
			dao.commitTransaction();
			dao.closeSession();
			throw new EccezioneDB(1203, e);
		}

		return buildId(" __", SbnAuthority.SO, id);
	}

	protected String incrementAndGet(String cdBib, String idCont, String anno,
			String key) throws InfrastructureException, EccezioneDB {
		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		Tbf_contatore cnt;
		try {
			Criteria c = session.createCriteria(Tbf_contatore.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.eq("cd_polo", BasicTableDao.creaIdBib(validator.getCodicePolo(), cdBib)));
			c.add(Restrictions.eq("cd_cont", idCont));

			if (ValidazioneDati.isFilled(anno))
				c.add(Restrictions.eq("anno", anno));

			if (ValidazioneDati.isFilled(key))
				c.add(Restrictions.eq("key1", key));

			cnt = (Tbf_contatore) c.setLockMode(LockMode.UPGRADE).uniqueResult();
			if (cnt == null)
				throw new InfrastructureException("Contatore non trovato");

			int last = cnt.getUltimo_prg();
			if (last == ResourceLoader.getPropertyInteger("XID_MAX_VALUE"))
				throw new EccezioneDB(5009);

			cnt.setUltimo_prg(last + 1);

			session.update(cnt);
		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}

		return String.format("%s%s%6d", validator.getCodicePolo(), cnt
				.getKey1().trim(), cnt.getUltimo_prg());

	}

	private String buildId(String cdBib, String prefix, int id)
			throws InfrastructureException, EccezioneDB {

		BasicTableDao dao = new BasicTableDao();
		Session session = dao.getSession();
		String cdPolo = validator.getCodicePolo();

		Tbf_contatore cnt;
		try {
			Criteria c = session.createCriteria(Tbf_contatore.class);
			c.add(Restrictions.ne("fl_canc", 'S'));
			c.add(Restrictions.eq("cd_polo", BasicTableDao.creaIdBib(validator.getCodicePolo(), cdBib)));
			c.add(Restrictions.eq("cd_cont", prefix));

			cnt = (Tbf_contatore) c.uniqueResult();
			if (cnt != null)
				cdPolo = cnt.getKey1().trim();

		} catch (HibernateException e) {
			throw new InfrastructureException(e);
		}
		if (ValidazioneDati.isFilled(prefix))
			return String.format("%s%s%06d", cdPolo, map.get(prefix), id);
		else
			return String.format("%s%07d", cdPolo, id);
	}

	private String buildId(String cdBib, SbnAuthority authority, int id)
			throws InfrastructureException, EccezioneDB {
		return buildId(cdBib, authority.toString(), id);
	}

	public static final char[] LETTERS = new char[] { 'C', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z' };
	public static final int MAX6 = 1000000;// 999999;

	public static void main(String[] args) throws Exception {
		BufferedWriter w = new BufferedWriter(new FileWriter(
				"c:\\tmp\\test-id.txt"));
		try {
			for (int i = 999990; i < Integer.MAX_VALUE; i++) {
				float f = (float) i / (float) (MAX6);
				int l = (int) Math.floor(f);
				// int l = (int) Math.ceil(f);
				int id = l > 0 ? +(i - ((MAX6) * l)) : i;

				String s = String.format("%s%s%06d", "XXX", LETTERS[l], id);
				// System.out.println(s);
				w.write(s);
				w.newLine();
			}
		} finally {
			w.close();
		}
	}

}
