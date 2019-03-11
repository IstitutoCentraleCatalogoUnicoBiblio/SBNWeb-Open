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
 * TbTitSet1.h
 *
 *  Created on: 09/03/2015
 *      Author: Arge
 */


#ifndef TBTITSET1_H_
#define TBTITSET1_H_

#include "Tb.h"

class TbTitSet1 : public Tb{

//	void initDereferencing();


public:
	enum fieldId {
		bid,
		s105_tp_testo_letterario,
		s125_indicatore_testo,
		s140_tp_testo_letterario,
		s181_tp_forma_contenuto_1,
		s181_cd_tipo_contenuto_1,
		s181_cd_movimento_1,
		s181_cd_dimensione_1,
		s181_cd_sensoriale_1_1,
		s181_cd_sensoriale_2_1,
		s181_cd_sensoriale_3_1,
		s181_tp_forma_contenuto_2,
		s181_cd_tipo_contenuto_2,
		s181_cd_movimento_2,
		s181_cd_dimensione_2,
		s181_cd_sensoriale_1_2,
		s181_cd_sensoriale_2_2,
		s181_cd_sensoriale_3_2,
		s182_tp_mediazione_1,
		s182_tp_mediazione_2,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,

		s183_tp_supporto_1, // 12/10/2015
		s183_tp_supporto_2

		};

	TbTitSet1(CFile *TbfBibliotecaIn, CFile *TbfBibliotecaOffsetIn, char *offsetBufferTbfBibliotecaPtr, long elementsTbfBiblioteca, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbTitSet1();
	bool loadRecord(const char *key);
};


#endif /* TBTBTITSET1_H_ */
