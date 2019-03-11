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
 * TrTitoloCollocazione.h
 *
 *  Created on: 19-gen-2009
 *      Author: Arge
 */
#include "library/CString.h"


#ifndef TB950ESE_H_
#define TB950ESE_H_
#include "Tb.h"

//#define TB950ESE_FIELDS 5

class Tb950Ese : public Tb{

//	CFile * tb950EseIn;
//	CFile *tb950EseOffsetIn;
//	char *offsetBufferTb950EsePtr;
//	long elementsTb950Ese;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//
//
//	ATTValVector<CString*> *fieldsVector;
//	void init();
//	void stop();

public:
	enum trTitoloCollocazioneFieldId {
		bid,
//		tbinv_key_loc,
//		tbcol_bid_doc,
//		tbcol_cd_doc,
		tbese_cd_doc,

		cd_biblioteca,
		nomeTabella,
//
//		tbinv_cd_bib,
//		tbinv_cd_serie,
//		tbinv_cd_inven,
//		tbinv_flg_disp,
//		tbinv_data_scarico,
//		tbinv_seq_coll,
//		tbinv_precis_inv,

//		tbcol_key_loc,
//		tbcol_cd_sez,
//		tbcol_cd_loc,
//		tbcol_spec_loc,
//		tbcol_consis,
//
//		tbese_bid,
		tbese_cons_doc,

	} ; // tbTitoloFieldId_type

	Tb950Ese(CFile * trTitCollIn, CFile *trTitCollOffsetIn, char *offsetBufferTrTitCollPtr, long elementsTrTitColl, int keyPlusOffsetPlusLfLength, int key_length);
	Tb950Ese(Tb950Ese* tb950Ese);
	virtual ~Tb950Ese();

//	char * getField(int aFieldId);
//	int    getFieldLength(int aFieldId);
//	CString * getFieldString(int aFieldId);
//
//	void loadRecord(long offset);
//	bool loadNextRecord(char *bid); // cur offset
	bool loadRecord(const char *key);
//	bool existsRecord(char *bid);
//
//	void clearRecord();
//	void dumpRecord();




};

#endif /* TB950ESE_H_ */
