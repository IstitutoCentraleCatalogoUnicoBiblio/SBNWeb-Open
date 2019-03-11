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
 * TrTitBib.cpp
 *
 *  Created on: 5-mag-2009
 *      Author: Arge
 */


#include "TrTitBib.h"
#include "BinarySearch.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

TrTitBib::TrTitBib(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{

	if (DATABASE_ID == DATABASE_INDICE)
	{
//		tbFields = 21; // 25-4
//		tbFields = 22; // 01/04/2011

		// 16/02/2018
		if (z899)
			tbFields = 23; // Gestione data di variazione per le biblioteche ecclesisastiche
		else
			tbFields = 22;



		initDereferencing();
//		setRecordSeparator("�"); // 0xC0
//		setRecordSeparator("\xC0"); // 0xC0 01/04/2011 Fottuta conversione a UTF8!!
		setRecordSeparator(0xC0); // 0xC0 25/10/2012

//		printf ("\nSET Record separator len = %d", getRecordSeparator()->Length());

	}
	else	// Default SBNWEB
	{
		tbFields = 26;
	}

	init();

}

TrTitBib::~TrTitBib() {

}

bool TrTitBib::loadRecord(const char *key)
{

	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TrTitBib::loadRecord");
		return false;
	}
	return true;
} // End loadRecord


void TrTitBib::initDereferencing()
{
	if (DATABASE_ID == DATABASE_INDICE)
	{
	    columnDereferenceAr[bid] 			= bid_indice;
	    columnDereferenceAr[cd_polo] 		= cd_polo_indice;
	    columnDereferenceAr[cd_biblioteca] 	= cd_biblioteca_indice;
	    columnDereferenceAr[fl_gestione] 	= fl_gestione_indice;
	    columnDereferenceAr[fl_disp_elettr] = fl_disp_elettr_indice;
	    columnDereferenceAr[fl_allinea] 	= fl_allinea_indice;
	    columnDereferenceAr[fl_allinea_sbnmarc] = fl_allinea_sbnmarc_indice;
	    columnDereferenceAr[fl_allinea_cla] = fl_allinea_cla_indice;
	    columnDereferenceAr[fl_allinea_sog] = fl_allinea_sog_indice;
	    columnDereferenceAr[fl_allinea_luo] = fl_allinea_luo_indice;
	    columnDereferenceAr[fl_allinea_rep] = fl_allinea_rep_indice;
	    columnDereferenceAr[fl_mutilo] 		= fl_mutilo_indice;
	    columnDereferenceAr[ds_consistenza] = ds_consistenza_indice;
	    columnDereferenceAr[fl_possesso] 	= fl_possesso_indice;
	    columnDereferenceAr[ds_fondo] 		= ds_fondo_indice;
	    columnDereferenceAr[ds_segn] 		= ds_segn_indice;
	    columnDereferenceAr[ds_antica_segn] = ds_antica_segn_indice;
	    columnDereferenceAr[nota_tit_bib] 	= nota_tit_bib_indice;
	    columnDereferenceAr[uri_copia] 		= uri_copia_indice;
	    columnDereferenceAr[tp_digitalizz] 	= tp_digitalizz_indice;
	    columnDereferenceAr[ts_ins_prima_coll] = -1;
//	    columnDereferenceAr[ute_ins] 		= ute_ins_indice;
	    columnDereferenceAr[ts_ins] 		= ts_ins_indice;
//	    columnDereferenceAr[ute_var] 		= ute_var_indice;
	    columnDereferenceAr[ts_var] 		= ts_var_indice;
	    columnDereferenceAr[fl_canc] 		= fl_canc_indice;

	    this->tableIsDereferenced = true;
	}
} // End initDereferencing
