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
 * TbLuogo.h
 *
 *  Created on: 30-mar-2009
 *      Author: Arge
 */


#ifndef TBLUOGO_H_
#define TBLUOGO_H_

//#include "library/tvvector.h"
//#include "library/CString.h"
#include "Tb.h"

//#define TB_LUOGO_FIELDS 13

class TbLuogo : public Tb {
//	CFile* tbLuogoIn;
//	CFile *tbLuogoOffsetIn;
//	char *offsetBufferTbLuogoPtr;
//	long elementsTbLuogo;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//	ATTValVector<CString*> *fieldsVector;
//	void init();
//	void stop();
public:
	enum tbLuogoFieldId {
		lid,
		tp_forma,
		cd_livello,
		ds_luogo,
		ky_luogo,
		ky_norm_luogo,
		cd_paese,
		nota_luogo,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		nota_catalogatore
	} ; // tbLuogoFieldId_type

	TbLuogo(CFile* tbLuogoIn, CFile *tbLuogoOffsetIn, char *offsetBufferTbLuogoPtr, long elementsTbLuogo, int keyPlusOffsetPlusLfLength, int key_length);
	TbLuogo();
	virtual ~TbLuogo();
	bool loadRecord(const char *key);

};

#endif /* TBLUOGO_H_ */
