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
* Module  : Generic.h                                                       *
* Author  : Argentino Trombin                                               *
* Desc.   : Standardizes what various compilers see of the generic.h        *
*           facility                                                        *
* Date    :                                                                 *
****************************************************************************/

#ifndef GENERIC_H
#define GENERIC_H

#include "LibraryConst.h"

/**
#    ifdef _MSC_VER	// Microsoft C/C++ ?
#      define NO_NATIVE_GENERIC_H 1
#    else
#      include <generic.h>
#    endif
*****/
#      define NO_NATIVE_GENERIC_H 1




#ifdef NO_NATIVE_GENERIC_H

/**
 * If the compiler did not supply a generic.h, then we will have to
 * do so:
 *
 * Here is the functionality we need:
 *
 *  MACROS:
 **   name2(one,Two)  (we use the name2 macro directly)
 *       result: oneTwo
 **   declare(Class,type)
 *       result: Classdeclare(type)
 **   implement(Class,type)
 *       result: Classimplement(type)
 **   callerror(Class,type,intarg,charsplatarg)
 *       result: (*errorhandler(Class,type))(intarg,charsplatarg)
 **   set_handler(Class,type,handlerRetType)
 *       result: set_typeClass_handler(handlerRetType)
 *    errorhandler(Class,type)
 *       result: typeClasshandler
 *  Declarations and typedefs:
 *    extern genericerror(int,char*)
 *    typedef int(*GPT)(int,char)
**/

//extern genericerror(int,char*);

//typedef int (*GPT)(int,char*);

#define name2(a,b) _AlName2(a,b) /* to force the args to be evaluated here */
#define _AlName2(a,b) a##b

#define name3(a,b,c) _AlName3(a,b,c)
#define _AlName3(a,b,c) a##b##c

#define name4(a,b,c,d) _AlName4(a,b,c,d)
#define _AlName4(a,b,c,d) a##b##c##d

#define declare(Class,type)     name2(Class,declare)(type)
#define implement(Class,type)   name2(Class,implement)(type)
/*
#define callerror(Class,type,iarg,csarg) \
	       (*errorhandler(Class,type))(iarg,csarg)
*/

//#define set_handler(Class,type,hrt) name4(set_,type,Class,_handler)(hrt)
//#define errorhandler(Class,type) name3(type,Class,handler)


#define Stringer( aToBeString ) ( #aToBeString )



#undef NO_NATIVE_GENERIC_H

#endif // NO_NATIVE_GENERIC_H

#endif //GENERIC_H

