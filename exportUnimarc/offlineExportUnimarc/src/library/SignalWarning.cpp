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
/***************************************************************************
* Program : SignalError.CPP                                                       *
* Purpose : Eror handling                                                  *
* Author  : Argentino Trombin                                              *
****************************************************************************/
#include <stdio.h>
#include <stdarg.h>
#include <ors/Const.h>
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


/**
*	Temporary error handler used for debugging purposes.
*	To be used only temporarily in place of CErr::SetErr
*	Used to place a breakpoint while developing.
**/
void SignalAWarning(	const OrsChar *Module,
					OrsInt Line,
					const OrsChar * MsgFmt, ...)
	{
	va_list argptr;
	OrsChar loc_buf[2048+1];

	// Expand mesasage with supplied arguments (if there are any)
	va_start(argptr, MsgFmt);
	vsprintf(loc_buf, MsgFmt, argptr);
	va_end(argptr);

	printf("\nSignalAWarning: Module=%s Line=%d Message:%s", Module, Line, loc_buf);

	} // End SignalAWarning
