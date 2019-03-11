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
 * Subfield.cpp
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#include "Subfield.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif



extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);



Subfield::Subfield() {
}

Subfield::~Subfield() {
}

/**
 * Creates a new <code>Subfield</code> and sets the data element
 * identifier.
 *
 * @param code
 *            the data element identifier
 */
Subfield::Subfield(char code) {
        //this->setCode(code);
		this->code = code;
    }

/**
 * Creates a new <code>Subfield</code> and sets the data element
 * identifier and the data element.
 *
 * @param code
 *            the data element identifier
 * @param data
 *            the data element
 */
Subfield::Subfield(char code, char* data) {
        this->setCode(code);
        this->setData(data, strlen(data));
    }

Subfield::Subfield(char code, CString* data) {
		this->code = code;
        this->data.assign(data->data(), data->Length());
    }

Subfield::Subfield(char code, char* data, int len) {
	this->code = code;

if (len < 0)
{
	SignalAnError(__FILE__, __LINE__, "Subfield::Subfield len %d < 0, code=%c data=%s", len, code, data);
	return;
}


    this->data.assign(data, len);
    }

Subfield::Subfield(int id, char code, CString* data) {
		this->code = code;
		this->id = id;
        this->data.assign(data->data(), data->Length());
    }




long Subfield::getId() //const
{
    return id;
}

void Subfield::setId(long  id)
{
    this->id = id;
}

char Subfield::getCode() //const
{
    return code;
}

void Subfield::setCode(char code)
{
    this->code = code;
}

char* Subfield::getData()
{
    return data.data();
}

long Subfield::getDataLength()
{
    return data.Length();
}



//void Subfield::appendData(char* data) // const
void Subfield::appendData(CString* sPtr)
{
	if (sPtr->Length() == 1)
	    this->data.AppendChar(sPtr->GetFirstChar()); // 24/03/2015
	else
		this->data.AppendString(sPtr);
}

void Subfield::appendData(char* ptr, int length)
{
	if (length == 1)
	    this->data.AppendChar(*ptr); // 24/03/2015
	else
		this->data.AppendString(ptr, length);
}

void Subfield::appendData(char* ptr) // TODO To be removed
{
	if (!*(ptr+1))
	    this->data.AppendChar(*ptr); // 24/03/2015
	else
		this->data.AppendString(ptr);
}





bool Subfield::find(char* pattern) {
//        Pattern p = Pattern.compile(pattern);
//        Matcher m = p.matcher(getData());
//        return m.find();
    printf ("TODO");
    return false;
}

/**
 * Returns a string representation of this subfield.
 *
 * <p>
 * Example:
 *
 * <pre>
 * $aSummerland /
 * </pre>
 *
 * @return String - a string representation of this subfield
 */
char* Subfield::toString() {
    stringed = '$';
    stringed.AppendChar(code);
    stringed.AppendString(&data);
    return stringed.data();
    }

CString* Subfield::toCString()
{

    stringed = '$';
    stringed.AppendChar(code);
    if (data.Length() == 1)
        stringed.AppendChar(data.GetFirstChar()); // 24/03/2015
    else
    	stringed.AppendString(&data);
    return &stringed;
}


long Subfield::getToStringLength() {
    return stringed.Length();
    }


CString* Subfield::getDataString()
{
	return &data;
}


CString* Subfield::toXml(int indent) {
	stringed = '\n';
	for (int i=0; i< indent; i++)
		stringed.AppendChar('\t');

	stringed.AppendString((char *)"<sf c=\"", 7);
    stringed.AppendChar(code);
    stringed.AppendString((char *)"\">", 2);


    data.Replace("<", "&lt;");
    data.Replace(">", "&gt;");
    if (data.IndexCharFrom('&', 0, CString::forward) != -1)
    	data.Replace("&", "&amp;");

    stringed.AppendString(&data);



    stringed.AppendString((char *)"</sf>", 5);
//    return stringed.data();
    return &stringed;
    }

long Subfield::getToXmlLength() {
    return stringed.Length();
    }

//void Subfield::setData(const char* data)
//{
//    this->data = data;
//}

void Subfield::setData(CString* data)
{
    this->data.assign(data);
}

void Subfield::setData(char* data, int len)
{
    this->data.assign(data, len);
}


