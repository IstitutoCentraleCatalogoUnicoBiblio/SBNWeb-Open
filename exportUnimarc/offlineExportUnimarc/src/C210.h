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
#include "C210ac.h"
/*
 * C210.h
 *
 *  Created on: 14-feb-2009
 *      Author: Arge
 */

#ifndef C210_H_
#define C210_H_


class C210 {
    ATTValVector<C210ac *> ac210vect;
    ATTValVector<CString *> d210vect;
    ATTValVector<CString *> e210vect;
    ATTValVector<CString *> f210vect;
    ATTValVector<CString *> g210vect;
    ATTValVector<CString *> h210vect;

public:
	C210();
	virtual ~C210();

	void addAC210(C210ac  *acPtr);
	void addD210(CString *sPtr);
	void addE210(CString *sPtr);
	void addF210(CString *sPtr);
	void addG210(CString *sPtr);
	void addH210(CString *sPtr);

	ATTValVector<C210ac*> *getAC210Vect();
	ATTValVector<CString *> *getD210Vect();
	ATTValVector<CString *> *getE210Vect();
	ATTValVector<CString *> *getF210Vect();
	ATTValVector<CString *> *getG210Vect();
	ATTValVector<CString *> *getH210Vect();

	void clear();




};

#endif /* C210_H_ */
