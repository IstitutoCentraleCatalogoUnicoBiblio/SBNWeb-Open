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
package it.iccu.sbn.batch.servizi;

import it.iccu.sbn.batch.unimarc.ExternalBatchExecutor;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchImportaUtentiVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

public class BatchImportaUtenti extends ExternalBatchExecutor {

	public static final String DB_LAST_EXTRACT_TS = "DB_LAST_EXTRACT_TS";
	private static final String QUERY_MODEL = "getBidList.model";
	private static final String QUERY_CONF_FILE = "getBidList.txt";
	private static final String PROPS_FILE = "db_extract.properties";
	private static final String XID_FILE = "bidList.out";
	private static final String BIB_FILE = "bibliotecheDaNonMostrareIn950.txt";

	private AmministrazioneBiblioteca amministrazioneBiblioteca;

	private AmministrazioneBiblioteca getAmministrazioneBiblioteca() throws Exception {

		if (amministrazioneBiblioteca != null)
			return amministrazioneBiblioteca;

		InitialContext ctx = JNDIUtil.getContext();
		this.amministrazioneBiblioteca = DomainEJBFactory.getInstance().getBiblioteca();

		return amministrazioneBiblioteca;

	}

	Logger _log;
	private AtomicBoolean status_ok = new AtomicBoolean(false);

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
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log, UserTransaction tx)
			throws Exception {

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);

		_log = log.getLogger();

		status_ok.set(true);
		String filePathName = "";

		// export effettivo
		if (status_ok.get())
			status_ok.set( execFase1_importUtenti(log) == 0);

		output.setStato(status_ok.get() ? ConstantsJMS.STATO_OK : ConstantsJMS.STATO_ERROR);
		return output;

		}


	private String cercaNomeFileExport(EsportaVO richiesta) throws Exception {
		String mrcPath = getDataPrepHome() + File.separator + "unimarc";
		String fname = richiesta.getCodPolo().toLowerCase() + ".mrc";
		if (new File(mrcPath + File.separator + fname).exists())
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
			for (String bid : richiesta.getListaBID() ) {
				if (!ValidazioneDati.leggiXID(bid)) {
					_log.warn("Identificativo scartato: " + bid);
					continue;
				}
				writer.write(bid);
				writer.newLine();
			}
		} finally  {
			writer.close();
		}

		return true;
	}


	private int execFase1_importUtenti(BatchLogWriter log) throws Exception {

		try {
			_log.debug("execFase1_importUtenti()");
			long startTime = 0;			//System.currentTimeMillis();
			status_ok.set(false);


			String exportHome = getImportaUtentiHome();
			//int exit = exec(log, exportHome, exportHome + File.separator + "importaUtenti.sh");
			//int exit = exec(log, exportHome, exportHome + File.separator + "importaUtenti.bat");
			//String nome = exportHome +"\\"+ ParametriBatchImportaUtentiVO.nomeF;
			//String fileConfig = exportHome +"\\ImportaUtentiLettori.cfg";
			String nome = exportHome +"/"+ ParametriBatchImportaUtentiVO.nomeF;
			String fileConfig = exportHome +"/ImportaUtentiLettori.cfg";
			String dt = ParametriBatchImportaUtentiVO.dataDa;
//			int exit = exec(log, exportHome, exportHome + File.separator + "importaUtenti.bat", fileConfig, nome, dt);
			int exit = exec(log, exportHome, exportHome + File.separator + "importaUtenti.sh", fileConfig, nome, dt);

			if (exit != 0)
				return exit;


			return exit;

		} catch (Exception e) {
			status_ok.set(false);
			_log.debug(e);
			throw e;
		}

	}


	private int execFase1_estrazioneDB(BatchLogWriter log) throws Exception {

		try {
			_log.debug("execFase1_estrazioneDB()");
			long startTime = 0;//System.currentTimeMillis();
			status_ok.set(false);

			String exportHome = getExportHome();
			int exit = exec(log, exportHome, exportHome + File.separator + "export.sh");

			if (exit != 0)
				return exit;

			checkFile(log, startTime, getExportHome() + File.separator + "export.proc");

			Properties props = new Properties();
			props.setProperty(DB_LAST_EXTRACT_TS, String.valueOf(System.currentTimeMillis()) );
			FileOutputStream out = new FileOutputStream(getExportHome() + File.separator + PROPS_FILE);
			props.store(out, "last db export timestamp");

			return exit;

		} catch (Exception e) {
			status_ok.set(false);
			_log.debug(e);
			throw e;
		}

	}

	protected void execFase2_writeQueryModel(String query) throws Exception {

		_log.debug("execFase2_writeQueryModel()");
		File f = new File(getExportHome() + File.separator + QUERY_MODEL);

		_log.debug("query: " + query);

		InputStream is = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;

		OutputStream out = new FileOutputStream(getExportHome() + File.separator + QUERY_CONF_FILE);
		OutputStreamWriter w = new OutputStreamWriter(out);

		while ((line = br.readLine()) != null) {
			if (!line.startsWith("query"))
				w.write(line+"\n");
			else
				w.write("query=\"" + query + "\"\n");
		}
		w.close();
		br.close();
	}

	protected int execFase3_estrazioneBID(BatchLogWriter log) throws Exception {
		_log.debug("execFase3_estrazioneBID()");
		long startTime = 0;//System.currentTimeMillis();
		int exit = exec(log, getExportHome(), getExportHome() + File.separator + "getBidList.sh");
		if (exit != 0)
			return exit;

		exit = checkFile(log, startTime, getExportHome() + File.separator + "getBidList.proc");
		return exit;
	}

	protected int execFase4_makeIndici(BatchLogWriter log) throws Exception {
		_log.debug("execFase4_makeIndici()");
		long startTime = 0;//System.currentTimeMillis();
		int exit = exec(log, getDataPrepHome(), getDataPrepHome() + File.separator + "makeIndici.sh");
		if (exit != 0)
			return exit;

		exit = checkFile(log, startTime, getDataPrepHome() + File.separator + "makeIndici.proc");
		return exit;
	}

	protected int execFase5_makeUnimarc(BatchLogWriter log) throws Exception {
		_log.debug("execFase5_makeUnimarc()");
		long startTime = 0;//System.currentTimeMillis();
		int exit = exec(log, getDataPrepHome(), getDataPrepHome() + File.separator + "makeUnimarc.sh");
		if (exit != 0)
			return exit;

		String mrcPath = getDataPrepHome() + File.separator + "unimarc";
		exit = checkFile(log, startTime, mrcPath + File.separator + "makeUnimarc.proc");
		return exit;
	}

	protected int execFase6_exportFileAccessori(BatchLogWriter log) throws Exception {
		_log.debug("execFase6_exportFileAccessori()");
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

}
