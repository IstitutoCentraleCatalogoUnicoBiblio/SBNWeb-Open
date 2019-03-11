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
 * TrTitTit.h
 *
 *  Created on: 8-mar-2009
 *      Author: Arge
 */


#ifndef TRTITTIT_H_
#define TRTITTIT_H_


#include "Tb.h"

//#define TR_TIT_TIT_FIELDS 37

class TrTitTit : public Tb{
public:
	enum fieldId {
		bid_base,
		bid_coll,
		tp_legame,
		tp_legame_musica,
		cd_natura_base,
		cd_natura_coll,
		sequenza,
		nota_tit_tit,
		sequenza_musica,
		sici,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		fl_condiviso,
		ute_condiviso,
		ts_condiviso
	};

	TrTitTit(CFile *TrTitTitIn, CFile *TrTitTitOffsetIn, char *offsetBufferTrTitTitPtr, long elementsTrTitTit, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrTitTit();
	bool loadRecord(const char *key);

};
#endif /* TRTITTIT_H_ */
