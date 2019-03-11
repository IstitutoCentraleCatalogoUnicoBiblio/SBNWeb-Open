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
 * Subfield.h
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#ifndef SUBFIELD_H_
#define SUBFIELD_H_
#include "library/CString.h"
#include "MarcConstants.h"

class Subfield {
private:
	long id;
    char code;
    CString data;
    CString stringed;


public:
	Subfield();
	Subfield(char code);
	Subfield(char code, char* data);
	Subfield(char code, CString* data);
	Subfield(int id, char code, CString* data);

	Subfield(char code, char* data, int len);

	virtual ~Subfield();

	long getId(); // sfNew->
    void setId(long  id);
    char getCode(); //  const
    void setCode(char code);
    char* getData();
    long getDataLength();
    long getToStringLength();
    CString* toCString();

    long getToXmlLength();


    CString* getDataString();
//    void setData(const char* data);
    void setData(CString *sPtr);
    void setData(char* data, int len);


    void appendData(char* data);

    void appendData(CString* sPtr);
    void appendData(char* ptr, int length);



    bool find(char* pattern);
    char* toString();
    CString* toXml(int indent);

};

#endif /* SUBFIELD_H_ */
