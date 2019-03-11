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
* Module  : CBufferedDataIterator                                           *
* Author  : Argentino Trombin                                               *
* Desc.   :                                                                 *
*                                                                           *
* Date    :                                                                 *
****************************************************************************/

#include "library/CBufferedData.h"
#include "ors/Const.h"


class CBufferedDataIterator
	{
	private:
	// Members
	CBufferedData*	BufferedDataObjectPtr;
					// The object holding the buffered data
	OrsChar *BufPtr;
					// The pointer into BufferedDataObjectPtr->Buffer
	public:

	CBufferedDataIterator(CBufferedData* aBufferedDataObjectPtr);
	CBufferedDataIterator();
	~CBufferedDataIterator();
	void	Reset();
	OrsBool	Setup(CBufferedData* aBufferedDataObjectPtr);

	OrsInt GetChar();
			// Gets the next character in buffer an moves the pointer forward
			// returns 0 on error
	OrsInt UnGetChar(OrsChar aChar);
			// Put a character back in the buffer
	OrsInt PeekChar();
			// Gets the next character in buffer without moving forward
			// returns 0 on error

	}; // End CBufferedDataIterator

