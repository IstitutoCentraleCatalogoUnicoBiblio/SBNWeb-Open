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
 * Tb.cpp
 *
 *  Created on: 6-mag-2009
 *      Author: Arge
 */

#include <stdio.h>
#include <stdlib.h>

#include "Tb.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"

#include "library/CTokenizer.h"
#include "BinarySearch.h"
#include "library/CString.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);


Tb::Tb(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) {
	init1(tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length);
}

void Tb::init1(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length)
{
	this->tbIn = tbIn;
	this->tbOffsetIn = tbOffsetIn;
	this->offsetBufferTbPtr = offsetBufferTbPtr;
	this->elementsTb = elementsTb;
	this->keyPlusOffsetPlusLfLength = keyPlusOffsetPlusLfLength;
	this->key_length = key_length;
	this->tableIsDereferenced = false;
	stringRecord = new CString(INITIAL_STRING_BUFFER_SIZE);
	recordSeparator = "&$%";
}




Tb::Tb(CFile *tbIn) {
	initCommon();
	this->tbIn = tbIn; // ricopri
}

Tb::Tb() {
	initCommon();
}

void Tb::initCommon()
{
	this->tbIn = 0;
	this->tbOffsetIn = 0;
	this->offsetBufferTbPtr = 0;
	this->elementsTb = 0;
	this->keyPlusOffsetPlusLfLength = 0;
	this->key_length = 0;
	this->tableIsDereferenced = false;
	stringRecord = new CString(INITIAL_STRING_BUFFER_SIZE);
	recordSeparator = "&$%";
}

void Tb::init()
{
	CString *sPtr;
	fieldsVector = new ATTValVector<CString*>();

	for (int i=0; i < tbFields; i++)
	{
		sPtr = new CString();
		if (!sPtr)
		{
		    SignalAnError(__FILE__, __LINE__, "failed to instantiate string for entry: %d", i);
		    break;
		}
		fieldsVector->Add(sPtr);
	}
}

//char *Tb::getRecordSeparator() {
//	return recordSeparator.data();
//}

CString *Tb::getRecordSeparator() {
	return &recordSeparator;
}


void Tb::setRecordSeparator(char *rs) {
	recordSeparator = rs;
}

void Tb::setRecordSeparator(char rs) {
	recordSeparator.Clear();
	recordSeparator.AppendChar(rs);
}


Tb::~Tb() {
	stop();
}






void Tb::stop() {
	fieldsVector->DeleteAndClear();
	delete fieldsVector;
	delete 	stringRecord;

}



void Tb::clearRecord()
{

	for (int i=0; i < tbFields; i++)
		fieldsVector->Entry(i)->Clear();
}


const char * Tb::getStringRecordData()
{
	return stringRecord->data();
}
CString * Tb::getStringRecord()
{
	return stringRecord;
}



Tb::Tb(Tb *aTb)
{
	this->tbIn = aTb->tbIn;
	this->tbOffsetIn = aTb->tbOffsetIn;
	this->offsetBufferTbPtr = aTb->offsetBufferTbPtr;
	this->elementsTb = aTb->elementsTb;
	this->keyPlusOffsetPlusLfLength = aTb->keyPlusOffsetPlusLfLength;
	this->key_length = aTb->key_length;
	this->tableIsDereferenced = aTb->tableIsDereferenced;

	this->recordSeparator = aTb->recordSeparator;

	stringRecord = new CString(INITIAL_STRING_BUFFER_SIZE);

	CString *sPtr;
	tbFields = aTb->tbFields;

	init();

	for (int i=0; i < tbFields; i++)
	{
		sPtr = fieldsVector->Entry(i);// new CString();
		sPtr->AppendString( aTb->getFieldString(i));
	}

}

void Tb::positionAtOffset(long offset)
{
	tbIn->SeekTo(offset);
}


void Tb::stripRecordFieldsTrailing()
{
	CString *sPtr;
	for (int i=0; i < tbFields; i++)
	{
		sPtr = fieldsVector->Entry(i);
		sPtr->Strip(sPtr->trailing, ' ');
	}
}


char *Tb::getOffsetBufferTbPtr()
{
	return offsetBufferTbPtr;
}

long Tb::getElementsTb()
{
	return elementsTb;
}

