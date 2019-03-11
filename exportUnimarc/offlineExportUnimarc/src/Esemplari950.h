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
 * Esemplari950.h
 *
 *  Created on: 2-feb-2009
 *      Author: Arge
 */
//#include "Tb950Coll.h"
#include "Tb950Ese.h"
#include "Collocazione950.h"


#ifndef ESEMPLARI950_H_
#define ESEMPLARI950_H_

class Esemplari950 {
	Tb950Ese *tb950Ese;
	ATTValVector<Collocazione950 *> *collocazione950Vect;
	void stop();

public:
	Esemplari950(Tb950Ese *tb950Ese);
	virtual ~Esemplari950();
	OrsInt addCollocazione(Collocazione950 *collocazione950);
	Tb950Ese *getTb950Ese();
	ATTValVector<Collocazione950 *> *getCollocazione950Vect();
};

#endif /* ESEMPLARI950_H_ */
