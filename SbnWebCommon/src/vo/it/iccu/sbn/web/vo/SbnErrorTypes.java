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
package it.iccu.sbn.web.vo;

import java.util.HashSet;
import java.util.Set;

public enum SbnErrorTypes {

	ERROR_GENERIC(0, true),
	UNRECOVERABLE(1, true),
	DB_FALUIRE(2),
	DB_CONCURRENT_MODIFICATION(3),
	TICKET_EXPIRED(4),
	COMMAND_INVOKE_VALIDATION(5),
	COMMAND_INVOKE_METHOD_SIGNATURE(6),
	COMMAND_INVOKE_INVALID_COMMAND(7),

	MAIL_INVALID_PARAMS(8),
	MAIL_DELIVERY_ERROR(9),

	BATCH_INVALID_PARAMS(10),
	BATCH_CONFIGURATION_ERROR(11),
	USER_NOT_AUTHORIZED(12),

	// Errori generici
	ERROR_GENERIC_DATA_INIZIO(13),
	ERROR_GENERIC_DATA_FINE(14),
	ERROR_GENERIC_INTERVALLO_DATA(15),
	ERROR_GENERIC_FORMATO_ANNO(16),
	ERROR_GENERIC_NOT_FOUND(17),
	ERROR_GENERIC_MANDATORY_FIELD(18),
	ERROR_GENERIC_UNMANAGED_FIELD(19),
	ERROR_GENERIC_FIELD_FORMAT(20),
	ERROR_GENERIC_OBJECT_IS_NULL(21),
	ERROR_GENERIC_EMPTY_LIST(22),

	ERROR_DB_MAX_ROWS_EXCEEDED(23),
	ERROR_GENERIC_NUMERIC_NOT_MANDATORY_FIELD(24),
	ERROR_GENERIC_TIPOLOGIA_NON_GESTITA(25),
	ERROR_GENERIC_TOO_MANY_CLIENTS(26),

	ERROR_DB_READ_SEQUENCE(27),

	BATCH_MANUAL_STOP(28),

	ERROR_UNSUPPORTED(29),
	ERROR_GENERIC_FIELD_TOO_LONG(30),
	ERROR_GENERIC_INTERVALLO_ORARIO(31),

	ERROR_IO_READ_FILE(998),
	ERROR_VALIDATOR_IS_NULL(999),


	//Errori Gestione Bibliografica (da 1000 a 1999)
	GB_GENERIC(1000),
	GB_ERRORE_PROTOCOLLO																		(1001),

	//Errori Gestione Semantica (da 2000 a 2999)
	GS_GENERIC(2000),
	GS_DESCRITTORE_DUPLICATO																	(2001),
	GS_DESCRITTORE_PRESENTE_ALTRI_SOGGETTARI													(2002),
	GS_CODICE_SOGGETTARIO_OBBLIGATORIO															(2003),
	GS_CODICE_SOGGETTARIO_OBBLIGATORIO_PER_EDIZIONE												(2004),
	GS_IDENTIFICATIVO_CLASSE_ERRATO																(2005),
	//almaviva5_20141117
	GS_EDIZIONE_DEWEY_NON_VALIDA																(2006),

	//Errori Documento Fisico (da 3000 a 3999)
	GDF_GENERIC(3000),
	GDF_INVENTARIO_NON_TROVATO																	(3001),
	GDF_INVENTARIO_CANCELLATO																	(3002),
	GDF_CODICE_SEZIONE_NON_VERIFICATO															(3003, "error.documentofisico.premereTastoSezione"),
	GDF_RANGE_COLLOCAZIONI_ERRATO																(3004, "error.documentofisico.dallaCollocazioneDeveEssereMinoreOUgualeAllaCollocazione"),
	GDF_RFID_INVENTARIO_ERRATO																	(3005),
	GDF_INVENTARIO_DISMESSO																		(3006),
	GDF_INVENTARIO_DUPLICATO																	(3007),
	GDF_SOLO_UN_RANGE_DATE_INVENTARIO															(3008),
	GDF_SEZIONE_SPAZIO_NON_DEFINITA																(3009),
	GDF_COLLOCAZIONI_RETICOLO_NON_TROVATE														(3010),

