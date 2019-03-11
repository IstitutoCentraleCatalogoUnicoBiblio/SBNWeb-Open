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
package it.iccu.sbn.batch.unimarc;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.NormalizzaRangeCollocazioni;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiciNormalizzatiVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoOutput;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.TabellaNumSTDImpronteVO;
import it.iccu.sbn.ejb.vo.gestionestampe.BidInventarioSegnaturaVO;
import it.iccu.sbn.persistence.dao.common.AbstractJDBCManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.first;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.leggiXID;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrNull;

public class SbnUnimarcBIDExtractor extends AbstractJDBCManager {


	private enum TipoElaborazioneUnimarc {
		TOTALE,
		TOTALE_PER_BIBLIOTECA,
		TOTALE_PER_RANGE_COLLOCAZIONI,
		TOTALE_PER_RANGE_INVENTARI,
		INCREMENTALE,
		INCREMENTALE_PER_BIBLIOTECA,
		INCREMENTALE_PER_RANGE_COLLOCAZIONI,
		INCREMENTALE_PER_RANGE_INVENTARI;
	}

	private final EsportaVO esporta;
	private final Logger log;
	private final Properties props;
	private final TipoElaborazioneUnimarc tipoElaborazione;

	private static final char BLANK = (char)32;
	private static final String WHERE_START = "where true";

	private static final int FLAG_NULL = 0x0;
	private static final int FLAG_INCREMENTALE = 0x1;
	private static final int FLAG_IGNORA_IDENTIFICATIVI = 0x2;

	private static final MultiKeyMap tipoElabMap = new MultiKeyMap();

	private Connection connection;
	private int flags;

	static {
		// La mappa qui definita identifica i possibili criteri d'estrazione
		// incrociando 4 flag booleani:
		// 1. scarico incrementale			true/false
		// 2. scarico per posseduto			true/false
		// 3. scarico range collocazioni	true/false
		// 4. scarico range inventari		true/false
		tipoElabMap.put(false, false, false, false, TipoElaborazioneUnimarc.TOTALE);
		tipoElabMap.put(false, true, false, false, TipoElaborazioneUnimarc.TOTALE_PER_BIBLIOTECA);
		tipoElabMap.put(false, true, true, false, TipoElaborazioneUnimarc.TOTALE_PER_RANGE_COLLOCAZIONI);
		tipoElabMap.put(false, true, false, true, TipoElaborazioneUnimarc.TOTALE_PER_RANGE_INVENTARI);

		tipoElabMap.put(true, false, false, false, TipoElaborazioneUnimarc.INCREMENTALE);
		tipoElabMap.put(true, true, false, false, TipoElaborazioneUnimarc.INCREMENTALE_PER_BIBLIOTECA);
		tipoElabMap.put(true, true, true, false, TipoElaborazioneUnimarc.INCREMENTALE_PER_RANGE_COLLOCAZIONI);
		tipoElabMap.put(true, true, false, true, TipoElaborazioneUnimarc.INCREMENTALE_PER_RANGE_INVENTARI);
	}



	public SbnUnimarcBIDExtractor(EsportaVO richiesta, BatchLogWriter blw) throws Exception {
		this.esporta = richiesta;
		this.log = blw.getLogger();

		log.debug("Creazione istanza extractor");
		InputStream resource = null;

		String config = CommonConfiguration.getProperty(Configuration.SBNWEB_EXPORT_CONFIG_FILE);
		if (config == null)
			resource = this.getClass().getResourceAsStream("SbnUnimarcBIDExtractor.properties");
		else
			resource = new FileInputStream(config);

		this.props = new Properties();
		this.props.load(resource);

		//determino tipo elaborazione
		tipoElaborazione = (TipoElaborazioneUnimarc) tipoElabMap.get(richiesta.isIncrementale(),
			richiesta.isSoloPosseduto(),
			richiesta.isRangeCollocazioni(),
			richiesta.isRangeInventari());

		//almaviva5_20120911
		if (esporta.isIncrementale()) {
			StringBuilder buf = new StringBuilder(1024);
			buf.append("estremi estrazione incrementale: '");
			buf.append(richiesta.getTsVar_da()).append("' --> '");
			buf.append(richiesta.getTsVar_a()).append("'");
			log.debug(buf.toString() );

			flags = flags | FLAG_INCREMENTALE;
		}
	}


	private void aggiungiFiltroDescTitolo(StringBuilder sql, EsportaVO richiesta) {
		String descTitoloDa = richiesta.getDescTitoloDa();
		if (!isFilled(descTitoloDa))
			return;

		String descTitoloA = richiesta.getDescTitoloA();
		if (!isFilled(descTitoloA))
			descTitoloA = descTitoloDa;

		// Intervento interno almaviva2 11.05.2012 le chiavi di ricerca devono essere maiuscolizzate altrimenti
		// la ricerca non restituisce nulla;
		descTitoloDa = descTitoloDa.toUpperCase();
		descTitoloA = descTitoloA.toUpperCase();

		String clet1Da = "";
		String clet1A = "";
		String clet2Da = "";
		String clet2A = "";

		log.debug("aggiungi Filtro Descrizione del Titolo");
		if (descTitoloDa.length() <= 6) {
			clet1Da = descTitoloDa;
		} else {
			clet1Da = descTitoloDa.substring(0,6);
			clet2Da = descTitoloDa.substring(6);
		}

		if (descTitoloA.length() <= 6) {
			clet1A = descTitoloA;
		} else {
			clet1A = descTitoloA.substring(0,6);
			clet2A = descTitoloA.substring(6);
		}


		sql.append(" and (t.ky_cles1_t >= '").append(clet1Da).append("' and t.ky_cles1_t <= '").append(clet1A).append("'");

		if (!isFilled(clet2Da) && !isFilled(clet2A)) {
			sql.append(")");
		} else if (isFilled(clet2Da) && !isFilled(clet2A)) {
			sql.append(" and t.ky_cles2_t >= '").append(clet2Da).append("')");
		} else if (!isFilled(clet2Da) && isFilled(clet2A)) {
			sql.append(" and t.ky_cles2_t <= '").append(clet2A).append("')");
		} else if (isFilled(clet2Da) && isFilled(clet2A)) {
			sql.append(" and t.ky_cles2_t >= '").append(clet2Da).append("' and t.ky_cles2_t <= '").append(clet2A).append("')");
		}
	}



	private void aggiungiFiltroDescAutore(StringBuilder sqlAutore, StringBuilder sqlTitolo, EsportaVO richiesta) {

		String descAutoreDa = richiesta.getCatalogoSelezDa();
		if (!isFilled(descAutoreDa))
			return;
		String descAutoreA = richiesta.getCatalogoSelezA();
		if (!isFilled(descAutoreA))
			descAutoreA = descAutoreDa;

		// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
		// si utilizza la select di  per rendere universale la codifica della chiave impostata dal bibliotecario
//		descAutoreDa = "translate(upper (TRIM( translate ( '" + descAutoreDa + "' ,'àèéìòù,*?<>','aeeiou'))),E'\\x27','')";
//		descAutoreA = "translate(upper (TRIM( translate ( '" + descAutoreA + "' ,'àèéìòù,*?<>','aeeiou'))),E'\\x27','')";
		// La select è stata ateriscata perchè tutti i controlli sono stati spostati nella EsportaAction

		descAutoreDa = "'" + descAutoreDa + "'";
		descAutoreA = "'" + descAutoreA + "'";

		log.debug("aggiungi Filtro Descrizione dello Autore/Titolo");
		sqlAutore.append(" and (a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') >= "+ descAutoreDa +
								" and a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') <= "+ descAutoreA +")");

		sqlTitolo.append(" and ta isnull");
		sqlTitolo.append(" and (t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') >= "+ descAutoreDa +
								" and t.ky_cles1_t||COALESCE(t.ky_cles2_t, '')  <= "+ descAutoreA +")");
	}


	// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
	private void aggiungiFiltroDescTipografo(StringBuilder sqlAutore, StringBuilder sqlTitolo, EsportaVO richiesta) {

		String descAutoreDa = richiesta.getCatalogoSelezDa();
		if (!isFilled(descAutoreDa))
			return;
		String descAutoreA = richiesta.getCatalogoSelezA();
		if (!isFilled(descAutoreA))
			descAutoreA = descAutoreDa;

		descAutoreDa = "'" + descAutoreDa + "'";
		descAutoreA = "'" + descAutoreA + "'";

		log.debug("aggiungi Filtro Descrizione dello Autore/Titolo");
		sqlAutore.append(" and (a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') >= "+ descAutoreDa +
								" and a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') <= "+ descAutoreA +")");

		sqlTitolo.append(" and ta isnull");
		sqlTitolo.append(" and (t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') >= "+ descAutoreDa +
								" and t.ky_cles1_t||COALESCE(t.ky_cles2_t, '')  <= "+ descAutoreA +")");
	}


	// Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
	private void aggiungiFiltroDescSoggetto(StringBuilder sqlSoggetto, StringBuilder sqlSecondaria, EsportaVO richiesta) {

		String descSoggettoDa = richiesta.getCatalogoSelezDa();
		if (!isFilled(descSoggettoDa))
			return;
		String descSoggettoA = richiesta.getCatalogoSelezA();
		if (!isFilled(descSoggettoA))
			descSoggettoA = descSoggettoDa;

		descSoggettoDa = "'" + descSoggettoDa + "'";
		descSoggettoA = "'" + descSoggettoA + "'";

		log.debug("aggiungi Filtro Descrizione del Soggetto/Autore/Titolo");
		sqlSoggetto.append(" and (s.ky_cles1_s||COALESCE(s.ky_cles2_s, '') >= "+ descSoggettoDa +
				" and s.ky_cles1_s||COALESCE(s.ky_cles2_s, '') <= "+ descSoggettoA +")" );

		sqlSecondaria.append(" and ta isnull");
		sqlSecondaria.append(" and (s.ky_cles1_s||COALESCE(s.ky_cles2_s, '') >= "+ descSoggettoDa +
				" and s.ky_cles1_s||COALESCE(s.ky_cles2_s, '') <= "+ descSoggettoA +")");

	}

	// Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
	private void aggiungiFiltroDescClasse(StringBuilder sqlClasse, StringBuilder sqlSecondaria, EsportaVO richiesta) {

		log.debug("aggiungi Filtro tipo Classe poi le descrizione della Classe/Autore/Titolo solo se valorizzato");
		sqlClasse.append(" and (tc.cd_sistema = case when 'D'='D' then 'D'||tc.cd_edizione else 'D' end)");

		sqlSecondaria.append(" and ta isnull");
		sqlSecondaria.append(" and (tc.cd_sistema = case when 'D'='D' then 'D'||tc.cd_edizione else 'D' end)");

		String descClasseDa = richiesta.getCatalogoSelezDa();
		if (!isFilled(descClasseDa))
			return;
		String descClasseA = richiesta.getCatalogoSelezA();
		if (!isFilled(descClasseA))
			descClasseA = descClasseDa;

		descClasseDa = "'" + descClasseDa + "'";
		descClasseA = "'" + descClasseA + "'";

		sqlClasse.append(" and (tc.classe >= "+ descClasseDa + " and tc.classe <= "+ descClasseA +")" );

		sqlSecondaria.append(" and (tc.classe >= "+ descClasseDa + " and tc.classe <= "+ descClasseA +")");

	}


	// Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
	private void aggiungiFiltroDescEditore(StringBuilder sqlEditore, StringBuilder sqlSecondaria, EsportaVO richiesta) {
		String descEditoreDa = richiesta.getCatalogoSelezDa();
		if (!isFilled(descEditoreDa))
			return;

		String descEditoreA = richiesta.getCatalogoSelezA();
		if (!isFilled(descEditoreA))
			descEditoreA = descEditoreDa;

		descEditoreDa = "'" + descEditoreDa + "'";
		descEditoreA = "'" + descEditoreA + "'";

		log.debug("aggiungi Filtro Descrizione dell'Editore/Autore/Titolo");
		sqlEditore.append(" and (vCatEdit.chiave_for >= " + descEditoreDa + " and vCatEdit.chiave_for <= " + descEditoreA +")");

		sqlSecondaria.append(" and ta isnull");
		sqlSecondaria.append(" and (vCatEdit.chiave_for >= " + descEditoreDa + " and vCatEdit.chiave_for <= " + descEditoreA +")");

	}

	// Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
	private void aggiungiFiltroDescPossessore(StringBuilder sqlEditore, StringBuilder sqlSecondaria, EsportaVO richiesta) {
		sqlSecondaria.append(" and ta isnull");
	}


	private void aggiungiFiltroSpecificita(StringBuilder sql, EsportaVO richiesta, String target) {
		String[] materiali = richiesta.getMateriali();
		if (!isFilled(materiali))
			return;

		log.debug("aggiungi Filtro Specificita");

		String in = buildINClause(target, Arrays.asList(materiali));
		if (in != null)
			sql.append(" and ").append(in);
	}

	private void aggiungiFiltroTipoRecord(StringBuilder sql, EsportaVO richiesta, String target) {

		List<String> tipoRecord = new ArrayList<String>();
		if (isFilled(richiesta.getTipoRecord1()) )
			tipoRecord.add(richiesta.getTipoRecord1() );
		if (isFilled(richiesta.getTipoRecord2()) )
			tipoRecord.add(richiesta.getTipoRecord2() );
		if (isFilled(richiesta.getTipoRecord3()) )
			tipoRecord.add(richiesta.getTipoRecord3() );

		if (!isFilled(tipoRecord))
			return;

		log.debug("aggiungi Filtro Tipo Record");

		String in = buildINClause(target, tipoRecord);
		if (in != null)
			sql.append(" and ").append(in);
	}

	private void aggiungiFiltroLingua(StringBuilder sql, EsportaVO richiesta, String... targets) {

		// Modifiche per Estrattore Magno almaviva2 marzo 2011
		if (richiesta.isSoloLinguaNoItaliano()) {
			log.debug("aggiungi Filtro Lingua Non Italiana");
			sql.append(" and t.cd_lingua_1!='ITA'");
			return;
		}

		List<String> tipoLingua = new ArrayList<String>();
		if (isFilled(richiesta.getLingua1()) )
			tipoLingua.add(richiesta.getLingua1() );
		if (isFilled(richiesta.getLingua2()) )
			tipoLingua.add(richiesta.getLingua2() );
		if (isFilled(richiesta.getLingua3()) )
			tipoLingua.add(richiesta.getLingua3() );

		if (!isFilled(tipoLingua))
			return;

		log.debug("aggiungi Filtro Lingua");

		Iterator<String> t = Arrays.asList(targets).iterator();
		sql.append(" and (");
		for (;;) {
			String target = t.next();
			String in = buildINClause(target, tipoLingua);
			sql.append(in);
			if (t.hasNext())
				sql.append(" or ");
			else
				break;
		}
		sql.append(" )");
	}

	private void aggiungiFiltroPaese(StringBuilder sql, EsportaVO richiesta, String target) {

		// Modifiche per Estrattore Magno almaviva2 marzo 2011
		if (richiesta.isSoloPaeseNoItalia()) {
			log.debug("aggiungi Filtro Paese Non Italia");
			sql.append(" and t.cd_paese!='IT'");
			return;
		}

		List<String> tipoPaese = new ArrayList<String>();
		if (isFilled(richiesta.getPaese1()) )
			tipoPaese.add(richiesta.getPaese1() );
		if (isFilled(richiesta.getPaese2()) )
			tipoPaese.add(richiesta.getPaese2() );

		if (!isFilled(tipoPaese))
			return;

		log.debug("aggiungi Filtro Paese");

		String in = buildINClause(target, tipoPaese);
		if (in != null)
			sql.append(" and ").append(in);
	}

	private void aggiungiFiltroNature(StringBuilder sql, EsportaVO richiesta, String target) {
		String[] nature = richiesta.getNature();
		if (!isFilled(nature))
			return;

		log.debug("aggiungi Filtro Nature");

		List<String> listaNat = new ArrayList<String>(Arrays.asList(nature));

		if (esporta.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI)) {
			// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
			// per la stampa cataloghi le nature W non devono essere estratte come radici ma solo come contenute nel reticolo
		} else {
			if (listaNat.contains("M") && !listaNat.contains("W") )	// le monografie includono anche le nature W
				listaNat.add("W");
		}

