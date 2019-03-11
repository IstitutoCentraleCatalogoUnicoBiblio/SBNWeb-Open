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
 * BinarySearch.h
 *
 *  Created on: 30-dic-2008
 *      Author: Arge
 */


#ifndef BINARYSEARCH_H_
#define BINARYSEARCH_H_
#include <stdio.h>
#include <string.h>
#include "library/CString.h"
#include "library/CFile.h"

class BinarySearch {
	bool debug;
public:
	BinarySearch();
	virtual ~BinarySearch();
	static bool search(char* offsetMapPtr, long elements, int rowLength, const char *keyToSearch, int keyLength, long& position, char ** entryPtr);
	static bool search(CFile* offsetMapFile, long elements, int rowLength, const char *keyToSearch, int keyLength, long& position, CString* entryFile);


	bool nonStatic_search(char* offsetMapPtr, long elements, int rowLength, const char *keyToSearch, int keyLength, long& position, char ** entryPtr);
	bool nonStatic_search(CFile* offsetMapFile, long elements, int rowLength, const char *keyToSearch, int keyLength, long& position, CString* entryFile);


	void setDebug();
	void unsetDebug();

};

#endif /* BINARYSEARCH_H_ */
