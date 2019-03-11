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
 * TbNota300.h
 *
 *  Created on: 13/09/2016
 *      Author: Argentino
 */


#ifndef TBNOTA_300_H_
#define TBNOTA_300_H_

#include "Tb.h"

class TbNota300: public Tb {
public:
	enum fieldId {
		bid,
		ds_nota,
		};

	TbNota300(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbNota300();
	bool loadRecord(const char *key);
};

#endif /* TBNOTA_300_H_ */
