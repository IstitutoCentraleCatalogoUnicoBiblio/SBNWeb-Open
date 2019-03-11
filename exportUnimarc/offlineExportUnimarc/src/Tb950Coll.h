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
#include "library/CString.h"
/*
 * TrTitoloCollocazione.h
 *
 *  Created on: 19-gen-2009
 *      Author: Arge
 */


#ifndef TB950COLL_H_
#define TB950COLL_H_

#define TB950COLL_FIELDS 13
#include "library/CFile.h"


class Tb950Coll {

	CFile * tb950CollIn;
	CFile *tbOffsetIn;
	char *offsetBufferTbPtr;

	CFile *tb950CollOffsetKlocIn;
	char *offsetBufferTb950CollKeylocPtr;


	long elementsTb;
	long elementsTb950KeylocColl;

	int OffsetPlusLfLength;
	int key_length, keylocKeyLength;

	ATTValVector<CString*> *fieldsVector;
	int tbFields;

	void init();
	void initFrom(Tb950Coll *tb950Coll);
	void stop();

public:
	enum trTitoloCollocazioneFieldId {
		bid,
		tbcol_bid_doc,
		tbcol_cd_doc,

		cd_biblioteca_sezione,
		nomeTabella,

		tbcol_key_loc,
		tbcol_cd_sez,
		tbcol_cd_loc,
		tbcol_spec_loc,
		tbcol_consis,

		// per le nuove etichette (960)
		tbcol_cd_sistema,
		tbcol_cd_edizione,
		tbcol_classe



	} ; // tbTitoloFieldId_type

//	Tb950Coll(CFile * tbTitCollIn, CFile *tbTitCollOffsetBidIn, CFile *tbTitCollOffsetKlocIn, char *offsetBufferTb950CollBidPtr, char *offsetBufferTb950CollKeylocPtr, long elementsTrTitColl, int OffsetPlusLfLength, int bidKeyLength, int keylocKeyLength);
	Tb950Coll(CFile * tbTitCollIn,
			CFile *tbTitCollOffsetBidIn, CFile *tbTitCollOffsetKlocIn,
			char *offsetBufferTb950CollBidPtr, char *offsetBufferTb950CollKeylocPtr,
			long elementsTb950BidColl, long elementsTb950KeylocColl,
			int bidKeyLength, int keylocKeyLength,
			int OffsetPlusLfLength
			);
	Tb950Coll(Tb950Coll* tb950Coll);
	virtual ~Tb950Coll();

	char * getField(int aFieldId);
	int    getFieldLength(int aFieldId);
	CString * getFieldString(int aFieldId);

	void loadRecord(long offset);

	bool loadNextRecord(); // quando non si conosce il bid
	bool loadNextRecord(char *bid); // cur offset
	bool loadRecord(char *bid);
	bool existsRecord(char *bid);

	bool loadRecordByKeyLoc(char *keyLoc);
	long existsRecordByKeyLoc(char *keyLoc);

	void clearRecord();
	void dumpRecord();

	void stripRecordFieldsTrailing();



};

#endif /* TB950COLL_H_ */
