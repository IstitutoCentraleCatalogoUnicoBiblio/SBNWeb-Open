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
 * TbReticoloTit.h
 *
 *  Created on: 15-feb-2009
 *      Author: Arge
 */


#ifndef TBRETICOLOTIT_H_
#define TBRETICOLOTIT_H_

#include "TbReticolo.h"

#define TBRETICOLO_TIT_FIELDS 6

class TbReticoloTit: public TbReticolo {
public:
	enum tbReticoloFieldId {
		bid,
		tp_legame,
		tp_legame_musica,
		cd_natura_base,
		cd_natura_coll,
		sequenza
	} ; // tbTitoloFieldId_type

	TbReticoloTit(char *record);
	virtual ~TbReticoloTit();
	bool loadRecord(const char *key);
};

#endif /* TBRETICOLOTIT_H_ */
