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
* Program : CINI.CPP                                                        *
* Purpose : Class to interpret the commands in a .ini or equivalent file    *
* Author  : Argentino Trombin                                               *
****************************************************************************/
//#include <stdarg.h>
//#include <stdio.h>
//#include <stdlib.h>
#include <string.h>
//#include <iostream.h>


//#include "ors\types.h"
#include "ors/Const.h"

#include "Cini.h"

extern void SignalAnError(	const OrsChar *Module,
							OrsInt Line,
							const OrsChar * MsgFmt, ...);

// Constructor
cIni::cIni(void)
	{
	init();
    }

void cIni::init(void)
	{
	fieldsVector = new ATTValVector<CString*>();

	for (int i=0; i < OrsMAX_INI_PARAM; i++)
		fieldsVector->Add(new CString());
    }

// Destructor
cIni::~cIni(void)
	{
	fieldsVector->DeleteAndClear();
	delete fieldsVector;
    }

// Initializer
OrsBool cIni::Setup(void)
	{
    return OrsTRUE;
	}

/**
*       Check for an empty line
*       Return OrsTRUE/OrsFALSE
**/
OrsBool cIni::EmptyLine (const OrsChar *ptr)
	{
	while(*ptr)
		{
		/* Note the 0x0D if for DOS files copied in UNIX senza vonversione */
		if 	( (*ptr != 0x20) && (*ptr != 0x09) && (*ptr != 0x0A) && (*ptr != 0x0D))
			{
			if (*ptr == '#') return (OrsTRUE);
				return (OrsFALSE);
			}
		ptr++;
		} /* End while */
	return (OrsTRUE);
	} // End OrsBool cIni::EmptyLine (const OrsChar *ptr)



/**
*	Split the fields of a .ini command line
**/
OrsBool cIni::SplitIniFields(OrsChar *ptr)
//OrsBool cIni::SplitIniFields(OrsChar *ptr, bool removeLeadingWS) // 11/03/2016
	{
	OrsChar Loop, Done, InExpression;
    OrsInt Len;
    CString *destString;

    IniFields = 0;
	for (int i=0; i < OrsMAX_INI_PARAM; i++)
		fieldsVector->Entry(i)->Clear();


	Done = OrsFALSE;
	InExpression = OrsFALSE;
	while (!Done)
    	{
		if	(IniFields == OrsMAX_INI_PARAM) // -1
			{
	        SignalAnError(__FILE__, __LINE__, "Troppi IniFields. Max %d", IniFields);
			return OrsFALSE;
			}


		// Move to the first non white character
		Loop = OrsTRUE;
		while (Loop)
            {
			switch (*ptr)
    	    	{
        	    case ' ':
				case '\t':
					ptr++;
    	        	continue;
				case '\"':
					ptr++;
					Loop = OrsFALSE;
                    InExpression = OrsTRUE;
	   	        	continue;
				case '\n':
//				case '#':
				case 0:
//					*ptr=0;
					Done = OrsTRUE;
                	break;
				} // End switch
			Loop = OrsFALSE;
			} // End While


		if	(Done)
			break;

        // Store current field
		destString = fieldsVector->Entry(IniFields); // IniField[IniFields];
		//*dest=0;

		Loop = OrsTRUE;
		Len=0;
		while (Loop)
			{
			if	(Len == OrsMAX_EXPRESS_LEN)
				{
		        SignalAnError(__FILE__, __LINE__, "Espressione troppo lunga. Max %d", Len);
                return OrsFALSE;
				}
			switch (*ptr)
				{
				case ' ':
				case '\t':
					if	(!InExpression)
						{
						IniFields++;
						Loop = OrsFALSE;
						ptr++;
						continue;
                        }
                	break;
				case '\"':
					IniFields++;
					Loop = OrsFALSE;
					InExpression = OrsFALSE;
                    ptr++;
					continue;
//				case '#':
				case '\n':
				case 0:
					*ptr=0;
					IniFields++;
					Done = OrsTRUE;
					Loop = OrsFALSE;
					continue;
				} // End switch
			//*dest = *ptr;
			destString->AppendChar(*ptr);
			ptr++;
			//dest++;
			Len++;
			} // End while Loop
		//*dest=0;
		} // End while *ptr

//	if (removeLeadingWS)
//		for (int i=0; i < OrsMAX_INI_PARAM; i++)
//			fieldsVector->Entry(i)->Strip(CString::leading, "	 ");

	return OrsTRUE;
	} // End cIni::SplitIniFields


