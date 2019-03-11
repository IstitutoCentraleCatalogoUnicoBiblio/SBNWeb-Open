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
 * TrRepMar.cpp
 *
 *  Created on: 29-mar-2010
 *      Author: Arge
 */


#include "TrRepMar.h"
#include "library/CTokenizer.h"


TrRepMar::TrRepMar(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 9;
	init();
}


bool TrRepMar::loadNextRecord(const char *key)
{
	clearRecord();
	if(!stringRecord->ReadLineWithPrefixedMaxSize(tbIn))
		return false;
	OrsChar *ptr;
	ptr = stringRecord->Data()+13;
	if (strncmp(key, ptr, 10))
		return false; // gone past bid

//	char * separator = "&$%";
//	long tokenLength;
//	OrsChar *tokenPtr;
//	CString *s;
//	CTokenizer Tokenizer(separator);
//	Tokenizer = stringRecord->Data();
//
//	int i=0;
//	while(Tokenizer.hasToken() && i < tbFields)
//	{
//		s = (CString*)fieldsVector->Entry(i);
//		tokenPtr = Tokenizer.GetToken (IS_NOT_QUOTED_TOKEN, separator, &tokenLength);
//		s->assign(tokenPtr, tokenLength); // Mettendo le due chiamate come argomento mi esegue per prima la seconda quando la lunghezza non [ ancora valorizzata
//
//		s->Strip(s->trailing, '\r');
//		i++;
//	}
//	s->Strip(s->trailing, '\n');


	CTokenizer Tokenizer(stringRecord->Data(), "&$%");
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

	return true;



} // End loadNextRecord


TrRepMar::~TrRepMar() {
}
