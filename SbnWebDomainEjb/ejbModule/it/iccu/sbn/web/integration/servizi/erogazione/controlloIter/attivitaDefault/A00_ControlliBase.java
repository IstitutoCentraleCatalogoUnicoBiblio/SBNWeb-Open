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
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.web.constant.PeriodiciConstants;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.AbstractAttivitaCheckerBase;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class A00_ControlliBase extends AbstractAttivitaCheckerBase {

	public A00_ControlliBase() throws RemoteException, NamingException, CreateException {
		super();
	}

	@Override
	public ControlloAttivitaServizioResult check(DatiControlloVO dati) throws Exception {

		dati.setControlloEseguito(StatoIterRichiesta.RICHIESTA_SERVIZIO);

		MovimentoVO movimento = dati.getMovimento();
		Servizi servizi = delegate.getGestioneServizi();
		ControlloAttivitaServizioResult check = ControlloAttivitaServizioResult.OK;

		String ticket = dati.getTicket();
		String codPolo = movimento.getCodPolo();
		String codBib = movimento.getCodBibOperante();
		String codBibUte = movimento.getCodBibUte();
		String codUtente = movimento.getCodUte();

		boolean controllaCodFrui = true;

		//almaviva5_20151217 controllo bib. fornitrice per ILL
		if (movimento.isRichiestaILL()) {
			DatiRichiestaILLVO datiILL = movimento.getDatiILL();

			if (datiILL.isRichiedente() ) {
				//la richiesta ill non supporta inventari ma solo doc. non sbn
				if (movimento.isRichiestaSuInventario())
					return ControlloAttivitaServizioResult.ERRORE_ILL_RICHIESTA_SENZA_DOCUMENTO_ASSEGNATO;

				DocumentoNonSbnVO doc = datiILL.getDocumento();
				if (doc == null)
					return ControlloAttivitaServizioResult.ERRORE_ILL_RICHIESTA_SENZA_DOCUMENTO_ASSEGNATO;

				//solo tipo D per richiedente
				if (doc.getTipo_doc_lett() != 'D')
					return ControlloAttivitaServizioResult.ERRORE_ILL_RICHIESTA_DOCUMENTO_TIPO_D;

				//il cod fruizione non Ã¨ significativo per le richieste ill come richiedente
				controllaCodFrui = false;
			}

			if (!ValidazioneDati.isFilled(datiILL.getResponderId()))
				return ControlloAttivitaServizioResult.ERRORE_ILL_BIBLIOTECA_FORNITRICE_NON_IMPOSTATA;
		}

		// controllo anno periodico
		if (movimento.isPeriodico()) {
			String annata = movimento.getAnnoPeriodico();
			if (ValidazioneDati.strIsNull(annata) || !ValidazioneDati.strIsNumeric(annata))
				return ControlloAttivitaServizioResult.ERRORE_ANNO_PERIODICO;
			//almaviva5_20120607 #5014
			if (!annata.matches(PeriodiciConstants.REGEX_FORMATO_ANNO))
				return ControlloAttivitaServizioResult.ERRORE_FORMATO_ANNO_PERIODICO;
		}

		// 1. controllo esistenza utente
		UtenteBaseVO utente = servizi.getUtente(ticket, codPolo, codBibUte, codUtente, codBib);
		if (utente == null)
			return ControlloAttivitaServizioResult.ERRORE_LETTORE_NON_PRESENTE_NON_ABILITATO;


		Timestamp now = DaoManager.now();
		String codTipoServ = movimento.getCodTipoServ();
		String codServ = movimento.getCodServ();


		// 3. controllo sospensione utente
		boolean utenteSospeso = servizi.isUtenteSospeso(ticket, codPolo,
				codBibUte, codUtente, codBib, codTipoServ, codServ, now);
		if (utenteSospeso)
			return ControlloAttivitaServizioResult.ERRORE_LETTORE_SOSPESO;


		// 4. controllo abilitazione utente
		boolean utenteAutorizzato = servizi.isUtenteAutorizzato(ticket, codPolo,
				codBibUte, codUtente, codBib, codTipoServ, codServ, now);
		if (!utenteAutorizzato)
			return ControlloAttivitaServizioResult.ERRORE_AUTORIZZAZIONE_SCADUTA;


		// 5. verifico la disponibilitÃ  del documento e recupero la Categoria di Fruizione del documento
		if (movimento.isRichiestaSuInventario()) {
			InventarioVO inventario = delegate.getInventario().getInventario(codPolo,
					movimento.getCodBibInv(), movimento.getCodSerieInv(),
					Integer.valueOf(movimento.getCodInvenInv()), null,
					ticket);

			//almaviva5_20190110 #6872 check inventario collocato
			if (!inventario.isCollocato() ) {
				return ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE;
			}

			movimento.setCat_fruizione(inventario.getCodFrui());

			if (ValidazioneDati.isFilled(inventario.getCodNoDisp()) ) {
				dati.setCodNoDisp(inventario.getCodNoDisp());
				dati.setDataRedisp(inventario.getDataRedisp());
				return ValidazioneDati.isFilled(inventario.getDataRedisp()) ?	//con data rientro
					ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE_FINO_AL :
					ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE;
			}

		}

		if (movimento.isRichiestaSuSegnatura()) {
			DocumentoNonSbnVO docNonSbn = servizi.getCategoriaFruizioneSegnatura(ticket, movimento);
			if (ValidazioneDati.isFilled(docNonSbn.getCodNoDisp()) ) {
				dati.setCodNoDisp(docNonSbn.getCodNoDisp());
				return ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE;
			}

			movimento.setCat_fruizione(docNonSbn.getCodFruizione());
		}

		// imposto la Categoria di Fruizione a partire da MovimentoVO
		String codFrui = movimento.getCat_fruizione();


		// 6. verifica cat. fruizione su tipo servizio
		boolean checkFrui = servizi.checkRelazioneTipoServizioCodFruizione(ticket,
				codPolo, codBib,
				codTipoServ, codFrui);

		if (!checkFrui && controllaCodFrui)
			return ControlloAttivitaServizioResult.ERRORE_DOCUMENTO_NON_DISPONIBILE_AL_TIPO_SERVIZIO;

		// 7. controllo movimenti e richieste pendenti sul documento
		//almaviva5_20110920 #3668 il controllo della disponibilitÃ  va eseguito al netto di eventuali prelazioni;
		//a tal scopo si riporta (temporaneamente) la data inizio prev. alla data reale della richiesta
		MovimentoVO movNoPrel = movimento.copy();
		movNoPrel.setDataInizioPrev(movNoPrel.getDataRichiesta());
		//
		ControlloDisponibilitaVO disponibilita = new ControlloDisponibilitaVO(movNoPrel, dati.isInoltroPrenotazione() );
		servizi.controlloDisponibilita(ticket, disponibilita);
		check = disponibilita.getResult();
		dati.setCheckData(disponibilita);

		//Ã© davvero un errore?
		if (!ControlloAttivitaServizioResult.isOK(check) &&
			!ControlloAttivitaServizioResult.isIterPrenotazione(check))
			return check;

		// 2. controllo movimento giÃ  presente per il doc. richiesto
		if (servizi.esisteRichiestaPerUtente(ticket, movimento) ) {

			//sulle richieste per periodico Ã© lasciata facoltÃ  ai bibliotecari di
			//forzare l'inserimento anche in presenza di movimenti giÃ  attivi
			if (movimento.isPeriodico() && dati.getOperatore() == OperatoreType.BIBLIOTECARIO)
				return ControlloAttivitaServizioResult.RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE;
			else
				return ControlloAttivitaServizioResult.ERRORE_RICHIESTA_PRESENTE_STESSO_LETTORE;
		}

		if (servizi.esistePrenotazionePerUtente(ticket, movimento) ) {

			//sulle richieste per periodico Ã© lasciata facoltÃ  ai bibliotecari di
			//forzare l'inserimento anche in presenza di prenotazioni giÃ  attive
			if (movimento.isPeriodico() && dati.getOperatore() == OperatoreType.BIBLIOTECARIO)
				return ControlloAttivitaServizioResult.RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE;
			else
				return ControlloAttivitaServizioResult.ERRORE_PRENOTAZIONE_PRESENTE_STESSO_LETTORE;
		}

		return check;
	}

}