int Tb::getKeyPlusOffsetPlusLfLength()
{
	return keyPlusOffsetPlusLfLength;
}

int Tb::getKeyLength()
{
	return key_length;
}

CFile* Tb::getTbIn()
{
	return tbIn;
}
CFile* Tb::getTbOffsetIn()
{
	return tbOffsetIn;
}


//const char * Tb::getField(int aFieldId)
char * Tb::getField(int aFieldId)
{

	if (tableIsDereferenced)
	{
		// Dereference field
		int dereferencedFieldId = columnDereferenceAr[aFieldId];
		aFieldId = dereferencedFieldId;
	}

	if (aFieldId < fieldsVector->Length())
		return fieldsVector->Entry(aFieldId)->data();
	else
	{
    SignalAnError(__FILE__, __LINE__, "Tb::getField: n out of range: %d", aFieldId);
	return (char *)"";
	}
}

int Tb::getFieldLength(int aFieldId)
{
	if (tableIsDereferenced)
	{
		// Dereference field
		int dereferencedFieldId = columnDereferenceAr[aFieldId];
		aFieldId = dereferencedFieldId;
	}

	if (aFieldId < fieldsVector->Length())
		return fieldsVector->Entry(aFieldId)->Length();
	else
	{
    SignalAnError(__FILE__, __LINE__, "Tb::getFieldLength: n out of range: %d", aFieldId);
	return 0;
	}
}

CString * Tb::getFieldString(int aFieldId)
{
	if (tableIsDereferenced)
	{
		// Dereference field
		int dereferencedFieldId = columnDereferenceAr[aFieldId];
		aFieldId = dereferencedFieldId;
	}

	if (aFieldId < fieldsVector->Length())
		return fieldsVector->Entry(aFieldId);
	else
	{
    SignalAnError(__FILE__, __LINE__, "Tb::getField: n out of range: %d", aFieldId);
	return 0;
	}
}








void Tb::dumpStringRecord()
{
	printf ("\nSTRING RECORD='%s'", stringRecord->data());
}

bool Tb::existsRecordNonUnique(const char *key)
{

	long position;
	char * entryPtr;
	bool retb;
	char *ptr;
	CString entryFile;

		if (offsetBufferTbPtr)
			retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryPtr);
		else
		{
			if (!this->tbOffsetIn)
				return false;
			retb = BinarySearch::search(this->tbOffsetIn, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryFile);
			entryPtr = entryFile.data();
		}

	if (!retb)
	{
		return false;
	}

	if (offsetBufferTbPtr)
	{ // Stiamo lavorando in Memoria
		while ((ptr = entryPtr-keyPlusOffsetPlusLfLength) >= offsetBufferTbPtr)
		{
			if (!(strncmp(ptr, entryPtr, key_length)))
				entryPtr = ptr;
			else
				break; // Trovato il primo
		}
	}
	else
	{ // Stiamo lavorando su file
		// Posizioniamoci sulla riga trovata
		int goBackBytes = -(keyPlusOffsetPlusLfLength << 1); // moltiplica per 2

		// Torniamo indietro di una riga
		while (true)
		{
			if (this->tbOffsetIn->CurOffset() <= keyPlusOffsetPlusLfLength)
				break; // siamo alla prima riga

			this->tbOffsetIn->SeekToFromCurPos(goBackBytes);
//			entryFile.ReadLine(tbOffsetIn);
			entryFile.ReadBytes(tbOffsetIn, keyPlusOffsetPlusLfLength); // 20/02/2015

			if (strncmp (key, entryFile.data(), key_length))
			{
//				entryFile.ReadLine(tbOffsetIn); // Read in first valid line
				entryFile.ReadBytes(tbOffsetIn, keyPlusOffsetPlusLfLength); // 20/02/2015
				entryPtr = entryFile.data();
				break;
			}
		}
	}

	// Dalla posizione prendiamo l'offset

