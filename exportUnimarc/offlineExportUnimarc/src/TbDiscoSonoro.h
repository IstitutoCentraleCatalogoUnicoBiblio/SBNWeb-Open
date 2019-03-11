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
 * TbfDiscoSonoro.h
 *
 *  Created on: 19/02/2015
 *      Author: Argentino
 */


#ifndef TBDISCOSONORO_H_
#define TBDISCOSONORO_H_

#include "Tb.h"

class TbDiscoSonoro : public Tb{

	void initDereferencing();


public:
	enum fieldId {
		bid,
		cd_livello,
		cd_forma,
		cd_velocita,
		tp_suono,
		cd_pista,
		cd_dimensione,
		cd_larg_nastro,
		cd_configurazione,
		cd_mater_accomp_1,
		cd_mater_accomp_2,
		cd_mater_accomp_3,
		cd_mater_accomp_4,
		cd_mater_accomp_5,
		cd_mater_accomp_6,
		cd_tecnica_regis,
		cd_riproduzione,
		tp_disco,
		tp_materiale,
		tp_taglio,
		durata,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	};


	TbDiscoSonoro(CFile *TbDiscoSonoroIn, CFile *TbDiscoSonoroOffsetIn, char *offsetBufferTbDiscoSonoroPtr, long elementsTbDiscoSonoro, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbDiscoSonoro();
	bool loadRecord(const char *key);
};

#endif /* TBDISCOSONORO_H_ */
