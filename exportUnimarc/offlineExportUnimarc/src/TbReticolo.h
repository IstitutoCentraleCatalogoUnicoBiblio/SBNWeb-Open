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
#include "library/tvvector.h"
#include "library/CString.h"
#include "../include/library/CString.h"
/*
 * TbReticolo.h
 *
 *  Created on: 15-feb-2009
 *      Author: Arge
 */


#ifndef TBRETICOLO_H_
#define TBRETICOLO_H_

class TbReticolo {
	ATTValVector<CString*> *fieldsVector;
	int tbFields;

	void init(char *record);
	void stop();

public:
	TbReticolo(char *record);
	virtual ~TbReticolo();
	char * getField(int aFieldId);
	CString * getFieldString(int aFieldId);
	void assign(char *record);
	void dumpRecord();

};

#endif /* TBRETICOLO_H_ */
