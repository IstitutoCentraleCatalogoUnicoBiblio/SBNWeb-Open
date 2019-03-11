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
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
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
import java.util.Date;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class F115_RichiestaRinnovo extends AbstractAttivitaCheckerILL {

	public F115_RichiestaRinnovo() throws RemoteException, NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) throws Exception {
		dati.setControlloEseguito(StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO);

		MovimentoVO mov = dati.getMovimento();

		Date dataProroga = mov.getDataProroga();
		if (dataProroga == null) {
			dati.getCodiciMsgSupplementari().add("errors.servizi.erogazione.rinnovo.dataProrogaNonInserita");
			return ControlloAttivitaServizioResult.ERRORE_CONTROLLO_BLOCCANTE_NON_SUPERATO;
		}

		//prepara messaggio
		MessaggioVO msg = new MessaggioVO(mov.getDatiILL());
		msg.setDataMessaggio(DaoManager.now());
		msg.setTipoInvio(TipoInvio.INVIATO);
		msg.setStato(StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO.getISOCode());
		dati.setUltimoMessaggio(msg);

		mov.setCodStatoRic("P"); //attesa proroga

		return ControlloAttivitaServizioResult.OK;
	}

	@Override
	public ControlloAttivitaServizioResult post(DatiControlloVO dati) throws Exception {
		MessaggioVO msg = dati.getUltimoMessaggio();
		try {
			ILLAPDU response = ILLRequestBuilder.richiestaDiRinnovo(dati.getMovimento(), msg);
			return checkResponse(response, dati.setControlloEseguito(StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO));
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
		}
	}



}
