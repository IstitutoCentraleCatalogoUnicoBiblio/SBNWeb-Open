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


#include "TbAudiovideo.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"
#include "BinarySearch.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);



TbAudiovideo::TbAudiovideo(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length) :
	Tb (tbIn, tbOffsetIn, offsetBufferTbPtr, elementsTb, keyPlusOffsetPlusLfLength, key_length)
{
/*
	if (DATABASE_ID == DATABASE_INDICE)
	{
		tbFields = 35;
		initDereferencing();
	}
	else	// Default SBNWEB
	{
		tbFields = 31;
	}
*/
	tbFields = 35;
	init();
}

void TbAudiovideo::initDereferencing()
{
/*
	if (DATABASE_ID == DATABASE_SBNWEB)
	{
		columnDereferenceAr[bid]=bid_polo;
		columnDereferenceAr[cd_livello]=-1;
		columnDereferenceAr[tp_mater_audiovis]=tp_mater_audiovis_polo;
		columnDereferenceAr[lunghezza]=lunghezza_polo;
		columnDereferenceAr[cd_colore]=cd_colore_polo;
		columnDereferenceAr[cd_suono]=cd_suono_polo;
		columnDereferenceAr[tp_media_suono]=tp_media_suono_polo;
		columnDereferenceAr[cd_dimensione]=cd_dimensione_polo;
		columnDereferenceAr[cd_forma_video]=cd_forma_video_polo;
		columnDereferenceAr[cd_tecnica]=cd_tecnica_polo;
		columnDereferenceAr[tp_formato_film]=tp_formato_film_polo;
		columnDereferenceAr[cd_mat_accomp_1]=cd_mat_accomp_polo;
		columnDereferenceAr[cd_mat_accomp_2]=-1;
		columnDereferenceAr[cd_mat_accomp_3]=-1;
		columnDereferenceAr[cd_mat_accomp_4]=-1;
		columnDereferenceAr[cd_forma_regist]=cd_forma_regist_polo;
		columnDereferenceAr[tp_formato_video]=tp_formato_video_polo;
		columnDereferenceAr[cd_materiale_base]=cd_materiale_base_polo;
		columnDereferenceAr[cd_supporto_second]=cd_supporto_second_polo;
		columnDereferenceAr[cd_broadcast]=cd_broadcast_polo;
		columnDereferenceAr[tp_generazione]=tp_generazione_polo;
		columnDereferenceAr[cd_elementi]=cd_elementi_polo;
		columnDereferenceAr[cd_categ_colore]=cd_categ_colore_polo;
		columnDereferenceAr[cd_polarita]=cd_polarita_polo;
		columnDereferenceAr[cd_pellicola]=cd_pellicola_polo;
		columnDereferenceAr[tp_suono]=tp_suono_polo;
		columnDereferenceAr[tp_stampa_film]=tp_stampa_film_polo;
		columnDereferenceAr[cd_deteriore]=cd_deteriore_polo;
		columnDereferenceAr[cd_completo]=cd_completo_polo;
		columnDereferenceAr[dt_ispezione]=dt_ispezione_polo;
		columnDereferenceAr[ute_ins]=ute_ins_polo;
		columnDereferenceAr[ts_ins]=ts_ins_polo;
		columnDereferenceAr[ute_var]=ute_var_polo;
		columnDereferenceAr[ts_var]=ts_var_polo;
		columnDereferenceAr[fl_canc]=fl_canc_polo;

	    this->tableIsDereferenced = true;
	}
*/
}






TbAudiovideo::~TbAudiovideo() {

}

bool TbAudiovideo::loadRecord(const char *key)
{


	if (!Tb::loadRecord(key))
	{
		SignalAnError(__FILE__, __LINE__, "Derived class TbAudiovideo::loadRecord");
		return false;
	}
	return true;
} // End loadRecord




