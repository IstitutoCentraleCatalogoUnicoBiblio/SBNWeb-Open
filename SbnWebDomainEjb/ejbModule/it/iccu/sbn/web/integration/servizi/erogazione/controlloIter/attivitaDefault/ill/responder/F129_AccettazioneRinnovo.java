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
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
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
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class F129_AccettazioneRinnovo extends AbstractAttivitaCheckerILL {

	public F129_AccettazioneRinnovo() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati)
			throws Exception {
		dati.setControlloEseguito(StatoIterRichiesta.F129_CONFERMA_RINNOVO);

		MovimentoVO mov = dati.getMovimento();
		mov.setCodStatoRic("N");	//prorogata
		Date nuovaDataFine = mov.getDataProroga();
		nuovaDataFine = DateUtil.copiaOrario(mov.getDataFinePrev(), nuovaDataFine);
		mov.setDataFinePrev(new Timestamp(nuovaDataFine.getTime()));

		DatiRichiestaILLVO datiILL = mov.getDatiILL();
		datiILL.setDataScadenza(nuovaDataFine);

		//prepara messaggio
		MessaggioVO msg = new MessaggioVO(datiILL);
		msg.setDataMessaggio(DaoManager.now());
		msg.setTipoInvio(TipoInvio.INVIATO);
		msg.setStato(StatoIterRichiesta.F129_CONFERMA_RINNOVO.getISOCode());
		dati.setUltimoMessaggio(msg);

		return ControlloAttivitaServizioResult.OK;
	}

	@Override
	public ControlloAttivitaServizioResult post(DatiControlloVO dati) throws Exception {
		MessaggioVO msg = dati.getUltimoMessaggio();
		try {
			ILLAPDU response = ILLRequestBuilder.rispostaRichiestaDiRinnovo(dati.getMovimento(), msg, true);
			return checkResponse(response, dati.setControlloEseguito(StatoIterRichiesta.F129_CONFERMA_RINNOVO));
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
		}

	}

}
