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
* Module  : Masks.h                                                         *
* Author  : Argentino Trombin                                               *
* Desc.   :                                                                 *
*                                                                           *
* Date    :                                                                 *
****************************************************************************/

#ifndef __MASKS_H__
#define __MASKS_H__

#include "ors/Const.h"
/*
 * This union is an array of masks used to pick off N chars of an OrsInt
 * int.  For example, suppose that sizeof(OrsInt) is 4 (a typical Unix
 * machine).  Then, if you wanted say, the first 3 bytes of the OrsInt
 * int, it could be retrieved using:
 *
 *	OrsInt i;
 *	CharMask mask;
 *	i & mask.in[3];
 *
 * This allows many byte-wise operations (such as hashing, or comparing byte
 * vectors) to be done using the machine's natural int size.  If the number
 * of chars doesn't divide evenly into an int then this mask can be used to
 * isolate the remaining bytes.
 */

union CharMask
	{
	OrsInt	in[sizeof(OrsInt)];
	char	ch[sizeof(OrsInt)*sizeof(OrsInt)];
	//CharMask();
	};

#endif //__MASKS_H__

