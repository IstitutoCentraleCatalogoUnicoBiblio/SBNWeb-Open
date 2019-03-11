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
 * TbNumeroStandard.h
 *
 *  Created on: 25-dic-2008
 *      Author: Arge
 */


#ifndef TBNUMEROSTANDARD_H_
#define TBNUMEROSTANDARD_H_

#include "Tb.h"
//#define TB_NUMERO_STD_FIELDS 11

class TbNumeroStd : public Tb{
public:
	enum tbTitoloFieldId {
		bid,
		tp_numero_std,	/*	P ACNP
							D BIBLIOGRAFIE STRANIERE
							B BNI
							S BOMS
							C CATALOGHI COLLETTIVI STRANIERI
							R CRP
							U CUBI
							I ISBN
							M ISMN
							J ISSN
							G N. PUBBLICAZIONE GOVERNATIVA
							L NUMERO DI LASTRA
							E NUMERO EDITORIALE
							Y SARTORI */
		numero_std,		// tp_numero_std I, J
		numero_lastra, 	// tp_numero_std L, E
		cd_paese,
		nota_numero_std,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc,
	};


	TbNumeroStd(CFile *tbNumeroStandardIn, CFile *tbNumeroStandardOffsetIn, char *offsetBufferTbNumeroStandardPtr, long elementsTbNumeroStandard, int keyPlusOffsetPlusLfLength, int key_length);
	virtual ~TbNumeroStd();
	bool loadRecord(const char *key);

};

#endif /* TBNUMEROSTANDARD_H_ */