	//Errori Acquisizioni (da 4000 a 4999)
	ACQ_GENERIC(4000),
	ACQ_CONFIG_ORDINE_RIGHE_RIPETUTE															(4001, "errors.acquisizioni.righeRipetuteLinguaTipoOrdine"),
	ACQ_CONFIG_ORDINE_TIPO_LAVORAZIONE_RIPETUTO													(4002),
	ACQ_CONFIG_ORDINE_INCOMPLETA																(4003),
	ACQ_DATA_USCITA_OBBLIGATORIA																(4004),
	ACQ_ERRORE_DISPONIBILITA_INVENTARI_RILEGATURA												(4005, "errors.acquisizioni.legameInventariAggDispKO"),
	ACQ_ERRORE_MODIFICA																			(4006, "errors.acquisizioni.erroreModifica"),
	ACQ_ERRORE_INVENTARI_NON_RIENTRATI															(4007),
	ACQ_POSIZIONE_INVENTARIO																	(4008),
	ACQ_RICALCOLO_BILANCIO_FALLITO																(4009),
	ACQ_VALUTA_RIFERIMENTO_NON_CANCELLABILE														(4010),

	//Errori Gestione Servizi (da 5000 a 5999)
	SRV_GENERIC(5000),
	SRV_PARAMETRI_BIBLIOTECA_ASSENTI															(5001, "errors.servizi.param.biblio.assenti"),
	SRV_DOCUMENTO_LETTORE_NON_TROVATO															(5002, "errors.servizi.doc.lettore.non.trovato"),
	SRV_CATEGORIA_FRUIZIONE_DEFAULT_NON_IMPOSTATA												(5003, "errors.servizi.noCatFruizioneDefault"),
	SRV_TIPO_SERVIZIO_NON_TROVATO																(5004),
	SRV_CONTROLLO_DEFAULT_ATTIVITA_NON_SUPERATO													(5005),
	SRV_DATA_INIZIO_INTERVALLO_ERRATA															(5006, "errors.servizi.documenti.dataInizioErrata"),
	SRV_DATA_FINE_INTERVALLO_ERRATA																(5007, "errors.servizi.documenti.dataFineErrata"),
	SRV_INTERVALLO_DATE_ERRATO																	(5008, "errors.servizi.documenti.intervalloDateErrato"),
	SRV_SEGNATURA_CANCELLATA_ESISTENTE															(5009),
	SRV_SEGNATURA_ESISTENTE																		(5010, "errors.servizi.documenti.segnaturaEsistente"),
	SRV_AUTORIZZAZIONE_ESISTENTE																(5011, "errors.servizi.documenti.autorizzazioneEsistente"),
	SRV_BIBLIOTECA_NO_SOLLECITI																	(5012, "errors.servizi.configurazione.parametri.noSolleciti"),
	SRV_ERRORE_BLOCCANTE_CAMBIO_SERVIZIO														(5013),
	SRV_SEGNATURA_NON_DISPONIBILE																(5014),
	SRV_NUSSUN_ELEMENTO_TROVATO																	(5015, "errors.servizi.ListaVuota"),
	SRV_CONTROLLO_ITER_BLOCCANTE																(5016),
	SRV_ERRORE_DOCUMENTO_INCOMPLETO																(5017),
	SRV_ERRORE_SUPPORTO_LEGATO_RICHIESTA														(5018),
	SRV_ERRORE_SUPPORTO_LEGATO_PARAMETRI_BIB													(5019),

	SRV_SIP2_INVALID_PARAMS																		(5020),
	//almaviva5_20120221 rfid
	SRV_ERRORE_RFID_BIBLIOTECA																	(5021),
	SRV_ERRORE_MATERIA_LEGATA_UTENTE															(5022),
	SRV_ERRORE_INTERVALLO_COPIE																	(5023, "message.servizi.erogazione.dettaglioMovimento.intervalloCopiaErrato"),

	SRV_ERRORE_DOPPIA_MAIL_UTENTE																(5024),
	SRV_ERRORE_EMAIL_DUPLICATA																	(5025, "errors.servizi.utenti.emailEsistente"),
	SRV_ERRORE_EMAIL_SECONDARIA_NON_PRESENTE													(5026),
	SRV_UTENTE_NON_TROVATO																		(5027, "errors.servizi.UtenteNonPresente"),

	SRV_ERRORE_MODELLO_SOLLECITO																(5028),

	SRV_RICHIESTA_NON_PROROGABILE																(5029),

	//almaviva5_20151021 #6007
	SRV_ERRORE_EMAIL_NON_VALIDA																	(5030),

	SRV_ERRORE_CONFIGURAZIONE_ITER																(5031, "errors.servizi.erroreConfigurazioneIter"),
	SRV_ERRORE_TIPO_SERVIZIO_CON_LEGAMI															(5032),
	SRV_UTENTE_NON_AUTORIZZATO																	(5033),

	SRV_SALA_ESISTENTE																			(5034),
	SRV_SALA_INTERVALLO_POSTI_ERRATO															(5035),
	SRV_SALA_GRUPPO_POSTI_SOVRAPPOSTO															(5036),
	SRV_SALA_TOTALE_POSTI_ERRATO																(5037),

