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
package it.iccu.sbn.util.config;

public class Configuration {

	public static final String REDEPLOY_WARNING_MESSAGE = "REDEPLOY_WARNING_MESSAGE";

	public static final String DATA_SOURCE = "DATA_SOURCE";

	public static final String QUEUE_NAME = "QUEUE_NAME";
	public static final String MESSAGE_TTL = "MESSAGE_TTL";

	public static final String SBNWEB_BATCH_FILES_PATH = "SBNWEB_BATCH_FILES_PATH";

	public static final String SBNWEB_EXPORT_UNIMARC_HOME = "SBNWEB_EXPORT_UNIMARC_HOME";
	public static final String SBNWEB_EXPORT_UNIMARC_UPDATE_DB_COPY = "SBNWEB_EXPORT_UNIMARC_UPDATE_DB_COPY";

	public static final String CLONER_CLASS = "CLONER_CLASS";
	public static final String CLONER_POOL_SIZE = "CLONER_POOL_SIZE";

	public static final String JBAC_BUFFER_SIZE = "JBAC_BUFFER_SIZE";

	public static final String BATCH_CLEANING_AGE_THRESHOLD = "BATCH_CLEANING_AGE_THRESHOLD";
	public static final String BATCH_CLEANING_DELETE_OUTPUTS = "BATCH_CLEANING_DELETE_OUTPUTS";
	public static final String BATCH_USER_DELETE_OUTPUTS = "BATCH_USER_DELETE_OUTPUTS";

	public static final String LOG_LEVEL_SBNWEB = "LOG_LEVEL_SBNWEB";
	public static final String LOG_LEVEL_SBNMARC = "LOG_LEVEL_SBNMARC";
	public static final String LOG_LEVEL_HIBERNATE = "LOG_LEVEL_HIBERNATE";

	public static final String SBNWEB_HTTP_SESSION_TIMEOUT = "SBNWEB_HTTP_SESSION_TIMEOUT";
	public static final String SERVIZI_HTTP_SESSION_TIMEOUT = "SERVIZI_HTTP_SESSION_TIMEOUT";

	public static final String DEFAULT_DISATTIVAZIONE_UTENTE = "DEFAULT_DISATTIVAZIONE_UTENTE";
	public static final String DEFAULT_RINNOVO_PASSWORD = "DEFAULT_RINNOVO_PASSWORD";

	public static final String SIP2_BIND_PORT = "SIP2_BIND_PORT";
	public static final String SIP2_CURR_POLE = "SIP2_CURR_POLE";
	public static final String SIP2_CURR_BIBLIO = "SIP2_CURR_BIBLIO";
	public static final String SIP2_CURR_COD_TIPOSERVIZIO = "SIP2_CURR_COD_TIPOSERVIZIO";
	public static final String SIP2_CURR_COD_SERVIZIO = "SIP2_CURR_COD_SERVIZIO";
	public static final String SIP2_TOTEM_ = "SIP2_TOTEM_";
	public static final String SIP2_BASE_URL = "SIP2_BASE_URL";

	public static final String DEBUG_SBNMARC_URL = "DEBUG_SBNMARC_URL";
	public static final String SBNMARC_LOCAL_DEBUG = "SBNMARC_LOCAL_DEBUG";
	//almaviva5_20140116
	public static final String DEBUG_SBNMARC_URL_INDICE = "DEBUG_SBNMARC_URL_INDICE";
	//almaviva5_20160523
	public static final String DEBUG_SBNMARC_USER_INDICE = "DEBUG_SBNMARC_USER_INDICE";
	public static final String DEBUG_SBNMARC_PASSWORD_INDICE = "DEBUG_SBNMARC_PASSWORD_INDICE";

	public static final String PAGE_CACHE_STRATEGY_CLASS = "PAGE_CACHE_STRATEGY_CLASS";

	public static final String MAX_RESULT_ROWS = "MAX_RESULT_ROWS";

	public static final String SMS_PROVIDER_CLASS = "SMS_PROVIDER_CLASS";

	//almaviva5_20111215 proxy indice
	public static final String SBNMARC_INDICE_USE_PROXY = "SBNMARC_INDICE_USE_PROXY";
	public static final String SBNMARC_INDICE_PROXY_URL = "SBNMARC_INDICE_PROXY_URL";
	public static final String SBNMARC_INDICE_PROXY_PORT = "SBNMARC_INDICE_PROXY_PORT";
	public static final String SBNMARC_INDICE_PROXY_USERNAME = "SBNMARC_INDICE_PROXY_USERNAME";
	public static final String SBNMARC_INDICE_PROXY_PASSWORD = "SBNMARC_INDICE_PROXY_PASSWORD";

	//almaviva5_20111222 timeout indice per allineamento
	public static final String SBNMARC_INDICE_ALLINEA_REQUEST_TIMEOUT = "SBNMARC_INDICE_ALLINEA_REQUEST_TIMEOUT";

	public static final String CODICE_SOGGETTARIO_FIRENZE = "CODICE_SOGGETTARIO_FIRENZE";

