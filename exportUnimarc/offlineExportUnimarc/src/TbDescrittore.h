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
 * TbDescrittore.h
 *
 *  Created on: 28-lug-2009
 *      Author: Arge
 */


#ifndef TBDESCRITTORE_H_
#define TBDESCRITTORE_H_

#include "Tb.h"

class TbDescrittore: public Tb {
	void initDereferencing();

public:

	enum fieldId {
		did,
		ds_descrittore,
		ky_norm_descritt,
		nota_descrittore,
		cd_soggettario,
		tp_forma_des,
		cd_livello,
		fl_condiviso,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		tidx_vector,
		cd_edizione, // 07/06/2012
		cat_termine  // 07/06/2012
	};

    enum fieldIdIndice {
		did_indice,
		ds_descrittore_indice,
		ky_norm_descritt_indice,
		tp_forma_des_indice,
		cd_livello_indice,
		cd_soggettario_indice,
		nota_descrittore_indice,
		ute_ins_indice,
		ts_ins_indice,
		ute_var_indice,
		ts_var_indice,
		fl_canc_indice
    };


	TbDescrittore(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbDescrittore();
};

#endif /* TBDESCRITTORE_H_ */
