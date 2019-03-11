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
 * ReadMarcExample.cpp
 *
 *  Created on: 10-dic-2008
 *      Author: Arge
 */

#include "ReadMarcExample.h"
#include "MarcStreamReader.h"


ReadMarcExample::ReadMarcExample() {

}

ReadMarcExample::~ReadMarcExample() {
}

void ReadMarcExample::read(char *filename)
{
CFile *in = new CFile(filename, AL_READ_FILE);

MarcStreamReader *marcStreamReader = new MarcStreamReader(in);
MarcRecord *marcRecord;
int ctr = 1;

while (marcStreamReader->hasNext()) {
	marcRecord = marcStreamReader->next();
	if (marcRecord)
		{
		//printf("%s", marcRecord->toString());
		printf ("\nRecord %d, bid=%s", ctr, marcRecord->getControlFieldsVector()->Entry(0)->getData()->data());
		}
	else
	{
		printf ("\nRecord ERRATO %d", ctr);
		break;
	}
	ctr++;

//	if (ctr == 10)
//		break;
}

delete in;
}

