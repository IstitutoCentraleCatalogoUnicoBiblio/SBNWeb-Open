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
 * Embedded.h
 *
 *  Created on: 5-feb-2010
 *      Author: Arge
 */


#ifndef EMBEDDED_H_
#define EMBEDDED_H_

//#include "DataField.h"
#include "ControlField.h"

class Embedded {
	ATTValVector <ControlField *> controlFieldsVector;
	ATTValVector < void *> dataFieldsVector; // DataField
	void stop();

public:
	Embedded();
	virtual ~Embedded();

	void addControlField(ControlField *controlField);
	ATTValVector<ControlField*> * getControlFieldsVector();
	void addDataField(void  *dataField); // DataField
	ATTValVector<void *> * getDataFieldsVector(); // DataField
	char* toXml();

};

#endif /* EMBEDDED_H_ */
