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
* Module  : CBufferedData                                                   *
* Author  : Argentino Trombin                                               *
* Desc.   :                                                                 *
*                                                                           *
* Date    :                                                                 *
****************************************************************************/

#ifndef CBUFFEREDDATA_H
#define CBUFFEREDDATA_H

#include "ors/Const.h"
#include "library/CBaseClass.h"
#include "library/CString.h"

#define		DEFAULT_BUFFERED_DATA_RESIZE_BY 8192

class CBufferedData : public CBaseClass
	{
	private:
	friend class CBufferedDataIterator;
	// Members
	OrsChar *Buffer;	// Buffer is not circular

	OrsBool GetCharFlag; // Flag used to test if GetChar failed
	OrsBool PeekCharFlag; // Flag used to test if GetChar failed


	OrsLong	BufSize;	// Size of allocated memory for Buffer

	OrsLong  UsedBytes;	// The number of characters (bytes) in use
						// Can be less than BufSize

	OrsLong  UnusedLeftBytes;
						// Bytes unused before first availabale byte

	OrsLong  UnusedRightBytes;
						// Bytes unused from last used byte to last byte in buffer

	OrsLong  Point;		// Point to next available slot position in Buffer

	OrsChar *BufTailPtr;	// Pointer within Buffer. Points to the byte at offset Point

	OrsChar *BufStartPtr;	// Pointer within Buffer where first byte is available


	OrsLong	ResizeBy;
						// Value by which the buffer can be resized
	// Methods

	OrsBool MakeNewBuffer(OrsInt Size);
			// Startup a brand new buffer

	OrsBool GrowBuffer(OrsInt GrowBy);
			// Resize the buffer by a growing factor of GrowBy

	void	AlignLeft();
			// Move valid contents in Buffer all to the left

	OrsBool	MoveRightBy(OrsInt n);
			// Move valid contents in Buffer to n positions to the right

	OrsBool	MoveRightFromBy(OrsInt Pos, OrsInt n);
			// Shift part of used bytes to the right by n positions
	public:


	CBufferedData();
	CBufferedData(OrsChar *aString);
	CBufferedData(long aSize);
	CBufferedData(long aSize, long resizeBy);

	virtual ~CBufferedData();

	// Redefined CBaseClass virtual methods
	//=============================
	virtual OrsInt ClassId() {return CBufferedDataID;};
	virtual const OrsChar* ClassName(){return "CBufferedDataID";};


	OrsBool PrependChar(OrsChar aChar);
						// Will add a string to the START of the buffer
	OrsBool PrependString(OrsChar *aString);
						// Will add a string to the START of the buffer
	OrsBool PrependBinaryData(OrsUChar *aBinaryBuf, OrsInt Size);
						// Will add size bytes to the START of buffer

	OrsBool InsertCharAt(OrsChar aChar, OrsInt Pos);
						// Will insert a char to a given POSITION in the buffer
						// Pos starts at 1 and cannot be greater that the characters
						// cuttently in buffer
						// Insertion will affect only the used part of the buffer
	OrsBool InsertStringAt(const OrsChar *aString, OrsInt Pos);
						// Will insert a string to a given POSITION is the buffer
						// Pos starts at 1 and cannot be grater that the characters
						// cuttently in buffer
						// Insertion will affect only the used part of the buffer
	OrsBool InsertBinaryDataAt(const OrsUChar *aBinaryBuf, OrsInt Size, OrsInt Pos);
						// Will insert a n bytes  at a given POSITION is the buffer
						// Pos starts at 1 and cannot be grater that the characters
						// cuttently in buffer
						// Insertion will affect only the used part of the buffer

	OrsBool AddChar(OrsChar aChar);
						// Will add a character to the END of the buffer
	OrsBool AddString(const OrsChar *aString);
						// Will add a string to the END of the buffer
	OrsBool AddString(CString *sPtr);




	OrsBool AddBinaryData(char *aBinaryBuf, OrsInt Size);
						// Will add size bytes to the END of buffer
	inline OrsBool AddBinary(char *aBinaryBuf, OrsInt Size);


//	OrsChar GetChar();
	OrsUChar GetChar();
			// Will retrieve and remove the first character in the buffer
//	OrsChar GetLastChar();
	OrsUChar GetLastChar();
			// Will retrieve and remove the last character in the buffer
	OrsBool GetCharWasOk();
			// Used to ensure we got a character with call GetChar and
			// that it was not an error

	OrsChar PeekChar();
			// Will retrieve without removing the first available character in the buffer
	OrsBool PeekCharWasOk();
			// Used to ensure we got a character with call GetChar and
			// that it was not an error


	void	Reset();
			// Will clear out and reset things to an empty state
	void 	ResetNoFree();
			// will reset the pointers only keeping the allocated memory
	OrsLong	GetNumOfCharsInBuffer();
			// Will return the number of characters held in the buffer
	OrsLong	SetResizeValueBy(OrsLong aValue);
			// Set the resize value by aValue

	OrsChar *ConstData();
	OrsChar *Data();
			// Return pointer to start of data
			// NMB: Pointer may change at any time

	OrsLong GetUsedBytes();
	OrsLong GetUnusedRightBytes();
	OrsLong GetResizeBy();
	OrsLong GetBufSize();



//	OrsBool operator = (char * aString);
	OrsBool operator = (char * aString);


	}; // End CBufferedData

#endif //CBUFFEREDDATA_H
