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
* Module  : CString                                                  *
* Author  : Argentino Trombin                                               *
* Desc.   : Handles Bufferinf                                           *
* Date    :                                                                 *
****************************************************************************/
#include <stdlib.h>
#include <malloc.h>
#include <memory.h>
#include <string.h>
#include <ctype.h>



#include "library/CString.h"
#include "library/Masks.h"
#include "library/macros.h"
CharMask Mask;
#include "GenericError.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);



CString::CString(OrsLong Size)
	{
	Buffer = OrsNULL;
	BufferSubString = OrsNULL;
	CaseSensitiveFlag = OrsFALSE;
//	SkipWhiteSpacesFlag = OrsTRUE;
	SkipWhiteSpacesFlag = OrsFALSE;	// 01/03/2016 Bug IEI RT1V020699
	KeepLineFeed = OrsTRUE;
	ResizeBy = CSTRING_RESIZE_DEFAULT;
	MakeNewBuffer(Size);
	}


/**
*	Shift all used bytes to the right by n
**/
//OrsBool	CString::MoveRightBy(OrsInt n)
OrsBool	CString::MoveRightBy(OrsLong n)
	{
	// In range?
	if	(n < 0)
		{
//	    SignalAnError(__FILE__, __LINE__, "MoveRightBy: n out of range");
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
	    SignalAWarning(__FILE__, __LINE__, "MoveRightBy: Buffer holds no data");
		return OrsTRUE;
		}

	// Move to the right
	memmove (Buffer+n, Buffer, UsedBytes);

	BufTailPtr += n;
	UsedBytes += n;
	UnusedRightBytes -= n;
	return OrsTRUE;
	} // End CString::MoveRightBy




// Will return the number of characters held in the buffer
//OrsInt	CString::GetNumOfCharsInBuffer()
//OrsInt	CString::Length()
OrsLong	CString::Length()
	{
	return UsedBytes-1; // -1 for EOB
	}


/**
*	Change the buffer ResizeBy value
**/
//OrsInt CString::SetResizeValueBy(OrsInt aValue)
OrsLong CString::SetResizeValueBy(OrsLong aValue)
	{
	if	(aValue < 2)
		return 0;

	ResizeBy = aValue;
	return aValue;
	} // End


CString::CString()
	{
	init();
	} // End CString::CString


CString::CString(OrsChar *aString)
	{
	init();
	if (!aString || !*aString)
		return;

	if (!*(aString+1))
		AppendChar(*aString); // 24/03/2015
	else
		AppendString(aString);
	}


CString::~CString()
	{
	if	(Buffer)
		free (Buffer);
	} // End CString::~CString






OrsChar *CString::ConstData()
	{
	return ((OrsChar *)Buffer);

	}

OrsChar *CString::Data()
	{
	return (Buffer);
	}

OrsChar *CString::data()
	{
	return (Buffer);
	}







OrsBool CString::operator + (OrsChar *aString)
	{
	if (!aString )
		return false;  // in case of null values
	if (!*aString)
		return true;

	return AppendString(aString);
	}

OrsBool CString::operator + (OrsChar aChar)
	{
	return AppendChar(aChar);
	}


OrsInt CString::operator == (char* aStringPtr)
	{
	if (GetCaseSensitiveFlag())
	    return !strcmp(data(), aStringPtr);
	else
		return !stricmp(data(), aStringPtr);
	}








// Will add a character to the END of the string
OrsBool CString::AppendChar(OrsChar aChar)
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
	BufTailPtr++;
	*BufTailPtr = 0;

	if	(UsedBytes)
		{
		UsedBytes++;
		UnusedRightBytes--;
		}
	else
		{
		UsedBytes+=2;
		UnusedRightBytes-=2;
		}
	return OrsTRUE;
	} // End CString::AppendChar


/**
*	Put a string uop front
**/
OrsBool CString::PrependString(const OrsChar *aString)
	{

	if (!aString)
		return false;  // in case of null values
	if (!*aString)
		return true;

	long count = strlen(aString)+1;

	// Esiste un buffer
	if	(!Buffer)
		return AppendString((OrsChar *)aString);


	// Prepara lo spazio in fronte
	// Abbiamo spazio in coda?
	if	(UnusedRightBytes >= (count -1))
		{
		// OK. spostiamo il buffer a destra
		if	(!MoveRightBy(count-1))
			return OrsFALSE;
		}
	else
		{
		// Mon abbiamo spazio ne' a destra ne' a sinistra
		if	(!GrowBuffer(count))
			return OrsFALSE;
		// Align the buffer right by one position
		if	(!MoveRightBy(count-1))
			return OrsFALSE;
		}

//	if ((Size-1) == 2)
//		*((short*)Buffer) = *((short*)aString); // 24/03/2015
//	else
//		memcpy(Buffer, aString, Size-1);

	char *BufTailPtr = Buffer;
	MACRO_COPY_FAST(count-1); // char *BufTailPtr , char *aString


//	UsedBytes+=Size-1;
	return OrsTRUE;
	} // End CString::PrependString


OrsBool CString::PrependChar(OrsChar aChar)
	{

	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		}

	// Prepara lo spazio in fronte
	// Abbiamo spazio in coda?
	if	(UnusedRightBytes)
		{
		// OK. spostiamo il buffer a destra di un byte se abbiamo gia'una stringa
		if	(UsedBytes)
			{
			if	(!MoveRightBy(1))
				return OrsFALSE;
			}
		}
	else
		{
		// Mon abbiamo spazio a destra
		if	(!GrowBuffer(ResizeBy))
			return OrsFALSE;

		// Align the buffer right by one position
		if	(!MoveRightBy(1))
			return OrsFALSE;
		}
	*Buffer = aChar;
//	UsedBytes++;
	return OrsTRUE;
	} // End CString::PrependChar



/**
*	Shift part of used bytes to the right by n posistions
*	Pos starts at 1
**/
//OrsBool	CString::MoveRightFromBy(OrsInt From, OrsInt By)
OrsBool	CString::MoveRightFromBy(OrsLong From, OrsLong By)
	{
//	int len;
	long len;

	// Something to move?
	if	(!UsedBytes)
		{
		return OrsFALSE;
		}

	// In range?
	if	(By < 0 || From > UsedBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "MoveRightBy: n out of range");
		return OrsFALSE;
		}

	// Enough room on the right to allow move?
	if	(By > UnusedRightBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "MoveRightBy: free space on the right non enough");
		return OrsFALSE;
		}

	// Move to the right
	len = UsedBytes-From+1;

	From--;
	memmove (Buffer+From+By, Buffer+From, len);

	BufTailPtr += By;
	UsedBytes+=By;

	UnusedRightBytes -= By;
	return OrsTRUE;
	} // End CString::MoveRightFromBy

// Will insert a string to a given POSITION is the buffer
// Pos starts at 0 and cannot be greater that the characters
// cuttently in buffer
//OrsBool CString::InsertStringAt(OrsChar *aString, OrsInt Pos)
OrsBool CString::InsertStringAt(const OrsChar *aString, OrsLong Pos)
	{
//	int Size = strlen(aString)+1;
	long count = strlen(aString)+1;

	if	(Pos > UsedBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "InsertStringAt: Pos (%d) out of range", Pos);
		return OrsFALSE;
		}

	// Esiste un buffer
	if	(!Buffer)
		return AppendString((OrsChar *)aString);

	// Prepara lo spazio nel mezzo
	// Abbiamo spazio in coda?
	if	(UnusedRightBytes >= (count -1))
		{
		// OK. spostiamo il buffer a destra
		if	(!MoveRightFromBy(Pos, count-1))
			return OrsFALSE;
		}
	else
		{
		// Mon abbiamo spazio ne' a destra ne' a sinistra
		if	(!GrowBuffer(count))
			return OrsFALSE;
		// Align the buffer right by one position
		if	(!MoveRightFromBy(Pos, count-1))
			return OrsFALSE;
		}

//	if ((Size-1) == 2)
//		*((short*)Buffer+Pos) = *((short*)aString); // 24/03/2015
//	else
//		memcpy(Buffer+Pos, aString, Size-1);

	char *BufTailPtr = Buffer+Pos;
	MACRO_COPY_FAST(count-1); // char *BufTailPtr , char *aString


	//UsedBytes+=Size-1;
	return OrsTRUE;
	} // End CString::InsertStringAt


/**
 Will insert a char to a given POSITION is the buffer
 Pos starts at 1 and cannot be grater that the characters
 cuttently in buffer
 Insertion will affect only the used part of the buffer

 Can insert only within some data or at end of data

**/
//OrsBool CString::InsertCharAt(OrsChar aChar, OrsInt Pos)
OrsBool CString::InsertCharAt(OrsChar aChar, OrsLong Pos)
	{
	int Size = 1+1;

	if	(Pos > UsedBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "InsertCharAt: Pos (%d) out of range", Pos);
		return OrsFALSE;
		}

	// Esiste un buffer
	if	(!Buffer)
		return AppendChar(aChar);

	// Prepara lo spazio nel mezzo
	// Abbiamo spazio in coda?
	if	(UnusedRightBytes >= (Size -1))
		{
		// OK. spostiamo il buffer a destra
		if	(!MoveRightFromBy(Pos, Size-1))
			return OrsFALSE;
		}
	else
		{
		// Mon abbiamo spazio ne' a destra ne' a sinistra
		if	(!GrowBuffer(ResizeBy))
			return OrsFALSE;
		// Align the buffer right by one position
		if	(!MoveRightFromBy(Pos, Size-1))
			return OrsFALSE;
		}

	*(Buffer+Pos-1) = aChar;

	return OrsTRUE;
	} // End CString::InsertCharAt


/**
*	Returns a character if available and removes it from buffer
*	0 on failure
**/
OrsChar CString::ExtractFirstChar()
	{
	OrsChar chr;

	if	(UsedBytes<2)
		{
	    SignalAWarning(__FILE__, __LINE__, "ExtractFirstChar: buffer is empty");
		return 0;
		}

	chr = *Buffer;

	UsedBytes--;
	BufTailPtr --;
	UnusedRightBytes ++;

	memmove(Buffer, Buffer+1, UsedBytes);
	return chr;
	} // End CString::ExtractFirstChar()




/**
*	Returns a character if available and removes it from buffer
**/
OrsChar CString::ExtractLastChar()
	{
	OrsChar chr;

	if	(UsedBytes<2)
		{
//	    SignalAWarning(__FILE__, __LINE__, "ExtractLastChar: buffer is empty");
		return 0;
		}

	BufTailPtr--;
	UsedBytes--;
	UnusedRightBytes++;

	chr = *BufTailPtr;
	*BufTailPtr = 0;
	return chr;
	} // End CString::ExtractLastChar()



//OrsInt	CString::First(char aChar)
OrsLong	CString::First(char aChar)
	{
	char* f = strchr(Buffer, aChar);
	return f ? f - Buffer : -1;
	} // End CString::First

