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

import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractControlloBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class NumeroMaxRiproduzioniPerRichiesta extends AbstractControlloBase {

	public NumeroMaxRiproduzioniPerRichiesta() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	@Override
	public Result check(FaseControlloIterVO iter, DatiControlloVO dati) throws Exception {

		MovimentoVO movimento = dati.getMovimento();

		// numero riproduzioni (numero copie) richieste per il movimento
		int numeroRiproduzioni = Integer.valueOf(movimento.getNumPezzi());

		ServizioBibliotecaVO servizio;
		if (dati.getServizio() == null) {
			servizio = delegate.getGestioneServizi()
											.getServizioBiblioteca(dati.getTicket(), movimento.getCodPolo(),
																movimento.getCodBibOperante(),
																movimento.getCodTipoServ(),
																movimento.getCodServ());
		} else {
			servizio = dati.getServizio();
		}

		// verifico che il numero di riproduzioni non sia maggiore del massimo consentito
		// per quel servizio
		return Result.get(iter.isFlagBloc(), (numeroRiproduzioni <= servizio.getNumMaxRiprod()) );
	}

}
