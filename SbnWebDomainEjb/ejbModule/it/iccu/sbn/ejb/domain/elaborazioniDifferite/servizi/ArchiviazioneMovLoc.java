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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.servizi.batch.ServiziBMT;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ArchiviazioneMovLocVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class ArchiviazioneMovLoc {

	private static Logger log = Logger.getLogger(ArchiviazioneMovLoc.class);

	private ServiziBMT serviziBMT;

	/**
	 * Required creation method for message-driven beans.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @ejb.create-method <!-- end-xdoclet-definition -->
	 * @generated
	 */
	public void ejbCreate() {
		try {

			this.serviziBMT = DomainEJBFactory.getInstance().getServiziBMT();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (RemoteException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
		} catch (CreateException e) {

			log.error("", e);
			throw new EJBException("Failed to init TextMDB", e);
		} catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}
	public ArchiviazioneMovLoc() {
		this.ejbCreate();
	}

	private ArchiviazioneMovLocVO richiesta;

	public ArchiviazioneMovLoc(ArchiviazioneMovLocVO archMovLocVO){
		this.richiesta = archMovLocVO;
		this.ejbCreate();
	}

	public ElaborazioniDifferiteOutputVo archiviazioneMovimentiLoc(BatchLogWriter blw)	{
		Logger _log = blw.getLogger();

		_log.debug("++============================================================================++");
		_log.debug("ArchiviazioneMovLoc Elaborazioni differite - Inizio di ArchiviazioneMovLoc.java metodo archiviazioneMovimentiLoc");
		_log.debug("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		_log.debug("++============================================================================++");

		//ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = null;

		String nomeFileLogIntero="";
		String suffisso="archiviazioneMovLoc_";

		if (blw!=null && blw.getFilename()!=null && blw.getFilename().getNomeFileVisualizzato()!=null && blw.getFilename().getNomeFileVisualizzato().trim().length()>0)
		{
			nomeFileLogIntero=blw.getFilename().getNomeFileVisualizzato();
			int pos=nomeFileLogIntero.lastIndexOf(".");
			String nomeFileLogParziale="";
			if (pos>0)
			{
				nomeFileLogParziale=nomeFileLogIntero.substring(0,pos);
			}
			if (!nomeFileLogParziale.equals(""))
			{
				suffisso=nomeFileLogParziale;
			}
		}

    	String filename =  suffisso+".htm";

    	try {
    		FileOutputStream fos = new FileOutputStream(richiesta.getDownloadPath() + File.separator + filename);
			PrintWriter streamOut = new PrintWriter(new OutputStreamWriter(fos, "UTF8"), true);

			ArchiviazioneMovLocVO outputVO = null;
			outputVO = this.serviziBMT.gestioneDifferitaArchiviazioneMovLoc(richiesta, blw);

			if (outputVO == null) {
				outputVO = new ArchiviazioneMovLocVO();
				List<String> errori = outputVO.getErrori();
				errori.add("SVECCHIAMENTO MOVIMENTI LOCALI");
				errori.add("CODICE BIBLIOTECA: " + richiesta.getCodBib() + "          DATA: " +  richiesta.getDataSvecchiamento());
				errori.add("DATA EFFETTUAZIONE: " + dateToString(Calendar.getInstance().getTime()));
				errori.add("NON SONO PRESENTI MOVIMENTI DA SVECCHIARE");

			}
			elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(outputVO);

			if (outputVO != null) {
				List<String> errori = outputVO.getErrori();
				for (String stringaMsg : errori) {
					stringaMsg = stringaMsg + "<br>";
					streamOut.write(stringaMsg);
				}
			}

			streamOut.write("FINE Archiviazione Movimenti Locali");
			FileUtil.close(streamOut);

		} catch (FileNotFoundException e) {

			_log.error("", e);
		} catch (IOException e) {

			_log.error("", e);
		}

		//listaDownload.add(new DownloadVO(filename, allineaVo.getDownloadPath()+filename));
		elaborazioniDifferiteOutputVo.addDownload(filename, richiesta.getDownloadLinkPath() + filename);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

		_log.debug("++============================================================================++");
		_log.debug("ArchiviazioneMovLoc Elaborazioni differite - Fine di ArchiviazioneMovLoc.java metodo archiviazioneMovimentiLoc");
		_log.debug("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		_log.debug("++============================================================================++");

		return elaborazioniDifferiteOutputVo;
	}

	private static final String dateToString(java.util.Date data) {
		if (data == null)
			return "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String value = format.format(data);

		return value;
	}

}
