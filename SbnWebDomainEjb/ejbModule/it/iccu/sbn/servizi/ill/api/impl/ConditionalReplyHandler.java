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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ConditionalReplyType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

public class ConditionalReplyHandler extends AvanzamentoRichiestaILLBaseHandler {

	private static final long serialVersionUID = 6222909448064102996L;
	private static final String ACCEPTED_VALUE = "B";

	private ConditionalReplyType conditionalReply;
	private boolean accepted;

	protected ConditionalReplyHandler(String ticket, ILLAPDU input) {
		super(ticket, input);
		conditionalReply = input.getConditionalReply();
		accepted = ValidazioneDati.equals(conditionalReply.getAnswer(), ACCEPTED_VALUE);
	}

	public void execute() {
		try {
			//1. ricerca richiesta ill
			DatiRichiestaILLVO dati_richiesta_ill = getDatiRichiestaIll(conditionalReply);
			//memorizzazione stato richiesta
			String oldState = dati_richiesta_ill.getCurrentState();

			//nuovo stato
			String newState = null;
			if (accepted)
				newState = StatoIterRichiesta.F113B_ACCETTAZIONE_CONDIZIONE_SU_RICHIESTA.getISOCode();
			else
				newState = StatoIterRichiesta.F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA.getISOCode();

			//prepara messaggio
			MessaggioVO msg = preparaMessaggio(StatoIterRichiesta.of(newState), dati_richiesta_ill.getRuolo(),
					conditionalReply.getRequesterNote());
			dati_richiesta_ill.addUltimoMessaggio(msg);

			dati_richiesta_ill.setCurrentState(newState);

			//5. ins/mod richiesta locale
			cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public String getXMLName() {
		return formattaNomeXML(conditionalReply.getTransactionId(),
				accepted ? StatoIterRichiesta.F113B_ACCETTAZIONE_CONDIZIONE_SU_RICHIESTA
						: StatoIterRichiesta.F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA,
				conditionalReply.getResponderId());
	}

}
