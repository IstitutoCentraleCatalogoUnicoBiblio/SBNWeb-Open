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
 * TrAutAut.h
 *
 *  Created on: 8-ott-2009
 *      Author: Arge
 */


#ifndef TRAUTAUT_H_
#define TRAUTAUT_H_

#include "Tb.h"

class TrAutAut: public Tb {

public:

	enum fieldId {
		vid_base,
		vid_coll,
		tp_legame,
		nota_aut_aut,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	};

	TrAutAut(CFile *TrAutAutIn, CFile *TrAutAutOffsetIn, char *offsetBufferTrAutAutPtr, long elementsTrAutAut, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrAutAut();
	bool loadRecord(const char *key);
	bool loadNextRecordDaIndice(const char *key, bool inverted);

};

#endif /* TRAUTAUT_H_ */
