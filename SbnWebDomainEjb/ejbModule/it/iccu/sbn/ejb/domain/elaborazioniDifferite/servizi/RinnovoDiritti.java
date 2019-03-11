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
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.domain.servizi.batch.ServiziBMT;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RinnovoDirittiDiffVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

public class RinnovoDiritti {


	private  Servizi servizi;
	private  ServiziBMT serviziBMT;

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
			this.servizi = factory.getServizi();
			this.serviziBMT = factory.getServiziBMT();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EJBException("Failed to init TextMDB", e);
		} catch (Exception e) {
			throw new EJBException("Failed to init TextMDB", e);
		}
	}
	public RinnovoDiritti() {
		this.ejbCreate();
	}

	private RinnovoDirittiDiffVO rinnovoDirittiVO;

	public RinnovoDiritti(RinnovoDirittiDiffVO rinnDirVO){
		this.rinnovoDirittiVO = rinnDirVO;
		this.ejbCreate();
	}

	public ElaborazioniDifferiteOutputVo rinnovoDiritti(BatchLogWriter log)	{
		System.out.println("++============================================================================++");
		System.out.println("RinnovoDiritti Elaborazioni differite - Inizio di rinnovoDiritti.java metodo RinnovoDiritti");
		System.out.println("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		System.out.println("++============================================================================++");

		//ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo();
		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = null;

		String nomeFileLogIntero="";
		String suffisso="rinnovoDiritti_";

		if (log!=null && log.getFilename()!=null && log.getFilename().getNomeFileVisualizzato()!=null && log.getFilename().getNomeFileVisualizzato().trim().length()>0)
		{
			nomeFileLogIntero=log.getFilename().getNomeFileVisualizzato();
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
		List listaDownload= new ArrayList();
    	//String filename =  "rifiutoSuggerimenti_"+String.valueOf(System.currentTimeMillis())+".htm";
    	String filename =  suffisso+".htm";

    	try {
			FileOutputStream streamOut = new FileOutputStream(rinnovoDirittiVO.getDownloadPath()+ File.separator + filename);
			RinnovoDirittiDiffVO outputVO = this.serviziBMT.gestioneDifferitaRinnovoDiritti(rinnovoDirittiVO);
			elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(outputVO);

			if (outputVO != null){
				for (int i = 0; i < outputVO.getErrori().size(); i++) {
					String stringaMsg = outputVO.getErrori().get(i);
					stringaMsg = stringaMsg + "<br>";
					streamOut.write(stringaMsg.getBytes());
				}
			}
			 streamOut.write("FINE Rinnovo Diritti".getBytes());
			streamOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//listaDownload.add(new DownloadVO(filename, allineaVo.getDownloadPath()+filename));
		listaDownload.add(new DownloadVO(filename, rinnovoDirittiVO.getDownloadLinkPath()+filename));
		elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);

		// Setta i parametri di ricerca in base a EsportaVO
		Map hm = new HashMap();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

		System.out.println("++============================================================================++");
		System.out.println("RinnovoDiritti Elaborazioni differite - Fine di rinnovoDiritti.java metodo RinnovoDiritti");
		System.out.println("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		System.out.println("++============================================================================++");
		return elaborazioniDifferiteOutputVo;
	}

}
