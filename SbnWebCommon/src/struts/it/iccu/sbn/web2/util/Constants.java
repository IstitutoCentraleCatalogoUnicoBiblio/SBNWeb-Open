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
package it.iccu.sbn.web2.util;


public class Constants {

	public static final String APP_NAME = "SBN Web";

	public static final String NEW_LINE = System.getProperty("line.separator");

	public static final String USER_FUNZ = "userFunzioni";
	public static final String USER_MENU = "userMenu";
	public static final String CURRENT_MENU = "currentMenu";
	public static final String NAVIGATION_PATH = "NAVIGATION_PATH";
	public static final String TREE_ELEMENT = "TID";
	public static final String FUNZIONI_BIBLIOTECA = "FUNZIONI_BIBLIOTECA";
	public static final String FUNZIONI_BIBLIOTECARIO = "FUNZIONI_BIBLIOTECARIO";
	public static final String SESSION_NOTIFIER = "SESSION_NOTIFIER";

	public static final String USER_ROOT = "root";
	public static final String ROOT_BIB = " __";
	public static final String SBNDIF_USER = "sbndif";
	public static final String UTENTE_BEAN = "UTENTE_BEAN";
	public static final String UTENTE_KEY = "UTENTE_KEY";
	public static final String UTENTE_WEB_KEY = "UTENTE_WEB_KEY";
	public static final String UTENTE_WEB_FAKE_BIB = " W$";
	public static final String UTENTE_WEB_TICKET = "webusr";
	public static final String PASSWORD = "password.utente";

	public static final String ID_UTE_BIB = "ID_UTE_BIB";
	public static final String DOCUMENTO = "DOCUMENTO";
	public static final String DATI_DOC_KEY = "DATI_DOC_KEY";
	public static final String UTENTE_CON = "UTENTE_CON";
	public static final String BIBLIO_SEL = "BIBLIO_SEL";
	public static final String BIBLIO = "BIBLIO";
	public static final String COD_BIBLIO = "COD_BIBLIO";
	public static final String POLO = "POLO";
	public static final String PATH_LOGON = "/logon.do";

	public static final String SALA = "SALA";
	public static final String CAT_MEDIAZIONE = "MED";
	public static final String NATURA = "NAT";
	public static final String AUTORE = "AUT";
	public static final String TITOLO = "TIT";
	public static final String ANNO = "ANNO";
	public static final String BIBLIOTECA = "BIB";
	public static final String AUTOREGISTRAZIONE = "AUTOREG";
	public static final String INVENTARIO = "INV";
	public static final String TIPO_CATALOGO = "type";

	// Attrbutes name LDAP
	public static final String LDAP_DYSPLAY_NAME = "displayName";
	public static final String LDAP_GIVEN_NAME = "givenName";
	public static final String LDAP_MAIL = "mail";
	public static final String LDAP_UID = "uid";
	public static final String LDAP_USER_PASSWORD = "userPassword";
	public static final String LDAP_OBJECT_CLASS = "objectClass";
	public static final String LDAP_CN = "cn";
	public static final String LDAP_SN = "sn";

	// Stringa relativa all'attributo che identifica il locale di default
	public static final String SBN_LOCALE = "SBN_LOCALE";

	//Numero di record trattati dai processi batch prima del commit intermedio
	public static final int BATCH_COMMIT_THRESHOLD = 1;

	//Versione di Postgres
	public static final String POSTGRES_VERSION_83 = "8.3";

	public static final String BUILD_TIME = "BUILD_TIME";
	public static final String BUILD_NUMBER = "BUILD_NUMBER";

	public static final String SBNMARC_BUILD_TIME = "SBNMARC_BUILD_TIME";

	public static final String POLO_NAME = "POLO_NAME";
	public static final String POLO_CODICE = "POLO_CODICE";

	public static final String REGEX_SEPARATORE_TERMINI_SOGGETTO = "\\s-\\s";
	public static final String SEPARATORE_TERMINI_SOGGETTO = " - ";

	public static final String RICHIESTA_OPAC = "RichiestaOpacVO";
	public static final String INFO_DOCUMENTO = "INFO_DOCUMENTO";

	public static final String SERVIZI_ATTIVI_DOCUMENTO = "SERVIZI_ATTIVI_DOCUMENTO";
	public static final String UTENTE_INIZIO_PROROGA = "UTENTE_INIZIO_PROROGA";
	public static final String UTENTE_FINE_PROROGA = "UTENTE_FINE_PROROGA";

	public static final String CODICE_ATTIVITA = "CDATTIVITA";

	public static final String OPAC_URL = "opac_url";

	public static final String LOCALE_LANG = "LOCALE_LANG";
	public static final String LOCALE_COUNTRY = "LOCALE_COUNTRY";

	public static final String SBNWEB_WS_SECURITY_DOMAIN = "ws-client";

	//almaviva5_20121120 evolutive google
	public static final String PROGRESSIVO_CART_NAME_GOOGLE = "GCN";
	public static final char CSV_DEFAULT_SEPARATOR = ',';

	public static final String EXPORT_UNIMARC_DEFAULT_TAGS = "001,100,200";

	//almaviva5_20130412 evolutive google
	public static final String PROGRESSIVO_MODULO_PRELIEVO = "MPR";

	public static final int URI_MAX_LENGTH = 1280;

	public static final int ISIL_MAX_LENGTH = 16;

	public static final int Z3950_DEFAULT_PORT = 210;

	public static final int MAX_BIBLIOTECHE_RICHIESTA_ILL = 4;
	
}
