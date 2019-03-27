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
#include "library/CKeyValueVector.h"
#include <stdlib.h>
#include <malloc.h>
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


extern void SignalAnError(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(	const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

// Constructor
CKeyValueVector::CKeyValueVector(unsigned char aKeyType, unsigned char aValType)
//CKeyValueVector::CKeyValueVector()
	{

//	CKeyValueVector(DEFAULT_VECTOR_SIZE, aKeyType, aValType);
	init(DEFAULT_VECTOR_SIZE, aKeyType, aValType);
	};

CKeyValueVector::CKeyValueVector(int aSize, unsigned char aKeyType, unsigned char aValType)
{
	init(aSize, aKeyType, aValType);
}

void CKeyValueVector::init(int aSize, unsigned char aKeyType, unsigned char aValType)
{
	Vector = (CKeyValue **) 0;
	VectorSize = 0;
	VectorEntries = 0;
	ResizeVectorBy = DEFAULT_VECTOR_SIZE;
	KeyType = aKeyType;
	ValType = aValType;
	_MakeVector(VectorEntries + ResizeVectorBy);
}

void CKeyValueVector::setKeyValtype(unsigned char aKeyType, unsigned char aValType)
{
	KeyType = aKeyType;
	ValType = aValType;
}


CKeyValueVector::~CKeyValueVector()
	{
	Clear();
	}
void CKeyValueVector::Clear()
	{
	int i;

	// Remove vector contents
	for	(i=0; i<VectorEntries; i++)
		{
		if (KeyType == tKVSTRING)
			free (Vector[i]->Key.p);
		if (ValType == tKVSTRING)
			free (Vector[i]->Val.p);
		delete Vector[i];
		//Vect or[i] = (CKeyValue *) 0;
		}

	// Remove vector itself
	free(Vector);
	Vector = 0;
	VectorSize = 0;
	VectorEntries = 0;
	}
OrsBool CKeyValueVector::Setup()
	{

	return OrsTRUE;
	}


OrsBool CKeyValueVector::Get(int Entry, char** aName, char** aValue)
	{
	return OrsFALSE;
	}


OrsBool CKeyValueVector::GrowVector()
	{
	return _MakeVector(VectorEntries + ResizeVectorBy);
	} // End CKeyValueVector::GrowVector



OrsBool CKeyValueVector::_MakeVector(int aSize)
{
	int NewSize = aSize * sizeof (CKeyValue*);
	int i;

	CKeyValue** NewArray, **OldArray = Vector;
	NewArray = (CKeyValue**)malloc(NewSize);

	//   Copia il contenuto del vecchio array nel nuovo
	for	(i=0; i<VectorEntries; i++)
		{
		NewArray[i] = OldArray[i];
		}
	if	(OldArray)
		free(OldArray);

	Vector = NewArray;

	VectorSize += ResizeVectorBy;
	return OrsTRUE;
} // End _MakeVector





int CKeyValueVector::Length()
	{
	return VectorEntries;
	} // End CKeyValueVector::Length


void *CKeyValueVector::GetKey(int Entry)
	{
	if	(Entry <0 || Entry >= VectorEntries)
		return 0;

	if (KeyType == tKVSTRING)
		return Vector[Entry]->Key.p;
	if (KeyType == tKVINT)
		return &Vector[Entry]->Key.i;
	if (KeyType == tKVLONG)
		return &Vector[Entry]->Key.l;
	if (KeyType == tKVVOID)
		return &Vector[Entry]->Key.v;

	return 0;
	} // End CKeyValueVector::GetName

void *CKeyValueVector::GetValue(int Entry)
	{
	if	(Entry <0 || Entry >= VectorEntries)
		return 0;

	if (ValType == tKVSTRING)
		return Vector[Entry]->Val.p;
	if (ValType == tKVINT)
		return &Vector[Entry]->Val.i;
	if (ValType == tKVLONG)
		return &Vector[Entry]->Val.l;
	if (ValType == tKVVOID)
		return Vector[Entry]->Val.v;

	return 0; // for now
	} // End CKeyValueVector::GetValue



//========================================================================

OrsBool CKeyValueVector::Add(const char* aKey, const char* aValue)
	{
	if (KeyType != tKVSTRING ||
		ValType != tKVSTRING)
		return false;


	if	(VectorSize == VectorEntries)
		{
		// Time to resize vector
		if	(!GrowVector())
			return OrsFALSE;
		}
	CKeyValue *NV = new CKeyValue();
	if	(!NV)
		{
	    SignalAnError(__FILE__, __LINE__, "CKeyValueVector::Add non posso creare CKeyValue()");
		return OrsFALSE;
		}
	NV->Key.p = strdup(aKey);
	NV->Val.p = strdup(aValue);

	Vector[VectorEntries] = NV;
	VectorEntries++;

	return OrsTRUE;
	} // End CKeyValueVector::Add








OrsBool CKeyValueVector::Add(const char* aKey, const char* aValue, unsigned char aKeyType, unsigned char aValType)
{
	if (KeyType != aKeyType ||	ValType != aValType)
		return false;
	// TODO CKeyValueVector::Add
	return false;
}

OrsBool CKeyValueVector::Add(int aKey, int aValue)
{
	if (KeyType != tKVINT ||
		ValType != tKVINT)
		return false;
	if	(VectorSize == VectorEntries)
		{
		// Time to resize vector
		if	(!GrowVector())
			return OrsFALSE;
		}
	CKeyValue *NV = new CKeyValue();
	if	(!NV)
		{
	    SignalAnError(__FILE__, __LINE__, "CKeyValueVector::Add non posso creare CKeyValue()");
		return OrsFALSE;
		}
	NV->Key.i = aKey;
	NV->Val.i = aValue;

	Vector[VectorEntries] = NV;
	VectorEntries++;

	return OrsTRUE;
} // End add (int, int)
OrsBool CKeyValueVector::Add(long aKey, long aValue)
{
	if (KeyType != tKVLONG ||
		ValType != tKVLONG)
		return false;

	if	(VectorSize == VectorEntries)
		{
		// Time to resize vector
		if	(!GrowVector())
			return OrsFALSE;
		}
	CKeyValue *NV = new CKeyValue();
	if	(!NV)
		{
	    SignalAnError(__FILE__, __LINE__, "CKeyValueVector::Add non posso creare CKeyValue()");
		return OrsFALSE;
		}
	NV->Key.l = aKey;
	NV->Val.l = aValue;

	Vector[VectorEntries] = NV;
	VectorEntries++;

	return OrsTRUE;
} // End add (long, long)


int CKeyValueVector::CompareKey(int entry, char* aKey)
{
	if (KeyType != tKVSTRING || entry <0 || entry >VectorEntries)
		return -9;
	return stricmp(aKey, Vector[entry]->Key.p );

}

int CKeyValueVector::CompareKey(int entry, int aKey)
{
	int ret;

	if (KeyType != tKVINT || entry < 0 || entry >VectorEntries)
		return -9;
	ret = aKey - Vector[entry]->Key.i;

	if (!ret)
		return 0;
	else if (ret < 0)
		return -1;
	else //if (ret > 0)
		return 1;
}
int CKeyValueVector::CompareKey(int entry, long aKey)
{
	int ret;


	if (KeyType != tKVLONG || entry < 0 || entry >VectorEntries)
		return -9;
	ret = aKey - Vector[entry]->Key.l;

	if (!ret)
		return 0;
	else if (ret < 0)
		return -1;
	else //if (ret > 0)
		return 1;
}

const char*	CKeyValueVector::GetKeyFromValue(char* aValue)
	{
	if (KeyType != tKVSTRING)
		return "";
	return "";
	} // End GetKeyFromValue
int		CKeyValueVector::GetKeyFromValue(int aValue)
	{
	return -1;
	} // End GetKeyFromValue
long	CKeyValueVector::GetKeyFromValue(long aValue)
	{
	return -1L;
	} // End GetKeyFromValue


char*	CKeyValueVector::GetKeyFromSortedValues(char* aValue)
	{
	if (KeyType != tKVSTRING)
		return 0;
	return 0;
	} // End GetKeyFromValue
int		CKeyValueVector::GetKeyFromSortedValues(int aValue)
	{
	return -1;
	} // End GetKeyFromValue






long	CKeyValueVector::GetKeyFromSortedValues(long aValue)
	{
	OrsInt Left = 0, Right = VectorEntries-1, x;

	while(Right >= Left)
		{
		x = (Left+Right) >> 1;
		// Value equal?
		if	(aValue == Vector[x]->Val.l)
			return Vector[x]->Key.l; // Found
		// Value < ?
		if	(  aValue < Vector[x]->Val.l)
			Right = x-1;
		else
			Left = x+1;
		}
	return -1; // Nothing Found
	} // End GetKeyFromValue



bool CKeyValueVector::SortAscendingByValue()
{
	bool sorted;
	CKeyValue *saevPtr;
//printf("\nSORT ASCENDING BY VALUE");
	if (VectorEntries < 2)
		return true;

	if (this->ValType == tKVSTRING)
		{
ResortAscValByString:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((stricmp(Vector[i]->Val.p, Vector[i+1]->Val.p)) > 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;

				sorted = false;
				}
			}
		if (!sorted)
			goto ResortAscValByString;
		}
	else if (this->ValType == tKVINT)
		{
ResortAscValByInt:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((Vector[i]->Val.i - Vector[i+1]->Val.i) > 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;
				sorted = false;
				}
			}
		if (!sorted)
			goto ResortAscValByInt;
		}


	else if (this->ValType == tKVLONG)
		{
ResortAscValByLong:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((Vector[i]->Val.l - Vector[i+1]->Val.l) > 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;
				sorted = false;
				}
			}
		if (!sorted)
			goto ResortAscValByLong;
		}
	return true;
} // End SortAscendingByValue

