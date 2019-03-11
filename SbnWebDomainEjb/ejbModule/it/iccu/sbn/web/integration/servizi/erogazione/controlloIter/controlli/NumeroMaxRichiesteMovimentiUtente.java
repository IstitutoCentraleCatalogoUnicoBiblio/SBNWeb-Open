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
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractControlloBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class NumeroMaxRichiesteMovimentiUtente extends AbstractControlloBase {

	public NumeroMaxRichiesteMovimentiUtente() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	@Override
	public Result check(FaseControlloIterVO iter, DatiControlloVO dati) throws Exception {

		MovimentoVO mov = dati.getMovimento();

		if (ValidazioneDati.equals(mov.getFlTipoRec(), RichiestaRecordType.FLAG_PRENOTAZIONE))
			return Result.OK;

		int numeroMovimenti =
			delegate.getGestioneServizi().getNumeroMovimentiAttiviPerUtente(dati.getTicket(), mov);

		ServizioBibliotecaVO servizio;
		if (dati.getServizio() == null) {
			servizio = delegate.getGestioneServizi().getServizioBiblioteca(
					dati.getTicket(),
					mov.getCodPolo(),
					mov.getCodBibOperante(),
					mov.getCodTipoServ(),
					mov.getCodServ());
		} else
			servizio = dati.getServizio();

		Result result;
		if (mov.isNuovo())
			 result = Result.get(iter.isFlagBloc(), (numeroMovimenti < servizio.getNumMaxMov()) );
		else
			// in aggiornamento di un mov. già presente devo escludere
			// il mov. stesso dalla count
			result = Result.get(iter.isFlagBloc(), (numeroMovimenti <= servizio.getNumMaxMov()) );

		/*
		//almaviva5_20110119 #4098
		//il controllo del num. max movimenti deve prendere in considerazione
		//il controllo base eseguito sulla disponibilità.
		//se il sistema ha individuato le condizioni per inserire una prenotazione il
		//presente controllo NON deve essere bloccante.
		if (result != Result.OK && dati.getPreviousResult() != null)
			switch (dati.getPreviousResult()) {
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO:
			case RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO:
			case RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE:
			case RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE:
				return Result.FAILED;
			default:
				return result;
			}
		*/
		return result;
	}

}
