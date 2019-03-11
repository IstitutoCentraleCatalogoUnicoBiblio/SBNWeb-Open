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
 * MarcRecord.h
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#ifndef MARCRECORD_H_
#define MARCRECORD_H_
#include "library/CString.h"
#include "library/tvvector.h"
#include "Leader.h"
#include "ControlField.h"
#include "DataField.h"


class MarcRecord {
private:
	long id;
	Leader *leader;
	ATTValVector<ControlField*> *controlFieldsVector;
	ATTValVector<DataField*> *dataFieldsVector;
	CString type;
	CString stringed;
	void init();
	void stop();

public:
	MarcRecord();
	virtual ~MarcRecord();

	long getId() const;
    void setId(long id);
    Leader *getLeader() const;
//    void setLeader(Leader *leader);
    ATTValVector<ControlField*> *getControlFieldsVector() const;
    void setControlFieldsVector(ATTValVector<ControlField*> *controlFieldsVector);
    ATTValVector<DataField*> * getDataFieldsVector() const;
    ControlField* getControlField(char * fieldId) const;
    DataField* getDataField(char * fieldId) const;

    void setDataFieldsVector(ATTValVector<DataField*> *dataFieldsVector);
    char* getType();
    void setType(char* type);
    void clear();
    void addControlField(ControlField* cf);
    void addDataField(DataField* df);
    char *toString();
    CString *toCString();
    long toStringLength();
    char *toXml();
    long toXmlLength();

    void sortDataFields();
    int existsDataField(int anId) const;
    int removeDataField(DataField *dfp) const;
    int removeDataFields(int anId) const;
;

};

#endif /* MARCRECORD_H_ */
