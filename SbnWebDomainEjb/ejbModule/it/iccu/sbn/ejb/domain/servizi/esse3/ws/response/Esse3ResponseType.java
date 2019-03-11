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
package it.iccu.sbn.ejb.domain.servizi.esse3.ws.response;
//LFV 12/07/2018
/*  Codici di response ESSE3 documentazione Login_su_ESSW3.pdf
 *
 * Da documentazione APIs:
 * 	1   OK
 * -1   Errore nel recupero dei dati
 * 1003 Autenticazione fallita
 * 1004 Fallita la creazione del componente
 * 1007 Fallita la connessione, al DB
 * 1110 Utente disabilitato
 * 1112 La password deve essere impostata (cambio password al primo login)
 * 1116 User_id non valido o nullo
 * 1119 Il gruppo a cui appartiene l'utente non è abilitato ad utilizzare questo tipo di client
 * 1126 Errore generico di LDAP
 * 1130 Password scaduta
 *
 * Custom: -> per convenzione sono partito da 15xx
 * 1500 errore generico
 * 1501 Campi vuoti
 */

public enum Esse3ResponseType {
	OK 								(1),
	ERRORE_RECUPERO_DATI 			(-1),
	AUTENTICAZIONE_FALLITA 			(1003),
	CREAZIONE_COMPONENTE_FALLITA	(1004),
	CONNESSIONE_DB_FALLITA			(1007),
	UTENTE_DISABILITATO 			(1110),
	PASSWORD_DA_REIMPOSTARE 		(1112),
	USER_ID_NON_VALIDO				(1116),
	GRUPPO_UTENTE_NON_ABILITATO 	(1119),
	ERRORE_LDAP 					(1126),
	PASSWORD_SCADUTA				(1130),

	ERRORE_GENERICO 				(1500),
	CAMPI_VUOTI 					(1501);


	private int code;

	private Esse3ResponseType(int code) {
		this.code = code;
	}
	public int getResponseCode() {
		return code;
	}
	public static Esse3ResponseType of(int code) {
		for (Esse3ResponseType type: values()) {
			if(type.getResponseCode() == code)
				return type;
		}
		return null;
	}
}

