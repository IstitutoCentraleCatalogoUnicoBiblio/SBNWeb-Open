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
* Module  : macros.h                                                       *
* Author  : Argentino Trombin                                               *
* Desc.   :                                                                 *
*                                                                           *
* Date    : 25/03/2015                                                                *
****************************************************************************/

#ifndef MACROS_H
#define MACROS_H

#define MACRO_COPY_FAST(count) {									\
switch (count)												\
	{														\
	case 1:													\
		*(BufTailPtr) = *(aString);						\
		break; 												\
	case 2:													\
		*((short*)BufTailPtr) = *((short*)aString); 		\
		break; 												\
	case 3:													\
		*((short*)BufTailPtr) = *((short*)aString);			\
		*(BufTailPtr+2) = *(aString+2);						\
		break; 												\
	case 4:													\
		*((int*)BufTailPtr) = *((int*)aString); 			\
		break; 												\
	case 5:													\
		*((int*)BufTailPtr) = *((int*)aString); 			\
		*(BufTailPtr+4) = *(aString+4);						\
		break; 												\
	case 6:													\
		*((int*)BufTailPtr) = *((int*)aString); 			\
		*((short*)BufTailPtr+2) = *((short*)aString+2);		\
		break; 												\
	case 7:													\
		*((int*)BufTailPtr) = *((int*)aString); 			\
		*((short*)BufTailPtr+2) = *((short*)aString+2);		\
		*(BufTailPtr+6) = *(aString+6);						\
		break; 												\
	case 8:													\
		*((long long*)BufTailPtr) = *((long long*)aString); \
		break; 												\
	case 10:												\
		*((long long*)BufTailPtr) = *((long long*)aString); \
		*((short*)BufTailPtr+4) = *((short*)aString+4);		\
		break; 												\
	case 12:												\
		*((long long*)BufTailPtr) = *((long long*)aString); \
		*((int*)BufTailPtr+2) = *((int*)aString+2);			\
		break; 												\
	default:												\
		memcpy(BufTailPtr, aString, count);					\
		break;												\
	}														\
}

/*
	if	(BufferSubString)\
	{\
		free (BufferSubString);\
		BufferSubString = 0;\
		BufferSubStringSize=0;\
	}\
 */
#define MACRO_RESET_NO_RESIZE	\
	if (Buffer)\
		*Buffer=0;\
	BufTailPtr = Buffer;\
	UsedBytes = 1;\
	UnusedRightBytes = BufferSize-1;\

// 31/03/2015
#define MACRO_ASSIGN_CHAR	\
	*BufTailPtr = aChar;\
	BufTailPtr++;\
	*BufTailPtr = 0;\
	UsedBytes=2;\
	UnusedRightBytes -=2;\

// 31/03/2015
#define MACRO_ASSIGN_ASTRING_CHAR	\
	*BufTailPtr = *aString;\
	BufTailPtr++;\
	*BufTailPtr = 0;\
	UsedBytes=2;\
	UnusedRightBytes -=2;\


#endif // MACROS_H
