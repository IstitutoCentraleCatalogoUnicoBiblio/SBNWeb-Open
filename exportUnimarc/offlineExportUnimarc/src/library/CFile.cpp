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
* Module  : CFile                                                           *
* Author  : Argentino Trombin                                               *
* Desc.   : Abstract base class                                             *
* Date    : 17/12/99                                                         *
****************************************************************************/


#include "library/CFile.h"
#include <stdlib.h>
#include <errno.h>
#include <string.h>
//#include <io.h>	// Looking for access() and unlink() in here.

//#include <iostream.h>	// Looking for access() and unlink() in here.
#include <iostream>	// Looking for access() and unlink() in here.
#include <sys/stat.h>	// Looking for statbuf
#include <malloc.h>
#include <stdio.h>

#include "GenericError.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


// Prove AIX
// FILE* fopen64(const char *filename, const char *type);
// provato -D_LARGE_FILES
//	/opt/freeware/lib/gcc/powerpc-ibm-aix5.2.0.0/4.2.4/include/c++/cstdio:109: error: '::fgetpos' has not been declared


extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

//#include "rw/defs.h"
//#include "rw/coreerr.h"

/****
STARTWRAP
#include <stdlib.h>
#include <stdio.h>	// Looking for fileno() and, in the case of VMS, remove()
#include <sys/types.h>	// Defines type used in <sys/stat.h>
#include <sys/stat.h>	// Looking for statbuf
#if defined(__ZTC__) || defined(__TURBOC__) || defined(MSC_BACKEND) || defined(__HIGHC__)
#  include <io.h>	// Looking for access() and unlink() in here.
#else
   extern "C" {
     int access( char*, int);
     int unlink( char*); // remove() may be more portable in the future
   }
#  ifdef SYSV
#    include <unistd.h>
#  else
#    ifdef VMS
#      include <file.h>
#      define unlink(file) remove(file)
#    else
#      include <sys/file.h>
#    endif
#  endif
#endif
ENDWRAP
*****/
/***
#ifdef RWMEMCK
   // If you're compiling w RWMEMCK, you'd better have Tools.h++,
   // or this will fail!
# include "rw/tooldefs.h"
# ifdef CPP_ANSI_RECURSION
#   define new rwnew
# endif
#endif
****/

#define READ(s)				fread((char*)&s, sizeof(s), 1, Filep)
#define READVEC(s,count) 	fread((char*)s, sizeof(*s), count, Filep)
#define WRITE(s)			fwrite((char*)&s, sizeof(s), 1, Filep)
#define WRITEVEC(s,count)	fwrite(( char*) s, sizeof(*s), count, Filep)


CFile::~CFile()
	{
	if (Filep != NULL)
		fclose(Filep);
	//delete Filename;
	free (Filename);
	} // End CFile::~CFile

OrsBool CFile::Exists()
	{
	//return access(Filename, 0) >= 0;

	//12/12/2010
	  struct stat stFileInfo;
	  bool blnReturn;
	  int intStat;

	  // Attempt to get the file attributes
	  intStat = stat(Filename,&stFileInfo);
	  if(intStat == 0) {
	    // We were able to get the file attributes
	    // so the file obviously exists.
	    blnReturn = true;
	  } else {
	    // We were not able to get the file attributes.
	    // This may mean that we don't have permission to
	    // access the folder which contains this file. If you
	    // need to do that level of checking, lookup the
	    // return values of stat which will give you
	    // more details on why stat failed.
	    blnReturn = false;
	  }
	  return(blnReturn);

	} // End CFile::Exists

OrsBool CFile::Exists( const char* name)
	{
//	return access(name, 0) >= 0;


	//12/12/2010
	  struct stat stFileInfo;
	  bool blnReturn;
	  int intStat;

	  // Attempt to get the file attributes
	  intStat = stat(name,&stFileInfo);
	  if(intStat == 0) {
	    // We were able to get the file attributes
	    // so the file obviously exists.
	    blnReturn = true;
	  } else {
	    // We were not able to get the file attributes.
	    // This may mean that we don't have permission to
	    // access the folder which contains this file. If you
	    // need to do that level of checking, lookup the
	    // return values of stat which will give you
	    // more details on why stat failed.
	    blnReturn = false;
	  }
	  return(blnReturn);


	} // End CFile::Exists

