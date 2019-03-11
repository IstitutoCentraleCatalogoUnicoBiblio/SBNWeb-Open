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
 * TbRel.h
 *
 *  Created on: 28-ott-2009
 *      Author: Arge
 */


#ifndef TBREL_H_
#define TBREL_H_

#include "Tb.h"
#include "Record.h"

class TrRel: public Tb {
	ATTValVector<Record*> recordsVector;
	CString sourceId;

	CString stringRecord;
	void clearRecord();
	void stop();

public:

	TrRel(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrRel();
	virtual bool loadRecord(const char *key);
	virtual bool loadRecordFromOffset(long offset);
	void dumpRecord();


};

#endif /* TBREL_H_ */