	SRV_ERROR_CALENDARIO_FASCE_SOVRAPPOSTE														(5100),
	SRV_ERROR_CALENDARIO_INTERVALLI_SOVRAPPOSTI													(5101),
	SRV_ERROR_CALENDARIO_INTERVALLO_APERTO														(5102),
	SRV_ERROR_CALENDARIO_TROPPI_INTERVALLI_APERTI												(5103),
	SRV_ERROR_CALENDARIO_BIB_NON_TROVATO														(5104),
	SRV_ERROR_GIORNO_NON_INCLUSO_CALENDARIO_BIB													(5105),
	SRV_ERROR_FASCIA_NON_INCLUSA_CALENDARIO_BIB													(5106),
	SRV_ERROR_POSTO_PRENOTAZIONE_ASSENTE														(5107),
	SRV_ERROR_SALE_PRENOTAZIONE_ATTIVA															(5108),
	SRV_ERROR_SALE_CAT_MEDIAZIONE_POSTO_NON_MODIFICABILE										(5109),
	SRV_ERROR_SALE_CAT_MEDIAZIONE_NON_TROVATA_NON_GESTITA										(5110),
	SRV_ERROR_SALE_POSTO_NON_DISPONIBILE														(5111),
	SRV_ERROR_SALE_UTENTE_GIA_PRENOTATO															(5112),

	SRV_UTENTE_ANAGRAFE_BIB_ESISTENTE															(5150),

	SRV_TIPO_ACCESSO_UTENTE_NON_VALIDO															(5200),

	//Errori servizi ILL (da 5500)
	SRV_ILL_CODICE_ANAGRAFE_OBBLIGATORIO														(5500),
	SRV_ILL_ERRORE_VALIDAZIONE_APDU																(5501),
	SRV_ILL_TIPO_AUTORIZZAZIONE_ASSENTE															(5502),
	SRV_ILL_ERRORE_SERVER																		(5503),
	SRV_ILL_ERRORE_CONNESSIONE_OPAC																(5504),
	SRV_ILL_TROPPE_BIBLIOTECHE_SELEZIONATE														(5505),
	SRV_ILL_PRIORITA_BIB_DUPLICATA																(5506),
	SRV_ILL_BIBLIOTECA_FORNITRICE_NON_IMPOSTATA													(5507),
	SRV_ILL_ERRORE_Z3950_QUERY																	(5508),
	SRV_ILL_ERRORE_CONFIGURAZIONE_ILL_INCOMPLETA												(5509),
	SRV_ILL_TROPPI_SERVIZI_LEGATI_A_SERVIZIO_ILL												(5510),

	//Errori Amministrazione (da 6000 a 6999);
	AMM_GENERIC(6000),
	AMM_CONFIGURAZIONE_CODICE																	(6001),
	AMM_CODICE_CAMPO_OBBLIGATORIO																(6002),
	AMM_CODICE_FORMATO_ERRATO																	(6003),
	AMM_CODICE_LUNGHEZZA_CODICE																	(6004),
	AMM_USER_NOT_FOUND																			(6005),
	AMM_USER_NON_ACTIVE																			(6006),
	AMM_USER_NOT_PROFILED																		(6007),
	AMM_ERRORE_INIZIALIZZAZIONE_CODICI															(6008),
	//almaviva5_20120127 #4842
	AMM_BIBLIOTECA_ESISTENTE																	(6009),
	//almaviva5_20120529 #5005
	AMM_COD_ANAGRAFE_BIBLIOTECA_ESISTENTE														(6010),
	AMM_SCRIPT_ERROR																			(6011),
	AMM_ERRORE_INTERROGAZIONE_CODICI															(6012),
	AMM_BIBLIOTECA_NON_TROVATA																	(6013),
	AMM_BATCH_SCHEDULAZIONE_ERRATA																(6014),

	//Errori Unimarc
	UNI_GENERIC(7000),
	UNI_ALMENO_UN_MATERIALE																		(7001),
	UNI_ALMENO_UNA_NATURA																		(7002),
	UNI_ESTRAZIONE_BIB_ATENEO																	(7003),
	UNI_TROPPE_BIBLIOTECHE_PER_RANGE															(7004),
	UNI_SPECIFICARE_SOLO_UN_RANGE_POSSEDUTO														(7005),
	UNI_CRITERI_NON_VALIDI																		(7006),
	UNI_FILE_ID_NON_PRESENTE																	(7007),
	UNI_TIPO_ESTRAZIONE_NON_VALIDO																(7008),
	UNI_SOLO_LOCALI_O_CONDIVISI																	(7009),

