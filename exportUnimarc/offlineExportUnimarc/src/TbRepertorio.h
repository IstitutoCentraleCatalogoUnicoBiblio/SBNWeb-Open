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
 * TbRepertorio.h
 *
 *  Created on: 23-lug-2009
 *      Author: Arge
 */


#ifndef TBREPERTORIO_H_
#define TBREPERTORIO_H_

#include "Tb.h"

class TbRepertorio: public Tb {
public:

	enum tbRepertorioFieldId {
		id_repertorio,
		cd_sig_repertorio,
		ds_repertorio,
		tp_repertorio,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		tidx_vector
	};

	TbRepertorio(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbRepertorio();
};

#endif /* TBREPERTORIO_H_ */
