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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.controlli;

import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractControlloBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class NumeroMaxRichiesteGGPerServizio extends AbstractControlloBase {

	public NumeroMaxRichiesteGGPerServizio() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	@Override
	public Result check(FaseControlloIterVO controlloIter, DatiControlloVO dati) throws Exception {

		MovimentoVO mov = dati.getMovimento();

		int numeroRichiesteGGPerTipoServizio =
			delegate.getGestioneServizi().getNumeroRichiesteGiornalierePerTipoServizio(dati.getTicket(), mov);

		TipoServizioVO tipoServizio;
		if (dati.getTipoServizio() == null) {
			tipoServizio = delegate.getGestioneServizi().getTipoServizio(
					dati.getTicket(),
					mov.getCodPolo(),
					mov.getCodBibOperante(),
					mov.getCodTipoServ());
		} else
			tipoServizio = dati.getTipoServizio();

		boolean bloccante = controlloIter.isFlagBloc();

		if (mov.isNuovo())
			return Result.get(bloccante, (numeroRichiesteGGPerTipoServizio < tipoServizio.getNumMaxMov()) );
		else
			// in aggiornamento di un mov. già presente devo escludere
			// il mov. stesso dalla count
			return Result.get(bloccante, (numeroRichiesteGGPerTipoServizio <= tipoServizio.getNumMaxMov()) );
	}

}
