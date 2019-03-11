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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.acquisizioni;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBMT;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class OperazioneSuOrdini {

	private AcquisizioniBMT acquisizioniBMT;

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
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			this.acquisizioniBMT = factory.getAcquisizioniBMT();

		} catch (NamingException e) {

			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (RemoteException e) {

			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (CreateException e) {

			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}
	public OperazioneSuOrdini() {
		this.ejbCreate();
	}

	private OperazioneSuOrdiniMassivaVO operazioneSuOrdiniVO;

	public OperazioneSuOrdini(OperazioneSuOrdiniMassivaVO operazioneSuOrdiniVO){
		this.operazioneSuOrdiniVO = operazioneSuOrdiniVO;
		this.ejbCreate();
	}

	public ElaborazioniDifferiteOutputVo operazioneSuOrdini(BatchLogWriter blw)	{
		Logger log = blw.getLogger();
		log.debug("++============================================================================++");
		log.debug("OperazioneSuOrdini Elaborazioni differite - Inizio di OperazioneSuOrdini.java metodo operazioneSuOrdini");
		log.debug("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		log.debug("++============================================================================++");

		//ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo =null;

		String nomeFileLogIntero="";
		String suffisso="operazioneSuOrdini_";

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

		// Crea lista dei file da scaricare
		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
    	//String filename =  suffisso+String.valueOf(System.currentTimeMillis())+".htm";
    	String filename =  suffisso+".htm";
    	BufferedWriter w = null;
    	try {
			FileOutputStream out = new FileOutputStream(operazioneSuOrdiniVO.getDownloadPath()+ File.separator + filename);
			w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writeReportHeader(w);
			OperazioneSuOrdiniMassivaVO outputAllineamentoVO = this.acquisizioniBMT.gestioneDifferitaOperazioniSuOrdineMassiva(operazioneSuOrdiniVO);
			// eventuale indagine  arraylist errori per la scrittura del log
			//outputAllineamentoVO.getErrori();
			elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(outputAllineamentoVO);

			if (outputAllineamentoVO != null){
				List<String> errori = outputAllineamentoVO.getErrori();
				int size = errori.size();
				for (int i = 0; i < size; i++) {
					String stringaMsg = errori.get(i);
					stringaMsg = stringaMsg + "<br/>";
					w.write(stringaMsg);
				}
			}

			w.write("FINE Operazione Su Ordini");
			writeReportFooter(w);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			FileUtil.close(w);
		}

		//listaDownload.add(new DownloadVO(filename, allineaVo.getDownloadPath()+filename));
		listaDownload.add(new DownloadVO(filename, operazioneSuOrdiniVO.getDownloadLinkPath()+filename));
		elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);

		// Setta i parametri di ricerca in base a EsportaVO
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

		log.debug("++============================================================================++");
		log.debug("OperazioneSuOrdini Elaborazioni differite - Fine di OperazioneSuOrdini.java metodo operazioneSuOrdini");
		log.debug("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		log.debug("++============================================================================++");
		return elaborazioniDifferiteOutputVo;
	}

	private void writeReportHeader(Writer w) throws IOException {
		w.append("<!DOCTYPE html>");
		w.append("<html>");
		w.append("<head>");
		w.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		w.append("<title>Operazione su Ordini</title>");
		w.append("</head>");
		w.append("<body>");
}

	private void writeReportFooter(Writer w) throws IOException {
		w.append("</body>");
		w.append("</html>");
	}

}
