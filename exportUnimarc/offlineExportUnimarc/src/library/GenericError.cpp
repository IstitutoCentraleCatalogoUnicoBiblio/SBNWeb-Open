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
 * Error.cpp
 *
 *  Created on: 4-mar-2009
 *      Author: Arge
 */
//#include <stdlib.h>
//#include <errno.h>
#include <stdio.h>

#include "GenericError.h"
#include <stdarg.h>
#include <string.h>


GenericError::GenericError () {
}

GenericError::GenericError (const char* msg) {
	strcpy (errorMessage, msg);
}


GenericError::GenericError (const char* module, int line, int code, bool exitOnError, const char * msgFmt, ...) {
	va_list argptr;
	char loc_buf[256];

	this->exitOnError = exitOnError;

	// Expand message with supplied arguments (if there are any)
	va_start(argptr, msgFmt);
	vsprintf(loc_buf, msgFmt, argptr);
	va_end(argptr);
	init (module, line, code, loc_buf);
}

void GenericError::init (const char* module, int line, int code, const char* msg) {

	sprintf(errorMessage, "\nError: Module=%s Line=%d Code=%d Msg: %s", module, line, code, msg);

}



GenericError::~GenericError() {
}
