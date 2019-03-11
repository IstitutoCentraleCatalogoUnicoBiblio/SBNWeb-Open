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
 * TbSoggetto.cpp
 *
 *  Created on: 21-dic-2008
 *      Author: Arge
 */


#include "TbSoggetto.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"
#include "BinarySearch.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);


TbSoggetto::TbSoggetto(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
//	tbFields = 19;
	if (DATABASE_ID == DATABASE_INDICE)
	{
		tbFields = 12;
		initDereferencing();
	}
	else	// Default SBNWEB
	{
		tbFields = 20; // 19, richiesta Sciamanna 05/06/2012s
	}

	init();
}


void TbSoggetto::initDereferencing()
{
    if (DATABASE_ID == DATABASE_INDICE)
    {
        columnDereferenceAr[cid] = cid_indice;
        columnDereferenceAr[cd_soggettario] = cd_soggettario_indice;
        columnDereferenceAr[ds_soggetto] = ds_soggetto_indice;
        columnDereferenceAr[fl_speciale] = fl_speciale_indice;
        columnDereferenceAr[ky_cles1_s] = ky_cles1_s_indice;
        columnDereferenceAr[ky_primo_descr] = -1;
        columnDereferenceAr[cat_sogg] = -1;
        columnDereferenceAr[cd_livello] = cd_livello_indice;
        columnDereferenceAr[fl_condiviso] = -1;
        columnDereferenceAr[ute_condiviso] = -1;
        columnDereferenceAr[ts_condiviso] = -1;
        columnDereferenceAr[ute_ins] = ute_ins_indice;
        columnDereferenceAr[ts_ins] = ts_ins_indice;
        columnDereferenceAr[ute_var] = ute_var_indice;
        columnDereferenceAr[ts_var] = ts_var_indice;
        columnDereferenceAr[fl_canc] = fl_canc_indice;
        columnDereferenceAr[ky_cles2_s] = ky_cles2_s_indice;
        columnDereferenceAr[tidx_vector] = -1;
        columnDereferenceAr[nota_soggetto] = -1;
        columnDereferenceAr[cd_edizione] = -1;

        this->tableIsDereferenced = true;
    }
}



TbSoggetto::~TbSoggetto() {

}

bool TbSoggetto::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TbSoggetto::loadRecord: Record non trovato per '%s'", key);
		return false;
	}

	// Replace markup characters
	this->getFieldString(this->ds_soggetto)->ChangeTo((char *)"#_", ' ');

//dumpRecord();

	return true;
} // End loadRecord
