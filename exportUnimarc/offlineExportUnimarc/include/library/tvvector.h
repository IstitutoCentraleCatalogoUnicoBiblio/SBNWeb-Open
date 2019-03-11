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

#ifndef __ATTVVECTOR_H__
#define __ATTVVECTOR_H__

#include <memory.h>
#include <ors/Const.h>
#include <library/CBaseClass.h>

#define MAX_RESIZE_BY  256
#define DEFAULT_RESIZE_BY  5

extern void SignalAFatalError(	OrsChar *Module, OrsInt Line, OrsChar * MsgFmt, ...);



template<class T> class ATTValVector : public CBaseClass
	{

protected:
	OrsInt	ResizeBy;
	OrsInt	_npts;
			// Number of entries in use
	OrsInt	ArraySize;
			// Number of entries allocated

	T*			_array;

	void		boundsCheck(int i) ;


public:
	// Virtual function redefinition from CBaseClass
	//==============================================
	virtual OrsInt ClassId()		{return ATTValVectorID;};
	virtual const OrsChar* ClassName()	{return "ATTValVectorID";};

	ATTValVector(OrsInt n = 0)
		{
		ArraySize = _npts = n;
		if	(n)
			 _array = new T[_npts];
		else
			_array = OrsNULL;

		ResizeBy=DEFAULT_RESIZE_BY;
		}

	ATTValVector(OrsInt n, T ival);
	ATTValVector( ATTValVector&);

	virtual ~ATTValVector()
		{

		if	(_array)
			delete[] _array;

		}

	void ZeroAll() // Found bug in instantiation so need this
		{
		ArraySize = _npts = 0; _array = OrsNULL;
		ResizeBy=DEFAULT_RESIZE_BY;
		}


	void	reshape	(OrsInt N);
	OrsBool resize	(OrsInt N);

	ATTValVector&	operator =	( ATTValVector&);
	ATTValVector&	operator =	(T ival);

	T*		Data()      {return _array;}
	OrsInt	length()    {return _npts;}
	OrsInt	Length()    {return _npts;}
	T&		ref(int i)  {return _array[i];}

	T&	operator()(int i)	{return _array[i];}
	T&	operator[](int i)	{return _array[i];}

	T	operator()(int i)const 	{return _array[i];}
	T	operator[](int i)const 	{return _array[i];}



	OrsBool SetEntry(int i, T ival);
	OrsInt	Add(T ival);
	OrsBool Insert(int anEntry, T ival); // 09/06/2015
	void	Clear();
	void	DeleteAndClear();
	T		Entry(int i)  {return _array[i];}
	void	SetResizeBy(int NewSize);

	OrsBool RemoveByEntry(int anEntry);
	OrsBool DeleteAndRemoveByEntry(int anEntry);
	OrsBool deleteAndRemoveLastEntry();

	OrsBool RemoveLastEntry();
	OrsBool RemoveByValue(T ival);
	OrsInt	FindByValue(T ival);
	T		Last() {if (_npts) return _array[_npts-1]; else return OrsNULL;}
	T		First() {if (_npts) return _array[0]; else return OrsNULL;}

	}; // End ATTValVector

// Implementation
template<class T> ATTValVector<T>::ATTValVector(register OrsInt n, T ival)
	{
	ArraySize = _npts = n;

	if	(n)
		{
		register T* dst = _array = new T[n];
		while (n--)
			*dst++ = ival;
		}
	else
		_array = OrsNULL;

	ResizeBy=DEFAULT_RESIZE_BY;
	}

template<class T> ATTValVector<T>::ATTValVector( ATTValVector<T>& a)
{
  register OrsInt i= _npts =  ArraySize = a._npts;
  register T* dst = _array = new T[i];

  register T* src = a._array;
  while (i--) *dst++ = *src++;
  ResizeBy=DEFAULT_RESIZE_BY;
}

template<class T> ATTValVector<T>& ATTValVector<T>::operator=( ATTValVector<T>& a)
{
  if(_array != a._array){
	delete[] _array;	/* Disconnect from old _array */
	register OrsInt i = _npts = ArraySize = a._npts;
	register T* dst = _array = new T[i];
	register T* src = a._array;
	while (i--) *dst++ = *src++;
  }
  return *this;
}

template<class T> ATTValVector<T>& ATTValVector<T>::operator=(T ival)
{
  for (int i=0; i<_npts; i++) _array[i] = ival;
  return *this;
}



template<class T> void ATTValVector<T>::boundsCheck(int i)
	{
	//  if (i<0 || i>=_npts)
	//    ATThrow(ATErrObject(TOOL_INDEX,ATFATAL,__ATTEMPLATE),i,(int)(_npts-1));

	SignalAFatalError(	__FILE__, __LINE__, "ATTValVector boundsCheck FATAL Error");
	}



template<class T> void ATTValVector<T>::Clear()
	{
	reshape(0);
	}