bool CKeyValueVector::SortDescendingByValue()
{
//printf("\nSORT DESCENDING BY VALUE");

	bool sorted;
	CKeyValue *saevPtr;

	if (VectorEntries < 2)
		return true;

	if (this->ValType == tKVSTRING)
		{
ResortDescValByString:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((stricmp(Vector[i]->Val.p, Vector[i+1]->Val.p)) < 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;

				sorted = false;
				}
			}
		if (!sorted)
			goto ResortDescValByString;
		}
	else if (this->ValType == tKVINT)
		{
ResortDescValByInt:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((Vector[i]->Val.i < Vector[i+1]->Val.i) )
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;
				sorted = false;
				}
			}
		if (!sorted)
			goto ResortDescValByInt;
		}


	else if (this->ValType == tKVLONG)
		{
ResortDescValByLong:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((Vector[i]->Val.l < Vector[i+1]->Val.l) )
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;
				sorted = false;
				}
			}
		if (!sorted)
			goto ResortDescValByLong;
		}
	return true;
} // End SortDescendingByValue



bool CKeyValueVector::SortAscendingByKey()
{
	bool sorted;
	CKeyValue *saevPtr;

	if (VectorEntries < 2)
		return true;

	if (this->KeyType == tKVSTRING)
		{
ResortAscKeyByString:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((stricmp(Vector[i]->Key.p, Vector[i+1]->Key.p)) > 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;

				sorted = false;
				}
			}
		if (!sorted)
			goto ResortAscKeyByString;
		}
	else if (this->KeyType == tKVINT)
		{
ResortAscKeyByInt:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((Vector[i]->Key.i - Vector[i+1]->Key.i) > 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;
				sorted = false;
				}
			}
		if (!sorted)
			goto ResortAscKeyByInt;
		}


	else if (this->KeyType == tKVLONG)
		{
ResortAscKeyByLong:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((Vector[i]->Key.l - Vector[i+1]->Key.l) > 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;
				sorted = false;
				}
			}
		if (!sorted)
			goto ResortAscKeyByLong;
		}
	return true;
} // End SortAscendingByKey