//	long long llPos = atoll (entryPtr+keyPlusOffsetPlusLfLength-12);
//	return tbIn->SeekToLarge(llPos);

	// 04/02/2015
	if (OFFSET_TYPE == OFFSET_TYPE_BINARY)
	{ // OFFSET BINARI
		// Stiamo usando un offset long o long long?
		int offsetLen = keyPlusOffsetPlusLfLength - key_length;
		if (offsetLen == 4)
		{ // LONG
			//long lPos;
			//int iPos; // 18/03/2014 intel 32 bit int e long sono a 32bit
//			memcpy (&iPos, entryPtr+ key_length, 4);
			int iPos =  *((int*)(entryPtr+ key_length)); // 24/03/2015
			if (!tbIn->SeekTo(iPos))
				return false;
		}
		else if (offsetLen == 8)
		{ // LONG LONG
			//long long llPos;
//			memcpy (&llPos, entryPtr+ key_length, 8);
			long long llPos =  *((long long*)(entryPtr+ key_length)); // 24/03/2015
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
		long long llPos = atoll (entryPtr+keyPlusOffsetPlusLfLength-12); // OFFSET in ASCII
		if (!tbIn->SeekToLarge(llPos))
			return false;
	}
	return true;


} // end existsRecordNonUnique




bool Tb::loadRecordFromOffset(long offset)
{
	tbIn->SeekTo(offset);
	return loadNextRecord();

} // End Tb::loadRecordFromOffset





bool Tb::existsRecord(const char *key)
{

	long position; // offset,
	char * entryPtr;
	bool retb;

	// Dalla chiave trova l'offset nel file degli esemplri
	CString entryFile;
	if (offsetBufferTbPtr)
		retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(this->tbOffsetIn, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}

	if (!retb)
	{
//		SignalAnError(__FILE__, __LINE__, "Record Tb950Ese non trovato per bid '%s'", bid);
		return false;
	}


	// Dalla posizione prendiamo l'offset
//	long long llPos = atoll (entryPtr+keyPlusOffsetPlusLfLength-12);
//	return tbIn->SeekToLarge(llPos);


	if (OFFSET_TYPE == OFFSET_TYPE_BINARY)
	{ // OFFSET BINARI
		// Stiamo usando un offset long o long long?
		int offsetLen = keyPlusOffsetPlusLfLength - key_length;
		if (offsetLen == 4)
		{ // LONG
			//long lPos;
			//int iPos; // 18/03/2014 intel 32 bit int e long sono a 32bit
//			memcpy (&iPos, entryPtr+ key_length, 4);
			int iPos =  *((int*)(entryPtr+ key_length)); // 24/03/2015
			if (!tbIn->SeekTo(iPos))
				return false;
		}
		else if (offsetLen == 8)
		{ // LONG LONG
			//long long llPos;
//			memcpy (&llPos, entryPtr+ key_length, 8);
			long long llPos =  *((long long*)(entryPtr+ key_length)); // 24/03/2015
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
		long long llPos = atoll (entryPtr+keyPlusOffsetPlusLfLength-12); // OFFSET in ASCII
		if (!tbIn->SeekToLarge(llPos))
			return false;
	}
return true;
} // end existsRecord





bool Tb::existsRecordNonUniqueDaIndice(const char *key)
{
	long position;
	bool retb;
	char *ptr;
	CString entryFile;

	if (offsetBufferTbPtr)
		retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryPtrDaIndice);
	else
	{
		retb = BinarySearch::search(this->tbOffsetIn, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryFile);
		entryPtrDaIndice = entryFile.data();
	}

	if (!retb)
		return false;

	// Portiamoci sul primo della serie (se per esempio son 4 potrenmmo essere posizionati sul secondo)
	if (offsetBufferTbPtr)
	{ // Stiamo lavorando in Memoria
		while ((ptr = entryPtrDaIndice-keyPlusOffsetPlusLfLength) >= offsetBufferTbPtr)
		{
			if (!(strncmp(ptr, entryPtrDaIndice, key_length)))
				entryPtrDaIndice = ptr;
			else
				break; // Trovato il primo
		}
	}
	else
	{ // Stiamo lavorando su file
		int goBackBytes = -(keyPlusOffsetPlusLfLength << 1); // moltiplica per 2
		// Torniamo indietro di una riga
		while (true)
		{
			if (this->tbOffsetIn->CurOffset() <= keyPlusOffsetPlusLfLength)
				return true;
				//break; // siamo alla prima riga

			this->tbOffsetIn->SeekToFromCurPos(goBackBytes);
//			entryFile.ReadLine(tbOffsetIn);
			entryFile.ReadBytes(tbOffsetIn, keyPlusOffsetPlusLfLength); // 20/02/2015

			if (strncmp (key, entryFile.data(), key_length))
			{
				// Lasciamo la lettura di chiave offset al metodo che cicla sulle occorrenze multiple
				return true;
			}
		}
	}

	return true;
} // end existsRecordNonUniqueDaIndice



















