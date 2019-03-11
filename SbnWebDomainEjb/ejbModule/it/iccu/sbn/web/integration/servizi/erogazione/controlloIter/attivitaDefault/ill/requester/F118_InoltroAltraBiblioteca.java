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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.requester;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.ill.ILLRequestBuilder;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLRequestType;
import it.iccu.sbn.vo.xml.binding.ill.apdu.TransactionIdType;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.AbstractAttivitaCheckerILL;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.first;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class F118_InoltroAltraBiblioteca extends AbstractAttivitaCheckerILL {


	public F118_InoltroAltraBiblioteca() throws RemoteException, NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) {
		dati.setControlloEseguito(StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA);
		MovimentoVO mov = dati.getMovimento();

		Timestamp now = DaoManager.now();
		DatiRichiestaILLVO datiILL = mov.getDatiILL();
		if (mov.getFlSvolg().equals("I")) {
			if (!isFilled(mov.getCodBibInv()) ) {
				mov.setCodBibInv(mov.getCodBibDest());
			}
			// indico sul campo inutilizzato che il campo deve essere readonly
			mov.setCodErogAlt("I");

			datiILL.setDataInizio(now);
			DocumentoNonSbnVO doc = datiILL.getDocumento();

			//dati periodico
			String annoPeriodico = mov.getAnnoPeriodico();
			if (isFilled(annoPeriodico))
				doc.setAnnata(annoPeriodico);
			String numVolume = mov.getNumVolume();
			if (isFilled(numVolume))
				doc.setNum_volume(numVolume);
			String numFascicolo = mov.getNumFascicolo();
			if (isFilled(numFascicolo))
				doc.setFascicolo(numFascicolo);
			doc.setPagine(mov.getIntervalloCopia());
		}

		//prepara messaggio
		MessaggioVO msg = new MessaggioVO(datiILL);
		msg.setDataMessaggio(now);
		msg.setTipoInvio(TipoInvio.INVIATO);
		msg.setStato(StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA.getISOCode());
		dati.setUltimoMessaggio(msg);

		try {
			datiILL.setCostoMax(mov.getPrezzoMaxDouble());
		} catch (ParseException e) { }

		return ControlloAttivitaServizioResult.OK;
	}


	@Override
	protected ControlloAttivitaServizioResult checkResponse(ILLAPDU response, DatiControlloVO dati) {
		DatiRichiestaILLVO datiILL = dati.getMovimento().getDatiILL();
		if (response != null) {
			ILLRequestType requestType = first(response.getILLRequest());
			if (requestType != null) {
				// cod. richiesta ill
				TransactionIdType tid = requestType.getTransactionId();
				if (tid != null) {
					datiILL.setTransactionId(Long.valueOf(tid.getValue()));
				/*
					// salvataggio id per ricerche successive
					CommandInvokeVO invoke = new CommandInvokeVO(
							dati.getTicket(),
							CommandType.SRV_AGGIORNA_DATI_RICHIESTA_ILL,
							datiILL.copy());
					try {
						CommandResultVO result = delegate.getGestioneServizi().invoke(invoke);
						result.throwError();
						dati.getMovimento().setDatiILL(
								(DatiRichiestaILLVO) result.getResult());
					} catch (Exception e) {
						log.error("", e);
						return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
					}
				*/
				}
			}
		}

		return super.checkResponse(response, dati);
	}

	@Override
	public ControlloAttivitaServizioResult post(DatiControlloVO dati) throws Exception {
		MessaggioVO msg = dati.getUltimoMessaggio();
		try {
			ILLAPDU response = ILLRequestBuilder.inoltraRichiestaAdAltraBiblioteca(dati.getMovimento(), msg);
			return checkResponse(response, dati.setControlloEseguito(StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA));
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
		}
	}

}
