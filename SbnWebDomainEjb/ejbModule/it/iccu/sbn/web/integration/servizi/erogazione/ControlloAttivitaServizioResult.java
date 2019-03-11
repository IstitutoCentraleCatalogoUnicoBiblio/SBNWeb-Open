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
package it.iccu.sbn.web.integration.servizi.erogazione;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;

public enum ControlloAttivitaServizioResult {

	OK,
	OK_NON_ANCORA_DISPONIBILE							("message.servizi.ins.richiesta.ok.no.disp"),
	ERRORE_ACCESSO_DB									("errors.servizi.accesso.db"),
	ERRORE_CONFIGURAZIONE_INCOMPLETA					("errors.servizi.conf.incompleta"),
	ERRORE_LETTORE_NON_PRESENTE_NON_ABILITATO			("errors.servizi.lettore.non.presente"),
	ERRORE_AUTORIZZAZIONE_SCADUTA						("errors.servizi.autorizzazione.scaduta"),
	ERRORE_LETTORE_SOSPESO								("errors.servizi.lettore.sospeso"),
	ERRORE_DOCUMENTO_NON_DISPONIBILE,
	ERRORE_DOCUMENTO_NON_DISPONIBILE_FINO_AL,
	ERRORE_RANGE_SEGNATURE_NON_DISPONIBILE				("errors.servizi.range.segn.nodisp"),
	ERRORE_DOCUMENTO_NON_DISPONIBILE_AL_TIPO_SERVIZIO	("errors.servizi.documento.nodisp.tipoServ"),
	ERRORE_NESSUN_CODICE_FRUIZIONE_DEFAULT				("errors.servizi.no.codfrui.bib"),
	ERRORE_DOCUMENTO_NON_DISPONIBILE_NO_PRENOT			("errors.servizi.documento.nodisp.noprenot"),
	ERRORE_COLLOCAZIONE_PUNTO_DEPOSITO_NO_DOPO_CONSEGNA	("errors.servizi.documento.collocazione.no.dopo.consegna"),

	ERRORE_MOVIMENTO_ATTIVO_SU_DOCUMENTO				("errors.servizi.movimento.attivo.documento"),
	ERRORE_TROPPE_PRENOTAZIONI_TIPO_SERVIZIO			("errors.servizi.troppe.prenot.tiposerv"),
	ERRORE_TROPPE_PRENOTAZIONI_SERVIZIO					("errors.servizi.troppe.prenot.serv"),
	ERRORE_TROPPE_PRENOTAZIONI_DOCUMENTO				("errors.servizi.troppe.prenot.documento"),
	ERRORE_NO_PRENOTAZIONI_DOCUMENTO				    ("errors.servizi.no.prenot.documento"),
	ERRORE_RICHIESTA_PRESENTE_STESSO_LETTORE			("errors.servizi.richiesta.presente.stesso.lettore"),
	ERRORE_PRENOTAZIONE_PRESENTE_STESSO_LETTORE			("errors.servizi.prenot.presente.stesso.lettore"),
	ERRORE_INOLTRO_PRENOTAZIONE_IMPOSSIBILE				("errors.servizi.inoltro.prenot.negato"),
	ERRORE_ANNO_PERIODICO,
	ERRORE_CONTROLLO_BLOCCANTE_NON_SUPERATO,

	ERRORE_PRENOTAZIONE_POSTO_MANCANTE,
	ERRORE_PRENOTAZIONE_POSTO_MANCANTE_INV_DIGITALIZZATO,
	ERRORE_CONSEGNA_DOC_GIORNO_PRENOTAZIONE_POSTO,

	RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO				(	"message.servizi.ins.prenot.mov.attivo",
															"message.servizi.ins.prenot.mov.attivo.altre.prenot1",
															"message.servizi.ins.prenot.mov.attivo.altre.prenot2"),

	RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO		("message.servizi.ins.prenot.mov.non.concluso"),
	RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE,
	RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE,
	//almaviva5_20120607 #5014
	ERRORE_FORMATO_ANNO_PERIODICO,

	//servizi ill
	ERRORE_POST_CONTROLLO,

	ERRORE_ILL_BIBLIOTECA_FORNITRICE_NON_IMPOSTATA,
	ERRORE_ILL_RICHIESTA_SENZA_DOCUMENTO_ASSEGNATO,
	ERRORE_ILL_RICHIESTA_DOCUMENTO_TIPO_D;


	public static final boolean isOK(ControlloAttivitaServizioResult value) {
		return ValidazioneDati.in(value, OK, OK_NON_ANCORA_DISPONIBILE);
	}

