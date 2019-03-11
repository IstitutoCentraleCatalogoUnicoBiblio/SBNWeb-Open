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
 * TbCartografia.cpp
 *
 *  Created on: 4-mag-2009
 *      Author: Arge
 */


#include "TbCartografia.h"
//#include "library/CTokenizer.h"
#include "BinarySearch.h"
#include "library/CString.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);


TbCartografia::TbCartografia(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
//	tbFields = 27;
	tbFields = 28; // 10/09/2018 Jira 43/17
	init();
}

TbCartografia::~TbCartografia() {

}

bool TbCartografia::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TbCartografia::loadRecord, key=%s", key);
		return false;
	}
	return true;

} // End loadRecord
