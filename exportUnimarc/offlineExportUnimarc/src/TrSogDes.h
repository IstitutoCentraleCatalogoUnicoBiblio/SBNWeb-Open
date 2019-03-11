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
 * TrSogDes.h
 *
 *  Created on: 4-ago-2009
 *      Author: Arge
 */


#ifndef TRSOGDES_H_
#define TRSOGDES_H_

#include "Tb.h"

class TrSogDes: public Tb {
	void initDereferencing();

public:
	enum fieldId {
		did,
		cid,
		fl_posizione,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		fl_primavoce
	};

	enum fieldIdIndice {
        cid_indice,
        did_indice,
        fl_primavoce_indice,
        ute_ins_indice,
        ts_ins_indice,
        ute_var_indice,
        ts_var_indice,
        fl_canc_indice
    };

	TrSogDes(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrSogDes();
};

#endif /* TRSOGDES_H_ */
