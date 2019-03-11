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
 * C210ac.h
 *
 *  Created on: 14-feb-2009
 *      Author: Arge
 */
#include "library/tvvector.h"
#include "library/CString.h"


#ifndef C210AC_H_
#define C210AC_H_

class C210ac {
    ATTValVector<CString *> a210vect;
    ATTValVector<CString *> b210vect;
    ATTValVector<CString *> c210vect;

public:
	C210ac();
	virtual ~C210ac();
	void clear();

	void addA210(CString *sPtr);
	void addB210(CString *sPtr);
	void addC210(CString *sPtr);

	ATTValVector<CString *> *getA210Vect();
	ATTValVector<CString *> *getB210Vect();
	ATTValVector<CString *> *getC210Vect();

};

#endif /* C210AC_H_ */
