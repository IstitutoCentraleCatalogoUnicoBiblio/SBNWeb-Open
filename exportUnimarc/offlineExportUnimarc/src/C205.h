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
#include "library/tvvector.h"
#include "library/CString.h"
/*
 * C205.h
 *
 *  Created on: 13-feb-2009
 *      Author: Arge
 */


#ifndef C205_H_
#define C205_H_

class C205 {
    CString * a205;
    ATTValVector<CString *> b205vect;
    ATTValVector<CString *> d205vect;
    ATTValVector<CString *> f205vect;
    ATTValVector<CString *> g205vect;
    void clear();

public:
	void setA205(CString *sPtr);
	void addB205(CString *sPtr);
	void addD205(CString *sPtr);
	void addF205(CString *sPtr);
	void addG205(CString *sPtr);

	CString *getA205();
	ATTValVector<CString *> *getB205Vect();
	ATTValVector<CString *> *getD205Vect();
	ATTValVector<CString *> *getF205Vect();
	ATTValVector<CString *> *getG205Vect();

	C205();
	virtual ~C205();
};

#endif /* C205_H_ */