	public static final boolean isOK(DatiControlloVO datiControllo) {
		return ValidazioneDati.in(datiControllo.getResult(), OK, OK_NON_ANCORA_DISPONIBILE);
	}

	public static final boolean isIterPrenotazione(ControlloAttivitaServizioResult value) {
		return ValidazioneDati.in(value,
				RICHIESTA_INSERIMENTO_PRENOT_MOV_ATTIVO,
				RICHIESTA_INSERIMENTO_PRENOT_MOV_NON_CONCLUSO,
				RICHIESTA_FORZATURA_RICHIESTA_PRESENTE_STESSO_LETTORE,
				RICHIESTA_FORZATURA_PRENOT_PRESENTE_STESSO_LETTORE);
	}

	private final String[] messages;


	public String getMessage() {
		return messages[0];
	}

	public String getMessage(int idx) {
		if (idx > (messages.length - 1) )
			return messages[0];

		return messages[idx];
	}

	private ControlloAttivitaServizioResult() {
		this.messages = new String[1];
		messages[0] = this.name();
	}

	private ControlloAttivitaServizioResult(String... messages) {
		this.messages = messages;
	}

}
	/*
	if ls_cod_serv="1" or ls_cod_serv="2" or ls_cod_serv="3" or ls_cod_serv="4" then
	   messagebox("Erogazione Servizi","L'esecuzione della procedura 'llduti0' sul server di Biblioteca non è terminata correttamente.",StopSign!)
		return -1
	elseif ls_cod_serv="5" then
	   messagebox("Erogazione Servizi","Lettore non presente o non abilitato al servizio.",StopSign!)
		return -1
	elseif ls_cod_serv="7" then
	   messagebox("Erogazione Servizi","Al Lettore è scaduta l'autorizzazione ad usufruire del servizio.",StopSign!)
		return -1
	elseif ls_cod_serv="6" then
	   messagebox("Erogazione Servizi","Il Lettore è sospeso dal servizio.",StopSign!)
		return -1
	elseif ls_cod_serv="8" then
		   messagebox("Erogazione Servizi","Il documento non è disponibile al servizio.",StopSign!)
		return -1
	//almaviva5_20051124
	elseif ls_cod_serv="10" then
		   messagebox("Erogazione Servizi","Il range di segnature cui appartiene il documento è dichiarato non disponibile.",StopSign!)
		return -1
	elseif ls_cod_serv="11" then
		   messagebox("Erogazione Servizi","Codice di fruizione di default non impostato per la biblioteca.",StopSign!)
		return -1
		//almaviva5_2006070710
	elseif ls_cod_serv="12" then
		   messagebox("Erogazione Servizi","Il documento non è disponibile e il servizio non prevede prenotazione.",StopSign!)
		return -1
	end if

	CASE "L0010"	// Mov attivo senza prenotazione
	messagebox("Erogazione Servizi","Risulta già presente un movimento attivo per il documento selezionato.", StopSign!)

CASE "L0012"	// Troppe prenotazioni su coda richieste
	messagebox("Erogazione Servizi","Troppe prenotazioni attive sul tipo servizio richiesto.", StopSign!)

CASE "L0013"	// Troppe prenotazioni su servizio
	messagebox("Erogazione Servizi","Troppe prenotazioni attive sul servizio richiesto.", StopSign!)

CASE "L0015"	// Richiesta già presente
	messagebox("Erogazione Servizi","Per il documento esiste già una richiesta per lo stesso utente.", StopSign!)

CASE "L0016"	// documento non disponibile e non prenotabile
   messagebox("Erogazione Servizi","Il documento non è disponibile e il servizio non ammette prenotazione.",StopSign!)

CASE ELSE
	messagebox("Erogazione Servizi","L'esecuzione della procedura 'llersa2' sul server di Biblioteca non è terminata correttamente.",StopSign!)
END CHOOSE


	CASE "L0011"	// Chiedo inserimento prenotazione (trovato mov attivo)
		li_msg = messagebox("Erogazione Servizi","Risulta già presente un movimento attivo di tipo '" + ls_tipo_serv + "'" + &
			+ " per il documento selezionato.~n"+ &
			+ "Tornerà disponibile in data " + String(ldt_data_pren) + ". Vuoi inserire una prenotazione?",Question!,YesNo!)

	CASE "L0014"	// Chiedo inserimento prenotazione (trovato mov non concluso)
		li_msg = messagebox("Erogazione Servizi","Risulta presente un movimento concluso di tipo '" + ls_tipo_serv + "'" + &
			+ " per il documento selezionato.~n"+ &
			+ "Tornerà disponibile in data " + String(ldt_data_pren) + ". Vuoi inserire una prenotazione?",Question!,YesNo!)

*/
