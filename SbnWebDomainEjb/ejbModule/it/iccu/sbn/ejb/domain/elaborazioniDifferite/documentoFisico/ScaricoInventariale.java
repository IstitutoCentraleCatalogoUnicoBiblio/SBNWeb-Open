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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoBMT;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.documentofisico.ScaricoInventarialeVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
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

public class ScaricoInventariale {


	private DocumentoFisicoBMT documentoFisicoBMT;

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
			this.documentoFisicoBMT = DomainEJBFactory.getInstance().getDocumentoFisicoBMT();
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
	public ScaricoInventariale() {
		this.ejbCreate();
	}

	private ScaricoInventarialeVO scaricoInventarialeVO;

	public ScaricoInventariale(ScaricoInventarialeVO scaricoInventarialeVO, BatchLogWriter log){
		this.scaricoInventarialeVO = scaricoInventarialeVO;
		this.ejbCreate();
	}

	public ElaborazioniDifferiteOutputVo scaricoInventariale(ScaricoInventarialeVO scaricoInventarialeVO, BatchLogWriter log)
	throws RemoteException{
		System.out.println("++============================================================================++");
		System.out.println("ScaricoInventariale Elaborazioni differite - Inizio di ScaricoInventariale.java metodo scaricoInventariale");
		System.out.println("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		System.out.println("++============================================================================++");

		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(scaricoInventarialeVO);

		// Crea lista dei file da scaricare
		List listaDownload= new ArrayList();
    	String filename =  scaricoInventarialeVO.getFirmaBatch()+"_scaricoInv_"+".htm";
		ScaricoInventarialeVO outputAllineamentoVO = this.documentoFisicoBMT.getScaricoInventariale(scaricoInventarialeVO, log);

    	try {
			FileOutputStream streamOut = new FileOutputStream(scaricoInventarialeVO.getDownloadPath()+ File.separator + filename);
			if (outputAllineamentoVO != null){
        		streamOut.write("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">".getBytes("UTF-8"));
				for (int i = 0; i < outputAllineamentoVO.getErrori().size(); i++) {
					String stringaMsg = outputAllineamentoVO.getErrori().get(i);
					stringaMsg = stringaMsg + "<br>";
					streamOut.write(stringaMsg.getBytes("UTF-8"));
				}
			}

			 streamOut.write("Scarico Inventariale fine".getBytes());
			streamOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//listaDownload.add(new DownloadVO(filename, allineaVo.getDownloadPath()+filename));
		listaDownload.add(new DownloadVO(filename, scaricoInventarialeVO.getDownloadLinkPath()+filename));
		elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);

		// Setta i parametri di ricerca in base a EsportaVO
		Map hm = new HashMap();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

		System.out.println("++============================================================================++");
		System.out.println("ScaricoInventariale Elaborazioni differite - Fine di ScaricoInventariale.java metodo scaricoInventariale");
		System.out.println("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		System.out.println("++============================================================================++");
		return elaborazioniDifferiteOutputVo;
	}

}
