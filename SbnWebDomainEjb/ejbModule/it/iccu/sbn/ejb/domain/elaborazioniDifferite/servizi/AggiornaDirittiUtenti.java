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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AggiornaDirittiUtenteVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class AggiornaDirittiUtenti {


	private ServiziBMT serviziBMT;

	private ServiziBMT getServizi() throws Exception {

		if (serviziBMT == null) {
			this.serviziBMT = DomainEJBFactory.getInstance().getServiziBMT();
		}
		return serviziBMT;
	}

	private AggiornaDirittiUtenteVO aggiornaDirittiUtentiVO;

	public AggiornaDirittiUtenti(AggiornaDirittiUtenteVO aggDirUteVO) {
		this.aggiornaDirittiUtentiVO = aggDirUteVO;
	}

	public ElaborazioniDifferiteOutputVo aggiornaDirittiUtenti(BatchLogWriter blw)	{
		System.out.println("++============================================================================++");
		System.out.println("AggiornaDirittiUtente Elaborazioni differite - Inizio di aggiornaDirittiUtenti.java metodo AggiornaDirittiUtenti");
		System.out.println("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		System.out.println("++============================================================================++");

		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = null;

		String nomeFileLogIntero="";
		String suffisso="aggiornaDiritti_";

		Logger _log = blw.getLogger();

		if (blw != null
				&& blw.getFilename() != null
				&& ValidazioneDati.isFilled(blw.getFilename().getNomeFileVisualizzato()) ) {
			nomeFileLogIntero=blw.getFilename().getNomeFileVisualizzato();
			int pos=nomeFileLogIntero.lastIndexOf(".");
			String nomeFileLogParziale="";
			if (pos > 0)
				nomeFileLogParziale = nomeFileLogIntero.substring(0, pos);

			if (!nomeFileLogParziale.equals(""))
				suffisso = nomeFileLogParziale;

		}

		String filename = suffisso + ".htm";

    	try {
			FileOutputStream streamOut = new FileOutputStream(aggiornaDirittiUtentiVO.getDownloadPath()+ File.separator + filename);
			AggiornaDirittiUtenteVO outputVO = getServizi().gestioneDifferitaAggiornamentoDirittiUtente(aggiornaDirittiUtentiVO, blw);
			elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(outputVO);

			if (outputVO != null) {
				for (int i = 0; i < outputVO.getErrori().size(); i++) {
					String stringaMsg = outputVO.getErrori().get(i);
					stringaMsg = stringaMsg + "<br>";
					streamOut.write(stringaMsg.getBytes());
				}
			}
			streamOut.write("FINE Aggiorna Diritti Utenti".getBytes());
			streamOut.close();

		} catch (Exception e) {
			_log.error("", e);
		}


		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		listaDownload.add(new DownloadVO(filename, aggiornaDirittiUtentiVO.getDownloadLinkPath() + filename));
		elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);

		// Setta i parametri di ricerca in base a EsportaVO
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.getDate()+DateUtil.getTime());

		System.out.println("++============================================================================++");
		System.out.println("AggiornaDirittiUtente Elaborazioni differite - Fine di aggiornaDirittiUtenti.java metodo AggiornaDirittiUtenti");
		System.out.println("Sone le " + DateUtil.getDate()+DateUtil.getTime());
		System.out.println("++============================================================================++");
		return elaborazioniDifferiteOutputVo;
	}

}