		String in = buildINClause(target, listaNat);
		if (in != null)
			sql.append(" and ").append(in);
	}

	private void aggiungiFiltroTsVar(StringBuilder sql, EsportaVO richiesta, String target) {
		String range = buildTimeStampRange(target, richiesta.getTsVar_da(), richiesta.getTsVar_a());
		if (range != null) {
			log.debug("aggiungi Filtro Date Aggiornamento");
			sql.append(" and (").append(range).append(" )");
		}
	}

	private void aggiungiFiltroCondivisione(StringBuilder sql, EsportaVO richiesta) {
		if (richiesta.isSoloDocLocali()) {
			log.debug("aggiungi Filtro Documenti solo Locali");
			sql.append(" and t.fl_condiviso='n'");
		}

		// Modifiche per Estrattore Magno almaviva2 marzo 2011
		if (richiesta.isSoloDocCondivisi()) {
			log.debug("aggiungi Filtro Documenti solo Condivisi");
			sql.append(" and t.fl_condiviso='s'");
		}


	}

	private void aggiungiFiltroLivelloAutorita(StringBuilder sql, EsportaVO richiesta) {
		log.debug("aggiungi Filtro Livello Autorita");
		if (!richiesta.isAncheTitoli01())
			sql.append(" and t.cd_livello>'01'");

	}

	private void aggiungiFiltroTipoData(StringBuilder buf, EsportaVO richiesta) {

		String tipoData = richiesta.getTipoData();

		// Inizio Manutenzione 07.07.2011 almaviva2 - BUG MANTIS 4404 (esercizio)
		// il filtro sulle date in estrazione deve essere applicato anche quando il tipo data non è valorizzato
//		if (ValidazioneDati.strIsNull(tipoData))
//			return;
//		buf.append(" and t.tp_aa_pubb='").append(tipoData).append("'");

		if (isFilled(tipoData))
			buf.append(" and t.tp_aa_pubb='").append(tipoData).append("'");

		// Fine Manutenzione 07.07.2011 almaviva2 - BUG MANTIS 4404 (esercizio)


		String da = richiesta.getAaPubbFrom();
		String a  = richiesta.getAaPubbTo();

		if (isFilled(da) && isFilled(a)) {
			buf.append(" and (t.aa_pubb_1 between '").append(da).append("' and '").append(a).append("')");
			return;
		}

		if (isFilled(da)) {
			buf.append(" and (t.aa_pubb_1>='").append(da).append("')");
			return;
		}

		if (isFilled(a))
			buf.append(" and (t.aa_pubb_1<='").append(a).append("')");

	}


	private void aggiungiFiltroDataTsVar(StringBuilder sql, EsportaVO richiesta) {
		String range = buildTimeStampRange("t.ts_var", richiesta.getDataAggFrom(), richiesta.getDataAggTo());
		if (range != null) {
			log.debug("aggiungi Filtro Date Aggiornamento per Estrazione Bid");
			sql.append(" and (").append(range).append(" )");
		}
	}

	private void aggiungiFiltroDataTsIns(StringBuilder sql, EsportaVO richiesta) {
		String range = buildTimeStampRange("t.ts_ins" , richiesta.getDataInsFrom(), richiesta.getDataInsTo());
		if (range != null) {
			log.debug("aggiungi Filtro Date Inserimento  per Estrazione Bid");
			sql.append(" and (").append(range).append(" )");
		}
	}

	private void aggiungiFiltroUteIns(StringBuilder sql, EsportaVO richiesta) {
		if (isFilled(richiesta.getUteIns()) ) {
			log.debug("aggiungi Filtro Utente Bibliotecario Inserimento  per Estrazione Bid");
			sql.append(" and substring(t.ute_ins,7)='" + richiesta.getUteIns() + "'");
		}
	}

	private void aggiungiFiltroUteVar(StringBuilder sql, EsportaVO richiesta) {
		if (isFilled(richiesta.getUteVar()) ) {
			log.debug("aggiungi Filtro Utente Bibliotecario Variazione  per Estrazione Bid");
			sql.append(" and substring(t.ute_var,7)='" + richiesta.getUteVar() + "'");
		}
	}


	private void createTempTables() throws Exception {

		log.debug("Creazione tabelle appoggio");

		StringBuilder buf = new StringBuilder(1024);
		Connection c = getConnection();
		String tableName = (String) props.get("TABLE_NAME");
		String sql = (String) props.get("CREATE_TABLE");

		buf.append("CREATE TEMP TABLE ");
		buf.append(sql);
		//ATTENZIONE: la clausola ON COMMIT DROP funziona solo per Postgres.
		buf.append(" ON COMMIT DROP");
		Statement st = c.createStatement();
		st.execute(buf.toString());

		//creo l'indice per bid
		buf.setLength(0);
		buf.append("CREATE INDEX ");
		buf.append(tableName);
		buf.append("_idx ON ");
		buf.append(tableName);
		buf.append(" USING btree (\"bid\")");
		st.execute(buf.toString());

		//tabella appoggio bid posseduti
		buf.setLength(0);
		tableName = (String) props.get("POSSEDUTO_TABLE_NAME");
		sql = tableName + " (\"bid\" CHAR(10)  )";

		buf.append("CREATE TEMP TABLE ");
		buf.append(sql);
		//ATTENZIONE: la clausola ON COMMIT DROP funziona solo per Postgres.
		buf.append(" ON COMMIT DROP");
		st.execute(buf.toString());

		//creo l'indice per bid
		buf.setLength(0);
		buf.append("CREATE INDEX ");
		buf.append(tableName);
		buf.append("_idx ON ");
		buf.append(tableName);
		buf.append(" USING btree (\"bid\")");
		st.execute(buf.toString());

		//almaviva5_20120911 tabella appoggio bid ignorati
		buf.setLength(0);
		tableName = (String) props.get("IGNORATI_TABLE_NAME");
		sql = tableName + " (\"bid\" CHAR(10)  )";

		buf.append("CREATE TEMP TABLE ");
		buf.append(sql);
		//ATTENZIONE: la clausola ON COMMIT DROP funziona solo per Postgres.
		buf.append(" ON COMMIT DROP");
		st.execute(buf.toString());

		//creo l'indice per bid
		buf.setLength(0);
		buf.append("CREATE INDEX ");
		buf.append(tableName);
		buf.append("_idx ON ");
		buf.append(tableName);
		buf.append(" USING btree (\"bid\")");
		st.execute(buf.toString());

		close(st);
	}


	private void createTempTablesSort() throws Exception {

		log.debug("Creazione tabelle appoggio per Ordinamento");

		StringBuilder buf = new StringBuilder(1024);
		Connection c = getConnection();
		String tableName = (String) props.get("ORDINATI_TABLE_NAME");
		String sql = tableName + " (\"bid\" CHAR(10)  )";

		buf.append("CREATE TEMP TABLE ");
		buf.append(sql);
		//ATTENZIONE: la clausola ON COMMIT DROP funziona solo per Postgres.
		buf.append(" ON COMMIT DROP");
		Statement st = c.createStatement();
		st.execute(buf.toString());

		//creo l'indice per bid
		buf.setLength(0);
		buf.append("CREATE INDEX ");
		buf.append(tableName);
		buf.append("_idx ON ");
		buf.append(tableName);
		buf.append(" USING btree (\"bid\")");
		st.execute(buf.toString());

		close(st);
	}


	private void fillTempTable(StringBuilder sql) throws Exception {
		Connection c = getConnection();
		String tableName = (String) props.get("TABLE_NAME");
		StringBuilder tmp = new StringBuilder(1024);
		tmp.append("INSERT INTO ").append(tableName).append(" ");
		sql.insert(0, tmp);

		String query = sql.toString();
		log.debug("sql= " + query);
		Statement st = c.createStatement();
		st.execute(query);
		close(st);
	}

	private long unloadTempTable(StringBuilder buf, Writer w) throws Exception {
		Connection c = getConnection();
		PreparedStatement st = null;

		TipoOutput tipoOutput = esporta.getTipoOutput();
		log.debug("Unload tabelle appoggio; tipo output: " + tipoOutput);

		switch (tipoOutput) {
		case BID:
		case BID_DATI:
			st = c.prepareStatement(buf.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			break;
		case BID_POSS:
			// Gestione Bibliografica: Intervento interno Per i Possessori si deve richiedere un tipo output (TipoOutput.BID_POSS)
			// che contenga sia il bid che l'area dell'inventario che quella della descrizione del possessore (area3 della select)
			st = c.prepareStatement(buf.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			break;
		case INV:
			//almaviva5_20130131 segnalazione NAP
			st = prepareStatementInventari(c, first(esporta.getListaBib()) );
			break;
		}

		// attivo cursore server-side
		Integer fetch = Integer.valueOf(props.getProperty("DB_CURSOR_FETCH_SIZE", "1000"));
		log.debug("server-side cursor fetch size: " + fetch);
		st.setFetchSize(fetch);
		ResultSet rt = st.executeQuery();

		long count = 0;
		String nl = System.getProperty("line.separator");
		while (rt.next() ) {
			count++;
			String key = null;

			switch (tipoOutput) {
			case BID:
				key = rt.getString(1);
				break;
			case BID_DATI:
				// Bug mantis collaudo 5317 -Maggio 2013
				// Il catalogo richiesto non riportava l'intestazione al soggetto ma per ogni BID
				// venivano prodotte due schede una con intestazione al titolo e l'altra all'autore
//				key = rt.getString(1) + "|" + rt.getString(3);
				key = rt.getString(1) + "|" + rt.getString(2);
				break;
			case BID_POSS:
				// Gestione Bibliografica: Intervento interno Per i Possessori si deve richiedere un tipo output (TipoOutput.BID_POSS)
				// che contenga sia il bid che l'area dell'inventario che quella della descrizione del possessore (area3 della select)
				key = rt.getString(1) + "|" + rt.getString(3);
				break;
			case INV:
				key = String.format("%3s%09d", rt.getString(1), rt.getLong(2));
				break;
			}

			w.append(key).append(nl);
		}

		rt.close();
		close(st);

		return count;

	}


	private PreparedStatement prepareStatementInventari(Connection c, BibliotecaVO bib) throws Exception {
		StringBuilder buf = new StringBuilder(1024);
		buf.append(props.get("QUERY_ESTRAI_ID_INVENTARIO"));

		switch (tipoElaborazione) {
		case TOTALE:
		case INCREMENTALE:
			throw new ValidationException(SbnErrorTypes.UNI_CRITERI_NON_VALIDI);

		case TOTALE_PER_BIBLIOTECA:
		case INCREMENTALE_PER_BIBLIOTECA:
			//almaviva5_20130910 evolutive google2
			aggiungiFiltroInventariDigitalizzati(buf);

			buf.append(BLANK).append(WHERE_START);
			aggiungiFiltriInventario(buf);

			buf.append(BLANK).append(props.get("WHERE_ESTRAI_ID_INVENTARIO"));
			break;

		case TOTALE_PER_RANGE_COLLOCAZIONI:
		case INCREMENTALE_PER_RANGE_COLLOCAZIONI:
			//almaviva5_20130910 evolutive google2
			aggiungiFiltroInventariDigitalizzati(buf);

			CodiciNormalizzatiVO rangeColl = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(esporta.getTipoCollocazione(),
					esporta.getPossDallaCollocazione(),
					esporta.getPossAllaCollocazione(),
					esporta.getPossSpecificazioneCollDa(),
					esporta.getPossSpecificazioneCollA(),
					null);

			buf.append(BLANK).append("inner join tbc_collocazione c on c.key_loc=i.key_loc");
			buf.append(BLANK).append(WHERE_START);
			buf.append(BLANK).append("and c.fl_canc<>'S'");
			buf.append(BLANK).append("and c.cd_polo_sezione='").append(esporta.getCodPoloSez()).append("'");
			buf.append(BLANK).append("and c.cd_biblioteca_sezione='").append(esporta.getCodBibSez()).append("'");
			buf.append(BLANK).append("and c.cd_sez='").append(esporta.getPossCodSez()).append("'");
			//filtro range coll
			buf.append(BLANK).append("and ( rpad(c.ord_loc,80)||rpad(c.ord_spec,40)");
			buf.append(BLANK).append("between '").append(rangeColl.getDaColl()).append(rangeColl.getDaSpec()).append("'");
			buf.append(BLANK).append("and '").append(rangeColl.getAColl()).append(rangeColl.getASpec()).append("' )");

			aggiungiFiltriInventario(buf);

			buf.append(BLANK).append(props.get("WHERE_ESTRAI_ID_INVENTARIO"));
			break;

		case TOTALE_PER_RANGE_INVENTARI:
		case INCREMENTALE_PER_RANGE_INVENTARI:
			//almaviva5_20130910 evolutive google2
			aggiungiFiltroInventariDigitalizzati(buf);

			buf.append(BLANK).append(WHERE_START);
			buf.append(BLANK).append("and ( i.cd_inven between ").append(esporta.getPossDalNumero());
			buf.append(BLANK).append("and ").append(esporta.getPossAlNumero()).append(" )");

			aggiungiFiltriInventario(buf);

			buf.append(BLANK).append(props.get("WHERE_ESTRAI_ID_INVENTARIO"));
			break;

		}

		String query = buf.toString();
		log.debug("sql= " + query);
		PreparedStatement st = c.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		st.setString(1, bib.getCod_polo());
		st.setString(2, bib.getCod_bib());
		return st;
	}


	private void aggiungiFiltroInventariDigitalizzati(StringBuilder buf) {
		//almaviva5_20130910 evolutive google2
		String digit = esporta.getDigitalizzati();
		if (!in(digit, "N"))
			return;

		log.debug("aggiungi Filtri Inventario digitalizzato");

		String filtro = props.getProperty("FILTRO_INVENTARI_DIGITALIZZATI");
		String tipoDigit = esporta.getTipoDigit();
		tipoDigit = isFilled(tipoDigit) ? tipoDigit : "C";
		String inClause = null;

		if ("T".equals(tipoDigit))
			//escludi parziale e completa
			inClause = AbstractJDBCManager.buildINClause("cod.cd_flg4", Arrays.asList(new String[] {"0", "1"} ));
		if ("C".equals(tipoDigit))
			//escludi completa
			inClause = AbstractJDBCManager.buildINClause("cod.cd_flg4", Arrays.asList(new String[] {"1"} ));
		if ("P".equals(tipoDigit))
			//escludi parziale e completa
			inClause = AbstractJDBCManager.buildINClause("cod.cd_flg4", Arrays.asList(new String[] {"0"} ));

		filtro = filtro.replace(":filtroDigit", inClause);

		buf.append(BLANK).append(filtro);
	}


	private void aggiungiFiltriInventario(StringBuilder buf) {
		log.debug("aggiungi Filtri Inventario");

		String cdFrui = esporta.getCodTipoFruizione();
		if (isFilled(cdFrui))
			buf.append(BLANK).append("and i.cd_frui='").append(cdFrui).append("'");

		String cdNoDisp = esporta.getCodNoDispo();
		if (isFilled(cdNoDisp))
			buf.append(BLANK).append("and i.cd_no_disp='").append(cdNoDisp).append("'");

		String cdRip = esporta.getCodRip();
		if (isFilled(cdRip))
			buf.append(BLANK).append("and i.cd_riproducibilita='").append(cdRip).append("'");

		String cdCons = esporta.getCodiceStatoConservazione();
		if (isFilled(cdCons))
			buf.append(BLANK).append("and i.stato_con='").append(cdCons).append("'");

		//almaviva5_20130910 evolutive google2
		String digit = esporta.getDigitalizzati();
		if (isFilled(digit)) {
			if ("S".equals(digit))
				buf.append(BLANK).append("and i.digitalizzazione>''");

			if ("N".equals(digit)) {
				buf.append(BLANK).append("and (i.digitalizzazione isnull or i.digitalizzazione='')");
				if (esporta.isEscludiDigit())
					//escludi tutti gli inventari che hanno un'altra copia digitalizzata
					buf.append(BLANK).append("and i2 isnull");
				else
					//includi solo gli inventari che hanno un'altra copia digitalizzata
					buf.append(BLANK).append("and not i2 isnull");
			}
		}

	}


	private List<Serializable> selectOrdinamentoTable(StringBuilder buf) throws Exception {


		Connection c = getConnection();

		log.debug("Select ordinamento tabella bid");

		Statement st = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		// attivo cursore server-side
		Integer fetch = Integer.valueOf(props.getProperty("DB_CURSOR_FETCH_SIZE", "1000"));
		log.debug("server-side cursor fetch size: " + fetch);
		st.setFetchSize(fetch);
		ResultSet rt = st.executeQuery(buf.toString());

		List<Serializable> listaBidOrdinati = new ArrayList<Serializable>();
		while (rt.next() ) {
			String bid = rt.getString(1);
			listaBidOrdinati.add(bid);
		}

		rt.close();
		close(st);

		return listaBidOrdinati;
	}


	private List<TabellaNumSTDImpronteVO> selectOrdinamentoTableConPossessori(StringBuilder buf) throws Exception {


		Connection c = getConnection();

		log.debug("Select ordinamento tabella bid con Possessori");

		Statement st = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		// attivo cursore server-side
		Integer fetch = Integer.valueOf(props.getProperty("DB_CURSOR_FETCH_SIZE", "1000"));
		log.debug("server-side cursor fetch size: " + fetch);
		st.setFetchSize(fetch);
		ResultSet rt = st.executeQuery(buf.toString());

		List<TabellaNumSTDImpronteVO> listaBidOrdinaticonPoss = new ArrayList<TabellaNumSTDImpronteVO>();
		TabellaNumSTDImpronteVO tabAppo;

		while (rt.next() ) {
			tabAppo = new TabellaNumSTDImpronteVO(rt.getString(1), rt.getString(2), rt.getString(3), rt.getString(4));
			listaBidOrdinaticonPoss.add(tabAppo);
		}

		rt.close();
		close(st);

		return listaBidOrdinaticonPoss;
	}



	public long extract(String targetFile) throws Exception {
		try {

			if (tipoElaborazione == null)
				throw new ValidationException(SbnErrorTypes.UNI_CRITERI_NON_VALIDI);

			createTempTables();
			StringBuilder sql = new StringBuilder(1024);

			//almaviva5_20120911 caricamento lista bid ignorati
			flags = flags | preparaListaIdentificativiIgnorati();

			// almaviva2 BUG 4701 (collaudo) correzione per stampare la lista di BID e non estrarre record UNIMARC
			log.debug("Inizio estrazione identificativi: Modalità " + tipoElaborazione);
			switch (tipoElaborazione) {
			case TOTALE:
				extractTOTALE();
				break;
			case TOTALE_PER_BIBLIOTECA:
			case TOTALE_PER_RANGE_COLLOCAZIONI:
			case TOTALE_PER_RANGE_INVENTARI:
				sql = extractTOTALE_PER_BIBLIOTECA();
				break;
			case INCREMENTALE:
				extractINCREMENTALE();
				break;
			case INCREMENTALE_PER_BIBLIOTECA:
			case INCREMENTALE_PER_RANGE_COLLOCAZIONI:
			case INCREMENTALE_PER_RANGE_INVENTARI:
				extractINCREMENTALE_PER_BIBLIOTECA();
				break;
			}

			//per stampa cataloghi si usa la query preparata da extractTOTALE_PER_BIBLIOTECA()
			if (!ValidazioneDati.equals(esporta.getCodAttivita(), CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI) ) {
				sql.setLength(0);
				sql.append("SELECT distinct(bid) FROM ").append(props.get("TABLE_NAME")).append(" order by bid");
			}

			//scrivo su file i bid estratti dalle query
			Writer w = new BufferedWriter(new FileWriter(targetFile));
			long count = unloadTempTable(sql, w);
			w.close();

			return count;

		} finally {
			closeConnection(null);
		}
	}



	public List<Serializable> ordina(List<Serializable> listaBid) throws Exception {
		try {

			createTempTablesSort();
			preparaTabellaIdentificativiDaOrdinare(listaBid);


			// almaviva2 BUG 4701 (collaudo) correzione per stampare la lista di BID e non estrarre record UNIMARC
			//almaviva5_20130107 #5218
			//Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
			// Inserito controllo per "EDI" lista BID documento ordinati per descrizione editore con i bid senza legami a editore in coda
			//Luglio 2013 Inserito nuova estrazione titoli x legame ad autore respons 4 e codice relazione editore 650 / tipografo:750

			String tipoCatalogo = trimOrEmpty(esporta.getTipoCatalogo());
			log.debug("Inizio ordinamento per " + tipoCatalogo);
			StringBuilder buf = new StringBuilder(1024);
			buf.setLength(0);
			if (tipoCatalogo.equals("AUT")) {
				buf.append("SELECT  bo.bid ").append(props.get("QUERY_BID_ORDINATI_PER_AUTORE_LEGATO"));
			} else if (tipoCatalogo.equals("TIP")) {
				buf.append("SELECT  bo.bid ").append(props.get("QUERY_BID_ORDINATI_PER_TIPOGRAFO"));
			} else if (tipoCatalogo.equals("TIT")) {
				buf.append("SELECT  bo.bid ").append(props.get("QUERY_BID_ORDINATI_PER_TITOLO"));
			} else if (tipoCatalogo.equals("EDI")) {
				buf.append("SELECT  bo.bid ").append(props.get("QUERY_BID_ORDINATI_PER_EDITORE_LEGATO"));
			} else if (tipoCatalogo.equals("SOG")) {
				buf.append("SELECT  bo.bid ").append(props.get("QUERY_BID_ORDINATI_PER_SOGGETTO_LEGATO"));
			} else if (tipoCatalogo.equals("CLA")) {
				buf.append("SELECT  bo.bid ").append(props.get("QUERY_BID_ORDINATI_PER_CLASSE_LEGATO"));
			} else if (tipoCatalogo.equals("POS")) {
				buf.append("SELECT  bo.bid, possprov.ds_nome_aut, posprovinv.cd_legame from tt_bid_ordinati bo ");
				buf.append("left outer join (tbc_inventario inv ");
				buf.append("inner join trc_poss_prov_inventari posprovinv ");
				buf.append("inner join tbc_possessore_provenienza possprov ");
				buf.append("on posprovinv.pid=possprov.pid ");

				String descPossDa = esporta.getCatalogoSelezDa();
				if (isFilled(descPossDa)) {
					String descPossA = esporta.getCatalogoSelezA();
					if (!isFilled(descPossA)) {
						descPossA = descPossDa;
					}
					descPossDa = descPossDa.toUpperCase();
					descPossA = descPossA.toUpperCase();
					buf.append("and possprov.ky_cles1_a>= '" + descPossDa + "' ");
					buf.append("and possprov.ky_cles1_a <= '" + descPossA + "' ");
				}
				buf.append("on posprovinv.cd_polo=inv.cd_polo ");
				buf.append("and posprovinv.cd_biblioteca=inv.cd_bib ");
				buf.append("and posprovinv.cd_serie=inv.cd_serie ");
				buf.append("and posprovinv.cd_inven=inv.cd_inven) ");
				buf.append("on inv.bid=bo.bid ");
				buf.append("and inv.fl_canc<>'S' ");
				buf.append("and inv.cd_sit not in ('0', '3') ");
				buf.append("and inv.cd_polo='" + esporta.getCodPolo() + "' ");
				buf.append("and inv.cd_bib='" + esporta.getCodBib() + "' ");
				buf.append("order by possprov.ky_cles1_a ");
			}

			//richiamo la classe che effettua la select di ordinamento
				return selectOrdinamentoTable(buf);

		} finally {
			closeConnection(null);
		}
	}

	public List<TabellaNumSTDImpronteVO> ordinaConPossessore(List<Serializable> listaBid) throws Exception {
		try {

			createTempTablesSort();
			preparaTabellaIdentificativiDaOrdinare(listaBid);
			String tipoCatalogo = trimOrEmpty(esporta.getTipoCatalogo());
			String descPossDa = esporta.getCatalogoSelezDa();

			log.debug("Inizio ordinamento per " + tipoCatalogo);
			StringBuilder buf = new StringBuilder(1024);
			buf.setLength(0);

			buf.append("(SELECT  bo.bid, possprov.ds_nome_aut, posprovinv.cd_legame, ");
			buf.append("inv.cd_bib||RPAD(inv.cd_serie,3,' ')||LPAD(CAST(inv.cd_inven as text),9,'0'), ");
			buf.append("possprov.ky_cles1_a as chiaveOrd1, ");
			buf.append("a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') as chiaveOrd2 ");
			buf.append("from tt_bid_ordinati bo ");
			buf.append("inner join tr_tit_aut ta on ta.bid = bo.bid and ta.tp_responsabilita in ('1', '2') and not ta.fl_canc='S' ");
			buf.append("inner join tb_autore a on a.vid = ta.vid and not a.fl_canc='S' ");
			buf.append("left outer join (tbc_inventario inv ");
			buf.append("inner join trc_poss_prov_inventari posprovinv ");
			buf.append("inner join tbc_possessore_provenienza possprov ");
			buf.append("on posprovinv.pid=possprov.pid ");

			if (isFilled(descPossDa)) {
				String descPossA = esporta.getCatalogoSelezA();
				if (!isFilled(descPossA)) {
					descPossA = descPossDa;
				}
				descPossDa = descPossDa.toUpperCase();
				descPossA = descPossA.toUpperCase();
				buf.append("and possprov.ky_cles1_a>= '" + descPossDa + "' ");
				buf.append("and possprov.ky_cles1_a <= '" + descPossA + "' ");
			}
			buf.append("on posprovinv.cd_polo=inv.cd_polo ");
			buf.append("and posprovinv.cd_biblioteca=inv.cd_bib ");
			buf.append("and posprovinv.cd_serie=inv.cd_serie ");
			buf.append("and posprovinv.cd_inven=inv.cd_inven) ");
			buf.append("on inv.bid=bo.bid ");
			buf.append("and inv.fl_canc<>'S' ");
			buf.append("and inv.cd_sit not in ('0', '3') ");
			buf.append("and inv.cd_polo='" + esporta.getCodPolo() + "' ");
			buf.append("and inv.cd_bib='" + esporta.getCodBib() + "') ");

			// inizio parte nuovo per ordinamento per chiave autore secondaria
			buf.append(" UNION ");
			buf.append("(SELECT  bo.bid, possprov.ds_nome_aut, posprovinv.cd_legame, ");
			buf.append("inv.cd_bib||RPAD(inv.cd_serie,3,' ')||LPAD(CAST(inv.cd_inven as text),9,'0'), ");
			buf.append("possprov.ky_cles1_a as chiaveOrd1, ");
			buf.append("'ZZZ autore mancante' as chiaveOrd2 ");
			buf.append("from tt_bid_ordinati bo ");
			buf.append("left outer join tr_tit_aut ta on ta.bid = bo.bid and ta.tp_responsabilita in ('1', '2') and not ta.fl_canc='S' ");
			buf.append("left outer join (tbc_inventario inv ");
			buf.append("inner join trc_poss_prov_inventari posprovinv ");
			buf.append("inner join tbc_possessore_provenienza possprov ");
			buf.append("on posprovinv.pid=possprov.pid ");

			if (isFilled(descPossDa)) {
				String descPossA = esporta.getCatalogoSelezA();
				if (!isFilled(descPossA)) {
					descPossA = descPossDa;
				}
				descPossDa = descPossDa.toUpperCase();
				descPossA = descPossA.toUpperCase();
				buf.append("and possprov.ky_cles1_a>= '" + descPossDa + "' ");
				buf.append("and possprov.ky_cles1_a <= '" + descPossA + "' ");
			}
			buf.append("on posprovinv.cd_polo=inv.cd_polo ");
			buf.append("and posprovinv.cd_biblioteca=inv.cd_bib ");
			buf.append("and posprovinv.cd_serie=inv.cd_serie ");
			buf.append("and posprovinv.cd_inven=inv.cd_inven) ");
			buf.append("on inv.bid=bo.bid ");
			buf.append("and inv.fl_canc<>'S' ");
			buf.append("and inv.cd_sit not in ('0', '3') ");
			buf.append("and inv.cd_polo='" + esporta.getCodPolo() + "' ");
			buf.append("and inv.cd_bib='" + esporta.getCodBib() + "') ");
			buf.append("order by chiaveOrd1, chiaveOrd2");

			//richiamo la classe che effettua la select di ordinamento
			return selectOrdinamentoTableConPossessori(buf);

		} finally {
			closeConnection(null);
		}


	}

	private int preparaListaIdentificativiIgnorati() throws Exception {
		String ignore = trimOrNull(CommonConfiguration.getProperty(Configuration.SBNWEB_EXPORT_IGNORE_FILE));
		if (ignore == null)
			return FLAG_NULL;

		File f = new File(ignore);
		if (!f.exists()) {
			log.warn("File identificativi ignorati non esistente: " + ignore);
			return FLAG_NULL;
		}

		int inserted = 0;
		StringBuilder sql = new StringBuilder(1024);
		String tableName = (String) props.get("IGNORATI_TABLE_NAME");
		sql.append("INSERT INTO ").append(tableName).append(" VALUES (?)");

		Connection c = getConnection();
		PreparedStatement ps = c.prepareStatement(sql.toString());
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String bid = null;
			while ((bid = reader.readLine()) != null) {
				if (!leggiXID(bid))
					continue;

				ps.setString(1, bid);
				ps.addBatch();
			}

			int[] results = ps.executeBatch();
			for (int r = 0; r < results.length; r++)
				inserted += results[r];

			log.debug("Numero bid ignorati: " + inserted);

		} finally {
			ps.close();
			reader.close();
		}


		return (inserted > 0) ? FLAG_IGNORA_IDENTIFICATIVI: FLAG_NULL;
	}

	private int preparaTabellaIdentificativiDaOrdinare(List<Serializable> listaBid) throws Exception {

		int inserted = 0;
		StringBuilder sql = new StringBuilder(1024);
		String tableName = (String) props.get("ORDINATI_TABLE_NAME");
		sql.append("INSERT INTO ").append(tableName).append(" VALUES (?)");

		Connection c = getConnection();
		PreparedStatement ps = c.prepareStatement(sql.toString());

		int sizeListaBid = listaBid.size();
		String bidAttuale="";
		try {

			if (listaBid.get(0) instanceof BidInventarioSegnaturaVO) {
				for (int i = 0; i < sizeListaBid; i++) {
					bidAttuale = ((BidInventarioSegnaturaVO) listaBid.get(i)).getBid();
					ps.setString(1, bidAttuale);
					ps.addBatch();
				}
			} else if (listaBid.get(0) instanceof CodiceVO){
				for (int i = 0; i < sizeListaBid; i++) {
					bidAttuale = ((CodiceVO) listaBid.get(i)).getCodice();
					ps.setString(1, bidAttuale);
					ps.addBatch();
				}
			}else {
				for (int i = 0; i < sizeListaBid; i++) {
					bidAttuale = ((String) listaBid.get(i));
					ps.setString(1, bidAttuale);
					ps.addBatch();
				}
			}

			int[] results = ps.executeBatch();
			for (int r = 0; r < results.length; r++)
				inserted += results[r];

			log.debug("Numero bid da ordinare: " + inserted);

		} finally {
			ps.close();
		}

		return (inserted > 0) ? FLAG_IGNORA_IDENTIFICATIVI: FLAG_NULL;
	}


	private void extractTOTALE() throws Exception {

		// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
		// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
		// variazione conseguente delle SELECT di estrazione e
		// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
		// INIZIO
		if ( esporta.isSoloDocPosseduti() ) {
			preparaListaBidPosseduti();
		}
		// FINE

		StringBuilder buf = new StringBuilder(1024);
		//query base su titoli
		preparaQuery(buf, "QUERY_BID", "t.ts_var", flags );
		fillTempTable(buf);
	}

	private StringBuilder extractTOTALE_PER_BIBLIOTECA() throws Exception {
		preparaListaBidPosseduti();
		StringBuilder buf = new StringBuilder(1024);

		// Inizio Modifica in test DATI CATALOGO AUTORI PER STAMPA CATALOGHI
		// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
		// gli estremi di filtro non sono più solo per titolo ma la tabella su cui applicare dipende dal catalogo selezionato
		//almaviva5_20130107 #5218
		String tipoCatalogo = trimOrEmpty(esporta.getTipoCatalogo());
		if (tipoCatalogo.equals("AUT") && isFilled(esporta.getCatalogoSelezDa())) {
			//query base su titoli ma filtrata per autori
			preparaQuery(buf, "QUERY_BID_VID", "t.ts_var", flags );
		} else if (tipoCatalogo.equals("TIP") && isFilled(esporta.getCatalogoSelezDa())) {
			// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
			preparaQuery(buf, "QUERY_BID_TIP", "t.ts_var", flags );
		} else if (tipoCatalogo.equals("EDI") && isFilled(esporta.getCatalogoSelezDa())) {
			// Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
			//query base su titoli ma filtrata per editori
			preparaQuery(buf, "QUERY_BID_EDI", "t.ts_var", flags );
		} else if (tipoCatalogo.equals("SOG")) {
			// Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
			//query base su titoli ma filtrata per soggetti
			preparaQuery(buf, "QUERY_BID_SOG", "t.ts_var", flags );
		} else if (tipoCatalogo.equals("CLA")) {
			// Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
			//query base su titoli ma filtrata per classi
			preparaQuery(buf, "QUERY_BID_CLA", "t.ts_var", flags );
		} else if (tipoCatalogo.equals("POS")) {
			// Intervento del 21.02.2013 su Richiesta ICCU in fase di Elaborazione Manuale Utente;
			//query base su titoli ma filtrata per classi
			preparaQuery(buf, "QUERY_BID_POS", "t.ts_var", flags );
		} else {
			//query base su titoli
			preparaQuery(buf, "QUERY_BID", "t.ts_var", flags );
		}
//		//query base su titoli
//		preparaQuery(buf, "QUERY_BID", "t.ts_var", flags );
		// Fine Modifica in test DATI CATALOGO AUTORI PER STAMPA CATALOGHI
		if (!ValidazioneDati.equals(esporta.getCodAttivita(), CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI) )
			fillTempTable(buf);

		return buf;
	}


	private void preparaListaBidPosseduti() throws Exception {
		log.debug("Preparazione lista bid posseduti");
		StringBuilder sql = new StringBuilder(1024);
		Connection c = getConnection();

		switch (tipoElaborazione) {
		case TOTALE_PER_BIBLIOTECA:
			sql.append(props.get("QUERY_BID_BASE_POSSEDUTI_TOT"));

			//sostituisco parametro filtro bib con i dati attuali
			aggiungiFiltroBiblioteca(sql, "filtroBib1", "t.bid"); // filtro bid con inventari diretti
			aggiungiFiltroBiblioteca(sql, "filtroBib2", "tti.bid_base"); // filtro bid con inventari tit. intermedi
			aggiungiFiltroBiblioteca(sql, "filtroBib3", "tti.bid_base"); // filtro bid con inventari tit. superiori
			aggiungiFiltroBiblioteca(sql, "filtroBib4", "inf.bid_coll"); // filtro bid con inventari tit. inferiori

			//almaviva5_20100401 filtro flag possesso
			aggiungiFiltroPossesso(sql, "filtroPossesso");
			break;

		case TOTALE_PER_RANGE_COLLOCAZIONI:
		case INCREMENTALE_PER_RANGE_COLLOCAZIONI:
			sql.append(props.get("QUERY_BID_BASE_POSSEDUTI_RANGE_COLLOCAZIONI_TOT"));

			aggiungiFiltroRangeCollocazioni(sql, "filtroRange1");
			aggiungiFiltroRangeCollocazioni(sql, "filtroRange2");
			aggiungiFiltroRangeCollocazioni(sql, "filtroRange3");
			aggiungiFiltroRangeCollocazioni(sql, "filtroRange4");
			break;

		case TOTALE_PER_RANGE_INVENTARI:
		case INCREMENTALE_PER_RANGE_INVENTARI:
			sql.append(props.get("QUERY_BID_BASE_POSSEDUTI_RANGE_COLLOCAZIONI_TOT"));

			aggiungiFiltroRangeInventari(sql, "filtroRange1");
			aggiungiFiltroRangeInventari(sql, "filtroRange2");
			aggiungiFiltroRangeInventari(sql, "filtroRange3");
			aggiungiFiltroRangeInventari(sql, "filtroRange4");
			break;

		case INCREMENTALE_PER_BIBLIOTECA:
			sql.append(props.get("QUERY_BID_BASE_POSSEDUTI_INC"));	// senza fl_canc!=S

			//sostituisco parametro filtro bib con i dati attuali
			aggiungiFiltroBiblioteca(sql, "filtroBib1", "t.bid"); // filtro bid con inventari diretti
			aggiungiFiltroBiblioteca(sql, "filtroBib2", "tti.bid_base"); // filtro bid con inventari tit. intermedi
			aggiungiFiltroBiblioteca(sql, "filtroBib3", "tti.bid_base"); // filtro bid con inventari tit. superiori
			aggiungiFiltroBiblioteca(sql, "filtroBib4", "inf.bid_coll"); // filtro bid con inventari tit. inferiori

			//almaviva5_20100401 filtro flag possesso
			aggiungiFiltroPossesso(sql, "filtroPossesso");
			break;

		case INCREMENTALE:
		case TOTALE:
			// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
			// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
			// variazione conseguente delle SELECT di estrazione e
			// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
			// INIZIO
			if ( esporta.isSoloDocPosseduti() ) {
				sql.append(props.get("QUERY_BID_BASE_POSSEDUTI_TOT"));

				//sostituisco parametro filtro bib con i dati attuali
				aggiungiFiltroSoloPosseduti(sql, "filtroBib1", "t.bid"); // filtro bid con inventari diretti
				aggiungiFiltroSoloPosseduti(sql, "filtroBib2", "tti.bid_base"); // filtro bid con inventari tit. intermedi
				aggiungiFiltroSoloPosseduti(sql, "filtroBib3", "tti.bid_base"); // filtro bid con inventari tit. superiori
				aggiungiFiltroSoloPosseduti(sql, "filtroBib4", "inf.bid_coll"); // filtro bid con inventari tit. inferiori

				//almaviva5_20100401 filtro flag possesso
				aggiungiFiltroPossesso(sql, "filtroPossesso");
				break;
			}
			// FINE
			break;
		}

		String query = sql.toString();
		log.debug("sql= " + query);
		Statement st = c.createStatement();
		log.debug("Numero bid posseduti: " + st.executeUpdate(query) );
		close(st);
	}

	private void aggiungiFiltroBiblioteca(StringBuilder buf, String nomeFiltro,	String campoBid) {

		List<BibliotecaVO> listaBib = esporta.getListaBib();
		if (!isFilled(listaBib))
			return;

		// clausola lista bib
		List<String> listaCodBib = new ArrayList<String>();
		for (BibliotecaVO b : listaBib)
			listaCodBib.add(b.getCod_bib());
		String bibINClause = buildINClause("inv.cd_bib", listaCodBib);

		StringBuilder filtro = new StringBuilder(1024);
		filtro.append(" ( select 1 from tbc_inventario inv ");
		filtro.append("where inv.fl_canc<>'S'");

		// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
		// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
		// variazione conseguente delle SELECT di estrazione e
		// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
		// INIZIO
		// filtro.append("  and inv.cd_sit not in ('0', '3')");
		if ( esporta.isSoloDocPosseduti() ) {
			filtro.append("  and inv.cd_sit = '2'");
		} else {
			filtro.append("  and inv.cd_sit not in ('0', '3')");
		}
		// FINE

		filtro.append("  and inv.bid=").append(campoBid);
		filtro.append("  and inv.cd_polo='").append(esporta.getCodPolo()).append("'");
		filtro.append("  and ").append(bibINClause);
		filtro.append(")");

		if (!nomeFiltro.startsWith(":") )
			nomeFiltro = ":" + nomeFiltro;

		int idx = buf.indexOf(nomeFiltro);
		if (idx > -1)
			buf.replace(idx, idx + nomeFiltro.length(), filtro.toString() );
	}

	private void aggiungiFiltroPossesso(StringBuilder buf, String nomeFiltro) {

		List<BibliotecaVO> listaBib = esporta.getListaBib();
		if (!isFilled(listaBib)) {
			// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
			// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
			// variazione conseguente delle SELECT di estrazione e
			// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
			// queste istruzioni servono ad eliminare la parte relativa al possesso nel caso in cui la listaBib è vuota;
			// INIZIO
			if (!nomeFiltro.startsWith(":") )
				nomeFiltro = ":" + nomeFiltro;

			int idx = buf.indexOf(nomeFiltro);
			if (idx > -1)
				buf.replace(idx, idx + nomeFiltro.length(), "true");
			// FINE
			return;

		}

		// clausola lista bib
		List<String> listaCodBib = new ArrayList<String>();
		for (BibliotecaVO b : listaBib)
			listaCodBib.add(b.getCod_bib());
		String bibINClause = buildINClause("tb.cd_biblioteca", listaCodBib);

		StringBuilder filtro = new StringBuilder(1024);
		filtro.append(" ( tb.cd_polo='").append(esporta.getCodPolo()).append("'");
		filtro.append(" and ").append(bibINClause);
		filtro.append(" )");

		if (!nomeFiltro.startsWith(":") )
			nomeFiltro = ":" + nomeFiltro;

		int idx = buf.indexOf(nomeFiltro);
		if (idx > -1)
			buf.replace(idx, idx + nomeFiltro.length(), filtro.toString() );
	}

	private void aggiungiFiltroRangeCollocazioni(StringBuilder buf, String nomeFiltro) throws Exception {

		CodiciNormalizzatiVO rangeColl = NormalizzaRangeCollocazioni.normalizzaCollSpecRange(esporta.getTipoCollocazione(),
				esporta.getPossDallaCollocazione(),
				esporta.getPossAllaCollocazione(),
				esporta.getPossSpecificazioneCollDa(),
				esporta.getPossSpecificazioneCollA(),
				null);

		StringBuilder filtro = new StringBuilder(1024);
		filtro.append(" ( select i.bid from tbc_collocazione c");
		filtro.append(" inner join tbc_inventario i on i.key_loc=c.key_loc");

		//almaviva5_20130910 evolutive google2
		aggiungiFiltroInventariDigitalizzati(filtro);

		filtro.append(" where c.fl_canc<>'S'");
		filtro.append(" and c.cd_polo_sezione='").append(esporta.getCodPoloSez()).append("'");
		filtro.append(" and c.cd_biblioteca_sezione='").append(esporta.getCodBibSez()).append("'");
		filtro.append(" and c.cd_sez='").append(esporta.getPossCodSez()).append("'");
		//filtro range coll
		filtro.append(" and ( rpad(c.ord_loc,80)||rpad(c.ord_spec,40)");
		filtro.append(" between '").append(rangeColl.getDaColl()).append(rangeColl.getDaSpec()).append("'");
		filtro.append(" and '").append(rangeColl.getAColl()).append(rangeColl.getASpec()).append("' )");

		aggiungiFiltriInventario(filtro);

		filtro.append(" )");

		if (!nomeFiltro.startsWith(":") )
			nomeFiltro = ":" + nomeFiltro;

		int idx = buf.indexOf(nomeFiltro);
		if (idx > -1)
			buf.replace(idx, idx + nomeFiltro.length(), filtro.toString() );
	}

	private void aggiungiFiltroRangeInventari(StringBuilder buf, String nomeFiltro) throws Exception {

		StringBuilder filtro = new StringBuilder(1024);
		filtro.append(" ( select i.bid from tbc_inventario i");

		//almaviva5_20130910 evolutive google2
		aggiungiFiltroInventariDigitalizzati(filtro);

		filtro.append(" where i.fl_canc<>'S'");
		filtro.append(" and i.cd_polo='").append(esporta.getCodPoloSerie()).append("'");
		filtro.append(" and i.cd_bib='").append(esporta.getCodBibSerie()).append("'");
		filtro.append(" and i.cd_serie='").append(esporta.getPossSerie()).append("'");

		// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
		// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
		// variazione conseguente delle SELECT di estrazione e
		// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
		// INIZIO
		// filtro.append(" and i.cd_sit not in ('0', '3')");
		if ( esporta.isSoloDocPosseduti() ) {
			filtro.append(" and i.cd_sit = '2'");
		} else {
			filtro.append(" and i.cd_sit not in ('0', '3')");
		}
		// FINE

		//filtro range inv
		filtro.append(" and ( i.cd_inven between ").append(esporta.getPossDalNumero());
		filtro.append(" and ").append(esporta.getPossAlNumero()).append(" )");

		aggiungiFiltriInventario(filtro);

		filtro.append(" )");

		if (!nomeFiltro.startsWith(":") )
			nomeFiltro = ":" + nomeFiltro;

		int idx = buf.indexOf(nomeFiltro);
		if (idx > -1)
			buf.replace(idx, idx + nomeFiltro.length(), filtro.toString() );
	}

	private void extractINCREMENTALE() throws Exception {
		StringBuilder buf = new StringBuilder(1024);

		//query base su titoli
		preparaQuery(buf, "QUERY_BID", "t.ts_var", flags );
		fillTempTable(buf);

		//lista BID documento legati a titoli modificati nell'intervallo temporale
		preparaQuery(buf, "QUERY_BID_LEGATI_TITOLI_MODIFICATI", "tc.ts_var", flags ); //tc = tit collegato
		fillTempTable(buf);

		//lista BID documento legati ad autori modificati nell'intervallo temporale
		preparaQuery(buf, "QUERY_BID_LEGATI_AUTORI_MODIFICATI", "aut.ts_var", flags ); //aut = autore
		fillTempTable(buf);

		//lista BID documento legati a marche modificate nell'intervallo temporale
		preparaQuery(buf, "QUERY_BID_LEGATI_MARCHE_MODIFICATE", "mar.ts_var", flags ); //mar = marca
		fillTempTable(buf);

		//lista BID documento legati a soggetti modificati nell'intervallo temporale
		preparaQuery(buf, "QUERY_BID_LEGATI_SOGGETTI_MODIFICATI", "sog.ts_var", flags ); //sog = soggetto
		fillTempTable(buf);

		//lista BID documento legati a thesauri modificati nell'intervallo temporale
		preparaQuery(buf, "QUERY_BID_LEGATI_THESAURI_MODIFICATI", "the.ts_var", flags ); //the=thesauri
		fillTempTable(buf);

		//lista BID documento legati a classi modificate nell'intervallo temporale
		preparaQuery(buf, "QUERY_BID_LEGATI_CLASSI_MODIFICATE", "cla.ts_var", flags ); //cla= classe
		fillTempTable(buf);

		//lista BID documento collegati a BID con inventari modificati
		preparaQuery(buf, "QUERY_BID_LEGATI_INVENTARI_MODIFICATI", "inv.ts_var", flags ); //inv= inventario
		fillTempTable(buf);

		preparaQuery(
				buf,
				"QUERY_BID_CHE_CONTENGONO_TITOLI_MODIFICATI",
				"tb.ts_var", flags ); // tb=titolo base
		fillTempTable(buf);

		//almaviva5_20120829 #4454 localizzazioni modificate
		preparaQuery(buf, "QUERY_BID_LEGATI_LOCALIZZAZIONI_MODIFICATE", "tb.ts_var", flags ); //tb=titolo-biblioteca
		fillTempTable(buf);
	}

	private void extractINCREMENTALE_PER_BIBLIOTECA() throws Exception {
		preparaListaBidPosseduti();
		extractINCREMENTALE();
	}

	private void preparaQuery(StringBuilder buf, String queryName, String campoFiltroTsVar, int flags) {
		boolean incrementale = ((flags & FLAG_INCREMENTALE) > 0);
		boolean ignora = ((flags & FLAG_IGNORA_IDENTIFICATIVI) > 0);

		String tipoCatalogo = trimOrEmpty(esporta.getTipoCatalogo());
		log.debug("preparo query '" + queryName + "':");
		buf.setLength(0);

		StringBuilder bufTestataPrimaIntest = new StringBuilder(1024);
		StringBuilder bufTestataSecondaIntest = new StringBuilder(1024);
		StringBuilder bufFiltri = new StringBuilder(1024);
		StringBuilder bufFiltroPrimaIntest = new StringBuilder(1024);
		StringBuilder bufFiltroSecondaIntest = new StringBuilder(1024);

		boolean isStampaCatalogo = esporta.getCodAttivita().equals(CodiciAttivita.getIstance().GDF_STAMPA_CATALOGHI);
		if (isStampaCatalogo && tipoCatalogo.equals("AUT")) {
			// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
			// tutta la select della stampa catalogo per autore/titolo viene scritta qui perchè fuori standard costruttore select
			bufTestataPrimaIntest.append("( SELECT ");
			bufTestataPrimaIntest.append(" t.bid, a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') as chiaveOrd1,");
			bufTestataPrimaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord2");
			bufTestataPrimaIntest.append(" from tb_titolo t");
			bufTestataPrimaIntest.append(" inner join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita in ('1', '2') and not ta.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join  tb_autore a on a.vid = ta.vid and not a.fl_canc='S'");
			bufTestataPrimaIntest.append(" where (1=1)");

			bufTestataSecondaIntest.append(" SELECT ");
			bufTestataSecondaIntest.append(" t.bid, t.ky_cles1_t as chiaveOrd, COALESCE(t.ky_cles2_t,'')  as chiaveord2");
			bufTestataSecondaIntest.append(" from tb_titolo t");
			bufTestataSecondaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita in ('1', '2') and not ta.fl_canc='S'");
			bufTestataSecondaIntest.append(" where (1=1)");
		} else if (isStampaCatalogo && tipoCatalogo.equals("TIP")) {
			// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
			bufTestataPrimaIntest.append("( SELECT ");
			bufTestataPrimaIntest.append(" t.bid, a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') as chiaveOrd1,");
			bufTestataPrimaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord2");
			bufTestataPrimaIntest.append(" from tb_titolo t");
			bufTestataPrimaIntest.append(" inner join tr_tit_aut ta on ta.bid = t.bid and ta.cd_relazione in ('650', '750') and not ta.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join  tb_autore a on a.vid = ta.vid and not a.fl_canc='S'");
			bufTestataPrimaIntest.append(" where (1=1)");

		} else if (isStampaCatalogo && tipoCatalogo.equals("SOG")) {
			// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
			// tutta la select della stampa catalogo per soggetto/autore/titolo viene scritta qui perchè fuori standard costruttore select

			// almaviva2 - Ottobre 2016
			// Bug Mantis 6277 - stampa catalogo soggetti ordinato in modo errato: il problema viene risolto modificando la
			// select di estrazione dei titoli legati al soggettario e variando le modalità di accoppiamento fra i bid estratti
			// da questa select (di tipo bibliografico) e quelli estratti in base ai filtri di documento fisico
			bufTestataPrimaIntest.append("( SELECT ");
			bufTestataPrimaIntest.append(" t.bid, s.ky_cles1_s||COALESCE(s.ky_cles2_s, '') as chiaveOrd1,");
			bufTestataPrimaIntest.append(" a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') as chiaveord2,");
			// bufTestataPrimaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord3, s.ds_soggetto");
			bufTestataPrimaIntest.append(" rpad(t.ky_cles1_t,6,' ')||COALESCE(t.ky_cles2_t, '') as chiaveord3, s.ds_soggetto");
			bufTestataPrimaIntest.append(" from tb_titolo t");
			bufTestataPrimaIntest.append(" inner join tr_tit_sog_bib ts on ts.bid = t.bid and ts.cd_sogg ='" + esporta.getCodSoggettario() + "' and not ts.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join  tb_soggetto s on s.cid = ts.cid and not s.fl_canc='S'");
			//bufTestataPrimaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita = '1' and not ta.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita = '1' and not ta.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join tb_autore a on a.vid = ta.vid and not a.fl_canc='S'");
			bufTestataPrimaIntest.append(" where (1=1)");

			bufTestataSecondaIntest.append(" SELECT ");
			bufTestataSecondaIntest.append(" t.bid, s.ky_cles1_s||COALESCE(s.ky_cles2_s, '') as chiaveOrd1,");
			// bufTestataSecondaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord2,");
			bufTestataSecondaIntest.append(" rpad(t.ky_cles1_t,6,' ')||COALESCE(t.ky_cles2_t, '') as chiaveord2,");
			// bufTestataSecondaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord3, s.ds_soggetto");
			bufTestataSecondaIntest.append(" rpad(t.ky_cles1_t,6,' ')||COALESCE(t.ky_cles2_t, '') as chiaveord3, s.ds_soggetto");
			bufTestataSecondaIntest.append(" from tb_titolo t");
			bufTestataSecondaIntest.append(" inner join tr_tit_sog_bib ts on ts.bid = t.bid and ts.cd_sogg ='" + esporta.getCodSoggettario() + "' and not ts.fl_canc='S'");
			bufTestataSecondaIntest.append(" inner join  tb_soggetto s on s.cid = ts.cid and not s.fl_canc='S'");
			bufTestataSecondaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita  = '1' and not ta.fl_canc='S'");
			bufTestataSecondaIntest.append(" where (1=1)");
			// Riga Nuova
			bufTestataSecondaIntest.append(" and ta.* isnull");


		} else if (isStampaCatalogo && tipoCatalogo.equals("CLA")) {
			// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
			// tutta la select della stampa catalogo per classe/autore/titolo viene scritta qui perchè fuori standard costruttore select
			bufTestataPrimaIntest.append("( SELECT ");
			bufTestataPrimaIntest.append(" t.bid, tc.classe as chiaveOrd1,");
			bufTestataPrimaIntest.append(" a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') as chiaveord2,");
			bufTestataPrimaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord3,");
			bufTestataPrimaIntest.append(" c.cd_sistema||' '||c.classe||' '||COALESCE(c.ds_classe, '') as descrizione ");
			bufTestataPrimaIntest.append(" from tb_titolo t");
			bufTestataPrimaIntest.append(" inner join tr_tit_cla tc on tc.bid = t.bid  and not tc.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join  tb_classe c on c.classe = tc.classe and c.cd_sistema = tc.cd_sistema and c.cd_edizione = tc.cd_edizione and not c.fl_canc='S'");
			bufTestataPrimaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita  = '1' and not ta.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join  tb_autore a on a.vid = ta.vid and not a.fl_canc='S'");
			bufTestataPrimaIntest.append(" where (1=1)");

			bufTestataSecondaIntest.append(" SELECT ");
			bufTestataSecondaIntest.append(" t.bid, tc.classe as chiaveOrd1,");
			bufTestataSecondaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord2,");
			bufTestataSecondaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord3,");
			bufTestataSecondaIntest.append(" c.cd_sistema||' '||c.classe||' '||COALESCE(c.ds_classe, '') as descrizione ");
			bufTestataSecondaIntest.append(" from tb_titolo t");
			bufTestataSecondaIntest.append(" inner join tr_tit_cla tc on tc.bid = t.bid  and not tc.fl_canc='S'");
			bufTestataSecondaIntest.append(" inner join  tb_classe c on c.classe = tc.classe and c.cd_sistema = tc.cd_sistema and c.cd_edizione = tc.cd_edizione and not c.fl_canc='S'");
			bufTestataSecondaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita  = '1' and not ta.fl_canc='S'");
			bufTestataSecondaIntest.append(" where (1=1)");

		} else if (isStampaCatalogo && tipoCatalogo.equals("EDI")) {
			// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
			// tutta la select della stampa catalogo per editore/autore/titolo viene scritta qui perchè fuori standard costruttore select


			bufTestataPrimaIntest.append("( SELECT ");
			bufTestataPrimaIntest.append(" t.bid, vCatEdit.chiave_for as chiaveOrd1,");
			bufTestataPrimaIntest.append(" a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') as chiaveord2,");
			bufTestataPrimaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord3,");
			bufTestataPrimaIntest.append(" vCatEdit.nom_fornitore as descrizione ");
			bufTestataPrimaIntest.append(" from tb_titolo t");
			bufTestataPrimaIntest.append(" inner join v_catalogo_editoria vCatEdit on vCatEdit.bid = t.bid");
			bufTestataPrimaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita  = '1' and not ta.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join  tb_autore a on a.vid = ta.vid and not a.fl_canc='S'");
			bufTestataPrimaIntest.append(" where (1=1)");

			bufTestataSecondaIntest.append(" SELECT ");
			bufTestataSecondaIntest.append(" t.bid, vCatEdit.chiave_for as chiaveOrd1,");
			bufTestataSecondaIntest.append(" 'ZZZ autore mancante'  as chiaveord2,");
			bufTestataSecondaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord3,");
			bufTestataSecondaIntest.append(" vCatEdit.nom_fornitore as descrizione ");
			bufTestataSecondaIntest.append(" from tb_titolo t");
			bufTestataSecondaIntest.append(" inner join v_catalogo_editoria vCatEdit on vCatEdit.bid = t.bid");
			bufTestataSecondaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita  = '1' and not ta.fl_canc='S'");
			bufTestataSecondaIntest.append(" where (1=1)");
		} else if (isStampaCatalogo && tipoCatalogo.equals("POS")) {
			// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
			// tutta la select della stampa catalogo per possessore/autore/titolo viene scritta qui perchè fuori standard costruttore select
			String descPossDa = esporta.getCatalogoSelezDa();
			if (!isFilled(descPossDa)) {
				descPossDa = "A";
			}

			String descPossA = esporta.getCatalogoSelezA();
			if (!isFilled(descPossA))
				descPossA = "ZZZZ";

			descPossDa = descPossDa.toUpperCase();
			descPossA = descPossA.toUpperCase();




			bufTestataPrimaIntest.append("( SELECT ");
			bufTestataPrimaIntest.append(" t.bid, possprov.ds_nome_aut,");
			bufTestataPrimaIntest.append(" trim (col.cd_sez) ");
			bufTestataPrimaIntest.append(" ||case when col.cd_loc>'' then ' '||trim(col.cd_loc) else '' end");
			bufTestataPrimaIntest.append(" ||case when col.spec_loc>'' then ' '||trim(col.spec_loc) else '' end");
			bufTestataPrimaIntest.append(" ||case when inv.seq_coll>'' then ' / '||inv.seq_coll else '' end ");
			bufTestataPrimaIntest.append(" ||'   '||RPAD(inv.cd_serie,3,' ')||LPAD(CAST(inv.cd_inven as text),9,'0')");
			bufTestataPrimaIntest.append(" ||case when inv.precis_inv='$' then '' else ' ('||trim(inv.precis_inv)||')' end");
			bufTestataPrimaIntest.append(" ||case when posprovinv.cd_legame = 'R'");
			bufTestataPrimaIntest.append(" then '    Esemplare proveniente da <'||trim(possprov.ds_nome_aut)||'>'");
			bufTestataPrimaIntest.append(" else '    Esemplare posseduto da <'||trim(possprov.ds_nome_aut)||'>' end as inventario,");
			bufTestataPrimaIntest.append(" possprov.ky_cles1_a as chiaveord1, ");
			bufTestataPrimaIntest.append(" a.ky_cles1_a||COALESCE(a.ky_cles2_a, '') as chiaveord2,");
			bufTestataPrimaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord3");
			bufTestataPrimaIntest.append(" from tb_titolo t");
			bufTestataPrimaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita  = '1' and not ta.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join  tb_autore a on a.vid = ta.vid and not a.fl_canc='S'");
			bufTestataPrimaIntest.append(" inner join tbc_inventario inv on inv.bid=t.bid and inv.fl_canc<>'S' and inv.cd_sit = '2'");
			bufTestataPrimaIntest.append(" and inv.cd_polo='" + esporta.getCodPolo() + "' and inv.cd_bib='" + esporta.getCodBib() + "'");
			bufTestataPrimaIntest.append(" inner join trc_poss_prov_inventari posprovinv on posprovinv.cd_polo=inv.cd_polo");
			bufTestataPrimaIntest.append(" and posprovinv.cd_biblioteca=inv.cd_bib and posprovinv.cd_serie=inv.cd_serie and posprovinv.cd_inven=inv.cd_inven");
			bufTestataPrimaIntest.append(" inner join tbc_possessore_provenienza possprov on posprovinv.pid=possprov.pid");
			bufTestataPrimaIntest.append(" and possprov.ky_cles1_a >= '" + descPossDa + "'");
			bufTestataPrimaIntest.append(" and possprov.ky_cles1_a <= '" + descPossA + "'");
			bufTestataPrimaIntest.append(" inner  join tbc_collocazione col on col.key_loc = inv.key_loc");
			bufTestataPrimaIntest.append(" where (1=1)");

			bufTestataSecondaIntest.append(" SELECT ");
			bufTestataSecondaIntest.append(" t.bid, possprov.ds_nome_aut,");
			bufTestataSecondaIntest.append(" trim (col.cd_sez) ");
			bufTestataSecondaIntest.append(" ||case when col.cd_loc>'' then ' '||trim(col.cd_loc) else '' end");
			bufTestataSecondaIntest.append(" ||case when col.spec_loc>'' then ' '||trim(col.spec_loc) else '' end");
			bufTestataSecondaIntest.append(" ||case when inv.seq_coll>'' then ' / '||inv.seq_coll else '' end ");
			bufTestataSecondaIntest.append(" ||'   '||RPAD(inv.cd_serie,3,' ')||LPAD(CAST(inv.cd_inven as text),9,'0')");
			bufTestataSecondaIntest.append(" ||case when inv.precis_inv='$' then '' else ' ('||trim(inv.precis_inv)||')' end");
			bufTestataSecondaIntest.append(" ||case when posprovinv.cd_legame = 'R'");
			bufTestataSecondaIntest.append(" then '    Esemplare proveniente da <'||trim(possprov.ds_nome_aut)||'>'");
			bufTestataSecondaIntest.append(" else '    Esemplare posseduto da <'||trim(possprov.ds_nome_aut)||'>' end as inventario,");
			bufTestataSecondaIntest.append(" possprov.ky_cles1_a as chiaveord1, ");
			bufTestataSecondaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord2,");
			bufTestataSecondaIntest.append(" t.ky_cles1_t||COALESCE(t.ky_cles2_t, '') as chiaveord3");
			bufTestataSecondaIntest.append(" from tb_titolo t");
			bufTestataSecondaIntest.append(" left outer join tr_tit_aut ta on ta.bid = t.bid and ta.tp_responsabilita  = '1' and not ta.fl_canc='S'");
			bufTestataSecondaIntest.append(" inner join tbc_inventario inv on inv.bid=t.bid and inv.fl_canc<>'S' and inv.cd_sit = '2'");
			bufTestataSecondaIntest.append(" and inv.cd_polo='" + esporta.getCodPolo() + "' and inv.cd_bib='" + esporta.getCodBib() + "'");
			bufTestataSecondaIntest.append(" inner join trc_poss_prov_inventari posprovinv on posprovinv.cd_polo=inv.cd_polo");
			bufTestataSecondaIntest.append(" and posprovinv.cd_biblioteca=inv.cd_bib and posprovinv.cd_serie=inv.cd_serie and posprovinv.cd_inven=inv.cd_inven");
			bufTestataSecondaIntest.append(" inner join tbc_possessore_provenienza possprov on posprovinv.pid=possprov.pid");
			bufTestataSecondaIntest.append(" and possprov.ky_cles1_a >= '" + descPossDa + "'");
			bufTestataSecondaIntest.append(" and possprov.ky_cles1_a <= '" + descPossA + "'");
			bufTestataSecondaIntest.append(" inner  join tbc_collocazione col on col.key_loc = inv.key_loc");
			bufTestataSecondaIntest.append(" where (1=1)");

		} else {
			buf.append("( SELECT ").append(props.get("CAMPI_FILTRO")).append(" ").append(props.get(queryName));
		}

		if (isStampaCatalogo
				&& (ValidazioneDati.in(tipoCatalogo, "AUT", "TIP", "SOG", "CLA", "EDI", "POS"))) {
			// MODIFICHE PER RIUNIONE almaviva1/ICCU marzo 2013
			// tutti i filtri generalizzati devono essere salvati perchè saranno applicati più volte
			if (!incrementale)
				bufFiltri.append(" and ").append(props.get("FILTRO_NON_CANCELLATI"));


			//filtri
			aggiungiFiltroLivelloAutorita(bufFiltri, esporta);
			aggiungiFiltroCondivisione(bufFiltri, esporta);
			aggiungiFiltroDescTitolo(bufFiltri, esporta);
			aggiungiFiltroSpecificita(bufFiltri, esporta, "t.tp_materiale");
			aggiungiFiltroTipoRecord(bufFiltri, esporta, "t.tp_record_uni");
			aggiungiFiltroNature(bufFiltri, esporta, "t.cd_natura");
			aggiungiFiltroLingua(bufFiltri, esporta, "t.cd_lingua_1", "t.cd_lingua_2", "t.cd_lingua_3");
			aggiungiFiltroPaese(bufFiltri, esporta, "t.cd_paese");
			aggiungiFiltroTipoData(bufFiltri, esporta);
			aggiungiFiltroTsVar(bufFiltri, esporta, campoFiltroTsVar);
			aggiungiFiltroDataTsVar(bufFiltri, esporta);
			aggiungiFiltroDataTsIns(bufFiltri, esporta);
			aggiungiFiltroUteIns(bufFiltri, esporta);
			aggiungiFiltroUteVar(bufFiltri, esporta);
		} else {
			if (!incrementale)
				buf.append(" and ").append(props.get("FILTRO_NON_CANCELLATI"));

			//filtri
			aggiungiFiltroLivelloAutorita(buf, esporta);
			aggiungiFiltroCondivisione(buf, esporta);

			// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
			// gli estremi di filtro non sono più solo per titolo ma la tabella su cui applicare dipende dal catalogo selezionato
			//almaviva5_20130107 #5218
			if (isStampaCatalogo && tipoCatalogo.equals("TIT")) {
				esporta.setDescTitoloDa(esporta.getCatalogoSelezDa());
				esporta.setDescTitoloA(esporta.getCatalogoSelezA());
			}

			aggiungiFiltroDescTitolo(buf, esporta);
			aggiungiFiltroSpecificita(buf, esporta, "t.tp_materiale");
			aggiungiFiltroTipoRecord(buf, esporta, "t.tp_record_uni");
			aggiungiFiltroNature(buf, esporta, "t.cd_natura");
			aggiungiFiltroLingua(buf, esporta, "t.cd_lingua_1", "t.cd_lingua_2", "t.cd_lingua_3");
			aggiungiFiltroPaese(buf, esporta, "t.cd_paese");
			aggiungiFiltroTipoData(buf, esporta);
			aggiungiFiltroTsVar(buf, esporta, campoFiltroTsVar);
			aggiungiFiltroDataTsVar(buf, esporta);
			aggiungiFiltroDataTsIns(buf, esporta);
			aggiungiFiltroUteIns(buf, esporta);
			aggiungiFiltroUteVar(buf, esporta);
		}


		// Inizio Modifica in test DATI CATALOGO AUTORI PER STAMPA CATALOGHI
		// Nuove richieste per Stampa Cataloghi 07.11.2012 Intervento Interno
		// gli estremi di filtro non sono più solo per titolo ma la tabella su cui applicare dipende dal catalogo selezionato
		// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
		if (tipoCatalogo.equals("AUT")) {
			aggiungiFiltroDescAutore(bufFiltroPrimaIntest, bufFiltroSecondaIntest, esporta);
		} else if (tipoCatalogo.equals("TIP")) {
			aggiungiFiltroDescTipografo(bufFiltroPrimaIntest, bufFiltroSecondaIntest, esporta);
		} else if (tipoCatalogo.equals("EDI")) {
			aggiungiFiltroDescEditore(bufFiltroPrimaIntest, bufFiltroSecondaIntest, esporta);
		} else if (tipoCatalogo.equals("SOG")) {
			aggiungiFiltroDescSoggetto(bufFiltroPrimaIntest, bufFiltroSecondaIntest, esporta);
		} else if (tipoCatalogo.equals("CLA")) {
			aggiungiFiltroDescClasse(bufFiltroPrimaIntest, bufFiltroSecondaIntest, esporta);
		} else if (tipoCatalogo.equals("POS")) {
			// il filtro sulla stringa è gia stato inserito nella select priincipale in INERR JOIN
			aggiungiFiltroDescPossessore(bufFiltroPrimaIntest, bufFiltroSecondaIntest, esporta);
		}


		switch(tipoElaborazione) {
		case TOTALE_PER_BIBLIOTECA:
		case TOTALE_PER_RANGE_COLLOCAZIONI:
		case TOTALE_PER_RANGE_INVENTARI:
		case INCREMENTALE_PER_BIBLIOTECA:
		case INCREMENTALE_PER_RANGE_COLLOCAZIONI:
		case INCREMENTALE_PER_RANGE_INVENTARI:
			//ulteriore filtro su bid posseduti
			if (isStampaCatalogo
					&& (ValidazioneDati.in(tipoCatalogo, "AUT", "TIP", "SOG", "CLA", "EDI", "POS"))) {
				bufFiltri.append(" and exists ( select bp.bid from tt_bid_posseduti bp where bp.bid=t.bid )");
			} else {
				buf.append(" and exists ( select bp.bid from tt_bid_posseduti bp where bp.bid=t.bid )");
			}
		case INCREMENTALE:
		case TOTALE:
			// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
			// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
			// variazione conseguente delle SELECT di estrazione e
			// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
			// INIZIO
			if ( esporta.isSoloDocPosseduti() ) {
				if (isStampaCatalogo
						&& (ValidazioneDati.in(tipoCatalogo, "AUT", "TIP", "SOG", "CLA", "EDI", "POS"))) {
					bufFiltri.append(" and exists ( select bp.bid from tt_bid_posseduti bp where bp.bid=t.bid )");
				} else {
					buf.append(" and exists ( select bp.bid from tt_bid_posseduti bp where bp.bid=t.bid )");
				}
			}
			break;
		}

		//almaviva5_20120911
		//almaviva5_20160223 segnalazione IEI: i bid ignorati non sono gestiti dalla stampa cataloghi
		if (ignora && !isStampaCatalogo)
			buf.append(" and not exists ( select bi.bid from tt_bid_ignorati bi where bi.bid=t.bid )");

		if (isStampaCatalogo
				&& (ValidazioneDati.in(tipoCatalogo, "AUT", "SOG", "CLA", "EDI","POS"))) {
			buf.append(bufTestataPrimaIntest);
			buf.append(bufFiltri);
			buf.append(bufFiltroPrimaIntest);
			buf.append(" UNION ");
			buf.append(bufTestataSecondaIntest);
			buf.append(bufFiltri);
			buf.append(bufFiltroSecondaIntest);
			buf.append(")");
			if (tipoCatalogo.equals("AUT")) {
				buf.append(" order by chiaveOrd1, chiaveord2");
			} else if (tipoCatalogo.equals("SOG") || tipoCatalogo.equals("CLA") || tipoCatalogo.equals("EDI")) {
				buf.append(" order by chiaveOrd1, chiaveord2, chiaveord3");
			} else if (tipoCatalogo.equals("POS")) {
				buf.append(" order by chiaveOrd1, chiaveord2, chiaveord3, inventario");
			}
		} else if (isStampaCatalogo
				&& tipoCatalogo.equals("TIP")) {
			// Luglio 2013 query base su titoli filtrata per autori con responsabilità 4 utilizzando il codice relazione editore 650/tipografo:750
			buf.append(bufTestataPrimaIntest);
			buf.append(bufFiltri);
			buf.append(bufFiltroPrimaIntest);
			buf.append(")");
			buf.append(" order by chiaveOrd1, chiaveord2");
		} else {
			buf.append(")");
		}

	}



	public EsportaVO getEsporta() {
		return esporta;
	}

	public Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = super.getConnection();
			log.debug("open  connection " + connection.toString());
		}

		return connection;
	}

	private void closeConnection(Connection c) throws SQLException {
		if (c == null)
			c = connection;
		if (c != null) {
			log.debug("close connection " + c.toString());
			close(c);
		}
	}

	// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
	// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
	// variazione conseguente delle SELECT di estrazione e
	// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
	// Nuovo metodo
	private void aggiungiFiltroSoloPosseduti(StringBuilder buf, String nomeFiltro,	String campoBid) {

		StringBuilder filtro = new StringBuilder(1024);
		filtro.append(" ( select 1 from tbc_inventario inv ");
		filtro.append("where inv.fl_canc<>'S'");
		filtro.append("  and inv.cd_sit = '2'");
		filtro.append("  and inv.bid=").append(campoBid);
		filtro.append("  and inv.cd_polo='").append(esporta.getCodPolo()).append("'");
		filtro.append(")");

		if (!nomeFiltro.startsWith(":") )
			nomeFiltro = ":" + nomeFiltro;

		int idx = buf.indexOf(nomeFiltro);
		if (idx > -1)
			buf.replace(idx, idx + nomeFiltro.length(), filtro.toString() );
	}
}
