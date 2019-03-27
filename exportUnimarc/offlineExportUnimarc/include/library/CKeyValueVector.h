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

#ifndef __KEYVALUEVECTOR__
#define __KEYVALUEVECTOR__

#include "ors/Const.h"
#include "library/CString.h"

#define tKVSTRING 'S'
#define tKVINT	'I'
#define tKVLONG	'L'
#define tKVFLOAT 'F'
#define tKVVOID 'V'
#define DEFAULT_VECTOR_SIZE	20

class CKeyValue
	{
	public:

	union {
		char *	p;
		int		i;
		long	l;
		float	f;
		void *	v;
		}Key;
	union {
		char *	p;
		int		i;
		long	l;
		float	f;
		void *	v;
		}Val;
	};

class CKeyValueVector
	{
	CKeyValue	**Vector;

	int VectorSize;
	int	VectorEntries;
	int ResizeVectorBy;

	OrsBool GrowVector();
			// Make the vector grow by ResizeVectorBy
	OrsBool _MakeVector(int aSize);

	void init(int aSize, unsigned char aKeyType, unsigned char aValType);

	public:
	unsigned char KeyType;
	unsigned char ValType;

	CKeyValueVector(unsigned char aKeyType, unsigned char aValType);
	CKeyValueVector(int aSize, unsigned char aKeyType, unsigned char aValType);
	~CKeyValueVector();

	void Clear();
	OrsBool Setup();
	OrsBool Add(const char* aKey, const char* aValue);
	OrsBool Add(const char* aKey, const char* aValue, unsigned char aKeyType, unsigned char aValType);
	OrsBool Add(int aKey, int aValue);
	OrsBool Add(long aKey, long aValue);
	OrsBool Add(int aKey, char* aValue);
//	OrsBool Add(const char* aKey, float aValue);
	OrsBool Add(const char* aKey, int aValue);
	OrsBool Add(const char* aKey, void* aValue);
	OrsBool Add(int aKey, void* aValue);

	void	setKeyValtype(unsigned char aKeyType, unsigned char aValType);

	OrsBool Get(int Entry, char** aName, char** aValue);
	CKeyValue *Get(int Entry);

	int Length();
	void *GetKey(int Entry);

	const char*	GetKeyFromValue(char* aValue);
	int		GetKeyFromValue(int aValue);
	long	GetKeyFromValue(long aValue);


	// Dicotomic search on vector sorted by value
	char*	GetKeyFromSortedValues(char* aValue);
	int		GetKeyFromSortedValues(int aValue);
	long	GetKeyFromSortedValues(long aValue);

	// Dicotomic search on vector sorted by key
	char*	GetValueFromSortedKey(char* aKey);
//	char*	findValueFromSortedKeys(char* aKeyPtr);
	int		GetFirstIndexFromSortedKey(char* aKey);
	void*	GetValuePtrFromSortedKey(char* aKey);


	void*	GetValue(int Entry);
	char*	GetValueFromKey(const char* aKey);
	int		GetValueFromKey(int);
	float	GetFloatValueFromKey(char* aKey);

	bool	SetValueFromKey(char* aKey, float aValue);
	bool	SetValueFromKey(int aKey, int aValue);
	bool	SetValueFromKey(int aKey, char* aValue);

	bool	SetValueFromValue(char* oldValue, char* newValue);
	bool	SetValueFromEntry(int entry, char* newValue);


	int CompareKey(int entry, char* aKey);
	int CompareKey(int entry, int aKey);
	int CompareKey(int entry, long aKey);

	bool SortAscendingByKey();
	bool SortAscendingByValue();
	bool SortDescendingByKey();
	bool SortDescendingByValue();
	bool existsKey(const char* aKey);

	void dump();


	};




#endif  //__KEYVALUEVECT__
