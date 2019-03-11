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
 * TrcPossProvInventari.cpp
 *
 *  Created on: 25-gen-2010
 *      Author: Arge
 */


#include "TrcPossProvInventari.h"
#include "BinarySearch.h"
#include "../include/library/CTokenizer.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);


TrcPossProvInventari::TrcPossProvInventari(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 12;
	init();
}

TrcPossProvInventari::~TrcPossProvInventari() {

}


void TrcPossProvInventari::getPrimaryKey(CString &pKey) {
	char primaryKey [16+1];
	primaryKey[16] = 0;
	memcpy (primaryKey, stringRecord->Data()+32, 3);
	memcpy (primaryKey+3, stringRecord->Data()+26, 3);
	memcpy (primaryKey+6, stringRecord->Data()+13, 10);
	pKey = primaryKey;
}

bool TrcPossProvInventari::loadNextRecord(const char *key)
{
	CTokenizer Tokenizer("&$%");
	OrsChar *Token;
	CString *s;
//	CString record;
	CString pKey;


	clearRecord();
	if(!stringRecord->ReadLineWithPrefixedMaxSize(tbIn))
		return false;
	getPrimaryKey(pKey);

//	if (!stringRecord->StartsWith(key)) // != 0
//		return false; // gone past bid

	if (!pKey.isEqual(key))
		return false;


//	if (stringRecord->GetLastChar() == '\n')
//		stringRecord->ExtractLastChar();

	Tokenizer = stringRecord->Data();

	int i=0;
	while(i < tbFields && Tokenizer.hasToken())
	{
		Token = Tokenizer.GetToken();
		s = (CString*)fieldsVector->Entry(i);
		s->assign(Token);
		i++;
	}
	s->Strip(s->trailing, '\n');

	#ifdef DEBUG_CONSOLE
	dumpRecord();
	#endif
	return true;

} // End loadNextRecord



bool TrcPossProvInventari::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TrcPossProvInventari::loadRecord");
		return false;
	}
	return true;
} // End loadRecord


