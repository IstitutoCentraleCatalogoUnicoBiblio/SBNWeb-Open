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
 * TbGrafica.h
 *
 *  Created on: 4-mag-2009
 *      Author: Arge
 */


#ifndef TBGRAFICA_H_
#define TBGRAFICA_H_

//#include "library/tvvector.h"
//#include "library/CString.h"
#include "Tb.h"

//#define TB_GRAFICA_FIELDS 17

class TbGrafica : public Tb {
//	CFile* tbGraficaIn;
//	CFile *tbGraficaOffsetIn;
//	char *offsetBufferTbGraficaPtr;
//	long elementsTbGrafica;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//	ATTValVector<CString*> *fieldsVector;
//	void init();
//	void stop();
public:

	enum tbGraficaFieldId {
		bid,
		cd_livello,
		tp_materiale_gra,
		cd_supporto,
		cd_colore,
		cd_tecnica_dis_1,
		cd_tecnica_dis_2,
		cd_tecnica_dis_3,
		cd_tecnica_sta_1,
		cd_tecnica_sta_2,
		cd_tecnica_sta_3,
		cd_design_funz,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	};



	TbGrafica(CFile* tbGraficaIn, CFile *tbGraficaOffsetIn, char *offsetBufferTbTitoloPtr, long elementsTbTitolo, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbGrafica();

	bool loadRecord(const char *key);

};

#endif /* TBGRAFICA_H_ */
