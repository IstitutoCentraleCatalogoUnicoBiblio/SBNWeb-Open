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
package it.iccu.sbn.web.actions.servizi.util;

public class FileConstants {

	public static final boolean NEW = true;
	public static final boolean OLD = false;
	public static final String VengoDaLista  = "Lista";
	public static final String Separatore = "-";
	public static final String VERIFICA_DATA1_GT_DATA2 = "1";
	public static final String VERIFICA_DATA1_EQ_DATA2 = "2";
	public static final String VERIFICA_DATA1_LT_DATA2 = "4";
	public static final int CONTROLLO_FASI_ITER_OK = 0;
	public static final int ERRORE_ACCESSO_BASE_DATI = -1;
	public static final int ERRORE_PARAMETRIZZAZIONE_SERVIZI = -2;
	public static final int PREVISTA_SOSPENSIONE_UTENTE_DA_CONFERMARE = -3;
	public static final int PREVISTI_MESSAGGI_UTENTE_NON_BLOCCANTI = -4;
	public static final int PREVISTI_MESSAGGI_UTENTE_CON_BLOCCO = -5;
	public static final int BIBLIOTECA_DESTINATARIA_NON_INDICATA = -6;
	public static final int MODALITA_EROGAZIONE_NON_TROVATA = -7;
}
