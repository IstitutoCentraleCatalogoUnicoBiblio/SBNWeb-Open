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
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AggiornamentoDisponibilita;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.servizi.batch.BatchExecutor;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.vo.domain.CodiciAttivita;

import java.rmi.RemoteException;

import javax.transaction.UserTransaction;

public class AggiornamentoDisponibilitaBatch implements BatchExecutor {

	DocumentoFisicoBMT getEjb() throws Exception {
		return DomainEJBFactory.getInstance().getDocumentoFisicoBMT();
	}

	public ElaborazioniDifferiteOutputVo execute(String prefissoOutput,
			ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log,
			UserTransaction tx)	throws Exception {

		ElaborazioniDifferiteOutputVo output = null;
		String codAttivita = params.getCodAttivita();
		if (CodiciAttivita.getIstance().GDF_AGGIORNAMENTO_DISPONIBILITA_INVENTARI.equals(codAttivita))
			output = aggiornaDisponibilita((AggDispVO) params, log);

		//almaviva5_20130916 evolutive google2
		if (CodiciAttivita.getIstance().IMPORTA_URI_COPIA_DIGITALE.equals(codAttivita))
			output = acquisizioneUriCopiaDigitale((AcquisizioneUriCopiaDigitaleVO) params, log);

		return output;
	}

	//almaviva5_20130916 evolutive google2
	private ElaborazioniDifferiteOutputVo acquisizioneUriCopiaDigitale(
			AcquisizioneUriCopiaDigitaleVO richiesta, BatchLogWriter log) throws Exception {
		log.logWriteLine("Sto eseguendo Acquisizione URI Copia Digitale");
		return getEjb().acquisizioneUriCopiaDigitale(richiesta, log);
	}

	public boolean validateInput(ParametriRichiestaElaborazioneDifferitaVO params, BatchLogWriter log) {

		return true;
	}

	private ElaborazioniDifferiteOutputVo aggiornaDisponibilita(AggDispVO aggDispVO, BatchLogWriter log) throws RemoteException {
		log.logWriteLine("Sto eseguendo il batch Aggiornamento Disponibilità");
		AggiornamentoDisponibilita aggiornamentoDisponibilita = new AggiornamentoDisponibilita(aggDispVO, log);
		return aggiornamentoDisponibilita.aggiornaDisponibilita(aggDispVO, log);

	}
}
