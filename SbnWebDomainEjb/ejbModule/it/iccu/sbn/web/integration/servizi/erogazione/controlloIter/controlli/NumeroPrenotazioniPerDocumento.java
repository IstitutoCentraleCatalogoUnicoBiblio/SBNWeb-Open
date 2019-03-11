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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractControlloBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class NumeroPrenotazioniPerDocumento extends AbstractControlloBase {

	public NumeroPrenotazioniPerDocumento() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	@Override
	public Result check(FaseControlloIterVO iter, DatiControlloVO dati) throws Exception {

		//dati.setControlloEseguito(StatoIterRichiesta.NUMERO_PRENOTAZIONI_PER_DOCUMENTO);

		MovimentoVO mov = dati.getMovimento();

		//almaviva5_20101228 MO1: controllo da non eseguire se non prenotazione
		if (!ValidazioneDati.equals(mov.getFlTipoRec(), RichiestaRecordType.FLAG_PRENOTAZIONE))
			return Result.OK;

		ParametriBibliotecaVO parametri = delegate.getGestioneServizi().getParametriBiblioteca(dati.getTicket(), mov.getCodPolo(), mov.getCodBibOperante());

		int prenotCnt = delegate.getGestioneServizi().getNumeroPrenotazioni(dati.getTicket(), mov);

		if (mov.isNuovo())
			return Result.get(iter.isFlagBloc(), (prenotCnt < parametri.getNumeroPrenotazioni()) );
		else
			return Result.get(iter.isFlagBloc(), (prenotCnt <= parametri.getNumeroPrenotazioni()) );
	}

}
