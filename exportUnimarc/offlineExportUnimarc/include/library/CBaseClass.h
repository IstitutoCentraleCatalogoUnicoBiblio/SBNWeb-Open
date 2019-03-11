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
* Module  : CBaseClass.h                                       *
* Author  : Argentino Trombin                                               *
* Desc.   :                                                                 *
* Date    : Abstract Base Class                                                                *
****************************************************************************/

#ifndef __CBASECLASS_H
#define __CBASECLASS_H

#include "ors/types.h"
#include "LibraryClassId.h"


class CBaseClass
	{
	public:
	CBaseClass(){};
	virtual ~CBaseClass(){};

	// This class virtual functions
	//=============================
	virtual OrsInt ClassId(){return CBaseClassID;};
			// Return the Class ID number
	virtual const OrsChar* ClassName(){return "CBaseClassID";};
			// Return the Class Name
	};


#define	ClassIDSize sizeof(OrsInt)


#endif // __CBASECLASS_H
