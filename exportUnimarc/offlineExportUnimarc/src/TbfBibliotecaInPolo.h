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
 * TbfBibliotecaInPolo.h
 *
 *  Created on: 25-mag-2009
 *      Author: Arge
 */


#ifndef TBFBIBLIOTECAINPOLO_H_
#define TBFBIBLIOTECAINPOLO_H_

#include "Tb.h"

class TbfBibliotecaInPolo: public Tb {
public:
	enum fieldId {
		cd_biblioteca,
		id_parametro,
		cd_polo,
		id_gruppo_attivita_polo,
		id_biblioteca,
		ky_biblioteca,
		cd_ana_biblioteca,
		ds_biblioteca,
		ds_citta,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
		};

	TbfBibliotecaInPolo(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	TbfBibliotecaInPolo();
	virtual ~TbfBibliotecaInPolo();
	bool loadRecord(const char *key);
};

#endif /* TBFBIBLIOTECAINPOLO_H_ */
