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
* Module  : CFile.h                                                         *
* Author  : Argentino Trombin                                               *
* Desc.   : Encapsulates ANSI-C binary file operations.                     *
* Date    : 17/12/99                                                        *
****************************************************************************/
#ifndef CFILE_H
#define CFILE_H

//#define _FILE_OFFSET_BITS 64	// To overcome 2gb file size problem
#define _LARGE_FILES 			// 22/10/2012. OK on AIX. Not required on linux. NB: Needs fseeko.
#define _FILE_OFFSET_BITS 64	// 22/10/2012. To overcome 2gb file size problem !!! NO EFFECT ON AIX. OK in linux. NB: Needs fseeko.


#include "ors/Const.h"
#include "library/LibraryConst.h"
#include <stdio.h>
#include <inttypes.h>
#include <sys/types.h>

enum CFileFlags // typedef
	{
	AL_READ_FILE = 1,
	AL_WRITE_FILE,
	AL_APPEND_FILE_AFTER_EOF,
	AL_APPEND_FILE_BEFORE_EOF,
	AL_READ_WRITE_FILE,
	AL_CLEAR_READ_WRITE_FILE
	}; // End CFileFlags

class  CFile {

	void init(OrsInt aMode);

protected:
	long seekCtr;
	char*		Filename;
	FILE*		Filep;

public:
	// Constructor
	CFile( const char *aFilename, FILE* aFilep);
	CFile( const char* name);
	CFile( const char* name, OrsInt aMode);
	virtual ~CFile();


	char*		GetName()	{return Filename;}
	OrsBool		Exists();
	static OrsBool 	Exists( const char* name);

	virtual OrsBool 	Read(bool& b); // Arge
	virtual OrsBool 	Read(char& c);
	virtual OrsBool 	Read(short& i);
	virtual OrsBool 	Read(int& i);
	virtual OrsBool 	Read(long& i);
	virtual OrsBool 	Read(unsigned int& i);
	virtual OrsBool 	Read(unsigned long& i);
	virtual OrsBool 	Read(float& f);
	virtual OrsBool 	Read(double& d);
	virtual OrsBool		Read(char* c, int N);
	virtual OrsBool 	Read(short* i, int N);
	virtual OrsBool 	Read(int* i, int N);
	virtual OrsBool		Read(long* i, int N);
//#ifndef CHAR_MATCHES_UCHAR
	virtual OrsBool		Read(unsigned char* c, int N){return Read((char*)c, N);}
//#endif
	virtual OrsBool		Read(unsigned int* i, int N) {return Read(( int*)i, N);}
	virtual OrsBool		Read(float* f, int N);
	virtual OrsBool		Read(double* d, int N);
//
//  // Read to null terminator or EOF; no CR/LF translation will be done. Beware of overflow.
	virtual OrsBool		Read(char* string);

	virtual OrsBool		ReadLine(char* string, int N);
	virtual long		readFixedLenLine(char* string, long N);

	OrsBool 	Write(bool b); // Arge
	OrsBool		Write(char c);
	OrsBool		Write(short s);
	OrsBool		Write(int i);
	OrsBool		Write(long l);
#ifndef CHAR_MATCHES_UCHAR
	OrsBool		Write(unsigned char c);
#endif
	OrsBool		Write(unsigned short s);
	OrsBool		Write(unsigned int i);
	OrsBool		Write(unsigned long l);
	OrsBool		Write(float f);
	OrsBool		Write(double d);
	OrsBool		Write( char* string);
	OrsBool		Write( const char* string);
//	OrsBool 	Write( char* string, bool nullTerminated);

	OrsBool		Write( short* i, int N);
	OrsBool		Write( int* i, int N);
	OrsBool		Write( long* i, int N);
#ifndef CHAR_MATCHES_UCHAR
	OrsBool		Write( unsigned char* c, int N){return Write(( char*)c, N);}
#endif
	OrsBool		Write( unsigned int*  i, int N){return Write((  int*)i, N);}
	OrsBool		Write( float* f, int N);
	OrsBool		Write( double* d, int N);
	OrsBool		Write( char* string, int N);

	OrsBool		Erase();
	OrsBool		Flush();     // Writes all pending output
	OrsBool		IsEmpty();   // TRUE if the file is empty

	virtual long			CurOffset(); // Returns current offset of file
	virtual OrsBool		Eof();	     // TRUE if file at EOF


	virtual OrsBool		SeekTo(long offset); // offset bytes from beginning of file
//	virtual OrsBool		SeekTo(fpos_t offset); // offset bytes from beginning of file
//	virtual OrsBool		SeekToLarge(off_t offset); // offset bytes from beginning of file
	virtual OrsBool		SeekToLarge(long long offset); // offset bytes from beginning of file


	virtual OrsBool		SeekToBegin();//	{return SeekTo(0);}

	virtual OrsBool		SeekToEnd();
	virtual OrsBool		SeekToFromCurPos(long RelOffset);
	//virtual long long getCurPosition();
	virtual long long CurOffsetLarge();

	virtual int		Peek();
	virtual int 	UnRead(char c);
	virtual OrsBool		Error();     // TRUE if the file has had an error.

	FILE * GetFilePtr();

	long getSeekCtr();

	}; // End CFile

#endif // CFILE_H



