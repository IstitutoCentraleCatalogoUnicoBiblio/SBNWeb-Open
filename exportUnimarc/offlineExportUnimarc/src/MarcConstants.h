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
/****************************************************************************
* Module  : MarcCostants.h                                                  *
* Author  : Argentino Trombin                                               *
* Desc.   :                                                                 *
* Date    :                                                                 *
****************************************************************************/
#ifndef __MARC_CONSTANTS_H__
#define __MARC_CONSTANTS_H__


#define RECORD_TERMINATOR 		0x1D		// RT
#define FIELD_TERMINATOR 		0x1E		// FT
#define SUBFIELD_DELIMITER 		0x1F		// US
#define BLANK 					0x20		// BLANK
#define NS_URI 					"http://www.loc.gov/MARC21/slim" // MARCXML_NS_URI
#define MARC_8_ANSEL_ENCODING 	"MARC8" 		// MARC_8_ENCODING
#define ISO5426 ENCODING 		"ISO5426" 	// ISO5426_ENCODING
#define ISO6937 ENCODING 		"ISO6937" 	// ISO6937_ENCODING

#define INDICATOR_UNDEFINED    ' '
#define INDICATOR_FILLER    '|'
#define FILLER    '|'
#define BID_LENGTH	10


// Valido per NON UTF
//#define NSB "\033H"// "{esc}H" // Not Sort Beginning
//#define NSE	"\033I" // "{esc}I" // No Sort Ending

// UNICODE
// 0xC298 NSB
// 0xC29C NSE
//#define NSB "\302\230"// "{esc}H" // Not Sort Beginning
//#define NSE	"\302\234" // "{esc}I" // No Sort Ending




#define ENCODING_ISO8859_1 '#'
#define ENCODING_UTF8 'a'

// TIPO MATERIALE (tb_codici.tp_tabella = TPMA )
#define TP_MATERIALE_CARTOGRAFICO_C 	'C'
#define TP_MATERIALE_GRAFICO_G			'G'
#define TP_MATERIALE_ANTICO_E			'E'
#define TP_MATERIALE_MODERNO_M			'M'
#define TP_MATERIALE_MUSICA_U			'U'
#define TP_MATERIALE_AUDIOVISIVO_H		'H'	// 16/02/2015

// TIPO RECORD (tb_codici.tp_tabella = GEUN )
#define TP_RECORD_GRAFICA_k								'k'
#define TP_RECORD_MATERIALE_CARTOGRAFICO_e 				'e'
#define TP_RECORD_MATERIALE_CARTOGRAFICO_MANOSCRITTO_f	'f'
#define TP_RECORD_MATERIALE_MULTIMEDIALE_m				'm'
#define TP_RECORD_MUSICA_NOTATA_c						'c'
#define TP_RECORD_MUSICA_NOTATA_MANOSCRITTA_d			'd'
#define TP_RECORD_OGGETTO_r								'r'
#define TP_RECORD_REGISTRAZIONE_SONORA_MUSICALE_j		'j'
#define TP_RECORD_REGISTRAZIONE_SONORA_NON_MUSICALE_i	'i'
#define TP_RECORD_RISORSA_ELETTRONICA_l					'l'
#define TP_RECORD_TESTO_a								'a'
#define TP_RECORD_TESTO_MANOSCRITTO_b					'b'
#define TP_RECORD_VIDEO_g								'g'



// Codici natura
#define NATURA_A_TITOLO_DI_RAGGRUPPAMENTO_CONTROLLATO       'A' // Titolo uniforme
#define NATURA_B_TITOLO_DI_RAGGRUPPAMENTO_NON_CONTROLLATO   'B'
#define NATURA_C_COLLANA                                    'C'
#define NATURA_D_TITOLO_SVILUPPATO_O_ESTRAPOLATO            'D'
#define NATURA_M_MONOGRAFIA                                 'M'
#define NATURA_N_TITOLO_ANALITICO                           'N' // Spoglio?
#define NATURA_P_TITOLO_PARALLELO                           'P'
#define NATURA_S_PERIODICO                                  'S'
#define NATURA_T_TITOLO_SUBORDINATO                         'T'
#define NATURA_W_VOLUME_PRIVO_DI_TITOLO_SIGNIFICATIVO       'W'


#define AUTHORITY_DOCUMENTO	1
#define AUTHORITY_AUTORI	2
#define AUTHORITY_SOGGETTI	3
#define AUTHORITY_CLASSI	4
#define AUTHORITY_TITOLI_UNIFORMI	5

