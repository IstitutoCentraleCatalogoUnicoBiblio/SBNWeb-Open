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
 * TrSogDes.cpp
 *
 *  Created on: 4-ago-2009
 *      Author: Arge
 */


#include "TrSogDes.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"

	TrSogDes::TrSogDes(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
		Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
	{
//		tbFields = 9;
//		init();

			if (DATABASE_ID == DATABASE_INDICE)
			{
				tbFields = 8;
				initDereferencing();
			}
			else	// Default SBNWEB
			{
				tbFields = 9;
			}
			init();
	}


TrSogDes::~TrSogDes() {
}

void TrSogDes::initDereferencing()
{
    if (DATABASE_ID == DATABASE_INDICE)
    {
        columnDereferenceAr[did] = did_indice;
        columnDereferenceAr[cid] = cid_indice;
        columnDereferenceAr[fl_primavoce] = fl_primavoce_indice;
        columnDereferenceAr[ute_ins] = ute_ins_indice;
        columnDereferenceAr[ts_ins] = ts_ins_indice;
        columnDereferenceAr[ute_var] = ute_var_indice;
        columnDereferenceAr[ts_var] = ts_var_indice;
        columnDereferenceAr[fl_canc] = fl_canc_indice;

        this->tableIsDereferenced = true;
    }
}

