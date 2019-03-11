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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.ill.ILLRequestBuilder;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.AbstractAttivitaCheckerILL;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class F128_RestituzioneDaBibRichiedente extends AbstractAttivitaCheckerILL {

	public F128_RestituzioneDaBibRichiedente() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati)
			throws Exception {
		dati.setControlloEseguito(StatoIterRichiesta.F128_RICEZIONE_MATERIALE);

		MovimentoVO mov = dati.getMovimento();
		Timestamp now = DaoManager.now();
		DatiRichiestaILLVO datiILL = mov.getDatiILL();
		datiILL.setDataFine(now);

		//prepara messaggio
		MessaggioVO msg = new MessaggioVO(datiILL);
		msg.setDataMessaggio(now);
		msg.setTipoInvio(TipoInvio.INVIATO);
		msg.setStato(StatoIterRichiesta.F128_RICEZIONE_MATERIALE.getISOCode());
		dati.setUltimoMessaggio(msg);

		//se la restituzione è l'ultimo passo dell'iter il movimento viene chiuso
		datiILL = datiILL.copy();
		datiILL.setCurrentState(StatoIterRichiesta.F128_RICEZIONE_MATERIALE.getISOCode());
		List<AttivitaServizioVO> attivitaSuccessive = delegate.getGestioneServizi().getListaAttivitaSuccessive(dati.getTicket(), mov.getCodPolo(),
			mov.getCodBibOperante(), mov.getCodTipoServ(), Integer.parseInt(mov.getProgrIter()),
			datiILL);

		if (!ValidazioneDati.isFilled(attivitaSuccessive) ){
			mov.setDataFineEff(now);
			mov.setCodStatoMov("C");	//chiuso
			mov.setCodStatoRic("H");	//conclusa
		}

		return ControlloAttivitaServizioResult.OK;
	}

	@Override
	public ControlloAttivitaServizioResult post(DatiControlloVO dati) throws Exception {
		DatiRichiestaILLVO datiILL = dati.getMovimento().getDatiILL();
		MessaggioVO msg = dati.getUltimoMessaggio();
		try {
			//se l'iter comprende la restituzione del documento (04) il mov. locale va avanzato a questo stato
			ControlloAttivitaServizioResult check = checkAttivita04(dati);
			if (!ControlloAttivitaServizioResult.isOK(check))
				return check;

			ILLAPDU response = ILLRequestBuilder.restituzioneDaBibRichiedente(datiILL, msg);
			check = checkResponse(response, dati.setControlloEseguito(StatoIterRichiesta.F128_RICEZIONE_MATERIALE));

			return check;

		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
		}

	}

	private ControlloAttivitaServizioResult checkAttivita04(DatiControlloVO dati) throws Exception {

		ControlloAttivitaServizioResult check = ControlloAttivitaServizioResult.OK;
		MovimentoVO mov = dati.getMovimento();
		DatiRichiestaILLVO datiILL = mov.getDatiILL().copy();
		datiILL.setCurrentState(StatoIterRichiesta.F128_RICEZIONE_MATERIALE.getISOCode());
		List<AttivitaServizioVO> attivitaSuccessive = delegate.getGestioneServizi().getListaAttivitaSuccessive(dati.getTicket(),
			mov.getCodPolo(), mov.getCodBibOperante(), mov.getCodTipoServ(), Integer.parseInt(mov.getProgrIter()), datiILL);

		AttivitaServizioVO a04 = get(attivitaSuccessive, StatoIterRichiesta.RESTITUZIONE_DOCUMENTO);
		if (a04 != null) {
			//si eseguono i controlli per questo iter
			ControlloAttivitaServizio cas = new ControlloAttivitaServizio(a04);
			//cas.controlloIter(dati, messaggi, controlloAggiornamento, controlloInoltraPrenotazione)
			check = cas.controlloDefault(dati);
			if (check == ControlloAttivitaServizioResult.OK) {
				IterServizioVO iter = a04.getPassoIter();
				mov.setProgrIter(iter.getProgrIter().toString());
				mov.setCodStatoMov(iter.getCodStatoMov());
				mov.setCodStatoRic(iter.getCodStatoRich());
				mov.setCodAttivita(iter.getCodAttivita());
				dati.setMovimentoAggiornato(mov);
			}
		}

		return check;
	}

}
