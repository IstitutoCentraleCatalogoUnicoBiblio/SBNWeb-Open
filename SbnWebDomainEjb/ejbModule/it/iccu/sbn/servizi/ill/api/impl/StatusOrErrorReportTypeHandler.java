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
package it.iccu.sbn.servizi.ill.api.impl;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CurrentState;
import it.iccu.sbn.vo.xml.binding.ill.apdu.HistoryReport;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.StatusOrErrorReportType;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.util.Locale;

public class StatusOrErrorReportTypeHandler extends AvanzamentoRichiestaILLBaseHandler {

	private static final long serialVersionUID = 7728466823280527591L;

	private StatusOrErrorReportType statusOrErrorReport;
	private String newState;

	public StatusOrErrorReportTypeHandler(String ticket, ILLAPDU input) {
		super(ticket, input);
		statusOrErrorReport = input.getStatusOrErrorReport();
	}

	public void execute() throws ApplicationException, ValidationException {
		try {
			//almaviva5_20171204 check cambio biblioteca fornitrice
			boolean checkCambioBibFornitrice = !ValidazioneDati.in(getTarget(),
					ServiziUtil.formattaIsil(statusOrErrorReport.getRequesterId(), Locale.getDefault().getCountry()),
					ServiziUtil.formattaIsil(statusOrErrorReport.getResponderId(), Locale.getDefault().getCountry()));

			DatiRichiestaILLVO dati_richiesta_ill;
			String oldState;
			MessaggioVO msg;

			if (checkCambioBibFornitrice) {
				//la richiesta è stata inoltrata ad altra biblioteca, questa richiesta va annullata
				dati_richiesta_ill = getDatiRichiestaIll(statusOrErrorReport.getTransactionId().getValue(),
						RuoloBiblioteca.FORNITRICE, null, getTarget());
				oldState = dati_richiesta_ill.getCurrentState();
				newState = StatoIterRichiesta.F1218_TERMINE_SCADUTO.getISOCode();
				msg = preparaMessaggio(StatoIterRichiesta.of(newState), dati_richiesta_ill.getRuolo(),
						String.format("Richiesta inviata ad altra biblioteca (%s).", statusOrErrorReport.getResponderId()));

			} else {
				//1. ricerca richiesta ill
				dati_richiesta_ill = getDatiRichiestaIll(statusOrErrorReport);
				//memorizzazione stato richiesta
				oldState = dati_richiesta_ill.getCurrentState();

				HistoryReport historyReport = statusOrErrorReport.getStatusReport().getHistoryReport();
				CurrentState currentState = historyReport.getMostRecentService().getCurrentState();
				newState = currentState.getState();

				//String isilLastBib = ServiziUtil.formattaIsil(historyReport.getInitiatorOfMostRecentService(), Locale.getDefault().getCountry());
				//determinazione biblioteca che ha cambiato lo stato
				//TipoInvio tipoInvio = ValidazioneDati.equals(getTarget(), isilLastBib) ? TipoInvio.INVIATO : dati_richiesta_ill.getRuolo();

				//prepara messaggio
				msg = preparaMessaggio(StatoIterRichiesta.of(newState), dati_richiesta_ill.getRuolo(), historyReport.getMostRecentServiceNote());
			}

			if (ValidazioneDati.equals(oldState, newState) )
				return;	//non ci sono modifiche, allineamento concluso

			//ci sono state modifiche alla richiesta
			dati_richiesta_ill.addUltimoMessaggio(msg);

			dati_richiesta_ill.setCurrentState(newState);

			cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);

		} catch (Exception e) {
			log.error("", e);
		}

	}

	public String getXMLName() {
		return formattaNomeXML(statusOrErrorReport.getTransactionId(), StatoIterRichiesta.of(newState), getTarget());
	}

}
