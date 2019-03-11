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

#ifndef __CATTVALVECTORITERATOR_H__
#define __CATTVALVECTORITERATOR_H__

#include "library/TvVector.h"

template <class T> class CAttValVectorIterator
	{
//	T*			_array;
	ATTValVector <T> *VectorPtr;
	OrsInt CurEntry;
public:
//	CAttValVectorIterator(T aValue);
	CAttValVectorIterator(ATTValVector <T> *aVectorPtr);
	~CAttValVectorIterator() {}
	//char * operator() ();
	T  operator() ();
	void Reset()
		{CurEntry = -1;}
	};


template<class T> CAttValVectorIterator<T>::CAttValVectorIterator(ATTValVector <T> *aVectorPtr)
	{
	VectorPtr = aVectorPtr;
	CurEntry = -1;
	}

template<class T> T CAttValVectorIterator<T>::operator() ()
	{
	CurEntry++;

	if	(CurEntry < VectorPtr->Length())
		return (*VectorPtr)[CurEntry];

	CurEntry--;
	return OrsNULL;
	}

#endif __CATTVALVECTORITERATOR_H__