// CODICI COLLEGAMENTI (LEGAMI) Pag. 112 del Verdone
#define CD_LEGAME_01_FA_PARTE_DI										"01"
#define CD_LEGAME_02_SUPPLEMENTO_DI										"02"
#define CD_LEGAME_03_CONTIENE_ANCHE										"03"
#define CD_LEGAME_04_CONTINUAZIONE_DI									"04"
#define CD_LEGAME_4_CONTINUAZIONE_DI									'4'
#define CD_LEGAME_05_EDIZIONE_SUCCESSIVA_DI								"05"
#define CD_LEGAME_06_HA_PER_TITOLO_DI_RAGGRUPPAMENTO_NON_CONTROLLATO	"06" // Titolo uniforme
#define CD_LEGAME_07_ALTRA_EDIZIONE_DI									"07"
#define CD_LEGAME_08_HA_PER_ALTRO_TITOLO								"08"
#define CD_LEGAME_09_HA_PER_TITOLO_DI_RAGGRUPPAMENTO_CONTROLLATO		"09" // Titolo uniforme
#define CD_LEGAME_41_ASSORBE											"41"
#define CD_LEGAME_42_SI_FONDE_CON										"42"
#define CD_LEGAME_43_CONTINUAZIONE_PARZIALE_DI							"43"
#define CD_LEGAME_51_COMPRENDE											"51" // M comprende W si legge W 01 M


// Codice del numero standard o identificativo
// Da tb_codici.tp_tabella NSTD
#define TP_NUMERO_STANDARD_A_NUMERO_EDIZIONE_REGISTRAZIONI_SONORE	'A' // 02/09/2014

#define TP_NUMERO_STANDARD_B_BNI 							'B'
#define TP_NUMERO_STANDARD_C_CATALOGHI_COLLETTIVI_STRANIERI 'C'		// Non usato
#define TP_NUMERO_STANDARD_D_BIBLIOGRAFIE_STRANIERE			'D'		// Non usato
#define TP_NUMERO_STANDARD_E_NUMERO_EDITORIALE 				'E'

#define TP_NUMERO_STANDARD_F_NUMERO_MATRICE_REGISTRAZIONI_SONORE 	'F' // 02/09/2014

#define TP_NUMERO_STANDARD_G_N_PUBBLICAZIONE_GOVERNATIVA	'G'

#define TP_NUMERO_STANDARD_H_NUMERO_VIDEOREGISTRAZIONE				'H' // 02/09/2014

#define TP_NUMERO_STANDARD_I_ISBN 							'I'
#define TP_NUMERO_STANDARD_J_ISSN 							'J'

#ifdef EVOLUTIVA_2014
#else
#define TP_NUMERO_STANDARD_K_ISBN_978 						'K'
#endif

#define TP_NUMERO_STANDARD_L_NUMERO_DI_LASTRA 				'L'
#define TP_NUMERO_STANDARD_M_ISMN 							'M'

#ifdef EVOLUTIVA_2014
#else
#define TP_NUMERO_STANDARD_N_ISBN_979 						'N'
#endif

#define TP_NUMERO_STANDARD_O_NUMERO_DI_RISORSA_ELETTRONICA			'O' // 02/09/2014

#define TP_NUMERO_STANDARD_P_ACNP 							'P'

#define TP_NUMERO_STANDARD_Q_UPC									'Q' // 02/09/2014

#define TP_NUMERO_STANDARD_R_CRP 							'R'
#define TP_NUMERO_STANDARD_S_BOMS 							'S'

#define TP_NUMERO_STANDARD_T_EAN									'T' // 02/09/2014

#define TP_NUMERO_STANDARD_U_CUBI 							'U'

#define TP_NUMERO_STANDARD_V_ISRC									'V' // 02/09/2014

#define TP_NUMERO_STANDARD_X_RISM 							'X'
#define TP_NUMERO_STANDARD_Y_SARTORI 						'Y'
#define TP_NUMERO_STANDARD_W_SIAE									'W' // 02/09/2014
#define TP_NUMERO_STANDARD_Z_ISSN_L 						'Z'


#define LIVELLO_GERARCHICO_0_SENZA_LIVELLI_E_PERIODICI	'0'
#define LIVELLO_GERARCHICO_1_SUPERIORE					'1'
#define LIVELLO_GERARCHICO_2_INTERMEDIA_O_INFERIORE		'2'

// CODICI GENERE GEPU


// CODICI di RESPONSABILITA'
#define TP_RESP_0_NOME_CITATO_NEL_DOCUMENTO					'0'
#define TP_RESP_1_RESPONSABILITA_PRINCIPALE					'1'
#define TP_RESP_2_RESPONSABILITA_ALTERNATIVA				'2'
#define TP_RESP_3_RESPONSABILITA_SECONDARIA					'3'
#define TP_RESP_4_RESPONSABILITA_NELLA_PRODUZIONE_MATERIALE	'4'


