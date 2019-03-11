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
 * CMisc.h
 *
 *  Created on: 9-lug-2009
 *      Author: Arge
 */


#ifndef CMISC_H_
#define CMISC_H_

//#include <string.h>
#include <time.h>
#include "library/CString.h"

class CMisc {
public:
	CMisc();
	virtual ~CMisc();

	static void formatDate (char *inputFormat, char *outputFormat, char *sourceDate, char *destDate);
	static void formatDate1 (const char *sourceDate, char *destDate);
	static void formatDate2 (const char *sourceDate, char *destDate);
	static void formatDate3(struct tm * timeinfo, char *destDate);
	static void formatDate4(struct tm * timeinfo, char *destDate);
	static bool IsEmpty(char *ptr);
	static bool isDate(char *stringPtr);
	static bool isDate(char *stringPtr, int len);

	static void gestisciTrattiniNumStandardIsbn(CString *numeroStdIn, CString *numeroStdOut);

};

#endif /* CMISC_H_ */
