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
* Module  : TYPES.H                                                         *
* Author  : Argentino Trombin                                               *
* Desc.   : Object Retrieval System type definitions                        *
* Date    :                                                                 *
****************************************************************************/
#ifndef __TYPES_H__
#define __TYPES_H__

/* OLD stuff
typedef int  BOOL;
typedef char BOOLC;
typedef unsigned char 	uchar;
typedef unsigned short 	ushort;
typedef unsigned int  	uint;
typedef unsigned long  	ulong;
#ifndef ULONG
 #define  ULONG unsigned long
 #define  UINT  unsigned int
#endif
*/

typedef short 				OrsBool;  // BOOLean (short)

typedef char  				OrsBoolc; // BOOLean (char)

typedef char				OrsChar;
typedef unsigned char  		OrsUChar;

typedef char				OrsByte;
typedef unsigned char  		OrsUByte;

typedef short   			OrsShort;
typedef unsigned short 		OrsUShort;

typedef short   			OrsWord;
typedef unsigned short 		OrsUWord;


//#ifdef PLATFORM_DEPENDENT_INT
	typedef int     		OrsInt;  // 16 or 32 bit platform dependent
	typedef unsigned int   	OrsUInt;
	typedef unsigned        OrsUnsigned;
//#else
//	typedef short   		OrsInt;  // 16 bit int. Platform independent
//	typedef unsigned short 	OrsUInt;
//	typedef unsigned short  OrsUnsigned;
//#endif PLATFORM_DEPENDENT_INT

typedef long    			OrsLong;
typedef unsigned long  		OrsULong;

typedef long    			OrsDoubleWord;
typedef unsigned long  		OrsUDoubleWord;


#endif //__TYPES_H__




