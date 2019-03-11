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
 * C200.h
 *
 *  Created on: 9-feb-2009
 *      Author: Arge
 */


#ifndef C200_H_
#define C200_H_

class C200 {

    ATTValVector<CString *> a200vect;
    ATTValVector<CString *> b200vect;
    ATTValVector<CString *> d200vect;
    ATTValVector<CString *> e200vect;
    ATTValVector<CString *> f200vect;
    ATTValVector<CString *> g200vect;
    ATTValVector<CString *> cf200vect;
    ATTValVector<CString *> h200vect;
    ATTValVector<CString *> i200vect;


public:
	C200();
	virtual ~C200();

	void addA200(CString *sPtr);
	void addB200(CString *sPtr);
	void addD200(CString *sPtr);
	void addE200(CString *sPtr);
	void addF200(CString *sPtr);
	void addG200(CString *sPtr);
	void addCF200(CString *sPtr);
	void addH200(CString *sPtr);
	void addI200(CString *sPtr);

	ATTValVector<CString *> *getA200Vect();
	ATTValVector<CString *> *getB200Vect();
	ATTValVector<CString *> *getD200Vect();
	ATTValVector<CString *> *getE200Vect();
	ATTValVector<CString *> *getF200Vect();
	ATTValVector<CString *> *getG200Vect();
	ATTValVector<CString *> *getCF200Vect();
	ATTValVector<CString *> *getH200Vect();
	ATTValVector<CString *> *getI200Vect();

	void clear();
};

#endif /* C200_H_ */
