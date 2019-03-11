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
 * TbRappresentazione.h
 *
 *  Created on: 6-mag-2009
 *      Author: Arge
 */


#ifndef TBRAPPRESENTAZIONE_H_
#define TBRAPPRESENTAZIONE_H_

#include "Tb.h"

class TbRappresent: public Tb {

public:
	enum fieldId {
		bid,
		tp_genere,
		aa_rapp,
		ds_periodo,
		ds_teatro,
		ds_luogo_rapp,
		ds_occasione,
		nota_rapp,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};

	TbRappresent(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbRappresent();
	bool loadRecord(const char *key);
};

#endif /* TBRAPPRESENTAZIONE_H_ */
