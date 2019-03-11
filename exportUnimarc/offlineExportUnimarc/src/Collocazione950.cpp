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
 * Collocazione950.cpp
 *
 *  Created on: 2-feb-2009
 *      Author: Arge
 */


#include "Collocazione950.h"

Collocazione950::Collocazione950(Tb950Coll *tb950Coll) {
	this->tb950Coll = tb950Coll;
	this->tb950InvVect = new ATTValVector<Tb950Inv *>();

}

Collocazione950::~Collocazione950() {
	stop();
}
void Collocazione950::Collocazione950::stop()
{
	delete tb950Coll;
	tb950InvVect->DeleteAndClear();
	delete tb950InvVect;

}



OrsInt Collocazione950::addInventario(Tb950Inv *tb950Inv)
{
//	printf("Argepf Collocazione950::addInventario");
//	tb950Inv->dumpRecord();
//	printf("Argepf end Collocazione950::addInventario");

	return this->tb950InvVect->Add(tb950Inv);
}

//OrsInt Collocazione950::addCollocazione(Tb950Coll *tb950Coll)
//{
//	return this->tb950Coll = tb950Coll;
//
//}

ATTValVector<Tb950Inv *> *Collocazione950::getTb950InvVect()
{
	return 	tb950InvVect;
}

Tb950Coll *Collocazione950::getTb950Coll()
{
	return tb950Coll;
}