	//Errori periodici
	PER_GENERIC(8000),
	PER_ERRORE_INTERVALLO_NUM_FASCICOLO															(8001),
	PER_ERRORE_NUM_ALTER_FASCICOLO_MULTIPLO														(8002),
	PER_ERRORE_NUM_ALTER_FASCICOLO_ALTERNATIVI													(8003),
	PER_ERRORE_NUM_ALTER_FASCICOLO_ALMENO_UNO													(8004),
	PER_ERRORE_INTERVALLO_DATE_OBBLIGATORIO														(8005),
	PER_ASSOCIARE_ORDINE																		(8006),
	PER_GRUPPO_VALIDO_SOLO_PER_INVENTARIO														(8007),
	PER_FASCICOLO_DUPLICATO																		(8008),
	PER_ERRORE_SMARRITO_RILEGATURA_ALTERNATIVI													(8009),
	PER_ANNO_RIFERIMENTO_ORDINE_OBBLIGATORIO													(8010),
	PER_ERRORE_ORDINE_NON_CONTINUATIVO															(8011),
	PER_DATA_POSIZIONA_ERRATA																	(8012),
	//almaviva5_2011104 #4654
	//PER_ESISTE_FASCICOLO_ALTRI_ESEMPLARI														(8013),
	PER_ASSOCIARE_INVENTARIO																	(8014),
	PER_NUMERO_PEZZI_TROPPO_GRANDE																(8015),
	PER_ERRORE_DATA_CONV_INCONGRUENTE															(8016),
	PER_ERRORE_FASCICOLO_RICEVUTO																(8017),
	PER_ERRORE_FASCICOLO_POSSEDUTO																(8018),
	PER_ERRORE_INVENTARIO_GRUPPO																(8019),
	PER_ERRORE_TIPO_PERIODICITA_NON_GESTITO														(8020),
	PER_ERRORE_PREVISIONALE_DEADLOCK															(8021),
	PER_ERRORE_PREVISIONALE_GRUPPI_DATE_INCONGRUENTI											(8022),
	PER_ERRORE_TIPO_NUMERAZIONE_NON_VALIDO														(8023),
	PER_ERRORE_CONFIG_TIPO_NUMERAZIONE															(8024),
	PER_ERRORE_NUMERO_FASCICOLO_NON_PRESENTE													(8025),
	PER_ERRORE_TIPO_NUMERAZIONE_MODELLO_NON_CONT												(8026),
	PER_ERRORE_NUM_PRIMO_FASCICOLO_SUPERIORE_MAX_VOLUME											(8027),
	PER_ERRORE_DATA_FASCICOLO_ECCEDE_ANNATA														(8028),
	PER_ERRORE_POS_FASCICOLO_ECCEDE_FASCICOLI_PER_VOLUME										(8029),
	PER_ERRORE_GIORNI_OBBLIGATORI_PERIODICITA													(8030),
	PER_ERRORE_ESISTE_FASCICOLO_LEGATO_ORDINE													(8031),
	//almaviva5_20111028 #4705
	PER_ERRORE_FASCICOLO_POSSEDUTO_POLO															(8032),
	PER_ERRORE_ANNATA_INCONGRUENTE																(8033),
	PER_ERRORE_TITOLO_NON_PERIODICO																(8034),
	//almaviva5_20121026
	PER_ERRORE_NUMERO_FASCICOLI_PER_VOL_ANNATE_DOPPIE											(8035);

	///////////////////////////////////////////////////////////////////////////

	private final int errorCode;
	private final String msg;
	private final boolean includeId;

	private SbnErrorTypes(int intCode, String msg) {
		this.errorCode = intCode;
		this.msg = msg;
		this.includeId = false;
	}

	private SbnErrorTypes(final int intCode) {
		this.errorCode = intCode;
		this.msg = this.name();
		this.includeId = false;
	}

	private SbnErrorTypes(final int intCode, final boolean includeId) {
		this.errorCode = intCode;
		this.msg = this.name();
		this.includeId = includeId;
	}

	public int getIntCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return msg;
	}

	public boolean isIncludeId() {
		return includeId;
	}

	public static void main(String... args) {
		//check duplicati
		Set<Integer> types = new HashSet<Integer>();
		System.out.println("--- INIZIO TEST ---");
		for (SbnErrorTypes e : SbnErrorTypes.values()) {
			int error = e.getIntCode();
			if (types.contains(error))
				System.out.println("Codice duplicato: " + error);
			else
				types.add(error);
		}
		System.out.println("--- FINE TEST ---");
	}

}
