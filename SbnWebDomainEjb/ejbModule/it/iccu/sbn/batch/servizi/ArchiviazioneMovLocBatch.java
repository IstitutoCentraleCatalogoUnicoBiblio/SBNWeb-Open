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

import it.iccu.sbn.ejb.domain.elaborazioniDifferite.servizi.ArchiviazioneMovLoc;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ArchiviazioneMovLocVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;

import javax.transaction.UserTransaction;

public class ArchiviazioneMovLocBatch implements BatchExecutor {



	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log,
			UserTransaction tx) throws Exception {

		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(params) ;

		log.logWriteLine("Sto eseguendo ARCHIVIAZIONE MOVIMENTI LOCALI");

		ArchiviazioneMovLocVO operazioneVO = ((ArchiviazioneMovLocVO) params);

		//ElaborazioniDifferiteOutputVo elaborazioniDifferiteOutputVo = new ElaborazioniDifferiteOutputVo(operazioneSuOrdiniVO);
		ArchiviazioneMovLoc archiviazioneMovLoc = new ArchiviazioneMovLoc(operazioneVO);

		ElaborazioniDifferiteOutputVo outputRinnovo = archiviazioneMovLoc.archiviazioneMovimentiLoc(log);
		ElaborazioniDifferiteOutputVo outputComposto = integraProprieta(output,	outputRinnovo);
		return outputComposto;

		//return  rifiutoSuggerimenti.rifiutoSuggerimenti();
	}

	public boolean validateInput(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {

		return true;
	}

	public ElaborazioniDifferiteOutputVo integraProprieta(ElaborazioniDifferiteOutputVo dest, ElaborazioniDifferiteOutputVo source) {
		dest.setDownloadList(source.getDownloadList());
		dest.setParametriDiRicercaMap(source.getParametriDiRicercaMap());
		dest.setStato(source.getStato());
		dest.setDataDiElaborazione(source.getDataDiElaborazione());
		return dest;
	}


}
