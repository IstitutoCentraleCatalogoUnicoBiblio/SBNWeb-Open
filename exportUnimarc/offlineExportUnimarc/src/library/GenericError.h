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
 * Error.h
 *
 *  Created on: 4-mar-2009
 *      Author: Arge
 */

#ifndef GENERICERROR_H_
#define GENERICERROR_H_


#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


#define	IO_GENERICERROR_CODE			1
#define MEMORY_GENERICERROR_CODE		2

#define	EXIT_ON_GENERICERROR_TRUE	1
#define	EXIT_ON_GENERICERROR_FALSE	0

class GenericError {
	  void init (const char* module, int line, int code, const char* msg);
public:
	char*	errorModule;		// Source module name where the error occured
	int  	errorLine;			// Source module line number where the error occured
	int  	errorCode;			// Error code
	bool	exitOnError;		// If OrsTRUE exit from program anyhow
	char	errorMessage[512];		// Message

  GenericError ();
  GenericError (const char* module, int line, int code, bool exitOnError, const char * msgFmt, ...);
  GenericError (const char* msg);
  virtual ~GenericError();

};

#endif /* GENERICERROR_H_ */
