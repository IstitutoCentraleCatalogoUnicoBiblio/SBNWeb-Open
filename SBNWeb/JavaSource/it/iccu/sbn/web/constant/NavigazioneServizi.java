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
package it.iccu.sbn.web.constant;

import javax.servlet.http.HttpServletRequest;

public final class NavigazioneServizi {

	public static final String BIBLIOTECA_OPERANTE = "gsrv.bib.operante";
	public static final String PRENOTAZIONE_RIFIUTATA = "gsrv.flag.pren.rifiutata";
	public static final String MOVIMENTO_SELEZIONATO = "gsrv.mov.selezionato";
	public static final String LISTA_MOVIMENTI_SELEZIONATI = "gsrv.mov.lista.selezionati";
	public static final String PROVENIENZA = "gsrv.action.caller";
	public static final String DATI_UTENTE_LETTORE = "UtenteBaseVO";
	public static final String DATI_DOCUMENTO = "InfoDocumentoVO";
	public static final String CAMBIO_SERVIZIO = "cambiaServizio";
	public static final String NUOVA_RICHIESTA = "NuovaRichiesta";
	public static final String CHIUDI = "Chiudi";
	public static final String INS_RICHIESTA_LISTA_SUPPORTI = "ins.mov.lista.supporti";
	public static final String INS_RICHIESTA_LISTA_MOD_EROGAZIONE = "ins.mov.lista.mod.erog";
	public static final String RICERCA_UTENTE = "srv.dati.ricerca.utente";

	//almaviva5_20101217 #4074
	public static final String OGGETTO_MODIFICATO = "srv.req.modified";
	public static final String CHIUDI_LISTA_MOVIMENTI = "ChiudiListaMovimenti";
	public static final String CHIUDI_DETTAGLIO_MOVIMENTO = "ChiudiDettaglioMovimento";
	public static final String CHIUDI_DETTAGLIO_UTENTE = "srv.chiudi.dett.utente.bib";
	public static final String CHIUDI_DETTAGLIO_DOCUMENTO = "srv.chiudi.dett.doc.no.sbn";

	public static final String ESAME = "ESAME";
	public static final String GESTIONE = "GESTIONE";
	public static final String DIRITTI = "DIRITTI";
	public static final String DETTAGLIO_MOVIMENTO = "DETTAGLIO_MOVIMENTO";
	public static final String SOLLECITI = "SOLLECITI";

	public static String FAKE_SERIE = "   "; //"cod.serie.inv.fake";

	public static final String BOOKMARK_CONFIGURAZIONE = "srv.conf.bookmark";
	public static final String BOOKMARK_EROGAZIONE = "srv.erogazione.bookmark";

	//almaviva5_20120220 rfid
	public static final String RFID	= "RFID";
	public static final String PRENOTAZIONE_POSTO = "srv.erog.prenot.posto";

	//almaviva5_20150511
	public static final String MODELLO_SOLLECITO = "srv.conf.modello.sollecito";
	public static final String PARAMETRI_BIBLIOTECA = "srv.conf.params.bib";
	public static final String DETTAGLIO_DOCUMENTO = "srv.dett.doc.no.sbn";

	//almaviva5_20171110
	public static final String COD_BIB_UTENTE = "CodBibUte";
	public static final String COD_UTENTE = "CodUte";

	public static final boolean isOggettoModificato(HttpServletRequest request) {
		return (request != null && request.getAttribute(OGGETTO_MODIFICATO) != null);
	}

}
