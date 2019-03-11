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
import it.iccu.sbn.vo.xml.binding.ill.apdu.AnswerType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ConditionalType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.CostEstimateType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateForReply;
import it.iccu.sbn.vo.xml.binding.ill.apdu.DateType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.EstimateDateAvailable;
import it.iccu.sbn.vo.xml.binding.ill.apdu.EstimateType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.HoldPlaced;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAnswerType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.LocationProvider;
import it.iccu.sbn.vo.xml.binding.ill.apdu.Retry;
import it.iccu.sbn.vo.xml.binding.ill.apdu.Unfilled;
import it.iccu.sbn.vo.xml.binding.ill.apdu.WillSupply;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ILLAnswerTypeHandler extends AvanzamentoRichiestaILLBaseHandler {

	private static final long serialVersionUID = -8639672965446405194L;

	static Map<Class<?>, StatoIterRichiesta> stati = new HashMap<Class<?>, StatoIterRichiesta>() {
		private static final long serialVersionUID = -4566288326046676553L;
	{
		put(ConditionalType.class, StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA);
		put(HoldPlaced.class, StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO);
		put(WillSupply.class, StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA);
		put(Unfilled.class, StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE);
		put(Retry.class, StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA);
		put(LocationProvider.class, StatoIterRichiesta.F1213_LISTA_POSSIBILI_DESTINATARI);
		put(EstimateType.class, StatoIterRichiesta.F127A_STIMA_DEI_COSTI);
	}
	};

	protected ILLAnswerType answer;

	private Serializable action;
	private DatiRichiestaILLVO dati_richiesta_ill;

	protected ILLAnswerTypeHandler(String ticket, ILLAPDU input) {
		super(ticket, input);
		answer = input.getILLAnswer();
		action = getAction(answer.getAnswerType());
	}

	private Serializable getAction(AnswerType type) {
		ConditionalType conditional = type.getConditional();
		if (conditional != null)
			return conditional;

		EstimateType estimate = type.getEstimate();
		if (estimate != null)
			return estimate;

		HoldPlaced holdPlaced = type.getHoldPlaced();
		if (holdPlaced != null)
			return holdPlaced;

		LocationProvider locationProvider = type.getLocationProvider();
		if (locationProvider != null)
			return locationProvider;

		Retry retry = type.getRetry();
		if (retry != null)
			return retry;

		Unfilled unfilled = type.getUnfilled();
		if (unfilled != null)
			return unfilled;

		WillSupply willSupply = type.getWillSupply();
		if (willSupply != null)
			return willSupply;

		return null;
	}

	public void execute() throws ApplicationException, ValidationException {
		try {
			dati_richiesta_ill = getDatiRichiestaIll(answer);
			StatoIterRichiesta stato = stati.get(action.getClass());
			switch (stato) {
			case F1211_RICHIESTA_CONDIZIONATA:
				executeConditional((ConditionalType) action);
				break;

			case F1215_PRENOTAZIONE_DOCUMENTO:
				executeHoldPlaced((HoldPlaced) action);
				break;

			case F1214_ACCETTAZIONE_RICHIESTA:
				executeWillSupply((WillSupply) action);
				break;

			case F1212_RICHIESTA_NON_SODDISFACIBILE:
				executeUnfilled((Unfilled) action);
				break;

			case F118_INVIO_A_BIB_DESTINATARIA:
				executeRetry((Retry) action);
				break;

			case F1213_LISTA_POSSIBILI_DESTINATARI:
				executeLocationProvider((LocationProvider) action);

			case F127A_STIMA_DEI_COSTI:
				executeEstimate((EstimateType) action);

			default:
				throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_VALIDAZIONE_APDU);
			}

		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
		}

	}

	private void executeConditional(ConditionalType conditional) throws Exception {

		//memorizzazione stato richiesta
		String oldState = dati_richiesta_ill.getCurrentState();

		//nuovo stato
		String newState = StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA.getISOCode();

		//prepara messaggio
		MessaggioVO msg = preparaMessaggio(StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA, dati_richiesta_ill.getRuolo(), answer.getResponderNote());
		msg.setCondizione(conditional.getConditions());
		dati_richiesta_ill.addUltimoMessaggio(msg);

		dati_richiesta_ill.setCurrentState(newState);

		DateForReply dateForReply = conditional.getDateForReply();
		DateType dt = ValidazioneDati.first(dateForReply.getDate());
		dati_richiesta_ill.setDataScadenza(dt != null ? ConvertFromXML.convertiIllDate(dt) : dati_richiesta_ill.getDataScadenza());

		//5. ins/mod richiesta locale
		cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);
	}

	private void executeUnfilled(Unfilled unfilled) throws Exception {

		//memorizzazione stato richiesta
		String oldState = dati_richiesta_ill.getCurrentState();

		//nuovo stato
		String newState = StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE.getISOCode();

		//prepara messaggio
		MessaggioVO msg = preparaMessaggio(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE, dati_richiesta_ill.getRuolo(), answer.getResponderNote());
		dati_richiesta_ill.addUltimoMessaggio(msg);

		dati_richiesta_ill.setCurrentState(newState);

		cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);
	}

	private void executeHoldPlaced(HoldPlaced holdPlaced) throws Exception {

		//memorizzazione stato richiesta
		String oldState = dati_richiesta_ill.getCurrentState();

		//nuovo stato
		String newState = StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO.getISOCode();

		//prepara messaggio
		MessaggioVO msg = preparaMessaggio(StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO, dati_richiesta_ill.getRuolo(), answer.getResponderNote());
		dati_richiesta_ill.addUltimoMessaggio(msg);

		dati_richiesta_ill.setCurrentState(newState);

		EstimateDateAvailable dateAvailable = holdPlaced.getEstimateDateAvailable();
		DateType dt = ValidazioneDati.first(dateAvailable.getDate());
		dati_richiesta_ill.setDataRientroPrevisto(dt != null ? ConvertFromXML.convertiIllDate(dt) : null);

		cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);
	}

	private void executeEstimate(EstimateType estimate) throws Exception {

		//memorizzazione stato richiesta
		String oldState = dati_richiesta_ill.getCurrentState();

		//nuovo stato
		String newState = StatoIterRichiesta.F127A_STIMA_DEI_COSTI.getISOCode();

		//prepara messaggio
		MessaggioVO msg = preparaMessaggio(StatoIterRichiesta.F127A_STIMA_DEI_COSTI, dati_richiesta_ill.getRuolo(), answer.getResponderNote());
		//estrazione eventuali note sui costi
		CostEstimateType costEstimate = estimate.getCostEstimate();
		String costo = ValidazioneDati.formatValueList(costEstimate.getNote(), "\u0020");
		if (isFilled(costo)) {
			String note = msg.getNote() + " " + costo;
			msg.setNote(note.trim());
		}

		dati_richiesta_ill.addUltimoMessaggio(msg);

		dati_richiesta_ill.setCurrentState(newState);

		cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);
	}

	private void executeWillSupply(WillSupply willSupply) throws Exception {

		//memorizzazione stato richiesta
		String oldState = dati_richiesta_ill.getCurrentState();

		//nuovo stato
		String newState = StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA.getISOCode();

		//prepara messaggio
		MessaggioVO msg = preparaMessaggio(StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA, dati_richiesta_ill.getRuolo(), answer.getResponderNote());
		dati_richiesta_ill.addUltimoMessaggio(msg);

		dati_richiesta_ill.setCurrentState(newState);

		cambiaStatoRichiestaILL(getTicket(), dati_richiesta_ill, oldState, newState);
	}

	private void executeRetry(Retry retry) throws Exception {
		log.warn("retry non implementato.");
	}

	private void executeLocationProvider(LocationProvider locationProvider) throws Exception {
		log.warn("location-provider non implementato.");
	}

	public String getXMLName() {
		StatoIterRichiesta stato = stati.get(action.getClass());
		return formattaNomeXML(answer.getTransactionId(), stato, getTarget());
	}

}