OrsBool CFile::Read(bool& t)              { return READ(t)==1; } // End CFile::Read // Arge
OrsBool CFile::Read(char& t)              { return READ(t)==1; } // End CFile::Read
OrsBool CFile::Read(short& t)             { return READ(t)==1; } // End CFile::Read
OrsBool CFile::Read(int& t)               { return READ(t)==1; } // End CFile::Read
OrsBool CFile::Read(long& t)              { return READ(t)==1; } // End CFile::Read
//#ifndef CHAR_MATCHES_UCHAR
//OrsBool CFile::Read(unsigned char& t)     { return READ(t)==1; } // End CFile::Read
//#endif
//OrsBool CFile::Read(unsigned short& t)    { return READ(t)==1; } // End CFile::Read
OrsBool CFile::Read(unsigned int& t)      { return READ(t)==1; } // End CFile::Read
OrsBool CFile::Read(unsigned long& t)     { return READ(t)==1; } // End CFile::Read
OrsBool CFile::Read(float& t)             { return READ(t)==1; } // End CFile::Read
OrsBool CFile::Read(double& t)            { return READ(t)==1; } // End CFile::Read
OrsBool CFile::Read(char* p, int count)   { return READVEC(p,count)==count; } // End CFile::Read
OrsBool CFile::Read(short* p, int count)  { return READVEC(p,count)==count; } // End CFile::Read
OrsBool CFile::Read(int* p, int count)    { return READVEC(p,count)==count; } // End CFile::Read
OrsBool CFile::Read(long* p, int count)   { return READVEC(p,count)==count; } // End CFile::Read
OrsBool CFile::Read(float* p, int count)  { return READVEC(p,count)==count; } // End CFile::Read
OrsBool CFile::Read(double* p, int count) { return READVEC(p,count)==count; } // End CFile::Read

OrsBool CFile::Read(char* string)
	{
	int c;
	while (1)
		{
		c = fgetc(Filep);
		if( c==EOF || c=='\0')
			break;
		*string++ = (char)c;
		}
	*string = '\0';
	return OrsTRUE;
	} // End CFile::Read













