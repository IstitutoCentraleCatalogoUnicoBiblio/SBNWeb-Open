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
import it.iccu.sbn.util.ConvertiVo.ConvertFromXML;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateDue;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.RenewAnswerType;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

public class RenewAnswerTypeHandler extends AvanzamentoRichiestaILLBaseHandler {

	private static final long serialVersionUID = -3346190852668582903L;
	private static final String ACCEPTED_VALUE = "9";

	private RenewAnswerType renewAnswer;
	private boolean accepted;

	protected RenewAnswerTypeHandler(String ticket, ILLAPDU input) {
		super(ticket, input);
		renewAnswer = input.getRenewAnswer();
		accepted = ValidazioneDati.equals(renewAnswer.getAnswer(), ACCEPTED_VALUE);
	}

	public void execute() throws ApplicationException, ValidationException {
		try {
			//1. ricerca richiesta ill
			DatiRichiestaILLVO dati_richiesta_ill = getDatiRichiestaIll(renewAnswer);
			//memorizzazione stato richiesta
			String oldState = dati_richiesta_ill.getCurrentState();

			//nuovo stato
			String newState = null;
			if (accepted) {
				newState = StatoIterRichiesta.F129_CONFERMA_RINNOVO.getISOCode();
				DateDue dateDue = renewAnswer.getDateDue();
				DateType dt = ValidazioneDati.first(dateDue.getDate());
				dati_richiesta_ill.setDataRinnovo(dt != null ? ConvertFromXML.convertiIllDate(dt) : null);

			} else
				newState = StatoIterRichiesta.F12A_NEGAZIONE_RINNOVO.getISOCode();

			//prepara messaggio
			MessaggioVO msg = preparaMessaggio(StatoIterRichiesta.of(newState), dati_richiesta_ill.getRuolo(),
					renewAnswer.getResponderNote());
			dati_richiesta_ill.addUltimoMessaggio(msg);

			dati_richiesta_ill.setCurrentState(newState);

			//5. ins/mod richiesta locale
			cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public String getXMLName() {
		return formattaNomeXML(renewAnswer.getTransactionId(),
				accepted ? StatoIterRichiesta.F129_CONFERMA_RINNOVO : StatoIterRichiesta.F12A_NEGAZIONE_RINNOVO,
				renewAnswer.getRequesterId());
	}

}
