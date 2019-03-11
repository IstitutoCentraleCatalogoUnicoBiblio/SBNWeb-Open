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
 * TrsTerminiTitoliBiblioteche.h
 *
 *  Created on: 33-12-2015
 *      Author: Arge
 */


#ifndef TRS_TERMINI_TITOLI_BIBLIOTECHE
#define TRS_TERMINI_TITOLI_BIBLIOTECHE

#include "Tb.h"


class TrsTerminiTitoliBiblioteche : public Tb {
public:
	enum trsTerminiTitoloBibliotecheFieldId {
		bid,
		cd_biblioteca,
		cd_the,
		cd_polo,
		did,
		nota_termine_titoli_biblioteca,
		ute_ins,
		ts_ins,
		ute_var,
		ts_var,
		fl_canc
	} ;

	TrsTerminiTitoliBiblioteche(CFile* trsTerminiTitoloBibliotecheIn, CFile *trsTerminiTitoloBibliotecheOffsetIn, char *offsetBufferTrsTerminiTitoliBibliotechePtr, long elementsTrsTerminiTitoliBiblioteche, int keyPlusOffsetPlusLfLength, int key_length);
	TrsTerminiTitoliBiblioteche();
	virtual ~TrsTerminiTitoliBiblioteche();
	bool loadRecord(const char *key);

};

#endif /* TRS_TERMINI_TITOLI_BIBLIOTECHE */
