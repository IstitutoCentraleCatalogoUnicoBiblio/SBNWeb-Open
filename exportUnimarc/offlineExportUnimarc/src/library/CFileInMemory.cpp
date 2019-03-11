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
 * CFileInMemory.cpp
 *
 *  Created on: 30-May-2009
 *      Author: argentino
 */

#include "CFileInMemory.h"
#include <malloc.h>
#include <string.h>

#include <stdio.h>
#include <stdlib.h>
#include "GenericError.h"

extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

CFileInMemory::CFileInMemory( const char *aFilename, FILE* aFilep) : CFile( aFilename, aFilep)
{
	init();
}
CFileInMemory::CFileInMemory( const char* aFilename) : CFile( aFilename)
{
	init();
}
CFileInMemory::CFileInMemory( const char* aFilename, OrsInt aMode) : CFile( aFilename, aMode)
{
	init();
}


CFileInMemory::~CFileInMemory() // base class destructor called implicitly
{
	if (fileInMemoryPtr)
		free (fileInMemoryPtr);
}

void CFileInMemory::init()
{
	CFile::SeekToEnd();
	fileSize = CFile::CurOffset();
//printf ("\nfileSize=%lld", fileSize);
	if (fileSize == -1)						// 30/07/2012 Fix per tr_tit_aut.out.srt di indice che sfora i 2.2 GB
		fileSize = CFile::CurOffsetLarge();
//printf ("\nfileSize=%lld", fileSize);

	fileInMemoryPtr = (char *)malloc(fileSize+1); // +1 per potersi posizionare alla fine del file
	if (fileInMemoryPtr)
	{
		char *ptr = fileInMemoryPtr;
		bool retb = true;
		int ctr=0;
		CFile::SeekToBegin();
		while (retb)
		{
			retb = CFile::Read(ptr, READ_BLOCK_SIZE);
			ptr+=READ_BLOCK_SIZE;
			ctr++;
		}
//		printf ("\nLoading file in memory: %s, Letti %lld bytes", Filename, fileSize);
		printf (" Caricati  %lld bytes", fileSize);
		*(fileInMemoryPtr+fileSize) = 0; // EOF

	}
	else
	{
		//throw exception;
		throw (GenericError (__FILE__, __LINE__, IO_GENERICERROR_CODE, EXIT_ON_GENERICERROR_TRUE, "CFileInMemory::CFileInMemory: Cannot alloc %lld of memory for %s", fileSize, Filename));

	}

	fileOffset = 0;
} // End CFileInMemory::init()
/**


*/
OrsBool CFileInMemory::Read(bool& t)              { return MEM_READ(t); }
//OrsBool CFileInMemory::Read(char& t)              { return MEM_READ(t); }
OrsBool CFileInMemory::Read(char& t)
{

	if ((fileOffset+1) > fileSize)
		return false;

	t = *(fileInMemoryPtr+fileOffset);

	fileOffset++;
	return true;
} // End CFileInMemory::Read
OrsBool CFileInMemory::Read(short& t)             { return MEM_READ(t); }
OrsBool CFileInMemory::Read(int& t)               { return MEM_READ(t); }
OrsBool CFileInMemory::Read(long& t)              { return MEM_READ(t); }
//#ifndef CHAR_MATCHES_UCHAR
//OrsBool CFileInMemory::Read(unsigned char& t)     { return MEM_READ(t); }
//#endif
//OrsBool CFileInMemory::Read(unsigned short& t)    { return MEM_READ(t); }
OrsBool CFileInMemory::Read(unsigned int& t)      { return MEM_READ(t); }
OrsBool CFileInMemory::Read(unsigned long& t)     { return MEM_READ(t); }
OrsBool CFileInMemory::Read(float& t)             { return MEM_READ(t); }
OrsBool CFileInMemory::Read(double& t)            { return MEM_READ(t); }
//
OrsBool CFileInMemory::Read(short* p, int count)  { return MEM_READVEC(p,count); }
OrsBool CFileInMemory::Read(int* p, int count)    { return MEM_READVEC(p,count); }
OrsBool CFileInMemory::Read(long* p, int count)   { return MEM_READVEC(p,count); }
OrsBool CFileInMemory::Read(float* p, int count)  { return MEM_READVEC(p,count); }
OrsBool CFileInMemory::Read(double* p, int count) { return MEM_READVEC(p,count); }
//
//
OrsBool CFileInMemory::Read(char* p, int count)
{
	long newOffset = fileOffset + count;

	if (newOffset < 0 || newOffset > fileSize)
		return false;

	memcpy(p, (fileInMemoryPtr+fileOffset), count);

	fileOffset = newOffset;
	return true;
} // End CFileInMemory::Read




OrsBool CFileInMemory::Read(char* string)
	{
	if (fileOffset == fileSize || fileOffset > fileSize)
		return false;

	char *ptr = fileInMemoryPtr+fileOffset;


	while (fileOffset < fileSize)
		{
		if( *ptr=='\0')
			break;
		*string++ = *ptr++;
		fileOffset++;
		}
	*string = '\0';

	return OrsTRUE;
	} // End CFileInMemory::Read



OrsBool CFileInMemory::Eof()
{
	if (fileOffset == fileSize || fileOffset > fileSize)
		return true;
	else
		return false;
}


