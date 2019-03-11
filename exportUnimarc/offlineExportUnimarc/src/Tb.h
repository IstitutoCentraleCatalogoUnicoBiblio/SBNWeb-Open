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
 * Tb.h
 *
 *  Created on: 6-mag-2009
 *      Author: Arge
 */


#ifndef TB_H_
#define TB_H_

#include "library/tvvector.h"
#include "library/CString.h"
#include "library/CFile.h"
#include "MarcGlobals.h" // per debug

#define INITIAL_STRING_BUFFER_SIZE	2048

#define MAX_DB_FIELDS 60

class Tb {
	void stop();
	void init1(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);

protected:
	char * entryPtrDaIndice;
	CFile *tbIn;
	CFile *tbOffsetIn;
	CString *stringRecord;

	char *offsetBufferTbPtr;
	long elementsTb;
	int keyPlusOffsetPlusLfLength;
	int key_length;

	ATTValVector<CString*> *fieldsVector;
	CString keyLoadedRecord;

	char columnDereferenceAr[MAX_DB_FIELDS]; // Tabella di referenziazione indiretta per mappare la stessa tabella di DB diversi
	bool tableIsDereferenced;

//	CTokenizer Tokenizer; // ("&$%")
	CString recordSeparator; // ("&$%")
	void initCommon();

public:


	int tbFields;	// #define TR_TIT_BIB_FIELDS 26
	void init();


	Tb(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	Tb(CFile *tbIn);
	Tb();
	Tb(Tb* aTb);
	virtual ~Tb();


	char * getField(int aFieldId); 	// const
	int    getFieldLength(int aFieldId);
	CString * getFieldString(int aFieldId);
	CString * getStringRecord();
	const char * getStringRecordData();
	char * 	getOffsetBufferTbPtr();
	int	getKeyPlusOffsetPlusLfLength();
	int getKeyLength();
	long 	getElementsTb();
	CFile* getTbIn();
	CFile* getTbOffsetIn();
	//char *getRecordSeparator();
	CString *getRecordSeparator();


	void positionAtOffset(long offset);

	virtual bool loadRecord(const char *key);
	virtual bool loadRecordFromOffset(long offset);

//	bool loadRecordFromBinaryOffset(const char *key); // 04/02/2015



	bool loadNextRecord();
	virtual bool loadNextRecord(const char *key);
	bool loadNextRecordDaIndice(const char *key);

	virtual bool loadRecordFromString(char *aStringRecord, int length);

	bool existsRecord(const char *key);
	virtual bool existsRecordNonUnique(const char *key);
	virtual bool existsRecordNonUniqueDaIndice(const char *key);


	void stripRecordFieldsTrailing();

	void setRecordSeparator(char *rs);
	void setRecordSeparator(char rs);

	void clearRecord();
	void dumpRecord();
	void dumpStringRecord();

};

#endif /* TB_H_ */
