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
 * TbImpronta.h
 *
 *  Created on: 21-mar-2009
 *      Author: Arge
 */


#ifndef TBIMPRONTA_H_
#define TBIMPRONTA_H_

#include "Tb.h"

//#include "library/tvvector.h"
//#include "library/CString.h"

//#define TB_IMPRONTA_FIELDS 10

class TbImpronta : public Tb {
public:
//	CFile *tbImprontaIn;
//	CFile *tbImprontaOffsetIn;
//	char *offsetBufferTbImprontaPtr;
//	long elementsTbImpronta;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//	ATTValVector<CString*> *fieldsVector;
//
//	void init(CFile *tbImprontaIn, CFile *tbImprontaOffsetIn, char *offsetBufferTbImprontaPtr, long elementsTbImpronta, int keyPlusOffsetPlusLfLength, int key_length);
//	void stop();
public:
	enum fieldId {
		bid,
		impronta_1,
		impronta_2,
		impronta_3,
		nota_impronta,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	};

	TbImpronta(CFile *tbImprontaIn, CFile *tbImprontaOffsetIn, char *offsetBufferTbImprontaPtr, long elementsTbImpronta, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbImpronta();

	bool loadRecord(const char *key);

};

#endif /* TBIMPRONTA_H_ */
