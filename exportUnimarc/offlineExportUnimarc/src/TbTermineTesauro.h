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
 * TbTermineTesauro.h
 *
 *  Created on: 30-mar-2009
 *      Author: Arge
 */


#ifndef TB_TERMINE_TESAURO_H_
#define TB_TERMINE_TESAURO_H_

#include "Tb.h"


class TbTermineTesauro : public Tb {
public:
	enum tbTermineTesauroFieldId {
		did,
		cd_the,
		ds_termine_thesauro,
		nota_termine_thesauro,
		ky_termine_thesauro,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		tp_forma_the,
		cd_livello,
		fl_condiviso,
		tidx_vector
	} ;

	TbTermineTesauro(CFile* tbTermineTesauroIn, CFile *tbTermineTesauroOffsetIn, char *offsetBufferTbTermineTesauroPtr, long elementsTbTermineTesauro, int keyPlusOffsetPlusLfLength, int key_length);
	TbTermineTesauro();
	virtual ~TbTermineTesauro();
	bool loadRecord(const char *key);

};

#endif /* TB_TERMINE_TESAURO_H_ */
