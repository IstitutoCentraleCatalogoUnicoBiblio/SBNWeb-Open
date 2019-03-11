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
 * MarcStreamWriter.h
 *
 *  Created on: 7-dic-2008
 *      Author: Arge
 */

#ifndef MARCSTREAMWRITER_H_
#define MARCSTREAMWRITER_H_


#include "library/CFile.h"
#include "MarcRecord.h"
#include "library/CBufferedData.h"

class MarcStreamWriter {

private:
    CString encoding;
    char buf[20];


//    void write(Leader *ldr);
	//void getEntry(char* tag, int length, int start, CString &entry);
	//void getDataElement(char * data, CString & dataElement);

protected:
	CFile *out;
    CString *ldrStr;
    CBufferedData *fieldData;
	CBufferedData *recordData;
	CBufferedData *directoryData;

public:

	MarcStreamWriter();
	MarcStreamWriter(CFile * out);
	MarcStreamWriter(CFile *out, char* encoding);

	virtual ~MarcStreamWriter();
	virtual bool write(MarcRecord* marcRecord);
	bool writeToFile();
	bool prepareRecordTowrite(MarcRecord* marcRecord);
	long getWriteToFileRecordLength();

	void init();
//	void setConverter(CharConverter converter);
//	CharConverter getConverter();
//	void close();




};

#endif /* MARCSTREAMWRITER_H_ */
