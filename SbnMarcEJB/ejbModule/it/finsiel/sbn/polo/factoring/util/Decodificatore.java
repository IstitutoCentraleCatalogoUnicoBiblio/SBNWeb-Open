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
import it.finsiel.sbn.polo.exception.util.Errore;
import it.finsiel.sbn.polo.exception.util.Errori;
import it.finsiel.sbn.polo.orm.Tb_codici;
import it.finsiel.sbn.util.parse.ErroriContentHandler;
import it.finsiel.sbn.util.parse.PoloXMLParser;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

/**
 * @author dato che la tabella Tb_codiciCommonDao è una tabella di decodifica
 *         abbiamo bisogno di una classe per Decodificare la tabella.
 *         L'obiettivo del decodificatore è quello di costruire un vettore di
 *         oggetti di tipo infoDecodifica: - nome_tabella (es. tb_autore) -
 *         nome_campo (es. cd_colore) - cd_tabella (es. B, C....) - ds_tabella
 *         (es. Bianco e Nero)
 *
 *
 */
public final class Decodificatore {

	static Function<Tb_codici, String> groupByTipoTabella() {
		return new Function<Tb_codici, String>() {
			public String apply(Tb_codici c) {
				return c.getTP_TABELLA();
			}
		};
	}

	private static final String _XSD_ERRORS_PATH = "/META-INF/errori/errori.xsd";
	private static final String _XML_ERRORS_PATH = "/META-INF/errori/errori.xml";

	private static Logger log = Logger.getLogger("iccu.box.Decodificatore");

	static final Decodificatore instance = new Decodificatore();

	private final Map<String, List<Tb_codici>> CODICI_ELENCO = new HashMap<String, List<Tb_codici>>();
	private final Map<String, String> _TABELLADECODIFICA = new HashMap<String, String>();

	private Errori _ERRORI = new Errori();

	private final AtomicBoolean initialized = new AtomicBoolean(false);
	final Lock w;
	final Lock r;

	/**
	 * prepara una hash-table con il contenuto della tabella TB_codici
	 *
	 * @throws InfrastructureException
	 */
	protected Decodificatore() {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		w = lock.writeLock();
		r = lock.readLock();

		w.lock();
		try {
			this.decodificaToHash();
			//this.initialize(true);
		} finally {
			w.unlock();
		}
	}

