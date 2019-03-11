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
 * MarcWriterExample.h
 *
 *  Created on: 11-dic-2008
 *      Author: Arge
 */

#ifndef MARCWRITEREXAMPLE_H_
#define MARCWRITEREXAMPLE_H_

#include "library/CFile.h"

class WriteMarcExample {
public:
	WriteMarcExample();
	virtual ~WriteMarcExample();
	void write(char *filenameIn, char *filenameOut);
};

#endif /* MARCWRITEREXAMPLE_H_ */
