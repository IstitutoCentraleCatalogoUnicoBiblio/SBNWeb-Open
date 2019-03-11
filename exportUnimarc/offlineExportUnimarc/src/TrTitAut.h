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
 * TrTitAut.h
 *
 *  Created on: 3-apr-2009
 *      Author: Arge
 */


#ifndef TRTITAUT_H_
#define TRTITAUT_H_

//#include "library/tvvector.h"
//#include "library/CString.h"
#include "Tb.h"

//#define TR_TIT_AUT_FIELDS 16

class TrTitAut : public Tb{
public:
	enum fieldId {
		bid,
		vid,
		tp_responsabilita,
		cd_relazione,
		nota_tit_aut,
		fl_incerto,
		fl_superfluo,
		cd_strumento_mus,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		fl_condiviso,
		ute_condiviso,
		ts_condiviso
		};

	TrTitAut(CFile *TrTitAutIn, CFile *TrTitAutOffsetIn, char *offsetBufferTrTitAutPtr, long elementsTrTitAut, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrTitAut();
	bool loadRecord(const char *key);
};


#endif /* TRTITAUT_H_ */
