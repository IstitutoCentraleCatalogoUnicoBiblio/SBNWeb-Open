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
* Module  : CString.h                                                       *
* Author  : Argentino Trombin                                               *
* Desc.   :                                                                 *
*                                                                           *
* Date    :                                                                 *
****************************************************************************/

#ifndef CSTRING_H
#define CSTRING_H

//#include <fstream.h>
//#include <iostream.h>
#include <iostream>
using namespace std;

#include <stdio.h>

//#include "ClassId.h"
#include "ors/Const.h"
#include "library/CBaseClass.h"
#include "library/CFile.h"
#include "library/CFileInMemory.h"
#include "library/tvvector.h"



#define CSTRING_RESIZE_DEFAULT 256

class CRegExp;

class CString : public CBaseClass
	{
private:
	friend class CStringIterator;
	// Members

	OrsChar *Buffer;	// Buffer is not circular
//	OrsBool GetCharFlag; // Flag used to test if GetChar failed
//	OrsBool PeekCharFlag; // Flag used to test if GetChar failed

	OrsChar *BufferSubString; // Buffer to hold a substring of buffer
//	OrsInt  BufferSubStringSize; // Size of allocated memory for BufferSubString
	OrsLong  BufferSubStringSize; // Size of allocated memory for BufferSubString
	OrsLong  BufferSubStringLength; // Length of string stored in buffer


//	OrsInt	BufferSize;	// Size of allocated memory for Buffer
	OrsLong	BufferSize;	// Size of allocated memory for Buffer
//	OrsInt  UsedBytes;	// The number of characters (bytes) in use
	OrsLong UsedBytes;	// The number of characters (bytes) in use
						// Can be less than BufferSize
//	OrsInt  UnusedRightBytes;
	OrsLong UnusedRightBytes;
						// Bytes unused from last used byte to last byte in buffer
	OrsChar *BufTailPtr;	// Pointer within Buffer. Points to the byte at offset Point
//	OrsInt	ResizeBy;
	OrsLong	ResizeBy;
						// Value by which the buffer can be resized

	// Methods
//	OrsBool	MoveRightBy(OrsInt n);
	OrsBool	MoveRightBy(OrsLong n);
			// Move valid contents in Buffer to n positions to the right
//	OrsBool	MoveRightFromBy(OrsInt From, OrsInt By);
	OrsBool	MoveRightFromBy(OrsLong From, OrsLong By);
			// Shift part of used bytes to the right by n positions
//	OrsBool MakeNewBuffer(OrsInt Size);
	OrsBool MakeNewBuffer(OrsLong Size);
			// Startup a brand new buffer
//	OrsBool GrowBuffer(OrsInt GrowBy);
	OrsBool GrowBuffer(OrsLong GrowBy);
			// Resize the buffer by a growing factor of GrowBy
	OrsBool CaseSensitiveFlag;
	OrsBool SkipWhiteSpacesFlag; // Skip whitespaces flag
	OrsBool KeepLineFeed;	// Used with readline
	void	Reset();
			// Will reset things to an empty state

	OrsInt HashCase();
	void init();
public:
	enum stripType {leading, trailing, both};
	enum direction {forward, backward};

	CString(OrsLong Size);
	CString(OrsChar *aString);
	CString(OrsChar *aString, OrsLong Size);

	CString();
	virtual ~CString();
	// Redefined base class virtual functions
	//=============================
	virtual OrsInt ClassId() {return CStringID;};
			// Return the Class ID number
	virtual const OrsChar* ClassName(){return "CStringID";};
			// Return the Class Name

	// Operators
	//==========
	CString& operator = (CString&); // Assign string
	OrsBool	 operator = (OrsChar * aString);
	OrsBool	 operator = (const OrsChar * aString);

	OrsBool  operator = (OrsChar aChar);

	bool	assign(OrsChar * aString);
//	bool	assign(const OrsChar * aString);
	OrsBool assign(OrsChar *aString, OrsLong Size);
	OrsBool assign(OrsLong Size, OrsChar *aString);

	OrsBool assign(CString *sPtr);

	OrsBool  Equals     (OrsChar *aString);
	OrsBool  Equals     (OrsChar *aString, OrsLong SubStringLen);

	OrsBool  Equals     (const OrsChar *aString);
	OrsBool  Equals     (const OrsChar *aString, OrsLong SubStringLen);


	OrsInt  operator == (char* aStringPtr); // Test for equality.
	OrsBool operator + (OrsChar * aString);
	OrsBool operator + (OrsChar aChar);
	OrsChar &operator[](int i);
//	OrsChar	operator[](int i);

//	istream&  operator>>(istream&, CString&);
//	ostream&  operator<<(ostream& os, CString& s);


	// TESTED
	OrsBool AppendChar(OrsChar aChar);
						// Will add a character to the END of the buffer
	OrsBool AppendString(OrsChar *aString);

//	OrsBool AppendString(const OrsChar *aString);

//	OrsBool AppendString(OrsChar *aString, OrsInt SubStringLen);
	OrsBool AppendString(OrsChar *aString, OrsLong Size);
						// Will add a string to the END of the buffer
	OrsBool AppendString(CString * sPtr);

	//	OrsBool AppendString(const OrsChar *aString, OrsLong Size);

	OrsChar *Data();
	OrsChar *data();
	OrsChar *ConstData();
			// Return  pointer to start of data
	OrsBool PrependString(const OrsChar *aString);
						// Will add a string to the START of the buffer
	OrsBool PrependChar(OrsChar aChar);
						// Will add a string to the START of the buffer
//	OrsBool InsertStringAt(OrsChar *aString, OrsInt Pos);
	OrsBool InsertStringAt(const OrsChar *aString, OrsLong Pos);
						// Will insert a string to a given POSITION is the buffer
						// Pos starts at 1 and cannot be grater that the characters
						// cuttently in buffer
						// Insertion will affect only the used part of the buffer
//	OrsBool InsertCharAt(OrsChar aChar, OrsInt Pos);
	OrsBool InsertCharAt(OrsChar aChar, OrsLong Pos);
						// Will insert a char to a given POSITION in the buffer
						// Pos starts at 1 and cannot be greater that the characters
						// cuttently in buffer
						// Insertion will affect only the used part of the buffer
	OrsChar ExtractFirstChar();
			// Will retrieve and remove the first character from the buffer
	OrsChar ExtractLastChar();
			// Will retrieve and remove the last character from the buffer

	OrsChar *ExtractFirstWord(char *Delimiters);
			// Will retrieve and remove the first word from the buffer
	OrsChar *ExtractLastWord(char *Delimiters);
			// Will retrieve and remove the last word from the buffer


	OrsChar GetFirstChar();
			// Will return the first character from the buffer
	OrsChar GetChar(long pos);

	OrsChar GetLastChar();
			// Will return the last character from the buffer
//	OrsInt  Length();
	OrsLong  Length();
			// Returns the length of the string
	void	Clear();
			// Will clear out and reset things to an empty state
	void	ClearTo(OrsChar aChar);
			// Will clear the contents of  the string to the specified character
	void	ChangeTo(OrsChar aCharFrom, OrsChar aCharTo);
			// Will change one character with another in all the string
	void	ChangeTo(OrsChar *aStringFrom, OrsChar *aStringTo);
			// Will change one substring with another in all the string
	void 	ChangeTo(OrsChar *charactersToBeChanged, OrsChar changeTo);
			// Will change one of many indicated character with another one

	void	Escape(OrsChar aChar);
			// Will change all occurances of aChar with its escaped equivalent

	void	UnEscape();
			// Will change all occurances of the escaped aChar with its equivalent




//	OrsInt	SetResizeValueBy(OrsInt aValue);
	OrsLong	SetResizeValueBy(OrsLong aValue);
			// Set the resize value by aValue




//	OrsInt	First(char aChar);
	OrsLong	First(char aChar);
			// return the position of first character in string
	OrsLong	Last(char aChar);
			// return the position of last character in string
	OrsLong	Nth(char aChar, short Nth);
			// return the position of the nth character in string

//	OrsBool Replace(OrsInt From, OrsInt Len, char *aString); // To
	OrsBool Replace(OrsLong From, OrsLong Len, const char *aString); // To
			// Replace a substring with a new string
	OrsBool ReplaceChar(OrsLong pos, char aChar);

	// Replace one or all occurances of a substring
	OrsBool Replace(const char *fromString, const char *toString, int occurance=0);


	//	OrsChar* ExtractFromTo(OrsInt From, OrsInt To);
	OrsChar* ExtractFromTo(OrsLong From, OrsLong To);
			// Will retrieve and remove the range of characters from the buffer

	void ToUpper();
	void ToLower();

	void SetCaseSensitiveFlag(OrsBool aCase);
	OrsBool GetCaseSensitiveFlag();

	void SetKeepLineFeedFlag(OrsBool aFlag);
	OrsBool GetKeepLineFeedFlag();

	OrsBool ReadLine(istream& aStream);
	OrsBool ReadLine(CFile *);
	OrsBool ReadLineWithPrefixedMaxSize(CFile * aFilePtr);
	//OrsBool ReadLine(CFileInMemory *);
	OrsBool keepReadingLine(CFile * aFilePtr);




	OrsBool ReadToDelim(istream& aStream, char aDelim);
	OrsBool ReadToDelim(CFile *aFilePtr, char aDelim);
//OrsBool ReadToDelim(CFileInMemory *aFilePtr, char aDelim);

	OrsBool ReadToDelim(istream& aStream, char *aDelimString);
	OrsBool ReadToDelim(CFile *, char * aDelimString);
//OrsBool ReadToDelim(CFileInMemory *, char * aDelimString);

	OrsBool ReadToDelimIgnoringQuoted(CFile *aFilePtr, char aDelim, OrsChar *QuotesStartEnd);
//OrsBool ReadToDelimIgnoringQuoted(CFileInMemory *aFilePtr, char aDelim, OrsChar *QuotesStartEnd);

			// Read to EOF or one ofthe delimitor.
	OrsBool ReadString(istream& aStream);
	OrsBool ReadString(CFile *);


	OrsBool ReadBytes(CFile *aFilePtr, int nBytes); // 06/02/2015



	OrsChar *FindSubstring(const char *aSubString);
			// Return the pointer to the found substring
	long	IndexSubString(const char *aSubString);
	long 	IndexSubStringCaseInsensitive(const char *aSubString);

	long	IndexLastSubString(const char *aSubString);
	long 	IndexLastSubStringUntil(const char *aSubString, int endPos);

	long 	IndexSubStringIgnoreWithin(const char *aSubString, char ignoreStart, char ignoreEnd);
	long 	IndexLastSubStringIgnoreWithin(const char *aSubString, char ignoreStart, char ignoreEnd);

			// Return the position of the substring or -1
	long IndexSubStringFrom(const char *aSubString, int from);
	long IndexSubStringFrom(const char *aSubString, int from, int direction);

	long IndexCharFrom(char aChar, int from, int direction);


	OrsChar *Find(char aChar);

	OrsChar *SubstringData(OrsLong From);

	OrsChar *SubstringData(OrsLong From, OrsLong Length);
			// Get pointer to start of substring beginning at 'From' for length 'Length'
	OrsChar *SubstringData(OrsLong From, char *Delimiters, OrsLong *To);
			// Get pointer to start of substring beginning at 'From'
			// ending at one ofthe characters in 'Delimiters' or EOS
			// and store the position of the character following
			// the found substring in the string

	OrsBool	CropLeftBy(OrsLong n);
			// Remove lefthandside n characters
	OrsBool	CropRightBy(OrsLong n);
			// Remove righthandside n characters
	OrsBool	CropRightFrom(OrsLong From);
			// Remove righthandside characters starting from
	OrsBool	CropRightFromChar(char aChar);
			// Remove righthandside characters starting from first char
	OrsBool	CropRightAfterChar(OrsLong From, char *delimiters);
		// Remove righthandside characters starting from a given position and on first of one of  the characters given

	OrsChar *Strip(OrsChar c); // 04/05/2015
	OrsChar *Strip(stripType s = trailing, OrsChar c=' ');
	OrsChar *Strip(stripType st, OrsChar *s);
	OrsBool IsEmpty();
	void	SkipWhitespace(OrsBool YesNo);// True to skip whitespace after token read.
	OrsBool Resize(OrsLong NewSize);
	OrsBool SizeAndClearTo(OrsLong aSize, OrsChar aChar);


	OrsLong	StoreSize();
	OrsInt	Hash();

	OrsInt	Compare(CString&);
	OrsInt	Compare(const OrsChar *);
	OrsInt	Compare(OrsChar *, OrsInt len);
	OrsInt	CompareI(CString&);
	OrsInt	CompareI(OrsChar *);
	OrsInt	CompareI(OrsChar *, OrsInt len);

	bool	StartsWith(const OrsChar * aString);
	bool	EndsWith(const OrsChar * aString);
	bool	StartsWithFrom(const OrsChar * aString, long from);





	OrsBool	SaveOn(FILE *aFilePtr);
	long 	SaveOnMem(unsigned char *MemPtr);
	OrsBool	RestoreFrom(FILE *aFilePtr);
	long	RestoreFromMem(unsigned char *MemPtr);

	OrsBool	SaveOn(CFile & aCFileRef);
	OrsBool	RestoreFrom(CFile & aCFileRef);



	long	index(CRegExp& pat, long i=0); // Search for Regular Expression
	long	index(CRegExp& pat, long* ext, long i=0);

	void	RegExpChangeTo(CRegExp& aSearchExp, OrsChar *aReplaceExp, OrsLong StartPos=0, OrsInt FromOccur=-1, OrsInt ToOccur=-1);
			// Will change a regular expression with a new pattern
			// FromOccur=-1 means from the first and
			// ToOccur=-1 means till the end. ToOccurr is inclusive

	void	GetValueFromKey(char *Entry, CString &Dest);

	bool	Split(ATTValVector <CString *> &OutStrings, char aSep);
				// split a string in its substring

	bool	Split(ATTValVector <CString *> &OutStrings, char *aStrSep);
	bool	Split(ATTValVector <CString *> &OutStrings, const char *aStrSep);
	bool 	Split(ATTValVector <CString *> &OutStrings, OrsLong aPos); // 22/07/2015

	bool SplitIgnoreWithin(ATTValVector <CString *> &OutStrings, char *aStrSep, char ignoreStart, char ignoreEnd);// split a string in its substring
	bool 	Split_assign(ATTValVector <CString *> &OutStrings, const char *aStrSep);


	OrsBool isEqual(OrsChar *aString); // 29/03/09
	OrsBool isEqual(const OrsChar *aString);

	void	removeCharacterOccurances(OrsChar chr);

	bool leftPadding (char filler, int maxStringLength);
	bool rightPadding (char filler, int maxStringLength);


	long 	getBufferSize();
	OrsBool ResizeNew(OrsLong NewSize);
	void ResetNoResize();

	long getBufferSubStringLength();
	}; // End CString



// Related global functions:
//istream&  rwexport operator>>(istream&, RWCString&);
//ostream&  rwexport operator<<(ostream& os,  RWCString& s);
//RWCString rwexport toLower( RWCString&); // Return lower-case version of argument.
//RWCString rwexport operator+( char* c,  RWCString& s);
//RWCString rwexport operator+( RWCString& a,  RWCString& s);
//inline    unsigned rwhash( RWCString& s) { return s.hash(); }
//inline    unsigned rwhash( RWCString* s) { return s->hash(); }

void toUpper(CString&); // Return upper-case version of argument.



#endif //CSTRING_H
