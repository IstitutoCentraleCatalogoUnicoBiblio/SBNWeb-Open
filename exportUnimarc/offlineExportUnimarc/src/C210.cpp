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
 * C210.cpp
 *
 *  Created on: 14-feb-2009
 *      Author: Arge
 */

#include "C210.h"

C210::C210() {
}


C210::~C210() {
	clear();
}
void C210::clear() {
    ac210vect.DeleteAndClear();
    d210vect.DeleteAndClear();
    e210vect.DeleteAndClear();
    f210vect.DeleteAndClear();
    g210vect.DeleteAndClear();
    h210vect.DeleteAndClear();
}


void C210::addAC210(C210ac  *acPtr)
{
	ac210vect.Add(acPtr);
}
void C210::addD210(CString *sPtr)
{
	d210vect.Add(sPtr);
}
void C210::addE210(CString *sPtr)
{
	e210vect.Add(sPtr);
}
void C210::addF210(CString *sPtr)
{
	f210vect.Add(sPtr);
}
void C210::addG210(CString *sPtr)
{
	g210vect.Add(sPtr);
}
void C210::addH210(CString *sPtr)
{
	h210vect.Add(sPtr);
}


ATTValVector<C210ac *> *C210::getAC210Vect()
{
	return &ac210vect;
}

ATTValVector<CString *> *C210::getD210Vect(){
	return &d210vect;
}

ATTValVector<CString *> *C210::getE210Vect(){
	return &e210vect;
}

ATTValVector<CString *> *C210::getF210Vect(){
	return &f210vect;
}

ATTValVector<CString *> *C210::getG210Vect(){
	return &g210vect;
}


ATTValVector<CString *> *C210::getH210Vect(){
	return &h210vect;
}

