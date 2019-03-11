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
 * TbTitSet2.cpp
 *
 *  Created on: 31/03/2016
 *      Author: Arge
 */


#include "TbTitSet2.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"
#include "BinarySearch.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);



TbTitSet2::TbTitSet2(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 9;
	init();
}


TbTitSet2::~TbTitSet2() {

}

bool TbTitSet2::loadRecord(const char *key)
{
	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TbTitSet2::loadRecord");
		return false;
	}
	return true;
} // End loadRecord




