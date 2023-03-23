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

import gnu.trove.THashSet;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodici;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.TipoEstrazioneUnimarc;
import it.iccu.sbn.persistence.dao.bibliografica.TitoloDAO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.batch.SchedulableBatchExecutor;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

public class ExportUnimarcBatch extends ExternalBatchExecutor implements SchedulableBatchExecutor {

	public static final String DB_LAST_EXTRACT_TS = "DB_LAST_EXTRACT_TS";
	private static final String XID_FILE = "bidList.out";
	private static final String BIB_FILE = "bibliotecheDaNonMostrareIn950.txt";
	private static final String TAGS_FILE = "tagsToExport.txt";

	protected static final String PROPS_FILE = "db_extract.properties";

	private AmministrazioneBiblioteca amministrazioneBiblioteca;

	private AmministrazioneGestioneCodici codici;

	private AmministrazioneBiblioteca getAmministrazioneBiblioteca() throws Exception {

		if (amministrazioneBiblioteca != null)
			return amministrazioneBiblioteca;

		this.amministrazioneBiblioteca = DomainEJBFactory.getInstance().getBiblioteca();

		return amministrazioneBiblioteca;

	}

	private AmministrazioneGestioneCodici getCodici() throws Exception {

		if (codici != null)
			return codici;

		this.codici = DomainEJBFactory.getInstance().getCodiciBMT();

		return codici;
	}

	protected AtomicBoolean status_ok = new AtomicBoolean(false);
	protected Logger _log;
	private Timestamp tsLastDBExtraction;
	private UserTransaction tx;

	public static final long getDbLastExtractionTime() {

		Properties props = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(getExportHome() + File.separator + PROPS_FILE);
			props.load(in);
			return Long.valueOf(props.getProperty(DB_LAST_EXTRACT_TS));
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter blw,
			UserTransaction tx) throws Exception {

		EsportaVO richiesta = (EsportaVO) params;

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);
		this.tx = tx;
		_log = blw.getLogger();
		status_ok.set(true);

		//almaviva5_20111027
		boolean isEsporta  = ValidazioneDati.equals(richiesta.getCodAttivita(), CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040);
		boolean isEstraiIdDoc = ValidazioneDati.equals(richiesta.getCodAttivita(), CodiciAttivita.getIstance().ESPORTA_IDENTIFICATIVI_DOCUMENTO);

		//almaviva5_20110628 #4534
		String update = CommonConfiguration.getProperty(Configuration.SBNWEB_EXPORT_UNIMARC_UPDATE_DB_COPY, "auto").trim().toLowerCase();
		if (!update.equals("auto"))
			richiesta.setExportDB(update.equals("yes"));
		//

		//almaviva5_20100210 filtro ateneo
		if (ValidazioneDati.isFilled(richiesta.getAteneo()))
			status_ok.set( impostaFiltroAteneo(richiesta) );

		// estrazione totale del DB
		//almaviva5_20111027 estrazione db solo per unimarc
		if (isEsporta && status_ok.get() && richiesta.isExportDB()) {
			status_ok.set( execFase1_estrazioneDB(blw) == 0 );

			// costruzione indici
			if (status_ok.get())
				status_ok.set( execFase2_makeIndici(blw) == 0 );

			if (status_ok.get())  {
				Properties props = new Properties();
				props.setProperty(DB_LAST_EXTRACT_TS, String.valueOf(DaoManager.now().getTime()) );
				FileOutputStream out = new FileOutputStream(getExportHome() + File.separator + PROPS_FILE);
				props.store(out, "last db export timestamp");
			}
		}

		String filePathName = "";
		String fileName = richiesta.getFirmaBatch() + "_listaIdentificativi";

		if (status_ok.get())
			if (richiesta.getTipoEstrazione() == TipoEstrazioneUnimarc.FILE) // devo leggere i bid da prenotazione
				status_ok.set(scriviFileBID(richiesta) );
			else {
				// Inizio almaviva2 BUG 4701 (collaudo) correzione per stampare la lista di BID e non estrarre record UNIMARC
				if (isEstraiIdDoc) {
					fileName += "_" + richiesta.getTipoOutput() + ".txt";
					filePathName = StampeUtil.getBatchFilesPath() + File.separator + fileName;
				} else {
					filePathName = getDataPrepHome() + File.separator + XID_FILE;
				}

				// Fine almaviva2 BUG 4701 (collaudo) correzione per stampare la lista di BID e non estrarre record UNIMARC

				//cancello il vecchio file lista bid
				File f = new File(filePathName);
				if (f.exists())
					f.delete();

				//Procedo all'estrazione dei bid
				long bidCount = execFase3_estrazioneBID(richiesta, filePathName, blw);
				status_ok.set( (bidCount > 0) );
				if (bidCount == 0) {
					//almaviva5_20150116 nessun bid estratto, batch concluso
					output.setStato(ConstantsJMS.STATO_OK);
					return output;
				}

			}

