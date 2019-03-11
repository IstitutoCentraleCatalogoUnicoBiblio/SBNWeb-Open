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
 * Esemplari950.cpp
 *
 *  Created on: 2-feb-2009
 *      Author: Arge
 */

#include "Esemplari950.h"


Esemplari950::Esemplari950(Tb950Ese *tb950Ese) {

	this->tb950Ese = tb950Ese;
//	this->tb950CollVect = new ATTValVector<Tb950Coll *>();
	collocazione950Vect = new ATTValVector<Collocazione950 *>();

}

Esemplari950::~Esemplari950() {
	stop();
}

void Esemplari950::stop()
{
	delete tb950Ese;
	collocazione950Vect->DeleteAndClear();
	delete collocazione950Vect;
}




//OrsInt Esemplari950::addCollocazione(Tb950Coll *tb950Coll)
//{
//	return this->tb950CollVect->Add(tb950Coll);
//}

OrsInt Esemplari950::addCollocazione(Collocazione950 *collocazione950)
{
	return this->collocazione950Vect->Add(collocazione950);
}




Tb950Ese *Esemplari950::getTb950Ese()
{
	return tb950Ese;
}
/*
ATTValVector<Tb950Coll *> *Esemplari950::getTb950CollVect()
{
	return tb950CollVect;
}
*/
ATTValVector<Collocazione950 *> *Esemplari950::getCollocazione950Vect()
{
	return collocazione950Vect;
}
