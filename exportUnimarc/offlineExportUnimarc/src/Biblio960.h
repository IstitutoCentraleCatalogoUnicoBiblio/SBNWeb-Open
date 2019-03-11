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
 * Biblio960.h
 *
 *  Created on: 12-mag-2009
 *      Author: Arge
 */

#ifndef BIBLIO960_H_
#define BIBLIO960_H_


#include "Biblio950.h"

class Biblio960: public Biblio950 {
public:
	Biblio960(char *idBiblioteca, char *descBiblioteca);
	virtual ~Biblio960();
};

#endif /* BIBLIO960_H_ */
