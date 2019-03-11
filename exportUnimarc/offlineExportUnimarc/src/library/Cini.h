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
* Module  : CINI.H                                                        *
* Author  : Argentino Trombin                                               *
* Desc.   :                       *
* Date    :                                                                 *
****************************************************************************/

#ifndef __CINI_H__
#define __CINI_H__

#include "library/CString.h"
#include "library/tvvector.h"

class  cIni
	{
	void init(void);

	// Public Interface
	// Data members
	public:
//	OrsChar IniField[OrsMAX_INI_PARAM][OrsMAX_EXPRESS_LEN+1];
			// far Array to contain fields of a .INI file command lin
	ATTValVector<CString*> *fieldsVector;

	OrsInt  IniFields;
			// Number of fields found in string from .in ifile



	// Methods
	cIni(void);   // Constructor

	~cIni(void);   		// Destructor


	OrsBool Setup(void);  	// Initializer

	OrsBool SplitIniFields(OrsChar *ptr);	// NMB: Il separatore e' lo spazio
//	OrsBool SplitIniFields(OrsChar *ptr, bool removeLeadingWS);
		// Decompose a string in n tokens


	static OrsBool EmptyLine (const OrsChar *ptr);
		// Test for an empty string

	}; // End

#endif //__CINI_H__

