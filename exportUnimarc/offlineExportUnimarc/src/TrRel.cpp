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
 * TbRel.cpp
 *
 *  Created on: 28-ott-2009
 *      Author: Arge
 */


#include "TrRel.h"
#include "BinarySearch.h"
#include "../include/library/CTokenizer.h"

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

TrRel::TrRel(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
}


TrRel::~TrRel() {
	stop();
}

void TrRel::stop() {
	recordsVector.DeleteAndClear();
}

bool TrRel::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TrRel::loadRecord");
		return false;
	}
	return true;
} // End loadRecord




void TrRel::clearRecord()
{
	stringRecord.Clear();
	for (int i=0; i < recordsVector.Length(); i++)
		recordsVector.Entry(i)->clear();
}



/*
 * Un record del file delle relazioni e[ un record ottimizzato con tutte le relazioni associate all'ID di partenza
 */

bool TrRel::loadRecordFromOffset(long offset)
{
	CTokenizer Tokenizer("|");
	OrsChar *Token;
	CString *fieldPtr;
	Record *recordPtr;
	CString subRecord;
	ATTValVector<CString*> localFieldsVector;

	clearRecord();
	tbIn->SeekTo(offset);


	// Controlla che il buffere non sia stato ridimensionato da qualche parte!!!
	if (stringRecord.getBufferSize() != INITIAL_STRING_BUFFER_SIZE)
		stringRecord.ResizeNew(INITIAL_STRING_BUFFER_SIZE);

	if (!stringRecord.ReadLineWithPrefixedMaxSize(tbIn))
		return false;
	Tokenizer = stringRecord.Data();


	sourceId = Tokenizer.GetToken();

	while(Tokenizer.hasToken())
	{
		Token = Tokenizer.GetToken();
		if (!Token)
		{
			printf ("\nargepf NOOOOOOOO token, %s", Token);
			break;
		}
		recordPtr = new Record(tbFields);
		recordPtr->stringRecord = Token;
		subRecord = Token;

		subRecord.Split(localFieldsVector, ',');
		for (int j=0; j<localFieldsVector.length(); j++)
		{
			fieldPtr = recordPtr->getFieldString(j);
			fieldPtr->assign(localFieldsVector.Entry(j));
		}
		localFieldsVector.DeleteAndClear(); // Poremmo ottimizzare un giorno passando direttamente il vettore

		recordsVector.Add(recordPtr);


	} // End while

//	dumpRecord();
	return true;
} // End  loadRecordFromOffset

void TrRel::dumpRecord()
{
	Record *recordPtr;
	printf ("\n\nRECORD: '%s'", this->stringRecord.data());
	printf ("\nSOURCE Id: '%s'", this->sourceId.data());
	printf ("\nTARGETS: ");

	for (int i=0; i < recordsVector.length(); i++)
	{
		recordPtr = recordsVector.Entry(i);
		recordPtr->dumpRecord();
	}
}
