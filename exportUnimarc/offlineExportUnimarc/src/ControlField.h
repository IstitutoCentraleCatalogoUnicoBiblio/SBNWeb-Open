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
 * ControlField.h
 *
 * Control fields go from 001 to 009
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#ifndef CONTROLFIELD_H_
#define CONTROLFIELD_H_
#include "library/CString.h"

class ControlField {

private:
	long id;
	CString data;
	CString tag;
	CString stringed;

public:
	ControlField();
	ControlField(char *tag);
	ControlField(char* tag, char* data);
	virtual ~ControlField();
	char *toString();
	CString* toCString();

	CString* toSubTag();

	char *toXml(int indent);
	CString* toCStringXml(int indent);

	void setId(long id);
	long getId();
	void setTag(char * tag);
	void setTag(CString * tag);
	char *getTag();
	void setData(char* data, int len);
	void setData(CString * data);
	//char * getData();
	CString * getData();

	long getToXmlLength();
	bool find(char* pattern);

};

#endif /* CONTROLFIELD_H_ */