/**
*	Returns position of last character
**/
OrsLong	CString::Last(char aChar)
	{
	char* f = strrchr(Buffer, aChar);
	return f ? f - Buffer : -1;
	} // End CString::Last()

/**
*	Returns position of last character
**/
OrsLong	CString::Nth(char aChar, short Nth)
	{
/*
	char* f = strrchr(Buffer, aChar);
	return f ? f - Buffer : -1;
*/
	char *ptr, *ptr1;
	int i;

	ptr = ptr1 = Buffer;
	for	(i=0; i<Nth; i++)
		{
		ptr1 = strchr (ptr, aChar);
		if	(!ptr1)
			break;
		ptr = ptr1+1;
		}
	if	(i != Nth)
		return -1;

	return ptr -1 - Buffer;
	} // End CString::Last()

OrsBool CString::GetCaseSensitiveFlag()
	{
	return CaseSensitiveFlag;
	} // End CString::GetCaseSensitiveFlag


void CString::SetCaseSensitiveFlag(OrsBool aCase)
	{
	CaseSensitiveFlag = aCase;
	} // End CString::SetCaseSensitiveFlag



void CString::ToUpper()
	{
	register char* p = Buffer;
	while( *p )
		{
		if	(*p > 0x60 && *p < 0x7b)
			*p -= 0x20; // If a-z decrease 0x20
		p++;
		}
	} // End CString::ToLower

void CString::ToLower()
	{
	register char* p = Buffer;
	while( *p )
		{
		if	(*p > 0x40 && *p < 0x5b)
			*p += 0x20; // If A-Z increase 0x20
		p++;
		}
	} // End CString::ToLower


//OrsBool CString::Replace(OrsInt From, OrsInt Len, char *aString)
OrsBool CString::Replace(OrsLong From, OrsLong Len, const char *aString)
	{
//	OrsInt To = From + Len - 1;
	OrsLong To = From + Len - 1;

	if	(Len > 0)
		{
		if	(!ExtractFromTo(From, To))
			return OrsFALSE;
		}
	return InsertStringAt(aString, From);
	} // End CString::Replace

OrsBool CString::ReplaceChar(OrsLong pos, char aChar)
	{
	if (pos > (this->Length()-1))
		return false;
	*(Buffer+pos)=aChar;
	} // End CString::Replace



/**
*	Returns a pointer to a substring removed from the buffer.
*	OrsNULL on Error
**/
//OrsChar* CString::ExtractFromTo (OrsInt From, OrsInt To)
OrsChar* CString::ExtractFromTo (OrsLong From, OrsLong To)
	{
//	OrsInt Size = To - From +1;
	OrsLong Size = To - From +1;

	// Copy substring to substring buffer
//	if	(!SubstringData (From, To))
	if	(!SubstringData (From, To-From+1))
		return OrsFALSE;

	// Remove substring from Buffer
	// Eg:
	//		-**--
	//		01234
	memmove(Buffer+From, Buffer + To + 1, UsedBytes -1 - To);


	UsedBytes		 -= Size;
	BufTailPtr		 -= Size;
	UnusedRightBytes += Size;

	return BufferSubString;
	} // End CString::ExtractFromTo()






/**
*	Shift all used bytes to the left by n positions
*	This operation will eliminate the leftomst characters
**/
//OrsBool	CString::CropLeftBy(OrsInt n)
OrsBool	CString::CropLeftBy(OrsLong n)
	{
	// In range?
	if	(n <= 0)
		{
	    SignalAnError(__FILE__, __LINE__, "CropLeftBy: range invalid");
		return OrsFALSE;
		}

	// Something to crop?
	if	(n > UsedBytes)
		{
	    SignalAWarning(__FILE__, __LINE__, "CropLeftBy: range too big");
		return OrsTRUE;
		}

	// Eg:
	//		--***
	//    	01234 -> 234
	memmove (Buffer, Buffer+n, UsedBytes-n);

	BufTailPtr -= n;
	*BufTailPtr = 0;
	UsedBytes -=n;
	UnusedRightBytes += n;
	return OrsTRUE;
	} // End CString::CropLeftBy








void CString::SkipWhitespace(OrsBool YesNo)
	{
	SkipWhiteSpacesFlag = YesNo;
	}







//OrsInt CString::StoreSize()
OrsLong CString::StoreSize()
	{
	return sizeof(UsedBytes)+UsedBytes; // sizeof(OrsInt)=Bytes a seguire
	} // End CString::StoreSize

OrsInt CString::Hash()
	{
	if	(GetCaseSensitiveFlag())
		return HashCase();
	else
		{
		CString aString;
		aString = Buffer;
		aString.ToUpper();
		return aString.HashCase();
		}
	} // End CString::Hash







OrsInt CString::HashCase()
	{
	register OrsInt i, h;
	register OrsInt* c;

	h = UsedBytes-1;		// Mix in the string length.
	i = h / sizeof(OrsInt);	// Could do "<<" here, but less portable.
	c = (OrsInt*)Buffer;

	while (i--)
		h ^= *c++;	// XOR in the characters.

	// If there are any remaining characters, then XOR in the rest, using a mask:
	if	((i = (UsedBytes-1) % sizeof(OrsInt)) != 0)
		{
		h ^= *c & Mask.in[i];
		}
	return h;
	} // End CString::HashCase


// ===============
// GLOBAL FUNCTIONS

// Return an upper-case version of self:
void toUpper(CString& str)
	{
	register       char* uc = (char*)str.data();
	while( *uc )
		*uc++ = toupper(*uc);
	} // End toUpper























OrsChar& CString::operator[](int i)
	{
	// In range?
	if	(i < 0 || i >= UsedBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "CString::operator[]: i out of range");
		return *BufTailPtr;
		}
	return Buffer[i];
	} // End CString::operator[]



void CString::Reset()
	{
	Buffer = OrsNULL;
	BufferSize = 0;
	BufferSubString = OrsNULL;
	BufferSubStringSize=0;
	UsedBytes = 0;
	UnusedRightBytes = 0;
	BufTailPtr = OrsNULL;
	ResizeBy = CSTRING_RESIZE_DEFAULT;

	} // End Reset








//OrsBool CString::Equals(OrsChar *aString, OrsInt SubStringLen)
OrsBool CString::Equals(OrsChar *aString, OrsLong SubStringLen)
	{
//	Clear(); // Empty buffer
	ResetNoResize();

	if (!SubStringLen)	// 25/03/2015
		return true;

	if (SubStringLen == 1)
		return AppendChar(*aString); // 24/03/2015

	return AppendString(aString, SubStringLen);
	} // End CString::Equals

OrsBool CString::Equals(const OrsChar *aString, OrsLong SubStringLen)
	{
//	Clear(); // Empty buffer
	ResetNoResize();

	if (!SubStringLen)	// 25/03/2015
		return true;
	if (SubStringLen == 1)
		return AppendChar(*aString); // 24/03/2015

	return AppendString((char *)aString, SubStringLen);
	} // End CString::Equals


OrsBool CString::Equals(OrsChar *aString)
	{
	//	Clear(); // Empty buffer
	ResetNoResize();
	if (!*aString)
		return true;

	if (!*(aString+1))
		return AppendChar(*aString); // 24/03/2015
	return AppendString(aString, strlen(aString));
	} // End CString::Equals


OrsBool CString::Equals(const OrsChar *aString)
	{
	//	Clear(); // Empty buffer
	ResetNoResize();
	if (!*aString)
		return true;
	if (!*(aString+1))
		return AppendChar(*aString); // 24/03/2015
	return AppendString((char *)aString, strlen(aString));
	} // End CString::Equals


OrsBool	CString::CropRightFromChar(char aChar)
	{
//	OrsInt	CropFrom = First(aChar);
	OrsLong	CropFrom = First(aChar);
	if	(CropFrom == -1)
		return OrsFALSE;

	return (CropRightBy(UsedBytes -1 - CropFrom ));
	} // End CString::CropRightFromChar






OrsBool	CString::RestoreFrom(FILE *aFilePtr)
	{
//	OrsInt StoreSize, ret;
	OrsLong StoreSize, ret;
	ret = fread(&StoreSize, sizeof(StoreSize), 1, aFilePtr);
	if	(ret != 1)
		{
        SignalAnError(__FILE__, __LINE__, "CString::RestoreFrom Non posso leggere");
		return OrsFALSE;
		}
	GrowBuffer(StoreSize);
	ret = fread(BufTailPtr, StoreSize, 1, aFilePtr);
	if	(ret != 1)
		{

        SignalAnError(__FILE__, __LINE__, "CString::RestoreFrom Non posso leggere");
		return OrsFALSE;
		}
	UsedBytes += (StoreSize--); // Get rid of extra EOS
	return OrsTRUE;
	} // End CString::RestoreFrom


OrsBool	CString::SaveOn(FILE *aFilePtr)
	{
//	OrsInt len = UsedBytes, ret;
//	OrsLong len = UsedBytes;
	OrsInt ret;

	// Store the number of characters that make up the string (including EOS)
	ret = fwrite(&UsedBytes, sizeof(UsedBytes), 1, aFilePtr);
	if	(ret != 1)
		{
        SignalAnError(__FILE__, __LINE__, "CString::SaveOn Non posso scrivere");
		return OrsFALSE;
		}
	// Write the stringitself
	ret = fwrite(Buffer, UsedBytes, 1, aFilePtr);
	if	(ret != 1)
		{
        SignalAnError(__FILE__, __LINE__, "CString::SaveOn Non posso scrivere");
		return OrsFALSE;
		}
	return OrsTRUE;
	} // End CString::SaveOn

OrsBool	CString::SaveOn(CFile & aCFileRef)
	{
  // Store the number of characters, then the string itself:
	if( !aCFileRef.Write(UsedBytes) )
		{
        SignalAnError(__FILE__, __LINE__, "CString::SaveOn Non posso scrivere");
		return OrsFALSE;
		}
	if( !aCFileRef.Write(data(), UsedBytes))
		{
        SignalAnError(__FILE__, __LINE__, "CString::SaveOn Non posso scrivere");
		return OrsFALSE;
		}
	return OrsTRUE;
	} // End CString::SaveOn

OrsBool	CString::RestoreFrom(CFile & aCFileRef)
	{
//	OrsInt StoreSize;
	OrsLong StoreSize;
	if( !aCFileRef.Read(StoreSize))
		{
        SignalAnError(__FILE__, __LINE__, "CString::SaveOn Non posso leggere");
		return OrsFALSE;
		}

	GrowBuffer(StoreSize-1);

	if( !aCFileRef.Read(BufTailPtr, StoreSize))
		{
        SignalAnError(__FILE__, __LINE__, "CString::SaveOn Non posso leggere");
		return OrsFALSE;
		}

	UsedBytes = StoreSize;
	BufTailPtr += StoreSize-1;
	UnusedRightBytes = 0;
	return OrsTRUE;
	} // End CString::RestoreFrom


















