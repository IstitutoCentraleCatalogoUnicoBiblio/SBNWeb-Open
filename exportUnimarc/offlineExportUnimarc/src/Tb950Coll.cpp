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
 * TrTitoloCollocazione.cpp
 *
 *  Created on: 19-gen-2009
 *      Author: Arge
 */
#include <stdio.h>
#include <stdlib.h>


#include "Tb950Coll.h"
#include "library/CTokenizer.h"
#include "BinarySearch.h"
#include "library/CString.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

//Tb950Coll::Tb950Coll(CFile * trTitCollIn, CFile *trTitCollOffsetBidIn, CFile *trTitCollOffsetKlocIn, char *offsetBufferTrTitCollPtr, long elementsTrTitColl, int keyPlusOffsetPlusLfLength, int key_length) {
Tb950Coll::Tb950Coll(CFile * tbTitCollIn,
		CFile *tbTitCollOffsetBidIn, CFile *tbTitCollOffsetKlocIn,
		char *offsetBufferTb950CollBidPtr, char *offsetBufferTb950CollKeylocPtr,
		long elementsTb950BidColl, long elementsTb950KeylocColl,
		int bidKeyLength, int keylocKeyLength,
		int OffsetPlusLfLength
		)
{

	this->tb950CollIn = tbTitCollIn;
	this->tbOffsetIn = tbTitCollOffsetBidIn;
	this->tb950CollOffsetKlocIn = tbTitCollOffsetKlocIn;

	this->offsetBufferTbPtr = offsetBufferTb950CollBidPtr;
	this->offsetBufferTb950CollKeylocPtr = offsetBufferTb950CollKeylocPtr;

	this->elementsTb = elementsTb950BidColl;
	this->elementsTb950KeylocColl = elementsTb950KeylocColl;

	this->key_length = bidKeyLength;
	this->keylocKeyLength = keylocKeyLength;

	this->OffsetPlusLfLength = OffsetPlusLfLength;

	init();
}


void Tb950Coll::init()
{
CString *sPtr;
fieldsVector = new ATTValVector<CString*>();

	for (int i=0; i < TB950COLL_FIELDS; i++)
	{
		sPtr = new CString();
		fieldsVector->Add(sPtr);
	}

	tbFields = fieldsVector->length();

//printf("\nTb950Coll::init() %s", fieldsVector->Entry(0)->data());
}

void Tb950Coll::initFrom(Tb950Coll *tb950Coll)
{
	tb950CollIn=0;
	tbOffsetIn=0;
	offsetBufferTbPtr=0;
	tb950CollOffsetKlocIn=0;
	offsetBufferTb950CollKeylocPtr=0;
	elementsTb=0;
	elementsTb950KeylocColl=0;
	OffsetPlusLfLength=0;
	key_length=0;
	keylocKeyLength=0;


	CString *sPtr;
	fieldsVector = new ATTValVector<CString*>();

	for (int i=0; i < TB950COLL_FIELDS; i++)
	{
		sPtr = new CString(tb950Coll->getField(i));
		fieldsVector->Add(sPtr);
	}
	tbFields = fieldsVector->length();

//	printf("\nTb950Coll::initFrom %s", fieldsVector->Entry(0)->data());

}



Tb950Coll::Tb950Coll(Tb950Coll *tb950Coll)
{
	initFrom(tb950Coll);
}



Tb950Coll::~Tb950Coll() {
stop();
}

void Tb950Coll::stop() {
//	printf("\nTb950Coll::stop() %s", fieldsVector->Entry(0)->data());

	fieldsVector->DeleteAndClear();
	delete fieldsVector;
}


void Tb950Coll::clearRecord()
{

for (int i=0; i < TB950COLL_FIELDS; i++)
	fieldsVector->Entry(i)->Clear();
}

void Tb950Coll::dumpRecord()
{
printf ("\n\nRECORD TrTitoloCollocazione:");
for (int i=0; i < TB950COLL_FIELDS; i++)
{
	printf ("\n%02d:'%s'", i, fieldsVector->Entry(i)->data());
}
}

char * Tb950Coll::getField(int aFieldId)
{
if (aFieldId < fieldsVector->Length())
	return fieldsVector->Entry(aFieldId)->data();
else
{
SignalAnError(__FILE__, __LINE__, "TrTitoloCollocazione::getField: n out of range: %d", aFieldId);
return (char *)"";
}
}

int Tb950Coll::getFieldLength(int aFieldId)
{
if (aFieldId < fieldsVector->Length())
	return fieldsVector->Entry(aFieldId)->Length();
else
{
SignalAnError(__FILE__, __LINE__, "TrTitoloCollocazione::getFieldLength: n out of range: %d", aFieldId);
return 0;
}
}

