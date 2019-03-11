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
* Module  : CBufferedData                                                  *
* Author  : Argentino Trombin                                               *
* Desc.   : Handles Bufferinf                                           *
* Date    :                                                                 *
* NOTE    : Strings handled in another class                                *
*         :                                                                 *
****************************************************************************/
#include "library/CBufferedData.h"
#include "library/macros.h"
#include <stdlib.h>
#include <malloc.h>
#include <memory.h>
#include <string.h>
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

// INLINE FUNCTIONS
inline OrsBool CBufferedData::AddBinary(char *aBinaryBuf, OrsInt Size)
	{
	if (!Size)
		return true; // Non far niente se non ci sono dati

	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(Size))
			return OrsFALSE;
		}

	// Abbiamo ancora spazio nel buffer?
	if	(!UnusedRightBytes || UnusedRightBytes < Size)
		{
		if	(!GrowBuffer(Size))
			return OrsFALSE;
		}

	memcpy(BufTailPtr, aBinaryBuf, Size);
	UnusedRightBytes -= Size;
	UsedBytes += Size;
	Point += Size;
	BufTailPtr += Size;
	return OrsTRUE;
	} // End CBufferedData::AddBinaryData

// END INLINE FUNCTIONS



// Will add size bytes to the START of buffer
OrsBool CBufferedData::PrependBinaryData(OrsUChar *aBinaryBuf, OrsInt Size)
	{
	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(Size))
			return OrsFALSE;
		}

	// Abbiamo spazio in fronte?
	if	(!UnusedLeftBytes)
		{
		// Prepara lo spazio in fronte
		// Abbiamo spazio in coda?
		if	(UnusedRightBytes >= Size)
			{
			// OK. spostiamo il buffer a destra di un byte
			if	(!MoveRightBy(Size))
				return OrsFALSE;
			}
		else
			{
			// Mon abbiamo spazio ne' a destra ne' a sinistra
			if	(!GrowBuffer(Size))
				return OrsFALSE;
			// Align the buffer right by one position
			if	(!MoveRightBy(Size))
				return OrsFALSE;
			}
		}

	if	(UnusedLeftBytes) // Check if at start of buffer
		{
		BufStartPtr-=Size;
		UnusedLeftBytes-=Size;
		}
	else
		{
		UnusedRightBytes-=Size;
		BufTailPtr+=Size;
		Point+=Size;
		}

	memcpy(BufStartPtr, aBinaryBuf, Size);

	UsedBytes+=Size;
	return OrsTRUE;
	} // End CBufferedData::PrependBinaryData


OrsBool CBufferedData::PrependString(OrsChar *aString)
	{
	return PrependBinaryData((OrsUChar *)aString, strlen(aString));
	} // End CBufferedData::PrependString




/**
 Will insert a char to a given POSITION is the buffer
 Pos starts at 1 and cannot be grater that the characters
 cuttently in buffer
 Insertion will affect only the used part of the buffer

 Can insert only within some data or at end of data

**/
OrsBool CBufferedData::InsertCharAt(OrsChar aChar, OrsInt Pos)
	{
	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		}
	// Siamo in range?
	if	(Pos < 0 || Pos > (UsedBytes+1))
		{
	    SignalAnError(__FILE__, __LINE__, "InsertCharAt: out of range");
		return OrsFALSE;
		}
	// Insert in coda?
//	if	(Pos == (UsedBytes+1))
	if	(Pos == Point)
		{
		return (AddChar(aChar));
		}

	// Abbiamo spazio a destra?
	if	(!UnusedRightBytes)
		{
		if	(!GrowBuffer(ResizeBy))
			return OrsFALSE;
		}

	// OK abbiamo abbastanzaspazio. spostiamo il buffer a destra di un byte
	// a partire da Pos
	if	(!MoveRightFromBy(Pos, 1))
		return OrsFALSE;

	Pos--;
	*(BufStartPtr+Pos) = aChar;
	return OrsTRUE;
	} // End CBufferedData::InsertCharAt