void CString::ClearTo(OrsChar aChar)
	{
	memset (Buffer, UsedBytes, 1);
	} // End CString::ClearTo



//OrsBool CString::SizeAndClearTo(OrsInt aSize, OrsChar aChar)
OrsBool CString::SizeAndClearTo(OrsLong aSize, OrsChar aChar)
	{
	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(aSize+1)) // EOS
			return OrsFALSE;
		}
	if	(BufferSize <= aSize)
		{
		// Must resize
		if	(!GrowBuffer(aSize - BufferSize + 1))
			return OrsFALSE;
		}

	memset(Buffer, aChar, aSize);

	// Beware the EOS
	UsedBytes = aSize+1;
	UnusedRightBytes = BufferSize - UsedBytes;

	BufTailPtr = Buffer + UsedBytes-1;
	*BufTailPtr = 0; // EOS

	return OrsTRUE;
	} // End CString::ClearTo



// Will return the last character from the buffer
OrsChar CString::GetLastChar()
	{
	if	(UsedBytes<2)
		return 0;
	return *(BufTailPtr-1);
	} // End CString::GetLastChar






//OrsBool CString::GrowBuffer(OrsInt GrowBy)
OrsBool CString::GrowBuffer(OrsLong GrowBy)
	{
//	OrsInt Size = _msize(Buffer) + GrowBy;
//	OrsLong Size = _msize(Buffer) + GrowBy;
//	OrsLong Size = malloc_usable_size(Buffer) + GrowBy;
	OrsLong Size = BufferSize + GrowBy; // 12/12/2010



	OrsChar *OrgBufPtr = Buffer;

//    printf ("\nCString::GrowBuffer for size=%ld, GrowBy=%ld", Size, GrowBy);

//	OrsChar *ReallocatedBufPtr;

//	ReallocatedBufPtr = (OrsChar *) realloc(Buffer, Size);
//	Buffer = ReallocatedBufPtr;
	Buffer = (OrsChar *) realloc(Buffer, Size);
	if	(!Buffer)
		{
	    SignalAnError(__FILE__, __LINE__, "CString ralloc failed for size=%ld, GrowBy=%ld", Size, GrowBy);
		return OrsFALSE;
		}


	// Now the Buffer address may be changed
//	if	(OrgBufPtr != ReallocatedBufPtr)
	if	(OrgBufPtr != Buffer)
		{
		// The buffer has been moved
		// Reposition pointers
//		Buffer = ReallocatedBufPtr;
		BufTailPtr = Buffer + UsedBytes -1; // positioned on EOB
		}

	UnusedRightBytes += GrowBy;
	BufferSize = Size;
	return OrsTRUE;
	} //  End CString::MakeNewBuffer


/**
*	This operation will eliminate the rightomst characters
**/
//OrsBool	CString::CropRightBy(OrsInt n)
OrsBool	CString::CropRightBy(OrsLong n)
	{
	// In range?
	if	(n <= 0)
		{
	    SignalAnError(__FILE__, __LINE__, "CropRightBy: range invalid");
		return OrsFALSE;
		}

	// Something to crop?
//	if	(n > UsedBytes)
	if	(n > (UsedBytes-1)) // 17/04/2015
		{
	    SignalAWarning(__FILE__, __LINE__, "CropRightBy: range too big");
//		return OrsTRUE;
		return OrsFALSE;
		}

	// Eg:
	//		***--
	// 		01234
	//    	01234 -> 012


	BufTailPtr -= n;
	*BufTailPtr = 0;
	UsedBytes -=n;
	UnusedRightBytes += n;

	return OrsTRUE;
	} // End CString::CropRightBy


/**
*	This operation will eliminate the rightomst characters
*	starting from position
**/
OrsBool	CString::CropRightFrom(OrsLong From)
	{
	// In range?
	if	(From < 0) // <=
		{
	    SignalAnError(__FILE__, __LINE__, "CropRightFrom: range invalid");
		return OrsFALSE;
		}

	// Something to crop?
	if	(From > UsedBytes)
		{
	    SignalAWarning(__FILE__, __LINE__, "CropRightFrom: range too big");
		return OrsFALSE; // OrsTRUE
		}

	// Eg:
	//		***--
	// 		01234
	//    	01234 -> 012


	BufTailPtr = Buffer + From;
	*BufTailPtr = 0;

	UnusedRightBytes += (UsedBytes-From-1);
	UsedBytes = From+1;

	return OrsTRUE;
	} // End CString::CropRightFrom

// Remove righthandside characters starting from a given position and on first of one of  the characters given
OrsBool	CString::CropRightAfterChar(OrsLong From, char *delimiters)
	{
	// In range?
	if	(From < 0) // <=
		{
	    SignalAnError(__FILE__, __LINE__, "CropRightFrom: range invalid");
		return OrsFALSE;
		}

	// Something to crop?
	if	(From > UsedBytes)
		{
	    SignalAWarning(__FILE__, __LINE__, "CropRightFrom: range too big");
		return OrsFALSE; // OrsTRUE
		}

char *ptr = data()+From;
char *d;
bool found=false;
while (*ptr)
	{
	d = delimiters;

	// Skip non delimiter
	while (*d)
		{
		if	(*ptr == *d)
		{
			found=true;
			break;
		}
		d++;
		}
	ptr ++;
	From++;
	if (found)
		break;
	} // End while
if (*ptr)
	return (CropRightBy(UsedBytes -1 - From ));
else
	return false;
	} // End CString::CropRightAfterChar





CString::CString(OrsChar *aString, OrsLong Size)
	{
	Buffer = OrsNULL;
	BufferSubString = OrsNULL;
	CaseSensitiveFlag = OrsFALSE;
	SkipWhiteSpacesFlag = OrsTRUE;
	KeepLineFeed = OrsTRUE;
	ResizeBy = CSTRING_RESIZE_DEFAULT;
	Clear();
	if (!Size)
		return;
	if (Size == 1)
		AppendChar(*aString); // 24/03/2015
	else
		AppendString(aString, Size);
	}








OrsBool CString::ReadString(CFile *aFilePtr)
	{
	return ReadToDelim(aFilePtr, '\0');
	} // End CString::ReadString

OrsBool CString::ReadString(istream& aStream)
	{

	return ReadToDelim(aStream, '\0');
/*
	if	(ReadToDelim(aStream, '\0'))
		{
		aStream.get(ch); // Read back in the delimiter that was put back in stream
		return OrsTRUE;
		}
	return OrsFALSE;
*/
	} // End CString::ReadString


OrsBool CString::ReadToDelimIgnoringQuoted(CFile *aFilePtr, char aDelim, OrsChar *QuotesStartEnd)
	{
	OrsBool InQuoted = OrsFALSE;
	char ch, Quote, *Ptr;

	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		*BufTailPtr = 0; // EOS
		UsedBytes++;
		UnusedRightBytes--;
		}
  	while(1)
		{
    	if	(!aFilePtr->Read(ch))
			return OrsFALSE;

		if	(ch == aDelim)
			{
			if	(!InQuoted)
				break;
			}
		// Abbiamo ancora spazio nel buffer?
		if	(!UnusedRightBytes)
			{
			if	(!GrowBuffer(ResizeBy))
				return OrsFALSE;
			}
    	if( aFilePtr->Eof() )
			break;
		*BufTailPtr = ch;
		BufTailPtr++;
		UsedBytes++;
		UnusedRightBytes--;
		if	(InQuoted)
			{
			if	(ch == Quote)
				InQuoted = OrsFALSE;
			}
		else
			{
			Ptr = QuotesStartEnd;
			while (*Ptr)
				{
				if	(ch == *Ptr)
					{
					InQuoted = OrsTRUE;
					Quote = ch;
					}
				Ptr++;
				}
			}
  		} //   End while
	*BufTailPtr = 0; // EOS
	return OrsTRUE;
	} // End CString::ReadToDelimIgnoringQuoted


OrsBool CString::ReadToDelim(CFile *aFilePtr, char aDelim)
	{
	char ch;
	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		*BufTailPtr = 0; // EOS
		UsedBytes++;
		UnusedRightBytes--;
		}

//	nchars = 0;
  	while(1)
		{
    	if	(!aFilePtr->Read(ch))
    	{
			*BufTailPtr = 0; // 31/08/2015
			return OrsFALSE;
    	}
		if	(ch == aDelim)
			{
//			aFilePtr->UnRead(ch);	// Put back trailing ws
			break;
			}
		// Abbiamo ancora spazio nel buffer?
		if	(!UnusedRightBytes)
			{
			if	(!GrowBuffer(ResizeBy))
				{
				*BufTailPtr = 0; // 31/08/2015
				return OrsFALSE;
				}
			}
    	if( aFilePtr->Eof())
			break;
		*BufTailPtr = ch;
		BufTailPtr++;
		UsedBytes++;
		UnusedRightBytes--;
  		} //   End while
	*BufTailPtr = 0; // EOS
	return OrsTRUE;
	} // End CString::ReadToDelim


void CString::SetKeepLineFeedFlag(OrsBool aFlag)
	{
	KeepLineFeed = aFlag;
	} // End CString::SetKeepLineFeedFlag

OrsBool CString::GetKeepLineFeedFlag()
	{
	return KeepLineFeed;
	} // End CString::GetKeepLineFeedFlag

OrsChar *CString::SubstringData(OrsLong From)
{
	OrsLong length = this->Length() - From;
	return SubstringData(From, length);
}




//OrsChar *CString::SubstringData(OrsInt From, OrsInt To)
//OrsChar *CString::SubstringData(OrsInt From, OrsInt Length)
OrsChar *CString::SubstringData(OrsLong From, OrsLong count)
   	{
//	OrsInt Length = To - From +1 ;

	// In range?
	if	(From < 0 || From >= UsedBytes || count < 0 || count >= UsedBytes)
		{
	    SignalAnError(__FILE__, __LINE__, "SubstringData out of range, from=%d, length=%d, strlen=%d string=%s", From, count, UsedBytes-1, this->Buffer);
		return OrsNULL;
		}

	// Buffer for substring big enough?
	if	(BufferSubStringSize <= count)
		{
		// Need bigger buffer
		if	(BufferSubString)
			free (BufferSubString);

		BufferSubString = (OrsChar *)malloc (count +1); // Con EOS
		if	(!BufferSubString)
			{
			SignalAnError(__FILE__, __LINE__, "CString malloc failed for Length %d", count);
			return OrsFALSE;
			}
		BufferSubStringSize = count + 1;
		}

	// Copy substring from Buffer to BufferSubString buffer
	BufferSubStringLength = count;

//	if (Length == 1)
//		*BufferSubString = *(Buffer+From); // 24/03/2015
//	else
//		memcpy (BufferSubString, Buffer+From, Length);
//	*(BufferSubString + Length) = 0; // EOS

	char *BufTailPtr = BufferSubString;
	char *aString = Buffer+From;
	MACRO_COPY_FAST(count); // char *BufTailPtr , char *aString
	*(BufferSubString + count) = 0; // EOS



	return BufferSubString;
	} // End CString::SubstringData


