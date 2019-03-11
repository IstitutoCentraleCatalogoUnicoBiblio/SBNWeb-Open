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
 * TbIncipit.cpp
 *
 *  Created on: 6-mag-2009
 *      Author: Arge
 */


#include "TbIncipit.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"
#include "BinarySearch.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif






extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

TbIncipit::TbIncipit(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	tbFields = 20;

	if (DATABASE_ID == DATABASE_INDICE)
	{
		initDereferencing();
	}

	init();
}

void TbIncipit::initDereferencing()
{
    if (DATABASE_ID == DATABASE_INDICE)
    {
        columnDereferenceAr[bid] = bid_indice;
        columnDereferenceAr[numero_mov] = numero_mov_indice;
        columnDereferenceAr[numero_p_mov] = numero_p_mov_indice;
        columnDereferenceAr[bid_letterario] = bid_letterario_indice;
        columnDereferenceAr[tp_indicatore] = tp_indicatore_indice;
        columnDereferenceAr[numero_comp] = numero_comp_indice    ;
        columnDereferenceAr[registro_mus] = registro_mus_indice;
        columnDereferenceAr[nome_personaggio] = nome_personaggio_indice;
        columnDereferenceAr[tempo_mus] = tempo_mus_indice;
        columnDereferenceAr[cd_forma] = cd_forma_indice;
        columnDereferenceAr[cd_tonalita] = cd_tonalita_indice;
        columnDereferenceAr[chiave_mus] = chiave_mus_indice;
        columnDereferenceAr[alterazione] = alterazione_indice;
        columnDereferenceAr[misura] = misura_indice;
        columnDereferenceAr[ds_contesto] = ds_contesto_indice;
        columnDereferenceAr[ute_ins] = ute_ins_indice;
        columnDereferenceAr[ts_ins] = ts_ins_indice;
        columnDereferenceAr[ute_var] = ute_var_indice;
        columnDereferenceAr[ts_var] = ts_var_indice;
        columnDereferenceAr[fl_canc] = fl_canc_indice;

        this->tableIsDereferenced = true;
    }
}

TbIncipit::~TbIncipit() {

}

bool TbIncipit::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TbIncipit::loadRecord");
		return false;
	}
	return true;
} // End loadRecord
