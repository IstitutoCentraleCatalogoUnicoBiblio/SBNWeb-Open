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
package it.iccu.sbn.servizi.codici;

import static org.hamcrest.Matchers.equalTo;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceValueType;
import it.iccu.sbn.ejb.vo.common.CodiciOrdinamentoType;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.factory.ConnectionFactory;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_modelli_stampe;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.matchers.Codici;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import ch.lambdaj.function.matcher.LambdaJMatcher;

public final class CodiciProvider {

	private static Logger log = Logger.getLogger(CodiciProvider.class);

	private static final class OrdinatoreCodPerCodiceSBN implements Comparator<Tb_codici> {

		private final CodiceValueType valueType;
		private final boolean desc;

		public OrdinatoreCodPerCodiceSBN(CodiceValueType valueType, boolean desc) {
			this.valueType = valueType;
			this.desc = desc;
		}

		public int compare(Tb_codici o1, Tb_codici o2) {
			return desc ?
					-cmp(o1, o2) : //ord discendente
					cmp(o1, o2);
		}

		private int cmp(Tb_codici o1, Tb_codici o2) {
			try {
				String e1 = ValidazioneDati.trimOrEmpty(o1.getCd_tabella());
				String e2 = ValidazioneDati.trimOrEmpty(o2.getCd_tabella());

				switch (valueType) {
				case ALL:
				case ALPHA:
				default:
					return e1.compareToIgnoreCase(e2);

				case NUMERIC:
					int i1 = Integer.valueOf(e1);
					int i2 = Integer.valueOf(e2);
					return i1 - i2;
				}

			} catch (Exception e) {
				log.error("", e);
				return 0;
			}
		};
	};

