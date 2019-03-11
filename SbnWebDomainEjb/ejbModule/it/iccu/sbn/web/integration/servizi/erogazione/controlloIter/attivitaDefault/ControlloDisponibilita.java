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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault;

import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractControlloBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class ControlloDisponibilita extends AbstractControlloBase {


	public ControlloDisponibilita() throws RemoteException, NamingException,
			CreateException {
		super();
	}

	@Override
	public Result check(FaseControlloIterVO iter, DatiControlloVO dati) {
		dati.setControlloEseguito(StatoIterRichiesta.CONTROLLO_DISPONIBILITA);
		Result stato = Result.OK;
		MovimentoVO mov = dati.getMovimento();

		try {
			//controllo se esiste un movimento attivo relativo al documento
			if (delegate.getGestioneServizi().controlloDisponibilita(dati.getTicket(),
					new ControlloDisponibilitaVO(mov, false)).getResult() != ControlloAttivitaServizioResult.OK)
				stato = Result.get(iter.isFlagBloc(), false);
		} catch (RemoteException e) {
			stato = Result.get(iter.isFlagBloc(), false);
			logger.error(e.getMessage());
		}

		return stato;
	}

}
