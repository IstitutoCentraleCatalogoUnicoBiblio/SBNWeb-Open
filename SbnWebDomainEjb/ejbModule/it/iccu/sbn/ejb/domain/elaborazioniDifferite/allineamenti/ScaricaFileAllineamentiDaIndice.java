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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.allineamenti;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoStampaOutputAllineamentoVO;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ScaricaFileAllineamentiDaIndice {

	public ElaborazioniDifferiteOutputVo execute(AllineaVO input, BatchLogWriter blw) throws Exception {
		blw.logWriteLine("++============================================================================++");
		blw.logWriteLine("Salvataggio file XML Indice per allineamento - Inizio");
		blw.logWriteLine("Sone le " + DateUtil.now() );
		blw.logWriteLine("++============================================================================++");

		Logger log = blw.getLogger();

		ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(input);

		// Crea lista dei file da scaricare
		List<DownloadVO> listaDownload = new ArrayList<DownloadVO>();
		String filename = input.getFirmaBatch() + ".htm";

		AllineaVO output = DomainEJBFactory.getInstance().getSrvBibliografica().scaricaFileAllineamentoBaseLocale(input,
				input.getTicket());

		BufferedWriter w = null;
		try {
			FileOutputStream out = new FileOutputStream(input.getDownloadPath() + File.separator + filename);
			w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

			List<TracciatoStampaOutputAllineamentoVO> logAnalitico = output.getLogAnalitico();
			int size = logAnalitico.size();

			for (int i = 0; i < size; i++) {
				TracciatoStampaOutputAllineamentoVO outputAllineamentoVO = logAnalitico.get(i);
				w.write(outputAllineamentoVO.getStringaToPrint());
			}

		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} finally {
			FileUtil.close(w);
		}

		// listaDownload.add(new DownloadVO(filename,
		// allineaVo.getDownloadPath()+filename));
		listaDownload.add(new DownloadVO(filename, input.getDownloadLinkPath() + filename));
		elaborazioniDifferiteOutputVo.setDownloadList(listaDownload);

		// Setta i parametri di ricerca in base a EsportaVO
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("campo", "valore");
		elaborazioniDifferiteOutputVo.setParametriDiRicercaMap(hm);

		elaborazioniDifferiteOutputVo.setStato(ConstantsJMS.STATO_OK);
		elaborazioniDifferiteOutputVo.setDataDiElaborazione(DateUtil.now() );

		blw.logWriteLine("++============================================================================++");
		blw.logWriteLine("Salvataggio file XML Indice per allineamento - Fine");
		blw.logWriteLine("Sone le " + DateUtil.getDate() + DateUtil.getTime());
		blw.logWriteLine("++============================================================================++");

		return elaborazioniDifferiteOutputVo;
	}

}
