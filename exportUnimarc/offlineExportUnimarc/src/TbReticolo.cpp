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
 * TbReticolo.cpp
 *
 *  Created on: 15-feb-2009
 *      Author: Arge
 */


#include "TbReticolo.h"
#include "library/CTokenizer.h"

extern void SignalAnError(const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(const OrsChar *Module, OrsInt Line,	const OrsChar * MsgFmt, ...);


TbReticolo::TbReticolo(char *record) {
	fieldsVector=0;
	init(record);
}


TbReticolo::~TbReticolo() {
	stop();
}

void TbReticolo::stop() {
	fieldsVector->DeleteAndClear();
	delete fieldsVector;
}

char * TbReticolo::getField(int aFieldId) {
	if (aFieldId < fieldsVector->Length())
		return fieldsVector->Entry(aFieldId)->data();
	else {
		SignalAnError(__FILE__, __LINE__, "TbReticolo::getField: n out of range: %d", aFieldId);
		return (char *)"";
	}
}

CString * TbReticolo::getFieldString(int aFieldId) {
	if (aFieldId < fieldsVector->Length())
		return fieldsVector->Entry(aFieldId);
	else {
		SignalAnError(__FILE__, __LINE__, "TbReticolo::getField: n out of range: %d", aFieldId);
		return 0;
	}
}

void TbReticolo::dumpRecord()
{
	printf ("\n\nRECORD TbReticolo");
	for (int i=0; i < fieldsVector->length(); i++)
	{
		printf ("\n%02d:%s", i, fieldsVector->Entry(i)->data());
	}
}

void TbReticolo::assign(char *record) {

	if (!tbFields)
		return init (record); // 12/11/2018

	CTokenizer Tokenizer(record, ",");
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


} // TbReticolo::assign


void TbReticolo::init(char *record) {
//	CTokenizer Tokenizer(",");
//	OrsChar *Token;

	if (!fieldsVector)
		fieldsVector = new ATTValVector<CString*> ();

	CTokenizer Tokenizer(record, ",");
	long tokenLength;
	OrsChar *tokenPtr;
	while(Tokenizer.GetToken(&tokenPtr, &tokenLength))
		fieldsVector->Add(new CString(tokenPtr, tokenLength));

//	record->Split_assign(*fieldsVector, ","); // 31/03/2015


	tbFields = fieldsVector->length();
}

