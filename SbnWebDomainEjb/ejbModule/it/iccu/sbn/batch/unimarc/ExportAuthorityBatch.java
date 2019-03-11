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

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaAuthorityVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.UserTransaction;

public class ExportAuthorityBatch extends ExportUnimarcBatch {

	private static final String MARC_AU_FILE = "autori.mrc";
	private static final String EXPORT_FILE_ACCESSORI_AUTORI = "exportFileAccessoriAutori.sh";
	private static final String VID_FILE = "vidXUnimarcSbw.txt";

	@Override
	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log,
			UserTransaction tx) throws Exception {

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);
		EsportaAuthorityVO esporta = (EsportaAuthorityVO) params;

		switch (esporta.getAuthority().getType()) {
		case SbnAuthority.AU_TYPE: //autori
			return esportaAutori(prefissoOutput, params, log);

		default:
			//authority non gestita
			output.setStato(ConstantsJMS.STATO_ERROR);
			log.getLogger().error("Tipo authority non gestita: " + esporta.getAuthority().toString());
		}

		return output;
	}

	private ElaborazioniDifferiteOutputVo esportaAutori(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter blw)
			throws Exception {

		EsportaAuthorityVO richiesta = (EsportaAuthorityVO) params;
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params);
		status_ok.set(true);
		_log = blw.getLogger();

		//almaviva5_20110628 #4534
		String update = CommonConfiguration.getProperty(Configuration.SBNWEB_EXPORT_UNIMARC_UPDATE_DB_COPY, "auto").trim().toLowerCase();
		if (!update.equals("auto"))
			richiesta.setExportDB(update.equals("yes"));
		//

		// estrazione totale del DB
		//almaviva5_20111027 estrazione db solo per unimarc
		if (status_ok.get() && richiesta.isExportDB()) {
			status_ok.set( execFase1_estrazioneDB(blw) == 0 );

			// costruzione indici
			if (status_ok.get())
				status_ok.set( execFase2_makeIndici(blw) == 0 );

			if (status_ok.get())  {
				Properties props = new Properties();
				props.setProperty(DB_LAST_EXTRACT_TS, String.valueOf(System.currentTimeMillis()) );
				FileOutputStream out = new FileOutputStream(getExportHome() + File.separator + PROPS_FILE);
				props.store(out, "last db export timestamp");
			}
		}

		String inputFile = richiesta.getInputFile();
		String filePathName = getDataPrepHome() + File.separator + VID_FILE;
		File f = new File(inputFile);
		StampeUtil.copy(f, new File(filePathName));
		BatchManager.getBatchManagerInstance().markForDeletion(f);

		// export effettivo
		if (status_ok.get())
			status_ok.set( exec_makeUnimarcAutori(blw) == 0);

		// file accessori per OPAC (solo se esiste)
		boolean exportFileAccessori = fileExists(getExportHome(), EXPORT_FILE_ACCESSORI_AUTORI);
		if (status_ok.get() && exportFileAccessori )
			status_ok.set( exec_exportFileAccessoriAutori(blw) == 0);

		if (status_ok.get()) {
			String mrcPath = getDataPrepHome() + File.separator + "unimarc";
			String prefix = richiesta.getFirmaBatch() + '_' + richiesta.getAuthority();
			String newMrcFile = prefix + ".mrc";
			String zipFile = prefix + ".zip";

			//rinomino file mrc da autori.mrc -> {firmaBatch}.mrc
			status_ok.set(exec(blw, mrcPath, "mv", MARC_AU_FILE, newMrcFile) == 0);

			List<String> files_to_zip = new ArrayList<String>();
			files_to_zip.add(newMrcFile);

			if (status_ok.get()) {
				//almaviva5_20121023 si aggiungono i file accessori
				String accessori = CommonConfiguration.getProperty(Configuration.EXPORT_UNIMARC_AUTORI_FILE_ACCESSORI);
				if (exportFileAccessori && ValidazioneDati.isFilled(accessori)) {
					_log.debug("export dei file accessori per OPAC SbnWeb");
					for (String fa : accessori.split(";"))
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
				status_ok.set(exec(blw, mrcPath, "rm", newMrcFile) == 0);

			if (status_ok.get())
				output.addDownload(zipFile, mrcPath + File.separator + zipFile);
		}

		output.setStato(status_ok.get() ? ConstantsJMS.STATO_OK : ConstantsJMS.STATO_ERROR);

		return output;

	}

	private int exec_makeUnimarcAutori(BatchLogWriter blw) throws Exception {
		_log.debug("exec_makeUnimarcAutori()");
		long startTime = 0;//System.currentTimeMillis();
		int exit = exec(blw, getDataPrepHome(), getDataPrepHome() + File.separator + "makeUnimarcAuthority.sh");
		if (exit != 0)
			return exit;

		String mrcPath = getDataPrepHome() + File.separator + "unimarc";
		exit = checkFile(blw, startTime, mrcPath + File.separator + "makeUnimarcAuthority.proc");
		return exit;
	}

	private int exec_exportFileAccessoriAutori(BatchLogWriter blw) throws Exception {
		_log.debug("exec_exportFileAccessoriAutori()");
		long startTime = 0;//System.currentTimeMillis();
		int exit = exec(blw, getExportHome(), getExportHome() + File.separator + EXPORT_FILE_ACCESSORI_AUTORI);
		if (exit != 0)
			return exit;

		exit = checkFile(blw, startTime, getExportHome() + File.separator + "exportFileAccessoriAutori.proc");
		return exit;
	}

}