OrsChar *CString::SubstringData(OrsLong From, char *Delimiters, OrsLong *To)
	{
	char *ptr, *dp;
	OrsLong Length = 0;

	// In range?
	if	(From < 0 || From >= UsedBytes)
		{
//	    SignalAnError(__FILE__, __LINE__, "SubstringData: out of range");
		return OrsNULL; // We are at the end of the string (possibly)
		}

	ptr = Buffer+From;
	while (*ptr)
		{
		dp = Delimiters;
		while (*dp)
			{
			if	(*dp == *ptr)
				{
				// Found delimiter
				*To = From+Length+1; // Position past the delimiter
				return SubstringData(From, Length);
				}
			dp++;
			}
		Length++;
		ptr++;
		}
	// Reached the End of String
	*To = From+Length+1; // Position on the EOS
	return SubstringData(From, Length);
	} // End CString::SubstringData




OrsBool CString::ReadLine(istream& aStream)
	{
	OrsChar ch;

	Clear(); // 20/7/00

	// If the skipWhite flag has been set, then skip any leading whitespace
	if	(SkipWhiteSpacesFlag)
		{
		do
			{
			aStream.get(ch);
			} while(aStream.good() && isspace(ch)) ;
		aStream.putback(ch);
		}

	if	(ReadToDelim(aStream, '\n'))
		{
//    	aStream.get(ch); // drop \n
		if	(!KeepLineFeed)
			{
			ch = GetLastChar();
			if	(ch == '\n' || ch == '\r')
				ExtractLastChar();
			}
		return OrsTRUE;
		};
	return OrsFALSE;
	} // End CString::ReadLine


OrsBool CString::ReadToDelim(istream& aStream, char *aDelimString)
	{
	char ch;
	char *Ptr;
	int FoundDelim = 0;

	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		*BufTailPtr = 0; // EOS
		UsedBytes++;
		UnusedRightBytes--;
		}
  	while(1)
		{
    	aStream.get(ch);
		FoundDelim = 0;
		Ptr = aDelimString;
		while (*Ptr)
			{
			if	(ch == *Ptr)
				{
				aStream.putback(ch);	// Put back trailing ws
				FoundDelim = 1;
				break;
				}
			Ptr++;
			}
		// Abbiamo ancora spazio nel buffer?
		if	(!UnusedRightBytes)
			{
			if	(!GrowBuffer(ResizeBy))
				return OrsFALSE;
			}
    	if( FoundDelim || !aStream.good() )
			break;
		*BufTailPtr = ch;
		BufTailPtr++;
		UsedBytes++;
		UnusedRightBytes--;
  		} //   End while
	*BufTailPtr = 0; // EOS
	return OrsTRUE;
	} // End CString::ReadToDelim


OrsBool CString::ReadToDelim(istream& aStream, char aDelim)
	{
	char ch;

	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		*BufTailPtr = 0; // EOS
		UsedBytes++;
		UnusedRightBytes--;
		}
  	while(1)
		{
    	aStream.get(ch);
		if	(ch == aDelim)
			{
//			aStream.putback(ch);	// Put back trailing ws
			break;
			}
		// Abbiamo ancora spazio nel buffer?
		if	(!UnusedRightBytes)
			{
			if	(!GrowBuffer(ResizeBy))
				return OrsFALSE;
			}
    	if( !aStream.good() )
			{
			*BufTailPtr = 0; // EOS
			if	(UsedBytes != 1) // !empty
				return OrsTRUE;
			else
				return OrsFALSE;
			}
		*BufTailPtr = ch;
		BufTailPtr++;
		UsedBytes++;
		UnusedRightBytes--;
  		} //   End while
	*BufTailPtr = 0; // EOS
	return OrsTRUE;
	} // End CString::ReadToDelim

// Will change all occurances of aChar with its escaped equivalent
void CString::Escape(OrsChar aChar)
	{
	OrsChar *Ptr = Buffer, ToReplace[3]; // , *To

	int StartOff =0; // , EndOff = 0

	ToReplace[0] = '\\';
	ToReplace[1] = aChar;
	ToReplace[2] = 0; // EOS

	while (*Ptr)
		{
		if	(*Ptr == aChar)
			{
			// Found a character to escape
			// Replace it
			Replace((OrsLong)StartOff, 1, ToReplace);

			StartOff += 2;
			Ptr = Buffer + StartOff;
			continue;
			}
		StartOff++;
		Ptr++;
		}
	} // End CString::Escape



// Will change all occurances of the escaped aChar with its equivalent
void CString::UnEscape()
	{
	OrsChar *Ptr = Buffer; // , *To
	int StartOff =0; //, EndOff = 0

	while (*Ptr)
		{
		if	(*Ptr == '\\')
			{
			// Found an escape character to escape
			// Replace it
			Replace((OrsLong)StartOff, 1, "");

			StartOff ++;
			Ptr = Buffer + StartOff;
			continue;
			}
		StartOff++;
		Ptr++;
		}
	} // End CString::UnEscape



// Work on a string that has the format ot the Key=['/"]Value['/"] pairs
void CString::GetValueFromKey(char *Entry, CString &Dest)
	{
	char *Ptr, *Middle, *Start = Buffer, chr;
	int Len = strlen (Entry);

	while (1)
		{
		if	(Ptr = strstr(Start, Entry))
			{
			if	(*(Ptr+Len) == '=')
				{
				// We got it


				Start = Ptr+Len+1;

				// Is it not a substring?
				if	(Ptr != Buffer && *(Ptr -1) != ' ')
					continue; // It was a substring, no good.. keep going

				// Is is a delimited value?
				if	(*Start == '\'')
					{
					Start++;
					Middle = Start;

					while (1)
						{
						Ptr = strchr (Middle, '\'');
						// Is it an escaped character?
						if	(Ptr && *(Ptr-1) == '\\')
							Middle = ++Ptr;
						else
							break; // Found the end
						}
					}
				else
					Ptr = strchr (Start, ' ');

				if	(Ptr)
					{
					chr = *Ptr;
					*Ptr = 0;
					}
				Dest = Start;
				if	(Ptr)
					{
					*Ptr = chr;
					}
				return;
				}
			else
				Start = Ptr+Len; // False alarm was not the correct substring
			}
		else
			{
			Dest.Clear();
			break;
			}
		}
	// Nothing found. We do nothing
	} // End CString::GetQueryEntry




// StoreSize must be used to get the size to allocate where SaveOnMem writes
long CString::SaveOnMem(unsigned char *MemPtr)
	{
	long Written = 0;
	memcpy (MemPtr, &UsedBytes, sizeof(UsedBytes));
	MemPtr += sizeof(UsedBytes);
	memcpy (MemPtr, Buffer, UsedBytes);

	Written = sizeof(UsedBytes) + UsedBytes;
	return Written;
	} // End CString::SaveOnMem

long CString::RestoreFromMem(unsigned char *MemPtr)
	{
	OrsLong StoreSize; // , ret

	memcpy (&StoreSize, MemPtr, sizeof(StoreSize));
	MemPtr += sizeof(StoreSize);
	GrowBuffer(StoreSize);
	memcpy (BufTailPtr, MemPtr, StoreSize);

	return (sizeof(StoreSize) + StoreSize);
	} // End CString::RestoreFromMem




OrsInt CString::Compare(CString& aStringRef)
	{
	if (GetCaseSensitiveFlag())
	    return strcmp(data(), aStringRef.Data());
	else
		return stricmp(data(), aStringRef.Data());
	} // End CString::Compare

OrsInt CString::Compare(const OrsChar *aString)
	{
	if (GetCaseSensitiveFlag())
	    return strcmp(data(), aString);
	else
		return stricmp(data(), aString);
	} // End CString::Compare


OrsInt CString::Compare(OrsChar *aString, OrsInt len)
	{
	if (GetCaseSensitiveFlag())
	    return strncmp(data(), aString, len);
	else
		return strnicmp(data(), aString, len);
	} // End CString::Compare





OrsInt CString::CompareI(CString& aStringRef)
	{
	return stricmp(data(), aStringRef.Data());
	} // End CString::CompareI

OrsInt CString::CompareI(OrsChar *aString)
	{
	return stricmp(data(), aString);
	} // End CString::CompareI


OrsInt CString::CompareI(OrsChar *aString, OrsInt len)
	{
	return strnicmp(data(), aString, len);
	} // End CString::CompareI




// Will return the first character from the buffer
OrsChar CString::GetFirstChar()
	{
	if	(UsedBytes<2)
		return 0;
	return *Buffer;
	} // End CString::GetLastChar

// Will return the character from the buffer at position pos
OrsChar CString::GetChar(long pos)
	{
	if	(UsedBytes < 2 || pos > (UsedBytes-1))
		return 0;

	return *(Buffer+pos);
	} // End CString::GetChar



OrsChar *CString::ExtractFirstWord(char *Delimiters)
	{
	OrsChar* chrPtr = 0, *ptr, *d;
	long From=0, To=0;


	ptr = Data();
	while (*ptr)
		{
		d = Delimiters;

		// Skip leading white space
		while (*d)
			{
			if	(*ptr == *d)
				break;
			d++;
			}
		if	(!*d)
			break; // Found start of word

		ptr ++;
		From ++;
		} // End while

	To = From;


	while (*ptr)
		{
		d = Delimiters;
		// Skip leading white space
		while (*d)
			{
			if	(*ptr == *d)
				break;
			d++;
			}
		if	(*d)
			break; // Found end of word
		ptr ++;
		To ++;
		} // End while

	if	(From != To)
		chrPtr = ExtractFromTo(From, To-1);

	return chrPtr;
	} // End CString::ExtractFirstWord






OrsChar *CString::Strip(stripType st, OrsChar c)
	{
//	OrsInt Ctr = 0;
	OrsLong Ctr;

	OrsChar *StartPtr, *EndPtr;

	Ctr = 0;
	if	(st == leading || st == both)
		{
		StartPtr = Buffer;

		while (*StartPtr)
			{
			if	( *StartPtr != c )
				break;
			Ctr++;
			StartPtr++;

			}

		if	(Ctr)
			CropLeftBy(Ctr);
		}


	Ctr = 0;
	if	( st== trailing || st == both)
		{
		EndPtr = BufTailPtr-1;

		while (EndPtr >= Buffer)
			{
			if	( *EndPtr != c )
				break;
			Ctr++;
			EndPtr--;
			}
		if	(Ctr)
			CropRightBy(Ctr);
		} // End if


	//return OrsNULL;
	return Buffer;
	} // End CString::Strip





