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
* Program : SignalFatalError.CPP                                                       *
* Purpose : Eror handling                                                  *
* Author  : Argentino Trombin                                              *
****************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
//#include <conio.h>
#include <termios.h> // x linux
#include <unistd.h> // x linux
#include <ors/Const.h>
#include <errno.h> // x linux
extern int errno;  // x linux
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


//OrsInt	SignalledErrors = 0;

void ferr( char *fmt, ... );
void SignalAFatalError(	OrsChar *Module, OrsInt Line, OrsChar * MsgFmt, ...);


int mygetch( ) {
struct termios oldt,
newt;
int ch;
tcgetattr( STDIN_FILENO, &oldt );
newt = oldt;
newt.c_lflag &= ~( ICANON | ECHO );
tcsetattr( STDIN_FILENO, TCSANOW, &newt );
ch = getchar();
tcsetattr( STDIN_FILENO, TCSANOW, &oldt );
return ch;
}


/**
*	Temporary error handler used for debugging purposes.
*	To be used only temporarily in place of CErr::SetErr
*	Used to place a breakpoint while developing.
**/
void SignalAFatalError(	OrsChar *Module,
						OrsInt Line,
						OrsChar * MsgFmt, ...)
	{
	va_list argptr;
	OrsChar loc_buf[512];

	// Expand message with supplied arguments (if there are any)
	va_start(argptr, MsgFmt);
	vsprintf(loc_buf, MsgFmt, argptr);
	va_end(argptr);

	printf("SignalAFatalError: Module=%s Line=%d Msg:%s\n", Module, Line, loc_buf);
	printf ("Press a key to terminate ....");

//	SignalledErrors++;
	mygetch(); //getch()
	exit(errno);
	} // End SignalAFatalError



