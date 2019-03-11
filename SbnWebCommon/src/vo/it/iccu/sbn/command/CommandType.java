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
package it.iccu.sbn.command;

import it.iccu.sbn.ejb.vo.acquisizioni.BilancioDettVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.OrdineStampaOnlineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ordini.stampa.StampaShippingManifestVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.DatiLegameTermineClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.periodici.RicercaPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.SeriePeriodicoType;
import it.iccu.sbn.ejb.vo.periodici.esame.RicercaKardexEsameBiblioPoloVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieBaseVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieOrdineVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.ordini.RicercaOrdiniPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.ModelloPrevisionaleVO;
import it.iccu.sbn.ejb.vo.periodici.previsionale.RicercaKardexPrevisionaleVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO.TipoModello;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public enum CommandType {

	ECHO,
	ECHO2(String.class),

	DETTAGLIO_DOCUMENTO_NON_SBN_CON_ESEMPLARE,
	CATEGORIA_FRUIZIONE_DOCUMENTO_NON_SBN,
	CATEGORIA_FRUIZIONE_DOC_NON_SBN_PER_SALVA,
	LISTA_RICHIESTE_SCADUTE,

	BATCH_SOLLECITI,
	LISTA_MOD_EROGAZIONE_LEGATE_SUPPORTI,
	DETTAGLIO_MOVIMENTO_DI_PRENOTAZIONE,
	ESISTE_MOVIMENTO_ATTIVO_PER_ITER,
	//INVIA_MAIL_UTENTE_PRENOTAZIONE,
	CERCA_ESEMPLARE_DOCUMENTO_LIBERO,
	LISTA_RICHIESTE_STORICO,
	ESISTE_INVENTARIO_BIB_SISTEMA_METRO,

	//almaviva5_20101206 #4043 #4044
	SRV_OCCUPAZIONE_BY_ID,
	SRV_TITOLO_STUDIO_BY_ID,
	SRV_LISTA_ALTRE_BIB_AUTOREG,
	//

	SRV_CONTA_ESEMPLARI_DOCUMENTO_LIBERO(Integer.class),
	SRV_CONTROLLO_DISPONIBILITA_WS,
	//almaviva5_20150429
	SRV_CONTROLLA_MAIL_UTENTE(String.class, String.class, String.class),
	SRV_VALIDA_MODELLO_SOLLECITO(ModelloSollecitoVO.class, TipoModello.class),
	SRV_AGGIORNA_RICHIESTA_PER_PROROGA(MovimentoVO.class, Date.class),
	SRV_NOTIFICA_UTENTE_PRENOTAZIONE(MovimentoVO.class),
	SRV_CANCELLA_TIPO_SERVIZIO(TipoServizioVO.class),
	SRV_NOTIFICA_NUOVA_RICHIESTA(MovimentoVO.class),
	SRV_DOCUMENTO_NON_SBN_CANCELLABILE(DocumentoNonSbnVO.class),

	//almaviva5_20150204 servizi ill
	SRV_ILL_XML_REQUEST(Serializable.class, String.class),
	SRV_ILL_RICERCA_RICHIESTE(DatiRichiestaILLRicercaVO.class),
	SRV_ILL_ALLINEA_RICHIESTA(DatiRichiestaILLVO.class),
	SRV_ILL_AGGIORNA_DATI_RICHIESTA(DatiRichiestaILLVO.class),
	SRV_ILL_DETTAGLIO_RICHIESTA(DatiRichiestaILLVO.class),
	SRV_ILL_RIFIUTA_RICHIESTA(DatiRichiestaILLVO.class),
	SRV_ILL_CONFIGURA_TIPO_SERVIZIO(TipoServizioVO.class),
	SRV_ILL_INSERISCI_PRENOTAZIONE(MovimentoVO.class),
	SRV_ILL_FILTRA_BIBLIOTECHE_AFFILIATE(List.class, String.class, String.class),
	SRV_ILL_INOLTRA_AD_ALTRA_BIBLIOTECA(MovimentoVO.class, BibliotecaVO.class),
	SRV_ILL_AGGIORNA_BIBLIOTECA(BibliotecaVO.class),
	SRV_ILL_INVIA_MESSAGGIO(DatiRichiestaILLVO.class),
	SRV_ILL_PONI_CONDIZIONE(DatiRichiestaILLVO.class),
	SRV_ILL_LISTA_BIBLIOTECHE_POLO_ILL(String.class),
	SRV_ILL_AVANZA_RICHIESTA(DatiRichiestaILLVO.class, String.class, MessaggioVO.class),
	SRV_ILL_CREA_DOC_DA_INVENTARIO(String.class, InventarioVO.class),

	PER_ESAME_SERIE_PERIODICO(RicercaPeriodicoVO.class),
	PER_KARDEX_PERIODICO(RicercaKardexPeriodicoVO.class),
	PER_AGGIORNA_FASCICOLO(SeriePeriodicoType.class, List.class),
	PER_ANNULLA_RICEZIONE_FASCICOLO(SeriePeriodicoType.class, List.class),
	PER_RICEZIONE_FASCICOLO(KardexPeriodicoVO.class, FascicoloVO.class),
	PER_LISTA_ORDINI(RicercaOrdiniPeriodicoVO.class),
	PER_ASSOCIA_FASCICOLI_INVENTARIO(InventarioVO.class, SeriePeriodicoType.class, List.class),
	PER_ASSOCIA_GRUPPO_INVENTARIO(String.class, SeriePeriodicoType.class, List.class),
	PER_LISTA_INVENTARI_COLLOCAZIONE(EsameCollocRicercaVO.class),
	PER_RICEZIONE_MULTIPLA_ORDINE(SerieOrdineVO.class, List.class),
	PER_CANCELLAZIONE_MULTIPLA_FASCICOLI(List.class),
	PER_KARDEX_ESAME_BIBLIO_POLO(RicercaKardexEsameBiblioPoloVO.class),
	PER_CERCA_UNICO_INVENTARIO(SerieBaseVO.class),
	PER_DETTAGLIO_FASCICOLO(List.class, Boolean.class),
	PER_LISTA_MODELLI_PREVISIONALE(ModelloPrevisionaleVO.class),
	PER_CALCOLA_PREVISIONALE(RicercaKardexPrevisionaleVO.class),
	PER_VERIFICA_ESISTENZA_FASCICOLO(FascicoloVO.class, List.class),
	PER_LISTA_BIBLIOTECHE_ESEMPLARE_FASCICOLO(String.class, Integer.class),
	PER_KARDEX_PERIODICO_OPAC(RicercaKardexPeriodicoVO.class),

	SEM_LISTA_CLASSI_COLLEGATE_TERMINE(RicercaClassiTermineVO.class),
	SEM_GESTIONE_LEGAME_TERMINE_CLASSE(DatiLegameTermineClasseVO.class),
	SEM_CAMBIO_EDIZIONE_SOGGETTO(DettaglioSoggettoVO.class, String.class),
	SEM_AGGIORNAMENTO_MASSIVO_SOGGETTI,
	SEM_DATI_CONDIVISIONE_SOGGETTO(String.class, String.class, String.class),
	//almaviva5_20121026 #4768
	SEM_VERIFICA_SISTEMA_EDIZIONE_BIBLIOTECA(String.class, String.class, String.class, String.class),
	//almaviva5_20130128 #5238
	SEM_LISTA_SISTEMA_EDIZIONE_BIBLIOTECA(String.class, String.class, String.class, String.class),
	SEM_AGGIORNA_DATI_CONDIVISIONE_SOGGETTO(List.class),
	//almaviva5_20141008 #5650
	SEM_VERIFICA_CID_CREAZIONE_INDICE(String.class),

	ACQ_SPEDISCI_ORDINE_RILEGATURA(OrdiniVO.class),
	ACQ_STAMPA_ORDINE_RILEGATURA(OrdiniVO.class),
	ACQ_STAMPA_SHIPPING_MANIFEST(StampaShippingManifestVO.class, Serializable.class),
	ACQ_STAMPA_CART_ROUTING_SHEET(OrdiniVO.class),
	//almaviva5_20130412 evolutive google
	ACQ_STAMPA_MODULO_PRELIEVO(OrdineStampaOnlineVO.class),

	ACQ_ESISTE_LEGAME_RIGA_BILANCIO(Integer.class, BilancioDettVO.class),
	//almaviva5_20131007 evolutive google2
	ACQ_ESISTE_INVENTARIO_DIGITALIZZATO(StrutturaInventariOrdVO.class, List.class),
	//almaviva5_20140612 #5078
	ACQ_CERCA_VALUTA_RIFERIMENTO(String.class, String.class),
	//almaviva5_20140618 #4967
	ACQ_IMPORTO_INVENTARI_ORDINE(OrdiniVO.class),
	ACQ_COUNT_INVENTARI_ORDINE(OrdiniVO.class),

	//almaviva5_20140213 evolutive google3
	GB_LOCALIZZAZIONI_PENDENTI,
	// Intervento almaviva2 Novembre 2013 INTERROGAZIONE MASSIVA - per gestione lista bid locali da confrontare con oggetti di Indice
	GB_INTERROG_MASSIMA_CREAZ_LISTE,

	AMM_RELOAD,

	//almaviva5_20150728 evolutiva ICCD
	GDF_POSSEDUTO_DOCUMENTO_WS(String.class, String.class, String.class, Boolean.class, Boolean.class);


	final Class<?>[] signature;

	private CommandType() {
		this.signature = null;
	}

	private CommandType(Class<?>... signature) {
		this.signature = signature;
	}

	public final Class<?>[] getSignature() {
		return signature;
	}

}
