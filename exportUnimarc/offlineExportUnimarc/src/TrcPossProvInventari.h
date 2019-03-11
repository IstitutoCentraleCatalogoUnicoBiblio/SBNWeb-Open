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
 * TrcPossProvInventari.h
 *
 *  Created on: 25-gen-2010
 *      Author: Arge
 */


#ifndef TRCPOSSPROVINVENTARI_H_
#define TRCPOSSPROVINVENTARI_H_

#include "Tb.h"

class TrcPossProvInventari: public Tb {

	void getPrimaryKey(CString &pKey);

public:

	enum fieldId {
		pid,
		cd_inven,
		cd_serie,
		cd_biblioteca,
		cd_polo,
		cd_legame,
		nota,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	};

	TrcPossProvInventari(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrcPossProvInventari();
	bool loadRecord(const char *key);
	virtual bool loadNextRecord(const char *key);


};

#endif /* TRCPOSSPROVINVENTARI_H_ */