		// Inizio almaviva2 BUG 4701 (collaudo) correzione per stampare la lista di BID e non estrarre record UNIMARC
		if (status_ok.get() && isEstraiIdDoc) {
			output.addDownload(fileName, filePathName);
			output.setStato(ConstantsJMS.STATO_OK);
			return output;
		}
		// Fine almaviva2 BUG 4701 (collaudo) correzione per stampare la lista di BID e non estrarre record UNIMARC



		//scrittura file per filtro biblioteca
		if (status_ok.get() )
			status_ok.set(preparaFileFiltroBiblioteca(richiesta) );

		//almaviva5_20130226 scrittura file etichette da esportare
		if (status_ok.get() )
			status_ok.set(scriviFileEtichette(richiesta) );

		// export effettivo
		// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
		// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
		// variazione conseguente delle SELECT di estrazione e
		// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
		// INIZIO viene passata anche l'area richiesta che contiene il valore del check suddetto
		if (status_ok.get())
			status_ok.set( execFase4_makeUnimarc(blw, richiesta) == 0);
		// FINE

		// file accessori per OPAC (solo se esiste)
		boolean exportFileAccessori = fileExists(getExportHome(), "exportFileAccessori.sh");
		if (status_ok.get() && exportFileAccessori )
			status_ok.set( execFase5_exportFileAccessori(blw) == 0);

		if (status_ok.get()) {
			String mrcFile = cercaNomeFileExport(richiesta);
			String newMrcFile = richiesta.getFirmaBatch() + ".mrc";
			String zipFile = richiesta.getFirmaBatch() + ".zip";
			String mrcPath = getDataPrepHome() + File.separator + "unimarc";

			//rinomino file mrc da {NOME_POLO}.mrc -> {firmaBatch}.mrc
			status_ok.set(exec(blw, mrcPath, "mv", mrcFile, newMrcFile) == 0);

			List<String> files_to_zip = new ArrayList<String>();
			files_to_zip.add(newMrcFile);

			if (status_ok.get()) {
				//almaviva5_20120911 si aggiungono i file accessori
				String accessori = CommonConfiguration.getProperty(Configuration.EXPORT_UNIMARC_FILE_ACCESSORI);
				if (exportFileAccessori && ValidazioneDati.isFilled(accessori)) {
					_log.debug("export dei file accessori per OPAC SbnWeb");
					for (String fa : accessori.split(";"))
						//almaviva5_20121001 #5126 solo se effettivamente presenti su disco
						if (fileExists(mrcPath, fa) )
							files_to_zip.add(fa.trim());
				}

				// compressione file mrc
				List<String> zip_params = ClonePool.deepCopy(files_to_zip);
				zip_params.add(0, "-9");	//livello compressione (9 == max)
				zip_params.add(1, zipFile);	//nome file zip
				status_ok.set(exec(blw, mrcPath, "zip", zip_params.toArray(new String[0]) ) == 0);
			}

			if (status_ok.get())
				// cancellazione file mrc
				status_ok.set(exec(blw, mrcPath, "rm", files_to_zip.toArray(new String[0]) ) == 0);

			if (status_ok.get())
				output.addDownload(zipFile, mrcPath + File.separator + zipFile);
		}

		if (status_ok.get()) {
			status_ok.set(this.scriviFileUtimoExport(richiesta));
		}

		output.setStato(status_ok.get() ? ConstantsJMS.STATO_OK : ConstantsJMS.STATO_ERROR);

