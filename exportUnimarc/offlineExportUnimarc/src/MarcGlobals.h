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
/*
 * MarcGlobals.h
 *
 *  Created on: 22-gen-2010
 *      Author: Arge
 *
 *
 *      Variabili globali contenenti parametrizzazioni
 *      NMB: Queste variabili vanno inizializzate una volta sola!!
 */


#ifndef MARCGLOBALS_H_
#define MARCGLOBALS_H_

#include "../include/library/CKeyValueVector.h"

// Valido per NON UTF
//#define NSB "\033H"// "{esc}H" // Not Sort Beginning
//#define NSE	"\033I" // "{esc}I" // No Sort Ending

// UNICODE
// 0xC298 NSB
// 0xC29C NSE
//#define NSB "\302\230"// "{esc}H" // Not Sort Beginning
//#define NSE	"\302\234" // "{esc}I" // No Sort Ending

extern CString NSB, NSE;
//		AUTHORITY,
//        IDXUNIMARC,
//        RETICOLO_OUT,
//        POLO,
//        DESCPOLO,
//        BIBLIOTECARICHIEDENTESCARICO,
//        MARKFILEOUT,
//        MARKFILEOUTTXT,
//        PROCESSLOG;
extern int	ISBN_CON_TRATTINI;
extern int 	RECORDUNIMARCSUSINGOLARIGA;
//extern long INIZIAELABORAZIONEDARIGA;
//extern bool POSITIONBYOFFSET;
//extern long ELABORANRIGHE=0;
//extern int 	LOGOGNIXRIGHE=0;
extern int 	DATABASE_ID;
extern int 	TIPO_SCARICO;
extern int 	TIPO_UNIMARC;
extern long	MARKFILEOUT_BUFFER_SIZE;
extern long	MARKFILEOUT_BUFFER_RESIZE_BY;

extern char const *paddingZeroes [];
extern CString	BIBLIOTECARICHIEDENTESCARICO;
extern CString	FixedLengthLine;
extern CString	POLO;

//extern CString titoliErrati; // titoliCancellati, titoliFusi, titoliLocDaCanc, titoliUnimarc, titoliVariati, titoliUnimarcRidotto
//extern CFile *titoliErratiOut; //*titoliCancellatiOut, *titoliFusiOut, *titoliLocDaCancOut, *titoliUnimarcOut, *titoliUnimarcRidottoOut

// PER PROFILING
//extern long tbAppendStringCtr;

extern char const *descTipoPeriodicita [];


extern char *tagsToGenerateBufferPtr;
#define IS_TAG_TO_GENERATE(tag) *(tagsToGenerateBufferPtr + tag)

extern bool	EXPORT_VIAF;

//extern CKeyValueVector *codiciNotaKV;

extern CKeyValueVector *codiciNotaKV; //03/05/2013 Mantis polo 5302
extern CKeyValueVector *codiciEclaKV; // 10/12/14
extern CKeyValueVector *codiciOrgaKV; // 21/01/15

extern CKeyValueVector *variantiSinonimiaKsogVarKV; // 15/01/2018
extern CKeyValueVector *variantiSinonimiaKvarSogKV; // 22/02/2018

extern CKeyValueVector *variantiStoricheKsogVarKV; // 23/03/2018
extern CKeyValueVector *variantiStoricheKvarSogKV; // 23/03/2018


extern CKeyValueVector *scomposizioneSoggettoKV; // 24/01/2018
extern CKeyValueVector *composto_non_preferito_ctr_KV; // 29/01/2018
extern CKeyValueVector *compostoNonPreferitoKV; // 28/02/2018

extern CKeyValueVector *bibliotecheDaMostrareIn899KV; // 30/01/2018

extern int OFFSET_TYPE;

extern int SISTEMA_NUMERICO_UNIMARC;
extern long	MAX_RECORD_SIZE;
extern long	MAX_FIELD_SIZE;

extern CString estrai_nature;

extern bool	z899;
extern bool	stampaRecordCancellato;
//extern bool	xy899;
extern bool	check899;
extern bool	_699_sintetica;
extern CString *incrementale_dal;

extern bool esportaSoloInventariCollocati;

#endif /* MARCGLOBALS_H_ */
