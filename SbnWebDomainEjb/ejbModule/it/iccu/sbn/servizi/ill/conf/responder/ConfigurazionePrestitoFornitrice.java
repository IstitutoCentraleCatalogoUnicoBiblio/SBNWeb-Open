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

public class ConfigurazionePrestitoFornitrice extends ConfigurazioneILLBaseImpl {

	public List<AttivitaServizioVO> getListaAttivitaSuccessive(String codBib,
			String srvLoc, StatoIterRichiesta statoILL,
			StatoIterRichiesta local, List<AttivitaServizioVO> iterLocale)
			throws Exception {

		List<StatoIterRichiesta> statiLocaliAmmessi = Collections.emptyList();
		AttivitaBuilder ab = new AttivitaBuilder(codBib, srvLoc);

		switch (statoILL) {
		case F118_INVIO_A_BIB_DESTINATARIA:
			statiLocaliAmmessi = Arrays.asList(StatoIterRichiesta.SCARICO_DA_MAGAZZINO);
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA));
			//almaviva5_20171002 richiesta ICCU: solo da pulsante
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA));
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1218_TERMINE_SCADUTO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE));
			break;

		case F112A_PROPOSTA_DI_ANNULLAMENTO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1220_RIFIUTO_ANNULLAMENTO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1221_CONFERMA_ANNULLAMENTO, "C", "H"));
			break;

		case F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA:
			statiLocaliAmmessi = Arrays.asList(
					StatoIterRichiesta.RICOLLOCAZIONE_A_MAGAZZINO);
			break;

		case F113B_ACCETTAZIONE_CONDIZIONE_SU_RICHIESTA:
			statiLocaliAmmessi = Arrays.asList(StatoIterRichiesta.SCARICO_DA_MAGAZZINO);
			iterLocale.add(ab.attivita(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE));
			break;

		case F114_ARRIVO_MATERIALE:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12D_SOLLECITO_RESTITUZIONE_PRESTITO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12E_RICHIESTA_RESTITUZIONE_URGENTE));
			break;

		case F115_RICHIESTA_DI_RINNOVO_PRESTITO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F129_CONFERMA_RINNOVO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12A_NEGAZIONE_RINNOVO));
			break;

		case F116_RESTITUZIONE_MATERIALE:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F128_RICEZIONE_MATERIALE));//, "C", "H"));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO));
			break;

		case F116B_MATERIALE_DANNEGGIATO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA));
			//almaviva5_20171002 richiesta ICCU: solo da pulsante
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA));
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO));
			//out.add(ab.attivita(StatoIterRichiesta.F1217_ origine="F" />-->
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1218_TERMINE_SCADUTO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F128_RICEZIONE_MATERIALE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F129_CONFERMA_RINNOVO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12A_NEGAZIONE_RINNOVO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12D_SOLLECITO_RESTITUZIONE_PRESTITO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12E_RICHIESTA_RESTITUZIONE_URGENTE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO));
			break;

		case F1211_RICHIESTA_CONDIZIONATA:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1218_TERMINE_SCADUTO));
			break;

		case F1214_ACCETTAZIONE_RICHIESTA:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE));
			//almaviva5_20171002 richiesta ICCU: solo da pulsante
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE));
			break;

		case F1215_PRENOTAZIONE_DOCUMENTO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE));
			break;

		case F1220_RIFIUTO_ANNULLAMENTO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1211_RICHIESTA_CONDIZIONATA));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F1214_ACCETTAZIONE_RICHIESTA));
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1215_PRENOTAZIONE_DOCUMENTO));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE));
			//almaviva5_20171002 richiesta ICCU: solo da pulsante
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F1212_RICHIESTA_NON_SODDISFACIBILE));
			break;

		case F128_RICEZIONE_MATERIALE:
			statiLocaliAmmessi = Arrays.asList(
					StatoIterRichiesta.RESTITUZIONE_DOCUMENTO,
					StatoIterRichiesta.RICOLLOCAZIONE_A_MAGAZZINO,
					StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO);
			//stato terminale
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO));
			break;

		case F129_CONFERMA_RINNOVO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12E_RICHIESTA_RESTITUZIONE_URGENTE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12D_SOLLECITO_RESTITUZIONE_PRESTITO));
			break;

		case F12A_NEGAZIONE_RINNOVO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F12E_RICHIESTA_RESTITUZIONE_URGENTE));
			break;

		case F1212_RICHIESTA_NON_SODDISFACIBILE:
			statiLocaliAmmessi = Arrays.asList(
					StatoIterRichiesta.RICOLLOCAZIONE_A_MAGAZZINO);
			break;

		case F127_SPEDIZIONE_MATERIALE:
			//solo per avanzamento automatico
			statiLocaliAmmessi = Arrays.asList(
					StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE,
					StatoIterRichiesta.CONSEGNA_COPIE);
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

		case F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA:
			datiILL.setDataFine(now);
			mov.setCodStatoMov("C");	//chiuso
			mov.setCodStatoRic("B");	//rifiutato
			break;

		case F116A_PERDITA_DEL_MATERIALE_PRESTATO:
			datiILL.setDataFine(now);
			mov.setCodStatoMov("C");	//chiuso
			mov.setCodStatoRic("H");	//conclusa
			break;

		default:
			super.controllaCambiamentoDiStato(mov, datiILL, servizio, _old, _new);
		}

	}

}
