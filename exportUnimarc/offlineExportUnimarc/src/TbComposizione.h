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
 * TbComposizione.h
 *
 *  Created on: 6-mag-2009
 *      Author: Arge
 */


#ifndef TBCOMPOSIZIONE_H_
#define TBCOMPOSIZIONE_H_

#include "Tb.h"

class TbComposizione: public Tb {
public:

	enum fieldId {
		bid,
		cd_forma_1,
		cd_forma_2,
		cd_forma_3,
		numero_ordine,
		numero_opera,
		numero_cat_tem,
		cd_tonalita,
		datazione,
		aa_comp_1,
		aa_comp_2,
		ds_sezioni,
		ky_ord_ric,
		ky_est_ric,
		ky_app_ric,
		ky_ord_clet,
		ky_est_clet,
		ky_app_clet,
		ky_ord_pre,
		ky_est_pre,
		ky_app_pre,
		ky_ord_den,
		ky_est_den,
		ky_app_den,
		ky_ord_nor_pre,
		ky_est_nor_pre,
		ky_app_nor_pre,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};

	TbComposizione(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbComposizione();
	bool loadRecord(const char *key);
};

#endif /* TBCOMPOSIZIONE_H_ */