CString * Tb950Coll::getFieldString(int aFieldId)
{
	if (aFieldId < fieldsVector->Length())
		return fieldsVector->Entry(aFieldId);
	else
	{
	SignalAnError(__FILE__, __LINE__, "TrTitoloCollocazione::getField: n out of range: %d", aFieldId);
	return 0;
	}
}

void Tb950Coll::loadRecord(long offset)
{
	CString *s;
	CString *recordPtr = new CString (2048);

	clearRecord();
	tb950CollIn->SeekTo(offset);
	recordPtr->ReadLineWithPrefixedMaxSize(tb950CollIn);
//	Tokenizer = recordPtr->Data();


	CTokenizer Tokenizer(recordPtr->Data(), "&$%");
	long tokenLength;
	OrsChar *tokenPtr;
	int i=0;
	while(Tokenizer.GetToken(&tokenPtr, &tokenLength))
	{
		if(tokenLength)
			fieldsVector->Entry(i)->assign(tokenPtr, tokenLength);
		i++;
	} // End while

	if (i)
		i--;
	fieldsVector->Entry(i)->Strip(CString::trailing, '\n');

	delete recordPtr;
}



bool Tb950Coll::loadRecord(char *key)
{

	long position; // offset
	int offset; // 18/03/2014 32 bit
	char * entryPtr;
	bool retb;
	//	CString s;

	// Dal bid trova l'offset nel file degli autori
	CString entryFile;
	if (offsetBufferTbPtr)
		retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, key_length+OffsetPlusLfLength, key, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(this->tbOffsetIn, elementsTb, key_length+OffsetPlusLfLength, key, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (!retb)
	{
		SignalAnError(__FILE__, __LINE__, "Record trTitoloCollocazione non trovato per bid %s", key);
		return false;
	}

	// Dalla posizione prendiamo l'offset
	//offset = atol (entryPtr+key_length); // offsetBufferTbAutorePtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 05/02/2015
//		memcpy (&offset, entryPtr+ key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII

	tb950CollIn->SeekTo(offset);
	return loadNextRecord(key);

}


bool Tb950Coll::existsRecord(char *bid)
{

	long position; // offset
	int offset; // 18/03/2014 32 bit
	char * entryPtr;
	bool retb;
	//	CString s;

	// Dal bid trova l'offset nel file degli autori

	CString entryFile;
	if (offsetBufferTbPtr)
		retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, key_length+OffsetPlusLfLength, bid, key_length, position, &entryPtr);
	else
	{
		if (!this->tbOffsetIn)
			return false;
		retb = BinarySearch::search(this->tbOffsetIn, elementsTb, key_length+OffsetPlusLfLength, bid, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (!retb)
	{
		//SignalAnError(__FILE__, __LINE__, "Record trTitoloCollocazione non trovato per bid %s", bid);
		return false;
	}

	// Dalla posizione prendiamo l'offset
	//offset = atol (entryPtr+key_length); // offsetBufferTbAutorePtr+position

	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 05/02/2015
		//memcpy (&offset, entryPtr+ key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII


	tb950CollIn->SeekTo(offset);
//	return loadNextRecord(bid);
	return true;
}


bool Tb950Coll::loadRecordByKeyLoc(char *keyLoc)
{
	long position; // offset
	int offset; // 18/03/2014 32 bit
	char * entryPtr;
	bool retb;
	//	CString s;

	// Dal bid trova l'offset nel file degli autori



	CString entryFile;
	if (offsetBufferTb950CollKeylocPtr)
		retb = BinarySearch::search(offsetBufferTb950CollKeylocPtr, elementsTb950KeylocColl, keylocKeyLength+OffsetPlusLfLength, keyLoc, keylocKeyLength, position, &entryPtr);
	else
	{
//		retb = BinarySearch::search(offsetBufferTb950CollKeylocPtr, elementsTb950KeylocColl, keylocKeyLength+OffsetPlusLfLength, keyLoc, keylocKeyLength, position, &entryPtr);
		retb = BinarySearch::search(this->tb950CollOffsetKlocIn, elementsTb950KeylocColl, keylocKeyLength+OffsetPlusLfLength, keyLoc, keylocKeyLength, position, &entryFile);
		entryPtr = entryFile.data();
	}




	if (!retb)
	{
//		SignalAWarning(__FILE__, __LINE__, "Record trTitoloCollocazione non trovato per keyLoc %s", keyLoc);
		return false;
	}

	// Dalla posizione prendiamo l'offset
	// offset = atol (entryPtr+keylocKeyLength); // offsetBufferTbAutorePtr+position

	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 05/02/2015
		//memcpy (&offset, entryPtr+ key_length, 4);	// OFFSET BINARI
		//offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
		offset =  *((int*)(entryPtr+keylocKeyLength)); // 20/07/2017
	else
		//offset = atoi (entryPtr+key_length); // OFFSET in ASCII
		offset = atoi (entryPtr+keylocKeyLength); // 20/07/2017

	tb950CollIn->SeekTo(offset);
	return loadNextRecord();

}

long Tb950Coll::existsRecordByKeyLoc(char *keyLoc)
{
	long position; // offset
	int offset; // 18/03/2014 32 bit

	char * entryPtr;
	bool retb;
	//	CString s;

	// Dal bid trova l'offset nel file degli autori
//	retb = BinarySearch::search(offsetBufferTb950CollKeylocPtr, elementsTb950KeylocColl, keylocKeyLength+OffsetPlusLfLength, keyLoc, keylocKeyLength, position, &entryPtr);

	CString entryFile;
	if (offsetBufferTb950CollKeylocPtr)
		retb = BinarySearch::search(offsetBufferTb950CollKeylocPtr, elementsTb950KeylocColl, keylocKeyLength+OffsetPlusLfLength, keyLoc, keylocKeyLength, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(this->tb950CollOffsetKlocIn, elementsTb950KeylocColl, keylocKeyLength+OffsetPlusLfLength, keyLoc, keylocKeyLength, position, &entryFile);
		entryPtr = entryFile.data();
	}



	if (!retb)
	{
		SignalAnError(__FILE__, __LINE__, "Record trTitoloCollocazione non trovato per bid %s", bid);
		return false;
	}

	// Dalla posizione prendiamo l'offset
	//offset = atol (entryPtr+keylocKeyLength); // offsetBufferTbAutorePtr+position
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY) // 05/02/2015
//		memcpy (&offset, entryPtr+ key_length, 4);	// OFFSET BINARI
		offset =  *((int*)(entryPtr+key_length)); // 24/03/2015
	else
		offset = atoi (entryPtr+key_length); // OFFSET in ASCII

	return offset;

}



bool Tb950Coll::loadNextRecord()
{
//	char * separator = "&$%";
//	CTokenizer Tokenizer(separator);
//	long tokenLength;
//	OrsChar *tokenPtr;
	CString *s;

	CString *recordPtr = new CString (2048);
	bool retb;

	clearRecord();
	retb = recordPtr->ReadLineWithPrefixedMaxSize(tb950CollIn);
	if (!retb)
	{
		delete recordPtr;
		return false;
	}


//	Tokenizer = recordPtr->Data();
//	int i=0;
//	while(Tokenizer.hasToken())
//	{
//		s = (CString*)fieldsVector->Entry(i);
//		tokenPtr = Tokenizer.GetToken (IS_NOT_QUOTED_TOKEN, separator, &tokenLength);
//		s->assign(tokenPtr, tokenLength); // Mettendo le due chiamate come argomento mi esegue per prima la seconda quando la lunghezza non [ ancora valorizzata
//
//		i++;
//	}
//	if (s->GetLastChar() == '\n')
//		s->ExtractLastChar();

	CTokenizer Tokenizer(recordPtr->Data(), "&$%");
	long tokenLength;
	OrsChar *tokenPtr;
	int i=0;
	while(Tokenizer.GetToken(&tokenPtr, &tokenLength) && i < tbFields)
	{
		if(tokenLength)
			fieldsVector->Entry(i)->assign(tokenPtr, tokenLength);
		i++;
	} // End while

	if (i)
		i--;
	fieldsVector->Entry(i)->Strip(CString::trailing, '\n');



	delete recordPtr;
	return true;

}

void Tb950Coll::stripRecordFieldsTrailing()
{
	CString *sPtr;
	for (int i=0; i < TB950COLL_FIELDS; i++)
	{
		sPtr = fieldsVector->Entry(i);
		sPtr->Strip(sPtr->trailing, ' ');
	}
}

bool Tb950Coll::loadNextRecord(char *bid)
{
//	char * separator = "&$%";
//	CTokenizer Tokenizer(separator);
//	long tokenLength;
//	OrsChar *tokenPtr;
	CString *s;

	clearRecord();
	CString *recordPtr = new CString (2048);
	recordPtr->ReadLineWithPrefixedMaxSize(tb950CollIn);
	if (!recordPtr->StartsWith(bid)) // != 0
	{
		delete recordPtr;
		return false; // gone past bid
	}



	CTokenizer Tokenizer(recordPtr->Data(), "&$%");
	long tokenLength;
	OrsChar *tokenPtr;
	int i=0;
	while(Tokenizer.GetToken(&tokenPtr, &tokenLength) && i < tbFields)
	{
		if(tokenLength)
			fieldsVector->Entry(i)->assign(tokenPtr, tokenLength);
		i++;
	} // End while

	if (i)
		i--;
	fieldsVector->Entry(i)->Strip(CString::trailing, '\n');

	delete recordPtr;
	return true;

}