template<class T> OrsBool ATTValVector<T>::SetEntry(int i, T ival)
		{
		if	(i<0 || i>=_npts)
			{
			// Set an error message
			return OrsFALSE;
			}
		_array[i] = ival;
		return OrsTRUE;
		}












template<class T> void ATTValVector<T>::SetResizeBy(int NewSize)
	{
	if (NewSize >=1 && NewSize <= MAX_RESIZE_BY)
		ResizeBy = NewSize;
	}

//template<class T> OrsBool ATTValVector<T>::Add(T ival)
// return entry index or -1 on error
template<class T> OrsInt ATTValVector<T>::Add(T ival)
		{
		OrsInt Save_npts = _npts;

		if	(_npts >= ArraySize)
			{
	 		OrsInt Size = _npts + ResizeBy;
			reshape(Size);

			if	(Size != ArraySize)
				return -1;

			}

		_npts = Save_npts+1;

		_array[Save_npts] = ival;
		return (Save_npts);
		} // End Add


template<class T> OrsBool ATTValVector<T>::Insert(int anEntry, T ival)
	{
	int i = anEntry, j;

	if	(i > _npts)
		return OrsFALSE;

	OrsInt Save_npts = _npts;
	if	(_npts >= ArraySize)
		{
 		OrsInt Size = _npts + ResizeBy;
		reshape(Size);

		if	(Size != ArraySize)
			return OrsFALSE;
		}
	_npts = Save_npts+1;

	if (_npts == 1)
		_array[0] = ival;
	else
	{
		// Move the rest of the array down
		for	(j=_npts-2; j >= anEntry; j--)
			_array[j+1] = _array[j];
		_array[anEntry] = ival;
	}

	return OrsTRUE;
	} // End Insert


template<class T> OrsBool ATTValVector<T>::RemoveByEntry(int anEntry)
	{
	int i = anEntry, j;

	if	(i>=_npts)
		return OrsFALSE;

	// Move the rest of the array up
	for	(j=i+1; j < _npts; j++, i++)
		{
		_array[i] = _array[j];
		}
	_npts--;
	return OrsTRUE;
	} // End RemoveByEntry





template<class T> void ATTValVector<T>::DeleteAndClear()
	{
#ifndef __BCPLUSPLUS__ // BC5 da errore
	int i;
	for	(i=0; i < _npts; i++)
		delete _array[i]; // (void *)
	reshape(0);
#endif
	}

template<class T> OrsBool ATTValVector<T>::RemoveByValue(T ival)
	{
	int i, j;
#ifndef __BCPLUSPLUS__ // BC5 da errore

	for	(i=0; i < _npts; i++)
		{
		if	(_array[i] == ival)
			break;
		}
	if	(i==_npts)
		return OrsFALSE;

	// Move the rest of the array up
	for	(j=i+1; j < _npts; j++, i++)
		{
		_array[i] = _array[j];
		}
	_npts--;
	return OrsTRUE;
#else
	return OrsFALSE;
   #endif

	} // End RemoveByValue

template<class T> OrsInt ATTValVector<T>::FindByValue(T ival)
	{
#ifndef __BCPLUSPLUS__ // BC5 da errore
	int i;
	for	(i=0; i < _npts; i++)
		{
		if	(_array[i] == ival)
			return i;
		}
#endif
	return -1;
	}



template<class T> OrsBool ATTValVector<T>::resize(OrsInt N)
	{
//	if	(N <= _npts)
	if	(N <= ArraySize)
		return OrsFALSE;

	reshape(N);
//	if	(N == _npts)
	if	(N == ArraySize)
		return OrsTRUE;
	else
		return OrsFALSE;
	} // End ATTValVector<T>::resize



template<class T> void ATTValVector<T>::reshape(OrsInt N)
	{
	if (N==ArraySize)
		return;

	// Zero out the new entries
	T* newArray = new T[N];
	memset(newArray, 0, N*sizeof(T));

	if	(_array)
		{
		register OrsInt i = (N<=_npts) ? N : ArraySize;
		register T* src = _array;
		register T* dst = newArray;
		while (i--)
			*dst++ = *src++;
		delete[] _array;
		}
	_array = newArray;
	_npts = ArraySize = N;
	}

template<class T> OrsBool ATTValVector<T>::DeleteAndRemoveByEntry(int anEntry)
	{
#ifndef __BCPLUSPLUS__ // BC5 da errore
	if	(anEntry < 0 && anEntry >=_npts)
		return OrsFALSE;
	delete (_array[anEntry]); // (void *)
#endif

	return RemoveByEntry(anEntry);
	} // End RemoveByEntry


template<class T> OrsBool ATTValVector<T>::RemoveLastEntry()
	{
	if	(!_npts)
		return OrsFALSE;

	_npts--;
	return OrsTRUE;
	} // End RemoveByEntry

template<class T> OrsBool ATTValVector<T>::deleteAndRemoveLastEntry()
	{
	if	(!_npts)
		return OrsFALSE;

	delete (_array[_npts-1]);

	_npts--;
	return OrsTRUE;
	} // End RemoveByEntry

   #endif  //__ATTVVECTOR_H__

