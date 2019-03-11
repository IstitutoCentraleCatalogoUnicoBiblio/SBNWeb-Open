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
 * TbMarca.h
 *
 *  Created on: 27-feb-2009
 *      Author: Arge
 */


#ifndef TBMARCA_H_
#define TBMARCA_H_
#include "Tb.h"

//#include "library/tvvector.h"
//#include "library/CString.h"

//#define TB_MARCA_FIELDS 15

class TbMarca : public Tb{
//	CFile *tbMarcaIn;
//	CFile *tbMarcaOffsetIn;
//	char *offsetBufferTbMarcaPtr;
//	long elementsTbMarca;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//	ATTValVector<CString*> *fieldsVector;
//
//	void init(CFile *tbMarcaIn, CFile *tbMarcaOffsetIn, char *offsetBufferTbMarcaPtr, long elementsTbMarca, int keyPlusOffsetPlusLfLength, int key_length);
public:
	enum fieldId {
		mid,
		cd_livello,
		fl_speciale,
		ds_marca,
		nota_marca,
		ds_motto,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		fl_condiviso,
		ute_condiviso,
		ts_condiviso,
		tidx_vector
	};

	TbMarca(CFile *tbMarcaIn, CFile *tbMarcaOffsetIn, char *offsetBufferTbMarcaPtr, long elementsTbMarca, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbMarca();
	bool loadRecord(const char *key);
};

#endif /* TBMARCA_H_ */
