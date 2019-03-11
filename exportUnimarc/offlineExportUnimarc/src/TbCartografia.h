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
 * TbCartografia.h
 *
 *  Created on: 4-mag-2009
 *      Author: Arge
 */


#ifndef TBCARTOGRAFIA_H_
#define TBCARTOGRAFIA_H_

//#include "library/tvvector.h"
//#include "library/CString.h"
#include "Tb.h"

//#define TB_CARTOGRAFIA_FIELDS 27

class TbCartografia : public Tb {
//	CFile* tbCartografiaIn;
//	CFile *tbCartografiaOffsetIn;
//	char *offsetBufferTbCartografiaPtr;
//	long elementsTbCartografia;
//	int keyPlusOffsetPlusLfLength;
//	int key_length;
//
//	ATTValVector<CString*> *fieldsVector;
//	void init();
//	void stop();

public:
	enum tbCartografiaFieldId {
		bid,
		cd_livello,
		tp_pubb_gov,
		cd_colore,
		cd_meridiano,
		cd_supporto_fisico,
		cd_tecnica,
		cd_forma_ripr,
		cd_forma_pubb,
		cd_altitudine,
		cd_tiposcala,
		tp_scala,
		scala_oriz,
		scala_vert,
		longitudine_ovest,
		longitudine_est,
		latitudine_nord,
		latitudine_sud,
		tp_immagine,
		cd_forma_cart,
		cd_piattaforma,
		cd_categ_satellite,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
		tp_proiezione // 10/09/2018 Jira 43/17
	};

	TbCartografia(CFile* tbGraficaIn, CFile *tbGraficaOffsetIn, char *offsetBufferTbTitoloPtr, long elementsTbTitolo, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbCartografia();

	bool loadRecord(const char *key);


};

#endif /* TBCARTOGRAFIA_H_ */
