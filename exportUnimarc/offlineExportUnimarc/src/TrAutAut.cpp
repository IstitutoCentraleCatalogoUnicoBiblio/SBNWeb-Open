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
 * TrAutAut.cpp
 *
 *  Created on: 8-ott-2009
 *      Author: Arge
 */

#include <stdio.h>
#include <stdlib.h>

#include "TrAutAut.h"

#include "BinarySearch.h"
#include "library/CTokenizer.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

#include "MarcConstants.h"

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);



TrAutAut::TrAutAut(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 9;
	init();
}

TrAutAut::~TrAutAut() {

}





bool TrAutAut::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TrAutAut::loadRecord key=%s",key);
		return false;
	}
	return true;
} // End loadRecord





bool TrAutAut::loadNextRecordDaIndice(const char *key, bool inverted)
{
//	char * separator = recordSeparator.data();
//	CTokenizer Tokenizer(separator);
//	long tokenLength;
//	OrsChar *tokenPtr;
	CString *s;
	CString entryFile;
	bool retb;
	if (offsetBufferTbPtr)
	{
		entryFile.assign(entryPtrDaIndice, keyPlusOffsetPlusLfLength);
		entryPtrDaIndice += keyPlusOffsetPlusLfLength; // get ready for next one
		retb = true;
	}
	else
	{
		entryFile.ReadLine(tbOffsetIn);
		retb = true;
	}

	if (!retb)
		return false;

	// Leggiamo il record

//	long long llPos; // off_t
//	llPos = atoll (entryFile.data() +keyPlusOffsetPlusLfLength-12);
//	retb = tbIn->SeekToLarge(llPos);
//	if (!retb)
//		return retb;
	// 09/02/2015
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY)
	{ // OFFSET BINARI
		// Stiamo usando un offset long o long long?
		int offsetLen = keyPlusOffsetPlusLfLength - key_length;
		if (offsetLen == 4)
		{ // LONG
//			int iPos; // 18/03/2014 intel 32 bit int e long sono a 32bit
//			memcpy (&iPos, entryFile.data()+ key_length, 4);
			int iPos =  *((int*)(entryFile.data()+ key_length)); // 24/03/2015
			if (!tbIn->SeekTo(iPos))
				return false;
		}
		else if (offsetLen == 8)
		{ // LONG LONG
//			long long llPos;
//			memcpy (&llPos, entryFile.data()+ key_length, 8);
			long long llPos =  *((long long*)(entryFile.data()+ key_length)); // 24/03/2015
			if (!tbIn->SeekToLarge(llPos))
				return false;
		}
		else
		{
			SignalAnError(__FILE__, __LINE__, "OFFSET type not 4(long) or 8(long long) %d", offsetLen);
			return false;
		}
	}
	else
	{
		// Dalla posizione prendiamo l'offset
		long long llPos = atoll (entryFile.data()+keyPlusOffsetPlusLfLength-12); // OFFSET in ASCII
		if (!tbIn->SeekToLarge(llPos))
			return false;
	}




	clearRecord();
	if(!stringRecord->ReadLineWithPrefixedMaxSize(tbIn))
		return false;
	if (!inverted)
	{
		if (!stringRecord->StartsWith(key)) // != 0
			return false; // gone past bid
	}
	else
	{
		if (strncmp(stringRecord->data()+13, key, 10))
			return false; // gone past bid
	}



	CTokenizer Tokenizer(stringRecord->Data(), recordSeparator.data());
	long tokenLength;
	OrsChar *tokenPtr;
	int i=0;
	while(Tokenizer.GetToken(&tokenPtr, &tokenLength)  && i < tbFields)
	{
		if(tokenLength)
			fieldsVector->Entry(i)->assign(tokenPtr, tokenLength);
		i++;
	} // End while

	if (i)
		i--;
	fieldsVector->Entry(i)->Strip(CString::trailing, '\n');

	//	dumpRecord();
	return true;

} // End loadNextRecordDaIndice



