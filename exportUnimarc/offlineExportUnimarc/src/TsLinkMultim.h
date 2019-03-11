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
 * TsLinkMultim.h
 *
 *  Created on: 17-set-2010
 *      Author: Arge
 */


#ifndef TSLINKMULTIM_H_
#define TSLINKMULTIM_H_

#include "Tb.h"

class TsLinkMultim: public Tb {
//    void initDereferencing();
public:
	enum fieldId {
		id_link_multim,
		ky_link_multim,
		uri_multim,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
//		immagine non gestita
		};
//    enum fieldIdIndice {
//    	id_link_multim_indice,
//    	ky_link_multim_indice,
//    	uri_multim_indice,
//    	immagine_indice,
//    	ute_ins_indice,
//    	ts_ins_indice,
//    	ute_var_indice,
//    	ts_var_indice,
//    	fl_canc_indice
//    };

	TsLinkMultim(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TsLinkMultim();
	bool loadRecord(const char *key);
	bool loadNextRecord(const char *key);
};


#endif /* TSLINKMULTIM_H_ */