OrsBool CFile::Write(bool t)                { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write(char t)                { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write(short t)               { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write(int t)                 { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write(long t)                { return WRITE(t)==1; } // CFile::Write
#ifndef CHAR_MATCHES_UCHAR
OrsBool CFile::Write(unsigned char t)       { return WRITE(t)==1; } // CFile::Write
#endif
OrsBool CFile::Write(unsigned short t)      { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write(unsigned int t)        { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write(unsigned long t)       { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write(float t)               { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write(double t)              { return WRITE(t)==1; } // CFile::Write
OrsBool CFile::Write( char* p, int count)   { return WRITEVEC(p,count)==count; } // CFile::Write
OrsBool CFile::Write( short* p, int count)  { return WRITEVEC(p,count)==count; } // CFile::Write
OrsBool CFile::Write( int* p, int count)    { return WRITEVEC(p,count)==count; } // CFile::Write
OrsBool CFile::Write( long* p, int count)   { return WRITEVEC(p,count)==count; } // CFile::Write
OrsBool CFile::Write( float* p, int count)  { return WRITEVEC(p,count)==count; } // CFile::Write
OrsBool CFile::Write( double* p, int count) { return WRITEVEC(p,count)==count; } // CFile::Write

/**
*	Write up to and including the null terminator.
*	Be careful of non-terminated strings!
**/
OrsBool CFile::Write( char* string)
	{
//	return Write(string, strlen(string)+1);
	return Write(string, strlen(string));
	}

OrsBool CFile::Write( const char* string)
	{
//	return Write(string, strlen(string)+1);
	return Write((char*)string, strlen(string));
	}


/**
*	Write up to and including the null terminator.
*	Be careful of non-terminated strings!
**/
//OrsBool CFile::Write( char* string, bool nullTerminated)
//	{
//	if (nullTerminated)
//		return Write(string, strlen(string)+1);
//	else
//		return Write(string, strlen(string));
//	}




OrsBool CFile::Eof()
	{
	return feof(Filep);
	} // End CFile::Eof
/*
OrsBool CFile::Erase()
	{
	return fclose(Filep) != EOF && unlink(Filename) == 0 &&
	#ifdef CRLF_CONVENTION
		(Filep = fopen(Filename, "wb+")) != NULL;
	#else
		(Filep = fopen(Filename, "w+")) != NULL;
	#endif
	} // End CFile::Erase
**/
OrsBool CFile::Error()
	{
	if	(!Filep)
		return 1;
	return ferror(Filep);
	} // End CFile::Error

OrsBool CFile::Flush()
	{
	return fflush(Filep) != EOF;
	} // End CFile::Flush

OrsBool CFile::IsEmpty()
	{
	#if defined(__OREGON__)
		int dummy;
		int nb;

		nb = fread((char*)&dummy, sizeof(dummy), 1, Filep);
		if	( nb )
			fseek(Filep, -nb, 1);	// Undo what we did

		return !nb;
	#else
		// Thanks to Michael Kent, Gainesville, FL for the following fix:
		struct stat statbuf;
		fstat(fileno(Filep), &statbuf);
		return statbuf.st_size == 0;
		#endif
	} // End CFile::IsEmpty



int CFile::Peek()
	{
	int ch = getc(Filep);
	ungetc (ch, Filep);
	return ch;
	} // End CFile::Peek


int CFile::UnRead(char ch)
	{
	return ungetc (ch, Filep);
	} // End CFile::UnRead


CFile::CFile( const char* name)
	{
	Filename = strdup(name);
#ifdef CRLF_CONVENTION
	Filep = fopen(Filename, "rb+");	// Open existing file for update
#else
	Filep = fopen(Filename, "r+");
#endif
	if (Filep == NULL)
		{
		#ifdef CRLF_CONVENTION
			Filep = fopen(Filename, "wb+");	// Open new file for writing
		#else
			Filep = fopen(Filename, "w+");
		#endif
		if (Filep == NULL)
			{
	//			RWThrow( RWErrObject(CORE_OPERR, RWDEFAULT, __CFile), Filename);
			SignalAnError(__FILE__, __LINE__, "CFile::CFile: bad filename");

			}
		}


	} // End CFile::CFile



CFile::CFile( const char *aFilename, FILE* aFilep)
	{
//	Filename = new char [strlen(aFilename) + 1];
//	strcpy(Filename, aFilename);
	Filename = strdup(aFilename);
	Filep = aFilep;
	} // End CFile::CFile






OrsBool CFile::SeekToBegin()
	{
	//return SeekTo(0);
	return fseek(Filep, 0, SEEK_SET) >= 0;
	}

OrsBool CFile::SeekToEnd()
	{
//	return fseek(Filep, 0, 2) >= 0;
	seekCtr++;
	return fseek(Filep, 0, SEEK_END) >= 0;
	} // End CFile::SeekToEnd

OrsBool CFile::SeekToFromCurPos(long RelOffset)
	{
//	return fseek(Filep, offset, 0) >= 0;
	seekCtr++;
	return fseek(Filep, RelOffset, SEEK_CUR) >= 0;
	} // End CFile::SeekTo


FILE * CFile::GetFilePtr()
	{
	seekCtr++;
	return Filep;
	}

CFile::CFile( const char* name, OrsInt aMode)
	{
//	Filename = new char [strlen(name) + 1];
//	strcpy(Filename, name);
	Filename = strdup(name);
	init(aMode);
	} // End CFile::CFile


void CFile::init(OrsInt aMode)
{
	Filep = OrsNULL;
	seekCtr = 0;



	switch (aMode)
		{
		case AL_READ_FILE:	// Open existing file for reading only
			#ifdef CRLF_CONVENTION
				Filep = fopen(Filename, "rb");
			#else
				Filep = fopen(Filename, "r");
			#endif
			break;
		case AL_WRITE_FILE:	// Open new file for writing, wipes existing content
			#ifdef CRLF_CONVENTION
				Filep = fopen(Filename, "wb");
			#else
				Filep = fopen(Filename, "w");
			#endif
			break;
		case AL_APPEND_FILE_AFTER_EOF:	// Open new file for appending.
										// Existing content will not be destroyed
										// EOF of opened file is not removed
			#ifdef CRLF_CONVENTION
				Filep = fopen(Filename, "ab");
			#else
				Filep = fopen(Filename, "a");
			#endif
			break;
		case AL_APPEND_FILE_BEFORE_EOF:	// Open new file for appending.
										// Existing content will not be destroyed
										// EOF of opened file is removed
			#ifdef CRLF_CONVENTION
				Filep = fopen(Filename, "ab+");
			#else
				Filep = fopen(Filename, "a+");
			#endif
			break;
		case AL_READ_WRITE_FILE: // Open existing file for update, reading and writing.
								 // Content is initially kept. The file must exist.
			#ifdef CRLF_CONVENTION
				Filep = fopen(Filename, "rb+");
			#else
				Filep = fopen(Filename, "r+");
			#endif
			break;
		case AL_CLEAR_READ_WRITE_FILE: // Open existing file for update,  writing and reading.
								 // Content is initially destroyed.
			#ifdef CRLF_CONVENTION
				Filep = fopen(Filename, "wb+");
			#else
				Filep = fopen(Filename, "w+");
			#endif
			break;

		} // End switch

	if (Filep == NULL)
	{
//	    SignalAnError(__FILE__, __LINE__, "CFile::CFile: Cannot open file %s", Filename);
//		GenericError *genericError = new GenericError (__FILE__, __LINE__, IO_GENERICERROR_CODE, EXIT_ON_GENERICERROR_TRUE, "CFile::CFile: Cannot open file %s", Filename);
		printf ("\nErrore in apertura di %s, errno=%d, errorMsg=%s", Filename, errno, strerror(errno));
		throw (GenericError (__FILE__, __LINE__, IO_GENERICERROR_CODE, EXIT_ON_GENERICERROR_TRUE, "CFile::CFile: Cannot open file %s, errno=%d, errorMsg=%s", Filename, errno, strerror(errno)));
//		throw (GenericError ("CFile::CFile: Cannot open file"));
	}
}


long CFile::getSeekCtr()
{
	return seekCtr;
}

/*
	Reads characters from stream and stores them as a C string into str until (num-1)
	characters have been read or either a newline or a the End-of-File is reached, whichever comes first.
	A newline character makes fgets stop reading, but it is considered a valid
	character and therefore it is included in the string copied to str.
	A null character is automatically appended in str after the characters read to signal the end of the C string.

	BEWARE the buffer is big enough!! Else we have memory overwriting
 */
OrsBool	CFile::ReadLine(char* string, int N)
{
	// Per controllo EOL
	char *ptr = string+N-1;
	*ptr = -1;

	char* str = fgets (string , N , Filep);
	if (str)
	{
		if (*ptr != -1)
			if (*(ptr-1) != '\n')
				return false;
		return true;
	}
	else
		return false;
}

/**
 * return  -1 on error
 * 			N on EOL not reached
 *			0 OK read up to \n (included)
 */
long	CFile::readFixedLenLine(char* string, long N)
{
	// Per controllo EOL
	char *ptr = string+N-1;
	*ptr = -1;

	char* str = fgets (string , N , Filep);
	if (str)
	{
		if (*ptr != -1)
		{
			if (*(ptr-1) != '\n')
				return N;
		}
		return 0;
	}
	else
		return -1;
}







OrsBool CFile::SeekTo(long offset)
	{
//	return fseek(Filep, offset, 0) >= 0;
	seekCtr++;
	return fseek(Filep, offset, SEEK_SET) >= 0;
	} // End CFile::SeekTo

OrsBool CFile::SeekToLarge(long long pos)
	{
	seekCtr++;
	int ret = fseeko(Filep, pos, SEEK_SET);
	return  ret >= 0;
	} // End CFile::SeekTo

/**
*	Miscellaneous positioning & query functions
**/

long CFile::CurOffset()
	{
	return ftell(Filep);
	} // End CFile::CurOffset


long long CFile::CurOffsetLarge()
	{
	return  ftello(Filep);
	} // End CFile::SeekTo


//OrsBool CFile::SeekTo(fpos_t pos)
//	{
//	seekCtr++;
//	int ret = fsetpos(Filep, &pos);
//	return  ret >= 0;
//	} // End CFile::SeekTo

//OrsBool CFile::SeekToLarge(off_t pos)