	private void decodificaToHash() {
		// è da cambiare bisogna leggere i dati da un file xml
		_TABELLADECODIFICA.put("Tb_soggetto.cat_sogg", "SCSO");
		_TABELLADECODIFICA.put("Tb_autore.cd_agenzia", "AGEN");
		_TABELLADECODIFICA.put("Tb_titolo.cd_agenzia", "AGEN");
		_TABELLADECODIFICA.put("Tb_grafica.cd_altitudine", "ALTI");
		_TABELLADECODIFICA.put("Tb_autore.tp_nome_aut", "AUTO");
		_TABELLADECODIFICA.put("Tb_grafica.cd_categ_satellite", "CASA");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_colore", "COLC");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_supporto_fisico", "SUFC");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_meridiano", "MEOR");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_altitudine", "ALTI");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_categ_satellite", "CASA");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_forma_cart", "FODC");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_forma_pubb", "FOPU");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_forma_ripr", "FORI");
		_TABELLADECODIFICA.put("Tb_cartografia.tp_immagine", "IMMA");
		_TABELLADECODIFICA.put("Tb_cartografia.tp_piattaforma", "PIAT");
		_TABELLADECODIFICA.put("Tb_cartografia.tp_pubb_gov", "PUGO");
		_TABELLADECODIFICA.put("Tb_cartografia.tp_scala", "SCAL");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_tiposcala", "TISC");
		_TABELLADECODIFICA.put("Tb_grafica.cd_colore", "COLO");
		_TABELLADECODIFICA.put("Tb_titolo.tp_aa_pubb", "DATA");
		_TABELLADECODIFICA.put("Tb_grafica.cd_design_funz", "DESF");
		_TABELLADECODIFICA.put("Tb_classe.cd_edizione", "ECLA");
		_TABELLADECODIFICA.put("Tr_tit_cla.cd_edizione", "ECLA");
		_TABELLADECODIFICA.put("Tb_musica.tp_elaborazione", "ELMU");
		_TABELLADECODIFICA.put("Tb_autore.tp_forma_aut", "FOAU");
		_TABELLADECODIFICA.put("Tb_grafica.cd_forma_cart", "FODC");
		_TABELLADECODIFICA.put("Tb_incipit.cd_forma", "FOMU");
		_TABELLADECODIFICA.put("Tb_luogo.tp_forma", "FOLU");
		_TABELLADECODIFICA.put("Tb_composizione.cd_forma_1", "FOMU");
		_TABELLADECODIFICA.put("Tb_composizione.cd_forma_2", "FOMU");
		_TABELLADECODIFICA.put("Tb_composizione.cd_forma_3", "FOMU");
		_TABELLADECODIFICA.put("Tb_titset_2.s231_forma_opera", "FOOP");
		_TABELLADECODIFICA.put("Tb_grafica.cd_forma_pubb", "FOPU");
		_TABELLADECODIFICA.put("Tb_grafica.cd_forma_ripr", "FORI");
		_TABELLADECODIFICA.put("Tb_rappresent.tp_genere", "GENR");
		_TABELLADECODIFICA.put("Tb_titolo.cd_genere_1", "GEPU");
		_TABELLADECODIFICA.put("Tb_titolo.cd_genere_2", "GEPU");
		_TABELLADECODIFICA.put("Tb_titolo.cd_genere_3", "GEPU");
		_TABELLADECODIFICA.put("Tb_titolo.cd_genere_4", "GEPU");
		_TABELLADECODIFICA.put("Tb_titolo.tp_record_uni", "GEUN");
		_TABELLADECODIFICA.put("Tb_grafica.tp_immagine", "IMMA");
		_TABELLADECODIFICA.put("Tb_incipit.tp_indicatore", "ININ");
		_TABELLADECODIFICA.put("Tr_aut_aut.tp_legame", "LEAA");
		_TABELLADECODIFICA.put("Tr_des_des.tp_legame", "LEDD");
		_TABELLADECODIFICA.put("Tb_titolo.tp_link", "LELK");
		_TABELLADECODIFICA.put("Tr_luo_luo.tp_legame", "LELL");
		_TABELLADECODIFICA.put("Tr_tit_tit.tp_legame_musica", "LEMU");
		_TABELLADECODIFICA.put("Tr_tit_aut.cd_relazione", "LETA");
		_TABELLADECODIFICA.put("Tb_titolo.cd_lingua_1", "LING");
		_TABELLADECODIFICA.put("Tb_autore.cd_lingua", "LING");
		_TABELLADECODIFICA.put("Tb_titolo.cd_lingua_2", "LING");
		_TABELLADECODIFICA.put("Tb_titolo.cd_lingua_3", "LING");
		_TABELLADECODIFICA.put("Tb_autore.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_classe.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_descrittore.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_luogo.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_marca.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_soggetto.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_titolo.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tr_tit_luo.tp_luogo", "LUOG");
		_TABELLADECODIFICA.put("Tb_grafica.tp_materiale_gra", "MAGR");
		_TABELLADECODIFICA.put("Tb_musica.cd_materia", "MAMU");
		_TABELLADECODIFICA.put("Tb_codici.tp_materiale", "MATE");
		_TABELLADECODIFICA.put("Tb_titolo.tp_materiale", "MATE");
		_TABELLADECODIFICA.put("Tb_titolo.cd_natura", "NABI");
		_TABELLADECODIFICA.put("Tb_nota.tp_nota", "NOTA");
		_TABELLADECODIFICA.put("Tb_numero_std.tp_numero_std", "NSTD");
		_TABELLADECODIFICA.put("Tb_autore.cd_paese", "PAES");
		_TABELLADECODIFICA.put("Tb_numero_std.cd_paese", "PAES");
		_TABELLADECODIFICA.put("Tb_titolo.cd_paese", "PAES");
		_TABELLADECODIFICA.put("Tb_luogo.cd_paese", "PAES");
		_TABELLADECODIFICA.put("Tb_musica.cd_presentazione", "PRES");
		_TABELLADECODIFICA.put("Tb_cartografia.tp_proiezione", "PROE");
		_TABELLADECODIFICA.put("Tb_grafica.tp_pubb_gov", "PUGO");
		_TABELLADECODIFICA.put("Tb_repert.tp_repertorio", "REPE");
		_TABELLADECODIFICA.put("Tr_tit_aut.tp_responsabilita", "RESP");
		_TABELLADECODIFICA.put("Tb_grafica.tp_scala", "SCAL");
		_TABELLADECODIFICA.put("Tb_classe.cd_sistema", "SCLA");
		_TABELLADECODIFICA.put("Tr_tit_cla.cd_sistema", "SCLA");
		_TABELLADECODIFICA.put("Tb_descrittore.cd_soggettario", "SOGG");
		_TABELLADECODIFICA.put("Tb_soggetto.cd_soggettario", "SOGG");
		_TABELLADECODIFICA.put("Tb_musica.cd_stesura", "STES");
		_TABELLADECODIFICA.put("Tr_tit_aut.cd_strumento_mus", "ORGA");
		_TABELLADECODIFICA.put("Tb_grafica.cd_supporto_fisico", "SUFI");
		_TABELLADECODIFICA.put("Tb_grafica.cd_supporto", "SUFG");
		_TABELLADECODIFICA.put("Tb_grafica.cd_tecnica_dis_1", "TECD");
		_TABELLADECODIFICA.put("Tb_grafica.cd_tecnica_dis_2", "TECD");
		_TABELLADECODIFICA.put("Tb_grafica.cd_tecnica_dis_3", "TECD");
		_TABELLADECODIFICA.put("Tb_cartografia.cd_tecnica", "TECN");
		_TABELLADECODIFICA.put("Tb_grafica.cd_tecnica_sta_1", "TECS");
		_TABELLADECODIFICA.put("Tb_grafica.cd_tecnica_sta_2", "TECS");
		_TABELLADECODIFICA.put("Tb_grafica.cd_tecnica_sta_3", "TECS");
		_TABELLADECODIFICA.put("Tb_musica.tp_testo_letter", "TESL");
		_TABELLADECODIFICA.put("Tb_personaggio.cd_timbro_vocale", "ORGA");
		_TABELLADECODIFICA.put("Tb_composizione.cd_tonalita", "TONO");
		_TABELLADECODIFICA.put("Tb_incipit.cd_tonalita", "TONO");
		_TABELLADECODIFICA.put("Tr_tit_tit.cd_natura_base", "LICR");
		_TABELLADECODIFICA.put("Tr_tit_tit.tp_legame", "LICR");
		_TABELLADECODIFICA.put("Tr_tit_tit.cd_natura_coll", "LICR");
		_TABELLADECODIFICA.put("Tb_par_aut.cd_par_auth", "TPAU");
		_TABELLADECODIFICA.put("Tb_parametro.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_parametro.tp_ret_doc", "TIRE");
		_TABELLADECODIFICA.put("Tb_parametro.cd_liv_ade", "ADES");
		_TABELLADECODIFICA.put("Tb_parametro.tp_all_pref", "ALLI");
		_TABELLADECODIFICA.put("Tb_par_mat.cd_contr_sim", "SIMD");
		_TABELLADECODIFICA.put("Tb_par_mat.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_par_aut.cd_contr_sim", "SIMA");
		_TABELLADECODIFICA.put("Tb_par_aut.cd_livello", "LIVE");
		_TABELLADECODIFICA.put("Tb_termine_thesauro.cd_the", "STHE");
		_TABELLADECODIFICA.put("Tr_termini_termini.tipo_coll", "STLT");
		_TABELLADECODIFICA.put("Tb_termine_thesauro.tipo_coll", "STLT");
	}

	@SuppressWarnings("unchecked")
	private void initialize(boolean force) {

		w.lock();
		try {
			if (!force && initialized.get())
				return;

			initialized.set(false);

			BasicTableDao dao = new BasicTableDao();
			Session session = dao.getSession();

			this.CODICI_ELENCO.clear();

			dao.beginTransaction();

			Criteria c = session.createCriteria(Tb_codici.class);
			c.addOrder(Order.asc("TP_TABELLA")).addOrder(Order.asc("CD_TABELLA"));
			List<Tb_codici> rows = c.list();

			Map<String, List<Tb_codici>> tabelle =
				Stream.of(rows).collect(Collectors.<Tb_codici, String>groupingBy(groupByTipoTabella()));

			List<Tb_codici> tipiTab = tabelle.get("0000");

			for (Tb_codici tabella : tipiTab) {
				String cdTabella = ValidazioneDati.trimOrEmpty(tabella.getCD_TABELLA());
				List<Tb_codici> codici = tabelle.get(cdTabella);
				//almaviva5_20180426 #6576 fix per caricamento tabelle vuote
				this.CODICI_ELENCO.put(cdTabella, ValidazioneDati.coalesce(codici, Collections.EMPTY_LIST ) );
			}

			dao.commitTransaction();
			dao.closeSession();
			log.info("tabella codici caricata");

			//almaviva5_20091001 aggiunto caricamento stop list
			ElencoArticoli.getInstance().init();
			ElencoParole.getInstance().init();
			ElencoStopList.getInstance().init();
			ElencoForme.getInstance().init();
			ElencoFormeU.getInstance().init();
			ElencoPoli.getInstance().init();
			ElencoVociAutori.getInstance().init();
			log.info("Elenchi caricati");

			//almaviva5_20131211
			parseErrorXML();
			log.info("XML errori caricato");

			initialized.set(true);

		} catch (InfrastructureException e) {
			log.error("", e);
			initialized.set(false);
		} catch (Exception e) {
			log.error("", e);
			initialized.set(false);
		} finally {
			w.unlock();
		}
	}

	private void parseErrorXML() {
		Class<Decodificatore> clazz = Decodificatore.class;
		InputStream schema = clazz.getResourceAsStream(_XSD_ERRORS_PATH);
		InputSource errori = new InputSource(clazz.getResourceAsStream(_XML_ERRORS_PATH));
		errori.setSystemId(_XML_ERRORS_PATH);

		_ERRORI = new Errori();
		PoloXMLParser pxp = new PoloXMLParser(schema, new ErroriContentHandler(_ERRORI), new DefaultHandler());

		pxp.parse(errori);
	}

	protected static final Decodificatore getInstance()	throws InfrastructureException {
		return instance;
	}

	protected List<Tb_codici> getCodiciElenco(String key) {
		r.lock();
		try {
			List<Tb_codici> results = Collections.emptyList();
			if (this.CODICI_ELENCO.containsKey(key))
				results = this.CODICI_ELENCO.get(key);

			return results;
		} finally {
			r.unlock();
		}

	}

	protected String getTpTabella(String tabella, String campo) {
		r.lock();
		try {
			String tp_tabella = null;
			if (this._TABELLADECODIFICA.containsKey(tabella + "." + campo))
				tp_tabella = this._TABELLADECODIFICA.get(tabella + "."	+ campo);

			if (tp_tabella != null)
				return tp_tabella.trim();

			return tp_tabella;
		} finally {
			r.unlock();
		}
	}

	// METODI static finalI PER L'ACCESSO AL DECODIFICATORE DALL'ESTERNO
	/*
	 * dato il nome della tabella, il campo e il codice tabella ("MIN")
	 * restituisce un solo oggetto di tipo infoDecodifica
	 *
	 * questo è da modificare facendo restituire una stringa in output - avendo
	 * a disposizione tabella.campo determinare il contesto:
	 * getTp_tabella(tabella,campo) - nel contesto trovato determinare il valore
	 * di cd_tabella es getDescrizione("Tb_Luogo","cd_livello","MIN")
	 */

	protected String getDs_tabellaFromUnimarcInternal(String tp_tabella, String cd_unimarc) {

		if (!this.initialized.get())
			initialize(false);

		r.lock();
		try {
			String resultString = null;
			List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
			int size = ValidazioneDati.size(results);
			for (int index = 0; index < size; index++) {
				Tb_codici tb_codice = results.get(index);
				String temp = tb_codice.getCD_UNIMARC();
				if (temp != null && cd_unimarc.equals(temp.trim())) {
					resultString = temp;
					break;
				}

			}
			if (resultString != null)
				return resultString.trim();

			return resultString;
		} finally {
			r.unlock();
		}
	}

	protected Errore getErroreInternal(int idErrore) {

		if (!this.initialized.get())
			initialize(false);

		r.lock();
		try {
			return _ERRORI.leggiErrore(idErrore);
		} finally {
			r.unlock();
		}
	}

	public static final String getDs_tabellaFromUnimarc(String tp_tabella, String cd_unimarc) {
		String resultString = null;
		Decodificatore instance;

		try {
			instance = Decodificatore.getInstance();
			resultString = instance.getDs_tabellaFromUnimarcInternal(tp_tabella, cd_unimarc);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;

	}

	public static final String getDs_tabellaFromUnimarc(String tabella, String campo, String cd_unimarc) {
		Decodificatore instance;
		String result = null;
		String tp_tabella = null;
		try {
			instance = Decodificatore.getInstance();
			if ((tp_tabella = instance.getTpTabella(tabella, campo)) != null)
				result = instance.getDs_tabellaFromUnimarcInternal(tp_tabella, cd_unimarc);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (result != null)
			return result.trim();

		return result;
	}

	/**
	 * INPUT: nome_tabella, campo, codice il codice è quello che si vuole
	 * decodificare es. getDescrizione("tb_luogo","cd_livello","51") restituisce
	 * come output "MIN" che costituisce la decodifica del livello 51
	 */
	protected String getDescrizioneInternal(String tabella, String campo, String cd_tabella) {

		if (!this.initialized.get())
			initialize(false);

		String tp_tabella;
		String resultString = null;

		r.lock();
		try {
			if ((tp_tabella = this.getTpTabella(tabella, campo)) != null) {
				List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
				int size = ValidazioneDati.size(results);
				for (int index = 0; index < size; index++) {
					Tb_codici tb_codice = results.get(index);
					String temp = tb_codice.getCD_TABELLA();
					if (temp != null && cd_tabella.equals(temp.trim())) {
						resultString = temp;
						break;
					}
				}
			}
		} finally {
			r.unlock();
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	protected String convertUnimarcToSbnInternal(String tp_tabella, String cd_unimarc) {

		if (!this.initialized.get())
			initialize(false);

		r.lock();
		String resultString = null;
		try {
			List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
			int size = ValidazioneDati.size(results);
			for (int index = 0; index < size; index++) {
				Tb_codici tb_codice = results.get(index);
				String temp = tb_codice.getCD_UNIMARC();
				if (temp != null && cd_unimarc.equals(temp.trim())) {
					resultString = tb_codice.getCD_TABELLA();
					break;
				}

			}
		} finally {
			r.unlock();
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	public static final String convertUnimarcToSbn(String tp_tabella, String cd_unimarc) {
		String resultString = null;
		Decodificatore instance;
		try {
			instance = Decodificatore.getInstance();
			resultString = instance.convertUnimarcToSbnInternal(tp_tabella,	cd_unimarc);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	/**
	 * INPUT: nome_tabella, campo, cd_tabella
	 */
	public static final String getCd_unimarc(String tabella, String campo, String cd_tabella) {
		String tp_tabella;
		String resultString = null;
		Decodificatore instance;
		try {
			instance = Decodificatore.getInstance();
			if ((tp_tabella = instance.getTpTabella(tabella, campo)) != null)
				resultString = instance.getCd_unimarcInternal(tp_tabella, cd_tabella);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	public static final String getCd_unimarc(String tp_tabella, String cd_tabella) {
		String resultString = null;
		Decodificatore instance;
		try {
			instance = Decodificatore.getInstance();
			resultString = instance.getCd_unimarcInternal(tp_tabella, cd_tabella);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	/**
	 * INPUT: tp_tabella, cd_tabella
	 */
	protected String getCd_unimarcInternal(String tp_tabella, String cd_tabella) {

		if (!this.initialized.get())
			initialize(false);

		String resultString = null;
		if (cd_tabella == null)
			return null;

		r.lock();
		try {
			cd_tabella = cd_tabella.trim();
			List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
			int size = ValidazioneDati.size(results);
			for (int index = 0; index < size; index++) {
				Tb_codici tb_codice = results.get(index);
				String temp = tb_codice.getCD_TABELLA();
				if (temp != null && cd_tabella.equals(temp.trim())) {
					resultString = tb_codice.getCD_UNIMARC();
					break;
				}
			}

		} finally {
			r.unlock();
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	protected String getTp_materiale(String tabella, String campo, String cd_tabella) {

		if (!this.initialized.get())
			initialize(false);

		String tp_tabella;
		String resultString = null;
		r.lock();
		try {
			if ((tp_tabella = this.getTpTabella(tabella, campo)) != null) {
				List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
				int size = ValidazioneDati.size(results);
				for (int index = 0; index < size; index++) {
					Tb_codici tb_codice = results.get(index);
					String temp = tb_codice.getCD_TABELLA();
					if (temp != null && cd_tabella.equals(temp.trim())) {
						resultString = tb_codice.getTP_MATERIALE();
						break;
					}
				}
			}
		} finally {
			r.unlock();
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	/**
	 * Metodo per ottenere la descrizione dei codici es
	 * getDs_tabella("Tb_luogo","cd_livello","97") --> "AUF"
	 * getDs_tabella("Tb_luogo","cd_livello","AUF") --> "97"
	 */
	public static final String getDs_tabella(String tabella, String campo,
			String cd_tabella) {
		String tp_tabella;
		String resultString = null;
		Decodificatore instance;

		try {
			instance = Decodificatore.getInstance();
			if ((tp_tabella = instance.getTpTabella(tabella, campo)) != null)
				resultString = instance.getDs_tabellaInternal(tp_tabella, cd_tabella);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	public static final String getDs_tabella(String tp_tabella, String cd_tabella) {
		String resultString = null;
		Decodificatore instance;
		try {
			instance = Decodificatore.getInstance();
			resultString = instance.getDs_tabellaInternal(tp_tabella, cd_tabella);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	protected String getDs_tabellaInternal(String tp_tabella, String cd_tabella) {

		if (!this.initialized.get())
			initialize(false);

		String resultString = null;
		if (cd_tabella == null)
			return null;

		r.lock();
		try {
			cd_tabella = cd_tabella.trim();
			List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
			int size = ValidazioneDati.size(results);
			for (int index = 0; index < size; index++) {
				Tb_codici tb_codice = results.get(index);
				String temp = tb_codice.getCD_TABELLA();
				if (temp != null && cd_tabella.equals(temp.trim())) {
					resultString = tb_codice.getDS_TABELLA();
					break;
				}
			}
		} finally {
			r.unlock();
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	/**
	 * INPUT: nome_tabella, campo, cd_unimarc
	 */
	public static final Tb_codici getTb_codici(String tp_tabella, String cd_tabella) {
		Decodificatore instance;
		try {
			instance = Decodificatore.getInstance();
			Tb_codici resultString = instance.getTb_codiciInternal(tp_tabella, cd_tabella);
			return resultString;

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		return null;
	}

	/**
	 * INPUT: nome_tabella, campo, cd_unimarc
	 */
	public static final String getCd_tabella(String tp_tabella, String cd_tabella) {
		String resultString = null;
		Decodificatore instance;

		try {
			instance = Decodificatore.getInstance();
			resultString = instance.getCd_tabellaInternal(tp_tabella, cd_tabella);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	public static final String getCd_tabella(String tabella, String campo, String cd_unimarc) {
		String tp_tabella;
		String resultString = null;
		Decodificatore instance;

		try {
			instance = Decodificatore.getInstance();
			if ((tp_tabella = instance.getTpTabella(tabella, campo)) != null)
				resultString = instance.getCd_tabellaInternal(tp_tabella, cd_unimarc);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	/**
	 * INPUT: nome_tabella, campo, cd_unimarc, tp_materiale
	 */
	public static final String getCd_tabella(String tabella, String campo, String cd_unimarc, String tp_materiale) {
		Decodificatore instance;
		String resultString = null;
		try {
			instance = Decodificatore.getInstance();
			resultString = instance.getCd_tabellaInternal(tabella, campo, cd_unimarc, tp_materiale);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

    // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
	// Inizio Adeguamenti alla nuova gestione dei controlli fatta in Indice
    //MANTIS 2403
	public static SbnData getDt_finevalidita(String tp_tabella, String cd_tabella) {
		Tb_codici cod = getTb_codici(tp_tabella, cd_tabella);
		if (cod != null)
			return new SbnData(cod.getDT_FINE_VALIDITA());

		return null;
	}

	protected String getCd_tabellaInternal(String tabella, String campo, String cd_unimarc, String tp_materiale) {

		if (!this.initialized.get())
			initialize(false);

		String tp_tabella;
		String resultString = null;
		r.lock();
		try {
			if ((tp_tabella = this.getTpTabella(tabella, campo)) != null) {
				tp_tabella = tp_tabella.trim();
				List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
				int size = ValidazioneDati.size(results);
				for (int index = 0; index < size; index++) {
					Tb_codici tb_codice = results.get(index);
					String temp = tb_codice.getCD_UNIMARC();
					if (temp != null
							&& tb_codice.getTP_MATERIALE() != null
							&& cd_unimarc.equals(temp.trim())
							&& tp_materiale.equals(tb_codice.getTP_MATERIALE().trim())) {
						resultString = tb_codice.getCD_TABELLA();
						break;
					}
				}
			}
		} finally {
			r.unlock();
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	/**
	 * INPUT: tp_tabella, cd_unimarc
	 */
	protected String getCd_tabellaInternal(String tp_tabella, String cd_unimarc) {

		if (!this.initialized.get())
			initialize(false);

		String resultString = null;
		if (!ValidazioneDati.isFilled(tp_tabella) || !ValidazioneDati.isFilled(cd_unimarc) )
			return null;

		r.lock();
		try {
			tp_tabella = tp_tabella.trim();
			cd_unimarc = cd_unimarc.trim();
			List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
			int size = ValidazioneDati.size(results);
			for (int index = 0; index < size; index++) {
				Tb_codici tb_codice = results.get(index);
				String temp = tb_codice.getCD_UNIMARC();
				if (temp != null && cd_unimarc.equals(temp.trim())) {
					resultString = tb_codice.getCD_TABELLA();
					break;
				}
			}
		} finally {
			r.unlock();
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	/**
	 * INPUT: tp_tabella, cd_unimarc
	 */
	protected Tb_codici getTb_codiciInternal(String tp_tabella,
			String cd_unimarc) {

		if (!this.initialized.get())
			initialize(false);

		if (tp_tabella == null || cd_unimarc == null)
			return null;

		r.lock();
		try {
			tp_tabella = tp_tabella.trim();
			List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
			int size = ValidazioneDati.size(results);
			for (int index = 0; index < size; index++) {
				Tb_codici tb_codice = results.get(index);
				String temp = tb_codice.getCD_UNIMARC();
				if (temp != null && cd_unimarc.equals(temp.trim()))
					return tb_codice;
			}
			return null;

		} finally {
			r.unlock();
		}
	}

	    public static String getCd_tabellaDaDescrizione(String tp_tabella, String ds_tabella) {
	        String resultString = null;
	        Decodificatore instance;

	        try {
	              instance = Decodificatore.getInstance();
	              resultString = instance.getCd_tabellaDaDescrizioneInternal(tp_tabella, ds_tabella);
	        } catch (InfrastructureException e) {
	              log.error("", e);
	        }

	        if (resultString != null)
	              return resultString.trim();

	        return resultString;
	  }



	    protected String getCd_tabellaDaDescrizioneInternal(String tp_tabella, String ds_tabella) {


		if (!this.initialized.get())
			initialize(false);

		String resultString = null;
		if (tp_tabella == null || ds_tabella == null)
			return null;

		r.lock();
		try {
			tp_tabella = tp_tabella.trim();
			List<Tb_codici> results = this.getCodiciElenco(tp_tabella);
			int size = ValidazioneDati.size(results);
			for (int index = 0; index < size; index++) {
				Tb_codici tb_codice = results.get(index);
				String temp = tb_codice.getDS_TABELLA();
				if (temp != null && ds_tabella.equals(temp.trim())) {
					resultString = tb_codice.getCD_TABELLA();
					break;
				}
			}
		} finally {
			r.unlock();
		}

		if (resultString != null)
			return resultString.trim();

		return resultString;
	}

	/**
	 * Ottiene il livello di soglia del cd_livello per le ricerche nel db a
	 * partire da un dato valore. Serve perchè nel db non ci sono i dati puliti
	 * sul db.
	 *
	 * @param cd_livello
	 * @return
	 */
	public static final String livelloSogliaDa(String cd_livello) {
		int livello = Integer.parseInt(cd_livello);
		if (livello == 1)
			return "01";
		if (livello == 2)
			return "02";
		if (livello == 3)
			return "03";
		if (livello == 4)
			return "04";
		if (livello == 5)
			return "00";
		else if (livello <= 51)
			return "06";
		else if (livello <= 71)
			return "52";
		else if (livello <= 90)
			return "72";
		else if (livello <= 95)
			return "91";

		return "96";
	}

	/**
	 * Ottiene il livello di soglia del cd_livello
	 *
	 * @param cd_livello
	 * @return
	 */
	public static final String livelloSoglia(String cd_livello) {
		try {
			int livello = Integer.parseInt(cd_livello);
			if (livello == 1)
				return "01";
			if (livello == 2)
				return "02";
			if (livello == 3)
				return "03";
			if (livello == 4)
				return "04";
			else if ((livello == 5) || (livello == 96) || (livello == 97))
				return cd_livello;
			else if ((livello > 5) && (livello <= 51))
				return "51";
			else if ((livello > 51) && (livello <= 71))
				return "71";
			else if ((livello > 71) && (livello <= 90))
				return "90";
			else if ((livello > 90) && (livello <= 95))
				return "95";
		} catch (Exception e) {
			log.error("cd_livello errato: " + cd_livello);
		}
		return "05";
	}

	public static final void reload() {
		Decodificatore instance;
		try {
			log.debug("Richiesto aggiornamento tabella codici");
			instance = Decodificatore.getInstance();
			instance.initialize(true);
		} catch (InfrastructureException e) {
			log.error("", e);
		}

	}

	public static final void clear() {
		Decodificatore instance;
		try {
			log.debug("Richiesto aggiornamento tabella codici");
			instance = Decodificatore.getInstance();
			instance.initialized.set(false);
		} catch (InfrastructureException e) {
			log.error("", e);
		}

	}

	public static final Errore getErrore(int iD) {
		Decodificatore instance;
		try {
			instance = Decodificatore.getInstance();
			return instance.getErroreInternal(iD);
		} catch (InfrastructureException e) {
			log.error("", e);
		}
		return null;
	}

	public static final List<Tb_codici> getCodici(String tp_tabella) {
		Decodificatore instance;
		try {
			instance = Decodificatore.getInstance();
			return instance.getCodiciElenco(tp_tabella);

		} catch (InfrastructureException e) {
			log.error("", e);
		}

		return null;
	}

}
