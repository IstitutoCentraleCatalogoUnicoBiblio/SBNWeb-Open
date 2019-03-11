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
 * TbLuogo.cpp
 *
 *  Created on: 30-mar-2009
 *      Author: Arge
 */


#include <stdio.h>
#include <stdlib.h>

#include "TbLuogo.h"
//#include "library/CTokenizer.h"
#include "BinarySearch.h"
#include "library/CString.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif
#include "MarcConstants.h"

//
extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);


TbLuogo::TbLuogo(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 14;
	init();
}

TbLuogo::~TbLuogo() {

}

bool TbLuogo::loadRecord(const char *key)
{
	long position;
	char * entryPtr;
	bool retb;
	// Dalla chiave trova l'offset nel file
	CString entryFile;
//printf ("\nTbLuogo::loadRecord\n-------------------");
	if (offsetBufferTbPtr)
//		retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryPtr);
		retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(this->tbOffsetIn, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}
//printf ("\nFINE TbLuogo::loadRecord\n-------------------");
	if (!retb)
	{
	    SignalAnError(__FILE__, __LINE__, "Record non trovato per chiave %s", key);
		return false;
	}
//	long offset;
	int offset; // 18/03/2014 32 bit

	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 10/02/2015
//		memcpy (&offset, entryPtr+ BID_KEY_LENGTH, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+BID_KEY_LENGTH)); // 24/03/2015
	else
		offset = atoi (entryPtr+BID_KEY_LENGTH); // OFFSET in ASCII


	return this->loadRecordFromOffset(offset); // atol (entryPtr+key_length)
} // End loadRecord
