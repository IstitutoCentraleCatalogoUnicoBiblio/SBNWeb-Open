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
 * TrTitAutRel.h
 *
 *  Created on: 28-ott-2009
 *      Author: Arge
 */


#ifndef TRTITAUTREL_H_
#define TRTITAUTREL_H_

#include "TrRel.h"

class TrTitAutRel: public TrRel {
public:
	enum fieldId {
		targetId,
		tpResponsabilita,
		cdRelazione,
		flIncerto,
		flSuperfluo
		};
	TrTitAutRel(CFile *tbIn, CFile *tbOffsetIn, char *offsetBufferTbPtr, long elementsTb, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TrTitAutRel();

};

#endif /* TRTITAUTREL_H_ */
