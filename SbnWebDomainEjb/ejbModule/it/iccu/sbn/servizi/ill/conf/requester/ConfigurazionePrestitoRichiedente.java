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
package it.iccu.sbn.servizi.ill.conf.requester;

import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.ill.conf.ConfigurazioneILLBaseImpl;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.in;

public class ConfigurazionePrestitoRichiedente extends ConfigurazioneILLBaseImpl {

	public List<AttivitaServizioVO> getListaAttivitaSuccessive(String codBib,
			String srvLoc, StatoIterRichiesta statoILL,
			StatoIterRichiesta statoLocale, List<AttivitaServizioVO> iterLocale)
			throws Exception {

		List<StatoIterRichiesta> statiLocaliAmmessi = Collections.emptyList();
		AttivitaBuilder ab = new AttivitaBuilder(codBib, srvLoc);

		switch (statoILL) {
		case F100_DEFINIZIONE_RICHIESTA_DA_UTENTE:
		case F111_DEFINIZIONE_RICHIESTA:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F118_INVIO_A_BIB_DESTINATARIA, null, "C"));
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO));
			break;

		case F114_ARRIVO_MATERIALE:
			//<stato iso="F114" origine="R" local="02,03,04,05,06">
			if (!in(statoLocale, StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE)) {
				iterLocale.add(ab.attivita(StatoIterRichiesta.F116_RESTITUZIONE_MATERIALE, "C", "H"));
				iterLocale.add(ab.attivita(StatoIterRichiesta.F116B_MATERIALE_DANNEGGIATO));
			}

			if (!in(statoLocale, StatoIterRichiesta.RESTITUZIONE_DOCUMENTO)) {
				iterLocale.add(ab.attivita(StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO, null, "P"));
			}

			//stati locali ammessi
			statiLocaliAmmessi = new ArrayList<StatoIterRichiesta>(Arrays.asList(
					//StatoIterRichiesta.SCARICO_DA_MAGAZZINO,
					//StatoIterRichiesta.RICOLLOCAZIONE_A_MAGAZZINO,
					StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE,
					StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO
			));

			//include restituzione (04) solo se già consegnato al lettore (03)
			if (!contiene(iterLocale, StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE))
				statiLocaliAmmessi.add(StatoIterRichiesta.RESTITUZIONE_DOCUMENTO);

			break;

		case F116B_MATERIALE_DANNEGGIATO:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA, "C", "B"));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F113B_ACCETTAZIONE_CONDIZIONE_SU_RICHIESTA));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F114_ARRIVO_MATERIALE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO, null, "P"));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F116_RESTITUZIONE_MATERIALE, "C", "H"));
			break;

		case F118_INVIO_A_BIB_DESTINATARIA:
			//almaviva5_20171002 richiesta ICCU: annullabile solo da pagina ILL
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO));
			break;

		case F1211_RICHIESTA_CONDIZIONATA:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F113A_RIFIUTO_CONDIZIONE_SU_RICHIESTA, "C", "B"));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F113B_ACCETTAZIONE_CONDIZIONE_SU_RICHIESTA));
			break;

		case F127_SPEDIZIONE_MATERIALE:
			iterLocale.add(ab.attivita(StatoIterRichiesta.F114_ARRIVO_MATERIALE));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO, "C", "H"));
			break;

		case F129_CONFERMA_RINNOVO:
			if (!in(statoLocale, StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE)) {
				iterLocale.add(ab.attivita(StatoIterRichiesta.F116_RESTITUZIONE_MATERIALE, "C", "H"));
			}

			iterLocale.add(ab.attivita(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO, "C", "H"));

			//stati locali ammessi
			statiLocaliAmmessi = new ArrayList<StatoIterRichiesta>(Arrays.asList(
					StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE,
					StatoIterRichiesta.RESTITUZIONE_DOCUMENTO
			));

			break;

		case F12A_NEGAZIONE_RINNOVO:
			if (!in(statoLocale, StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE)) {
				iterLocale.add(ab.attivita(StatoIterRichiesta.F116_RESTITUZIONE_MATERIALE, "C", "H"));
			}

			iterLocale.add(ab.attivita(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO, "C", "H"));

			//stati locali ammessi
			statiLocaliAmmessi = Arrays.asList(
					StatoIterRichiesta.RESTITUZIONE_DOCUMENTO,
					StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE,
					StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO
			);

			break;

		case F12D_SOLLECITO_RESTITUZIONE_PRESTITO:
			if (!in(statoLocale, StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE)) {
				iterLocale.add(ab.attivita(StatoIterRichiesta.F116_RESTITUZIONE_MATERIALE, "C", "H"));
			}
			iterLocale.add(ab.attivita(StatoIterRichiesta.F115_RICHIESTA_DI_RINNOVO_PRESTITO, null, "P"));
			iterLocale.add(ab.attivita(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO));

			//stati locali ammessi
			if (statoLocale == StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE) {
				statiLocaliAmmessi = Arrays.asList(
						StatoIterRichiesta.RESTITUZIONE_DOCUMENTO,
						StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO
				);
			}

			break;

		case F12E_RICHIESTA_RESTITUZIONE_URGENTE:
			if (!in(statoLocale, StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE)) {
				iterLocale.add(ab.attivita(StatoIterRichiesta.F116_RESTITUZIONE_MATERIALE, "C", "H"));
			}
			iterLocale.add(ab.attivita(StatoIterRichiesta.F116A_PERDITA_DEL_MATERIALE_PRESTATO));

			//stati locali ammessi
			if (statoLocale == StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE) {
				statiLocaliAmmessi = Arrays.asList(
						StatoIterRichiesta.RESTITUZIONE_DOCUMENTO,
						StatoIterRichiesta.COLLOCAZIONE_PUNTO_DEPOSITO
				);
			}

			break;

		case F1214_ACCETTAZIONE_RICHIESTA:
			//almaviva5_20171002 richiesta ICCU: annullabile solo da pagina ILL
			//iterLocale.add(ab.attivita(StatoIterRichiesta.F112A_PROPOSTA_DI_ANNULLAMENTO));
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
		case F128_RICEZIONE_MATERIALE:
			//doc rientrato in bib. fornitrice
			datiILL.setDataFine(now);
			if (mov.getDataFineEff() == null)
				mov.setDataFineEff(now);
			mov.setCodStatoMov("C");	//chiuso
			mov.setCodStatoRic("H");	//conclusa
			break;

		case F12A_NEGAZIONE_RINNOVO:
			//proroga rifiutata
			mov.setCodStatoRic("S");	//non prorogata
			break;

		default:
			super.controllaCambiamentoDiStato(mov, datiILL, servizio, _old, _new);
		}

	}

}