OrsBool CBufferedData::PrependChar(OrsChar aChar)
	{
	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		}

	// Abbiamo spazio in fronte?
	if	(!UnusedLeftBytes)
		{
		// Prepara lo spazio in fronte
		// Abbiamo spazio in coda?
		if	(UnusedRightBytes)
			{
			// OK. spostiamo il buffer a destra di un byte
			if	(!MoveRightBy(1))
				return OrsFALSE;
			}
		else
			{
			// Mon abbiamo spazio ne' a destra ne' a sinistra
			if	(!GrowBuffer(ResizeBy))
				return OrsFALSE;
			// Align the buffer right by one position
			if	(!MoveRightBy(1))
				return OrsFALSE;
			}
		}
	if	(UnusedLeftBytes) // Check if at start of buffer
		{
		BufStartPtr--;
		UnusedLeftBytes--;
		}
	else
		{
		UnusedRightBytes--;
		BufTailPtr++;
		Point++;
		}
	*BufStartPtr = aChar;
	UsedBytes++;
	return OrsTRUE;
	} // End CBufferedData::PrependChar


/**
*	Shift all used bytes to the right by n
**/
OrsBool	CBufferedData::MoveRightBy(OrsInt n)
	{
	// In range?
	if	(n < 0)
		{
	    SignalAnError(__FILE__, __LINE__, "MoveRightBy: n out of range");
		return OrsFALSE;
		}

	// Enough room on the right to allow move?
	if	(n > UnusedRightBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "MoveRightBy: free space on the right non enough");
		return OrsFALSE;
		}

	// Something to move?
	if	(!UsedBytes)
		{
//	    SignalAnError(__FILE__, __LINE__, "MoveRightBy: Buffer holds no data");
//		return OrsFALSE;
		return OrsTRUE;
		}

	// Move to the right
	memmove (BufStartPtr+n, BufStartPtr, UsedBytes);

	Point += n;
	BufStartPtr += n;
	BufTailPtr += n;

	UnusedRightBytes -= n;
	UnusedLeftBytes += n;
	return OrsTRUE;
	} // End CBufferedData::MoveRightBy

/**
*	Shift part of used bytes to the right by n posistions
*	Pos starts at 1
**/
OrsBool	CBufferedData::MoveRightFromBy(OrsInt Pos, OrsInt n)
	{
	// Something to move?
	if	(!UsedBytes)
		{
		return OrsFALSE;
		}

	// In range?
	if	(n < 0 || Pos > UsedBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "MoveRightBy: n out of range");
		return OrsFALSE;
		}

	// Enough room on the right to allow move?
	if	(n > UnusedRightBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "MoveRightBy: free space on the right non enough");
		return OrsFALSE;
		}


	// Move to the right
	Pos--;
	memmove (BufStartPtr+Pos+n, BufStartPtr+Pos, UsedBytes);

	Point += n;
	BufTailPtr += n;
	UsedBytes+=n;

	UnusedRightBytes -= n;
	return OrsTRUE;
	} // End CBufferedData::MoveRightFromBy

OrsChar CBufferedData::PeekChar()
	{
	PeekCharFlag = OrsTRUE;
	if	(!UsedBytes)
		{
		PeekCharFlag = OrsFALSE;
		return 0;
		}
	return *BufStartPtr;
	}

OrsBool CBufferedData::PeekCharWasOk()
	{
	return PeekCharFlag;
	}


/**
*	Shift all used bytes to the left
**/
void	CBufferedData::AlignLeft()
	{
	// Something to move?
	if	(!UnusedLeftBytes)
		return;

	// Move left
	memmove (Buffer, BufTailPtr, UsedBytes);

	Point -= UnusedLeftBytes;
	BufTailPtr -= UnusedLeftBytes;
	BufStartPtr = Buffer;
	UnusedRightBytes += UnusedLeftBytes;
	UnusedLeftBytes = 0;

	} // End CBufferedData::AlignLeft


// Will return the number of characters held in the buffer
OrsLong	CBufferedData::GetNumOfCharsInBuffer()
	{
	return UsedBytes;
	}


/**
*	Change the buffer ResizeBy value
**/
OrsLong CBufferedData::SetResizeValueBy(OrsLong aValue)
	{
	if	(aValue < 0)
		return 0;

	ResizeBy = aValue;
	return ResizeBy;
	} // End

/**
*	Returns a character if available and removes it from buffer
**/
//OrsChar CBufferedData::GetChar()
OrsUChar CBufferedData::GetChar()
	{
//	OrsChar chr;
	OrsUChar chr;

	GetCharFlag = OrsTRUE;
	if	(!UsedBytes)
		{
		GetCharFlag = OrsFALSE;
		return 0;
		}
	chr = *BufStartPtr;

	BufStartPtr++;
	UsedBytes--;
	UnusedLeftBytes++;
	return chr;
	} // End CBufferedData::GetChar()


