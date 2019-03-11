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
 * TbMusica.h
 *
 *  Created on: 4-mag-2009
 *      Author: Arge
 */


#ifndef TBMUSICA_H_
#define TBMUSICA_H_

#include "Tb.h"

//#define TB_MUSICA_FIELDS 21

class TbMusica  : public Tb{



public:
	enum tbMusicaFieldId {
		bid,
		cd_livello,
		ds_org_sint,
		ds_org_anal,
		tp_elaborazione,
		cd_stesura,
		fl_composito,
		fl_palinsesto,
		datazione,
		cd_presentazione,
		cd_materia,
		ds_illustrazioni,
		notazione_musicale,
		ds_legatura,
		ds_conservazione,
		tp_testo_letter,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	};

	TbMusica(CFile* tbMusicaIn, CFile *tbMusicaOffsetIn, char *offsetBufferTbMusicaPtr, long elementsTbMusica, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbMusica();

	bool loadRecord(const char *key);
};

#endif /* TBMUSICA_H_ */
