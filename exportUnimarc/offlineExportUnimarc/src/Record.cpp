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
 * Record.cpp
 *
 *  Created on: 28-ott-2009
 *      Author: Arge
 */

#include "Record.h"


extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);


Record::Record(int fields)
{
	this->fields = fields;
	init();
} // End Record

void Record::init()
{
	CString *sPtr;

	fieldsVector = new ATTValVector<CString*>();

	for (int i=0; i < fields; i++)
	{
		sPtr = new CString();
		if (!sPtr)
		{
		    SignalAnError(__FILE__, __LINE__, "failed to instantiate string for entry: %d", i);
		    break;
		}
		fieldsVector->Add(sPtr);
	}
} // End init

Record::~Record() {
	stop();
}

void Record::stop() {
	fieldsVector->DeleteAndClear();
	delete fieldsVector;
} // end stop


void Record::clear()
{
	stringRecord.Clear();
	for (int i=0; i < fields; i++)
		fieldsVector->Entry(i)->Clear();
} // End init


char * Record::getField(int aFieldId)
{
	if (aFieldId < fieldsVector->Length())
		return fieldsVector->Entry(aFieldId)->data();
	else
	{
    SignalAnError(__FILE__, __LINE__, "Tb::getField: n out of range: %d", aFieldId);
	return (char *)"";
	}
}

CString * Record::getFieldString(int aFieldId)
{
	if (aFieldId < fieldsVector->Length())
		return fieldsVector->Entry(aFieldId);
	else
	{
    SignalAnError(__FILE__, __LINE__, "Tb::getField: n out of range: %d", aFieldId);
	return 0;
	}
}

void Record::dumpRecord()
{
	printf ("\n\nRECORD string: '%s'", stringRecord.data());
	for (int i=0; i < fieldsVector->length(); i++)
	{
		printf ("\n%02d:%s", i, fieldsVector->Entry(i)->data());
	}
}