OrsBool CFileInMemory::SeekTo(long offset)
	{
	seekCtr++;
	//return fseek(Filep, offset, SEEK_SET) >= 0;
	if (offset < 0 || offset >= fileSize)
		return false;

	fileOffset = offset;
	return true;
	} // End CFileInMemory::SeekTo

OrsBool CFileInMemory::SeekToLarge(long long pos)
	{
	seekCtr++;
//	int ret = fseeko(Filep, pos, SEEK_SET);
//	return  ret >= 0;

	if (pos < 0 || pos >= fileSize)
		return false;

	fileOffset = pos;
	return true;

	} // End CFile::SeekTo













OrsBool CFileInMemory::SeekToEnd()
	{
	seekCtr++;
	//return fseek(Filep, 0, SEEK_END) >= 0;
	fileOffset = fileSize;
	return true;
	} // End CFileInMemory::SeekToEnd

OrsBool CFileInMemory::SeekToFromCurPos(long RelOffset)
	{
	seekCtr++;
	//return fseek(Filep, RelOffset, SEEK_CUR) >= 0;
	long newOffset = fileOffset + RelOffset;

	if (newOffset < 0 || newOffset >= fileSize)
		return false;

	fileOffset = newOffset;
	return true;
	} // End CFileInMemory::SeekTo



int CFileInMemory::UnRead(char ch)
	{
	//return ungetc (ch, Filep);
	if (fileOffset)
	{
		fileOffset--;
		*(fileInMemoryPtr+fileOffset) = ch;
		return ch;
	}

	return -1;// TO DO lancia eccezione
	} // End CFile::UnRead

int CFileInMemory::Peek()
	{
	if (!Eof())
		return (*(fileInMemoryPtr+fileOffset));
	return -1; // TO DO lancia eccezione
	} // End CFileOnDisc::Peek


OrsBool CFileInMemory::Error()
	{
	if	(!Filep)
		return true;
	return false; // TO DO
	} // End CFileOnDisc::Error



OrsBool CFileInMemory::ReadLine(char* string, int N)
	{
	if (fileOffset == fileSize || fileOffset > fileSize)
		return false;

	char *ptr = fileInMemoryPtr+fileOffset;

	char *dest=string;
//	char *source=ptr;

	int ctr = 0;
	int maxLen = N-1;

	while (fileOffset < fileSize && ctr < maxLen)
		{
		if( *ptr=='\n')
		{
			fileOffset++;
			*string++ = '\n';
			*string = 0; // EOL
			ctr+=2;
			break;
		}
		*string++ = *ptr++;
		fileOffset++;
		ctr++;
		}
	if (ctr == maxLen)
	{
		*(string-1) = 0; // EOL
		//throw (GenericError (__FILE__, __LINE__, IO_GENERICERROR_CODE, EXIT_ON_GENERICERROR_TRUE, "\nCFileInMemory::ReadLine: line ended before NEWLINE: '%s'", dest));
//		SignalAnError(__FILE__, __LINE__, "%s: Buffer size: %d, End of line non raggiunta: %s", Filename, N, dest);
//		printf ("%s: Buffer size: %d, End of line non raggiunta: %s", Filename, N, dest);

		*(dest+30)=0; // 02/02/2015 Truncate
		printf ("\n%s: Buffer size: %d, End of line non raggiunta: %s...", Filename, N, dest);

		return OrsFALSE;
	}

	return OrsTRUE;
	} // End CFileInMemory::ReadLine


long CFileInMemory::getFileSize()
{
	return fileSize;
}






/**
 * return  -1 on error
 * 			N on EOL not reached
 *			0 OK read up to \n (included)
 */
long	CFileInMemory::readFixedLenLine(char* string, long N)
{
	if (fileOffset == fileSize || fileOffset > fileSize)
		return -1; // EOL reached

	char *ptr = fileInMemoryPtr+fileOffset;

	char *dest=string;

	int ctr = 0;
	int maxLen = N-1;

	while (fileOffset < fileSize && ctr < maxLen)
		{
		if( *ptr=='\n')
		{
			fileOffset++;
			*string++ = '\n';
			*string = 0; // EOL
			ctr+=2;
			break;
		}
		*string++ = *ptr++;
		fileOffset++;
		ctr++;
		}

	if (ctr == maxLen)
	{
//		*(string-1) = 0; // EOL 19/03/2015 (per poter concatenare resto di riga)

		//throw (GenericError (__FILE__, __LINE__, IO_GENERICERROR_CODE, EXIT_ON_GENERICERROR_TRUE, "\nCFileInMemory::ReadLine: line ended before NEWLINE: '%s'", dest));
//		SignalAnError(__FILE__, __LINE__, "%s: Buffer size: %d, End of line non raggiunta: %s", Filename, N, dest);
//		printf ("%s: Buffer size: %d, End of line non raggiunta: %s", Filename, N, dest);

//		*(dest+30)=0; // 02/02/2015 Truncate
		//printf ("\n%s: Buffer size: %d, End of line non raggiunta: %s...", Filename, N, dest);

		return maxLen; // N  KO EOL non reached
	}

	return 0; // OK read a whole line
}



long CFileInMemory::CurOffset()
	{
	return fileOffset;
	} // End CFileInMemory::CurOffset


long long CFileInMemory::CurOffsetLarge()
	{
	return  fileOffset;
	} // End CFile::SeekTo