OrsChar *CString::Strip(stripType st, OrsChar *s)
	{
//	OrsInt Ctr = 0;
	OrsLong Ctr;

	OrsChar *StartPtr, *EndPtr, *p;

	Ctr = 0;
	if	(st == leading || st == both)
		{
		StartPtr = Buffer;

		while (*StartPtr)
			{
			p = s;
			while (*p)
				{
				if	(*p == *StartPtr)
					break;
				p++;
				}

			if	( *StartPtr != *p )
				break;
			Ctr++;
			StartPtr++;

			}

		if	(Ctr)
			CropLeftBy(Ctr);
		}


	Ctr = 0;
	if	( st== trailing || st == both)
		{
		EndPtr = BufTailPtr-1;

		while (EndPtr >= Buffer)
			{
			p = s;
			while (*p)
				{
//				if	(*p == *StartPtr)
				if	(*p == *EndPtr) // 13/08/2015
					break;
				p++;

				}


			if	( *EndPtr != *p )
				break;

			Ctr++;
			EndPtr--;
			}
		if	(Ctr)
			CropRightBy(Ctr);
		} // End if


	//return OrsNULL;
	return Buffer;
	} // End CString::Strip



// Return the pointer to the found substring
OrsChar *CString::FindSubstring(const char *aSubString)
	{
	char *Ptr;

	// NO CASE HANDLING
	Ptr = strstr(Buffer, aSubString);
	return Ptr;

	} // End CString::FindSubstring

OrsChar *CString::Find(char aChar)
	{
	return strchr(Buffer, aChar);;
	} // End CString::Find




OrsChar *CString::ExtractLastWord(char *Delimiters)
	{
	OrsChar* chrPtr = 0, *ptr, *d;
	long From, To;

	From = To = this->Length()-1;

	// Point to the end of the string
/*	if	(UsedBytes<2)
		ptr = data();
	else
		ptr = (BufTailPtr-1);
*/
	ptr = data()+To;

	while (ptr >= data())
		{
		d = Delimiters;

		// Skip leading white space
		while (*d)
			{
			if	(*ptr == *d)
				break;
			d++;
			}
		if	(!*d)
			break; // Found start of word

		ptr --;
		To --;
		} // End while

	From = To;

	ptr--;
	From--;
	while (ptr >= data())
		{
		d = Delimiters;
		// Skip leading white space
		while (*d)
			{
			if	(*ptr == *d)
				break;
			d++;
			}
		if	(*d)
			break; // Found end of word
		ptr --;
		From --;
		} // End while

	if	(From != To)
	{
		if (From == 0)
			chrPtr = ExtractFromTo(From, To);
		else
			chrPtr = ExtractFromTo(From+1, To);
	}

	return chrPtr;
	} // End CString::ExtractLastWord




bool CString::StartsWith(const OrsChar * aString)
	{
	int len = strlen(aString);
	int ret;
	if (GetCaseSensitiveFlag())
	    ret =  strncmp(data(), aString, len);
	else
		ret = strnicmp(data(), aString, len);

	if (ret)
		return false;
	else
		return true;

	} // End CString::StartsWith

bool CString::StartsWithFrom(const OrsChar * aString, long from)
{
	int len = strlen(aString);
	int ret;

	if (GetCaseSensitiveFlag())
	    ret = strncmp(data()+from, aString, len);
	else
		ret = strnicmp(data()+from, aString, len);
	if (ret)
		return false;
	else
		return true;
}



OrsBool CString::isEqual(OrsChar *aString)
	{
	int ret;
	if (GetCaseSensitiveFlag())
		ret =  strcmp(data(), aString);
	else
		ret = stricmp(data(), aString);

	if (ret)
		return false;
	else
		return true;
	} // End CString::Compare

OrsBool CString::isEqual(const OrsChar *aString)
{
	return isEqual((OrsChar *)aString);
}


void	CString::removeCharacterOccurances(OrsChar chr)
{
	OrsChar *source, *dest;
	source = dest = data();
	int ctr=0;

	while (*source)
	{
		if (*source != chr)
			{
			*dest = *source;
			dest++;
			}
		else
		{
			ctr++;
		}
		source ++;
	}
	*dest=0; // EOS
	UsedBytes-=ctr;
	UnusedRightBytes+=ctr;
}

bool CString::leftPadding (char filler, int maxStringLength)
{
	int padLength;
	if ((UsedBytes-1) >= maxStringLength)
		return true;

	padLength = maxStringLength-(UsedBytes-1);
	CString s;
	for (int i = 0; i < padLength; i++)
		s.AppendChar(filler);

	return PrependString(s.data());
}
bool CString::rightPadding (char filler, int maxStringLength)
{
	int padLength;
	if ((UsedBytes-1) >= maxStringLength)
		return true;

	padLength = maxStringLength-(UsedBytes-1);
	CString s;
	for (int i = 0; i < padLength; i++)
		s.AppendChar(filler);

	return AppendString(s.data());
}







OrsBool CString::ReadToDelim(CFile * aFilePtr, char *aDelimString)
	{
	char ch;
	char *Ptr;
	int FoundDelim = 0;

	// Esiste un buffer
	if	(!Buffer)
		{
		if	(!MakeNewBuffer(ResizeBy))
			return OrsFALSE;
		*BufTailPtr = 0; // EOS
		UsedBytes++;
		UnusedRightBytes--;
		}
  	while(1)
		{
    	if	(!aFilePtr->Read(ch))
			return OrsFALSE;
		FoundDelim = 0;
		Ptr = aDelimString;
		while (*Ptr)
			{
			if	(ch == *Ptr)
				{
				aFilePtr->UnRead(ch);	// Put back trailing ws
				FoundDelim = 1;
				break;
				}
			Ptr++;
			}
		// Abbiamo ancora spazio nel buffer?
		if	(!UnusedRightBytes)
			{
			if	(!GrowBuffer(ResizeBy))
				return OrsFALSE;
			}
    	if( FoundDelim || aFilePtr->Eof() )
			break;
		*BufTailPtr = ch;
		BufTailPtr++;
		UsedBytes++;
		UnusedRightBytes--;
  		} //   End while
	*BufTailPtr = 0; // EOS
	return OrsTRUE;
	} // End CString::ReadToDelim




//OrsBool CString::Resize(OrsInt NewSize)
OrsBool CString::Resize(OrsLong NewSize)
	{
	if	(NewSize < 0 )
		{
	    SignalAnError(__FILE__, __LINE__, "CString::Resize: NewSize deve essere > 0");
		return OrsFALSE;
		}
	if	(NewSize == 0 )
		{
		Clear();
		return OrsTRUE;
		}

//	OrsInt Size = NewSize + 1 ;
	OrsLong Size = NewSize + 1 ;
	OrsChar *OrgBufPtr = Buffer;

	Buffer = (OrsChar *) realloc(Buffer, Size+1);
	if	(!Buffer)
		{
	    SignalAnError(__FILE__, __LINE__, "CString ralloc failed");
		return OrsFALSE;
		}

	// Reposition pointers
	if	(OrgBufPtr != Buffer)
		BufTailPtr = Buffer + UsedBytes -1;  // The Buffer address has be changed

	if	((Buffer+Size) <= BufTailPtr)
		{
		BufTailPtr = Buffer+Size-1;
		*BufTailPtr=0; // EOS
		UsedBytes=Size;
		UnusedRightBytes = 0;
		}
	else
		{
		UnusedRightBytes = Size - UsedBytes;
		}
	BufferSize = Size;
	return OrsTRUE;
	} // End Resize

long CString::getBufferSize()
{
	return BufferSize;
}
/*
* Will free the previous buffer if it exists and allcoate a new one
**/
OrsBool CString::ResizeNew(OrsLong NewSize)
	{
	if	(NewSize < 0 )
		{
	    SignalAnError(__FILE__, __LINE__, "CString::Resize: NewSize deve essere > 0");
		return OrsFALSE;
		}
	if	(NewSize == 0 )
		{
		Clear();
		return OrsTRUE;
		}
	if (Buffer)
		free (Buffer);
	return MakeNewBuffer(NewSize);
	} // End ResizeClean







bool CString::Split(ATTValVector <CString *> &OutStrings, char *aStrSep)
{
	return Split(OutStrings, (const char *)aStrSep);
}

bool CString::Split(ATTValVector <CString *> &OutStrings, char aSep)
	{

	char *ptr, *ptr1;
	CString *aNewString;

	ptr = Buffer;


	if	(OutStrings.Length())
		OutStrings.DeleteAndClear();

	while (1)
		{
		ptr1 = strchr(ptr, aSep);
		if	(ptr1)
			{
			*ptr1=0;
			aNewString = new CString (ptr);
			if	(!aNewString || (OutStrings.Add(aNewString) == -1))
				{
				OutStrings.DeleteAndClear();
				return OrsFALSE;
				}
			*ptr1 = aSep;
			ptr = ++ptr1;
			}
		else
			break;
		}

	aNewString = new CString (ptr);
	if	(!aNewString || (OutStrings.Add(aNewString) == -1))
		{
		OutStrings.DeleteAndClear();
		return OrsFALSE;
		}

	return OrsTRUE;
	} // End CString::Split


bool CString::Split(ATTValVector <CString *> &OutStrings, const char *aStrSep)
	{
	char *ptr, *ptr1, chr;
	CString *aNewString;
//	int ret;

	ptr = Buffer;
	int len = strlen(aStrSep);

	if	(OutStrings.Length())
		OutStrings.DeleteAndClear();

	while (1)
		{
		ptr1 = strstr(ptr, aStrSep);
		if	(ptr1)
			{
			chr = *ptr1;
			*ptr1=0;
			aNewString = new CString (ptr);
			if	(!aNewString || (OutStrings.Add(aNewString) == -1))
				{
				OutStrings.DeleteAndClear();
				return OrsFALSE;
				}
			*ptr1 = chr;
			ptr = ptr1+len;
			}
		else
			break;
		}

	aNewString = new CString (ptr);
	if	(!aNewString || (OutStrings.Add(aNewString) == -1))
		{
		OutStrings.DeleteAndClear();
		return OrsFALSE;
		}

	return OrsTRUE;
	} // End CString::Split



