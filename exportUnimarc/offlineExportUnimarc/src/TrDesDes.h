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
 * TrDesDes.h
 *
 *  Created on: 6-ago-2009
 *      Author: Arge
 */


#ifndef TRDESDES_H_
#define TRDESDES_H_

#include "Tb.h"

class TrDesDes: public Tb {
public:
	enum fieldId {
		did_base,
		did_coll,
		tp_legame,
		nota_des_des,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};

	TrDesDes(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrDesDes();
};

#endif /* TRDESDES_H_ */
