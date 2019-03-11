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
 * Embedded.cpp
 *
 *  Created on: 5-feb-2010
 *      Author: Arge
 */


#include "Embedded.h"
#include "DataField.h"

Embedded::Embedded() {
}

Embedded::~Embedded() {
	stop();
}


void Embedded::stop() {
	controlFieldsVector.DeleteAndClear();

	//	dataFieldsVector.DeleteAndClear(); // Non funziona con i void pointers
	DataField * df;
	for (int i=0; i < dataFieldsVector.length(); i++)
	{
		df = (DataField *)dataFieldsVector.Entry(i);
		delete (df);
	}
}


void Embedded::addControlField(ControlField *controlField) {
	controlFieldsVector.Add(controlField);
    }

ATTValVector<ControlField*> * Embedded::getControlFieldsVector() {
        return &controlFieldsVector;
    }

void Embedded::addDataField(void *dataField) { // DataField
	dataFieldsVector.Add(dataField);
    }

ATTValVector<void *> * Embedded::getDataFieldsVector() { // DataField
        return &dataFieldsVector;
    }




char* Embedded::toXml() {
	return "";
    }

