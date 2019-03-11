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
 * TrBidAltroid.h
 *
 *  Created on: 13-ott-2016
 *      Author: Arge
 */


#ifndef TRBIDALTROID_H_
#define TRBIDALTROID_H_

#include "Tb.h"

class TrBidAltroid: public Tb {
public:
	enum fieldId {
		bid,
		cd_istituzione,
		ist_id,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};

	TrBidAltroid(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrBidAltroid();

};

#endif /* TRBIDALTROID_H_ */