bool CKeyValueVector::SortDescendingByKey()
{
	bool sorted;
	CKeyValue *saevPtr;

	if (VectorEntries < 2)
		return true;

	if (this->KeyType == tKVSTRING)
		{
ResortDescKeyByString:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((stricmp(Vector[i]->Key.p, Vector[i+1]->Key.p)) < 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;

				sorted = false;
				}
			}
		if (!sorted)
			goto ResortDescKeyByString;
		}
	else if (this->KeyType == tKVINT)
		{
ResortDescKeyByInt:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((Vector[i]->Key.i - Vector[i+1]->Key.i) < 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;
				sorted = false;
				}
			}
		if (!sorted)
			goto ResortDescKeyByInt;
		}


	else if (this->KeyType == tKVLONG)
		{
ResortDescKeyByLong:
		sorted = true;
		// Bubble sort
		for (int i=0; i < (VectorEntries-1); i++)
			{
			if ((Vector[i]->Key.l - Vector[i+1]->Key.l) < 0)
				{
				saevPtr = Vector[i+1];
				Vector[i+1] = Vector[i];
				Vector[i] = saevPtr;
				sorted = false;
				}
			}
		if (!sorted)
			goto ResortDescKeyByLong;
		}
	return true;
} // End SortDescendingByKey

