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

import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AttivitaServizioChecker;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.requester.F112A_PropostaAnnullamentoRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.requester.F113A_RifiutoCondizioneSuRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.requester.F113B_AccettazioneCondizioneSuRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.requester.F114_RegistrazioneArrivoDocumento;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.requester.F115_RichiestaRinnovo;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.requester.F116_SpedizioneAllaBibliotecaFornitrice;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.requester.F118_InoltroAltraBiblioteca;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F1211_CondizioneSuRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F1212_RifiutaRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F1214_AccettazioneRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F1218_TermineScaduto;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F1220_RifiutoAnnullamentoRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F1221_AccettaAnnullamentoRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F127_SpedizioneMateriale;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F128_RestituzioneDaBibRichiedente;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F129_AccettazioneRinnovo;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder.F12A_RifiutoRinnovo;

import java.util.HashMap;
import java.util.Map;

public final class ControlloAttivitaILL {

	private static Map<String, AttivitaServizioChecker> controllers = new HashMap<String, AttivitaServizioChecker>();

	static {
		try {
			// REQUESTER (richiedente)
			controllers.put("F118", new F118_InoltroAltraBiblioteca());
			controllers.put("F114", new F114_RegistrazioneArrivoDocumento());
			controllers.put("F112A", new F112A_PropostaAnnullamentoRichiesta());
			controllers.put("F113A", new F113A_RifiutoCondizioneSuRichiesta());
			controllers.put("F113B", new F113B_AccettazioneCondizioneSuRichiesta());
			controllers.put("F115", new F115_RichiestaRinnovo());
			controllers.put("F116", new F116_SpedizioneAllaBibliotecaFornitrice());

			// RESPONDER (fornitrice)
			controllers.put("F127", new F127_SpedizioneMateriale());
			controllers.put("F128", new F128_RestituzioneDaBibRichiedente());
			controllers.put("F129", new F129_AccettazioneRinnovo());
			controllers.put("F12A", new F12A_RifiutoRinnovo());
			controllers.put("F1211", new F1211_CondizioneSuRichiesta());
			controllers.put("F1212", new F1212_RifiutaRichiesta());
			controllers.put("F1214", new F1214_AccettazioneRichiesta());
			controllers.put("F1220", new F1220_RifiutoAnnullamentoRichiesta());
			controllers.put("F1221", new F1221_AccettaAnnullamentoRichiesta());
			controllers.put("F1218", new F1218_TermineScaduto());

			//
			controllers.put("F116A", new AttivitaILLFake());
			controllers.put("F116B", new AttivitaILLFake());
			controllers.put("F1213", new AttivitaILLFake());
			controllers.put("F1215", new AttivitaILLFake());

			controllers.put("F12D", new AttivitaILLFake());
			controllers.put("F12E", new AttivitaILLFake());

		} catch (Exception e) {	}

	}

	public static ControlloAttivitaServizioResult pre(DatiControlloVO datiControllo) throws Exception {
		DatiRichiestaILLVO datiILL = datiControllo.getMovimento().getDatiILL();
		AttivitaServizioChecker controller = controllers.get(datiILL.getCurrentState());

		return controller.check(datiControllo);
	}

	public static ControlloAttivitaServizioResult post(DatiControlloVO datiControllo) throws Exception {
		DatiRichiestaILLVO datiILL = datiControllo.getMovimento().getDatiILL();
		AttivitaServizioChecker controller = controllers.get(datiILL.getCurrentState());

		return controller.post(datiControllo);
	}
}