// LEGAMI TITOLO AUTORE (indice LETA)


// Punteggiatura ISBD
#define PUNTEGGIATURA_AREA_TITOLO_INIZIO_TITOLO_PARALLELO " = "
#define PUNTEGGIATURA_AREA_TITOLO_INIZIO_COMPLEMENTO_TITOLO " : "
#define PUNTEGGIATURA_AREA_TITOLO_INIZIO_PRIMA_FORMULAZIONE_RESPONSABILITA " / "
#define PUNTEGGIATURA_AREA_TITOLO_INIZIO_SUCCESSIVA_FORMULAZIONE_RESPONSABILITA " ; "
#define PUNTEGGIATURA_AREA_TITOLO_INIZIO_DESIGANZIONE_MATERIALE " ["
#define PUNTEGGIATURA_AREA_TITOLO_FINE_DESIGANZIONE_MATERIALE "]"
#define PUNTEGGIATURA_AREA_TITOLO_INIZIO_TITOLO_AUTORE_DIVERSO " . "
#define PUNTEGGIATURA_AREA_TITOLO_INIZIO_ALTRO_TITOLO_STESSO_AUTORE " ; "

#define PUNTEGGIATURA_AREA_TITOLO_INIZIO_QUALIFICAZIONE "<"
#define PUNTEGGIATURA_AREA_TITOLO_FINE_QIALIFICAZIONE ">"

#define STATO_INIZIO_TITOLO								1
#define STATO_IN_DESIGANZIONE_MATERIALE					2
#define STATO_IN_TITOLO_PARALLELO 						3
#define STATO_IN_COMPLEMENTO_TITOLO 					4
#define STATO_IN_PRIMA_FORMULAZIONE_RESPONSABILITA		5
#define STATO_IN_SUCCESSIVA_FORMULAZIONE_RESPONSABILITA	6
#define STATO_IN_QUALIFICAZIONE							7


// Tipo di digitalizzazione della copia (tb_codici.tp_abella CDIG)
#define TIPO_DIGITALIZZAZIONE_PARZIALE 		0
#define TIPO_DIGITALIZZAZIONE_COMPLETA 		1
#define TIPO_DIGITALIZZAZIONE_BORN_DIGITAL	2
#define TIPO_DIGITALIZZAZIONE_SCONOSCIUTA 	?



// POLI
#define POLO_SCONOSCIUTO	0
#define POLO_AQ1        1
#define POLO_BA1        2
#define POLO_BIA        3
#define POLO_BVE        4
#define POLO_CAM        5
#define POLO_CFI        6
#define POLO_CSA        7
#define POLO_IEI        8
#define POLO_INDICE     9
#define POLO_LIG        10
#define POLO_MIL        11
#define POLO_MO1        12
#define POLO_PA1        13
#define POLO_PAL        14
#define POLO_RML        15
#define POLO_RMR        16
#define POLO_SBR        17
#define POLO_SBW        18
#define POLO_SNT        19
#define POLO_TER        20
#define POLO_TO3        21 // 09/03/2011
#define POLO_UM1        22
#define POLO_UM2        23
#define POLO_CSW        24 // 27/01/2015
#define POLO_PIS        25 // 16/04/2015
#define POLO_TO0        26 // 27/04/2015
#define POLO_TEL        27 // 16/06/2015
#define POLO_DDS        28 // 12/01/2016
#define POLO_UPG        29 // 14/11/2016
#define POLO_BAT        30 // 23/02/2017
#define POLO_NAP        31 // 29/05/2019


// DATABASE DA DOVE SONO STATI ESTRATTI I DATI
#define DATABASE_SBNWEB	1
#define DATABASE_INDICE	2

#define TIPO_SCARICO_UNIMARC 1
#define TIPO_SCARICO_OPAC 2

#define TIPO_UNIMARC_STANDARD 1
#define TIPO_UNIMARC_RIDOTTO 2

#define BID_KEY_LENGTH	10
#define VID_KEY_LENGTH	10
#define PID_KEY_LENGTH	10
#define MID_KEY_LENGTH	10

#define LONG_SIZE	4
#define LONG_LONG_SIZE	8

#define OFFSET_TYPE_ASCII	1
#define OFFSET_TYPE_BINARY	2

// 20/09/2017
#define SISTEMA_NUMERICO_DECIMALE	1
#define SISTEMA_NUMERICO_ESADECIMALE	2


#endif // __MARC_CONSTANTS_H__