OrsBool CBufferedData::GetCharWasOk()
	{
	return GetCharFlag;
	}




OrsBool CBufferedData::MakeNewBuffer(OrsInt Size)
	{
	Buffer = (OrsChar *)malloc (Size);
	if	(!Buffer)
		{
	    SignalAnError(__FILE__, __LINE__, "CBufferedData calloc failed");
		return OrsFALSE;
		}
	BufSize = Size;
	UsedBytes = 0;
	UnusedLeftBytes = 0;
	UnusedRightBytes=Size;
	Point = 0;
	BufTailPtr = Buffer;
	BufStartPtr = Buffer;
	return OrsTRUE;
	} // End CBufferedData::MakeNewBuffer




OrsBool CBufferedData::GrowBuffer(OrsInt GrowBy)
	{
	// OrsInt Size = _msize(Buffer) + GrowBy; // WINDOWS ONLY
//	OrsInt Size = malloc_usable_size(Buffer) + GrowBy;
	OrsInt Size = BufSize + GrowBy;

	OrsChar *OrgBufPtr = Buffer;
	OrsChar *ReallocatedBufPtr;

	ReallocatedBufPtr = (OrsChar *) realloc(Buffer, Size);
	if	(!ReallocatedBufPtr)
		{
	    SignalAnError(__FILE__, __LINE__, "CBufferedData realloc failed, oldSize=%d, newSize=%d", BufSize, Size);
		return OrsFALSE;
		}
	// Now the Buffer address may be changed
	if	(OrgBufPtr != ReallocatedBufPtr)
		{
		// The buffer has been moved
		// Reposition pointers
		Buffer = ReallocatedBufPtr;
		BufTailPtr = Buffer + Point;
		BufStartPtr = Buffer + UnusedLeftBytes;
		}
	UnusedRightBytes += GrowBy;
	BufSize = Size;
	return OrsTRUE;
	} //  End CBufferedData::MakeNewBuffer

CBufferedData::CBufferedData()
	{
	Buffer = OrsNULL;
	ResizeBy = DEFAULT_BUFFERED_DATA_RESIZE_BY;
	Reset();
	}

CBufferedData::CBufferedData(long aSize)
	{
	MakeNewBuffer(aSize);
	ResizeBy = DEFAULT_BUFFERED_DATA_RESIZE_BY;
	}

CBufferedData::CBufferedData(long aSize, long resizeBy)
	{
	MakeNewBuffer(aSize);
	this->ResizeBy = resizeBy;
	}




CBufferedData::~CBufferedData()
	{
	if	(Buffer)
		free (Buffer);
	}



// Will insert a n bytes  at a given POSITION is the buffer
// Pos starts at 1 and cannot be grater that the characters
// cuttently in buffer
OrsBool CBufferedData::InsertBinaryDataAt(const OrsUChar *aBinaryBuf, OrsInt Size, OrsInt Pos)
	{
	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(Size))
			return OrsFALSE;
		}

	// Siamo in range?
	if	(Pos < 0 || Pos > (UsedBytes+1))
		{
	    SignalAnError(__FILE__, __LINE__, "InsertCharAt: out of range");
		return OrsFALSE;
		}
	// Insert in coda?
//	if	(Pos == (UsedBytes+1))
	if	(Pos == Point)
		{
		return (AddBinaryData((char *)aBinaryBuf, Size));
		}

	// Abbiamo spazio a destra?
	if	(UnusedRightBytes < Size)
		{
		if	(!GrowBuffer(Size))
			return OrsFALSE;
		}

	// OK abbiamo abbastanza spazio. spostiamo il buffer a destra di un byte
	// a partire da Pos
	if	(!MoveRightFromBy(Pos, Size))
		return OrsFALSE;

	Pos--;
	memcpy (BufStartPtr+Pos, aBinaryBuf, Size);
	return OrsTRUE;
	} // End CBufferedData::InsertBinaryDataAt


// Will insert a string to a given POSITION is the buffer
// Pos starts at 1 and cannot be grater that the characters
// cuttently in buffer
OrsBool CBufferedData::InsertStringAt(const OrsChar *aString, OrsInt Pos)
	{
	return InsertBinaryDataAt((OrsUChar *)aString, strlen(aString), Pos);
	} // End CBufferedData::InsertStringAt

