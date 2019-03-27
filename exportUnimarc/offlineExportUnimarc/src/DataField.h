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
 * DataField.h
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#ifndef DATAFIELD_H_
#define DATAFIELD_H_
#include "library/tvvector.h"
#include "library/CString.h"

#include "Embedded.h"
#include "Subfield.h"

class DataField {

private:
	int tagId;
	CString tag;
	char indicator1;
	char indicator2;
	ATTValVector <Subfield *> *subfieldsVector;
	CString stringed;

	void stop();

public:
//	ATTValVector <Embedded *> *embeddedVector;
	Embedded *embedded;

	DataField();
	DataField(char* tag, int len);
	DataField(char* tag, char ind1, char ind2);
	DataField(CString* tag, char ind1, char ind2);
	virtual ~DataField();
	void setIndicator1(char ind1);
	char getIndicator1();
	void setIndicator2(char ind2);
	char getIndicator2();
	void addSubfield(Subfield *subfield);
	void insertSubfield(int pos, Subfield *subfield);
	ATTValVector<Subfield*> * getSubfields();
	ATTValVector <Subfield *> * getSubfields(char code);
	Subfield *getSubfield(char code);
	Subfield *getNthSubfield(char code, int nth);

	int getId();
//    void setId(long  id);
    bool find(char* pattern);
    char* toString();
    CString * toCString(char *recId);
    char* toXml(int indent);
    CString* toCStringXml(int indent);

    CString* toSubTag();
    long getToXmlLength();

    //char* getTag();
    CString *getTagString();
    void setTag(char* tag);
//    ATTValVector<Subfield*> *getSubfieldsVector(); //  const
    void setSubfieldsVector(ATTValVector<Subfield*> *subfieldsVector);
    void removeSubfield(char code);
    void removeLastSubfield(char code);
	bool setDataField(DataField* aDf);
	void dump();
	void embedDataField(DataField * dfPtr);

};

#endif /* DATAFIELD_H_ */
