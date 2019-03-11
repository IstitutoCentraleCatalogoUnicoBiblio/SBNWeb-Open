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
 * C205.cpp
 *
 *  Created on: 13-feb-2009
 *      Author: Arge
 */

#include "C205.h"

C205::C205() {
	a205 = 0;
}


C205::~C205() {
	clear();
}

void C205::clear()
{
	delete a205;
	b205vect.DeleteAndClear();
	d205vect.DeleteAndClear();
	f205vect.DeleteAndClear();
	g205vect.DeleteAndClear();
}

void C205::setA205(CString *sPtr)
{
	a205 = sPtr;
}
void C205::addB205(CString *sPtr)
{
	b205vect.Add(sPtr);
}
void C205::addD205(CString *sPtr)
{
	d205vect.Add(sPtr);
}
void C205::addF205(CString *sPtr)
{
	f205vect.Add(sPtr);
}
void C205::addG205(CString *sPtr)
{
	g205vect.Add(sPtr);
}

CString *C205::getA205()
{
	return a205;
}
ATTValVector<CString *> *C205::getB205Vect(){
	return &b205vect;
}

ATTValVector<CString *> *C205::getD205Vect(){
	return &d205vect;
}

ATTValVector<CString *> *C205::getF205Vect(){
	return &f205vect;
}

ATTValVector<CString *> *C205::getG205Vect(){
	return &g205vect;
}
