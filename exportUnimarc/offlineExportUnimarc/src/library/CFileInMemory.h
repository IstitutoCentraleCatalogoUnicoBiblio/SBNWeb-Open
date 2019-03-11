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
 * CFileInMemory.h
 *
 *  Created on: 30-May-2009
 *      Author: argentino
 */

#ifndef CFILEINMEMORY_H_
#define CFILEINMEMORY_H_

#include "library/CFile.h"

//#define READ(s)				fread((char*)&s, sizeof(s), 1, Filep)
//#define READVEC(s,count) 	fread((char*)s, sizeof(*s), count, Filep)

#define MEM_READ(s)				Read((char*)&s, sizeof(s))
#define MEM_READVEC(s,count) 	Read((char*)s, count)

#define READ_BLOCK_SIZE 8096

class CFileInMemory: public CFile {
	char * fileInMemoryPtr;
//	long fileSize;
//	long fileOffset;

	long long fileSize;		// 30/07/2012
	long long fileOffset;

public:
	CFileInMemory( const char *aFilename, FILE* aFilep);
	CFileInMemory( const char* aFilename);
	CFileInMemory( const char* aFilename, OrsInt aMode);
	virtual ~CFileInMemory();
	void init();




	// Rading functions
	OrsBool 	Read(bool& b); // Arge
	OrsBool 	Read(char& c);
	OrsBool 	Read(short& i);
	OrsBool 	Read(int& i);
	OrsBool 	Read(long& i);
//#ifndef CHAR_MATCHES_UCHAR
//	OrsBool 	Read(unsigned char& c);
//#endif
//	OrsBool 	Read(unsigned short& i);
	OrsBool 	Read(unsigned int& i);
	OrsBool 	Read(unsigned long& i);
	OrsBool 	Read(float& f);
	OrsBool 	Read(double& d);
	OrsBool		Read(char* c, int N);
	OrsBool 	Read(short* i, int N);
	OrsBool 	Read(int* i, int N);
	OrsBool		Read(long* i, int N);
//#ifndef CHAR_MATCHES_UCHAR
	OrsBool		Read(unsigned char* c, int N){return Read((char*)c, N);}
//#endif
	OrsBool		Read(unsigned int* i, int N) {return Read(( int*)i, N);}
	OrsBool		Read(float* f, int N);
	OrsBool		Read(double* d, int N);
	OrsBool		Read(char* string); // Read to null terminator or EOF; no CR/LF translation will be done. Beware of overflow.

	OrsBool		ReadLine(char* string, int N);
	long		readFixedLenLine(char* string, long N);

	/*
	// Writing functions
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

*/
	long 		CurOffset(); // Returns current offset of file
	OrsBool 	Eof();

	OrsBool	SeekTo(long offset); // offset bytes from beginning of file
	OrsBool SeekToLarge(long long pos);
	OrsBool	SeekToBegin()	{return SeekTo(0);}
	OrsBool	SeekToEnd();
	OrsBool	SeekToFromCurPos(long RelOffset);
	long long CurOffsetLarge();

	int		Peek();
	int 	UnRead(char ch);
	OrsBool	Error();     // TRUE if the file has had an error.
	long 	getFileSize();

};

#endif /* CFILEINMEMORY_H_ */
