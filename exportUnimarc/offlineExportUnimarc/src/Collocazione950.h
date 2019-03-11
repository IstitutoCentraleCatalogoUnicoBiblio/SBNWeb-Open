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
 * Collocazione950.h
 *
 *	Contiene la collocazione e gli inventari della collcoazione
 *
 *  Created on: 2-feb-2009
 *      Author: Arge
 */
#include "Tb950Coll.h"
#include "Tb950Inv.h"


#ifndef COLLOCAZIONE950_H_
#define COLLOCAZIONE950_H_

class Collocazione950 {
	Tb950Coll *tb950Coll;
	ATTValVector<Tb950Inv *> *tb950InvVect;
	void stop();

public:
	Collocazione950(Tb950Coll *tb950Coll);
	virtual ~Collocazione950();
//	OrsInt addCollocazione(Tb950Coll *tb950Coll);
	OrsInt addInventario(Tb950Inv *tb950Inv);

	ATTValVector<Tb950Inv *> *getTb950InvVect();
	Tb950Coll *getTb950Coll();

};

#endif /* COLLOCAZIONE950_H_ */
