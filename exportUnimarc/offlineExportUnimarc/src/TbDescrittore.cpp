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
 * TbDescrittore.cpp
 *
 *  Created on: 28-lug-2009
 *      Author: Arge
 */


#include "TbDescrittore.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"

TbDescrittore::TbDescrittore(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
//	tbFields = 14;
	if (DATABASE_ID == DATABASE_INDICE)
	{
		tbFields = 12;
		initDereferencing();
	}
	else	// Default SBNWEB
	{
		tbFields = 16; // 14, 07/06/2012
	}

	init();

}

TbDescrittore::~TbDescrittore() {
}


void TbDescrittore::initDereferencing()
{
    if (DATABASE_ID == DATABASE_INDICE)
    {
    	columnDereferenceAr[did] = did_indice;
    	columnDereferenceAr[ds_descrittore] = ds_descrittore_indice;
    	columnDereferenceAr[ky_norm_descritt] = ky_norm_descritt_indice;
    	columnDereferenceAr[nota_descrittore] = nota_descrittore_indice;
    	columnDereferenceAr[cd_soggettario] = cd_soggettario_indice;
    	columnDereferenceAr[tp_forma_des] = tp_forma_des_indice;
    	columnDereferenceAr[cd_livello] =cd_livello_indice ;
    	columnDereferenceAr[fl_condiviso] = -1;
    	columnDereferenceAr[ute_ins] = ute_ins_indice;
    	columnDereferenceAr[ts_ins] = ts_ins_indice;
    	columnDereferenceAr[ute_var] = ute_var_indice;
    	columnDereferenceAr[ts_var] = ts_var_indice;
    	columnDereferenceAr[fl_canc] = fl_canc_indice;
    	columnDereferenceAr[tidx_vector] = -1;

        this->tableIsDereferenced = true;
    }
}
