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
 * TbAutore.h
 *
 *  Created on: 21-dic-2008
 *      Author: Arge
 */


#ifndef TBSOGGETTO_H_
#define TBSOGGETTO_H_
#include "Tb.h"

//#include "library/tvvector.h"
//#include "library/CString.h"

//#define TB_SOGGETTO_FIELDS 19

class TbSoggetto : public Tb{
	void initDereferencing();

public:
	enum fieldId {
		cid,
		cd_soggettario,
		ds_soggetto,
		fl_speciale,
		ky_cles1_s,
		ky_primo_descr,
		cat_sogg,
		cd_livello,
		fl_condiviso,
		ute_condiviso,
		ts_condiviso,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		ky_cles2_s,
		tidx_vector,
		nota_soggetto,
		cd_edizione 	// richiesta Sciamanna 05/06/2012
	};

    enum fieldIdIndice { //12
        cid_indice,
        cd_livello_indice,
        cd_soggettario_indice,
        fl_speciale_indice,
        ds_soggetto_indice,
        ky_cles1_s_indice,
        ky_cles2_s_indice,
        ute_ins_indice,
        ts_ins_indice,
        ute_var_indice,
        ts_var_indice,
        fl_canc_indice
    };

	TbSoggetto(CFile *tbSoggettoIn, CFile *tbSoggettoOffsetIn, char *offsetBufferTbSoggettoPtr, long elementsTbSoggetto, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbSoggetto();
	bool loadRecord(const char *key);
};

#endif /* TBSOGGETTO_H_ */
