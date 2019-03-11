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
 * TbcPossessoreProvenienza.h
 *
 *  Created on: 25-gen-2010
 *      Author: Arge
 */


#ifndef TBCPOSSESSOREPROVENIENZA_H_
#define TBCPOSSESSOREPROVENIENZA_H_

#include "Tb.h"

class TbcPossessoreProvenienza: public Tb {
public:
	enum fieldId {
		pid,
		tp_forma_pp,
		ky_cautun,
		ky_auteur,
		ky_el1,
		ky_el2,
		tp_nome_pp,
		ky_el3,
		ky_el4,
		ky_el5,
		ky_cles1_a,
		ky_cles2_a,
		note,
		tot_inv,
		cd_livello,
		fl_speciale,
		ds_nome_aut,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		tidx_vector_ds_nome_aut
	};

	TbcPossessoreProvenienza(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbcPossessoreProvenienza();
	bool loadRecord(const char *key);

};

#endif /* TBCPOSSESSOREPROVENIENZA_H_ */
