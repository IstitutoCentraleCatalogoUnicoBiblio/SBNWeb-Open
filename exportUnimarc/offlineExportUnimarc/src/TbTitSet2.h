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
 * TbTitSet2.h
 *
 *  Created on: 09/03/2015
 *      Author: Arge
 */


#ifndef TBTITSET2_H_
#define TBTITSET2_H_

#include "Tb.h"

class TbTitSet2 : public Tb{

//	void initDereferencing();


public:
	enum fieldId {
		bid,
//		s231_titolo,
//		s231_numero_sezione,
//		s231_nome_sezione,
		s231_forma_opera,
		s231_data_opera,
//		s231_paese_opera, 20/07/17
//		s231_lingua_originale,
		s231_altre_caratteristiche,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};

	TbTitSet2(CFile *TbfBibliotecaIn, CFile *TbfBibliotecaOffsetIn, char *offsetBufferTbfBibliotecaPtr, long elementsTbfBiblioteca, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbTitSet2();
	bool loadRecord(const char *key);
};


#endif /* TBTBTITSET2_H_ */