char*	CKeyValueVector::GetValueFromKey(const char* aKey)
	{
	if (KeyType != tKVSTRING)
		return NULL;
	for (int i=0; i < (VectorEntries); i++)
		{
		//vKey = Vector[i]->Key.p;

//printf ("\nKey = '%s'", Vector[i]->Key.p);

		if (!stricmp(aKey, Vector[i]->Key.p))
			return (Vector[i]->Val.p);
		}
	return NULL;
	} // End GetValueFromKey


CKeyValue *CKeyValueVector::Get(int Entry)
{
	if (Entry >=0 && Entry < VectorEntries)
		return (Vector[Entry]);
	else
		return 0; // NULL
}

char*	CKeyValueVector::GetValueFromSortedKey(char* aKey)
{
	if (KeyType != tKVSTRING)
		return 0;
	int ret;

	OrsInt Left = 0, Right = VectorEntries-1, x;

	while(Right >= Left)
		{
		x = (Left+Right) >> 1;
		// Value equal?
		ret = strcmp(aKey, Vector[x]->Key.p);
//char *v_key = Vector[x]->Key.p;
//ret = strcmp(aKey, v_key);
		if	(!ret )
			return Vector[x]->Val.p; // Found
		// Value < ?
		if	(  ret < 0)
			Right = x-1;
		else
			Left = x+1;
		}
	return 0; // Nothing Found
}

void*	CKeyValueVector::GetValuePtrFromSortedKey(char* aKey)
{
	if (KeyType != tKVSTRING)
		return 0;
	int ret;

	OrsInt Left = 0, Right = VectorEntries-1, x;

	while(Right >= Left)
		{
		x = (Left+Right) >> 1;
		// Value equal?
		ret = strcmp(aKey, Vector[x]->Key.p);
//char *v_key = Vector[x]->Key.p;
//ret = strcmp(aKey, v_key);
		if	(!ret )
		{
			if (ValType == tKVSTRING)
				return Vector[x]->Val.p; // Found
			else if (ValType == tKVINT)
				return &Vector[x]->Val.i;
		}
		// Value < ?
		if	(  ret < 0)
			Right = x-1;
		else
			Left = x+1;
		}
	return 0; // Nothing Found
}