bool CString::SplitIgnoreWithin(ATTValVector <CString *> &OutStrings, char *aStrSep, char ignoreStart, char ignoreEnd)
	{
	char *ptr, *ptr1; // , chr
	CString *aNewString;
//	int ret;
	bool ignore = false;

	ptr = Buffer;
	int len = strlen(aStrSep);

	if	(OutStrings.Length())
		OutStrings.DeleteAndClear();
//
//	while (1)
//		{
//		ptr1 = strstr(ptr, aStrSep);
//		if	(ptr1)
//			{
//			chr = *ptr1;
//			*ptr1=0;
//			aNewString = new CString (ptr);
//			if	(!aNewString || (OutStrings.Add(aNewString) == -1))
//				{
//				OutStrings.DeleteAndClear();
//				return OrsFALSE;
//				}
//			*ptr1 = chr;
//			ptr = ptr1+len;
//			}
//		else
//			break;
//		}
//
//	aNewString = new CString (ptr);
//	if	(!aNewString || (OutStrings.Add(aNewString) == -1))
//		{
//		OutStrings.DeleteAndClear();
//		return OrsFALSE;
//		}

	ptr1 = ptr;
	while (*ptr++)
	{
		if (ignore)
		{
			if (*ptr == ignoreEnd)
				ignore = false;
			continue;
		}
		if (*ptr == ignoreStart)
		{
			ignore = true;
			continue;
		}

		if (!strncmp(ptr, aStrSep, len))
		{
			// return ptr-Buffer;

			// Found a string
			char chr = *ptr;
			*ptr = 0;

			aNewString = new CString (ptr1);
			if	(!aNewString || (OutStrings.Add(aNewString) == -1))
				{
				OutStrings.DeleteAndClear();
				return OrsFALSE;
				}
			*ptr = chr;
			ptr += len;
			ptr1 = ptr;
		}
	} // End while
	// Facciamo l'ultinmo
	aNewString = new CString (ptr1);
	if	(!aNewString || (OutStrings.Add(aNewString) == -1))
		{
		OutStrings.DeleteAndClear();
		return OrsFALSE;
		}

	return OrsTRUE;
	} // End CString::Split


long CString::IndexSubString(const char *aSubString)
	{
	char *Ptr;
	// NO CASE HANDLING
	Ptr = strstr(Buffer, aSubString);
	if	(Ptr != 0)
		return Ptr - Buffer;
	else
		return -1;
	} // End CString::IndexSubString







long CString::IndexSubStringIgnoreWithin(const char *aSubString, char ignoreStart, char ignoreEnd)
	{
	bool ignore = false;
	int len = strlen(aSubString);

	char *ptr;
	// NO CASE HANDLING

//	Ptr = strstr(Buffer, aSubString);
//	if	(Ptr != 0)
//		return Ptr - Buffer;
//	else
//		return -1;

	ptr = Buffer;

	while (*ptr++)
	{
		if (ignore)
		{
			if (*ptr == ignoreEnd)
				ignore = false;
			continue;
		}
		if (*ptr == ignoreStart)
		{
			ignore = true;
			continue;
		}

		if (!strncmp(ptr, aSubString, len))
			return ptr-Buffer;
	}

	return -1;


	} // End CString::IndexSubStringIgnoreWithin

/**
 * returns the last substring found
 */
long CString::IndexLastSubStringIgnoreWithin(const char *aSubString, char ignoreStart, char ignoreEnd)
	{
	bool ignore = false;
	int len = strlen(aSubString);

	char *ptr;
	long pos = -1;
	// NO CASE HANDLING


	ptr = Buffer;

	while (*ptr++)
	{
		if (ignore)
		{
			if (*ptr == ignoreEnd)
				ignore = false;
			continue;
		}
		if (*ptr == ignoreStart)
		{
			ignore = true;
			continue;
		}

		if (!strncmp(ptr, aSubString, len))
			//return ptr-Buffer;
			pos = ptr-Buffer;
	}

	return pos;


	} // End CString::IndexSubStringIgnoreWithin




// Change a substring with another substring within the string
void CString::ChangeTo(OrsChar *aStringFrom, OrsChar *aStringTo)
	{
	OrsChar *Ptr = Buffer, *To;

	int aStringToLen = strlen(aStringTo);
	int StartOff =0, EndOff = 0; // , Off=0

	while (*Ptr)
		{
		To = aStringFrom;
		EndOff =0;
		StartOff = Ptr-Buffer;
		while (*To)
			{
			if	(*Ptr != *To)
				break;
			EndOff ++;
			Ptr++;
			To++;
			}
		if	(!*To)
			{
			// Found a substring
			// Replace it
			Replace((OrsLong)StartOff, (OrsLong)EndOff, aStringTo);

			Ptr = Buffer + StartOff + aStringToLen;
			continue;
			}
		Ptr++;
		}
	} // CString::ChangeTo


void CString::ChangeTo(OrsChar aCharFrom, OrsChar aCharTo)
	{
	OrsChar *Ptr = Buffer;

	while (*Ptr++)
		if	(*Ptr == aCharFrom)
			*Ptr = aCharTo;
	} // CString::ChangeTo

/*
 * Sostituisci ognuno dei caratteri indicati in charactersToBeChanged con il carattere indicato della stringa sottostante
 */
void CString::ChangeTo(OrsChar *charactersToBeChanged, OrsChar changeTo)
	{
	OrsChar *Ptr = Buffer; // , *To
	char *toChange;

	while (*Ptr)
		{
		toChange =  charactersToBeChanged;
		while (*toChange)
		{
			if (*Ptr == *toChange)
			{
				*Ptr = changeTo;
				break;
			}
			toChange++;
		}

		Ptr++;
		}
	} // CString::ChangeTo

/**
 * returns the last substring found
 */
long CString::IndexLastSubString(const char *aSubString)
	{
	//bool ignore = false;
	int len = strlen(aSubString);

	char *ptr;
	long pos = -1;
	// NO CASE HANDLING

	ptr = Buffer;
	while (*ptr) // 24/09/2015
	{
		if (!strncmp(ptr, aSubString, len))
			pos = ptr-Buffer;
		ptr++;  // 24/09/2015
	}
	return pos;
	} // End CString::IndexLastSubString

// 01/07/2016
// endPos excluded
long CString::IndexLastSubStringUntil(const char *aSubString, int endPos)
	{
	int len = strlen(aSubString);

	if (endPos < 0 ||  endPos >= this->Length())
		return -1; // ERROR

	char *ptr;
	long pos = -1;

	ptr = Buffer;
	int ctr=0;
	while (*ptr && ctr < endPos)
	{
		if (!strncmp(ptr, aSubString, len))
			pos = ptr-Buffer;
		ptr++;
		ctr++;
	}
	return pos;
	} // End CString::IndexLastSubString





bool CString::EndsWith(const OrsChar * aString)
	{
	int len = strlen(aString);

	int pos = this->UsedBytes - 1 - len;
	if (pos < 0)
		return false;

	return StartsWithFrom(aString, pos);

	} // End CString::StartsWith







long CString::IndexSubStringCaseInsensitive(const char *aSubString)
	{
	char *srcPtr = Buffer;
	if (!*srcPtr && !*aSubString)
		return 0; // Due stringhe vuote

	const char *toFindPtr;
	long pos = 0;
	char chrL, chrR;
	long startPos;

	while(*srcPtr)
	{
		startPos = pos;
		toFindPtr = aSubString;
		while (*toFindPtr)
		{
			chrL = *srcPtr;
			chrR = *toFindPtr;

			toFindPtr++;
			srcPtr++;
			pos++;

			if (chrL == chrR) // same case and same character?
				continue;
			// make lower
			if (chrL >= 'A' && chrL <= 'Z')
				chrL += 0x20;
			if (chrR >= 'A' && chrR <= 'Z')
				chrR += 0x20;

			if (chrL != chrR) // same case?
			{
				// Riprendiamo da dove abbiamo trovato la diversita'. A meno che non sia il primo carattere
				if ((toFindPtr-1) != aSubString)
				{
					toFindPtr--;
					srcPtr--; //
					pos--;
				}
				break;
			}
		}
		if (!*toFindPtr)
			return startPos;

	} // End while *srcPtr

	return -1; // not found

	} // End CString::IndexSubString

// from starts at 0
long CString::IndexSubStringFrom(const char *aSubString, int from)
	{
	char *Ptr;

	// Something to crop?
	if	(from >= UsedBytes)
		{
	    SignalAWarning(__FILE__, __LINE__, "IndexSubStringFrom: range too big");
		return OrsTRUE;
		}


	// NO CASE HANDLING
	Ptr = strstr(Buffer+from, aSubString);
	if	(Ptr != 0)
		return Ptr - Buffer;
	else
		return -1;
	} // End CString::IndexSubString



// from starts at 0
long CString::IndexSubStringFrom(const char *aSubString, int from, int direction)
	{
	char *Ptr;

	// Something to crop?
	if	(from >= UsedBytes)
		{
	    SignalAWarning(__FILE__, __LINE__, "IndexSubStringFrom: range too big");
		return OrsTRUE;
		}

	// NO CASE HANDLING
	if (direction == forward)
	{
		Ptr = strstr(Buffer+from, aSubString);
		if	(Ptr != 0)
			return Ptr - Buffer;
		else
			return -1;
	}
	else
	{
		// search backwards
		int subStringLen = strlen(aSubString);

		char *srcPtr = Buffer+from;
		if (!*srcPtr && !*aSubString)
			return 0; // Due stringhe vuote

		const char *toFindPtr;
		long pos = from;
		char chrL, chrR;
		long startPos;

		while(*srcPtr)
		{
			startPos = pos;
			toFindPtr = aSubString+subStringLen-1;

			int len = subStringLen;
			while (len--)
			{
				chrL = *srcPtr;
				chrR = *toFindPtr;

				toFindPtr--;
				srcPtr--;
				pos--;

				if (chrL == chrR)
					continue;
				// Riprendiamo da dove abbiamo trovato la diversita'. A meno che non sia il primo carattere
				if ((subStringLen-1) != len)
				{
					toFindPtr++;
					srcPtr++; //
					pos++;
				}
				break;
			}
			if (len == -1)
				return pos+1;
		} // End while *srcPtr
		return -1; // not found
	}
	return -1; // not found
	} // End CString::IndexSubString


// from starts at 0
long CString::IndexCharFrom(char aChar, int from, int direction)
	{
	char *ptr;
	int pos = from;

	// Something to crop?
	if	(from >= UsedBytes)
		{
	    SignalAWarning(__FILE__, __LINE__, "IndexCharFrom: range too big");
		return OrsTRUE;
		}


	// NO CASE HANDLING
	ptr = Buffer+from;
	if (direction == backward)
	{
		while (pos >= 0)
		{
			if (*ptr == aChar)
				return pos;
			pos--;
			ptr--;
		}
	}
	else
		while (*ptr)
		{
			if (*ptr == aChar)
				return pos;
			pos++;
			ptr++;
		}

	return -1;
	} // End CString::IndexCharFrom


