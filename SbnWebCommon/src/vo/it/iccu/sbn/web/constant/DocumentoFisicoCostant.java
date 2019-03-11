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

public class DocumentoFisicoCostant {

//	tipi collocazione
	public final static String COD_A_SCAFFALE_APERTO_COLL 		= "S";
	public final static String COD_CHIAVE_TITOLO 				= "A";
	public final static String COD_CONTINUAZIONE 				= "C";
	public final static String COD_ESPLICITA_NON_STRUTTURATA 	= "M";
	public final static String COD_ESPLICITA_STRUTTURATA 		= "T";
	public final static String COD_MAGAZZINO_A_FORMATO 			= "F";
	public final static String COD_MAGAZZINO_NON_A_FORMATO 		= "N";
	public final static String COD_SISTEMA_DI_CLASSIFICAZIONE 	= "Z";

	public final static String A_SCAFFALE_APERTO_COLL 		= "a scaffale aperto";
	public final static String CHIAVE_TITOLO 				= "chiave titolo";
	public final static String CONTINUAZIONE 				= "continuazione";
	public final static String ESPLICITA_NON_STRUTTURATA 	= "esplicita non strutturata";
	public final static String ESPLICITA_STRUTTURATA 		= "esplicita strutturata";
	public final static String MAGAZZINO_A_FORMATO 			= "magazzino formato";
	public final static String MAGAZZINO_NON_A_FORMATO 		= "magazzino non a formato";
	public final static String SISTEMA_DI_CLASSIFICAZIONE 	= "sistema di classificazione";

//	tipi sezione
	public final static String COD_A_SCAFFALE_APERTO_SEZ	= "S";
	public final static String COD_MAGAZZINO				= "M";
	public final static String COD_MISCELLANEA				= "L";
	public final static String COD_TEMPORANEA				= "T";
	public final static char	COD_TEMP					= 'T';

	public final static String A_SCAFFALE_APERTO_SEZ	= "a scaffale aperto";
	public final static String MAGAZZINO				= "magazzino";
	public final static String MISCELLANEA				= "miscellanea";
	public final static String TEMPORANEA				= "temporanea";

//  tipi operazione per nuovo inventario
	public final static String N_INVENTARIO_NON_PRESENTE	= "N";
	public final static String S_INVENTARIO_PRESENTE		= "S";
	public final static String C_PROGRESSIVO_CORRENTE		= "C";
	public final static String P_PROGRESSIVO_PREGRESSO		= "P";
	public final static String O_ATTIVATO_DA_GEST_ORDINI	= "O";
	public final static String T_CAMBIO_TIT_REC_INV_CANC	= "T";

//  tipi operazione per modifica esemplare
	public final static String C_CANCELLA_ESEMPL		= "C";
	public final static String M_MODIFICA_ESEMPL		= "M";

//  Stringa creazione inventario manuale
	public final static String MSG_CANC	= "C";
	public final static String MSG_REC	= "R";
	public final static String MSG_SPOSTAMENTO	= "S";

// tipi operazione per lista inventari
	public final static String T_TITOLO		= "T";
	public final static String L_LOC		= "L";
	public final static String O_ORDINI		= "O";
	public final static String F_FATTURA	= "F";
	public final static String P_POS		= "P";
	public final static String N_NONCOLLOC	= "N";
	public final static String D_DETTAGLIO	= "D";
	public final static String R_RIDOTTO	= "R";

//	 tipi operazione per gestione inventario
	public final static String M_MODIFICA_INV		= "M_INV";
	public final static String C_CANCELLA_INV		= "C_INV";
	public final static String S_SCOLLOCAZIONE_INV	= "S";
	public final static String N_COLLOCAZIONE_INV	= "N";

//	 tipi operazione per gestione collocazione
	public final static String M_MODIFICA_COLL		= "M_COLL";
	public final static String I_INSERIMENTO_COLL	= "I_COLL";

//	 tipi liste collocazioni
	public final static String LS_COLL_0	= "0";//commentare con il significato
	public final static String LS_COLL_1	= "1";
	public final static String LS_COLL_2	= "2";
	public final static String LS_COLL_3	= "3";
	public final static String LS_COLL_4	= "4";
	public final static String LS_COLL_5	= "5";

//	 numero elementi per lista
	public final static int NUM_REC_LISTA	= 1000;//
	public final static int NUM_REC_ULTCOLLSPEC	= 10;//
//	 bookmarkDocFis
	public final static String BOOKMARK_GEST_ESEMPL	= "BGE";//
//	 spostamento collocazioni
	public final static String PRESTITO	= "P";//
	public final static String ETICHETTE	= "E";//

	public static final int MAX_PROGRESSIVO_INVENTARIO = 999999999;
	public static final int MIN_PROGRESSIVO_INV_PREGRESSO = 900000000;

}
