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
package it.iccu.sbn.servizi.ill.conf.responder;

import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.ill.conf.ConfigurazioneILLBaseImpl;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigurazionePreventivoFornitrice extends ConfigurazioneILLBaseImpl {

	public List<AttivitaServizioVO> getListaAttivitaSuccessive(String codBib,
			String srvLoc, StatoIterRichiesta statoILL,
			StatoIterRichiesta local, List<AttivitaServizioVO> iterLocale)
			throws Exception {

		List<StatoIterRichiesta> statiLocaliAmmessi = Collections.emptyList();
		AttivitaBuilder ab = new AttivitaBuilder(codBib, srvLoc);

		switch (statoILL) {
		case F118_INVIO_A_BIB_DESTINATARIA:
			statiLocaliAmmessi = Arrays.asList(StatoIterRichiesta.SCARICO_DA_MAGAZZINO);
			//almaviva5_20171002 richiesta ICCU: solo da pulsante
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1218_TERMINE_SCADUTO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F127A_STIMA_DEI_COSTI));
			break;

		case F112A_PROPOSTA_DI_ANNULLAMENTO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1220_RIFIUTO_ANNULLAMENTO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1221_CONFERMA_ANNULLAMENTO, "C", "H"));
			break;

		case F1220_RIFIUTO_ANNULLAMENTO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F127A_STIMA_DEI_COSTI));
			//almaviva5_20171002 richiesta ICCU: solo da pulsante
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE));
			break;

		case F1212_RICHIESTA_NON_SODDISFACIBILE:
			statiLocaliAmmessi = Arrays.asList(
					StatoIterRichiesta.RICOLLOCAZIONE_A_MAGAZZINO);
			break;

		case F127A_STIMA_DEI_COSTI:
			//solo per avanzamento automatico
			statiLocaliAmmessi = Arrays.asList(
					StatoIterRichiesta.RICOLLOCAZIONE_A_MAGAZZINO);
			break;

		default:
			break;

		}

		return pulisciAttivitaLocali(iterLocale, statiLocaliAmmessi);

	}

	@Override
	public void controllaCambiamentoDiStato(MovimentoVO mov, DatiRichiestaILLVO datiILL,
			ServizioBibliotecaVO servizio, StatoIterRichiesta _old,
			StatoIterRichiesta _new) throws Exception {
		// controlla se la richiesta ha subito un cambiamento di stato, in questo caso la richiesta
		// locale deve essere avanzata di conseguenza
		if (_old == _new)
			return;

		Timestamp now = DaoManager.now();

		switch (_new) {

		default:
			super.controllaCambiamentoDiStato(mov, datiILL, servizio, _old, _new);
		}

	}

}
