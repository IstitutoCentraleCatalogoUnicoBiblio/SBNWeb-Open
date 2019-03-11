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
package it.iccu.sbn.batch;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoBMT;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.SpostamentoCollocazioni;
import it.iccu.sbn.ejb.vo.documentofisico.SpostamentoCollocazioniVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;

import javax.transaction.UserTransaction;

public class SpostamentoCollocazioniBatch implements BatchExecutor {

	DocumentoFisicoBMT getEjb() throws Exception {
		return DomainEJBFactory.getInstance().getDocumentoFisicoBMT();
	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log, UserTransaction tx)
			throws Exception {

		log.logWriteLine("Sto eseguendo il batch Spostamento Collocazioni");
		ElaborazioniDifferiteOutputVo output = spostamentoCollocazioni((SpostamentoCollocazioniVO) params, log);
		return output;
	}

	public boolean validateInput(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {

		return true;
	}

	private ElaborazioniDifferiteOutputVo spostamentoCollocazioni(SpostamentoCollocazioniVO spostamentoCollocazioniVO, BatchLogWriter log) throws java.rmi.RemoteException {

		SpostamentoCollocazioni spostamentoCollocazioni = new SpostamentoCollocazioni(spostamentoCollocazioniVO, log);
			return  spostamentoCollocazioni.spostaCollocazioni(spostamentoCollocazioniVO, log);

	}
}
