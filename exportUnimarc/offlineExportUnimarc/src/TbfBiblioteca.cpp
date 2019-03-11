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
 * TbfBiblioteca.cpp
 *
 *  Created on: 6-mag-2009
 *      Author: Arge
 */


#include "TbfBiblioteca.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"
#include "BinarySearch.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);



TbfBiblioteca::TbfBiblioteca(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
	if (DATABASE_ID == DATABASE_INDICE)
	{
		tbFields = 7;
		initDereferencing();
	}
	else	// Default SBNWEB
	{
		tbFields = 32;
	}
	init();
}

void TbfBiblioteca::initDereferencing()
{
	if (DATABASE_ID == DATABASE_INDICE)
	{
	    columnDereferenceAr[id_biblioteca] = cd_biblioteca_indice;
	    columnDereferenceAr[cd_ana_biblioteca] = cd_ana_biblioteca_indice;
	    columnDereferenceAr[cd_polo] = cd_polo_indice;
	    columnDereferenceAr[cd_bib] = -1;
	    columnDereferenceAr[nom_biblioteca] = ds_biblioteca_indice;
	    columnDereferenceAr[unit_org] = -1;
	    columnDereferenceAr[indirizzo] = -1;
	    columnDereferenceAr[cpostale] = -1;
	    columnDereferenceAr[cap] = -1;
	    columnDereferenceAr[telefono] = -1;
	    columnDereferenceAr[fax] = -1;
	    columnDereferenceAr[note] = -1;
	    columnDereferenceAr[p_iva] = -1;
	    columnDereferenceAr[cd_fiscale] = -1;
	    columnDereferenceAr[e_mail] = -1;
	    columnDereferenceAr[tipo_biblioteca] = -1;
	    columnDereferenceAr[paese] = -1;
	    columnDereferenceAr[provincia] = -1;
	    columnDereferenceAr[cd_bib_cs] = -1;
	    columnDereferenceAr[cd_bib_ut] = -1;
	    columnDereferenceAr[cd_utente] = -1;
	    columnDereferenceAr[flag_bib] = -1;
	    columnDereferenceAr[localita] = ds_citta_indice;
	    columnDereferenceAr[chiave_bib] = -1;
	    columnDereferenceAr[chiave_ente] = -1;
	    columnDereferenceAr[ute_ins] = -1;
	    columnDereferenceAr[ts_ins] = -1;
	    columnDereferenceAr[ute_var] = -1;
	    columnDereferenceAr[ts_var] = -1;
	    columnDereferenceAr[fl_canc] = fl_canc_indice;
	    columnDereferenceAr[tidx_vector_nom_biblioteca] = -1;
	    columnDereferenceAr[tidx_vector_indirizzo] = -1;
	    // ky_biblioteca_indice; non usato

	    this->tableIsDereferenced = true;
	}

}






TbfBiblioteca::~TbfBiblioteca() {

}

bool TbfBiblioteca::loadRecord(const char *key)
{


	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TbfBiblioteca::loadRecord");
		return false;
	}
	return true;
} // End loadRecord