	//20110208 - Importa Utenti
	public static final String SBNWEB_IMPORTA_UTENTI_HOME = "SBNWEB_IMPORTA_UTENTI_HOME";

	//almaviva5_20122020 rfid
	public static final String RFID_ENABLE = "RFID_ENABLE";

	public static final String FUSIONE_DELEGATE_CLASS = "FUSIONE_DELEGATE_CLASS";

	public static final String SBNWEB_EXPORT_CONFIG_FILE = "SBNWEB_EXPORT_CONFIG_FILE";
	public static final String SBNWEB_EXPORT_IGNORE_FILE = "SBNWEB_EXPORT_IGNORE_FILE";
	public static final String EXPORT_UNIMARC_FILE_ACCESSORI = "EXPORT_UNIMARC_FILE_ACCESSORI";

	public static final String WS_MAX_CONCURRENT_CLIENTS = "WS_MAX_CONCURRENT_CLIENTS";

	public static final String EXPORT_UNIMARC_AUTORI_FILE_ACCESSORI = "EXPORT_UNIMARC_AUTORI_FILE_ACCESSORI";

	public static final String CSV_FIELD_SEPARATOR = "CSV_FIELD_SEPARATOR";

	//almaviva5_20140408
	public static final String ESAME_COLLOCAZIONI_MAX_RESULT_ROWS = "ESAME_COLLOCAZIONI_MAX_RESULT_ROWS";

	//almaviva5_20140303 evolutive google3
	public static final String LOC_MASSIVA_INDICE_WAIT_TIMEOUT = "LOC_MASSIVA_INDICE_WAIT_TIMEOUT";

	public static final String SBNWEB_IMPORT_UNIMARC_HOME = "SBNWEB_IMPORT_UNIMARC_HOME";

	public static final String JASPER_REPORT_LOG_THRESHOLD = "JASPER_REPORT_LOG_THRESHOLD";

	//almaviva5_20141013 evolutiva servizi ill
	public static final String ILL_SBN_SERVER_URL = "ILL_SBN_SERVER_URL";
	public static final String ILL_SBN_DOC_DELIVERY_URL = "ILL_SBN_DOC_DELIVERY_URL";

	public static final String SBNMARC_SCHEMA_VERSION = "SBNMARC_SCHEMA_VERSION";

	//almaviva5_20180416 scarico anagrafe biblioteche
	public static final String JSON_ANAGRAFE_BIBLIOTECHE_PATH = "JSON_ANAGRAFE_BIBLIOTECHE_PATH";

	//almaviva5_20150126
	public static final String INDICE_CONNECTION_CHECKER_TIMEOUT = "INDICE_CONNECTION_CHECKER_TIMEOUT";

	//almaviva5_20150410
	public static final String SBNMARC_ALLINEAMENTO_SU_FILE_INTERVAL = "SBNMARC_ALLINEAMENTO_SU_FILE_INTERVAL";

	//almaviva5_20180206
	public static final String SRV_EVENTO_ACCESSO_MERGE_THRESHOLD = "SRV_EVENTO_ACCESSO_MERGE_THRESHOLD";

	//almaviva5_20151120
	public static final String SRV_UTENTE_NAZIONE = "SRV_UTENTE_NAZIONE";

	//richiesta DDS: l'utente che prenota un posto può essere accompagnato da uno o più utenti
	public static final String SRV_PRENOTAZIONE_POSTO_UTENTI_MULTIPLI = "SRV_PRENOTAZIONE_POSTO_UTENTI_MULTIPLI";

	//almaviva5_20160728
	public static final String SRV_ACCESSO_UTENTE_ESCLUDI_AUTOREGISTRATI = "SRV_ACCESSO_UTENTE_ESCLUDI_AUTOREGISTRATI";

	//almaviva5_20161005 evolutiva oclc
	public static final String GB_CARICA_OCLC_CONTROL_NUMBER = "GB_CARICA_OCLC_CONTROL_NUMBER";

	//almaviva5_20160426 servizi ILL
	public static final String ILL_XML_SAVE_PATH = "ILL_XML_SAVE_PATH";
	public static final String OPAC_Z3950_SEARCH_ENABLE = "OPAC_Z3950_SEARCH_ENABLE";
	public static final String OPAC_Z3950_CLIENT = "OPAC_Z3950_CLIENT";
	public static final String OPAC_Z3950_URL = "OPAC_Z3950_URL";
	public static final String OPAC_Z3950_DB = "OPAC_Z3950_DB";

	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.

	public static final String SBNWEB_BATCH_ALLINEAMENTO_DA_LOCALE = "SBNWEB_BATCH_ALLINEAMENTO_DA_LOCALE";

	//codice biblioteca per web-service sync con ESSE3
	public static final String ESSE3_COD_BIB = "ESSE3_COD_BIB";

	public static final String EMAIL_OTHER_PROPERTIES_FILE = "EMAIL_OTHER_PROPERTIES_FILE";

	public static final String ILL_SBN_DEFAULT_COD_FRUIZIONE = "ILL_SBN_DEFAULT_CODFRUI";
}
