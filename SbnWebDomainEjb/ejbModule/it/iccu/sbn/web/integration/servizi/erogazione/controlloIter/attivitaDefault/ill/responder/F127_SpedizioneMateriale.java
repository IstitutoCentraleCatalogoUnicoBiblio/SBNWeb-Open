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
package it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.responder;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO.TipoInvio;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLRequestBuilder;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLAPDU;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.attivitaDefault.ill.AbstractAttivitaCheckerILL;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class F127_SpedizioneMateriale extends AbstractAttivitaCheckerILL {

	public F127_SpedizioneMateriale() throws RemoteException,
			NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati)
			throws Exception {
		dati.setControlloEseguito(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE);

		MovimentoVO mov = dati.getMovimento();
		dati.setMovimentoAggiornato(mov);
		mov.setDataInizioEff(DaoManager.now());

		//prepara messaggio
		MessaggioVO msg = new MessaggioVO(mov.getDatiILL());
		msg.setDataMessaggio(DaoManager.now());
		msg.setTipoInvio(TipoInvio.INVIATO);
		msg.setStato(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE.getISOCode());
		dati.setUltimoMessaggio(msg);

		DatiRichiestaILLVO datiILL = mov.getDatiILL();
		try {
			datiILL.setImporto(mov.getCostoServizioDouble());
			datiILL.setDataScadenza(mov.getDataFinePrev());
		} catch (ParseException e) { }

		return ControlloAttivitaServizioResult.OK;
	}

	@Override
	public ControlloAttivitaServizioResult post(DatiControlloVO dati) throws Exception {
		MessaggioVO msg = dati.getUltimoMessaggio();
		try {
			MovimentoVO mov = dati.getMovimento();

			/*alla spedizione del documento il servizio locale viene avanzato automaticamente:
				prestito, consultazione -> CONSEGNA_DOCUMENTO_AL_LETTORE
				riproduzione			-> CONSEGNA_COPIE
			*/
			CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(mov.getCodTipoServ(),
					CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
			if (ts == null)
				throw new ApplicationException(SbnErrorTypes.SRV_TIPO_SERVIZIO_NON_TROVATO);

			//stato locale a cui avanzare la richiesta
			StatoIterRichiesta statoLocale = StatoIterRichiesta.CUSTOM;
			if (ts.isPrestito() || ts.isConsultazione() )
				statoLocale = StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE;
			if (ts.isRiproduzione() )
				statoLocale = StatoIterRichiesta.CONSEGNA_COPIE;

			ControlloAttivitaServizioResult check = checkAttivitaLocale(dati, statoLocale);
			if (!ControlloAttivitaServizioResult.isOK(check))
				return check;

			ILLAPDU response = ILLRequestBuilder.spedizioneMateriale(mov, msg);
			check = checkResponse(response, dati.setControlloEseguito(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE));

			return check;

		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			log.error("", e);
			return ControlloAttivitaServizioResult.ERRORE_POST_CONTROLLO;
		}
	}

	private ControlloAttivitaServizioResult checkAttivitaLocale(DatiControlloVO dati, StatoIterRichiesta statoLocale) throws Exception {

		ControlloAttivitaServizioResult check = ControlloAttivitaServizioResult.OK;
		MovimentoVO mov = dati.getMovimento();
		DatiRichiestaILLVO datiILL = mov.getDatiILL().copy();
		datiILL.setCurrentState(StatoIterRichiesta.F127_SPEDIZIONE_MATERIALE.getISOCode());
		List<AttivitaServizioVO> attivitaSuccessive = delegate.getGestioneServizi().getListaAttivitaSuccessive(dati.getTicket(),
			mov.getCodPolo(), mov.getCodBibOperante(), mov.getCodTipoServ(), Integer.parseInt(mov.getProgrIter()), datiILL);

		AttivitaServizioVO asloc = get(attivitaSuccessive, statoLocale);
		if (asloc != null) {
			//si eseguono i controlli per questo iter
			ControlloAttivitaServizio cas = new ControlloAttivitaServizio(asloc);
			//cas.controlloIter(dati, messaggi, controlloAggiornamento, controlloInoltraPrenotazione)
			check = cas.controlloDefault(dati);
			if (check == ControlloAttivitaServizioResult.OK) {
				IterServizioVO iter = asloc.getPassoIter();
				mov.setProgrIter(iter.getProgrIter().toString());
				mov.setCodStatoMov(iter.getCodStatoMov());
				mov.setCodStatoRic(iter.getCodStatoRich());
				mov.setCodAttivita(iter.getCodAttivita());
				dati.setMovimentoAggiornato(mov);
			}
		}

		return check;
	}

}
