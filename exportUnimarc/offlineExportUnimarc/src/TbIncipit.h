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
 * TbIncipit.h
 *
 *  Created on: 6-mag-2009
 *      Author: Arge
 */


#ifndef TBINCIPIT_H_
#define TBINCIPIT_H_

#include "Tb.h"

class TbIncipit: public Tb {
    void initDereferencing();
public:
	enum fieldId {
		bid,
		numero_mov,
		numero_p_mov,
		bid_letterario,
		tp_indicatore,
		numero_comp,
		registro_mus,
		nome_personaggio,
		tempo_mus,
		cd_forma,
		cd_tonalita,
		chiave_mus,
		alterazione,
		misura,
		ds_contesto,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};
    enum fieldIdIndice {
        bid_indice,
        numero_mov_indice,
        bid_letterario_indice,
        tp_indicatore_indice,
        numero_comp_indice,
        numero_p_mov_indice,
        registro_mus_indice,
        nome_personaggio_indice,
        tempo_mus_indice,
        cd_forma_indice,
        cd_tonalita_indice,
        chiave_mus_indice,
        alterazione_indice,
        misura_indice,
        ds_contesto_indice,
        ute_ins_indice,
        ts_ins_indice,
        ute_var_indice,
        ts_var_indice,
        fl_canc_indice
    };

	TbIncipit(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbIncipit();
	bool loadRecord(const char *key);

};

#endif /* TBINCIPIT_H_ */