void Tb::dumpRecord()
{
//	printf ("\n\nRECORD Tb %s:", this->stringRecord->data());
	printf ("\n\nRECORD Tb");
	for (int i=0; i < tbFields; i++)
	{
		printf ("\n%02d:'%s'", i, fieldsVector->Entry(i)->data());
	}
}











bool Tb::loadRecord(const char *key)
{
	long position; // offset,
	char * entryPtr;
	bool retb;


	// Dal bid trova l'offset nel file
	CString entryFile;
	if (offsetBufferTbPtr)
		retb = BinarySearch::search(offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryPtr);
	else
	{
		retb = BinarySearch::search(this->tbOffsetIn, elementsTb, keyPlusOffsetPlusLfLength, key, key_length, position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (!retb)
	{
	    SignalAnError(__FILE__, __LINE__, "Record non trovato per chiave %s", key);
		return false;
	}


	if (OFFSET_TYPE == OFFSET_TYPE_BINARY)
	{ // OFFSET BINARI
		// Stiamo usando un offset long o long long?
		int offsetLen = keyPlusOffsetPlusLfLength - key_length;
		if (offsetLen == 4)
		{ // LONG
			//long lPos;
			//int iPos; // 18/03/2014 intel 32 bit int e long sono a 32bit
//			memcpy (&iPos, entryPtr+ key_length, 4);
			int iPos =  *((int*)(entryPtr+ key_length)); // 24/03/2015
			if (!tbIn->SeekTo(iPos))
				return false;
		}
		else if (offsetLen == 8)
		{ // LONG LONG
			//long long llPos;
//			memcpy (&llPos, entryPtr+ key_length, 8);
			long long llPos =  *((long long*)(entryPtr+ key_length)); // 24/03/2015
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
		long long llPos = atoll (entryPtr+keyPlusOffsetPlusLfLength-12); // OFFSET in ASCII
		if (!tbIn->SeekToLarge(llPos))
			return false;
	}

	return loadNextRecord();
} // End loadRecord



/**
 * Reads record at current position
 */
bool Tb::loadNextRecord()
{
	// Controlla che il buffer non sia stato ridimensionato da qualche parte!!!
	if (stringRecord->getBufferSize() != INITIAL_STRING_BUFFER_SIZE)
		stringRecord->ResizeNew(INITIAL_STRING_BUFFER_SIZE);

	// clearRecord(); assegnazione e pulizia gestito da Split_assign

	if(!stringRecord->ReadLineWithPrefixedMaxSize(tbIn))
		return false;

//printf ("\nRecord: '%s'", stringRecord->data());

/*
	CTokenizer Tokenizer(stringRecord->Data(), recordSeparator.data());
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

*/
	stringRecord->Split_assign(*fieldsVector, (const char *)recordSeparator.data());

//	dumpRecord();
	return true;
} // End loadNextRecord

bool Tb::loadNextRecord(const char *key)
{
	bool retb = stringRecord->ReadLineWithPrefixedMaxSize(tbIn);

	if(!retb)
		return false;
	if (!stringRecord->StartsWith(key)) // != 0
		return false; // gone past bid

/*
	clearRecord();
	CTokenizer Tokenizer(stringRecord->Data(), recordSeparator.data());
	OrsChar *tokenPtr;
	long tokenLength;
	int i=0;
	while(Tokenizer.GetToken(&tokenPtr, &tokenLength) && i < tbFields)
	{
		if(tokenLength)
		{
			fieldsVector->Entry(i)->assign(tokenPtr, tokenLength);
		}
		i++;
	} // End while

	if (i)
		i--;
	fieldsVector->Entry(i)->Strip(CString::trailing, '\n');
*/
	stringRecord->Split_assign(*fieldsVector, (const char *)recordSeparator.data());

	return true;
} // End loadNextRecord



bool Tb::loadNextRecordDaIndice(const char *key)
{
//	char * separator = recordSeparator.data();
//	CString *sPtr;
	CString entryFile;

	//long position;
	bool retb;

	if (offsetBufferTbPtr)
	{
		entryFile.assign(entryPtrDaIndice, keyPlusOffsetPlusLfLength);
		entryPtrDaIndice += keyPlusOffsetPlusLfLength; // get ready for next one
		retb = true;
	}
	else
	{
//		entryFile.ReadLine(tbOffsetIn);
		entryFile.ReadBytes(tbOffsetIn, keyPlusOffsetPlusLfLength); // 09/02/2015
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
			//long lPos;
			//int iPos; // 18/03/2014 intel 32 bit int e long sono a 32bit
//			memcpy (&iPos, entryFile.data()+ key_length, 4);
			int iPos =  *((int*)(entryFile.data()+ key_length)); // 24/03/2015
			if (!tbIn->SeekTo(iPos))
				return false;
		}
		else if (offsetLen == 8)
		{ // LONG LONG
			//long long llPos;
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

	if (!stringRecord->StartsWith(key)) // != 0
		return false; // gone past bid


//	CTokenizer Tokenizer(separator);
//	Tokenizer = stringRecord->Data();
//	int i=0;
//	while(Tokenizer.hasToken())
//	{
//		sPtr = (CString*)fieldsVector->Entry(i);
//		tokenPtr = Tokenizer.GetToken (IS_NOT_QUOTED_TOKEN, separator, &tokenLength);
//		sPtr->assign(tokenPtr, tokenLength); // Mettendo le due chiamate come argomento mi esegue per prima la seconda quando la lunghezza non [ ancora valorizzata
//		sPtr->Strip(sPtr->trailing, '\r'); //20/01/2010 14.48
//		i++;
//	}
//	sPtr->Strip(sPtr->trailing, '\n');


//	CTokenizer Tokenizer(stringRecord->Data(), recordSeparator.data());
//	long tokenLength;
//	OrsChar *tokenPtr;
//	int i=0;
//	while(Tokenizer.GetToken(&tokenPtr, &tokenLength) && i < tbFields)
//	{
//		if(tokenLength)
//		{
//			fieldsVector->Entry(i)->assign(tokenPtr, tokenLength);
//		}
//		i++;
//	} // End while
//
//	if (i)
//		i--;
//	fieldsVector->Entry(i)->Strip(CString::trailing, '\n');
//	fieldsVector->Entry(i)->Strip(CString::trailing, '\r');

	stringRecord->Split_assign(*fieldsVector, (const char *)recordSeparator.data()); // 30/03/2015



//	dumpRecord();
	return true;
} // End loadNextRecordDaIndice


bool Tb::loadRecordFromString(char *aStringRecord, int length)
{
//	char * separator = recordSeparator.data();
//	CTokenizer Tokenizer(separator);
//	OrsChar *tokenPtr;
//	long tokenLength;
//	CString *s;
//
//	clearRecord();
//	CString sr = aStringRecord;
//	stringRecord->assign(aStringRecord, sr.Length());
//
//	Tokenizer = sr.Data();
//	int i=0;
//	while(Tokenizer.hasToken() && i < fieldsVector->length())
//	{
//		s = (CString*)fieldsVector->Entry(i);
//		tokenPtr = Tokenizer.GetToken (IS_NOT_QUOTED_TOKEN, separator, &tokenLength);
//		s->assign(tokenPtr, tokenLength); // Mettendo le due chiamate come argomento mi esegue per prima la seconda quando la lunghezza non [ ancora valorizzata
//		i++;
//	}


	clearRecord();
	stringRecord->assign(aStringRecord,  length);

//	CTokenizer Tokenizer(stringRecord->Data(), recordSeparator.data());
//	OrsChar *tokenPtr;
//	long tokenLength;
//
//	int i=0;
//	while(Tokenizer.GetToken(&tokenPtr, &tokenLength) && i < tbFields)
//	{
//		if(tokenLength)
//			fieldsVector->Entry(i)->assign(tokenPtr, tokenLength);
//		i++;
//	} // End while
//
//	if (i)
//		i--;
//	fieldsVector->Entry(i)->Strip(CString::trailing, '\n');

	stringRecord->Split_assign(*fieldsVector, (const char *)recordSeparator.data()); // 30/03/2015


	return true;
}

