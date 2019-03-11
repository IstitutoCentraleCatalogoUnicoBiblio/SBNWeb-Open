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
/****************************************************************************
* Module  : CBufferedDataIteratorIterator                                           *
* Author  : Argentino Trombin                                               *
* Desc.   :                                                                 *
* Date    :                                                                 *
****************************************************************************/
#include <malloc.h>
#include "library/CBufferedDataIterator.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


//#include "ors\cerr.h"

extern void SignalAnError(	OrsChar *Module,
							OrsInt Line,
							OrsChar * MsgFmt, ...);
#ifndef EOF
	#define EOF -1
#endif

CBufferedDataIterator::CBufferedDataIterator(CBufferedData* aBufferedDataObjectPtr)
	{
	BufferedDataObjectPtr = aBufferedDataObjectPtr;
	Reset();
	} // End CBufferedDataIterator::CBufferedDataIterator
CBufferedDataIterator::CBufferedDataIterator()
	{
	} // End CBufferedDataIterator::CBufferedDataIterator

CBufferedDataIterator::~CBufferedDataIterator()
	{
	} // end CBufferedDataIterator::~CBufferedDataIterator

void	CBufferedDataIterator::Reset()
	{
	BufPtr = BufferedDataObjectPtr->BufStartPtr -1;
	}

/**
*	Returns a character if available and removes it from buffer
**/
OrsInt CBufferedDataIterator::GetChar()
	{
	if	(++BufPtr >= BufferedDataObjectPtr->BufTailPtr)
		return EOF; // No More characters in buffer

	return *BufPtr;
	} // End CBufferedDataIterator::GetChar()

/**
*	Puts a character back in the buffer
**/
OrsInt CBufferedDataIterator::UnGetChar(OrsChar aChar)
	{
	if	(BufPtr >= BufferedDataObjectPtr->BufStartPtr)
		{
		*BufPtr = aChar; // NOT NEEDED REALLY
		return aChar;
		}
	return EOF;
	} // End CBufferedDataIterator::GetChar()


OrsInt CBufferedDataIterator::PeekChar()
	{
	if	(BufPtr >= BufferedDataObjectPtr->BufTailPtr)
		return EOF; // No More characters in buffer

	return *BufPtr;
	} // End CBufferedDataIterator::PeekChar



OrsBool CBufferedDataIterator::Setup(CBufferedData* aBufferedDataObjectPtr)
	{
	BufferedDataObjectPtr = aBufferedDataObjectPtr;
	Reset();
	return OrsTRUE;
	} // End CBufferedDataIterator::Setup





