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
 * Biblio950.h
 *
 * Contiene le 950 specifiche di una singola biblioteca
 *
 *
 *  Created on: 2-feb-2009
 *      Author: Arge
 */


#include "Tb950Inv.h"
#include "Tb950Coll.h"
#include "Tb950Ese.h"
#include "library/tvvector.h"
#include "library/CKeyValueVector.h"
#include "library/CString.h"
#include "Collocazione950.h"
#include "Esemplari950.h"


#ifndef BIBLIO950_H_
#define BIBLIO950_H_

#define KEY_LOC_SIZE 9

class Biblio950 {

	CString idBiblioteca;
	//bool addEsemplare();
	CString descBiblioteca;

	// Inventari non collocati
	ATTValVector<Tb950Inv *> *tb950InvNonCollocatiVect;
	ATTValVector<Tb950Inv *> *tb950InvCollocatiVect; // 27/02/2015

	// COLLOCAZIONI - senza inventario
//	CKeyValueVector *collocazione950SenzaInventariVectKV; // bidColl/collocazione950

	// COLLOCAZIONI - senza esemplare
//	CKeyValueVector *collocazione950SenzaEsemplareVectKV; // bidcoll/collocazione950

	// COLLOCAZIONI - con o senza inventario ed esemplare
	CKeyValueVector *collocazione950AnyVectKV; // da sostituire con tb950CollocazioniVect

	// ESEMPLARI - collocazioni in Esemplari
	CKeyValueVector *esemplare950VectKV; // bidEse/Esemplari950


//	Esemplari950 *findEsemplare950(CString &esemplare);
	Collocazione950 *findCollocazione950(char *keyLoc); // char *bidColl
	void stop();

public:

	Biblio950(const char *idBiblioteca, const char *descBiblioteca);
	virtual ~Biblio950();

	char *findEsemplare950(CString &esemplare);

//	bool addInventario(Tb950Inv *Tb950Inv, Tb950Coll *tb950Coll, Tb950Ese *tb950Ese);
	bool addInventario_coll_noColl(Tb950Inv *tb950Inv, Tb950Coll *tb950Coll, Tb950Ese *tb950Ese);
	void addEsemplare(char *key, char *value);



//	bool addCollocazione(Tb950Coll *tb950Coll, Tb950Ese *tb950Ese);
	bool addCollocazione_any(Tb950Coll *tb950Coll, Tb950Ese *tb950Ese);

	Esemplari950 *creaEsemplare950(Tb950Ese *tb950Ese, const char *bidDoc, const char *cdDoc, const char *cdBib, const char *idEsemplare);

	char *getDescBiblioteca();
	char *getIdBiblioteca();

	ATTValVector<Tb950Inv *> *getTb950InvNonCollocatiVect();
	ATTValVector<Tb950Inv *> *getTb950InvCollocatiVect();
//	CKeyValueVector *getCollocazione950SenzaInventariVectKV();
//	CKeyValueVector *getCollocazione950SenzaEsemplareVectKV();
	CKeyValueVector *getCollocazione950VectKV();
	CKeyValueVector *getEsemplare950VectKV();

	bool isInvCollocato(char *keyLoc);

	void dump();
};

#endif /* BIBLIO950_H_ */