		return output;
	}

	private boolean scriviFileUtimoExport(EsportaVO richiesta) throws Exception {
		final String fileName = getDataPrepHome() + File.separator + "unimarc" + File.separator + "ultimo_exp.txt";
		return FileUtil.writeStringToFile(fileName, richiesta.getIdBatch());
	}

	private String cercaNomeFileExport(EsportaVO richiesta) throws Exception {
		String mrcPath = getDataPrepHome() + File.separator + "unimarc";
		String fname = richiesta.getCodPolo().toLowerCase() + ".mrc";
		if (fileExists(mrcPath, fname))
			return fname;

		//polo maiuscolo
		fname = richiesta.getCodPolo() + ".mrc";
		return fname;
	}

	private boolean impostaFiltroAteneo(EsportaVO richiesta) throws Exception {
		String ateneo = richiesta.getAteneo();
		List<BibliotecaVO> listaBib = getAmministrazioneBiblioteca()
				.getListaBibliotecheAteneoInPolo(richiesta.getCodPolo(), ateneo);

		if (!ValidazioneDati.isFilled(listaBib)) {
			_log.error("Nessuna biblioteca trovata per cod. ateneo='"+ ateneo + "'" );
			return false;
		}

		richiesta.setListaBib(listaBib);
		return true;
	}

	private boolean preparaFileFiltroBiblioteca(EsportaVO richiesta) throws Exception {
		String path = getDataPrepHome() + File.separator + BIB_FILE;
		File f = new File(path);
		if (!richiesta.isSoloPosseduto()) {
			//se non ho filtro su biblioteca cancello il file
			f.delete();
			return true;
		}

		//procedo a scrivere il file con le biblioteche da escludere
		List<BibliotecaVO> listaBibPolo = getAmministrazioneBiblioteca().getListaBibliotechePolo(richiesta.getCodPolo());

		//devo eseguire l'intersezione fra le due liste;
		listaBibPolo.removeAll(richiesta.getListaBib());

		if (!ValidazioneDati.isFilled(listaBibPolo)) {
			//se non ho filtro su biblioteca cancello il file
			f.delete();
			return true;
		}

		//scrivo il file
		StringBuilder buf = new StringBuilder();
		Iterator<BibliotecaVO> i = listaBibPolo.iterator();
		for (;;) {
			buf.append(i.next().getCod_bib());
			if (i.hasNext())
				buf.append(",");
			else
				break;
		}

		String bibEscluse = buf.toString();
		_log.debug("Esclusione etichette 9xx per:" + bibEscluse);
		FileOutputStream o = new FileOutputStream(f);
		o.write(bibEscluse.getBytes());
		o.close();

		return true;
	}

	private boolean scriviFileBID(EsportaVO richiesta) throws Exception {

		String fileName = getDataPrepHome() + File.separator + XID_FILE;
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

		try {
			switch (richiesta.getTipoInput()) {
			case BID:
				for (String bid : richiesta.getListaBID() ) {
					if (!ValidazioneDati.leggiXID(bid)) {
						_log.warn("Identificativo scartato: " + bid);
						continue;
					}
					writer.write(bid);
					writer.newLine();
				}
				break;

			case INV:
				scriviFileBIDInventario(writer, richiesta);
				break;

			default:
				break;
			}

		} finally  {
			FileUtil.close(writer);
		}

		return true;
	}

	private boolean scriviFileEtichette(EsportaVO richiesta) throws Exception {

		String etichette = richiesta.getEtichette();
		if (!ValidazioneDati.isFilled(etichette)) {
			etichette = Constants.EXPORT_UNIMARC_DEFAULT_TAGS;
			_log.warn("Parametro etichette da esportare non valorizzato.");
		}

		String fileName = getDataPrepHome() + File.separator + TAGS_FILE;
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

		try {
			for (String tag : etichette.split(",") ) {
				writer.append("exportTags ");
				writer.append(tag);
				writer.newLine();
			}

		} finally  {
			writer.close();
		}

		_log.debug("Etichette da esportare: " + etichette);
		return true;
	}

	private void scriviFileBIDInventario(BufferedWriter writer,	EsportaVO richiesta) throws Exception {

		TitoloDAO dao = new TitoloDAO();

		String inputFile = richiesta.getInputFile();
		File f = new File(inputFile);
		BatchManager.getBatchManagerInstance().markForDeletion(f);

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

		String codBib = ValidazioneDati.first(richiesta.getListaBib()).getCod_bib();
		Set<String> ids = new THashSet<String>(4096);
		String id = null;

		try {
			DaoManager.begin(tx);
			int written = 0;
			while ((id = reader.readLine()) != null) {
				try {
					InventarioVO inv = InventarioRFIDParser.parse(id);
					//check biblioteca selezionata
					String bibInv = inv.getCodBib();
					if (ValidazioneDati.isFilled(bibInv) && !bibInv.equals(codBib))
						continue;

					inv.setCodBib(codBib);
					String bid = dao.getBidInventario(richiesta.getCodPolo(), inv.getCodBib(), inv.getCodSerie(), inv.getCodInvent());
					if (bid == null) {
						_log.error("inventario non trovato: " + inv.getChiaveInventario());
						continue;
					}

					if (!ids.contains(bid)) {
						writer.write(bid);
						writer.newLine();
						written++;
						ids.add(bid);
					}

				} catch (ValidationException e) {
					_log.error("identificativo rfid errato: " + id);
				}
			}

			_log.debug("identificativi titolo estratti da lista inventari: " + written);

		} finally {
			FileUtil.close(reader);
			DaoManager.endTransaction(tx, true);
		}

	}

	protected int execFase1_estrazioneDB(BatchLogWriter log) throws Exception {

		try {
			_log.debug("execFase1_estrazioneDB()");
			long startTime = 0;//System.currentTimeMillis();
			status_ok.set(false);

			//almaviva5_20130326 evolutive LO1
			tsLastDBExtraction = DaoManager.now();

			String exportHome = getExportHome();
			int exit = exec(log, exportHome, exportHome + File.separator + "export.sh");

			if (exit != 0)
				return exit;

			checkFile(log, startTime, getExportHome() + File.separator + "export.proc");

			return exit;

		} catch (Exception e) {
			status_ok.set(false);
			_log.debug(e);
			throw e;
		}

	}

	protected long execFase3_estrazioneBID(EsportaVO richiesta, String filePathName, BatchLogWriter blw) throws Exception {
		_log.debug("execFase3_estrazioneBID()");
		long bidCount = getAmministrazioneBiblioteca().export(richiesta, filePathName, blw);
		_log.debug("SbnUnimarcBIDExtractor - Numero bid estratti: " + bidCount );
		return bidCount;
	}

	protected int execFase2_makeIndici(BatchLogWriter log) throws Exception {
		_log.debug("execFase2_makeIndici()");
		long startTime = 0;//System.currentTimeMillis();
		int exit = exec(log, getDataPrepHome(), getDataPrepHome() + File.separator + "makeIndici.sh");
		if (exit != 0)
			return exit;

		exit = checkFile(log, startTime, getDataPrepHome() + File.separator + "makeIndici.proc");
		return exit;
	}

	// almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
	// Intervento su Esporta Documenti: inserimento del check per esportare solo i documenti Posseduti
	// variazione conseguente delle SELECT di estrazione e
	// passaggio successivo all'eseguibile dell'argomento --esportaSoloInventariCollocati
	protected int execFase4_makeUnimarc(BatchLogWriter log, EsportaVO richiesta) throws Exception {
		_log.debug("execFase4_makeUnimarc()");
		long startTime = 0;//System.currentTimeMillis();
		int exit;
		if ( richiesta.isSoloDocPosseduti() ) {
			exit = exec(log, getDataPrepHome(), getDataPrepHome() + File.separator + "makeUnimarc.sh", "-t", TAGS_FILE, "-q");
		} else {
			exit = exec(log, getDataPrepHome(), getDataPrepHome() + File.separator + "makeUnimarc.sh", "-t", TAGS_FILE);
		}

		if (exit != 0)
			return exit;

		String mrcPath = getDataPrepHome() + File.separator + "unimarc";
		exit = checkFile(log, startTime, mrcPath + File.separator + "makeUnimarc.proc");
		return exit;
	}

	protected int execFase5_exportFileAccessori(BatchLogWriter log) throws Exception {
		_log.debug("execFase5_exportFileAccessori()");
		long startTime = 0;//System.currentTimeMillis();
		int exit = exec(log, getExportHome(), getExportHome() + File.separator + "exportFileAccessori.sh");
		if (exit != 0)
			return exit;

		exit = checkFile(log, startTime, getExportHome() + File.separator + "exportFileAccessori.proc");
		return exit;
	}

	public boolean validateInput(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {
		return true;
	}

	public static void main (String[] args) {
		System.exit(0);
	}

	public ParametriRichiestaElaborazioneDifferitaVO buildActivationParameters(
			SchedulableBatchVO params, Serializable... otherParams)
			throws Exception {

		_log = Logger.getLogger(ExportUnimarcBatch.class);

		EsportaVO esporta = new EsportaVO();
		esporta.setPayload(params);

		String userId = params.getUser();

		String codPolo = userId.substring(0, 3);
		String codBib = userId.substring(3, 6);
		String user = userId.substring(6);

		esporta.setCodPolo(codPolo);
		esporta.setCodBib(codBib);
		esporta.setUser(user);
		String ticket = SbnSIP2Ticket.getUtenteTicket(codPolo, codBib, user, InetAddress.getLocalHost());
		esporta.setTicket(ticket);

		esporta.setCodAttivita(CodiciAttivita.getIstance().ESPORTA_DOCUMENTI_1040);

		esporta.setExportDB(true);
		//almaviva5_20131106 segnalazione RML: eliminato filtro su tipo materiale
		//esporta.setMateriali(EsportaVO.MATERIALI);
		esporta.setMateriali(null);
		esporta.setNature(EsportaVO.NATURE);
		esporta.setTipoEstrazione(TipoEstrazioneUnimarc.ARCHIVIO);

		//almaviva5_20160322 tipo scarico da tabella
		String tipoScarico = ValidazioneDati.coalesce(params.getTipoScarico(), "ALL");
		_log.debug("tipo scarico unimarc: " + tipoScarico);
		esporta.setCodScaricoSelez(tipoScarico);
		impostaEtichetteDaEsportare(esporta);

		String downloadPath = StampeUtil.getBatchFilesPath();
		esporta.setDownloadPath(downloadPath);
		esporta.setDownloadLinkPath("/"); // eliminato

		//almaviva5_20160308 prepara filtro biblioteche
		List<String> biblioteche = params.getBiblioteche();
		if (ValidazioneDati.isFilled(biblioteche)) {
			_log.debug("preparazione filtro per biblioteca: " + biblioteche);
			List<BibliotecaVO> listaBib = new ArrayList<BibliotecaVO>();
			for (String cdBib : biblioteche) {
				BibliotecaVO bib = DomainEJBFactory.getInstance().getBiblioteca().getBiblioteca(codPolo, cdBib);
				if (bib != null)
					listaBib.add(bib);
			}
			esporta.setListaBib(listaBib);
			//preparaFileFiltroBiblioteca(esporta);
		}

		return esporta;
	}

	public void setStart(ParametriRichiestaElaborazioneDifferitaVO params) throws Exception {
		SchedulableBatchVO sb = (SchedulableBatchVO) params.getPayload();
		if (sb == null)
			return;

		TB_CODICI cod = getCodici().caricaCodice(sb.getJobName(), CodiciType.CODICE_BATCH_PIANIFICABILE);
		sb = new SchedulableBatchVO(cod);

		if (sb.isParziale()) {
			Timestamp start = sb.getLatestSuccessfulEnd();
			EsportaVO esporta = (EsportaVO) params;
			esporta.setTsVar_da(start);
		}

		sb.setLatestStart(DaoManager.now() );

		getCodici().salvaTabellaCodici(sb.getConfig(), false);
	}

	public void setEnd(ParametriRichiestaElaborazioneDifferitaVO params, ElaborazioniDifferiteOutputVo output) throws Exception {

		SchedulableBatchVO sb = (SchedulableBatchVO) params.getPayload();
		if (sb == null)
			return;

		TB_CODICI cod = getCodici().caricaCodice(sb.getJobName(), CodiciType.CODICE_BATCH_PIANIFICABILE);
		sb = new SchedulableBatchVO(cod);

		String stato = output.getStato();
		if (!ValidazioneDati.equals(stato, ConstantsJMS.STATO_OK))
			return;

		sb.setLatestSuccessfulEnd(tsLastDBExtraction);

		getCodici().salvaTabellaCodici(sb.getConfig(), false);
	}

	public static final void impostaEtichetteDaEsportare(EsportaVO esporta) throws Exception {
		//almaviva5_20130226 scelta etichette
		String cdScarico = esporta.getCodScaricoSelez();
		List<TB_CODICI> codici = CodiciProvider.getCodiciCross(CodiciType.CODICE_TIPO_ESTRAZIONE_ETICHETTE_UNIMARC, cdScarico, true);
		if (!ValidazioneDati.isFilled(codici) )
			throw new ValidationException(SbnErrorTypes.UNI_CRITERI_NON_VALIDI);

		Set<String> etichette = new TreeSet<String>();
		for (TB_CODICI c : codici)
			etichette.add(c.getCd_tabellaTrim());

		esporta.setEtichette(ValidazioneDati.formatValueList(new ArrayList<String>(etichette), ",") );

	}

}
