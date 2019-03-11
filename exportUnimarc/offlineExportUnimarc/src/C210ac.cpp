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
 * C210ac.cpp
 *
 *  Created on: 14-feb-2009
 *      Author: Arge
 */

#include "C210ac.h"

C210ac::C210ac() {

}


C210ac::~C210ac() {
	clear();
}

void C210ac::clear()
{
	a210vect.DeleteAndClear();
	b210vect.DeleteAndClear();
	c210vect.DeleteAndClear();
}

void C210ac::addA210(CString *sPtr)
{
	a210vect.Add(sPtr);
}
void C210ac::addB210(CString *sPtr)
{
	b210vect.Add(sPtr);
}
void C210ac::addC210(CString *sPtr)
{
	c210vect.Add(sPtr);
}

ATTValVector<CString *> *C210ac::getA210Vect()
{
	return &a210vect;
}
ATTValVector<CString *> *C210ac::getB210Vect()
{
	return &b210vect;
}
ATTValVector<CString *> *C210ac::getC210Vect()
{
	return &c210vect;
}
