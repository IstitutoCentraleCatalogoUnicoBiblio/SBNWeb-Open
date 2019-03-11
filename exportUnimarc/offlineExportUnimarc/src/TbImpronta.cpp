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
 * TbImpronta.cpp
 *
 *  Created on: 21-mar-2009
 *      Author: Arge
 */


#include "TbImpronta.h"
//#include "library/CTokenizer.h"
#include "BinarySearch.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);



TbImpronta::TbImpronta(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 10;
	init();
}

TbImpronta::~TbImpronta() {

}


bool TbImpronta::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		// Troppi titoli senza impronta, messaggi inutili.
		// SignalAnError(__FILE__, __LINE__, "Derived class TbImpronta::loadRecord: Record non trovato per chiave %s", key);
		return false;
	}
	return true;

//	return Tb::loadRecord(key); // 15/03/2010 16.27

} // End loadRecord
