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
 * MarcStreamReader.h
 *
 *  Created on: 8-dic-2008
 *      Author: Arge
 */


#ifndef MARCSTREAMREADER_H_
#define MARCSTREAMREADER_H_

#include "MarcRecord.h"
#include "Leader.h"
#include "library/CFile.h"

#define MAX_FIELD_LENGTH 9999
#define MAX_SUBFIELD_LENGTH 9999

class MarcStreamReader {
private:
	class DirectoryEntry
	{
	public:
		DirectoryEntry(){};
		CString tag;
		int 	length;
		int 	start;
	};

	CFile *in;
    CString encoding;
    MarcRecord *record;
//    Leader *ldr;
    unsigned char readBuffer[MAX_FIELD_LENGTH+2+2+1]; // +2 indicators +$ + subfieldId + EOS
    unsigned char subfieldBuffer[MAX_FIELD_LENGTH+1]; // + EOS


	ATTValVector <DirectoryEntry *> *directoryEntries;

    DataField *parseDataField(char* tag); // byte[] field
    void parseLeader(unsigned char* leaderData); // byte[] leaderData
    char *getDataAsString(unsigned char*); // byte[] bytes
    bool isControlField(char* tag);
    bool isControlNumberField(char* tag);
    int getSubfield(int pos);
    void init();

public:
	MarcStreamReader();
	MarcStreamReader(CFile *in);
	MarcStreamReader(CFile *in, char * encoding);
	bool hasNext();
	MarcRecord* next();
	~MarcStreamReader();

};


#endif /* MARCSTREAMREADER_H_ */
