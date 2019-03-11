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

import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.PenalitaServizioVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractAttivitaCheckerBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class A04_RestituzioneDocumento extends AbstractAttivitaCheckerBase {



	public A04_RestituzioneDocumento() throws RemoteException, NamingException,
			CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) {

		dati.setControlloEseguito(StatoIterRichiesta.RESTITUZIONE_DOCUMENTO);
		ControlloAttivitaServizioResult stato = ControlloAttivitaServizioResult.OK;
		MovimentoVO mov = dati.getMovimento();

		if (delegate == null)
			return ControlloAttivitaServizioResult.ERRORE_ACCESSO_DB;

		try {
			Timestamp now = DaoManager.now();
			Timestamp dataFinePrevista = mov.getDataFinePrev();

			// imposta con la data odierna dataFineEff del movimento
			mov.setDataFineEff(now);

			Servizi servizi = delegate.getGestioneServizi();

			if (now.after(dataFinePrevista) ) {
				// se la dataOdierna > dataFinePrev il documento
				// è stato riconsegnato in ritardo
				dataFinePrevista = new Timestamp(DateUtil.copiaOrario(now, dataFinePrevista).getTime());
				int ggRitardo = DateUtil.diffDays(dataFinePrevista, now);
				// imposta in ggRitardo di DatiControlloVO
				// i giorni di ritardo
				dati.setGgRitardo(new Long(ggRitardo));

				TipoServizioVO tipoServizio = servizi.getTipoServizio(dati.getTicket(), mov.getCodPolo(),
						mov.getCodBibOperante(), mov.getCodTipoServ());

				if (tipoServizio.isPenalita() ) {
					// se il servizio prevede penalità

					//Controllo penalità sul diritto
					PenalitaServizioVO penalita = servizi.getPenalitaServizio(dati.getTicket(), mov.getCodPolo(),
																		 mov.getCodBibOperante(),
																		 mov.getCodTipoServ(),
																		 mov.getCodServ());
					if (penalita != null) {

						if (ggRitardo > penalita.getGgTolleranza()) {
							// se è stata superata la tolleranza

							// estraggo il coefficiente di sospensione dal diritto
							Long coeffSosp = penalita.getCoeffSosp();
							// estraggo i giorni di sospensione dal diritto
							Long ggSosp = penalita.getGgSosp().longValue();

							// il totale sospensione è dato dai giorni di sospensione +
							// il coefficiente di sospensione * i giorni di ritardo
							Long totaleSospensione = ggSosp + (coeffSosp * ggRitardo);

							if (totaleSospensione > 0) {
								// calcolo la data di sospensione (data corrente + totale giorni di sospensione)
								Timestamp dataSospensione = DateUtil.addDay(now, totaleSospensione.intValue() );

								// imposta in ggSospensione di DatiControlloVO il totale dei giorni di sospensione
								dati.setGgSospensione(totaleSospensione);
								// imposta in dataSospensione di DatiControlloVO la data di sospensione
								dati.setDataSospensione(dataSospensione);
							}
						}
					}
				}

			}

			// controlla se per il documento restituito esistono delle prenotazioni
			if (servizi.esistonoPrenotazioni(dati.getTicket(), dati.getMovimento(), null)) {
				List<String> codMsg = new ArrayList<String>();
				codMsg.add("message.servizi.erogazione.restituzione.esistePrenotazione");
				dati.setCodiciMsgSupplementari(codMsg);
			}

			//almaviva5_20170505 gestione sale
			if (mov.isWithPrenotazionePosto()) {
				//PrenotazionePostoVO pp = mov.getPrenotazionePosto();
				//pp.setStato(StatoPrenotazionePosto.CONCLUSA);
			}

		} catch (Exception e) {
			stato = ControlloAttivitaServizioResult.ERRORE_ACCESSO_DB;
			log.error("", e);
		}

		return stato;
	}

}