//char*	CKeyValueVector::findValueFromSortedKeys(char* aKeyPtr)
//	{
//	if (KeyType != tKVSTRING)
//		return 0;
//
//	OrsInt Left = 0, Right = VectorEntries-1, x;
//	int ret;
//
//	while(Right >= Left)
//		{
//		x = (Left+Right) >> 1;
//		// Value equal?
//		ret = strcmp(aKeyPtr, Vector[x]->Key.p);
////printf ("\nsearh key=%s, vector key=%s, ret = %d", aKeyPtr, Vector[x]->Key.p, ret);
//		if	(!ret)
//			return Vector[x]->Val.p; // Found
//		if	(  ret < 0)
//			Right = x-1;
//		else
//			Left = x+1;
//		}
//	return 0; // Nothing Found
//	}




OrsBool CKeyValueVector::Add(const char* aKey, void* aValue)
{
	if (KeyType != tKVSTRING ||
		ValType != tKVVOID)
		return false;


	if	(VectorSize == VectorEntries)
		{
		// Time to resize vector
		if	(!GrowVector())
			return OrsFALSE;
		}
	CKeyValue *NV = new CKeyValue();
	if	(!NV)
		{
	    SignalAnError(__FILE__, __LINE__, "CKeyValueVector::Add non posso creare CKeyValue()");
		return OrsFALSE;
		}
	NV->Key.p = strdup(aKey);
	NV->Val.v = aValue;

	Vector[VectorEntries] = NV;
	VectorEntries++;

	return OrsTRUE;

}


OrsBool CKeyValueVector::Add(const char* aKey, int aValue)
{
	if (KeyType != tKVSTRING ||
		ValType != tKVINT)
		return false;


	if	(VectorSize == VectorEntries)
		{
		// Time to resize vector
		if	(!GrowVector())
			return OrsFALSE;
		}
	CKeyValue *NV = new CKeyValue();
	if	(!NV)
		{
	    SignalAnError(__FILE__, __LINE__, "CKeyValueVector::Add non posso creare CKeyValue()");
		return OrsFALSE;
		}
	NV->Key.p = strdup(aKey);
	NV->Val.i = aValue;

	Vector[VectorEntries] = NV;
	VectorEntries++;

	return OrsTRUE;

}

bool CKeyValueVector::existsKey(const char* aKey)
	{
	if (KeyType != tKVSTRING)
		return NULL;
	for (int i=0; i < (VectorEntries); i++)
		{
		//vKey = Vector[i]->Key.p;

//printf ("\nKey = '%s'", Vector[i]->Key.p);

		if (!stricmp(aKey, Vector[i]->Key.p))
			return (true);
		}
	return false;
	} // End GetValueFromKey


int	CKeyValueVector::GetFirstIndexFromSortedKey(char* aKey)
{
	if (KeyType != tKVSTRING)
		return 0;
	int ret;

	OrsInt Left = 0, Right = VectorEntries-1, x;

	while(Right >= Left)
		{
		x = (Left+Right) >> 1;
		// Value equal?
		ret = strcmp(aKey, Vector[x]->Key.p);
		if	(!ret )
		{
			while ((x-1) > -1)
			{
			ret = strcmp(aKey, Vector[x-1]->Key.p);
			if (ret)
				return x; // Found
			x--;
			}
//			return x-1; // Found
			return x; // Found 30/01/2018
		}
		// Value < ?
		if	(  ret < 0)
			Right = x-1;
		else
			Left = x+1;
		}
	return -1; // Nothing Found
}

void CKeyValueVector::dump()
{
	if (KeyType == tKVSTRING && ValType == tKVSTRING)
	{
		for (int i=0; i < (VectorEntries); i++)
			printf ("\nKey = '%s' : value - '%s'", Vector[i]->Key.p, Vector[i]->Val.p);
	}
	else if (KeyType == tKVSTRING && ValType == tKVINT)
	{
		for (int i=0; i < (VectorEntries); i++)
			printf ("\nKey = '%s' : value - '%d'", Vector[i]->Key.p, Vector[i]->Val.i);
	}

}

