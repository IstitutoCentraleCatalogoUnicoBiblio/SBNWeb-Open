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
 * TbNota.h
 *
 *  Created on: 12-mag-2009
 *      Author: Arge
 */


#ifndef TBNOTA_H_
#define TBNOTA_H_

#include "Tb.h"

class TbNota: public Tb {
public:
	enum fieldId {
		bid,
		tp_nota,
		progr_nota,
		ds_nota,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};

	TbNota(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbNota();
	bool loadRecord(const char *key);
};

#endif /* TBNOTA_H_ */
