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
 * TrTitMAr.h
 *
 *  Created on: 5-ott-2009
 *      Author: Arge
 */


#ifndef TRTITMAR_H_
#define TRTITMAR_H_

#include "Tb.h"

class TrTitMar: public Tb {
public:
	enum fieldId {
		bid,
		mid,
		nota_tit_mar,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	};

	TrTitMar(CFile *TrTitMarIn, CFile *TrTitMarOffsetIn, char *offsetBufferTrTitMarPtr, long elementsTrTitMar, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrTitMar();
	bool loadRecord(const char *key);
};

#endif /* TRTITMAR_H_ */
