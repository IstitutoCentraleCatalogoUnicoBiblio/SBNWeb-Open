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
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.ill.ILLRequestBuilder;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.AbstractAttivitaCheckerILL;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class F112A_PropostaAnnullamentoRichiesta extends AbstractAttivitaCheckerILL {

	public F112A_PropostaAnnullamentoRichiesta() throws RemoteException, NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) throws Exception {
		dati.setControlloEseguito(StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO);

		DatiRichiestaILLVO datiILL = dati.getMovimento().getDatiILL();
		if (datiILL.getTransactionId() > 0) {
			//il messaggio di annullamento va inviato solo se la richiesta è
			//già stata inviata alla bib. fornitrice

			//prepara messaggio
			MessaggioVO msg = new MessaggioVO(datiILL);
			msg.setDataMessaggio(DaoManager.now());
			msg.setTipoInvio(TipoInvio.INVIATO);
			msg.setStato(StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO.getISOCode());
			dati.setUltimoMessaggio(msg);

		}

		return ControlloAttivitaServizioResult.OK;
	}

	@Override
	public ControlloAttivitaServizioResult post(DatiControlloVO dati) throws Exception {
		DatiRichiestaILLVO datiILL = dati.getMovimento().getDatiILL();
		MessaggioVO msg = dati.getUltimoMessaggio();
		try {
			if (datiILL.getTransactionId() == 0)
				return ControlloAttivitaServizioResult.OK;

			//il messaggio di annullamento va inviato solo se la richiesta è
			//già stata inviata alla bib. fornitrice
			ILLAPDU response = ILLRequestBuilder.propostaAnnullamentoRichiesta(datiILL, msg);
			return checkResponse(response, dati.setControlloEseguito(StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO));

		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
		}
	}



}
