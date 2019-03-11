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
#include "../include/library/tvvector.h"
#include "../include/library/CString.h"
/*
 * Record.h
 *
 *  Created on: 28-ott-2009
 *      Author: Arge
 */


#ifndef RECORD_H_
#define RECORD_H_

class Record {
	ATTValVector<CString*> *fieldsVector;
	int fields;
	void init();
	void stop();

public:
	CString stringRecord;
	Record(int fields);
	virtual ~Record();
	void clear();
	char * getField(int aFieldId);
	CString * getFieldString(int aFieldId);
	void dumpRecord();

};

#endif /* RECORD_H_ */