OrsBool CString::ReadLineWithPrefixedMaxSize(CFile * aFilePtr)
{
	bool retb;
	try{

		//long long lastPos = aFilePtr->getCurPosition();

		//retb = aFilePtr->ReadLine(Buffer, BufferSize);
//		if (retb)
//			return retb;
//		retb = aFilePtr->SeekToLarge(lastPos);
//		if (lastPos == -1 || !retb)
//		{
//		    SignalAnError(__FILE__, __LINE__, "Could not reRead!");
//			return false;
//		}
//		retb = ReadLine(aFilePtr);

		long read = aFilePtr->readFixedLenLine(Buffer, BufferSize);
		if (!read)
			return true; // OK read up to newline
		if (read == -1)
			return false; // Read fails

		// Read only partially, must keep on reading

//SignalAWarning(__FILE__, __LINE__, "ReadLineWithPrefixedMaxSize: read failed (probably buffer too small). Rereading with no fixed size!");

		// Keep on reading without repositioning

		UsedBytes  = BufferSize;
		UnusedRightBytes  = 0;
		BufTailPtr = Buffer + BufferSize-1;

		if	(!GrowBuffer(ResizeBy))
			return OrsFALSE;

		retb = keepReadingLine(aFilePtr);
//		if (!retb)
//			SignalAWarning(__FILE__, __LINE__, "ReadLineWithPrefixedMaxSize: read failed after reread");
//printf ("Buffer = '%s'", Buffer);
		return retb;

		// Usedbytes is left unchanged...This function has to be quick...
	} catch (GenericError e) {
		std::cout << e.errorMessage;
		return OrsFALSE;
	}

} // End CString::ReadLineWithPrefixedMaxSize

OrsBool CString::ReadLine(CFile * aFilePtr)
	{
	OrsChar ch;
	OrsLong ret;

	Clear(); // 20/7/00

	// If the skipWhite flag has been set, then skip any leading whitespace
	if	(SkipWhiteSpacesFlag)
		{
		do
			{
			ret = aFilePtr->Read(ch);
			} while(!aFilePtr->Eof() && isspace(ch)) ;
		if	(ret)
			aFilePtr->UnRead(ch);
		}

	if	(ReadToDelim(aFilePtr, '\n'))
		{
		if	(!KeepLineFeed)
			{
			ch = GetLastChar();
			if	(ch == '\n' || ch == '\r')
				ExtractLastChar();
			}
		else
			AppendChar('\n'); // 31/08/2015


		return OrsTRUE;
		};
	return OrsFALSE;
	} // End CString::ReadLine


OrsBool CString::keepReadingLine(CFile * aFilePtr)
	{
	OrsChar ch;
	OrsLong ret;

	// If the skipWhite flag has been set, then skip any leading whitespace
	if	(SkipWhiteSpacesFlag)
		{
		do
			{
			ret = aFilePtr->Read(ch);
			} while(!aFilePtr->Eof() && isspace(ch)) ;
		if	(ret)
			aFilePtr->UnRead(ch);
		}
	if	(ReadToDelim(aFilePtr, '\n'))
		{
		if	(!KeepLineFeed)
			{
			ch = GetLastChar();
			if	(ch == '\n' || ch == '\r')
				ExtractLastChar();
			}
		return OrsTRUE;
		};
	return OrsFALSE;
	} // End CString::keepReadingLine






//OrsBool CString::AppendString(const OrsChar *aString, OrsLong Size)
//{
//	return AppendString((OrsChar *)aString, Size);
//}




//bool	CString::assign(const OrsChar * aString)
//{
//	Clear(); // Empty buffer
//	if (!*aString)
//		return true;
//	return AppendString(aString);
//}



//OrsBool CString::AppendString(const OrsChar *aString)
//	{
//	if (!aString)
//		return false;
//	int Size = strlen(aString);
//	return AppendString((char *)aString, Size);
//
//	} // End CString::AppendString








// Will add a string to the END of the buffer

//OrsBool CString::AppendString(OrsLong Size, OrsChar *aString)
OrsBool CString::AppendString(CString * pStr)
	{

	// Ensure length of substring <= string
//	int Size = strlen(aString);
//	Size = SubStringLen <= Size ? SubStringLen : Size;

	int count = pStr->Length();
	if (!pStr)
		return false;

	// Esiste un buffer
	if	(!Buffer)
		{
		long SizeToAllocate = ResizeBy;
		while (SizeToAllocate < count)
			SizeToAllocate += ResizeBy;

		if	(!MakeNewBuffer(SizeToAllocate+1)) // EOS
			return OrsFALSE;
		}

	// Abbiamo ancora spazio nel buffer?
	if	(!UnusedRightBytes || UnusedRightBytes < count)
		{
		long SizeToAllocate = ResizeBy;
		while (SizeToAllocate < count)
			SizeToAllocate += ResizeBy;

		if	(!GrowBuffer(SizeToAllocate))
			return OrsFALSE;
		}

	char * aString = pStr->data();
	MACRO_COPY_FAST(count);

//	if (Size == 2)
//		*((short*)BufTailPtr) = *((short*)pStr->data()); // 24/03/2015
//	else
//		memcpy(BufTailPtr, pStr->data(), Size); // aString

	// Beware the EOS
	if	(!UsedBytes)
		{
		UsedBytes += count+1;
		UnusedRightBytes -= count+1;
		BufTailPtr += count;
		}
	else
		{ // We had an EOS
		UsedBytes += count;
		UnusedRightBytes -= count;
		BufTailPtr += count;
		}

	*BufTailPtr = 0; // EOS

	return OrsTRUE;
	} // End CString::AppendString


long CString::getBufferSubStringLength()
{
	//return BufferSubStringSize-1;

	return BufferSubStringLength;
}












OrsBool CString::IsEmpty()
	{
//	return (UsedBytes-1)? OrsFALSE : OrsTRUE;
	char *ptr = Buffer;
	while (*ptr)
		{
		if	(*ptr != ' ' &&
			*ptr != '\t' &&
			*ptr != '\n' &&
			*ptr != '\r' &&
			*ptr != '\v' )
			return OrsFALSE;
		ptr++;
		}
	return OrsTRUE;
	} // End CString::IsEmpty


// Will add a string to the END of the buffer

// DA RIMUOVERE EVENTUALMENTE in quanto sostituita da OrsBool CString::AppendString(OrsLong Size, OrsChar *aString)
// Questo perche' se i due argomenti sono funzioni si elabora da destra a sinistra
OrsBool CString::AppendString(OrsChar *aString, OrsLong count)
	{
	if (!count )
	{
		printf ("\nSkip Append string empty string");
		return true;
	}
	if (count < 1)
	{
		printf ("\nAppend string with negative number: %ld", count);
		return false;
	}
	// Esiste un buffer
	if	(!Buffer)
		{
		long SizeToAllocate = ResizeBy;
		while (SizeToAllocate < count)
			SizeToAllocate += ResizeBy;

		if	(!MakeNewBuffer(SizeToAllocate+1)) // EOS
			return OrsFALSE;
		}

	// Abbiamo ancora spazio nel buffer?
	else if	(!UnusedRightBytes || UnusedRightBytes < count)
		{
		long SizeToAllocate = ResizeBy;
		while (SizeToAllocate < count)
			SizeToAllocate += ResizeBy;

		if	(!GrowBuffer(SizeToAllocate))
			return OrsFALSE;
		}

	MACRO_COPY_FAST(count);
//	switch (count)
//		{
//		case 1:
//			*(BufTailPtr) = *(aString);
//			break;
//		case 2:
//			*((short*)BufTailPtr) = *((short*)aString);
//			break;
//		case 3:
//			*((short*)BufTailPtr) = *((short*)aString);
//			*(BufTailPtr+2) = *(aString+2);
//			break;
//		case 4:
//			*((int*)BufTailPtr) = *((int*)aString);
//			break;
//		case 5:
//			*((int*)BufTailPtr) = *((int*)aString);
//			*(BufTailPtr+4) = *(aString+4);
//			break;
//		case 6:
//			*((int*)BufTailPtr) = *((int*)aString);
//			*((short*)BufTailPtr+2) = *((short*)aString+2);
//			break;
//		case 7:
//			*((int*)BufTailPtr) = *((int*)aString);
//			*((short*)BufTailPtr+2) = *((short*)aString+2);
//			*(BufTailPtr+6) = *(aString+6);
//			break;
//		case 8:
//			*((long long*)BufTailPtr) = *((long long*)aString);
//			break;
//		case 10:
//			*((long long*)BufTailPtr) = *((long long*)aString);
//			*((short*)BufTailPtr+4) = *((short*)aString+4);
//			break;
//		case 12:
//			*((long long*)BufTailPtr) = *((long long*)aString);
//			*((int*)BufTailPtr+2) = *((int*)aString+2);
//			break;
//		default:
//			memcpy(BufTailPtr, aString, count);
//			break;
//		}



//	if (count == 2)
//		*((short*)BufTailPtr) = *((short*)aString); // 24/03/2015
//	else
//		memcpy(BufTailPtr, aString, count);




	// Beware the EOS
	if	(!UsedBytes)
		{
		UsedBytes += count+1;
		UnusedRightBytes -= count+1;
		BufTailPtr += count;
		}
	else
		{ // We had an EOS
		UsedBytes += count;
		UnusedRightBytes -= count;
		BufTailPtr += count;
		}

	*BufTailPtr = 0; // EOS

	return OrsTRUE;
	} // End


//OrsBool CString::MakeNewBuffer(OrsInt Size)
OrsBool CString::MakeNewBuffer(OrsLong Size)
	{
	Buffer = (OrsChar *)malloc (Size);
	if	(!Buffer)
		{
	    SignalAnError(__FILE__, __LINE__, "CString malloc failed for size %d", Size);
		return OrsFALSE;
		}
	BufferSize = Size;
	UsedBytes = 0;
	UnusedRightBytes=Size;
	BufTailPtr = Buffer;
	return OrsTRUE;
	} // End CString::MakeNewBuffer


void CString::init()
{
	Buffer = OrsNULL;
	BufferSubString = OrsNULL;
	CaseSensitiveFlag = OrsFALSE;
	//SkipWhiteSpacesFlag = OrsTRUE;
	SkipWhiteSpacesFlag = OrsFALSE; // 25/05/2009
	KeepLineFeed = OrsTRUE;
	ResizeBy = CSTRING_RESIZE_DEFAULT;

//	Clear();
//	MakeNewBuffer(ResizeBy);

	BufferSubStringSize=0;

//	AppendString("");
	//long SizeToAllocate = ResizeBy+1;
	Buffer = (OrsChar *)malloc (ResizeBy);
	if	(!Buffer)
		{
		SignalAnError(__FILE__, __LINE__, "CString malloc failed for size %d", ResizeBy);
		return;
		}
	BufferSize = ResizeBy;
	UnusedRightBytes=ResizeBy-1;
	BufTailPtr = Buffer;
	UsedBytes = 1;
	*BufTailPtr = 0; // EOS

}









OrsBool CString::AppendString(OrsChar *aString)
	{
	if (!aString || !*aString)
		return false;  // in case of null values
	int Size = strlen(aString);
	return AppendString(aString, Size);

	} // End CString::AppendString









OrsBool CString::operator = (OrsChar aChar)
	{
//	Clear(); // TO DO use Reset inline
//	return AppendChar(aChar);

	//ResetNoResize();
//	if	(BufferSubString)
//		free (BufferSubString);
//	BufferSubStringSize=0;
	MACRO_RESET_NO_RESIZE

//	if (Buffer)
//		*Buffer=0;
//	UsedBytes = 1;
//	UnusedRightBytes = BufferSize-1;
//	BufTailPtr = Buffer;

//	*BufTailPtr = aChar;
//	BufTailPtr++;
//	*BufTailPtr = 0;
//	UsedBytes=2;
//	UnusedRightBytes -=2;
	MACRO_ASSIGN_CHAR
	return true;

	}