	private static final Comparator<Tb_codici> ordinatoreCodPerDescrizione_asc = new Comparator<Tb_codici>() {

		public int compare(Tb_codici o1, Tb_codici o2) {
			try {
				String e1 = ValidazioneDati.trimOrEmpty(o1.getDs_tabella());
				String e2 = ValidazioneDati.trimOrEmpty(o2.getDs_tabella());
				return e1.compareToIgnoreCase(e2);
			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		};
	};


	private static final Comparator<Tb_codici> ordinatoreCodPerDescrizione_desc = new Comparator<Tb_codici>() {

		public int compare(Tb_codici o1, Tb_codici o2) {
			try {
				String e1 = ValidazioneDati.trimOrEmpty(o1.getDs_tabella());
				String e2 = ValidazioneDati.trimOrEmpty(o2.getDs_tabella());
				return e2.compareToIgnoreCase(e1);
			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		};
	};

	private static final CodiciProvider instance = new CodiciProvider();

	private Map<String, TB_CODICI> listaTipoTabella;
	private Map<String, List<TB_CODICI>> listaCodici;
	private List<ModelloStampaVO> modelliStampa;

	private AtomicBoolean initialized = new AtomicBoolean(false);
	private Lock w;
	private Lock r;


	private static final CodiciProvider getInstance() {
		return instance;
	}

	private CodiciProvider() {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		w = lock.writeLock();
		r = lock.readLock();
	}

	private static final TB_CODICI convert(Tb_codici codice) {
		TB_CODICI web = new TB_CODICI();
		ClonePool.copyCommonProperties(web, codice);
		return web;
	}

	private static final boolean tipoCodiceEffettivo(CodiciType tipoCodice) {
		return (CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO2 != tipoCodice)
				&& (CodiciType.CODICE_SI_NO != tipoCodice)
				&& (CodiciType.CODICE_REPERTORIO_MARCHE != tipoCodice)
				&& (CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO_TUTTI != tipoCodice);
	}

	private static final void check(CodiciProvider instance) throws Exception {
		boolean initialized = true;
		if (!instance.initialized.get())
			initialized = instance.initialize();
		if (!initialized)
			throw new ApplicationException(SbnErrorTypes.AMM_ERRORE_INIZIALIZZAZIONE_CODICI);
	}

	@SuppressWarnings("unchecked")
	private final boolean initialize() throws Exception {

		w.lock();
		try {
			if (initialized.get())
				return true; // un altro thread ha provveduto al reload.

			initialized.set(false);

			// carico i codici dal DB
			this.listaTipoTabella = new THashMap<String, TB_CODICI>();
			this.listaCodici = new THashMap<String, List<TB_CODICI>>();
			this.modelliStampa = Collections.EMPTY_LIST;

			ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
			Session session = connectionFactory.getFactory().openSession();
			Transaction tx = session.beginTransaction();

			Criteria c = session.createCriteria(Tb_codici.class);
			c.addOrder(Order.asc("tp_tabella")).addOrder(Order.asc("cd_tabella"));
			List<Tb_codici> rows = c.list();


			Map<String, List<Tb_codici>> tabelle =
				Stream.of(rows).collect(Collectors.groupingBy(Codici.groupByTipoTabella()));

			List<Tb_codici> tipiTab = tabelle.get("0000");
			for (Tb_codici tabella : tipiTab) {
				String tipoTabella = ValidazioneDati.trimOrEmpty(tabella.getCd_tabella());
				//almaviva5_20131104
				CodiciType type = CodiciType.fromString(tipoTabella);
				if (type == null) {
					log.warn(String.format("Tabella codici '%s' non gestita.", tipoTabella));
					continue;
				}

				listaTipoTabella.put(tipoTabella, convert(tabella));

				List<Tb_codici> codici = tabelle.get(tipoTabella);

				CodiciOrdinamentoType defOrd = null;
				CodiceValueType valueType = CodiceValueType.ALL;
				try {
					// l'ordinamento, se specificato sul DB, é prioritario
					CodiceConfigVO config = CodiceConfigVO.build(tabella);
					valueType = config.getType();
					defOrd = config.getOrdinamento();

				} catch (ValidationException ve) {
					log.error("Errore configurazione tabella codici '" + tipoTabella + "'");
				}

				List<Tb_codici> listaOrdinata = ordinaLista(tipoTabella, codici, defOrd, valueType);
				listaOrdinata.add(0, new Tb_codici(tipoTabella, "", "", ""));

				int size = ValidazioneDati.size(listaOrdinata);
				List<TB_CODICI> valoriTabella = new ArrayList<TB_CODICI>(size);
				for (Tb_codici cod : listaOrdinata)
					valoriTabella.add(convert(cod).lock());

				listaCodici.put(tipoTabella, valoriTabella);
			}

			this.modelliStampa = caricaModelliStampa();
			log.info("Tabella modelli stampa caricata nel provider");
			log.info("Tabella codici caricata nel provider");

			tx.commit();

			initialized.set(true);
			return true;

		} catch (Exception e) {
			log.error("", e);
			initialized.set(false);
			return false;

		} finally {
			w.unlock();
		}
	}

	public static final void invalidate() {
		CodiciProvider provider = CodiciProvider.getInstance();
		provider.initialized.set(false);
		log.info("Richiesto aggiornamento tabella codici");
	}

	private static final List<Tb_codici> ordinaLista(String tipoTabella, List<Tb_codici> lista, CodiciOrdinamentoType defOrd,
			CodiceValueType valueType) {
		if (lista == null)
			return new ArrayList<Tb_codici>();

		CodiciType type = CodiciType.fromString(tipoTabella);
		if (type == null)
			return lista;

		CodiciOrdinamentoType ord = CodiciOrdinamentoType.ORDINAMENTO_PER_CODICE_ASC;

		if (defOrd != null)
			ord = defOrd;
		else
			ord = type.getTipoOrdinamento();

		if (ord == null)
			return lista;

		switch (ord) {
		case ORDINAMENTO_PER_CODICE_ASC:
			Collections.sort(lista, new OrdinatoreCodPerCodiceSBN(valueType, false));
			break;
		case ORDINAMENTO_PER_CODICE_DESC:
			Collections.sort(lista, new OrdinatoreCodPerCodiceSBN(valueType, true));
			break;
		case ORDINAMENTO_PER_DESCRIZIONE_ASC:
			Collections.sort(lista, ordinatoreCodPerDescrizione_asc);
			break;
		case ORDINAMENTO_PER_DESCRIZIONE_DESC:
			Collections.sort(lista, ordinatoreCodPerDescrizione_desc);
			break;
		default:
			Collections.sort(lista, ordinatoreCodPerDescrizione_asc);
		}

		return lista;
	}


	private final List<ModelloStampaVO> caricaModelliStampa()
			throws DaoManagerException {

		DaoManager dao = new DaoManager();
		Session session = dao.getCurrentSession();

		Criteria c = session.createCriteria(Tbf_modelli_stampe.class);
		c.add(Restrictions.ne("fl_canc", 'S')).addOrder(Order.asc("id_modello"));

		List<ModelloStampaVO> output = new ArrayList<ModelloStampaVO>();
		for (Object o : c.list()) {
			Tbf_modelli_stampe modello = (Tbf_modelli_stampe) o;
			output.add(ConversioneHibernateVO.toWeb().modelloStampa(modello));
		}
		return output;
	}


	private final List<TB_CODICI> nosyncGetCodici(CodiciType TB_TABELLA, boolean soloAttivi) {

		List<TB_CODICI> codici = null;
		if (listaCodici != null)
			if (listaCodici.containsKey(TB_TABELLA.getTp_Tabella()))
				codici = listaCodici.get(TB_TABELLA.getTp_Tabella());

		if (codici == null) {
			codici = new ArrayList<TB_CODICI>();
			codici.add(0, new TB_CODICI("", ""));
		}

		if (soloAttivi) {
			Date now = DaoManager.now();
			List<TB_CODICI> listaCodiciAttivi = new ArrayList<TB_CODICI>();
			for (TB_CODICI cod : codici) {
				Date fineValidita = cod.getDt_fine_validita();
				if (fineValidita == null || fineValidita.after(now))
					listaCodiciAttivi.add(cod);
			}
			return listaCodiciAttivi;

		}

		return new ArrayList<TB_CODICI>(codici);
	}

	public static final List<TB_CODICI> getCodici(CodiciType TB_TABELLA) throws Exception {
		return getCodici(TB_TABELLA, false);
	}

	public static final List<TB_CODICI> getCodici(CodiciType TB_TABELLA, boolean soloAttivi) throws Exception {

		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		provider.r.lock();
		try {
			List<TB_CODICI> codici = provider.nosyncGetCodici(TB_TABELLA, soloAttivi);
			log.debug("Tabella codici richiesta: " + TB_TABELLA.getTp_Tabella()	+ " (" + codici.size() + " elementi)");
			return codici;
		} finally {
			provider.r.unlock();
		}
	}

	public static final List<TB_CODICI> getCodici(CodiciType TB_TABELLA, String... esclusi) throws Exception {
		return getCodici(TB_TABELLA, false, esclusi);
	}

	public static final List<TB_CODICI> getCodici(CodiciType TB_TABELLA, boolean soloAttivi, String... esclusi) throws Exception {

		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		provider.r.lock();
		try {
			List<TB_CODICI> codici = provider.nosyncGetCodici(TB_TABELLA, soloAttivi);
			int size = ValidazioneDati.size(codici);
			if (size < 1)
				return null;
			if (!ValidazioneDati.isFilled(esclusi))
				return codici;

			List<TB_CODICI> filtered = new ArrayList<TB_CODICI>(size);
			List<String> excluded = Arrays.asList(esclusi);
			for (TB_CODICI cod : codici)
				if (!excluded.contains(cod.getCd_tabellaTrim()))
					filtered.add(cod);

			log.debug("Tabella codici richiesta: " + TB_TABELLA.getTp_Tabella()	+ " (" + filtered.size() + " elementi)");
			return filtered;

		} finally {
			provider.r.unlock();
		}
	}

	public static final List<TB_CODICI> getCodiciCross(CodiciType tpTabellaP,
			String codiceP, boolean soloAttivi) throws Exception {

		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		provider.r.lock();
		try {
			List<TB_CODICI> codiciP = null;
			String tp_Tabella = tpTabellaP.getTp_Tabella();
			if (provider.listaCodici != null)
				if (provider.listaCodici.containsKey(tp_Tabella))
					codiciP = provider.listaCodici.get(tp_Tabella);

			if (codiciP == null)
				return null;

			if (soloAttivi) {
				Date now = DaoManager.now();
				List<TB_CODICI> listaCodiciAttivi = new ArrayList<TB_CODICI>();
				for (TB_CODICI cod : codiciP) {
					Date fineValidita = cod.getDt_fine_validita();
					if (fineValidita == null || fineValidita.after(now))
						listaCodiciAttivi.add(cod);
				}
				codiciP = listaCodiciAttivi;
			}

			// cerco il mio codice sulla tabella cross
			boolean foundP = false;
			for (TB_CODICI cod : codiciP) {
				if (!ValidazioneDati.equals(cod.getCd_flg1(), codiceP))
					continue;
				foundP = true;
				break;
			}
			if (!foundP)
				return null;

			// ricavo dalla config il tipo di tabella collegata
			TB_CODICI configP = provider.listaTipoTabella.get(tp_Tabella);
			CodiceConfigVO config = null;
			try {
				config = CodiceConfigVO.build(configP);
			} catch (ValidationException e) {
				throw new Exception(e);
			}

			CodiciType tpTabellaC = config.getTpTabellaC();
			List<TB_CODICI> codiciC = provider.nosyncGetCodici(tpTabellaC, false);
			if (ValidazioneDati.size(codiciC) < 2) // non ci sono codici collegati
				return null;

			List<TB_CODICI> output = new ArrayList<TB_CODICI>();

			// per ogni codice della tabella P carico tutte le occorrenze dalla tabella C
			for (TB_CODICI cod : codiciP) {
				if (!ValidazioneDati.equals(cod.getCd_flg1(), codiceP))
					continue;
				TB_CODICI codiceC = cercaCodice(cod.getCd_flg2(), tpTabellaC, CodiciRicercaType.RICERCA_CODICE_SBN);
				if (codiceC != null)
					output.add(codiceC);
			}

			if (ValidazioneDati.size(output) < 1)
				return null;

			log.debug("Tabella codici CROSS richiesta: " + tp_Tabella + " (" + output.size() + " elementi)");

			return output;

		} finally {
			provider.r.unlock();
		}
	}

	public static final List<TB_CODICI> getCodiciWithFlags(CodiciType TB_TABELLA, boolean soloAttivi, Integer[] flags, String[] values) throws Exception {

		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		List<TB_CODICI> codici;

		provider.r.lock();
		try {
			codici = provider.nosyncGetCodici(TB_TABELLA, soloAttivi);
			log.debug("Tabella codici richiesta: " + TB_TABELLA.getTp_Tabella()	+ " (" + codici.size() + " elementi)");
		} finally {
			provider.r.unlock();
		}

		if (ValidazioneDati.size(flags) != ValidazioneDati.size(values))
			return codici;

		int v_idx = 0;
		TB_CODICI p = on(TB_CODICI.class);
		LambdaJMatcher<Object> cnd = having(p.getTp_tabella(), equalTo(TB_TABELLA.getTp_Tabella()));

		for (Integer f_idx : flags) {
			switch (f_idx) {
			case 1:
				cnd = cnd.and(having(p.getCd_flg1(), equalTo(values[v_idx++]) ));
				break;
			case 2:
				cnd = cnd.and(having(p.getCd_flg2(), equalTo(values[v_idx++]) ));
				break;
			case 3:
				cnd = cnd.and(having(p.getCd_flg3(), equalTo(values[v_idx++]) ));
				break;
			case 4:
				cnd = cnd.and(having(p.getCd_flg4(), equalTo(values[v_idx++]) ));
				break;
			case 5:
				cnd = cnd.and(having(p.getCd_flg5(), equalTo(values[v_idx++]) ));
				break;
			case 6:
				cnd = cnd.and(having(p.getCd_flg6(), equalTo(values[v_idx++]) ));
				break;
			case 7:
				cnd = cnd.and(having(p.getCd_flg7(), equalTo(values[v_idx++]) ));
				break;
			case 8:
				cnd = cnd.and(having(p.getCd_flg8(), equalTo(values[v_idx++]) ));
				break;
			case 9:
				cnd = cnd.and(having(p.getCd_flg9(), equalTo(values[v_idx++]) ));
				break;
			case 10:
				cnd = cnd.and(having(p.getCd_flg10(), equalTo(values[v_idx++]) ));
				break;
			case 11:
				cnd = cnd.and(having(p.getCd_flg11(), equalTo(values[v_idx++]) ));
				break;
			default:
				throw new ApplicationException(SbnErrorTypes.AMM_ERRORE_INTERROGAZIONE_CODICI);
			}

			//cnd = cnd.and(having(p.getFlag(f_idx), equalTo(values[v_idx++]) ));
		}

		return select(codici, cnd);

	}

	public static final TB_CODICI cercaCodice(String codiceRicerca, CodiciType tipoCodice,
			CodiciRicercaType tipoRicerca) throws Exception {
		return cercaCodice(codiceRicerca, tipoCodice, tipoRicerca, false);
	}

	public static final TB_CODICI cercaCodice(String codiceRicerca, CodiciType tipoCodice,
			CodiciRicercaType tipoRicerca, boolean soloAttivi) throws Exception {

		if (ValidazioneDati.strIsNull(codiceRicerca))
			return null;

		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		provider.r.lock();
		try {
			// Lista di codici nella quale cerca
			List<TB_CODICI> listaRicerca = provider.nosyncGetCodici(tipoCodice, false);
			// Variabile di appoggio contenente la riga del codice eventualmente trovato
			String target = codiceRicerca.trim();
			TB_CODICI found = null;
			if (listaRicerca != null) {
				int size = listaRicerca.size();
				// La ricerca prosegue fino alla fine della lista e finchè il codice non è stato trovato:
				for (int i = 0; ((found == null) && (i < size)); i++) {
					// Codice successivo:
					TB_CODICI cod = listaRicerca.get(i);
					// A seconda del tipo di codice da cercare (SBN o UNIMARC) esegue un test diverso
					switch (tipoRicerca) {
					case RICERCA_CODICE_SBN:
						if (target.equals(cod.getCd_tabellaTrim() ))
							// Codice SBN trovato
							found = cod;

						break;

					case RICERCA_CODICE_UNIMARC:
						if (target.equals(cod.getCd_unimarcTrim()))
							// Codice UNIMARC trovato
							found = cod;
						break;

					default:
						throw new ApplicationException(SbnErrorTypes.ERROR_UNSUPPORTED);
					}
				}
			}

			//almaviva5_20100429 solo codici attivi
			if (soloAttivi && found != null) {
				Date now = DaoManager.now();
				Date fineValidita = found.getDt_fine_validita();
				if (fineValidita != null && fineValidita.before(now))
					return null;
			}

			return found;

		} finally {
			provider.r.unlock();
		}
	}

	public static final String cercaDescrizioneCodice(String codiceRicerca,
			CodiciType tipoCodice, CodiciRicercaType tipoRicerca)
			throws Exception {
		return cercaDescrizioneCodice(codiceRicerca, tipoCodice, tipoRicerca, false);
	}

	public static final String cercaDescrizioneCodice(Character codiceRicerca,
			CodiciType tipoCodice, CodiciRicercaType tipoRicerca)
			throws Exception {
		return cercaDescrizioneCodice(codiceRicerca, tipoCodice, tipoRicerca, false);
	}

	public static final String cercaDescrizioneCodice(Character codiceRicerca,
			CodiciType tipoCodice, CodiciRicercaType tipoRicerca,
			boolean soloAttivi) throws Exception {
		if (codiceRicerca == null)
			return null;
		return cercaDescrizioneCodice(codiceRicerca.toString(), tipoCodice, tipoRicerca, soloAttivi);
	}

	public static final String cercaDescrizioneCodice(String codiceRicerca,
			CodiciType tipoCodice, CodiciRicercaType tipoRicerca,
			boolean soloAttivi)	throws Exception {

		if (ValidazioneDati.strIsNull(codiceRicerca))
			return null;

		List<TB_CODICI> listaRicerca = null;
		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		provider.r.lock();
		try {
			listaRicerca = provider.nosyncGetCodici(tipoCodice, soloAttivi);

			TB_CODICI found = null;
			String target = codiceRicerca.trim();
			String descrizione = "";

			if (listaRicerca != null) {
				int size = listaRicerca.size();
				for (int i = 0; ((found == null) && (i < size)); i++) {
					TB_CODICI cod = listaRicerca.get(i);
					switch (tipoRicerca) {
					case RICERCA_CODICE_SBN:
					case RICERCA_CODICE_SBN_ULTERIORE:
						if (cod.getCd_tabella() != null) {
							if (target.equals(cod.getCd_tabellaTrim())) {
								found = cod;
								descrizione = (tipoRicerca == CodiciRicercaType.RICERCA_CODICE_SBN) ?
										found.getDs_tabella() :
										found.getDs_cdsbn_ulteriore();
							}
						}
						break;

					case RICERCA_CODICE_UNIMARC:
					case RICERCA_CODICE_UNIMARC_ULTERIORE:
						if (cod.getCd_unimarc() != null) {
							if (target.equals(cod.getCd_unimarcTrim())) {
								found = cod;
								descrizione = (tipoRicerca == CodiciRicercaType.RICERCA_CODICE_UNIMARC) ?
										found.getDs_tabella() :
										found.getDs_cdsbn_ulteriore();
							}
						}
						break;
					case CODICE_LEGAME_TITOLO_TITOLO:
						if (cod.getCd_unimarc() != null) {
							if (target.equals(cod.getCd_unimarcTrim())) {
								found = cod;
								descrizione = found.getCd_tabella().substring(1, 3);
							}
						}
						break;

					}
				}
			}

			return descrizione;

		} finally {
			provider.r.unlock();
		}
	}

	public static final String getDescrizioneCodiceUNIMARC(CodiciType tipoCodice,
			String codiceUnimarc) throws Exception {
		return cercaDescrizioneCodice(codiceUnimarc, tipoCodice, CodiciRicercaType.RICERCA_CODICE_UNIMARC);
	}

	public static final String getDescrizioneCodiceSBN(CodiciType tipoCodice,
			String codiceSBN) throws Exception {
		return cercaDescrizioneCodice(codiceSBN, tipoCodice, CodiciRicercaType.RICERCA_CODICE_SBN);
	}

	public static final String getDescrizioneCodiceSBN(CodiciType tipoCodice,
			Character codiceSBN) throws Exception {
		return cercaDescrizioneCodice(codiceSBN, tipoCodice, CodiciRicercaType.RICERCA_CODICE_SBN);
	}

	public static final String SBNToUnimarc(CodiciType tipoCodice, String codiceSBN) throws Exception {
		// La stringa vuota non corrisponde ad alcun codice e viene ritornata
		// non convertita
		if (ValidazioneDati.strIsNull(codiceSBN) || !tipoCodiceEffettivo(tipoCodice))
			return codiceSBN;

		// Ricerca del codice SBN
		TB_CODICI codice = cercaCodice(codiceSBN, tipoCodice, CodiciRicercaType.RICERCA_CODICE_SBN);
		if (codice == null)
			return null;

		// Viene ritornato il codice UNIMARC corrispondente
		String cd_unimarc = codice.getCd_unimarc();
		if (ValidazioneDati.strIsNull(cd_unimarc))
			return null;

		return cd_unimarc.trim();
	}

	public static final String unimarcToSBN(CodiciType tipoCodice, String codiceUnimarc)
			throws Exception {
		// La stringa vuota non corrisponde ad alcun codice e viene ritornata
		// non convertita
		if (ValidazioneDati.strIsNull(codiceUnimarc) || !tipoCodiceEffettivo(tipoCodice))
			return codiceUnimarc;
		// Ricerca del codice UNIMARC
		TB_CODICI codice = cercaCodice(codiceUnimarc, tipoCodice, CodiciRicercaType.RICERCA_CODICE_UNIMARC);
		if (codice == null)
			return null;

		String cd_tabella = codice.getCd_tabella();
		if (ValidazioneDati.strIsNull(cd_tabella))
			return null;

		return cd_tabella.trim();
	}

	public static final List<ModelloStampaVO> getModelliStampaPerAttivita(String codAttivita) throws Exception {

		if (ValidazioneDati.strIsNull(codAttivita))
			return null;

		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		provider.r.lock();
		try {
			List<ModelloStampaVO> output = new ArrayList<ModelloStampaVO>();
			for (ModelloStampaVO mod : provider.modelliStampa ) {
				String attivita = mod.getAttivita();
				if (ValidazioneDati.isFilled(attivita) && attivita.equals(codAttivita))
					output.add(mod);
			}

			return ClonePool.deepCopy(output);

		} finally {
			provider.r.unlock();
		}
	}

	public static final ModelloStampaVO getModelloStampa(int idModello) throws Exception {

		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		provider.r.lock();
		try {
			int result = Collections.binarySearch(provider.modelliStampa, new ModelloStampaVO(idModello));
			if (result < 0)
				return null;
			else
				return provider.modelliStampa.get(result).copy();

		} finally {
			provider.r.unlock();
		}
	}

	public static final ModelloStampaVO getModelloStampa(String jrxml) throws Exception {

		if (ValidazioneDati.strIsNull(jrxml))
			return null;

		CodiciProvider provider = CodiciProvider.getInstance();
		check(provider);

		provider.r.lock();
		try {
			if (provider.modelliStampa == null)
				return null;

			for (ModelloStampaVO mod : provider.modelliStampa) {
				if (mod != null) {
					String value = mod.getJrxml();
					if (value != null) {
						if (value.equalsIgnoreCase(jrxml)) {
							return mod.copy();
						}
					}
				}
			}
			return null;

		} finally {
			provider.r.unlock();
		}
	}

	public static final boolean exists(CodiciType tipoCodice, String codiceRicerca) throws Exception {
		TB_CODICI cod = cercaCodice(codiceRicerca, tipoCodice, CodiciRicercaType.RICERCA_CODICE_SBN, false);
		return (cod != null);
	}

}
