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
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CheckedInType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

public class CheckedInTypeHandler extends AvanzamentoRichiestaILLBaseHandler {

	private static final long serialVersionUID = -7548432625821434813L;

	private CheckedInType checkedIn;

	public CheckedInTypeHandler(String ticket, ILLAPDU input) {
		super(ticket, input);
		checkedIn = input.getCheckedIn();
	}

	public void execute() throws ApplicationException, ValidationException {
		try {
			//1. ricerca richiesta ill
			DatiRichiestaILLVO dati_richiesta_ill = getDatiRichiestaIll(checkedIn);
			//memorizzazione stato richiesta
			String oldState = dati_richiesta_ill.getCurrentState();

			//nuovo stato
			String newState = StatoIterRichiesta.F128_RICEZIONE_MATERIALE.getISOCode();

			//prepara messaggio
			MessaggioVO msg = preparaMessaggio(StatoIterRichiesta.of(newState), dati_richiesta_ill.getRuolo(),
					checkedIn.getResponderNote());
			dati_richiesta_ill.addUltimoMessaggio(msg);

			dati_richiesta_ill.setCurrentState(newState);

			cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);

		} catch (Exception e) {
			log.error("", e);
		}

	}

	public String getXMLName() {
		return formattaNomeXML(checkedIn.getTransactionId(), StatoIterRichiesta.F128_RICEZIONE_MATERIALE,
				checkedIn.getRequesterId());
	}

}
