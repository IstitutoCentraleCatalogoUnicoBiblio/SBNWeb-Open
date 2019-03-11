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
 * C200.cpp
 *
 *  Created on: 9-feb-2009
 *      Author: Arge
 */


#include "C200.h"

C200::C200() {

}

C200::~C200() {
	clear();
}

void C200::clear() {
    a200vect.DeleteAndClear();
    b200vect.DeleteAndClear();
    d200vect.DeleteAndClear();
    e200vect.DeleteAndClear();
    f200vect.DeleteAndClear();
    g200vect.DeleteAndClear();
    cf200vect.DeleteAndClear();
    h200vect.DeleteAndClear();
    i200vect.DeleteAndClear();
}


void C200::addA200(CString *sPtr)
{
	a200vect.Add(sPtr);
}
void C200::addB200(CString *sPtr)
{
	b200vect.Add(sPtr);
}
void C200::addD200(CString *sPtr)
{
	d200vect.Add(sPtr);
}
void C200::addE200(CString *sPtr)
{
	e200vect.Add(sPtr);
}
void C200::addF200(CString *sPtr)
{
	f200vect.Add(sPtr);
}
void C200::addG200(CString *sPtr)
{
	g200vect.Add(sPtr);
}
void C200::addCF200(CString *sPtr)
{
	cf200vect.Add(sPtr);
}
void C200::addH200(CString *sPtr)
{
	h200vect.Add(sPtr);
}
void C200::addI200(CString *sPtr)
{
	i200vect.Add(sPtr);
}


ATTValVector<CString *> *C200::getA200Vect()
{
	return &a200vect;
}
ATTValVector<CString *> *C200::getB200Vect(){
	return &b200vect;
}

ATTValVector<CString *> *C200::getD200Vect(){
	return &d200vect;
}

ATTValVector<CString *> *C200::getE200Vect(){
	return &e200vect;
}

ATTValVector<CString *> *C200::getF200Vect(){
	return &f200vect;
}

ATTValVector<CString *> *C200::getG200Vect(){
	return &g200vect;
}

ATTValVector<CString *> *C200::getCF200Vect(){
	return &cf200vect;
}

ATTValVector<CString *> *C200::getH200Vect(){
	return &h200vect;
}

ATTValVector<CString *> *C200::getI200Vect(){
	return &i200vect;
}






