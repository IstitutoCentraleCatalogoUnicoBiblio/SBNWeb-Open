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
 * TrTitBib.h
 *
 *  Created on: 5-mag-2009
 *      Author: Arge
 */


#ifndef TRTITBIB_H_
#define TRTITBIB_H_

//#include "library/tvvector.h"
//#include "library/CString.h"
#include "Tb.h"

//#define TR_TIT_BIB_FIELDS 26

class TrTitBib : public Tb {

	void initDereferencing();

public:
	enum fieldId {
		bid,
		cd_polo,
		cd_biblioteca,
		fl_gestione,
		fl_disp_elettr,
		fl_allinea,
		fl_allinea_sbnmarc,
		fl_allinea_cla,
		fl_allinea_sog,
		fl_allinea_luo,
		fl_allinea_rep,
		fl_mutilo,
		ds_consistenza,
		fl_possesso,
		ds_fondo,
		ds_segn,
		ds_antica_segn,
		nota_tit_bib,
		uri_copia,
		tp_digitalizz,
		ts_ins_prima_coll,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};


	enum fieldIdIndice {
		bid_indice,
		cd_polo_indice,
		cd_biblioteca_indice,

		ts_ins_indice,	// 01/04/2011 per gestione statistiche

		fl_mutilo_indice,
		ds_consistenza_indice,
		fl_possesso_indice,
		fl_gestione_indice,
		fl_disp_elettr_indice,
		fl_allinea_indice,
		fl_allinea_sbnmarc_indice,
		fl_allinea_cla_indice,
		fl_allinea_sog_indice,
		fl_allinea_luo_indice,
		fl_allinea_rep_indice,
		ds_fondo_indice,
		ds_segn_indice,
		ds_antica_segn_indice,
		nota_tit_bib_indice,
		uri_copia_indice,
		tp_digitalizz_indice,
//		ute_ins_indice,
//		ts_ins_indice,
//		ute_var_indice,
//		ts_var_indice,
		fl_canc_indice,

		ts_var_indice	// 01/02/2018 Esiste solo per gestione biblioteche ecclesiastiche non PBE
	};





	TrTitBib(CFile *trTitBibIn, CFile *trTitBibOffsetIn, char *offsetBufferTrTitBibPtr, long elementsTrTitBib, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrTitBib();

	bool loadRecord(const char *key);

};

#endif /* TRTITBIB_H_ */
