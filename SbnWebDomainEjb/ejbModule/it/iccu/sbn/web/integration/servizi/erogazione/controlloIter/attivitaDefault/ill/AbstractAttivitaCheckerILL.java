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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StatusOrErrorReportType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.TransactionIdType;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractAttivitaCheckerBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public abstract class AbstractAttivitaCheckerILL extends AbstractAttivitaCheckerBase {

	public AbstractAttivitaCheckerILL() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	protected ControlloAttivitaServizioResult checkResponse(ILLAPDU response,
			DatiControlloVO dati) {
		DatiRichiestaILLVO datiILL = dati.getMovimento().getDatiILL();
		if (response != null) {
			StatusOrErrorReportType statusOrErrorReport = response.getStatusOrErrorReport();
			if (statusOrErrorReport != null) {
				String errorReport = statusOrErrorReport.getErrorReport();
				if (ValidazioneDati.isFilled(errorReport) && !errorReport.equals("0")) {
					dati.getCodiciMsgSupplementari().add(errorReport);
					return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
				}
				//cod. richiesta ill
				TransactionIdType tid = statusOrErrorReport.getTransactionId();
				if (tid != null)
					datiILL.setTransactionId(Long.valueOf(tid.getValue()));
			}
		}
/*
		CommandInvokeVO command = CommandInvokeVO.build(dati.getTicket(), CommandType.SRV_ILL_XML_REQUEST, response);
		try {
			CommandResultVO result = this.delegate.getGestioneServizi().invoke(command);
		} catch (Exception e) {
			log.error("", e);
			return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
		}
*/
		//aggiorno stato ill
		datiILL.setCurrentState(dati.getControlloEseguito().getISOCode());
		datiILL.addUltimoMessaggio(dati.getUltimoMessaggio());

		return ControlloAttivitaServizioResult.OK;

	}

	protected AttivitaServizioVO get(List<AttivitaServizioVO> listaAttivita,
			StatoIterRichiesta stato) {
		for (AttivitaServizioVO as : listaAttivita)
			if (StatoIterRichiesta.of(as.getCodAttivita()) == stato)
				return as;

		return null;
	}

}
