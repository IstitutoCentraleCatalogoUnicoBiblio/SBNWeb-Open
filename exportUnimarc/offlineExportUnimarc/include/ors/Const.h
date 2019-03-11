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
* Module  : CONST.H                                                         *
* Author  : Argentino Trombin                                               *
* Desc.   : Object Retrieval System constants definitions                   *
* Date    :                                                                 *
****************************************************************************/
#ifndef __CONST_H__
#define __CONST_H__

#include "ors/types.h"

#ifndef __WIN32__
#include <strings.h>
#define stricmp strcasecmp
#define strnicmp strncasecmp
#endif

#define OrsERROR      -1
#define OrsFATALERROR -2

#define OrsTRUE      1
#define OrsON		 1
#define OrsIN        1

#define OrsFALSE     0
#define OrsOFF       0
#define OrsOUT		 0
#define OrsNULL      0

#define OrsBLOCK_SIZE 	4096

#define OrsMAX_KEYS 	256
#define OrsMAX_ELEMENTS	OrsMAX_KEYS

//#define OrsMAX_INI_PARAM		25 // 20
//#define OrsMAX_INI_PARAM		28
#define OrsMAX_INI_PARAM		30

//#define OrsMAX_EXPRESS_LEN		80
//define OrsMAX_EXPRESS_LEN		200
#define OrsMAX_EXPRESS_LEN		500 // 27/02/13 per gestione tagsToExport su un'unica riga


#define OrsCREATE 1
#define OrsREAD   2
#define OrsWRITE  4
#define OrsAPPEND 8



/*
#define OrsDISPLAY_MSG_NOT 	      0
#define OrsDISPLAY_MSG_IN_CONSOLE 1
#define OrsDISPLAY_MSG_IN_WINDOW  2
*/

/*
 Logging levels.
 Logging will be generated according to the level requested
 by te runtime argument supplier. Thi can go from no logging
 to detailed logging
*/
#define OrsLOG_NOT			0
#define	OrsLOG_FATAL_ERROR	1
#define	OrsLOG_ERROR		2
#define OrsLOG_WARNING		3
#define OrsLOG_MESSAGE		4
#define OrsLOG_INFO_LEV1	5
#define OrsLOG_INFO_LEV2	6
#define OrsLOG_INFO_LEV3	7
#define OrsLOG_INFO_LEV4	8
#define OrsLOG_INFO_LEV5	9

/**
*	Error Levels
**/

#define OrsFATAL_ERROR  -1

#define OrsWARNING_LEV1 -11
#define OrsWARNING_LEV2 -12
#define OrsWARNING_LEV3 -13
#define OrsWARNING_LEV4 -14
#define OrsWARNING_LEV5 -15
#define OrsWARNING_LEV6 -16
#define OrsWARNING_LEV7 -17
#define OrsWARNING_LEV8 -18

#define OrsIO_ERROR         -21 // Level 1
#define OrsDTD_ERROR        -22 // Level 2
#define OrsINSTANCE_ERROR   -23 // Level 3
#define OrsOBJECT_ERROR  	-24 // Level 4
#define OrsMEM_ERROR        -25 // Level 5
#define OrsSTEER_ERROR      -26 // Level 6
#define OrsQUERY_ERROR      -27 // Level 7
#define OrsBITMAP_ERROR     -28 // Level 8
#define OrsVK_ERROR   	  	-29 // Level 9



// Definition for the data types
#define OrsDATA_STYLE_1    '1'
#define OrsDATA_STYLE_2    '2'
#define OrsDATA_STYLE_3    '3'
#define OrsDATA_STYLE_4    '4'
#define OrsDATA_STYLE_5    '5'
#define OrsDATA_STYLE_6    '6'
#define OrsDATA_STYLE_7    '7'


// Used for parsing
#define OrsMDO_FOUND       0
#define OrsMDO_NOTFOUND    1
#define OrsMDO_SYNTAXERROR 2

#define OrsMDC_FOUND       0
#define OrsMDC_NOTFOUND    1

// Error management
//void OrsSetErr(char *Module, OrsInt Function, OrsInt Err, OrsInt ErrLev, OrsInt Line);
//#define OrsCallErr(FuncCode, ErrorCode, ErrorLevel)      OrsSetErr(__FILE__, FuncCode, ErrorCode,  ErrorLevel, __LINE__)

#ifndef MIN
#define MIN(x,y) ((x) < (y) ? (x) : (y))
#endif
#ifndef MAX
#define MAX(x,y) ((x) > (y) ? (x) : (y))
#endif
#ifndef ABS
#define ABS(x) ((x) < 0 ? -(x) : (x))
#endif



#endif // __CONST_H__