OrsBool CString::assign(OrsChar *aString, OrsLong Size)
	{
	//ResetNoResize();
//	MACRO_RESET_NO_RESIZE
	if (Buffer)
		*Buffer=0;
	BufTailPtr = Buffer;
	UsedBytes = 1;
	UnusedRightBytes = BufferSize-1;


//	if	(BufferSubString)
//		free (BufferSubString);
//	BufferSubStringSize=0;
//	if (Buffer)
//		*Buffer=0;
//	BufTailPtr = Buffer;
//	UsedBytes = 1;
//	UnusedRightBytes = BufferSize-1;

	if (!Size)
		return true;
	if (Size == 1)
	{
		//return AppendChar(*aString); // 24/03/2015
		MACRO_ASSIGN_ASTRING_CHAR
		return true;
	}
	return AppendString(aString, Size);
	} // End CString::Equals

OrsBool CString::assign(OrsLong Size, OrsChar *aString )
	{
//	ResetNoResize();
	MACRO_RESET_NO_RESIZE
	if (!Size)
		return true;
	if (Size == 1)
	{
	//	return AppendChar(*aString); // 24/03/2015
		MACRO_ASSIGN_ASTRING_CHAR
		return true;
	}
	return AppendString(aString, Size);
	} // End CString::assign




OrsBool CString::assign(CString *sPtr)
	{
//	ResetNoResize();
	MACRO_RESET_NO_RESIZE
	if (!sPtr->Length())
		return true;
	return AppendString(sPtr);
	} // End CString::assign


bool	CString::assign(OrsChar * aString)
{
/*
	 24/03/2015 Non necessario veramente
	if	(BufferSubString)	// Should use macro really
		free (BufferSubString);
	BufferSubStringSize=0;
*/
//	if (Buffer)
//		*Buffer=0;
//	UsedBytes = 1;
//	UnusedRightBytes = BufferSize-1;
//	BufTailPtr = Buffer;
	MACRO_RESET_NO_RESIZE


	if (!aString || !*aString)
		return true; // Do nothing if empty string
	if (!*(aString+1))
	{
	//	return AppendChar(*aString); // 24/03/2015
		MACRO_ASSIGN_ASTRING_CHAR
		return true;
	}

	long len = strlen(aString);
	return AppendString(aString, len);
}

OrsBool CString::operator = (const OrsChar *aString)
	{
	MACRO_RESET_NO_RESIZE

	if (!aString)
		return false;  // in case of null values
/*
 24/03/2015 Non necessario veramente
	if	(BufferSubString)	// Should use macro really
		free (BufferSubString);
	BufferSubStringSize=0;
*/
//	if (Buffer)
//		*Buffer=0;
//	UsedBytes = 1;
//	UnusedRightBytes = BufferSize-1;
//	BufTailPtr = Buffer;

	if (!*aString)
		return true; // Do nothing if empty string
	if (!*(aString+1))
	{
	//	return AppendChar(*aString); // 24/03/2015
		MACRO_ASSIGN_ASTRING_CHAR
		return true;
	}

	long len = strlen(aString);
	return AppendString((char *)aString, len);
	}



CString& CString::operator = (CString& aStringRef)
	{
	MACRO_RESET_NO_RESIZE
//	Clear(); // Empty buffer
	AppendString(aStringRef.Data(), aStringRef.Length());
	return *this;

	} // End CString::operator =


// 19/04/2013
OrsBool CString::Replace(const char *fromString, const char *toString, int occurance ) // 0 = all
	{
	int Len = strlen(fromString);
	int toStringLen = strlen(toString);
	int occCtr=0;

	OrsLong From = IndexSubString(fromString);
	if (From == -1)
		return true; // not found

	while (true)
	{
		occCtr++;
		if (occurance && occCtr != occurance)
		{
			From += Len;
			continue;
		}
		else
		{

		OrsLong To = From + Len - 1;

		if	(Len > 0)
			{
			if	(!ExtractFromTo(From, To))
				return OrsFALSE;
			}
		InsertStringAt(toString, From);

		From+=toStringLen;
		} // end else

		From = IndexSubStringFrom(fromString, From);
		if (From == -1)
			return true; // finished
	}

	return true;


	} // End CString::Replace





OrsBool CString::ReadBytes(CFile *aFilePtr, int nBytes)
	{
	char ch;
	int Size = nBytes+1;// +1=LF

//	ResetNoResize();
	MACRO_RESET_NO_RESIZE

	// Abbiamo ancora spazio nel buffer?
	 if	(!UnusedRightBytes || UnusedRightBytes < Size)
		{
		long SizeToAllocate = ResizeBy;
		while (SizeToAllocate < Size)
			SizeToAllocate += ResizeBy;

		if	(!GrowBuffer(SizeToAllocate))
			return OrsFALSE;
		}


	if (!aFilePtr->Read(BufTailPtr, nBytes))
	{
		SignalAnError(__FILE__, __LINE__, "Could not read %d bytes", nBytes);
		return OrsFALSE;
	}

	BufTailPtr+=nBytes;
	UsedBytes+=nBytes;
	UnusedRightBytes-=nBytes;

	*BufTailPtr = 0; // EOS
	return OrsTRUE;
	} // End CString::ReadBytes




void CString::ResetNoResize()
	{
/* 30/03/2015
	if	(BufferSubString)
		free (BufferSubString);
	BufferSubStringSize=0;
*/
	if	(BufferSubString)	// 30/03/2015
		*BufferSubString = 0;
	BufferSubStringLength=0;

	if (Buffer)
		*Buffer=0;
	UsedBytes = 1;
	UnusedRightBytes = BufferSize-1;
	BufTailPtr = Buffer;

	} // End ResetNoResize



void CString::Clear()
	{
	if	(Buffer)
		free (Buffer);
	if	(BufferSubString)
		free (BufferSubString);

//	Reset();
	Buffer = OrsNULL;
	BufferSize = 0;

	BufferSubString = OrsNULL;
	BufferSubStringSize=0;

	UsedBytes = 0;
	UnusedRightBytes = 0;
	BufTailPtr = OrsNULL;
	ResizeBy = CSTRING_RESIZE_DEFAULT;


//	AppendString("");
	long SizeToAllocate = ResizeBy+1;
	Buffer = (OrsChar *)malloc (SizeToAllocate);
	if	(!Buffer)
		{
		SignalAnError(__FILE__, __LINE__, "CString malloc failed for size %d", SizeToAllocate);
		return;
		}
	BufferSize = SizeToAllocate;
	UnusedRightBytes=SizeToAllocate-1;
	BufTailPtr = Buffer;
	UsedBytes = 1;
	*BufTailPtr = 0; // EOS
	} // end Clear


OrsBool CString::operator = (OrsChar *aString)
	{
	if (!aString)
		return false;  // in case of null values

	ResetNoResize();
	if (!*aString)
		return true;

//	if	(BufferSubString)	// Should use macro really
//		free (BufferSubString);
//	BufferSubStringSize=0;
//
//	if (Buffer)
//		*Buffer=0;
//	UsedBytes = 1;
//	UnusedRightBytes = BufferSize-1;
//	BufTailPtr = Buffer;

	if (!*(aString+1))
		return AppendChar(*aString); // 24/03/2015

	long len = strlen(aString);
	return AppendString(aString, len);
	} // End =



// We assume the string are already instantiated and to be assigned
bool CString::Split_assign(ATTValVector <CString *> &OutStrings, const char *aStrSep)
	{
	char *ptr, *ptr1, chr;
	CString *aNewString;

	ptr = Buffer;

	int sepLen = strlen(aStrSep);

//if (sepLen=1)
//	printf ("\nSplit_assign: '%s'", Buffer);


	int idx = -1;
	OrsLong strLen;
	while (true)
		{
		idx++;
		if (sepLen == 1)
			ptr1 = strchr (ptr, * aStrSep);
		else
			ptr1 = strstr(ptr, aStrSep);

		if	(ptr1)
			{
			chr = *ptr1;
			*ptr1=0;

			strLen = ptr1-ptr;

			//OutStrings.Entry(idx)->assign(ptr); // assign also empty strings
			OutStrings.Entry(idx)->assign(ptr, strLen); // 31/03/2015

			*ptr1 = chr;
			ptr = ptr1+sepLen;
			}
		else
		{
			if (idx < OutStrings.Length())
			{

//				strLen = (Buffer+UsedBytes-1)-ptr;
//				OutStrings.Entry(idx)->assign(ptr, strLen); // assign also empty strings

				OutStrings.Entry(idx)->assign(ptr); // assign also empty strings
				OutStrings.Entry(idx)->Strip(CString::trailing, '\n');
			}
			break;
		}
		}

/*
	// NMB: Campi dereferenziati non usati
	while (++idx < OutStrings.length())
	{
		//OutStrings.Entry(idx)->Clear();
		OutStrings.Entry(idx)->ResetNoResize(); //
	}
*/
	return OrsTRUE;
	} // End CString::Split_assign


// Remove all occurences of character
OrsChar *CString::Strip(OrsChar c)
	{
	OrsInt ctr = 0;


	OrsChar *sourcePtr, *destPtr;
	sourcePtr = destPtr = Buffer;


	while (*sourcePtr)
		{
		if	( *sourcePtr == c )
			{
			sourcePtr++;
			ctr++;
			}
		*destPtr = *sourcePtr;

		sourcePtr++;
		destPtr++;
		} // End while
	*destPtr = 0; // eos

	// Adjust buffer size by ctr;
	if (ctr)
	{
		UnusedRightBytes+=ctr;
		BufTailPtr = destPtr;
		UsedBytes -= ctr;
	}

	//return OrsNULL;
	return Buffer;
	} // End CString::Strip


bool CString::Split(ATTValVector <CString *> &OutStrings, OrsLong aPos)
	{

	char *ptr, *ptr1;
	CString *aNewString;

	if (aPos < 1 ||  aPos > Length()-1)
		return false;


	ptr = Buffer;
	if	(OutStrings.Length())
		OutStrings.DeleteAndClear();

		ptr1 = ptr+aPos;
		char aSep = *ptr1;
		*ptr1=0;
		aNewString = new CString (ptr);
		if	(!aNewString || (OutStrings.Add(aNewString) == -1))
			{
			OutStrings.DeleteAndClear();
			return OrsFALSE;
			}
		*ptr1 = aSep;
		ptr = ptr1;

	aNewString = new CString (ptr);
	if	(!aNewString || (OutStrings.Add(aNewString) == -1))
		{
		OutStrings.DeleteAndClear();
		return OrsFALSE;
		}

	return OrsTRUE;
	} // End CString::Split

