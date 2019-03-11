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
 * TbcNotaInv.cpp
 *
 *  Created on: 20-nov-2009
 *      Author: Arge
 */


#include "TbcNotaInv.h"
#include <string.h>
#include "../include/library/CTokenizer.h"

TbcNotaInv::TbcNotaInv(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 11;
	init();
}

TbcNotaInv::~TbcNotaInv() {
}



void TbcNotaInv::getPrimaryKey(CString &pKey) {
	char primaryKey [16+1];
	primaryKey[16] = 0;
	memcpy (primaryKey, stringRecord->Data()+6, 3);
	memcpy (primaryKey+3, stringRecord->Data()+12, 3);
	memcpy (primaryKey+6, stringRecord->Data()+18, 10);
	pKey = primaryKey;
}

bool TbcNotaInv::loadNextRecord(const char *key)
{
	CString pKey;

	clearRecord();
	if(!stringRecord->ReadLineWithPrefixedMaxSize(tbIn))
		return false;
	getPrimaryKey(pKey);

	if (!pKey.isEqual(key))
		return false;


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







