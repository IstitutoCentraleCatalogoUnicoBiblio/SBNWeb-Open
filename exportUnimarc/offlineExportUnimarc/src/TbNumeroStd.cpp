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
 * TbNumeroStandard.cpp
 *
 *  Created on: 25-dic-2008
 *      Author: Arge
 */


#include "TbNumeroStd.h"
#include "BinarySearch.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

TbNumeroStd::TbNumeroStd(CFile *tbNumeroStandardIn, CFile *tbNumeroStandardOffsetIn, char *offsetBufferTbNumeroStandardPtr, long elementsTbNumeroStandard, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbNumeroStandardIn, tbNumeroStandardOffsetIn, offsetBufferTbNumeroStandardPtr, elementsTbNumeroStandard, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 11;
	init();
}

TbNumeroStd::~TbNumeroStd() {
}

bool TbNumeroStd::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TbNumeroStd::loadRecord");
		return false;
	}
	return true;
} // End loadRecord