/**
*	Returns a character if available and removes it from buffer
**/
//OrsChar CBufferedData::GetLastChar()
OrsUChar CBufferedData::GetLastChar()
	{
	GetCharFlag = OrsTRUE;
	if	(!UsedBytes)
		{
		GetCharFlag = OrsFALSE;
		return 0;
		}

	BufTailPtr--;
	UsedBytes--;
	Point--;
	UnusedRightBytes++;

	return *BufTailPtr;
	} // End CBufferedData::GetLastChar()


 OrsChar *CBufferedData::ConstData()
	{
	return (( OrsChar *)BufStartPtr);
	}

OrsChar *CBufferedData::Data()
	{
	return (BufStartPtr);
	}


OrsBool CBufferedData::operator= (char *aString)
	{
	Reset(); // Empty buffer
	return AddBinaryData(aString, strlen(aString));
	}
/****
OrsBool CBufferedData::operator= ( char *aString)
	{
	Reset(); // Empty buffer
	return AddBinaryData((OrsUChar *)aString, strlen(aString));
	}
**/

CBufferedData::CBufferedData(OrsChar *aString)
	{
	Buffer = OrsNULL;
	Reset();
	AddBinaryData(aString, strlen(aString));
	}

OrsLong CBufferedData::GetUsedBytes()
{
	return UsedBytes;
}

OrsLong CBufferedData::GetResizeBy()
{
	return ResizeBy;
}

OrsLong CBufferedData::GetUnusedRightBytes()
{
	return UnusedRightBytes;
}

OrsLong CBufferedData::GetBufSize()
{
	return BufSize;
}



void CBufferedData::Reset()
	{
	if	(Buffer)
		free (Buffer);
	Buffer = OrsNULL;
	GetCharFlag = OrsFALSE;
	PeekCharFlag = OrsFALSE;
	BufSize = 0;
	UsedBytes = 0;
	UnusedLeftBytes = 0;
	UnusedRightBytes = 0;
	Point = -1;
	BufTailPtr = OrsNULL;
	BufStartPtr = OrsNULL;
	//ResizeBy = DEFAULT_RESIZE_BY;
	}

void CBufferedData::ResetNoFree()
	{
	UsedBytes = 0;
	UnusedLeftBytes = 0;
	UnusedRightBytes=BufSize;
	Point = 0;
	BufTailPtr = Buffer;
	BufStartPtr = Buffer;
	}


// Will add size bytes to the END of buffer
//OrsBool CBufferedData::AddBinaryData(const OrsUChar *aBinaryBuf, OrsInt Size)
OrsBool CBufferedData::AddBinaryData(char *aBinaryBuf, OrsInt count)
	{
	if (!count)
		return true; // Non far niente se non ci sono dati

	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(count))
			return OrsFALSE;
		}

	// Abbiamo ancora spazio nel buffer?
	if	(!UnusedRightBytes || UnusedRightBytes < count)
		{
		if	(!GrowBuffer(count))
			return OrsFALSE;
		}


//	if (count == 1)
//		*BufTailPtr = *aBinaryBuf; // 24/03/2015
//	else if (count == 2)
//		*((short*)BufTailPtr) = *((short*)aBinaryBuf); // 24/03/2015
//	else
//		memcpy(BufTailPtr, aBinaryBuf, count);

	char * aString = aBinaryBuf;
	MACRO_COPY_FAST(count); // char *BufTailPtr , char *aString



	UnusedRightBytes -= count;
	UsedBytes += count;
	Point += count;
	BufTailPtr += count;
	return OrsTRUE;
	} // End CBufferedData::AddBinaryData


// Will add a string to the END of the buffer
OrsBool CBufferedData::AddString(const OrsChar *aString)
	{
	//return AddBinaryData((char *)aString, strlen(aString));
	return AddBinary((char *)aString, strlen(aString));
	} // End CBufferedData::AddString

OrsBool CBufferedData::AddString(CString *sPtr)
{
	//return AddBinaryData(sPtr->data(), sPtr->Length());
	return AddBinary(sPtr->data(), sPtr->Length());
}









// Will add a character to the END of the buffer
OrsBool CBufferedData::AddChar(OrsChar aChar)
	{
	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		}
	// Abbiamo ancora spazio nel buffer?
	if	(!UnusedRightBytes)
		{
		if	(!GrowBuffer(ResizeBy))
			return OrsFALSE;
		}

	*BufTailPtr = aChar;
	UsedBytes++;
	UnusedRightBytes--;
	Point++;
	BufTailPtr++;
	return OrsTRUE;
	} // End CBufferedData::AddChar
